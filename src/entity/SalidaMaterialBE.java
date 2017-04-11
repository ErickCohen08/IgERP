/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.sql.Date;

/**
 *
 * @author ERCO
 */
public class SalidaMaterialBE {
    
    int IdSalidaMaterial;
    Date FechaSalida;
    String Direccion;
    int id_personal;
    int id_obra;
    int id_empresa;
    String UsuarioInserta;
    Date FechaInserta;
    String UsuarioModifica;
    Date FechaModifica;
    String UsuarioAnula;
    Date FechaAnula;
    int EstadoActivo;
    int EstadoAbierto;
    String Motivo;
    
    String DesPersonal;
    String DesObra;
    String DesEstadoActivo;
    String DesEstadoAbierto;
    
    int Fila;

    public int getIdSalidaMaterial() {
        return IdSalidaMaterial;
    }

    public void setIdSalidaMaterial(int IdSalidaMaterial) {
        this.IdSalidaMaterial = IdSalidaMaterial;
    }

    public Date getFechaSalida() {
        return FechaSalida;
    }

    public void setFechaSalida(Date FechaSalida) {
        this.FechaSalida = FechaSalida;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public int getId_personal() {
        return id_personal;
    }

    public void setId_personal(int id_personal) {
        this.id_personal = id_personal;
    }

    public int getId_obra() {
        return id_obra;
    }

    public void setId_obra(int id_obra) {
        this.id_obra = id_obra;
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

    public String getUsuarioAnula() {
        return UsuarioAnula;
    }

    public void setUsuarioAnula(String UsuarioAnula) {
        this.UsuarioAnula = UsuarioAnula;
    }

    public Date getFechaAnula() {
        return FechaAnula;
    }

    public void setFechaAnula(Date FechaAnula) {
        this.FechaAnula = FechaAnula;
    }

    public int getEstadoActivo() {
        return EstadoActivo;
    }

    public void setEstadoActivo(int EstadoActivo) {
        this.EstadoActivo = EstadoActivo;
    }

    public int getEstadoAbierto() {
        return EstadoAbierto;
    }

    public void setEstadoAbierto(int EstadoAbierto) {
        this.EstadoAbierto = EstadoAbierto;
    }

    public String getMotivo() {
        return Motivo;
    }

    public void setMotivo(String Motivo) {
        this.Motivo = Motivo;
    }

    public String getDesPersonal() {
        return DesPersonal;
    }

    public void setDesPersonal(String DesPersonal) {
        this.DesPersonal = DesPersonal;
    }

    public String getDesObra() {
        return DesObra;
    }

    public void setDesObra(String DesObra) {
        this.DesObra = DesObra;
    }

    public int getFila() {
        return Fila;
    }

    public void setFila(int Fila) {
        this.Fila = Fila;
    }  

    public String getDesEstadoActivo() {
        return DesEstadoActivo;
    }

    public void setDesEstadoActivo(String DesEstadoActivo) {
        this.DesEstadoActivo = DesEstadoActivo;
    }

    public String getDesEstadoAbierto() {
        return DesEstadoAbierto;
    }

    public void setDesEstadoAbierto(String DesEstadoAbierto) {
        this.DesEstadoAbierto = DesEstadoAbierto;
    }
    
}