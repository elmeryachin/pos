package bo.clync.pos.servicios.reporte;

/**
 * Created by eyave on 12-11-17.
 */
public interface ReporteServicio {

    byte[] reporteArticulos(String token, String nombre, String format);

}
