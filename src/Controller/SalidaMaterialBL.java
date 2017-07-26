/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import dao.SalidaMaterialDAO;
import entity.SalidaMaterialBE;
import java.util.List;

/**
 *
 * @author admin
 */
public class SalidaMaterialBL {

    SalidaMaterialDAO sdao;
    
    public SalidaMaterialBL() {
        sdao = new SalidaMaterialDAO();
    }
    
    public List<SalidaMaterialBE> read(SalidaMaterialBE pbe) throws Exception {
        return sdao.read(pbe);
    }   
    
    public int create(SalidaMaterialBE sm) throws Exception {
        return sdao.create(sm);
    }
    
    public SalidaMaterialBE readId(int id_salida, int id_empresa) throws Exception {
        return sdao.readId(id_salida, id_empresa);
    }
    
    public int delete(SalidaMaterialBE sm) throws Exception {
        return sdao.delete(sm);
    }
}
