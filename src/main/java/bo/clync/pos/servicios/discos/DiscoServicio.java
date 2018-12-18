package bo.clync.pos.servicios.discos;

import bo.clync.pos.arquetipo.objetos.DiscoRequest;
import bo.clync.pos.arquetipo.objetos.DiscoResponse;
import bo.clync.pos.arquetipo.tablas.AbcOperaciones;


public interface DiscoServicio {

    void guardarOperaciones(AbcOperaciones operaciones) throws Exception;

    //DiscoResponse grabar(String token, DiscoRequest request);

    //DiscoResponse leer(String token, Byte[] array);

    DiscoResponse recuperar(String token, String proceso);

    boolean verificarProcesoExterno(String proceso);

    boolean getGrabarCodigoProceso(String token, String proceso);
}
