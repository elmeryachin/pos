package bo.clync.pos.servicios.articulo;


import bo.clync.pos.arquetipo.dto.DatosUsuario;
import bo.clync.pos.arquetipo.objetos.articulo.obtener.ObjetoArticulo;
import bo.clync.pos.arquetipo.objetos.articulo.ArticuloResponseMin;
import bo.clync.pos.arquetipo.tablas.PosArticulo;
import bo.clync.pos.arquetipo.objetos.articulo.ArticuloRequest;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.articulo.lista.ServListaResponse;
import bo.clync.pos.arquetipo.objetos.articulo.obtener.ServObtenerResponse;
import bo.clync.pos.repository.common.AdmCredencialRepository;
import bo.clync.pos.repository.common.PosArticuloRepository;
import bo.clync.pos.repository.common.PosAmbienteRepository;
import bo.clync.pos.servicios.discos.DiscoServicio;
import bo.clync.pos.utilitarios.UtilsArticulo;
import bo.clync.pos.utilitarios.UtilsDisco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by eyave on 05-10-17.
 */
@Service
@Transactional
public class ArticuloServicioImpl implements ArticuloServicio {

    @Autowired
    private PosArticuloRepository repository;
    @Autowired
    private AdmCredencialRepository credencialRepository;
    @Autowired
    private PosArticuloRepository posArticuloRepository;
    @Autowired
    private PosAmbienteRepository posAmbienteRepository;
    @Autowired
    private DiscoServicio discoServicio;


    @Override
    public ServListaResponse listaPorCodigo(String token, String patron) {
        ServListaResponse response = new ServListaResponse();
        response.setLista(repository.getListResumenArticulosPorPatron(patron));
        if (response.getLista().size() == 0) {
            response.setRespuesta(false);
            response.setMensaje("true");
        } else {
            response.setRespuesta(true);
        }
        return response;
    }

    @Override
    public ServObtenerResponse obtener(String codigo, String token) {
        ServObtenerResponse response = new ServObtenerResponse();
        response.setArticulo(repository.getObtenerArticuloPorCodigo(codigo));
        if (response.getArticulo() == null)
            response.setMensaje("No se encontro un articulo con codigo " + codigo);
        else response.setRespuesta(true);
        return response;
    }

    @Override
    public ServResponse nuevo(ArticuloRequest request, String token, HttpServletRequest http) {
        ServResponse response = new ServResponse();
        try {

            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            if (repository.getObtenerArticuloPorCodigo(request.getObjetoArticulo().getCodigo()) == null) {

                PosArticulo posArticulo = new PosArticulo();
                posArticulo.setCodigo(request.getObjetoArticulo().getCodigo());

                UtilsArticulo.getArticulo(posArticulo, request);

                posArticulo.setFechaAlta(new Date());
                posArticulo.setOperadorAlta(datosUsuario.getIdUsuario().toString());

                repository.save(posArticulo);

                this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, request, token));

                response.setRespuesta(true);
                response.setMensaje("Se guardo el nuevo posArticulo: " + posArticulo.getCodigo());
            } else {
                response.setMensaje("El codigo de posArticulo ya existe ingreso otro.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al guardar el registro");
        }
        return response;
    }

    @Override
    public ServResponse actualizar(String codigo, ArticuloRequest request, String token, HttpServletRequest http) {

        ServResponse response = new ServResponse();
        try {
            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            PosArticulo posArticulo = repository.findByCodigoAndFechaBajaIsNull(codigo);
            if (posArticulo != null) {

                UtilsArticulo.getArticulo(posArticulo, request);

                posArticulo.setFechaActualizacion(new Date());
                posArticulo.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                repository.save(posArticulo);

                this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, request, token));

                response.setRespuesta(true);
                response.setMensaje("Se actualizo el posArticulo");
            } else {
                response.setMensaje("No se encontro ningun posArticulo con el codigo enviado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error al actualizar el registro ");
        }
        return response;
    }

    @Override
    public ServResponse eliminar(String codigo, String token, HttpServletRequest http) {

        ServResponse response = new ServResponse();
        try {
            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            if (repository.getUsoArticulo(codigo) == 0) {

                PosArticulo posArticulo = repository.findByCodigoAndFechaBajaIsNull(codigo);
                if (posArticulo != null) {
                    posArticulo.setFechaBaja(new Date());
                    posArticulo.setOperadorBaja(datosUsuario.getIdUsuario().toString());
                    repository.save(posArticulo);

                    this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, null, token));

                    response.setRespuesta(true);
                    response.setMensaje("Se elimino el posArticulo: " + posArticulo.getCodigo());
                } else {
                    response.setMensaje("No existe el posArticulo que quiere eliminar");
                }
            } else {
                    response.setMensaje("El posArticulo se encuentra en uso");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMensaje("Error en el servicio de eliminacion");
        }
        return response;
    }

    @Override
    public ArticuloResponseMin obtenerArticulo(String token, String codigo) {
        ArticuloResponseMin response = new ArticuloResponseMin();
        try {
            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            Integer count = posAmbienteRepository.verificarSucursal(datosUsuario.getCodigoAmbiente());

            ObjetoArticulo o = posArticuloRepository.getObtenerArticuloPorCodigo(codigo);
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
}
