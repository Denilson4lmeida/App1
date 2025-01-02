package Telas;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.NumberFormat;
import java.util.EventObject;
import javax.imageio.ImageIO;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import sounds.Sounds;

/**
 *
 * @author denyl
 */
public class comprar extends javax.swing.JInternalFrame {

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/darcksoftware";
    private final String DATABASE_USER = "denny";
    private final String DATABASE_PASSWORD = "123456789";
    private int selectedRowId = -1;

    public comprar() {
        initComponents();
        carregarTecnicos();
        carregarDadosNaTabela();
        String nomeJFrame = carregarNomeSalvoDoBancoDeDados();
        ImageIcon icone = carregarIconeSalvoDoBancoDeDados();

        // Definir título e ícone do JFrame
        definirTituloDoJFrame(nomeJFrame);
        if (icone != null) {
            setFrameIcon(icone); // Adiciona um ícone à barra de título do JInternalFrame
        }
        // Configura a formatação de moeda para o campo 'valor'
        @SuppressWarnings("deprecation")
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new java.util.Locale("pt", "BR"));
        NumberFormatter currencyFormatter = new NumberFormatter(currencyFormat);
        currencyFormatter.setAllowsInvalid(false);
        currencyFormatter.setMinimum(0.0);

        valor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(currencyFormatter));
        valor.setValue(0.0); // Define um valor inicial para o campo

        list_ferramentas.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            int selectedRow = list_ferramentas.getSelectedRow();
            if (selectedRow != -1) {
                selectedRowId = (Integer) list_ferramentas.getValueAt(selectedRow, 0); // Supondo que o ID está na primeira coluna
            }
        });

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

    private void carregarTecnicos() {
        String query = "SELECT nome FROM atendentes";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nomeTecnico = rs.getString("nome");
                tecnico.addItem(nomeTecnico); // Adiciona o nome ao JComboBox
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar técnicos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarDados() {
        String ferramentaNome = ferrament.getText();
        String tecnicoNome = (String) tecnico.getSelectedItem();
        String descricaoFerramenta = descricao.getText();

        // Remove espaços em branco, sinais de moeda e substitui vírgulas por pontos, se houver
        String valorFerramentaStr = valor.getText().trim()
                .replaceAll("[^\\d,]", "") // Remove caracteres não numéricos, exceto vírgulas
                .replace(",", "."); // Substitui vírgulas por pontos

        System.out.println("Valor lido: " + valorFerramentaStr);

        // Verifica se a string é um número válido
        if (valorFerramentaStr.isEmpty() || !valorFerramentaStr.matches("\\d+(\\.\\d{1,2})?")) {
            JOptionPane.showMessageDialog(this, "Valor inválido: " + valorFerramentaStr, "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return; // Saia do método se o valor for inválido
        }

        try {
            BigDecimal valorFerramenta = new BigDecimal(valorFerramentaStr);

            String estadoFerramenta = comprado.isSelected() ? "Comprado" : "Lista";

            String sql = "INSERT INTO ferramentas (feramenta, descricao, tecnico, estado, preco) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, ferramentaNome);
                stmt.setString(2, descricaoFerramenta);
                stmt.setString(3, tecnicoNome);
                stmt.setString(4, estadoFerramenta);
                stmt.setBigDecimal(5, valorFerramenta);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Dados salvos com sucesso!");

                // Recarrega os dados na JTable
                carregarDadosNaTabela();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                System.out.println(e);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro ao converter valor: " + e.getMessage(), "Erro de Conversão", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        ferrament.setText("");
        descricao.setText("");
        tecnico.setSelectedIndex(0); // Reseta para o primeiro item
        valor.setValue(0.0); // Reseta o valor para 0.0
        buttonGroup1.clearSelection(); // Limpa a seleção dos JRadioButtons
        selectedRowId = -1; // Reseta o ID selecionado
    }

    private void carregarDadosNaTabela() {
        // Cria um modelo de tabela com as colunas desejadas
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID"); // Adicione a coluna ID se ainda não estiver lá
        model.addColumn("Ferramenta");
        model.addColumn("Preço");
        model.addColumn("Status");

        // Conexão com o banco de dados
        String url = "jdbc:mysql://localhost:3306/darcksoftware";
        String user = "denny";
        String password = "123456789";
        String query = "SELECT id, feramenta, preco, estado FROM ferramentas";

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            // Preenche o modelo de tabela com os dados da consulta
            while (rs.next()) {
                int id = rs.getInt("id");
                String ferramenta = rs.getString("feramenta");
                BigDecimal preco = rs.getBigDecimal("preco");
                String status = rs.getString("estado");

                model.addRow(new Object[]{id, ferramenta, preco, status});
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        // Define o modelo de tabela na JTable
        list_ferramentas.setModel(model);

        // Define o comportamento da JTable para não permitir edição
        for (int i = 0; i < list_ferramentas.getColumnCount(); i++) {
            final int column = i;
            list_ferramentas.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(new JTextField()) {
                @Override
                public boolean isCellEditable(EventObject e) {
                    return false; // Desativa a edição
                }
            });
        }

        // Adiciona o ListSelectionListener para a JTable
        list_ferramentas.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            int selectedRow = list_ferramentas.getSelectedRow();
            if (selectedRow != -1) {
                selectedRowId = (Integer) list_ferramentas.getValueAt(selectedRow, 0); // Supondo que o ID está na primeira coluna
            }
        });
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
        lista = new javax.swing.JRadioButton();
        comprado = new javax.swing.JRadioButton();
        save = new javax.swing.JButton();
        upgrade = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ferrament = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        valor = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descricao = new javax.swing.JTextPane();
        tecnico = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        list_ferramentas = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);

        jPanel1.setBackground(new java.awt.Color(0, 153, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Painel", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI Black", 1, 36), new java.awt.Color(255, 255, 255))); // NOI18N

        lista.setBackground(new java.awt.Color(102, 102, 102));
        buttonGroup1.add(lista);
        lista.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        lista.setForeground(new java.awt.Color(255, 255, 255));
        lista.setText("Na lista");
        lista.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaMouseClicked(evt);
            }
        });

        comprado.setBackground(new java.awt.Color(102, 102, 102));
        buttonGroup1.add(comprado);
        comprado.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        comprado.setForeground(new java.awt.Color(255, 255, 255));
        comprado.setText("Comprado");
        comprado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                compradoMouseClicked(evt);
            }
        });

        save.setBackground(new java.awt.Color(102, 102, 102));
        save.setFont(new java.awt.Font("Segoe UI Black", 3, 18)); // NOI18N
        save.setForeground(new java.awt.Color(255, 255, 255));
        save.setText("Salvar");
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

        upgrade.setBackground(new java.awt.Color(102, 102, 102));
        upgrade.setFont(new java.awt.Font("Segoe UI Black", 3, 18)); // NOI18N
        upgrade.setForeground(new java.awt.Color(255, 255, 255));
        upgrade.setText("Upgrade");
        upgrade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                upgradeMouseClicked(evt);
            }
        });
        upgrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upgradeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comprado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lista, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(47, 47, 47)
                .addComponent(save)
                .addGap(26, 26, 26)
                .addComponent(upgrade)
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lista)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comprado))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(upgrade, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 153, 51));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Compras", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI Black", 1, 36), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Segoe UI Light", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Ferramenta e/ou item:");

        ferrament.setBackground(new java.awt.Color(255, 255, 255));
        ferrament.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        ferrament.setForeground(new java.awt.Color(0, 0, 0));

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Segoe UI Light", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Valor a pagar:");

        valor.setBackground(new java.awt.Color(255, 255, 255));
        valor.setForeground(new java.awt.Color(0, 0, 0));
        valor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        valor.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Segoe UI Light", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Descrição item:");

        descricao.setBackground(new java.awt.Color(255, 255, 255));
        descricao.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        descricao.setForeground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(descricao);

        tecnico.setBackground(new java.awt.Color(255, 255, 255));
        tecnico.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        tecnico.setForeground(new java.awt.Color(0, 0, 0));

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Segoe UI Light", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Tecnico:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ferrament)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(valor, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(tecnico, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ferrament, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addGap(12, 12, 12))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lista", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI Black", 1, 36), new java.awt.Color(0, 0, 0))); // NOI18N

        list_ferramentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Imagem", "Ferramenta", "Valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(list_ferramentas);
        if (list_ferramentas.getColumnModel().getColumnCount() > 0) {
            list_ferramentas.getColumnModel().getColumn(0).setResizable(false);
            list_ferramentas.getColumnModel().getColumn(1).setResizable(false);
            list_ferramentas.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed

        salvarDados();
        limparCampos();
    }//GEN-LAST:event_saveActionPerformed

    private void upgradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upgradeActionPerformed
        // Verifica se uma linha foi selecionada
        if (selectedRowId == -1) {
            JOptionPane.showMessageDialog(this, "Nenhuma linha selecionada.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Determina o novo status com base na seleção dos JRadioButtons
        String novoStatus = comprado.isSelected() ? "Comprado" : lista.isSelected() ? "Lista" : null;

        if (novoStatus == null) {
            JOptionPane.showMessageDialog(this, "Selecione um status.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Atualiza o banco de dados
        String url = "jdbc:mysql://localhost:3306/darcksoftware";
        String user = "denny";
        String password = "123456789";
        String query = "UPDATE ferramentas SET estado = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, novoStatus);
            stmt.setInt(2, selectedRowId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Status atualizado com sucesso!");
                carregarDadosNaTabela(); // Recarregar os dados na JTable para refletir a atualização
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar o status.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar status: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_upgradeActionPerformed

    Sounds sound = new Sounds();

    private void upgradeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_upgradeMouseClicked
        sound.tocarSom("src/sounds/upgrade.wav");
    }//GEN-LAST:event_upgradeMouseClicked

    private void saveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_saveMouseClicked

    private void listaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_listaMouseClicked

    private void compradoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_compradoMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_compradoMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton comprado;
    private javax.swing.JTextPane descricao;
    private javax.swing.JTextField ferrament;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable list_ferramentas;
    private javax.swing.JRadioButton lista;
    private javax.swing.JButton save;
    private javax.swing.JComboBox<String> tecnico;
    private javax.swing.JButton upgrade;
    private javax.swing.JFormattedTextField valor;
    // End of variables declaration//GEN-END:variables
}
