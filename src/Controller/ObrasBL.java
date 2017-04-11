/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import dao.ObrasDAO;
import entity.ObrasBE;
import java.util.List;

/**
 *
 * @author admin
 */
public class ObrasBL {

    ObrasDAO pdao;
    
    public ObrasBL() {
        pdao = new ObrasDAO();
    }
    
    public List<ObrasBE> read(ObrasBE pbe) throws Exception {
        return pdao.read(pbe);
    }   
    
}
