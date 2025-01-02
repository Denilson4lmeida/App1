package Telas;

import java.awt.Image;
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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sounds.Sounds;

/**
 *
 * @author denyl
 */
public class excluirPro extends javax.swing.JFrame {

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/darcksoftware";
    private final String DATABASE_USER = "denny";
    private final String DATABASE_PASSWORD = "123456789";

    /**
     * Creates new form excluirPro
     */
    public excluirPro() {
        setUndecorated(true);
        initComponents();
        setLocationRelativeTo(null);
        // Configure o modelo da tabela apenas uma vez
        TabelaProdutos.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Nome", "Imagem"}
        ) {
            Class[] types = new Class[]{
                java.lang.String.class, javax.swing.ImageIcon.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });

        // Carregar os produtos após a configuração do modelo
        carregarProdutos();
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelaProdutos = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        excluirProduto = new javax.swing.JButton();
        fechar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 51));

        jPanel2.setBackground(new java.awt.Color(0, 102, 51));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jLabel1.setFont(new java.awt.Font("Wide Latin", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Exclusao de produtos");

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

        TabelaProdutos.setBackground(new java.awt.Color(0, 0, 0));
        TabelaProdutos.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        TabelaProdutos.setForeground(new java.awt.Color(255, 255, 255));
        TabelaProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(TabelaProdutos);

        jPanel3.setBackground(new java.awt.Color(0, 102, 51));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        excluirProduto.setBackground(new java.awt.Color(102, 102, 102));
        excluirProduto.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        excluirProduto.setForeground(new java.awt.Color(255, 255, 255));
        excluirProduto.setText("Excluir");
        excluirProduto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                excluirProdutoMouseClicked(evt);
            }
        });
        excluirProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excluirProdutoActionPerformed(evt);
            }
        });

        fechar.setBackground(new java.awt.Color(102, 102, 102));
        fechar.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        fechar.setForeground(new java.awt.Color(255, 255, 255));
        fechar.setText("Fechar");
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(excluirProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fechar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(excluirProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(fechar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 702, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void excluirProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excluirProdutoActionPerformed
        // Obter a linha selecionada na tabela
        int row = TabelaProdutos.getSelectedRow();

        if (row != -1) { // Verifica se há uma linha selecionada
            // Obtém o nome do produto da linha selecionada
            String nomeProduto = TabelaProdutos.getValueAt(row, 0).toString();

            int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o produto: " + nomeProduto + "?", "Confirmar Exclusão", javax.swing.JOptionPane.YES_NO_OPTION);

            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM produtos WHERE nome = ?";

                try {
                    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789"); PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setString(1, nomeProduto);
                        int rowsAffected = ps.executeUpdate();

                        if (rowsAffected > 0) {
                            javax.swing.JOptionPane.showMessageDialog(this, "Produto excluído com sucesso.");
                            carregarProdutos(); // Recarregar os produtos na tabela
                        } else {
                            javax.swing.JOptionPane.showMessageDialog(this, "Erro ao excluir o produto.");
                        }

                    }
                } catch (SQLException ex) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Erro ao excluir o produto.");
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.");
        }
    }//GEN-LAST:event_excluirProdutoActionPerformed

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

    Sounds sound = new Sounds();

    private void excluirProdutoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_excluirProdutoMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_excluirProdutoMouseClicked

    private void fecharMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fecharMouseClicked
        sound.tocarSom("src/sounds/sair.wav");
    }//GEN-LAST:event_fecharMouseClicked

    private void carregarProdutos() {
        String sql = "SELECT nome, imagem FROM produtos";

        try {
            DefaultTableModel model;
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789"); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                model = (DefaultTableModel) TabelaProdutos.getModel();
                model.setRowCount(0); // Limpa a tabela antes de preencher
                while (rs.next()) {
                    String nome = rs.getString("nome");

                    // Converte o blob da imagem para ImageIcon e redimensiona para 200x200
                    byte[] imgBytes = rs.getBytes("imagem");
                    ImageIcon imagem = new ImageIcon(imgBytes);

                    // Redimensiona a imagem para 200x200 pixels
                    Image img = imagem.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    ImageIcon imgRedimensionada = new ImageIcon(img);

                    // Adiciona a linha à tabela
                    model.addRow(new Object[]{nome, imgRedimensionada});
                }
            }

            // Notifica a tabela que os dados mudaram
            model.fireTableDataChanged();
            // Ajusta a largura da coluna da imagem para 200 pixels
            TabelaProdutos.getColumnModel().getColumn(1).setPreferredWidth(200);
            TabelaProdutos.setRowHeight(200);
        } catch (SQLException ex) {
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
            java.util.logging.Logger.getLogger(excluirPro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new excluirPro().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TabelaProdutos;
    private javax.swing.JButton excluirProduto;
    private javax.swing.JButton fechar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
