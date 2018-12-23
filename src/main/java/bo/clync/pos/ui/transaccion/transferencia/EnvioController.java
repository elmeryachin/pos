package bo.clync.pos.ui.transaccion.transferencia;


import bo.clync.pos.arquetipo.objetos.ServPatron;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.servicios.ambiente.AmbienteServicio;
import bo.clync.pos.servicios.articulo.ArticuloServicio;
import bo.clync.pos.servicios.transaccion.transferencia.EnvioServicio;
import bo.clync.pos.servicios.usuario.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transferencia/envio")
public class EnvioController {

    @Autowired
    private EnvioServicio envioServicio;
    @Autowired
    private ArticuloServicio articuloServicio;
    @Autowired
    private AmbienteServicio ambienteServicio;

    @CrossOrigin
    @GetMapping("/init")
    public ResponseEntity<?> init(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(envioServicio.init(token), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<?> nuevo(@RequestHeader(value="token") String token,
                                   @RequestBody TransaccionRequest request,
                                   HttpServletRequest http) {
        TransaccionResponse response = null;
        try {
            response = envioServicio.adicionar(token, request, http);
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
            response = envioServicio.actualizar(token, request, http);
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
            response = envioServicio.eliminar(token, id, http);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response , HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<?> lista(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(envioServicio.lista(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/quest/{id}")
    public ResponseEntity<?> obtener(@RequestHeader(value="token") String token,
                                     @PathVariable("id") String id) {
        return new ResponseEntity<>(this.envioServicio.obtener(token, id), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/quest/movimiento/{nro}")
    public ResponseEntity<?> obtenerPorNroMovimiento(@RequestHeader(value="token") String token,
                                                     @PathVariable("nro") String nro) {
        String id = this.envioServicio.getIdTransaccion(nro, token);
        System.out.println("Print id generado de nro mmovimiento " + id);
        return new ResponseEntity<>(this.envioServicio.obtener(token, id), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/reconfirmar/{id}")
    public ResponseEntity<?> reconfirmar(@RequestHeader(value="token") String token,
                                         @PathVariable("id") String id,
                                         HttpServletRequest http) throws Exception{
        return new ResponseEntity<>(envioServicio.reconfirmar(token, id, http), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/reconfirmar/list")
    public ResponseEntity<?> listaReConfirmados(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(envioServicio.listaReConfirmados(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/confirmados/list")
    public ResponseEntity<?> listaConfirmados(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(envioServicio.listaConfirmados(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/diferencia/quest/{id}")
    public ResponseEntity<?> obtenerDiferencia(@RequestHeader(value="token") String token,
                                               @PathVariable("id") String id) {
        //Donde "A" es el generado para registros extras por q no llegaron.
        return new ResponseEntity<>(this.envioServicio.obtenerDiferencia(token, id + "A"), HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/ambiente/quest/{codigo}")
    public ResponseEntity<?> obtenerAmbiente(@RequestHeader(value="token") String token,
                                             @PathVariable("codigo") String codigo) {
        return new ResponseEntity<>(ambienteServicio.obtenerSucursal(token, codigo), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/ambiente/list")
    public ResponseEntity<?> listaAmbiente(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(ambienteServicio.listaSurcursal(token, null), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/ambiente/list")
    public ResponseEntity<?> listaAmbientePorPatron(@RequestHeader(value="token") String token,
                                                    @RequestBody ServPatron patron) {
        return new ResponseEntity<>(ambienteServicio.listaSurcursal(token, patron.getPatron()), HttpStatus.OK);
    }
}
