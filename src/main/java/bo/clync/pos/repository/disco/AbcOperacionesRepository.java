package bo.clync.pos.repository.disco;

import bo.clync.pos.arquetipo.objetos.ProcesoResumen;
import bo.clync.pos.arquetipo.tablas.AbcOperaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface AbcOperacionesRepository extends JpaRepository<AbcOperaciones, Integer> {

    //funcion por token y fechas dia hora minuto segundo.

    @Query(" SELECT o.operador, o.codigoAmbiente, o.proceso, o.fecha " +
            "  FROM AbcOperaciones o " +
            " WHERE o.token=:token " +
            " GROUP BY o.operador, o.codigoAmbiente, o.proceso, o.fecha " +
            " ORDER BY o.fecha asc ")
    List<Object[]> procesoPorToken(@Param("token") String token);

    @Query("SELECT o " +
            " FROM AbcOperaciones o " +
            "WHERE o.proceso=:proceso " +
            "ORDER BY o.id asc")
    List<AbcOperaciones> operacionesPorProceso(@Param("proceso") String proceso);

    List<AbcOperaciones> findAllByProceso(String proceso);

    List<AbcOperaciones> findAllByTokenAndProcesoIsNull(String token);
}
