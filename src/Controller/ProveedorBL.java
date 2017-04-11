/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import dao.ProveedorDAO;
import entity.ProveedorBE;
import java.util.List;

/**
 *
 * @author admin
 */
public class ProveedorBL {

    ProveedorDAO pdao;
    
    public ProveedorBL() {
        pdao = new ProveedorDAO();
    }
    
    public List<ProveedorBE> ProveedorBuscar(String bus) throws Exception {
        return pdao.readTablaBuscar(bus);
    }   
    
    public int crear(ProveedorBE pbe) throws Exception {
        return pdao.create(pbe);
    }
}
