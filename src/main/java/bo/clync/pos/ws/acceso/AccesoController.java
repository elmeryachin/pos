package bo.clync.pos.ws.acceso;

import bo.clync.pos.model.acceso.AccesoRequest;
import bo.clync.pos.servicios.acceso.AccesoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by eyave on 29-10-17.
 */
@RestController
@RequestMapping("/acceso")
public class AccesoController {

    @Autowired
    private AccesoServicio servicio;

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<?> ingreso(@RequestBody AccesoRequest request) {
        return new ResponseEntity<>(servicio.ingreso(request), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/about")
    public ResponseEntity<?> acercaDe() {
        return new ResponseEntity<>("Software de Almacenes, Ventas, e Inventario", HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/credit")
    public ResponseEntity<?> credito() {
        return new ResponseEntity<>(new AccesoRequest(), HttpStatus.OK);
    }
}
