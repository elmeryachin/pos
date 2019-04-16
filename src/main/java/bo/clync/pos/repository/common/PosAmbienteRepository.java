package bo.clync.pos.repository.common;

import bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin;
import bo.clync.pos.arquetipo.tablas.PosAmbiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by eyave on 25-11-17.
 */
public interface PosAmbienteRepository extends JpaRepository<PosAmbiente, String> {

    @Query("SELECT count(d)" +
            " FROM AdmDominio d, PosAmbiente a " +
            "WHERE d.codigo = 'SUCURSAL'" +
            "  AND a.tipoAmbiente = d.dominio " +
            "  AND a.codigo = :codigo")
    Integer verificarSucursal(@Param("codigo") String codigo);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin(o.codigo, o.nombre) " +
            " FROM PosAmbiente o " +
            "WHERE :valor = :valor" +
            "  AND o.codigo = :codigo " +
            "  AND o.codigo <> :codigoAmbiente")
    List<UsuarioResponseMin> obtenerAmbiente(@Param("codigoAmbiente") String codigoAmbiente,
                                              @Param("codigo") String codigo,
                                              @Param("valor") String valor);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin(o.codigo, o.nombre) " +
            " FROM PosAmbiente o " +
            "WHERE :valor = :valor" +
            "  AND o.codigo like :patron " +
            "  AND o.codigo <> :codigoAmbiente" )
    List<UsuarioResponseMin> listaAmbientePorPatron(@Param("codigoAmbiente") String codigoAmbiente,
                                                    @Param("patron") String patron,
                                                    @Param("valor") String valor);

    @Query("SELECT new bo.clync.pos.arquetipo.objetos.generic.UsuarioResponseMin(o.codigo, o.nombre) " +
            " FROM PosAmbiente o " +
            "WHERE :valor = :valor" +
            "  AND o.codigo <> :codigoAmbiente" )
    List<UsuarioResponseMin> listaAmbiente(@Param("codigoAmbiente") String codigoAmbiente,
                                            @Param("valor") String valor);
}
