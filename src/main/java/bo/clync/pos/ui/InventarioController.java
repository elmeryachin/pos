package bo.clync.pos.ui;

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

    @CrossOrigin
    @GetMapping(value = "/articulo/{codigo}/existence")
    public ResponseEntity<?> existenciaArticulo(@RequestHeader(value="token") String token,
                                                @PathVariable("codigo") String codigo) {
        //Postergado
        return new ResponseEntity<>( service.existenciaArticulo(token, codigo), HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping(value = "/articulo/sucursales")
    public ResponseEntity<?> listaSucursales(@RequestHeader(value="token") String token) {
        //Postergado
        return new ResponseEntity<>( service.listaSucursales(token), HttpStatus.OK);
    }

}
