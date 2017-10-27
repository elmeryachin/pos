package bo.clync.pos;

import bo.clync.pos.model.articulo.lista.ServListaResponse;
import bo.clync.pos.model.articulo.obtener.ServObtenerResponse;
import bo.clync.pos.servicios.articulo.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.System.exit;

/**
 * Created by eyave on 26-10-17.
 */
//@SpringBootApplication
public class AppCommander  implements CommandLineRunner {

    @Autowired
    private Servicio service;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AppCommander.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        ServListaResponse r = service.lista(null);
        System.out.println(r.getLista().size());
        System.out.println(r.getMensaje());
        System.out.println(r.isRespuesta());
        ServObtenerResponse ro = service.obtener("C011", null);
        System.out.println(ro.isRespuesta());
        System.out.println(ro.getMensaje());
        System.out.println(ro.getArticulo().getDescripcion());
        exit(0);
    }


}
