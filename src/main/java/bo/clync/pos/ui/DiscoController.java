package bo.clync.pos.ui;

import bo.clync.pos.arquetipo.objetos.DiscoRequest;
import bo.clync.pos.arquetipo.objetos.DiscoResponse;
import bo.clync.pos.arquetipo.tablas.AbcOperaciones;
import bo.clync.pos.servicios.discos.DiscoServicio;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/discos")
public class DiscoController {

    @Autowired
    private DiscoServicio service;

    private String tmpdir = "/home/eyave/Desktop/";//System.getProperty("java.io.tmpdir");

    @CrossOrigin
    @RequestMapping(value="/procesos", method = RequestMethod.GET)
    public ResponseEntity<?> grabando(@RequestHeader(value="token") String token) throws Exception {
        return new ResponseEntity<>( this.service.listaProcesos(token), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value="/grabando", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> grabando(@RequestHeader(value="token") String token,
                                      @RequestParam("file") MultipartFile file) throws Exception {
        try {
            String nombreCompleto = file.getOriginalFilename();
            String nombre = file.getName();

            if (service.verificarProcesoExterno(nombre)) {
                throw new Exception("El proceso ya existe");
            }

            File uploadedFile = new File(tmpdir, nombreCompleto);
            uploadedFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(uploadedFile);
            fileOutputStream.write(file.getBytes());

            ZipFile zipFile = new ZipFile(uploadedFile);

            zipFile.setPassword("elmeryachin");

            List list = zipFile.getFileHeaders();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < list.size(); i++) {
                FileHeader f = (FileHeader) list.get(i);
                String fileName = f.getFileName();
                if (fileName.contains(".json")) {
                    File destino = new File("destino.json");
                    ZipInputStream is = zipFile.getInputStream(f);

                    OutputStream out = new FileOutputStream(destino);

                    int readLen = -1;
                    while ((readLen = is.read()) != -1) {
                        out.write(readLen);
                    }

                    is.close();
                    out.close();

                    FileReader fr = new FileReader(destino);
                    BufferedReader br = new BufferedReader(fr);
                    String sCurrentLine;
                    while ((sCurrentLine = br.readLine()) != null) {
                        sb.append(sCurrentLine);
                    }
                    br.close();
                    fr.close();
                    destino.deleteOnExit();
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            ArrayList<AbcOperaciones> lst = mapper.readValue(sb.toString(),
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, AbcOperaciones.class));

            String http = "http://localhost:8080";
            String tokenProceso = null;
            Date fecha = null;
            for (int i = 0; i < lst.size(); i++) {
                AbcOperaciones operaciones = lst.get(i);
                tokenProceso = operaciones.getToken();
                fecha = operaciones.getFecha();
                String url = http + operaciones.getUrl();
                if (operaciones.getTipo().equals("POST")) {
                    getEnviarPeticionPost(url, operaciones.getJson(), operaciones.getToken());
                } else if (operaciones.getTipo().equals("PUT")) {
                    getEnviarPeticionPut(url, operaciones.getJson(), operaciones.getToken());
                } else if (operaciones.getTipo().equals("DELETE")) {
                    getEnviarPeticionDelete(url, operaciones.getToken());
                }
                System.out.println("c $$$$$$$$$$$$$$$$$$  " + i);
                System.out.println("" + operaciones.getId());
                System.out.println(operaciones.getProceso());
                System.out.println(operaciones.getJson());
                System.out.println(operaciones.getTipo());
                System.out.println(operaciones.getToken());
                System.out.println(operaciones.getUrl());
            }

            //Se guardar el id proceso en los registros segun el token
            service.getGrabarCodigoProceso(tokenProceso, nombre, fecha);

            return new ResponseEntity<>("Se actualizo exitosamente la base de datos", HttpStatus.OK);
        } catch (Exception e ) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.OK);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/generar", method = RequestMethod.GET)
    public ResponseEntity<Object> generar(@RequestHeader(value="token") String token,
                                          @RequestParam(value = "proceso", defaultValue = "") String proceso) throws Exception {

        DiscoResponse response = this.service.recuperar( token , proceso);
        ObjectMapper mapper = new ObjectMapper();

        String nombreFile = response.getNombre() + ".json";
        File file = new File(nombreFile);
        mapper.writeValue(file, response.getList());

        String nombreZip = tmpdir + response.getNombre() + ".zip";
        File fZip = new File(nombreZip);
        ZipFile zipFile = new ZipFile(fZip);
        ArrayList<File> fileToAdd = new ArrayList<File>();
        fileToAdd.add(file);

        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        parameters.setPassword("elmeryachin");
        zipFile.addFiles(fileToAdd, parameters);

        response.setArray(Files.readAllBytes(zipFile.getFile().toPath()));
        response.setList(null);
        response.setRuta(tmpdir);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean getEnviarPeticionPost(String url, String json, String token) {
        boolean respuesta = false;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(url);

            if(json != null) {
                StringEntity input = new StringEntity(json);
                input.setContentType("application/json");
                postRequest.setEntity(input);
            }
            postRequest.setHeader("token",token);
            HttpResponse response = httpClient.execute(postRequest);

            int code = response.getStatusLine().getStatusCode();
            System.out.println("code ::::: "  + code);

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String output;
            while((output = br.readLine()) != null) {
                System.out.println("Output:::: " + output);
            }

            httpClient.getConnectionManager().shutdown();

            respuesta = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    private boolean getEnviarPeticionPut(String url, String json, String token) {
        boolean respuesta = false;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPut putRequest = new HttpPut(url);

            if(json != null) {
                StringEntity input = new StringEntity(json);
                input.setContentType("application/json");
                putRequest.setEntity(input);
            }
            putRequest.setHeader("token",token);

            HttpResponse response = httpClient.execute(putRequest);

            int code = response.getStatusLine().getStatusCode();
            System.out.println("code ::::: "  + code);

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String output;
            while((output = br.readLine()) != null) {
                System.out.println("Output:::: " + output);
            }

            httpClient.getConnectionManager().shutdown();

            respuesta = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    private boolean getEnviarPeticionDelete(String url, String token) {
        boolean respuesta = false;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpDelete deleteRequest = new HttpDelete(url);

            deleteRequest.setHeader("token",token);

            HttpResponse response = httpClient.execute(deleteRequest);

            int code = response.getStatusLine().getStatusCode();
            System.out.println("code ::::: "  + code);

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String output;
            while((output = br.readLine()) != null) {
                System.out.println("Output:::: " + output);
            }

            httpClient.getConnectionManager().shutdown();

            respuesta = true;

        } catch (Exception e) {

        }
        return respuesta;
    }

}
