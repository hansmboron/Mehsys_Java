package view;

import Utils.ConexaoSQLITE;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author hans
 */
public class Tela_Ser extends javax.swing.JInternalFrame {

    ConexaoSQLITE conexao = new ConexaoSQLITE();
    PreparedStatement pst = null;
    ResultSet rs = null;

    public Tela_Ser() {
        initComponents();
        conexao.conectar();
        this.popula_cbb();
        pesquisar_servico();
    }
    
    // preenche combobox com profissionais cadastrados no banco de dados
    public final void popula_cbb() {
        cbbPro_ser.removeAllItems();
        cbbPro_ser.addItem("Selecionar");
        String sql = "select usuario from tbusuarios order by usuario";
        try {
            pst = conexao.criarPreparedStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                cbbPro_ser.addItem(rs.getString("usuario"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void limpar() {
        txtId_ser.setText(null);
        txtNom_ser.setText(null);
        cbbPro_ser.setSelectedIndex(0);
        txtVal_ser.setText("000000");
        txtDur_ser.setText("000");

        btnNov_ser.setEnabled(true);
        this.popula_cbb();
    }

    // metodo para adicionar novo serviço
    public void adicionar() {
        String sql = "insert into tbservicos(nome, usuario, "
                + "valor, duracao) values(?,?,?,?)";
        try {
            pst = conexao.criarPreparedStatement(sql);
            pst.setString(1, txtNom_ser.getText());
            pst.setString(2, cbbPro_ser.getSelectedItem().toString());
            pst.setString(3, txtVal_ser.getText());
            pst.setString(4, txtDur_ser.getText());

            // validação dos campos obrigatorios
            if (txtNom_ser.getText().trim().length() < 3 || txtVal_ser.getText().trim().length() < 10 ||!txtVal_ser.getText().contains("1")
                    && !txtVal_ser.getText().contains("2") && !txtVal_ser.getText().contains("3")
                    && !txtVal_ser.getText().contains("4") && !txtVal_ser.getText().contains("5")
                    && !txtVal_ser.getText().contains("6") && !txtVal_ser.getText().contains("7")
                    && !txtVal_ser.getText().contains("8") && !txtVal_ser.getText().contains("9")) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
                txtVal_ser.setText("000000");
            } else {

                // a linha abaixo atualiza a tabela serviços com os dados do formu
                // a estrutura abaixo é usada para confirmar a insersão de dados
                int adicionado = pst.executeUpdate();  // retorna 1 ou 0
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Serviço adicionado com sucesso!");
                    this.limpar();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // metodo para alterar dados do serviço
    private void alterar() {
        String sql = "update tbservicos set nome=?, usuario=?, valor=?, duracao=? where id=?";
        try {
            pst = conexao.criarPreparedStatement(sql);
            pst.setString(1, txtNom_ser.getText());
            pst.setString(2, cbbPro_ser.getSelectedItem().toString());
            pst.setString(3, txtVal_ser.getText());
            pst.setString(4, txtDur_ser.getText());
            pst.setString(5, txtId_ser.getText());

            // validação dos campos obrigatorios
            if (txtNom_ser.getText().trim().length() < 3 || txtVal_ser.getText().trim().length() < 10 || !txtVal_ser.getText().contains("1")
                    && !txtVal_ser.getText().contains("2") && !txtVal_ser.getText().contains("3")
                    && !txtVal_ser.getText().contains("4") && !txtVal_ser.getText().contains("5")
                    && !txtVal_ser.getText().contains("6") && !txtVal_ser.getText().contains("7")
                    && !txtVal_ser.getText().contains("8") && !txtVal_ser.getText().contains("9")) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
                txtVal_ser.setText("000000");
            } else {

                // a linha abaixo atualiza a tabela servicos com os dados do formu
                // a estrutura abaixo é usada para confirmar a alteração de dados
                int adicionado = pst.executeUpdate();  // retorna 1 ou 0
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do serviço alterados com sucesso!");
                    this.limpar();
                    txtId_ser.setText(null);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método para consultar serviços no bd
    private void pesquisar_servico() {
        String sql = "select id as ID, nome as Nome, usuario as Profissional, valor as Valor, duracao as Duração from tbservicos where nome like ?";
        try {
            pst = conexao.criarPreparedStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            // atençao ao % que é a continuação da string sql
            pst.setString(1, txtPes_ser.getText() + "%");
            rs = pst.executeQuery();
            // a linha abaixo usa a biblioteca rs2xml.jar
            tblSer.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // metodo para setar campos do formulario com dados da tabela
    private void setar_campos() {
        int setar = tblSer.getSelectedRow();
        txtId_ser.setText(tblSer.getModel().getValueAt(setar, 0).toString());
        txtNom_ser.setText(tblSer.getModel().getValueAt(setar, 1).toString());
        // caso esteja vazio no banco de dados ja que estes campos não são obrigatorios
        cbbPro_ser.setSelectedItem(tblSer.getModel().getValueAt(setar, 2));
        txtVal_ser.setText(tblSer.getModel().getValueAt(setar, 3).toString());
        txtDur_ser.setText(tblSer.getModel().getValueAt(setar, 4).toString());

        // a linha abaixo desabilita btn adicionar
        btnNov_ser.setEnabled(false);
    }

    // metodo para remover serviço do banco de dados
    private void remover() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este serviço?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbservicos where id=?";
            try {
                pst = conexao.criarPreparedStatement(sql);
                pst.setString(1, txtId_ser.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Serviço removido com sucesso!");
                    limpar();
                    txtId_ser.setText(null);
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
        txtId_ser = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNom_ser = new javax.swing.JTextField();
        btnNov_ser = new javax.swing.JButton();
        btnEdi_ser = new javax.swing.JButton();
        btnDel_ser = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnLim = new javax.swing.JButton();
        cbbPro_ser = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtVal_ser = new javax.swing.JFormattedTextField();
        txtDur_ser = new javax.swing.JFormattedTextField();
        btnPes_ser = new javax.swing.JButton();
        txtPes_ser = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSer = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Tela Serviços");
        setPreferredSize(new java.awt.Dimension(670, 568));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameActivated(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cadastro de serviços"));

        txtId_ser.setToolTipText("Número do serviço");
        txtId_ser.setEnabled(false);

        jLabel1.setText("Id:*");

        jLabel2.setText("Nome:*");

        jLabel4.setText("Profissional:");

        jLabel5.setText("Valor:*");

        txtNom_ser.setToolTipText("Nome do serviço");
        txtNom_ser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNom_serActionPerformed(evt);
            }
        });

        btnNov_ser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nov_blue.png"))); // NOI18N
        btnNov_ser.setText("Salvar");
        btnNov_ser.setToolTipText("Cadastrar novo serviço no sistema");
        btnNov_ser.setPreferredSize(new java.awt.Dimension(100, 33));
        btnNov_ser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNov_serActionPerformed(evt);
            }
        });

        btnEdi_ser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edi_blue.png"))); // NOI18N
        btnEdi_ser.setText("Atualizar");
        btnEdi_ser.setToolTipText("Atualizar serviço selecionado");
        btnEdi_ser.setPreferredSize(new java.awt.Dimension(100, 33));
        btnEdi_ser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEdi_serActionPerformed(evt);
            }
        });

        btnDel_ser.setForeground(java.awt.Color.red);
        btnDel_ser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/del_blue.png"))); // NOI18N
        btnDel_ser.setText("Excluir");
        btnDel_ser.setToolTipText("Excluir serviço selecionado");
        btnDel_ser.setPreferredSize(new java.awt.Dimension(100, 33));
        btnDel_ser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDel_serActionPerformed(evt);
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

        cbbPro_ser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        cbbPro_ser.setToolTipText("Profissional responsável por este serviço");
        cbbPro_ser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbbPro_serMouseClicked(evt);
            }
        });
        cbbPro_ser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbPro_serActionPerformed(evt);
            }
        });

        jLabel3.setText("Duração:");

        try {
            txtVal_ser.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("R$#.###,##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtVal_ser.setText("000000");
        txtVal_ser.setToolTipText("Valor a ser cobrado pelo serviço");

        try {
            txtDur_ser.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#:##min")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDur_ser.setToolTipText("Duração do serviço");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtId_ser, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNom_ser, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbbPro_ser, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtVal_ser)
                                    .addComponent(txtDur_ser))))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 86, Short.MAX_VALUE)
                .addComponent(btnNov_ser, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEdi_ser, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDel_ser, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLim)
                .addGap(68, 68, 68))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtId_ser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtNom_ser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(txtVal_ser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cbbPro_ser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtDur_ser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNov_ser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdi_ser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDel_ser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLim))
                .addContainerGap())
        );

        btnPes_ser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pes_blue.png"))); // NOI18N
        btnPes_ser.setText("Pesquisar");
        btnPes_ser.setToolTipText("Pesquisar serviços por nome");
        btnPes_ser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPes_serActionPerformed(evt);
            }
        });

        txtPes_ser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPes_serKeyReleased(evt);
            }
        });

        tblSer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nome", "Profissional", "Valor", "Duração"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSerMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSer);
        if (tblSer.getColumnModel().getColumnCount() > 0) {
            tblSer.getColumnModel().getColumn(0).setResizable(false);
            tblSer.getColumnModel().getColumn(0).setPreferredWidth(20);
        }

        jLabel8.setText("Nome:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPes_ser, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPes_ser, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtPes_ser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPes_ser))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addContainerGap())
        );

        setBounds(0, 0, 670, 568);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPes_serKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPes_serKeyReleased
        pesquisar_servico();
    }//GEN-LAST:event_txtPes_serKeyReleased

    private void btnPes_serActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPes_serActionPerformed
        pesquisar_servico();
    }//GEN-LAST:event_btnPes_serActionPerformed

    private void tblSerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSerMouseClicked
        this.setar_campos();
    }//GEN-LAST:event_tblSerMouseClicked

    private void btnLimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimActionPerformed

    private void btnDel_serActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDel_serActionPerformed
        remover();
        pesquisar_servico();
    }//GEN-LAST:event_btnDel_serActionPerformed

    private void btnEdi_serActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEdi_serActionPerformed
        alterar();
        pesquisar_servico();
    }//GEN-LAST:event_btnEdi_serActionPerformed

    private void btnNov_serActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNov_serActionPerformed
        adicionar();
        pesquisar_servico();
    }//GEN-LAST:event_btnNov_serActionPerformed

    private void txtNom_serActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNom_serActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNom_serActionPerformed

    private void cbbPro_serActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbPro_serActionPerformed
        
    }//GEN-LAST:event_cbbPro_serActionPerformed

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        txtPes_ser.requestFocusInWindow();
    }//GEN-LAST:event_formInternalFrameActivated

    private void cbbPro_serMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbbPro_serMouseClicked

    }//GEN-LAST:event_cbbPro_serMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDel_ser;
    private javax.swing.JButton btnEdi_ser;
    private javax.swing.JButton btnLim;
    private javax.swing.JButton btnNov_ser;
    private javax.swing.JButton btnPes_ser;
    private javax.swing.JComboBox<Object> cbbPro_ser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblSer;
    private javax.swing.JFormattedTextField txtDur_ser;
    public static javax.swing.JTextField txtId_ser;
    private javax.swing.JTextField txtNom_ser;
    public static javax.swing.JTextField txtPes_ser;
    private javax.swing.JFormattedTextField txtVal_ser;
    // End of variables declaration//GEN-END:variables
}
