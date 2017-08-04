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

/**
 *
 * @author ERCO
 */
public class SalidaMaterialDAO {
    
    // variables
    Connection cn = null;
    ResultSet rs = null;
    CallableStatement cs = null;
    PreparedStatement ps = null;
    final String INSERT = "{call usp_SalidaMaterial_insert(?,?,?,?,?,?,?,?,?)}";    //Inserta y Modifica la salida de material
    final String DELETE = "{call usp_SalidaMaterial_Delete(?,?)}";
    final String READ = "{call usp_SalidaMaterial_Read(?,?,?,?,?,?,?,?,?)}";
    final String GETDATA = "{call usp_SalidaMaterial_GetData(?,?)}";
    final String RETORNOMATERIAL = "{call usp_SalidaMaterial_RetornoMaterial(?,?,?,?)}";
    final String CONFIRMARSALIDA = "{call usp_SalidaMaterial_ConfirmarSalida(?,?,?)}";
    final String CONFIRMARRETORNO = "{call usp_SalidaMaterial_ConfirmarRetorno(?,?,?)}";
    
    String sql = "";
    
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
                id_salida = rs.getInt("IdSalidaMaterial");
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

    public int delete(SalidaMaterialBE o) throws Exception {
        int respuesta = 0;
        
        try {
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(DELETE);
            cs.setInt(1, o.getIdSalidaMaterial());
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
    
    public int confirmarSalida(SalidaMaterialBE o) throws Exception {
        int respuesta = 0;
        
        try {
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(CONFIRMARSALIDA);
            cs.setInt(1, o.getIdSalidaMaterial());
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
    
    public int confirmarRetorno(SalidaMaterialBE o) throws Exception {
        int respuesta = 0;
        
        try {
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(CONFIRMARRETORNO);
            cs.setInt(1, o.getIdSalidaMaterial());
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
            cs.setDate(8, pbe.getFechaRetorno()== null ? null:new java.sql.Date(pbe.getFechaRetorno().getTime()));
            cs.setString(9, pbe.getDesEstadoAbierto());
            
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

    public SalidaMaterialBE readId(int idSalidaMaterial, int idEmpresa) throws Exception {
        SalidaMaterialBE obj = null;
        
        try {
            cn = AccesoDB.getConnection();        
            
            cs = cn.prepareCall(GETDATA);            
            cs.setInt(1,idSalidaMaterial);
            cs.setInt(2,idEmpresa);
            rs=cs.executeQuery();
            
            while (rs.next()) {                
                obj = new SalidaMaterialBE();
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

    public int retornoMaterial(SalidaMaterialBE o) throws Exception {
        int respuesta = 0;
        
        try {
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(RETORNOMATERIAL);
            cs.setInt(1, o.getIdSalidaMaterial());
            cs.setDate(2, o.getFechaRetorno()== null ? null:new java.sql.Date(o.getFechaRetorno().getTime()));
            cs.setInt(3, o.getId_empresa());
            cs.setString(4, o.getUsuarioModifica());
            
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