package bo.clync.pos.modulo.articulo.entity;

import bo.clync.pos.modulo.articulo.entity.obtener.ObjetoArticulo;

/**
 * Created by eyave on 09-10-17.
 */
public class ServRequest {
    private ObjetoArticulo objetoArticulo;

    public ServRequest(){}

    public ObjetoArticulo getObjetoArticulo() {
        return objetoArticulo;
    }

    public void setObjetoArticulo(ObjetoArticulo objetoArticulo) {
        this.objetoArticulo = objetoArticulo;
    }
}
