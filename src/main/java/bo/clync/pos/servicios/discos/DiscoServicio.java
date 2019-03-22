package bo.clync.pos.servicios.discos;

import bo.clync.pos.arquetipo.objetos.DiscoProcesoResponse;
import bo.clync.pos.arquetipo.objetos.DiscoRequest;
import bo.clync.pos.arquetipo.objetos.DiscoResponse;
import bo.clync.pos.arquetipo.tablas.AbcOperaciones;

import java.util.Date;
import java.util.List;


public interface DiscoServicio {

    DiscoProcesoResponse listaProcesos(String token);

    void guardarOperaciones(AbcOperaciones operaciones) throws Exception;

    DiscoResponse recuperar(String token, String proceso);

    boolean verificarProcesoExterno(String proceso);

    List<AbcOperaciones> getGrabarCodigoProceso(String token, String proceso, Date fecha);
}
