/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import dao.MonedaDAO;
import entity.MonedaBE;
import java.util.List;

/**
 *
 * @author admin
 */
public class MonedaBL {
    
    MonedaDAO mdao;
    
    public MonedaBL() {
        mdao = new MonedaDAO();
    }
    
    public List<MonedaBE> MonedaListar(MonedaBE pbe) throws Exception {
        return mdao.read(pbe);
    }
}
