/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import dao.CompraMaterialDetalleDAO;
import entity.CompraMaterialDetalleBE;
import java.util.List;

/**
 *
 * @author ERCO
 */
public class CompraMaterialDetalleBL {
    CompraMaterialDetalleDAO dao;
    
    public CompraMaterialDetalleBL() {
        dao = new CompraMaterialDetalleDAO();
    }
    
    public List<CompraMaterialDetalleBE> read(CompraMaterialDetalleBE pbe) throws Exception {
        return dao.read(pbe);
    }   
    
    public int create(CompraMaterialDetalleBE sm) throws Exception {
        return dao.create(sm);
    }
    
    public int delete(CompraMaterialDetalleBE sm) throws Exception {
        return dao.delete(sm);
    }
    
    public int deleteAll(int id_salida_material, int id_empresa) throws Exception {
        return dao.deleteAll(id_salida_material, id_empresa);
    }       
}
