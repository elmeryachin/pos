package bo.clync.pos.ui.transaccion.transferencia;


import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.servicios.ambiente.AmbienteServicio;
import bo.clync.pos.servicios.articulo.ArticuloServicio;
import bo.clync.pos.servicios.transaccion.transferencia.RecibirServicio;
import bo.clync.pos.servicios.usuario.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transferencia/recibir")
public class RecibirController {

    @Autowired
    private RecibirServicio recibirServicio;

    private String token = "123456789";

    @CrossOrigin
    @GetMapping("/recibidos/list")
    public ResponseEntity<?> lista() {
        token = "123456789";
        return new ResponseEntity<>(recibirServicio.lista(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/recibidos/quest/{id}")
    public ResponseEntity<?> obtener(@PathVariable("id") String id) {
        token = "123456789";
        return new ResponseEntity<>(recibirServicio.obtener(token, id), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/confirmar/recepcion/{id}")
    public ResponseEntity<?> confirmar(@PathVariable("id") String id) {
        ServResponse response = null;
        try {
            token = "123456789";
            response = recibirServicio.confirmarRecepcion(token, id);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/cancelar/recepcion/{id}")
    public ResponseEntity<?> cancelar(@PathVariable("id") String id) {
        ServResponse response = null;
        try {
            token = "123456789";
            response = recibirServicio.cancelarRecepcion(token, id);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }


}
