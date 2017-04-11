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
public class cPersonal {

    int id_personal;
    String Nombre;
    String Apellido;
    String dni;
    String direccion;
    String sexo;
    String telefono;
    String celular;
    String cargo;
    String fecha_nacimiento;
    String nro_licencia;
    int id_empresa;
    int id_usuario;
    protected static String error;

    public boolean Personal_crear(String Nombre, String Apellido, String dni, String direccion, 
            String sexo, String telefono, String celular, String cargo, String fecha_nacimiento, 
            String nro_licencia, int id_empresa, int id_usuario) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spPersonal_crear(?,?,?,?,?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.VARCHAR);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.VARCHAR);
            st.registerOutParameter(7, Types.VARCHAR);
            st.registerOutParameter(8, Types.VARCHAR);
            st.registerOutParameter(9, Types.VARCHAR);
            st.registerOutParameter(10, Types.VARCHAR);
            st.registerOutParameter(11, Types.INTEGER);
            st.registerOutParameter(12, Types.INTEGER);

            st.setString(1, Nombre);
            st.setString(2, Apellido);
            st.setString(3, dni);
            st.setString(4, direccion);
            st.setString(5, sexo);
            st.setString(6, telefono);
            st.setString(7, celular);
            st.setString(8, cargo);
            st.setString(9, fecha_nacimiento);
            st.setString(10, nro_licencia);
            st.setInt(11, id_empresa);
            st.setInt(12, id_usuario);
            
             
            if (st.execute()) {
                error = "error al crear el personal";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear el personal");
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }

    public boolean Personal_modificar(int id_personal, String Nombre, String Apellido, String dni, 
            String direccion, String sexo, String telefono, String celular, String cargo, 
            String fecha_nacimiento, String nro_licencia, int id_empresa, int id_usuario) {
        try {
            
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            
            CallableStatement st = con.prepareCall("{CALL spPersonal_modificar(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.VARCHAR);
            st.registerOutParameter(7, Types.VARCHAR);
            st.registerOutParameter(8, Types.VARCHAR);
            st.registerOutParameter(9, Types.VARCHAR);
            st.registerOutParameter(10, Types.VARCHAR);
            st.registerOutParameter(11, Types.VARCHAR);
            st.registerOutParameter(12, Types.INTEGER);
            st.registerOutParameter(13, Types.INTEGER);

            st.setInt(1, id_personal);
            st.setString(2, Nombre);
            st.setString(3, Apellido);
            st.setString(4, dni);
            st.setString(5, direccion);
            st.setString(6, sexo);
            st.setString(7, telefono);
            st.setString(8, celular);
            st.setString(9, cargo);
            st.setString(10, fecha_nacimiento);
            st.setString(11, nro_licencia);
            st.setInt(12, id_empresa);
            st.setInt(13, id_usuario);


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

    public boolean Personal_eliminar(int id_personal) {
        try {
            error = null;
            cAccesoo dbm = new cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spPersonal_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_personal);
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

    public int dni_existente(String dni, int id_empresa) {
        int cantidad = 1;
        String sql = "select count(id_personal) from tpersonal where dni='" + dni + "' and id_empresa='" + id_empresa + "'";

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
        System.out.println("La DNI fue registrado: " + cantidad + " veces");

        return cantidad;
    }

    public int nro_licencia_existente(String nro_licencia, int id_empresa) {
        int cantidad = 1;
        String sql = "select count(id_personal) from tpersonal where nro_licencia='" + nro_licencia + "' and id_empresa='" + id_empresa + "'";

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
        System.out.println("El nro_licencia fue registrado: " + cantidad + " veces");

        return cantidad;
    }

    public cPersonal() {
    }

    public cPersonal(String Nombre, String Apellido, String dni, String direccion, String sexo, String telefono, String celular, String cargo, String fecha_nacimiento, String nro_licencia, int id_empresa, int id_usuario) {
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.dni = dni;
        this.direccion = direccion;
        this.sexo = sexo;
        this.telefono = telefono;
        this.celular = celular;
        this.cargo = cargo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.nro_licencia = nro_licencia;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cPersonal(int id_personal, String Nombre, String Apellido, String dni, String direccion, String sexo, String telefono, String celular, String cargo, String fecha_nacimiento, String nro_licencia, int id_empresa, int id_usuario) {
        this.id_personal = id_personal;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.dni = dni;
        this.direccion = direccion;
        this.sexo = sexo;
        this.telefono = telefono;
        this.celular = celular;
        this.cargo = cargo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.nro_licencia = nro_licencia;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public int getId_personal() {
        return id_personal;
    }

    public void setId_personal(int id_personal) {
        this.id_personal = id_personal;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getNro_licencia() {
        return nro_licencia;
    }

    public void setNro_licencia(String nro_licencia) {
        this.nro_licencia = nro_licencia;
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
