package bo.clync.pos.repository.common;

import bo.clync.pos.entity.Ciclo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by eyave on 28-10-17.
 */
public interface CicloRepository extends JpaRepository<Ciclo, Integer> {

    @Query("SELECT o.id FROM Ciclo o WHERE o.fechaBaja is null")
    public Integer getIdCiclo();

    @Query("SELECT o.fechaInicio FROM Ciclo o WHERE o.fechaBaja is null")
    public java.util.Date getFechaInicio();

}
