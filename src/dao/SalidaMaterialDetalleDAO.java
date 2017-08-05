/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import database.AccesoDB;
import entity.SalidaMaterialDetalleBE;
import java.sql.CallableStatement;
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
public class SalidaMaterialDetalleDAO implements ICrudService<SalidaMaterialDetalleBE>{
    
    // variables
    Connection cn = null;
    ResultSet rs = null;
    CallableStatement cs = null;
    PreparedStatement ps = null;
    final String INSERT = "{call spSalidaMaterialDetalle_crear(?,?,?,?,?,?,?)}";
    final String UPDATE = "{}";
    final String DELETE = "{call spSalidaMaterialDetalle_eliminar(?,?)}}";
    final String READ = "{}";
    final String RETURN = "{call spSalidaMaterialDetalle_RetornarMaterial(?,?,?,?)}}";
    
    
    String sql = "";
    
    @Override
    public int create(SalidaMaterialDetalleBE o) throws Exception {
        try {
            
            //guardamos el producto
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(INSERT);
            cs.setInt(1,o.getId_detalle_salida_material());
            cs.setInt(2,o.getId_salida_material());
            cs.setInt(3,o.getId_producto());
            cs.setBigDecimal(4,o.getCantidadSalida());
            cs.setString(5,o.getUsuarioSalida());
            cs.setInt(6,o.getTipoOperacion());
            cs.setInt(7,o.getId_empresa());
            cs.executeUpdate();            
            cs.close();
        } catch (SQLException e) {
            throw e;
        } catch (InstantiationException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        } finally {
            cn.close();
        }
        
        return 0;
    }

    @Override
    public int update(SalidaMaterialDetalleBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(SalidaMaterialDetalleBE o) throws Exception {
        int respuesta = 0;
        
        try {
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(DELETE);
            cs.setInt(1, o.getId_salida_material());
            cs.setInt(2, o.getId_empresa());
            respuesta = cs.executeUpdate();
            cs.close();
            
        } catch (SQLException e) {
            throw e;
        } catch (InstantiationException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        } finally {
            cn.close();
        }
        
        return respuesta;
    }

    @Override
    public List<SalidaMaterialDetalleBE> read(SalidaMaterialDetalleBE pbe) throws Exception {
        
        List<SalidaMaterialDetalleBE> lista = new ArrayList();
        
        try {
            //abrir conexion a la base de datos
            cn = AccesoDB.getConnection();
            sql = "select "
                    + "dsm.id_detalle_salida_material, "
                    + "dsm.id_salida_material, "
                    + "p.id_producto, "
                    + "p.descripcion, "
                    + "dc.DescripcionCorta UnidadMaterial, "
                    + "dsm.CantidadSalida, "
                    + "ISNULL(dsm.CantidadRetorno, 0) CantidadRetorno, "
                    + "dsm.ComentarioRetorno "
                    + "from TDetalle_salida_material dsm "
                    + "inner join TProducto p on p.id_producto = dsm.id_producto "
                    + "left join TDatoComun dc on p.id_unidad = dc.IdDatoComun "
                    + "where "
                    + "dsm.id_salida_material = "+pbe.getId_salida_material()+" and "
                    + "dsm.id_empresa = "+pbe.getId_empresa()+"";
            
            
            ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            SalidaMaterialDetalleBE pdbe;
            
            while (rs.next()) {
                pdbe = new SalidaMaterialDetalleBE();
                pdbe.setId_detalle_salida_material(rs.getInt("id_detalle_salida_material"));
                pdbe.setId_salida_material(rs.getInt("id_salida_material"));
                pdbe.setId_producto(rs.getInt("id_producto"));
                pdbe.setUnidadMaterial(rs.getString("UnidadMaterial"));
                pdbe.setNombreMaterial(rs.getString("descripcion"));
                pdbe.setCantidadSalida(rs.getBigDecimal("CantidadSalida"));
                pdbe.setCantidadRetorno(rs.getDouble("CantidadRetorno"));
                pdbe.setComentarioRetorno(rs.getString("ComentarioRetorno"));
                lista.add(pdbe);
            }
            
            rs.close();
            ps.close();
        } catch (IllegalAccessException e) {
            throw e;
        } catch (InstantiationException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            cn.close();
        }
        
        return lista;
    }

    @Override
    public SalidaMaterialDetalleBE readId(int o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int deleteAll(int id_salida_material, int id_empresa) throws Exception {
        int respuesta = 0;
        
        try {
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(DELETE);
            cs.setInt(1, id_salida_material);
            cs.setInt(2, id_empresa);
            respuesta = cs.executeUpdate();
            cs.close();
            
        } catch (SQLException e) {
            throw e;
        } catch (InstantiationException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        } finally {
            cn.close();
        }
        
        return respuesta;
    }
    
    public int retornoMaterial(SalidaMaterialDetalleBE pbe) throws Exception {
        int respuesta = 0;
        
        try {
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(RETURN);
            cs.setInt(1, pbe.getId_detalle_salida_material());
            cs.setDouble(2, pbe.getCantidadRetorno());
            cs.setString(3, pbe.getComentarioRetorno());
            cs.setString(4, pbe.getUsuarioEntrega());
            
            respuesta = cs.executeUpdate();
            cs.close();
            
        } catch (SQLException e) {
            throw e;
        } catch (InstantiationException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        } finally {
            cn.close();
        }
        
        return respuesta;
    }
}
