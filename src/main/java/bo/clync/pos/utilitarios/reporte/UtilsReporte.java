package bo.clync.pos.utilitarios.reporte;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import java.io.*;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

/**
 * Created by eyave on 12-11-17.
 */
public class UtilsReporte {

    private String nombreArchivo = "/report.properties";
    private Properties properties = new Properties();
    private InputStream inputStream;
    private Map<String, Object> parameters;
    private Connection connection;

    private void getProperties() throws Exception{
        inputStream = getClass().getResourceAsStream(nombreArchivo);
        properties.load(inputStream);
    }

    public byte[] getPrint(String formato, String nombre, Map<String, Object> parameters, Connection connection) throws Exception{
        getProperties();
        String rutaReporte = this.properties.getProperty(nombre);
        System.out.println("rutaReporte : " + rutaReporte);
        this.inputStream = getClass().getResourceAsStream(rutaReporte);
        this.connection = connection;
        this.parameters = parameters;
        System.out.println(" inputStream " + inputStream);
        if(formato.equals(TipoDocumento.FORMAT_PDF)) return getPrintPdf();
        else if(formato.equals(TipoDocumento.FORMAT_XLS)) return getPrintXls();
        else if(formato.equals(TipoDocumento.FORMAT_XLS)) return getPrintTxt();

        return JasperRunManager.runReportToPdf(inputStream, parameters, connection);
    }

    private byte[] getPrintPdf() throws Exception {
        byte[] b= JasperRunManager.runReportToPdf(this.inputStream, this.parameters, this.connection);
        System.out.println("fin generacion " + b.length);
        return b;
    }

    private byte[] getPrintXls() throws Exception {

        JasperPrint jasperPrint = JasperFillManager.fillReport(this.inputStream, this.parameters, this.connection);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setOnePagePerSheet(true);
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        configuration.setWhitePageBackground(false);

        //File file = new File("listaArticulos.xls");
        //FileOutputStream fos = new FileOutputStream(file,true);

        JRXlsExporter exporterXLS = new JRXlsExporter();
        exporterXLS.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        exporterXLS.setConfiguration(configuration);
        exporterXLS.exportReport();

        return outputStream.toByteArray();
    }

    private byte[] getPrintTxt() throws Exception {
        return JasperRunManager.runReportToPdf(this.inputStream, this.parameters, this.connection);
    }

}
