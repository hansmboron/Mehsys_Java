package view;

import Utils.ConexaoSQLITE;
import java.awt.HeadlessException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author hans
 */
public class Tela_Hor extends javax.swing.JInternalFrame {
    // instanciar conexao com banco
    ConexaoSQLITE conexao = new ConexaoSQLITE();
    PreparedStatement pst = null;
    ResultSet rs = null;
    

    DefaultListModel Modelo;

    public Tela_Hor() {
        initComponents();
        conexao.conectar();
        this.popula_cbbSer();
        this.popula_cbbPro();
        lisCli_hor.setVisible(false);
        Modelo = new DefaultListModel();
        lisCli_hor.setModel(Modelo);
        
        txtId_ser.setVisible(false);

        pesquisar_cli();
    }

    // preenche lista com clientes do banco de dados
    public final void popula_lisC() {
        lisCli_hor.clearSelection();
        String sql = "select nome from tbclientes where nome like '" + txtCli_hor.getText() + "%' order by nome";
        try {
            Modelo.removeAllElements();
            pst = conexao.criarPreparedStatement(sql);
            rs = pst.executeQuery();
            int v = 0;
            while (rs.next() && v < 5) {
                Modelo.addElement(rs.getString("nome"));
                v++;
            }

            if (v >= 1) {
                lisCli_hor.setVisible(true);
            } else {
                lisCli_hor.setVisible(false);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // preenche combobox com serviços do banco de dados
    public final void popula_cbbSer() {
        cbbSer_hor.removeAllItems();
        cbbSer_hor.addItem("Selecionar");
        String sql = "select nome, id from tbservicos order by nome";
        try {
            pst = conexao.criarPreparedStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                cbbSer_hor.addItem(rs.getString("nome") + "  - " + rs.getString("id"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // preenche combobox com profissionais cadastrados do banco de dados
    public final void popula_cbbPro() {
        cbbPro_hor.removeAllItems();
        cbbPro_hor.addItem("Selecionar");
        String sql = "select usuario from tbusuarios order by usuario";
        try {
            pst = conexao.criarPreparedStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                cbbPro_hor.addItem(rs.getString("usuario"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método para preencher cbb horarios de acorde com funcionario e data
    public void preencher_horarios(String profissional, String data) {
        cbbHor_hor.removeAllItems();
        cbbHor_hor.addItem("Selecionar");
        String sql = "select p.* from (select '" + data + "' data, '" + profissional + "' funcionario, horario from horarios) p left join horarios_agendados c on p.horario=c.horario and p.data=c.data and p.funcionario=c.funcionario where c.data is null";
        try {
            pst = conexao.criarPreparedStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                cbbHor_hor.addItem(rs.getString("horario"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, " Erro a buscar dados " + ex);
        }
        //conexao.close(); depois crie um metodo para fechar coneção
    }

    // método para limpar campos e retornar ao estado inicial
    private void limpar() {
        txtId_hor.setText(null);
        txtId_ser.setText(null);
        txtCli_hor.setText(null);
        cbbSer_hor.setSelectedIndex(0);
        txtDat_hor.setText(null);
        cbbHor_hor.removeAllItems();
        cbbHor_hor.addItem("Selecione");
        cbbPro_hor.setSelectedIndex(0);

        btnNov_hor.setEnabled(true);
        btnEdi_hor.setEnabled(false);
        lisCli_hor.setVisible(false);
        cbbSer_hor.setEnabled(true);

        this.popula_cbbSer();
        this.popula_cbbPro();
    }

    // metodo para adicionar novo horario
    private void adicionar() {
        String sql = "insert into tbhorarios(cliente, servico, data, horario, profissional, id_ser) values(?,?,?,?,?,?)";
        String sql2 = "insert into horarios_agendados(data, funcionario, horario) values(?,?,?)";
        try {
            pst = conexao.criarPreparedStatement(sql);
            pst.setString(1, txtCli_hor.getText());
            pst.setString(2, cbbSer_hor.getSelectedItem().toString());
            pst.setString(3, txtDat_hor.getText());
            // no banco salva com segundos, mas não queremos os segundos então utilizamos .substring para fatiar
            pst.setString(4, cbbHor_hor.getSelectedItem().toString());
            pst.setString(5, cbbPro_hor.getSelectedItem().toString());
            pst.setInt(6, Integer.parseInt(txtId_ser.getText()));

            PreparedStatement pst2 = conexao.criarPreparedStatement(sql2);
            pst2.setString(1, txtDat_hor.getText());
            pst2.setString(2, cbbPro_hor.getSelectedItem().toString());
            pst2.setString(3, cbbHor_hor.getSelectedItem().toString());

            // validação dos campos obrigatorios
            if (txtCli_hor.getText().trim().length() < 4 || cbbSer_hor.getSelectedIndex() == 0
                    || txtDat_hor.getText().trim().length() < 10
                    || cbbHor_hor.getSelectedIndex() == 0 || cbbPro_hor.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            } else {

                // a linha abaixo atualiza a tabela de horários com os dados do formu
                // a estrutura abaixo é usada para confirmar a insersão de dados
                int adicionado = pst.executeUpdate();  // retorna 1 ou 0
                int adicionado2 = pst2.executeUpdate();
                if (adicionado > 0 && adicionado2 > 0) {
                    JOptionPane.showMessageDialog(null, "Agendamento adicionado com sucesso!");
                    this.limpar();
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
        }
    }

    // metodo para alterar dados do agendamento
    private void alterar() {
        String sql = "update tbhorarios set cliente=?, servico=?, data=?, horario=?, profissional=?, id_ser=? where id=?";
        String sql2 = "update horarios_agendados set data=?, funcionario=?, horario=? where data=? and funcionario=? and horario=?";

        try {
            pst = conexao.criarPreparedStatement(sql);
            pst.setString(1, txtCli_hor.getText());
            pst.setString(2, cbbSer_hor.getSelectedItem().toString());
            pst.setString(3, txtDat_hor.getText());
            pst.setString(4, cbbHor_hor.getSelectedItem().toString());
            pst.setString(5, cbbPro_hor.getSelectedItem().toString());
            pst.setInt(6, Integer.parseInt(txtId_ser.getText()));
            pst.setString(7, txtId_hor.getText());

            PreparedStatement pst2 = conexao.criarPreparedStatement(sql2);
            pst2.setString(1, txtDat_hor.getText());
            pst2.setString(2, cbbPro_hor.getSelectedItem().toString());
            pst2.setString(3, cbbHor_hor.getSelectedItem().toString());
            pst2.setString(4, tblHor.getModel().getValueAt(tblHor.getSelectedRow(), 3).toString());
            pst2.setString(5, tblHor.getModel().getValueAt(tblHor.getSelectedRow(), 5).toString());
            pst2.setString(6, tblHor.getModel().getValueAt(tblHor.getSelectedRow(), 4).toString());

            // validação dos campos obrigatorios
            if (txtCli_hor.getText().trim().length() < 4
                    || cbbSer_hor.getSelectedIndex() == 0 || txtDat_hor.getText().trim().length() < 10
                    || cbbHor_hor.getSelectedIndex() == 0 || cbbPro_hor.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            } else {

                // a linha abaixo atualiza a tabela de horarios com os dados do formu
                // a estrutura abaixo é usada para confirmar a alteração de dados
                int adicionado2 = pst2.executeUpdate();
                int adicionado = pst.executeUpdate();  // retorna 1 ou 0

                if (adicionado > 0 && adicionado2 > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do agendamento alterados com sucesso!");
                    this.limpar();
                }
            }
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);
        }
    }

    // método para consultar cliente no bd
    private void pesquisar_cli() {
        String sql = "select id as ID, cliente as Cliente, servico as Serviço, data as Data, horario as Horário, profissional as Profissional from tbhorarios where cliente like ?";
        try {
            pst = conexao.criarPreparedStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            // atençao ao % que é a continuação da string sql
            pst.setString(1, txtPesC_hor.getText() + "%");
            rs = pst.executeQuery();
            // a linha abaixo usa a biblioteca rs2xml.jar
            tblHor.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método para consultar cliente no bd
    private void pesquisar_data() {
        String sql = "select id as ID, cliente as Cliente, servico as Serviço, data as Data, horario as Horário, profissional as Profissional from tbhorarios where data like ?";
        try {
            pst = conexao.criarPreparedStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            // atençao ao % que é a continuação da string sql
            pst.setString(1, txtPesD_hor.getText() + "%");
            rs = pst.executeQuery();
            // a linha abaixo usa a biblioteca rs2xml.jar
            tblHor.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // metodo para setar campos do formulario com dados da tabela
    private void setar_campos() {
        this.popula_cbbSer();
        int setar = tblHor.getSelectedRow();
        txtId_hor.setText(tblHor.getModel().getValueAt(setar, 0).toString());
        txtCli_hor.setText(tblHor.getModel().getValueAt(setar, 1).toString());
        cbbSer_hor.setSelectedItem(tblHor.getModel().getValueAt(setar, 2));
        String id_ser = (String) tblHor.getModel().getValueAt(setar, 2);
        txtId_ser.setText(id_ser.substring(id_ser.length() - 5, id_ser.length()).replace("-", "").trim());
        txtDat_hor.setText(tblHor.getModel().getValueAt(setar, 3).toString());
        cbbHor_hor.setSelectedItem(tblHor.getModel().getValueAt(setar, 4));
        cbbPro_hor.setSelectedItem(tblHor.getModel().getValueAt(setar, 5));

        // desabilita btn adicionar e habilita atualizar quando linha é selecionada
        btnNov_hor.setEnabled(false);
        btnEdi_hor.setEnabled(true);
        cbbSer_hor.setEnabled(false);
    }

    // metodo para remover usuário do banco de dados
    private void remover() {
        if (!txtId_hor.getText().isEmpty()) {
            int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este agendamento?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {
                String sql = "delete from tbhorarios where id=?";
                String sql2 = "delete from horarios_agendados where data=? and funcionario=? and horario=?";
                try {
                    pst = conexao.criarPreparedStatement(sql);
                    pst.setString(1, txtId_hor.getText());

                    PreparedStatement pst2 = conexao.criarPreparedStatement(sql2);
                    pst2.setString(1, txtDat_hor.getText());
                    pst2.setString(2, cbbPro_hor.getSelectedItem().toString());
                    pst2.setString(3, tblHor.getModel().getValueAt(tblHor.getSelectedRow(), 4).toString());

                    int apagado = pst.executeUpdate();
                    int apagado2 = pst2.executeUpdate();
                    if (apagado > 0 && apagado2 > 0) {
                        JOptionPane.showMessageDialog(null, "Agendamento removido com sucesso!");
                        this.limpar();
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecione um agendamento para poder excluir!");
        }
    }

    //select * from tbhorarios where id_hor like  $P{os}
    // imprimindo uma ordem se serviço
    private void imprimir_age() {
        if (!txtId_hor.getText().isEmpty()) {
            int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impresão do agendamento número " + txtId_hor.getText() + "?", "OS", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {
                // imprimindo relatorio com framework JasperReport
                try {
                    // usando a class HashMap para criar um filtro
                    HashMap filtro = new HashMap();
                    filtro.put("os", Integer.parseInt(txtId_hor.getText()));
                    // "os" tem que ser igual à variavel criada no Jaspersoft

                    // usuando a classe JasperPrint para preparar a impressoa do relatorio
                    JasperPrint print = JasperFillManager.fillReport("rel/rel_comprovante.jasper", filtro, conexao.getConexao());
                    // a linha abaixo exibe o relatório atraves da classe JasperViwer
                    JasperViewer.viewReport(print, false);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Para imprimir:\nÉ necessário selecionar um agendamento na tabela!");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnNov_hor = new javax.swing.JButton();
        btnEdi_hor = new javax.swing.JButton();
        btnDel_hor = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnLim = new javax.swing.JButton();
        txtId_hor = new javax.swing.JTextField();
        cbbSer_hor = new javax.swing.JComboBox<>();
        lisCli_hor = new javax.swing.JList<>();
        txtCli_hor = new javax.swing.JTextField();
        btnImp_hor = new javax.swing.JButton();
        cbbPro_hor = new javax.swing.JComboBox<>();
        cbbHor_hor = new javax.swing.JComboBox<>();
        txtDat_hor = new javax.swing.JFormattedTextField();
        btnCarregarhor = new javax.swing.JButton();
        txtId_ser = new javax.swing.JTextField();
        btnNew_cli = new javax.swing.JButton();
        btnNew_ser = new javax.swing.JButton();
        btnPesC_hor = new javax.swing.JButton();
        txtPesC_hor = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHor = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        btnPesD_hor = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtPesD_hor = new javax.swing.JTextField();

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Tela Horários");
        setPreferredSize(new java.awt.Dimension(670, 568));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Agendamento de horários"));

        jLabel1.setText("Id:*");

        jLabel2.setText("Cliente:*");

        jLabel3.setText("Horário:*");

        jLabel4.setText("Profissional:*");

        jLabel5.setText("Data:*");

        jLabel6.setText("Serviço:*");

        btnNov_hor.setForeground(new java.awt.Color(0, 200, 0));
        btnNov_hor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nov_blue.png"))); // NOI18N
        btnNov_hor.setText("Agendar");
        btnNov_hor.setToolTipText("Salvar agendamento no banco de dados");
        btnNov_hor.setPreferredSize(new java.awt.Dimension(100, 33));
        btnNov_hor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNov_horActionPerformed(evt);
            }
        });

        btnEdi_hor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edi_blue.png"))); // NOI18N
        btnEdi_hor.setText("Atualizar");
        btnEdi_hor.setToolTipText("Atualizar agendamento selecionado");
        btnEdi_hor.setEnabled(false);
        btnEdi_hor.setPreferredSize(new java.awt.Dimension(100, 33));
        btnEdi_hor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEdi_horActionPerformed(evt);
            }
        });

        btnDel_hor.setForeground(java.awt.Color.red);
        btnDel_hor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/del_blue.png"))); // NOI18N
        btnDel_hor.setText("Excluir");
        btnDel_hor.setToolTipText("Excluir agendamento selecionado");
        btnDel_hor.setPreferredSize(new java.awt.Dimension(100, 33));
        btnDel_hor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDel_horActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Noto Sans", 0, 10)); // NOI18N
        jLabel7.setText("* Campos Obrigatórios");

        btnLim.setText("Limpar");
        btnLim.setToolTipText("Limpar campos");
        btnLim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimActionPerformed(evt);
            }
        });

        txtId_hor.setToolTipText("número do agendamento");
        txtId_hor.setEnabled(false);

        cbbSer_hor.setMaximumRowCount(16);
        cbbSer_hor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        cbbSer_hor.setToolTipText("Selecione serviço");
        cbbSer_hor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbbSer_horMouseClicked(evt);
            }
        });
        cbbSer_hor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbSer_horActionPerformed(evt);
            }
        });

        lisCli_hor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lisCli_horMouseClicked(evt);
            }
        });

        txtCli_hor.setToolTipText("Pesquisar clientes");
        txtCli_hor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCli_horKeyReleased(evt);
            }
        });

        btnImp_hor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/imp2.png"))); // NOI18N
        btnImp_hor.setText("Imprimir");
        btnImp_hor.setToolTipText("Imprimir agendamento selecionado");
        btnImp_hor.setPreferredSize(new java.awt.Dimension(100, 33));
        btnImp_hor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImp_horActionPerformed(evt);
            }
        });

        cbbPro_hor.setMaximumRowCount(16);
        cbbPro_hor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        cbbPro_hor.setToolTipText("Selecione profissional que realizará o serviço");

        cbbHor_hor.setMaximumRowCount(17);
        cbbHor_hor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecionar" }));
        cbbHor_hor.setToolTipText("Selecione horário á ser agendado");

        try {
            txtDat_hor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDat_hor.setToolTipText("Data à ser agendado, DD/MM/AAAA");

        btnCarregarhor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/refres.png"))); // NOI18N
        btnCarregarhor.setToolTipText("<html>Clique aqui para atualizar lista de horários disponíveis<br>para evitar conflitos de agendamentos!</html>");
        btnCarregarhor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCarregarhorActionPerformed(evt);
            }
        });

        btnNew_cli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/novo.png"))); // NOI18N
        btnNew_cli.setToolTipText("<html>Clique aqui para adicionar um cliente<br>que não esteja cadastrado<html/>");
        btnNew_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_cliActionPerformed(evt);
            }
        });

        btnNew_ser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/novo.png"))); // NOI18N
        btnNew_ser.setToolTipText("<html>Clique aqui para adicionar um serviço<br>que não esteja cadastrado<html/>");
        btnNew_ser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_serActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(btnNov_hor, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEdi_hor, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDel_hor, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImp_hor, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLim)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtId_hor, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtId_ser, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(txtCli_hor, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(btnNew_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(lisCli_hor, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnCarregarhor)
                            .addComponent(cbbPro_hor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(cbbHor_hor, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtDat_hor)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(cbbSer_hor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnNew_ser, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)))))
                .addGap(182, 182, 182))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(txtId_hor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(cbbSer_hor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtId_ser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnNew_ser, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCli_hor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(btnNew_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lisCli_hor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cbbPro_hor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtDat_hor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCarregarhor, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(cbbHor_hor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNov_hor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdi_hor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDel_hor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLim)
                    .addComponent(btnImp_hor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        btnPesC_hor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pes_blue.png"))); // NOI18N
        btnPesC_hor.setToolTipText("Pesquisar clientes por nome");
        btnPesC_hor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesC_horActionPerformed(evt);
            }
        });

        txtPesC_hor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesC_horKeyReleased(evt);
            }
        });

        tblHor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblHor.setToolTipText("Selecione uma linha! Não edite a tabela!");
        tblHor.setEditingRow(0);
        tblHor.getTableHeader().setReorderingAllowed(false);
        tblHor.setUpdateSelectionOnSort(false);
        tblHor.setVerifyInputWhenFocusTarget(false);
        tblHor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHorMouseClicked(evt);
            }
        });
        tblHor.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                tblHorInputMethodTextChanged(evt);
            }
        });
        tblHor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblHorKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblHor);

        jLabel8.setText("Cliente:");

        btnPesD_hor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pes_blue.png"))); // NOI18N
        btnPesD_hor.setToolTipText("Pesquisar clientes por nome");
        btnPesD_hor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesD_horActionPerformed(evt);
            }
        });

        jLabel9.setText("Data:");

        txtPesD_hor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesD_horKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesC_hor, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesC_hor)
                        .addGap(45, 45, 45)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesD_hor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesD_hor))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 638, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPesC_hor)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(txtPesC_hor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(btnPesD_hor))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPesD_hor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addContainerGap())
        );

        setBounds(0, 0, 670, 568);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNov_horActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNov_horActionPerformed
        adicionar();
        pesquisar_cli();
    }//GEN-LAST:event_btnNov_horActionPerformed

    private void btnEdi_horActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEdi_horActionPerformed
        alterar();
        pesquisar_cli();
    }//GEN-LAST:event_btnEdi_horActionPerformed

    private void btnDel_horActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDel_horActionPerformed
        remover();
        pesquisar_cli();
    }//GEN-LAST:event_btnDel_horActionPerformed

    private void txtPesC_horKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesC_horKeyReleased
        pesquisar_cli();
    }//GEN-LAST:event_txtPesC_horKeyReleased

    private void btnPesC_horActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesC_horActionPerformed
        pesquisar_cli();
    }//GEN-LAST:event_btnPesC_horActionPerformed

    private void tblHorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHorMouseClicked
        setar_campos();
        String nome = (String) cbbPro_hor.getSelectedItem();
        String data = txtDat_hor.getText();
        preencher_horarios(nome, data);
        setar_campos();
    }//GEN-LAST:event_tblHorMouseClicked

    private void btnLimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimActionPerformed

    private void btnImp_horActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImp_horActionPerformed
        imprimir_age();
    }//GEN-LAST:event_btnImp_horActionPerformed

    private void btnPesD_horActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesD_horActionPerformed
        pesquisar_data();
    }//GEN-LAST:event_btnPesD_horActionPerformed

    private void txtCli_horKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCli_horKeyReleased
        popula_lisC();
    }//GEN-LAST:event_txtCli_horKeyReleased

    private void lisCli_horMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lisCli_horMouseClicked
        txtCli_hor.setText((String) lisCli_hor.getSelectedValue());
        lisCli_hor.setVisible(false);
    }//GEN-LAST:event_lisCli_horMouseClicked

    private void cbbSer_horMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbbSer_horMouseClicked
        
    }//GEN-LAST:event_cbbSer_horMouseClicked

    private void txtPesD_horKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesD_horKeyReleased
        pesquisar_data();
    }//GEN-LAST:event_txtPesD_horKeyReleased

    private void btnCarregarhorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCarregarhorActionPerformed
        String nome = (String) cbbPro_hor.getSelectedItem();
        String data = txtDat_hor.getText();
        preencher_horarios(nome, data);
    }//GEN-LAST:event_btnCarregarhorActionPerformed

    private void tblHorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblHorKeyPressed
        JOptionPane.showMessageDialog(null, "Não edite a tabela, pois por aqui ela não será alterada no banco de dados!");
        this.limpar();
        this.pesquisar_cli();
    }//GEN-LAST:event_tblHorKeyPressed

    private void tblHorInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblHorInputMethodTextChanged

    }//GEN-LAST:event_tblHorInputMethodTextChanged

    private void cbbSer_horActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbSer_horActionPerformed
        try {
            if (cbbSer_hor.getSelectedItem() != "Selecionar") {
                String id_ser = (String) cbbSer_hor.getSelectedItem();
                txtId_ser.setText(id_ser.substring(id_ser.length() - 5, id_ser.length()).replace("-", "").trim());
            }
        } catch (Exception e) {
            txtId_ser.setText(null);
        }
    }//GEN-LAST:event_cbbSer_horActionPerformed

    private void btnNew_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_cliActionPerformed
        Tela_Cli c = new Tela_Cli();
        c.setVisible(true);
        Tela_Principal.desk.removeAll();
        Tela_Principal.desk.add(c);
    }//GEN-LAST:event_btnNew_cliActionPerformed

    private void btnNew_serActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_serActionPerformed
        Tela_Ser s = new Tela_Ser();
        s.setVisible(true);
        Tela_Principal.desk.removeAll();
        Tela_Principal.desk.add(s);
    }//GEN-LAST:event_btnNew_serActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCarregarhor;
    private javax.swing.JButton btnDel_hor;
    private javax.swing.JButton btnEdi_hor;
    private javax.swing.JButton btnImp_hor;
    private javax.swing.JButton btnLim;
    private javax.swing.JButton btnNew_cli;
    private javax.swing.JButton btnNew_ser;
    private javax.swing.JButton btnNov_hor;
    private javax.swing.JButton btnPesC_hor;
    private javax.swing.JButton btnPesD_hor;
    private javax.swing.JComboBox<Object> cbbHor_hor;
    private javax.swing.JComboBox<Object> cbbPro_hor;
    private javax.swing.JComboBox<Object> cbbSer_hor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<Object> lisCli_hor;
    private javax.swing.JTable tblHor;
    private javax.swing.JTextField txtCli_hor;
    private javax.swing.JFormattedTextField txtDat_hor;
    public static javax.swing.JTextField txtId_hor;
    private javax.swing.JTextField txtId_ser;
    public static javax.swing.JTextField txtPesC_hor;
    private javax.swing.JTextField txtPesD_hor;
    // End of variables declaration//GEN-END:variables
}
