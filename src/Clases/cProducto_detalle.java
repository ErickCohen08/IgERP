/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import javax.swing.JOptionPane;

/**
 *
 * @author ErCo
 */

public class cProducto_detalle {
    int id_producto_detalle;
    int id_producto;
    int id_proveedor;
    int id_moneda;
    float precio;
    int id_empresa;
    int id_usuario;
    protected static String error;
    
    public boolean crear(int id_producto, int id_proveedor, int id_moneda, float precio, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spProductoDetalle_crear(?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.INTEGER);
            st.registerOutParameter(3, Types.INTEGER);
            st.registerOutParameter(4, Types.FLOAT);
            st.registerOutParameter(5, Types.INTEGER);
            st.registerOutParameter(6, Types.INTEGER);

            st.setInt(1, id_producto);
            st.setInt(2, id_proveedor);
            st.setInt(3, id_moneda);
            st.setFloat(4, precio);
            st.setInt(5, id_empresa);
            st.setInt(6, id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear el Detalle de Producto");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al CREAR el Detalle de Producto.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean modificar(int id_producto_detalle, int id_producto, int id_proveedor, int id_moneda, float precio, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spProductoDetalle_modificar(?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.INTEGER);
            st.registerOutParameter(3, Types.INTEGER);
            st.registerOutParameter(4, Types.FLOAT);
            st.registerOutParameter(5, Types.INTEGER);
            st.registerOutParameter(6, Types.INTEGER);

            st.setInt(1, id_producto_detalle);
            st.setInt(2, id_producto);
            st.setInt(3, id_proveedor);
            st.setInt(4, id_moneda);
            st.setFloat(5, precio);
            st.setInt(6, id_empresa);
            st.setInt(7, id_usuario);


            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al MODIDIFAR el Detalle de Producto");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al MODIFICAR el Detalle de Producto.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean eliminar(int id_producto_detalle) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spProductoDetalle_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_producto_detalle);
            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al ELIMINAR el Detalle de Producto");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al ELIMINAR el Detalle de Producto.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public cProducto_detalle(int id_producto_detalle, int id_producto, int id_proveedor, int id_moneda, float precio, int id_empresa, int id_usuario) {
        this.id_producto_detalle = id_producto_detalle;
        this.id_producto = id_producto;
        this.id_proveedor = id_proveedor;
        this.id_moneda = id_moneda;
        this.precio = precio;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cProducto_detalle() {
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

    public int getId_moneda() {
        return id_moneda;
    }

    public void setId_moneda(int id_moneda) {
        this.id_moneda = id_moneda;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
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
    
    
    
    
}
