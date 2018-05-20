/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.AccesoDB;
import entity.DatoComunBE;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import service.DatoComunService;
import service.ICrudService;

/**
 *
 * @author admin
 */
public class DatoComunDAO implements ICrudService<DatoComunBE>, DatoComunService<DatoComunBE> {

    // variables
    Connection cn = null;
    ResultSet rs = null;
    CallableStatement cs = null;
    PreparedStatement ps = null;
    Statement stm = null;
    final String READDETALLE = "{call usp_DatoComun_ReadDetalle(?)}";
    final String READTABLA = "{call usp_DatoComun_ReadTabla(?)}";
    final String READALL = "{call usp_DatoComun_ReadAll(?)}";
    final String READID = "{call usp_DatoComun_ReadId(?)}";
    final String INSERT = "{call usp_DataComun_create(?,?,?,?,?,?,?,?)}";

    String sql = "";
    StringBuilder sqlB = null;

    @Override
    public int create(DatoComunBE o) throws Exception {
        int id_datocomun = 0;

        try {
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(INSERT);

            //valores de los parametros del sp
            cs.setInt(1, o.getIdDatoComun());
            cs.setInt(2, o.getCodigoTabla());
            cs.setInt(3, o.getCodigoFila());
            cs.setString(4, o.getDescripcionCorta());
            cs.setString(5, o.getDescripcionLarga());
            cs.setString(6, o.getValorTexto1());
            cs.setString(7, o.getValorTexto2());
            cs.setString(8, o.getUsuarioInserta());

            //ejecutar el sp
            rs = cs.executeQuery();

            while (rs.next()) {
                id_datocomun = rs.getInt("IdDatoComun");
            }
            rs.close();
            cs.close();

        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw e;
        } finally {
            cn.close();
        }

        return id_datocomun;
    }

    @Override
    public int update(DatoComunBE o) throws Exception {
        return 0;
    }

    public int updateAlmacen(DatoComunBE o) throws Exception {
        sqlB = new StringBuilder();
        sqlB.setLength(0);
        sqlB.append("UPDATE TDatoComun SET ");
        sqlB.append("DescripcionCorta = ?, ");
        sqlB.append("ValorTexto1 = ?, ");
        sqlB.append("UsuarioModifica = ?, ");
        sqlB.append("FechaModifica = GETDATE() ");
        sqlB.append("WHERE IdDatoComun = ? ");

        cn = AccesoDB.getConnection();
        ps = cn.prepareStatement(sqlB.toString());
        ps.clearParameters();
        ps.setString(1, o.getDescripcionCorta());
        ps.setString(2, o.getValorTexto1());
        ps.setString(3, o.getUsuarioDes());
        ps.setInt(4, o.getIdDatoComun());
        int resp = ps.executeUpdate();

        ps.close();
        cn.close();
        return resp;
    }

    @Override
    public int delete(DatoComunBE o) throws Exception {
        sqlB = new StringBuilder();
        sqlB.setLength(0);
        sqlB.append("DELETE FROM TDatoComun ");
        sqlB.append("WHERE IdDatoComun = ?");

        cn = AccesoDB.getConnection();
        ps = cn.prepareStatement(sqlB.toString());
        ps.clearParameters();
        ps.setInt(1, o.getIdDatoComun());
        int resp = ps.executeUpdate();

        ps.close();
        cn.close();
        return resp;
    }

    @Override
    public List<DatoComunBE> read(DatoComunBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DatoComunBE> readDetalle(int CodigoTabla) throws Exception {
        List<DatoComunBE> lista = new ArrayList();

        try {
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(READDETALLE);
            cs.setInt(1, CodigoTabla);

            rs = cs.executeQuery();
            while (rs.next()) {
                DatoComunBE emp = new DatoComunBE();

                emp.setIdDatoComun(rs.getInt("IdDatoComun"));
                emp.setCodigoTabla(rs.getInt("CodigoTabla"));
                emp.setCodigoFila(rs.getInt("CodigoFila"));
                emp.setDescripcionCorta(rs.getString("DescripcionCorta"));
                emp.setDescripcionLarga(rs.getString("DescripcionLarga"));
                emp.setValorTexto1(rs.getString("ValorTexto1"));
                emp.setValorTexto2(rs.getString("ValorTexto2"));
                emp.setEstado(rs.getString("Estado"));
                emp.setIdDatoComunDescripcionCorta(rs.getInt("IdDatoComun") + "|" + rs.getString("DescripcionCorta"));
                lista.add(emp);
            }

            rs.close();
            cs.close();
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw e;
        } finally {
            cn.close();
        }

        return lista;
    }

    @Override
    public List<DatoComunBE> readTabla(DatoComunBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DatoComunBE> readAll(DatoComunBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DatoComunBE readId(int Id) throws Exception {
        sqlB = new StringBuilder();
        sqlB.setLength(0);
        sqlB.append("SELECT ");
        sqlB.append("IdDatoComun, ");
        sqlB.append("CodigoTabla, ");
        sqlB.append("CodigoFila, ");
        sqlB.append("DescripcionCorta, ");
        sqlB.append("DescripcionLarga, ");
        sqlB.append("ValorTexto1, ");
        sqlB.append("ValorTexto2, ");
        sqlB.append("UsuarioInserta, ");
        sqlB.append("FechaInserta, ");
        sqlB.append("UsuarioModifica, ");
        sqlB.append("FechaModifica, ");
        sqlB.append("Estado, ");
        sqlB.append("id_empresa ");
        sqlB.append("FROM TDatoComun ");
        sqlB.append("WHERE IdDatoComun = ?");

        cn = AccesoDB.getConnection();
        ps = cn.prepareStatement(sqlB.toString());
        ps.clearParameters();
        ps.setInt(1, Id);
        rs = ps.executeQuery();

        DatoComunBE obj = null;
        if (rs.next()) {
            obj = new DatoComunBE();
            obj.setIdDatoComun(rs.getInt("IdDatoComun"));
            obj.setCodigoTabla(rs.getInt("CodigoTabla"));
            obj.setCodigoFila(rs.getInt("CodigoFila"));
            obj.setDescripcionCorta(rs.getString("DescripcionCorta"));
            obj.setDescripcionLarga(rs.getString("DescripcionLarga"));
            obj.setValorTexto1(rs.getString("ValorTexto1"));
            obj.setValorTexto2(rs.getString("ValorTexto2"));
            obj.setUsuarioInserta(rs.getString("UsuarioInserta"));
            obj.setFechaInserta(rs.getDate("FechaInserta"));
            obj.setUsuarioModifica(rs.getString("UsuarioModifica"));
            obj.setFechaModifica(rs.getDate("FechaModifica"));
            obj.setEstado(rs.getString("Estado"));
            obj.setId_empresa(rs.getInt("id_empresa"));
        }

        rs.close();
        ps.close();
        cn.close();
        return obj;
    }
}
