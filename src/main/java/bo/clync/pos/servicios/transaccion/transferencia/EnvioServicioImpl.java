package bo.clync.pos.servicios.transaccion.transferencia;

import bo.clync.pos.arquetipo.dto.DatosUsuario;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.*;
import bo.clync.pos.arquetipo.tablas.PosTransaccion;
import bo.clync.pos.repository.common.AdmCredencialRepository;
import bo.clync.pos.repository.transaccion.pedido.TransaccionRepository;
import bo.clync.pos.servicios.discos.DiscoServicio;
import bo.clync.pos.servicios.transaccion.generic.TransaccionServicio;
import bo.clync.pos.utilitarios.UtilsDisco;
import bo.clync.pos.utilitarios.UtilsDominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
public class EnvioServicioImpl implements EnvioServicio {

    @Autowired
    private TransaccionServicio transaccionServicio;
    @Autowired
    private AdmCredencialRepository credencialRepository;
    @Autowired
    private TransaccionRepository transaccionRepository;
    @Autowired
    private DiscoServicio discoServicio;
    @Override
    public TransaccionResponseInit init(String token) {
        return transaccionServicio.init(token, UtilsDominio.TRANSFERENCIA);
    }

    @Override
    public TransaccionResponse adicionar(String token, TransaccionRequest request, HttpServletRequest http) throws Exception {

        TransaccionResponse response = transaccionServicio.nuevo(token, request, UtilsDominio.TRANSFERENCIA, UtilsDominio.TRANSFERENCIA_ENVIO, UtilsDominio.TIPO_PAGO_NO_REQUERIDO);

        if(response.isRespuesta())
            this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, request, token));

        return response;
    }

    @Override
    public TransaccionResponse actualizar(String token, TransaccionRequest request, HttpServletRequest http) throws Exception {
        TransaccionResponse response = transaccionServicio.actualizar(token, request, UtilsDominio.TRANSFERENCIA);

        if(response.isRespuesta())
            this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, request, token));

        return response;
    }

    @Override
    public ServResponse eliminar(String token, String idTransaccion, HttpServletRequest http) throws Exception {
        ServResponse response = transaccionServicio.eliminar(token, idTransaccion, UtilsDominio.TRANSFERENCIA_ENVIO);

        if(response.isRespuesta())
            this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, null, token));

        return response;
    }

    @Override
    public TransaccionResponseList lista(String token) {
        return transaccionServicio.lista(token, UtilsDominio.TRANSFERENCIA, UtilsDominio.TRANSFERENCIA_ENVIO, null);
    }

    @Override
    public TransaccionResponseList listaConfirmados(String token) {
        return transaccionServicio.lista(token, UtilsDominio.TRANSFERENCIA, UtilsDominio.TRANSFERENCIA_RECIBIR_ORIGEN_AUX, null);
    }

    @Override
    public ServResponse reconfirmar(String token, String id, HttpServletRequest http) throws Exception{
        ServResponse response = new ServResponse();

        DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

        PosTransaccion posTransaccion = transaccionRepository.getTransaccion(id);
        if (posTransaccion != null) {
            if (posTransaccion.getCodigoValor().equals(UtilsDominio.TRANSFERENCIA_RECIBIR_EDIT)
                    || posTransaccion.getCodigoValor().equals(UtilsDominio.TRANSFERENCIA_RECIBIR)) {

                posTransaccion.setCodigoValor(UtilsDominio.TRANSFERENCIA_RECIBIR_CONF);
                posTransaccion.setFechaActualizacion(new Date());
                posTransaccion.setOperadorActualizacion(datosUsuario.getIdUsuario().toString());
                this.transaccionRepository.save(posTransaccion);
                this.discoServicio.guardarOperaciones(UtilsDisco.getOperaciones(http, null, token));
                response.setRespuesta(true);

            } else {
                response.setMensaje("La posTransaccion se encuentra en estado de " + posTransaccion.getCodigoValor());
            }
        } else {
            response.setMensaje("No se encontro la posTransaccion");
        }
        return response;
    }

    @Override
    public TransaccionResponseList listaReConfirmados(String token) {
        return transaccionServicio.lista(token, UtilsDominio.TRANSFERENCIA, UtilsDominio.TRANSFERENCIA_RECIBIR_CONF, null);
    }


    @Override
    public TransaccionResponse obtener(String token, String id) {
        return transaccionServicio.obtener(token, id, UtilsDominio.TRANSFERENCIA, UtilsDominio.TRANSFERENCIA_ENVIO);
    }

    @Override
    public String getIdTransaccion( String nroMovimiento, String token ) {
        return transaccionServicio.getIdTransaccion(UtilsDominio.TRANSFERENCIA, nroMovimiento, token);
    }

    @Override
    public TransaccionResponse obtenerDiferencia(String token, String id) {
        return transaccionServicio.obtener(token, id, UtilsDominio.TRANSFERENCIA_NLL, UtilsDominio.TRANSFERENCIA_NLL_RECIBIR_NO_LLEGO);
    }
}
