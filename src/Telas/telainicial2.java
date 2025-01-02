package Telas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import sounds.Sounds;

/**
 *
 * @author denyl
 */
public class telainicial2 extends javax.swing.JFrame {

    private boolean conectado; // Define o estado da conexão
    // URL do banco de dados, usuário e senha
    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/darcksoftware";
    private final String DATABASE_USER = "denny";
    private final String DATABASE_PASSWORD = "123456789";
    private final String CONNECTED_ICON_PATH = "src/icons/conect.jpeg";
    private final String DISCONNECTED_ICON_PATH = "src/icons/desconect.jpg";
    private int ultimoIdVerificado = -1; // Armazena o último ID verificado

    /**
     * Creates new form telainicial
     */
    public telainicial2() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Abre a janela maximizada

         configureKeyBindings();
        
        verificarConexao(); // Verifica a conexão ao iniciar
        configurarIconeConexao(); // Atualiza o ícone de conexão
        String nomeJFrame = carregarNomeSalvoDoBancoDeDados();
        ImageIcon icone = carregarIconeSalvoDoBancoDeDados();
        loadLatestMensagem();
        // Definir título e ícone do JFrame
        definirTituloDoJFrame(nomeJFrame);
        if (icone != null) {
            setIconImage(icone.getImage());
        }
        ajustarTamanhoLinhasETabela();
        configurarRenderizadores();
        carregarProdutosNaTabela();

        // Carregar o último ID existente no banco de dados
        ultimoIdVerificado = obterUltimoIdNoBancoDeDados();

        // Inicia o Timer para verificar a tabela `externo` a cada 5 segundos
        Timer timer = new Timer(5000, e -> verificarNovosRegistros());
        timer.start();
    }

    private int obterUltimoIdNoBancoDeDados() {
        int ultimoId = -1;
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            String query = "SELECT MAX(id) FROM externo";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    ultimoId = rs.getInt(1); // Armazena o maior ID existente
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar o último ID.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return ultimoId;
    }

      // Método para configurar o KeyBinding
    private void configureKeyBindings() {
        // Obtém o painel raiz da janela (RootPane)
        JRootPane rootPane = this.getRootPane();

        // Mapeamento de entrada para a tecla F5
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F5"), "goToLogin");

        // Ação associada ao evento
        rootPane.getActionMap().put("goToLogin", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voltarParaTelaDeLogin();
            }
        });
    }
     // Método para voltar à tela de login
    private void voltarParaTelaDeLogin() {
        // Fechar a tela atual
        this.dispose();

        // Abrir a tela de login
        login telaLogin = new login();
        telaLogin.setVisible(true);
    }
    
    private void verificarNovosRegistros() {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            String query = "SELECT MAX(id) FROM externo";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    int idAtual = rs.getInt(1);
                    if (idAtual > ultimoIdVerificado) {
                        ultimoIdVerificado = idAtual;
                        mostrarNotificacao(); // Mostra a notificação apenas se há novo registro
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao verificar novos registros.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarNotificacao() {
        // Criar o JDialog
        JDialog notificacaoDialog = new JDialog(this, "Aviso", true);
        notificacaoDialog.setLayout(new BorderLayout());
        notificacaoDialog.setSize(310, 150);
        notificacaoDialog.setLocationRelativeTo(this);

        // Criar painel para o conteúdo da notificação
        JPanel painelNotificacao = new JPanel();
        painelNotificacao.setLayout(new BorderLayout());
        painelNotificacao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelNotificacao.setBackground(new Color(255, 255, 204)); // Cor de fundo suave

        // Adicionar ícone
        JLabel iconeLabel = new JLabel(new ImageIcon("src/icons/aviso-previo.png")); // Substitua pelo caminho do ícone
        painelNotificacao.add(iconeLabel, BorderLayout.WEST);

        // Adicionar mensagem
        JLabel mensagemLabel = new JLabel("Novo registro detectado!");
        mensagemLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mensagemLabel.setForeground(Color.BLACK);
        painelNotificacao.add(mensagemLabel, BorderLayout.CENTER);

        // Adicionar botão de fechamento
        JButton fecharButton = new JButton("Fechar");
        fecharButton.addActionListener((ActionEvent e) -> {
            notificacaoDialog.dispose(); // Fecha o diálogo quando o botão é clicado
        });
        painelNotificacao.add(fecharButton, BorderLayout.SOUTH);

        // Adicionar o painel ao JDialog
        notificacaoDialog.add(painelNotificacao);

        // Exibir o JDialog
        notificacaoDialog.setVisible(true);
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

    private void loadLatestMensagem() {
        System.out.println("Método loadLatestMensagem chamado."); // Mensagem de depuração
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            System.out.println("Conectado ao banco de dados.");
            String sql = "SELECT texto FROM nav ORDER BY id DESC LIMIT 1";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String latestText = resultSet.getString("texto");
                    if (latestText != null && !latestText.trim().isEmpty()) {
                        System.out.println("Texto mais recente: " + latestText); // Mensagem de depuração
                        o_texto_deve_aparecer_aqui.setText(latestText);
                    } else {
                        System.out.println("Texto mais recente é nulo ou vazio.");
                        o_texto_deve_aparecer_aqui.setText("Texto não disponível.");
                    }
                } else {
                    System.out.println("Nenhum registro encontrado.");
                    o_texto_deve_aparecer_aqui.setText("Nenhum texto disponível.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao carregar a mensagem: " + e.getMessage());
        }
    }

    private JDialog dialogSobre;

    private void mostrarSobre() {
        // Cria um painel para o conteúdo
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adiciona uma borda ao painel

        // Adiciona uma etiqueta com o título
        JLabel titulo = new JLabel("Sobre o Aplicativo");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(titulo);
        painel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento

        // Adiciona uma etiqueta com o nome do aplicativo e a versão
        JLabel ino = new JLabel("Nome do Aplicativo: "
                + " VALLE SOFTWARE");
        ino.setFont(new Font("Arial", Font.PLAIN, 14));
        ino.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(ino);

        JLabel versao = new JLabel("Versão: 1.0.0");
        versao.setFont(new Font("Arial", Font.PLAIN, 14));
        versao.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(versao);

        // Adiciona uma etiqueta com o nome do desenvolvedor
        JLabel desenvolvedor = new JLabel("Desenvolvedor: Denilson Almeida");
        desenvolvedor.setFont(new Font("Arial", Font.PLAIN, 14));
        desenvolvedor.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(desenvolvedor);

        // Adiciona uma etiqueta com os direitos autorais
        JLabel copyright = new JLabel("© 2024 VALLE SOFTWARE. Todos os direitos reservados.");
        copyright.setFont(new Font("Arial", Font.PLAIN, 12));
        copyright.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(copyright);

        // Adiciona um espaço fixo antes do botão
        painel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Cria o botão para fechar o diálogo
        JButton fecharBotao = new JButton("Fechar");
        fecharBotao.setFont(new Font("Arial", Font.PLAIN, 14));
        fecharBotao.setPreferredSize(new Dimension(100, 30)); // Ajusta o tamanho do botão
        fecharBotao.addActionListener(e -> {
            if (dialogSobre != null) {
                dialogSobre.dispose(); // Fecha o diálogo quando o botão é clicado
            }
        });
        fecharBotao.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(fecharBotao);

        // Cria o diálogo e define o painel como seu conteúdo
        dialogSobre = new JDialog(this, "Sobre", true);
        dialogSobre.setContentPane(painel);
        dialogSobre.pack();
        dialogSobre.setLocationRelativeTo(this); // Centraliza o diálogo
        dialogSobre.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        orçamento = new javax.swing.JButton();
        conexao_com_banco = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        o_texto_deve_aparecer_aqui = new javax.swing.JTextArea();
        limpar = new javax.swing.JButton();
        pendencias = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jom = new javax.swing.JDesktopPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        produtos = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        andament = new javax.swing.JMenuItem();
        concluid = new javax.swing.JMenuItem();
        history = new javax.swing.JMenuItem();
        entrou1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        novo = new javax.swing.JMenuItem();
        excluir = new javax.swing.JMenuItem();
        editar = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        info = new javax.swing.JMenuItem();
        sair = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153)));

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel2.setForeground(new java.awt.Color(0, 0, 0));

        orçamento.setBackground(new java.awt.Color(102, 102, 102));
        orçamento.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        orçamento.setForeground(new java.awt.Color(255, 255, 255));
        orçamento.setText("Orçamento");
        orçamento.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        orçamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                orçamentoMouseClicked(evt);
            }
        });
        orçamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orçamentoActionPerformed(evt);
            }
        });

        conexao_com_banco.setBackground(new java.awt.Color(0, 0, 0));
        conexao_com_banco.setForeground(new java.awt.Color(0, 0, 0));
        conexao_com_banco.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createEtchedBorder()));

        o_texto_deve_aparecer_aqui.setBackground(new java.awt.Color(0, 0, 0));
        o_texto_deve_aparecer_aqui.setColumns(20);
        o_texto_deve_aparecer_aqui.setFont(new java.awt.Font("Sylfaen", 0, 14)); // NOI18N
        o_texto_deve_aparecer_aqui.setForeground(new java.awt.Color(255, 255, 255));
        o_texto_deve_aparecer_aqui.setLineWrap(true);
        o_texto_deve_aparecer_aqui.setRows(5);
        o_texto_deve_aparecer_aqui.setText("\n");
        o_texto_deve_aparecer_aqui.setWrapStyleWord(true);
        o_texto_deve_aparecer_aqui.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 153, 153), new java.awt.Color(0, 153, 153), new java.awt.Color(204, 0, 153), new java.awt.Color(255, 0, 204)));
        jScrollPane1.setViewportView(o_texto_deve_aparecer_aqui);

        limpar.setBackground(new java.awt.Color(102, 102, 102));
        limpar.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        limpar.setForeground(new java.awt.Color(255, 255, 255));
        limpar.setText("Limpar");
        limpar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        limpar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                limparMouseClicked(evt);
            }
        });
        limpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limparActionPerformed(evt);
            }
        });

        pendencias.setBackground(new java.awt.Color(102, 102, 102));
        pendencias.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        pendencias.setForeground(new java.awt.Color(255, 255, 255));
        pendencias.setText("Pendentes");
        pendencias.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        pendencias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pendenciasMouseClicked(evt);
            }
        });
        pendencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pendenciasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(orçamento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                                        .addComponent(limpar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(conexao_com_banco, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pendencias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pendencias, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(limpar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orçamento, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(conexao_com_banco, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jTabbedPane1.setBackground(new java.awt.Color(0, 102, 51));
        jTabbedPane1.setForeground(new java.awt.Color(0, 0, 0));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jom.setBackground(new java.awt.Color(0, 153, 102));
        jom.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 2, true));
        jom.setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);

        javax.swing.GroupLayout jomLayout = new javax.swing.GroupLayout(jom);
        jom.setLayout(jomLayout);
        jomLayout.setHorizontalGroup(
            jomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 534, Short.MAX_VALUE)
        );
        jomLayout.setVerticalGroup(
            jomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 497, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Inicio", jom);

        jPanel4.setBackground(new java.awt.Color(0, 153, 51));

        produtos = new javax.swing.JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desativa a edição de todas as células
            }
        };
        produtos.setBackground(new java.awt.Color(51, 255, 102));
        produtos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        produtos.setGridColor(new java.awt.Color(0, 204, 153));
        produtos.setUpdateSelectionOnSort(false);
        jScrollPane2.setViewportView(produtos);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Menu", jPanel4);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jMenuBar1.setBackground(new java.awt.Color(51, 51, 51));
        jMenuBar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jMenuBar1.setForeground(new java.awt.Color(0, 0, 0));
        jMenuBar1.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jMenuBar1.setPreferredSize(new java.awt.Dimension(70, 30));

        jMenu2.setText("Controle de serviços");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });

        andament.setBackground(new java.awt.Color(255, 255, 255));
        andament.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        andament.setForeground(new java.awt.Color(0, 0, 0));
        andament.setText("Serviços em andamento");
        andament.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                andamentActionPerformed(evt);
            }
        });
        jMenu2.add(andament);

        concluid.setBackground(new java.awt.Color(255, 255, 255));
        concluid.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        concluid.setForeground(new java.awt.Color(0, 0, 0));
        concluid.setText("Serviços concluidos");
        concluid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                concluidActionPerformed(evt);
            }
        });
        jMenu2.add(concluid);

        history.setBackground(new java.awt.Color(255, 255, 255));
        history.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        history.setForeground(new java.awt.Color(0, 0, 0));
        history.setText("Historico de serviços");
        history.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historyActionPerformed(evt);
            }
        });
        jMenu2.add(history);

        entrou1.setBackground(new java.awt.Color(255, 255, 255));
        entrou1.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        entrou1.setForeground(new java.awt.Color(0, 0, 0));
        entrou1.setText("Recebimento por site");
        entrou1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entrou1ActionPerformed(evt);
            }
        });
        jMenu2.add(entrou1);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Controle de produtos");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });

        novo.setBackground(new java.awt.Color(255, 255, 255));
        novo.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        novo.setForeground(new java.awt.Color(0, 0, 0));
        novo.setText("Adicionar produtos");
        novo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                novoActionPerformed(evt);
            }
        });
        jMenu3.add(novo);

        excluir.setBackground(new java.awt.Color(255, 255, 255));
        excluir.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        excluir.setForeground(new java.awt.Color(0, 0, 0));
        excluir.setText("Excluir produtos");
        excluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excluirActionPerformed(evt);
            }
        });
        jMenu3.add(excluir);

        editar.setBackground(new java.awt.Color(255, 255, 255));
        editar.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        editar.setForeground(new java.awt.Color(0, 0, 0));
        editar.setText("Editar informações");
        editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarActionPerformed(evt);
            }
        });
        jMenu3.add(editar);

        jMenuBar1.add(jMenu3);

        jMenu5.setForeground(new java.awt.Color(0, 0, 0));
        jMenu5.setText("Menu");
        jMenu5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu5MouseClicked(evt);
            }
        });

        info.setBackground(new java.awt.Color(255, 255, 255));
        info.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        info.setForeground(new java.awt.Color(0, 0, 0));
        info.setText("Sobre");
        info.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoActionPerformed(evt);
            }
        });
        jMenu5.add(info);

        sair.setBackground(new java.awt.Color(255, 255, 255));
        sair.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        sair.setForeground(new java.awt.Color(0, 0, 0));
        sair.setText("Sair");
        sair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sairActionPerformed(evt);
            }
        });
        jMenu5.add(sair);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

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

    Sounds sound = new Sounds();

    private void limparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limparActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        clearTable();
    }//GEN-LAST:event_limparActionPerformed

    private void orçamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orçamentoActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        Orcamento tela = new Orcamento();
        tela.setVisible(true);

    }//GEN-LAST:event_orçamentoActionPerformed

    private void historyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historyActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        historico tela = new historico();
        tela.setVisible(true);

    }//GEN-LAST:event_historyActionPerformed

    private void concluidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_concluidActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        concluid com = new concluid();
        jom.add(com);
        com.setVisible(true);

    }//GEN-LAST:event_concluidActionPerformed

    private void novoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_novoActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        adiciona add = new adiciona();
        add.setVisible(true);
    }//GEN-LAST:event_novoActionPerformed

    private void excluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excluirActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        excluirPro pro = new excluirPro();
        pro.setVisible(true);
    }//GEN-LAST:event_excluirActionPerformed

    private void editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarActionPerformed

        sound.tocarSom("src/sounds/clique.wav");
        edit ed = new edit();
        ed.setVisible(true);
    }//GEN-LAST:event_editarActionPerformed

    private void sairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sairActionPerformed
      // Tocar som ao clicar
    sound.tocarSom("src/sounds/sair.wav");

    // Mostrar caixa de diálogo de confirmação
    int resposta = JOptionPane.showConfirmDialog(
        this,
        "Você deseja sair ' Yes ' , tela de login' No' ",
        "Confirmar saída",
        JOptionPane.YES_NO_OPTION
    );

    if (resposta == JOptionPane.YES_OPTION) {
        // Fechar o aplicativo
        System.exit(0);
    } else if (resposta == JOptionPane.NO_OPTION) {
        // Voltar para a tela de login
        // Substitua 'TelaLogin' pela classe da sua tela de login
        login telaLogin = new login();
        telaLogin.setVisible(true);
        this.dispose(); // Fechar a tela atual
    }
    }//GEN-LAST:event_sairActionPerformed

    private void andamentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_andamentActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        Andamento anda = new Andamento();
        anda.setVisible(true);
    }//GEN-LAST:event_andamentActionPerformed

    private void infoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        mostrarSobre();
    }//GEN-LAST:event_infoActionPerformed

    private void entrou1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entrou1ActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        remoto remo = new remoto();
        jom.add(remo);
        remo.setVisible(true);
    }//GEN-LAST:event_entrou1ActionPerformed

    private void pendenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pendenciasActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        pendentes tela = new pendentes();
        tela.setVisible(true);
    }//GEN-LAST:event_pendenciasActionPerformed

    private void pendenciasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pendenciasMouseClicked
      sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_pendenciasMouseClicked

    private void limparMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_limparMouseClicked
         sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_limparMouseClicked

    private void orçamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orçamentoMouseClicked
    sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_orçamentoMouseClicked

    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked
      sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_jMenu2MouseClicked

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
  sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_jMenu3MouseClicked

    private void jMenu5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu5MouseClicked
   sound.tocarSom("src/sounds/clique.wav");
    }//GEN-LAST:event_jMenu5MouseClicked

    private void clearTable() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            String sql = "DELETE FROM nav";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
                System.out.println("Tabela nav limpa com sucesso!");
                // Limpa a área de texto após a exclusão
                o_texto_deve_aparecer_aqui.setText("");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao limpar a tabela: " + e.getMessage());
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
            java.util.logging.Logger.getLogger(telainicial2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new telainicial2().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem andament;
    private javax.swing.JMenuItem concluid;
    private javax.swing.JLabel conexao_com_banco;
    private javax.swing.JMenuItem editar;
    private javax.swing.JMenuItem entrou1;
    private javax.swing.JMenuItem excluir;
    private javax.swing.JMenuItem history;
    private javax.swing.JMenuItem info;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JDesktopPane jom;
    private javax.swing.JButton limpar;
    private javax.swing.JMenuItem novo;
    private javax.swing.JTextArea o_texto_deve_aparecer_aqui;
    private javax.swing.JButton orçamento;
    private javax.swing.JButton pendencias;
    private javax.swing.JTable produtos;
    private javax.swing.JMenuItem sair;
    // End of variables declaration//GEN-END:variables

    private void verificarConexao() {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            if (conn != null) {
                conectado = true;  // Atualiza o estado para conectado
            }
        } catch (SQLException e) {
            conectado = false;  // Atualiza o estado para desconectado

        }
    }

    private void configurarIconeConexao() {
        String iconPath = conectado ? CONNECTED_ICON_PATH : DISCONNECTED_ICON_PATH;
        try {
            // Carrega a imagem do arquivo
            BufferedImage bufferedImage = ImageIO.read(new File(iconPath));

            // Redimensiona a imagem para o tamanho do JLabel
            Image img = bufferedImage.getScaledInstance(conexao_com_banco.getWidth(), conexao_com_banco.getHeight(), Image.SCALE_SMOOTH);

            // Define o ícone redimensionado no JLabel
            conexao_com_banco.setIcon(new ImageIcon(img));
        } catch (IOException e) {

        }
    }

    private void carregarProdutosNaTabela() {
        DefaultTableModel model = (DefaultTableModel) produtos.getModel();
        // Definindo as colunas na ordem desejada
        model.setColumnIdentifiers(new Object[]{"Imagem", "Nome", "Preço", "Descrição"});

        model.setRowCount(0); // Limpa a tabela antes de adicionar os novos dados

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Conexão com o banco de dados
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DarckSoftware", "denny", "123456789");

            // Consulta SQL para obter os produtos, sem o id
            String sql = "SELECT nome, descricao, preco, imagem FROM produtos";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            // Processar os resultados
            while (rs.next()) {
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                double preco = rs.getDouble("preco");
                byte[] imagemBytes = rs.getBytes("imagem");

                // Formata o preço com "R$" e no padrão brasileiro
                String precoFormatado = String.format("R$ %.2f", preco);

                // Redimensionar a imagem
                ImageIcon imagemIcone = new ImageIcon(new ImageIcon(imagemBytes).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));

                // Adiciona os dados na tabela na nova ordem
                model.addRow(new Object[]{imagemIcone, nome, precoFormatado, descricao});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao acessar o banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
            }
        }
        // Após carregar os produtos, configure os renderizadores
        configurarRenderizadores();
    }

    private void ajustarTamanhoLinhasETabela() {
        produtos.setRowHeight(200); // Define a altura da linha para 200 pixels

        // Ajusta a largura de todas as colunas para 200 pixels
        TableColumnModel columnModel = produtos.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(200); // 200 pixels de largura para cada coluna
        }
    }

    private void configurarRenderizadores() {
        TableColumnModel columnModel = produtos.getColumnModel();

        // Fonte personalizada para renderização
        Font customFont = new Font("SansSerif", Font.BOLD, 16); // Ajuste conforme necessário

        if (columnModel.getColumnCount() > 0) {
            // Aplica o renderizador para a coluna de imagem (coluna 0)
            produtos.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = new JLabel();
                    label.setHorizontalAlignment(JLabel.CENTER);
                    if (value != null) {
                        label.setIcon((Icon) value);
                    }
                    label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
                    return label;
                }
            });
        }

        if (columnModel.getColumnCount() > 1) {
            // Aplica o renderizador personalizado para a coluna "Nome" (coluna 1)
            columnModel.getColumn(1).setCellRenderer(new CustomCellRenderer(customFont));
        }

        if (columnModel.getColumnCount() > 2) {
            // Aplica o renderizador personalizado para a coluna "Preço" (coluna 2)
            columnModel.getColumn(2).setCellRenderer(new CustomCellRenderer(customFont));
        }

        if (columnModel.getColumnCount() > 3) {
            // Aplica o renderizador de texto longo para a coluna "Descrição" (coluna 3)
            columnModel.getColumn(3).setCellRenderer(new TextAreaRenderer());
        }
        // Adiciona bordas para todas as células
        produtos.setShowGrid(true); // Exibe linhas e colunas
        produtos.setGridColor(Color.BLACK); // Cor das linhas de grade

        // Ajusta o espaçamento entre as células para mostrar apenas linhas horizontais
        produtos.setIntercellSpacing(new Dimension(0, 1)); // Espaçamento entre linhas
    }

}
