package bo.clync.pos.ui;

import bo.clync.pos.arquetipo.objetos.acceso.AccesoRequest;
import bo.clync.pos.servicios.acceso.AccesoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by eyave on 29-10-17.
 */
@RestController
@RequestMapping("/acceso")
public class AccesoController {

    private static final Logger LOGGER = LogManager.getLogger(AccesoController.class);

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
        LOGGER.error("about ...");
        LOGGER.trace("about ...");
        LOGGER.info("about ...");
        LOGGER.debug("about ...");
        return new ResponseEntity<>("Software de Almacenes, Ventas, e Inventario", HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/credit")
    public ResponseEntity<?> credito() {
        LOGGER.debug("Creditos ...");
        return new ResponseEntity<>(new AccesoRequest(), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/salir")
    public ResponseEntity<?> salir(@RequestHeader(value="token") String token){
        return new ResponseEntity<>(servicio.salir(token), HttpStatus.OK);
    }
}
