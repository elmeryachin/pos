package bo.clync.pos.repository.disco;

import bo.clync.pos.arquetipo.tablas.AbcOperaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AbcOperacionesRepository extends JpaRepository<AbcOperaciones, Integer> {

    //funcion por token y fechas dia hora minuto segundo.

    @Transactional
    @Modifying
    @Query("UPDATE AbcOperaciones o " +
            " SET o.proceso =:proceso " +
            " WHERE o.token=:token " +
            "   AND o.proceso is null")
    void actualizar(@Param("token") String token, @Param("proceso") String proceso);

    @Query("SELECT o " +
            " FROM AbcOperaciones o " +
            "WHERE o.proceso=:proceso " +
            "ORDER BY o.id asc")
    List<AbcOperaciones> operacionesPorProceso(@Param("proceso") String proceso);

    List<AbcOperaciones> findAllByProceso(String proceso);

}
