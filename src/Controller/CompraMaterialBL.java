/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import dao.CompraMaterialDAO;
import entity.CompraMaterialBE;
import java.util.List;

/**
 *
 * @author ERCO
 */
public class CompraMaterialBL {
    
    CompraMaterialDAO dao;
    
   public CompraMaterialBL() {
        dao = new CompraMaterialDAO();
    }
    
    public List<CompraMaterialBE> read(CompraMaterialBE pbe) throws Exception {
        return dao.read(pbe);
    }   
    
    public int create(CompraMaterialBE sm) throws Exception {
        return dao.create(sm);
    }
    
    public CompraMaterialBE readId(int id_salida, int id_empresa) throws Exception {
        return dao.readId(id_salida, id_empresa);
    }
    
    public int delete(CompraMaterialBE sm) throws Exception {
        return dao.delete(sm);
    }
    
    public int confirmarCompra(CompraMaterialBE sm) throws Exception {
        return dao.confirmarCompra(sm);
    }
}
