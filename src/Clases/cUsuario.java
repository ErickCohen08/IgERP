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
public class cUsuario {

    int id_usuario;
    String nombre;
    String alias;
    String contrasenia;
    String perfil;
    int id_empresa;
    protected static String error;

    public boolean Usuario_crear(String nombre, String alias, String contrasenia, String perfil, int id_empresa) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spUsuario_crear(?,?,?,?,?)}");
            st.registerOutParameter(1, Types.VARCHAR);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.INTEGER);

            st.setString(1, nombre);
            st.setString(2, alias);
            st.setString(3, contrasenia);
            st.setString(4, perfil);
            st.setInt(5, id_empresa);

            if (st.execute()) {
                error = "error al crear el usuario: ";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear el usuario");
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }

    public boolean Usuario_modificar(int id_usuario, String nombre, String alias, String contrasenia, String perfil, int id_empresa) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spUsuario_modificar(?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.INTEGER);

            st.setInt(1, id_usuario);
            st.setString(2, nombre);
            st.setString(3, alias);
            st.setString(4, contrasenia);
            st.setString(5, perfil);
            st.setInt(6, id_empresa);

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

    public boolean Usuario_eliminar(int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spUsuario_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_usuario);
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

    public int alias_existente(String alias, int id_empresa) {
        int cantidad = 1;
        String sql = "select count(id_usuario) as cantidad from tusuario where alias='" + alias + "' and id_empresa='"+id_empresa+"'";

        System.out.println("enviando consulta: " + sql);

        try {
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                cantidad = Integer.parseInt(rs.getString("cantidad"));
            }
            rs.close();
            stm.close();
            dbm = null;

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        System.out.println("El Alias fue registrado: " + cantidad + " veces");

        return cantidad;
    }

    public cUsuario() {
    }

    public cUsuario(String nombre, String alias, String contrasenia, String perfil, int id_empresa) {
        this.nombre = nombre;
        this.alias = alias;
        this.contrasenia = contrasenia;
        this.perfil = perfil;
        this.id_empresa = id_empresa;
    }

    public cUsuario(int id_usuario, String nombre, String alias, String contrasenia, String perfil, int id_empresa) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.alias = alias;
        this.contrasenia = contrasenia;
        this.perfil = perfil;
        this.id_empresa = id_empresa;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

     public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }
}
