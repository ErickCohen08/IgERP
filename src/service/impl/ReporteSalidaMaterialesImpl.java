/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service.impl;

import entity.ReporteSalidaMaterialBE;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import service.ReporteSalidaMateriales;

/**
 *
 * @author vector
 */
public class ReporteSalidaMaterialesImpl implements ReporteSalidaMateriales{

    @Override
    public void generaExcelAllSalidaMateriales(List<ReporteSalidaMaterialBE> listReporte, String rutaArchivo, String nombreArchivo) throws Exception {
        
        /* Abrimos la plantilla */
        //FileInputStream file = new FileInputStream(new File("reporteSalidaMateriales.xlsx"));
        FileInputStream file = new FileInputStream(new File("D:\\Documentos Erick Cohen\\ProyectosJava\\IgERP\\src\\recursos\\reporteSalidaMateriales.xlsx"));
	
        /* Creamos el libro */
        workbook = new HSSFWorkbook(file);
        HSSFRow row = null;
	HSSFCell cell = null;
        
        
    }
    
}
