/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.Date;

/**
 *
 * @author vector
 */
public class ReporteSalidaMaterialBE {

    String numeroSalida;
    String fechaSalida;
    String solicitante;
    String motivo;
    String cliente;
    String obra;
    String direccion;
    String estadoSalida;
    double cantidadRetorno;
    int idProducto;
    String material;
    String unidad;
    double cantidad;
    String marca;
    String categoria;

    public String getNumeroSalida() {
        return numeroSalida;
    }

    public void setNumeroSalida(String numeroSalida) {
        this.numeroSalida = numeroSalida;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getObra() {
        return obra;
    }

    public void setObra(String obra) {
        this.obra = obra;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstadoSalida() {
        return estadoSalida;
    }

    public void setEstadoSalida(String estadoSalida) {
        this.estadoSalida = estadoSalida;
    }

    public double getCantidadRetorno() {
        return cantidadRetorno;
    }

    public void setCantidadRetorno(double cantidadRetorno) {
        this.cantidadRetorno = cantidadRetorno;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "ReporteSalidaMaterialBE{" + "numeroSalida=" + numeroSalida + ", fechaSalida=" + fechaSalida + ", solicitante=" + solicitante + ", motivo=" + motivo + ", cliente=" + cliente + ", obra=" + obra + ", direccion=" + direccion + ", estadoSalida=" + estadoSalida + ", cantidadRetorno=" + cantidadRetorno + ", idProducto=" + idProducto + ", material=" + material + ", unidad=" + unidad + ", cantidad=" + cantidad + ", marca=" + marca + ", categoria=" + categoria + '}';
    }

}
