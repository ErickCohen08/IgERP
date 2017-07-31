/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import dao.SalidaMaterialDetalleDAO;
import entity.SalidaMaterialDetalleBE;
import java.util.List;

/**
 *
 * @author admin
 */
public class SalidaMaterialDetalleBL {

    SalidaMaterialDetalleDAO sdao;
    
    public SalidaMaterialDetalleBL() {
        sdao = new SalidaMaterialDetalleDAO();
    }
    
    public List<SalidaMaterialDetalleBE> read(SalidaMaterialDetalleBE pbe) throws Exception {
        return sdao.read(pbe);
    }   
    
    public int create(SalidaMaterialDetalleBE sm) throws Exception {
        return sdao.create(sm);
    }
    
    public int delete(SalidaMaterialDetalleBE sm) throws Exception {
        return sdao.delete(sm);
    }
    
    public int deleteAll(int id_salida_material, int id_empresa) throws Exception {
        return sdao.deleteAll(id_salida_material, id_empresa);
    }
    
    public int retornoMaterial(SalidaMaterialDetalleBE sm) throws Exception {
        return sdao.retornoMaterial(sm);
    }    
}
