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

    public void criarTabelas() {
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
        String sql2 = "CREATE TABLE horarios ("
                + "horario	TEXT NOT NULL UNIQUE"
                + ");";
        String sql3 = "CREATE UNIQUE INDEX horario ON horarios ("
                + "	horario"
                + ");";
        String sql4 = "INSERT INTO tbusuarios ("
                + "usuario, fone, login, senha, perfil, hora_in, hora_out) "
                + "VALUES ('admin', '(00)00000-0000', 'admin', '1234', 'admin', '', '');";
        String sql5 = "INSERT INTO horarios (horario) "
                + "VALUES ('08:00'), ('08:15'), ('08:30'), ('08:45'), ('09:00'), "
                + "('09:15'), ('09:30'), ('09:45'), ('10:00'), ('10:15'), "
                + "('10:30'), ('10:45'), ('11:00'), ('11:15'), ('11:30'), "
                + "('11:45'), ('13:00'), ('13:15'), ('13:30'), ('13:45'), "
                + "('14:00'), ('14:15'), ('14:30'), ('14:45'), ('15:00'), "
                + "('15:15'), ('15:30'), ('15:45'), ('16:00'), ('16:15'), "
                + "('16:30'), ('16:45');";
        String sql6 = "CREATE TABLE tbservicos ("
                + "id	INTEGER NOT NULL UNIQUE, "
                + "nome TEXT NOT NULL, "
                + "usuario	TEXT NOT NULL, "
                + "valor	TEXT NOT NULL, "
                + "duracao	TEXT DEFAULT NULL, "
                + "PRIMARY KEY(id AUTOINCREMENT)"
                + ");";
        String sql7 = "CREATE TABLE horarios_agendados ("
                + "	data	TEXT NOT NULL,"
                + "	funcionario	TEXT NOT NULL,"
                + "	horario	TEXT NOT NULL"
                + ");";
        String sql8 = "CREATE TABLE tbclientes ("
                + "	id	INTEGER NOT NULL UNIQUE,"
                + "	nome	TEXT NOT NULL,"
                + "	sexo	TEXT DEFAULT NULL,"
                + "	cpf	TEXT NOT NULL,"
                + "	endereco	TEXT NOT NULL,"
                + "	fone	TEXT NOT NULL,"
                + "	PRIMARY KEY(id AUTOINCREMENT)"
                + ");";
        String sql9 = "CREATE UNIQUE INDEX agendamento ON horarios_agendados ("
                + "	data,"
                + "	funcionario,"
                + "	horario"
                + ");";
        String sql10 = "CREATE TABLE tbhorarios ("
                + "	id	INTEGER NOT NULL,"
                + "	cliente	TEXT NOT NULL,"
                + "	servico	TEXT NOT NULL,"
                + "	data	TEXT NOT NULL,"
                + "	horario	TEXT NOT NULL,"
                + "	profissional	TEXT NOT NULL,"
                + "	id_ser	INTEGER DEFAULT NULL,"
                + "	FOREIGN KEY(id_ser) REFERENCES tbservicos(id),"
                + "	PRIMARY KEY(id AUTOINCREMENT)"
                + ");";

        // executando o sql de criar tabelas
        try {
            this.conexaoSqlite.conectar();
            Statement stmt1 = this.conexaoSqlite.criarStatement();
            Statement stmt2 = this.conexaoSqlite.criarStatement();
            Statement stmt3 = this.conexaoSqlite.criarStatement();
            Statement stmt4 = this.conexaoSqlite.criarStatement();
            Statement stmt5 = this.conexaoSqlite.criarStatement();
            Statement stmt6 = this.conexaoSqlite.criarStatement();
            Statement stmt7 = this.conexaoSqlite.criarStatement();
            Statement stmt8 = this.conexaoSqlite.criarStatement();
            Statement stmt9 = this.conexaoSqlite.criarStatement();
            Statement stmt10 = this.conexaoSqlite.criarStatement();
            stmt1.execute(sql);            
            stmt2.execute(sql2);          
            stmt3.execute(sql3);           
            stmt4.execute(sql4);           
            stmt5.execute(sql5);         
            stmt6.execute(sql6);      
            stmt7.execute(sql7);           
            stmt8.execute(sql8);      
            stmt9.execute(sql9);         
            stmt10.execute(sql10);

        } catch (SQLException e) {
            System.out.println(e);
        }

    }
}
