/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.math.BigDecimal;

/**
 *
 * @author ERCO
 */
public class ProductoDetalleBE {
    int id_producto_detalle;
    int id_producto;
    int id_proveedor;
    BigDecimal precio;
    int id_empresa;
    int id_usuario;
    
    String rucProveedor;
    String razonsocialProveedor;
    
    String fechaCompra;
    String numeroCompra;
    String desMoneda;
    BigDecimal PrecioUnitario;
    
    
    public ProductoDetalleBE() {
    }

    public int getId_producto_detalle() {
        return id_producto_detalle;
    }

    public void setId_producto_detalle(int id_producto_detalle) {
        this.id_producto_detalle = id_producto_detalle;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
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

    public String getRucProveedor() {
        return rucProveedor;
    }

    public void setRucProveedor(String rucProveedor) {
        this.rucProveedor = rucProveedor;
    }

    public String getRazonsocialProveedor() {
        return razonsocialProveedor;
    }

    public void setRazonsocialProveedor(String razonsocialProveedor) {
        this.razonsocialProveedor = razonsocialProveedor;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(String numeroCompra) {
        this.numeroCompra = numeroCompra;
    }

    public String getDesMoneda() {
        return desMoneda;
    }

    public void setDesMoneda(String desMoneda) {
        this.desMoneda = desMoneda;
    }

    public BigDecimal getPrecioUnitario() {
        return PrecioUnitario;
    }

    public void setPrecioUnitario(BigDecimal PrecioUnitario) {
        this.PrecioUnitario = PrecioUnitario;
    }
    
    
}
