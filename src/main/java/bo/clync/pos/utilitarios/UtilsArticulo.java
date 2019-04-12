package bo.clync.pos.utilitarios;

import bo.clync.pos.arquetipo.objetos.articulo.ArticuloRequest;
import bo.clync.pos.arquetipo.tablas.PosArticulo;

public class UtilsArticulo {

    public static void getArticulo(PosArticulo posArticulo, ArticuloRequest request) {
        posArticulo.setNombre(request.getObjetoArticulo().getNombre());
        posArticulo.setDescripcion(request.getObjetoArticulo().getDescripcion());
        posArticulo.setPrecioKilo(request.getObjetoArticulo().getPrecioKilo());
        posArticulo.setPeso(request.getObjetoArticulo().getPeso());
        posArticulo.setPrecioZonaLibre(request.getObjetoArticulo().getPrecioZonaLibre());
        posArticulo.setPorcentajeGasto(request.getObjetoArticulo().getPorcentajeGasto());
        posArticulo.setPrecioCompra(request.getObjetoArticulo().getPrecioCompra());
        posArticulo.setPrecioVenta(request.getObjetoArticulo().getPrecioVenta());
        posArticulo.setPrecioMercado(request.getObjetoArticulo().getPrecioMercado());
    }
}
