package com.br.ligeirinho.models;

/**
 * Created by PC on 09/06/2018.
 */

public class Destino {
    private String endereco;
    private String complemento;
    private String latlng;
    private String lat;
    private String lng;

    public Destino(String endereco, String complemento, String latlng, String lat, String lng) {
        this.endereco = endereco;
        this.complemento = complemento;
        this.latlng = latlng;
        this.lat = lat;
        this.lng = lng;
    }

    public Destino(){

    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
