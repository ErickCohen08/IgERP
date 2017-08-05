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
 * @author ErCo
 */
public class CompraMaterialBE {
    
    int IdCompra;
    Date FechaCompra;
    String NumeroCompra;
    BigDecimal CalculoIgv;
    BigDecimal SubTotal;
    BigDecimal Total;
    String TotalLetras;
    int id_proveedor;
    int id_moneda;
    int id_igv;
    int id_obra;
    int id_documento;
    int id_empresa;
    String UsuarioInserta;
    Date FechaInserta;
    String UsuarioModifica;
    Date FechaModifica;
    int EstadoActivo;
    int EstadoAbierto;
    
    String DesProveedor;
    String RucProveedor;
    String DesMoneda;
    double DesIgv;
    String DesObra;
    String DesDocumento;
    String DesEstadoAbierto;
    
    int TipoOperacion;
    int Fila;

    public int getFila() {
        return Fila;
    }

    public void setFila(int Fila) {
        this.Fila = Fila;
    }
    
    public int getIdCompra() {
        return IdCompra;
    }

    public void setIdCompra(int IdCompra) {
        this.IdCompra = IdCompra;
    }

    public Date getFechaCompra() {
        return FechaCompra;
    }

    public void setFechaCompra(Date FechaCompra) {
        this.FechaCompra = FechaCompra;
    }

    public String getNumeroCompra() {
        return NumeroCompra;
    }

    public void setNumeroCompra(String NumeroCompra) {
        this.NumeroCompra = NumeroCompra;
    }

    public BigDecimal getCalculoIgv() {
        return CalculoIgv;
    }

    public void setCalculoIgv(BigDecimal CalculoIgv) {
        this.CalculoIgv = CalculoIgv;
    }

    public BigDecimal getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(BigDecimal SubTotal) {
        this.SubTotal = SubTotal;
    }

    public BigDecimal getTotal() {
        return Total;
    }

    public void setTotal(BigDecimal Total) {
        this.Total = Total;
    }

    public String getTotalLetras() {
        return TotalLetras;
    }

    public void setTotalLetras(String TotalLetras) {
        this.TotalLetras = TotalLetras;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public int getId_moneda() {
        return id_moneda;
    }

    public void setId_moneda(int id_moneda) {
        this.id_moneda = id_moneda;
    }

    public int getId_igv() {
        return id_igv;
    }

    public void setId_igv(int id_igv) {
        this.id_igv = id_igv;
    }

    public int getId_obra() {
        return id_obra;
    }

    public void setId_obra(int id_obra) {
        this.id_obra = id_obra;
    }

    public int getId_documento() {
        return id_documento;
    }

    public void setId_documento(int id_documento) {
        this.id_documento = id_documento;
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

    public String getDesProveedor() {
        return DesProveedor;
    }

    public void setDesProveedor(String DesProveedor) {
        this.DesProveedor = DesProveedor;
    }

    public String getDesMoneda() {
        return DesMoneda;
    }

    public void setDesMoneda(String DesMoneda) {
        this.DesMoneda = DesMoneda;
    }

    public double getDesIgv() {
        return DesIgv;
    }

    public void setDesIgv(double DesIgv) {
        this.DesIgv = DesIgv;
    }

    public String getDesObra() {
        return DesObra;
    }

    public void setDesObra(String DesObra) {
        this.DesObra = DesObra;
    }

    public String getDesDocumento() {
        return DesDocumento;
    }

    public void setDesDocumento(String DesDocumento) {
        this.DesDocumento = DesDocumento;
    }

    public String getDesEstadoAbierto() {
        return DesEstadoAbierto;
    }

    public void setDesEstadoAbierto(String DesEstadoAbierto) {
        this.DesEstadoAbierto = DesEstadoAbierto;
    }

    public int getTipoOperacion() {
        return TipoOperacion;
    }

    public void setTipoOperacion(int TipoOperacion) {
        this.TipoOperacion = TipoOperacion;
    }

    public String getRucProveedor() {
        return RucProveedor;
    }

    public void setRucProveedor(String RucProveedor) {
        this.RucProveedor = RucProveedor;
    }
    
}
