/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import dao.ProductoDAO;
import entity.ProductoBE;
import java.util.List;

/**
 *
 * @author admin
 */
public class ProductoBL {
    
    ProductoDAO pdao;
    
    public ProductoBL() {
        pdao = new ProductoDAO();
    }
    
    public List<ProductoBE> ProductoListar(ProductoBE pbe) throws Exception {
        return pdao.read(pbe);
    }
    
    public ProductoBE GetData(int id_producto) throws Exception {
        return pdao.readId(id_producto);
    }
    
    public int ProductoEliminar(ProductoBE pbe) throws Exception {
        return pdao.delete(pbe);
    }    

    public int crear(ProductoBE pbe) throws Exception {
        return pdao.create(pbe);
    }
}
