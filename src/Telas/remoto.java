package Telas;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sounds.Sounds;

/**
 *
 * @author denyl
 */
public class remoto extends javax.swing.JInternalFrame {

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/darcksoftware";
    private final String DATABASE_USER = "denny";
    private final String DATABASE_PASSWORD = "123456789";

    /**
     * Creates new form remoto
     */
    public remoto() {
        initComponents();
        carregarDadosNaTabela();
        String nomeJFrame = carregarNomeSalvoDoBancoDeDados();
        ImageIcon icone = carregarIconeSalvoDoBancoDeDados();

        // Definir título e ícone do JFrame
        definirTituloDoJFrame(nomeJFrame);
        if (icone != null) {
            setFrameIcon(icone); // Adiciona um ícone à barra de título do JInternalFrame
        }
    }

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

    private void carregarDadosNaTabela() {
        // Cria um modelo de tabela personalizado para desativar a edição
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Retorna false para desativar a edição de todas as células
                return false;
            }
        };

        // Adiciona as colunas necessárias ao modelo de tabela
        model.addColumn("ID Externo");
        model.addColumn("Nome do Dono");
        model.addColumn("Computador");
        model.addColumn("Telefone");
        model.addColumn("Endereço");

        String url = "jdbc:mysql://localhost/DarckSoftware?user=denny&password=123456789&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
        String query = "SELECT * FROM externo";

        try (Connection conn = DriverManager.getConnection(url); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String compu = rs.getString("computador");
                String tel = rs.getString("telefone");
                String ende = rs.getString("endereco");

                // Adiciona os dados ao modelo da tabela
                model.addRow(new Object[]{id, nome, compu, tel, ende});
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        // Define o modelo de tabela na JTable
        tabel.setModel(model);
    }

    private void preencherJListComProblema() {
        int selectedRow = tabel.getSelectedRow();
        if (selectedRow != -1) {
            String idExterno = tabel.getValueAt(selectedRow, 0).toString();
            carregarProblemasNaJList(idExterno);
        }
    }

// Método para carregar os problemas associados ao ID Externo selecionado
    private void carregarProblemasNaJList(String idExterno) {
        DefaultListModel<String> model = new DefaultListModel<>();
        String query = "SELECT problema FROM externo WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, idExterno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String problema = rs.getString("problema");
                model.addElement(problema);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar problemas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        problem.setModel(model);
        problem.setCellRenderer(new LineWrapListCellRenderer());  // Define o renderer personalizado
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        transferido = new javax.swing.JCheckBox();
        recebido = new javax.swing.JCheckBox();
        apagar = new javax.swing.JButton();
        sistem = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        problem = new javax.swing.JList<>();

        setClosable(true);

        jPanel1.setBackground(new java.awt.Color(0, 204, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102), 2));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));

        jPanel2.setBackground(new java.awt.Color(0, 204, 102));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));

        jLabel1.setFont(new java.awt.Font("Wide Latin", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Resultados atendimento remoto");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true));

        tabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelMouseClicked(evt);
            }
        });
        tabel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabelKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tabel);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 4, true));

        buttonGroup1.add(transferido);
        transferido.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        transferido.setForeground(new java.awt.Color(255, 255, 255));
        transferido.setText("Transferido");
        transferido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                transferidoMouseClicked(evt);
            }
        });
        transferido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transferidoActionPerformed(evt);
            }
        });

        buttonGroup1.add(recebido);
        recebido.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        recebido.setForeground(new java.awt.Color(255, 255, 255));
        recebido.setText("Recebido");
        recebido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                recebidoMouseClicked(evt);
            }
        });

        apagar.setBackground(new java.awt.Color(153, 153, 153));
        apagar.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        apagar.setForeground(new java.awt.Color(51, 51, 51));
        apagar.setText("Atualizar");
        apagar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                apagarMouseClicked(evt);
            }
        });
        apagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apagarActionPerformed(evt);
            }
        });

        sistem.setBackground(new java.awt.Color(153, 153, 153));
        sistem.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        sistem.setForeground(new java.awt.Color(51, 51, 51));
        sistem.setText("Inserir no sistema");
        sistem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sistemMouseClicked(evt);
            }
        });
        sistem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sistemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(apagar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(transferido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(recebido, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(45, 45, 45))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(sistem, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(transferido)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(recebido)
                .addGap(18, 18, 18)
                .addComponent(apagar, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sistem, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        problem.setBackground(new java.awt.Color(51, 51, 51));
        problem.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        problem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        problem.setForeground(new java.awt.Color(255, 255, 255));
        problem.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { " " };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        problem.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        problem.setValueIsAdjusting(true);
        jScrollPane2.setViewportView(problem);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jScrollPane2)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sistemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sistemActionPerformed
        Orcamento orca = new Orcamento();
        orca.setVisible(true);
    }//GEN-LAST:event_sistemActionPerformed

    private void tabelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelKeyPressed

    private void tabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelMouseClicked

        preencherJListComProblema();

    }//GEN-LAST:event_tabelMouseClicked

    private void apagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apagarActionPerformed
        // Verifica se a tabela tem uma linha selecionada
        int selectedRow = tabel.getSelectedRow();
        if (selectedRow != -1) {
            // Obtém o ID da linha selecionada
            int idExterno = (int) tabel.getValueAt(selectedRow, 0);

            // Verifica o estado das JCheckBoxes
            if (transferido.isSelected()) {
                // Se a JCheckBox "transferido" estiver selecionada, exclui a linha da tabela
                excluirLinhaDoBancoDeDados(idExterno);
            } else if (recebido.isSelected()) {
                // Se a JCheckBox "recebido" estiver selecionada, mostra uma mensagem
                JOptionPane.showMessageDialog(this, "Deve ser transferido primeiro", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                // Se nenhuma JCheckBox estiver selecionada, mostra uma mensagem de erro
                JOptionPane.showMessageDialog(this, "Selecione uma opção para apagar", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Se nenhuma linha estiver selecionada, mostra uma mensagem de erro
            JOptionPane.showMessageDialog(this, "Selecione uma linha da tabela", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_apagarActionPerformed

    private void transferidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transferidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_transferidoActionPerformed

    Sounds sound = new Sounds();

    private void transferidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transferidoMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_transferidoMouseClicked

    private void recebidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recebidoMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_recebidoMouseClicked

    private void apagarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_apagarMouseClicked
        sound.tocarSom("src/sounds/upgrade.wav");
    }//GEN-LAST:event_apagarMouseClicked

    private void sistemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sistemMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_sistemMouseClicked

    // Método para excluir a linha da tabela no banco de dados
    private void excluirLinhaDoBancoDeDados(int idExterno) {
        String query = "DELETE FROM externo WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idExterno);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Atualiza a tabela após a exclusão
                carregarDadosNaTabela();
                JOptionPane.showMessageDialog(this, "Linha excluída com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma linha foi excluída", "Aviso", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir a linha: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton apagar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> problem;
    private javax.swing.JCheckBox recebido;
    private javax.swing.JButton sistem;
    private javax.swing.JTable tabel;
    private javax.swing.JCheckBox transferido;
    // End of variables declaration//GEN-END:variables
}
