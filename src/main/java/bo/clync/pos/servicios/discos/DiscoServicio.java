package bo.clync.pos.servicios.discos;

import bo.clync.pos.arquetipo.objetos.DiscoRequest;
import bo.clync.pos.arquetipo.objetos.DiscoResponse;


public interface DiscoServicio {

    DiscoResponse grabar(String token, DiscoRequest request);

    DiscoResponse leer(String token, Byte[] array);

    DiscoResponse recuperar(String token, DiscoRequest request);


}
