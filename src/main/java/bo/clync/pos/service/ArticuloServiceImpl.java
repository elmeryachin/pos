package bo.clync.pos.service;

import bo.clync.pos.dao.ArticuloRepository;
import bo.clync.pos.model.Articulo;
import bo.clync.pos.util.ArticuloUtil;
import bo.clync.pos.util.Conversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by eyave on 05-10-17.
 */
@Service
@Transactional
public class ArticuloServiceImpl implements ArticuloService {

    @Autowired
    private ArticuloRepository repository;

    @Override
    public List<Articulo> findAll() {
        return repository.findAll();
    }

    @Override
    public Articulo findOne(long id) {
        return repository.findOne(id);
    }

    @Override
    public Articulo findByCodigo(String codigo) {
        return repository.findByCodigo(codigo);
    }


    @Override
    public void save(Articulo articulo) {
        articulo.setFechaRegistro(new Timestamp(new java.util.Date().getTime()));
        ArticuloUtil.calculoPrecio(articulo);
        repository.save(articulo);
    }

    @Override
    public void update(Long id, Articulo a) {
        Articulo articulo = findOne(id);
        if(articulo!=null) {
            articulo.setNombre(a.getNombre());
            articulo.setDescripcion(a.getDescripcion());
            articulo.setPrecioKilo(a.getPrecioKilo());
            articulo.setPeso(a.getPeso());
            articulo.setGasto(a.getGasto());
            articulo.setPrecioZonaLibre(a.getPrecioZonaLibre());
            articulo.setPorcentajeGasto(a.getPorcentajeGasto());
            articulo.setPrecioCompra(a.getPrecioCompra());
            articulo.setPrecioVenta(a.getPrecioVenta());
            articulo.setPrecioMercado(a.getPrecioMercado());
            articulo.setFechaActualizacion(new Timestamp(new java.util.Date().getTime()));
            ArticuloUtil.calculoPrecio(articulo);
            repository.save(articulo);
        }
    }

    @Override
    public boolean delete(long id) {
        if(!repository.exists(id)) return false;
        //falta Control de articulo en movimiento.
        repository.delete(id);
        return true;
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
