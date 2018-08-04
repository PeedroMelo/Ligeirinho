package com.br.ligeirinho.models;

/**
 * Created by PC on 04/06/2018.
 */

public class PedidosBkp {
    private String id_pedido;
    private String id_usuario;
    private String origem;
    private String destino;
    private String origem_comp;
    private String destino_comp;
    private String detalhe;
    private String status;
    private String servico;

    public PedidosBkp(String id_pedido, String id_usuario, String origem, String destino, String origem_comp, String destino_comp, String detalhe, String status, String servico) {
        this.id_pedido    = id_pedido;
        this.id_usuario   = id_usuario;
        this.origem       = origem;
        this.destino      = destino;
        this.origem_comp  = origem_comp;
        this.destino_comp = destino_comp;
        this.detalhe      = detalhe;
        this.status       = status;
        this.servico      = servico;
    }

    public PedidosBkp(){

    }

    public String getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(String id_pedido) { this.id_pedido = id_pedido;  }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getOrigem_comp() {
        return origem_comp;
    }

    public void setOrigem_comp(String origem_comp) {
        this.origem_comp = origem_comp;
    }

    public String getDestino_comp() {
        return destino_comp;
    }

    public void setDestino_comp(String destino_comp) {
        this.destino_comp = destino_comp;
    }

    public String getDetalhe() {
        return detalhe;
    }

    public void setDetalhe(String detalhe) {
        this.detalhe = detalhe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }
}
