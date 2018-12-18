package bo.clync.pos.utilitarios;

/**
 * Created by eyave on 29-10-17.
 */
public class UtilsDominio {

    //public static final String TIPO_USUARIO = "TIPO_USUARIO";
    public static final String TIPO_USUARIO_ADMIN = "ADMIN";
    public static final String TIPO_USUARIO_EMPLEADO = "EMPLEADO";
    public static final String TIPO_USUARIO_PROVEEDOR = "PROVEEDOR";
    public static final String TIPO_USUARIO_CLIENTE = "CLIENTE";

    //public static final String TIPO_AMBIENTE = "TIPO_AMBIENTE";
    public static final String TIPO_AMBIENTE_ALMACEN = "ALMACEN";
    public static final String TIPO_AMBIENTE_SUCURSAL = "SUCURSAL";

    public static final String PEDIDO = "PEDIDO";
    public static final String PEDIDO_SOLICITUD = "SOLICITUD";
    public static final String PEDIDO_LLEGADA = "LLEGADA";

    public static final String TRANSFERENCIA = "TRANSFERENCIA";
    public static final String TRANSFERENCIA_ENVIO = "ENVIADO";
    // Auxiliar para obtener lista enviados destinos por origien/destino
    public static final String TRANSFERENCIA_ENVIO_DESTINO_AUX = "ENVIADO_DESTINO";
    public static final String TRANSFERENCIA_RECIBIR = "RECIBIDO";
    //Referencia que se recibio los articulos pero se tuvo que editar.
    public static final String TRANSFERENCIA_RECIBIR_EDIT = "RECIBIDO_EDIT";
    public static final String TRANSFERENCIA_RECIBIR_CONF = "RECIBIDO_CONF";
    // Auxiliar para obtener lista recibidos confirmados destinos por origien/destino 
    public static final String TRANSFERENCIA_RECIBIR_ORIGEN_AUX = "RECIBIDO_POR_ORIGEN";
    
    public static final String TRANSFERENCIA_NLL = "TRANSFERENCIA_NLL";
    // Solo se genera tras editar lo recibido por almacen.
    public static final String TRANSFERENCIA_NLL_RECIBIR_NO_LLEGO = "RECIBIDO_NO_LLE";
    
    // Lo generan las tiendas cuando solicitan a otra tienda u almacen articulos.
    // No descarta ni adiciona a inventarios.
    public static final String SOLICITUD_INTERNA = "SOLICITUD_INTERNA";
    public static final String SOLICITUD_INTERNA_AUX = "SOLICITUD_INTERNA_AUX";
    public static final String SOLICITUD_INTERNA_PEDIDO = "SOLIC_SUCURSAL";
    //public static final String SOLICITUD_INTERNA_ATENDIDO = "SOLIC_SUCURSAL";
    public static final String SOLICITUD_INTERNA_CONFIRMADO = "SOLIC_CONFIRM";
    
    public static final String VENTA = "VENTA";
    public static final String VENTA_REALIZADA  = "REALIZADA";
    public static final String VENTA_CONFIRMADA = "CONSOLIDA";

    //public static final String TIPO_PAGO = "TIPO_PAGO";
    public static final String TIPO_PAGO_POR_PAGAR = "POR_PAGAR";
    public static final String TIPO_PAGO_PARCIAL = "PARCIAL";
    public static final String TIPO_PAGO_PAGADO = "PAGADO";
    public static final String TIPO_PAGO_NO_REQUERIDO = "NO_REQUERIDO";


}
