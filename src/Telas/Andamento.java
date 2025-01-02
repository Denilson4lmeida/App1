/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Telas;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.swing.JOptionPane;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import sounds.Sounds;

public class Andamento extends javax.swing.JFrame {

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/darcksoftware";
    private final String DATABASE_USER = "denny";
    private final String DATABASE_PASSWORD = "123456789";

    public Andamento() {
        setUndecorated(true);
        initComponents();

        setLocationRelativeTo(null);
        // Define o modelo da tabela e desativa a edição
        DefaultTableModel modeloTabela = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nome Dono", "Telefone", "Data", "Computador", "Atendente", "Total"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desativa a edição das células
            }
        };
        aparecer_listado_como_adc.setModel(modeloTabela);
        carregarDados(); // Carregar os dados ao iniciar a tela

        // Preencher o JCheckBox fila automaticamente e ajustar o estado do botão Salvar
        fila.setSelected(true);
        save.setEnabled(!fila.isSelected()); // Desativa o botão salvar se fila estiver selecionado

        // Adiciona ActionListener aos JCheckBox para alterar o estado do botão Salvar
        fila.addActionListener((ActionEvent e) -> {
            atualizarEstadoBotaoSalvar();
        });

        consertando.addActionListener((ActionEvent e) -> {
            atualizarEstadoBotaoSalvar();
        });

        rejeitado.addActionListener((ActionEvent e) -> {
            atualizarEstadoBotaoSalvar();
        });
        String nomeJFrame = carregarNomeSalvoDoBancoDeDados();
        ImageIcon icone = carregarIconeSalvoDoBancoDeDados();

        // Definir título e ícone do JFrame
        definirTituloDoJFrame(nomeJFrame);
        if (icone != null) {
            setIconImage(icone.getImage());
        }

        aparecer_listado_como_adc.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Garante que o evento seja processado apenas uma vez
                int selectedRow = aparecer_listado_como_adc.getSelectedRow();
                if (selectedRow >= 0) {
                    // Obtém o ID do cliente da linha selecionada
                    int clienteId = (int) aparecer_listado_como_adc.getValueAt(selectedRow, 0); // Assume que o ID está na primeira coluna
                    carregarProdutosAssociados(clienteId); // Carrega os produtos associados
                }
            }
        });

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

    private void atualizarEstadoBotaoSalvar() {
        // O botão Salvar deve estar ativado se qualquer dos JCheckBox consertando ou rejeitado estiver selecionado
        save.setEnabled(!fila.isSelected() || consertando.isSelected() || rejeitado.isSelected());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        aparecer_listado_como_adc = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        fila = new javax.swing.JCheckBox();
        consertando = new javax.swing.JCheckBox();
        rejeitado = new javax.swing.JCheckBox();
        save = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        produtos_associados = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 51));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Wide Latin", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Em analise");

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 3, true));

        aparecer_listado_como_adc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(aparecer_listado_como_adc);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Painel de status", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        fila.setBackground(new java.awt.Color(51, 51, 51));
        buttonGroup1.add(fila);
        fila.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        fila.setForeground(new java.awt.Color(0, 0, 0));
        fila.setSelected(true);
        fila.setText("Na fila");
        fila.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 102), 2, true));
        fila.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filaMouseClicked(evt);
            }
        });

        consertando.setBackground(new java.awt.Color(51, 51, 51));
        buttonGroup1.add(consertando);
        consertando.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        consertando.setForeground(new java.awt.Color(0, 0, 0));
        consertando.setText("Para aprovação");
        consertando.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 102), 2, true));
        consertando.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                consertandoMouseClicked(evt);
            }
        });
        consertando.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consertandoActionPerformed(evt);
            }
        });

        rejeitado.setBackground(new java.awt.Color(51, 51, 51));
        buttonGroup1.add(rejeitado);
        rejeitado.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        rejeitado.setForeground(new java.awt.Color(0, 0, 0));
        rejeitado.setText("Rejeitado");
        rejeitado.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 102), 2, true));
        rejeitado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rejeitadoMouseClicked(evt);
            }
        });
        rejeitado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rejeitadoActionPerformed(evt);
            }
        });

        save.setBackground(new java.awt.Color(204, 204, 204));
        save.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        save.setForeground(new java.awt.Color(51, 51, 51));
        save.setText("Salvar");
        save.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fila)
                    .addComponent(consertando)
                    .addComponent(rejeitado))
                .addGap(18, 18, 18)
                .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(fila)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(consertando)
                        .addGap(18, 18, 18)
                        .addComponent(rejeitado)
                        .addContainerGap())
                    .addComponent(save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("fechar");
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
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

        produtos_associados.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jScrollPane2.setViewportView(produtos_associados);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(192, 192, 192))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    Sounds sound = new Sounds();

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed

        try {
            salvarDados();
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Erro ao processar a data. Verifique o formato.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(Andamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Tocar o som primeiro
        sound.tocarSom("src/sounds/sair.wav");

        // Adicionar um atraso antes de fechar
        try {
            Thread.sleep(1000); // 1 segundo de atraso, ajuste conforme necessário
        } catch (InterruptedException e) {
        }

        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void consertandoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_consertandoActionPerformed

    }//GEN-LAST:event_consertandoActionPerformed


    private void rejeitadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rejeitadoActionPerformed

    }//GEN-LAST:event_rejeitadoActionPerformed

    private void consertandoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_consertandoMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_consertandoMouseClicked

    private void rejeitadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rejeitadoMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_rejeitadoMouseClicked

    private void filaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_filaMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_filaMouseClicked

    private void saveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_saveMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        sound.tocarSom("src/sounds/sir.wav");
    }//GEN-LAST:event_jButton1MouseClicked

    private void salvarDados() throws ParseException, IOException {
        DefaultTableModel model = (DefaultTableModel) aparecer_listado_como_adc.getModel();
        int selectedRow = aparecer_listado_como_adc.getSelectedRow();

        if (selectedRow != -1) {
            int idNota = (Integer) model.getValueAt(selectedRow, 0);
            String nomeDono = (String) model.getValueAt(selectedRow, 1);
            String tele = (String) model.getValueAt(selectedRow, 2);
            String dataString = (String) model.getValueAt(selectedRow, 3);
            String computador = (String) model.getValueAt(selectedRow, 4);
            String atendente = (String) model.getValueAt(selectedRow, 5);
            double total = (Double) model.getValueAt(selectedRow, 6);
            String status = "";

            if (consertando.isSelected()) {
                status = "consertando";
            } else if (rejeitado.isSelected()) {
                status = "rejeitado";
            }

            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                // Converte a String para java.sql.Date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = sdf.parse(dataString);
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                // Conexão com o banco de dados
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789");

                // Verifica se o registro já existe
                String checkSql = "SELECT COUNT(*) FROM servicos_em_andamento WHERE id_nota = ?";
                stmt = conn.prepareStatement(checkSql);
                stmt.setInt(1, idNota);
                rs = stmt.executeQuery();
                rs.next();
                boolean exists = rs.getInt(1) > 0;

                if (exists) {
                    if (status.equals("rejeitado")) {
                        moverParaHistorico(idNota, nomeDono, tele, sqlDate, computador, atendente, total, status);

                    } else {
                        // Atualiza o registro existente
                        String updateSql = "UPDATE servicos_em_andamento SET nome_dono = ?,telefone = ?, data = ?, computador = ?, atendente = ?, total = ?, status = ? WHERE id_nota = ?";
                        stmt = conn.prepareStatement(updateSql);
                        stmt.setString(1, nomeDono);
                        stmt.setString(2, tele);
                        stmt.setDate(3, sqlDate);
                        stmt.setString(4, computador);
                        stmt.setString(5, atendente);
                        stmt.setDouble(6, total);
                        stmt.setString(7, status);
                        stmt.setInt(8, idNota);
                        stmt.executeUpdate();
                    }
                } else {
                    // Insere um novo registro
                    String insertSql = "INSERT INTO servicos_em_andamento (id_nota, nome_dono,telefone, data, computador, atendente, total, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    stmt = conn.prepareStatement(insertSql);
                    stmt.setInt(1, idNota);
                    stmt.setString(2, nomeDono);
                    stmt.setString(3, tele);
                    stmt.setDate(4, sqlDate); // Use sqlDate aqui
                    stmt.setString(5, computador);
                    stmt.setString(6, atendente);
                    stmt.setDouble(7, total);
                    stmt.setString(8, status);
                    stmt.executeUpdate();
                }
                // Se o status for "aguardando aprovação", gera o PDF
                if (status.equals("consertando")) {
                    gerarPDF(idNota);
                }

            } catch (SQLException e) {
                System.out.println("erro" + e);

            } finally {
                // Fechar ResultSet, PreparedStatement e Connection
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                }
            }
        }
    }

    private void moverParaHistorico(int idNota, String nomeDono, String telefone, java.sql.Date data, String computador, String atendente, double total, String status) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789")) {

            // Mover dados para a tabela `historico_servicos`
            String moveToHistoricoSql = """
       INSERT INTO historico_servicos (
                           id_nota, nome_dono, data, computador, atendente, total, status, produto_nome, preco, quantidade, total_produto, telefone
                       )
                       SELECT 
                           n.id AS id_nota, 
                           n.nome_dono, 
                           n.data, 
                           n.computador, 
                           n.atendente, 
                           n.total, 
                           'rejeitado', 
                           p.produto_nome, 
                           p.preco, 
                           IFNULL(CAST(p.quantidade AS SIGNED), 0) AS quantidade,  -- Garantir que a quantidade seja um inteiro, ou 0 se for NULL
                           (p.preco * IFNULL(CAST(p.quantidade AS SIGNED), 0)) AS total_produto,  -- Garantir multiplicação com a quantidade convertida
                           n.telefone
                       FROM 
                           notas n
                       INNER JOIN 
                           nota_produtos p 
                           ON n.nome_dono = p.nome_dono  -- Utilizando nome_dono para associar
                       WHERE 
                           n.id = ?;  -- Certifique-se de passar o parâmetro correto para filtrar a nota
        """;

            try (PreparedStatement moveStmt = conn.prepareStatement(moveToHistoricoSql)) {
                moveStmt.setInt(1, idNota);
                int rowsInserted = moveStmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Dados movidos para o histórico com sucesso.");
                }
            }

            // Deletar produtos associados da tabela `nota_produtos`
            String deleteProdutosSql = "DELETE FROM nota_produtos WHERE nome_dono = ?";  // Alterado para id_nota
            try (PreparedStatement deleteProdutosStmt = conn.prepareStatement(deleteProdutosSql)) {
                deleteProdutosStmt.setString(1, nomeDono);  // Altere para passar o nome do dono, não o idNota
                deleteProdutosStmt.executeUpdate();
            }

            // Deletar registro da tabela `servicos_em_andamento`
            String deleteServicoSql = "DELETE FROM servicos_em_andamento WHERE id_nota = ?";
            try (PreparedStatement deleteServicoStmt = conn.prepareStatement(deleteServicoSql)) {
                deleteServicoStmt.setInt(1, idNota);
                int rowsDeleted = deleteServicoStmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Registro excluído de 'servicos_em_andamento' com sucesso.");
                }
            }

            // Deletar o registro da tabela `notas` se o status for 'rejeitado'
            if ("rejeitado".equals(status)) {
                String deleteNotaSql = "DELETE FROM notas WHERE id = ?";
                try (PreparedStatement deleteNotaStmt = conn.prepareStatement(deleteNotaSql)) {
                    deleteNotaStmt.setInt(1, idNota);
                    int rowsDeletedNota = deleteNotaStmt.executeUpdate();
                    if (rowsDeletedNota > 0) {
                        System.out.println("Registro excluído de 'notas' com sucesso.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")

    public void gerarPDF(int idNota) {
        // Exibir um seletor de arquivos para salvar o PDF
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Escolha o local para salvar o PDF");
        fileChooser.setSelectedFile(new File("saida.pdf")); // Nome padrão do arquivo

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String outputPdfPath = fileToSave.getAbsolutePath();

            Document document = new Document(PageSize.A4);
            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPdfPath));
                document.open();

                // Fontes
                Font headerFont = new Font(Font.HELVETICA, 19, Font.BOLD);
                Font normalFont = new Font(Font.HELVETICA, 12);
                Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);

                // Título
                Paragraph title = new Paragraph("Ordem de Serviços | VS", headerFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph("\n"));

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789")) {
                    // Consultar informações do cliente
                    String selectNotaSql = "SELECT * FROM notas WHERE id = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(selectNotaSql)) {
                        stmt.setInt(1, idNota);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                String nomeDono = rs.getString("nome_dono");
                                String telefone = rs.getString("telefone");
                                String data = rs.getString("data");
                                String computador = rs.getString("computador");
                                String atendente = rs.getString("atendente");
                                double total = rs.getDouble("total");

                                // Tabela de informações principais
                                PdfPTable infoTable = new PdfPTable(2);
                                infoTable.setWidthPercentage(100);
                                infoTable.setSpacingBefore(10f);
                                infoTable.setSpacingAfter(10f);

                                infoTable.addCell(new Phrase("ID Nota:", boldFont));
                                infoTable.addCell(new Phrase(String.valueOf(idNota), normalFont));
                                infoTable.addCell(new Phrase("Nome:", boldFont));
                                infoTable.addCell(new Phrase(nomeDono, normalFont));
                                infoTable.addCell(new Phrase("Telefone:", boldFont));
                                infoTable.addCell(new Phrase(telefone, normalFont));
                                infoTable.addCell(new Phrase("Data:", boldFont));
                                infoTable.addCell(new Phrase(data, normalFont));
                                infoTable.addCell(new Phrase("Computador:", boldFont));
                                infoTable.addCell(new Phrase(computador, normalFont));
                                infoTable.addCell(new Phrase("Atendente:", boldFont));
                                infoTable.addCell(new Phrase(atendente, normalFont));

                                document.add(infoTable);

                                document.add(new Paragraph("\nServiços:", boldFont));
                                PdfPTable servicosTable = new PdfPTable(2);
                                servicosTable.setWidthPercentage(100);
                                servicosTable.setSpacingBefore(5f);  // Espaçamento antes da tabela
                                servicosTable.setSpacingAfter(5f);   // Espaçamento após a tabela
                                servicosTable.addCell(new Phrase("Serviço", boldFont));
                                servicosTable.addCell(new Phrase("Preço", boldFont));

                                // Consultar os serviços da tabela notas
                                String selectServicosSql = "SELECT servico1, servico2, servico3, servico4, servico5, servico6, servico7, servico8, servico9, servico10,"
                                        + " servico11, servico12, servico13, servico14, servico15, servico16, servico17, servico18, servico19, servico20 FROM notas WHERE id = ?";
                                try (PreparedStatement servStmt = conn.prepareStatement(selectServicosSql)) {
                                    servStmt.setInt(1, idNota);
                                    try (ResultSet servRs = servStmt.executeQuery()) {
                                        if (servRs.next()) {
                                            for (int i = 1; i <= 20; i++) {
                                                String servico = servRs.getString("servico" + i);
                                                if (servico != null && !servico.isEmpty()) {
                                                    // Agora consultar o preço do serviço na tabela servicos
                                                    String selectPrecoSql = "SELECT preco FROM servicos WHERE nome = ?";
                                                    try (PreparedStatement precoStmt = conn.prepareStatement(selectPrecoSql)) {
                                                        precoStmt.setString(1, servico);
                                                        try (ResultSet precoRs = precoStmt.executeQuery()) {
                                                            if (precoRs.next()) {
                                                                servicosTable.addCell(new Phrase(servico, normalFont));
                                                                servicosTable.addCell(new Phrase(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(precoRs.getDouble("preco")), normalFont));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                document.add(servicosTable);

                                // Adicionar tabela de produtos
                                document.add(new Paragraph("\nProdutos:", boldFont));
                                PdfPTable produtosTable = new PdfPTable(3);
                                produtosTable.setWidthPercentage(100);
                                produtosTable.setSpacingBefore(5f);
                                produtosTable.setSpacingAfter(5f);
                                produtosTable.addCell(new Phrase("Produto", boldFont));
                                produtosTable.addCell(new Phrase("Preço", boldFont));
                                produtosTable.addCell(new Phrase("Quantidade", boldFont));

                                // Alterando o tipo do ResultSet
                                String selectProdutosSql = "SELECT produto_nome, preco, quantidade FROM nota_produtos WHERE nome_dono = ?";
                                try (PreparedStatement prodStmt = conn.prepareStatement(selectProdutosSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                                    prodStmt.setString(1, nomeDono);  // Certifique-se de que nome_dono está correto
                                    try (ResultSet prodRs = prodStmt.executeQuery()) {
                                        // Se não houver produtos, informar
                                        if (!prodRs.next()) {
                                            System.out.println("Nenhum produto encontrado para o nome_dono: " + nomeDono);
                                        } else {
                                            // Agora, pode usar o cursor normalmente sem precisar de beforeFirst()
                                            do {
                                                // Debugging: Verifique os dados dos produtos
                                                String produtoNome = prodRs.getString("produto_nome");
                                                double preco = prodRs.getDouble("preco");
                                                int quantidade = prodRs.getInt("quantidade");
                                                System.out.println("Produto: " + produtoNome + ", Preço: " + preco + ", Quantidade: " + quantidade);

                                                // Adicionar dados na tabela de produtos no PDF
                                                produtosTable.addCell(new Phrase(produtoNome, normalFont));
                                                produtosTable.addCell(new Phrase(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(preco), normalFont));
                                                produtosTable.addCell(new Phrase(String.valueOf(quantidade), normalFont));
                                            } while (prodRs.next()); // Percorrer todos os registros de produtos
                                        }
                                    } catch (SQLException e) {
                                        System.out.println("Erro ao consultar os produtos: " + e.getMessage());
                                    }
                                }
                                document.add(produtosTable);

                                // Total
                                document.add(new Paragraph("Valor final _________________________________________________________________", boldFont));

                                PdfPTable totalTable = new PdfPTable(2);
                                totalTable.setWidthPercentage(100);
                                totalTable.setSpacingBefore(0f); // Retirar qualquer espaçamento antes da tabela

                                PdfPCell titleCell = new PdfPCell(new Phrase("Total:", boldFont));
                                titleCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Alinhamento centralizado
                                titleCell.setBorder(0); // Remover borda para não interferir no layout
                                totalTable.addCell(titleCell);

                                PdfPCell totalCell = new PdfPCell(new Phrase(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(total), normalFont));
                                totalCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Alinhamento centralizado
                                totalCell.setBorder(0);  // Remover borda para não interferir no layout
                                totalTable.addCell(totalCell);

                                document.add(totalTable);

                                // **Movendo o Status para antes da assinatura**
                                document.add(new Paragraph("\nStatus:", boldFont));
                                Paragraph statusParagraph = new Paragraph("Aguardando aprovação", normalFont);
                                document.add(statusParagraph);

                                // Espaço para assinatura
                                document.add(new Paragraph("\n\n\n"));

                                Chunk linhaAssinatura = new Chunk("__________________________________", boldFont);
                                Paragraph assinaturaLinha = new Paragraph(linhaAssinatura);
                                assinaturaLinha.setAlignment(Element.ALIGN_CENTER);
                                document.add(assinaturaLinha);

                                Paragraph assinaturaTexto = new Paragraph("Assinatura", normalFont);
                                assinaturaTexto.setAlignment(Element.ALIGN_CENTER);
                                document.add(assinaturaTexto);
                            } else {
                                JOptionPane.showMessageDialog(null, "Nota não encontrada no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Erro ao consultar o banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    System.out.println("erro" + e);
                }

                document.close();
                JOptionPane.showMessageDialog(null, "PDF gerado com sucesso em: " + outputPdfPath, "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao gerar PDF: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
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
            java.util.logging.Logger.getLogger(Andamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Andamento().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable aparecer_listado_como_adc;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox consertando;
    private javax.swing.JCheckBox fila;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> produtos_associados;
    private javax.swing.JCheckBox rejeitado;
    private javax.swing.JButton save;
    // End of variables declaration//GEN-END:variables

    private void carregarDados() {
        DefaultTableModel model = (DefaultTableModel) aparecer_listado_como_adc.getModel();
        model.setRowCount(0); // Limpa a tabela antes de adicionar os dados

        try {
            try ( // Conexão com o banco de dados
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789")) {
                String sql = "SELECT * FROM notas WHERE adc = true";
                try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

                    // Adicionando os dados à tabela
                    while (rs.next()) {
                        Object[] row = {
                            rs.getInt("id"),
                            rs.getString("nome_dono"),
                            rs.getString("telefone"),
                            rs.getString("data"),
                            rs.getString("computador"),
                            rs.getString("atendente"),
                            rs.getDouble("total"), // Adicione mais colunas conforme necessário
                        };
                        model.addRow(row);
                    }

                }
            }
        } catch (SQLException e) {
        }
    }

    private void carregarProdutosAssociados(int clienteId) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        produtos_associados.setModel(listModel); // Define o modelo do JList

        try {
            // Conexão com o banco de dados
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789")) {
                // Alterei a consulta SQL para usar o id do cliente (id da tabela 'notas')
                String sql = "SELECT produto_nome, preco, quantidade FROM nota_produtos WHERE nome_dono = (SELECT nome_dono FROM notas WHERE id = ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, clienteId); // Substitui o placeholder pelo ID do cliente
                    try (ResultSet rs = stmt.executeQuery()) {

                        // Adicionando os produtos ao modelo do JList
                        while (rs.next()) {
                            String produto = String.format("%s - R$ %.2f (Qtd: %d)",
                                    rs.getString("produto_nome"),
                                    rs.getDouble("preco"),
                                    rs.getInt("quantidade"));
                            listModel.addElement(produto);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
