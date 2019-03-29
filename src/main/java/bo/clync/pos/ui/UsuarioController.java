package bo.clync.pos.ui;

import bo.clync.pos.arquetipo.objetos.ServPatron;
import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.generic.UsuarioRequest;
import bo.clync.pos.servicios.usuario.UsuarioServicio;
import bo.clync.pos.utilitarios.UtilsDominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioServicio servicio;

    @CrossOrigin
    @PostMapping("/proveedor/add")
    public ResponseEntity<ServResponse> nuevo(@RequestHeader(value="token") String token,
                                              @RequestBody UsuarioRequest request,
                                              HttpServletRequest http) {
        return new ResponseEntity<>(servicio.nuevoUsuario(token, request, UtilsDominio.TIPO_USUARIO_PROVEEDOR, http), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/proveedor/quest/{codigo}")
    public ResponseEntity<?> obtenerProveedor(@RequestHeader(value="token") String token,
                                              @PathVariable("codigo") String codigo) {
        return new ResponseEntity<>( servicio.obtenerUsuario(token, codigo, UtilsDominio.TIPO_USUARIO_PROVEEDOR), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/proveedor/list")
    public ResponseEntity<?> listaProveedorPorCodigo(@RequestHeader(value="token") String token,
                                                     @RequestBody ServPatron patron) {
        return new ResponseEntity<>( servicio.listaUsuario(token, patron.getPatron(), UtilsDominio.TIPO_USUARIO_PROVEEDOR), HttpStatus.OK);
    }


    @CrossOrigin
    @PostMapping("/cliente/add")
    public ResponseEntity<ServResponse> nuevoCliente(@RequestHeader(value="token") String token,
                                              @RequestBody UsuarioRequest request,
                                              HttpServletRequest http) {
        return new ResponseEntity<>(servicio.nuevoUsuario(token, request, UtilsDominio.TIPO_USUARIO_CLIENTE, http), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/cliente/quest/{codigo}")
    public ResponseEntity<?> obtenerCliente(@RequestHeader(value="token") String token,
                                            @PathVariable("codigo") String codigo) {
        return new ResponseEntity<>( servicio.obtenerUsuario(token, codigo, UtilsDominio.TIPO_USUARIO_CLIENTE), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/cliente/list")
    public ResponseEntity<?> listaClientesPorCodigo(@RequestHeader(value="token") String token,
                                                    @RequestBody ServPatron patron) {
        return new ResponseEntity<>( servicio.listaUsuario(token, patron.getPatron(), UtilsDominio.TIPO_USUARIO_CLIENTE), HttpStatus.OK);
    }
}
