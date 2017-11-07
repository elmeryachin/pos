package bo.clync.pos.repository.common;

import bo.clync.pos.entity.Dominio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by eyave on 28-10-17.
 */
public interface DominioRepository extends JpaRepository<Dominio, String> {
}
