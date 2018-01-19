package bo.clync.pos.ws.transaccion.pedido;

import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.transaccion.pedido.PedidoRequest;
import bo.clync.pos.dao.transaccion.pedido.PedidoResponse;
import bo.clync.pos.dao.transaccion.pedido.UsuarioRequest;
import bo.clync.pos.servicios.transaccion.pedido.PedidoServicio;
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
    private PedidoServicio service;

    private String token = "20171029130500-1-1";

    @CrossOrigin
    @GetMapping("/init")
    public ResponseEntity<?> init() {
        return new ResponseEntity<>(service.init(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/proveedor/quest/{codigo}")
    public ResponseEntity<?> obtenerProveedor(@PathVariable("codigo") String codigo) {
        return new ResponseEntity<>( service.obtenerProveedor(token, codigo), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/proveedor/list")
    public ResponseEntity<?> listaProveedor() {
        return new ResponseEntity<>( service.listaProveedor(token, null), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/proveedor/list")
    public ResponseEntity<?> listaProveedorPorCodigo(@RequestParam("patron") String patron) {
        return new ResponseEntity<>( service.listaProveedor(token, patron), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/articulo/quest/{codigo}")
    public ResponseEntity<?> obtenerArticulo(@PathVariable("codigo") String codigo) {
        return new ResponseEntity<>( service.obtenerArticulo(token, codigo), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/articulo/list/{patron}")
    public ResponseEntity<?> listaArticuloCodigo(@PathVariable("patron") String patron) {
        return new ResponseEntity<>( service.listaArticulo(token, patron), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/articulo/list")
    public ResponseEntity<?> listaArticulo() {
        return new ResponseEntity<>( service.listaArticulo(token, null), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<?> nuevo(@RequestBody PedidoRequest request) {
        PedidoResponse response = null;
        try {
            response = service.nuevo(token, request);
        } catch (Exception e) {
            response = new PedidoResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<?> lista() {
        return new ResponseEntity<>( service.listaSolicitud(token), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/update")
    public ResponseEntity<?> actualizar(@RequestBody PedidoRequest request) {
        PedidoResponse response = null;
        try {
            response = service.actualizar(token, request);
        } catch (Exception e) {
            response = new PedidoResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") String id) {
        ServResponse response = null;
        try {
            response = service.eliminar(token, id);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response , HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/quest/{id}")
    public ResponseEntity<?> obtener(@PathVariable("id") String id) {
        return new ResponseEntity<>(service.obtener(token, id), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/llegada/confirmar/{id}")
    public ResponseEntity<?> confirmarLlegada(@PathVariable("id") String id) {
        ServResponse response = null;
        try {
            response = service.confirmarLlegada(token, id);
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
            response = service.cancelarLlegada(token, id);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/llegada/list")
    public ResponseEntity<?> listaLlegadas() {
        return new ResponseEntity<>( service.listaLlegadas(token), HttpStatus.OK);
    }


    @CrossOrigin
    @PostMapping("/proveedor/add")
    public ResponseEntity<ServResponse> nuevo(@RequestBody UsuarioRequest request) {
        return new ResponseEntity<>(service.nuevoProveedor(token, request), HttpStatus.CREATED);
    }
}
