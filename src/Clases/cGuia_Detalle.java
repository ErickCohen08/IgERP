/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

/**
 *
 * @author ErCo
 */
public class cGuia_Detalle {
    
    int id_detalle_guia;
    int id_guia;
    String descripcion;
    float cantidad;
    String unidad;
    float peso;      
    protected static String error;
    
    public boolean crear(int id_guia, String descripcion, float cantidad, String unidad, float peso) {
        try {
            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spGuia_Detalle_crear(?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.FLOAT);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.FLOAT);
            
            st.setInt(1, id_guia);
            st.setString(2, descripcion);
            st.setFloat(3, cantidad);
            st.setString(4, unidad);
            st.setFloat(5, peso);
            

            if (st.execute()) {
                error = "error al crear el detalle de Guia";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear el detalle de Guia");
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }

    public boolean modificar(int id_detalle_guia, int id_guia, String descripcion, float cantidad, String unidad, float peso) {
        try {

            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spGuia_Detalle_modificar(?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.INTEGER);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.FLOAT);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.FLOAT);
            
            st.setInt(1, id_detalle_guia);
            st.setInt(2, id_guia);
            st.setString(3, descripcion);
            st.setFloat(4, cantidad);
            st.setString(5, unidad);
            st.setFloat(6, peso);

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

    public boolean eliminar(int id_detalle_guia) {
        try {
            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spGuia_Detalle_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_detalle_guia);
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
    

    public cGuia_Detalle(int id_detalle_guia, int id_guia, String descripcion, float cantidad, String unidad, float peso) {
        this.id_detalle_guia = id_detalle_guia;
        this.id_guia = id_guia;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.peso = peso;
    }

    public cGuia_Detalle(int id_guia, String descripcion, float cantidad, String unidad, float peso) {
        this.id_guia = id_guia;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.peso = peso;
    }

    public cGuia_Detalle() {
    }

    public int getId_detalle_guia() {
        return id_detalle_guia;
    }

    public void setId_detalle_guia(int id_detalle_guia) {
        this.id_detalle_guia = id_detalle_guia;
    }

    public int getId_guia() {
        return id_guia;
    }

    public void setId_guia(int id_guia) {
        this.id_guia = id_guia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }
    
}
