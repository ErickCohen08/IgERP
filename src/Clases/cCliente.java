package Clases;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

public class cCliente {
    
    int id_cliente;
    String razon_social;
    String ruc;
    String direccion;
    String telefono;
    String celular;
    String correo;
    int id_empresa;
    int id_usuario;
    protected static String error;
    
    public boolean Cliente_crear(String razon_social, String ruc, String direccion, String telefono, String celular, String correo, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spCliente_crear(?,?,?,?,?,?,?,?)}");
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
                error = "error al crear el cliente";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear el cliente");
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }

    public boolean Cliente_modificar(int id_cliente, String razon_social, String ruc, String direccion, String telefono, String celular, String correo, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spCliente_modificar(?,?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.VARCHAR);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.VARCHAR);
            st.registerOutParameter(7, Types.VARCHAR);
            st.registerOutParameter(8, Types.INTEGER);
            st.registerOutParameter(9, Types.INTEGER);

            st.setInt(1, id_cliente);
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

    public boolean Cliente_eliminar(int id_cliente) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spCliente_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_cliente);
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
        String sql = "select count(id_cliente) from tcliente where razon_social='" + razon_social + "' and id_empresa='"+id_empresa+"'";

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
        String sql = "select count(id_cliente) from tcliente where ruc='" + ruc + "' and id_empresa='"+id_empresa+"'";

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

    public cCliente() {
    }

    public cCliente(String razon_social, String ruc, String direccion, String telefono, String celular, String correo, int id_empresa, int id_usuario) {
        this.razon_social = razon_social;
        this.ruc = ruc;
        this.direccion = direccion;
        this.telefono = telefono;
        this.celular = celular;
        this.correo = correo;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cCliente(int id_cliente, String razon_social, String ruc, String direccion, String telefono, String celular, String correo, int id_empresa, int id_usuario) {
        this.id_cliente = id_cliente;
        this.razon_social = razon_social;
        this.ruc = ruc;
        this.direccion = direccion;
        this.telefono = telefono;
        this.celular = celular;
        this.correo = correo;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
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
