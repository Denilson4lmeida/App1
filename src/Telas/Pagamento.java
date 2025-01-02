package Telas;
import java.awt.Image;
import java.sql.*;
import javax.swing.ImageIcon;

public class Pagamento extends javax.swing.JFrame {

   public Pagamento(double valorFinal) {
    setUndecorated(true);
    initComponents();
    setLocationRelativeTo(null);

    // Define o valor total recebido e formata para 2 casas decimais
    valor_total.setText(String.format("%.2f", valorFinal));
    
      // Carregar e exibir o QR code na JLabel
        carregarQRCode();
    }

    private void carregarQRCode() {
        // Sua conexão com o banco de dados
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Substitua com a sua conexão de banco de dados
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789");
            
            // Consulta SQL para pegar a imagem do QR Code
            String sql = "SELECT imagem FROM pagamento_qrcode WHERE id = 1";  // Altere o ID conforme necessário
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                byte[] imagemBytes = rs.getBytes("imagem");
                
                if (imagemBytes != null) {
                    // Converte o byte array em uma imagem
                    ImageIcon icon = new ImageIcon(imagemBytes);
                    
                    // Redimensiona a imagem para o tamanho da JLabel (qrcode)
                    Image imagem = icon.getImage();
                    Image imagemRedimensionada = imagem.getScaledInstance(qrcode.getWidth(), qrcode.getHeight(), Image.SCALE_SMOOTH);
                    
                    // Define a imagem redimensionada na JLabel
                    qrcode.setIcon(new ImageIcon(imagemRedimensionada));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  // Tratar erros adequadamente
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        painel_completo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        qrcode = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        valor_total = new javax.swing.JLabel();
        confirmar_pagamento = new javax.swing.JButton();
        fechar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        painel_completo.setBackground(new java.awt.Color(0, 255, 51));

        jPanel1.setBackground(new java.awt.Color(0, 153, 0));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 255, 102), 2, true));

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Finalizar pagamento");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
        );

        qrcode.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("R$");
        jLabel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        valor_total.setBackground(new java.awt.Color(255, 255, 255));
        valor_total.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        valor_total.setForeground(new java.awt.Color(0, 0, 0));
        valor_total.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        confirmar_pagamento.setBackground(new java.awt.Color(51, 51, 51));
        confirmar_pagamento.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        confirmar_pagamento.setForeground(new java.awt.Color(255, 255, 255));
        confirmar_pagamento.setText("Confirmar pagamento");
        confirmar_pagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmar_pagamentoActionPerformed(evt);
            }
        });

        fechar.setBackground(new java.awt.Color(51, 51, 51));
        fechar.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        fechar.setForeground(new java.awt.Color(255, 255, 255));
        fechar.setText("Cancelar");
        fechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fecharActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout painel_completoLayout = new javax.swing.GroupLayout(painel_completo);
        painel_completo.setLayout(painel_completoLayout);
        painel_completoLayout.setHorizontalGroup(
            painel_completoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painel_completoLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(qrcode, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
            .addGroup(painel_completoLayout.createSequentialGroup()
                .addGap(153, 153, 153)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(valor_total, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(painel_completoLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(confirmar_pagamento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fechar, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        painel_completoLayout.setVerticalGroup(
            painel_completoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painel_completoLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painel_completoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painel_completoLayout.createSequentialGroup()
                        .addGroup(painel_completoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(valor_total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(qrcode, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(confirmar_pagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painel_completoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(fechar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painel_completo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painel_completo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fecharActionPerformed
        dispose();
    }//GEN-LAST:event_fecharActionPerformed

    private void confirmar_pagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmar_pagamentoActionPerformed
   dispose();
    }//GEN-LAST:event_confirmar_pagamentoActionPerformed

    public static void main(String args[], double valorFinal) {
       
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
            java.util.logging.Logger.getLogger(Pagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        


        
        java.awt.EventQueue.invokeLater(() -> {
       new Pagamento(valorFinal).setVisible(true);

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton confirmar_pagamento;
    private javax.swing.JButton fechar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel painel_completo;
    private javax.swing.JLabel qrcode;
    private javax.swing.JLabel valor_total;
    // End of variables declaration//GEN-END:variables
}
