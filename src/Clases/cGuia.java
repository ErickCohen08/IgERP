/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

/**
 *
 * @author ErCo
 */
public class cGuia {

    int id_guia;
    String numero_guia;
    String fecha;
    String motivo_traslado;
    String punto_partida;
    String punto_llegada;
    String impreso;
    String anulado;
    int id_documento;
    int id_cliente;
    int id_factura;
    int id_vehiculo;
    int id_empresatransporte;
    int id_conductor;
    int id_empresa;
    int id_usuario;
    protected static String error;

    public boolean crear(int id_empresa, int id_usuario) {
        try {
            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spGuia_crear(?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.INTEGER);

            st.setInt(1, id_empresa);
            st.setInt(2, id_usuario);


            if (st.execute()) {
                error = "error al crear la Guia";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al crear la Guia");
            error = e.toString();
            System.out.println(e.getMessage());
        }
        return (error == null);
    }

    public boolean modificar(int id_guia, String numero_guia, String fecha, String motivo_traslado, String punto_partida, String punto_llegada, int id_documento, int id_cliente, int id_factura, int id_vehiculo, int id_empresatransporte, int id_conductor, int id_empresa, int id_usuario) {
        try {

            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spGuia_modificar(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.VARCHAR);
            st.registerOutParameter(7, Types.INTEGER);
            st.registerOutParameter(8, Types.INTEGER);
            st.registerOutParameter(9, Types.INTEGER);
            st.registerOutParameter(10, Types.INTEGER);
            st.registerOutParameter(11, Types.INTEGER);
            st.registerOutParameter(12, Types.INTEGER);
            st.registerOutParameter(13, Types.INTEGER);
            st.registerOutParameter(14, Types.INTEGER);

            st.setInt(1, id_guia);
            st.setString(2, numero_guia);
            st.setString(3, fecha);
            st.setString(4, motivo_traslado);
            st.setString(5, punto_partida);
            st.setString(6, punto_llegada);
            st.setInt(7, id_documento);
            st.setInt(8, id_cliente);
            st.setInt(9, id_factura);
            st.setInt(10, id_vehiculo);
            st.setInt(11, id_empresatransporte);
            st.setInt(12, id_conductor);
            st.setInt(13, id_empresa);
            st.setInt(14, id_usuario);


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

    public boolean eliminar(int id_guia) {
        try {
            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spGuia_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_guia);
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

    public boolean imprimir(int id_guia) {
        try {

            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spGuia_imprimir(?)}");
            st.registerOutParameter(1, Types.INTEGER);

            st.setInt(1, id_guia);

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

    public boolean anular(int id_guia) {
        try {

            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spGuia_anular(?)}");
            st.registerOutParameter(1, Types.INTEGER);

            st.setInt(1, id_guia);

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

    public String Generar_Numero(int id_empresa) {

        int vcorre = 1;
        String sql, vCeros = "";
        try {
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            sql = "SELECT numero_guia from tguia where id_empresa='" + id_empresa + "' order by numero_guia";
            System.out.println("enviando consulta: " + sql);
            PreparedStatement st = con.prepareStatement(sql, 1005, 1007);
            ResultSet rs = st.executeQuery();
            rs.afterLast();
            if (rs.previous()) {
                vcorre = Integer.parseInt(rs.getString("numero_guia"));
                vcorre++;
            }
            for (int i = 1; i < 7 - String.valueOf(vcorre).length(); i++) {
                vCeros = vCeros + "0";
            }
            rs.close();
            st.close();
            con.close();
            System.out.println("Numero de Guia generada = " + vCeros + vcorre);

        } catch (Exception e) {
            e.getMessage();
            System.out.println(e.getMessage());
        }
        return (vCeros + vcorre);
    }

    public cGuia(int id_empresa, int id_usuario) {
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cGuia(int id_guia, String numero_guia, String fecha, String motivo_traslado, String punto_partida, String punto_llegada, int id_documento, int id_cliente, int id_factura, int id_vehiculo, int id_empresatransporte, int id_conductor, int id_empresa, int id_usuario) {
        this.id_guia = id_guia;
        this.numero_guia = numero_guia;
        this.fecha = fecha;
        this.motivo_traslado = motivo_traslado;
        this.punto_partida = punto_partida;
        this.punto_llegada = punto_llegada;
        this.id_documento = id_documento;
        this.id_cliente = id_cliente;
        this.id_factura = id_factura;
        this.id_vehiculo = id_vehiculo;
        this.id_empresatransporte = id_empresatransporte;
        this.id_conductor = id_conductor;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    public cGuia() {
    }

    public int getId_guia() {
        return id_guia;
    }

    public void setId_guia(int id_guia) {
        this.id_guia = id_guia;
    }

    public String getNumero_guia() {
        return numero_guia;
    }

    public void setNumero_guia(String numero_guia) {
        this.numero_guia = numero_guia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMotivo_traslado() {
        return motivo_traslado;
    }

    public void setMotivo_traslado(String motivo_traslado) {
        this.motivo_traslado = motivo_traslado;
    }

    public String getPunto_partida() {
        return punto_partida;
    }

    public void setPunto_partida(String punto_partida) {
        this.punto_partida = punto_partida;
    }

    public String getPunto_llegada() {
        return punto_llegada;
    }

    public void setPunto_llegada(String punto_llegada) {
        this.punto_llegada = punto_llegada;
    }

    public String getImpreso() {
        return impreso;
    }

    public void setImpreso(String impreso) {
        this.impreso = impreso;
    }

    public String getAnulado() {
        return anulado;
    }

    public void setAnulado(String anulado) {
        this.anulado = anulado;
    }

    public int getId_documento() {
        return id_documento;
    }

    public void setId_documento(int id_documento) {
        this.id_documento = id_documento;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public int getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(int id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public int getId_empresatransporte() {
        return id_empresatransporte;
    }

    public void setId_empresatransporte(int id_empresatransporte) {
        this.id_empresatransporte = id_empresatransporte;
    }

    public int getId_conductor() {
        return id_conductor;
    }

    public void setId_conductor(int id_conductor) {
        this.id_conductor = id_conductor;
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
