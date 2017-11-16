package bo.clync.pos.servicios.acceso;

import bo.clync.pos.dao.ServResponse;
import bo.clync.pos.dao.acceso.AccesoRequest;
import bo.clync.pos.dao.acceso.AccesoResponse;

/**
 * Created by eyave on 29-10-17.
 */
public interface AccesoServicio {

        public AccesoResponse ingreso(AccesoRequest request);
        public ServResponse salir(String token);
}
