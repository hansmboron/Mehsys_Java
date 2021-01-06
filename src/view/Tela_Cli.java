package view;

import Utils.ConexaoSQLITE;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author hans
 */
public class Tela_Cli extends javax.swing.JInternalFrame {

     ConexaoSQLITE conexao = new ConexaoSQLITE();
    PreparedStatement pst = null;
    ResultSet rs = null;

    public Tela_Cli() {
        initComponents();
        conexao.conectar();
        pesquisar_cliente();
    }

    private void limpar() {
        txtId_cli.setText(null);
        txtNom_cli.setText(null);
        cbbSex_cli.setSelectedIndex(0);
        txtCpf_cli.setText(null);
        txtEnd_cli.setText(null);
        txtFon_cli.setText(null);
        
        btnNov_cli.setEnabled(true);
    }
    
    
    // metodo para adicionar novo cliente
    public void adicionar() {
        String sql = "insert into tbclientes(nome, sexo, cpf, "
                + "endereco, fone) values(?,?,?,?,?)";
        try {
            pst = conexao.criarPreparedStatement(sql);
            pst.setString(1, txtNom_cli.getText());
            pst.setString(2, cbbSex_cli.getSelectedItem().toString());
            pst.setString(3, txtCpf_cli.getText());
            pst.setString(4, txtEnd_cli.getText());
            pst.setString(5, txtFon_cli.getText());

            // validação dos campos obrigatorios
            if (txtNom_cli.getText().isEmpty() || txtCpf_cli.getText().trim().length() < 14 
                    || txtFon_cli.getText().trim().length() < 14) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            } else {

                // a linha abaixo atualiza a tabela usuarios com os dados do formu
                // a estrutura abaixo é usada para confirmar a insersão de dados
                int adicionado = pst.executeUpdate();  // retorna 1 ou 0
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso!");
                    this.limpar();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // metodo para alterar dados do cliente
    private void alterar() {
        String sql = "update tbclientes set nome=?, sexo=?, cpf=?, endereco=?, fone=? where id=?";
        try {
            pst = conexao.criarPreparedStatement(sql);
            pst.setString(1, txtNom_cli.getText());
            pst.setString(2, cbbSex_cli.getSelectedItem().toString());
            pst.setString(3, txtCpf_cli.getText());
            pst.setString(4, txtEnd_cli.getText());
            pst.setString(5, txtFon_cli.getText());
            pst.setString(6, txtId_cli.getText());

            // validação dos campos obrigatorios
            if (txtNom_cli.getText().isEmpty() || txtCpf_cli.getText().trim().length() < 14 
                    || txtFon_cli.getText().trim().length() < 14) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            } else {
                // a linha abaixo atualiza a tabela usuarios com os dados do formu
                // a estrutura abaixo é usada para confirmar a alteração de dados
                int adicionado = pst.executeUpdate();  // retorna 1 ou 0
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do cliente alterados com sucesso!");
                    this.limpar();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método para consultar clientes no bd
    private void pesquisar_cliente() {
        String sql = "select id as ID, nome as Nome, sexo as Sexo, cpf as CPF, endereco as Ender, fone as Fone from tbclientes where nome like ?";
        try {
            pst = conexao.criarPreparedStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            // atençao ao % que é a continuação da string sql
            pst.setString(1, txtPes_cli.getText() + "%");
            rs = pst.executeQuery();
            // a linha abaixo usa a biblioteca rs2xml.jar
            tblCli.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // metodo para setar campos do formulario com dados da tabela
    private void setar_campos() {
        int setar = tblCli.getSelectedRow();
        txtId_cli.setText(tblCli.getModel().getValueAt(setar, 0).toString());
        txtNom_cli.setText(tblCli.getModel().getValueAt(setar, 1).toString());
        cbbSex_cli.setSelectedItem(tblCli.getModel().getValueAt(setar, 2));
        txtCpf_cli.setText(tblCli.getModel().getValueAt(setar, 3).toString());
        // caso esteja vazio no banco de dados ja que este campos não é obrigatorio
        try {
            txtEnd_cli.setText(tblCli.getModel().getValueAt(setar, 4).toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        txtFon_cli.setText(tblCli.getModel().getValueAt(setar, 5).toString());

        // a linha abaixo desabilita btn adicionar
        btnNov_cli.setEnabled(false);
    }
    
    // metodo para remover cliente do banco de dados
    private void remover() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este cliente?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbclientes where id=?";
            try {
                pst = conexao.criarPreparedStatement(sql);
                pst.setString(1, txtId_cli.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente removido com sucesso!");
                    limpar();
                    txtId_cli.setText(null);
                }
                 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
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

        jPanel1 = new javax.swing.JPanel();
        txtId_cli = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNom_cli = new javax.swing.JTextField();
        txtEnd_cli = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnNov_cli = new javax.swing.JButton();
        btnEdi_cli = new javax.swing.JButton();
        btnDel_cli = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnLim = new javax.swing.JButton();
        cbbSex_cli = new javax.swing.JComboBox<>();
        txtCpf_cli = new javax.swing.JFormattedTextField();
        txtFon_cli = new javax.swing.JFormattedTextField();
        btnPes_cli = new javax.swing.JButton();
        txtPes_cli = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCli = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Tela Clientes");
        setPreferredSize(new java.awt.Dimension(670, 568));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cadastro de novo cliente"));

        txtId_cli.setToolTipText("Número do cliente");
        txtId_cli.setEnabled(false);

        jLabel1.setText("Id:*");

        jLabel2.setText("Nome:*");

        jLabel3.setText("Ender:");

        jLabel4.setText("Fone:*");

        jLabel5.setText("CPF:*");

        txtNom_cli.setToolTipText("Nome do cliente");

        txtEnd_cli.setToolTipText("Endereço atual do cliente");

        jLabel6.setText("Sexo:");

        btnNov_cli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nov_blue.png"))); // NOI18N
        btnNov_cli.setText("Salvar");
        btnNov_cli.setToolTipText("Cadastrar novo cliente no sistema");
        btnNov_cli.setPreferredSize(new java.awt.Dimension(100, 33));
        btnNov_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNov_cliActionPerformed(evt);
            }
        });

        btnEdi_cli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edi_blue.png"))); // NOI18N
        btnEdi_cli.setText("Atualizar");
        btnEdi_cli.setToolTipText("Atualizar cliente selecionado");
        btnEdi_cli.setPreferredSize(new java.awt.Dimension(100, 33));
        btnEdi_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEdi_cliActionPerformed(evt);
            }
        });

        btnDel_cli.setForeground(java.awt.Color.red);
        btnDel_cli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/del_blue.png"))); // NOI18N
        btnDel_cli.setText("Excluir");
        btnDel_cli.setToolTipText("Excluir cliente selecionado");
        btnDel_cli.setPreferredSize(new java.awt.Dimension(100, 33));
        btnDel_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDel_cliActionPerformed(evt);
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

        cbbSex_cli.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Indefinido", "Feminino", "Masculino" }));
        cbbSex_cli.setToolTipText("Selecione sexo do cliente");

        try {
            txtCpf_cli.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCpf_cli.setToolTipText("CPF do cliente");

        try {
            txtFon_cli.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtFon_cli.setToolTipText("Telefone do cliente");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtId_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNom_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbSex_cli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEnd_cli, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                            .addComponent(txtCpf_cli)
                            .addComponent(txtFon_cli))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(btnNov_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEdi_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDel_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLim)
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtCpf_cli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtEnd_cli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtFon_cli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtId_cli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(txtNom_cli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(cbbSex_cli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNov_cli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdi_cli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDel_cli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLim))
                .addGap(22, 22, 22))
        );

        btnPes_cli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pes_blue.png"))); // NOI18N
        btnPes_cli.setText("Pesquisar");
        btnPes_cli.setToolTipText("Pesquisar clientes por nome");
        btnPes_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPes_cliActionPerformed(evt);
            }
        });

        txtPes_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPes_cliKeyReleased(evt);
            }
        });

        tblCli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nome", "Sexo", "CPF", "Ender", "Fone"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCliMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCli);
        if (tblCli.getColumnModel().getColumnCount() > 0) {
            tblCli.getColumnModel().getColumn(0).setResizable(false);
            tblCli.getColumnModel().getColumn(0).setPreferredWidth(20);
        }

        jLabel8.setText("Nome:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPes_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPes_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtPes_cli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPes_cli))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                .addContainerGap())
        );

        setBounds(0, 0, 670, 568);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNov_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNov_cliActionPerformed
        adicionar();
        pesquisar_cliente();
    }//GEN-LAST:event_btnNov_cliActionPerformed

    private void btnEdi_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEdi_cliActionPerformed
        alterar();
        pesquisar_cliente();
    }//GEN-LAST:event_btnEdi_cliActionPerformed

    private void btnDel_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDel_cliActionPerformed
        remover();
        pesquisar_cliente();
    }//GEN-LAST:event_btnDel_cliActionPerformed

    private void txtPes_cliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPes_cliKeyReleased
        pesquisar_cliente();
    }//GEN-LAST:event_txtPes_cliKeyReleased

    private void btnPes_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPes_cliActionPerformed
        pesquisar_cliente();
    }//GEN-LAST:event_btnPes_cliActionPerformed

    private void tblCliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCliMouseClicked
        setar_campos();
    }//GEN-LAST:event_tblCliMouseClicked

    private void btnLimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDel_cli;
    private javax.swing.JButton btnEdi_cli;
    private javax.swing.JButton btnLim;
    private javax.swing.JButton btnNov_cli;
    private javax.swing.JButton btnPes_cli;
    private javax.swing.JComboBox<String> cbbSex_cli;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblCli;
    private javax.swing.JFormattedTextField txtCpf_cli;
    private javax.swing.JTextField txtEnd_cli;
    private javax.swing.JFormattedTextField txtFon_cli;
    public static javax.swing.JTextField txtId_cli;
    private javax.swing.JTextField txtNom_cli;
    public static javax.swing.JTextField txtPes_cli;
    // End of variables declaration//GEN-END:variables
}
