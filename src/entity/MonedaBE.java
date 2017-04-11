/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.util.Date;

/**
 *
 * @author ERCO
 */
public class MonedaBE {
    
    int id_moneda;
    String nombre;
    String simbolo;
    String moneda_local;
    int id_empresa;
    int id_usuario;
    Date f_creacion;
    Date f_modificacion;
    
    String usuarioDes;
    
    public MonedaBE() {
        
    }

    public MonedaBE(String nombre, String simbolo, String moneda_local, int id_empresa, int id_usuario) {
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.moneda_local = moneda_local;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public MonedaBE(int id_moneda, String nombre, String simbolo, String moneda_local, int id_empresa, int id_usuario, Date f_creacion, Date f_modificacion, String usuarioDes) {
        this.id_moneda = id_moneda;
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.moneda_local = moneda_local;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
        this.f_creacion = f_creacion;
        this.f_modificacion = f_modificacion;
        this.usuarioDes = usuarioDes;
    }

    public MonedaBE(int id_moneda, String nombre, String simbolo, String moneda_local) {
        this.id_moneda = id_moneda;
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.moneda_local = moneda_local;
    }

    
    
    public int getId_moneda() {
        return id_moneda;
    }

    public void setId_moneda(int id_moneda) {
        this.id_moneda = id_moneda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getMoneda_local() {
        return moneda_local;
    }

    public void setMoneda_local(String moneda_local) {
        this.moneda_local = moneda_local;
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

    public Date getF_creacion() {
        return f_creacion;
    }

    public void setF_creacion(Date f_creacion) {
        this.f_creacion = f_creacion;
    }

    public Date getF_modificacion() {
        return f_modificacion;
    }

    public void setF_modificacion(Date f_modificacion) {
        this.f_modificacion = f_modificacion;
    }

    public String getUsuarioDes() {
        return usuarioDes;
    }

    public void setUsuarioDes(String usuarioDes) {
        this.usuarioDes = usuarioDes;
    }

    
    
    
    
    
    
}
