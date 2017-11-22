package bo.clync.pos.utilitarios;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by eyave on 09-11-17.
 */
public class UtilsGeneral {

    private static final String FORMATO_FECHA = "dd/MM/yyyy";
    private static final SimpleDateFormat format = new SimpleDateFormat(FORMATO_FECHA);

    public static String fechaActual(){
        return format.format(new Date());
    }

    public static Date convertirFecha(String sFecha) throws Exception{
        return format.parse(sFecha);
    }

    public static String fecha(Date fecha){
        return format.format(fecha);
    }

    public static void mainFecha(String[] args) throws Exception{
        System.out.println(fecha(convertirFecha("20/30/2017")));
        System.out.println(fechaActual());
        System.out.println(fecha(convertirFecha(fechaActual())));

    }
}
