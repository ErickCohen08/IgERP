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
public class cDocumentos {

    int id_documento;
    String nombre;
    String serie;
    String numero_inicial;
    String descripcion;
    int id_empresa;
    int id_usuario;
    protected static String error;

    public boolean Documento_crear(String nombre, String serie, String numero_inicial, String descripcion, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spDocumento_crear(?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.VARCHAR);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.INTEGER);
            st.registerOutParameter(6, Types.INTEGER);

            st.setString(1, nombre);
            st.setString(2, serie);
            st.setString(3, numero_inicial);
            st.setString(4, descripcion);
            st.setInt(5, id_empresa);
            st.setInt(6, id_usuario);

            if (st.execute()) {
                error = "error al crear el documento: ";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear el documento");
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }

    public boolean Documento_modificar(int id_documento, String nombre, String serie, String numero_inicial, String descripcion, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spDocumento_modificar(?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.INTEGER);
            st.registerOutParameter(7, Types.INTEGER);

            st.setInt(1, id_documento);
            st.setString(2, nombre);
            st.setString(3, serie);
            st.setString(4, numero_inicial);
            st.setString(5, descripcion);
            st.setInt(6, id_empresa);
            st.setInt(7, id_usuario);

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

    public boolean Documento_eliminar(int id_documento) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spDocumento_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_documento);
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

    public int nombre_existente(String nombre, int id_empresa) {
        int cantidad = 1;
        String sql = "select count(id_documento) from tdocumentos where nombre='" + nombre + "' and id_empresa='"+id_empresa+"'";

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
        System.out.println("El Nombre del Documento fue registrado: " + cantidad + " veces");

        return cantidad;
    }
    
    
    public cDocumentos() {
    }

    public cDocumentos(String nombre, String serie, String numero_inicial, String descripcion, int id_empresa, int id_usuario) {
        this.nombre = nombre;
        this.serie = serie;
        this.numero_inicial = numero_inicial;
        this.descripcion = descripcion;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cDocumentos(int id_documento, String nombre, String serie, String numero_inicial, String descripcion, int id_empresa, int id_usuario) {
        this.id_documento = id_documento;
        this.nombre = nombre;
        this.serie = serie;
        this.numero_inicial = numero_inicial;
        this.descripcion = descripcion;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public int getId_documento() {
        return id_documento;
    }

    public void setId_documento(int id_documento) {
        this.id_documento = id_documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumero_inicial() {
        return numero_inicial;
    }

    public void setNumero_inicial(String numero_inicial) {
        this.numero_inicial = numero_inicial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
