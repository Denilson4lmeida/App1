package Telas;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class ControleDeCaixa extends JFrame {
    private final JLabel totalCaixaLabel;
    private final JLabel subtracaoFilaLabel;
    private final JButton atualizarButton;
    private final JButton sairButton;
    private final JButton temaButton;
    private final JPanel graficoPanel;
    private boolean isDarkTheme = false;

    public ControleDeCaixa() {
        setTitle("Controle de Caixa | VALLE SOFTWARE");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurando o painel principal com BoxLayout
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel para exibir o valor total do caixa
        JPanel painelTotalCaixa = new JPanel();
        painelTotalCaixa.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel labelTotalCaixa = new JLabel("Total do Caixa: ");
        labelTotalCaixa.setFont(new Font("Arial", Font.BOLD, 16));
        totalCaixaLabel = new JLabel("Calculando...");
        totalCaixaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        painelTotalCaixa.add(labelTotalCaixa);
        painelTotalCaixa.add(totalCaixaLabel);

        // Painel para exibir a subtração dos itens na fila de compras
        JPanel painelSubtracaoFila = new JPanel();
        painelSubtracaoFila.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel labelSubtracaoFila = new JLabel("Subtração Fila de Compras: ");
        labelSubtracaoFila.setFont(new Font("Arial", Font.BOLD, 16));
        subtracaoFilaLabel = new JLabel("Calculando...");
        subtracaoFilaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        painelSubtracaoFila.add(labelSubtracaoFila);
        painelSubtracaoFila.add(subtracaoFilaLabel);

        // Painel de ações com botões
        JPanel painelAcoes = new JPanel();
        painelAcoes.setLayout(new FlowLayout(FlowLayout.RIGHT));
        atualizarButton = new JButton("Atualizar");
        atualizarButton.addActionListener((ActionEvent e) -> {
            atualizarValores();
        });
        sairButton = new JButton("Sair");
        sairButton.addActionListener((ActionEvent e) -> {
            dispose();
        });
        temaButton = new JButton("Trocar Tema");
        temaButton.addActionListener((ActionEvent e) -> {
            toggleTheme();
        });
        painelAcoes.add(atualizarButton);
        painelAcoes.add(temaButton);
        painelAcoes.add(sairButton);

        // Painel para gráfico de barras
        graficoPanel = new JPanel();
        graficoPanel.setLayout(new BorderLayout());
        atualizarGrafico(0, 0); // Inicializa com valores zerados

        // Adicionando painéis ao painel principal
        painelPrincipal.add(painelTotalCaixa);
        painelPrincipal.add(painelSubtracaoFila);
        painelPrincipal.add(graficoPanel);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        painelPrincipal.add(painelAcoes);

        // Adicionando o painel principal ao frame
        add(painelPrincipal);

        // Atualização automática dos valores a cada 30 segundos
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                atualizarValores();
            }
        }, 0, 30000); // 30 segundos
    }

    private void atualizarValores() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/darcksoftware", "denny", "123456789")) {
            // Total do caixa
            String queryTotalCaixa = "SELECT SUM(total) FROM historico_servicos";
            double totalCaixa = 0;
            try (PreparedStatement psTotal = connection.prepareStatement(queryTotalCaixa);
                 ResultSet rsTotal = psTotal.executeQuery()) {
                if (rsTotal.next()) {
                    totalCaixa = rsTotal.getDouble(1);
                    totalCaixaLabel.setText(String.format("R$ %.2f", totalCaixa));
                }
            }

            // Subtração fila de compras
            String querySubtracaoFila = "SELECT SUM(preco) FROM ferramentas WHERE estado = 'lista'";
            double subtracaoFila = 0;
            try (PreparedStatement psSubtracao = connection.prepareStatement(querySubtracaoFila);
                 ResultSet rsSubtracao = psSubtracao.executeQuery()) {
                if (rsSubtracao.next()) {
                    subtracaoFila = rsSubtracao.getDouble(1);
                    subtracaoFilaLabel.setText(String.format("R$ %.2f", subtracaoFila));
                }
            }

            // Atualiza o gráfico
            atualizarGrafico(totalCaixa, subtracaoFila);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao acessar o banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarGrafico(double totalCaixa, double subtracaoFila) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(totalCaixa, "Total Caixa", "Total");
        dataset.addValue(subtracaoFila, "Subtração Fila", "Fila");

        JFreeChart barChart = ChartFactory.createBarChart(
                "Controle de Caixa",
                "Categoria",
                "Valor (R$)",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(500, 300));
        graficoPanel.removeAll();
        graficoPanel.add(chartPanel, BorderLayout.CENTER);
        graficoPanel.validate();
    }

    private void applyTheme() {
        if (isDarkTheme) {
            UIManager.put("control", new Color(50, 50, 50));
            UIManager.put("info", new Color(100, 100, 100));
            UIManager.put("nimbusBase", new Color(18, 30, 49));
            UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
            UIManager.put("nimbusDisabledText", new Color(142, 143, 145));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusGreen", new Color(176, 179, 50));
            UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
            UIManager.put("nimbusLightBackground", new Color(50, 50, 50));
            UIManager.put("nimbusOrange", new Color(191, 98, 4));
            UIManager.put("nimbusRed", new Color(169, 46, 34));
            UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
            UIManager.put("nimbusSelectionBackground", new Color(50, 50, 50));
            UIManager.put("text", new Color(230, 230, 230));
        } else {
            UIManager.put("control", new Color(238, 238, 238));
            UIManager.put("info", new Color(242, 242, 189));
            UIManager.put("nimbusBase", new Color(51, 98, 140));
            UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
            UIManager.put("nimbusDisabledText", new Color(142, 143, 145));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusGreen", new Color(176, 179, 50));
            UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
            UIManager.put("nimbusLightBackground", new Color(255, 255, 255));
            UIManager.put("nimbusOrange", new Color(191, 98, 4));
            UIManager.put("nimbusRed", new Color(169, 46, 34));
            UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
            UIManager.put("nimbusSelectionBackground", new Color(57, 105, 138));
            UIManager.put("text", new Color(0, 0, 0));
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
    
      private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        applyTheme();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControleDeCaixa controleDeCaixa = new ControleDeCaixa();
            controleDeCaixa.setVisible(true);
        });
    }
}
