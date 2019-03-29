package bo.clync.pos.ui.transaccion.transferencia;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.servicios.transaccion.transferencia.EnvioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transferencia/envio")
public class EnvioController {

    @Autowired
    private EnvioServicio servicio;

    @CrossOrigin
    @GetMapping("/init")
    public ResponseEntity<?> init(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(servicio.init(token), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<?> nuevo(@RequestHeader(value="token") String token,
                                   @RequestBody TransaccionRequest request,
                                   HttpServletRequest http) {
        TransaccionResponse response = null;
        try {
            response = servicio.adicionar(token, request, http);
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
            response = servicio.actualizar(token, request, http);
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
            response = servicio.eliminar(token, id, http);
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
        String id = this.servicio.getIdTransaccion(nro, token);
        return new ResponseEntity<>(this.servicio.obtener(token, id), HttpStatus.OK);
    }

    // ########################## SEGUNDA PARTE ENVIO ##############################
    @CrossOrigin
    @PutMapping("/reconfirmar/{id}")
    public ResponseEntity<?> reconfirmar(@RequestHeader(value="token") String token,
                                         @PathVariable("id") String id,
                                         HttpServletRequest http) throws Exception{
        return new ResponseEntity<>(servicio.reconfirmar(token, id, http), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/reconfirmar/list")
    public ResponseEntity<?> listaReConfirmados(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(servicio.listaReConfirmados(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/diferencia/quest/{id}")
    public ResponseEntity<?> obtenerDiferencia(@RequestHeader(value="token") String token,
                                               @PathVariable("id") String id) {
        //Donde "A" son los registros nuevos generados extras por q no llegaron.
        return new ResponseEntity<>(this.servicio.obtenerDiferencia(token, id + "A"), HttpStatus.OK);
    }

    // ########################## TERCERA PARTE ENVIO ##############################
    @CrossOrigin
    @GetMapping("/confirmados/list")
    public ResponseEntity<?> listaConfirmados(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(servicio.listaConfirmados(token), HttpStatus.OK);
    }

}
