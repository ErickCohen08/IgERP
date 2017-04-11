/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import database.AccesoDB;
import entity.ProveedorBE;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import service.ICrudService;
import service.ProveedorService;


/**
 *
 * @author admin
 */
public class ProveedorDAO implements ICrudService<ProveedorBE>, ProveedorService<ProveedorBE>{
    
    // variables
    private Connection cn = null;
    private Statement stm = null;
    CallableStatement cs = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private String sql = null;
    
    final String READTABLABUSCAR = "{call usp_Proveedor_ReadTablaBuscar(?)}";
    final String INSERT = "{call usp_Proveedor_insert(?,?,?,?,?,?,?,?)}";
    
    ProveedorBE pBE = null;
    
    @Override
    public int create(ProveedorBE o) throws Exception {
        int id_proveedor = 0;
        
        try {
            cn = AccesoDB.getConnection();
            cs = cn.prepareCall(INSERT);
            
            //valores de los parametros del sp
            cs.setString(1, o.getRazon_social());
            cs.setString(2, o.getRuc());
            cs.setString(3, o.getDireccion());
            cs.setString(4, o.getTelefono());
            cs.setString(5, o.getCelular());
            cs.setString(6, o.getCorreo());
            cs.setInt(7, o.getId_empresa());
            cs.setInt(8, o.getId_usuario());
            
            //ejecutar el sp
            rs = cs.executeQuery();
            
            while (rs.next()) {
                id_proveedor = rs.getInt("id_proveedor");
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
        
        return id_proveedor;
    }

    @Override
    public int update(ProveedorBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(ProveedorBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ProveedorBE> read(ProveedorBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ProveedorBE> readTablaBuscar(String bus) throws Exception {
        List<ProveedorBE> lista = new ArrayList();
        
        try {
            cn = AccesoDB.getConnection();        
            cs = cn.prepareCall(READTABLABUSCAR);
            cs.setString(1,bus);
            
            rs=cs.executeQuery();
            
            while (rs.next()) {
                pBE = new ProveedorBE();
                pBE.setId_provedor(rs.getInt("id_proveedor"));
                pBE.setRuc(rs.getString("ruc"));
                pBE.setRazon_social(rs.getString("razon_social"));
                lista.add(pBE);
            }
            rs.close();
            cs.close();
        } catch (SQLException e) {
            throw e;
        } finally {
            cn.close();
        }
        return lista;
    }

    @Override
    public ProveedorBE readId(int o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
