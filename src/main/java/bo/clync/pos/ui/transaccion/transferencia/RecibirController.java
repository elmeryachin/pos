package bo.clync.pos.ui.transaccion.transferencia;

import bo.clync.pos.arquetipo.objetos.ServPatron;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.servicios.articulo.ArticuloServicio;
import bo.clync.pos.servicios.transaccion.transferencia.RecibirServicio;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transferencia/recibir")
public class RecibirController {

    @Autowired
    private RecibirServicio recibirServicio;
    @Autowired
    private ArticuloServicio articuloServicio;

    @CrossOrigin
    @GetMapping("/porrecibir/list")
    public ResponseEntity<?> listaPorRecibir(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(recibirServicio.listaPorRecibir(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/porrecibir/quest/{id}")
    public ResponseEntity<?> obtenerPorRecibir(@RequestHeader(value="token") String token,
                                     @PathVariable("id") String id) {
        return new ResponseEntity<>(recibirServicio.obtenerPorRecibir(token, id), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/recibidos/list")
    public ResponseEntity<?> lista(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(recibirServicio.lista(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/recibidos/quest/{id}")
    public ResponseEntity<?> obtener(@RequestHeader(value="token") String token,
                                     @PathVariable("id") String id) {
        return new ResponseEntity<>(recibirServicio.obtener(token, id), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/confirmar/recepcion/{id}")
    public ResponseEntity<?> confirmar(@RequestHeader(value="token") String token,
                                       @PathVariable("id") String id, 
                                       @RequestBody TransaccionRequest request) {
        ServResponse response = null;
        try {
            response = recibirServicio.confirmarRecepcion(token, id, request);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/cancelar/recepcion/{id}")
    public ResponseEntity<?> cancelar(@RequestHeader(value="token") String token,
                                      @PathVariable("id") String id) {
        ServResponse response = null;
        try {
            response = recibirServicio.cancelarRecepcion(token, id);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/articulo/quest/{codigo}")
    public ResponseEntity<?> obtenerArticulo(@RequestHeader(value="token") String token,
                                             @PathVariable("codigo") String codigo) {
        return new ResponseEntity<>(articuloServicio.obtenerArticulo(token, codigo), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/articulo/list")
    public ResponseEntity<?> listaArticulo(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(articuloServicio.listaArticulo(token, null), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/articulo/list")
    public ResponseEntity<?> listaArticuloPorPatron(@RequestHeader(value="token") String token,
                                                    @RequestBody ServPatron patron) {
        return new ResponseEntity<>(articuloServicio.listaArticulo(token, patron.getPatron()), HttpStatus.OK);
    }
}
