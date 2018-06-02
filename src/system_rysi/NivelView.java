package system_rysi;

import Controller.DatoComunBL;
import Controller.NivelBL;
import Controller.StandBL;
import entity.DatoComunBE;
import entity.NivelBE;
import entity.StandBE;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author ErC
 */
public class NivelView extends javax.swing.JPanel {

    //datos de conexion
    private final int id_empresa_index;
    private String perfil_usuario_index = "";
    private final String aliasUsuarioIndex;

    //New
    private final NivelBL objNivelBL = new NivelBL();
    private final StandBL objStandBL = new StandBL();
    private final DatoComunBL objDatoComunBL = new DatoComunBL();

    //variables globales
    private final boolean limpiarCboFiltros = true;
    private int crear0_modificar1_producto = 0;
    private int id_producto_global;
    private Component producto;

    public NivelView(int id_empresa, String perfil_usuario, String alias_usuario) {
        id_empresa_index = id_empresa;
        perfil_usuario_index = perfil_usuario;
        aliasUsuarioIndex = alias_usuario;

        System.out.println("\n\nconectando con Nivel");
        initComponents();

        if (perfil_usuario_index.equals("Solo Lectura")) {
            btn_nuevo.setVisible(false);
            btn_modificar.setVisible(false);
            btn_eliminar.setVisible(false);
        }

        System.out.println("Mostrar Tabla Stand");
        mostrarTablaNivel();
        MostrarObjetos(false);
    }

    private void tamaño_de_caja(JTextField caja, KeyEvent evt, int limite) {
        if (caja.getText().length() == limite) {
            evt.consume();
        }
    }

    //Mostrar tablas
    private void mostrarTablaNivel() {
        try {
            List<NivelBE> list = objNivelBL.read(null);
            if (list != null) {
                tablaNivel(list);
                lblTotal.setText("Total: " + list.size() + " registros.");
            } else {
                lblTotal.setText("No se encontraron resultados");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tablaNivel(List<NivelBE> list) {
        try {
            DefaultTableModel tabla = (DefaultTableModel) tabla_general.getModel();
            tabla.setRowCount(0);
            for (NivelBE obj : list) {
                Object[] fila = {
                    obj.getIdNivel(),
                    obj.getNombreAlmacen(),
                    obj.getNombreStand(),
                    obj.getNombre()
                };
                tabla.addRow(fila);
            }

            tabla_general.setRowHeight(35);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Limpiar
    private void limpiar_caja_texto_crear_material() {
        //Material
        txtCodigo.setText("");
        txtNombre.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogCrearStand = new javax.swing.JDialog();
        norte = new javax.swing.JPanel();
        lbl_titulo = new javax.swing.JLabel();
        centro = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        cboAlmacen = new javax.swing.JComboBox();
        jLabel47 = new javax.swing.JLabel();
        cboStand = new javax.swing.JComboBox();
        sur = new javax.swing.JPanel();
        btn_cancelar = new javax.swing.JButton();
        btn_guardar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btn_nuevo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btn_modificar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btn_eliminar = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        panel_tabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_general = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        lblTotal = new javax.swing.JLabel();

        dialogCrearStand.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialogCrearStand.setResizable(false);

        norte.setBackground(new java.awt.Color(0, 110, 204));
        norte.setPreferredSize(new java.awt.Dimension(458, 40));

        lbl_titulo.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_titulo.setForeground(new java.awt.Color(255, 255, 255));
        lbl_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_titulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nivel.png"))); // NOI18N
        lbl_titulo.setText("Nivel");

        javax.swing.GroupLayout norteLayout = new javax.swing.GroupLayout(norte);
        norte.setLayout(norteLayout);
        norteLayout.setHorizontalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
        );
        norteLayout.setVerticalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialogCrearStand.getContentPane().add(norte, java.awt.BorderLayout.NORTH);

        centro.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 153));
        jLabel12.setText("Código:");

        txtCodigo.setEditable(false);
        txtCodigo.setBackground(new java.awt.Color(204, 204, 204));
        txtCodigo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
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

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 51, 153));
        jLabel11.setText("Nivel:*");

        txtNombre.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNombre.setToolTipText("Ingrese el Nombre del Material");
        txtNombre.setOpaque(false);
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(0, 51, 153));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel46.setText("Almacen:*");

        cboAlmacen.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboAlmacen.setToolTipText("Seleccione el Tipo o Familia al que pertenece el Material.");
        cboAlmacen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboAlmacenItemStateChanged(evt);
            }
        });
        cboAlmacen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAlmacenActionPerformed(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(0, 51, 153));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel47.setText("Stand:*");

        cboStand.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboStand.setToolTipText("Seleccione el Tipo o Familia al que pertenece el Material.");
        cboStand.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboStandItemStateChanged(evt);
            }
        });
        cboStand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStandActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout centroLayout = new javax.swing.GroupLayout(centro);
        centro.setLayout(centroLayout);
        centroLayout.setHorizontalGroup(
            centroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(centroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(centroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(centroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(centroLayout.createSequentialGroup()
                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 243, Short.MAX_VALUE))
                    .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cboAlmacen, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboStand, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        centroLayout.setVerticalGroup(
            centroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(centroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(centroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCodigo)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(centroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(centroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboStand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(centroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(84, Short.MAX_VALUE))
        );

        dialogCrearStand.getContentPane().add(centro, java.awt.BorderLayout.CENTER);

        sur.setPreferredSize(new java.awt.Dimension(458, 40));

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

        javax.swing.GroupLayout surLayout = new javax.swing.GroupLayout(sur);
        sur.setLayout(surLayout);
        surLayout.setHorizontalGroup(
            surLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, surLayout.createSequentialGroup()
                .addContainerGap(315, Short.MAX_VALUE)
                .addComponent(btn_guardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar)
                .addContainerGap())
        );
        surLayout.setVerticalGroup(
            surLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(surLayout.createSequentialGroup()
                .addGroup(surLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cancelar)
                    .addComponent(btn_guardar))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        dialogCrearStand.getContentPane().add(sur, java.awt.BorderLayout.SOUTH);

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(0, 110, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nivel.png"))); // NOI18N
        jLabel1.setText("Nivel");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);

        jToolBar1.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar1.setFloatable(false);
        jToolBar1.setPreferredSize(new java.awt.Dimension(13, 55));

        btn_nuevo.setBackground(new java.awt.Color(255, 255, 255));
        btn_nuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo_24_24.png"))); // NOI18N
        btn_nuevo.setText("Nuevo");
        btn_nuevo.setFocusable(false);
        btn_nuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_nuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_nuevo);
        jToolBar1.add(jSeparator1);

        btn_modificar.setBackground(new java.awt.Color(255, 255, 255));
        btn_modificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/actualizar_24_24.png"))); // NOI18N
        btn_modificar.setText("Modificar");
        btn_modificar.setFocusable(false);
        btn_modificar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_modificar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modificarActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_modificar);
        jToolBar1.add(jSeparator2);

        btn_eliminar.setBackground(new java.awt.Color(255, 255, 255));
        btn_eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar_24_24.png"))); // NOI18N
        btn_eliminar.setText("Eliminar");
        btn_eliminar.setFocusable(false);
        btn_eliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_eliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_eliminar);
        jToolBar1.add(jSeparator3);

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
                "IdNivel", "Almacen", "Stand", "Nnivel"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
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
            tabla_general.getColumnModel().getColumn(3).setPreferredWidth(1000);
        }

        javax.swing.GroupLayout panel_tablaLayout = new javax.swing.GroupLayout(panel_tabla);
        panel_tabla.setLayout(panel_tablaLayout);
        panel_tablaLayout.setHorizontalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
        );
        panel_tablaLayout.setVerticalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
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
            .addGap(0, 764, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE))
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

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        crear0_modificar1_producto = 0;
        id_producto_global = 0;
        MostrarObjetos(true);
        mostrarMaterial(crear0_modificar1_producto, 0);
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        CerrarDialogoCrearNivel();
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        crearmodificarNivel();
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void tabla_generalMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_generalMouseReleased
        /*if (band_index == 0) {
         int fila;
         int id_producto;
         fila = tabla_general.getSelectedRow();
         m = (DefaultTableModel) tabla_general.getModel();
         id_producto = Integer.parseInt((String) m.getValueAt(fila, 0));
         mostrar_datos_producto(id_producto);
         }*/
    }//GEN-LAST:event_tabla_generalMouseReleased

    private void tabla_generalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_generalKeyReleased
        /*if (band_index == 0) {
         int fila;
         int id_producto;
         fila = tabla_general.getSelectedRow();
         m = (DefaultTableModel) tabla_general.getModel();
         id_producto = Integer.parseInt((String) m.getValueAt(fila, 0));
         mostrar_datos_producto(id_producto);
         }*/
    }//GEN-LAST:event_tabla_generalKeyReleased

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
        int fila = tabla_general.getSelectedRow();
        //tabla_general.getModel().getValueAt(tabla_general.convertRowIndexToView(tabla_general.getSelectedRow()), 0);

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro.");
        } else {
            DefaultTableModel tm = (DefaultTableModel) tabla_general.getModel();
            int id_producto = (Integer) tm.getValueAt(fila, 0);

            crear0_modificar1_producto = 1;
            id_producto_global = id_producto;
            MostrarObjetos(true);
            mostrarMaterial(crear0_modificar1_producto, id_producto);
        }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        int fila = tabla_general.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro.");
        } else {
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este registro?", "Eliminar", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                DefaultTableModel tm = (DefaultTableModel) tabla_general.getModel();
                int id = (Integer) tm.getValueAt(fila, 0);

                NivelBE obj = new NivelBE();
                obj.setIdNivel(id);

                try {
                    if (objNivelBL.delete(obj) > 0) {
                        mostrarTablaNivel();
                        JOptionPane.showMessageDialog(null, "Registro eliminado con éxito.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar el registro seleccionado.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        JTextField caja = txtNombre;
        int limite = 500;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyTyped
        JTextField caja = txtCodigo;
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txtCodigoKeyTyped

    private void cboAlmacenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboAlmacenItemStateChanged
        incializarCombo(cboStand);
        MostrarComboStand(cboStand, true, false, null, getCodigoCombo(cboAlmacen.getSelectedItem().toString().trim()));
    }//GEN-LAST:event_cboAlmacenItemStateChanged

    private void cboAlmacenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAlmacenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboAlmacenActionPerformed

    private void btn_cancelarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_cancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CerrarDialogoCrearNivel();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearNivel();
        }
    }//GEN-LAST:event_btn_cancelarKeyReleased

    private void btn_guardarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_guardarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearmodificarNivel();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearNivel();
        }
    }//GEN-LAST:event_btn_guardarKeyReleased

    private void txtCodigoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearmodificarNivel();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearNivel();
        }
    }//GEN-LAST:event_txtCodigoKeyReleased

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearmodificarNivel();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearNivel();
        }
    }//GEN-LAST:event_txtNombreKeyReleased

    private void cboStandItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboStandItemStateChanged

    }//GEN-LAST:event_cboStandItemStateChanged

    private void cboStandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboStandActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboStandActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_modificar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JComboBox cboAlmacen;
    private javax.swing.JComboBox cboStand;
    private javax.swing.JPanel centro;
    private javax.swing.JDialog dialogCrearStand;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JPanel norte;
    private javax.swing.JPanel panel_tabla;
    private javax.swing.JPanel sur;
    private javax.swing.JTable tabla_general;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables

    private void MostrarCombo(JComboBox cbo, int CodigoTabla, boolean MostrarFilaVacia, boolean Autocompletado, String cadenaDefault) {
        try {
            List<DatoComunBE> list = objDatoComunBL.ReadDetalle(CodigoTabla);
            if (!list.isEmpty()) {
                DefaultComboBoxModel cboModel = new DefaultComboBoxModel();

                if (Autocompletado) {
                    AutoCompleteDecorator.decorate(cbo);
                }

                if (MostrarFilaVacia && (cadenaDefault == null || cadenaDefault.isEmpty())) {
                    cboModel.addElement("");
                }

                if (cadenaDefault != null && cadenaDefault.length() > 0) {
                    cboModel.addElement(cadenaDefault);
                }

                String DescripcionCorta;

                for (DatoComunBE obj : list) {
                    DescripcionCorta = obj.getIdDatoComun() + "|" + obj.getDescripcionCorta();

                    if (cadenaDefault != null) {
                        if (!DescripcionCorta.equals(cadenaDefault)) {
                            cboModel.addElement(DescripcionCorta);
                        }
                    } else {
                        cboModel.addElement(DescripcionCorta);
                    }
                }

                cbo.setModel(cboModel);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void incializarCombo(JComboBox cbo) {
        DefaultComboBoxModel cboModel = new DefaultComboBoxModel();
        cbo.setModel(cboModel);
    }

    private void mostrarMaterial(int accion, int id_producto) {
        limpiar_caja_texto_crear_material();

        if (accion == 0) {
            incializarCombo(cboStand);
            cargarCombos();

        } else {
            modificarMaterial(id_producto);
        }

        dialogCrearStand.setSize(564, 284);
        dialogCrearStand.setLocationRelativeTo(producto);
        dialogCrearStand.setModal(true);
        dialogCrearStand.setVisible(true);

    }

    private void cargarCombos() {
        MostrarCombo(cboAlmacen, 3, true, false, null);
    }

    private void MostrarComboStand(JComboBox cbo, boolean MostrarFilaVacia, boolean Autocompletado, String cadenaDefault, int idAlmacen) {
        try {
            List<StandBE> list = objStandBL.readByAlmacen(idAlmacen);

            if (!list.isEmpty()) {
                DefaultComboBoxModel cboModel = new DefaultComboBoxModel();

                if (Autocompletado) {
                    AutoCompleteDecorator.decorate(cbo);
                }

                if (MostrarFilaVacia && (cadenaDefault == null || cadenaDefault.isEmpty())) {
                    cboModel.addElement("");
                }

                if (cadenaDefault != null && cadenaDefault.length() > 0) {
                    cboModel.addElement(cadenaDefault);
                }

                String valor;

                for (StandBE obj : list) {
                    valor = insertarCeros(obj.getIdStand()) + "|" + obj.getNombre();

                    if (cadenaDefault != null) {
                        if (!valor.equals(cadenaDefault)) {
                            cboModel.addElement(valor);
                        }
                    } else {
                        cboModel.addElement(valor);
                    }
                }

                cbo.setModel(cboModel);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void CerrarDialogoCrearNivel() {
        crear0_modificar1_producto = 0;
        id_producto_global = 0;
        MostrarObjetos(true);
        dialogCrearStand.dispose();
    }

    private NivelBE capturarValorNivel() {
        NivelBE pbe = null;

        int codigo = 0;
        if (txtCodigo.getText().trim().length() > 0) {
            codigo = Integer.parseInt(txtCodigo.getText().trim());
        }

        String nombre = txtNombre.getText().trim();
        int band = 0;

        if (cboAlmacen.getSelectedItem()!= null && cboAlmacen.getSelectedItem().toString().trim().length() == 0) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione un almacén.");
            band++;
        }

        if (band == 0) {
            if (cboStand.getSelectedItem()!= null && cboStand.getSelectedItem().toString().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "Por favor seleccione un stand.");
                band++;
            }
        }

        if (band == 0) {
            if (nombre.length() == 0) {
                JOptionPane.showMessageDialog(null, "El campo Nombre es obligarotio");
                band++;
            }
        }

        if (band == 0) {
            pbe = new NivelBE();
            pbe.setIdNivel(codigo);
            pbe.setIdStand(getCodigoCombo(cboStand.getSelectedItem().toString().trim()));
            pbe.setNombre(nombre);
            pbe.setIdEmpresa(id_empresa_index);
            pbe.setUsuarioDes(aliasUsuarioIndex);
        }

        return pbe;
    }

    private void crearmodificarNivel() {
        try {
            NivelBE obj = capturarValorNivel();

            if (obj != null) {
                if (crear0_modificar1_producto == 0) {
                    if (objNivelBL.create(obj) > 0) {
                        JOptionPane.showMessageDialog(null, "Registro exitoso.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo registrar la operacion.");
                    }
                } else {
                    if (objNivelBL.update(obj) > 0) {
                        JOptionPane.showMessageDialog(null, "Actualización exitosa.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo actualizar el registro.");
                    }
                }
                CerrarDialogoCrearNivel();
                mostrarTablaNivel();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarMaterial(int id) {
        try {
            NivelBE obj = objNivelBL.readId(id);

            if (obj != null) {
                mostrarDatosCajaTexto(obj);
                incializarCombo(cboStand);
                MostrarCombo(cboAlmacen, 3, false, false, obj.getIdAlmacen() + "|" + obj.getNombreAlmacen());
                MostrarComboStand(cboStand, false, false, insertarCeros(obj.getIdStand()) + "|" + obj.getNombreStand(), getCodigoCombo(cboAlmacen.getSelectedItem().toString().trim()));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDatosCajaTexto(NivelBE obj) {
        txtCodigo.setText(insertarCeros(obj.getIdNivel()));

        if (obj.getNombre() != null && obj.getNombre().length() > 0) {
            txtNombre.setText(obj.getNombre());
        }
    }

    private void MostrarObjetos(boolean b) {
        btn_guardar.setVisible(b);
    }

    private int getCodigoCombo(String valueCombo) {
        int codigo = Integer.parseInt(valueCombo.split("[|]")[0]);
        return codigo;
    }

    private String insertarCeros(int idSalidaMaterial) {
        String valor = String.valueOf(idSalidaMaterial);

        while (valor.length() <= 4) {
            valor = "0" + valor;
        }

        return valor;
    }

}
