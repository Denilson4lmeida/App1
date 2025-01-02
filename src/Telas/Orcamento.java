package Telas;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.text.NumberFormatter;
import sounds.Sounds;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author denyl
 */
public class Orcamento extends javax.swing.JFrame {

    private static final String DATABASE_URL = "jdbc:mysql://localhost/DarckSoftware?user=denny&password=123456789&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
    // Formatadores para data
    private final SimpleDateFormat formatoExibicao = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat formatoBanco = new SimpleDateFormat("yyyy-MM-dd");
    private static Orcamento instancia;
    // Exemplo de campo para armazenar valor total

    public Orcamento() {
        setUndecorated(true);
        initComponents();
        setLocationRelativeTo(null);

        // Máscara de data
        MaskFormatter formatoData = null;
        try {
            formatoData = new MaskFormatter("##/##/####");
        } catch (ParseException ex) {
            Logger.getLogger(Orcamento.class.getName()).log(Level.SEVERE, null, ex);
        }
        data.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatoData));
        //   EXTRA.setColumns(10); // Ajuste o número de colunas conforme necessário
        try {
            calcularTotal();
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Orcamento.class.getName()).log(Level.SEVERE, null, ex);
        }
// Preenche o JComboBox com os dados dos atendentes
        preencherAtendentes();
        attachListeners();
        formatarCampoPreco();
        preencherComboBoxes();

        // Defina o ícone da aplicação
        String nomeJFrame = carregarNomeSalvoDoBancoDeDados();
        ImageIcon icone = carregarIconeSalvoDoBancoDeDados();

        // Definir título e ícone do JFrame
        definirTituloDoJFrame(nomeJFrame);
        if (icone != null) {
            setIconImage(icone.getImage());
        }

    }

    // Método para salvar a data no banco
    public String obterDataParaBanco() {
        try {
            Date dataFormatada = formatoExibicao.parse(data.getText());
            return formatoBanco.format(dataFormatada); // Retorna a data no formato yyyy-MM-dd
        } catch (ParseException e) {
            return null; // Caso ocorra erro na formatação
        }
    }

    // Método para exibir a data no campo
    public void exibirDataFormatada(String dataBanco) {
        try {
            Date dataFormatada = formatoBanco.parse(dataBanco);
            data.setText(formatoExibicao.format(dataFormatada)); // Exibe no formato dd/MM/yyyy
        } catch (ParseException e) {
        }
    }

    public static Orcamento getInstance() {
        if (instancia == null) {
            instancia = new Orcamento();
        }
        return instancia;
    }

    public void atualizarValorTotal(double valorTotal) {
        EXTRA.setText("R$" + valorTotal);
        EXTRA.setValue(valorTotal);  // Atualiza o campo EXTRA
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

    private void salvarDadosNoBanco() {
        String nomeDono = nome_dono.getText();
        String tele = telefone.getText();
        String computadorNome = computador.getText();
        String totalString = calculo.getText().replace(",", ".");
        double total = Double.parseDouble(totalString);

        // Obter os itens selecionados nos comboboxes
        String atendente = (String) ATENDENTE.getSelectedItem();
        String servico1 = (String) jComboBox1.getSelectedItem();
        String servico2 = (String) jComboBox2.getSelectedItem();
        String servico3 = (String) jComboBox3.getSelectedItem();
        String servico4 = (String) jComboBox4.getSelectedItem();
        String servico5 = (String) jComboBox5.getSelectedItem();
        String servico6 = (String) jComboBox6.getSelectedItem();
        String servico7 = (String) jComboBox7.getSelectedItem();
        String servico8 = (String) jComboBox8.getSelectedItem();
        String servico9 = (String) jComboBox9.getSelectedItem();
        String servico10 = (String) jComboBox10.getSelectedItem();
        String servico11 = (String) jComboBox11.getSelectedItem();
        String servico12 = (String) jComboBox12.getSelectedItem();
        String servico13 = (String) jComboBox13.getSelectedItem();
        String servico14 = (String) jComboBox14.getSelectedItem();
        String servico15 = (String) jComboBox15.getSelectedItem();
        String servico16 = (String) jComboBox16.getSelectedItem();
        String servico17 = (String) jComboBox17.getSelectedItem();
        String servico18 = (String) jComboBox18.getSelectedItem();
        String servico19 = (String) jComboBox19.getSelectedItem();
        String servico20 = (String) jComboBox20.getSelectedItem();

        // Obter o estado do JCheckBox
        boolean adcSelecionado = adc.isSelected();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Abrir a conexão
            String url = "jdbc:mysql://localhost/DarckSoftware?user=denny&password=123456789&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
            conn = DriverManager.getConnection(url);

            // Verificar se já existe um registro com o mesmo nome
            String checkSQL = "SELECT * FROM notas WHERE nome_dono = ?";
            stmt = conn.prepareStatement(checkSQL);
            stmt.setString(1, nomeDono);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Se o nome já existir, exibe a mensagem e pergunta se deseja atualizar
                int resposta = JOptionPane.showConfirmDialog(this, "Já existe um registro com o nome " + nomeDono + ". Deseja atualizar?", "Aviso", JOptionPane.YES_NO_OPTION);

                if (resposta == JOptionPane.YES_OPTION) {
                    // Se o usuário confirmar, realiza o UPDATE
                    String updateSQL = "UPDATE notas SET telefone = ?, computador = ?, total = ?, atendente = ?, "
                            + "servico1 = ?, servico2 = ?, servico3 = ?, servico4 = ?, servico5 = ?, servico6 = ?, "
                            + "servico7 = ?, servico8 = ?, servico9 = ?, servico10 = ?, servico11 = ?, servico12 = ?, "
                            + "servico13 = ?, servico14 = ?, servico15 = ?, servico16 = ?, servico17 = ?, servico18 = ?, "
                            + "servico19 = ?, servico20 = ?, adc = ? WHERE nome_dono = ?";

                    stmt = conn.prepareStatement(updateSQL);
                    stmt.setString(1, tele);
                    stmt.setString(2, computadorNome);
                    stmt.setDouble(3, total);
                    stmt.setString(4, atendente);
                    stmt.setString(5, servico1);
                    stmt.setString(6, servico2);
                    stmt.setString(7, servico3);
                    stmt.setString(8, servico4);
                    stmt.setString(9, servico5);
                    stmt.setString(10, servico6);
                    stmt.setString(11, servico7);
                    stmt.setString(12, servico8);
                    stmt.setString(13, servico9);
                    stmt.setString(14, servico10);
                    stmt.setString(15, servico11);
                    stmt.setString(16, servico12);
                    stmt.setString(17, servico13);
                    stmt.setString(18, servico14);
                    stmt.setString(19, servico15);
                    stmt.setString(20, servico16);
                    stmt.setString(21, servico17);
                    stmt.setString(22, servico18);
                    stmt.setString(23, servico19);
                    stmt.setString(24, servico20);
                    stmt.setBoolean(25, adcSelecionado);
                    stmt.setString(26, nomeDono);

                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Registro atualizado com sucesso!");
                }
            } else {
                String insertSQL = "INSERT INTO notas (nome_dono, telefone, data, computador, total, atendente, "
                        + "servico1, servico2, servico3, servico4, servico5, servico6, servico7, "
                        + "servico8, servico9, servico10, servico11, servico12, servico13, "
                        + "servico14, servico15, servico16, servico17, servico18, servico19, "
                        + "servico20, adc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                stmt = conn.prepareStatement(insertSQL);

// Inserir os valores nos parâmetros da instrução SQL
                stmt.setString(1, nomeDono);
                stmt.setString(2, tele);
                stmt.setString(3, obterDataParaBanco()); // Usa o formato yyyy-MM-dd
                stmt.setString(4, computadorNome);
                stmt.setDouble(5, total);
                stmt.setString(6, atendente);
                stmt.setString(7, servico1);
                stmt.setString(8, servico2);
                stmt.setString(9, servico3);
                stmt.setString(10, servico4);
                stmt.setString(11, servico5);
                stmt.setString(12, servico6);
                stmt.setString(13, servico7);
                stmt.setString(14, servico8);
                stmt.setString(15, servico9);
                stmt.setString(16, servico10);
                stmt.setString(17, servico11);
                stmt.setString(18, servico12);
                stmt.setString(19, servico13);
                stmt.setString(20, servico14);
                stmt.setString(21, servico15);
                stmt.setString(22, servico16);
                stmt.setString(23, servico17);
                stmt.setString(24, servico18);
                stmt.setString(25, servico19);
                stmt.setString(26, servico20);
                stmt.setBoolean(27, adcSelecionado); // Definir o valor do JCheckBox

// Executar a instrução SQL
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Dados salvos com sucesso!");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.out.println("Erro ao salvar dados: " + ex);
        } finally {
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
                e.printStackTrace();
            }
        }
    }

    private void formatarCampoPreco() {
        // Define o formato do número
        DecimalFormat formatoNumero = new DecimalFormat("#,##0.00");
        NumberFormatter formatter = new NumberFormatter(formatoNumero);
        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(0.0); // Define o valor mínimo como 0

        // Adiciona o NumberFormatter ao campo de texto
        // Aplica o NumberFormatter ao jTextField1
        EXTRA.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));

        // Adiciona um listener de foco para formatar o texto ao perder o foco
        EXTRA.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // formatarCampo();
            }
        });
    }

    private void limparCampos() {
        nome_dono.setText("");
        data.setText("");
        EXTRA.setText("");
        computador.setText("");
        calculo.setText("");
        telefone.setText("");
        // Limpar comboboxes
        limparComboboxes();
    }

    private void limparComboboxes() {
        @SuppressWarnings("unchecked")
        JComboBox<String>[] comboBoxes = new JComboBox[]{
            jComboBox1, jComboBox2, jComboBox3, jComboBox4, jComboBox5,
            jComboBox6, jComboBox7, jComboBox8, jComboBox9, jComboBox10,
            jComboBox11, jComboBox12, jComboBox13, jComboBox14, jComboBox15,
            jComboBox16, jComboBox17, jComboBox18, jComboBox19, jComboBox20
        };

        for (JComboBox<String> comboBox : comboBoxes) {
            if (comboBox.getItemCount() > 0) {
                comboBox.setSelectedIndex(0);
            }
        }
    }

    private void preencherComboBoxes() {
        @SuppressWarnings("unchecked")
        JComboBox<String>[] comboBoxes = new JComboBox[]{
            jComboBox1, jComboBox2, jComboBox3, jComboBox4, jComboBox5,
            jComboBox6, jComboBox7, jComboBox8, jComboBox9, jComboBox10,
            jComboBox11, jComboBox12, jComboBox13, jComboBox14, jComboBox15,
            jComboBox16, jComboBox17, jComboBox18, jComboBox19, jComboBox20
        };

        for (JComboBox<String> comboBox : comboBoxes) {
            preencherComboBox("servicos", comboBox);
        }
    }

    private void attachListeners() {
        // Adicionar listener para os JCheckBoxes
        CREDITO.addActionListener(e -> {
            try {
                calcularTotal();
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Orcamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        DEBITO.addActionListener(e -> {
            try {
                calcularTotal();
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Orcamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        DINHEIRO.addActionListener(e -> {
            try {
                calcularTotal();
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Orcamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        PIX.addActionListener(e -> {
            try {
                calcularTotal();
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Orcamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Adicionar listener para o JComboBox de PARCELAS
        PARCELAS.addActionListener(e -> {
            try {
                calcularTotal();
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Orcamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Adicionar listener para os JComboBox de serviços
        @SuppressWarnings("unchecked")
        JComboBox<String>[] comboBoxes = new JComboBox[]{
            jComboBox1, jComboBox2, jComboBox3, jComboBox4, jComboBox5,
            jComboBox6, jComboBox7, jComboBox8, jComboBox9, jComboBox10,
            jComboBox11, jComboBox12, jComboBox13, jComboBox14, jComboBox15,
            jComboBox16, jComboBox17, jComboBox18, jComboBox19, jComboBox20
        };

        for (JComboBox<String> comboBox : comboBoxes) {
            comboBox.addActionListener(e -> {
                try {
                    calcularTotal();
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Orcamento.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }

        // Adicionar listener para o campo de data
        data.addActionListener(e -> {
            try {
                calcularTotal();
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Orcamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void preencherComboBox(String tableName, JComboBox<String> comboBox) {
        String url = "jdbc:mysql://localhost/DarckSoftware?user=denny&password=123456789&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";

        try (Connection connection = DriverManager.getConnection(url); PreparedStatement stmt = connection.prepareStatement("SELECT nome FROM " + tableName); ResultSet rs = stmt.executeQuery()) {

            comboBox.removeAllItems(); // Limpa o combobox
            comboBox.addItem(""); // Adiciona uma opção vazia como a primeira opção

            while (rs.next()) {
                comboBox.addItem(rs.getString("nome")); // Adiciona os nomes dos serviços ao combobox
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher JComboBox: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            // Imprime a pilha de erro para ajudar na depuração
        }
    }

    private void preencherAtendentes() {
        Connection connection = null;

        try {
            // Abrir a conexão
            String url = "jdbc:mysql://localhost/DarckSoftware?user=denny&password=123456789&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
            connection = DriverManager.getConnection(url);

            // Consulta SQL para recuperar os nomes dos atendentes
            String sql = "SELECT nome FROM atendentes";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Adiciona os nomes dos atendentes ao JComboBox
            while (rs.next()) {
                String nomeAtendente = rs.getString("nome");
                ATENDENTE.addItem(nomeAtendente);
            }

            // Fecha a conexão
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao recuperar os atendentes: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void calcularTotal() throws IllegalArgumentException, IllegalAccessException {
        double totalBase = 0;

        // Calcular os valores dos serviços selecionados nos JComboBox
        for (int i = 1; i <= 20; i++) {
            try {
                totalBase += calcularValor((JComboBox<String>) this.getClass().getDeclaredField("jComboBox" + i).get(this));
            } catch (NoSuchFieldException | SecurityException ex) {
                Logger.getLogger(Orcamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Calcular o valor extra digitado no campo EXTRA
        double valorExtra = 0;
        try {
            valorExtra = Double.parseDouble(EXTRA.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            valorExtra = 0; // Caso o valor digitado não seja um número, trata como 0
        }
        totalBase += valorExtra;

        // Atualizar o campo de cálculo inicial
        calculo.setText(String.format("%.2f", totalBase));

        // Cálculo das taxas com base no plano de recebimento
        calcularTotalComTaxas(totalBase);
    }

    private void calcularTotalComTaxas(double valorBase) {
        double taxa = 0.0;
        double total = 0.0;

        // Obtém o valor base existente no campo "calculo"
        try {
            double valorExistente = Double.parseDouble(calculo.getText());
            valorBase += valorExistente;
        } catch (NumberFormatException e) {
            calculo.setText("0.00"); // Define como 0.00 se não for um número válido
        }

        if (DEBITO.isSelected()) {
            taxa = 1.99 / 100; // Taxa de 1,99%
            total = valorBase + (valorBase * taxa);
        } else if (CREDITO.isSelected()) {
            if (PARCELAS.getSelectedIndex() == 0) { // À vista
                taxa = 3.19 / 100; // Taxa de 3,19%
                total = valorBase + (valorBase * taxa);
            } else { // Parcelado
                int parcelas = Integer.parseInt((String) PARCELAS.getSelectedItem());
                taxa = 3.79 / 100; // Taxa de 3,79%
                total = valorBase + (valorBase * taxa);

                // Acrescenta o acréscimo mensal por parcela
                double acrescimoMensal = 2.99 / 100;
                total += valorBase * acrescimoMensal * parcelas;
            }
        } else if (PIX.isSelected()) {
            taxa = 0.99 / 100; // Taxa de 0,99%
            total = valorBase + (valorBase * taxa);
        } else if (DINHEIRO.isSelected()) {
            taxa = 0.00 / 100;
            total = valorBase + (valorBase * taxa);
        } else {
            JOptionPane.showMessageDialog(null, "Selecione um método de pagamento.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Formata o resultado para duas casas decimais
        calculo.setText(String.format("%.2f", total));
    }

    public class Result {

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    private double calcularValor(JComboBox<String> comboBox) {
        String selectedService = (String) comboBox.getSelectedItem();
        if (selectedService == null || selectedService.isEmpty()) {
            return 0;
        }

        String url = "jdbc:mysql://localhost/DarckSoftware?user=denny&password=123456789&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";

        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "SELECT preco FROM servicos WHERE nome = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, selectedService);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble("preco");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao calcular valor: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        EXTRA = new javax.swing.JFormattedTextField();
        calculo = new javax.swing.JTextField();
        ATENDENTE = new javax.swing.JComboBox<>();
        computador = new javax.swing.JTextField();
        data = new javax.swing.JFormattedTextField();
        nome_dono = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        telefone = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        jComboBox6 = new javax.swing.JComboBox<>();
        jComboBox7 = new javax.swing.JComboBox<>();
        jComboBox8 = new javax.swing.JComboBox<>();
        jComboBox9 = new javax.swing.JComboBox<>();
        jComboBox10 = new javax.swing.JComboBox<>();
        jComboBox11 = new javax.swing.JComboBox<>();
        jComboBox12 = new javax.swing.JComboBox<>();
        jComboBox13 = new javax.swing.JComboBox<>();
        jComboBox14 = new javax.swing.JComboBox<>();
        jComboBox15 = new javax.swing.JComboBox<>();
        jComboBox16 = new javax.swing.JComboBox<>();
        jComboBox17 = new javax.swing.JComboBox<>();
        jComboBox18 = new javax.swing.JComboBox<>();
        jComboBox19 = new javax.swing.JComboBox<>();
        jComboBox20 = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        adc = new javax.swing.JCheckBox();
        PARCELAS = new javax.swing.JComboBox<>();
        PIX = new javax.swing.JCheckBox();
        CREDITO = new javax.swing.JCheckBox();
        DEBITO = new javax.swing.JCheckBox();
        DINHEIRO = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        salvar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        clean = new javax.swing.JButton();
        Produt = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 51));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel2.setBackground(new java.awt.Color(0, 153, 51));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jLabel1.setBackground(new java.awt.Color(0, 153, 51));
        jLabel1.setFont(new java.awt.Font("Wide Latin", 1, 60)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Painel de orçamento");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(95, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 153), 1, true));

        EXTRA.setEditable(false);
        EXTRA.setBackground(new java.awt.Color(255, 255, 255));
        EXTRA.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 0, 255), new java.awt.Color(255, 0, 255), null, null), "Valor/Peças:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        EXTRA.setForeground(new java.awt.Color(0, 0, 0));
        EXTRA.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        EXTRA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EXTRAKeyPressed(evt);
            }
        });

        calculo.setEditable(false);
        calculo.setBackground(new java.awt.Color(255, 255, 255));
        calculo.setFont(new java.awt.Font("SimSun", 1, 14)); // NOI18N
        calculo.setForeground(new java.awt.Color(0, 0, 0));
        calculo.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 2, true), "Preço Total:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Agency FB", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N

        ATENDENTE.setBackground(new java.awt.Color(255, 255, 255));
        ATENDENTE.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        ATENDENTE.setForeground(new java.awt.Color(0, 0, 0));
        ATENDENTE.setToolTipText("tecnico");
        ATENDENTE.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tecnico:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Light", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        computador.setBackground(new java.awt.Color(255, 255, 255));
        computador.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        computador.setForeground(new java.awt.Color(0, 0, 0));
        computador.setToolTipText("Equipamento");
        computador.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Equipamento:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Light", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        data.setBackground(new java.awt.Color(255, 255, 255));
        data.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Light", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        data.setForeground(new java.awt.Color(0, 0, 0));
        data.setToolTipText("Data");
        data.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        nome_dono.setBackground(new java.awt.Color(255, 255, 255));
        nome_dono.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        nome_dono.setForeground(new java.awt.Color(0, 0, 0));
        nome_dono.setToolTipText("Nome");
        nome_dono.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nome:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Light", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Wide Latin", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Informações");

        telefone.setBackground(new java.awt.Color(255, 255, 255));
        telefone.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        telefone.setForeground(new java.awt.Color(0, 0, 0));
        telefone.setToolTipText("Telefone");
        telefone.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Telefone:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Light", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nome_dono)
                    .addComponent(computador, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(telefone, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(data))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(ATENDENTE, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(EXTRA, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(calculo, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(nome_dono, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(telefone, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                    .addComponent(data))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(computador, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(EXTRA)
                    .addComponent(ATENDENTE, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(calculo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jComboBox1.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox1.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox1.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox1.setToolTipText("Serviços");

        jComboBox2.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox2.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox2.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox2.setToolTipText("Serviços");

        jComboBox3.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox3.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox3.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox3.setToolTipText("Serviços");

        jComboBox4.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox4.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox4.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox4.setToolTipText("Serviços");

        jComboBox5.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox5.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox5.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox5.setToolTipText("Serviços");

        jComboBox6.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox6.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox6.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox6.setToolTipText("Serviços");

        jComboBox7.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox7.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox7.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox7.setToolTipText("Serviços");

        jComboBox8.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox8.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox8.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox8.setToolTipText("Serviços");

        jComboBox9.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox9.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox9.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox9.setToolTipText("Serviços");

        jComboBox10.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox10.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox10.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox10.setToolTipText("Serviços");
        jComboBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox10ActionPerformed(evt);
            }
        });

        jComboBox11.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox11.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox11.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox11.setToolTipText("Serviços");

        jComboBox12.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox12.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox12.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox12.setToolTipText("Serviços");

        jComboBox13.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox13.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox13.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox13.setToolTipText("Serviços");

        jComboBox14.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox14.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox14.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox14.setToolTipText("Serviços");

        jComboBox15.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox15.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox15.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox15.setToolTipText("Serviços");
        jComboBox15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox15ActionPerformed(evt);
            }
        });

        jComboBox16.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox16.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox16.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox16.setToolTipText("Serviços");

        jComboBox17.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox17.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox17.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox17.setToolTipText("Serviços");
        jComboBox17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox17ActionPerformed(evt);
            }
        });

        jComboBox18.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox18.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox18.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox18.setToolTipText("Serviços");

        jComboBox19.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox19.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox19.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox19.setToolTipText("Serviços");

        jComboBox20.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox20.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jComboBox20.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox20.setToolTipText("Serviços");

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        adc.setBackground(new java.awt.Color(0, 0, 0));
        adc.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        adc.setForeground(new java.awt.Color(255, 255, 255));
        adc.setText("Adiçionar a fila de manutenção");
        adc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adcMouseClicked(evt);
            }
        });
        adc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adcActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adc, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        PARCELAS.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        PARCELAS.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 2, true));

        buttonGroup1.add(PIX);
        PIX.setText("PIX");
        PIX.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 255, 0), 2, true));
        PIX.setBorderPainted(true);
        PIX.setBorderPaintedFlat(true);

        buttonGroup1.add(CREDITO);
        CREDITO.setText("CREDITO");
        CREDITO.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 255, 0), 2, true));
        CREDITO.setBorderPainted(true);
        CREDITO.setBorderPaintedFlat(true);

        buttonGroup1.add(DEBITO);
        DEBITO.setText("DEBITO");
        DEBITO.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 255, 0), 2, true));
        DEBITO.setBorderPainted(true);
        DEBITO.setBorderPaintedFlat(true);
        DEBITO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DEBITOActionPerformed(evt);
            }
        });

        buttonGroup1.add(DINHEIRO);
        DINHEIRO.setSelected(true);
        DINHEIRO.setText("DINHEIRO");
        DINHEIRO.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 255, 0), 2, true));
        DINHEIRO.setBorderPainted(true);
        DINHEIRO.setBorderPaintedFlat(true);

        salvar.setBackground(new java.awt.Color(102, 102, 102));
        salvar.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        salvar.setForeground(new java.awt.Color(255, 255, 255));
        salvar.setText("Salvar");
        salvar.setToolTipText("Guardar");
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

        jButton1.setBackground(new java.awt.Color(102, 102, 102));
        jButton1.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Fechar");
        jButton1.setToolTipText("Fechar");
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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(salvar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(salvar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        clean.setBackground(new java.awt.Color(51, 51, 51));
        clean.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        clean.setForeground(new java.awt.Color(255, 255, 255));
        clean.setText("Limpar");
        clean.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cleanMouseClicked(evt);
            }
        });
        clean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleanActionPerformed(evt);
            }
        });

        Produt.setBackground(new java.awt.Color(51, 51, 51));
        Produt.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        Produt.setForeground(new java.awt.Color(255, 255, 255));
        Produt.setText("Atribuir");
        Produt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProdutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox17, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox19, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox20, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(clean, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Produt, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(42, 42, 42)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(DINHEIRO, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(PARCELAS, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(PIX, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(DEBITO, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(CREDITO, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(6, 6, 6))
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jComboBox18, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox20, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Produt, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(clean, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(16, 16, 16))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(DINHEIRO, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(PIX, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(DEBITO, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(19, 19, 19)
                                .addComponent(CREDITO, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PARCELAS, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1305, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void salvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salvarActionPerformed
        // Obter os itens selecionados (talvez a partir de uma interface)
        // Aqui você pode acessar a lista diretamente, se ela já for um campo da classe

        // Chama o método de salvar passando a lista de itens
        salvarDadosNoBanco();
    }//GEN-LAST:event_salvarActionPerformed

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

    private void cleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanActionPerformed
        limparCampos();
    }//GEN-LAST:event_cleanActionPerformed

    private void adcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_adcActionPerformed

    Sounds sound = new Sounds();

    private void adcMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adcMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_adcMouseClicked

    private void cleanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cleanMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_cleanMouseClicked

    private void salvarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salvarMouseClicked
        sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_salvarMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        sound.tocarSom("src/sounds/sair.wav");
    }//GEN-LAST:event_jButton1MouseClicked

    private void DEBITOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DEBITOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DEBITOActionPerformed

    private void jComboBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox10ActionPerformed

    private void jComboBox17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox17ActionPerformed

    private void jComboBox15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox15ActionPerformed


    private void ProdutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProdutActionPerformed
        Produto prod = new Produto();
        prod.setVisible(true);
        dispose();
    }//GEN-LAST:event_ProdutActionPerformed

    private void EXTRAKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EXTRAKeyPressed
        try {
            calcularTotal();
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Orcamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_EXTRAKeyPressed

    public static void main(String args[]) {
        // Configura o look and feel
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Orcamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

       java.awt.EventQueue.invokeLater(() -> {
           new Orcamento().setVisible(true);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ATENDENTE;
    private javax.swing.JCheckBox CREDITO;
    private javax.swing.JCheckBox DEBITO;
    private javax.swing.JCheckBox DINHEIRO;
    private javax.swing.JFormattedTextField EXTRA;
    private javax.swing.JComboBox<String> PARCELAS;
    private javax.swing.JCheckBox PIX;
    private javax.swing.JButton Produt;
    private javax.swing.JCheckBox adc;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField calculo;
    private javax.swing.JButton clean;
    private javax.swing.JTextField computador;
    private javax.swing.JFormattedTextField data;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox10;
    private javax.swing.JComboBox<String> jComboBox11;
    private javax.swing.JComboBox<String> jComboBox12;
    private javax.swing.JComboBox<String> jComboBox13;
    private javax.swing.JComboBox<String> jComboBox14;
    private javax.swing.JComboBox<String> jComboBox15;
    private javax.swing.JComboBox<String> jComboBox16;
    private javax.swing.JComboBox<String> jComboBox17;
    private javax.swing.JComboBox<String> jComboBox18;
    private javax.swing.JComboBox<String> jComboBox19;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox20;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField nome_dono;
    private javax.swing.JButton salvar;
    private javax.swing.JTextField telefone;
    // End of variables declaration//GEN-END:variables

}
