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
    final String INSERT = "{call usp_Obra_insert(?,?,?,?,?)}";
    final String UPDATE = "{call usp_Producto(?,?,?,?,?)}";
    final String DELETE = "{call usp_Producto_Delete(?)}";
    final String READ = "{call usp_Obra_Read(?,?,?,?)}";
    final String GETDATA = "{call usp_Producto_GetData(?)}";
    
    String sql = "";
    
    @Override
    public int create(ObrasBE o) throws Exception {
        int respuesta = 0;
        
        try {
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(INSERT);
            cs.setString(1, o.getDescripcion()); 
            cs.setString(2, o.getDesCliente()); 
            cs.setString(3, o.getDireccion());
            cs.setInt(4, o.getId_empresa());
            cs.setString(5, o.getUsuarioInserta());
                    
            respuesta = cs.executeUpdate();
            cs.close();
            
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw e;
        } finally {
            cn.close();
        }
        
        return respuesta;
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
            cs = cn.prepareCall(READ);
            
            cs.setInt(1,pbe.getIdObra());
            cs.setString(2, pbe.getDescripcion());
            cs.setInt(3, pbe.getId_cliente());
            cs.setInt(4, pbe.getId_empresa());
            
            rs=cs.executeQuery();
            
            ObrasBE emp;
            while (rs.next()) {                
                emp = new ObrasBE();
                emp.setIdObra(rs.getInt("IdObra"));
                emp.setDescripcion(rs.getString("Descripcion"));
                emp.setId_cliente(rs.getInt("id_cliente"));
                emp.setDireccion(rs.getString("direccion"));
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
