package dal;

import java.sql.*;

/**
 *
 * @author hans
 */
public class ModuloConexao {

    // método responsavel por estabelecer conexao com o banco
    public static Connection conector() {
//        java.sql.Connection conexao = null;
        // a linha abaixo "chama" o driver de conexao que eu importei
        //String driver = "com.mysql.cj.jdbc.Driver";
        String driver = "org.sqlite.JDBC";
        
        // Armazenando informações referentes ao banco
        //String url = "jdbc:mysql://192.168.100.108:3306/dbsalao?useSSL=false";
        String url = "jdbc:sqlite:db/dbmehsys.db";
        //String user = "hans";
        //String password = "Pato#47#";
        
//        String url = "jdbc:mysql://localhost:3306/dbsalao";
//        String user = "root";
//        String password = "";
        
        
        
        // estabelecendo a conexao com o bd
        try {
            Class.forName(driver);
            Connection conexao = DriverManager.getConnection(url);
            return conexao;
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }
    }
}
