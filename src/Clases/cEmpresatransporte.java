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

/**
 *
 * @author ErCo
 */
public class cEmpresatransporte {
    int id_empresatransporte;
    String razon_social;
    String ruc;
    String direccion;
    String telefono;
    String celular;
    String correo;
    int id_empresa;
    int id_usuario;
    protected static String error;
    
    public boolean crear(String razon_social, String ruc, String direccion, String telefono, String celular, String correo, int id_empresa, int id_usuario) {
        try {
            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spEmpresatransporte_crear(?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.VARCHAR);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.VARCHAR);
            st.registerOutParameter(7, Types.INTEGER);
            st.registerOutParameter(8, Types.INTEGER);

            st.setString(1, razon_social);
            st.setString(2, ruc);
            st.setString(3, direccion);
            st.setString(4, telefono);
            st.setString(5, celular);
            st.setString(6, correo);
            st.setInt(7, id_empresa);
            st.setInt(8, id_usuario);

            if (st.execute()) {
                error = "error al crear la empresa de transportes";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear la empresa de transportes");
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }

    public boolean modificar(int id_empresatransporte, String razon_social, String ruc, String direccion, String telefono, String celular, String correo, int id_empresa, int id_usuario) {
        try {
            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spEmpresatransporte_modificar(?,?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.VARCHAR);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.VARCHAR);
            st.registerOutParameter(7, Types.VARCHAR);
            st.registerOutParameter(8, Types.INTEGER);
            st.registerOutParameter(9, Types.INTEGER);

            st.setInt(1, id_empresatransporte);
            st.setString(2, razon_social);
            st.setString(3, ruc);
            st.setString(4, direccion);
            st.setString(5, telefono);
            st.setString(6, celular);
            st.setString(7, correo);
            st.setInt(8, id_empresa);
            st.setInt(9, id_usuario);


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

    public boolean eliminar(int id_empresatransporte) {
        try {
            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spEmpresatransporte_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_empresatransporte);
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

    public int razon_social_existente(String razon_social, int id_empresa) {
        int cantidad = 1;
        String sql = "select count(id_empresatransporte) from tempresatransporte where razon_social='" + razon_social + "' and id_empresa='"+id_empresa+"'";

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
        System.out.println("La razon_social fue registrado: " + cantidad + " veces");

        return cantidad;
    }
    
    public int ruc_existente(String ruc, int id_empresa) {
        int cantidad = 1;
        String sql = "select count(id_empresatransporte) from tempresatransporte where ruc='" + ruc + "' and id_empresa='"+id_empresa+"'";

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
        System.out.println("El ruc fue registrado: " + cantidad + " veces");

        return cantidad;
    }
    
    

    public cEmpresatransporte(String razon_social, String ruc, String direccion, String telefono, String celular, String correo, int id_empresa, int id_usuario) {
        this.razon_social = razon_social;
        this.ruc = ruc;
        this.direccion = direccion;
        this.telefono = telefono;
        this.celular = celular;
        this.correo = correo;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cEmpresatransporte(int id_empresatransporte, String razon_social, String ruc, String direccion, String telefono, String celular, String correo, int id_empresa, int id_usuario) {
        this.id_empresatransporte = id_empresatransporte;
        this.razon_social = razon_social;
        this.ruc = ruc;
        this.direccion = direccion;
        this.telefono = telefono;
        this.celular = celular;
        this.correo = correo;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cEmpresatransporte() {
    }

    public int getId_empresatransporte() {
        return id_empresatransporte;
    }

    public void setId_empresatransporte(int id_empresatransporte) {
        this.id_empresatransporte = id_empresatransporte;
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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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
