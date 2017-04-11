package system_rysi;

import Clases.cPersonal;
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

/**
 *
 * @author ErC
 */
public class personal extends javax.swing.JPanel {

    private Component personal;
    String controlador_index;
    String DSN_index;
    String user_index;
    String password_index;
    int id_empresa_index;
    int id_usuario_index;
    private Connection conexion;
    private Statement sentencia;
    cPersonal clase_personal = new cPersonal();
    DefaultTableModel m;
    int crear0_modificar1 = 0;
    int id_personal_global;
    String band_dni = "";
    String band_nro_licencia = "";
    String perfil_usuario_index = "";

    public personal(String controlador, String DSN, String user, String password, int id_empresa, int id_usuario, String perfil_usuario) {
        controlador_index = controlador;
        DSN_index = DSN;
        user_index = user;
        password_index = password;
        id_empresa_index = id_empresa;
        id_usuario_index = id_usuario;
        perfil_usuario_index = perfil_usuario;

        System.out.println("\n\nconectando con Personal");
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
        
        System.out.println("mostrar tabla cliente");
        Mostrar_tabla_personal();
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

    private void Mostrar_tabla_personal() {
        try {
            ResultSet r = sentencia.executeQuery("select id_personal, nombre, apellido from tpersonal where id_empresa = '" + id_empresa_index + "' order by nombre asc");
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Nombre");
            modelo.addColumn("Apellido");

            String fila[] = new String[3];
            while (r.next()) {
                fila[0] = r.getString("id_personal").trim();
                fila[1] = r.getString("nombre").trim();
                fila[2] = r.getString("apellido").trim();
                tabla_general.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_general.setModel(modelo);
            TableColumn columna1 = tabla_general.getColumn("");
            columna1.setPreferredWidth(1);
            TableColumn columna2 = tabla_general.getColumn("Nombre");
            columna2.setPreferredWidth(150);
            TableColumn columna3 = tabla_general.getColumn("Apellido");
            columna3.setPreferredWidth(150);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Personal - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
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
            txt_nombre.setEditable(true);
            txt_apellido.setEditable(true);
            txt_dni.setEditable(true);
            txt_direcion.setEditable(true);
            txt_telefono.setEditable(true);
            txt_celular.setEditable(true);
            txt_cargo.setEditable(true);
            txt_fecha_nacimiento.setEditable(true);
            txt_numero_licencia.setEditable(true);
            r_masculino.setEnabled(true);
            r_femenino.setEnabled(true);
        }

        if (funcion.equals("no_editable")) {
            txt_nombre.setEditable(false);
            txt_apellido.setEditable(false);
            txt_dni.setEditable(false);
            txt_direcion.setEditable(false);
            txt_telefono.setEditable(false);
            txt_celular.setEditable(false);
            txt_cargo.setEditable(false);
            txt_fecha_nacimiento.setEditable(false);
            txt_numero_licencia.setEditable(false);
            r_masculino.setEnabled(false);
            r_femenino.setEnabled(false);
            txt_f_creacion.setEditable(false);
            txt_f_modificacion.setEditable(false);
            txt_usuario.setEditable(false);
        }
    }

    private void limpiar_caja_texto() {
        txt_nombre.setText("");
        txt_apellido.setText("");
        txt_dni.setText("");
        txt_direcion.setText("");
        txt_telefono.setText("");
        txt_celular.setText("");
        txt_cargo.setText("");
        txt_fecha_nacimiento.setText("");
        txt_numero_licencia.setText("");

        r_masculino.setSelected(true);

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

    private void crear_personal(String Nombre, String Apellido, String dni, String direccion, String sexo, String telefono, String celular, String cargo, String fecha_nacimiento, String nro_licencia, int id_empresa, int id_usuario) {

        System.out.println("\nejecutandose la función: crear_personal");

        if (clase_personal.Personal_crear(Nombre, Apellido, dni, direccion, sexo, telefono, celular, cargo, fecha_nacimiento, nro_licencia, id_empresa, id_usuario)) {
            System.out.println("\nEl Personal se logró registrar exitosamente!");

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

            band_dni = "";
            band_nro_licencia = "";

            System.out.println("actualizamos tabla personal");
            Mostrar_tabla_personal();

            JOptionPane.showMessageDialog(null, "El personal se registro exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear el personal. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar_personal(int id_personal, String Nombre, String Apellido, String dni, String direccion, String sexo, String telefono, String celular, String cargo, String fecha_nacimiento, String nro_licencia, int id_empresa, int id_usuario) {

        System.out.println("ejecutandose la función: modificar_Personal");

        if (clase_personal.Personal_modificar(id_personal, Nombre, Apellido, dni, direccion, sexo, telefono, celular, cargo, fecha_nacimiento, nro_licencia, id_empresa, id_usuario)) {
            System.out.println("El personal se logró modificar exitosamente!");
            System.out.println(sexo);


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

            band_dni = "";
            band_nro_licencia = "";

            System.out.println("actualizamos tabla personal");
            Mostrar_tabla_personal();

            JOptionPane.showMessageDialog(null, "El personal se modificó exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al modificar el personal. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
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
        radio_sexo = new javax.swing.ButtonGroup();
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
        txt_nombre = new javax.swing.JTextField();
        txt_apellido = new javax.swing.JTextField();
        txt_dni = new javax.swing.JTextField();
        txt_direcion = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        r_masculino = new javax.swing.JRadioButton();
        r_femenino = new javax.swing.JRadioButton();
        jLabel12 = new javax.swing.JLabel();
        txt_telefono = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txt_celular = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txt_cargo = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txt_numero_licencia = new javax.swing.JTextField();
        txt_fecha_nacimiento = new javax.swing.JFormattedTextField();
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

        txt_f_creacion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txt_f_modificacion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

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
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/personal_32_32.png"))); // NOI18N
        jLabel1.setText("Personal");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1029, Short.MAX_VALUE)
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
        jScrollPane1.setViewportView(tabla_general);

        javax.swing.GroupLayout panel_tablaLayout = new javax.swing.GroupLayout(panel_tabla);
        panel_tabla.setLayout(panel_tablaLayout);
        panel_tablaLayout.setHorizontalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        panel_tablaLayout.setVerticalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
        );

        jPanel5.add(panel_tabla, java.awt.BorderLayout.LINE_START);

        Panel_detalle.setLayout(new java.awt.BorderLayout());

        norte.setBackground(new java.awt.Color(255, 255, 255));
        norte.setPreferredSize(new java.awt.Dimension(458, 40));

        lbl_titulo.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_titulo.setForeground(new java.awt.Color(0, 110, 204));
        lbl_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_titulo.setText("Información del Personal");

        javax.swing.GroupLayout norteLayout = new javax.swing.GroupLayout(norte);
        norte.setLayout(norteLayout);
        norteLayout.setHorizontalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 729, Short.MAX_VALUE)
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
            .addGap(0, 729, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        centro.add(jPanel6, java.awt.BorderLayout.PAGE_END);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(153, 213, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(150, 250));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 286, Short.MAX_VALUE)
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
            .addGap(0, 286, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel10, java.awt.BorderLayout.LINE_END);

        jPanel11.setBackground(new java.awt.Color(153, 213, 255));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 153));
        jLabel3.setText("Nombre:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 153));
        jLabel4.setText("Apellido:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 51, 153));
        jLabel5.setText("D.N.I.:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 153));
        jLabel6.setText("Dirección:");

        txt_nombre.setEditable(false);
        txt_nombre.setBackground(new java.awt.Color(255, 255, 255));
        txt_nombre.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nombreKeyTyped(evt);
            }
        });

        txt_apellido.setEditable(false);
        txt_apellido.setBackground(new java.awt.Color(255, 255, 255));
        txt_apellido.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_apellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_apellidoKeyTyped(evt);
            }
        });

        txt_dni.setEditable(false);
        txt_dni.setBackground(new java.awt.Color(255, 255, 255));
        txt_dni.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_dni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_dniKeyTyped(evt);
            }
        });

        txt_direcion.setEditable(false);
        txt_direcion.setBackground(new java.awt.Color(255, 255, 255));
        txt_direcion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_direcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_direcionKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 51, 153));
        jLabel11.setText("Sexo:");

        r_masculino.setBackground(new java.awt.Color(153, 213, 255));
        radio_sexo.add(r_masculino);
        r_masculino.setSelected(true);
        r_masculino.setText("Masculino");
        r_masculino.setEnabled(false);

        r_femenino.setBackground(new java.awt.Color(153, 213, 255));
        radio_sexo.add(r_femenino);
        r_femenino.setText("Femenino");
        r_femenino.setEnabled(false);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 153));
        jLabel12.setText("Teléfono:");

        txt_telefono.setEditable(false);
        txt_telefono.setBackground(new java.awt.Color(255, 255, 255));
        txt_telefono.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_telefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telefonoKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 153));
        jLabel13.setText("Celular:");

        txt_celular.setEditable(false);
        txt_celular.setBackground(new java.awt.Color(255, 255, 255));
        txt_celular.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_celular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_celularKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 51, 153));
        jLabel14.setText("Cargo:");

        txt_cargo.setEditable(false);
        txt_cargo.setBackground(new java.awt.Color(255, 255, 255));
        txt_cargo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_cargo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cargoKeyTyped(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 51, 153));
        jLabel15.setText("F. Nacimiento:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 51, 153));
        jLabel16.setText("N. Licencia:");

        txt_numero_licencia.setEditable(false);
        txt_numero_licencia.setBackground(new java.awt.Color(255, 255, 255));
        txt_numero_licencia.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_numero_licencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_numero_licenciaKeyTyped(evt);
            }
        });

        txt_fecha_nacimiento.setEditable(false);
        txt_fecha_nacimiento.setBackground(new java.awt.Color(255, 255, 255));
        txt_fecha_nacimiento.setFormatterFactory(new javax.swing.JFormattedTextField.AbstractFormatterFactory() {
            public javax.swing.JFormattedTextField.AbstractFormatter getFormatter
            (javax.swing.JFormattedTextField tf)
            { try { return new javax.swing.text.MaskFormatter("##/##/####");
            }
            catch (java.text.ParseException pe){
                pe.printStackTrace();
            }
            return null;
        }
    });

    javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
    jPanel11.setLayout(jPanel11Layout);
    jPanel11Layout.setHorizontalGroup(
        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel11Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                    .addComponent(txt_apellido)
                    .addComponent(txt_direcion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(r_masculino)
                        .addGap(18, 18, 18)
                        .addComponent(r_femenino))
                    .addComponent(txt_telefono, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                    .addComponent(txt_celular)
                    .addComponent(txt_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_cargo)
                    .addComponent(txt_numero_licencia, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(txt_fecha_nacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(67, Short.MAX_VALUE))
    );
    jPanel11Layout.setVerticalGroup(
        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel11Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(txt_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5)
                .addComponent(txt_dni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6)
                .addComponent(txt_direcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel11)
                .addComponent(r_masculino)
                .addComponent(r_femenino))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel12)
                .addComponent(txt_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel13)
                .addComponent(txt_celular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel14)
                .addComponent(txt_cargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel15)
                .addComponent(txt_fecha_nacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel16)
                .addComponent(txt_numero_licencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    jPanel7.add(jPanel11, java.awt.BorderLayout.CENTER);

    javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 729, Short.MAX_VALUE)
    );
    jPanel9Layout.setVerticalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .addGap(0, 144, Short.MAX_VALUE)
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
        dialog_fecha_creacion.setLocationRelativeTo(personal);
        dialog_fecha_creacion.setModal(true);
        dialog_fecha_creacion.setVisible(true);
    }//GEN-LAST:event_btn_creacionActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        dialog_fecha_creacion.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void txt_dniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dniKeyTyped
        JTextField caja = txt_dni;
        ingresar_solo_numeros(caja, evt);
        int limite = 8;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_dniKeyTyped

    private void txt_nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nombreKeyTyped
        JTextField caja = txt_nombre;
        int limite = 100;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_nombreKeyTyped

    private void txt_apellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_apellidoKeyTyped
        JTextField caja = txt_apellido;
        int limite = 100;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_apellidoKeyTyped

    private void txt_direcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_direcionKeyTyped
        JTextField caja = txt_direcion;
        int limite = 200;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_direcionKeyTyped

    private void txt_telefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefonoKeyTyped
        JTextField caja = txt_telefono;
        ingresar_solo_numeros(caja, evt);
        int limite = 15;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_telefonoKeyTyped

    private void txt_celularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_celularKeyTyped
        JTextField caja = txt_celular;
        ingresar_solo_numeros(caja, evt);
        int limite = 15;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_celularKeyTyped

    private void txt_cargoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cargoKeyTyped
        JTextField caja = txt_cargo;
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_cargoKeyTyped

    private void txt_numero_licenciaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_numero_licenciaKeyTyped
        JTextField caja = txt_numero_licencia;
        int limite = 15;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_numero_licenciaKeyTyped

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        System.out.println("\ncrear Personal");
        System.out.println("=================");

        System.out.println("inicializar la bandera crear en cero");
        crear0_modificar1 = 0;

        System.out.println("cambiando el titulo");
        lbl_titulo.setText("Nuevo Personal");

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
            System.out.println("\nModifar Personal");
            System.out.println("=================");

            System.out.println("inicializar la bandera crear en uno");
            crear0_modificar1 = 1;

            System.out.println("cambiando el titulo");
            lbl_titulo.setText("Modificar Personal");

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

            band_dni = txt_dni.getText().trim();
            band_nro_licencia = txt_numero_licencia.getText().trim();
        }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        System.out.println("\nEliminar Personal");
        System.out.println("=================");

        int fila;
        int id_personal;
        int respuesta;
        try {
            fila = tabla_general.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar el registro a borrar");
            } else {
                respuesta = JOptionPane.showConfirmDialog(null, "¿Desea eliminar?", "Eliminar", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    m = (DefaultTableModel) tabla_general.getModel();
                    id_personal = Integer.parseInt((String) m.getValueAt(fila, 0));
                    System.out.println("el id_personal que se eliminara es: " + id_personal);
                    clase_personal.Personal_eliminar(id_personal);

                    System.out.println("el Personal se logró eliminar exitosamente!");
                    System.out.println("ocultar el panel de detalle de datos");
                    Panel_detalle.setVisible(false);
                    JOptionPane.showMessageDialog(null, "El Personal se Eliminó exitosamente!");

                    System.out.println("actualizamos tabla Personal");
                    Mostrar_tabla_personal();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al eliminar la Personal.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void txt_buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscarKeyReleased
        ResultSet r;
        String bus = txt_buscar.getText();

        try {
            System.out.println("select id_personal, nombre, apellido from tpersonal where (nombre like '%" + bus + "%'  or apellido like '%" + bus + "%' or dni like '%" + bus + "%' or direccion like '%" + bus + "%' or sexo like '%" + bus + "%' or telefono like '%" + bus + "%' or celular like '%" + bus + "%' or cargo like '%" + bus + "%' or fecha_nacimiento like '%" + bus + "%' or nro_licencia like '%" + bus + "%') and id_empresa = '" + id_empresa_index + "' order by nombre asc");
            r = sentencia.executeQuery("select id_personal, nombre, apellido from tpersonal where (nombre like '%" + bus + "%'  or apellido like '%" + bus + "%' or dni like '%" + bus + "%' or direccion like '%" + bus + "%' or sexo like '%" + bus + "%' or telefono like '%" + bus + "%' or celular like '%" + bus + "%' or cargo like '%" + bus + "%' or fecha_nacimiento like '%" + bus + "%' or nro_licencia like '%" + bus + "%') and id_empresa = '" + id_empresa_index + "' order by nombre asc");

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Nombre");
            modelo.addColumn("Apellido");

            String fila[] = new String[3];
            while (r.next()) {
                fila[0] = r.getString("id_personal").trim();
                fila[1] = r.getString("nombre").trim();
                fila[2] = r.getString("apellido").trim();
                tabla_general.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_general.setModel(modelo);
            TableColumn columna1 = tabla_general.getColumn("");
            columna1.setPreferredWidth(1);
            TableColumn columna2 = tabla_general.getColumn("Nombre");
            columna2.setPreferredWidth(150);
            TableColumn columna3 = tabla_general.getColumn("Apellido");
            columna3.setPreferredWidth(150);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Personal - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txt_buscarKeyReleased

    private void tabla_generalMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_generalMouseReleased
        System.out.println("\nMostrar datos de Personal");
        System.out.println("===========================");

        System.out.println("Limpiar cajas de texto");
        limpiar_caja_texto();

        int fila;
        int id_personal;
        fila = tabla_general.getSelectedRow();
        m = (DefaultTableModel) tabla_general.getModel();
        id_personal = Integer.parseInt((String) m.getValueAt(fila, 0));
        System.out.println("el id seleccionado es: " + id_personal);
        id_personal_global = id_personal;
        ResultSet r;
        String sexo;

        try {
            r = sentencia.executeQuery("select p.Nombre Nombre, p.Apellido Apellido, p.dni dni, p.direccion direccion, p.sexo sexo, p.telefono telefono, p.celular celular, p.cargo cargo, p.fecha_nacimiento fecha_nacimiento, p.nro_licencia nro_licencia, p.f_creacion f_creacion, p.f_modificacion f_modificacion, u.nombre nombre_usuario from tpersonal p, tusuario u where p.id_usuario=u.id_usuario and p.id_personal=" + id_personal + "");
            while (r.next()) {
                System.out.println("Valores extraidos de la BD:");

                txt_nombre.setText(r.getString("Nombre").trim());
                System.out.println(txt_nombre.getText().trim());

                txt_apellido.setText(r.getString("Apellido").trim());
                System.out.println(txt_apellido.getText().trim());

                txt_dni.setText(r.getString("dni").trim());
                System.out.println(txt_dni.getText().trim());

                txt_direcion.setText(r.getString("direccion").trim());
                System.out.println(txt_direcion.getText().trim());

                sexo = r.getString("sexo").trim();
                if (sexo.equals("Masculino")) {
                    r_masculino.setSelected(true);
                    System.out.println("Masculino");
                }
                if (sexo.equals("Femenino")) {
                    r_femenino.setSelected(true);
                    System.out.println("Femenino");
                }

                txt_telefono.setText(r.getString("telefono").trim());
                System.out.println(txt_telefono.getText().trim());

                txt_celular.setText(r.getString("celular").trim());
                System.out.println(txt_celular.getText().trim());

                txt_cargo.setText(r.getString("cargo").trim());
                System.out.println(txt_cargo.getText().trim());

                txt_fecha_nacimiento.setText(r.getString("fecha_nacimiento").trim());
                System.out.println(txt_fecha_nacimiento.getText().trim());

                txt_numero_licencia.setText(r.getString("nro_licencia").trim());
                System.out.println(txt_numero_licencia.getText().trim());

                txt_f_creacion.setText(r.getString("f_creacion").trim());
                System.out.println(txt_f_creacion.getText().trim());

                txt_f_modificacion.setText(r.getString("f_modificacion"));
                System.out.println(txt_f_modificacion.getText().trim());

                txt_usuario.setText(r.getString("nombre_usuario").trim());
                System.out.println(txt_usuario.getText().trim());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos del Personal", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("Cambiando el titulo");
        lbl_titulo.setText("Información del Personal");

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

    private void btn_vista_previaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vista_previaActionPerformed
        JOptionPane.showMessageDialog(null, "La opción aun no se encuentra disponible. \n Por favor, consulte con el Administrador del Sistema");
    }//GEN-LAST:event_btn_vista_previaActionPerformed

    private void btn_imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imprimirActionPerformed
        JOptionPane.showMessageDialog(null, "La opción aun no se encuentra disponible. \n Por favor, consulte con el Administrador del Sistema");
    }//GEN-LAST:event_btn_imprimirActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        int respuesta;
        respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea Cancelar el registro del Personal?", "Cancelar", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {

            System.out.println("\nCancelar creacion de Personal");
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

            System.out.println("actualizamos tabla Personal");
            Mostrar_tabla_personal();
        }
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        System.out.println("\npresionó boton Guardar");
        System.out.println("========================");

        System.out.println("capturando datos ingresados");

        String nombre = txt_nombre.getText().trim();
        String apellido = txt_apellido.getText().trim();
        String dni = txt_dni.getText().trim();
        String direccion = txt_direcion.getText().trim();
        String sexo = "";
        if (r_masculino.isSelected()) {
            sexo = "Masculino";
        } else if (r_femenino.isSelected()) {
            sexo = "Femenino";
        }
        System.out.println(sexo);
        String telefono = txt_telefono.getText().trim();
        String celular = txt_celular.getText().trim();
        String cargo = txt_cargo.getText().trim();
        String fecha_nacimiento = txt_fecha_nacimiento.getText().trim();
        String nro_licencia = txt_numero_licencia.getText().trim();
        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;

        if (txt_nombre.getText().length() == 0 || txt_apellido.getText().length() == 0 || txt_dni.getText().length() == 0 || txt_direcion.getText().length() == 0) {
            System.out.println("\nFalta ingresar los campos Nombre, Apellido, DNI y Dirección");
            JOptionPane.showMessageDialog(null, "Los campos: \nNombre, \nApellido, \nDNI y \nDirección son necesarios \n\n Por favor ingrese estos campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            if (txt_dni.getText().length() != 8) {
                System.out.println("\n el dni tiene un tamaño diferente a 8");
                JOptionPane.showMessageDialog(null, "El D.N.I. debe tener 8 digitos.\n Por favor ingrese un D.N.I. válido.", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {

                if (crear0_modificar1 == 0) {

                    System.out.println("crear0_modificar1= " + crear0_modificar1);

                    if (clase_personal.dni_existente(dni, id_empresa) > 0) {
                        System.out.println("El DNI ya se encuentra registrado.");
                        JOptionPane.showMessageDialog(null, "El D.N.I. ya se encuentra Registrdo.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {

                        if (nro_licencia.length() == 0) {
                            System.out.println("llamamos a la funcion crear_Personal");
                            crear_personal(nombre, apellido, dni, direccion, sexo, telefono, celular, cargo, fecha_nacimiento, nro_licencia, id_empresa, id_usuario);
                        } else {
                            if (clase_personal.nro_licencia_existente(nro_licencia, id_empresa) > 0) {
                                System.out.println("El Número de Licencia ya se encuentra registrado.");
                                JOptionPane.showMessageDialog(null, "El Número de Licencia ya se encuentra Registrado.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                            } else {
                                System.out.println("llamamos a la funcion crear_Personal");
                                crear_personal(nombre, apellido, dni, direccion, sexo, telefono, celular, cargo, fecha_nacimiento, nro_licencia, id_empresa, id_usuario);
                            }
                        }
                    }
                } else {
                    System.out.println("crear0_modificar1= " + crear0_modificar1);

                    if (band_dni.equals(txt_dni.getText().trim()) && band_nro_licencia.equals(txt_numero_licencia.getText().trim())) {

                        System.out.println("El DNI y el Numero de Licencia son iguales, se procede con la modificacion");
                        int id_personal = id_personal_global;
                        System.out.println("llamamos a la funcion modificar_Personal");
                        modificar_personal(id_personal, nombre, apellido, dni, direccion, sexo, telefono, celular, cargo, fecha_nacimiento, nro_licencia, id_empresa, id_usuario);
                    } else {
                        if (band_dni.equals(txt_dni.getText().trim()) && !band_nro_licencia.equals(txt_numero_licencia.getText().trim())) {
                            System.out.println("El DNI es igual y el Numero de Licencia NO, se procede a aplicar restricciones");

                            if (nro_licencia.length() == 0) {
                                System.out.println("llamamos a la funcion crear_Personal");
                                int id_personal = id_personal_global;
                                modificar_personal(id_personal, nombre, apellido, dni, direccion, sexo, telefono, celular, cargo, fecha_nacimiento, nro_licencia, id_empresa, id_usuario);
                            } else {

                                if (clase_personal.nro_licencia_existente(nro_licencia, id_empresa) > 0) {
                                    System.out.println("El Numero de licencia ya se encuentra registrado.");
                                    JOptionPane.showMessageDialog(null, "El Numero de Licencia ya se encuentra Registrado.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    int id_personal = id_personal_global;
                                    System.out.println("llamamos a la funcion modificar_Personal");
                                    modificar_personal(id_personal, nombre, apellido, dni, direccion, sexo, telefono, celular, cargo, fecha_nacimiento, nro_licencia, id_empresa, id_usuario);
                                }
                            }
                        } else {
                            if (!band_dni.equals(txt_dni.getText().trim()) && band_nro_licencia.equals(txt_numero_licencia.getText().trim())) {
                                System.out.println("El DNI NO es igual y el Numero de Licencia si es igual, se procede a aplicar restricciones");

                                if (clase_personal.dni_existente(dni, id_empresa) > 0) {
                                    System.out.println("El DNI ya se encuentra registrado.");
                                    JOptionPane.showMessageDialog(null, "El D.N.I. ya se encuentra Registrado.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    int id_personal = id_personal_global;
                                    System.out.println("llamamos a la funcion modificar_Personal");
                                    modificar_personal(id_personal, nombre, apellido, dni, direccion, sexo, telefono, celular, cargo, fecha_nacimiento, nro_licencia, id_empresa, id_usuario);
                                }
                            } else {
                                System.out.println("El D.N.I. y el Numero de Licencia NO son iguales, se procede a aplicar restricciones");

                                if (clase_personal.dni_existente(dni, id_empresa) > 0) {
                                    System.out.println("El DNI ya se encuentra registrado.");
                                    JOptionPane.showMessageDialog(null, "El D.N.I. ya se encuentra Registrado.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                                } else {

                                    if (clase_personal.nro_licencia_existente(nro_licencia, id_empresa) > 0) {
                                        System.out.println("El Numero de licencia ya se encuentra registrado.");
                                        JOptionPane.showMessageDialog(null, "El Numero de Licencia ya se encuentra Registrado.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        if (clase_personal.nro_licencia_existente(nro_licencia, id_empresa) > 0) {
                                            System.out.println("El Numero de licencia ya se encuentra registrado.");
                                            JOptionPane.showMessageDialog(null, "El Numero de Licencia ya se encuentra Registrado.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                                        } else {
                                            int id_personal = id_personal_global;
                                            System.out.println("llamamos a la funcion modificar_Personal");
                                            modificar_personal(id_personal, nombre, apellido, dni, direccion, sexo, telefono, celular, cargo, fecha_nacimiento, nro_licencia, id_empresa, id_usuario);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_btn_guardarActionPerformed
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
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JRadioButton r_femenino;
    private javax.swing.JRadioButton r_masculino;
    private javax.swing.ButtonGroup radio_sexo;
    private javax.swing.JTable tabla_general;
    private javax.swing.JTextField txt_apellido;
    private javax.swing.JTextField txt_buscar;
    private javax.swing.JTextField txt_cargo;
    private javax.swing.JTextField txt_celular;
    private javax.swing.JTextField txt_direcion;
    private javax.swing.JTextField txt_dni;
    private javax.swing.JTextField txt_f_creacion;
    private javax.swing.JTextField txt_f_modificacion;
    private javax.swing.JFormattedTextField txt_fecha_nacimiento;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_numero_licencia;
    private javax.swing.JTextField txt_telefono;
    private javax.swing.JTextField txt_usuario;
    // End of variables declaration//GEN-END:variables
}
