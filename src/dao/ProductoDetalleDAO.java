/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import database.AccesoDB;
import entity.ProductoDetalleBE;
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
public class ProductoDetalleDAO implements ICrudService<ProductoDetalleBE>{
    
    // variables
    Connection cn = null;
    ResultSet rs = null;
    CallableStatement cs = null;
    PreparedStatement ps = null;
    final String INSERT = "{call spProductoDetalle_crear(?,?,?,?,?)}";
    final String UPDATE = "{}";
    final String DELETE = "{call spProductoDetalle_eliminar(?)}}";
    final String READ = "{}";
    
    String sql = "";
    
    @Override
    public int create(ProductoDetalleBE o) throws Exception {
        int id_producto = 0;
        
        try {
            
            //guardamos el producto
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(INSERT);
            cs.setInt(1,o.getId_producto());
            cs.setString(2,o.getRucProveedor());
            cs.setBigDecimal(3,o.getPrecio());
            cs.setInt(4,o.getId_empresa());
            cs.setInt(5,o.getId_usuario());
            
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
        
        return id_producto;
    }

    @Override
    public int update(ProductoDetalleBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(ProductoDetalleBE o) throws Exception {
        int respuesta = 0;
        
        try {
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(DELETE);
            cs.setInt(1, o.getId_producto());           
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
    public List<ProductoDetalleBE> read(ProductoDetalleBE pbe) throws Exception {
        List<ProductoDetalleBE> lista = new ArrayList();
        
        try {
            //abrir conexion a la base de datos
            cn = AccesoDB.getConnection();
            sql = "select pr.ruc ruc, pr.razon_social razon_social, pd.precio precio, pd.id_empresa id_empresa, pd.id_usuario id_usuario from TProducto_detalle pd inner join TProveedor pr on pd.id_proveedor = pr.id_proveedor  where id_producto = "+pbe.getId_producto()+"";
            ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            ProductoDetalleBE pdbe;
            
            while (rs.next()) {
                pdbe = new ProductoDetalleBE();
                pdbe.setRucProveedor(rs.getString("ruc"));
                pdbe.setRazonsocialProveedor(rs.getString("razon_social"));
                pdbe.setPrecio(rs.getBigDecimal("precio"));
                pdbe.setId_empresa(rs.getInt("id_empresa"));
                pdbe.setId_usuario(rs.getInt("id_usuario"));
                
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
    public ProductoDetalleBE readId(int o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
