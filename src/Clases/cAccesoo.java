package Clases;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class cAccesoo {

    private String error;

    public String geterror() {
        return error;
    }

    public Connection getConnection() {
        error = null;
        Connection cn = null;
        
        try {
            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            String url = "jdbc:sqlserver://" + getData("conexion\\sqlserver.txt") + ";databaseName=" + getData("conexion\\databaseName.txt") + "";
            String user = getData("conexion\\user.txt");
            String pwd = getData("conexion\\password.txt");

            Class.forName(driver).newInstance();
            cn = DriverManager.getConnection(url, user, pwd);
            
        } catch (Exception e) {
            error = e.toString();
        }
        return cn;
    }

    public void closeConnection(Connection con) {
        error = null;
        try {
            con.close();
        } catch (Exception e) {
            error = e.toString();
        }
    }

    private static String getData(String archivo) throws Exception {
        try {
            String text;
            try (FileReader fichero = new FileReader(archivo)) {
                BufferedReader br = new BufferedReader(fichero);
                text = br.readLine();
            }
            return text.trim();
        } catch (IOException e) {
            throw e;
        }
    }

}
