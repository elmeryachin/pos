package bo.clync.pos.servicios.discos;

import bo.clync.pos.arquetipo.objetos.DiscoRequest;
import bo.clync.pos.arquetipo.objetos.DiscoResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class DiscoServicioImpl implements DiscoServicio {


    @Override
    public DiscoResponse grabar(String token, DiscoRequest request) {
        return null;
    }

    @Override
    public DiscoResponse leer(String token, Byte[] array) {
        return null;
    }

    @Override
    public DiscoResponse recuperar(String token, DiscoRequest request) {
        return null;
    }
}
