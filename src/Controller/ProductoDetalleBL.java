/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import dao.ProductoDetalleDAO;
import entity.ProductoDetalleBE;
import java.util.List;

/**
 *
 * @author admin
 */
public class ProductoDetalleBL {
    
    ProductoDetalleDAO pdao;
    
    public ProductoDetalleBL() {
        pdao = new ProductoDetalleDAO();
    }
    
    public List<ProductoDetalleBE> Listar(ProductoDetalleBE pbe) throws Exception {
        return pdao.read(pbe);
    }
    
    public int Eliminar(ProductoDetalleBE pbe) throws Exception {
        return pdao.delete(pbe);
    }    

    public int crear(ProductoDetalleBE pbe) throws Exception {
        return pdao.create(pbe);
    }
}
