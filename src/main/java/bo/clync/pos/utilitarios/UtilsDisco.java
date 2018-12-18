package bo.clync.pos.utilitarios;

import bo.clync.pos.arquetipo.tablas.AbcOperaciones;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class UtilsDisco {

    public static AbcOperaciones getOperaciones(HttpServletRequest http, Object objecto, String token) throws Exception {
        String json = null;
        AbcOperaciones operaciones = null;
        try {
            if (objecto != null)
                json = new ObjectMapper().writeValueAsString(objecto);

            operaciones = new AbcOperaciones();
            //Integer id = 1;
            operaciones.setId(null);
            operaciones.setTipo(http.getMethod());
            operaciones.setUrl(http.getRequestURI());
            operaciones.setJson(json);
            operaciones.setToken(token) ;
            operaciones.setProceso(null);
        } catch (Exception e) { e.printStackTrace(); }
        return operaciones;
    }



    public static void zip(String sourceFile, String fileZip) throws IOException {
        //String sourceFile = "C:\\Users\\eyave\\Desktop\\misarchivos";
        //String fileZip = "C:\\Users\\eyave\\Desktop\\source.zip";
        FileOutputStream fos = new FileOutputStream(fileZip);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(sourceFile);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    public static void unZip(String fileZip, String outDir) throws IOException {
        //String fileZip = "C:\\Users\\eyave\\Desktop\\source.zip";
        //Striong outDir = "C:\\Users\\eyave\\Desktop\\unzipTest\\";
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while(zipEntry != null){
            String fileName = zipEntry.getName();
            File newFile = new File(outDir + fileName);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }
}
