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
public interface ReporteSalidaMateriales {
    public void generaExcelAllSalidaMateriales(List<ReporteSalidaMaterialBE> listReporte, String rutaArchivo, String nombreArchivo) throws Exception;
}
