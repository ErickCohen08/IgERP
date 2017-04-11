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
public class cProducto {

    int id_producto;
    String codigo;
    String descripcion;
    String modelo;
    float peso;
    String marca;
    int id_unidad;
    int id_productotipo;
    int id_empresa;
    int id_usuario;
    int guardado;
    int id_moneda;
    float precio_promedio;
    
    float precio_manoobra;
    float precio_material;
    float precio_equipo;
    
    String referencia_precio;
    String descripcion_coloquial;

    
    
    protected static String error;

    public boolean crear(int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spProducto_crear(?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.INTEGER);

            st.setInt(1, id_empresa);
            st.setInt(2, id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear el Producto");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al CREAR el Producto.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean modificar(int id_producto, String codigo, String descripcion, String modelo, float peso, String marca, int id_unidad, int id_productotipo, int id_empresa, int id_usuario, int guardado, int id_moneda, float precio_promedio, float precio_manoobra, float precio_material, float precio_equipo, String referencia_precio, String descripcion_coloquial) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spProducto_modificar(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.FLOAT);
            st.registerOutParameter(6, Types.VARCHAR);
            st.registerOutParameter(7, Types.INTEGER);
            st.registerOutParameter(8, Types.INTEGER);
            st.registerOutParameter(9, Types.INTEGER);
            st.registerOutParameter(10, Types.INTEGER);
            st.registerOutParameter(11, Types.INTEGER);
            st.registerOutParameter(12, Types.INTEGER);
            st.registerOutParameter(13, Types.FLOAT);
            st.registerOutParameter(14, Types.FLOAT);
            st.registerOutParameter(15, Types.FLOAT);
            st.registerOutParameter(16, Types.FLOAT);
            st.registerOutParameter(17, Types.VARCHAR);
            st.registerOutParameter(18, Types.VARCHAR);
            
            
            st.setInt(1, id_producto);
            st.setString(2, codigo);
            st.setString(3, descripcion);
            st.setString(4, modelo);
            st.setFloat(5, peso);
            st.setString(6, marca);
            st.setInt(7, id_unidad);
            st.setInt(8, id_productotipo);
            st.setInt(9, id_empresa);
            st.setInt(10, id_usuario);
            st.setInt(11, guardado);
            st.setInt(12, id_moneda);
            st.setFloat(13, precio_promedio);
            st.setFloat(14, precio_manoobra);
            st.setFloat(15, precio_material);
            st.setFloat(16, precio_equipo);
            st.setString(17, referencia_precio);
            st.setString(18, descripcion_coloquial);
            
            
            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al MODIDIFAR el Producto");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al MODIFICAR el Producto.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean eliminar(int id_producto) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spProducto_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_producto);
            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al ELIMINAR el Producto");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al ELIMINAR el Producto.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public int descripcion_existente(String descripcion) {
        int cantidad = 1;
        String sql = "select count(id_producto) from TProducto where descripcion='" + descripcion + "'";
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
        System.out.println("La descripcion del Producto fue registrado: " + cantidad + " veces");

        return cantidad;
    }

    public int codigo_existente(String codigo) {
        int cantidad = 1;
        String sql = "select count(id_producto) from TProducto where codigo='" + codigo + "'";
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
        System.out.println("El Codigo del Producto fue registrado: " + cantidad + " veces");

        return cantidad;
    }

    public cProducto(int id_producto, String codigo, String descripcion, String modelo, float peso, String marca, int id_unidad, int id_productotipo, int id_empresa, int id_usuario, int guardado, int id_moneda, float precio_promedio, float precio_manoobra, float precio_material, float precio_equipo, String referencia_precio, String descripcion_coloquial) {
        this.id_producto = id_producto;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.modelo = modelo;
        this.peso = peso;
        this.marca = marca;
        this.id_unidad = id_unidad;
        this.id_productotipo = id_productotipo;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
        this.guardado = guardado;
        this.id_moneda = id_moneda;
        this.precio_promedio = precio_promedio;
        this.precio_manoobra = precio_manoobra;
        this.precio_material = precio_material;
        this.precio_equipo = precio_equipo;
        this.referencia_precio = referencia_precio;
        this.descripcion_coloquial = descripcion_coloquial;
    }
    
    public cProducto() {
    }

    

    public String getReferencia_precio() {
        return referencia_precio;
    }

    public void setReferencia_precio(String referencia_precio) {
        this.referencia_precio = referencia_precio;
    }

    public String getDescripcion_coloquial() {
        return descripcion_coloquial;
    }

    public void setDescripcion_coloquial(String descripcion_coloquial) {
        this.descripcion_coloquial = descripcion_coloquial;
    }
    
    public float getPrecio_manoobra() {
        return precio_manoobra;
    }

    public void setPrecio_manoobra(float precio_manoobra) {
        this.precio_manoobra = precio_manoobra;
    }

    public float getPrecio_material() {
        return precio_material;
    }

    public void setPrecio_material(float precio_material) {
        this.precio_material = precio_material;
    }

    public float getPrecio_equipo() {
        return precio_equipo;
    }

    public void setPrecio_equipo(float precio_equipo) {
        this.precio_equipo = precio_equipo;
    }

    public int getId_moneda() {
        return id_moneda;
    }

    public void setId_moneda(int id_moneda) {
        this.id_moneda = id_moneda;
    }

    public float getPrecio_promedio() {
        return precio_promedio;
    }

    public void setPrecio_promedio(float precio_promedio) {
        this.precio_promedio = precio_promedio;
    }
    
    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getId_unidad() {
        return id_unidad;
    }

    public void setId_unidad(int id_unidad) {
        this.id_unidad = id_unidad;
    }

    public int getId_productotipo() {
        return id_productotipo;
    }

    public void setId_productotipo(int id_productotipo) {
        this.id_productotipo = id_productotipo;
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

    public int getGuardado() {
        return guardado;
    }

    public void setGuardado(int guardado) {
        this.guardado = guardado;
    }
}
