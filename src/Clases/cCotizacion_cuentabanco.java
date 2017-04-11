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
public class cCotizacion_cuentabanco {

    int id_cotizacion_cuentabanco;
    int id_cotizacion;
    int id_cuentabanco;
    int id_empresa;
    int id_usuario;
    protected static String error;

    public boolean crear(int id_cotizacion, int id_cuentabanco, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spCotizacion_cuentabanco_crear(?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.INTEGER);
            st.registerOutParameter(3, Types.INTEGER);
            st.registerOutParameter(4, Types.INTEGER);
            
            st.setInt(1, id_cotizacion);
            st.setInt(2, id_cuentabanco);
            st.setInt(3, id_empresa);
            st.setInt(4, id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear Cotizacion_cuentabanco");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al CREAR Cotizacion_cuentabanco.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean modificar(int id_cotizacion_cuentabanco, int id_cotizacion, int id_cuentabanco, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spCotizacion_cuentabanco_modificar(?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.INTEGER);
            st.registerOutParameter(3, Types.INTEGER);
            st.registerOutParameter(4, Types.INTEGER);
            st.registerOutParameter(5, Types.INTEGER);

            st.setInt(1, id_cotizacion_cuentabanco);
            st.setInt(2, id_cotizacion);
            st.setInt(3, id_cuentabanco);
            st.setInt(4, id_empresa);
            st.setInt(5, id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al MODIDIFAR Cotizacion_cuentabanco");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al MODIFICAR Cotizacion_cuentabanco.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean eliminar(int id_cotizacion_cuentabanco) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spCotizacion_cuentabanco_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_cotizacion_cuentabanco);
            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al ELIMINAR Cotizacion_cuentabanco");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al ELIMINAR Cotizacion_cuentabanco.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public cCotizacion_cuentabanco(int id_cotizacion_cuentabanco, int id_cotizacion, int id_cuentabanco, int id_empresa, int id_usuario) {
        this.id_cotizacion_cuentabanco = id_cotizacion_cuentabanco;
        this.id_cotizacion = id_cotizacion;
        this.id_cuentabanco = id_cuentabanco;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cCotizacion_cuentabanco() {
    }

    public int getId_cotizacion_cuentabanco() {
        return id_cotizacion_cuentabanco;
    }

    public void setId_cotizacion_cuentabanco(int id_cotizacion_cuentabanco) {
        this.id_cotizacion_cuentabanco = id_cotizacion_cuentabanco;
    }

    public int getId_cotizacion() {
        return id_cotizacion;
    }

    public void setId_cotizacion(int id_cotizacion) {
        this.id_cotizacion = id_cotizacion;
    }

    public int getId_cuentabanco() {
        return id_cuentabanco;
    }

    public void setId_cuentabanco(int id_cuentabanco) {
        this.id_cuentabanco = id_cuentabanco;
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
