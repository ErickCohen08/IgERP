/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import Clases.cAccesoo;

/**
 *
 * @author ErCo
 */
public class cTipopago {
    int id_tipopago;
    String descripcion;
    int cantdias;
    int id_empresa;
    int id_usuario;
    int avisar_por_vencer;

    protected static String error;

    public boolean Tipopago_crear(String descripcion, int cantdias, int id_empresa, int id_usuario,int avisar_por_vencer) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spTipopago_crear(?,?,?,?,?)}");
            st.registerOutParameter(1, Types.VARCHAR);
            st.registerOutParameter(2, Types.INTEGER);
            st.registerOutParameter(3, Types.INTEGER);
            st.registerOutParameter(4, Types.INTEGER);
            st.registerOutParameter(5, Types.INTEGER);
            
            st.setString(1, descripcion);
            st.setInt(2, cantdias);
            st.setInt(3, id_empresa);
            st.setInt(4, id_usuario);
            st.setInt(5, avisar_por_vencer);

            if (st.execute()) {
                error = "error al crear el tipo de pago: ";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear el tipo de pago");
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }
    public boolean Tipopago_modificar(int id_tipopago, String descripcion, int cantdias, int id_empresa, int id_usuario,int avisar_por_vencer) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spTipopago_modificar(?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.INTEGER);
            st.registerOutParameter(4, Types.INTEGER);
            st.registerOutParameter(5, Types.INTEGER);
            st.registerOutParameter(6, Types.INTEGER);

            st.setInt(1, id_tipopago);
            st.setString(2, descripcion);
            st.setInt(3, cantdias);
            st.setInt(4, id_empresa);
            st.setInt(5, id_usuario);
            st.setInt(6, avisar_por_vencer);

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
    public boolean Tipopago_eliminar(int id_tipopago) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spTipopago_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_tipopago);
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
    public int nombre_existente(String descripcion, int id_empresa) {
        int cantidad = 1;
        String sql = "select count(id_tipopago) from ttipopago where descripcion='" + descripcion + "' and id_empresa='"+id_empresa+"'";

        System.out.println("enviando consulta: " + sql);

        try {
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                cantidad = Integer.parseInt(rs.getString(1));
            }
            rs.close();
            stm.close();
            dbm = null;

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        System.out.println("El Nombre del Tipo de pago fue registrado: " + cantidad + " veces");

        return cantidad;
    }
 

    public cTipopago() {
    }

    public int getAvisar_por_vencer() {
        return avisar_por_vencer;
    }

    public void setAvisar_por_vencer(int avisar_por_vencer) {
        this.avisar_por_vencer = avisar_por_vencer;
    }
    
    public cTipopago(String descripcion, int cantdias, int id_empresa, int id_usuario, int avisar_por_vencer) {
        this.descripcion = descripcion;
        this.cantdias = cantdias;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
        this.avisar_por_vencer = avisar_por_vencer;
        
    }
   
    public cTipopago(int id_tipopago, String descripcion, int cantdias, int id_empresa, int id_usuario, int avisar_por_vencer) {
        this.id_tipopago = id_tipopago;
        this.descripcion = descripcion;
        this.cantdias = cantdias;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
        this.avisar_por_vencer = avisar_por_vencer;
    }

    public int getId_tipopago() {
        return id_tipopago;
    }

    public void setId_tipopago(int id_tipopago) {
        this.id_tipopago = id_tipopago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantdias() {
        return cantdias;
    }

    public void setCantdias(int cantdias) {
        this.cantdias = cantdias;
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
