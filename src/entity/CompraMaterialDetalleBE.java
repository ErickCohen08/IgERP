/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author admin
 */
public class CompraMaterialDetalleBE {
    int IdDetalleCompra;
    int IdCompra;
    int id_producto;
    BigDecimal Cantidad;
    BigDecimal PrecioUnitario;
    BigDecimal PrecioTotal;
    int id_empresa;
    int EstadoActivo;
    String UsuarioInserta;
    Date FechaInserta;
    String UsuarioModifica;
    Date FechaModifica;
    
    String DesMaterial;
    String DesUnidad;
    
    int TipoOperacion;

    public int getIdDetalleCompra() {
        return IdDetalleCompra;
    }

    public void setIdDetalleCompra(int IdDetalleCompra) {
        this.IdDetalleCompra = IdDetalleCompra;
    }

    public int getIdCompra() {
        return IdCompra;
    }

    public void setIdCompra(int IdCompra) {
        this.IdCompra = IdCompra;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public BigDecimal getCantidad() {
        return Cantidad;
    }

    public void setCantidad(BigDecimal Cantidad) {
        this.Cantidad = Cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return PrecioUnitario;
    }

    public void setPrecioUnitario(BigDecimal PrecioUnitario) {
        this.PrecioUnitario = PrecioUnitario;
    }

    public BigDecimal getPrecioTotal() {
        return PrecioTotal;
    }

    public void setPrecioTotal(BigDecimal PrecioTotal) {
        this.PrecioTotal = PrecioTotal;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public int getEstadoActivo() {
        return EstadoActivo;
    }

    public void setEstadoActivo(int EstadoActivo) {
        this.EstadoActivo = EstadoActivo;
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

    public String getDesMaterial() {
        return DesMaterial;
    }

    public void setDesMaterial(String DesMaterial) {
        this.DesMaterial = DesMaterial;
    }

    public String getDesUnidad() {
        return DesUnidad;
    }

    public void setDesUnidad(String DesUnidad) {
        this.DesUnidad = DesUnidad;
    }

    public int getTipoOperacion() {
        return TipoOperacion;
    }

    public void setTipoOperacion(int TipoOperacion) {
        this.TipoOperacion = TipoOperacion;
    }
    
    
}
