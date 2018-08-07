package bo.clync.pos.repository.common;

import bo.clync.pos.arquetipo.tablas.AbcActualizador;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

public interface AbcActualizadorRepository extends JpaRepository<AbcActualizador, String> {

}
