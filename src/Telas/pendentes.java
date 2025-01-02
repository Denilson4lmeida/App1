package Telas;

import java.text.SimpleDateFormat;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import com.itextpdf.text.pdf.draw.LineSeparator;
import com.lowagie.text.Element;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Date;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTable;
import sounds.Sounds;

public class pendentes extends javax.swing.JFrame {

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/darcksoftware";
    private final String DATABASE_USER = "denny";
    private final String DATABASE_PASSWORD = "123456789";

    public pendentes() {
        setUndecorated(true);
        initComponents();
        setLocationRelativeTo(null);
        preencherTabela();
        // Adicionar MouseListener e ListSelectionListener na JTable
        serviços.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = serviços.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    // Ações com base no clique
                    int idNota = (Integer) serviços.getValueAt(row, 0);
                    String nomeDono = (String) serviços.getValueAt(row, 1); // Assumindo que o nome do dono está na coluna 1

                    // Preencher lista de serviços relacionados
                    preencherListaServicosRelacionados(idNota);

                    // Carregar produtos relacionados ao nome do dono
                    carregarProdutosRelacionados(nomeDono);
                }
            }
        });

        serviços.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = serviços.getSelectedRow();
                if (selectedRow != -1) {
                    int idNota = (Integer) serviços.getValueAt(selectedRow, 0);
                    preencherListaServicosRelacionados(idNota);
                }
            }
        });

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

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789");
    }

    // Método genérico para preencher JTable
    private void preencherTabela() {
        String sql = "SELECT id_nota, nome_dono, telefone, data, computador, atendente, total "
                + "FROM servicos_em_andamento WHERE status = 'consertando'";

        preencherTabelaComSQL(serviços, sql, new String[]{"ID Nota", "Nome Dono", "Telefone", "Data", "Computador", "Atendente", "Total"});
    }

// Método auxiliar para preencher JTable com uma consulta SQL
    private void preencherTabelaComSQL(JTable tabela, String sql, String[] colunas) {
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setColumnIdentifiers(colunas);
        model.setRowCount(0); // Limpar dados existentes

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] rowData = new Object[colunas.length];
                for (int i = 0; i < colunas.length; i++) {
                    rowData[i] = rs.getObject(i + 1); // Mapear as colunas na ordem
                }
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Tratar erro ou registrar logs
        }
    }

    // Método para preencher lista de serviços relacionados
    private void preencherListaServicosRelacionados(int idNota) {
        String sql = "SELECT servico1, servico2, servico3, servico4, servico5, servico6, servico7, "
                + "servico8, servico9, servico10, servico11, servico12, servico13, servico14, "
                + "servico15, servico16, servico17, servico18, servico19, servico20 "
                + "FROM notas WHERE id = ?";

        carregarListaComSQL(sql, serviços_relacionados_pelo_nome, idNota, 20);
    }

// Método auxiliar para carregar uma JList a partir de SQL
    private void carregarListaComSQL(String sql, JList<String> lista, int parametro, int numeroColunas) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        lista.setModel(listModel);

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    for (int i = 1; i <= numeroColunas; i++) {
                        String servico = rs.getString(i);
                        if (servico != null && !servico.isEmpty()) {
                            listModel.addElement(servico);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// Método para carregar produtos relacionados ao nome do dono
    private void carregarProdutosRelacionados(String nomeDono) {
        String sql = "SELECT produto_nome, preco, quantidade FROM nota_produtos WHERE nome_dono = ?";
        carregarListaDeProdutos(nomeDono, sql, produtos_relacionados_pelo_nome1);
    }

// Método auxiliar para carregar JList com produtos
    private void carregarListaDeProdutos(String parametro, String sql, JList<String> lista) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        lista.setModel(listModel);

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String produtoInfo = rs.getString("produto_nome") + " - R$"
                            + rs.getDouble("preco") + " (Quantidade: "
                            + rs.getInt("quantidade") + ")";
                    listModel.addElement(produtoInfo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        serviços = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        concluido = new javax.swing.JCheckBox();
        desistencia = new javax.swing.JCheckBox();
        imprimir = new javax.swing.JButton();
        exit = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        serviços_relacionados_pelo_nome = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        produtos_relacionados_pelo_nome1 = new javax.swing.JList<>();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        serviços.setBackground(new java.awt.Color(102, 102, 102));
        serviços.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(serviços);

        jPanel3.setBackground(new java.awt.Color(0, 153, 51));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 3));

        jLabel1.setFont(new java.awt.Font("Wide Latin", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Pendentes para entrega");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 3, true));

        buttonGroup1.add(concluido);
        concluido.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        concluido.setForeground(new java.awt.Color(255, 255, 255));
        concluido.setText("Concluido");
        concluido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                concluidoMouseClicked(evt);
            }
        });
        concluido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                concluidoActionPerformed(evt);
            }
        });

        buttonGroup1.add(desistencia);
        desistencia.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        desistencia.setForeground(new java.awt.Color(255, 255, 255));
        desistencia.setText("Desistencia");
        desistencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                desistenciaMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(concluido, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(desistencia)
                .addContainerGap(76, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(desistencia)
                    .addComponent(concluido))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        imprimir.setBackground(new java.awt.Color(102, 102, 102));
        imprimir.setFont(new java.awt.Font("Agency FB", 1, 36)); // NOI18N
        imprimir.setForeground(new java.awt.Color(255, 255, 255));
        imprimir.setText("Finalizar");
        imprimir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imprimirMouseClicked(evt);
            }
        });
        imprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimirActionPerformed(evt);
            }
        });

        exit.setBackground(new java.awt.Color(102, 102, 102));
        exit.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        exit.setForeground(new java.awt.Color(255, 255, 255));
        exit.setText("Fechar");
        exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitMouseClicked(evt);
            }
        });
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(140, 140, 140)
                .addComponent(imprimir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(exit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addComponent(imprimir)))
        );

        serviços_relacionados_pelo_nome.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 255), 3, true));
        serviços_relacionados_pelo_nome.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        serviços_relacionados_pelo_nome.setForeground(new java.awt.Color(0, 0, 0));
        serviços_relacionados_pelo_nome.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { " " };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(serviços_relacionados_pelo_nome);

        produtos_relacionados_pelo_nome1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 255), 3, true));
        produtos_relacionados_pelo_nome1.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        produtos_relacionados_pelo_nome1.setForeground(new java.awt.Color(0, 0, 0));
        produtos_relacionados_pelo_nome1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { " " };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(produtos_relacionados_pelo_nome1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 923, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimirActionPerformed
        // Simulando o valor final da tela pendentes
        double valorFinal = calcularValorFinal();

        // Abrir a tela de Pagamento passando o valor final
        Pagamento pagamentoTela = new Pagamento(valorFinal);
        pagamentoTela.setVisible(true);

        // Bloqueia o fluxo até que a janela de Pagamento seja fechada
        pagamentoTela.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Após a janela ser fechada, abre o JFileChooser para salvar o PDF
                JFileChooser fileChooser = new JFileChooser();

                // Define o filtro para arquivos PDF
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Documents", "pdf");
                fileChooser.setFileFilter(filter);

                // Abre o diálogo de salvar
                int returnValue = fileChooser.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    // Obtém o arquivo selecionado pelo usuário
                    File selectedFile = fileChooser.getSelectedFile();

                    // Verifica se a extensão .pdf está presente no nome do arquivo
                    String filePath = selectedFile.getPath();
                    if (!filePath.endsWith(".pdf")) {
                        filePath += ".pdf";
                    }

                    try {
                        // Chama o método para obter informações e gerar o PDF
                        obterInformacoesSelecionadas(filePath);
                    } catch (DocumentException ex) {
                        Logger.getLogger(pendentes.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (com.lowagie.text.DocumentException ex) {
                        Logger.getLogger(pendentes.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });
    }//GEN-LAST:event_imprimirActionPerformed

    private double calcularValorFinal() {
        double total = 0.0;
        DefaultTableModel model = (DefaultTableModel) serviços.getModel();

        // Percorre as linhas da tabela e soma os valores da coluna "Total"
        for (int i = 0; i < model.getRowCount(); i++) {
            total += ((BigDecimal) model.getValueAt(i, 6)).doubleValue();  // Coluna "total"
        }
        return total;
    }

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // Tocar o som primeiro
        sound.tocarSom("src/sounds/sair.wav");

        // Adicionar um atraso antes de fechar
        try {
            Thread.sleep(1000); // 1 segundo de atraso, ajuste conforme necessário
        } catch (InterruptedException e) {
        }

        dispose();
    }//GEN-LAST:event_exitActionPerformed

    private void concluidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_concluidoActionPerformed

    }//GEN-LAST:event_concluidoActionPerformed

    Sounds sound = new Sounds();

    private void desistenciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_desistenciaMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_desistenciaMouseClicked

    private void concluidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_concluidoMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_concluidoMouseClicked

    private void imprimirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imprimirMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_imprimirMouseClicked

    private void exitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitMouseClicked
        sound.tocarSom("src/sounds/sair.wav");
    }//GEN-LAST:event_exitMouseClicked

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
            java.util.logging.Logger.getLogger(pendentes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new pendentes().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox concluido;
    private javax.swing.JCheckBox desistencia;
    private javax.swing.JButton exit;
    private javax.swing.JButton imprimir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList<String> produtos_relacionados_pelo_nome1;
    private javax.swing.JTable serviços;
    private javax.swing.JList<String> serviços_relacionados_pelo_nome;
    // End of variables declaration//GEN-END:variables

    private void obterInformacoesSelecionadas(String filePath) throws DocumentException, com.lowagie.text.DocumentException {
        int selectedRow = serviços.getSelectedRow();
        if (selectedRow != -1) {
            // Dados da linha selecionada na JTable
            int idNota = (Integer) serviços.getValueAt(selectedRow, 0);
            String nomeDono = (String) serviços.getValueAt(selectedRow, 1);
            String telefone = (String) serviços.getValueAt(selectedRow, 2); // Corrigido para 'String'
            Date data = (Date) serviços.getValueAt(selectedRow, 3); // A data deve ser obtida como Date diretamente
            String computador = (String) serviços.getValueAt(selectedRow, 4);
            String atendente = (String) serviços.getValueAt(selectedRow, 5);
            BigDecimal total = (BigDecimal) serviços.getValueAt(selectedRow, 6);

            // Itens do JList
            DefaultListModel<String> listModel = (DefaultListModel<String>) serviços_relacionados_pelo_nome.getModel();
            List<String> servicosRelacionados = new ArrayList<>();
            for (int i = 0; i < listModel.size(); i++) {
                servicosRelacionados.add(listModel.getElementAt(i));
            }
            // Itens do JList de produtos relacionados
            DefaultListModel<String> produtosListModel = (DefaultListModel<String>) produtos_relacionados_pelo_nome1.getModel();
            List<String> produtosRelacionados = new ArrayList<>();
            for (int i = 0; i < produtosListModel.size(); i++) {
                produtosRelacionados.add(produtosListModel.getElementAt(i));
            }

            // Estado dos checkboxes
            boolean isConcluido = concluido.isSelected();
            boolean isDesistencia = desistencia.isSelected();

            // Gerar o PDF com as informações
            gerarPDF(filePath, idNota, nomeDono, telefone, data, computador, atendente, total, servicosRelacionados, produtosRelacionados, isConcluido, isDesistencia);
        }
    }

    private void gerarPDF(String filePath, int idNota, String nomeDono, String telefone, Date data, String computador, String atendente, BigDecimal total, List<String> servicosRelacionados, List<String> produtosRelacionados, boolean isConcluido, boolean isDesistencia) throws DocumentException, com.lowagie.text.DocumentException {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Adiciona título com fonte maior e negrito
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Relatório de Serviço - VALLE SOFTWARE", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            // Adiciona uma linha horizontal (simulando a linha separadora)
            document.add(new Paragraph(" "));
            document.add(new Paragraph("#####-----------------------------------------------****------------------------------------------------------#####")); // Linha simples

            // Adiciona informações da nota em uma tabela
            Font sectionFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            // Tabela de informações da nota
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidths(new float[]{1, 2}); // Define a largura das colunas

            // Formatar a data
            String dataFormatada = formatDate(data);

            // Adiciona as informações da nota
            infoTable.addCell(new Phrase("Nº nota:", boldFont));
            infoTable.addCell(new Phrase(String.valueOf(idNota), sectionFont));

            infoTable.addCell(new Phrase("Nome:", boldFont));
            infoTable.addCell(new Phrase(nomeDono, sectionFont));

            infoTable.addCell(new Phrase("Telefone:", boldFont));
            infoTable.addCell(new Phrase(telefone, sectionFont));

            infoTable.addCell(new Phrase("Data:", boldFont));
            infoTable.addCell(new Phrase(dataFormatada, sectionFont));

            infoTable.addCell(new Phrase("Computador:", boldFont));
            infoTable.addCell(new Phrase(computador, sectionFont));

            infoTable.addCell(new Phrase("Técnico:", boldFont));
            infoTable.addCell(new Phrase(atendente, sectionFont));

            infoTable.addCell(new Phrase("Total:", boldFont));
            infoTable.addCell(new Phrase("R$ " + total, sectionFont));

            document.add(infoTable);
            document.add(new Paragraph(" ")); // Linha em branco

            // Adiciona serviços realizados em uma tabela
            if (!servicosRelacionados.isEmpty()) {
                Paragraph servicesTitle = new Paragraph("Serviços Realizados:", boldFont);
                document.add(servicesTitle);

                PdfPTable servicesTable = new PdfPTable(1);
                for (String servico : servicosRelacionados) {
                    PdfPCell cell = new PdfPCell(new Phrase("• " + servico, sectionFont));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    servicesTable.addCell(cell);
                }

                document.add(servicesTable);
                document.add(new Paragraph(" ")); // Linha em branco
            }

            // Adiciona produtos relacionados em uma tabela
            if (!produtosRelacionados.isEmpty()) {
                Paragraph productsTitle = new Paragraph("Produtos Relacionados:", boldFont);
                document.add(productsTitle);

                PdfPTable productsTable = new PdfPTable(1);
                for (String produto : produtosRelacionados) {
                    PdfPCell cell = new PdfPCell(new Phrase("• " + produto, sectionFont));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    productsTable.addCell(cell);
                }

                document.add(productsTable);
                document.add(new Paragraph(" ")); // Linha em branco
            }

            // Adiciona o estado dos checkboxes em uma tabela
            Paragraph statusTitle = new Paragraph("Status:", boldFont);
            document.add(statusTitle);

            PdfPTable statusTable = new PdfPTable(2);
            statusTable.setWidths(new float[]{1, 2}); // Define a largura das colunas

            statusTable.addCell(new Phrase("Concluído:", boldFont));
            statusTable.addCell(new Phrase(isConcluido ? "Sim" : "Não", sectionFont));

            statusTable.addCell(new Phrase("Desistência:", boldFont));
            statusTable.addCell(new Phrase(isDesistencia ? "Sim" : "Não", sectionFont));

            document.add(statusTable);

            document.close();

            // Apaga o registro correspondente na tabela notas
            excluirNota(idNota, nomeDono, data, computador, atendente, total, isConcluido ? "Concluído" : "Desistência");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    private void excluirNota(int idNota, String nomeDono, Date data, String computador, String atendente, BigDecimal total, String status) {
        String dbUrl = "jdbc:mysql://localhost:3306/DarckSoftware";
        String dbUser = "denny";
        String dbPassword = "123456789";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Conexão estabelecida com sucesso.");

            // Inserir ou atualizar em historico_servicos
            salvarHistoricoComProdutos(connection, idNota, nomeDono, data, computador, atendente, total, status);

            // Excluir registros relacionados
            excluirRegistrosRelacionados(connection, idNota, nomeDono);

            System.out.println("Nota e dados relacionados foram movidos e excluídos com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        preencherTabela(); // Atualiza a JTable após as alterações
    }

    private void salvarHistoricoComProdutos(Connection connection, int idNota, String nomeDono, Date data, String computador, String atendente, BigDecimal total, String status) throws SQLException {
        String historicoSQL = """
        INSERT INTO historico_servicos (id_nota, nome_dono, data, computador, atendente, total, status)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE 
            nome_dono = VALUES(nome_dono), 
            data = VALUES(data), 
            computador = VALUES(computador), 
            atendente = VALUES(atendente), 
            total = VALUES(total), 
            status = VALUES(status);
    """;

        try (PreparedStatement historicoStmt = connection.prepareStatement(historicoSQL)) {
            // Configura os parâmetros para o INSERT ou UPDATE
            historicoStmt.setInt(1, idNota);
            historicoStmt.setString(2, nomeDono);
            historicoStmt.setDate(3, new java.sql.Date(data.getTime()));
            historicoStmt.setString(4, computador);
            historicoStmt.setString(5, atendente);
            historicoStmt.setBigDecimal(6, total);
            historicoStmt.setString(7, status);

            // Executa a instrução
            historicoStmt.executeUpdate();
        }

        // Agora transferir os produtos relacionados ao histórico
        String selectProdutosSQL = "SELECT produto_nome, preco, quantidade FROM nota_produtos WHERE nome_dono = ?";
        String insertProdutosSQL = """
    INSERT INTO historico_servicos 
    (id_nota, nome_dono, data, computador, atendente, total, status, produto_nome, preco, quantidade, total_produto) 
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    ON DUPLICATE KEY UPDATE 
        produto_nome = VALUES(produto_nome), 
        preco = VALUES(preco), 
        quantidade = VALUES(quantidade), 
        total_produto = VALUES(total_produto);
    """;

        try (PreparedStatement selectProdutosStmt = connection.prepareStatement(selectProdutosSQL); PreparedStatement insertProdutosStmt = connection.prepareStatement(insertProdutosSQL)) {

            selectProdutosStmt.setString(1, nomeDono);

            try (ResultSet rs = selectProdutosStmt.executeQuery()) {
                while (rs.next()) {
                    String produtoNome = rs.getString("produto_nome");
                    double preco = rs.getDouble("preco");
                    int quantidade = rs.getInt("quantidade");
                    double totalProduto = preco * quantidade;

                    // Configurar parâmetros para inserção dos produtos
                    insertProdutosStmt.setInt(1, idNota);
                    insertProdutosStmt.setString(2, nomeDono);
                    insertProdutosStmt.setDate(3, new java.sql.Date(data.getTime()));
                    insertProdutosStmt.setString(4, computador);
                    insertProdutosStmt.setString(5, atendente);
                    insertProdutosStmt.setBigDecimal(6, total);
                    insertProdutosStmt.setString(7, status);
                    insertProdutosStmt.setString(8, produtoNome);
                    insertProdutosStmt.setDouble(9, preco);
                    insertProdutosStmt.setInt(10, quantidade);
                    insertProdutosStmt.setDouble(11, totalProduto);

                    insertProdutosStmt.addBatch(); // Adicionar à batch
                }

                // Executar todos os inserts de uma vez
                insertProdutosStmt.executeBatch();
            }
        }
    }

    private void excluirRegistrosRelacionados(Connection connection, int idNota, String nomeDono) throws SQLException {
        String deleteServicosSQL = "DELETE FROM servicos_em_andamento WHERE id_nota = ?";
        String deleteProdutosSQL = "DELETE FROM nota_produtos WHERE nome_dono = ?";
        String deleteNotaSQL = "DELETE FROM notas WHERE id = ?";

        try (PreparedStatement deleteServicosStmt = connection.prepareStatement(deleteServicosSQL); PreparedStatement deleteProdutosStmt = connection.prepareStatement(deleteProdutosSQL); PreparedStatement deleteNotaStmt = connection.prepareStatement(deleteNotaSQL)) {

            deleteServicosStmt.setInt(1, idNota);
            deleteServicosStmt.executeUpdate();

            deleteProdutosStmt.setString(1, nomeDono);
            deleteProdutosStmt.executeUpdate();

            deleteNotaStmt.setInt(1, idNota);
            deleteNotaStmt.executeUpdate();
        }
    }

}
