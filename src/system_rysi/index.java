package system_rysi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;
import javax.swing.JOptionPane;

public class index extends javax.swing.JFrame {

    String controlador;
    String DSN;
    String user;
    String password;
    private Connection conexion;
    private Statement sentencia;
    int id_empresa_index;
    int id_usuario_index;
    String alias_usuario_index;
    String perfil_usuario_index;
    String nombre_usuario_index;
    String nombre_empresa_index;

    public index(int id_usuario, String nombre_usuario, String perfil_usuario, int id_empresa, String nombre_empresa, String alias_usuario) {
        Locale locale = new Locale("es","PE");
        Locale.setDefault(locale);
            
        id_empresa_index = id_empresa;
        id_usuario_index = id_usuario;
        perfil_usuario_index = perfil_usuario;
        nombre_usuario_index = nombre_usuario;
        nombre_empresa_index = nombre_empresa;
        alias_usuario_index = alias_usuario;
        System.out.println("Iniciando aplicación");
        initComponents();
//        setIconImage(new ImageIcon(getClass().getResource("../imagenes/icono.png")).getImage());
        System.out.println("conectando a la base de datos");
        conexion();
        
        reglas_perfil(perfil_usuario);
        label_logo.setText(nombre_empresa);
        label_nombre.setText(nombre_usuario);
        
        Menu_compra_salidamaterial.setVisible(false);
        menu_reportes.setVisible(false);
        menu_backup.setVisible(false);
        menu_obras.setVisible(false);
        jSeparator2.setVisible(false);

    }

    private void conexion() {

        System.out.println("Iniciando conexion al servidor");
        System.out.println("==============================");

        FileReader fichero;
        BufferedReader br;

        try {
            fichero = new FileReader("conexion\\controlador.txt");
            br = new BufferedReader(fichero);
            String controlador_file = br.readLine();
            fichero.close();
            System.out.println("controlador: " + controlador_file);

            controlador = controlador_file;
            Class.forName(controlador).newInstance();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        try {
            fichero = new FileReader("conexion\\dsn.txt");
            br = new BufferedReader(fichero);
            String dsn_file = br.readLine();
            fichero.close();
            System.out.println("DNS        : " + dsn_file);

            DSN = dsn_file;

            fichero = new FileReader("conexion\\user.txt");
            br = new BufferedReader(fichero);
            String user_file = br.readLine();
            fichero.close();
            System.out.println("usuario    : " + user_file);

            user = user_file;

            fichero = new FileReader("conexion\\password.txt");
            br = new BufferedReader(fichero);
            String password_file = br.readLine();
            fichero.close();
            System.out.println("contraseña : " + password_file);

            password = password_file;

            conexion = DriverManager.getConnection(DSN, user, password);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los archivos de conexion", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        try {
            sentencia = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear el objeto sentencia", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("==============================");
    }

    private void reglas_perfil(String perfil_usuario) {

        if (perfil_usuario.equals("Facturas")) {
            //Registros
            menu_cliente.setVisible(true);
            menu_proveedor.setVisible(false);
            menu_personal.setVisible(false);

            menu_documento.setVisible(true);
            menu_igv.setVisible(true);
            menu_vehiculo.setVisible(true);
            menu_empresa_transportes.setVisible(true);
            menu_conductor.setVisible(true);
            menu_tipopago.setVisible(true);

            menu_producto.setVisible(false);
            menu_obras.setVisible(false);
            
            menu_moneda.setVisible(true);
            menu_producto.setVisible(true);

            menu_usuario.setVisible(false);
            menu_empresa.setVisible(false);

            //Factyura - Guia
            Menu_factura_guia.setVisible(true);
            menu_factura.setVisible(true);
            menu_guia.setVisible(true);


            //Otros menus
            Menu_compra_salidamaterial.setVisible(false);
            menu_reportes.setVisible(false);
            menu_backup.setVisible(false);

            //Botones
            btn_Factura.setVisible(true);
            btn_Guias.setVisible(true);
            btn_clientes.setVisible(true);


        }
        if (perfil_usuario.equals("Guias")) {
            //Registros
            menu_cliente.setVisible(true);
            menu_proveedor.setVisible(false);
            menu_personal.setVisible(false);

            menu_documento.setVisible(true);
            menu_igv.setVisible(true);
            menu_vehiculo.setVisible(true);
            menu_empresa_transportes.setVisible(true);
            menu_conductor.setVisible(true);
            menu_tipopago.setVisible(true);

            menu_producto.setVisible(false);
            menu_obras.setVisible(false);

            menu_moneda.setVisible(true);
            menu_producto.setVisible(true);
            
            menu_usuario.setVisible(false);
            menu_empresa.setVisible(false);

            //Factyura - Guia
            Menu_factura_guia.setVisible(true);
            menu_factura.setVisible(true);
            menu_guia.setVisible(true);


            //Otros menus
            Menu_compra_salidamaterial.setVisible(false);
            menu_reportes.setVisible(false);
            menu_backup.setVisible(false);

            //Botones
            btn_Factura.setVisible(true);
            btn_Guias.setVisible(true);
            btn_clientes.setVisible(true);
        }
        
        if (perfil_usuario.equals("Facturas y Guias")) {
            //Registros
            menu_cliente.setVisible(true);
            menu_proveedor.setVisible(false);
            menu_personal.setVisible(false);

            menu_documento.setVisible(true);
            menu_igv.setVisible(true);
            menu_vehiculo.setVisible(true);
            menu_empresa_transportes.setVisible(true);
            menu_conductor.setVisible(true);
            menu_tipopago.setVisible(true);

            menu_producto.setVisible(false);
            menu_obras.setVisible(false);

            menu_moneda.setVisible(true);
            menu_producto.setVisible(true);
            
            menu_usuario.setVisible(false);
            menu_empresa.setVisible(false);

            //Factyura - Guia
            Menu_factura_guia.setVisible(true);
            menu_factura.setVisible(true);
            menu_guia.setVisible(true);


            //Otros menus
            Menu_compra_salidamaterial.setVisible(false);
            menu_reportes.setVisible(false);
            menu_backup.setVisible(false);

            //Botones
            btn_Factura.setVisible(true);
            btn_Guias.setVisible(true);
            btn_clientes.setVisible(true);
        }
        
        if (perfil_usuario.equals("Compras")) {

            //Otros menus
            Menu_registros.setVisible(false);
            Menu_factura_guia.setVisible(false);
            Menu_compra_salidamaterial.setVisible(false);
            menu_reportes.setVisible(false);
            menu_backup.setVisible(false);
        }
        
        if (perfil_usuario.equals("Salidad de Materiales")) {
            //Otros menus
            Menu_registros.setVisible(false);
            Menu_factura_guia.setVisible(false);
            Menu_compra_salidamaterial.setVisible(false);
            menu_reportes.setVisible(false);
            menu_backup.setVisible(false);
        }
        
        if (perfil_usuario.equals("Compras y Salidas de Materiales")) {
            //Otros menus
            Menu_registros.setVisible(false);
            Menu_factura_guia.setVisible(false);
            Menu_compra_salidamaterial.setVisible(false);
            menu_reportes.setVisible(false);
            menu_backup.setVisible(false);
        }
        
        if (perfil_usuario.equals("Reportes")) {
            //Otros menus
            Menu_registros.setVisible(false);
            Menu_factura_guia.setVisible(false);
            Menu_compra_salidamaterial.setVisible(false);
            menu_reportes.setVisible(false);
            menu_backup.setVisible(false);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_cabecera = new javax.swing.JPanel();
        panel_logo = new javax.swing.JPanel();
        label_logo = new javax.swing.JLabel();
        panel_usuario = new javax.swing.JPanel();
        label_bienvenidos = new javax.swing.JLabel();
        label_nombre = new javax.swing.JLabel();
        label_foto_usuario = new javax.swing.JLabel();
        panel_cuerpo = new javax.swing.JPanel();
        panel_menu = new javax.swing.JPanel();
        btn_Factura = new javax.swing.JButton();
        btn_Guias = new javax.swing.JButton();
        btn_clientes = new javax.swing.JButton();
        jcMousePanel1 = new jcMousePanel.jcMousePanel();
        btn_cerrar_sesion = new javax.swing.JButton();
        btn_salir = new javax.swing.JButton();
        panel_cuerpo2 = new javax.swing.JPanel();
        panel_pie_de_pagina = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        panel_lateral_derecha = new javax.swing.JPanel();
        panel_contenido = new javax.swing.JScrollPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        Menu_registros = new javax.swing.JMenu();
        menu_cliente = new javax.swing.JMenuItem();
        menu_proveedor = new javax.swing.JMenuItem();
        menu_personal = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        menu_documento = new javax.swing.JMenuItem();
        menu_igv = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        menu_moneda = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menu_obras = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menu_usuario = new javax.swing.JMenuItem();
        menu_empresa = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        menu_producto = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        Menu_compra_salidamaterial = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        menu_cotizaciones = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        menu_cuenta_banco = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        Menu_factura_guia = new javax.swing.JMenu();
        menu_factura = new javax.swing.JMenuItem();
        menu_tipopago = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        menu_guia = new javax.swing.JMenuItem();
        menu_vehiculo = new javax.swing.JMenuItem();
        menu_empresa_transportes = new javax.swing.JMenuItem();
        menu_conductor = new javax.swing.JMenuItem();
        menu_reportes = new javax.swing.JMenu();
        menu_backup = new javax.swing.JMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ErCo System's");

        panel_cabecera.setPreferredSize(new java.awt.Dimension(798, 50));
        panel_cabecera.setLayout(new java.awt.BorderLayout());

        panel_logo.setBackground(new java.awt.Color(255, 255, 255));
        panel_logo.setPreferredSize(new java.awt.Dimension(600, 70));

        label_logo.setFont(new java.awt.Font("Neuropol X Rg", 0, 26)); // NOI18N
        label_logo.setForeground(new java.awt.Color(0, 110, 204));
        label_logo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        label_logo.setText("RYSI S.A.C.");

        javax.swing.GroupLayout panel_logoLayout = new javax.swing.GroupLayout(panel_logo);
        panel_logo.setLayout(panel_logoLayout);
        panel_logoLayout.setHorizontalGroup(
            panel_logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(label_logo, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );
        panel_logoLayout.setVerticalGroup(
            panel_logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(label_logo, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        panel_cabecera.add(panel_logo, java.awt.BorderLayout.LINE_START);

        panel_usuario.setBackground(new java.awt.Color(255, 255, 255));
        panel_usuario.setPreferredSize(new java.awt.Dimension(350, 70));

        label_bienvenidos.setFont(new java.awt.Font("Candara", 1, 13)); // NOI18N
        label_bienvenidos.setForeground(new java.awt.Color(180, 204, 0));
        label_bienvenidos.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_bienvenidos.setText("Bienvenido:");

        label_nombre.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        label_nombre.setForeground(new java.awt.Color(0, 110, 204));
        label_nombre.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_nombre.setText("ERCO");

        label_foto_usuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_foto_usuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/user_48_48.png"))); // NOI18N
        label_foto_usuario.setPreferredSize(new java.awt.Dimension(32, 50));

        javax.swing.GroupLayout panel_usuarioLayout = new javax.swing.GroupLayout(panel_usuario);
        panel_usuario.setLayout(panel_usuarioLayout);
        panel_usuarioLayout.setHorizontalGroup(
            panel_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_usuarioLayout.createSequentialGroup()
                .addGroup(panel_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_bienvenidos, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                    .addComponent(label_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(label_foto_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panel_usuarioLayout.setVerticalGroup(
            panel_usuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_usuarioLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(label_bienvenidos, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panel_usuarioLayout.createSequentialGroup()
                .addComponent(label_foto_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panel_cabecera.add(panel_usuario, java.awt.BorderLayout.CENTER);

        getContentPane().add(panel_cabecera, java.awt.BorderLayout.NORTH);

        panel_cuerpo.setLayout(new java.awt.BorderLayout());

        panel_menu.setBackground(new java.awt.Color(255, 255, 255));
        panel_menu.setPreferredSize(new java.awt.Dimension(150, 358));

        btn_Factura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/factura_32_32.png"))); // NOI18N
        btn_Factura.setText("Facturas");
        btn_Factura.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_Factura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_FacturaActionPerformed(evt);
            }
        });

        btn_Guias.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guia_32_32.png"))); // NOI18N
        btn_Guias.setText("Guias");
        btn_Guias.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_Guias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GuiasActionPerformed(evt);
            }
        });

        btn_clientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cliente_32_32.png"))); // NOI18N
        btn_clientes.setText("Clientes");
        btn_clientes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_clientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clientesActionPerformed(evt);
            }
        });

        jcMousePanel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/panel.png"))); // NOI18N

        javax.swing.GroupLayout jcMousePanel1Layout = new javax.swing.GroupLayout(jcMousePanel1);
        jcMousePanel1.setLayout(jcMousePanel1Layout);
        jcMousePanel1Layout.setHorizontalGroup(
            jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jcMousePanel1Layout.setVerticalGroup(
            jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 108, Short.MAX_VALUE)
        );

        btn_cerrar_sesion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cerrar_sesion_32_32.png"))); // NOI18N
        btn_cerrar_sesion.setText("Cerrar Sesión");
        btn_cerrar_sesion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_cerrar_sesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cerrar_sesionActionPerformed(evt);
            }
        });

        btn_salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir_32_32.png"))); // NOI18N
        btn_salir.setText("Salir");
        btn_salir.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_menuLayout = new javax.swing.GroupLayout(panel_menu);
        panel_menu.setLayout(panel_menuLayout);
        panel_menuLayout.setHorizontalGroup(
            panel_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_Factura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_Guias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_clientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jcMousePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_salir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_cerrar_sesion, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
        );
        panel_menuLayout.setVerticalGroup(
            panel_menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_menuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Factura)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Guias)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_clientes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cerrar_sesion, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_salir)
                .addGap(147, 147, 147)
                .addComponent(jcMousePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_cuerpo.add(panel_menu, java.awt.BorderLayout.WEST);

        panel_cuerpo2.setLayout(new java.awt.BorderLayout());

        panel_pie_de_pagina.setBackground(new java.awt.Color(255, 255, 255));
        panel_pie_de_pagina.setPreferredSize(new java.awt.Dimension(701, 20));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("ErCo System's");

        javax.swing.GroupLayout panel_pie_de_paginaLayout = new javax.swing.GroupLayout(panel_pie_de_pagina);
        panel_pie_de_pagina.setLayout(panel_pie_de_paginaLayout);
        panel_pie_de_paginaLayout.setHorizontalGroup(
            panel_pie_de_paginaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_pie_de_paginaLayout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 636, Short.MAX_VALUE))
        );
        panel_pie_de_paginaLayout.setVerticalGroup(
            panel_pie_de_paginaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        panel_cuerpo2.add(panel_pie_de_pagina, java.awt.BorderLayout.SOUTH);

        panel_lateral_derecha.setBackground(new java.awt.Color(255, 255, 255));
        panel_lateral_derecha.setPreferredSize(new java.awt.Dimension(68, 258));

        javax.swing.GroupLayout panel_lateral_derechaLayout = new javax.swing.GroupLayout(panel_lateral_derecha);
        panel_lateral_derecha.setLayout(panel_lateral_derechaLayout);
        panel_lateral_derechaLayout.setHorizontalGroup(
            panel_lateral_derechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );
        panel_lateral_derechaLayout.setVerticalGroup(
            panel_lateral_derechaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 475, Short.MAX_VALUE)
        );

        panel_cuerpo2.add(panel_lateral_derecha, java.awt.BorderLayout.EAST);

        panel_contenido.setBackground(new java.awt.Color(255, 255, 255));
        panel_cuerpo2.add(panel_contenido, java.awt.BorderLayout.CENTER);

        panel_cuerpo.add(panel_cuerpo2, java.awt.BorderLayout.CENTER);

        getContentPane().add(panel_cuerpo, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Inicio     ");

        jMenuItem16.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/incio_32_32.png"))); // NOI18N
        jMenuItem16.setText("Inicio");
        jMenu1.add(jMenuItem16);

        jMenuItem17.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cerrar_sesion_32_32.png"))); // NOI18N
        jMenuItem17.setText("Cerrar Sesión");
        jMenu1.add(jMenuItem17);

        jMenuItem18.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir_32_32.png"))); // NOI18N
        jMenuItem18.setText("Salir");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem18);

        jMenuBar1.add(jMenu1);

        Menu_registros.setText("Registros         ");

        menu_cliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cliente_32_32.png"))); // NOI18N
        menu_cliente.setText("Cliente");
        menu_cliente.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menu_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_clienteActionPerformed(evt);
            }
        });
        Menu_registros.add(menu_cliente);

        menu_proveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/proveedor_32_32.png"))); // NOI18N
        menu_proveedor.setText("Provedor");
        menu_proveedor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menu_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_proveedorActionPerformed(evt);
            }
        });
        Menu_registros.add(menu_proveedor);

        menu_personal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/personal_32_32.png"))); // NOI18N
        menu_personal.setText("Personal");
        menu_personal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menu_personal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_personalActionPerformed(evt);
            }
        });
        Menu_registros.add(menu_personal);
        Menu_registros.add(jSeparator3);

        menu_documento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/documentos_32_32.png"))); // NOI18N
        menu_documento.setText("Documento");
        menu_documento.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menu_documento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_documentoActionPerformed(evt);
            }
        });
        Menu_registros.add(menu_documento);

        menu_igv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/igv_32_32.png"))); // NOI18N
        menu_igv.setText("I.G.V.");
        menu_igv.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menu_igv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_igvActionPerformed(evt);
            }
        });
        Menu_registros.add(menu_igv);
        Menu_registros.add(jSeparator5);

        menu_moneda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/moneda_32_32.png"))); // NOI18N
        menu_moneda.setText("Monedas");
        menu_moneda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_monedaActionPerformed(evt);
            }
        });
        Menu_registros.add(menu_moneda);
        Menu_registros.add(jSeparator2);

        menu_obras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/obra_32_32.png"))); // NOI18N
        menu_obras.setText("Obras");
        menu_obras.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menu_obras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_obrasActionPerformed(evt);
            }
        });
        Menu_registros.add(menu_obras);
        Menu_registros.add(jSeparator1);

        menu_usuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/usuario_32_32.png"))); // NOI18N
        menu_usuario.setText("Usuario");
        menu_usuario.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menu_usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_usuarioActionPerformed(evt);
            }
        });
        Menu_registros.add(menu_usuario);

        menu_empresa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/empresa_32_32.png"))); // NOI18N
        menu_empresa.setText("Empresa");
        menu_empresa.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menu_empresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_empresaActionPerformed(evt);
            }
        });
        Menu_registros.add(menu_empresa);

        jMenuBar1.add(Menu_registros);

        jMenu2.setText("Materiales y Precios       ");

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compraMaterial_32_32.png"))); // NOI18N
        jMenuItem5.setText("Conpra de Materiales");
        jMenu2.add(jMenuItem5);

        menu_producto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/producto_32_32.png"))); // NOI18N
        menu_producto.setText("Materiales");
        menu_producto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menu_producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_productoActionPerformed(evt);
            }
        });
        jMenu2.add(menu_producto);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salida_material_32_32.png"))); // NOI18N
        jMenuItem3.setText("Salida de Materiales");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        Menu_compra_salidamaterial.setText("Compras y Salidas     ");

        jMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compra_32_32.png"))); // NOI18N
        jMenuItem13.setText("Compra");
        Menu_compra_salidamaterial.add(jMenuItem13);

        jMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salida_material_32_32.png"))); // NOI18N
        jMenuItem14.setText("Salida de Material");
        Menu_compra_salidamaterial.add(jMenuItem14);

        jMenuBar1.add(Menu_compra_salidamaterial);

        menu_cotizaciones.setText("Cotizaciones        ");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cotizacion.png"))); // NOI18N
        jMenuItem1.setText("Cotizaciones");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        menu_cotizaciones.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/forma_de_pago.png"))); // NOI18N
        jMenuItem2.setText("Formas de Pago");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        menu_cotizaciones.add(jMenuItem2);

        menu_cuenta_banco.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/bancos_32_32.png"))); // NOI18N
        menu_cuenta_banco.setText("Cuentas de Bancos");
        menu_cuenta_banco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_cuenta_bancoActionPerformed(evt);
            }
        });
        menu_cotizaciones.add(menu_cuenta_banco);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/tipo_cotizacion_32_32.png"))); // NOI18N
        jMenuItem4.setText("Tipos de Cotizacion");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        menu_cotizaciones.add(jMenuItem4);

        jMenuBar1.add(menu_cotizaciones);

        Menu_factura_guia.setText("Facturas          ");

        menu_factura.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        menu_factura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/factura_32_32.png"))); // NOI18N
        menu_factura.setText("Factura");
        menu_factura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_facturaActionPerformed(evt);
            }
        });
        Menu_factura_guia.add(menu_factura);

        menu_tipopago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/tipopago_32_32.png"))); // NOI18N
        menu_tipopago.setText("Tipo de Pago");
        menu_tipopago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_tipopagoActionPerformed(evt);
            }
        });
        Menu_factura_guia.add(menu_tipopago);

        jMenuBar1.add(Menu_factura_guia);

        jMenu3.setText("Guías       ");

        menu_guia.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        menu_guia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guia_32_32.png"))); // NOI18N
        menu_guia.setText("Guia");
        menu_guia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_guiaActionPerformed(evt);
            }
        });
        jMenu3.add(menu_guia);

        menu_vehiculo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/vehiculo.png"))); // NOI18N
        menu_vehiculo.setText("Vehículo");
        menu_vehiculo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menu_vehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_vehiculoActionPerformed(evt);
            }
        });
        jMenu3.add(menu_vehiculo);

        menu_empresa_transportes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/empresa_transportes_32_32.png"))); // NOI18N
        menu_empresa_transportes.setText("Empresa de Transportes");
        menu_empresa_transportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_empresa_transportesActionPerformed(evt);
            }
        });
        jMenu3.add(menu_empresa_transportes);

        menu_conductor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/conductor_32_32.png"))); // NOI18N
        menu_conductor.setText("Conductor");
        menu_conductor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_conductorActionPerformed(evt);
            }
        });
        jMenu3.add(menu_conductor);

        jMenuBar1.add(jMenu3);

        menu_reportes.setText("Reportes     ");
        jMenuBar1.add(menu_reportes);

        menu_backup.setText("Backup");

        jMenuItem15.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/restore_32_32.png"))); // NOI18N
        jMenuItem15.setText("Generar");
        menu_backup.add(jMenuItem15);

        jMenuItem21.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/backup_32_32.png"))); // NOI18N
        jMenuItem21.setText("Restaurar");
        menu_backup.add(jMenuItem21);

        jMenuBar1.add(menu_backup);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menu_empresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_empresaActionPerformed
        panel_contenido.setViewportView(new empresa(controlador, DSN, user, password, perfil_usuario_index));
    }//GEN-LAST:event_menu_empresaActionPerformed

    private void menu_usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_usuarioActionPerformed
        panel_contenido.setViewportView(new usuario(controlador, DSN, user, password, id_empresa_index, perfil_usuario_index));
    }//GEN-LAST:event_menu_usuarioActionPerformed

    private void menu_documentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_documentoActionPerformed
        panel_contenido.setViewportView(new documentos(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_menu_documentoActionPerformed

    private void menu_igvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_igvActionPerformed
        panel_contenido.setViewportView(new igv(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_menu_igvActionPerformed

    private void menu_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_clienteActionPerformed
        panel_contenido.setViewportView(new cliente(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_menu_clienteActionPerformed

    private void btn_clientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clientesActionPerformed
        panel_contenido.setViewportView(new cliente(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_btn_clientesActionPerformed

    private void menu_vehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_vehiculoActionPerformed
        panel_contenido.setViewportView(new vehiculo(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_menu_vehiculoActionPerformed

    private void menu_personalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_personalActionPerformed
        panel_contenido.setViewportView(new personal(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_menu_personalActionPerformed

    private void menu_productoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_productoActionPerformed
        panel_contenido.setViewportView(new ProductoView(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_menu_productoActionPerformed

    private void menu_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_proveedorActionPerformed
        panel_contenido.setViewportView(new proveedor(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_menu_proveedorActionPerformed

    private void menu_obrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_obrasActionPerformed
        panel_contenido.setViewportView(new obras());
    }//GEN-LAST:event_menu_obrasActionPerformed

    private void menu_facturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_facturaActionPerformed
        panel_contenido.setViewportView(new factura(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index, nombre_usuario_index, nombre_empresa_index));
    }//GEN-LAST:event_menu_facturaActionPerformed

    private void btn_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salirActionPerformed
        int resp;
        resp = JOptionPane.showConfirmDialog(null, "¿Está seguro de Salir del Sistema?", "Cancelar", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_btn_salirActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        int resp;
        resp = JOptionPane.showConfirmDialog(null, "¿Está seguro de Salir del Sistema?", "Cancelar", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void menu_guiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_guiaActionPerformed
        panel_contenido.setViewportView(new guia(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_menu_guiaActionPerformed

    private void btn_FacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_FacturaActionPerformed
        panel_contenido.setViewportView(new factura(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index, nombre_usuario_index, nombre_empresa_index));
    }//GEN-LAST:event_btn_FacturaActionPerformed

    private void menu_tipopagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_tipopagoActionPerformed
        panel_contenido.setViewportView(new tipopago(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_menu_tipopagoActionPerformed

    private void menu_empresa_transportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_empresa_transportesActionPerformed
        panel_contenido.setViewportView(new EmpresaTransporte(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_menu_empresa_transportesActionPerformed

    private void menu_conductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_conductorActionPerformed
        panel_contenido.setViewportView(new Conductor(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_menu_conductorActionPerformed

    private void btn_GuiasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GuiasActionPerformed
        panel_contenido.setViewportView(new guia(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_btn_GuiasActionPerformed

    private void btn_cerrar_sesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cerrar_sesionActionPerformed
        int resp;
        resp = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea Cerrar la Sesión?", "", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            login objPrincipio = new login();
            objPrincipio.setVisible(true);
            objPrincipio.setLocationRelativeTo(null);
            this.setVisible(false);
        }
    }//GEN-LAST:event_btn_cerrar_sesionActionPerformed

    private void menu_monedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_monedaActionPerformed
        panel_contenido.setViewportView(new Moneda(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_menu_monedaActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        panel_contenido.setViewportView(new Forma_de_pago_cotizacion(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void menu_cuenta_bancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_cuenta_bancoActionPerformed
        panel_contenido.setViewportView(new Cuenta_banco(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_menu_cuenta_bancoActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        panel_contenido.setViewportView(new Tipo_cotizacion(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        panel_contenido.setViewportView(new Cotizacion(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index));
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        panel_contenido.setViewportView(new SalidaMaterialView(controlador, DSN, user, password, id_empresa_index, id_usuario_index, perfil_usuario_index, alias_usuario_index));
    }//GEN-LAST:event_jMenuItem3ActionPerformed

public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //index main = new index();
                //main.setExtendedState(MAXIMIZED_BOTH);
                //main.setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Menu_compra_salidamaterial;
    private javax.swing.JMenu Menu_factura_guia;
    private javax.swing.JMenu Menu_registros;
    private javax.swing.JButton btn_Factura;
    private javax.swing.JButton btn_Guias;
    private javax.swing.JButton btn_cerrar_sesion;
    private javax.swing.JButton btn_clientes;
    private javax.swing.JButton btn_salir;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private jcMousePanel.jcMousePanel jcMousePanel1;
    private javax.swing.JLabel label_bienvenidos;
    private javax.swing.JLabel label_foto_usuario;
    private javax.swing.JLabel label_logo;
    private javax.swing.JLabel label_nombre;
    private javax.swing.JMenu menu_backup;
    private javax.swing.JMenuItem menu_cliente;
    private javax.swing.JMenuItem menu_conductor;
    private javax.swing.JMenu menu_cotizaciones;
    private javax.swing.JMenuItem menu_cuenta_banco;
    private javax.swing.JMenuItem menu_documento;
    private javax.swing.JMenuItem menu_empresa;
    private javax.swing.JMenuItem menu_empresa_transportes;
    private javax.swing.JMenuItem menu_factura;
    private javax.swing.JMenuItem menu_guia;
    private javax.swing.JMenuItem menu_igv;
    private javax.swing.JMenuItem menu_moneda;
    private javax.swing.JMenuItem menu_obras;
    private javax.swing.JMenuItem menu_personal;
    private javax.swing.JMenuItem menu_producto;
    private javax.swing.JMenuItem menu_proveedor;
    private javax.swing.JMenu menu_reportes;
    private javax.swing.JMenuItem menu_tipopago;
    private javax.swing.JMenuItem menu_usuario;
    private javax.swing.JMenuItem menu_vehiculo;
    private javax.swing.JPanel panel_cabecera;
    private javax.swing.JScrollPane panel_contenido;
    private javax.swing.JPanel panel_cuerpo;
    private javax.swing.JPanel panel_cuerpo2;
    private javax.swing.JPanel panel_lateral_derecha;
    private javax.swing.JPanel panel_logo;
    private javax.swing.JPanel panel_menu;
    private javax.swing.JPanel panel_pie_de_pagina;
    private javax.swing.JPanel panel_usuario;
    // End of variables declaration//GEN-END:variables
}
