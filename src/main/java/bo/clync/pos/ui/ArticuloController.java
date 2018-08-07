package bo.clync.pos.ui;

import bo.clync.pos.arquetipo.objetos.ServPatron;
import bo.clync.pos.arquetipo.objetos.articulo.ArticuloRequest;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.articulo.lista.ServListaResponse;
import bo.clync.pos.arquetipo.objetos.articulo.obtener.ServObtenerResponse;
import bo.clync.pos.servicios.articulo.ArticuloServicio;
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
    private ArticuloServicio service;

    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<ServListaResponse> lista(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(service.lista(token), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/list")
    public ResponseEntity<ServListaResponse> listaPorCodigo(@RequestHeader(value="token") String token,
                                                            @RequestBody ServPatron patron) {
        return new ResponseEntity<>(service.listaPorCodigo(token, patron.getPatron()), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/quest/{codigo}")
    public ResponseEntity<ServObtenerResponse> obtener(@RequestHeader(value="token") String token,
                                                       @PathVariable("codigo") String codigo) {
        return new ResponseEntity<>(service.obtener(codigo, token), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<ServResponse> nuevo(@RequestHeader(value="token") String token,
                                              @RequestBody ArticuloRequest request) {
        return new ResponseEntity<>(service.nuevo(request, token), HttpStatus.CREATED);
    }

    @CrossOrigin
    @PutMapping("/update/{codigo}")
    public ResponseEntity<ServResponse> actualizar(@RequestHeader(value="token") String token,
                                                   @PathVariable("codigo") String codigo,
                                               @RequestBody ArticuloRequest request) {
        return new ResponseEntity<>(service.actualizar(codigo, request, token), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{codigo}")
    public ResponseEntity<ServResponse> eliminar(@RequestHeader(value="token") String token,
                                                 @PathVariable("codigo") String codigo) {
        return new ResponseEntity<>(service.eliminar(codigo, token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/reporte/list/{tipo}")
    public ResponseEntity<byte[]> reporteListaArticulos(@RequestHeader(value="token") String token,
                                                        @PathVariable("tipo") String tipo) {
        return new ResponseEntity<>(service.reporteListaArticulos(token, tipo), HttpStatus.OK);
    }

}
