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
public class Render_CuentaBanco extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setForeground(Color.black);

        if (value instanceof String) {
            String valor = (String) value;

            if (column == 0) {
                if (valor.equals("ACTIVO")) {
                    cell.setBackground(Color.WHITE);
                } else {
                    if (valor.equals("INACTIVO")) {
                        cell.setBackground(new Color(255,153,51));
                    }
                }
            }
        }

        return cell;
    }
}
