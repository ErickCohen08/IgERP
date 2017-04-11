package Clases;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

public class cAccesoo {
    private final String driver=controlador();
    private final String url=dsn();
    private final String user=usuario();
    private final String pass=pass(); 
   
    
    private String error;
    public String geterror(){
        return error;
    }
    public Connection getConnection(){
    error = null;
        try{
            Class.forName(driver);
            return (DriverManager.getConnection(url, user, pass));
        }catch (Exception e){error = e.toString();}
        return null;
    }
    
    public void closeConnection(Connection con){
        error = null;
        try{
            con.close();
        }catch (Exception e){error=e.toString();}
    }    
    
    
    public String controlador(){
        String controlador="";
        
       try{
           FileReader fichero = new FileReader("conexion\\controlador.txt");
           BufferedReader br= new BufferedReader (fichero);
           controlador=br.readLine();
           fichero.close();
            
        }catch (Exception e){System.out.println("Error: Ocurrio al traer el valor del archivo CONTROLADOR.TXT");}
              
        return controlador;
    }    
    public String dsn(){
        String dsn="";
        
       try{
           FileReader fichero = new FileReader("conexion\\dsn.txt");
           BufferedReader br= new BufferedReader (fichero);
           dsn=br.readLine();
           fichero.close();            
        }catch (Exception e){System.out.println("Error: Ocurrio al traer el valor del archivo DSN.TXT");}
              
        return dsn;
    }    
    public String usuario(){
        String usuario="";
        
       try{
           FileReader fichero = new FileReader("conexion\\user.txt");
           BufferedReader br= new BufferedReader (fichero);
           usuario=br.readLine();
           fichero.close();            
        }catch (Exception e){System.out.println("Error: Ocurrio al traer el valor del archivo USER.TXT");}
              
        return usuario;
    }
    public String pass(){
        String pass="";
        
       try{
           FileReader fichero = new FileReader("conexion\\password.txt");
           BufferedReader br= new BufferedReader (fichero);
           pass=br.readLine();
           fichero.close();            
        }catch (Exception e){System.out.println("Error: Ocurrio al traer el valor del archivo PASSWORD.TXT");}
              
        return pass;
    }
}



//package Clases;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//
//public class cAccesoo {
//
//    private final String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
//    private final String url = "jdbc:odbc:system_rysi";
//    private final String user = "sa";
//    private final String pass = "erco";
//    private String error;
//
//    public String geterror() {
//        return error;
//    }
//
//    public Connection getConnection() {
//        error = null;
//        try {
//            Class.forName(driver);
//            return (DriverManager.getConnection(url, user, pass));
//        } catch (Exception e) {
//            error = e.toString();
//        }
//        return null;
//    }
//
//    public void closeConnection(Connection con) {
//        error = null;
//        try {
//            con.close();
//        } catch (Exception e) {
//            error = e.toString();
//        }
//    }
//}