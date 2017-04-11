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
public class DatoComunBE {
    
    int IdDatoComun;
    int CodigoTabla;
    int CodigoFila;
    String DescripcionCorta;
    String DescripcionLarga;
    String ValorTexto1;
    String ValorTexto2;
    String UsuarioInserta;
    Date FechaInserta;
    String UsuarioModifica;
    Date FechaModifica;
    String Estado;
    int id_empresa;
    
    String usuarioDes;
    
    public DatoComunBE() {
        
    }

    public DatoComunBE(int CodigoTabla, int CodigoFila, String DescripcionCorta, String DescripcionLarga, String ValorTexto1, String ValorTexto2, String UsuarioInserta, String Estado) {
        this.CodigoTabla = CodigoTabla;
        this.CodigoFila = CodigoFila;
        this.DescripcionCorta = DescripcionCorta;
        this.DescripcionLarga = DescripcionLarga;
        this.ValorTexto1 = ValorTexto1;
        this.ValorTexto2 = ValorTexto2;
        this.UsuarioInserta = UsuarioInserta;
        this.Estado = Estado;
    }

    public DatoComunBE(int CodigoTabla, int CodigoFila, String DescripcionCorta, String DescripcionLarga, String ValorTexto1, String ValorTexto2, String UsuarioInserta, String Estado, int id_empresa) {
        this.CodigoTabla = CodigoTabla;
        this.CodigoFila = CodigoFila;
        this.DescripcionCorta = DescripcionCorta;
        this.DescripcionLarga = DescripcionLarga;
        this.ValorTexto1 = ValorTexto1;
        this.ValorTexto2 = ValorTexto2;
        this.UsuarioInserta = UsuarioInserta;
        this.Estado = Estado;
        this.id_empresa = id_empresa;
    }

    public DatoComunBE(int IdDatoComun, int CodigoTabla, int CodigoFila, String DescripcionCorta, String DescripcionLarga, String ValorTexto1, String ValorTexto2, String UsuarioInserta, Date FechaInserta, String UsuarioModifica, Date FechaModifica, String Estado, int id_empresa, String usuarioDes) {
        this.IdDatoComun = IdDatoComun;
        this.CodigoTabla = CodigoTabla;
        this.CodigoFila = CodigoFila;
        this.DescripcionCorta = DescripcionCorta;
        this.DescripcionLarga = DescripcionLarga;
        this.ValorTexto1 = ValorTexto1;
        this.ValorTexto2 = ValorTexto2;
        this.UsuarioInserta = UsuarioInserta;
        this.FechaInserta = FechaInserta;
        this.UsuarioModifica = UsuarioModifica;
        this.FechaModifica = FechaModifica;
        this.Estado = Estado;
        this.id_empresa = id_empresa;
        this.usuarioDes = usuarioDes;
    }

    public DatoComunBE(int IdDatoComun, int CodigoTabla, int CodigoFila, String DescripcionCorta, String DescripcionLarga, String ValorTexto1, String ValorTexto2, String Estado) {
        this.IdDatoComun = IdDatoComun;
        this.CodigoTabla = CodigoTabla;
        this.CodigoFila = CodigoFila;
        this.DescripcionCorta = DescripcionCorta;
        this.DescripcionLarga = DescripcionLarga;
        this.ValorTexto1 = ValorTexto1;
        this.ValorTexto2 = ValorTexto2;
        this.Estado = Estado;
    }

    

    
    public int getIdDatoComun() {
        return IdDatoComun;
    }

    public void setIdDatoComun(int IdDatoComun) {
        this.IdDatoComun = IdDatoComun;
    }

    public int getCodigoTabla() {
        return CodigoTabla;
    }

    public void setCodigoTabla(int CodigoTabla) {
        this.CodigoTabla = CodigoTabla;
    }

    public int getCodigoFila() {
        return CodigoFila;
    }

    public void setCodigoFila(int CodigoFila) {
        this.CodigoFila = CodigoFila;
    }

    public String getDescripcionCorta() {
        return DescripcionCorta;
    }

    public void setDescripcionCorta(String DescripcionCorta) {
        this.DescripcionCorta = DescripcionCorta;
    }

    public String getDescripcionLarga() {
        return DescripcionLarga;
    }

    public void setDescripcionLarga(String DescripcionLarga) {
        this.DescripcionLarga = DescripcionLarga;
    }

    public String getValorTexto1() {
        return ValorTexto1;
    }

    public void setValorTexto1(String ValorTexto1) {
        this.ValorTexto1 = ValorTexto1;
    }

    public String getValorTexto2() {
        return ValorTexto2;
    }

    public void setValorTexto2(String ValorTexto2) {
        this.ValorTexto2 = ValorTexto2;
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

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getUsuarioDes() {
        return usuarioDes;
    }

    public void setUsuarioDes(String usuarioDes) {
        this.usuarioDes = usuarioDes;
    }

    
    
    
    
    
}
