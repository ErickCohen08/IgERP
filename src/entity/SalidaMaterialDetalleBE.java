/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author admin
 */
public class SalidaMaterialDetalleBE {
    
    int id_detalle_salida_material;
    int id_salida_material;
    int id_producto;
    int id_empresa;
    BigDecimal CantidadSalida;
    BigDecimal CantidadRetorno;
    int EstadoSalida;
    String UsuarioSalida;
    Date FechaSalida;
    String UsuarioEntrega;
    Date FechaEntrega;
    String ComentarioRetorno;

    String NombreMaterial;
    String UnidadMaterial;
    BigDecimal StockMaterial;
    
    int TipoOperacion;
    
    public String getNombreMaterial() {
        return NombreMaterial;
    }

    public void setNombreMaterial(String NombreMaterial) {
        this.NombreMaterial = NombreMaterial;
    }

    public String getUnidadMaterial() {
        return UnidadMaterial;
    }

    public void setUnidadMaterial(String UnidadMaterial) {
        this.UnidadMaterial = UnidadMaterial;
    }

    public BigDecimal getStockMaterial() {
        return StockMaterial;
    }

    public void setStockMaterial(BigDecimal StockMaterial) {
        this.StockMaterial = StockMaterial;
    }
    
    
    public SalidaMaterialDetalleBE() {
    }

    public int getId_detalle_salida_material() {
        return id_detalle_salida_material;
    }

    public void setId_detalle_salida_material(int id_detalle_salida_material) {
        this.id_detalle_salida_material = id_detalle_salida_material;
    }

    public int getId_salida_material() {
        return id_salida_material;
    }

    public void setId_salida_material(int id_salida_material) {
        this.id_salida_material = id_salida_material;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public BigDecimal getCantidadSalida() {
        return CantidadSalida;
    }

    public void setCantidadSalida(BigDecimal CantidadSalida) {
        this.CantidadSalida = CantidadSalida;
    }

    public BigDecimal getCantidadRetorno() {
        return CantidadRetorno;
    }

    public void setCantidadRetorno(BigDecimal CantidadRetorno) {
        this.CantidadRetorno = CantidadRetorno;
    }

    public int getEstadoSalida() {
        return EstadoSalida;
    }

    public void setEstadoSalida(int EstadoSalida) {
        this.EstadoSalida = EstadoSalida;
    }

    public String getUsuarioSalida() {
        return UsuarioSalida;
    }

    public void setUsuarioSalida(String UsuarioSalida) {
        this.UsuarioSalida = UsuarioSalida;
    }

    public Date getFechaSalida() {
        return FechaSalida;
    }

    public void setFechaSalida(Date FechaSalida) {
        this.FechaSalida = FechaSalida;
    }

    public String getUsuarioEntrega() {
        return UsuarioEntrega;
    }

    public void setUsuarioEntrega(String UsuarioEntrega) {
        this.UsuarioEntrega = UsuarioEntrega;
    }

    public Date getFechaEntrega() {
        return FechaEntrega;
    }

    public void setFechaEntrega(Date FechaEntrega) {
        this.FechaEntrega = FechaEntrega;
    }

    public String getComentarioRetorno() {
        return ComentarioRetorno;
    }

    public void setComentarioRetorno(String ComentarioRetorno) {
        this.ComentarioRetorno = ComentarioRetorno;
    }

    public int getTipoOperacion() {
        return TipoOperacion;
    }

    public void setTipoOperacion(int TipoOperacion) {
        this.TipoOperacion = TipoOperacion;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }
    
}
