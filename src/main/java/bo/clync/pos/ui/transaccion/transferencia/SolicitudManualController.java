package bo.clync.pos.ui.transaccion.transferencia;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.servicios.transaccion.transferencia.SolicitudManualServicio;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transferencia/recibir/solicitud")
public class SolicitudManualController {

    @Autowired
    private SolicitudManualServicio servicio;
    
	@CrossOrigin
    @GetMapping("/init")
    public ResponseEntity<?> init(@RequestHeader(value="token") String token) {
        System.out.println("init de solicitud ");
	    return new ResponseEntity<>(this.servicio.init(token), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<?> nuevo(@RequestHeader(value="token") String token,
                                   @RequestBody TransaccionRequest request,
                                   HttpServletRequest http) {
        TransaccionResponse response = null;
        try {
            response = this.servicio.adicionar(token, request, http);
        } catch (Exception e) {
            response = new TransaccionResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/update")
    public ResponseEntity<?> actualizar(@RequestHeader(value="token") String token,
                                        @RequestBody TransaccionRequest request,
                                        HttpServletRequest http) {
        TransaccionResponse response = null;
        try {
            response = this.servicio.actualizar(token, request, http);
        } catch (Exception e) {
            response = new TransaccionResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminar(@RequestHeader(value="token") String token,
                                      @PathVariable("id") String id,
                                      HttpServletRequest http) {
        ServResponse response = null;
        try {
            response = this.servicio.eliminar(token, id, http);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response , HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/quest/{id}")
    public ResponseEntity<?> obtener(@RequestHeader(value="token") String token,
                                     @PathVariable("id") String id) {
        return new ResponseEntity<>(this.servicio.obtener(token, id), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<?> lista(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(servicio.lista(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/quest/movimiento/{nro}")
    public ResponseEntity<?> obtenerPorNroMovimiento(@RequestHeader(value="token") String token,
                                                     @PathVariable("nro") String nro) {
        System.out.println("obtenerPorNroMovimiento ......");
        String id = servicio.getIdTransaccion(nro, token);
        return new ResponseEntity<>(servicio.obtener(token, id), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/confirmados/list")
    public ResponseEntity<?> listaConfirmados(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(servicio.listaConfirmados(token), HttpStatus.OK);
    }

}
