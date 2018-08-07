package bo.clync.pos.servicios.transaccion.venta;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.*;
import bo.clync.pos.servicios.transaccion.generic.TransaccionServicio;
import bo.clync.pos.utilitarios.UtilsDominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class VentaServicioImpl implements VentaServicio {

    @Autowired
    private TransaccionServicio transaccionServicio;

    @Override
    public TransaccionResponseInit init(String token) {
        return transaccionServicio.init(token, UtilsDominio.VENTA);
    }

    @Override
    public TransaccionResponse adicionar(String token, TransaccionRequest request) throws Exception {
        //validarLISTA DETALLE DE ARTICULOS POR SUCURSAL
        //String[] codigoArticulo = arrayArticulos(request);

        return transaccionServicio.nuevo(token, request, UtilsDominio.VENTA, UtilsDominio.VENTA_REALIZADA, UtilsDominio.TIPO_PAGO_NO_REQUERIDO);
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
        return transaccionServicio.actualizar(token, request, UtilsDominio.VENTA);
    }

    @Override
    public ServResponse eliminar(String token, String idTransaccion) throws Exception {
        return transaccionServicio.eliminar(token, idTransaccion, UtilsDominio.VENTA_REALIZADA);
    }

    @Override
    public TransaccionResponseList lista(String token) {
        return transaccionServicio.lista(token, UtilsDominio.VENTA, UtilsDominio.VENTA_REALIZADA, null);
    }

    @Override
    public TransaccionResponse obtener(String token, String id) {
        return transaccionServicio.obtener(token, id, UtilsDominio.VENTA, UtilsDominio.VENTA_REALIZADA);
    }

}
