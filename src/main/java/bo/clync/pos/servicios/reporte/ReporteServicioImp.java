package bo.clync.pos.servicios.reporte;

import bo.clync.pos.entity.Ambiente;
import bo.clync.pos.repository.acceso.UsuarioAmbienteCredencialRepository;
import bo.clync.pos.repository.common.AmbienteRepository;
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
    private UsuarioAmbienteCredencialRepository credencialRepository;
    @Autowired
    private AmbienteRepository ambienteRepository;
    @Override
    public byte[] reporteArticulos(String token, String nombre, String format) {
        Connection connection = null;
        Map<String, Object> parameters = null;
        try {
            Object[] arrayId = (Object[]) credencialRepository.getIdUsuarioByToken(token);
            if(arrayId != null) {
                //Integer idUsuario = (Integer) arrayId[0];
                String codigoAmbiente = (String) arrayId[1];
                Ambiente ambiente = ambienteRepository.findOne(codigoAmbiente);
                parameters = new HashMap<>();
                parameters.put("ambiente-codigo",ambiente.getNombre() + "(" + ambiente.getCodigo() + ")");
                connection = getConnection();
                return new UtilsReporte().getPrint(format, nombre, parameters, connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {connection.close(); } catch (Exception e){}
            System.out.println("Proceso terminado....");
        }
        return new byte[0];
    }

    private Connection getConnection() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/application.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        String url      = properties.getProperty("spring.datasource.url");
        String user     = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");
        return DriverManager.getConnection(url, user, password);
    }

    private Connection getConnectionBackup() throws SQLException {
        System.out.println("::: " + entityManager.unwrap(Connection.class));
        org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean s=null;
        if(entityManager.unwrap(Connection.class) != null) {
            return (Connection) this.entityManager.unwrap(Connection.class);
        } else {
            Map dc = entityManager.getProperties();
            String url = (String) dc.get("javax.persistence.jdbc.url");
            String user = (String) dc.get("javax.persistence.jdbc.user");
            String password = (String) dc.get("javax.persistence.jdbc.password");
            return DriverManager.getConnection(url, user, password);
        }
    }
}
