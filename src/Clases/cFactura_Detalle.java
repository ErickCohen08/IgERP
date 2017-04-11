package Clases;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import Clases.cAccesoo;
import java.math.BigDecimal;

public class cFactura_Detalle {

    int id_detalle_factura;
    int id_factura;
    BigDecimal cantidad;
    String unidad;
    String descripcion;
    BigDecimal precio_unitario;
    BigDecimal precio_total;
    BigDecimal descuento_por;
    BigDecimal descuento_val;

    protected static String error;

    public boolean crear(int id_factura, BigDecimal cantidad, String unidad, String descripcion, BigDecimal precio_unitario, BigDecimal precio_total, BigDecimal descuento_por, BigDecimal descuento_val) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spFactura_Detalle_crear(?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.NUMERIC);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.NUMERIC);
            st.registerOutParameter(6, Types.NUMERIC);
            st.registerOutParameter(7, Types.NUMERIC);
            st.registerOutParameter(8, Types.NUMERIC);

            st.setInt(1, id_factura);
            st.setBigDecimal(2, cantidad);
            st.setString(3, unidad);
            st.setString(4, descripcion);
            st.setBigDecimal(5, precio_unitario);
            st.setBigDecimal(6, precio_total);
            st.setBigDecimal(7, descuento_por);
            st.setBigDecimal(8, descuento_val);


            if (st.execute()) {
                error = "error al crear el detalle de Factura";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear el detalle de Factura");
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }

    public boolean modificar(int id_detalle_factura, int id_factura, BigDecimal cantidad, String unidad, String descripcion, BigDecimal precio_unitario, BigDecimal precio_total, BigDecimal descuento_por, BigDecimal descuento_val) {
        try {

            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spFactura_Detalle_modificar(?,?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.INTEGER);
            st.registerOutParameter(3, Types.NUMERIC);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.NUMERIC);
            st.registerOutParameter(7, Types.NUMERIC);
            st.registerOutParameter(8, Types.NUMERIC);
            st.registerOutParameter(9, Types.NUMERIC);

            st.setInt(1, id_detalle_factura);
            st.setInt(2, id_factura);
            st.setBigDecimal(3, cantidad);
            st.setString(4, unidad);
            st.setString(5, descripcion);
            st.setBigDecimal(6, precio_unitario);
            st.setBigDecimal(7, precio_total);
            st.setBigDecimal(8, descuento_por);
            st.setBigDecimal(9, descuento_val);

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

    public boolean eliminar(int id_detalle_factura) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spFactura_Detalle_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_detalle_factura);
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

    public cFactura_Detalle() {
    }

    public cFactura_Detalle(int id_factura, BigDecimal cantidad, String unidad, String descripcion, BigDecimal precio_unitario, BigDecimal precio_total, BigDecimal descuento_por, BigDecimal descuento_val) {
        this.id_factura = id_factura;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.descripcion = descripcion;
        this.precio_unitario = precio_unitario;
        this.precio_total = precio_total;
        this.descuento_por = descuento_por;
        this.descuento_val = descuento_val;
    }

    public cFactura_Detalle(int id_detalle_factura, int id_factura, BigDecimal cantidad, String unidad, String descripcion, BigDecimal precio_unitario, BigDecimal precio_total, BigDecimal descuento_por, BigDecimal descuento_val) {
        this.id_detalle_factura = id_detalle_factura;
        this.id_factura = id_factura;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.descripcion = descripcion;
        this.precio_unitario = precio_unitario;
        this.precio_total = precio_total;
        this.descuento_por = descuento_por;
        this.descuento_val = descuento_val;
    }

    public int getId_detalle_factura() {
        return id_detalle_factura;
    }
    public void setId_detalle_factura(int id_detalle_factura) {
        this.id_detalle_factura = id_detalle_factura;
    }
    public int getId_factura() {
        return id_factura;
    }
    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }
    public String getUnidad() {
        return unidad;
    }
    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(BigDecimal precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public BigDecimal getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(BigDecimal precio_total) {
        this.precio_total = precio_total;
    }

    public BigDecimal getDescuento_por() {
        return descuento_por;
    }

    public void setDescuento_por(BigDecimal descuento_por) {
        this.descuento_por = descuento_por;
    }

    public BigDecimal getDescuento_val() {
        return descuento_val;
    }

    public void setDescuento_val(BigDecimal descuento_val) {
        this.descuento_val = descuento_val;
    }
}