package bo.clync.pos.servicios.transaccion.transferencia;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.*;
import bo.clync.pos.arquetipo.tablas.Transaccion;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.transaccion.pedido.TransaccionRepository;
import bo.clync.pos.servicios.transaccion.generic.TransaccionServicio;
import bo.clync.pos.utilitarios.UtilsDominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class EnvioServicioImpl implements EnvioServicio {

    @Autowired
    private TransaccionServicio transaccionServicio;
    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;
    @Autowired
    private TransaccionRepository transaccionRepository;

    @Override
    public TransaccionResponseInit init(String token) {
        return transaccionServicio.init(token, UtilsDominio.TRANSFERENCIA);
    }

    @Override
    public TransaccionResponse adicionar(String token, TransaccionRequest request) throws Exception {
        //validarLISTA DETALLE DE ARTICULOS POR SUCURSAL
        String[] codigoArticulo = arrayArticulos(request);

        return transaccionServicio.nuevo(token, request, UtilsDominio.TRANSFERENCIA, UtilsDominio.TRANSFERENCIA_ENVIO, UtilsDominio.TIPO_PAGO_NO_REQUERIDO);
    }

    public String[] arrayArticulos(TransaccionRequest request) {
        List<TransaccionDetalle> list = request.getTransaccionObjeto().getLista();
        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i).getCodigoArticulo();
        }
        return array;
    }

    @Override
    public TransaccionResponse actualizar(String token, TransaccionRequest request) throws Exception {
        return transaccionServicio.actualizar(token, request, UtilsDominio.TRANSFERENCIA);
    }

    @Override
    public ServResponse eliminar(String token, String idTransaccion) throws Exception {
        return transaccionServicio.eliminar(token, idTransaccion, UtilsDominio.TRANSFERENCIA_ENVIO);
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
    public ServResponse reconfirmar(String token, String id){
        ServResponse response = new ServResponse();
        Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
        if(arrayId!=null) {
            Integer idUsuario = (Integer) arrayId[0];
            Transaccion transaccion = transaccionRepository.getTransaccion(id);
            if (transaccion != null) {
                if (transaccion.getCodigoValor().equals(UtilsDominio.TRANSFERENCIA_RECIBIR_EDIT)
                        || transaccion.getCodigoValor().equals(UtilsDominio.TRANSFERENCIA_RECIBIR)) {
                    transaccion.setCodigoValor(UtilsDominio.TRANSFERENCIA_RECIBIR_CONF);
                    transaccion.setFechaActualizacion(new Date());
                    transaccion.setOperadorActualizacion(String.valueOf(idUsuario));
                    this.transaccionRepository.save(transaccion);
                    response.setRespuesta(true);
                } else {
                    response.setMensaje("La transaccion se encuentra en estado de " + transaccion.getCodigoValor());
                }
            } else {
                response.setMensaje("No se encontro la transaccion");
            }
        } else {
            response.setMensaje("Las credenciales vencieron, inicie session nuevamente.");
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
    public TransaccionResponse obtenerDiferencia(String token, String id) {
        return transaccionServicio.obtener(token, id, UtilsDominio.TRANSFERENCIA_NLL, UtilsDominio.TRANSFERENCIA_NLL_RECIBIR_NO_LLEGO);
    }
}
