package system_rysi;

//import de.javasoft.plaf.synthetica.SyntheticaBlueMoonLookAndFeel;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import splash.InicioAplicacion;

public final class login extends javax.swing.JFrame {

    //inicio de login
    String controlador;
    String DSN;
    String user;
    String password;
    private Connection conexion;
    private Statement sentencia;
    //declaracion de variable privada
    private InicioAplicacion splashFrame;

    //Metodos para asignar un valor y texto al splash
    public login(InicioAplicacion splashFrame) {
        this.splashFrame = splashFrame;
        setProgress(0, "Cargando componentes del Sistema...");
        initComponents();
//        setIconImage(new ImageIcon(getClass().getResource("../imagenes/icono.png")).getImage());
//        Mayusculas();
        setProgress(20, "Conectandose a la Base de Datos...");
        conexion();
        mostrar_combo_nombreempresa();
        setProgress(40, "Cargando Módulos...");
        setProgress(60, "Carga de Modulos de Módulos Terminada...");
        setProgress(80, "Cargando Interfaces...");
        setProgress(90, "Interfaces Cargadas...");
        setProgress(100, "Bienvenido a ErCo System's");
        this.setLocationRelativeTo(null);
    }

    private void setProgress(int porcent, String infornation) {
        splashFrame.getLabel().setText(infornation);
        splashFrame.getProgressBar().setValue(porcent);
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null, "No se Pudo iniciar la aplicación");
        }
    }

    public login() {
        initComponents();
        System.out.println("iniciando conexion");
        conexion();
//        setIconImage(new ImageIcon(getClass().getResource("imagenes\\icono.png")).getImage());

        mostrar_combo_nombreempresa();
//        Mayusculas();
    }

    public void tamaño_de_caja(JTextField caja, KeyEvent evt, int limite) {
        if (caja.getText().length() == limite) {
            evt.consume();
        }
    }

    public void Mayusculas() {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
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
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (InstantiationException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
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

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los archivos de conexion", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los archivos de conexion", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        try {
            sentencia = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear el objeto sentencia", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        System.out.println("==============================");
    }

    private void mostrar_combo_nombreempresa() {
        try {
            ResultSet r = sentencia.executeQuery("select razon_social from tempresa order by razon_social desc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            String resultado;
            modelo1.addElement("");
            while (r.next()) {
                resultado = (r.getString("razon_social").trim());
                modelo1.addElement(resultado);
            }
            cbo_nombre_empresa.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Nombre Empresa", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jcMousePanel1 = new jcMousePanel.jcMousePanel();
        titulo1 = new javax.swing.JLabel();
        titulo2 = new javax.swing.JLabel();
        txt_alias = new javax.swing.JTextField();
        btn_entrar = new javax.swing.JButton();
        txt_contrasena = new javax.swing.JPasswordField();
        titulo3 = new javax.swing.JLabel();
        cbo_nombre_empresa = new javax.swing.JComboBox();
        btn_entrar1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ErCo System's");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(780, 570));
        setUndecorated(true);
        setResizable(false);

        jcMousePanel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/inicio_sesion.png"))); // NOI18N

        titulo1.setFont(new java.awt.Font("Vrinda", 1, 18)); // NOI18N
        titulo1.setForeground(new java.awt.Color(0, 153, 255));
        titulo1.setText("Alias:");

        titulo2.setFont(new java.awt.Font("Vrinda", 1, 18)); // NOI18N
        titulo2.setForeground(new java.awt.Color(0, 153, 255));
        titulo2.setText("Contraseña:");

        txt_alias.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txt_alias.setForeground(new java.awt.Color(0, 0, 153));
        txt_alias.setText("erco");
        txt_alias.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_aliasKeyReleased(evt);
            }
        });

        btn_entrar.setText("Iniciar Sesion");
        btn_entrar.setBorder(null);
        btn_entrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_entrarActionPerformed(evt);
            }
        });

        txt_contrasena.setForeground(new java.awt.Color(0, 51, 153));
        txt_contrasena.setText("erco.123");
        txt_contrasena.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_contrasenaKeyReleased(evt);
            }
        });

        titulo3.setFont(new java.awt.Font("Vrinda", 1, 18)); // NOI18N
        titulo3.setForeground(new java.awt.Color(0, 153, 255));
        titulo3.setText("Empresa:");

        cbo_nombre_empresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cbo_nombre_empresaKeyReleased(evt);
            }
        });

        btn_entrar1.setText("Cancelar");
        btn_entrar1.setBorder(null);
        btn_entrar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_entrar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jcMousePanel1Layout = new javax.swing.GroupLayout(jcMousePanel1);
        jcMousePanel1.setLayout(jcMousePanel1Layout);
        jcMousePanel1Layout.setHorizontalGroup(
            jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel1Layout.createSequentialGroup()
                .addGap(347, 347, 347)
                .addGroup(jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(titulo3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titulo2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titulo1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_alias, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_contrasena, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbo_nombre_empresa, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jcMousePanel1Layout.createSequentialGroup()
                        .addComponent(btn_entrar1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_entrar, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(351, Short.MAX_VALUE))
        );
        jcMousePanel1Layout.setVerticalGroup(
            jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jcMousePanel1Layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(titulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_alias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(titulo2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_contrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(titulo3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_nombre_empresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jcMousePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_entrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_entrar1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(160, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcMousePanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcMousePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_entrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_entrarActionPerformed
        String usu = txt_alias.getText();
        String pass = txt_contrasena.getText();

        int id_usuario = 0;
        String nombre_usuario = "";
        String perfil_usuario = "";
        int id_empresa = 0;
        String nombre_empresa = cbo_nombre_empresa.getSelectedItem().toString().trim();
        ResultSet r;

        if (nombre_empresa.length() > 0) {
            try {
                System.out.println(usu);
                System.out.println(pass);
                r = sentencia.executeQuery("select u.id_usuario, u.nombre, u.perfil, u.id_empresa from tusuario u, tempresa e where u.id_empresa = e.id_empresa and u.alias = '" + txt_alias.getText().trim() + "' and u.contrasenia = '" + txt_contrasena.getText().trim() + "' and e.razon_social = '" + nombre_empresa + "'");

                while (r.next()) {
                    id_usuario = Integer.parseInt(r.getString("id_usuario").trim());
                    nombre_usuario = r.getString("nombre").trim();
                    perfil_usuario = r.getString("perfil").trim();
                    id_empresa = Integer.parseInt(r.getString("id_empresa").trim());
                }


                if (id_usuario != 0 && id_empresa != 0) {
                    index objPrincipio = new index(id_usuario, nombre_usuario, perfil_usuario, id_empresa, nombre_empresa);
                    objPrincipio.setExtendedState(MAXIMIZED_BOTH);
                    objPrincipio.setVisible(true);
                    this.setVisible(false);

                } else {
                    JOptionPane.showMessageDialog(null, "Alias o Contraseña incorrecta. \n Por favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "No se pudo lograr la conexión con la BD", "ERROR", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor seleccione una Empresa", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btn_entrarActionPerformed

    private void txt_contrasenaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_contrasenaKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String usu = txt_alias.getText();
            String pass = txt_contrasena.getText();

            //JOptionPane.showMessageDialog(null, usu+pass);

            int id_usuario = 0;
            String nombre_usuario = "";
            String perfil_usuario = "";
            int id_empresa = 0;
            String nombre_empresa = cbo_nombre_empresa.getSelectedItem().toString();

            try {
                ResultSet r = sentencia.executeQuery("select u.id_usuario, u.nombre, u.perfil, u.id_empresa from tusuario u, tempresa e where u.id_empresa = e.id_empresa and u.alias = '" + txt_alias.getText().trim() + "' and u.contrasenia = '" + txt_contrasena.getText().trim() + "' and e.razon_social = '" + nombre_empresa + "'");

                while (r.next()) {
                    id_usuario = Integer.parseInt(r.getString("id_usuario").trim());
                    nombre_usuario = r.getString("nombre").trim();
                    perfil_usuario = r.getString("perfil").trim();
                    id_empresa = Integer.parseInt(r.getString("id_empresa").trim());
                }

                if (id_usuario != 0 && id_empresa != 0) {
                    index objPrincipio = new index(id_usuario, nombre_usuario, perfil_usuario, id_empresa, nombre_empresa);
                    objPrincipio.setExtendedState(MAXIMIZED_BOTH);
                    objPrincipio.setVisible(true);
                    this.setVisible(false);

                } else {
                    JOptionPane.showMessageDialog(null, "Alias o Contraseña incorrecta. \n Por favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_txt_contrasenaKeyReleased

    private void txt_aliasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_aliasKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String usu = txt_alias.getText();
            String pass = txt_contrasena.getText();

            //JOptionPane.showMessageDialog(null, usu+pass);

            int id_usuario = 0;
            String nombre_usuario = "";
            String perfil_usuario = "";
            int id_empresa = 0;
            String nombre_empresa = cbo_nombre_empresa.getSelectedItem().toString();

            try {
                ResultSet r = sentencia.executeQuery("select u.id_usuario, u.nombre, u.perfil, u.id_empresa from tusuario u, tempresa e where u.id_empresa = e.id_empresa and u.alias = '" + txt_alias.getText().trim() + "' and u.contrasenia = '" + txt_contrasena.getText().trim() + "' and e.razon_social = '" + nombre_empresa + "'");

                while (r.next()) {
                    id_usuario = Integer.parseInt(r.getString("id_usuario").trim());
                    nombre_usuario = r.getString("nombre").trim();
                    perfil_usuario = r.getString("perfil").trim();
                    id_empresa = Integer.parseInt(r.getString("id_empresa").trim());
                }

                if (id_usuario != 0 && id_empresa != 0) {
                    index objPrincipio = new index(id_usuario, nombre_usuario, perfil_usuario, id_empresa, nombre_empresa);
                    objPrincipio.setExtendedState(MAXIMIZED_BOTH);
                    objPrincipio.setVisible(true);
                    this.setVisible(false);

                } else {
                    JOptionPane.showMessageDialog(null, "Alias o Contraseña incorrecta. \n Por favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_txt_aliasKeyReleased

    private void btn_entrar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_entrar1ActionPerformed
        int resp;
        resp = JOptionPane.showConfirmDialog(null, "¿Está seguro de Salir del Sistema?", "Cancelar", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_btn_entrar1ActionPerformed

    private void cbo_nombre_empresaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbo_nombre_empresaKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String usu = txt_alias.getText();
            String pass = txt_contrasena.getText();

            //JOptionPane.showMessageDialog(null, usu+pass);

            int id_usuario = 0;
            String nombre_usuario = "";
            String perfil_usuario = "";
            int id_empresa = 0;
            String nombre_empresa = cbo_nombre_empresa.getSelectedItem().toString();

            try {
                ResultSet r = sentencia.executeQuery("select u.id_usuario, u.nombre, u.perfil, u.id_empresa from tusuario u, tempresa e where u.id_empresa = e.id_empresa and u.alias = '" + txt_alias.getText().trim() + "' and u.contrasenia = '" + txt_contrasena.getText().trim() + "' and e.razon_social = '" + nombre_empresa + "'");

                while (r.next()) {
                    id_usuario = Integer.parseInt(r.getString("id_usuario").trim());
                    nombre_usuario = r.getString("nombre").trim();
                    perfil_usuario = r.getString("perfil").trim();
                    id_empresa = Integer.parseInt(r.getString("id_empresa").trim());
                }

                if (id_usuario != 0 && id_empresa != 0) {
                    index objPrincipio = new index(id_usuario, nombre_usuario, perfil_usuario, id_empresa, nombre_empresa);
                    objPrincipio.setExtendedState(MAXIMIZED_BOTH);
                    objPrincipio.setVisible(true);
                    this.setVisible(false);

                } else {
                    JOptionPane.showMessageDialog(null, "Alias o Contraseña incorrecta. \n Por favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(null, "Ocurrio al cargar el controlador", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }




    }//GEN-LAST:event_cbo_nombre_empresaKeyReleased
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                login main = new login();
                main.setLocationRelativeTo(null);
                main.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_entrar;
    private javax.swing.JButton btn_entrar1;
    private javax.swing.JComboBox cbo_nombre_empresa;
    private jcMousePanel.jcMousePanel jcMousePanel1;
    private javax.swing.JLabel titulo1;
    private javax.swing.JLabel titulo2;
    private javax.swing.JLabel titulo3;
    private javax.swing.JTextField txt_alias;
    private javax.swing.JPasswordField txt_contrasena;
    // End of variables declaration//GEN-END:variables
}
