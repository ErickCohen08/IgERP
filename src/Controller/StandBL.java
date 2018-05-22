/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import dao.StandDAO;
import entity.StandBE;
import java.util.List;

/**
 *
 * @author admin
 */
public class StandBL {

    StandDAO dao;

    public StandBL() {
        dao = new StandDAO();
    }

    public int create(StandBE o) throws Exception {
        return dao.create(o);
    }

    public int update(StandBE o) throws Exception {
        return dao.update(o);
    }

    public int delete(StandBE o) throws Exception {
        return dao.delete(o);
    }

    public List<StandBE> read(StandBE pbe) throws Exception {
        return dao.read(pbe);
    }

    public StandBE readId(int id) throws Exception {
        return dao.readId(id);
    }

    public List<StandBE> readByAlmacen(int id) throws Exception {
        return dao.readByAlmacen(id);
    }
}
