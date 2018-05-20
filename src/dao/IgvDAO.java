/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import database.AccesoDB;
import entity.IgvBE;
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
public class IgvDAO implements ICrudService<IgvBE>{
    
    // variables
    Connection cn = null;
    ResultSet rs = null;
    CallableStatement cs = null;
    PreparedStatement ps = null;
    private Statement stm = null;
    
    String sql = "";
    
    @Override
    public int create(IgvBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(IgvBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(IgvBE o) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<IgvBE> read(IgvBE pbe) throws Exception {
        List<IgvBE> lista = new ArrayList();
        
        try {
            cn = AccesoDB.getConnection();
            stm = cn.createStatement();
            rs = stm.executeQuery("select igv from tigv where id_empresa='"+pbe.getId_empresa()+"' order by predeterminado desc");
            
            IgvBE obj;
            
            while (rs.next()) {
                obj = new IgvBE();
                obj.setIgv(rs.getInt("igv"));
                lista.add(obj);
            }
            rs.close();
            stm.close(); 
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw e;
        } finally {
            cn.close();
        }
        
        return lista;
    }

    @Override
    public IgvBE readId(int Id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
