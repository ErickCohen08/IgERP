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
import javax.swing.JOptionPane;

/**
 *
 * @author ErCo
 */
public class cFormaPago {

    int id_formapago;
    String descripcion;
    String estado;
    int id_empresa;
    int id_usuario;
    protected static String error;

    public boolean crear(String descripcion, String estado, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spFormaPago_crear(?,?,?,?)}");
            st.registerOutParameter(1, Types.VARCHAR);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.INTEGER);
            st.registerOutParameter(4, Types.INTEGER);

            st.setString(1, descripcion);
            st.setString(2, estado);
            st.setInt(3, id_empresa);
            st.setInt(4, id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear FORMA DE PAGO");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al CREAR FORMA DE PAGO.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean modificar(int id_formapago, String descripcion, String estado, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spFormaPago_modificar(?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.INTEGER);
            st.registerOutParameter(5, Types.INTEGER);

            st.setInt(1, id_formapago);
            st.setString(2, descripcion);
            st.setString(3, estado);
            st.setInt(4, id_empresa);
            st.setInt(5, id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al MODIDIFAR FORMA DE PAGO");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al MODIFICAR FORMA DE PAGO.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean eliminar(int id_formapago) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spFormaPago_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_formapago);
            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al ELIMINAR FORMA DE PAGO");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al ELIMINAR FORMA DE PAGO.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public int descripcion_existente(String descripcion) {
        int cantidad = 1;
        String sql = "select count(id_formapago) from TFormaPago where descripcion='" + descripcion + "'";
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
        System.out.println("La descripcion de FORMA DE PAGO fue registrado: " + cantidad + " veces");

        return cantidad;
    }

    public boolean activar(int id_formapago,String estado,int id_usuario) {
        try {
            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spFormaPago_activar_desactivar(?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.INTEGER);

            st.setInt(1, id_formapago);
            st.setString(2, estado);
            st.setInt(3, id_usuario);

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
    
    public cFormaPago(int id_formapago, String descripcion, String estado, int id_empresa, int id_usuario) {
        this.id_formapago = id_formapago;
        this.descripcion = descripcion;
        this.estado = estado;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cFormaPago() {
    }

    public int getId_formapago() {
        return id_formapago;
    }

    public void setId_formapago(int id_formapago) {
        this.id_formapago = id_formapago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
