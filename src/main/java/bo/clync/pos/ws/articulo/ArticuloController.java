package bo.clync.pos.ws.articulo;

import bo.clync.pos.model.articulo.ServRequest;
import bo.clync.pos.model.articulo.ServResponse;
import bo.clync.pos.model.articulo.lista.ServListaResponse;
import bo.clync.pos.model.articulo.obtener.ServObtenerResponse;
import bo.clync.pos.servicios.articulo.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by eyave on 05-10-17.
 */
@RestController
@RequestMapping("/articulo")
public class ArticuloController {

    @Autowired
    private Servicio service;

    private String token = "default";

    @RequestMapping(value = "/hola", method = RequestMethod.GET)
    public ResponseEntity<String> listaSaludo(@RequestHeader("token") String token) {
        return new ResponseEntity<>("Hola " + token, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<ServListaResponse> lista() {
        return new ResponseEntity<>(service.lista(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/quest/{codigo}")
    public ResponseEntity<ServObtenerResponse> obtener(@PathVariable("codigo") String codigo) {
        return new ResponseEntity<>(service.obtener(codigo, token), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<ServResponse> nuevo(@RequestBody ServRequest request) {
        return new ResponseEntity<>(service.nuevo(request, token), HttpStatus.CREATED);
    }


    @CrossOrigin
    @PutMapping("/update/{codigo}")
    public ResponseEntity<ServResponse> update(@PathVariable("codigo") String codigo,
                                               @RequestBody ServRequest request) {
        return new ResponseEntity<>(service.actualizar(codigo, request, token), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{codigo}")
    public ResponseEntity<ServResponse> delete(@PathVariable("codigo") String codigo) {
        return new ResponseEntity<>(service.baja(codigo, token), HttpStatus.OK);
    }

}
