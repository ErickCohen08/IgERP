/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import dao.DatoComunDAO;
import entity.DatoComunBE;
import java.util.List;

/**
 *
 * @author admin
 */
public class DatoComunBL {
    
    DatoComunDAO dao;
    
    public DatoComunBL() {
        dao = new DatoComunDAO();
    }
    
    public List<DatoComunBE> ReadDetalle(int CodigoTabla) throws Exception {
        return dao.readDetalle(CodigoTabla);
    }

    public int crear(DatoComunBE obj) throws Exception {
        return dao.create(obj);
    }
}