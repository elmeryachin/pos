package bo.clync.pos.rest;

import bo.clync.pos.entity.Entrada;
import bo.clync.pos.entity.Salida;
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
    public ResponseEntity<Salida> list() {
        //Aun por Corregir recuperar el detalle Precio
        List<Articulo> articulos = service.findAll();
        Salida salida = new Salida(articulos, true, "Todo salio Bien");
        return new ResponseEntity<Salida>(salida, HttpStatus.OK);
    }

    //recuperar el objeto
    @RequestMapping(value = "/articulo/{codigo}", method = RequestMethod.GET)
    public ResponseEntity<Salida> get(@PathVariable("codigo") String codigo) {
        Salida salida = new Salida(service.findOne(codigo));
        return new ResponseEntity<Salida>(salida, HttpStatus.OK);
    }


    @RequestMapping(value = "/articulo/", method = RequestMethod.POST)
    public ResponseEntity<Salida> create(@RequestBody Entrada entrada, UriComponentsBuilder ucBuilder) {
        String codigo = service.save(entrada);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/articulo/{codigo}").buildAndExpand(codigo).toUri());
        return new ResponseEntity<Salida>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/articulo/{codigo}", method = RequestMethod.PUT)
    public ResponseEntity<Articulo> update(@PathVariable("codigo") String codigo, @RequestBody Articulo articulo) {
        service.update(codigo, articulo);
        if(articulo == null)
            return new ResponseEntity<Articulo>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Articulo>(articulo, HttpStatus.OK);
    }

    @RequestMapping(value = "/articulo/{codigo}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("codigo") String codigo) {
        if(!service.delete(codigo))
            return new ResponseEntity<Articulo>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Articulo>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/articulo/", method = RequestMethod.DELETE)
    public ResponseEntity<Articulo> deleteAll() {
        service.deleteAll();
        return new ResponseEntity<Articulo>(HttpStatus.NO_CONTENT);
    }
}
