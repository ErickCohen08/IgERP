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
public class ObrasBE {

    int IdObra;
    String Descripcion;
    int id_cliente;
    String EstadoActivo;
    Date FechaInicio;
    int id_empresa;
    String UsuarioInserta;
    Date FechaInserta;
    String UsuarioModifica;
    Date FechaModifica;
    
    String DesCliente;
    
    public ObrasBE() {        
    }

    public int getIdObra() {
        return IdObra;
    }

    public void setIdObra(int IdObra) {
        this.IdObra = IdObra;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getEstadoActivo() {
        return EstadoActivo;
    }

    public void setEstadoActivo(String EstadoActivo) {
        this.EstadoActivo = EstadoActivo;
    }

    public Date getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(Date FechaInicio) {
        this.FechaInicio = FechaInicio;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getUsuarioInserta() {
        return UsuarioInserta;
    }

    public void setUsuarioInserta(String UsuarioInserta) {
        this.UsuarioInserta = UsuarioInserta;
    }

    public Date getFechaInserta() {
        return FechaInserta;
    }

    public void setFechaInserta(Date FechaInserta) {
        this.FechaInserta = FechaInserta;
    }

    public String getUsuarioModifica() {
        return UsuarioModifica;
    }

    public void setUsuarioModifica(String UsuarioModifica) {
        this.UsuarioModifica = UsuarioModifica;
    }

    public Date getFechaModifica() {
        return FechaModifica;
    }

    public void setFechaModifica(Date FechaModifica) {
        this.FechaModifica = FechaModifica;
    }

    public String getDesCliente() {
        return DesCliente;
    }

    public void setDesCliente(String DesCliente) {
        this.DesCliente = DesCliente;
    }
    
    
}
