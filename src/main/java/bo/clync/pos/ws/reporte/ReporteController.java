package bo.clync.pos.ws.reporte;

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

    private String token = "20171029130500-1-1";


    @CrossOrigin
    @GetMapping(value = "/{nombre}/pdf/download", produces = {"application/pdf"})
    public ResponseEntity<?> pdfDownload(@PathVariable("nombre") String nombre) {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"my-pdf-file.pdf\"")
                .body(service.reporteArticulos(token, nombre, TipoDocumento.FORMAT_PDF));
    }


    @CrossOrigin
    @GetMapping(value = "/{nombre}/xls/download", produces = {"application/vnd.ms-excel"})
    public ResponseEntity<?> xlsDownload(@PathVariable("nombre") String nombre) {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"my-xls-file.xls\"")
                .body(service.reporteArticulos(token, nombre, TipoDocumento.FORMAT_XLS));
    }

    @CrossOrigin
    @GetMapping(value = "/{nombre}/pdf/view", produces = {"application/pdf"})
    public ResponseEntity<?> pdfView(@PathVariable("nombre") String nombre) {
        return new ResponseEntity<>(service.reporteArticulos(token, nombre, TipoDocumento.FORMAT_PDF), HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping(value = "/{nombre}/xls/view", produces = {"application/vnd.ms-excel"})
    public ResponseEntity<?> xlsView(@PathVariable("nombre") String nombre) {
        return new ResponseEntity<>(service.reporteArticulos(token, nombre, TipoDocumento.FORMAT_XLS), HttpStatus.OK);
    }

}
