
package Clases;

/**
 *
 * @author ErC
 */

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import Clases.cAccesoo;

public class cIGV {
    
    int id_igv;
    int igv;
    String prederteminado;
    int id_empresa;
    int id_usuario;
    protected static String error;
    
    public boolean Igv_crear(int igv, String prederteminado, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spIgv_crear(?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.INTEGER);
            st.registerOutParameter(4, Types.INTEGER);

            st.setInt(1, igv);
            st.setString(2, prederteminado);
            st.setInt(3, id_empresa);
            st.setInt(4, id_usuario);

            if (st.execute()) {
                error = "error al crear el I.G.V.: ";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear el I.G.V.");
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }

    public boolean Igv_modificar(int id_igv, int igv, String prederteminado, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spIgv_modificar(?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.INTEGER);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.INTEGER);
            st.registerOutParameter(5, Types.INTEGER);

            st.setInt(1, id_igv);
            st.setInt(2, igv);
            st.setString(3, prederteminado);
            st.setInt(4, id_empresa);
            st.setInt(5, id_usuario);

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

    public boolean Igv_eliminar(int id_igv) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spIgv_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_igv);
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

    public int igv_existente(int igv, int id_empresa) {
        int cantidad = 1;
        String sql = "select count(id_igv) from tigv where igv='" + igv + "' and id_empresa='"+id_empresa+"'";

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
        System.out.println("El I.G.V. fue registrado: " + cantidad + " veces");

        return cantidad;
    }

    public int predeterminado_existente(int id_empresa) {
        int cantidad = 1;
        String sql = "select count(id_igv) from tigv where predeterminado='SI' and id_empresa='"+id_empresa+"'";

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
        System.out.println("El I.G.V. predeterminado fue registrado: " + cantidad + " veces");

        return cantidad;
    }
    
    public cIGV() {
    }

    public cIGV(int igv, String prederteminado, int id_empresa, int id_usuario) {
        this.igv = igv;
        this.prederteminado = prederteminado;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cIGV(int id_igv, int igv, String prederteminado, int id_empresa, int id_usuario) {
        this.id_igv = id_igv;
        this.igv = igv;
        this.prederteminado = prederteminado;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public int getId_igv() {
        return id_igv;
    }

    public void setId_igv(int id_igv) {
        this.id_igv = id_igv;
    }

    public int getIgv() {
        return igv;
    }

    public void setIgv(int igv) {
        this.igv = igv;
    }

    public String getPrederteminado() {
        return prederteminado;
    }

    public void setPrederteminado(String prederteminado) {
        this.prederteminado = prederteminado;
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