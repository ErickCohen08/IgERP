/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import database.AccesoDB;
import entity.ObrasBE;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import service.ICrudService;

/**
 *
 * @author ERCO
 */
public class ObrasDAO implements ICrudService<ObrasBE>{
    
    // variables
    Connection cn = null;
    ResultSet rs = null;
    CallableStatement cs = null;
    PreparedStatement ps = null;
    private Statement stm = null;
    final String INSERT = "{call usp_Producto_insert(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    final String UPDATE = "{call usp_Producto(?,?,?,?,?)}";
    final String DELETE = "{call usp_Producto_Delete(?)}";
    final String READ = "{call usp_Producto_Read(?,?,?,?,?,?,?,?,?,?,?,?)}";
    final String GETDATA = "{call usp_Producto_GetData(?)}";
    
    String sql = "";
    
    @Override
    public int create(ObrasBE o) throws Exception {
        int id_producto = 0;
        
        /*try {
            
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
        }*/
        
        return id_producto;
    }

    @Override
    public int update(ObrasBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(ObrasBE o) throws Exception {
        int respuesta = 0;
        
        /*try {
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
        }*/
        
        return respuesta;
    }

    @Override
    public List<ObrasBE> read(ObrasBE pbe) throws Exception {
        List<ObrasBE> lista = new ArrayList();
        
        try {
            cn = AccesoDB.getConnection();
            stm = cn.createStatement();
            rs = stm.executeQuery("SELECT DBO.TRIM(Descripcion) AS Descripcion FROM TObras WHERE EstadoActivo = 'A'");
            
            ObrasBE obj;
            
            while (rs.next()) {
                obj = new ObrasBE();
                obj.setDescripcion(rs.getString("Descripcion"));
                lista.add(obj);
            }
            rs.close();
            stm.close(); 
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
    public ObrasBE readId(int Id) throws Exception {
        ObrasBE obj = null;
        
        /*try {
            cn = AccesoDB.getConnection();        
            cs = cn.prepareCall(GETDATA);
            
            cs.setInt(1,Id);
            
            rs=cs.executeQuery();
            
            while (rs.next()) {                
                obj = new ObrasBE();
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
        }*/
       
        return obj;
    }
}
