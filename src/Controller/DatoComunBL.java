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

    public DatoComunBE readId(int id) throws Exception {
        return dao.readId(id);
    }
    
    public int delete(DatoComunBE o) throws Exception{
        return dao.delete(o);
    }
    
    public int updateAlmacen(DatoComunBE o) throws Exception{
        return dao.updateAlmacen(o);
    }
    
}
