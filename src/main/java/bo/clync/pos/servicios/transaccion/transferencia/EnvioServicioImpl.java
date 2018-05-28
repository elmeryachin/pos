package bo.clync.pos.servicios.transaccion.transferencia;

import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.inventario.SucursalesResponseList;
import bo.clync.pos.dao.transaccion.generic.TransaccionRequest;
import bo.clync.pos.dao.transaccion.generic.TransaccionResponse;
import bo.clync.pos.dao.transaccion.generic.TransaccionResponseInit;
import bo.clync.pos.dao.articulo.ArticuloResponseList;
import bo.clync.pos.dao.articulo.ArticuloResponseMin;
import bo.clync.pos.dao.transaccion.generic.TransaccionResponseList;
import bo.clync.pos.dao.transaccion.transferencia.*;
import bo.clync.pos.repository.acceso.ConectadoRepository;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.common.CicloRepository;
import bo.clync.pos.repository.transaccion.pedido.TransaccionRepository;
import bo.clync.pos.servicios.transaccion.generic.TransaccionServicio;
import bo.clync.pos.utilitarios.UtilsDominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EnvioServicioImpl implements EnvioServicio {

    @Autowired
    private UsuarioAmbienteCredencialRepository credencialRepository;
    @Autowired
    private ConectadoRepository conectadoRepositor;
    @Autowired
    private TransaccionRepository transaccionRepository;
    @Autowired
    private CicloRepository cicloRepository;
    @Autowired
    private TransaccionServicio transaccionServicio;

    @Override
    public TransaccionResponseInit init(String token) {
        return transaccionServicio.init(token, UtilsDominio.TRANSFERENCIA);
    }

    @Override
    public TransaccionResponse nuevo(String token, TransaccionRequest request) throws Exception {
        //validarLISTA DETALLE DE ARTICULOS POR SUCURSAL

        return transaccionServicio.nuevo(token, request, UtilsDominio.TRANSFERENCIA, UtilsDominio.TRANSFERENCIA_ENVIO, UtilsDominio.TIPO_PAGO_NO_REQUERIDO);
    }

    @Override
    public TransaccionResponse actualizar(String token, TransaccionRequest request) throws Exception {
        return transaccionServicio.actualizar(token, request, UtilsDominio.TRANSFERENCIA);
    }

    @Override
    public ServResponse eliminar(String token, String idTransaccion) throws Exception {
        return transaccionServicio.eliminar(token, idTransaccion);
    }

    @Override
    public TransaccionResponseList lista(String token) {
        return transaccionServicio.lista(token, UtilsDominio.TRANSFERENCIA, UtilsDominio.TRANSFERENCIA_ENVIO, null);
    }

}