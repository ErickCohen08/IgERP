/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.ReporteSalidaMaterialBE;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import service.ReporteService;

/**
 *
 * @author vector
 */
public class ReporteDAO implements ReporteService {

    @Override
    public List<ReporteSalidaMaterialBE> salidaMateriales(Date desde, Date hasta) throws Exception {
        ReporteSalidaMaterialBE obj;
        List<ReporteSalidaMaterialBE> list = new ArrayList<>();

        for (int i = 1; i < 101; i++) {
            obj = new ReporteSalidaMaterialBE();
            obj.setItemLista(i);
            obj.setNombreMaterial("Material " + i);
            obj.setUnidadMaterial("Unidad " + i);
            obj.setCategoriaMaterial("Categoria " + i);
            obj.setNumeroSalida("0000000000" + i);
            obj.setCantidadSalida(i * 100);
            obj.setFechaSalida(new Date());
            obj.setObra("Obra " + i);
            obj.setDireccionObra("Direccion " + i);
            list.add(obj);
        }
        return list;
    }
}
