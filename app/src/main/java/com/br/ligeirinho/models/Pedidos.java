package com.br.ligeirinho.models;

import android.util.ArrayMap;

/**
 * Created by PC on 04/06/2018.
 */

public class Pedidos {
    private String id_pedido;
    private String id_usuario;
    private String detalhes;
    private String status;
    private String servico;
    private String tempoEntrega;
    private String distancia;
    private String valor;
    private Origem origem;
    private Destino destino;



    public Pedidos(String id_pedido, String id_usuario, String detalhes, String status, String servico, String tempoEntrega, String distancia, String valor) {
        this.id_pedido    = id_pedido;
        this.id_usuario   = id_usuario;
        this.detalhes      = detalhes;
        this.status       = status;
        this.servico      = servico;
        this.tempoEntrega = tempoEntrega;
        this.distancia    = distancia;
        this.valor        = valor;
    }

    public Pedidos(){

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

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
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

    public String getTempoEntrega() {  return tempoEntrega;   }

    public void setTempoEntrega(String tempoEntrega) {
        this.tempoEntrega = tempoEntrega;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
