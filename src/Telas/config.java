package Telas;

import java.sql.*;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import sounds.Sounds;

/**
 *
 * @author denyl
 */
public class config extends javax.swing.JFrame {

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/darcksoftware";
    private final String DATABASE_USER = "denny";
    private final String DATABASE_PASSWORD = "123456789";

    /**
     * Creates new form config
     */
    public config() {
        setUndecorated(true);
        initComponents();
        setLocationRelativeTo(null);
        // Carregar nome e ícone do banco de dados
        String nomeJFrame = carregarNomeSalvoDoBancoDeDados();
        ImageIcon icone = carregarIconeSalvoDoBancoDeDados();
        carregarDadosEmpresa(); // Definir título e ícone do JFrame
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        imagem = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        nomeApp = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        salvar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        arquivo = new javax.swing.JButton();
        codigoqr = new javax.swing.JLabel();
        qrcode = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 51));

        jPanel2.setBackground(new java.awt.Color(0, 102, 51));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel1.setFont(new java.awt.Font("Wide Latin", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Painel de personalização");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1056, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );

        imagem.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true), "Logo da empresa", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_BOTTOM, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 18)); // NOI18N
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

        nomeApp.setBackground(new java.awt.Color(0, 0, 0));
        nomeApp.setFont(new java.awt.Font("Microsoft Tai Le", 1, 48)); // NOI18N
        nomeApp.setForeground(new java.awt.Color(255, 255, 255));
        nomeApp.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jLabel3.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nome:");

        salvar.setBackground(new java.awt.Color(51, 51, 51));
        salvar.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        salvar.setForeground(new java.awt.Color(255, 255, 255));
        salvar.setText("Salvar");
        salvar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salvarMouseClicked(evt);
            }
        });
        salvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvarActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        arquivo.setBackground(new java.awt.Color(51, 51, 51));
        arquivo.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        arquivo.setForeground(new java.awt.Color(255, 255, 255));
        arquivo.setText("Arquivo");
        arquivo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                arquivoMouseClicked(evt);
            }
        });
        arquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arquivoActionPerformed(evt);
            }
        });

        codigoqr.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Qr code vazio", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N

        qrcode.setBackground(new java.awt.Color(51, 51, 51));
        qrcode.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        qrcode.setForeground(new java.awt.Color(255, 255, 255));
        qrcode.setText("Select code");
        qrcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qrcodeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(imagem, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(270, 270, 270)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nomeApp, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(salvar, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(arquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(52, 52, 52)
                        .addComponent(codigoqr, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(qrcode, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(imagem, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(38, Short.MAX_VALUE))
                    .addComponent(jSeparator1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nomeApp, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(codigoqr, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(arquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(67, 67, 67))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(117, 117, 117)
                                        .addComponent(qrcode)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(salvar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
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

    private void arquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arquivoActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedImage bufferedImage = ImageIO.read(selectedFile);

                // Redimensiona a imagem para se ajustar ao JLabel, se necessário
                ImageIcon icone = new ImageIcon(bufferedImage);
                Image img = icone.getImage();
                Image newImg = img.getScaledInstance(imagem.getWidth(), imagem.getHeight(), Image.SCALE_SMOOTH);
                imagem.setIcon(new ImageIcon(newImg));

                salvarImagemNoBancoDeDados(bufferedImage);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar a imagem: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_arquivoActionPerformed

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

    private void salvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salvarActionPerformed
        String novoNome = nomeApp.getText();
        byte[] novaImagem = obterBytesDaImagem();

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD); PreparedStatement pstmt = conn.prepareStatement("UPDATE empresa SET nome = ?, imagem = ? WHERE id = 1")) {

            pstmt.setString(1, novoNome);
            pstmt.setBytes(2, novaImagem);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar os dados da empresa: " + e.getMessage());
        }
    }//GEN-LAST:event_salvarActionPerformed

    Sounds sound = new Sounds();

    private void arquivoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_arquivoMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_arquivoMouseClicked

    private void salvarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salvarMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_salvarMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        sound.tocarSom("src/sounds/sair.wav");
    }//GEN-LAST:event_jButton1MouseClicked

    private void qrcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qrcodeActionPerformed
   JFileChooser fileChooser = new JFileChooser();
    int returnValue = fileChooser.showOpenDialog(null);

    if (returnValue == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        try {
            BufferedImage bufferedImage = ImageIO.read(selectedFile);

            // Redimensiona a imagem para se ajustar ao JLabel codigoqr
            ImageIcon icone = new ImageIcon(bufferedImage);
            Image img = icone.getImage();
            Image newImg = img.getScaledInstance(codigoqr.getWidth(), codigoqr.getHeight(), Image.SCALE_SMOOTH);
            codigoqr.setIcon(new ImageIcon(newImg));

            // Verifica o resultado do método salvarImagemQRCODE
            boolean sucesso = salvarImagemQRCODE(bufferedImage);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "QR Code salvo com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao salvar o QR Code!");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar a imagem: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_qrcodeActionPerformed

  private boolean salvarImagemQRCODE(BufferedImage bufferedImage) {
    String DATABASE_URL = "jdbc:mysql://localhost:3306/darcksoftware";
    String DATABASE_USER = "denny";
    String DATABASE_PASSWORD = "123456789";

    try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
         ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

        // Primeiro, exclui a imagem anterior (se houver)
        String deleteSql = "DELETE FROM pagamento_qrcode";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.executeUpdate();
        }

        // Redefine o AUTO_INCREMENT para 1
        String resetAutoIncrementSql = "ALTER TABLE pagamento_qrcode AUTO_INCREMENT = 1";
        try (PreparedStatement resetAutoIncrementStmt = conn.prepareStatement(resetAutoIncrementSql)) {
            resetAutoIncrementStmt.executeUpdate();
        }

        // Converte a nova imagem para bytes
        ImageIO.write(bufferedImage, "png", baos);
        byte[] imagemBytes = baos.toByteArray();

        // SQL para salvar a nova imagem
        String sql = "INSERT INTO pagamento_qrcode (imagem) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBytes(1, imagemBytes);
            pstmt.executeUpdate();
            return true; // Retorna sucesso
        }

    } catch (SQLException | IOException e) {
        JOptionPane.showMessageDialog(this, "Erro ao salvar o QR Code no banco de dados: " + e.getMessage());
        return false; // Retorna falha
    }
}

    
    private byte[] obterBytesDaImagem() {
        Icon iconeAtual = imagem.getIcon();
        if (iconeAtual != null && iconeAtual instanceof ImageIcon) {
            Image imagemIcone = ((ImageIcon) iconeAtual).getImage();
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                BufferedImage bufferedImage = new BufferedImage(
                        imagemIcone.getWidth(null), imagemIcone.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.drawImage(imagemIcone, 0, 0, null);
                g2d.dispose();

                ImageIO.write(bufferedImage, "png", baos);
                return baos.toByteArray();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao processar a imagem: " + e.getMessage());
            }
        }
        return null;
    }

    private void carregarDadosEmpresa() {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT nome, imagem FROM empresa WHERE id = 1")) {

            if (rs.next()) {
                nomeApp.setText(rs.getString("nome"));

                byte[] imagemBytes = rs.getBytes("imagem");
                if (imagemBytes != null) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(imagemBytes);
                    BufferedImage bufferedImage = ImageIO.read(bais);
                    Image img = bufferedImage.getScaledInstance(imagem.getWidth(), imagem.getHeight(), Image.SCALE_SMOOTH);
                    imagem.setIcon(new ImageIcon(img));
                }
            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar os dados da empresa: " + e.getMessage());
        }
    }

    private void salvarImagemNoBancoDeDados(BufferedImage bufferedImage) {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            ImageIO.write(bufferedImage, "png", baos);
            byte[] imagemBytes = baos.toByteArray();

            String sql = "UPDATE empresa SET imagem = ? WHERE id = 1";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setBytes(1, imagemBytes);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Imagem salva com sucesso!");
            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar a imagem no banco de dados: " + e.getMessage());
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
            java.util.logging.Logger.getLogger(config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new config().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton arquivo;
    private javax.swing.JLabel codigoqr;
    private javax.swing.JLabel imagem;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField nomeApp;
    private javax.swing.JButton qrcode;
    private javax.swing.JButton salvar;
    // End of variables declaration//GEN-END:variables
}
