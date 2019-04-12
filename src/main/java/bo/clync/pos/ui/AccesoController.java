package bo.clync.pos.ui;

import bo.clync.pos.arquetipo.objetos.acceso.AccesoRequest;
import bo.clync.pos.servicios.acceso.AccesoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
}
