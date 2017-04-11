/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ErCo
 */
public class RenderCotizacion extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setForeground(Color.black);

        if (value instanceof String) {
            String valor = (String) value;

            if (column == 0) {
                if (valor.equals("EDICION")) {
                    cell.setBackground(Color.WHITE);
                } else {
                    if (valor.equals("APROBADO")) {
                        cell.setBackground(new Color(102, 255, 102));
                    } else {
                        if (valor.equals("RECHAZADO")) {
                            cell.setBackground(new Color(255,102,102));
                        }
                    }
                }
            }
        }

        return cell;
    }
}
