package bo.clync.pos.servicios.acceso;

import bo.clync.pos.model.ServResponse;
import bo.clync.pos.model.acceso.AccesoRequest;
import bo.clync.pos.model.acceso.AccesoResponse;

/**
 * Created by eyave on 29-10-17.
 */
public interface AccesoServicio {

        public AccesoResponse ingreso(AccesoRequest request);
        public ServResponse salir(String token);
}
