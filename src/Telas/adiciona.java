package Telas;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.sql.*;
import javax.imageio.ImageIO;
import sounds.Sounds;

/**
 *
 * @author denyl
 */
public class adiciona extends javax.swing.JFrame {

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/darcksoftware";
    private final String DATABASE_USER = "denny";
    private final String DATABASE_PASSWORD = "123456789";

    /**
     * Creates new form adiciona
     */
    public adiciona() {
        setUndecorated(true);
        initComponents();

        formatarCampoPreco();

        setLocationRelativeTo(null); // Centraliza a tela ao iniciá-la
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Mostrar_imagem = new javax.swing.JLabel();
        imagem = new javax.swing.JButton();
        salvar = new javax.swing.JButton();
        fechar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        DESCRICAO = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        PRODUTO = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        preco = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 51));

        Mostrar_imagem.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, new java.awt.Color(204, 0, 204), new java.awt.Color(204, 0, 204), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)), "Imagem", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        imagem.setBackground(new java.awt.Color(0, 0, 0));
        imagem.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        imagem.setForeground(new java.awt.Color(255, 255, 255));
        imagem.setText("INSERIR");
        imagem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imagemMouseClicked(evt);
            }
        });
        imagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imagemActionPerformed(evt);
            }
        });

        salvar.setBackground(new java.awt.Color(0, 0, 0));
        salvar.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        salvar.setForeground(new java.awt.Color(255, 255, 255));
        salvar.setText("SALVAR");
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

        fechar.setBackground(new java.awt.Color(0, 0, 0));
        fechar.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        fechar.setForeground(new java.awt.Color(255, 255, 255));
        fechar.setText("FECHAR");
        fechar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fecharMouseClicked(evt);
            }
        });
        fechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fecharActionPerformed(evt);
            }
        });

        DESCRICAO.setColumns(20);
        DESCRICAO.setFont(new java.awt.Font("Century", 0, 14)); // NOI18N
        DESCRICAO.setRows(5);
        DESCRICAO.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jScrollPane1.setViewportView(DESCRICAO);

        jLabel3.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("DESCRIÇÃO:");

        PRODUTO.setFont(new java.awt.Font("Century", 0, 16)); // NOI18N
        PRODUTO.setForeground(new java.awt.Color(0, 0, 0));
        PRODUTO.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        jLabel2.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("PRODUTOS:");

        preco.setBackground(new java.awt.Color(0, 0, 0));
        preco.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Preço:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Historic", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        preco.setForeground(new java.awt.Color(255, 255, 255));
        preco.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        preco.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("R$:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Mostrar_imagem, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(imagem, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fechar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(salvar, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(PRODUTO)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(preco, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 11, Short.MAX_VALUE)
                        .addComponent(Mostrar_imagem, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(imagem, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salvar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(fechar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(PRODUTO))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3))
                            .addComponent(preco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7))
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

    private void imagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imagemActionPerformed
        // Código para selecionar e exibir a imagem
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Lê o arquivo de imagem e exibe na label
                BufferedImage image = ImageIO.read(selectedFile);
                ImageIcon icon = new ImageIcon(image.getScaledInstance(190, 180, Image.SCALE_SMOOTH));
                Mostrar_imagem.setIcon(icon);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao ler a imagem: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_imagemActionPerformed

    private void salvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salvarActionPerformed
        // Recupera os dados dos campos
        String produto = PRODUTO.getText();
        String descricao = DESCRICAO.getText();
        String precoString = preco.getText().replace(".", "").replace(",", ".").trim(); // Remove separador de milhar e substitui vírgula por ponto
        double precoProduto = 0.0; // Inicializa o valor do preço

        // Verifica se os campos estão preenchidos
        if (produto.isEmpty() || descricao.isEmpty() || precoString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos corretamente.");
            return;
        }

        try {
            // Tenta converter a string para double
            precoProduto = Double.parseDouble(precoString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Formato de preço inválido: " + e.getMessage());
            return;
        }

        byte[] imagemBytes = null; // Inicializa o array de bytes da imagem como nulo

        // Verifica se uma imagem foi selecionada
        if (Mostrar_imagem.getIcon() != null) {
            // Obtém o ícone da imagem da label
            ImageIcon icon = (ImageIcon) Mostrar_imagem.getIcon();
            Image n = icon.getImage();

            // Converte a imagem para um array de bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(convertToBufferedImage(n), "jpg", baos);
                baos.flush();
                imagemBytes = baos.toByteArray();
                baos.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao converter a imagem para bytes: " + e.getMessage());
                return;
            }
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Abrir a conexão
            String url = "jdbc:mysql://localhost/DarckSoftware?user=denny&password=123456789&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
            connection = DriverManager.getConnection(url);

            // Insere o produto no banco de dados
            String sql = "INSERT INTO produtos (nome, descricao, preco, imagem) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, produto);
            preparedStatement.setString(2, descricao);
            preparedStatement.setDouble(3, precoProduto);
            preparedStatement.setBytes(4, imagemBytes); // Define o array de bytes da imagem
            preparedStatement.executeUpdate();

            // Fecha a conexão
            connection.close();

            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar produto: " + e.getMessage());
        }
    }//GEN-LAST:event_salvarActionPerformed
    Sounds sound = new Sounds();

    private void fecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fecharActionPerformed
        // Tocar o som primeiro
        sound.tocarSom("src/sounds/sair.wav");

        // Adicionar um atraso antes de fechar
        try {
            Thread.sleep(1000); // 1 segundo de atraso, ajuste conforme necessário
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dispose();
    }//GEN-LAST:event_fecharActionPerformed

    private void imagemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imagemMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_imagemMouseClicked

    private void salvarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salvarMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_salvarMouseClicked

    private void fecharMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fecharMouseClicked
        sound.tocarSom("src/sounds/sair.wav");
    }//GEN-LAST:event_fecharMouseClicked

    private BufferedImage convertToBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return bufferedImage;
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
            java.util.logging.Logger.getLogger(adiciona.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new adiciona().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea DESCRICAO;
    private javax.swing.JLabel Mostrar_imagem;
    private javax.swing.JTextField PRODUTO;
    private javax.swing.JButton fechar;
    private javax.swing.JButton imagem;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JFormattedTextField preco;
    private javax.swing.JButton salvar;
    // End of variables declaration//GEN-END:variables
}
