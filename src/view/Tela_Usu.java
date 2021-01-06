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
public class Tela_Usu extends javax.swing.JInternalFrame {

    ConexaoSQLITE conexao = new ConexaoSQLITE();
    PreparedStatement pst = null;
    ResultSet rs = null;

    public Tela_Usu() {
        initComponents();
        conexao.conectar();
        pesquisar_usuario();
        txtIduser_sys.setVisible(false);
    }

    private void limpar() {
        txtId_usu.setText(null);
        txtNom_usu.setText(null);
        txtFon_usu.setText(null);
        txtLog_usu.setText(null);
        txtSen_usu.setText(null);
        cbbPer_usu.setSelectedIndex(0);
        cbbHor_in.setSelectedIndex(0);
        cbbHor_out.setSelectedIndex(0);

        btnNov_usu.setEnabled(true);
    }

    // metodo para adicionar novo usuario
    public void adicionar() {
        String sql = "insert into tbusuarios(usuario, fone, login, "
                + "senha, perfil, hora_in, hora_out) values(?,?,?,?,?,?,?)";
        try {
            pst = conexao.criarPreparedStatement(sql);
            pst.setString(1, txtNom_usu.getText());
            pst.setString(2, txtFon_usu.getText());
            pst.setString(3, txtLog_usu.getText());
            pst.setString(4, txtSen_usu.getText());
            pst.setString(5, cbbPer_usu.getSelectedItem().toString());
            pst.setString(6, cbbHor_in.getSelectedItem().toString());
            pst.setString(7, cbbHor_out.getSelectedItem().toString());

            // validação dos campos obrigatorios
            if (txtNom_usu.getText().isEmpty() || txtFon_usu.getText().trim().length() < 14) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            } else if (txtLog_usu.getText().trim().length() < 4 || txtSen_usu.getText().trim().length() < 4) {
                JOptionPane.showMessageDialog(null, "Usuário e senha precisam ter pelo menos 4 caracteres!");
            } else {

                // a linha abaixo atualiza a tabela usuarios com os dados do formu
                // a estrutura abaixo é usada para confirmar a insersão de dados
                int adicionado = pst.executeUpdate();  // retorna 1 ou 0
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário adicionado com sucesso!");
                    this.limpar();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // metodo para alterar dados do usuário
    private void alterar() {
        String sql = "update tbusuarios set usuario=?, fone=?, login=?, senha=?, perfil=?, hora_in=?, hora_out=? where id=?";
        try {
            pst = conexao.criarPreparedStatement(sql);
            pst.setString(1, txtNom_usu.getText());
            pst.setString(2, txtFon_usu.getText());
            pst.setString(3, txtLog_usu.getText());
            pst.setString(4, txtSen_usu.getText());
            pst.setString(5, cbbPer_usu.getSelectedItem().toString());
            pst.setString(6, cbbHor_in.getSelectedItem().toString());
            pst.setString(7, cbbHor_out.getSelectedItem().toString());
            pst.setString(8, txtId_usu.getText());

            // validação dos campos obrigatorios
            if (txtNom_usu.getText().isEmpty() || txtFon_usu.getText().trim().length() < 14) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            } else if (txtLog_usu.getText().trim().length() < 4 || txtSen_usu.getText().trim().length() < 4) {
                JOptionPane.showMessageDialog(null, "Usuário e senha precisam ter pelo menos 4 caracteres!");
            } else {

                // a linha abaixo atualiza a tabela usuarios com os dados do formu
                // a estrutura abaixo é usada para confirmar a alteração de dados
                int adicionado = pst.executeUpdate();  // retorna 1 ou 0
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do usuário alterados com sucesso!");
                    this.limpar();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método para consultar usuarios no bd
    private void pesquisar_usuario() {
        String sql = "select id as ID,usuario as Nome,fone as Fone,login as Login,senha as Senha,perfil as Perfil,hora_in as Entrada,hora_out as Saída from tbusuarios where usuario like ?";
        try {
            pst = conexao.criarPreparedStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            // atençao ao % que é a continuação da string sql
            pst.setString(1, txtPes_usu.getText() + "%");
            rs = pst.executeQuery();
            // a linha abaixo usa a biblioteca rs2xml.jar
            tblUsu.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // metodo para setar campos do formulario com dados da tabela
    private void setar_campos() {
        int setar = tblUsu.getSelectedRow();
        txtId_usu.setText(tblUsu.getModel().getValueAt(setar, 0).toString());
        txtNom_usu.setText(tblUsu.getModel().getValueAt(setar, 1).toString());
        txtFon_usu.setText(tblUsu.getModel().getValueAt(setar, 2).toString());
        txtLog_usu.setText(tblUsu.getModel().getValueAt(setar, 3).toString());
        txtSen_usu.setText(tblUsu.getModel().getValueAt(setar, 4).toString());
        cbbPer_usu.setSelectedItem(tblUsu.getModel().getValueAt(setar, 5));
        // caso esteja vazio no banco de dados ja que estes campos não são obrigatorios
        try {
            cbbHor_in.setSelectedItem(tblUsu.getModel().getValueAt(setar, 6));
            cbbHor_out.setSelectedItem(tblUsu.getModel().getValueAt(setar, 7));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        // a linha abaixo desabilita btn adicionar
        btnNov_usu.setEnabled(false);
    }

    // metodo para remover usuário do banco de dados
    private void remover() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este usuário?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION && !txtId_usu.getText().equals(txtIduser_sys.getText())) {
            String sql = "delete from tbusuarios where id=?";
            try {
                pst = conexao.criarPreparedStatement(sql);
                pst.setString(1, txtId_usu.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário removido com sucesso!");
                    limpar();
                    txtId_usu.setText(null);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else if (txtId_usu.getText().equals(txtIduser_sys.getText())) {
            JOptionPane.showMessageDialog(null, "Você não pode remover a sí próprio!");
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
        txtId_usu = new javax.swing.JTextField();
        txtSen_usu = new javax.swing.JPasswordField();
        cbbPer_usu = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNom_usu = new javax.swing.JTextField();
        txtLog_usu = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnNov_usu = new javax.swing.JButton();
        btnEdi_usu = new javax.swing.JButton();
        btnDel_usu = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnLim = new javax.swing.JButton();
        txtFon_usu = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cbbHor_in = new javax.swing.JComboBox<>();
        cbbHor_out = new javax.swing.JComboBox<>();
        btnPes_usu = new javax.swing.JButton();
        txtPes_usu = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsu = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        txtIduser_sys = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Tela Usuários");
        setPreferredSize(new java.awt.Dimension(670, 568));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cadastro de novo usuário"));

        txtId_usu.setToolTipText("Número do usuário");
        txtId_usu.setEnabled(false);

        txtSen_usu.setToolTipText("senha para entrar no sistema");

        cbbPer_usu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "user", "admin" }));
        cbbPer_usu.setToolTipText("Perfil do usuário");

        jLabel1.setText("Id:*");

        jLabel2.setText("Nome:*");

        jLabel3.setText("Login:*");

        jLabel4.setText("Senha:*");

        jLabel5.setText("Perfil:*");

        txtNom_usu.setToolTipText("Nome do usuário");

        txtLog_usu.setToolTipText("login para entrar no sistema");

        jLabel6.setText("Fone:*");

        btnNov_usu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nov_blue.png"))); // NOI18N
        btnNov_usu.setText("Salvar");
        btnNov_usu.setToolTipText("Cadastrar novo usuário do sistema");
        btnNov_usu.setPreferredSize(new java.awt.Dimension(100, 33));
        btnNov_usu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNov_usuActionPerformed(evt);
            }
        });

        btnEdi_usu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edi_blue.png"))); // NOI18N
        btnEdi_usu.setText("Atualizar");
        btnEdi_usu.setToolTipText("Atualizar usuário selecionado");
        btnEdi_usu.setPreferredSize(new java.awt.Dimension(100, 33));
        btnEdi_usu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEdi_usuActionPerformed(evt);
            }
        });

        btnDel_usu.setForeground(java.awt.Color.red);
        btnDel_usu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/del_blue.png"))); // NOI18N
        btnDel_usu.setText("Excluir");
        btnDel_usu.setToolTipText("Excluir usuário selecionado");
        btnDel_usu.setPreferredSize(new java.awt.Dimension(100, 33));
        btnDel_usu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDel_usuActionPerformed(evt);
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

        try {
            txtFon_usu.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtFon_usu.setToolTipText("Telefone do usuário");

        jLabel9.setText("Hora começo:");

        jLabel10.setText("Hora termino:");

        cbbHor_in.setMaximumRowCount(16);
        cbbHor_in.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30" }));

        cbbHor_out.setMaximumRowCount(16);
        cbbHor_out.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel7))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtId_usu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNom_usu, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                                    .addComponent(txtFon_usu))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel5))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cbbPer_usu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtSen_usu, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtLog_usu, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbbHor_out, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbbHor_in, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(71, 71, 71)
                                .addComponent(btnNov_usu, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEdi_usu, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnDel_usu, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnLim)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
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
                            .addComponent(cbbPer_usu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(txtNom_usu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(txtLog_usu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtSen_usu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtId_usu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtFon_usu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(cbbHor_in, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbHor_out, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNov_usu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdi_usu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDel_usu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLim))
                .addGap(22, 22, 22))
        );

        btnPes_usu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pes_blue.png"))); // NOI18N
        btnPes_usu.setText("Pesquisar");
        btnPes_usu.setToolTipText("Pesquisar usuários por nome");
        btnPes_usu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPes_usuActionPerformed(evt);
            }
        });

        txtPes_usu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPes_usuKeyReleased(evt);
            }
        });

        tblUsu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nome", "Fone", "Login", "Senha", "Perfil", "Entrada", "Saída"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblUsu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUsu);
        if (tblUsu.getColumnModel().getColumnCount() > 0) {
            tblUsu.getColumnModel().getColumn(0).setResizable(false);
            tblUsu.getColumnModel().getColumn(0).setPreferredWidth(20);
        }

        jLabel8.setText("Nome:");

        txtIduser_sys.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPes_usu, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPes_usu, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(txtIduser_sys)
                .addGap(0, 79, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtPes_usu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPes_usu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIduser_sys)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .addContainerGap())
        );

        setBounds(0, 0, 670, 568);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNov_usuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNov_usuActionPerformed
        adicionar();
        pesquisar_usuario();
    }//GEN-LAST:event_btnNov_usuActionPerformed

    private void btnEdi_usuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEdi_usuActionPerformed
        alterar();
        pesquisar_usuario();
    }//GEN-LAST:event_btnEdi_usuActionPerformed

    private void btnDel_usuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDel_usuActionPerformed
        remover();
        pesquisar_usuario();
    }//GEN-LAST:event_btnDel_usuActionPerformed

    private void txtPes_usuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPes_usuKeyReleased
        pesquisar_usuario();
    }//GEN-LAST:event_txtPes_usuKeyReleased

    private void btnPes_usuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPes_usuActionPerformed
        pesquisar_usuario();
    }//GEN-LAST:event_btnPes_usuActionPerformed

    private void tblUsuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsuMouseClicked
        setar_campos();
    }//GEN-LAST:event_tblUsuMouseClicked

    private void btnLimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDel_usu;
    private javax.swing.JButton btnEdi_usu;
    private javax.swing.JButton btnLim;
    private javax.swing.JButton btnNov_usu;
    private javax.swing.JButton btnPes_usu;
    private javax.swing.JComboBox<String> cbbHor_in;
    private javax.swing.JComboBox<String> cbbHor_out;
    private javax.swing.JComboBox<String> cbbPer_usu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUsu;
    private javax.swing.JFormattedTextField txtFon_usu;
    public static javax.swing.JTextField txtId_usu;
    public static javax.swing.JLabel txtIduser_sys;
    private javax.swing.JTextField txtLog_usu;
    private javax.swing.JTextField txtNom_usu;
    public static javax.swing.JTextField txtPes_usu;
    private javax.swing.JPasswordField txtSen_usu;
    // End of variables declaration//GEN-END:variables
}
