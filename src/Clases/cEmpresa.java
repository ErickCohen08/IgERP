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
 * @author ErC
 */
public class cEmpresa {

    String id_empresa;
    String razon_social;
    String ruc;
    String direccion;
    String telefono;
    protected static String error;

    public boolean Empresa_crear(String razon_social, String ruc, String direccion, String telefono) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spEmpresa_crear(?,?,?,?)}");
            st.registerOutParameter(1, Types.VARCHAR);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);

            st.setString(1, razon_social);
            st.setString(2, ruc);
            st.setString(3, direccion);
            st.setString(4, telefono);

            if (st.execute()) {
                error = "error al crear la empresa: ";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear la empresa");
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }

    public boolean Empresa_modificar(String id_empresa, String razon_social, String ruc, String direccion, String telefono) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spEmpresa_modificar(?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);

            st.setString(1, id_empresa);
            st.setString(2, razon_social);
            st.setString(3, ruc);
            st.setString(4, direccion);
            st.setString(5, telefono);

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

    public boolean Empresa_eliminar(String id_empresa) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spEmpresa_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setString(1, id_empresa);
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

    public int ruc_existente(String ruc) {
        int cantidad=1;
        String sql = "select count(id_empresa) from tempresa where ruc='" + ruc + "'";

        System.out.println("enviando consulta: "+sql);
        
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
        System.out.println("El R.U.C. fue registrado: " + cantidad+ " veces");

        return cantidad;
    }

    public int razon_social_existente(String razon_social) {
        int cantidad = 0;
        String sql = "select count(id_empresa) from tempresa where razon_social='" + razon_social + "'";

        System.out.println("Enviando consulta: "+sql);
        
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
        System.out.println("La Razon Social fue registrado: " + cantidad+ " veces");

        return cantidad;
    }

    public cEmpresa() {}

    public cEmpresa(String razon_social, String ruc, String direccion, String telefono) {
        this.razon_social = razon_social;
        this.ruc = ruc;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public cEmpresa(String id_empresa, String razon_social, String ruc, String direccion, String telefono) {
        this.id_empresa = id_empresa;
        this.razon_social = razon_social;
        this.ruc = ruc;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(String id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
