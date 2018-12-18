package bo.clync.pos.ui.transaccion.venta;

import bo.clync.pos.arquetipo.objetos.ServPatron;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionRequest;
import bo.clync.pos.arquetipo.objetos.transaccion.generic.TransaccionResponse;
import bo.clync.pos.servicios.articulo.ArticuloServicio;
import bo.clync.pos.servicios.transaccion.venta.VentaServicio;
import bo.clync.pos.servicios.usuario.UsuarioServicio;
import bo.clync.pos.utilitarios.UtilsDominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaServicio service;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ArticuloServicio articuloServicio;

    @CrossOrigin
    @GetMapping("/init")
    public ResponseEntity<?> init(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(service.init(token), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<?> nuevo(@RequestHeader(value="token") String token,
                                   @RequestBody TransaccionRequest request,
                                   HttpServletRequest http) {
        TransaccionResponse response = null;
        try {
            response = service.adicionar(token, request, http);
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
            response = service.actualizar(token, request, http);
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
            response = service.eliminar(token, id, http);
        } catch (Exception e) {
            response = new ServResponse();
            response.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>( response , HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<?> lista(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(service.lista(token), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/quest/{id}")
    public ResponseEntity<?> obtener(@RequestHeader(value="token") String token,
                                     @PathVariable("id") String id) {
        return new ResponseEntity<>(this.service.obtener(token, id), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/quest/movimiento/{nro}")
    public ResponseEntity<?> obtenerPorNroMovimiento(@RequestHeader(value="token") String token,
                                                     @PathVariable("nro") String nro) {
        String id = service.getIdTransaccion(nro, token);
        System.out.println("Print id generado de nro mmovimiento " + id);
        return new ResponseEntity<>(this.service.obtener(token, id), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/confirmar/{id}")
    public ResponseEntity<?> confirmar(@RequestHeader(value="token") String token,
                                         @PathVariable("id") String id,
                                       HttpServletRequest http) throws Exception {
        return new ResponseEntity<>(this.service.confirmar(token, id, http), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/confirmar/list")
    public ResponseEntity<?> listaConfirmados(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(this.service.listaConfirmados(token), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/cliente/add")
    public ResponseEntity<ServResponse> nuevo(@RequestHeader(value="token") String token,
                                              @RequestBody UsuarioRequest request,
                                              HttpServletRequest http) {
        return new ResponseEntity<>(usuarioServicio.nuevoUsuario(token, request, UtilsDominio.TIPO_USUARIO_CLIENTE, http), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/cliente/quest/{codigo}")
    public ResponseEntity<?> obtenerCliente(@RequestHeader(value="token") String token,
                                              @PathVariable("codigo") String codigo) {
        return new ResponseEntity<>( usuarioServicio.obtenerUsuario(token, codigo, UtilsDominio.TIPO_USUARIO_CLIENTE), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/cliente/list")
    public ResponseEntity<?> listaClientes(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>( usuarioServicio.listaUsuario(token, null, UtilsDominio.TIPO_USUARIO_CLIENTE), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/cliente/list")
    public ResponseEntity<?> listaClientesPorCodigo(@RequestHeader(value="token") String token,
                                                    @RequestBody ServPatron patron) {
        return new ResponseEntity<>( usuarioServicio.listaUsuario(token, patron.getPatron(), UtilsDominio.TIPO_USUARIO_CLIENTE), HttpStatus.OK);
    }
}
