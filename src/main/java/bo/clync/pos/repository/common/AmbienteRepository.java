package bo.clync.pos.repository.common;

import bo.clync.pos.arquetipo.objetos.generic.AmbienteResponseMin;
import bo.clync.pos.arquetipo.tablas.Ambiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by eyave on 25-11-17.
 */
public interface AmbienteRepository extends JpaRepository<Ambiente, String> {

    @Query("SELECT count(d)" +
            " FROM Dominio d, Valor v, Ambiente a " +
            "WHERE d.dominio = v.codigoDominio " +
            "  AND v.valor = 'SUCURSAL'" +
            "  AND a.tipoAmbiente = v.valor " +
            "  AND a.codigo = :codigo")
    Integer verificarSucursal(@Param("codigo") String codigo);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.generic.AmbienteResponseMin(o.codigo, o.nombre) " +
            " FROM Ambiente o " +
            "WHERE :valor = :valor" +
            "  AND o.codigo = :codigo " +
            "  AND o.codigo <> :codigoAmbiente")
    List<AmbienteResponseMin> obtenerAmbiente(@Param("codigoAmbiente") String codigoAmbiente,
                                              @Param("codigo") String codigo,
                                              @Param("valor") String valor);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.generic.AmbienteResponseMin(o.codigo, o.nombre) " +
            " FROM Ambiente o " +
            "WHERE :valor = :valor" +
            "  AND o.codigo like :patron " +
            "  AND o.codigo <> :codigoAmbiente" )
    List<AmbienteResponseMin> listaAmbientePorPatron(@Param("codigoAmbiente") String codigoAmbiente,
                                                     @Param("patron") String patron,
                                                     @Param("valor") String valor);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.generic.AmbienteResponseMin(o.codigo, o.nombre) " +
            " FROM Ambiente o " +
            "WHERE :valor = :valor" +
            "  AND o.codigo <> :codigoAmbiente" )
    List<AmbienteResponseMin> listaAmbiente(@Param("codigoAmbiente") String codigoAmbiente,
                                            @Param("valor") String valor);
}
