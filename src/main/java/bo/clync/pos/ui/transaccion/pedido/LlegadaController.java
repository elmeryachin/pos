package bo.clync.pos.ui.transaccion.pedido;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.servicios.transaccion.pedido.LlegadaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/llegada")
public class LlegadaController {

    @Autowired
    private LlegadaServicio servicio;

    @CrossOrigin
    @PutMapping("/llegada/confirmar/{id}")
    public ResponseEntity<?> confirmarLlegada(@RequestHeader(value="token") String token,
                                              @PathVariable("id") String id,
                                              HttpServletRequest http) {
        ServResponse response = null;
        try {
            response = servicio.confirmarLlegada(token, id, http);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/llegada/cancelar/{id}")
    public ResponseEntity<?> llegadaCancelar(@RequestHeader(value="token") String token,
                                             @PathVariable("id") String id,
                                             HttpServletRequest http) {
        ServResponse response = null;
        try {
            response = servicio.cancelarLlegada(token, id, http);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/llegada/list")
    public ResponseEntity<?> listaLlegadas(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>( servicio.lista(token), HttpStatus.OK);
    }
}
