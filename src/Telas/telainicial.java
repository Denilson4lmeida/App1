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
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.Timer;
import sounds.Sounds;

/**
 *
 * @author denyl
 */
public class telainicial extends javax.swing.JFrame {

    private boolean conectado;
    private final String CONNECTED_ICON_PATH = "src/icons/conect.jpeg";
    private final String DISCONNECTED_ICON_PATH = "src/icons/desconect.jpg";
    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/darcksoftware";
    private final String DATABASE_USER = "denny";
    private final String DATABASE_PASSWORD = "123456789";
    private int ultimoIdVerificado = -1; // Armazena o último ID verificado

    public telainicial() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Abre a janela maximizada
        
         configureKeyBindings(); // Configura o KeyBinding para F5
        
        //Verifica a conexão ao iniciar
        verificarConexao();
        configurarIconeConexao();
        // Carregar nome e ícone do banco de dados
        String nomeJFrame = carregarNomeSalvoDoBancoDeDados();
        ImageIcon icone = carregarIconeSalvoDoBancoDeDados();

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
    Sounds sound = new Sounds();

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        orcamento = new javax.swing.JButton();
        recusos = new javax.swing.JButton();
        pendencias = new javax.swing.JButton();
        aComprar = new javax.swing.JButton();
        conexao_com_banco = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        borda = new javax.swing.JDesktopPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        produtos = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        nota = new javax.swing.JMenu();
        atendente = new javax.swing.JMenuItem();
        hidtoricoDEdinheiro = new javax.swing.JMenuItem();
        notinhas = new javax.swing.JMenuItem();
        adcionar = new javax.swing.JMenuItem();
        remoto = new javax.swing.JMenu();
        entrou = new javax.swing.JMenuItem();
        entrega = new javax.swing.JMenuItem();
        pagos = new javax.swing.JMenuItem();
        entrou1 = new javax.swing.JMenuItem();
        delete = new javax.swing.JMenu();
        novo = new javax.swing.JMenuItem();
        excluir = new javax.swing.JMenuItem();
        editar = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        configuracao = new javax.swing.JMenuItem();
        Sobre = new javax.swing.JMenu();
        info = new javax.swing.JMenuItem();
        sair = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 204), new java.awt.Color(153, 153, 153)));

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel2.setForeground(new java.awt.Color(0, 0, 0));

        orcamento.setBackground(new java.awt.Color(102, 102, 102));
        orcamento.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        orcamento.setForeground(new java.awt.Color(255, 255, 255));
        orcamento.setText("Orçamento");
        orcamento.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        orcamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orcamentoActionPerformed(evt);
            }
        });

        recusos.setBackground(new java.awt.Color(102, 102, 102));
        recusos.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        recusos.setForeground(new java.awt.Color(255, 255, 255));
        recusos.setText("Recursos");
        recusos.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        recusos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recusosActionPerformed(evt);
            }
        });

        pendencias.setBackground(new java.awt.Color(102, 102, 102));
        pendencias.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        pendencias.setForeground(new java.awt.Color(255, 255, 255));
        pendencias.setText("Pendentes");
        pendencias.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        pendencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pendenciasActionPerformed(evt);
            }
        });

        aComprar.setBackground(new java.awt.Color(102, 102, 102));
        aComprar.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        aComprar.setForeground(new java.awt.Color(255, 255, 255));
        aComprar.setText("Comprar");
        aComprar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        aComprar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aComprarActionPerformed(evt);
            }
        });

        conexao_com_banco.setBackground(new java.awt.Color(0, 0, 0));
        conexao_com_banco.setForeground(new java.awt.Color(0, 0, 0));
        conexao_com_banco.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createEtchedBorder()));
        conexao_com_banco.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(aComprar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pendencias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(recusos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(orcamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(conexao_com_banco, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
            .addComponent(jSeparator1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(orcamento, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(recusos, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pendencias, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(aComprar, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(conexao_com_banco, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(0, 153, 51));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jTabbedPane1.setBackground(new java.awt.Color(0, 102, 51));
        jTabbedPane1.setForeground(new java.awt.Color(0, 0, 0));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        borda.setBackground(new java.awt.Color(0, 153, 102));
        borda.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 2, true));
        borda.setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);

        javax.swing.GroupLayout bordaLayout = new javax.swing.GroupLayout(borda);
        borda.setLayout(bordaLayout);
        bordaLayout.setHorizontalGroup(
            bordaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 730, Short.MAX_VALUE)
        );
        bordaLayout.setVerticalGroup(
            bordaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 494, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Inicio", borda);

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
        jScrollPane1.setViewportView(produtos);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
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

        jMenuBar1.setBackground(new java.awt.Color(102, 102, 102));
        jMenuBar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jMenuBar1.setForeground(new java.awt.Color(0, 0, 0));
        jMenuBar1.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jMenuBar1.setPreferredSize(new java.awt.Dimension(70, 30));

        nota.setForeground(new java.awt.Color(0, 0, 0));
        nota.setText("Gerenciamento");
        nota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notaActionPerformed(evt);
            }
        });

        atendente.setBackground(new java.awt.Color(255, 255, 255));
        atendente.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        atendente.setForeground(new java.awt.Color(0, 0, 0));
        atendente.setText("Cadastro de funcionarios");
        atendente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                atendenteActionPerformed(evt);
            }
        });
        nota.add(atendente);

        hidtoricoDEdinheiro.setBackground(new java.awt.Color(255, 255, 255));
        hidtoricoDEdinheiro.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        hidtoricoDEdinheiro.setForeground(new java.awt.Color(0, 0, 0));
        hidtoricoDEdinheiro.setText("Controle de caixa");
        hidtoricoDEdinheiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hidtoricoDEdinheiroActionPerformed(evt);
            }
        });
        nota.add(hidtoricoDEdinheiro);

        notinhas.setBackground(new java.awt.Color(255, 255, 255));
        notinhas.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        notinhas.setForeground(new java.awt.Color(0, 0, 0));
        notinhas.setText("Mensagens");
        notinhas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notinhasActionPerformed(evt);
            }
        });
        nota.add(notinhas);

        adcionar.setBackground(new java.awt.Color(255, 255, 255));
        adcionar.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        adcionar.setForeground(new java.awt.Color(0, 0, 0));
        adcionar.setText("CRUD de serviços");
        adcionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adcionarActionPerformed(evt);
            }
        });
        nota.add(adcionar);

        jMenuBar1.add(nota);

        remoto.setText("Controle de serviços");
        remoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remotoActionPerformed(evt);
            }
        });

        entrou.setBackground(new java.awt.Color(255, 255, 255));
        entrou.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        entrou.setForeground(new java.awt.Color(0, 0, 0));
        entrou.setText("Serviços em andamento");
        entrou.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entrouActionPerformed(evt);
            }
        });
        remoto.add(entrou);

        entrega.setBackground(new java.awt.Color(255, 255, 255));
        entrega.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        entrega.setForeground(new java.awt.Color(0, 0, 0));
        entrega.setText("Serviços concluidos");
        entrega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entregaActionPerformed(evt);
            }
        });
        remoto.add(entrega);

        pagos.setBackground(new java.awt.Color(255, 255, 255));
        pagos.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        pagos.setForeground(new java.awt.Color(0, 0, 0));
        pagos.setText("Historico de serviços");
        pagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pagosActionPerformed(evt);
            }
        });
        remoto.add(pagos);

        entrou1.setBackground(new java.awt.Color(255, 255, 255));
        entrou1.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        entrou1.setForeground(new java.awt.Color(0, 0, 0));
        entrou1.setText("Recebimento por site");
        entrou1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entrou1ActionPerformed(evt);
            }
        });
        remoto.add(entrou1);

        jMenuBar1.add(remoto);

        delete.setText("Controle de produtos");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        novo.setBackground(new java.awt.Color(255, 255, 255));
        novo.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        novo.setForeground(new java.awt.Color(0, 0, 0));
        novo.setText("Adicionar produto");
        novo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                novoActionPerformed(evt);
            }
        });
        delete.add(novo);

        excluir.setBackground(new java.awt.Color(255, 255, 255));
        excluir.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        excluir.setForeground(new java.awt.Color(0, 0, 0));
        excluir.setText("Excluir produtos");
        excluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excluirActionPerformed(evt);
            }
        });
        delete.add(excluir);

        editar.setBackground(new java.awt.Color(255, 255, 255));
        editar.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        editar.setForeground(new java.awt.Color(0, 0, 0));
        editar.setText("Editar informações");
        editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarActionPerformed(evt);
            }
        });
        delete.add(editar);

        jMenuBar1.add(delete);

        jMenu4.setText("Configuração");

        configuracao.setBackground(new java.awt.Color(255, 255, 255));
        configuracao.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        configuracao.setForeground(new java.awt.Color(0, 0, 0));
        configuracao.setText("configurações do sistema");
        configuracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configuracaoActionPerformed(evt);
            }
        });
        jMenu4.add(configuracao);

        jMenuBar1.add(jMenu4);

        Sobre.setForeground(new java.awt.Color(0, 0, 0));
        Sobre.setText("Menu");
        Sobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SobreActionPerformed(evt);
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
        Sobre.add(info);

        sair.setBackground(new java.awt.Color(255, 255, 255));
        sair.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        sair.setForeground(new java.awt.Color(0, 0, 0));
        sair.setText("Sair");
        sair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sairActionPerformed(evt);
            }
        });
        Sobre.add(sair);

        jMenuBar1.add(Sobre);

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

    private void atendenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_atendenteActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        atendente atend = new atendente();
        atend.setVisible(true);
    }//GEN-LAST:event_atendenteActionPerformed

    private void notaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notaActionPerformed

    }//GEN-LAST:event_notaActionPerformed

    private void entrouActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entrouActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        Andamento anda = new Andamento();
        anda.setVisible(true);
    }//GEN-LAST:event_entrouActionPerformed

    private void orcamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orcamentoActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        Orcamento tela = new Orcamento();
        tela.setVisible(true);
    }//GEN-LAST:event_orcamentoActionPerformed

    private void pagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pagosActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        historico tela = new historico();
        tela.setVisible(true);

    }//GEN-LAST:event_pagosActionPerformed

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

    private void pendenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pendenciasActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        pendentes tela = new pendentes();
        tela.setVisible(true);
    }//GEN-LAST:event_pendenciasActionPerformed

    private void entregaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entregaActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        concluid com = new concluid();
        borda.add(com);
        com.setVisible(true);

    }//GEN-LAST:event_entregaActionPerformed

    private void adcionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adcionarActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        servico serv = new servico();
        serv.setVisible(true);
    }//GEN-LAST:event_adcionarActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed

    }//GEN-LAST:event_deleteActionPerformed

    private void editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        edit ed = new edit();
        ed.setVisible(true);
    }//GEN-LAST:event_editarActionPerformed

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

    private void configuracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configuracaoActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        config con = new config();
        con.setVisible(true);
    }//GEN-LAST:event_configuracaoActionPerformed

    private void notinhasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notinhasActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        notass not = new notass();
        not.setVisible(true);
    }//GEN-LAST:event_notinhasActionPerformed

    private void SobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SobreActionPerformed
    }//GEN-LAST:event_SobreActionPerformed

    private void infoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        System.out.println("Botão 'Sobre' clicado"); // Mensagem de depuração
        mostrarSobre();
    }//GEN-LAST:event_infoActionPerformed

    private void aComprarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aComprarActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        comprar com = new comprar();
        borda.add(com);
        com.setVisible(true);
    }//GEN-LAST:event_aComprarActionPerformed

    private void recusosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recusosActionPerformed
        sound.tocarSom("src/sounds/money.wav");
        mostrarInformacoes();
    }//GEN-LAST:event_recusosActionPerformed

    private void entrou1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entrou1ActionPerformed
        sound.tocarSom("src/sounds/clique.wav");
        remoto remo = new remoto();
        borda.add(remo);
        remo.setVisible(true);
    }//GEN-LAST:event_entrou1ActionPerformed

    private void remotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remotoActionPerformed


    }//GEN-LAST:event_remotoActionPerformed

    private void hidtoricoDEdinheiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hidtoricoDEdinheiroActionPerformed
      ControleDeCaixa  cont = new ControleDeCaixa();
      cont.setVisible(true);
              
    }//GEN-LAST:event_hidtoricoDEdinheiroActionPerformed

    // Método para mostrar informações separadas por mês
    private void mostrarInformacoes() {
        try {
            Map<String, Double> totaisPorMes = calcularTotaisPorMes();
            double totalFila = calcularTotalFila();

            // Obtém o ano atual e o ano anterior
            int anoAtual = LocalDate.now().getYear();
            int anoAnterior = anoAtual - 1;

            // Obtém o mês atual e o mês anterior
            int mesAtual = LocalDate.now().getMonthValue();
            int mesAnterior = mesAtual == 1 ? 12 : mesAtual - 1;
            int anoMesAnterior = mesAtual == 1 ? anoAnterior : anoAtual;

            // Gera as chaves para o mês atual e o mês anterior
            String chaveMesAtual = String.format("%04d-%02d", anoAtual, mesAtual);
            String chaveMesAnterior = String.format("%04d-%02d", anoMesAnterior, mesAnterior);

            // Recupera os totais dos meses
            double totalMesAtual = totaisPorMes.getOrDefault(chaveMesAtual, 0.0);
            double totalMesAnterior = totaisPorMes.getOrDefault(chaveMesAnterior, 0.0);

            // Cria a mensagem para exibir
            String mensagem = String.format(
                    "Total do Mês Atual (%04d-%02d): R$ %.2f%n"
                    + "Total do Mês Anterior (%04d-%02d): R$ %.2f%n%n"
                    + "Total dos Itens em Fila: R$ %.2f%n"
                    + "Saldo Atual: R$ %.2f",
                    anoAtual, mesAtual, totalMesAtual,
                    anoMesAnterior, mesAnterior, totalMesAnterior,
                    totalFila,
                    (totalMesAtual + totalMesAnterior) - totalFila
            );

            JOptionPane.showMessageDialog(null, mensagem, "Controle de Caixa", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao acessar o banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/darcksoftware";
        String user = "denny";
        String password = "123456789";
        return DriverManager.getConnection(url, user, password);
    }

    // Método para calcular o total do caixa por mês
    private Map<String, Double> calcularTotaisPorMes() throws SQLException {
        String sql = "SELECT YEAR(data) AS ano, MONTH(data) AS mes, SUM(total) AS total_mes "
                + "FROM historico_servicos "
                + "GROUP BY ano, mes "
                + "ORDER BY ano DESC, mes DESC";
        Map<String, Double> totaisPorMes = new LinkedHashMap<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int ano = rs.getInt("ano");
                int mes = rs.getInt("mes");
                double totalMes = rs.getDouble("total_mes");
                String chave = String.format("%04d-%02d", ano, mes);
                totaisPorMes.put(chave, totalMes);
            }
        }

        return totaisPorMes;
    }

    // Método para calcular o total dos itens em fila por mês
    private double calcularTotalFila() throws SQLException {
        String sql = "SELECT SUM(preco) AS total_fila FROM ferramentas WHERE estado = 'fila'";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("total_fila");
            }
            return 0.0;
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
            java.util.logging.Logger.getLogger(telainicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new telainicial().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Sobre;
    private javax.swing.JButton aComprar;
    private javax.swing.JMenuItem adcionar;
    private javax.swing.JMenuItem atendente;
    private javax.swing.JDesktopPane borda;
    private javax.swing.JLabel conexao_com_banco;
    private javax.swing.JMenuItem configuracao;
    private javax.swing.JMenu delete;
    private javax.swing.JMenuItem editar;
    private javax.swing.JMenuItem entrega;
    private javax.swing.JMenuItem entrou;
    private javax.swing.JMenuItem entrou1;
    private javax.swing.JMenuItem excluir;
    private javax.swing.JMenuItem hidtoricoDEdinheiro;
    private javax.swing.JMenuItem info;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenu nota;
    private javax.swing.JMenuItem notinhas;
    private javax.swing.JMenuItem novo;
    private javax.swing.JButton orcamento;
    private javax.swing.JMenuItem pagos;
    private javax.swing.JButton pendencias;
    private javax.swing.JTable produtos;
    private javax.swing.JButton recusos;
    private javax.swing.JMenu remoto;
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
