package bo.clync.pos.ui;

import bo.clync.pos.servicios.reporte.ReporteServicio;
import bo.clync.pos.utilitarios.reporte.TipoDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by eyave on 24-11-17.
 */
@RestController
@RequestMapping("/reporte")
public class ReporteController {

    @Autowired
    private ReporteServicio service;

    @CrossOrigin
    @GetMapping(value = "/{nombre}/pdf/download", produces = {"application/pdf"})
    public ResponseEntity<?> pdfDownload(@RequestHeader(value="token") String token,
                                         @PathVariable("nombre") String nombre) {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"my-pdf-file.pdf\"")
                .body(service.reporteArticulos(token, nombre, TipoDocumento.FORMAT_PDF));
    }

    @CrossOrigin
    @GetMapping(value = "/{nombre}/xls/download", produces = {"application/vnd.ms-excel"})
    public ResponseEntity<?> xlsDownload(@RequestHeader(value="token") String token,
                                         @PathVariable("nombre") String nombre) {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"my-xls-file.xls\"")
                .body(service.reporteArticulos(token, nombre, TipoDocumento.FORMAT_XLS));
    }

    @CrossOrigin
    @GetMapping(value = "/{nombre}/pdf/view", produces = {"application/pdf"})
    public ResponseEntity<?> pdfView(@RequestHeader(value="token") String token,
                                     @PathVariable("nombre") String nombre) {
        return new ResponseEntity<>(service.reporteArticulos(token, nombre, TipoDocumento.FORMAT_PDF), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/{nombre}/xls/view", produces = {"application/vnd.ms-excel"})
    public ResponseEntity<?> xlsView(@RequestHeader(value="token") String token,
                                     @PathVariable("nombre") String nombre) {
        return new ResponseEntity<>(service.reporteArticulos(token, nombre, TipoDocumento.FORMAT_XLS), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/{nombre}/html/view", produces = {"text/plain"})
    public ResponseEntity<?> htmlView(@RequestHeader(value="token") String token,
                                      @PathVariable("nombre") String nombre) {
        return new ResponseEntity<>(service.reporteArticulos(token, nombre, TipoDocumento.FORMAT_HTML), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/{nombre}/pdf/view/{id}", produces = {"application/pdf"})
    public ResponseEntity<?> pdfViewId(@RequestHeader(value="token") String token,
                                       @PathVariable("nombre") String nombre,
                                       @PathVariable("id") String id) {
        return new ResponseEntity<>(service.reporteArticulos(token, nombre, TipoDocumento.FORMAT_PDF, id), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/{nombre}/xls/view/{id}", produces = {"application/vnd.ms-excel"})
    public ResponseEntity<?> xlsViewId(@RequestHeader(value="token") String token,
                                       @PathVariable("nombre") String nombre,
                                       @PathVariable("id") String id) {
        return new ResponseEntity<>(service.reporteArticulos(token, nombre, TipoDocumento.FORMAT_XLS, id), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/{nombre}/html/view/{id}", produces = {"text/plain"})
    public ResponseEntity<?> htmlViewId(@RequestHeader(value="token") String token,
                                        @PathVariable("nombre") String nombre,
                                       @PathVariable("id") String id) {
        return new ResponseEntity<>(service.reporteArticulos(token, nombre, TipoDocumento.FORMAT_HTML, id), HttpStatus.OK);
    }

}
