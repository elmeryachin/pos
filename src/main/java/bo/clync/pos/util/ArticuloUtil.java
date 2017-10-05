package bo.clync.pos.util;

import bo.clync.pos.model.Articulo;

import java.math.BigDecimal;

/**
 * Created by eyave on 05-10-17.
 */
public class ArticuloUtil {

    public static void calculoPrecio(Articulo articulo) {
        java.math.BigDecimal subTotalPeso = new BigDecimal(0);
        java.math.BigDecimal subTotalPorGasto = new BigDecimal(0);

        if(articulo.getPeso()!=null && articulo.getPrecioKilo()!=null)
            subTotalPeso = articulo.getPeso().multiply(articulo.getPrecioKilo());
        if(articulo.getPorcentajeGasto()!=null && articulo.getPrecioZonaLibre()!=null)
            subTotalPorGasto = articulo.getPorcentajeGasto()
                                .divide(new BigDecimal(100));
        articulo.setGasto(subTotalPorGasto);

        articulo.setPrecioCompra(articulo.getGasto()
                .multiply(articulo.getPrecioZonaLibre())
                .add(articulo.getPrecioZonaLibre())
                .add(subTotalPeso).add(subTotalPeso));
    }
}
