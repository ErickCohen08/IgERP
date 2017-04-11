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
public class RenderFactura extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setForeground(Color.black);

        if (value instanceof String) {
            String valor = (String) value;

            if (column == 0) {

                if (valor.equals("POR VENCER")) {
                    cell.setBackground(new Color(255,255,102));
                } else {
                    if (valor.equals("VENCIDO")) {
                        cell.setBackground(new Color(255,102,102));
                        
                    } else {
                        if (valor.equals("COBRADO")) {
                            cell.setBackground(new Color(91,155,213));
                        } else {
                            if (valor.equals("ANULADO")) {
                                cell.setBackground(new Color(204,204,204));
                            } else {
                                if (valor.equals("POR COBRAR")) {
                                    cell.setBackground(Color.WHITE);
                                }
                            }
                        }
                    }
                }
            }
        }

        return cell;
    }
}
