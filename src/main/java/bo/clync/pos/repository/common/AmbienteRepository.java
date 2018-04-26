package bo.clync.pos.repository.common;

import bo.clync.pos.entity.Ambiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by eyave on 25-11-17.
 */
public interface AmbienteRepository extends JpaRepository<Ambiente, String> {

    @Query("SELECT count(d)" +
            " FROM Dominio d, Valor v, Ambiente a " +
            "WHERE d.dominio = v.codigoDominio " +
            "  AND v.valor = 'SUCURSAL'" +
            "  AND a.codigo = :codigo")
    Integer verificarSucursal(@Param("codigo") String codigo);

}
