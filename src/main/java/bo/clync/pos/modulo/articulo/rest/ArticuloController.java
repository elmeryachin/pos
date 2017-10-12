package bo.clync.pos.modulo.articulo.rest;

import bo.clync.pos.model.Articulo;
import bo.clync.pos.modulo.articulo.entity.lista.ServListaResponse;
import bo.clync.pos.modulo.articulo.entity.ServRequest;
import bo.clync.pos.modulo.articulo.entity.ServResponse;
import bo.clync.pos.modulo.articulo.entity.obtener.ServObtenerResponse;
import bo.clync.pos.modulo.articulo.servic.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by eyave on 05-10-17.
 */
@RestController
public class ArticuloController {

    @Autowired
    private Servicio service;
    private String token = "default";


    @RequestMapping(value = "/articulo/hola", method = RequestMethod.GET)
    public ResponseEntity<String> listaSaludo(@RequestHeader("token") String token) {
        return new ResponseEntity<>("Hola " + token, HttpStatus.OK);
    }

    @RequestMapping(value = "/articulo/list", method = RequestMethod.GET)
    public ResponseEntity<ServListaResponse> lista() {//@RequestHeader("token") String token
        return new ResponseEntity<>(service.lista(token), HttpStatus.OK);
    }

    //recuperar el objeto
    @RequestMapping(value = "/articulo/quest/{codigo}", method = RequestMethod.GET)
    public ResponseEntity<ServObtenerResponse> obtener(@PathVariable("codigo") String codigo) {
        return new ResponseEntity<>(service.obtener(codigo, token), HttpStatus.OK);
    }

    @RequestMapping(value = "/articulo/add", method = RequestMethod.POST)
    public ResponseEntity<ServResponse> nuevo(@RequestBody ServRequest request) {
        return new ResponseEntity<>(service.nuevo(request, token), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/articulo/update/{codigo}", method = RequestMethod.PUT)
    public ResponseEntity<ServResponse> update(@PathVariable("codigo") String codigo,
                                               @RequestBody ServRequest request) {
        System.out.println("Ingresando a actualizar. " + request);
        return new ResponseEntity<>(service.actualizar(codigo, request, token), HttpStatus.OK);
    }

    @RequestMapping(value = "/articulo/delete/{codigo}", method = RequestMethod.DELETE)
    public ResponseEntity<ServResponse> delete(@PathVariable("codigo") String codigo) {
        return new ResponseEntity<>(service.baja(codigo, token), HttpStatus.OK);
    }

}
