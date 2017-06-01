package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class AccesoDB {

    private AccesoDB() {
    }

    public static Connection getConnection() throws SQLException, InstantiationException, IllegalAccessException {
        Connection cn = null;
        try {
            // Parámetros de Connexión
            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            String url = "jdbc:sqlserver://ERCO-PC:1433;databaseName=bd_ig-projet";
            String user = "sa";
            String pwd = "Erco.123";
            
//            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//            String url = "jdbc:sqlserver://172.22.1.20:1433;databaseName=bd_ig-projet";
//            String user = "sa";
//            String pwd = "V3ct0r";

            // cargar driver en memoria
            Class.forName(driver).newInstance();
            //obtener la conexion a la BD
            cn = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException ex) {
            throw new SQLException(ex);
        } 
//        catch (InstantiationException | IllegalAccessException e) {
//            throw new SQLException("No se puede acceder a la base de datos.");
//        }
        return cn;
    }

}
