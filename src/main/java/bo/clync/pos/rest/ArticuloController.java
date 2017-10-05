package bo.clync.pos.rest;

import bo.clync.pos.model.Articulo;
import bo.clync.pos.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by eyave on 05-10-17.
 */
@RestController
public class ArticuloController {

    @Autowired
    private ArticuloService service;

    @RequestMapping(value = "/articulo/", method = RequestMethod.GET)
    public ResponseEntity<List<Articulo>> list() {
        List<Articulo> articulos = service.findAll();
        return new ResponseEntity<List<Articulo>>(articulos, HttpStatus.OK);
    }

    @RequestMapping(value = "/articulo/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        Articulo articulo = service.findOne(id);
        if (articulo == null) {
            return new ResponseEntity<Articulo>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Articulo>(articulo, HttpStatus.OK);
    }


    @RequestMapping(value = "/articulo/codigo/{codigo}", method = RequestMethod.GET)
    public ResponseEntity<?> getByCode(@PathVariable("codigo") String codigo) {
        Articulo articulo = service.findByCodigo(codigo);
        if (articulo == null) {
            return new ResponseEntity<Articulo>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Articulo>(articulo, HttpStatus.OK);
    }

    @RequestMapping(value = "/articulo/", method = RequestMethod.POST)
    public ResponseEntity<Articulo> create(@RequestBody Articulo articulo, UriComponentsBuilder ucBuilder) {
        service.save(articulo);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/articulo/{id}").buildAndExpand(articulo.getId()).toUri());
        return new ResponseEntity<Articulo>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/articulo/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Articulo> update(@PathVariable("id") Long id, @RequestBody Articulo articulo) {
        service.update(id, articulo);
        if(articulo == null)
            return new ResponseEntity<Articulo>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Articulo>(articulo, HttpStatus.OK);
    }

    @RequestMapping(value = "/articulo/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if(!service.delete(id))
            return new ResponseEntity<Articulo>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Articulo>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/articulo/", method = RequestMethod.DELETE)
    public ResponseEntity<Articulo> deleteAll() {
        service.deleteAll();
        return new ResponseEntity<Articulo>(HttpStatus.NO_CONTENT);
    }
}
