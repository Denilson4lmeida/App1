package Telas;

import java.awt.Color;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import sounds.Sounds;

/**
 *
 * @author denyl
 */
public class login extends javax.swing.JFrame {

    private Timer timer;
    private int progresso;
    private final String url = "jdbc:mysql://localhost:3306/DarckSoftware?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
    private final String usuario = "denny";
    private final String senha = "123456789";

    /**
     * Creates new form NewJFram
     */
    public login() {
        initComponents();
        setLocationRelativeTo(null);
        preencherComboBox();
        inicializarBarraProgresso();
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
        try (Connection conn = DriverManager.getConnection(url, usuario, senha); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT nome FROM empresa WHERE id = 1")) {

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
        try (Connection conn = DriverManager.getConnection(url, usuario, senha); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT imagem FROM empresa WHERE id = 1")) {

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
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        LOGIN = new javax.swing.JComboBox<>();
        SENHA = new javax.swing.JPasswordField();
        jPanel4 = new javax.swing.JPanel();
        VERIFY = new javax.swing.JButton();
        ACESSAR = new javax.swing.JButton();
        VERIFICAR = new javax.swing.JProgressBar();
        fechar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 51));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));

        jPanel2.setBackground(new java.awt.Color(0, 153, 51));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Wide Latin", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("VERIFICAÇÃO ");

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Wide Latin", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText(" DE LOGIN");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(231, 231, 231)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(0, 153, 51));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel3.setForeground(new java.awt.Color(0, 0, 0));

        LOGIN.setBackground(new java.awt.Color(255, 255, 255));
        LOGIN.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        LOGIN.setForeground(new java.awt.Color(0, 0, 0));
        LOGIN.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SELECIONE SEU LOGIN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Black", 1, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        LOGIN.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                LOGINMouseWheelMoved(evt);
            }
        });

        SENHA.setBackground(new java.awt.Color(255, 255, 255));
        SENHA.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        SENHA.setForeground(new java.awt.Color(0, 0, 0));
        SENHA.setFocusCycleRoot(true);
        SENHA.setFocusTraversalPolicyProvider(true);
        SENHA.setOpaque(true);
        SENHA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SENHAActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(SENHA)
                    .addComponent(LOGIN, 0, 435, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(LOGIN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(SENHA, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 153, 51));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel4.setForeground(new java.awt.Color(0, 0, 0));

        VERIFY.setBackground(new java.awt.Color(102, 102, 102));
        VERIFY.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        VERIFY.setForeground(new java.awt.Color(255, 255, 255));
        VERIFY.setText("VERIFICAR ");
        VERIFY.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VERIFYMouseClicked(evt);
            }
        });
        VERIFY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VERIFYActionPerformed(evt);
            }
        });

        ACESSAR.setBackground(new java.awt.Color(102, 102, 102));
        ACESSAR.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        ACESSAR.setForeground(new java.awt.Color(255, 255, 255));
        ACESSAR.setText("ENTRAR");
        ACESSAR.setEnabled(false);
        ACESSAR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ACESSARMouseClicked(evt);
            }
        });
        ACESSAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ACESSARActionPerformed(evt);
            }
        });

        VERIFICAR.setBackground(new java.awt.Color(0, 0, 0));
        VERIFICAR.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        VERIFICAR.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        VERIFICAR.setStringPainted(true);

        fechar.setBackground(new java.awt.Color(102, 102, 102));
        fechar.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(VERIFY, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(VERIFICAR, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ACESSAR)
                .addGap(27, 27, 27)
                .addComponent(fechar, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 17, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(fechar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(VERIFICAR, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(VERIFY, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ACESSAR, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void SENHAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SENHAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SENHAActionPerformed

    private void VERIFYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VERIFYActionPerformed

        verificarCredenciais();
    }//GEN-LAST:event_VERIFYActionPerformed

    private void ACESSARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ACESSARActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        // Verifica se o botão "Acessar" está habilitado
        if (ACESSAR.isEnabled()) {
            String usuarioSelecionado = LOGIN.getSelectedItem().toString();
            String senhaDigitada = new String(SENHA.getPassword());
            String tipoUsuario = realizarVerificacao(usuarioSelecionado, senhaDigitada);
            if (tipoUsuario.equalsIgnoreCase("Funcionario")) {
                new telainicial2().setVisible(true);
            } else if (tipoUsuario.equalsIgnoreCase("Administrador")) {
                new telainicial().setVisible(true);
            }
            dispose();
        }
    }//GEN-LAST:event_ACESSARActionPerformed

    Sounds sound = new Sounds();

    private void fecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fecharActionPerformed
        // Tocar o som primeiro
        sound.tocarSom("src/sounds/sair.wav");

        // Adicionar um atraso antes de fechar
        try {
            Thread.sleep(1000); // 1 segundo de atraso, ajuste conforme necessário
        } catch (InterruptedException e) {
        }
        System.exit(0);
    }//GEN-LAST:event_fecharActionPerformed

    private void VERIFYMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VERIFYMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_VERIFYMouseClicked

    private void ACESSARMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ACESSARMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_ACESSARMouseClicked

    private void fecharMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fecharMouseClicked
        sound.tocarSom("src/sounds/sair.wav");
    }//GEN-LAST:event_fecharMouseClicked

    private void LOGINMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_LOGINMouseWheelMoved
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_LOGINMouseWheelMoved

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
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new login().setVisible(true);
        });
    }

    private Connection conectarBancoDados() throws SQLException {
        return DriverManager.getConnection(url, usuario, senha);
    }

    private void preencherComboBox() {
        try (Connection conexao = conectarBancoDados(); PreparedStatement stmt = conexao.prepareStatement("SELECT nome FROM atendentes"); ResultSet rs = stmt.executeQuery()) {

            DefaultComboBoxModel<String> modeloComboBox = new DefaultComboBoxModel<>();
            while (rs.next()) {
                modeloComboBox.addElement(rs.getString("nome"));
            }
            LOGIN.setModel(modeloComboBox);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao preencher combobox: " + e.getMessage());
        }
    }

    private String realizarVerificacao(String usuario, String senha) {
        String tipoUsuario = null; // Inicializamos tipoUsuario com null
        String r = "jdbc:mysql://localhost/DarckSoftware?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";

        try {
            try (Connection conexao = DriverManager.getConnection(r, this.usuario, this.senha)) {
                String sql = "SELECT * FROM atendentes WHERE nome = ? AND senha = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, usuario);
                stmt.setString(2, senha);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    tipoUsuario = rs.getString("tipo"); // Se encontramos um usuário, obtemos o tipo
                    // Se for Funcionário ou Administrador, habilita o botão "Acessar"
                    if (tipoUsuario.equalsIgnoreCase("Funcionario") || tipoUsuario.equalsIgnoreCase("Administrador")) {
                        VERIFY.setBackground(Color.GREEN);
                        VERIFY.repaint(); // Atualiza a interface
                        ACESSAR.setEnabled(true);
                    } else {
                        // Outro tipo de usuário não suportado
                        System.out.println("Tipo de usuário não suportado: " + tipoUsuario);
                    }
                } else {
                    // Usuário e/ou senha não correspondem no banco de dados
                    VERIFY.setBackground(Color.RED);
                    VERIFY.repaint(); // Atualiza a interface
                    ACESSAR.setEnabled(false);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar credenciais: " + e.getMessage());
        }

        return tipoUsuario; // Retornamos o tipo de usuário encontrado (ou null)
    }

    private void inicializarBarraProgresso() {
        progresso = 0;
        VERIFICAR.setValue(progresso);
        VERIFICAR.setVisible(false);
    }

    private void verificarCredenciais() {
        String usuarioSelecionado = LOGIN.getSelectedItem().toString();
        String senhaDigitada = new String(SENHA.getPassword());

        // Verifica se usuário e senha estão preenchidos
        if (usuarioSelecionado.isEmpty() || senhaDigitada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
            return;
        }

        // Inicia a barra de progresso
        progresso = 0;
        VERIFICAR.setValue(progresso);
        VERIFICAR.setVisible(true);

        // Simula a verificação de credenciais com uma barra de progresso
        timer = new Timer(100, (java.awt.event.ActionEvent evt) -> {
            progresso += 10;
            VERIFICAR.setValue(progresso);

            if (progresso >= 100) {
                timer.stop();
                realizarVerificacao(usuarioSelecionado, senhaDigitada);
                VERIFICAR.setVisible(false);
            }
        });

        timer.start();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ACESSAR;
    private javax.swing.JComboBox<String> LOGIN;
    private javax.swing.JPasswordField SENHA;
    private javax.swing.JProgressBar VERIFICAR;
    private javax.swing.JButton VERIFY;
    private javax.swing.JButton fechar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    // End of variables declaration//GEN-END:variables
}
