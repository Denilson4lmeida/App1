/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Telas;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.sql.*;

public final class Produto extends javax.swing.JFrame implements ActionListener {
// Referência para a interface Orcamento

    private Orcamento orcamento;
    private static final String DATABASE_URL = "jdbc:mysql://localhost/DarckSoftware?user=denny&password=123456789&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
    private boolean nomeDonoSolicitado = false; // Variável de controle

    public Produto() {
        setUndecorated(true);
        initComponents();
        setLocationRelativeTo(null);
        // Defina o ícone da aplicação
        String nomeJFrame = carregarNomeSalvoDoBancoDeDados();
        ImageIcon icone = carregarIconeSalvoDoBancoDeDados();
        orcamento = Orcamento.getInstance();
        carregarTabela();

        // Definir título e ícone do JFrame
        definirTituloDoJFrame(nomeJFrame);
        if (icone != null) {
            setIconImage(icone.getImage());
        }

        inserir_ao_CLIENTE_INTERFACE_ORCAMENTO.addActionListener(this);
    }

    public void setOrcamento(Orcamento orcamento) {
    }

    public void carregarTabela() {
        String[] colunas = {"Imagem", "Nome", "Descrição", "Preço", "Selecionar", "Quantidade"};
        DefaultTableModel modelo = new DefaultTableModel(null, colunas) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 ->
                        ImageIcon.class; // Coluna da imagem
                    case 1 ->
                        String.class;     // Nome
                    case 2 ->
                        String.class;     // Descrição
                    case 3 ->
                        Double.class;     // Preço
                    case 4 ->
                        Boolean.class;    // Checkbox
                    case 5 ->
                        Integer.class;    // Quantidade
                    default ->
                        String.class;
                };
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5; // Coluna checkbox e quantidade são editáveis
            }
        };

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/DarckSoftware?user=denny&password=123456789&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC"); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT imagem, nome, descricao, preco FROM produtos")) {

            while (rs.next()) {
                // Carregar a imagem do banco de dados
                byte[] imgBytes = rs.getBytes("imagem");
                ImageIcon imagem = null;
                if (imgBytes != null) {
                    imagem = new ImageIcon(new ImageIcon(imgBytes).getImage().getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH));
                }

                // Carregar as outras colunas
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                double preco = rs.getDouble("preco");

                // Adicionar uma nova linha ao modelo
                modelo.addRow(new Object[]{imagem, nome, descricao, preco, false, 1}); // Quantidade inicial = 1
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar produtos: " + e.getMessage());
        }

        produtos.setModel(modelo);
        modelo.addTableModelListener(e -> atualizarValorTotal());
        int larguraImagem = 170;
        int alturaImagem = 170;

        produtos.getColumnModel().getColumn(0).setCellRenderer(new ImagemRenderer(larguraImagem, alturaImagem));

        produtos.getColumnModel().getColumn(2).setCellRenderer(new JTextAreaRenderer());
        produtos.getColumnModel().getColumn(3).setCellRenderer(new CenteredCurrencyRenderer());

        // Ajustar o tamanho das colunas
        produtos.getColumnModel().getColumn(0).setPreferredWidth(170); // Imagem
        produtos.getColumnModel().getColumn(1).setPreferredWidth(170); // Nome
        produtos.getColumnModel().getColumn(2).setPreferredWidth(300); // Descrição
        produtos.getColumnModel().getColumn(3).setPreferredWidth(120); // Preço
        produtos.getColumnModel().getColumn(4).setPreferredWidth(80);  // Selecionar
        produtos.getColumnModel().getColumn(5).setPreferredWidth(80);  // Quantidade

        // Configurar um editor para a coluna de quantidade
        produtos.getColumnModel().getColumn(5).setCellEditor(new SpinnerEditor(1, 100, 1));

        produtos.setRowHeight(150);
        atualizarValorTotal();
    }

    public JTable getProdutos() {
        return produtos;
    }

    // Método para carregar o nome do aplicativo do banco de dados
    private String carregarNomeSalvoDoBancoDeDados() {
        String nome = "Nome Padrão"; // Nome padrão caso não consiga carregar
        try (Connection conn = DriverManager.getConnection(DATABASE_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT nome FROM empresa WHERE id = 1")) {

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
        try (Connection conn = DriverManager.getConnection(DATABASE_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT imagem FROM empresa WHERE id = 1")) {

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
        jScrollPane1 = new javax.swing.JScrollPane();
        produtos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        inserir_ao_CLIENTE_INTERFACE_ORCAMENTO = new javax.swing.JButton();
        VOLTAR = new javax.swing.JButton();
        Valor_total = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 0));

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));

        produtos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(produtos);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1055, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 70)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Anexação");

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(102, 255, 102), null, new java.awt.Color(102, 255, 102)));

        inserir_ao_CLIENTE_INTERFACE_ORCAMENTO.setBackground(new java.awt.Color(51, 51, 51));
        inserir_ao_CLIENTE_INTERFACE_ORCAMENTO.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        inserir_ao_CLIENTE_INTERFACE_ORCAMENTO.setForeground(new java.awt.Color(255, 255, 255));
        inserir_ao_CLIENTE_INTERFACE_ORCAMENTO.setText("Inserir");
        inserir_ao_CLIENTE_INTERFACE_ORCAMENTO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inserir_ao_CLIENTE_INTERFACE_ORCAMENTOActionPerformed(evt);
            }
        });

        VOLTAR.setBackground(new java.awt.Color(51, 51, 51));
        VOLTAR.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        VOLTAR.setForeground(new java.awt.Color(255, 255, 255));
        VOLTAR.setText("Fechar");
        VOLTAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VOLTARActionPerformed(evt);
            }
        });

        Valor_total.setBackground(new java.awt.Color(0, 0, 0));
        Valor_total.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Valor_total.setForeground(new java.awt.Color(255, 255, 255));
        Valor_total.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        Valor_total.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(VOLTAR, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(inserir_ao_CLIENTE_INTERFACE_ORCAMENTO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(Valor_total, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(inserir_ao_CLIENTE_INTERFACE_ORCAMENTO, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(VOLTAR, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(Valor_total, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 872, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

    private void VOLTARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VOLTARActionPerformed
        dispose();
    }//GEN-LAST:event_VOLTARActionPerformed

    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        // Verifica qual botão foi clicado
        if (evt.getSource() == inserir_ao_CLIENTE_INTERFACE_ORCAMENTO) {
            inserir_ao_CLIENTE_INTERFACE_ORCAMENTOActionPerformed(evt);
        }
    }

    private void inserir_ao_CLIENTE_INTERFACE_ORCAMENTOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inserir_ao_CLIENTE_INTERFACE_ORCAMENTOActionPerformed
        // Verificar se o nome já foi solicitado
        if (nomeDonoSolicitado) {
            return; // Se já foi solicitado, não faz nada
        }

        // Solicita o nome do dono
        String nomeDono = JOptionPane.showInputDialog(this, "Digite o nome do dono:", "Nome do Dono", JOptionPane.PLAIN_MESSAGE);

        if (nomeDono == null || nomeDono.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome do dono é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            return; // Não prossegue sem o nome do dono
        }

        // Marcar que o nome já foi solicitado
        nomeDonoSolicitado = true;

        // Variável para calcular o valor total
        double valorTotal = 0.0;

        // Itera sobre as linhas da tabela
        DefaultTableModel modelo = (DefaultTableModel) produtos.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Boolean selecionado = (Boolean) modelo.getValueAt(i, 4);
            if (Boolean.TRUE.equals(selecionado)) {
                // Obtem os dados da linha
                String nomeProduto = (String) modelo.getValueAt(i, 1);
                Double preco = (Double) modelo.getValueAt(i, 3);
                Integer quantidade = (Integer) modelo.getValueAt(i, 5);

                // Soma ao valor total
                valorTotal += preco * quantidade;

                // Para depuração (opcional)
                System.out.println("Nome: " + nomeProduto + ", Preço: " + preco + ", Quantidade: " + quantidade);
            }
        }

        // Verifica se algum item foi selecionado
        if (valorTotal > 0) {
            // Atualiza a interface de orçamento
            Orcamento.getInstance().setVisible(true);
            Orcamento.getInstance().atualizarValorTotal(valorTotal);
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum produto foi selecionado.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        inserirNoBancoDeDados(nomeDono, modelo);
        dispose();
    }//GEN-LAST:event_inserir_ao_CLIENTE_INTERFACE_ORCAMENTOActionPerformed

    public static void main(String args[]) {

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
            java.util.logging.Logger.getLogger(Produto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>
        java.awt.EventQueue.invokeLater(() -> {
            new Produto().setVisible(true);
        });
    }

    public void inserirNoBancoDeDados(String nomeDono, DefaultTableModel modelo) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/darcksoftware", "denny", "123456789")) {
            // Verifica se já existe uma nota para o nome do dono
            String selectNotaSQL = "SELECT id FROM notas WHERE nome_dono = ?";
            int notaId = -1;

            try (PreparedStatement stmt = conn.prepareStatement(selectNotaSQL)) {
                stmt.setString(1, nomeDono);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    // Se a nota existir, recupera o id
                    notaId = rs.getInt("id");
                }
            }

            // Se a nota não existir, cria uma nova
            if (notaId == -1) {
                String insertNotaSQL = "INSERT INTO notas (nome_dono, telefone, data, computador, atendente, total, servico1, servico2, servico3, servico4, servico5, servico6, servico7, servico8, servico9, servico10, servico11, servico12, servico13, servico14, servico15, servico16, servico17, servico18, servico19, servico20, adc) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

                try (PreparedStatement stmt = conn.prepareStatement(insertNotaSQL, Statement.RETURN_GENERATED_KEYS)) {
                    // Preenchendo os valores
                    stmt.setString(1, nomeDono); // Exemplo: "Erika Santos"
                    stmt.setString(2, ""); // Exemplo: "+5511919162364"
                    stmt.setDate(3, new java.sql.Date(System.currentTimeMillis())); // data
                    stmt.setString(4, ""); // Exemplo: "Dell Inspiron 15"
                    stmt.setString(5, ""); // Exemplo: "Admin"
                    stmt.setDouble(6, 0.0); // Exemplo: 114.98

// Preenchendo os serviços. Caso não haja um serviço, use null.
                    stmt.setString(7, "");  // Exemplo: "UPGRADE"
                    stmt.setString(8, "");  // Exemplo: "DIAGNOSTICO DE HARDWARE"
                    stmt.setString(9, "");  // Repita até o servico20
                    stmt.setString(10, "");
                    stmt.setString(11, "");
                    stmt.setString(12, "");
                    stmt.setString(13, "");
                    stmt.setString(14, "");
                    stmt.setString(15, "");
                    stmt.setString(16, "");
                    stmt.setString(17, "");
                    stmt.setString(18, "");
                    stmt.setString(19, "");
                    stmt.setString(20, "");
                    stmt.setString(21, "");
                    stmt.setString(22, "");
                    stmt.setString(23, "");
                    stmt.setString(24, "");
                    stmt.setString(25, "");
                    stmt.setString(26, "");

// Valor adicional (adc)
                    stmt.setInt(27, 0); // Exemplo: 1
                    stmt.executeUpdate();

                    // Obtendo o ID gerado automaticamente
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            notaId = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("Erro ao obter o ID da nota.");
                        }
                    }
                }
            }

            // Agora vamos inserir os produtos na tabela nota_produtos
            String insertProdutoSQL = "INSERT INTO nota_produtos (nome_dono, produto_nome, preco, quantidade) VALUES (?, ?, ?, ?)";
            String checkProdutoSQL = "SELECT * FROM nota_produtos WHERE nome_dono = ? AND produto_nome = ?"; // Check duplicata

            try (PreparedStatement stmtCheck = conn.prepareStatement(checkProdutoSQL); PreparedStatement stmtInsert = conn.prepareStatement(insertProdutoSQL)) {
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    boolean selecionado = (Boolean) modelo.getValueAt(i, 4); // Verifica se o checkbox está marcado
                    if (selecionado) {
                        String produtoNome = (String) modelo.getValueAt(i, 1); // Nome do produto
                        double preco = (Double) modelo.getValueAt(i, 3); // Preço do produto
                        int quantidade = (Integer) modelo.getValueAt(i, 5); // Quantidade

                        // Verifica se os valores estão corretos
                        if (produtoNome != null && preco >= 0 && quantidade >= 0) {
                            // Verificar se o produto já foi inserido para este dono
                            stmtCheck.setString(1, nomeDono);
                            stmtCheck.setString(2, produtoNome);
                            ResultSet rsCheck = stmtCheck.executeQuery();

                            if (!rsCheck.next()) { // Se não houver duplicação
                                stmtInsert.setString(1, nomeDono);  // Nome do dono
                                stmtInsert.setString(2, produtoNome);  // Nome do produto
                                stmtInsert.setDouble(3, preco);  // Preço
                                stmtInsert.setInt(4, quantidade);  // Quantidade
                                stmtInsert.addBatch();  // Adiciona ao batch
                            }
                        }
                    }
                }

                // Executar todas as inserções de uma vez
                stmtInsert.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao salvar produtos: " + e.getMessage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar nota: " + e.getMessage());
        }
    }

    private void atualizarValorTotal() {
        double total = 0.0;

        // Iterar sobre as linhas da tabela
        for (int i = 0; i < produtos.getRowCount(); i++) {
            boolean selecionado = (Boolean) produtos.getValueAt(i, 4); // Verificar se a coluna 'Selecionar' está marcada
            if (selecionado) {
                double preco = (Double) produtos.getValueAt(i, 3);     // Obter o preço
                int quantidade = (Integer) produtos.getValueAt(i, 5); // Obter a quantidade
                total += preco * quantidade;                          // Adicionar ao total
            }
        }

        // Atualizar o JLabel com o valor formatado
        Valor_total.setText(String.format("Total: R$ %.2f", total));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton VOLTAR;
    private javax.swing.JLabel Valor_total;
    private javax.swing.JButton inserir_ao_CLIENTE_INTERFACE_ORCAMENTO;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable produtos;
    // End of variables declaration//GEN-END:variables

// Renderizador para JTextArea
    static class JTextAreaRenderer extends JTextArea implements TableCellRenderer {

        public JTextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setFont(table.getFont());
            return this;
        }
    }

    // Renderizador para preços centralizados e formatados
    static class CenteredCurrencyRenderer extends DefaultTableCellRenderer {

        private final NumberFormat currencyFormat;

        @SuppressWarnings("deprecation")
        public CenteredCurrencyRenderer() {
            currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            setHorizontalAlignment(CENTER);
            setFont(new Font("SansSerif", Font.BOLD, 20)); // Fonte maior
        }

        @Override
        protected void setValue(Object value) {
            if (value instanceof Number) {
                setText(currencyFormat.format(value));
            } else {
                setText("");
            }
        }
    }

    // Renderizador de imagens personalizado
    public class ImagemRenderer extends DefaultTableCellRenderer {

        private final int largura;
        private final int altura;

        public ImagemRenderer(int largura, int altura) {
            this.largura = largura;
            this.altura = altura;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageIcon imagem) {
                Image img = imagem.getImage();
                Image imgRedimensionada = img.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(imgRedimensionada));
            } else {
                setIcon(null);
            }
            setHorizontalAlignment(CENTER);
            return this;
        }
    }

}
