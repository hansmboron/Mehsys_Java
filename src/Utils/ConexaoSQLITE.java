package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author hans
 */
public class ConexaoSQLITE {

    //private Connection conexao;
    private Connection conexao;

    // conecta ou cria se não existir um banco de dados local
    public boolean conectar() {
        String driver = "org.sqlite.JDBC";
        String url = "jdbc:sqlite:dbmehsys.db";
        try {
            Class.forName(driver);
            this.conexao = DriverManager.getConnection(url);
            System.out.println("Conectado");
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            return false;
        }
    }

    public boolean desconectar() {

        try {
            if (this.conexao.isClosed() == false) {
                this.conexao.close();
            }
            System.out.println("desconectado");
        } catch (SQLException e) {

            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Criar os statements para nossos sqls serem executados
     *
     * @param sql
     * @return
     */
    public PreparedStatement criarPreparedStatement(String sql) {
        try {
            return this.conexao.prepareStatement(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    
    public Statement criarStatement() {
        try {
            return this.conexao.createStatement();
        } catch (SQLException e) {
            return null;
        }
    }

    public Connection getConexao() {
        return this.conexao;
    }
}
