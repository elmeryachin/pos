package bo.clync.pos.ui;


import bo.clync.pos.arquetipo.objetos.ServPatron;
import bo.clync.pos.servicios.ambiente.AmbienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ambiente")
public class AmbienteController {
    @Autowired
    private AmbienteServicio  servicio;

    @CrossOrigin
    @GetMapping("/quest/{codigo}")
    public ResponseEntity<?> obtenerAmbiente(@RequestHeader(value="token") String token,
                                             @PathVariable("codigo") String codigo) {
        return new ResponseEntity<>(servicio.obtenerSucursal(token, codigo), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<?> listaAmbiente(@RequestHeader(value="token") String token) {
        return new ResponseEntity<>(servicio.listaSurcursal(token, null), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/list")
    public ResponseEntity<?> listaAmbientePorPatron(@RequestHeader(value="token") String token,
                                                    @RequestBody ServPatron patron) {
        return new ResponseEntity<>(servicio.listaSurcursal(token, patron.getPatron()), HttpStatus.OK);
    }

}
