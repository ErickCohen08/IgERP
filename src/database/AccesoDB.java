package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class AccesoDB {

    public AccesoDB() {
    }

    public static Connection getConnection() throws SQLException, InstantiationException, IllegalAccessException, Exception {
        Connection cn = null;
        try {
            // Parámetros de Connexión
            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            String url = "jdbc:sqlserver://" + getData("conexion\\sqlserver.txt") + ";databaseName=" + getData("conexion\\databaseName.txt") + "";
            String user = getData("conexion\\user.txt");
            String pwd = getData("conexion\\password.txt");

            Class.forName(driver).newInstance();
            cn = DriverManager.getConnection(url, user, pwd);
            System.out.println("Conexion realizada");
        } catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException ex) {
            throw new SQLException(ex);
        }
        return cn;
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
