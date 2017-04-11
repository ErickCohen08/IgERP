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
public class cBanco_detalles_cotizacion {
    int id_banco_detalle_presupesto;
    String descripcion;
    int id_unidad;
    int id_moneda;
    float precio_modeda_extrangera;
    int id_tipodecambio;
    float precio_moneda_local;
    int id_empresa;
    int id_usuario;
    
    protected static String error;

    public boolean crear(String descripcion, int id_unidad, int id_moneda, float precio_modeda_extrangera, int id_tipodecambio, float precio_moneda_local, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spBanco_detalles_cotizacion_crear(?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.VARCHAR);
            st.registerOutParameter(2, Types.INTEGER);
            st.registerOutParameter(3, Types.INTEGER);
            st.registerOutParameter(4, Types.FLOAT);
            st.registerOutParameter(5, Types.INTEGER);
            st.registerOutParameter(6, Types.FLOAT);
            st.registerOutParameter(7, Types.INTEGER);
            st.registerOutParameter(8, Types.INTEGER);
            
            st.setString(1, descripcion);
            st.setInt(2, id_unidad);
            st.setInt(3, id_moneda);
            st.setFloat(4, precio_modeda_extrangera);
            st.setInt(5, id_tipodecambio);
            st.setFloat(6, precio_moneda_local);
            st.setInt(7, id_empresa);
            st.setInt(8, id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear BANCO DETALLE COTIZACION");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al CREAR BANCO DETALLE COTIZACION.\n Detalles: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean modificar(int id_banco_detalle_presupesto, String descripcion, int id_unidad, int id_moneda, float precio_modeda_extrangera, int id_tipodecambio, float precio_moneda_local, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spBanco_detalles_cotizacion_modificar(?,?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(1, Types.VARCHAR);
            st.registerOutParameter(2, Types.INTEGER);
            st.registerOutParameter(3, Types.INTEGER);
            st.registerOutParameter(4, Types.FLOAT);
            st.registerOutParameter(5, Types.INTEGER);
            st.registerOutParameter(6, Types.FLOAT);
            st.registerOutParameter(7, Types.INTEGER);
            st.registerOutParameter(8, Types.INTEGER);
            
            st.setInt(1, id_banco_detalle_presupesto);
            st.setString(2, descripcion);
            st.setInt(3, id_unidad);
            st.setInt(4, id_moneda);
            st.setFloat(5, precio_modeda_extrangera);
            st.setInt(6, id_tipodecambio);
            st.setFloat(7, precio_moneda_local);
            st.setInt(8, id_empresa);
            st.setInt(9, id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al MODIDIFAR BANCO DETALLE COTIZACION");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al MODIFICAR BANCO DETALLE COTIZACION.\n Detalles: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean eliminar(int id_banco_detalle_presupesto) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spBanco_detalles_cotizacion_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_banco_detalle_presupesto);
            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al ELIMINAR BANCO DETALLE COTIZACION");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al ELIMINAR BANCO DETALLE COTIZACION.\n Detalles: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);           
        }
        return (error == null);
    }

    public cBanco_detalles_cotizacion(int id_banco_detalle_presupesto, String descripcion, int id_unidad, int id_moneda, float precio_modeda_extrangera, int id_tipodecambio, float precio_moneda_local, int id_empresa, int id_usuario) {
        this.id_banco_detalle_presupesto = id_banco_detalle_presupesto;
        this.descripcion = descripcion;
        this.id_unidad = id_unidad;
        this.id_moneda = id_moneda;
        this.precio_modeda_extrangera = precio_modeda_extrangera;
        this.id_tipodecambio = id_tipodecambio;
        this.precio_moneda_local = precio_moneda_local;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cBanco_detalles_cotizacion() {
    }

    public int getId_banco_detalle_presupesto() {
        return id_banco_detalle_presupesto;
    }

    public void setId_banco_detalle_presupesto(int id_banco_detalle_presupesto) {
        this.id_banco_detalle_presupesto = id_banco_detalle_presupesto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_unidad() {
        return id_unidad;
    }

    public void setId_unidad(int id_unidad) {
        this.id_unidad = id_unidad;
    }

    public int getId_moneda() {
        return id_moneda;
    }

    public void setId_moneda(int id_moneda) {
        this.id_moneda = id_moneda;
    }

    public float getPrecio_modeda_extrangera() {
        return precio_modeda_extrangera;
    }

    public void setPrecio_modeda_extrangera(float precio_modeda_extrangera) {
        this.precio_modeda_extrangera = precio_modeda_extrangera;
    }

    public int getId_tipodecambio() {
        return id_tipodecambio;
    }

    public void setId_tipodecambio(int id_tipodecambio) {
        this.id_tipodecambio = id_tipodecambio;
    }

    public float getPrecio_moneda_local() {
        return precio_moneda_local;
    }

    public void setPrecio_moneda_local(float precio_moneda_local) {
        this.precio_moneda_local = precio_moneda_local;
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
