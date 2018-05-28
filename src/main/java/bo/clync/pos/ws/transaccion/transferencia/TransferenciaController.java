package bo.clync.pos.ws.transaccion.transferencia;


import bo.clync.pos.dao.ServPatron;
import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.transaccion.generic.TransaccionRequest;
import bo.clync.pos.dao.transaccion.generic.TransaccionResponse;
import bo.clync.pos.servicios.ambiente.AmbienteServicio;
import bo.clync.pos.servicios.articulo.ArticuloServicio;
import bo.clync.pos.servicios.transaccion.transferencia.EnvioServicio;
import bo.clync.pos.servicios.usuario.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transferencia")
public class TransferenciaController {

    @Autowired
    private EnvioServicio envioServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ArticuloServicio articuloServicio;
    @Autowired
    private AmbienteServicio ambienteServicio;

    private String token = "20171029130500-1-1";

    @CrossOrigin
    @GetMapping("/init")
    public ResponseEntity<?> init() {
        return new ResponseEntity<>(envioServicio.init(token), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> nuevo(@RequestBody TransaccionRequest request) {
        TransaccionResponse response = null;
        try {
            response = envioServicio.nuevo(token, request);
        } catch (Exception e) {
            response = new TransaccionResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> actualizar(@RequestBody TransaccionRequest request) {
        TransaccionResponse response = null;
        try {
            response = envioServicio.actualizar(token, request);
        } catch (Exception e) {
            response = new TransaccionResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") String id) {
        ServResponse response = null;
        try {
            response = envioServicio.eliminar(token, id);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response , HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/articulo/quest/{codigo}")
    public ResponseEntity<?> obtenerArticulo(@PathVariable("codigo") String codigo) {
        return new ResponseEntity<>(articuloServicio.obtenerArticulo(token, codigo), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/articulo/list")
    public ResponseEntity<?> listaArticulo() {
        return new ResponseEntity<>(articuloServicio.listaArticulo(token, null), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/articulo/list")
    public ResponseEntity<?> listaArticuloPorPatron(@RequestBody ServPatron patron) {
        return new ResponseEntity<>(articuloServicio.listaArticulo(token, patron.getPatron()), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/ambiente/quest/{codigo}")
    public ResponseEntity<?> obtenerAmbiente(@PathVariable("codigo") String codigo) {
        return new ResponseEntity<>(ambienteServicio.obtenerSucursal(token, codigo), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/ambiente/list")
    public ResponseEntity<?> listaAmbiente() {
        return new ResponseEntity<>(ambienteServicio.listaSurcursal(token, null), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/ambiente/list")
    public ResponseEntity<?> listaAmbientePorPatron(@RequestBody ServPatron patron) {
        return new ResponseEntity<>(articuloServicio.listaArticulo(token, patron.getPatron()), HttpStatus.OK);
    }
}
