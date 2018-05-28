package bo.clync.pos.ws.transaccion.pedido;

import bo.clync.pos.dao.ServPatron;
import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.transaccion.generic.TransaccionRequest;
import bo.clync.pos.dao.transaccion.generic.TransaccionResponse;
import bo.clync.pos.dao.usuario.generic.UsuarioRequest;
import bo.clync.pos.servicios.articulo.ArticuloServicio;
import bo.clync.pos.servicios.transaccion.pedido.LlegadaServicio;
import bo.clync.pos.servicios.transaccion.pedido.SolicitudServicio;
import bo.clync.pos.servicios.usuario.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by eyave on 27-10-17.
 */
@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private SolicitudServicio solicitudServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private LlegadaServicio llegadaServicio;
    @Autowired
    private ArticuloServicio articuloServicio;

    private String token = "20171029130500-1-1";

    @CrossOrigin
    @GetMapping("/init")
    public ResponseEntity<?> init() {
        return new ResponseEntity<>(solicitudServicio.init(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/articulo/quest/{codigo}")
    public ResponseEntity<?> obtenerArticulo(@PathVariable("codigo") String codigo) {
        return new ResponseEntity<>( articuloServicio.obtenerArticulo(token, codigo), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/articulo/list")
    public ResponseEntity<?> listaArticuloCodigo(@RequestBody ServPatron patron) {
        return new ResponseEntity<>( articuloServicio.listaArticulo(token, patron.getPatron()), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/articulo/list")
    public ResponseEntity<?> listaArticulo() {
        return new ResponseEntity<>( articuloServicio.listaArticulo(token, null), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<?> nuevo(@RequestBody TransaccionRequest request) {
        TransaccionResponse response = null;
        try {
            response = solicitudServicio.nuevo(token, request);
        } catch (Exception e) {
            response = new TransaccionResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<?> lista() {
        return new ResponseEntity<>( solicitudServicio.lista(token), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/update")
    public ResponseEntity<?> actualizar(@RequestBody TransaccionRequest request) {
        TransaccionResponse response = null;
        try {
            response = solicitudServicio.actualizar(token, request);
        } catch (Exception e) {
            response = new TransaccionResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") String id) {
        ServResponse response = null;
        try {
            response = solicitudServicio.eliminar(token, id);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response , HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/quest/{id}")
    public ResponseEntity<?> obtener(@PathVariable("id") String id) {
        return new ResponseEntity<>(solicitudServicio.obtener(token, id), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/llegada/confirmar/{id}")
    public ResponseEntity<?> confirmarLlegada(@PathVariable("id") String id) {
        ServResponse response = null;
        try {
            response = solicitudServicio.confirmarLlegada(token, id);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/llegada/cancelar/{id}")
    public ResponseEntity<?> llegadaCancelar(@PathVariable("id") String id) {
        ServResponse response = null;
        try {
            response = llegadaServicio.cancelarLlegada(token, id);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/llegada/list")
    public ResponseEntity<?> listaLlegadas() {
        return new ResponseEntity<>( llegadaServicio.lista(token), HttpStatus.OK);
    }


    @CrossOrigin
    @PostMapping("/proveedor/add")
    public ResponseEntity<ServResponse> nuevo(@RequestBody UsuarioRequest request) {
        return new ResponseEntity<>(usuarioServicio.nuevoProveedor(token, request), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/proveedor/quest/{codigo}")
    public ResponseEntity<?> obtenerProveedor(@PathVariable("codigo") String codigo) {
        return new ResponseEntity<>( usuarioServicio.obtenerProveedor(token, codigo), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/proveedor/list")
    public ResponseEntity<?> listaProveedor() {
        return new ResponseEntity<>( usuarioServicio.listaProveedor(token, null), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/proveedor/list")
    public ResponseEntity<?> listaProveedorPorCodigo(@RequestBody ServPatron patron) {
        return new ResponseEntity<>( usuarioServicio.listaProveedor(token, patron.getPatron()), HttpStatus.OK);
    }
}
