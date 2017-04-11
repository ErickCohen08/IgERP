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
public class cCotizacion_detalle {
    int id_cotizaciondetalle;
    int id_cotizacion;
    String categ_padre;
    float item;
    String descripcion;
    float cantidad;
    String id_unidad;
    float precio_unitario;
    float precio_total;
    int id_empresa;
    int id_usuario;
    String no_afecta_total;

    protected static String error;
    
    public boolean crear(int id_cotizacion, String categ_padre, float item, String descripcion, float cantidad, String id_unidad, float precio_unitario, float precio_total, int id_empresa, int id_usuario, String no_afecta_total) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spCotizacion_detalle_crear(?,?,?,?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.FLOAT);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.INTEGER);
            st.registerOutParameter(7, Types.FLOAT);
            st.registerOutParameter(8, Types.FLOAT);
            st.registerOutParameter(9, Types.INTEGER);
            st.registerOutParameter(10, Types.INTEGER);
            st.registerOutParameter(11, Types.CHAR);
            
            st.setInt(1, id_cotizacion);
            st.setString(2, categ_padre);
            st.setFloat(3, item);
            st.setString(4, descripcion);
            st.setFloat(5, cantidad);
            st.setString(6, id_unidad);
            st.setFloat(7, precio_unitario);
            st.setFloat(8, precio_total);
            st.setInt(9, id_empresa);
            st.setInt(10, id_usuario);
            st.setString(11, no_afecta_total);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear DETALLE DE COTIZACION");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al CREAR DETALLE DE COTIZACION.\n Detalles: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean modificar(int id_cotizaciondetalle, int id_cotizacion, String categ_padre, float item, String descripcion, float cantidad, String id_unidad, float precio_unitario, float precio_total, int id_empresa, int id_usuario, String no_afecta_total) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spCotizacion_detalle_modificar(?,?,?,?,?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.INTEGER);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.FLOAT);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.VARCHAR);
            st.registerOutParameter(7, Types.INTEGER);
            st.registerOutParameter(8, Types.FLOAT);
            st.registerOutParameter(9, Types.FLOAT);
            st.registerOutParameter(10, Types.INTEGER);
            st.registerOutParameter(11, Types.INTEGER);
            st.registerOutParameter(12, Types.CHAR);
            
            st.setInt(1, id_cotizaciondetalle);
            st.setInt(2, id_cotizacion);
            st.setString(3, categ_padre);
            st.setFloat(4, item);
            st.setString(5, descripcion);
            st.setFloat(6, cantidad);
            st.setString(7, id_unidad);
            st.setFloat(8, precio_unitario);
            st.setFloat(9, precio_total);
            st.setInt(10, id_empresa);
            st.setInt(11, id_usuario);
            st.setString(12, no_afecta_total);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al MODIDIFAR DETALLE DE COTIZACION");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al MODIFICAR DETALLE DE COTIZACION.\n Detalles: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean modificar_categoriapadre(int id_cotizaciondetalle, String categ_padre, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spCotizacion_detalle_modificar_categoriapadre(?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.INTEGER);
            
            st.setInt(1, id_cotizaciondetalle);
            st.setString(2, categ_padre);
            st.setInt(3, id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al MODIDIFAR DETALLE DE COTIZACION - CATEGORIA PADRE");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al MODIFICAR DETALLE DE COTIZACION - CATEGORIA PADRE.\n Detalles: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }
    
    public boolean modificar_item(int id_cotizaciondetalle, float item, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spCotizacion_detalle_modificar_item(?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.FLOAT);
            st.registerOutParameter(3, Types.INTEGER);
            
            st.setInt(1, id_cotizaciondetalle);
            st.setFloat(2, item);
            st.setInt(3, id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al MODIDIFAR DETALLE DE COTIZACION - ITEM");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al MODIFICAR DETALLE DE COTIZACION - ITEM.\n Detalles: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }
    
    public boolean eliminar(int id_cotizaciondetalle) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spCotizacion_detalle_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_cotizaciondetalle);
            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al ELIMINAR DETALLE DE COTIZACION");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al ELIMINAR DETALLE DE COTIZACION.\n Detalles: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);           
        }
        return (error == null);
    }
    
    public cCotizacion_detalle(int id_cotizaciondetalle, int id_cotizacion, String categ_padre, float item, String descripcion, float cantidad, String id_unidad, float precio_unitario, float precio_total, int id_empresa, int id_usuario, String no_afecta_total) {
        this.id_cotizaciondetalle = id_cotizaciondetalle;
        this.id_cotizacion = id_cotizacion;
        this.categ_padre = categ_padre;
        this.item = item;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.id_unidad = id_unidad;
        this.precio_unitario = precio_unitario;
        this.precio_total = precio_total;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
        this.no_afecta_total = no_afecta_total;
    }

    public cCotizacion_detalle() {
    }

    public String getNo_afecta_total() {
        return no_afecta_total;
    }

    public void setNo_afecta_total(String no_afecta_total) {
        this.no_afecta_total = no_afecta_total;
    }
    
    public int getId_cotizaciondetalle() {
        return id_cotizaciondetalle;
    }

    public void setId_cotizaciondetalle(int id_cotizaciondetalle) {
        this.id_cotizaciondetalle = id_cotizaciondetalle;
    }

    public int getId_cotizacion() {
        return id_cotizacion;
    }

    public void setId_cotizacion(int id_cotizacion) {
        this.id_cotizacion = id_cotizacion;
    }

    public String getCateg_padre() {
        return categ_padre;
    }

    public void setCateg_padre(String categ_padre) {
        this.categ_padre = categ_padre;
    }

    public float getItem() {
        return item;
    }

    public void setItem(float item) {
        this.item = item;
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

    public String getId_unidad() {
        return id_unidad;
    }

    public void setId_unidad(String id_unidad) {
        this.id_unidad = id_unidad;
    }

    public float getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(float precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public float getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(float precio_total) {
        this.precio_total = precio_total;
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
