/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.AccesoDB;
import entity.StandBE;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import service.ICrudService;

/**
 *
 * @author ERCO
 */
public class StandDAO implements ICrudService<StandBE> {

    // variables
    Connection cn = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    StringBuilder sqlB = null;

    @Override
    public int create(StandBE o) throws Exception {
        sqlB = new StringBuilder();
        sqlB.setLength(0);
        sqlB.append("INSERT INTO TStand( ");
        sqlB.append("IdAlmacen, ");
        sqlB.append("Nombre, ");
        sqlB.append("UsuarioInserta, ");
        sqlB.append("FechaInserta, ");
        sqlB.append("id_empresa ");
        sqlB.append(") VALUES( ?,  ?,  ?, GETDATE(),  ?) ");

        cn = AccesoDB.getConnection();
        ps = cn.prepareStatement(sqlB.toString());
        ps.clearParameters();
        ps.setInt(1, o.getIdAlmacen());
        ps.setString(2, o.getNombre());
        ps.setString(3, o.getUsuarioDes());
        ps.setInt(4, o.getIdEmpresa());
        int resp = ps.executeUpdate();

        ps.close();
        cn.close();
        return resp;
    }

    @Override
    public int update(StandBE o) throws Exception {
        sqlB = new StringBuilder();
        sqlB.setLength(0);
        sqlB.append("UPDATE TStand SET ");
        sqlB.append("IdAlmacen = ?, ");
        sqlB.append("Nombre = ?, ");
        sqlB.append("UsuarioModifica = ?, ");
        sqlB.append("FechaModifica = GETDATE(), ");
        sqlB.append("id_empresa = ? ");
        sqlB.append("WHERE IdStand = ? ");

        cn = AccesoDB.getConnection();
        ps = cn.prepareStatement(sqlB.toString());
        ps.clearParameters();
        ps.setInt(1, o.getIdAlmacen());
        ps.setString(2, o.getNombre());
        ps.setString(3, o.getUsuarioDes());
        ps.setInt(4, o.getIdEmpresa());
        ps.setInt(5, o.getIdStand());
        int resp = ps.executeUpdate();

        ps.close();
        cn.close();
        return resp;
    }

    @Override
    public int delete(StandBE o) throws Exception {
        sqlB = new StringBuilder();
        sqlB.setLength(0);
        sqlB.append("DELETE FROM TStand ");
        sqlB.append("WHERE IdStand = ? ");

        cn = AccesoDB.getConnection();
        ps = cn.prepareStatement(sqlB.toString());
        ps.clearParameters();
        ps.setInt(1, o.getIdStand());
        int resp = ps.executeUpdate();

        ps.close();
        cn.close();
        return resp;
    }

    @Override
    public List<StandBE> read(StandBE pbe) throws Exception {
        List<StandBE> lista = new ArrayList();

        sqlB = new StringBuilder();
        sqlB.setLength(0);
        sqlB.append("SELECT ");
        sqlB.append("ST.IdStand, ");
        sqlB.append("ST.IdAlmacen, ");
        sqlB.append("ST.Nombre, ");
        sqlB.append("ST.UsuarioInserta, ");
        sqlB.append("ST.FechaInserta, ");
        sqlB.append("ST.UsuarioModifica, ");
        sqlB.append("ST.FechaModifica, ");
        sqlB.append("ST.id_empresa, ");
        sqlB.append("dc.DescripcionCorta as nombreAlmacen ");
        sqlB.append("FROM TStand ST ");
        sqlB.append("INNER JOIN TDatoComun DC ");
        sqlB.append("ON DC.IdDatoComun = ST.IdAlmacen ");
        sqlB.append("ORDER BY DC.DescripcionCorta ASC, Nombre ASC ");

        cn = AccesoDB.getConnection();
        ps = cn.prepareStatement(sqlB.toString());
        ps.clearParameters();
        rs = ps.executeQuery();

        while (rs.next()) {
            lista.add(cargaReadBE(rs));
        }

        rs.close();
        ps.close();
        cn.close();

        return lista;
    }

    private StandBE cargaReadBE(ResultSet rs) throws SQLException {
        StandBE be = new StandBE();
        be.setIdStand(rs.getInt("IdStand"));
        be.setIdAlmacen(rs.getInt("IdAlmacen"));
        be.setNombre(rs.getString("Nombre"));
        be.setUsuarioInserta(rs.getString("UsuarioInserta"));
        be.setFechaInserta(rs.getDate("FechaInserta"));
        be.setUsuarioModifica(rs.getString("UsuarioModifica"));
        be.setFechaModifica(rs.getDate("FechaModifica"));
        be.setIdEmpresa(rs.getInt("id_empresa"));
        be.setNombreAlmacen(rs.getString("nombreAlmacen"));
        return be;
    }

    @Override
    public StandBE readId(int id) throws Exception {
        sqlB = new StringBuilder();
        sqlB.setLength(0);
        sqlB.append("SELECT ");
        sqlB.append("ST.IdStand, ");
        sqlB.append("ST.IdAlmacen, ");
        sqlB.append("ST.Nombre, ");
        sqlB.append("ST.UsuarioInserta, ");
        sqlB.append("ST.FechaInserta, ");
        sqlB.append("ST.UsuarioModifica, ");
        sqlB.append("ST.FechaModifica, ");
        sqlB.append("ST.id_empresa, ");
        sqlB.append("dc.DescripcionCorta as nombreAlmacen ");
        sqlB.append("FROM TStand ST ");
        sqlB.append("INNER JOIN TDatoComun DC ");
        sqlB.append("ON DC.IdDatoComun = ST.IdAlmacen ");
        sqlB.append("WHERE IdStand = ? ");
        sqlB.append("ORDER BY DC.DescripcionCorta ASC, Nombre ASC ");

        cn = AccesoDB.getConnection();
        ps = cn.prepareStatement(sqlB.toString());
        ps.clearParameters();
        ps.setInt(1, id);
        rs = ps.executeQuery();

        StandBE obj = null;
        if (rs.next()) {
            obj = cargaReadBE(rs);
        }

        rs.close();
        ps.close();
        cn.close();
        return obj;
    }

    public List<StandBE> readByAlmacen(int id) throws Exception {
        List<StandBE> lista = new ArrayList();

        sqlB = new StringBuilder();
        sqlB.setLength(0);
        sqlB.append("SELECT ");
        sqlB.append("ST.IdStand, ");
        sqlB.append("ST.IdAlmacen, ");
        sqlB.append("ST.Nombre, ");
        sqlB.append("ST.UsuarioInserta, ");
        sqlB.append("ST.FechaInserta, ");
        sqlB.append("ST.UsuarioModifica, ");
        sqlB.append("ST.FechaModifica, ");
        sqlB.append("ST.id_empresa, ");
        sqlB.append("dc.DescripcionCorta as nombreAlmacen ");
        sqlB.append("FROM TStand ST ");
        sqlB.append("INNER JOIN TDatoComun DC ");
        sqlB.append("ON DC.IdDatoComun = ST.IdAlmacen ");
        sqlB.append("WHERE ST.IdAlmacen = ? ");
        sqlB.append("ORDER BY DC.DescripcionCorta ASC, Nombre ASC ");

        cn = AccesoDB.getConnection();
        ps = cn.prepareStatement(sqlB.toString());
        ps.clearParameters();
        ps.setInt(1, id);
        rs = ps.executeQuery();

        while (rs.next()) {
            lista.add(cargaReadBE(rs));
        }

        rs.close();
        ps.close();
        cn.close();

        return lista;
    }
}
