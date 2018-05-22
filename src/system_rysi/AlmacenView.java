package system_rysi;

import Controller.DatoComunBL;
import Util.Constantes;
import entity.DatoComunBE;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ErCo
 */
public class AlmacenView extends javax.swing.JPanel {

    //datos de conexion
    private final int id_empresa_index;
    private final String perfil_usuario_index;
    private final String aliasUsuarioIndex;

    //New
    private final DatoComunBL objDatoComunBL = new DatoComunBL();

    //variables globales
    private int crear0_modificar1_almacen = 0;
    private Component ventana;

    public AlmacenView(String user, String password, int id_empresa, int id_usuario, String perfil_usuario, String alias_usuario) {
        id_empresa_index = id_empresa;
        perfil_usuario_index = perfil_usuario;
        aliasUsuarioIndex = alias_usuario;

        System.out.println("\n\nconectando con Compra de materiales");
        initComponents();

        if (perfil_usuario_index.equals("Solo Lectura")) {
            btnNuevo.setVisible(false);
            btnModificar.setVisible(false);
            btnEliminar.setVisible(false);
        }

        System.out.println("Mostrar Tabla Compra de materiales");
        try {
            mostrar_tabla_general();
            MostrarObjetos(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tamaño_de_caja(JTextField caja, KeyEvent evt, int limite) {
        if (caja.getText().length() == limite) {
            evt.consume();
        }
    }

    private void mostrar_tabla_general() throws Exception {
        try {
            List<DatoComunBE> list = objDatoComunBL.ReadDetalle(Constantes.DC_CODTAB_ALMACEN);
            if (list != null) {
                tablaGeneral(list);
                lblTotal.setText("Total: " + list.size() + " registros.");
            } else {
                lblTotal.setText("No se encontraron resultados");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tablaGeneral(List<DatoComunBE> list) {
        try {
            DefaultTableModel tabla = (DefaultTableModel) tabla_general.getModel();
            tabla.setRowCount(0);

            for (DatoComunBE obj : list) {
                Object[] fila = {
                    obj.getIdDatoComun(),
                    obj.getDescripcionCorta(),
                    obj.getValorTexto1()
                };
                tabla.addRow(fila);
            }
            tabla_general.setRowHeight(35);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCajaTextoCrearAlmacen() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDireccion.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mantenimiento_tabla_detalle_salida = new javax.swing.JPopupMenu();
        Eliminar = new javax.swing.JMenuItem();
        dialog_crear_compra = new javax.swing.JDialog();
        norte = new javax.swing.JPanel();
        lbl_titulo = new javax.swing.JLabel();
        Centro = new javax.swing.JPanel();
        txtCodigo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        sur = new javax.swing.JPanel();
        btn_guardar = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btnNuevo = new javax.swing.JButton();
        spNuevo = new javax.swing.JToolBar.Separator();
        btnModificar = new javax.swing.JButton();
        spModificar = new javax.swing.JToolBar.Separator();
        btnEliminar = new javax.swing.JButton();
        spEliminar = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        panel_tabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_general = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        lblTotal = new javax.swing.JLabel();

        Eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar_24_24.png"))); // NOI18N
        Eliminar.setText("Eliminar");
        Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarActionPerformed(evt);
            }
        });
        mantenimiento_tabla_detalle_salida.add(Eliminar);

        dialog_crear_compra.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialog_crear_compra.setTitle("Compra de Materiales");
        dialog_crear_compra.setResizable(false);

        norte.setBackground(new java.awt.Color(0, 110, 204));
        norte.setPreferredSize(new java.awt.Dimension(458, 40));

        lbl_titulo.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_titulo.setForeground(new java.awt.Color(255, 255, 255));
        lbl_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_titulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/almacen.png"))); // NOI18N
        lbl_titulo.setText("Almacen");

        javax.swing.GroupLayout norteLayout = new javax.swing.GroupLayout(norte);
        norte.setLayout(norteLayout);
        norteLayout.setHorizontalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
        );
        norteLayout.setVerticalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_crear_compra.getContentPane().add(norte, java.awt.BorderLayout.NORTH);

        Centro.setBackground(new java.awt.Color(255, 255, 255));

        txtCodigo.setEditable(false);
        txtCodigo.setBackground(new java.awt.Color(204, 204, 204));
        txtCodigo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCodigo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtCodigo.setToolTipText("Ingrese un Codigo para este Material");
        txtCodigo.setOpaque(false);
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 153));
        jLabel12.setText("Código:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 153));
        jLabel13.setText("Nombre:*");

        txtNombre.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNombre.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtNombre.setToolTipText("Ingrese un Codigo para este Material");
        txtNombre.setOpaque(false);
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 51, 153));
        jLabel14.setText("Direccion/Ubicación:");

        txtDireccion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDireccion.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtDireccion.setToolTipText("Ingrese un Codigo para este Material");
        txtDireccion.setOpaque(false);
        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDireccionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDireccionKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout CentroLayout = new javax.swing.GroupLayout(Centro);
        Centro.setLayout(CentroLayout);
        CentroLayout.setHorizontalGroup(
            CentroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CentroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CentroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CentroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombre)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addGroup(CentroLayout.createSequentialGroup()
                        .addComponent(txtCodigo)
                        .addGap(156, 156, 156)))
                .addContainerGap())
        );
        CentroLayout.setVerticalGroup(
            CentroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CentroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CentroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CentroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CentroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(117, Short.MAX_VALUE))
        );

        dialog_crear_compra.getContentPane().add(Centro, java.awt.BorderLayout.CENTER);

        sur.setPreferredSize(new java.awt.Dimension(111, 41));

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_guardar.setText("Guardar");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });
        btn_guardar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btn_guardarKeyReleased(evt);
            }
        });

        btn_cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cancelar.setText("Cancelar");
        btn_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarActionPerformed(evt);
            }
        });
        btn_cancelar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btn_cancelarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout surLayout = new javax.swing.GroupLayout(sur);
        sur.setLayout(surLayout);
        surLayout.setHorizontalGroup(
            surLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, surLayout.createSequentialGroup()
                .addContainerGap(326, Short.MAX_VALUE)
                .addComponent(btn_guardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_cancelar)
                .addContainerGap())
        );
        surLayout.setVerticalGroup(
            surLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(surLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btn_guardar)
                .addComponent(btn_cancelar))
        );

        dialog_crear_compra.getContentPane().add(sur, java.awt.BorderLayout.SOUTH);

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(0, 110, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/almacen.png"))); // NOI18N
        jLabel1.setText("Almacen");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);

        jToolBar1.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar1.setFloatable(false);
        jToolBar1.setPreferredSize(new java.awt.Dimension(13, 55));

        btnNuevo.setBackground(new java.awt.Color(255, 255, 255));
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo_24_24.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setFocusable(false);
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNuevo);
        jToolBar1.add(spNuevo);

        btnModificar.setBackground(new java.awt.Color(255, 255, 255));
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/actualizar_24_24.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.setFocusable(false);
        btnModificar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModificar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnModificar);
        jToolBar1.add(spModificar);

        btnEliminar.setBackground(new java.awt.Color(255, 255, 255));
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar_24_24.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.setFocusable(false);
        btnEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEliminar);
        jToolBar1.add(spEliminar);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        panel_tabla.setBackground(new java.awt.Color(255, 255, 255));
        panel_tabla.setPreferredSize(new java.awt.Dimension(300, 461));

        tabla_general.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Direccion/Ubicación/Referencia"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tabla_general.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabla_generalMouseReleased(evt);
            }
        });
        tabla_general.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tabla_generalKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tabla_general);
        if (tabla_general.getColumnModel().getColumnCount() > 0) {
            tabla_general.getColumnModel().getColumn(0).setResizable(false);
            tabla_general.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabla_general.getColumnModel().getColumn(1).setPreferredWidth(1000);
            tabla_general.getColumnModel().getColumn(2).setPreferredWidth(1000);
        }

        javax.swing.GroupLayout panel_tablaLayout = new javax.swing.GroupLayout(panel_tabla);
        panel_tabla.setLayout(panel_tablaLayout);
        panel_tablaLayout.setHorizontalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
        );
        panel_tablaLayout.setVerticalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
        );

        jPanel5.add(panel_tabla, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setPreferredSize(new java.awt.Dimension(992, 30));

        lblTotal.setBackground(new java.awt.Color(255, 255, 255));
        lblTotal.setForeground(new java.awt.Color(0, 51, 153));
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setToolTipText("");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 528, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel11, java.awt.BorderLayout.SOUTH);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        crear0_modificar1_almacen = 0;
        MostrarObjetos(true);
        try {
            mostrarAlmacen(crear0_modificar1_almacen, 0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        CerrarDialogoCrearAlmacen();
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarActionPerformed
    }//GEN-LAST:event_EliminarActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        crearmodificarAlmacen();
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        int fila = tabla_general.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro.");
        } else {
            try {
                DefaultTableModel tm = (DefaultTableModel) tabla_general.getModel();

                int id = (Integer) tm.getValueAt(fila, 0);
                crear0_modificar1_almacen = 1;
                MostrarObjetos(true);
                mostrarAlmacen(crear0_modificar1_almacen, id);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getStackTrace(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int fila = tabla_general.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro.");
        } else {
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este registro?", "Eliminar", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                DefaultTableModel tm = (DefaultTableModel) tabla_general.getModel();

                DatoComunBE obj = new DatoComunBE();
                obj.setIdDatoComun((Integer) tm.getValueAt(fila, 0));

                try {
                    objDatoComunBL.delete(obj);
                    mostrar_tabla_general();
                    JOptionPane.showMessageDialog(null, "Registro eliminado con éxito.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "No se puede eliminar el registro seleccionado. \n Está siendo usado en otras operaciones.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btn_cancelarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_cancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CerrarDialogoCrearAlmacen();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearAlmacen();
        }
    }//GEN-LAST:event_btn_cancelarKeyReleased

    private void btn_guardarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_guardarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearmodificarAlmacen();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearAlmacen();
        }
    }//GEN-LAST:event_btn_guardarKeyReleased

    private void tabla_generalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_generalKeyReleased

    }//GEN-LAST:event_tabla_generalKeyReleased

    private void tabla_generalMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_generalMouseReleased

    }//GEN-LAST:event_tabla_generalMouseReleased

    private void txtCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyTyped
        JTextField caja = txtCodigo;
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txtCodigoKeyTyped

    private void txtCodigoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearmodificarAlmacen();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearAlmacen();
        }
    }//GEN-LAST:event_txtCodigoKeyReleased

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreKeyReleased

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtDireccionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionKeyReleased

    private void txtDireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Centro;
    private javax.swing.JMenuItem Eliminar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JDialog dialog_crear_compra;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JPopupMenu mantenimiento_tabla_detalle_salida;
    private javax.swing.JPanel norte;
    private javax.swing.JPanel panel_tabla;
    private javax.swing.JToolBar.Separator spEliminar;
    private javax.swing.JToolBar.Separator spModificar;
    private javax.swing.JToolBar.Separator spNuevo;
    private javax.swing.JPanel sur;
    private javax.swing.JTable tabla_general;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables

    private void mostrarAlmacen(int accion, int id_salida) throws Exception {
        if (accion == 0) {
            limpiarCajaTextoCrearAlmacen();
        } else {
            limpiarCajaTextoCrearAlmacen();
            modificarMaterial(id_salida);
        }

        dialog_crear_compra.setSize(564, 284);
        dialog_crear_compra.setLocationRelativeTo(ventana);
        dialog_crear_compra.setModal(true);
        dialog_crear_compra.setVisible(true);
    }

    private void CerrarDialogoCrearAlmacen() {
        limpiarCajaTextoCrearAlmacen();
        crear0_modificar1_almacen = 0;
        MostrarObjetos(true);
        dialog_crear_compra.dispose();
    }

    private DatoComunBE capturarValorAlmacen() {
        DatoComunBE obj = null;

        if (validateFormAlmacen()) {
            obj = new DatoComunBE();

            String idDatoComun = txtCodigo.getText().trim();
            String descripcion = txtNombre.getText().trim();
            String valor = txtDireccion.getText().trim();

            obj.setDescripcionCorta(descripcion);

            if (idDatoComun != null && idDatoComun.length() > 0) {
                obj.setIdDatoComun(Integer.parseInt(idDatoComun));
            }

            if (valor != null && valor.length() > 0) {
                obj.setValorTexto1(valor);
            }

            obj.setId_empresa(id_empresa_index);
            obj.setUsuarioDes(aliasUsuarioIndex);
            obj.setCodigoTabla(Constantes.DC_CODTAB_ALMACEN);
        }

        return obj;
    }

    private void crearmodificarAlmacen() {
        try {
            DatoComunBE obj = capturarValorAlmacen();

            if (obj != null) {
                if (crear0_modificar1_almacen == 0) {
                    if (objDatoComunBL.crear(obj) > 0) {
                        JOptionPane.showMessageDialog(null, "Registro exitoso.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo registrar la operacion.");
                    }
                } else {
                    if (objDatoComunBL.updateAlmacen(obj) > 0) {
                        JOptionPane.showMessageDialog(null, "Actualización exitosa.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo actualizar el registro.");
                    }
                }
                CerrarDialogoCrearAlmacen();
                mostrar_tabla_general();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarMaterial(int id) throws Exception {
        DatoComunBE obj = objDatoComunBL.readId(id);

        if (obj != null) {
            mostrarDatosCajaTexto(obj);
        }
    }

    private void mostrarDatosCajaTexto(DatoComunBE obj) {
        txtCodigo.setText(String.valueOf(obj.getIdDatoComun()));

        if (obj.getDescripcionCorta() != null && obj.getDescripcionCorta().length() > 0) {
            txtNombre.setText(obj.getDescripcionCorta());
        }

        if (obj.getValorTexto1() != null && obj.getValorTexto1().length() > 0) {
            txtDireccion.setText(obj.getValorTexto1());
        }
    }

    private void MostrarObjetos(boolean mostrar) {
        btn_guardar.setVisible(mostrar);
    }

    private String insertarCeros(int idAlmacen) {
        String valor = String.valueOf(idAlmacen);

        while (valor.length() <= 10) {
            valor = "0" + valor;
        }

        return valor;
    }

    private boolean validateFormAlmacen() {
        boolean resp = true;
        String descripcion = txtNombre.getText().trim();

        if (descripcion == null || descripcion.length() == 0) {
            JOptionPane.showMessageDialog(null, "Ingrese el nombre del Almacén");
            return false;
        }

        return resp;
    }

}
