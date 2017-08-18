package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class AccesoDB {

    private static String getData(String archivo) throws Exception{
        try{
           FileReader fichero = new FileReader(archivo);
           BufferedReader br = new BufferedReader (fichero);
           String text = br.readLine();
           fichero.close();
           return text.trim();
        }catch (IOException e){
            throw e;
        }
    }
    
    private AccesoDB() {
    }

    public static Connection getConnection() throws SQLException, InstantiationException, IllegalAccessException, Exception {
        Connection cn = null;
        try {
            // Parámetros de Connexión
            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            String url = "jdbc:sqlserver://"+getData("conexion\\sqlserver.txt")+";databaseName="+getData("conexion\\databaseName.txt")+"";
            String user = getData("conexion\\user.txt");
            String pwd = getData("conexion\\password.txt");
            
//            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//            String url = "jdbc:sqlserver://172.22.1.20:1433;databaseName=bd_ig-projet";
//            String user = "sa";
//            String pwd = "V3ct0r";

//            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//            String url = "jdbc:sqlserver://VP000058\\VECTOR;databaseName=bd_ig-projet";
//            String user = "sa";
//            String pwd = "vector2016";
            
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
