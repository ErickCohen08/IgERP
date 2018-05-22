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
public class NivelBE {

    int IdNivel;
    int IdStand;
    String nombre;
    String usuarioInserta;
    Date fechaInserta;
    String usuarioModifica;
    Date fechaModifica;
    int idEmpresa;

    String usuarioDes;
    String nombreStand;
    String nombreAlmacen;
    int idAlmacen;

    public NivelBE() {

    }

    public int getIdNivel() {
        return IdNivel;
    }

    public void setIdNivel(int IdNivel) {
        this.IdNivel = IdNivel;
    }

    public int getIdStand() {
        return IdStand;
    }

    public void setIdStand(int IdStand) {
        this.IdStand = IdStand;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuarioInserta() {
        return usuarioInserta;
    }

    public void setUsuarioInserta(String usuarioInserta) {
        this.usuarioInserta = usuarioInserta;
    }

    public Date getFechaInserta() {
        return fechaInserta;
    }

    public void setFechaInserta(Date fechaInserta) {
        this.fechaInserta = fechaInserta;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getUsuarioDes() {
        return usuarioDes;
    }

    public void setUsuarioDes(String usuarioDes) {
        this.usuarioDes = usuarioDes;
    }

    public String getNombreStand() {
        return nombreStand;
    }

    public void setNombreStand(String nombreStand) {
        this.nombreStand = nombreStand;
    }

    public String getNombreAlmacen() {
        return nombreAlmacen;
    }

    public void setNombreAlmacen(String nombreAlmacen) {
        this.nombreAlmacen = nombreAlmacen;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

}
