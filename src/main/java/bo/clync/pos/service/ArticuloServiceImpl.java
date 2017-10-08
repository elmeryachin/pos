package bo.clync.pos.service;

import bo.clync.pos.dao.ArticuloRepository;
import bo.clync.pos.dao.DetalleArticuloPrecioRepository;
import bo.clync.pos.entity.Entrada;
import bo.clync.pos.model.Articulo;
import bo.clync.pos.model.DetalleArticuloPrecio;
import bo.clync.pos.util.ArticuloUtil;
import bo.clync.pos.util.Conversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @Autowired
    private DetalleArticuloPrecioRepository detalleRepository;
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Articulo> findAll() {
        //String query = "select a from Articulo a,   where a.customer";
        //List<Articulo> list = em.createQuery(query).getResultList();

        return repository.findAll();
    }

    @Override
    public Articulo findOne(String codigo) {
        Articulo articulo = repository.findOne(codigo);
        if(articulo!=null)
            articulo.setDetalleArticuloPrecio(detalleRepository.findByCodigoArticulo(codigo));
        return articulo;
    }

    @Override
    public String save(Entrada entrada) {
        //articulo.setFechaRegistro(new Timestamp(new java.util.Date().getTime()));
        //ArticuloUtil.calculoPrecio(articulo);
        //Falta validar token
        Articulo articulo = (Articulo) entrada.getEntity();
        repository.save(articulo);
        articulo.getDetalleArticuloPrecio().setCodigoArticulo(articulo.getCodigo());
        detalleRepository.save(articulo.getDetalleArticuloPrecio());
        return articulo.getCodigo();
    }

    @Override
    public void update(String codigo, Articulo a) {
        Articulo articulo = findOne(codigo);
        if(articulo!=null) {
            articulo.setNombre(a.getNombre());
            articulo.setDescripcion(a.getDescripcion());
            articulo.setDetalleArticuloPrecio(new DetalleArticuloPrecio());
            articulo.getDetalleArticuloPrecio().setPrecioKilo(a.getDetalleArticuloPrecio().getPrecioKilo());
            articulo.getDetalleArticuloPrecio().setPeso(a.getDetalleArticuloPrecio().getPeso());
            articulo.getDetalleArticuloPrecio().setGasto(a.getDetalleArticuloPrecio().getGasto());
            articulo.getDetalleArticuloPrecio().setPrecioZonaLibre(a.getDetalleArticuloPrecio().getPrecioZonaLibre());
            articulo.getDetalleArticuloPrecio().setPorcentajeGasto(a.getDetalleArticuloPrecio().getPorcentajeGasto());
            articulo.getDetalleArticuloPrecio().setPrecioCompra(a.getDetalleArticuloPrecio().getPrecioCompra());
            articulo.getDetalleArticuloPrecio().setPrecioVenta(a.getDetalleArticuloPrecio().getPrecioVenta());
            articulo.getDetalleArticuloPrecio().setPrecioMercado(a.getDetalleArticuloPrecio().getPrecioMercado());
            articulo.setFechaActualizacion(new Timestamp(new java.util.Date().getTime()));
            ArticuloUtil.calculoPrecio(articulo);
            repository.save(articulo);
        }
    }

    @Override
    public boolean delete(String codigo) {
        if(!repository.exists(codigo)) return false;
        //falta Control de articulo en movimiento.
        repository.delete(codigo);
        return true;
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
