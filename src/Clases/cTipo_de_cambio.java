/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import static Clases.cUnidad_medida.error;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import javax.swing.JOptionPane;

/**
 *
 * @author ErCo
 */
public class cTipo_de_cambio {
    int id_tipodecambio;
    int id_moneda;
    float valor_compra;
    float valor_venta;
    String fecha;
    int id_empresa;
    int id_usuario;
    
    protected static String error;
    
    public boolean crear(int id_moneda, float valor_compra, float valor_venta, String fecha, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spTipo_de_cambio_crear(?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.FLOAT);
            st.registerOutParameter(3, Types.FLOAT);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.INTEGER);
            st.registerOutParameter(6, Types.INTEGER);
            
            st.setInt(1, id_moneda);
            st.setFloat(2, valor_compra);
            st.setFloat(3, valor_venta);
            st.setString(4, fecha);
            st.setInt(5, id_empresa);
            st.setInt(6, id_usuario);
            

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear el Tipo de Cambio");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al CREAR el Tipo de Cambio.\n Detalles: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean modificar(int id_tipodecambio, int id_moneda, float valor_compra, float valor_venta, String fecha, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spTipo_de_cambio_modificar(?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.INTEGER);
            st.registerOutParameter(3, Types.FLOAT);
            st.registerOutParameter(4, Types.FLOAT);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.INTEGER);
            st.registerOutParameter(7, Types.INTEGER);
            
            st.setInt(1, id_tipodecambio);
            st.setInt(2, id_moneda);
            st.setFloat(3, valor_compra);
            st.setFloat(4, valor_venta);
            st.setString(5, fecha);
            st.setInt(6, id_empresa);
            st.setInt(7, id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al MODIDIFAR el Tipo de Cambio");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al MODIFICAR el Tipo de Cambio.\n Detalles: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean eliminar(int id_tipodecambio) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spTipo_de_cambio_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_tipodecambio);
            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al ELIMINAR el Tipo de Cambio");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al ELIMINAR el Tipo de Cambio.\n Detalles: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);           
        }
        return (error == null);
    }

    public cTipo_de_cambio(int id_tipodecambio, int id_moneda, float valor_compra, float valor_venta, String fecha, int id_empresa, int id_usuario) {
        this.id_tipodecambio = id_tipodecambio;
        this.id_moneda = id_moneda;
        this.valor_compra = valor_compra;
        this.valor_venta = valor_venta;
        this.fecha = fecha;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cTipo_de_cambio() {
    }

    public int getId_tipodecambio() {
        return id_tipodecambio;
    }

    public void setId_tipodecambio(int id_tipodecambio) {
        this.id_tipodecambio = id_tipodecambio;
    }

    public int getId_moneda() {
        return id_moneda;
    }

    public void setId_moneda(int id_moneda) {
        this.id_moneda = id_moneda;
    }

    public float getValor_compra() {
        return valor_compra;
    }

    public void setValor_compra(float valor_compra) {
        this.valor_compra = valor_compra;
    }

    public float getValor_venta() {
        return valor_venta;
    }

    public void setValor_venta(float valor_venta) {
        this.valor_venta = valor_venta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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
