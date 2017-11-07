package bo.clync.pos.ws.transaccion.llegada;

import bo.clync.pos.model.transaccion.llegada.LlegadaRequest;
import bo.clync.pos.model.transaccion.llegada.LlegadaResponse;
import bo.clync.pos.model.transaccion.pedido.PedidoRequest;
import bo.clync.pos.servicios.transaccion.llegada.LlegadaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by eyave on 02-11-17.
 */
@RestController
@RequestMapping("/llegada")
public class llegadaController {

    @Autowired
    private LlegadaServicio service;

    private String token = "20171029130500-1-1";


    @CrossOrigin
    @PutMapping("/confirmar/{id}")
    public ResponseEntity<?> actualizar(@RequestBody LlegadaRequest request) {
        LlegadaResponse response = null;
        try {
            response = service.confirmarLlegada(token, request);
        } catch (Exception e) {
            response = new LlegadaResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<?> lista() {
        return new ResponseEntity<>( service.lista(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/quest/{id}")
    public ResponseEntity<?> obtener(@PathVariable("id") String id) {
        return new ResponseEntity<>(service.obtener(token, id), HttpStatus.OK);
    }

}
