package bo.clync.pos.repository.common;

import bo.clync.pos.arquetipo.tablas.Dominio;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by eyave on 28-10-17.
 */
public interface DominioRepository extends JpaRepository<Dominio, String> {
}
