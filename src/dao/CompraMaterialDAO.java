/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import database.AccesoDB;
import entity.CompraMaterialBE;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ERCO
 */
public class CompraMaterialDAO {
    
    // variables
    Connection cn = null;
    ResultSet rs = null;
    CallableStatement cs = null;
    PreparedStatement ps = null;
    final String INSERT = "{call usp_SalidaMaterial_insert(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";    //Inserta y Modifica
    final String DELETE = "{call usp_SalidaMaterial_Delete(?,?)}";
    final String READ = "{call usp_SalidaMaterial_Read(?,?,?,?,?,?,?,?,?)}";
    final String GETDATA = "{call usp_SalidaMaterial_GetData(?,?)}";
    final String CONFIRMARCOMPRA = "{call usp_SalidaMaterial_ConfirmarSalida(?,?,?)}";
    
    String sql = "";
    
    public int create(CompraMaterialBE o) throws Exception {
        int idCompra = 0;
        
        try {
            
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(INSERT);
            
            cs.setInt(1,o.getIdCompra());
            cs.setDate(2, o.getFechaCompra() == null ? null:new java.sql.Date(o.getFechaCompra().getTime()));
            cs.setString(3,o.getNumeroCompra());
            cs.setBigDecimal(4,o.getCalculoIgv());
            cs.setBigDecimal(5,o.getSubTotal());
            cs.setBigDecimal(6,o.getTotal());
            cs.setString(7,o.getTotalLetras());
            cs.setString(8,o.getDesProveedor());
            cs.setString(9,o.getDesMoneda());
            cs.setDouble(10,o.getDesIgv());
            cs.setString(11,o.getDesObra());
            cs.setString(12,o.getDesDocumento());
            cs.setInt(13,o.getId_empresa());
            cs.setString(14,o.getUsuarioInserta());
            cs.setInt(15, o.getTipoOperacion());
            
            rs=cs.executeQuery();
            
            while (rs.next()) {                
                idCompra = rs.getInt("IdCompra");
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
        
        return idCompra;
    }

    public int delete(CompraMaterialBE o) throws Exception {
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
    
    public int confirmarCompra(CompraMaterialBE o) throws Exception {
        int respuesta = 0;
        
        try {
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(CONFIRMARCOMPRA);
            cs.setInt(1, o.getIdCompra());
            cs.setInt(2, o.getId_empresa());
            cs.setString(3, o.getUsuarioModifica());
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
    
    public List<CompraMaterialBE> read(CompraMaterialBE obj) throws Exception {
        List<CompraMaterialBE> lista = new ArrayList();
        
        try {
            cn = AccesoDB.getConnection();        
            cs = cn.prepareCall(READ);
            
            cs.setInt(1,obj.getIdCompra());
            cs.setDate(2, obj.getFechaCompra() == null ? null:new java.sql.Date(obj.getFechaCompra().getTime()));
            cs.setString(3,obj.getNumeroCompra());
            cs.setString(8,obj.getDesProveedor());
            cs.setString(9,obj.getDesMoneda());
            cs.setString(11,obj.getDesObra());
            cs.setString(12,obj.getDesDocumento());
            cs.setInt(13,obj.getId_empresa());
            
            rs=cs.executeQuery();
            
            CompraMaterialBE emp;
            while (rs.next()) {                
                emp = new CompraMaterialBE();
                emp.setIdSalidaMaterial(rs.getInt("IdSalidaMaterial"));
                emp.setFechaSalida(rs.getDate("FechaSalida"));
                emp.setDesPersonal(rs.getString("DesPersonal"));
                emp.setDesObra(rs.getString("DesObra"));
                emp.setDireccion(rs.getString("Direccion"));
                emp.setDesEstadoAbierto(rs.getString("DesEstadoAbierto"));
                emp.setFechaInserta(rs.getDate("FechaInserta"));
                
                emp.setFila(rs.getInt("Fila"));
                emp.setIdCompra(rs.getInt("IdCompra"));
                emp.setFechaCompra(rs.getDate("FechaCompra"));
                emp.setNumeroCompra(3,obj.getNumeroCompra());
                emp.setDesProveedor(8,obj.getDesProveedor());
                emp.setDesMoneda(9,obj.getDesMoneda());
                emp.setDesDocumento(12,obj.getDesDocumento());
                
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

    public CompraMaterialBE readId(int idSalidaMaterial, int idEmpresa) throws Exception {
        CompraMaterialBE obj = null;
        
        try {
            cn = AccesoDB.getConnection();        
            
            cs = cn.prepareCall(GETDATA);            
            cs.setInt(1,idSalidaMaterial);
            cs.setInt(2,idEmpresa);
            rs=cs.executeQuery();
            
            while (rs.next()) {                
                obj = new CompraMaterialBE();
                obj.setIdSalidaMaterial(idSalidaMaterial);
                obj.setFechaSalida(rs.getDate("FechaSalida"));
                obj.setDireccion(rs.getString("Direccion"));
                obj.setId_personal(rs.getInt("id_personal"));
                obj.setId_obra(rs.getInt("id_obra"));
                obj.setId_empresa(rs.getInt("id_empresa"));
                obj.setMotivo(rs.getString("Motivo"));
                obj.setDesObra(rs.getString("DesObra"));
                obj.setDesPersonal(rs.getString("DesPersonal"));
                obj.setEstadoAbierto(rs.getInt("EstadoAbierto"));
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