package bo.clync.pos.ui;

import bo.clync.pos.arquetipo.objetos.DiscoRequest;
import bo.clync.pos.servicios.discos.DiscoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discos")
public class DiscoController {

    @Autowired
    private DiscoServicio service;

    private String token = "20171029130500-1-1";

    @CrossOrigin
    @PostMapping("/grabar")
    public ResponseEntity<?> grabar(@RequestBody DiscoRequest request) {
        return new ResponseEntity<>( service.grabar (token, request), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/leer")
    public ResponseEntity<?> leer(String token, Byte[] array) {
        return new ResponseEntity<>(service.leer(token, array),HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/recuperar")
    public ResponseEntity<?> recuperar(String token, DiscoRequest request) {
        return new ResponseEntity<>(service.recuperar(token, request), HttpStatus.OK);
    }


}
