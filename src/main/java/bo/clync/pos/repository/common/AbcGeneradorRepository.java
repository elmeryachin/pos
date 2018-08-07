package bo.clync.pos.repository.common;

import bo.clync.pos.arquetipo.tablas.AbcGenerador;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;

public interface AbcGeneradorRepository extends JpaRepository<AbcGenerador, String> {

}
