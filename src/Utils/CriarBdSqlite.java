/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author hans
 */
public class CriarBdSqlite {

    private final ConexaoSQLITE conexaoSqlite;

    // construtor
    public CriarBdSqlite(ConexaoSQLITE pconexaoSqlite) {
        this.conexaoSqlite = pconexaoSqlite;
    }

    public void criarTabelaUsuarios() {
        String sql = "CREATE TABLE tbusuarios ("
                + " id	INTEGER NOT NULL,"
                + " usuario	TEXT NOT NULL,"
                + " fone	TEXT NOT NULL,"
                + " login	TEXT NOT NULL,"
                + " senha	TEXT NOT NULL,"
                + " perfil	TEXT NOT NULL,"
                + " hora_in	TEXT DEFAULT NULL,"
                + " hora_out TEXT DEFAULT NULL,"
                + " PRIMARY KEY(id)"
                + ");";

        // executando o sql de criar tabelas
        boolean conectou = false;

        try {
            conectou = this.conexaoSqlite.conectar();
            Statement stmt = this.conexaoSqlite.criarStatement();
            stmt.execute(sql);
            System.out.println("Tabela usuarios criada!!!");
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (conectou) {
                this.conexaoSqlite.desconectar();
            }
        }

    }
}
