package system_rysi;

import Clases.cVehiculo;
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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


public class vehiculo extends javax.swing.JPanel {

    private Component vehiculo;
    String controlador_index;
    String DSN_index;
    String user_index;
    String password_index;
    int id_empresa_index;
    int id_usuario_index;
    private Connection conexion;
    private Statement sentencia;
    cVehiculo clase_vehiculo = new cVehiculo();
    DefaultTableModel m;
    int crear0_modificar1 = 0;
    int id_vehiculo_global;
    String band_placa = "";
    String band_nro_inscripcion = "";
    String perfil_usuario_index = "";

    public vehiculo(String controlador, String DSN, String user, String password, int id_empresa, int id_usuario, String perfil_usuario) {
        controlador_index = controlador;
        DSN_index = DSN;
        user_index = user;
        password_index = password;
        id_empresa_index = id_empresa;
        id_usuario_index = id_usuario;
        perfil_usuario_index = perfil_usuario;

        System.out.println("\n\nconectando con Vehiculo");
        initComponents();

        System.out.println("iniciando conexion");
        conexion();

        System.out.println("ocultar el panel de detalle de datos");
        Panel_detalle.setVisible(false);

        System.out.println("activando la función de letra Mayúsculas");
        Activar_letras_Mayusculas();
        
        if(perfil_usuario_index.equals("Solo Lectura"))
        {
            btn_nuevo.setVisible(false);
            btn_modificar.setVisible(false);
            btn_eliminar.setVisible(false);
        }

        System.out.println("mostrar tabla vehiculo");
        Mostrar_tabla_vehiculo();
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

    private void Mostrar_tabla_vehiculo() {
        try {
            ResultSet r = sentencia.executeQuery("select id_vehiculo, marca, placa from tvehiculo where id_empresa = '" + id_empresa_index + "' order by marca asc");
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Marca");
            modelo.addColumn("Placa");

            String fila[] = new String[3];
            while (r.next()) {
                fila[0] = r.getString("id_vehiculo").trim();
                fila[1] = r.getString("marca").trim();
                fila[2] = r.getString("placa").trim();
                tabla_general.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_general.setModel(modelo);
            TableColumn columna1 = tabla_general.getColumn("");
            columna1.setPreferredWidth(1);
            TableColumn columna2 = tabla_general.getColumn("Marca");
            columna2.setPreferredWidth(150);
            TableColumn columna3 = tabla_general.getColumn("Placa");
            columna3.setPreferredWidth(150);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla vehiculo - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
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
            txt_marca.setEditable(true);
            txt_placa.setEditable(true);
            txt_modelo.setEditable(true);
            txt_nro_inscripcion.setEditable(true);
        }

        if (funcion.equals("no_editable")) {
            txt_marca.setEditable(false);
            txt_placa.setEditable(false);
            txt_modelo.setEditable(false);
            txt_nro_inscripcion.setEditable(false);
            txt_f_creacion.setEditable(false);
            txt_f_modificacion.setEditable(false);
            txt_usuario.setEditable(false);
        }
    }

    private void limpiar_caja_texto() {
        txt_marca.setText("");
        txt_placa.setText("");
        txt_modelo.setText("");
        txt_nro_inscripcion.setText("");
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

    private void crear_vehiculo(String marca, String placa, String modelo, String nro_inscripcion, int id_empresa, int id_usuario) {
        System.out.println("\nejecutandose la función: crear_vehiculo");

        if (clase_vehiculo.crear(marca, placa, modelo, nro_inscripcion, id_empresa, id_usuario)) {
            System.out.println("\nEl vehiculo se logró registrar exitosamente!");

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

            band_placa = "";
            band_nro_inscripcion = "";

            System.out.println("actualizamos tabla vehiculo");
            Mostrar_tabla_vehiculo();

            JOptionPane.showMessageDialog(null, "El vehiculo se registro exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear el vehiculo. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar_vehiculo(int id_vehiculo, String marca, String placa, String modelo, String nro_inscripcion, int id_empresa, int id_usuario) {
        System.out.println("ejecutandose la función: modificar_vehiculo");

        if (clase_vehiculo.modificar(id_vehiculo, marca, placa, modelo, nro_inscripcion, id_empresa, id_usuario)) {
            System.out.println("El vehiculo se logró modificar exitosamente!");

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

            band_placa = "";
            band_nro_inscripcion = "";

            System.out.println("actualizamos tabla vehiculo");
            Mostrar_tabla_vehiculo();

            JOptionPane.showMessageDialog(null, "El vehiculo se modificó exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al modificar el vehiculo. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_marca = new javax.swing.JTextField();
        txt_placa = new javax.swing.JTextField();
        txt_modelo = new javax.swing.JTextField();
        txt_nro_inscripcion = new javax.swing.JTextField();
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
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
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
                .addGap(0, 330, Short.MAX_VALUE)
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
                    .addComponent(txt_f_creacion)
                    .addComponent(txt_f_modificacion)
                    .addComponent(txt_usuario, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
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
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/vehiculo.png"))); // NOI18N
        jLabel1.setText("Vehículo");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE)
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

        label_buscar.setText("Buscar");
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
        );

        jPanel5.add(panel_tabla, java.awt.BorderLayout.LINE_START);

        Panel_detalle.setLayout(new java.awt.BorderLayout());

        norte.setBackground(new java.awt.Color(255, 255, 255));
        norte.setPreferredSize(new java.awt.Dimension(458, 40));

        lbl_titulo.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_titulo.setForeground(new java.awt.Color(0, 110, 204));
        lbl_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_titulo.setText("Información del Vehículo");

        javax.swing.GroupLayout norteLayout = new javax.swing.GroupLayout(norte);
        norte.setLayout(norteLayout);
        norteLayout.setHorizontalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
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
            .addGap(0, 590, Short.MAX_VALUE)
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
            .addGap(0, 131, Short.MAX_VALUE)
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
            .addGap(0, 131, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel10, java.awt.BorderLayout.LINE_END);

        jPanel11.setBackground(new java.awt.Color(153, 213, 255));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 153));
        jLabel3.setText("Marca:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 153));
        jLabel4.setText("Placa:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 51, 153));
        jLabel5.setText("Modelo:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 153));
        jLabel6.setText("N. de Inscripcion:");

        txt_marca.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_marca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_marcaKeyTyped(evt);
            }
        });

        txt_placa.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_placa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_placaKeyTyped(evt);
            }
        });

        txt_modelo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_modelo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_modeloKeyTyped(evt);
            }
        });

        txt_nro_inscripcion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_nro_inscripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nro_inscripcionKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_nro_inscripcion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                            .addComponent(txt_modelo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_placa, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txt_marca, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_placa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_nro_inscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
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
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(89, Short.MAX_VALUE))
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
                .addGap(0, 5, Short.MAX_VALUE)
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
        dialog_fecha_creacion.setLocationRelativeTo(vehiculo);
        dialog_fecha_creacion.setModal(true);
        dialog_fecha_creacion.setVisible(true);
    }//GEN-LAST:event_btn_creacionActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        dialog_fecha_creacion.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        System.out.println("\ncrear vehiculo");
        System.out.println("=================");

        System.out.println("inicializar la bandera crear en cero");
        crear0_modificar1 = 0;

        System.out.println("cambiando el titulo");
        lbl_titulo.setText("Nuevo Vehiculo");

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

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
        int fil = tabla_general.getSelectedRow();
        if (fil == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un elemento para Modificar");
        } else {
            System.out.println("\nModifar vehiculo");
            System.out.println("=================");

            System.out.println("inicializar la bandera crear en uno");
            crear0_modificar1 = 1;

            System.out.println("cambiando el titulo");
            lbl_titulo.setText("Modificar Vehiculo");

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

            band_placa = txt_placa.getText().trim();
            band_nro_inscripcion = txt_nro_inscripcion.getText().trim();
        }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        System.out.println("\nEliminar Vehiculo");
        System.out.println("=================");

        int fila;
        int id_vehiculo;
        int respuesta;
        try {
            fila = tabla_general.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar el registro a borrar");
            } else {
                respuesta = JOptionPane.showConfirmDialog(null, "¿Desea eliminar?", "Eliminar", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    m = (DefaultTableModel) tabla_general.getModel();
                    id_vehiculo = Integer.parseInt((String) m.getValueAt(fila, 0));
                    System.out.println("el id_vehiculo que se eliminara es: " + id_vehiculo);
                    clase_vehiculo.eliminar(id_vehiculo);

                    System.out.println("el vehiculo se logró eliminar exitosamente!");
                    System.out.println("ocultar el panel de detalle de datos");
                    Panel_detalle.setVisible(false);
                    JOptionPane.showMessageDialog(null, "El vehiculo se Eliminó exitosamente!");

                    System.out.println("actualizamos tabla vehiculo");
                    Mostrar_tabla_vehiculo();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al eliminar la vehiculo.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_vista_previaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vista_previaActionPerformed
        JOptionPane.showMessageDialog(null, "La opción aun no se encuentra disponible. \n Por favor, consulte con el Administrador del Sistema");
    }//GEN-LAST:event_btn_vista_previaActionPerformed

    private void btn_imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imprimirActionPerformed
        JOptionPane.showMessageDialog(null, "La opción aun no se encuentra disponible. \n Por favor, consulte con el Administrador del Sistema");
    }//GEN-LAST:event_btn_imprimirActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        int respuesta;
        respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea Cancelar el registro del vehiculo?", "Cancelar", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {

            System.out.println("\nCancelar creacion de vehiculo");
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

            System.out.println("actualizamos tabla vehiculo");
            Mostrar_tabla_vehiculo();
        }
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void txt_buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscarKeyReleased
        ResultSet r;
        String bus = txt_buscar.getText();

        try {
            System.out.println("select id_vehiculo, marca, placa from tvehiculo where (marca like '%" + bus + "%'  or placa like '%" + bus + "%' or modelo like '%" + bus + "%' or nro_inscripcion like '%" + bus + "%') and id_empresa = '" + id_empresa_index + "' order by marca asc");
            r = sentencia.executeQuery("select id_vehiculo, marca, placa from tvehiculo where (marca like '%" + bus + "%'  or placa like '%" + bus + "%' or modelo like '%" + bus + "%' or nro_inscripcion like '%" + bus + "%') and id_empresa = '" + id_empresa_index + "' order by marca asc");
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Marca");
            modelo.addColumn("Placa");

            String fila[] = new String[3];
            while (r.next()) {
                fila[0] = r.getString("id_vehiculo").trim();
                fila[1] = r.getString("marca").trim();
                fila[2] = r.getString("placa").trim();
                tabla_general.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_general.setModel(modelo);
            TableColumn columna1 = tabla_general.getColumn("");
            columna1.setPreferredWidth(1);
            TableColumn columna2 = tabla_general.getColumn("Marca");
            columna2.setPreferredWidth(150);
            TableColumn columna3 = tabla_general.getColumn("Placa");
            columna3.setPreferredWidth(150);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla vehiculo - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txt_buscarKeyReleased

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        System.out.println("\npresionó boton Guardar");
        System.out.println("========================");

        System.out.println("capturando datos ingresados");

        String marca = txt_marca.getText().trim();
        String placa = txt_placa.getText().trim();
        String modelo = txt_modelo.getText().trim();
        String nro_inscripcion = txt_nro_inscripcion.getText().trim();
        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;

        if (txt_marca.getText().length() == 0 || txt_placa.getText().length() == 0 || txt_modelo.getText().length() == 0 || txt_nro_inscripcion.getText().length() == 0) {
            System.out.println("\nFalta ingresar los campos Marca, Placa, Modelo y Numero de Inscripcion");
            JOptionPane.showMessageDialog(null, "Los Campos Marca, Placa, Modelo y Numero de Inscripcion son necesarios \n Por favor ingrese estos campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            if (crear0_modificar1 == 0) {

                System.out.println("crear0_modificar1= " + crear0_modificar1);

                if (clase_vehiculo.placa_existente(placa, id_empresa) > 0) {
                    System.out.println("La Placa ya se encuentra registrada.");
                    JOptionPane.showMessageDialog(null, "Esta Placa ya se encuentra Registra.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {

                    if (clase_vehiculo.Numero_inscripcion_existente(nro_inscripcion, id_empresa) > 0) {
                        System.out.println("El Numero de Inscripcion ya se encuentra registrado.");
                        JOptionPane.showMessageDialog(null, "Este Numero de Inscripcion ya se encuentra Registro. \n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {

                        System.out.println("llamamos a la funcion crear_vehiculo");
                        crear_vehiculo(marca, placa, modelo, nro_inscripcion, id_empresa, id_usuario);
                    }
                }
            } else {
                System.out.println("crear0_modificar1= " + crear0_modificar1);

                if (band_placa.equals(txt_placa.getText().trim()) && band_nro_inscripcion.equals(txt_nro_inscripcion.getText().trim())) {

                    System.out.println("La Placa y el Numero de Inscripcion son iguales, se procede con la modificacion");
                    int id_vehiculo = id_vehiculo_global;
                    System.out.println("llamamos a la funcion modificar_vehiculo");
                    modificar_vehiculo(id_vehiculo, marca, placa, modelo, nro_inscripcion, id_empresa, id_usuario);

                } else {

                    if (band_placa.equals(txt_placa.getText().trim()) && !band_nro_inscripcion.equals(txt_nro_inscripcion.getText().trim())) {
                        System.out.println("La Placa es igual y el Numero de Inscripcion NO, se procede a aplicar restricciones");

                        if (clase_vehiculo.Numero_inscripcion_existente(nro_inscripcion, id_empresa) > 0) {
                            System.out.println("El Numero de Inscripcion ya se encuentra registrado.");
                            JOptionPane.showMessageDialog(null, "Este Numero de Inscripcion ya se encuentra Registro. \n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            int id_vehiculo = id_vehiculo_global;
                            System.out.println("llamamos a la funcion modificar_vehiculo");
                            modificar_vehiculo(id_vehiculo, marca, placa, modelo, nro_inscripcion, id_empresa, id_usuario);
                        }
                    } else {
                        if (!band_placa.equals(txt_placa.getText().trim()) && band_nro_inscripcion.equals(txt_nro_inscripcion.getText().trim())) {
                            System.out.println("La Placa NO es igual y el Numero de inscripcion si es igual, se procede a aplicar restricciones");

                            if (clase_vehiculo.placa_existente(placa, id_empresa) > 0) {
                                System.out.println("La Placa ya se encuentra registrada.");
                                JOptionPane.showMessageDialog(null, "Esta Placa ya se encuentra Registra.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                            } else {
                                int id_vehiculo = id_vehiculo_global;
                                System.out.println("llamamos a la funcion modificar_vehiculo");
                                modificar_vehiculo(id_vehiculo, marca, placa, modelo, nro_inscripcion, id_empresa, id_usuario);
                            }
                        } else {
                            System.out.println("La RAZON SOCIAL y el RUC NO son iguales, se procede a aplicar restricciones");

                            if (clase_vehiculo.placa_existente(placa, id_empresa) > 0) {
                                System.out.println("La Placa ya se encuentra registrada.");
                                JOptionPane.showMessageDialog(null, "Esta Placa ya se encuentra Registra.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                            } else {
                                if (clase_vehiculo.Numero_inscripcion_existente(nro_inscripcion, id_empresa) > 0) {
                                    System.out.println("El Numero de Inscripcion ya se encuentra registrado.");
                                    JOptionPane.showMessageDialog(null, "Este Numero de Inscripcion ya se encuentra Registro. \n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    int id_vehiculo = id_vehiculo_global;
                                    System.out.println("llamamos a la funcion modificar_vehiculo");
                                    modificar_vehiculo(id_vehiculo, marca, placa, modelo, nro_inscripcion, id_empresa, id_usuario);
                                }
                            }
                        }
                    }
                }
            }

        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void tabla_generalMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_generalMouseReleased
        System.out.println("\nMostrar datos de Vehiculo");
        System.out.println("===========================");
        
        System.out.println("Limpiar cajas de texto");
        limpiar_caja_texto();
        
        int fila;
        int id_vehiculo;
        fila = tabla_general.getSelectedRow();
        m = (DefaultTableModel) tabla_general.getModel();
        id_vehiculo = Integer.parseInt((String) m.getValueAt(fila, 0));
        System.out.println("el id seleccionado es: " + id_vehiculo);
        id_vehiculo_global = id_vehiculo;
        
        try {
            ResultSet r = sentencia.executeQuery("select v.marca marca, v.placa placa, v.modelo modelo, v.nro_inscripcion nro_inscripcion, v.f_creacion f_creacion, v.f_modificacion f_modificacion, u.nombre nombre_usuario from tvehiculo v, tusuario u where v.id_usuario=u.id_usuario and v.id_vehiculo=" + id_vehiculo + "");
            while (r.next()) {
                System.out.println("Valores extraidos de la BD:");
                
                txt_marca.setText(r.getString("marca").trim());
                System.out.println(txt_marca.getText().trim());
                
                txt_placa.setText(r.getString("placa").trim());
                System.out.println(txt_placa.getText().trim());
                
                txt_modelo.setText(r.getString("modelo").trim());
                System.out.println(txt_modelo.getText().trim());
                
                txt_nro_inscripcion.setText(r.getString("nro_inscripcion").trim());
                System.out.println(txt_nro_inscripcion.getText().trim());
                
                txt_f_creacion.setText(r.getString("f_creacion").trim());
                System.out.println(txt_f_creacion.getText().trim());
                
                txt_f_modificacion.setText(r.getString("f_modificacion"));
                System.out.println(txt_f_modificacion.getText().trim());
                
                txt_usuario.setText(r.getString("nombre_usuario").trim());
                System.out.println(txt_usuario.getText().trim());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos del vehiculo", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        System.out.println("Cambiando el titulo");
        lbl_titulo.setText("Información del Vehiculo");
        
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
       System.out.println("\nMostrar datos de Vehiculo");
        System.out.println("===========================");
        
        System.out.println("Limpiar cajas de texto");
        limpiar_caja_texto();
        
        int fila;
        int id_vehiculo;
        fila = tabla_general.getSelectedRow();
        m = (DefaultTableModel) tabla_general.getModel();
        id_vehiculo = Integer.parseInt((String) m.getValueAt(fila, 0));
        System.out.println("el id seleccionado es: " + id_vehiculo);
        id_vehiculo_global = id_vehiculo;
        
        try {
            ResultSet r = sentencia.executeQuery("select v.marca marca, v.placa placa, v.modelo modelo, v.nro_inscripcion nro_inscripcion, v.f_creacion f_creacion, v.f_modificacion f_modificacion, u.nombre nombre_usuario from tvehiculo v, tusuario u where v.id_usuario=u.id_usuario and v.id_vehiculo=" + id_vehiculo + "");
            while (r.next()) {
                System.out.println("Valores extraidos de la BD:");
                
                txt_marca.setText(r.getString("marca").trim());
                System.out.println(txt_marca.getText().trim());
                
                txt_placa.setText(r.getString("placa").trim());
                System.out.println(txt_placa.getText().trim());
                
                txt_modelo.setText(r.getString("modelo").trim());
                System.out.println(txt_modelo.getText().trim());
                
                txt_nro_inscripcion.setText(r.getString("nro_inscripcion").trim());
                System.out.println(txt_nro_inscripcion.getText().trim());
                
                txt_f_creacion.setText(r.getString("f_creacion").trim());
                System.out.println(txt_f_creacion.getText().trim());
                
                txt_f_modificacion.setText(r.getString("f_modificacion"));
                System.out.println(txt_f_modificacion.getText().trim());
                
                txt_usuario.setText(r.getString("nombre_usuario").trim());
                System.out.println(txt_usuario.getText().trim());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos del vehiculo", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        System.out.println("Cambiando el titulo");
        lbl_titulo.setText("Información del Vehiculo");
        
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

    private void txt_marcaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_marcaKeyTyped
        JTextField caja = txt_marca;
        int limite = 100;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_marcaKeyTyped

    private void txt_placaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_placaKeyTyped
        JTextField caja = txt_placa;
        int limite = 30;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_placaKeyTyped

    private void txt_modeloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_modeloKeyTyped
        JTextField caja = txt_modelo;
        int limite = 30;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_modeloKeyTyped

    private void txt_nro_inscripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nro_inscripcionKeyTyped
        JTextField caja = txt_nro_inscripcion;
        int limite = 50;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_nro_inscripcionKeyTyped

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
    private javax.swing.JTextField txt_buscar;
    private javax.swing.JTextField txt_f_creacion;
    private javax.swing.JTextField txt_f_modificacion;
    private javax.swing.JTextField txt_marca;
    private javax.swing.JTextField txt_modelo;
    private javax.swing.JTextField txt_nro_inscripcion;
    private javax.swing.JTextField txt_placa;
    private javax.swing.JTextField txt_usuario;
    // End of variables declaration//GEN-END:variables
}
