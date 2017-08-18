/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.SQLException;

public class prueba {

    public static void main(String[] args) throws Exception {
        try {
            Connection cn = AccesoDB.getConnection();
            System.out.println("Conexion conforme....");
        } catch (IllegalAccessException e) {
            System.out.println("error :" + e.getMessage());
        } catch (InstantiationException e) {
            System.out.println("error :" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("error :" + e.getMessage());
        }
    }
}
