package bo.clync.pos.ui.transaccion.transferencia;

import bo.clync.pos.arquetipo.objetos.ServPatron;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.servicios.ambiente.AmbienteServicio;
import bo.clync.pos.servicios.articulo.ArticuloServicio;
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
    private AmbienteServicio ambienteServicio;
    @Autowired
    private ArticuloServicio articuloServicio;
    @Autowired
    private SolicitudManualServicio solicitudManualServicio;
    
	@CrossOrigin
    @GetMapping("/init")
    public ResponseEntity<?> init(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(this.solicitudManualServicio.init(token), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<?> nuevo(@RequestHeader(value="token") String token,
                                   @RequestBody TransaccionRequest request,
                                   HttpServletRequest http) {
        TransaccionResponse response = null;
        try {
            response = this.solicitudManualServicio.adicionar(token, request, http);
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
            response = this.solicitudManualServicio.actualizar(token, request, http);
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
            response = this.solicitudManualServicio.eliminar(token, id, http);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response , HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<?> lista(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(solicitudManualServicio.lista(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/quest/{id}")
    public ResponseEntity<?> obtener(@RequestHeader(value="token") String token,
                                     @PathVariable("id") String id) {
        return new ResponseEntity<>(this.solicitudManualServicio.obtener(token, id), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/confirmados/list")
    public ResponseEntity<?> listaConfirmados(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(solicitudManualServicio.listaConfirmados(token), HttpStatus.OK);
    }

}
