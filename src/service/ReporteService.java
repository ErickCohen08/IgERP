/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entity.ReporteSalidaMaterialBE;
import java.util.List;

/**
 *
 * @author vector
 */
public interface ReporteService {

    List<ReporteSalidaMaterialBE> salidaMaterialesEntreFechas(String desde, String hasta, int empresa) throws Exception;
    List<ReporteSalidaMaterialBE> salidaMaterialesAll(int empresa) throws Exception;
}
