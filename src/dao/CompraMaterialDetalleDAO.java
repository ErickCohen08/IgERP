/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import database.AccesoDB;
import entity.CompraMaterialDetalleBE;
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
public class CompraMaterialDetalleDAO implements ICrudService<CompraMaterialDetalleBE>{
    
    // variables
    Connection cn = null;
    ResultSet rs = null;
    CallableStatement cs = null;
    PreparedStatement ps = null;
    final String INSERT = "{call spCompraMaterialDetalle_crear(?,?,?,?,?,?,?,?,?)}";
    final String DELETE = "{call spCompraMaterialDetalle_eliminar(?,?)}}";
    final String READ = "{call spCompraMaterialDetalle_Read(?,?)}}";
    
    String sql = "";
    
    @Override
    public int create(CompraMaterialDetalleBE o) throws Exception {
        try {
            
            //guardamos el producto
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(INSERT);
            cs.setInt(1,o.getIdDetalleCompra());
            cs.setInt(2,o.getIdCompra());
            cs.setInt(3,o.getId_producto());
            cs.setBigDecimal(4,o.getCantidad());
            cs.setBigDecimal(5,o.getPrecioUnitario());
            cs.setBigDecimal(6,o.getPrecioTotal());
            cs.setString(7,o.getUsuarioInserta());
            cs.setInt(8,o.getTipoOperacion());
            cs.setInt(9,o.getId_empresa());
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
    public int update(CompraMaterialDetalleBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(CompraMaterialDetalleBE o) throws Exception {
        int respuesta = 0;
        
        try {
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(DELETE);
            cs.setInt(1, o.getIdCompra());
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
    public List<CompraMaterialDetalleBE> read(CompraMaterialDetalleBE pbe) throws Exception {
        
        List<CompraMaterialDetalleBE> lista = new ArrayList();
        
        try {
            //abrir conexion a la base de datos
            cn = AccesoDB.getConnection();        
            cs = cn.prepareCall(READ);
            
            cs.setInt(1,pbe.getIdCompra());
            cs.setInt(1,pbe.getId_empresa());
            
            rs=cs.executeQuery();
            
            CompraMaterialDetalleBE pdbe;
            
            while (rs.next()) {
                pdbe = new CompraMaterialDetalleBE();
                pdbe.setIdDetalleCompra(rs.getInt("IdDetalleCompra"));
                pdbe.setIdCompra(rs.getInt("IdCompra"));
                pdbe.setId_producto(rs.getInt("id_producto"));
                pdbe.setCantidad(rs.getBigDecimal("Cantidad"));
                pdbe.setPrecioUnitario(rs.getBigDecimal("PrecioUnitario"));
                pdbe.setPrecioTotal(rs.getBigDecimal("PrecioTotal"));
                pdbe.setDesMaterial(rs.getString("DesMaterial"));
                pdbe.setDesUnidad(rs.getString("DesUnidad"));

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
    public CompraMaterialDetalleBE readId(int o) throws Exception {
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
}