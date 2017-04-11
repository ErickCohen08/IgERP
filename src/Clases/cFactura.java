package Clases;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import Clases.cAccesoo;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

public class cFactura {

    int id_factura;
    String numero_factura;
    String fecha;
    BigDecimal calculo_igv;
    BigDecimal sub_total;
    BigDecimal total;
    String total_letras;
    int id_cliente;
    String moneda;
    int id_igv;
    int id_documento;
    String impreso;
    String anulado;
    String pagado;
    String simbolo_moneda;
    int id_empresa;
    int id_usuario;    
    int id_tipopago;
    String vencimiento;
    String ordencompra;
    int id_guia;
    BigDecimal descuento;
    String observaciones;

    protected static String error;

    public boolean crear(int id_empresa, int id_usuario) {
        try {
            error = null;
            //JOptionPane.showMessageDialog(null, "Abrir clase acceso", "", JOptionPane.INFORMATION_MESSAGE);
            cAccesoo dbm;
            dbm = new cAccesoo();
            //JOptionPane.showMessageDialog(null, "Clase instanciada", "", JOptionPane.INFORMATION_MESSAGE);
            
            Connection con = dbm.getConnection();
            //JOptionPane.showMessageDialog(null, "conexion iniciada", "", JOptionPane.INFORMATION_MESSAGE);
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spFactura_crear(?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.INTEGER);

            st.setInt(1, id_empresa);
            st.setInt(2, id_usuario);


            if (st.execute()) {
                error = "error al crear la Factura";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear la Factura");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, ""+e.getMessage(), "error al instancias la clase", JOptionPane.INFORMATION_MESSAGE);
            
        }
        return (error == null);
    }

    public boolean modificar(int id_factura,String numero_factura,String fecha,BigDecimal calculo_igv,BigDecimal sub_total,BigDecimal total,String total_letras,int id_cliente,String moneda,int id_igv,int id_documento,String simbolo_moneda,int id_empresa,int id_usuario,int id_tipopago,String vencimiento,String ordencompra,int id_guia,BigDecimal descuento,String observaciones) {
        try {

            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spFactura_modificar(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.NUMERIC);
            st.registerOutParameter(5, Types.NUMERIC);
            st.registerOutParameter(6, Types.NUMERIC);
            st.registerOutParameter(7, Types.VARCHAR);
            st.registerOutParameter(8, Types.INTEGER);
            st.registerOutParameter(9, Types.VARCHAR);
            st.registerOutParameter(10, Types.VARCHAR);
            st.registerOutParameter(11, Types.INTEGER);
            st.registerOutParameter(12, Types.INTEGER);
            st.registerOutParameter(13, Types.INTEGER);
            st.registerOutParameter(14, Types.INTEGER);
            st.registerOutParameter(15, Types.INTEGER);
            st.registerOutParameter(16, Types.VARCHAR);
            st.registerOutParameter(17, Types.VARCHAR);
            st.registerOutParameter(18, Types.INTEGER);
            st.registerOutParameter(19, Types.NUMERIC);
            st.registerOutParameter(20, Types.VARCHAR);
          
            st.setInt(1, id_factura);
            st.setString(2, numero_factura);
            st.setString(3, fecha);
            st.setBigDecimal(4, calculo_igv);
            st.setBigDecimal(5, sub_total);
            st.setBigDecimal(6, total);
            st.setString(7, total_letras);
            st.setInt(8, id_cliente);
            st.setString(9, moneda);
            st.setString(10, simbolo_moneda);
            st.setInt(11, id_igv);
            st.setInt(12, id_documento);
            st.setInt(13, id_empresa);
            st.setInt(14, id_usuario);
            st.setInt(15, id_tipopago);
            st.setString(16, vencimiento);
            st.setString(17, ordencompra);
            st.setInt(18, id_guia);
            st.setBigDecimal(19, descuento);
            st.setString(20, observaciones);
            

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }

    public boolean eliminar(int id_factura) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spFactura_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_factura);
            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            error = e.toString();
        }
        return (error == null);
    }

    public boolean imprimir(int id_factura) {
        try {

            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spFactura_imprimir(?)}");
            st.registerOutParameter(1, Types.INTEGER);

            st.setInt(1, id_factura);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }

    public boolean anular(int id_factura, BigDecimal calculo_igv, BigDecimal sub_total, BigDecimal total, BigDecimal descuento, String total_letras) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spFactura_anular(?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.FLOAT);
            st.registerOutParameter(3, Types.FLOAT);
            st.registerOutParameter(4, Types.FLOAT);
            st.registerOutParameter(5, Types.FLOAT);
            st.registerOutParameter(6, Types.VARCHAR);

            st.setInt(1, id_factura);
            st.setBigDecimal(2, calculo_igv);
            st.setBigDecimal(3, sub_total);
            st.setBigDecimal(4, total);
            st.setBigDecimal(5, descuento);
            st.setString(6, total_letras);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }
    
    public boolean pagar(int id_factura, String valor) {
        try {

            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spFactura_pagado(?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.CHAR);

            st.setInt(1, id_factura);
            st.setString(2, valor);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }
    
    public String generar_Numero(int id_empresa) {

        int vcorre = 1;
        String sql, vCeros = "";
        try {
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            sql = "SELECT numero_factura from tfactura where id_empresa='" + id_empresa + "' order by numero_factura";
            System.out.println("enviando consulta: " + sql);
            PreparedStatement st = con.prepareStatement(sql, 1005, 1007);
            ResultSet rs = st.executeQuery();
            rs.afterLast();
            if (rs.previous()) {
                vcorre = Integer.parseInt(rs.getString("numero_factura"));
                vcorre++;
            }
            for (int i = 1; i < 7 - String.valueOf(vcorre).length(); i++) {
                vCeros = vCeros + "0";
            }
            rs.close();
            st.close();
            con.close();
            System.out.println("Numero de Factura generada = " + vCeros + vcorre);

        } catch (Exception e) {
            e.getMessage();
            System.out.println(e.getMessage());
        }
        return (vCeros + vcorre);
    }
    
    public boolean vincularGuia(int id_factura, int id_guia) {
        try {

            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spFactura_vincular_guia(?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.INTEGER);

            st.setInt(1, id_factura);
            st.setInt(2, id_guia);
            
            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }

    public cFactura() {
    }

    public cFactura(int id_empresa, int id_usuario) {
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cFactura(int id_factura, String numero_factura, String fecha, BigDecimal calculo_igv, BigDecimal sub_total, BigDecimal total, String total_letras, int id_cliente, String moneda, int id_igv, int id_documento, String simbolo_moneda, int id_empresa, int id_usuario, int id_tipopago, String vencimiento, String ordencompra, int id_guia, BigDecimal descuento, String observaciones) {
        this.id_factura = id_factura;
        this.numero_factura = numero_factura;
        this.fecha = fecha;
        this.calculo_igv = calculo_igv;
        this.sub_total = sub_total;
        this.total = total;
        this.total_letras = total_letras;
        this.id_cliente = id_cliente;
        this.moneda = moneda;
        this.id_igv = id_igv;
        this.id_documento = id_documento;
        this.simbolo_moneda = simbolo_moneda;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
        this.id_tipopago = id_tipopago;
        this.vencimiento = vencimiento;
        this.ordencompra = ordencompra;
        this.id_guia = id_guia;
        this.descuento = descuento;
        this.observaciones = observaciones;
    }
    
//    public cFactura(int id_factura, int id_guia) {
//        this.id_factura = id_factura;
//        this.id_guia = id_guia;
//    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public String getNumero_factura() {
        return numero_factura;
    }

    public void setNumero_factura(String numero_factura) {
        this.numero_factura = numero_factura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTotal_letras() {
        return total_letras;
    }

    public void setTotal_letras(String total_letras) {
        this.total_letras = total_letras;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public int getId_igv() {
        return id_igv;
    }

    public void setId_igv(int id_igv) {
        this.id_igv = id_igv;
    }

    public int getId_documento() {
        return id_documento;
    }

    public void setId_documento(int id_documento) {
        this.id_documento = id_documento;
    }

    public String getImpreso() {
        return impreso;
    }

    public void setImpreso(String impreso) {
        this.impreso = impreso;
    }

    public String getAnulado() {
        return anulado;
    }

    public void setAnulado(String anulado) {
        this.anulado = anulado;
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
    
    public String getPagado() {
        return pagado;
    }

    public void setPagado(String pagado) {
        this.pagado = pagado;
    }

    public String getSimbolo_moneda() {
        return simbolo_moneda;
    }

    public void setSimbolo_moneda(String simbolo_moneda) {
        this.simbolo_moneda = simbolo_moneda;
    }
    
    public int getId_tipopago() {
        return id_tipopago;
    }

    public void setId_tipopago(int id_tipopago) {
        this.id_tipopago = id_tipopago;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getOrdencompra() {
        return ordencompra;
    }

    public void setOrdencompra(String ordencompra) {
        this.ordencompra = ordencompra;
    }

    public int getId_guia() {
        return id_guia;
    }

    public void setId_guia(int id_guia) {
        this.id_guia = id_guia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
