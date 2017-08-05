/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import dao.IgvDAO;
import entity.IgvBE;
import java.util.List;

/**
 *
 * @author admin
 */
public class IgvBL {

    IgvDAO pdao;
    
    public IgvBL() {
        pdao = new IgvDAO();
    }
    
    public List<IgvBE> read(IgvBE pbe) throws Exception {
        return pdao.read(pbe);
    }   
    
}
