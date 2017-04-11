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
public class cMoneda {
    
    int id_moneda;
    String nombre;
    String simbolo;
    String moneda_local;
    int id_empresa;
    int id_usuario;
    protected static String error;
    
    public boolean crear(String nombre, String simbolo, String moneda_local, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spMoneda_crear(?,?,?,?,?)}");
            st.registerOutParameter(1, Types.VARCHAR);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.INTEGER);
            st.registerOutParameter(5, Types.INTEGER);
            
            st.setString(1, nombre);
            st.setString(2, simbolo);
            st.setString(3, moneda_local);
            st.setInt(4, id_empresa);
            st.setInt(5, id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear la Moneda");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al CREAR la Moneda.\n Detalles: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean modificar(int id_moneda, String nombre, String simbolo, String moneda_local, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spMoneda_modificar(?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.INTEGER);
            st.registerOutParameter(6, Types.INTEGER);

            st.setInt(1, id_moneda);
            st.setString(2, nombre);
            st.setString(3, simbolo);
            st.setString(4, moneda_local);
            st.setInt(5, id_empresa);
            st.setInt(6, id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al MODIDIFAR la Moneda");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al MODIFICAR la Moneda.\n Detalles: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean eliminar(int id_moneda) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spMoneda_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_moneda);
            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al ELIMINAR la Moneda");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al ELIMINAR la Moneda.\n Detalles: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);           
        }
        return (error == null);
    }

    public int descripcion_existente(String nombre) {
        int cantidad = 1;
        String sql = "select count(id_moneda) from TMoneda where nombre='" + nombre + "'";
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
        System.out.println("La descripcion de la Moneda fue registrado: " + cantidad + " veces");

        return cantidad;
    }

    public int simbolo_existente(String simbolo) {
        int cantidad = 1;
        String sql = "select count(id_moneda) from TMoneda where simbolo='" + simbolo + "'";
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
        System.out.println("El Simbolo de la Moneda fue registrado: " + cantidad + " veces");

        return cantidad;
    }

    public int monedalocal_existente(String moneda_local) {
        int cantidad = 1;
        String sql = "select count(id_moneda) from TMoneda where moneda_local='" + moneda_local + "'";
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
        System.out.println("La Moneda Local de la Moneda fue registrado: " + cantidad + " veces");

        return cantidad;
    }
    
    public cMoneda(int id_moneda, String nombre, String simbolo, String moneda_local, int id_empresa, int id_usuario) {
        this.id_moneda = id_moneda;
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.moneda_local = moneda_local;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cMoneda() {
    }

    public int getId_moneda() {
        return id_moneda;
    }

    public void setId_moneda(int id_moneda) {
        this.id_moneda = id_moneda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getMoneda_local() {
        return moneda_local;
    }

    public void setMoneda_local(String moneda_local) {
        this.moneda_local = moneda_local;
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
