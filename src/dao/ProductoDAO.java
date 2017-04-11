/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import database.AccesoDB;
import entity.ProductoBE;
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
public class ProductoDAO implements ICrudService<ProductoBE>{
    
    // variables
    Connection cn = null;
    ResultSet rs = null;
    CallableStatement cs = null;
    PreparedStatement ps = null;
    final String INSERT = "{call usp_Producto_insert(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    final String UPDATE = "{call usp_Producto(?,?,?,?,?)}";
    final String DELETE = "{call usp_Producto_Delete(?)}";
    final String READ = "{call usp_Producto_Read(?,?,?,?,?,?,?,?,?,?,?,?)}";
    final String GETDATA = "{call usp_Producto_GetData(?)}";
    
    String sql = "";
    
    @Override
    public int create(ProductoBE o) throws Exception {
        int id_producto = 0;
        
        try {
            
            //guardamos el producto
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(INSERT);
            cs.setString(1,o.getCodigo());
            cs.setString(2,o.getDescripcion());
            cs.setString(3,o.getModelo());
            cs.setString(4,o.getMarca());
            cs.setString(5,o.getDescripcion_coloquial());
            cs.setBigDecimal(6,o.getPeso());
            cs.setBigDecimal(7,o.getPrecio_promedio());
            cs.setBigDecimal(8,o.getCantidad());
            
            cs.setString(9,o.getDesmoneda());
            cs.setString(10,o.getDesunidad());
            cs.setString(11,o.getDesproductotipo());
            cs.setString(12,o.getDesAlmacen());
            cs.setString(13,o.getDesReferencia_precio());
            
            cs.setInt(14,o.getId_empresa());
            cs.setString(15,o.getUsuarioInserta());
            cs.setInt(16, o.getTipoOperacion());
            cs.setInt(17, o.getId_producto());
            
            rs=cs.executeQuery();
            
            while (rs.next()) {                
                id_producto = rs.getInt("id_producto");
            }
            
            rs.close();
            cs.close(); 
            
            
            //guardamos el detalle
            
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
    public int update(ProductoBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(ProductoBE o) throws Exception {
        int respuesta;
        
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
    public List<ProductoBE> read(ProductoBE pbe) throws Exception {
        List<ProductoBE> lista = new ArrayList();
        
        try {
            cn = AccesoDB.getConnection();        
            cs = cn.prepareCall(READ);
            
            cs.setInt(1,pbe.getId_producto());
            cs.setString(2, pbe.getCodigo());
            cs.setString(3, pbe.getDescripcion());
            cs.setString(4, pbe.getModelo());
            cs.setString(5, pbe.getMarca());
            cs.setString(6, pbe.getDescripcion_coloquial());
            cs.setInt(7, pbe.getId_empresa());
            cs.setString(8, pbe.getDesmoneda());
            cs.setString(9, pbe.getDesunidad());
            cs.setString(10, pbe.getDesproductotipo());
            cs.setString(11, pbe.getDesAlmacen());
            cs.setString(12, pbe.getDesReferencia_precio());
            
            rs=cs.executeQuery();
            
            ProductoBE emp;
            while (rs.next()) {                
                emp = new ProductoBE();
                emp.setFila(rs.getInt("fila"));
                emp.setId_producto(rs.getInt("id_producto"));
                emp.setCodigo(rs.getString("codigo"));
                emp.setDescripcion(rs.getString("descripcion"));
                emp.setDesunidad(rs.getString("Desunidad"));
                emp.setCantidad(rs.getBigDecimal("cantidad"));
                emp.setDesmoneda(rs.getString("Desmoneda"));
                emp.setPrecio_promedio(rs.getBigDecimal("precio_promedio"));
                emp.setMarca(rs.getString("marca"));
                emp.setModelo(rs.getString("modelo"));
                lista.add(emp);
            }
            
            rs.close();
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
        
        return lista;
    }

    @Override
    public ProductoBE readId(int Id) throws Exception {
        ProductoBE obj = null;
        
        try {
            cn = AccesoDB.getConnection();        
            cs = cn.prepareCall(GETDATA);
            
            cs.setInt(1,Id);
            
            rs=cs.executeQuery();
            
            while (rs.next()) {                
                obj = new ProductoBE();
                obj.setId_producto(rs.getInt("id_producto"));
                obj.setCodigo(rs.getString("codigo"));
                obj.setDescripcion(rs.getString("descripcion"));
                obj.setDescripcion_coloquial(rs.getString("descripcion_coloquial"));
                obj.setDesmoneda(rs.getString("Desmoneda"));
                obj.setPrecio_promedio(rs.getBigDecimal("precio_promedio"));
                obj.setMarca(rs.getString("marca"));
                obj.setModelo(rs.getString("modelo"));
                obj.setPeso(rs.getBigDecimal("peso"));
                obj.setDesunidad(rs.getString("Desunidad"));
                obj.setDesAlmacen(rs.getString("DesAlmacen"));
                obj.setDesReferencia_precio(rs.getString("DesReferencia_precio"));
                obj.setDesproductotipo(rs.getString("Desproductotipo"));
                obj.setCantidad(rs.getBigDecimal("cantidad"));
            }
            
            rs.close();
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
       
        return obj;
    }
}
