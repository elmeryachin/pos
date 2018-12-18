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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    @GetMapping("/porrecibir/quest/movimiento/{nro}")
    public ResponseEntity<?> obtenerPorNroMovimiento(@RequestHeader(value="token") String token,
                                                     @PathVariable("nro") String nro) {
        String tokenAlmacen = "12345-1";
        //Se quema el token para salvar este caso, considerando que es unico q envia
        String id = this.recibirServicio.getIdTransaccionPorRecibir(nro, tokenAlmacen);
        System.out.println("Print id generado de nro mmovimiento " + id);
        return new ResponseEntity<>(this.recibirServicio.obtenerPorRecibir(token, id), HttpStatus.OK);
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
                                       @RequestBody TransaccionRequest request,
                                       HttpServletRequest http) {
        ServResponse response = null;
        try {
            response = recibirServicio.confirmarRecepcion(token, id, request, http);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/cancelar/recepcion/{id}")
    public ResponseEntity<?> cancelar(@RequestHeader(value="token") String token,
                                      @PathVariable("id") String id,
                                      HttpServletRequest http) {
        ServResponse response = null;
        try {
            response = recibirServicio.cancelarRecepcion(token, id, http);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }


}
