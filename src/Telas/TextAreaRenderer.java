package Telas;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

// Renderizador para células de texto longo usando JTextArea
public class TextAreaRenderer implements TableCellRenderer {

    private JTextArea textArea;

    public TextAreaRenderer() {
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(true);
        textArea.setEditable(false);
        // Define o tamanho mínimo do JTextArea para o caso de não haver texto
        textArea.setMinimumSize(new Dimension(200, textArea.getPreferredSize().height));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        textArea.setText(value != null ? value.toString() : "");

        // Atualiza o tamanho do JTextArea
        textArea.setPreferredSize(new Dimension(200, textArea.getPreferredSize().height));

        // Ajusta a altura do JTextArea para acomodar até 5 linhas de texto
        int lineHeight = textArea.getFontMetrics(textArea.getFont()).getHeight();
        int maxLines = 5;
        int height = lineHeight * maxLines;
        textArea.setPreferredSize(new Dimension(200, height)); // Ajuste a largura conforme necessário

        return textArea;
    }
}

// Renderizador personalizado para ajustar fonte e tamanho de texto
class CustomCellRenderer extends JLabel implements TableCellRenderer {

    private Font customFont;

    public CustomCellRenderer(Font font) {
        this.customFont = font;
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value != null ? value.toString() : "");
        setFont(customFont);

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        
        return this;
    }
}
