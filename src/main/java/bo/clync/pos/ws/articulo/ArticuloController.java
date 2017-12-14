package bo.clync.pos.ws.articulo;

import bo.clync.pos.dao.articulo.ArticuloRequest;
import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.articulo.lista.ServListaResponse;
import bo.clync.pos.dao.articulo.obtener.ServObtenerResponse;
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

    private String token = "20171029130500-1-1";


    @CrossOrigin
    @GetMapping("/hola")
    public ResponseEntity<String> listaSaludo(@RequestHeader("token") String token) {
        return new ResponseEntity<>("Hola " + token, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<ServListaResponse> lista() {
        return new ResponseEntity<>(service.lista(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/list/nativa")
    public ResponseEntity<ServListaResponse> listaNativa() {
        return new ResponseEntity<>(service.listaNativa(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/list/{patron}")
    public ResponseEntity<ServListaResponse> listaPorCodigo(@PathVariable("patron") String patron) {
        return new ResponseEntity<>(service.listaPorCodigo(token, patron), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/quest/{codigo}")
    public ResponseEntity<ServObtenerResponse> obtener(@PathVariable("codigo") String codigo) {
        return new ResponseEntity<>(service.obtener(codigo, token), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<ServResponse> nuevo(@RequestBody ArticuloRequest request) {
        return new ResponseEntity<>(service.nuevo(request, token), HttpStatus.CREATED);
    }

    @CrossOrigin
    @PutMapping("/update/{codigo}")
    public ResponseEntity<ServResponse> actualizar(@PathVariable("codigo") String codigo,
                                               @RequestBody ArticuloRequest request) {
        return new ResponseEntity<>(service.actualizar(codigo, request, token), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{codigo}")
    public ResponseEntity<ServResponse> eliminar(@PathVariable("codigo") String codigo) {
        return new ResponseEntity<>(service.eliminar(codigo, token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/reporte/list/{tipo}")
    public ResponseEntity<byte[]> reporteListaArticulos(@PathVariable("tipo") String tipo) {
        return new ResponseEntity<>(service.reporteListaArticulos(token, tipo), HttpStatus.OK);
    }

}
