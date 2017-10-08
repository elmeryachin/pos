package bo.clync.pos.service;

import bo.clync.pos.entity.Entrada;
import bo.clync.pos.model.Articulo;

import java.util.List;

/**
 * Created by eyave on 03-10-17.
 */
public interface ArticuloService {
    List<Articulo> findAll();
    Articulo findOne(String codigo);
    String save(Entrada entrada);
    void update(String codigo, Articulo articulo);
    boolean delete(String codigo);
    void deleteAll();
}
