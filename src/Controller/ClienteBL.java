/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import dao.ClienteDAO;
import entity.ClienteBE;
import java.util.List;

/**
 *
 * @author admin
 */
public class ClienteBL {

    ClienteDAO pdao;
    
    public ClienteBL() {
        pdao = new ClienteDAO();
    }
    
    public List<ClienteBE> read(ClienteBE pbe) throws Exception {
        return pdao.read(pbe);
    }   
    
}
