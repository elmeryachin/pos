package bo.clync.pos.servicios.acceso;

import bo.clync.pos.arquetipo.objetos.ServResponse;
import bo.clync.pos.arquetipo.objetos.acceso.AccesoRequest;
import bo.clync.pos.arquetipo.objetos.acceso.AccesoResponse;

/**
 * Created by eyave on 29-10-17.
 */
public interface AccesoServicio {

        public AccesoResponse ingreso(AccesoRequest request);
}
