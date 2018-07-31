package bo.clync.pos.servicios.articulo;


import bo.clync.pos.arquetipo.objetos.articulo.lista.ResumenArticulo;
import bo.clync.pos.arquetipo.objetos.articulo.obtener.ObjetoArticulo;
import bo.clync.pos.arquetipo.objetos.articulo.ArticuloResponseList;
import bo.clync.pos.arquetipo.objetos.articulo.ArticuloResponseMin;
import bo.clync.pos.arquetipo.tablas.Articulo;
import bo.clync.pos.arquetipo.objetos.articulo.ArticuloRequest;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.articulo.lista.ServListaResponse;
import bo.clync.pos.arquetipo.objetos.articulo.obtener.ServObtenerResponse;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.articulo.ArticuloRepository;
import bo.clync.pos.repository.common.AmbienteRepository;
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
    @Autowired
    private ArticuloRepository articuloRepository;
    @Autowired
    private AmbienteRepository ambienteRepository;

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
    public ServListaResponse listaPorCodigo(String token, String patron) {
        ServListaResponse response = new ServListaResponse();
        try {
            response.setLista(repository.getListResumenArticulosPorPatron(patron));
            if (response.getLista().size() == 0) {
                response.setRespuesta(false);
                response.setMensaje("true");
            } else {
                response.setRespuesta(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al recuperar los registros");
        }
        return response;
    }

    @Override
    public ServObtenerResponse obtener(String codigo, String token) {
        ServObtenerResponse response = new ServObtenerResponse();
        try {
            response.setArticulo(repository.getObtenerArticuloPorCodigo(codigo));
            if (response.getArticulo() == null)
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
        String operador = null;
        Articulo articulo = null;
        ServResponse response = new ServResponse();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            operador = String.valueOf(arrayId[0]);
            if (repository.getObtenerArticuloPorCodigo(request.getObjetoArticulo().getCodigo()) == null) {
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
        Articulo articulo = null;
        String operador = null;
        ServResponse response = new ServResponse();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            operador = String.valueOf(arrayId[0]);
            articulo = repository.findByCodigoAndFechaBajaIsNull(codigo);
            if (articulo != null) {
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
        Articulo articulo = null;
        String operador = null;
        Object[] arrayId = null;
        ServResponse response = new ServResponse();
        try {
            arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if (arrayId != null) {
                Integer usoArticulo = repository.getUsoArticulo(codigo);
                if (usoArticulo == 0) {
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
                    response.setMensaje("El articulo se encuentra en uso");
                }
            } else {
                response.setMensaje("Su session expiro");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error en el servicio de eliminacion");
        }
        return response;
    }

    @Override
    public byte[] reporteListaArticulos(String token, String tipo) {

        return new byte[0];
    }

    @Override
    public ArticuloResponseMin obtenerArticulo(String token, String codigo) {
        ArticuloResponseMin response = new ArticuloResponseMin();
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);

            Integer count = ambienteRepository.verificarSucursal((String) arrayId[1]);

            ObjetoArticulo o = articuloRepository.getObtenerArticuloPorCodigo(codigo);
            if (o == null) {
                response.setMensaje("No se encontro el articulo con el codigo");
            } else {
                response.setCodigo(o.getCodigo());
                response.setNombre(o.getNombre());
                if (count == 1) {
                    response.setPrecio(o.getPrecioZonaLibre());
                } else {
                    response.setPrecio(o.getPrecioVenta());
                }
                response.setRespuesta(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error Al recuperar el articulo por codigo");
        }
        return response;
    }

    @Override
    public ArticuloResponseList listaArticulo(String token, String patron) {
        ArticuloResponseList response = new ArticuloResponseList();
        try {
            if (patron == null) response.setList(articuloRepository.getListaArticuloResumen());
            else response.setList(articuloRepository.getListaArticuloResumenPorCodigo(patron));
            response.setRespuesta(true);
        } catch (Exception e) {
            response.setMensaje("Error al listar los Articulos");
            e.printStackTrace();
        }
        return response;
    }

}
