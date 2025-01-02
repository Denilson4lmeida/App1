
package Telas;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author denyl
 */
public class LineWrapListCellRenderer extends JLabel implements ListCellRenderer<String> {
    public LineWrapListCellRenderer() {
        setOpaque(true);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        setText("<html>" + value.replaceAll("\n", "<br>") + "</html>");
        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
        return this;
    }
}
