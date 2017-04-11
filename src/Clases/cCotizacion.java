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
import javax.swing.JOptionPane;

/**
 *
 * @author ErCo
 */
public class cCotizacion {

    int id_cotizacion;
    String fecha;
    String numero;
    String atencion;
    String proyecto;
    String ubicacion;
    String tiempo_duracion;
    float costo_neto;
    float gasto_gen_por;
    float gasto_gen_monto;
    float utilidad_por;
    float utilidad_monto;
    float subtotal;
    float descuento_por;
    float descuento_monto;
    float subtotal_neto;
    float igv_monto;
    float total;
    String total_letras;
    int id_cliente;
    int id_igv;
    int id_moneda;
    int id_tipocotizacion;
    int id_documento;
    int id_formapago;
    int id_empresa;
    int id_usuario;
    String aprobado;
    String aprobado_fecha;
    String aprobado_persona;
    String aprobado_area;
    int aprobado_id_usuario;
    
    String rechazado;
    String rechazado_fecha;
    String rechazado_persona;
    String rechazado_area;
    String rechazado_motivo;
    int rechazado_id_usuario;

    protected static String error;

    public boolean crear(int id_empresa, int id_usuario) {
        try {
            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spCotizacion_crear(?,?)}");
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
            System.out.println("se ejecuto el error al crear la COTIZACION");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al CREAR la COTIZACION.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean modificar(int id_cotizacion, String fecha, String numero, String atencion, String proyecto, String ubicacion, String tiempo_duracion, float costo_neto, float gasto_gen_por, float gasto_gen_monto, float utilidad_por, float utilidad_monto, float subtotal, float descuento_por, float descuento_monto, float subtotal_neto, float igv_monto, float total, String total_letras, int id_cliente, int id_igv, int id_moneda, int id_tipocotizacion, int id_documento, int id_formapago, int id_empresa, int id_usuario) {
        try {

            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spCotizacion_modificar(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.VARCHAR);
            st.registerOutParameter(7, Types.VARCHAR);
            st.registerOutParameter(8, Types.FLOAT);
            st.registerOutParameter(9, Types.FLOAT);
            st.registerOutParameter(10, Types.FLOAT);
            st.registerOutParameter(11, Types.FLOAT);
            st.registerOutParameter(12, Types.FLOAT);
            st.registerOutParameter(13, Types.FLOAT);
            st.registerOutParameter(14, Types.FLOAT);
            st.registerOutParameter(15, Types.FLOAT);
            st.registerOutParameter(16, Types.FLOAT);
            st.registerOutParameter(17, Types.FLOAT);
            st.registerOutParameter(18, Types.FLOAT);
            st.registerOutParameter(19, Types.VARCHAR);
            st.registerOutParameter(20, Types.INTEGER);
            st.registerOutParameter(21, Types.INTEGER);
            st.registerOutParameter(22, Types.INTEGER);
            st.registerOutParameter(23, Types.INTEGER);
            st.registerOutParameter(24, Types.INTEGER);
            st.registerOutParameter(25, Types.INTEGER);
            st.registerOutParameter(26, Types.INTEGER);
            st.registerOutParameter(27, Types.INTEGER);

            st.setInt(1, id_cotizacion);
            st.setString(2, fecha);
            st.setString(3, numero);
            st.setString(4, atencion);
            st.setString(5, proyecto);
            st.setString(6, ubicacion);
            st.setString(7, tiempo_duracion);
            st.setFloat(8, costo_neto);
            st.setFloat(9, gasto_gen_por);
            st.setFloat(10, gasto_gen_monto);
            st.setFloat(11, utilidad_por);
            st.setFloat(12, utilidad_monto);
            st.setFloat(13, subtotal);
            st.setFloat(14, descuento_por);
            st.setFloat(15, descuento_monto);
            st.setFloat(16, subtotal_neto);
            st.setFloat(17, igv_monto);
            st.setFloat(18, total);
            st.setString(19, total_letras);
            st.setInt(20, id_cliente);
            st.setInt(21, id_igv);
            st.setInt(22, id_moneda);
            st.setInt(23, id_tipocotizacion);
            st.setInt(24, id_documento);
            st.setInt(25, id_formapago);
            st.setInt(26, id_empresa);
            st.setInt(27, id_usuario);


            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al MODIFICAR la COTIZACION");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al MODIFICAR la COTIZACION.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean eliminar(int id_cotizacion) {
        try {
            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }
            CallableStatement st = con.prepareCall("{CALL spCotizacion_eliminar(?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.setInt(1, id_cotizacion);
            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al ELIMINAR la COTIZACION");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al ELIMINAR la COTIZACION.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
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

            sql = "SELECT numero from TCotizacion where id_empresa='" + id_empresa + "' order by numero";
            System.out.println("enviando consulta: " + sql);
            PreparedStatement st = con.prepareStatement(sql, 1005, 1007);
            ResultSet rs = st.executeQuery();
            rs.afterLast();
            if (rs.previous()) {
                vcorre = Integer.parseInt(rs.getString("numero"));
                vcorre++;
            }
            for (int i = 1; i < 7 - String.valueOf(vcorre).length(); i++) {
                vCeros = vCeros + "0";
            }
            rs.close();
            st.close();
            con.close();
            System.out.println("Numero de Cotizacion generada = " + vCeros + vcorre);

        } catch (Exception e) {
            e.getMessage();
            System.out.println(e.getMessage());
        }
        return (vCeros + vcorre);
    }

    public boolean aprobar(int id_cotizacion, String aprobado, String aprobado_fecha, String aprobado_persona, String aprobado_area, int aprobado_id_usuario) {
        try {

            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spCotizacion_aprobar(?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.INTEGER);

            st.setInt(1, id_cotizacion);
            st.setString(2, aprobado);
            st.setString(3, aprobado_fecha);
            st.setString(4, aprobado_persona);
            st.setString(5, aprobado_area);
            st.setInt(6, aprobado_id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al APROBAR la COTIZACION");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al APROBAR la COTIZACION.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public boolean rechazar(int id_cotizacion, String rechazado, String rechazado_fecha, String rechazado_persona, String rechazado_area, String rechazado_motivo, int rechazado_id_usuario) {
        try {

            error = null;
            Clases.cAccesoo dbm = new Clases.cAccesoo();
            Connection con = dbm.getConnection();
            if (con == null) {
                throw new NullPointerException(dbm.geterror());
            }

            CallableStatement st = con.prepareCall("{CALL spCotizacion_rechazar(?,?,?,?,?,?,?)}");
            st.registerOutParameter(1, Types.INTEGER);
            st.registerOutParameter(2, Types.VARCHAR);
            st.registerOutParameter(3, Types.VARCHAR);
            st.registerOutParameter(4, Types.VARCHAR);
            st.registerOutParameter(5, Types.VARCHAR);
            st.registerOutParameter(6, Types.VARCHAR);
            st.registerOutParameter(7, Types.INTEGER);

            st.setInt(1, id_cotizacion);
            st.setString(2, rechazado);
            st.setString(3, rechazado_fecha);
            st.setString(4, rechazado_persona);
            st.setString(5, rechazado_area);
            st.setString(6, rechazado_motivo);
            st.setInt(7, rechazado_id_usuario);

            if (st.execute()) {
                error = "error";
            }
            st.close();
            con.close();
            dbm.closeConnection(con);
        } catch (Exception e) {
            System.out.println("se ejecuto el error al RECHAZAR la COTIZACION");
            error = e.toString();
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrio al RECHAZAR la COTIZACION.\n Detalles: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return (error == null);
    }

    public cCotizacion(int id_cotizacion, String fecha, String numero, String atencion, String proyecto, String ubicacion, String tiempo_duracion, float costo_neto, float gasto_gen_por, float gasto_gen_monto, float utilidad_por, float utilidad_monto, float subtotal, float descuento_por, float descuento_monto, float subtotal_neto, float igv_monto, float total, String total_letras, int id_cliente, int id_igv, int id_moneda, int id_tipocotizacion, int id_documento, int id_formapago, int id_empresa, int id_usuario) {
        this.id_cotizacion = id_cotizacion;
        this.fecha = fecha;
        this.numero = numero;
        this.atencion = atencion;
        this.proyecto = proyecto;
        this.ubicacion = ubicacion;
        this.tiempo_duracion = tiempo_duracion;
        this.costo_neto = costo_neto;
        this.gasto_gen_por = gasto_gen_por;
        this.gasto_gen_monto = gasto_gen_monto;
        this.utilidad_por = utilidad_por;
        this.utilidad_monto = utilidad_monto;
        this.subtotal = subtotal;
        this.descuento_por = descuento_por;
        this.descuento_monto = descuento_monto;
        this.subtotal_neto = subtotal_neto;
        this.igv_monto = igv_monto;
        this.total = total;
        this.total_letras = total_letras;
        this.id_cliente = id_cliente;
        this.id_igv = id_igv;
        this.id_moneda = id_moneda;
        this.id_tipocotizacion = id_tipocotizacion;
        this.id_documento = id_documento;
        this.id_formapago = id_formapago;
        this.id_empresa = id_empresa;
        this.id_usuario = id_usuario;
    }

    //constructor de aprobado
    public cCotizacion(int id_cotizacion, String aprobado, String aprobado_fecha, String aprobado_persona, String aprobado_area, int aprobado_id_usuario) {
        this.id_cotizacion = id_cotizacion;
        this.aprobado = aprobado;
        this.aprobado_fecha = aprobado_fecha;
        this.aprobado_persona = aprobado_persona;
        this.aprobado_area = aprobado_area;
        this.aprobado_id_usuario = aprobado_id_usuario;
    }
    
    //constructor rechazado
    public cCotizacion(int id_cotizacion, String rechazado, String rechazado_fecha, String rechazado_persona, String rechazado_area, String rechazado_motivo, int rechazado_id_usuario) {
        this.id_cotizacion = id_cotizacion;
        this.rechazado = rechazado;
        this.rechazado_fecha = rechazado_fecha;
        this.rechazado_persona = rechazado_persona;
        this.rechazado_area = rechazado_area;
        this.rechazado_motivo = rechazado_motivo;
        this.rechazado_id_usuario = rechazado_id_usuario;
    }

    public cCotizacion() {
    }

    public String getRechazado() {
        return rechazado;
    }

    public void setRechazado(String rechazado) {
        this.rechazado = rechazado;
    }

    public String getRechazado_fecha() {
        return rechazado_fecha;
    }

    public void setRechazado_fecha(String rechazado_fecha) {
        this.rechazado_fecha = rechazado_fecha;
    }

    public String getRechazado_persona() {
        return rechazado_persona;
    }

    public void setRechazado_persona(String rechazado_persona) {
        this.rechazado_persona = rechazado_persona;
    }

    public String getRechazado_area() {
        return rechazado_area;
    }

    public void setRechazado_area(String rechazado_area) {
        this.rechazado_area = rechazado_area;
    }

    public String getRechazado_motivo() {
        return rechazado_motivo;
    }

    public void setRechazado_motivo(String rechazado_motivo) {
        this.rechazado_motivo = rechazado_motivo;
    }

    public int getRechazado_id_usuario() {
        return rechazado_id_usuario;
    }

    public void setRechazado_id_usuario(int rechazado_id_usuario) {
        this.rechazado_id_usuario = rechazado_id_usuario;
    }
    
    public String getAprobado() {
        return aprobado;
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }

    public String getAprobado_fecha() {
        return aprobado_fecha;
    }

    public void setAprobado_fecha(String aprobado_fecha) {
        this.aprobado_fecha = aprobado_fecha;
    }

    public String getAprobado_persona() {
        return aprobado_persona;
    }

    public void setAprobado_persona(String aprobado_persona) {
        this.aprobado_persona = aprobado_persona;
    }

    public String getAprobado_area() {
        return aprobado_area;
    }

    public void setAprobado_area(String aprobado_area) {
        this.aprobado_area = aprobado_area;
    }

    public int getAprobado_id_usuario() {
        return aprobado_id_usuario;
    }

    public void setAprobado_id_usuario(int aprobado_id_usuario) {
        this.aprobado_id_usuario = aprobado_id_usuario;
    }

    public int getId_cotizacion() {
        return id_cotizacion;
    }

    public void setId_cotizacion(int id_cotizacion) {
        this.id_cotizacion = id_cotizacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAtencion() {
        return atencion;
    }

    public void setAtencion(String atencion) {
        this.atencion = atencion;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTiempo_duracion() {
        return tiempo_duracion;
    }

    public void setTiempo_duracion(String tiempo_duracion) {
        this.tiempo_duracion = tiempo_duracion;
    }

    public float getCosto_neto() {
        return costo_neto;
    }

    public void setCosto_neto(float costo_neto) {
        this.costo_neto = costo_neto;
    }

    public float getGasto_gen_por() {
        return gasto_gen_por;
    }

    public void setGasto_gen_por(float gasto_gen_por) {
        this.gasto_gen_por = gasto_gen_por;
    }

    public float getGasto_gen_monto() {
        return gasto_gen_monto;
    }

    public void setGasto_gen_monto(float gasto_gen_monto) {
        this.gasto_gen_monto = gasto_gen_monto;
    }

    public float getUtilidad_por() {
        return utilidad_por;
    }

    public void setUtilidad_por(float utilidad_por) {
        this.utilidad_por = utilidad_por;
    }

    public float getUtilidad_monto() {
        return utilidad_monto;
    }

    public void setUtilidad_monto(float utilidad_monto) {
        this.utilidad_monto = utilidad_monto;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getDescuento_por() {
        return descuento_por;
    }

    public void setDescuento_por(float descuento_por) {
        this.descuento_por = descuento_por;
    }

    public float getDescuento_monto() {
        return descuento_monto;
    }

    public void setDescuento_monto(float descuento_monto) {
        this.descuento_monto = descuento_monto;
    }

    public float getSubtotal_neto() {
        return subtotal_neto;
    }

    public void setSubtotal_neto(float subtotal_neto) {
        this.subtotal_neto = subtotal_neto;
    }

    public float getIgv_monto() {
        return igv_monto;
    }

    public void setIgv_monto(float igv_monto) {
        this.igv_monto = igv_monto;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getTotal_letras() {
        return total_letras;
    }

    public void setTotal_letras(String total_letras) {
        this.total_letras = total_letras;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_igv() {
        return id_igv;
    }

    public void setId_igv(int id_igv) {
        this.id_igv = id_igv;
    }

    public int getId_moneda() {
        return id_moneda;
    }

    public void setId_moneda(int id_moneda) {
        this.id_moneda = id_moneda;
    }

    public int getId_tipocotizacion() {
        return id_tipocotizacion;
    }

    public void setId_tipocotizacion(int id_tipocotizacion) {
        this.id_tipocotizacion = id_tipocotizacion;
    }

    public int getId_documento() {
        return id_documento;
    }

    public void setId_documento(int id_documento) {
        this.id_documento = id_documento;
    }

    public int getId_formapago() {
        return id_formapago;
    }

    public void setId_formapago(int id_formapago) {
        this.id_formapago = id_formapago;
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
