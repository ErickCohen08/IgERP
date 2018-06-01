/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import dao.NivelDAO;
import entity.NivelBE;
import java.util.List;

/**
 *
 * @author admin
 */
public class NivelBL {
    
    NivelDAO dao;
    
    public NivelBL() {
        dao = new NivelDAO();
    }
    
    public int create(NivelBE o) throws Exception {
        return dao.create(o);
    }
    
    public int update(NivelBE o) throws Exception{
        return dao.update(o);
    }
    
    public int delete(NivelBE o) throws Exception{
        return dao.delete(o);
    }
    
    public List<NivelBE> read(NivelBE pbe) throws Exception{
        return dao.read(pbe);
    }
    
    public NivelBE readId(int id) throws Exception{
        return dao.readId(id);
    }
    
    public List<NivelBE> readByStand(int id) throws Exception {
        return dao.readByStand(id);
    }
}
