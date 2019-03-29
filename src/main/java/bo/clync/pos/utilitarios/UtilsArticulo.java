package bo.clync.pos.utilitarios;

import bo.clync.pos.arquetipo.objetos.articulo.ArticuloRequest;
import bo.clync.pos.arquetipo.tablas.Articulo;

public class UtilsArticulo {

    public static void getArticulo(Articulo articulo, ArticuloRequest request) {
        articulo.setNombre(request.getObjetoArticulo().getNombre());
        articulo.setDescripcion(request.getObjetoArticulo().getDescripcion());
        articulo.setPrecioKilo(request.getObjetoArticulo().getPrecioKilo());
        articulo.setPeso(request.getObjetoArticulo().getPeso());
        articulo.setPrecioZonaLibre(request.getObjetoArticulo().getPrecioZonaLibre());
        articulo.setPorcentajeGasto(request.getObjetoArticulo().getPorcentajeGasto());
        articulo.setPrecioCompra(request.getObjetoArticulo().getPrecioCompra());
        articulo.setPrecioVenta(request.getObjetoArticulo().getPrecioVenta());
        articulo.setPrecioMercado(request.getObjetoArticulo().getPrecioMercado());
    }
}
