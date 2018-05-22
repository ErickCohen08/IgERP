/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.AccesoDB;
import entity.NivelBE;
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
public class NivelDAO implements ICrudService<NivelBE> {

    // variables
    Connection cn = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    StringBuilder sqlB = null;

    @Override
    public int create(NivelBE o) throws Exception {
        sqlB = new StringBuilder();
        sqlB.setLength(0);
        sqlB.append("INSERT INTO TNivel( ");
        sqlB.append("IdStand, ");
        sqlB.append("Nombre, ");
        sqlB.append("UsuarioInserta, ");
        sqlB.append("FechaInserta, ");
        sqlB.append("id_empresa ");
        sqlB.append(") VALUES( ?,  ?,  ?, GETDATE(),  ?) ");

        cn = AccesoDB.getConnection();
        ps = cn.prepareStatement(sqlB.toString());
        ps.clearParameters();
        ps.setInt(1, o.getIdStand());
        ps.setString(2, o.getNombre());
        ps.setString(3, o.getUsuarioDes());
        ps.setInt(4, o.getIdEmpresa());
        int resp = ps.executeUpdate();

        ps.close();
        cn.close();
        return resp;
    }

    @Override
    public int update(NivelBE o) throws Exception {
        sqlB = new StringBuilder();
        sqlB.setLength(0);
        sqlB.append("UPDATE TNivel SET ");
        sqlB.append("IdStand = ?, ");
        sqlB.append("Nombre = ?, ");
        sqlB.append("UsuarioModifica = ?, ");
        sqlB.append("FechaModifica = GETDATE(), ");
        sqlB.append("id_empresa = ? ");
        sqlB.append("WHERE IdNivel = ? ");

        cn = AccesoDB.getConnection();
        ps = cn.prepareStatement(sqlB.toString());
        ps.clearParameters();
        ps.setInt(1, o.getIdStand());
        ps.setString(2, o.getNombre());
        ps.setString(3, o.getUsuarioDes());
        ps.setInt(4, o.getIdEmpresa());
        ps.setInt(5, o.getIdNivel());
        int resp = ps.executeUpdate();

        ps.close();
        cn.close();
        return resp;
    }

    @Override
    public int delete(NivelBE o) throws Exception {
        sqlB = new StringBuilder();
        sqlB.setLength(0);
        sqlB.append("DELETE FROM TNivel ");
        sqlB.append("WHERE IdNivel = ? ");

        cn = AccesoDB.getConnection();
        ps = cn.prepareStatement(sqlB.toString());
        ps.clearParameters();
        ps.setInt(1, o.getIdNivel());
        int resp = ps.executeUpdate();

        ps.close();
        cn.close();
        return resp;
    }

    @Override
    public List<NivelBE> read(NivelBE pbe) throws Exception {
        List<NivelBE> lista = new ArrayList();

        sqlB = new StringBuilder();
        sqlB.setLength(0);
        sqlB.append("SELECT "); 
        sqlB.append("NV.IdNivel, ");
        sqlB.append("NV.IdStand, ");
        sqlB.append("NV.Nombre, ");
        sqlB.append("NV.UsuarioInserta, ");
        sqlB.append("NV.FechaInserta, ");
        sqlB.append("NV.UsuarioModifica, ");
        sqlB.append("NV.FechaModifica, ");
        sqlB.append("NV.id_empresa, ");
        sqlB.append("ST.Nombre as nombreStand, ");
        sqlB.append("DC.DescripcionCorta as nombreAlmacen, ");
        sqlB.append("ST.IdAlmacen as IdAlmacen ");
        sqlB.append("FROM TNivel NV ");
        sqlB.append("INNER JOIN TStand ST ON ST.IdStand = NV.IdStand ");
        sqlB.append("INNER JOIN TDatoComun DC ON DC.IdDatoComun = ST.IdAlmacen ");
        sqlB.append("ORDER BY DC.DescripcionCorta, ST.Nombre ASC, NV.Nombre ASC ");

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

    private NivelBE cargaReadBE(ResultSet rs) throws SQLException {
        NivelBE be = new NivelBE();
        be.setIdNivel(rs.getInt("IdNivel"));
        be.setIdStand(rs.getInt("IdStand"));
        be.setNombre(rs.getString("Nombre"));
        be.setUsuarioInserta(rs.getString("UsuarioInserta"));
        be.setFechaInserta(rs.getDate("FechaInserta"));
        be.setUsuarioModifica(rs.getString("UsuarioModifica"));
        be.setFechaModifica(rs.getDate("FechaModifica"));
        be.setIdEmpresa(rs.getInt("id_empresa"));
        be.setNombreStand(rs.getString("nombreStand"));
        be.setNombreAlmacen(rs.getString("nombreAlmacen"));
        be.setIdAlmacen(rs.getInt("IdAlmacen"));
        return be;
    }

    @Override
    public NivelBE readId(int id) throws Exception {
        sqlB = new StringBuilder();
        sqlB.setLength(0);
        sqlB.append("SELECT "); 
        sqlB.append("NV.IdNivel, ");
        sqlB.append("NV.IdStand, ");
        sqlB.append("NV.Nombre, ");
        sqlB.append("NV.UsuarioInserta, ");
        sqlB.append("NV.FechaInserta, ");
        sqlB.append("NV.UsuarioModifica, ");
        sqlB.append("NV.FechaModifica, ");
        sqlB.append("NV.id_empresa, ");
        sqlB.append("ST.Nombre as nombreStand, ");
        sqlB.append("DC.DescripcionCorta as nombreAlmacen, ");
        sqlB.append("ST.IdAlmacen as IdAlmacen ");
        sqlB.append("FROM TNivel NV ");
        sqlB.append("INNER JOIN TStand ST ON ST.IdStand = NV.IdStand ");
        sqlB.append("INNER JOIN TDatoComun DC ON DC.IdDatoComun = ST.IdAlmacen ");
        sqlB.append("WHERE NV.IdNivel = ? ");
        sqlB.append("ORDER BY DC.DescripcionCorta, ST.Nombre ASC, NV.Nombre ASC ");

        cn = AccesoDB.getConnection();
        ps = cn.prepareStatement(sqlB.toString());
        ps.clearParameters();
        ps.setInt(1, id);
        rs = ps.executeQuery();

        NivelBE obj = null;
        if (rs.next()) {
            obj = cargaReadBE(rs);
        }

        rs.close();
        ps.close();
        cn.close();
        return obj;
    }
}
