/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import dao.ReporteDAO;
import entity.ReporteSalidaMaterialBE;
import java.util.Date;
import java.util.List;

/**
 *
 * @author vector
 */
public class ReporteBL {

    ReporteDAO dao;

    public ReporteBL() {
        dao = new ReporteDAO();
    }

    public List<ReporteSalidaMaterialBE> salidaMateriales(Date desde, Date hasta) throws Exception {
        return dao.salidaMateriales(desde, hasta);
    }
    
    
}
