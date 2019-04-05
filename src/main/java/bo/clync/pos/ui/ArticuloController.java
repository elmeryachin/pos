package bo.clync.pos.ui;

import bo.clync.pos.arquetipo.objetos.ServPatron;
import bo.clync.pos.arquetipo.objetos.articulo.ArticuloRequest;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.articulo.lista.ServListaResponse;
import bo.clync.pos.arquetipo.objetos.articulo.obtener.ServObtenerResponse;
import bo.clync.pos.servicios.articulo.ArticuloServicio;
import bo.clync.pos.servicios.discos.DiscoServicio;
import bo.clync.pos.utilitarios.UtilsDisco;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/articulo")
public class ArticuloController {

    @Autowired
    private ArticuloServicio service;

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
                                              @RequestBody ArticuloRequest request,
                                              HttpServletRequest http) {
        return new ResponseEntity<>(service.nuevo(request, token, http), HttpStatus.CREATED);
    }

    @CrossOrigin
    @PutMapping("/update/{codigo}")
    public ResponseEntity<ServResponse> actualizar(@RequestHeader(value="token") String token,
                                                   @PathVariable("codigo") String codigo,
                                                   @RequestBody ArticuloRequest request,
                                                   HttpServletRequest http) throws Exception {
        return new ResponseEntity<>(service.actualizar(codigo, request, token, http), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{codigo}")
    public ResponseEntity<ServResponse> eliminar(@RequestHeader(value="token") String token,
                                                 @PathVariable("codigo") String codigo,
                                                 HttpServletRequest http) throws Exception {
        return new ResponseEntity<>(service.eliminar(codigo, token, http), HttpStatus.OK);
    }

    /*@CrossOrigin
    @GetMapping("/reporte/list/{tipo}")
    public ResponseEntity<byte[]> reporteListaArticulos(@RequestHeader(value="token") String token,
                                                        @PathVariable("tipo") String tipo) {
        return new ResponseEntity<>(service.reporteListaArticulos(token, tipo), HttpStatus.OK);
    }*/

}
