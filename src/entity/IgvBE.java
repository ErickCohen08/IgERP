/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

/**
 *
 * @author ERCO
 */
public class IgvBE {
    int id_igv;
    int igv;
    String prederteminado;
    int id_empresa;
    int id_usuario;

    public int getId_igv() {
        return id_igv;
    }

    public void setId_igv(int id_igv) {
        this.id_igv = id_igv;
    }

    public int getIgv() {
        return igv;
    }

    public void setIgv(int igv) {
        this.igv = igv;
    }

    public String getPrederteminado() {
        return prederteminado;
    }

    public void setPrederteminado(String prederteminado) {
        this.prederteminado = prederteminado;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    
}
