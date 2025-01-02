package Telas;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import sounds.Sounds;

/**
 *
 * @author denyl
 */
public class servico extends javax.swing.JFrame {

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/darcksoftware";
    private final String DATABASE_USER = "denny";
    private final String DATABASE_PASSWORD = "123456789";
    private Connection connection; // Adicione essa linha para declarar a conexão
    private PreparedStatement preparedStatement; // Adicione essa linha para declarar o preparedStatement
    private ResultSet resultSet; // Adicione essa linha para declarar o resultSet

    /**
     * Creates new form serviço
     */
    public servico() {
        setUndecorated(true);
        initComponents();

        formatarCampoPreco();
        setLocationRelativeTo(null);
        carregarServicosNaTabela();
        carregarServicos();
        // Criando um modelo de tabela customizado
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Todas as células não são editáveis
                return false;
            }
        };

// Definindo o modelo na JTable
        tabelaEdit.setModel(model);
        carregarServicosNaTabela();
        String nomeJFrame = carregarNomeSalvoDoBancoDeDados();
        ImageIcon icone = carregarIconeSalvoDoBancoDeDados();

        // Definir título e ícone do JFrame
        definirTituloDoJFrame(nomeJFrame);
        if (icone != null) {
            setIconImage(icone.getImage());
        }
    }

    // Método para carregar o nome do aplicativo do banco de dados
    private String carregarNomeSalvoDoBancoDeDados() {
        String nome = "Nome Padrão"; // Nome padrão caso não consiga carregar
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT nome FROM empresa WHERE id = 1")) {

            if (rs.next()) {
                nome = rs.getString("nome");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar o nome do banco de dados: " + e.getMessage());
        }
        return nome;
    }

    // Método para carregar o ícone do aplicativo do banco de dados
    private ImageIcon carregarIconeSalvoDoBancoDeDados() {
        ImageIcon icone = null;
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT imagem FROM empresa WHERE id = 1")) {

            if (rs.next()) {
                byte[] imagemBytes = rs.getBytes("imagem");
                if (imagemBytes != null) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(imagemBytes);
                    BufferedImage bufferedImage = ImageIO.read(bais);
                    icone = new ImageIcon(bufferedImage);
                }
            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar o ícone do banco de dados: " + e.getMessage());
        }
        return icone;
    }

    // Método para definir o título do JFrame
    private void definirTituloDoJFrame(String nome) {
        setTitle(nome);
    }

    private void carregarServicos() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        crregarServiços.setModel(listModel);

        try {
            try ( // Conectar ao banco de dados
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789")) {
                String sql = "SELECT nome FROM servicos";
                try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                    // Carregar os nomes dos serviços no JList
                    while (rs.next()) {
                        String nomeServico = rs.getString("nome");
                        listModel.addElement(nomeServico);
                    }

                }
            }
        } catch (SQLException e) {
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

        jPanel2 = new javax.swing.JPanel();
        paineis = new javax.swing.JTabbedPane();
        inserir = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        servico = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        preco = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        editar = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaEdit = new javax.swing.JTable();
        noma = new javax.swing.JTextField();
        preço = new javax.swing.JTextField();
        excluir = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        crregarServiços = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        save = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        delet = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        jPanel2.setBackground(new java.awt.Color(0, 153, 51));

        paineis.setBackground(new java.awt.Color(51, 51, 51));
        paineis.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true));
        paineis.setForeground(new java.awt.Color(255, 255, 255));
        paineis.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        paineis.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        paineis.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        paineis.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                paineisStateChanged(evt);
            }
        });
        paineis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paineisMouseClicked(evt);
            }
        });

        inserir.setBackground(new java.awt.Color(51, 51, 51));
        inserir.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("SERVIÇO:");

        servico.setFont(new java.awt.Font("Century", 0, 14)); // NOI18N
        servico.setToolTipText("SERVIÇO A SER PRESTADO");

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Agency FB", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ADICIONAR SERVIÇO");

        preco.setToolTipText("INSIRA O VALOR");
        preco.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 14)); // NOI18N

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("VALOR:");

        javax.swing.GroupLayout inserirLayout = new javax.swing.GroupLayout(inserir);
        inserir.setLayout(inserirLayout);
        inserirLayout.setHorizontalGroup(
            inserirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inserirLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inserirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inserirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(servico)
                    .addGroup(inserirLayout.createSequentialGroup()
                        .addGroup(inserirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(preco, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 52, Short.MAX_VALUE)))
                .addContainerGap())
        );
        inserirLayout.setVerticalGroup(
            inserirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inserirLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inserirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(servico, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(inserirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(preco, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(72, 72, 72))
        );

        paineis.addTab("Inserir serviço", inserir);

        editar.setBackground(new java.awt.Color(51, 51, 51));

        tabelaEdit.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabelaEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaEditMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelaEdit);

        noma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout editarLayout = new javax.swing.GroupLayout(editar);
        editar.setLayout(editarLayout);
        editarLayout.setHorizontalGroup(
            editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
            .addGroup(editarLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(preço, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(noma, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        editarLayout.setVerticalGroup(
            editarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editarLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(noma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(preço, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        paineis.addTab("Editar serviço", editar);

        excluir.setBackground(new java.awt.Color(51, 51, 51));

        crregarServiços.setBackground(new java.awt.Color(0, 0, 0));
        crregarServiços.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        crregarServiços.setForeground(new java.awt.Color(255, 255, 255));
        crregarServiços.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                crregarServiçosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(crregarServiços);

        javax.swing.GroupLayout excluirLayout = new javax.swing.GroupLayout(excluir);
        excluir.setLayout(excluirLayout);
        excluirLayout.setHorizontalGroup(
            excluirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(excluirLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );
        excluirLayout.setVerticalGroup(
            excluirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(excluirLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        paineis.addTab("Excluir serviço", excluir);

        jLabel1.setFont(new java.awt.Font("Wide Latin", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Painel de serviços");
        jLabel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 3, true));

        jButton1.setBackground(new java.awt.Color(102, 102, 102));
        jButton1.setFont(new java.awt.Font("Agency FB", 1, 20)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Fechar");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        save.setBackground(new java.awt.Color(102, 102, 102));
        save.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        save.setForeground(new java.awt.Color(255, 255, 255));
        save.setText("Salvar");
        save.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        save.setEnabled(false);
        save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveMouseClicked(evt);
            }
        });
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        edit.setBackground(new java.awt.Color(102, 102, 102));
        edit.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        edit.setForeground(new java.awt.Color(255, 255, 255));
        edit.setText("Editar");
        edit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        edit.setEnabled(false);
        edit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editMouseClicked(evt);
            }
        });
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        delet.setBackground(new java.awt.Color(102, 102, 102));
        delet.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        delet.setForeground(new java.awt.Color(255, 255, 255));
        delet.setText("Apagar");
        delet.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        delet.setEnabled(false);
        delet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deletMouseClicked(evt);
            }
        });
        delet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paineis)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85)
                .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(delet, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paineis, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(save, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(delet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(edit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void paineisStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_paineisStateChanged
        int selectedIndex = paineis.getSelectedIndex();

        // Desativa todos os botões
        edit.setEnabled(false);
        delet.setEnabled(false);
        save.setEnabled(false);

        // Ativa os botões com base no painel selecionado
        switch (selectedIndex) {
            case 0 -> // Painel "Editar"
                save.setEnabled(true);
            case 1 -> // Painel "Excluir"
                edit.setEnabled(true);
            case 2 -> // Painel "Inserir"
                delet.setEnabled(true);
        }
    }//GEN-LAST:event_paineisStateChanged

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        // Verifica se uma linha está selecionada
        int selectedRow = tabelaEdit.getSelectedRow();
        if (selectedRow != -1) {
            // Obtém o ID da linha selecionada
            int id = (int) tabelaEdit.getValueAt(selectedRow, 0);

            // Obtém os novos valores dos JTextFields
            String novoNome = noma.getText();
            double novoPreco = Double.parseDouble(preço.getText());

            // Chama o método para atualizar o banco de dados
            atualizarServico(id, novoNome, novoPreco);

            // Atualiza a JTable com os novos valores
            tabelaEdit.setValueAt(novoNome, selectedRow, 1);
            tabelaEdit.setValueAt(novoPreco, selectedRow, 2);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma linha para editar.");
        }

    }//GEN-LAST:event_editActionPerformed

    private void nomaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomaActionPerformed

    private void tabelaEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaEditMouseClicked

        // Verifica se uma linha está selecionada
        int selectedRow = tabelaEdit.getSelectedRow();
        if (selectedRow != -1) {
            // Obtém os valores da linha selecionada
            String nome = (String) tabelaEdit.getValueAt(selectedRow, 1);
            double pre = (double) tabelaEdit.getValueAt(selectedRow, 2);

            // Define os valores nos JTextFields
            noma.setText(nome);
            preço.setText(String.valueOf(pre));
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_tabelaEditMouseClicked

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        salvarServico();
    }//GEN-LAST:event_saveActionPerformed

    private void deletActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletActionPerformed

        if (selectedServiceId != -1) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789")) {
                String deleteQuery = "DELETE FROM servicos WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(deleteQuery);
                stmt.setInt(1, selectedServiceId);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Serviço excluído com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir o serviço.");
                }

                // Atualizar a JList após a exclusão
                updateJList();

            } catch (SQLException e) {
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum serviço selecionado.");
        }

    }//GEN-LAST:event_deletActionPerformed

    private void updateJList() {
        // Método para atualizar a JList após a exclusão
        DefaultListModel<String> model = new DefaultListModel<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789")) {
            String query = "SELECT nome FROM servicos";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addElement(rs.getString("nome"));
            }
            crregarServiços.setModel(model);
        } catch (SQLException e) {
        }
    }

    private int selectedServiceId = -1;
    private void crregarServiçosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_crregarServiçosMouseClicked

        // Quando o usuário clica em um item da JList
        String selectedService = crregarServiços.getSelectedValue(); // Obter o servico selecionado
        if (selectedService != null) {
            // Supondo que o nome do servico é exibido na JList, você pode buscar o ID correspondente
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789")) {
                String query = "SELECT id FROM servicos WHERE nome = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, selectedService);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    selectedServiceId = rs.getInt("id");
                }
            } catch (SQLException e) {
            }
        }

    }//GEN-LAST:event_crregarServiçosMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Tocar o som primeiro
        sound.tocarSom("src/sounds/sair.wav");

        // Adicionar um atraso antes de fechar
        try {
            Thread.sleep(1000); // 1 segundo de atraso, ajuste conforme necessário
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    Sounds sound = new Sounds();

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        sound.tocarSom("src/sounds/sair.wav");
    }//GEN-LAST:event_jButton1MouseClicked

    private void paineisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paineisMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_paineisMouseClicked

    private void saveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_saveMouseClicked

    private void editMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_editMouseClicked

    private void deletMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deletMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_deletMouseClicked

    private void atualizarServico(int id, String nome, double preco) {
        try {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789")) {
                String sql = "UPDATE servicos SET nome = ?, preco = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, nome);
                    ps.setDouble(2, preco);
                    ps.setInt(3, id);

                    int rowsUpdated = ps.executeUpdate();

                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this, "Serviço atualizado com sucesso!");
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar o serviço: " + e.getMessage());
        }
    }

    private void formatarCampoPreco() {
        // Define o formato do número
        DecimalFormat formatoNumero = new DecimalFormat("#,##0.00");
        NumberFormatter formatter = new NumberFormatter(formatoNumero);
        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(0.0); // Define o valor mínimo como 0

        // Adiciona o NumberFormatter ao campo de texto
        preco.setFormatterFactory(new DefaultFormatterFactory(formatter));

        // Adiciona um listener de foco para formatar o texto ao perder o foco
        preco.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                formatarCampo();
            }
        });
    }

    private void formatarCampo() {
        String texto = preco.getText();
        texto = texto.replaceAll("[^0-9]", ""); // Remove todos os caracteres não numéricos
        if (!texto.isEmpty()) {
            double valor = Double.parseDouble(texto) / 100;
            DecimalFormat df = new DecimalFormat("#,##0.00");
            preco.setText(df.format(valor));
        }
    }

    private void salvarServico() {
        // Recupera os dados dos campos
        String nomeServico = servico.getText();

        // Recupera o valor do campo preco como Double
        Double precoServico = null;
        try {
            precoServico = Double.valueOf(preco.getText().replace(",", "."));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Insira um valor válido para o preço.");
            return;
        }

        System.out.println("Nome do Serviço: " + nomeServico);
        System.out.println("Preço do Serviço: " + precoServico);

        // Verifica se os campos estão preenchidos
        if (nomeServico.isEmpty() || precoServico == null || precoServico == 0) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos corretamente.");
            return;
        }

        // Insere o servico no banco de dados
        try {
            // Abrir a conexão
            String url = "jdbc:mysql://localhost/DarckSoftware?user=denny&password=123456789&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
            connection = DriverManager.getConnection(url);

            System.out.println("Conexão estabelecida com sucesso.");

            // Insere o servico no banco de dados
            String sql = "INSERT INTO servicos (nome, preco) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, nomeServico);
            stmt.setDouble(2, precoServico);
            stmt.executeUpdate();

            System.out.println("Serviço adicionado com sucesso.");

            // Fecha a conexão
            connection.close();

            JOptionPane.showMessageDialog(this, "Serviço adicionado com sucesso.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar serviço: " + ex.getMessage());
            // Imprime a exceção completa para depuração
        }
    }

    private void carregarServicosNaTabela() {
        // Obtém o modelo da tabela
        DefaultTableModel model = (DefaultTableModel) tabelaEdit.getModel();
        model.setRowCount(0); // Limpa as linhas existentes na tabela

        // Define as colunas da tabela
        model.setColumnIdentifiers(new Object[]{"ID", "Nome", "Preço"});

        try {
            // Query SQL para obter os dados da tabela servicos
            try ( // Conexão com o banco de dados
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789")) {
                // Query SQL para obter os dados da tabela servicos
                String sql = "SELECT id, nome, preco FROM servicos";
                try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                    // Itera sobre os resultados da consulta e adiciona-os ao modelo da tabela
                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getDouble("preco")
                        });
                    }
                    // Fecha as conexões

                }
            }
        } catch (SQLException e) {
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(servico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new servico().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> crregarServiços;
    private javax.swing.JButton delet;
    private javax.swing.JButton edit;
    private javax.swing.JPanel editar;
    private javax.swing.JPanel excluir;
    private javax.swing.JPanel inserir;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField noma;
    private javax.swing.JTabbedPane paineis;
    private javax.swing.JFormattedTextField preco;
    private javax.swing.JTextField preço;
    private javax.swing.JButton save;
    private javax.swing.JTextField servico;
    private javax.swing.JTable tabelaEdit;
    // End of variables declaration//GEN-END:variables
}
