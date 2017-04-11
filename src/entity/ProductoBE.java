/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ERCO
 */
public class ProductoBE {
    
    int id_producto;
    String codigo;
    String descripcion;
    String modelo;
    String marca;
    String descripcion_coloquial;
    int estado;
    int cierreInventario;
    BigDecimal peso;
    BigDecimal precio_promedio;
    BigDecimal cantidad;
    
    int	id_moneda;
    int id_unidad;
    int id_productotipo;
    int id_empresa;
    int id_Almacen;
    int id_Referencia_precio;
    
    String UsuarioInserta;
    Date FechaInserta;
    String UsuarioModifica;
    Date FechaModifica;
    
    String Desmoneda;
    String Desunidad;
    String Desproductotipo;
    String Desempresa;
    String DesAlmacen;
    String DesReferencia_precio;
    
    int Fila;
    int TipoOperacion;
    List<ProductoDetalleBE> listaDetalleProducto;

    public ProductoBE() {
    }

    public ProductoBE(int id_producto, String codigo, String descripcion, String modelo, String marca, String descripcion_coloquial, int id_empresa, String Desmoneda, String Desunidad, String Desproductotipo, String DesAlmacen, String DesReferencia_precio) {
        this.id_producto = id_producto;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.modelo = modelo;
        this.marca = marca;
        this.descripcion_coloquial = descripcion_coloquial;
        this.id_empresa = id_empresa;
        this.Desmoneda = Desmoneda;
        this.Desunidad = Desunidad;
        this.Desproductotipo = Desproductotipo;
        this.DesAlmacen = DesAlmacen;
        this.DesReferencia_precio = DesReferencia_precio;
    }

    
    
    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getId_unidad() {
        return id_unidad;
    }

    public void setId_unidad(int id_unidad) {
        this.id_unidad = id_unidad;
    }

    public int getId_productotipo() {
        return id_productotipo;
    }

    public void setId_productotipo(int id_productotipo) {
        this.id_productotipo = id_productotipo;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public int getId_moneda() {
        return id_moneda;
    }

    public void setId_moneda(int id_moneda) {
        this.id_moneda = id_moneda;
    }

    public BigDecimal getPrecio_promedio() {
        return precio_promedio;
    }

    public void setPrecio_promedio(BigDecimal precio_promedio) {
        this.precio_promedio = precio_promedio;
    }

    public String getDescripcion_coloquial() {
        return descripcion_coloquial;
    }

    public void setDescripcion_coloquial(String descripcion_coloquial) {
        this.descripcion_coloquial = descripcion_coloquial;
    }

    public List<ProductoDetalleBE> getListaDetalleProducto() {
        return listaDetalleProducto;
    }

    public void setListaDetalleProducto(List<ProductoDetalleBE> listaDetalleProducto) {
        this.listaDetalleProducto = listaDetalleProducto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public int getId_Almacen() {
        return id_Almacen;
    }

    public void setId_Almacen(int id_Almacen) {
        this.id_Almacen = id_Almacen;
    }

    public int getId_Referencia_precio() {
        return id_Referencia_precio;
    }

    public void setId_Referencia_precio(int id_Referencia_precio) {
        this.id_Referencia_precio = id_Referencia_precio;
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

    public String getDesmoneda() {
        return Desmoneda;
    }

    public void setDesmoneda(String Desmoneda) {
        this.Desmoneda = Desmoneda;
    }

    public String getDesunidad() {
        return Desunidad;
    }

    public void setDesunidad(String Desunidad) {
        this.Desunidad = Desunidad;
    }

    public String getDesproductotipo() {
        return Desproductotipo;
    }

    public void setDesproductotipo(String Desproductotipo) {
        this.Desproductotipo = Desproductotipo;
    }

    public String getDesempresa() {
        return Desempresa;
    }

    public void setDesempresa(String Desempresa) {
        this.Desempresa = Desempresa;
    }

    public String getDesAlmacen() {
        return DesAlmacen;
    }

    public void setDesAlmacen(String DesAlmacen) {
        this.DesAlmacen = DesAlmacen;
    }

    public String getDesReferencia_precio() {
        return DesReferencia_precio;
    }

    public void setDesReferencia_precio(String DesReferencia_precio) {
        this.DesReferencia_precio = DesReferencia_precio;
    }

    public int getFila() {
        return Fila;
    }

    public void setFila(int Fila) {
        this.Fila = Fila;
    }    

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getCierreInventario() {
        return cierreInventario;
    }

    public void setCierreInventario(int cierreInventario) {
        this.cierreInventario = cierreInventario;
    }

    public int getTipoOperacion() {
        return TipoOperacion;
    }

    public void setTipoOperacion(int TipoOperacion) {
        this.TipoOperacion = TipoOperacion;
    }
}