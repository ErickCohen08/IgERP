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
public class cVehiculo {
    int id_vehiculo;
    String marca;
    String placa;
    String modelo;
    String nro_inscripcion;
    int id_empresa;
    int id_usuario;
    protected static String error;
    
    public boolean crear(String marca, String placa, String modelo, String nro_inscripcion, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spVehiculo_crear(?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.VARCHAR);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.INTEGER);
            st.registerOutParameter(6, Types.INTEGER);

            st.setString(1, marca);
            st.setString(2, placa);
            st.setString(3, modelo);
            st.setString(4, nro_inscripcion);
            st.setInt(5, id_empresa);
            st.setInt(6, id_usuario);

            if (st.execute()) {
                error = "error al crear el vehiculo";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear el vehiculo");
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }

    public boolean modificar(int id_vehiculo, String marca, String placa, String modelo, String nro_inscripcion, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spVehiculo_modificar(?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.INTEGER);
            st.registerOutParameter(7, Types.INTEGER);

            st.setInt(1, id_vehiculo);
            st.setString(2, marca);
            st.setString(3, placa);
            st.setString(4, modelo);
            st.setString(5, nro_inscripcion);
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

    public boolean eliminar(int id_vehiculo) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spVehiculo_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_vehiculo);
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

    public int placa_existente(String placa, int id_empresa) {
        int cantidad = 1;
        String sql = "select count(id_vehiculo) from tvehiculo where placa='" + placa + "' and id_empresa='"+id_empresa+"'";

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
        System.out.println("La placa fue registrado: " + cantidad + " veces");

        return cantidad;
    }
    
    public int Numero_inscripcion_existente(String nro_inscripcion, int id_empresa) {
        int cantidad = 1;
        String sql = "select count(id_vehiculo) from tvehiculo where nro_inscripcion='" + nro_inscripcion + "' and id_empresa='"+id_empresa+"'";

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
        System.out.println("El Numero de Inscripcion fue registrado: " + cantidad + " veces");

        return cantidad;
    }
    
    public cVehiculo() {
    }

    public cVehiculo(String marca, String placa, String modelo, String nro_inscripcion, int id_empresa, int id_usuario) {
        this.marca = marca;
        this.placa = placa;
        this.modelo = modelo;
        this.nro_inscripcion = nro_inscripcion;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cVehiculo(int id_vehiculo, String marca, String placa, String modelo, String nro_inscripcion, int id_empresa, int id_usuario) {
        this.id_vehiculo = id_vehiculo;
        this.marca = marca;
        this.placa = placa;
        this.modelo = modelo;
        this.nro_inscripcion = nro_inscripcion;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public int getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(int id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNro_inscripcion() {
        return nro_inscripcion;
    }

    public void setNro_inscripcion(String nro_inscripcion) {
        this.nro_inscripcion = nro_inscripcion;
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
