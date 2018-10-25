/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entity.ReporteSalidaMaterialBE;
import java.util.Date;
import java.util.List;

/**
 *
 * @author vector
 */
public interface ReporteService {

    List<ReporteSalidaMaterialBE> salidaMateriales(Date desde, Date hasta) throws Exception;
}
