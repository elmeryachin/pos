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

    @CrossOrigin
    @PostMapping("/grabar")
    public ResponseEntity<?> grabar(@RequestHeader(value="token") String token,
                                    @RequestBody DiscoRequest request) {
        return new ResponseEntity<>( service.grabar (token, request), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/leer")
    public ResponseEntity<?> leer(@RequestHeader(value="token") String token,
                                  Byte[] array) {
        return new ResponseEntity<>(service.leer(token, array),HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/recuperar")
    public ResponseEntity<?> recuperar(@RequestHeader(value="token") String token,
                                       DiscoRequest request) {
        return new ResponseEntity<>(service.recuperar(token, request), HttpStatus.OK);
    }


}
