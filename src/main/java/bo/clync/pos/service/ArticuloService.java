package bo.clync.pos.service;

import bo.clync.pos.model.Articulo;

import java.util.List;

/**
 * Created by eyave on 03-10-17.
 */
public interface ArticuloService {
    List<Articulo> findAll();
    Articulo findOne(long id);
    Articulo findByCodigo(String codigo);
    void save(Articulo articulo);
    void update(Long id, Articulo articulo);
    boolean delete(long id);
    void deleteAll();
}
