package bo.clync.pos.servicios.discos;

import bo.clync.pos.arquetipo.objetos.DiscoRequest;
import bo.clync.pos.arquetipo.objetos.DiscoResponse;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Map;

@Service
public class DiscoServicioImpl implements DiscoServicio {


    /*private DiscoResponse nuevaSucursal() {
        return null;
    }*/

    @Override
    public DiscoResponse grabar(String token, DiscoRequest request) {
        /*Firma firma = new Firma();
        List<Ambiente> listaAmbiente = new ArrayList<Ambiente>();
        List<Ciclo> listaCiclo = new ArrayList<Ciclo>();
        List<Inventario> listInventario = new ArrayList<Inventario>();
        List<Usuario> listUsuario = new ArrayList<Usuario>();
        List<UsuarioAmbienteCredencial> listUsuarioAmbienteCredencial = new ArrayList<UsuarioAmbienteCredencial>();
        List<Articulo> listArticulo = new ArrayList<Articulo>();
        List<Transaccion> listaTransaccion = new ArrayList<Transaccion>();
        List<DetalleTransaccion> listaDetalleTransaccion = new ArrayList<DetalleTransaccion>();*/
        return null;
    }

    @Override
    public DiscoResponse leer(String token, Byte[] array) {
        return null;
    }
/*
    private Object mapearObjetoDesdeJson(File file ) {//new File("employee.json")
        ObjectMapper objectMapper = new ObjectMapper();
        Object emp = objectMapper.readValue(file, Object.class);
        logger.info(emp.toString());
        return emp;
    }*/


    @Override
    public DiscoResponse recuperar(String token, DiscoRequest request) {
        return null;
    }
}
