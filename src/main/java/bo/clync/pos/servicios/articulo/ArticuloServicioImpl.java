package bo.clync.pos.servicios.articulo;


import bo.clync.pos.dao.articulo.lista.ResumenArticulo;
import bo.clync.pos.entity.Articulo;
import bo.clync.pos.dao.articulo.ArticuloRequest;
import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.articulo.lista.ServListaResponse;
import bo.clync.pos.dao.articulo.obtener.ServObtenerResponse;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.articulo.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by eyave on 05-10-17.
 */
@Service
@Transactional
public class ArticuloServicioImpl implements ArticuloServicio {

    @Autowired
    private ArticuloRepository repository;
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;

    @Override
    public ServListaResponse lista(String token) {
        ServListaResponse response = new ServListaResponse();
        try {
            long ini = System.currentTimeMillis();
            response.setLista(repository.getListResumenArticulos());
            long res = ini - System.currentTimeMillis();
            System.out.println("TIEMPO TRANSCURRIDO : " + res);
            response.setRespuesta(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al recuperar los registros");
        }
        return response;
    }

    @Override
    public ServListaResponse listaNativa(String token) {
        ServListaResponse response = new ServListaResponse();
        Connection connection = null;
        try {

            long ini = System.currentTimeMillis();
            List<ResumenArticulo> lista = new ArrayList<>();

            connection = getConnection();
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM ARTICULO");
            ResumenArticulo ra = null;
            while (rs.next()) {
                ra = new ResumenArticulo(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );
                lista.add(ra);
            }
            rs.close();
            connection.close();
            response.setLista(lista);

            long res = ini - System.currentTimeMillis();
            System.out.println("TIEMPO TRANSCURRIDO nativo : " + res);
            response.setRespuesta(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al recuperar los registros");
        }
        return response;
    }

    private Connection getConnection() throws Exception {

        Class.forName("org.postgresql.Driver");
        InputStream inputStream = getClass().getResourceAsStream("/application.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        String url      = properties.getProperty("test.source.url");
        String user     = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");
        return DriverManager.getConnection(url,user, password);
    }

    @Override
    public ServListaResponse listaPorCodigo(String token, String patron) {
        ServListaResponse response = new ServListaResponse();
        try {
            response.setLista(repository.getListResumenArticulosPorPatron(patron));
            response.setRespuesta(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al recuperar los registros");
        }
        return response;
    }

    @Override
    public ServObtenerResponse obtener(String codigo, String token){
        ServObtenerResponse response = new ServObtenerResponse();
        try {
            response.setArticulo(repository.getObtenerArticuloPorCodigo(codigo));
            if(response.getArticulo()==null)
                response.setMensaje("No se encontro un articulo con codigo " + codigo);
            else response.setRespuesta(true);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al recuperar el articulo");
        }
        return response;
    }

    @Override
    public ServResponse nuevo(ArticuloRequest request, String token) {
        String operador         = null;
        Articulo articulo       = null;
        ServResponse response   = new ServResponse();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            operador = String.valueOf(arrayId[0]);
            if(repository.getObtenerArticuloPorCodigo(request.getObjetoArticulo().getCodigo()) == null) {
                articulo = new Articulo();
                articulo.setCodigo(request.getObjetoArticulo().getCodigo());
                articulo.setNombre(request.getObjetoArticulo().getNombre());
                articulo.setDescripcion(request.getObjetoArticulo().getDescripcion());
                articulo.setPrecioKilo(request.getObjetoArticulo().getPrecioKilo());
                articulo.setPeso(request.getObjetoArticulo().getPeso());
                articulo.setPrecioZonaLibre(request.getObjetoArticulo().getPrecioZonaLibre());
                articulo.setPorcentajeGasto(request.getObjetoArticulo().getPorcentajeGasto());
                articulo.setPrecioCompra(request.getObjetoArticulo().getPrecioCompra());
                articulo.setPrecioVenta(request.getObjetoArticulo().getPrecioVenta());
                articulo.setPrecioMercado(request.getObjetoArticulo().getPrecioMercado());
                articulo.setFechaAlta(new Date());
                articulo.setOperadorAlta(operador);
                repository.save(articulo);
                response.setRespuesta(true);
            } else {
                response.setMensaje("El codigo de articulo ya existe ingreso otro.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al guardar el registro");
        }
        return response;
    }

    @Override
    public ServResponse actualizar(String codigo, ArticuloRequest request, String token) {
        Articulo articulo               = null;
        String operador                 = null;
        ServResponse response = new ServResponse();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            operador = String.valueOf(arrayId[0]);
            articulo = repository.findByCodigoAndFechaBajaIsNull(codigo);
            if(articulo != null) {
                articulo.setNombre(request.getObjetoArticulo().getNombre());
                articulo.setDescripcion(request.getObjetoArticulo().getDescripcion());
                articulo.setPrecioKilo(request.getObjetoArticulo().getPrecioKilo());
                articulo.setPeso(request.getObjetoArticulo().getPeso());
                articulo.setPrecioZonaLibre(request.getObjetoArticulo().getPrecioZonaLibre());
                articulo.setPorcentajeGasto(request.getObjetoArticulo().getPorcentajeGasto());
                articulo.setPrecioCompra(request.getObjetoArticulo().getPrecioCompra());
                articulo.setPrecioVenta(request.getObjetoArticulo().getPrecioVenta());
                articulo.setPrecioMercado(request.getObjetoArticulo().getPrecioMercado());
                articulo.setFechaActualizacion(new Date());
                articulo.setOperadorActualizacion(operador);
                repository.save(articulo);
                response.setRespuesta(true);
            } else {
                response.setMensaje("No se encontro ningun articulo con el codigo enviado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al actualizar el articulo");
        }
        return response;
    }

    @Override
    public ServResponse eliminar(String codigo, String token) {
        Articulo articulo       = null;
        String operador         = null;
        Object[] arrayId        = null;
        ServResponse response   = new ServResponse();
        try {
            arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if(arrayId != null) {
                operador = String.valueOf(arrayId[0]);
                articulo = repository.findByCodigoAndFechaBajaIsNull(codigo);
                if (articulo != null) {
                    articulo.setFechaBaja(new Date());
                    articulo.setOperadorBaja(operador);
                    repository.save(articulo);
                    response.setRespuesta(true);
                } else {
                    response.setMensaje("No existe el articulo que quiere eliminar");
                }
            } else {
                response.setMensaje("Su session expiro");
            }
        } catch (Exception e ){
            e.printStackTrace();
            response.setMensaje("Error en el servicio de eliminacion");
        }
        return response;
    }

    @Override
    public byte[] reporteListaArticulos(String token, String tipo) {

        return new byte[0];
    }

}
