package bo.clync.pos.ui.transaccion.pedido;

import bo.clync.pos.arquetipo.objetos.ServPatron;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioRequest;
import bo.clync.pos.servicios.articulo.ArticuloServicio;
import bo.clync.pos.servicios.transaccion.pedido.LlegadaServicio;
import bo.clync.pos.servicios.transaccion.pedido.SolicitudServicio;
import bo.clync.pos.servicios.usuario.UsuarioServicio;
import bo.clync.pos.utilitarios.UtilsDominio;
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

    @CrossOrigin
    @GetMapping("/init")
    public ResponseEntity<?> init(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(solicitudServicio.init(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/articulo/quest/{codigo}")
    public ResponseEntity<?> obtenerArticulo(@RequestHeader(value="token") String token,
                                             @PathVariable("codigo") String codigo) {
        return new ResponseEntity<>( articuloServicio.obtenerArticulo(token, codigo), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/articulo/list")
    public ResponseEntity<?> listaArticuloCodigo(@RequestHeader(value="token") String token,
                                                 @RequestBody ServPatron patron) {
        return new ResponseEntity<>( articuloServicio.listaArticulo(token, patron.getPatron()), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/articulo/list")
    public ResponseEntity<?> listaArticulo(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>( articuloServicio.listaArticulo(token, null), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<?> adicionar(@RequestHeader(value="token") String token,
                                       @RequestBody TransaccionRequest request) {
        TransaccionResponse response = null;
        try {
            response = solicitudServicio.adicionar(token, request);
        } catch (Exception e) {
            response = new TransaccionResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<?> lista(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>( solicitudServicio.lista(token), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/update")
    public ResponseEntity<?> actualizar(@RequestHeader(value="token") String token,
                                        @RequestBody TransaccionRequest request) {
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
    public ResponseEntity<?> eliminar(@RequestHeader(value="token") String token,
                                      @PathVariable("id") String id) {
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
    public ResponseEntity<?> obtener(@RequestHeader(value="token") String token,
                                     @PathVariable("id") String id) {
        return new ResponseEntity<>(solicitudServicio.obtener(token, id), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/llegada/confirmar/{id}")
    public ResponseEntity<?> confirmarLlegada(@RequestHeader(value="token") String token,
                                              @PathVariable("id") String id) {
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
    public ResponseEntity<?> llegadaCancelar(@RequestHeader(value="token") String token,
                                             @PathVariable("id") String id) {
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
    public ResponseEntity<?> listaLlegadas(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>( llegadaServicio.lista(token), HttpStatus.OK);
    }


    @CrossOrigin
    @PostMapping("/proveedor/add")
    public ResponseEntity<ServResponse> nuevo(@RequestHeader(value="token") String token,
                                              @RequestBody UsuarioRequest request) {
        return new ResponseEntity<>(usuarioServicio.nuevoUsuario(token, request, UtilsDominio.TIPO_USUARIO_PROVEEDOR), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/proveedor/quest/{codigo}")
    public ResponseEntity<?> obtenerProveedor(@RequestHeader(value="token") String token,
                                              @PathVariable("codigo") String codigo) {
        return new ResponseEntity<>( usuarioServicio.obtenerUsuario(token, codigo, UtilsDominio.TIPO_USUARIO_PROVEEDOR), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/proveedor/list")
    public ResponseEntity<?> listaProveedor(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>( usuarioServicio.listaUsuario(token, null, UtilsDominio.TIPO_USUARIO_PROVEEDOR), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/proveedor/list")
    public ResponseEntity<?> listaProveedorPorCodigo(@RequestHeader(value="token") String token,
                                                     @RequestBody ServPatron patron) {
        return new ResponseEntity<>( usuarioServicio.listaUsuario(token, patron.getPatron(), UtilsDominio.TIPO_USUARIO_PROVEEDOR), HttpStatus.OK);
    }
}
