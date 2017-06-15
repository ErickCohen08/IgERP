/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import database.AccesoDB;
import entity.SalidaMaterialBE;
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
public class SalidaMaterialDAO implements ICrudService<SalidaMaterialBE>{
    
    // variables
    Connection cn = null;
    ResultSet rs = null;
    CallableStatement cs = null;
    PreparedStatement ps = null;
    final String INSERT = "{call usp_SalidaMaterial_insert(?,?,?,?,?,?,?,?,?)}";
    final String UPDATE = "{call usp_Producto(?,?,?,?,?)}";
    final String DELETE = "{call usp_Producto_Delete(?)}";
    final String READ = "{call usp_SalidaMaterial_Read(?,?,?,?,?,?,?)}";
    final String GETDATA = "{call usp_Producto_GetData(?)}";
    
    String sql = "";
    
    @Override
    public int create(SalidaMaterialBE o) throws Exception {
        int id_salida = 0;
        
        try {
            
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(INSERT);
            
            cs.setInt(1,o.getIdSalidaMaterial());
            cs.setDate(2, o.getFechaSalida() == null ? null:new java.sql.Date(o.getFechaSalida().getTime()));
            cs.setString(3,o.getDesPersonal());
            cs.setString(4,o.getDesObra());
            cs.setString(5,o.getDireccion());
            cs.setString(6,o.getMotivo());
            cs.setInt(7,o.getId_empresa());
            cs.setString(8,o.getUsuarioInserta());
            cs.setInt(9, o.getTipoOperacion());
            
            rs=cs.executeQuery();
            
            while (rs.next()) {                
                id_salida = rs.getInt("id_salida");
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
        
        return id_salida;
    }

    @Override
    public int update(SalidaMaterialBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(SalidaMaterialBE o) throws Exception {
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
    public List<SalidaMaterialBE> read(SalidaMaterialBE pbe) throws Exception {
        List<SalidaMaterialBE> lista = new ArrayList();
        
        try {
            cn = AccesoDB.getConnection();        
            cs = cn.prepareCall(READ);
            
            cs.setInt(1,pbe.getIdSalidaMaterial());
            cs.setInt(2, pbe.getId_empresa());
            cs.setDate(3, pbe.getFechaSalida() == null ? null:new java.sql.Date(pbe.getFechaSalida().getTime()));
            cs.setString(4, pbe.getDireccion());
            cs.setString(5, pbe.getMotivo());
            cs.setString(6, pbe.getDesPersonal());
            cs.setString(7, pbe.getDesObra());
            
            rs=cs.executeQuery();
            
            SalidaMaterialBE emp;
            while (rs.next()) {                
                emp = new SalidaMaterialBE();
                emp.setFila(rs.getInt("Fila"));
                emp.setIdSalidaMaterial(rs.getInt("IdSalidaMaterial"));
                emp.setFechaSalida(rs.getDate("FechaSalida"));
                emp.setDesPersonal(rs.getString("DesPersonal"));
                emp.setDesObra(rs.getString("DesObra"));
                emp.setDireccion(rs.getString("Direccion"));
                emp.setDesEstadoAbierto(rs.getString("DesEstadoAbierto"));
                emp.setFechaInserta(rs.getDate("FechaInserta"));
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
    public SalidaMaterialBE readId(int Id) throws Exception {
        SalidaMaterialBE obj = null;
        
        /*try {
            cn = AccesoDB.getConnection();        
            cs = cn.prepareCall(GETDATA);
            
            cs.setInt(1,Id);
            
            rs=cs.executeQuery();
            
            while (rs.next()) {                
                obj = new SalidaMaterialBE();
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
