/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Telas;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {
    private final JSpinner spinner;

    public SpinnerEditor(int min, int max, int step) {
        spinner = new JSpinner(new SpinnerNumberModel(min, min, max, step));
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        spinner.setValue(value); // Define o valor atual do spinner
        return spinner;
    }
}

