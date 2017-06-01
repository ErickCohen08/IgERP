/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import database.AccesoDB;
import entity.MonedaBE;
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
public class MonedaDAO implements ICrudService<MonedaBE>{
    
    // variables
    Connection cn = null;
    ResultSet rs = null;
    CallableStatement cs = null;
    PreparedStatement ps = null;
    final String READ = "{call usp_Moneda_Read(?)}";
    
    String sql = "";
    
    @Override
    public int create(MonedaBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(MonedaBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(MonedaBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MonedaBE> read(MonedaBE pbe) throws Exception {
        List<MonedaBE> lista = new ArrayList();
        
        try {
            cn = AccesoDB.getConnection();        
            cs = cn.prepareCall(READ);
            cs.setInt(1, pbe.getId_moneda());
            
            rs=cs.executeQuery();
            while (rs.next()) {                
               MonedaBE emp = cargaReadBE(rs);
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

    private MonedaBE cargaReadBE(ResultSet rs) throws SQLException {
        MonedaBE be = new MonedaBE(
                rs.getInt("id_moneda"),
                rs.getString("nombre"),
                rs.getString("simbolo"),
                rs.getString("moneda_local")
        );                                   
                                    
       return be;
    }

    @Override
    public MonedaBE readId(int o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
