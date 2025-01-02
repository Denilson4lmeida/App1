package Telas;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.*;
import sounds.Sounds;

/**
 *
 * @author denyl
 */
public class atendente extends javax.swing.JFrame {

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/darcksoftware";
    private final String DATABASE_USER = "denny";
    private final String DATABASE_PASSWORD = "123456789";

    /**
     * Creates new form atendente
     */
    public atendente() {
        setUndecorated(true);
        initComponents();
        setLocationRelativeTo(null);
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
        String n = "Nome Padrão"; // Nome padrão caso não consiga carregar
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT nome FROM empresa WHERE id = 1")) {

            if (rs.next()) {
                n = rs.getString("nome");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar o nome do banco de dados: " + e.getMessage());
        }
        return n;
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        nome = new javax.swing.JTextField();
        cargo = new javax.swing.JTextField();
        SENHA = new javax.swing.JPasswordField();
        DATAHOJE = new javax.swing.JTextField();
        empresa = new javax.swing.JTextField();
        imagemaqui = new javax.swing.JLabel();
        arquivo_sistema = new javax.swing.JButton();
        funcionario = new javax.swing.JRadioButton();
        admin = new javax.swing.JRadioButton();
        save = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 51));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));

        jPanel2.setBackground(new java.awt.Color(0, 102, 51));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(0, 102, 51));
        jLabel1.setFont(new java.awt.Font("Wide Latin", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Inserir funcionario ");
        jLabel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel3.setBackground(new java.awt.Color(0, 102, 51));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 2, true));

        nome.setBackground(new java.awt.Color(255, 255, 255));
        nome.setFont(new java.awt.Font("Century", 0, 18)); // NOI18N
        nome.setForeground(new java.awt.Color(0, 0, 0));
        nome.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nome:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Light", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        cargo.setBackground(new java.awt.Color(255, 255, 255));
        cargo.setFont(new java.awt.Font("Century", 0, 18)); // NOI18N
        cargo.setForeground(new java.awt.Color(0, 0, 0));
        cargo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cargo:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Light", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        SENHA.setBackground(new java.awt.Color(255, 255, 255));
        SENHA.setFont(new java.awt.Font("Century", 0, 18)); // NOI18N
        SENHA.setForeground(new java.awt.Color(0, 0, 0));
        SENHA.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Senha:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Light", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        DATAHOJE.setBackground(new java.awt.Color(255, 255, 255));
        DATAHOJE.setFont(new java.awt.Font("Century", 0, 18)); // NOI18N
        DATAHOJE.setForeground(new java.awt.Color(0, 0, 0));
        DATAHOJE.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Light", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        DATAHOJE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DATAHOJEActionPerformed(evt);
            }
        });

        empresa.setBackground(new java.awt.Color(255, 255, 255));
        empresa.setFont(new java.awt.Font("Century", 0, 18)); // NOI18N
        empresa.setForeground(new java.awt.Color(0, 0, 0));
        empresa.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Empresa:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Light", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        imagemaqui.setBackground(new java.awt.Color(0, 102, 51));
        imagemaqui.setForeground(new java.awt.Color(0, 153, 51));
        imagemaqui.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 3));

        arquivo_sistema.setBackground(new java.awt.Color(102, 102, 102));
        arquivo_sistema.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        arquivo_sistema.setForeground(new java.awt.Color(255, 255, 255));
        arquivo_sistema.setText("ARQUIVOS");
        arquivo_sistema.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                arquivo_sistemaMouseClicked(evt);
            }
        });
        arquivo_sistema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arquivo_sistemaActionPerformed(evt);
            }
        });

        funcionario.setBackground(new java.awt.Color(0, 0, 0));
        buttonGroup1.add(funcionario);
        funcionario.setFont(new java.awt.Font("Agency FB", 1, 21)); // NOI18N
        funcionario.setForeground(new java.awt.Color(255, 255, 255));
        funcionario.setSelected(true);
        funcionario.setText("Funcionário");
        funcionario.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 255), 2, true));
        funcionario.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        funcionario.setIconTextGap(32);
        funcionario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                funcionarioMouseClicked(evt);
            }
        });

        admin.setBackground(new java.awt.Color(0, 0, 0));
        buttonGroup1.add(admin);
        admin.setFont(new java.awt.Font("Agency FB", 1, 21)); // NOI18N
        admin.setForeground(new java.awt.Color(255, 255, 255));
        admin.setText("Administrador");
        admin.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 255), 2, true));
        admin.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        admin.setIconTextGap(20);
        admin.setInheritsPopupMenu(true);
        admin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adminMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nome)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cargo, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                                    .addComponent(SENHA)
                                    .addComponent(DATAHOJE)
                                    .addComponent(empresa))
                                .addGap(36, 36, 36)
                                .addComponent(imagemaqui, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(funcionario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(admin, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 159, Short.MAX_VALUE)
                        .addComponent(arquivo_sistema, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(110, 110, 110))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(cargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(SENHA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(DATAHOJE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(empresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(imagemaqui, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(admin, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(arquivo_sistema, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(funcionario)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        save.setBackground(new java.awt.Color(153, 153, 153));
        save.setFont(new java.awt.Font("Agency FB", 1, 36)); // NOI18N
        save.setForeground(new java.awt.Color(0, 0, 0));
        save.setText("ADD");
        save.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 51)));
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

        jButton1.setBackground(new java.awt.Color(153, 153, 153));
        jButton1.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("Fechar");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 102)));
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(save, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))))
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

    private void DATAHOJEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DATAHOJEActionPerformed
        // Cria uma instância de Date com a data e hora atuais
        Date hoje = new Date();

        // Define o formato desejado para a data
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); // Exemplo: 24/07/2024

        // Formata a data atual de acordo com o formato especificado
        String dataFormatada = formato.format(hoje);

        // Define o texto do campo com a data formatada
        DATAHOJE.setText(dataFormatada); // Substitua 'DATAHOJE' pelo nome real do seu campo
    }//GEN-LAST:event_DATAHOJEActionPerformed

    private void arquivo_sistemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arquivo_sistemaActionPerformed
        // Cria uma instância de JFileChooser
        JFileChooser fileChooser = new JFileChooser();

        // Define o filtro para arquivos de imagem
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos de Imagem", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        // Mostra o seletor de arquivos e aguarda a seleção
        int returnValue = fileChooser.showOpenDialog(null);

        // Verifica se o usuário selecionou um arquivo
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // Atualiza o JLabel com a imagem selecionada
            exibirImagem(selectedFile);
        }
    }//GEN-LAST:event_arquivo_sistemaActionPerformed


    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        String nomeAtendente = nome.getText();
        String cargoAtendente = cargo.getText();
        String senhaAtendente = new String(SENHA.getPassword());
        String empresaAtendente = empresa.getText();
        String tipoAtendente;

        if (nomeAtendente.isEmpty() || cargoAtendente.isEmpty() || senhaAtendente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos corretamente.");
            return;
        }

        if (admin.isSelected()) {
            tipoAtendente = "Administrador";
        } else {
            tipoAtendente = "Funcionario";
        }

        // Obtém a data atual formatada
        String dataAtual = DATAHOJE.getText();

        // Obtém a imagem do JLabel como um ImageIcon
        ImageIcon imageIcon = (ImageIcon) imagemaqui.getIcon();
        byte[] imagemBytes = null;

        if (imageIcon != null) {
            try {
                // Converte o ImageIcon em um array de bytes
                BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.drawImage(imageIcon.getImage(), 0, 0, null);
                g2d.dispose();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpg", baos);
                imagemBytes = baos.toByteArray();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao converter imagem: " + e.getMessage());
                return;
            }
        }

        Connection connection = null;

        try {
            // Abrir a conexão
            String url = "jdbc:mysql://localhost/DarckSoftware?user=denny&password=123456789&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
            connection = DriverManager.getConnection(url);

            // Insere o atendente no banco de dados
            String sql = "INSERT INTO atendentes (foto, dia, nome, cargo, senha, empresa, tipo) VALUES (?, ?, ?, ?, ?, ?,?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBytes(1, imagemBytes); // Define o valor da imagem
            stmt.setString(2, dataAtual);  // Define o valor da data
            stmt.setString(3, nomeAtendente);
            stmt.setString(4, cargoAtendente);
            stmt.setString(5, senhaAtendente);
            stmt.setString(6, empresaAtendente);
            stmt.setString(7, tipoAtendente);
            stmt.executeUpdate();

            // Fecha a conexão
            connection.close();

            JOptionPane.showMessageDialog(this, "Atendente adicionado com sucesso.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar atendente: " + e.getMessage());
        }
    }//GEN-LAST:event_saveActionPerformed

    Sounds sound = new Sounds();

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

    private void arquivo_sistemaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_arquivo_sistemaMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_arquivo_sistemaMouseClicked

    private void adminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adminMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_adminMouseClicked

    private void funcionarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_funcionarioMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_funcionarioMouseClicked

    private void saveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_saveMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        sound.tocarSom("src/sounds/sair.wav");
    }//GEN-LAST:event_jButton1MouseClicked

    private void exibirImagem(File file) {
        try {
            // Cria um ImageIcon a partir do arquivo
            ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());

            // Redimensiona a imagem se necessário
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(imagemaqui.getWidth(), imagemaqui.getHeight(), Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(scaledImage);

            // Define o ImageIcon no JLabel
            imagemaqui.setIcon(imageIcon);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar a imagem.");
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
            java.util.logging.Logger.getLogger(atendente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new atendente().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DATAHOJE;
    private javax.swing.JPasswordField SENHA;
    private javax.swing.JRadioButton admin;
    private javax.swing.JButton arquivo_sistema;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField cargo;
    private javax.swing.JTextField empresa;
    private javax.swing.JRadioButton funcionario;
    private javax.swing.JLabel imagemaqui;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField nome;
    private javax.swing.JButton save;
    // End of variables declaration//GEN-END:variables
}
