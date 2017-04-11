package system_rysi;

import Clases.cTipopago;
import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author ErC
 */
public class tipopago extends javax.swing.JPanel {

    String controlador_index;
    String DSN_index;
    String user_index;
    String password_index;
    int id_empresa_index;
    int id_usuario_index;
    String perfil_usuario_index = "";
    private Connection conexion;
    private Statement sentencia;
    cTipopago clase_tipopago = new cTipopago();
    DefaultTableModel m;
    int crear0_modificar1 = 0;
    String id_tipopago_global = "";
    String band_nombre = "";
    private Component tipopago;

    public tipopago(String controlador, String DSN, String user, String password, int id_empresa, int id_usuario, String perfil_usuario) {
        controlador_index = controlador;
        DSN_index = DSN;
        user_index = user;
        password_index = password;
        id_empresa_index = id_empresa;
        id_usuario_index = id_usuario;
        perfil_usuario_index = perfil_usuario;

        System.out.println("\n\nconectando con Tipo Pago");
        initComponents();

        System.out.println("iniciando conexion");
        conexion();

        System.out.println("ocultar el panel de detalle de datos");
        Panel_detalle.setVisible(false);

        System.out.println("activando la función de letra Mayúsculas");
        Activar_letras_Mayusculas();

        if (perfil_usuario_index.equals("Solo Lectura")) {
            btn_nuevo.setVisible(false);
            btn_modificar.setVisible(false);
            btn_eliminar.setVisible(false);
        }

        System.out.println("mostrar tabla Tipo pago");
        Mostrar_tabla_tipopago();
    }

    private void conexion() {

        System.out.println("valores recibidos para la conexion");
        System.out.println("==============================");
        System.out.println("controlador: " + controlador_index);
        System.out.println("DNS: " + DSN_index);
        System.out.println("usuario: " + user_index);
        System.out.println("contraseña: " + password_index);
        System.out.println("==============================");

        FileReader fichero;
        BufferedReader br;

        try {
            Class.forName(controlador_index).newInstance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        try {
            conexion = DriverManager.getConnection(DSN_index, user_index, password_index);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los archivos de conexion", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        try {
            sentencia = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear el objeto sentencia", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Mostrar_tabla_tipopago() {
        try {
            ResultSet r = sentencia.executeQuery("select id_tipopago, descripcion, cantdias from ttipopago where id_empresa = '" + id_empresa_index + "' order by descripcion asc");
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Descripcion");
            modelo.addColumn("Dias");

            String fila[] = new String[3];
            while (r.next()) {
                fila[0] = r.getString("id_tipopago").trim();
                fila[1] = r.getString("descripcion").trim();
                fila[2] = r.getString("cantdias").trim();
                tabla_general.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_general.setModel(modelo);
            TableColumn columna1 = tabla_general.getColumn("");
            columna1.setPreferredWidth(1);
            TableColumn columna2 = tabla_general.getColumn("Descripcion");
            columna2.setPreferredWidth(200);
            TableColumn columna3 = tabla_general.getColumn("Dias");
            columna3.setPreferredWidth(100);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Tipo de pago" + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Activar_letras_Mayusculas() {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_TYPED) {
                    if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
                        e.setKeyChar((char) (((int) e.getKeyChar()) - 32));
                    }
                }
                return false;
            }
        });
    }

    private void activar_caja_texto(String funcion) {
        if (funcion.equals("editable")) {
            txt_descripcion.setEditable(true);
            txt_cantdias.setEditable(true);
            txt_avisar_vencer.setEditable(true);
        }

        if (funcion.equals("no_editable")) {
            txt_descripcion.setEditable(false);
            txt_cantdias.setEditable(false);
            txt_f_creacion.setEditable(false);
            txt_f_modificacion.setEditable(false);
            txt_usuario.setEditable(false);
            txt_avisar_vencer.setEditable(false);
        }
    }

    private void limpiar_caja_texto() {
        txt_descripcion.setText("");
        txt_cantdias.setText("");
        txt_avisar_vencer.setText("");
        txt_f_creacion.setText("");
        txt_f_modificacion.setText("");
        txt_usuario.setText("");
    }

    private void activar_barra_herramientas(String funcion) {
        if (funcion.equals("activar")) {
            btn_nuevo.setEnabled(true);
            btn_modificar.setEnabled(true);
            btn_eliminar.setEnabled(true);
            txt_buscar.setEnabled(true);
        }

        if (funcion.equals("desactivar")) {
            btn_nuevo.setEnabled(false);
            btn_modificar.setEnabled(false);
            btn_eliminar.setEnabled(false);
            txt_buscar.setEnabled(false);
        }
    }

    private void tamaño_de_caja(JTextField caja, KeyEvent evt, int limite) {
        if (caja.getText().length() == limite) {
            evt.consume();
        }
    }

    private void tamaño_de_caja_jtextarea(JTextArea caja, KeyEvent evt, int limite) {
        if (caja.getText().length() == limite) {
            evt.consume();
        }
    }

    private void ingresar_solo_numeros(JTextField caja, KeyEvent evt) {
        int k = (int) evt.getKeyChar();
        if (k >= 97 && k <= 122 || k >= 65 && k <= 90) {
            evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            JOptionPane.showMessageDialog(null, "No puede ingresar letras", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (k == 241 || k == 209) {
            evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            JOptionPane.showMessageDialog(null, "No puede ingresar letras", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (k == 10) {
            caja.transferFocus();
        }
    }

    private void crear_tipopago(String descripcion, int cantdias, int id_empresa, int id_usuario, int avisar_por_vencer) {
        System.out.println("\nejecutandose la función: crear_tipopago");

        if (clase_tipopago.Tipopago_crear(descripcion, cantdias, id_empresa, id_usuario, avisar_por_vencer)) {
            System.out.println("\nEl Tipo de pago se logró registrar exitosamente!");

            System.out.println("desactivar las cajas de texto para el registro");
            activar_caja_texto("no_editable");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto();

            System.out.println("mostrar botones: vista previa, imprimir, creacion");
            btn_vista_previa.setVisible(true);
            btn_imprimir.setVisible(true);
            btn_creacion.setVisible(true);

            System.out.println("ocultar botones: guardar, cancelar");
            btn_guardar.setVisible(false);
            btn_cancelar.setVisible(false);

            System.out.println("activar barra de herramientas");
            activar_barra_herramientas("activar");

            System.out.println("ocultar el panel de detalle de datos");
            Panel_detalle.setVisible(false);

            band_nombre = "";

            System.out.println("actualizamos tabla tipopago");
            Mostrar_tabla_tipopago();

            JOptionPane.showMessageDialog(null, "El Tipo de Pago se registro exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear el Tipo de pago. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar_tipopago(int id_tipopago, String descripcion, int cantdias, int id_empresa, int id_usuario, int avisar_por_vencer) {
        System.out.println("ejecutandose la función: modificar_tipopago");

        if (clase_tipopago.Tipopago_modificar(id_tipopago, descripcion, cantdias, id_empresa, id_usuario, avisar_por_vencer)) {
            System.out.println("El tipo de pago se logró modificar exitosamente!");

            System.out.println("desactivar las cajas de texto para el registro");
            activar_caja_texto("no_editable");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto();

            System.out.println("mostrar botones: vista previa, imprimir, creacion");
            btn_vista_previa.setVisible(true);
            btn_imprimir.setVisible(true);
            btn_creacion.setVisible(true);

            System.out.println("ocultar botones: guardar, cancelar");
            btn_guardar.setVisible(false);
            btn_cancelar.setVisible(false);

            System.out.println("activar barra de herramientas");
            activar_barra_herramientas("activar");

            System.out.println("ocultar el panel de detalle de datos");
            Panel_detalle.setVisible(false);

            band_nombre = "";

            System.out.println("actualizamos tabla tipo de pago");
            Mostrar_tabla_tipopago();

            JOptionPane.showMessageDialog(null, "El Tipo de pago se modificó exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al modificar el Tipo de pago. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialog_fecha_creacion = new javax.swing.JDialog();
        jPanel12 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txt_f_creacion = new javax.swing.JTextField();
        txt_f_modificacion = new javax.swing.JTextField();
        txt_usuario = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btn_nuevo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btn_modificar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btn_eliminar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        barra_buscar = new javax.swing.JToolBar();
        label_buscar = new javax.swing.JLabel();
        txt_buscar = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        panel_tabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_general = new javax.swing.JTable();
        Panel_detalle = new javax.swing.JPanel();
        norte = new javax.swing.JPanel();
        lbl_titulo = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        centro = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_descripcion = new javax.swing.JTextField();
        txt_cantdias = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_avisar_vencer = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btn_cancelar = new javax.swing.JButton();
        btn_guardar = new javax.swing.JButton();
        btn_imprimir = new javax.swing.JButton();
        btn_creacion = new javax.swing.JButton();
        btn_vista_previa = new javax.swing.JButton();

        jPanel12.setBackground(new java.awt.Color(0, 110, 204));
        jPanel12.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/creacion_32_32.png"))); // NOI18N
        jLabel7.setText("Fecha de Creación y Modificación");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_fecha_creacion.getContentPane().add(jPanel12, java.awt.BorderLayout.PAGE_START);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jPanel15.setPreferredSize(new java.awt.Dimension(418, 40));

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cerrar_ventana_32_32.png"))); // NOI18N
        jButton6.setText("Cerrar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 319, Short.MAX_VALUE)
                .addComponent(jButton6))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jButton6)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel15, java.awt.BorderLayout.PAGE_END);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 153));
        jLabel8.setText("Fecha de Creación:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 51, 153));
        jLabel9.setText("Fecha de Modificación:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 51, 153));
        jLabel10.setText("Usuario:");

        txt_f_creacion.setEditable(false);
        txt_f_creacion.setBackground(new java.awt.Color(255, 255, 255));
        txt_f_creacion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txt_f_modificacion.setEditable(false);
        txt_f_modificacion.setBackground(new java.awt.Color(255, 255, 255));
        txt_f_modificacion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txt_usuario.setEditable(false);
        txt_usuario.setBackground(new java.awt.Color(255, 255, 255));
        txt_usuario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_f_creacion, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                    .addComponent(txt_f_modificacion)
                    .addComponent(txt_usuario))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_f_creacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txt_f_modificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txt_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel16, java.awt.BorderLayout.CENTER);

        dialog_fecha_creacion.getContentPane().add(jPanel14, java.awt.BorderLayout.CENTER);

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(0, 110, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/tipopago_32_32.png"))); // NOI18N
        jLabel1.setText("Tipo de Pago");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 887, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);

        jToolBar1.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar1.setRollover(true);
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

        jPanel1.add(jToolBar1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.BorderLayout());

        barra_buscar.setBackground(new java.awt.Color(255, 255, 255));
        barra_buscar.setRollover(true);
        barra_buscar.setPreferredSize(new java.awt.Dimension(13, 25));

        label_buscar.setText("Buscar:");
        barra_buscar.add(label_buscar);

        txt_buscar.setColumns(50);
        txt_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_buscarKeyReleased(evt);
            }
        });
        barra_buscar.add(txt_buscar);

        jPanel2.add(barra_buscar, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.BorderLayout());

        panel_tabla.setBackground(new java.awt.Color(255, 255, 255));
        panel_tabla.setPreferredSize(new java.awt.Dimension(300, 461));

        tabla_general.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
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

        javax.swing.GroupLayout panel_tablaLayout = new javax.swing.GroupLayout(panel_tabla);
        panel_tabla.setLayout(panel_tablaLayout);
        panel_tablaLayout.setHorizontalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        panel_tablaLayout.setVerticalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
        );

        jPanel5.add(panel_tabla, java.awt.BorderLayout.LINE_START);

        Panel_detalle.setLayout(new java.awt.BorderLayout());

        norte.setBackground(new java.awt.Color(255, 255, 255));
        norte.setPreferredSize(new java.awt.Dimension(458, 40));

        lbl_titulo.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_titulo.setForeground(new java.awt.Color(0, 110, 204));
        lbl_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_titulo.setText("Información del Tipo de pago");

        javax.swing.GroupLayout norteLayout = new javax.swing.GroupLayout(norte);
        norte.setLayout(norteLayout);
        norteLayout.setHorizontalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
            .addComponent(jSeparator3)
        );
        norteLayout.setVerticalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(norteLayout.createSequentialGroup()
                .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Panel_detalle.add(norte, java.awt.BorderLayout.PAGE_START);

        centro.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 587, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        centro.add(jPanel6, java.awt.BorderLayout.PAGE_END);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(153, 213, 255));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 223, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel8, java.awt.BorderLayout.LINE_START);

        jPanel10.setBackground(new java.awt.Color(153, 213, 255));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 223, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel10, java.awt.BorderLayout.LINE_END);

        jPanel11.setBackground(new java.awt.Color(153, 213, 255));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 153));
        jLabel3.setText("Descripción:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 153));
        jLabel4.setText("Días:");

        txt_descripcion.setEditable(false);
        txt_descripcion.setBackground(new java.awt.Color(255, 255, 255));
        txt_descripcion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_descripcionKeyTyped(evt);
            }
        });

        txt_cantdias.setEditable(false);
        txt_cantdias.setBackground(new java.awt.Color(255, 255, 255));
        txt_cantdias.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_cantdias.setToolTipText("Ingrese la Cantidad de Dias en las que se puede hacer el pago");
        txt_cantdias.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cantdiasKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 51, 153));
        jLabel5.setText("Por vencer:");

        txt_avisar_vencer.setEditable(false);
        txt_avisar_vencer.setBackground(new java.awt.Color(255, 255, 255));
        txt_avisar_vencer.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_avisar_vencer.setToolTipText("Ingrese la Cantidad de Dias en las que se puede hacer el pago");
        txt_avisar_vencer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_avisar_vencerKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 153));
        jLabel6.setText("días");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_descripcion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(txt_cantdias, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(txt_avisar_vencer, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_cantdias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_avisar_vencer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(137, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel11, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        centro.add(jPanel9, java.awt.BorderLayout.CENTER);

        Panel_detalle.add(centro, java.awt.BorderLayout.CENTER);

        jPanel4.setPreferredSize(new java.awt.Dimension(458, 40));

        btn_cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cancelar.setText("Cancelar");
        btn_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarActionPerformed(evt);
            }
        });

        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_guardar.setText("Guardar");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });

        btn_imprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/imprimir_32_32.png"))); // NOI18N
        btn_imprimir.setText("Imprimir");
        btn_imprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_imprimirActionPerformed(evt);
            }
        });

        btn_creacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/creacion_32_32.png"))); // NOI18N
        btn_creacion.setText("Creación");
        btn_creacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_creacionActionPerformed(evt);
            }
        });

        btn_vista_previa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/vista_previa_32_32.png"))); // NOI18N
        btn_vista_previa.setText("Vista Previa");
        btn_vista_previa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_vista_previaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 2, Short.MAX_VALUE)
                .addComponent(btn_vista_previa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_creacion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_imprimir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_guardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cancelar)
                    .addComponent(btn_guardar)
                    .addComponent(btn_imprimir)
                    .addComponent(btn_creacion)
                    .addComponent(btn_vista_previa))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        Panel_detalle.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jPanel5.add(Panel_detalle, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_creacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_creacionActionPerformed

        dialog_fecha_creacion.setSize(429, 270);
        dialog_fecha_creacion.setLocationRelativeTo(tipopago);
        dialog_fecha_creacion.setModal(true);
        dialog_fecha_creacion.setVisible(true);

    }//GEN-LAST:event_btn_creacionActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        dialog_fecha_creacion.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        System.out.println("\ncrear tipo de pago");
        System.out.println("====================");

        System.out.println("inicializar la bandera crear en cero");
        crear0_modificar1 = 0;

        System.out.println("cambiando el titulo");
        lbl_titulo.setText("Nuevo Tipo de Pago");

        System.out.println("activar las cajas de texto para el registro");
        activar_caja_texto("editable");

        System.out.println("limpiar cajas de texto");
        limpiar_caja_texto();

        System.out.println("ocultar botones: vista previa, imprimir, creacion");
        btn_vista_previa.setVisible(false);
        btn_imprimir.setVisible(false);
        btn_creacion.setVisible(false);

        System.out.println("mostrar botones: guardar, cancelar");
        btn_guardar.setVisible(true);
        btn_cancelar.setVisible(true);
        btn_guardar.setText("Guardar");
        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png")));

        System.out.println("desactivar barra de herramientas");
        activar_barra_herramientas("desactivar");

        System.out.println("mostrar el panel de detalle de datos");
        Panel_detalle.setVisible(true);
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        System.out.println("\npresionó boton Guardar");
        System.out.println("========================");

        System.out.println("capturando datos ingresados");

        if (txt_descripcion.getText().length() == 0 || txt_cantdias.getText().length() == 0 || txt_avisar_vencer.getText().length() == 0) {
            System.out.println("\nFalta ingresar Descripcion, cantidad de dias o la cantidad de dias a vencer.");
            JOptionPane.showMessageDialog(null, "Los campos Descripcion y Cantidad de dias son necesarios\n Por favor ingrese estos campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {

            String descripcion = txt_descripcion.getText().trim();
            int cantdias = Integer.parseInt(txt_cantdias.getText().trim());
            int id_empresa = id_empresa_index;
            int id_usuario = id_usuario_index;
            int avisar_por_vencer = Integer.parseInt(txt_avisar_vencer.getText().trim());

            if (crear0_modificar1 == 0) {

                System.out.println("crear0_modificar1= " + crear0_modificar1);

                if (clase_tipopago.nombre_existente(descripcion, id_empresa_index) > 0) {
                    System.out.println("Descripcion del Tipo de pago existente");
                    JOptionPane.showMessageDialog(null, "Descripcion del Tipo de pago existente.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println("llamamos a la funcion crear_tipopago");
                    crear_tipopago(descripcion, cantdias, id_empresa, id_usuario, avisar_por_vencer);
                }
            } else {
                System.out.println("crear0_modificar1= " + crear0_modificar1);

                if (band_nombre.equals(txt_descripcion.getText().trim())) {

                    System.out.println("El Nombre es igual, se procede con la modificacion");
                    int id_tipopago = Integer.parseInt(id_tipopago_global);
                    System.out.println("llamamos a la funcion modificar_tipopago");
                    modificar_tipopago(id_tipopago, descripcion, cantdias, id_empresa, id_usuario, avisar_por_vencer);

                } else {
                    System.out.println("El nombre no es igual, se procede a aplicar restricciones");

                    if (clase_tipopago.nombre_existente(descripcion, id_empresa_index) > 0) {
                        System.out.println("El Nombre ya se encuentra registrado.");
                        JOptionPane.showMessageDialog(null, "El Nombre ingresado ya se encuentra Registrado. \n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int id_tipopago = Integer.parseInt(id_tipopago_global);
                        System.out.println("llamamos a la funcion modificar_tipopago");
                        modificar_tipopago(id_tipopago, descripcion, cantdias, id_empresa, id_usuario, avisar_por_vencer);
                    }
                }
            }

        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
        int fil = tabla_general.getSelectedRow();
        if (fil == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un elemento para Modificar");
        } else {
            System.out.println("\nModifar Tipo de pago");
            System.out.println("=================");

            System.out.println("inicializar la bandera crear en uno");
            crear0_modificar1 = 1;

            System.out.println("cambiando el titulo");
            lbl_titulo.setText("Modificar Tipo de pago");

            System.out.println("activar las cajas de texto para el registro");
            activar_caja_texto("editable");

            System.out.println("ocultar botones: vista previa, imprimir, creacion");
            btn_vista_previa.setVisible(false);
            btn_imprimir.setVisible(false);
            btn_creacion.setVisible(false);

            System.out.println("mostrar botones: guardar, cancelar");
            btn_guardar.setVisible(true);
            btn_cancelar.setVisible(true);
            btn_guardar.setText("Modificar");
            btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_actualizar_32_32.png")));

            System.out.println("desactivar barra de herramientas");
            activar_barra_herramientas("desactivar");

            band_nombre = txt_descripcion.getText().trim();
        }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        System.out.println("\nEliminar Tipo pago");
        System.out.println("=================");

        int fila;
        int id_tipopago;
        int respuesta;
        try {
            fila = tabla_general.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar el registro a borrar");
            } else {
                respuesta = JOptionPane.showConfirmDialog(null, "¿Desea eliminar?", "Eliminar", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    m = (DefaultTableModel) tabla_general.getModel();
                    id_tipopago = Integer.parseInt((String) m.getValueAt(fila, 0));
                    System.out.println("el id_tipopago que se eliminara es: " + id_tipopago);
                    clase_tipopago.Tipopago_eliminar(id_tipopago);

                    System.out.println("el tipo de pago se logró eliminar exitosamente!");
                    System.out.println("ocultar el panel de detalle de datos");
                    Panel_detalle.setVisible(false);
                    JOptionPane.showMessageDialog(null, "El Tipo de Pago se Eliminó exitosamente!");

                    System.out.println("actualizamos tabla Tipo de pago");
                    Mostrar_tabla_tipopago();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al eliminar la empresa.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void tabla_generalMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_generalMouseReleased
        System.out.println("\nMostrar datos de Tipo de pago");
        System.out.println("===========================");

        System.out.println("Limpiar cajas de texto");
        limpiar_caja_texto();

        int fila;
        String id_tipopago;
        fila = tabla_general.getSelectedRow();
        m = (DefaultTableModel) tabla_general.getModel();
        id_tipopago = (String) m.getValueAt(fila, 0);
        System.out.println("el id seleccionado es: " + id_tipopago);
        id_tipopago_global = id_tipopago;
        ResultSet r;

        try {
            r = sentencia.executeQuery("select d.descripcion descripcion, d.cantdias cantdias, d.avisar_por_vencer avisar_por_vencer, d.f_creacion f_creacion, d.f_modificacion f_modificacion, u.nombre nombre_usuario from ttipopago d, tusuario u where d.id_usuario=u.id_usuario and d.id_tipopago=" + id_tipopago + "");
            while (r.next()) {
                System.out.println("Valores extraidos de la BD:");

                txt_descripcion.setText(r.getString("descripcion").trim());
                System.out.println(txt_descripcion.getText().trim());

                txt_cantdias.setText(r.getString("cantdias").trim());
                System.out.println(txt_cantdias.getText().trim());
                
                txt_avisar_vencer.setText(r.getString("avisar_por_vencer").trim());
                System.out.println(txt_avisar_vencer.getText().trim());

                txt_f_creacion.setText(r.getString("f_creacion").trim());
                System.out.println(txt_f_creacion.getText().trim());

                txt_f_modificacion.setText(r.getString("f_modificacion"));
                System.out.println(txt_f_modificacion.getText().trim());

                txt_usuario.setText(r.getString("nombre_usuario").trim());
                System.out.println(txt_usuario.getText().trim());

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos del tipo de pago", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("Cambiando el titulo");
        lbl_titulo.setText("Información del Tipo de Pago");

        System.out.println("Desactivar las cajas de texto para el registro");
        activar_caja_texto("no_editable");

        System.out.println("Mostrar botones: vista previa, imprimir, creacion");
        btn_vista_previa.setVisible(true);
        btn_imprimir.setVisible(true);
        btn_creacion.setVisible(true);

        System.out.println("Ocultar botones: guardar, cancelar");
        btn_guardar.setVisible(false);
        btn_cancelar.setVisible(false);

        System.out.println("Activar barra de herramientas");
        activar_barra_herramientas("activar");

        System.out.println("Mostrar el panel de detalle de datos");
        Panel_detalle.setVisible(true);
    }//GEN-LAST:event_tabla_generalMouseReleased

    private void tabla_generalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_generalKeyReleased
        System.out.println("\nMostrar datos de Tipo de pago");
        System.out.println("===========================");

        System.out.println("Limpiar cajas de texto");
        limpiar_caja_texto();

        int fila;
        String id_tipopago;
        fila = tabla_general.getSelectedRow();
        m = (DefaultTableModel) tabla_general.getModel();
        id_tipopago = (String) m.getValueAt(fila, 0);
        System.out.println("el id seleccionado es: " + id_tipopago);
        id_tipopago_global = id_tipopago;
        ResultSet r;

        try {
            r = sentencia.executeQuery("select d.descripcion descripcion, d.cantdias cantdias, d.avisar_por_vencer avisar_por_vencer, d.f_creacion f_creacion, d.f_modificacion f_modificacion, u.nombre nombre_usuario from ttipopago d, tusuario u where d.id_usuario=u.id_usuario and d.id_tipopago=" + id_tipopago + "");
            while (r.next()) {
                System.out.println("Valores extraidos de la BD:");

                txt_descripcion.setText(r.getString("descripcion").trim());
                System.out.println(txt_descripcion.getText().trim());

                txt_cantdias.setText(r.getString("cantdias").trim());
                System.out.println(txt_cantdias.getText().trim());

                txt_avisar_vencer.setText(r.getString("avisar_por_vencer").trim());
                System.out.println(txt_avisar_vencer.getText().trim());
                
                txt_f_creacion.setText(r.getString("f_creacion").trim());
                System.out.println(txt_f_creacion.getText().trim());

                txt_f_modificacion.setText(r.getString("f_modificacion"));
                System.out.println(txt_f_modificacion.getText().trim());

                txt_usuario.setText(r.getString("nombre_usuario").trim());
                System.out.println(txt_usuario.getText().trim());

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos del tipo de pago", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("Cambiando el titulo");
        lbl_titulo.setText("Información del Tipo de Pago");

        System.out.println("Desactivar las cajas de texto para el registro");
        activar_caja_texto("no_editable");

        System.out.println("Mostrar botones: vista previa, imprimir, creacion");
        btn_vista_previa.setVisible(true);
        btn_imprimir.setVisible(true);
        btn_creacion.setVisible(true);

        System.out.println("Ocultar botones: guardar, cancelar");
        btn_guardar.setVisible(false);
        btn_cancelar.setVisible(false);

        System.out.println("Activar barra de herramientas");
        activar_barra_herramientas("activar");

        System.out.println("Mostrar el panel de detalle de datos");
        Panel_detalle.setVisible(true);
    }//GEN-LAST:event_tabla_generalKeyReleased

    private void txt_buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscarKeyReleased
        ResultSet r;
        String bus = txt_buscar.getText();

        try {
            System.out.println("select id_tipopago, descripcion, cantdias from ttipopago where (descripcion like '%" + bus + "%'  or cantdias like '%" + bus + "%') and id_empresa = '" + id_empresa_index + "' order by descripcion asc");
            r = sentencia.executeQuery("select id_tipopago, descripcion, cantdias from ttipopago where (descripcion like '%" + bus + "%'  or cantdias like '%" + bus + "%') and id_empresa = '" + id_empresa_index + "' order by descripcion asc");
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Descripcion");
            modelo.addColumn("Dias");

            String fila[] = new String[3];
            while (r.next()) {
                fila[0] = r.getString("id_tipopago").trim();
                fila[1] = r.getString("descripcion").trim();
                fila[2] = r.getString("cantdias").trim();
                tabla_general.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_general.setModel(modelo);
            TableColumn columna1 = tabla_general.getColumn("");
            columna1.setPreferredWidth(1);
            TableColumn columna2 = tabla_general.getColumn("Descripcion");
            columna2.setPreferredWidth(200);
            TableColumn columna3 = tabla_general.getColumn("Dias");
            columna3.setPreferredWidth(100);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Tipo de pago" + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txt_buscarKeyReleased

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        int respuesta;
        respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea Cancelar el registro del Tipo de Pago?", "Cancelar", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {

            System.out.println("\nCancelar creacion de Tipo de pago");
            System.out.println("==============================");

            System.out.println("desactivar las cajas de texto para el registro");
            activar_caja_texto("no_editable");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto();

            System.out.println("mostrar botones: vista previa, imprimir, creacion");
            btn_vista_previa.setVisible(true);
            btn_imprimir.setVisible(true);
            btn_creacion.setVisible(true);

            System.out.println("ocultar botones: guardar, cancelar");
            btn_guardar.setVisible(false);
            btn_cancelar.setVisible(false);

            System.out.println("activar barra de herramientas");
            activar_barra_herramientas("activar");

            System.out.println("ocultar el panel de detalle de datos");
            Panel_detalle.setVisible(false);

            System.out.println("actualizamos tabla Tipo de pago");
            Mostrar_tabla_tipopago();
        }
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void btn_imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imprimirActionPerformed
        JOptionPane.showMessageDialog(null, "La opción aun no se encuentra disponible. \n Por favor, consulte con el Administrador del Sistema");
    }//GEN-LAST:event_btn_imprimirActionPerformed

    private void btn_vista_previaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vista_previaActionPerformed
        JOptionPane.showMessageDialog(null, "La opción aun no se encuentra disponible. \n Por favor, consulte con el Administrador del Sistema");
    }//GEN-LAST:event_btn_vista_previaActionPerformed

    private void txt_descripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_descripcionKeyTyped
        JTextField caja = txt_descripcion;
        int limite = 50;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_descripcionKeyTyped

    private void txt_cantdiasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cantdiasKeyTyped
        JTextField caja = txt_cantdias;
        ingresar_solo_numeros(caja, evt);
        int limite = 4;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_cantdiasKeyTyped

    private void txt_avisar_vencerKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_avisar_vencerKeyTyped
        JTextField caja = txt_avisar_vencer;
        ingresar_solo_numeros(caja, evt);        
    }//GEN-LAST:event_txt_avisar_vencerKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Panel_detalle;
    private javax.swing.JToolBar barra_buscar;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_creacion;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_imprimir;
    private javax.swing.JButton btn_modificar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JButton btn_vista_previa;
    private javax.swing.JPanel centro;
    private javax.swing.JDialog dialog_fecha_creacion;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel label_buscar;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JPanel norte;
    private javax.swing.JPanel panel_tabla;
    private javax.swing.JTable tabla_general;
    private javax.swing.JTextField txt_avisar_vencer;
    private javax.swing.JTextField txt_buscar;
    private javax.swing.JTextField txt_cantdias;
    private javax.swing.JTextField txt_descripcion;
    private javax.swing.JTextField txt_f_creacion;
    private javax.swing.JTextField txt_f_modificacion;
    private javax.swing.JTextField txt_usuario;
    // End of variables declaration//GEN-END:variables
}
