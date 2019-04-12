package bo.clync.pos.repository.disco;

import bo.clync.pos.arquetipo.tablas.GesOperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AbcOperacionesRepository extends JpaRepository<GesOperacion, Integer> {

    //funcion por token y fechas dia hora minuto segundo.

    @Query(" SELECT o.operador, o.codigoAmbiente, o.proceso, o.fecha " +
            "  FROM GesOperacion o " +
            " WHERE o.token=:token " +
            " GROUP BY o.operador, o.codigoAmbiente, o.proceso, o.fecha " +
            " ORDER BY o.fecha asc ")
    List<Object[]> procesoPorToken(@Param("token") String token);

    @Query("SELECT o " +
            " FROM GesOperacion o " +
            "WHERE o.proceso=:proceso " +
            "ORDER BY o.id asc")
    List<GesOperacion> operacionesPorProceso(@Param("proceso") String proceso);

    List<GesOperacion> findAllByProceso(String proceso);

    List<GesOperacion> findAllByTokenAndProcesoIsNull(String token);
}
