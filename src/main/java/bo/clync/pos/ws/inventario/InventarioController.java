package bo.clync.pos.ws.inventario;

import bo.clync.pos.servicios.inventario.InventarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by eyave on 03-12-17.
 */
@RestController
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private InventarioServicio service;

    private String token = "20171029130500-1-1";

    @CrossOrigin
    @GetMapping(value = "/articulo/{codigo}/existence")
    public ResponseEntity<?> existenciaArticulo(@PathVariable("codigo") String codigo) {
        //Postergado
        return new ResponseEntity<>( service.existenciaArticulo(token, codigo), HttpStatus.OK);
    }
}
