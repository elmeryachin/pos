package bo.clync.pos.servicios.reporte;

import bo.clync.pos.arquetipo.dto.DatosUsuario;
import bo.clync.pos.arquetipo.tablas.PosAmbiente;
import bo.clync.pos.repository.common.AdmCredencialRepository;
import bo.clync.pos.repository.common.PosAmbienteRepository;
import bo.clync.pos.utilitarios.reporte.UtilsReporte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by eyave on 12-11-17.
 */
@Service
@Transactional
public class ReporteServicioImp implements ReporteServicio {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AdmCredencialRepository credencialRepository;
    @Autowired
    private PosAmbienteRepository posAmbienteRepository;
    @Override
    public Object reporteArticulos(String token, String nombre, String format) {

        Connection connection = null;
        Map<String, Object> parameters = null;

        try {
            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            PosAmbiente posAmbiente = posAmbienteRepository.findOne(datosUsuario.getCodigoAmbiente());

            parameters = getParametros(posAmbiente, null);

            connection = getConnection();

            return new UtilsReporte().getPrint(format, nombre, parameters, connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {connection.close(); } catch (Exception e){}
            System.out.println("Proceso terminado....");
        }
        return new byte[0];

    }

    @Override
    public Object reporteArticulos(String token, String nombre, String format, String id) {
        Connection connection = null;
        Map<String, Object> parameters = null;
        try {
            DatosUsuario datosUsuario = credencialRepository.getDatosUsuario(token);

            PosAmbiente posAmbiente = posAmbienteRepository.findOne(datosUsuario.getCodigoAmbiente());

            parameters = getParametros(posAmbiente, id);

            connection = getConnection();

            return new UtilsReporte().getPrint(format, nombre, parameters, connection);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {connection.close(); } catch (Exception e){}
            System.out.println("Proceso terminado....");
        }
        return new byte[0];

    }

    private HashMap getParametros(PosAmbiente posAmbiente, String id) {
        HashMap parameters = new HashMap();
        parameters.put("ambiente-codigo", posAmbiente.getTipoAmbiente() + ": " + posAmbiente.getNombre() + " (" + posAmbiente.getCodigo() + ")");
        parameters.put("codigo-ambiente", posAmbiente.getCodigo());
        parameters.put("id-transaccion", id);
        return parameters;
    }

    private Connection getConnection() throws Exception {

        Class.forName("org.postgresql.Driver");
        //"jdbc:postgresql://ec2-54-163-245-14.compute-1.amazonaws.com:5432/d6ih3rc9tcudf5";
        InputStream inputStream = getClass().getResourceAsStream("/application.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        String url      = properties.getProperty("test.source.url");
        String user     = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");
        return DriverManager.getConnection(url,user, password);
    }

}
