/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import dao.PersonalDAO;
import entity.PersonalBE;
import java.util.List;

/**
 *
 * @author admin
 */
public class PersonalBL {

    PersonalDAO pdao;
    
    public PersonalBL() {
        pdao = new PersonalDAO();
    }
    
    public List<PersonalBE> read(PersonalBE pbe) throws Exception {
        return pdao.read(pbe);
    }   
    
}
