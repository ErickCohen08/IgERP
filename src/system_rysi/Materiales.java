package system_rysi;

//Clases
//import Clases.TextAreaEditor;
//import Clases.TextAreaRenderer;
import Clases.cMoneda;

import Clases.cProducto;

import Clases.cProducto_detalle;
import Clases.cProducto_tipo;
import Clases.cProveedor;
import Clases.cUnidad_medida;
import database.AccesoDB;

//Otros
import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
//import javax.swing.table.TableColumnModel;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author ErC
 */
public class Materiales extends javax.swing.JPanel {

    //datos de conexion
    String controlador_index;
    String DSN_index;
    String user_index;
    String password_index;
    private Connection conexion;
    private Statement sentencia;
    int id_empresa_index;
    int id_usuario_index;
    String perfil_usuario_index = "";
    //Clases
    cProducto clase_producto = new cProducto();
    cProducto_detalle clase_producto_detalle = new cProducto_detalle();
    cProveedor clase_proveedor = new cProveedor();
    cMoneda clase_moneda = new cMoneda();
    cUnidad_medida clase_unidadMedida = new cUnidad_medida();
    cProducto_tipo clase_productotipo = new cProducto_tipo();
    //Banderas
    DefaultTableModel m;
    int band_index = 0; //sive para saber si estamos creando o mostrando la informacion de la factura, para desactivar funciones de visualizacion de un nuevo detalle
    int band_cbo_unidadmedida = 0;
    int band_cbo_tipoproducto = 0;
    int band_cbo_proveedor = 0;
    int band_cbo_moneda_proveedor = 0;
    int band_cbo_moneda_material = 0;
    int band_mantenimiento_producto_detalle = 0;
    int crear0_modificar1_producto = 0;
    int crear0_modificar1_producto_detalle = 0;
    int band_crear = 0;         //Sirve para saber si se preciono el boton crear
    int band_modificar = 0;     //Sirve para saber si se preciono el boton modificar
    String cbo_moneda = "";
    private Component producto;
    //id globales
    int id_producto_global;
    int id_producto_detalle_global;
    int id_unidad_global;
    int id_productotipo_global;
    int id_proveedor_global;
    int id_moneda_global;
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public Materiales(String controlador, String DSN, String user, String password, int id_empresa, int id_usuario, String perfil_usuario) {
        controlador_index = controlador;
        DSN_index = DSN;
        user_index = user;
        password_index = password;
        id_empresa_index = id_empresa;
        id_usuario_index = id_usuario;
        perfil_usuario_index = perfil_usuario;

        System.out.println("\n\nconectando con Producto");
        initComponents();

        System.out.println("iniciando conexion");
        conexion();

        System.out.println("ocultar el panel de detalle de datos");
        Panel_detalle.setVisible(false);

        System.out.println("activando la función de letra Mayúsculas");
        Activar_letras_Mayusculas();

        System.out.println("Llenar combo Unidad");
        AutoCompleteDecorator.decorate(this.cbo_unidad);

        System.out.println("Llenar combo Tipo de Producto");
        AutoCompleteDecorator.decorate(this.cbo_tipoproducto);

        System.out.println("Llenar combo Proveedor");
        AutoCompleteDecorator.decorate(this.cbo_proveedor);

        System.out.println("Llenar combo Moneda");
        AutoCompleteDecorator.decorate(this.cbo_moneda_proveedor);

        if (perfil_usuario_index.equals("Solo Lectura")) {
            btn_nuevo.setVisible(false);
            btn_modificar.setVisible(false);
            btn_eliminar.setVisible(false);
        }

        System.out.println("Mostrar Tabla Producto");
        mostrar_tabla_producto("");
        mostrar_combo_proveedor_buscar();
        mostrar_combo_tipomaterial_buscar();
        band_index = 0;
    }

    //Utilidades
    private void conexion() {
        System.out.println("Iniciando conexion al servidor");
        System.out.println("==============================");

        try {
            conexion = AccesoDB.getConnection();
            sentencia = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(), "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        System.out.println("==============================");
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
            //Material
            txt_codigo.setEditable(true);
            txt_descripcion.setEditable(true);

            //Caracteristicas
            txt_marca.setEditable(true);
            txt_modelo.setEditable(true);
            txt_peso.setEditable(true);

            //Tipo
            cbo_unidad.setEditable(true);
            cbo_unidad.setEnabled(true);
            txt_unidad_codigo.setEditable(false);
            cbo_tipoproducto.setEditable(true);
            cbo_tipoproducto.setEnabled(true);

            //Precio
            cbo_moneda_material.setEditable(true);
            cbo_moneda_material.setEnabled(true);
            txt_unidad_precio_unitario.setEditable(true);
            txt_unidad_precio_manoobra.setEditable(true);
            txt_unidad_precio_material.setEditable(true);
            txt_unidad_precio_equipo.setEditable(true);

            txt_referencia_precio.setEditable(true);
            txt_descripcion_coloquial.setEditable(true);

            //Detalle
            cbo_proveedor.setEditable(true);
            cbo_proveedor.setEnabled(true);
            cbo_moneda_proveedor.setEditable(true);
            cbo_moneda_proveedor.setEnabled(true);
            txt_precio.setEditable(true);

            //ventana Crear Proveedor
            txt_razon_social_proveedor_crear.setEditable(true);
            txt_ruc_proveedor_crear.setEditable(true);
            txt_direccion_proveedor_crear.setEditable(true);
            txt_telefono_proveedor_crear.setEditable(true);
            txt_celular_proveedor_crear.setEditable(true);
            txt_correo_proveedor_crear.setEditable(true);

            //ventana Crear Moneda
            txt_crear_moneda_nombre.setEditable(true);
            txt_crear_moneda_simbolo.setEditable(true);

            //ventana Crear Unidad de Medida
            txt_crear_unidadmedida_codigo.setEditable(true);
            txt_crear_unidadmedida_descripcion.setEditable(true);

            //ventana Crear Tipo de Matrial
            txt_crear_tipomaterial_descripcion.setEditable(true);

            //Buscar
            txt_buscar_proveedor.setEditable(true);

        }

        if (funcion.equals("no_editable")) {
            //Material
            txt_codigo.setEditable(false);
            txt_descripcion.setEditable(false);

            //Caracteristicas
            txt_marca.setEditable(false);
            txt_modelo.setEditable(false);
            txt_peso.setEditable(false);

            //Tipo
            cbo_unidad.setEditable(false);
            cbo_unidad.setEnabled(false);
            txt_unidad_codigo.setEditable(false);
            cbo_tipoproducto.setEditable(false);
            cbo_tipoproducto.setEnabled(false);

            //Precio
            cbo_moneda_material.setEditable(false);
            cbo_moneda_material.setEnabled(false);
            txt_unidad_precio_unitario.setEditable(false);

            txt_unidad_precio_manoobra.setEditable(false);
            txt_unidad_precio_material.setEditable(false);
            txt_unidad_precio_equipo.setEditable(false);

            txt_referencia_precio.setEditable(false);
            txt_descripcion_coloquial.setEditable(false);

            //Detalle
            cbo_proveedor.setEditable(false);
            cbo_proveedor.setEnabled(false);
            cbo_moneda_proveedor.setEditable(false);
            cbo_moneda_proveedor.setEnabled(false);
            txt_precio.setEditable(false);

            //ventana Crear Proveedor
            txt_razon_social_proveedor_crear.setEditable(false);
            txt_ruc_proveedor_crear.setEditable(false);
            txt_direccion_proveedor_crear.setEditable(false);
            txt_telefono_proveedor_crear.setEditable(false);
            txt_celular_proveedor_crear.setEditable(false);
            txt_correo_proveedor_crear.setEditable(false);

            //ventana Crear Moneda
            txt_crear_moneda_nombre.setEditable(false);
            txt_crear_moneda_simbolo.setEditable(false);

            //ventana Crear Unidad de Medida
            txt_crear_unidadmedida_codigo.setEditable(false);
            txt_crear_unidadmedida_descripcion.setEditable(false);

            //ventana Crear Tipo de Matrial
            txt_crear_tipomaterial_descripcion.setEditable(false);

            //Buscar
            txt_buscar_proveedor.setEditable(false);

            //Ventana fecha de creacion
            txt_f_creacion.setEditable(false);
            txt_f_modificacion.setEditable(false);
            txt_usuario.setEditable(false);
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

    private void tamaño_de_caja(JTextField caja, KeyEvent evt, int limite) {
        if (caja.getText().length() == limite) {
            evt.consume();
        }
    }

    private void activar_barra_herramientas(String funcion) {
        if (funcion.equals("activar")) {
            btn_nuevo.setEnabled(true);
            btn_modificar.setEnabled(true);
            btn_eliminar.setEnabled(true);
            txt_buscar.setEnabled(true);
            cbo_tipomaterial_buscar.setEnabled(true);
            cbo_proveedor_buscar.setEnabled(true);
        }

        if (funcion.equals("desactivar")) {
            btn_nuevo.setEnabled(false);
            btn_modificar.setEnabled(false);
            btn_eliminar.setEnabled(false);
            txt_buscar.setEnabled(false);
            cbo_tipomaterial_buscar.setEnabled(false);
            cbo_proveedor_buscar.setEnabled(false);

        }
    }

    private void mostrar_botones(String funcion) {
        if (funcion.equals("mostrar")) {
            //Buscar
            btn_buscar_proveedor.setVisible(true);

            //Crear
            btn_nueva_unidad.setVisible(true);
            btn_nuevo_tipo_producto.setVisible(true);
            btn_nueva_moneda_material.setVisible(true);
            btn_nuevo_proveedor.setVisible(true);
            btn_nuevo_moneda.setVisible(true);
            btn_nuevo_detalle.setVisible(true);
        }

        if (funcion.equals("ocultar")) {
            //Buscar
            btn_buscar_proveedor.setVisible(false);

            //Crear
            btn_nueva_unidad.setVisible(false);
            btn_nuevo_tipo_producto.setVisible(false);
            btn_nueva_moneda_material.setVisible(false);
            btn_nuevo_proveedor.setVisible(false);
            btn_nuevo_moneda.setVisible(false);
            btn_nuevo_detalle.setVisible(false);

            //botones de control
            btn_guardar.setVisible(false);
            btn_cancelar.setVisible(false);
        }
    }

    private void tamaño_de_caja_jtextarea(JTextArea caja, KeyEvent evt, int limite) {
        if (caja.getText().length() == limite) {
            evt.consume();
        }
    }

    private void mostrar_datos_producto(int id_producto) {
        System.out.println("\nMostrar datos de Producto");
        System.out.println("===========================");

        System.out.println("Limpiar cajas de texto");
        limpiar_caja_texto();

        System.out.println("el id_producto seleccionado es: " + id_producto);
        id_producto_global = id_producto;

        ResultSet r;
        //Material
        String codigo = "";
        String descripcion = "";

        //Caracteristicas
        String modelo = "";
        String peso = "";
        String marca = "";

        //Tipo
        String nombre_unidad = "";
        String codigo_unidad = "";
        String descripcion_tipo = "";

        //Precio
        String moneda = "";
        String precio_promedio = "";

        String precio_manoobra = "";
        String precio_material = "";
        String precio_equipo = "";

        String referencia_precio = "";
        String descripcion_coloquial = "";

        //Datos de creacion
        String f_creacion = "";
        String f_modificacion = "";
        String nombre_usuario = "";

        try {
            r = sentencia.executeQuery("select\n"
                    + "p.codigo as codigo,\n"
                    + "p.descripcion as descripcion,\n"
                    + "p.modelo as modelo,\n"
                    + "p.peso as peso,\n"
                    + "p.marca as marca,\n"
                    + "up.descripcion as nombre_unidad,\n"
                    + "up.codigo as codigo_unidad,\n"
                    + "pt.descripcion as descripcion_tipo,\n"
                    + "m.nombre as moneda,\n"
                    + "p.precio_promedio as precio_promedio,\n"
                    + "p.precio_manoobra as precio_manoobra,\n"
                    + "p.precio_material as precio_material,\n"
                    + "p.precio_equipo as precio_equipo,\n"
                    + "p.referencia_precio as referencia_precio,\n"
                    + "p.descripcion_coloquial as descripcion_coloquial,\n"
                    + "p.f_creacion,\n"
                    + "p.f_modificacion,\n"
                    + "u.nombre as nombre_usuario\n"
                    + "from \n"
                    + "TProducto as p, \n"
                    + "TUnidad_producto as up,\n"
                    + "TProducto_tipo as pt,\n"
                    + "TMoneda as m,\n"
                    + "TUsuario as u\n"
                    + "where\n"
                    + "p.id_unidad = up.id_unidad and \n"
                    + "p.id_productotipo = pt.id_productotipo and\n"
                    + "p.id_moneda = m.id_moneda and\n"
                    + "p.id_usuario = u.id_usuario and \n"
                    + "p.id_producto = '" + id_producto + "'");
            while (r.next()) {
                //Material
                codigo = r.getString("codigo").trim();
                descripcion = r.getString("descripcion").trim();

                //Caracteristicas
                modelo = r.getString("modelo").trim();
                peso = r.getString("peso").trim();
                marca = r.getString("marca").trim();

                //Tipo
                nombre_unidad = r.getString("nombre_unidad").trim();
                codigo_unidad = r.getString("codigo_unidad").trim();
                descripcion_tipo = r.getString("descripcion_tipo").trim();

                //Precio
                moneda = r.getString("moneda").trim();
                precio_promedio = r.getString("precio_promedio").trim();

                precio_manoobra = r.getString("precio_manoobra").trim();
                precio_material = r.getString("precio_material").trim();
                precio_equipo = r.getString("precio_equipo").trim();

                referencia_precio = r.getString("referencia_precio").trim();
                descripcion_coloquial = r.getString("descripcion_coloquial").trim();

                //Datos de creacion
                f_creacion = r.getString("f_creacion").trim();
                f_modificacion = r.getString("f_modificacion").trim();
                nombre_usuario = r.getString("nombre_usuario").trim();

            }

            System.out.println("Cambiando el titulo");
            lbl_titulo.setText("Información del Material");

            //Material
            txt_codigo.setText(codigo);
            txt_descripcion.setText(descripcion);

            //Caracteristicas
            txt_modelo.setText(modelo);
            txt_peso.setText(peso);
            txt_marca.setText(marca);

            //Tipo
            txt_unidad_codigo.setText(codigo_unidad);
            mostrar_combo_unidadmedida_buscar(nombre_unidad);
            mostrar_combo_tipomaterial_buscar(descripcion_tipo);

            //Precio
            mostrar_combo_moneda_material_buscar(moneda);

            float precio = Float.parseFloat(precio_promedio);
            BigDecimal precio_decimal = new BigDecimal(precio);
            precio_decimal = precio_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            float precio_promedio1 = (float) precio_decimal.doubleValue();
            txt_unidad_precio_unitario.setText("" + precio_promedio1);

            float precio_manoobra1 = Float.parseFloat(precio_manoobra);
            BigDecimal precio_manoobra1_decimal = new BigDecimal(precio_manoobra1);
            precio_manoobra1_decimal = precio_manoobra1_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            float precio_manoobra2 = (float) precio_manoobra1_decimal.doubleValue();
            txt_unidad_precio_manoobra.setText("" + precio_manoobra2);

            float precio_material1 = Float.parseFloat(precio_material);
            BigDecimal precio_material1_decimal = new BigDecimal(precio_material1);
            precio_material1_decimal = precio_material1_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            float precio_material2 = (float) precio_material1_decimal.doubleValue();
            txt_unidad_precio_material.setText("" + precio_material2);

            float precio_equipo1 = Float.parseFloat(precio_equipo);
            BigDecimal precio_equipo1_decimal = new BigDecimal(precio_equipo1);
            precio_equipo1_decimal = precio_equipo1_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            float precio_equipo2 = (float) precio_equipo1_decimal.doubleValue();
            txt_unidad_precio_equipo.setText("" + precio_equipo2);

            txt_referencia_precio.setText(referencia_precio);
            txt_descripcion_coloquial.setText(descripcion_coloquial);

            //Datos de creacion
            txt_f_creacion.setText(f_creacion);
            txt_f_modificacion.setText(f_modificacion);
            txt_usuario.setText(nombre_usuario);

            //activar banderas
            band_mantenimiento_producto_detalle = 1;

            //Mostrar tabla de detalle de guia
            mostrar_tabla_producto_detalle(id_producto);
            System.out.println("Desactivar las cajas de texto para el registro");
            activar_caja_texto("no_editable");

            System.out.println("Ocultar botones: guardar, cancelar y otros");
            mostrar_botones("ocultar");

            System.out.println("Mostrar botones: vista previa, imprimir, creacion");
            btn_vista_previa.setVisible(true);
            btn_imprimir.setVisible(true);
            btn_creacion.setVisible(true);

            System.out.println("Activar barra de herramientas");
            activar_barra_herramientas("activar");

            System.out.println("Ocultar Panel Detalle");
            panel_nuevo_detalle.setVisible(false);

            System.out.println("Mostrar el panel de detalle de datos");
            Panel_detalle.setVisible(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos de la Factura", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Mostrar tablas
    private void mostrar_tabla_producto(String consulta) {
        ResultSet r;

        try {
            if (consulta.equals("")) {
                r = sentencia.executeQuery("select id_producto, descripcion from tproducto where guardado = '1' order by descripcion asc");
            } else {
                r = sentencia.executeQuery(consulta);
            }

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Descripcion");

            String id_producto;
            String descripcion;

            String fila[] = new String[2];
            while (r.next()) {
                id_producto = r.getString("id_producto");
                descripcion = r.getString("descripcion");

                if (id_producto.trim().length() > 0 && descripcion.trim().length() > 0) {
                    fila[0] = id_producto.trim();
                    fila[1] = descripcion.trim();
                    modelo.addRow(fila);
                }
            }

            tabla_general.setRowHeight(35);
            tabla_general.setModel(modelo);

            TableColumn columna1 = tabla_general.getColumn("");
            columna1.setPreferredWidth(0);
            TableColumn columna2 = tabla_general.getColumn("Descripcion");
            columna2.setPreferredWidth(1000);

        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Producto - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, "No existen Datos que cumplan con esta condición", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_tabla_proveedor(String consulta) {
        ResultSet r;

        try {
            if (consulta.equals("")) {
                r = sentencia.executeQuery("select id_proveedor, razon_social, direccion, ruc from tproveedor order by razon_social asc");
            } else {
                r = sentencia.executeQuery(consulta);
            }

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Razon Social");
            modelo.addColumn("Direccion");
            modelo.addColumn("R.U.C.");

            String fila[] = new String[4];
            while (r.next()) {
                fila[0] = r.getString("id_proveedor").trim();
                fila[1] = r.getString("razon_social").trim();
                fila[2] = r.getString("direccion").trim();
                fila[3] = r.getString("ruc").trim();
                tabla_cliente_buscar.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_cliente_buscar.setModel(modelo);
            TableColumn columna1 = tabla_cliente_buscar.getColumn("");
            columna1.setPreferredWidth(0);
            TableColumn columna2 = tabla_cliente_buscar.getColumn("Razon Social");
            columna2.setPreferredWidth(300);
            TableColumn columna3 = tabla_cliente_buscar.getColumn("Direccion");
            columna3.setPreferredWidth(300);
            TableColumn columna4 = tabla_cliente_buscar.getColumn("R.U.C.");
            columna4.setPreferredWidth(150);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Proveedor - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_tabla_producto_detalle_vacia() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("");
        modelo.addColumn("Proveedor");
        modelo.addColumn("Moneda");
        modelo.addColumn("Precio");
        tabla_detalle.setModel(modelo);
        TableColumn columna1 = tabla_detalle.getColumn("");
        columna1.setPreferredWidth(0);
        TableColumn columna2 = tabla_detalle.getColumn("Proveedor");
        columna2.setPreferredWidth(700);
        TableColumn columna3 = tabla_detalle.getColumn("Moneda");
        columna3.setPreferredWidth(100);
        TableColumn columna4 = tabla_detalle.getColumn("Precio");
        columna4.setPreferredWidth(100);
    }

    private void mostrar_tabla_producto_detalle(int id_producto) {
        System.out.println("\nEjecutandose mostrar_tabla_producto_detalle");

        try {
            ResultSet r = sentencia.executeQuery("select pd.id_producto_detalle as id_producto_detalle, p.razon_social as proveedor, m.nombre as moneda, pd.precio as precio from TProducto_detalle pd, TProveedor p, TMoneda m where pd.id_proveedor = p.id_proveedor and pd.id_moneda = m.id_moneda and  pd.id_producto ='" + id_producto + "'");
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Proveedor");
            modelo.addColumn("Moneda");
            modelo.addColumn("Precio");

            float precio;

            String fila[] = new String[4];
            while (r.next()) {

                fila[0] = r.getString("id_producto_detalle").trim();
                fila[1] = r.getString("proveedor").trim();
                fila[2] = r.getString("moneda").trim();

                precio = Float.parseFloat(r.getString("precio").trim());
                BigDecimal precio_decimal = new BigDecimal(precio);
                precio_decimal = precio_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                precio = (float) precio_decimal.doubleValue();
                fila[3] = String.valueOf(precio);

                tabla_detalle.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_detalle.setModel(modelo);

            TableColumn columna1 = tabla_detalle.getColumn("");
            columna1.setPreferredWidth(0);

            TableColumn columna2 = tabla_detalle.getColumn("Proveedor");
            columna2.setPreferredWidth(700);

            TableColumn columna3 = tabla_detalle.getColumn("Moneda");
            columna3.setPreferredWidth(300);

            TableColumn columna4 = tabla_detalle.getColumn("Precio");
            columna4.setPreferredWidth(200);

            //Alinear a la derecha
            DefaultTableCellRenderer alineacion1 = new DefaultTableCellRenderer();
            alineacion1.setHorizontalAlignment(SwingConstants.RIGHT);
            tabla_detalle.getColumnModel().getColumn(3).setCellRenderer(alineacion1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Producto detalle - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Limpiar
    private void limpiar_caja_texto() {
        //Material
        txt_codigo.setText("");
        txt_descripcion.setText("");

        //Caracteristicas
        txt_marca.setText("");
        txt_modelo.setText("");
        txt_peso.setText("");
        txt_unidad_codigo.setText("");

        //Moneda
        txt_unidad_precio_unitario.setText("");

        txt_unidad_precio_manoobra.setText("");
        txt_unidad_precio_material.setText("");
        txt_unidad_precio_equipo.setText("");

        txt_referencia_precio.setText("");
        txt_descripcion_coloquial.setText("");

        //Detalle
        txt_precio.setText("");

        //ventana Crear Proveedor
        txt_razon_social_proveedor_crear.setText("");
        txt_ruc_proveedor_crear.setText("");
        txt_direccion_proveedor_crear.setText("");
        txt_telefono_proveedor_crear.setText("");
        txt_celular_proveedor_crear.setText("");
        txt_correo_proveedor_crear.setText("");

        //ventana Crear Moneda
        txt_crear_moneda_nombre.setText("");
        txt_crear_moneda_simbolo.setText("");

        //ventana Crear Unidad de Medida
        txt_crear_unidadmedida_codigo.setText("");
        txt_crear_unidadmedida_descripcion.setText("");

        //ventana Crear Tipo de Matrial
        txt_crear_tipomaterial_descripcion.setText("");

        //Buscar
        txt_buscar_proveedor.setText("");

        //Ventana fecha de creacion
        txt_f_creacion.setText("");
        txt_f_modificacion.setText("");
        txt_usuario.setText("");
    }

    private void limpiar_caja_texto_crear_proveedor() {
        txt_razon_social_proveedor_crear.setText("");
        txt_ruc_proveedor_crear.setText("");
        txt_direccion_proveedor_crear.setText("");
        txt_telefono_proveedor_crear.setText("");
        txt_celular_proveedor_crear.setText("");
        txt_correo_proveedor_crear.setText("");
    }

    private void limpiar_caja_texto_crear_moneda() {
        txt_crear_moneda_nombre.setText("");
        txt_crear_moneda_simbolo.setText("");
    }

    private void limpiar_caja_texto_crear_unidadmedida() {
        txt_crear_unidadmedida_codigo.setText("");
        txt_crear_unidadmedida_descripcion.setText("");
    }

    private void limpiar_caja_texto_tipoproducto() {
        txt_crear_tipomaterial_descripcion.setText("");
    }

    private void limpiar_caja_texto_crear_detalle_producto() {
        txt_precio.setText("");
    }

    //Validaciones
    private static boolean ValidarCorreo(String email) {
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Creaciones
    private void crear_proveedor(String razon_social, String ruc, String direccion, String telefono, String celular, String correo, int id_empresa, int id_usuario) {
        System.out.println("\nejecutandose la función: crear_proveedor");

        if (clase_proveedor.crear(razon_social, ruc, direccion, telefono, celular, correo, id_empresa, id_usuario)) {
            System.out.println("\nEl Proveedor se logró registrar exitosamente!");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_proveedor();

            System.out.println("actualizamos tabla Proveedor");
            mostrar_tabla_proveedor("");

            mostrar_combo_proveedor_buscar(razon_social);

            txt_buscar_proveedor.setText("");
            dialog_crear_proveedor.dispose();

            JOptionPane.showMessageDialog(null, "El Proveedor se registro exitosamente!");
        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear el Proveedor. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear_moneda(String nombre, String simbolo, String moneda_local, int id_empresa, int id_usuario) {
        System.out.println("\nejecutandose la función: crear_moneda");

        if (clase_moneda.crear(nombre, simbolo, moneda_local, id_empresa, id_usuario)) {
            System.out.println("\nLa Moneda se logró registrar exitosamente!");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_moneda();

            System.out.println("cbo_moneda:" + cbo_moneda);
            if (cbo_moneda.equals("cbo_moneda_material")) {
                mostrar_combo_moneda_material_buscar(nombre);
                cbo_moneda = "";
            } else {
                if (cbo_moneda.equals("cbo_moneda_proveedor")) {
                    mostrar_combo_moneda_proveedor_buscar(nombre);
                    cbo_moneda = "";
                }
            }

            dialog_crear_moneda.dispose();
            JOptionPane.showMessageDialog(null, "La Moneda se registró exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear la Moneda.\nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear_unidadmedida(String codigo, String descripcion, int id_empresa, int id_usuario) {
        System.out.println("\nejecutandose la función: crear_unidaddemedida");

        if (clase_unidadMedida.crear(codigo, descripcion, id_empresa, id_usuario)) {
            System.out.println("\nLa Unidad de Medida se logró registrar exitosamente");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_unidadmedida();

            mostrar_combo_unidadmedida_buscar(descripcion);

            dialog_crear_unidadmaterial.dispose();

            JOptionPane.showMessageDialog(null, "El Unidad de Medida del Material se registro exitosamente");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear la Unidad de Medida. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear_tipoproducto(String descripcion, int id_empresa, int id_usuario) {
        System.out.println("\nejecutandose la función: crear_tipoproducto");

        if (clase_productotipo.crear(descripcion, id_empresa, id_usuario)) {
            System.out.println("\nEl tipo de Material se logró registrar exitosamente");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_tipoproducto();

            mostrar_combo_tipomaterial_buscar(descripcion);

            dialog_crear_tipoproducto.dispose();

            JOptionPane.showMessageDialog(null, "El Tipo de Material se registro exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear el Tipo de Material. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear_Producto_Detalle(int id_producto, int id_proveedor, int id_moneda, float precio, int id_empresa, int id_usuario) {
        System.out.println("\nejecutandose la función: crear_Producto_Detalle");

        if (clase_producto_detalle.crear(id_producto, id_proveedor, id_moneda, precio, id_empresa, id_usuario)) {
            System.out.println("\nEl Detalle de Producto se logró registrar exitosamente!");

            panel_nuevo_detalle.setVisible(false);
            btn_nuevo_detalle.setVisible(true);

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_detalle_producto();

            System.out.println("actualizamos tabla producto_detalle");
            mostrar_tabla_producto_detalle(id_producto);

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear el Material_Detalle. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar_Producto_Detalle(int id_producto_detalle, int id_producto, int id_proveedor, int id_moneda, float precio, int id_empresa, int id_usuario) {
        System.out.println("\nejecutandose la función: modificar_Producto_Detalle");

        if (clase_producto_detalle.modificar(id_producto_detalle, id_producto, id_proveedor, id_moneda, precio, id_empresa, id_usuario)) {
            System.out.println("\nEl Detalle de Producto se logró registrar exitosamente!");
            id_producto_detalle_global = 0;
            crear0_modificar1_producto_detalle = 0;

            panel_nuevo_detalle.setVisible(false);
            btn_nuevo_detalle.setVisible(true);

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_detalle_producto();

            System.out.println("actualizamos tabla producto_detalle");
            mostrar_tabla_producto_detalle(id_producto);

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al modificar la Guia_Detalle. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar_Producto(int id_producto, String codigo, String descripcion, String modelo, float peso, String marca, int id_unidad, int id_productotipo, int id_empresa, int id_usuario, int guardado, int id_moneda, float precio_promedio, float precio_manoobra, float precio_material, float precio_equipo, String referencia_precio, String descripcion_coloquial) {
        System.out.println("ejecutandose la función: modificar_Producto");

        if (clase_producto.modificar(id_producto, codigo, descripcion, modelo, peso, marca, id_unidad, id_productotipo, id_empresa, id_usuario, guardado, id_moneda, precio_promedio, precio_manoobra, precio_material, precio_equipo, referencia_precio, descripcion_coloquial)) {
            System.out.println("El Producto se logró modificar exitosamente!");

            operaciones_postModificacion();

            JOptionPane.showMessageDialog(null, "El Material se Guardó exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al Guardar/Modificar El Material.\nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void operaciones_postModificacion() {
        System.out.println("limpiar cajas de texto");
        limpiar_caja_texto();

        System.out.println("desactivar las cajas de texto para el registro");
        activar_caja_texto("no_editable");

        System.out.println("ocultar botones: guardar, cancelar y otros");
        mostrar_botones("ocultar");

        System.out.println("mostrar botones: vista previa, imprimir, creacion");
        btn_vista_previa.setVisible(true);
        btn_imprimir.setVisible(true);
        btn_creacion.setVisible(true);

        System.out.println("activar barra de herramientas");
        activar_barra_herramientas("activar");

        System.out.println("Mostrar tabla guia_detalle_vacia");
        mostrar_tabla_producto_detalle_vacia();

        System.out.println("ocultar el panel de detalle de datos");
        Panel_detalle.setVisible(false);

        System.out.println("actualizamos tabla Producto");
        mostrar_tabla_producto("");

        System.out.println("Inicializamos lo id_globales");
        inicializar_id_global();

        band_index = 0;
        band_cbo_moneda_proveedor = 1;
        band_cbo_moneda_material = 1;
        band_cbo_proveedor = 1;
        band_cbo_tipoproducto = 1;
        band_cbo_unidadmedida = 1;
        band_mantenimiento_producto_detalle = 0;
    }

    private int CapturarId(String campocapturar, String tabla, String campocondicion, String valorcondicion) {
        System.out.println("\nCapturar el " + campocapturar + "");
        System.out.println("===============================");

        System.out.println("Iniciando metodo Capturar id");

        System.out.println("Datos recibidos");
        System.out.println("Valor          :" + campocapturar);
        System.out.println("Tabla          :" + tabla);
        System.out.println("Campo condicion:" + campocondicion);
        System.out.println("Valor condicion:" + valorcondicion);

        int valor = 0;

        try {
            System.out.println("select " + campocapturar + " from " + tabla + " where " + campocondicion + " = '" + valorcondicion + "'");
            ResultSet r = sentencia.executeQuery("select " + campocapturar + " from " + tabla + " where " + campocondicion + " = '" + valorcondicion + "'");
            while (r.next()) {
                valor = Integer.parseInt(r.getString("" + campocapturar + "").trim());
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al obtener el valor del campo " + campocapturar + " de la tabla " + tabla + "", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Valor obtenido:" + valor);
        return valor;

    }

    private void inicializar_id_global() {
        //banderas de creacion
        crear0_modificar1_producto = 0;
        crear0_modificar1_producto_detalle = 0;

        //id globales
        id_producto_global = 0;
        id_producto_detalle_global = 0;
        id_unidad_global = 0;
        id_productotipo_global = 0;
        id_proveedor_global = 0;
        id_moneda_global = 0;
    }

    //Mostrar combos buscar
    private void mostrar_combo_proveedor_buscar() {
        try {
            ResultSet r = sentencia.executeQuery("select p.razon_social from tproveedor p , TProducto_detalle pd where pd.id_proveedor = p.id_proveedor group by p.razon_social order by razon_social asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("razon_social").trim());
                modelo1.addElement(resultado);
            }
            cbo_proveedor_buscar.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Proveedor", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_proveedor() {
        try {
            ResultSet r = sentencia.executeQuery("select razon_social from tproveedor order by razon_social asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("razon_social").trim());
                modelo1.addElement(resultado);
            }
            cbo_proveedor.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Proveedor", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_moneda_proveedor() {
        System.out.println("Ejecuntandose mostrar_combo_conductor");
        try {
            ResultSet r = sentencia.executeQuery("select nombre from tmoneda order by nombre asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("nombre").trim());
                modelo1.addElement(resultado);
            }
            cbo_moneda_proveedor.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Moneda", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_moneda_material() {
        System.out.println("Ejecuntandose mostrar_combo_conductor");
        try {
            ResultSet r = sentencia.executeQuery("select nombre from tmoneda order by nombre asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("nombre").trim());
                modelo1.addElement(resultado);
            }
            cbo_moneda_material.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Moneda", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_unidadmedida() {
        System.out.println("Ejecuntandose mostrar_combo_conductor");
        try {
            ResultSet r = sentencia.executeQuery("select descripcion from TUnidad_producto order by descripcion asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("descripcion").trim());
                modelo1.addElement(resultado);
            }
            cbo_unidad.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Unidad de Medida", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_tipomaterial() {
        try {
            ResultSet r = sentencia.executeQuery("select descripcion from TProducto_tipo order by descripcion asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("descripcion").trim());
                modelo1.addElement(resultado);
            }
            cbo_tipoproducto.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Tipo de Material", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_tipomaterial_buscar() {
        try {
            ResultSet r = sentencia.executeQuery("select pt.descripcion from TProducto_tipo pt, TProducto p where p.id_productotipo = pt.id_productotipo group by pt.descripcion order by descripcion asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("descripcion").trim());
                modelo1.addElement(resultado);
            }
            cbo_tipomaterial_buscar.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Tipo de Material", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Mostrar combos buscar
    private void mostrar_combo_proveedor_buscar(String razon_social) {
        try {
            ResultSet r = sentencia.executeQuery("select razon_social from tproveedor order by razon_social asc");
            DefaultComboBoxModel modelo = new DefaultComboBoxModel();
            modelo.addElement(razon_social);
            String resultado;
            while (r.next()) {
                resultado = (r.getString("razon_social").trim());
                if (!resultado.equals(razon_social)) {
                    modelo.addElement(resultado);
                }
            }
            cbo_proveedor.setModel(modelo);
            band_cbo_proveedor = 1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Proveedor", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_moneda_proveedor_buscar(String nombre) {
        try {
            ResultSet r = sentencia.executeQuery("select nombre from tmoneda order by nombre asc");
            DefaultComboBoxModel modelo = new DefaultComboBoxModel();
            modelo.addElement(nombre);
            String resultado;
            while (r.next()) {
                resultado = (r.getString("nombre").trim());
                if (!resultado.equals(nombre)) {
                    modelo.addElement(resultado);
                }
            }

            cbo_moneda_proveedor.setModel(modelo);
            cbo_moneda = "";
            band_cbo_moneda_proveedor = 1;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Moneda", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_moneda_material_buscar(String nombre) {
        try {
            ResultSet r = sentencia.executeQuery("select nombre from tmoneda order by nombre asc");
            DefaultComboBoxModel modelo = new DefaultComboBoxModel();
            modelo.addElement(nombre);
            String resultado;
            while (r.next()) {
                resultado = (r.getString("nombre").trim());
                if (!resultado.equals(nombre)) {
                    modelo.addElement(resultado);
                }
            }
            cbo_moneda_material.setModel(modelo);
            cbo_moneda = "";
            band_cbo_moneda_material = 1;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Moneda", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_unidadmedida_buscar(String descripcion) {
        try {
            ResultSet r = sentencia.executeQuery("select descripcion from TUnidad_producto order by descripcion asc");
            DefaultComboBoxModel modelo = new DefaultComboBoxModel();
            modelo.addElement(descripcion);
            String resultado;
            while (r.next()) {
                resultado = (r.getString("descripcion").trim());
                if (!resultado.equals(descripcion)) {
                    modelo.addElement(resultado);
                }
            }
            cbo_unidad.setModel(modelo);
            band_cbo_unidadmedida = 1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Unidad de Medida", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_tipomaterial_buscar(String descripcion) {
        try {
            ResultSet r = sentencia.executeQuery("select descripcion from TProducto_tipo order by descripcion asc");
            DefaultComboBoxModel modelo = new DefaultComboBoxModel();
            modelo.addElement(descripcion);
            String resultado;
            while (r.next()) {
                resultado = (r.getString("descripcion").trim());
                if (!resultado.equals(descripcion)) {
                    modelo.addElement(resultado);
                }
            }
            cbo_tipoproducto.setModel(modelo);
            band_cbo_tipoproducto = 1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Tipo de Material", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Obtener
    private String obtener_numerofactura(String id_factura) {
        String numero_factura = "";
        try {
            ResultSet r = sentencia.executeQuery("select numero_factura from tfactura where id_factura ='" + id_factura + "' and id_empresa='" + id_empresa_index + "'");
            while (r.next()) {
                numero_factura = r.getString("numero_factura");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el numero de Factura", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return numero_factura;
    }

    private String obtener_seriefactura(String id_factura) {
        String serie_factura = "";
        try {
            ResultSet r = sentencia.executeQuery("select serie from tdocumentos where id_documento = (select id_documento from tfactura where id_factura = '" + id_factura + "' and id_empresa='" + id_empresa_index + "')");
            while (r.next()) {
                serie_factura = r.getString("serie");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el numero de Serie de la Factura", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return serie_factura;
    }

    private String obtener_codigobusqueda() {
        String codigo_busqueda = "";

        if (txt_buscar.getText().trim().length() >= 1) {
            codigo_busqueda = codigo_busqueda.concat("1");
        } else {
            codigo_busqueda = codigo_busqueda.concat("0");
        }

        if (cbo_tipomaterial_buscar.getSelectedItem().toString().trim().length() >= 1) {
            codigo_busqueda = codigo_busqueda.concat("1");
        } else {
            codigo_busqueda = codigo_busqueda.concat("0");
        }

        if (cbo_proveedor_buscar.getSelectedItem().toString().length() >= 1) {
            codigo_busqueda = codigo_busqueda.concat("1");
        } else {
            codigo_busqueda = codigo_busqueda.concat("0");
        }

        return codigo_busqueda;
    }

    private void ejecucion_de_buscador() {
        String bus_txt = txt_buscar.getText().trim();
        String bus_tipo = cbo_tipomaterial_buscar.getSelectedItem().toString().trim();
        String bus_proveedor = cbo_proveedor_buscar.getSelectedItem().toString().trim();
        String consulta = "";

        //Documentacion (1 es cuando esta seleccionado)
        //TXT	TIPO	PROVEEDOR
        //000
        //001
        //010
        //011
        //100
        //110
        //111
        int band = 0;
        String codigo_busqueda = "";
        codigo_busqueda = obtener_codigobusqueda();

        if (codigo_busqueda.equals("001")) {
            System.out.println("codigo_busqueda: " + codigo_busqueda);
            band = 1;
        }

        if (codigo_busqueda.equals("010")) {
            System.out.println("codigo_busqueda: " + codigo_busqueda);
            band = 2;
        }

        if (codigo_busqueda.equals("011")) {
            System.out.println("codigo_busqueda: " + codigo_busqueda);
            band = 3;
        }

        if (codigo_busqueda.equals("100")) {
            System.out.println("codigo_busqueda: " + codigo_busqueda);
            band = 4;
        }

        if (codigo_busqueda.equals("110")) {
            System.out.println("codigo_busqueda: " + codigo_busqueda);
            band = 5;
        }

        if (codigo_busqueda.equals("111")) {
            System.out.println("codigo_busqueda: " + codigo_busqueda);
            band = 6;
        }

        switch (band) {
            case 0:
                mostrar_tabla_producto(consulta);
                break;

            case 1:
                consulta = "select p.id_producto, p.descripcion from tproducto p, TProducto_detalle pd, TProveedor pro where pd.id_producto = p.id_producto and pd.id_proveedor = pro.id_proveedor and pro.razon_social = '" + bus_proveedor + "' and p.guardado = '1' group by p.id_producto, p.descripcion order by p.descripcion asc";
                System.out.println(consulta);
                mostrar_tabla_producto(consulta);
                break;

            case 2:
                consulta = "select p.id_producto, p.descripcion from tproducto p, TProducto_tipo pt where p.id_productotipo = pt.id_productotipo and  pt.descripcion = '" + bus_tipo + "'and p.guardado = '1' group by p.id_producto, p.descripcion order by p.descripcion asc";
                System.out.println(consulta);
                mostrar_tabla_producto(consulta);
                break;

            case 3:
                consulta = "select p.id_producto,  p.descripcion  from tproducto p, TProducto_tipo pt, TProducto_detalle pd, TProveedor pro where ((p.id_productotipo = pt.id_productotipo and pt.descripcion = '" + bus_tipo + "') and (pd.id_producto = p.id_producto and pd.id_proveedor = pro.id_proveedor and pro.razon_social = '" + bus_proveedor + "')) and p.guardado = '1' group by p.id_producto, p.descripcion  order by p.descripcion asc";
                System.out.println(consulta);
                mostrar_tabla_producto(consulta);
                break;

            case 4:
                consulta = "select p.id_producto, p.descripcion from tproducto p, TUnidad_producto up, TMoneda m where (p.codigo like '%" + bus_txt + "%' or p.descripcion like '%" + bus_txt + "%' or p.marca like '%" + bus_txt + "%' or p.modelo like '%" + bus_txt + "%' or p.referencia_precio like '%" + bus_txt + "%' or p.descripcion_coloquial like '%" + bus_txt + "%' or up.descripcion like '%" + bus_txt + "%' or up.codigo like '%" + bus_txt + "%' or m.nombre like '%" + bus_txt + "%') and  p.id_unidad = up.id_unidad and p.id_moneda = m.id_moneda and p.guardado = '1' group by p.id_producto, p.descripcion order by p.descripcion asc";
                System.out.println(consulta);
                mostrar_tabla_producto(consulta);
                break;

            case 5:
                consulta = "select p.id_producto, p.descripcion\n"
                        + "from tproducto p, TUnidad_producto up, TMoneda m, TProducto_tipo pt\n"
                        + "where\n"
                        + "((p.codigo like '%" + bus_txt + "%' or\n"
                        + "p.descripcion like '%" + bus_txt + "%' or\n"
                        + "p.marca like '%" + bus_txt + "%' or\n"
                        + "p.modelo like '%" + bus_txt + "%' or\n"
                        + "p.referencia_precio like '%" + bus_txt + "%' or\n"
                        + "p.descripcion_coloquial like '%" + bus_txt + "%' or\n"
                        + "up.descripcion like '%" + bus_txt + "%' or\n"
                        + "up.codigo like '%" + bus_txt + "%' or\n"
                        + "m.nombre like '%" + bus_txt + "%'\n"
                        + ") \n"
                        + "and \n"
                        + "p.id_unidad = up.id_unidad and\n"
                        + "p.id_moneda = m.id_moneda\n"
                        + ")  and \n"
                        + "(p.id_productotipo = pt.id_productotipo and pt.descripcion = '" + bus_tipo + "')\n"
                        + "and p.guardado = '1'\n"
                        + "group by p.id_producto, p.descripcion\n"
                        + "order by p.descripcion asc";
                System.out.println(consulta);
                mostrar_tabla_producto(consulta);

                break;

            case 6:
                consulta = "select p.id_producto, p.descripcion\n"
                        + "from tproducto p, TUnidad_producto up, TMoneda m, TProducto_tipo pt, TProveedor pro, TProducto_detalle pd \n"
                        + "where\n"
                        + "((p.codigo like '%" + bus_txt + "%' or\n"
                        + "p.descripcion like '%" + bus_txt + "%' or\n"
                        + "p.marca like '%" + bus_txt + "%' or\n"
                        + "p.modelo like '%" + bus_txt + "%' or\n"
                        + "p.referencia_precio like '%" + bus_txt + "%' or\n"
                        + "p.descripcion_coloquial like '%" + bus_txt + "%' or\n"
                        + "up.descripcion like '%" + bus_txt + "%' or\n"
                        + "up.codigo like '%" + bus_txt + "%' or\n"
                        + "m.nombre like '%" + bus_txt + "%'\n"
                        + ")  and \n"
                        + "p.id_unidad = up.id_unidad and\n"
                        + "p.id_moneda = m.id_moneda\n"
                        + ")  and \n"
                        + "(p.id_productotipo = pt.id_productotipo and pt.descripcion = '" + bus_tipo + "')\n"
                        + "and \n"
                        + "(pd.id_producto = p.id_producto and pd.id_proveedor = pro.id_proveedor and pro.razon_social = '" + bus_proveedor + "')\n"
                        + "and p.guardado = '1'\n"
                        + "group by p.id_producto, p.descripcion\n"
                        + "order by p.descripcion asc";
                System.out.println(consulta);
                mostrar_tabla_producto(consulta);
                break;
        }
    }

    //Mostrar Ultimos Id's
    private void producto_id_ultimo() {
        try {
            String consulta = "SELECT id_producto FROM tproducto WHERE id_producto = (SELECT MAX(id_producto) from tproducto)";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                id_producto_global = Integer.parseInt(r.getString("id_producto"));
                System.out.println("El ultimo id_producto generado es " + id_producto_global);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el ultimo Id de Producto Creado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mantenimiento_tabla_detalle_factura = new javax.swing.JPopupMenu();
        Modificar = new javax.swing.JMenuItem();
        Eliminar = new javax.swing.JMenuItem();
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
        dialog_crear_proveedor = new javax.swing.JDialog();
        jPanel24 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        btn_cancelar_cliente = new javax.swing.JButton();
        btn_crea_cliente = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        txt_razon_social_proveedor_crear = new javax.swing.JTextField();
        txt_ruc_proveedor_crear = new javax.swing.JTextField();
        txt_direccion_proveedor_crear = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txt_telefono_proveedor_crear = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txt_celular_proveedor_crear = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        txt_correo_proveedor_crear = new javax.swing.JTextField();
        dialog_crear_moneda = new javax.swing.JDialog();
        jPanel35 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        btn_cancelar_crear_empresatransporte = new javax.swing.JButton();
        btn_guardar_empresatransporte = new javax.swing.JButton();
        jPanel38 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txt_crear_moneda_nombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_crear_moneda_simbolo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_crear_moneda_check_moneda_local = new javax.swing.JCheckBox();
        dialog_crear_unidadmaterial = new javax.swing.JDialog();
        jPanel43 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        btn_cancelar_crear_empresatransporte1 = new javax.swing.JButton();
        btn_guardar_empresatransporte1 = new javax.swing.JButton();
        jPanel46 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txt_crear_unidadmedida_codigo = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txt_crear_unidadmedida_descripcion = new javax.swing.JTextField();
        dialog_crear_tipoproducto = new javax.swing.JDialog();
        jPanel47 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jPanel48 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        btn_cancelar_crear_empresatransporte2 = new javax.swing.JButton();
        btn_guardar_empresatransporte2 = new javax.swing.JButton();
        jPanel50 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txt_crear_tipomaterial_descripcion = new javax.swing.JTextField();
        dialog_buscar_proveedor = new javax.swing.JDialog();
        jPanel39 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        txt_buscar_proveedor = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jPanel40 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        btn_cliente_cancelar_busqueda = new javax.swing.JButton();
        btn_cliente_seleccionar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel42 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabla_cliente_buscar = new javax.swing.JTable();
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
        jLabel22 = new javax.swing.JLabel();
        cbo_tipomaterial_buscar = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        cbo_proveedor_buscar = new javax.swing.JComboBox();
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
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_codigo = new javax.swing.JTextField();
        txt_descripcion = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt_modelo = new javax.swing.JTextField();
        txt_marca = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txt_peso = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        cbo_unidad = new javax.swing.JComboBox();
        txt_unidad_codigo = new javax.swing.JTextField();
        btn_nueva_unidad = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        cbo_tipoproducto = new javax.swing.JComboBox();
        btn_nuevo_tipo_producto = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        txt_descripcion_coloquial = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        btn_nueva_moneda_material = new javax.swing.JButton();
        cbo_moneda_material = new javax.swing.JComboBox();
        jLabel35 = new javax.swing.JLabel();
        txt_unidad_precio_unitario = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        txt_unidad_precio_manoobra = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        txt_unidad_precio_material = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        txt_unidad_precio_equipo = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txt_referencia_precio = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        panel_nuevo_detalle = new javax.swing.JPanel();
        btn_guardar_detalle = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        txt_precio = new javax.swing.JTextField();
        btn_cancelar_guardar_detalle = new javax.swing.JButton();
        cbo_moneda_proveedor = new javax.swing.JComboBox();
        btn_nuevo_moneda = new javax.swing.JButton();
        cbo_proveedor = new javax.swing.JComboBox();
        jLabel63 = new javax.swing.JLabel();
        btn_buscar_proveedor = new javax.swing.JButton();
        btn_nuevo_proveedor = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabla_detalle = new javax.swing.JTable();
        btn_nuevo_detalle = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btn_cancelar = new javax.swing.JButton();
        btn_guardar = new javax.swing.JButton();
        btn_imprimir = new javax.swing.JButton();
        btn_creacion = new javax.swing.JButton();
        btn_vista_previa = new javax.swing.JButton();

        Modificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/actualizar_24_24.png"))); // NOI18N
        Modificar.setText("Modificar");
        Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModificarActionPerformed(evt);
            }
        });
        mantenimiento_tabla_detalle_factura.add(Modificar);

        Eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar_24_24.png"))); // NOI18N
        Eliminar.setText("Eliminar");
        Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarActionPerformed(evt);
            }
        });
        mantenimiento_tabla_detalle_factura.add(Eliminar);

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
        txt_f_creacion.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txt_f_modificacion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_f_modificacion.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

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

        jPanel24.setBackground(new java.awt.Color(0, 110, 204));
        jPanel24.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/proveedor_32_32.png"))); // NOI18N
        jLabel39.setText("Crear Proveedor");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_crear_proveedor.getContentPane().add(jPanel24, java.awt.BorderLayout.PAGE_START);

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setLayout(new java.awt.BorderLayout());

        jPanel26.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_cancelar_cliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cancelar_cliente.setText("Cancelar");
        btn_cancelar_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_clienteActionPerformed(evt);
            }
        });

        btn_crea_cliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_crea_cliente.setText("Guardar");
        btn_crea_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_crea_clienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addGap(0, 205, Short.MAX_VALUE)
                .addComponent(btn_crea_cliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar_cliente))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cancelar_cliente)
                    .addComponent(btn_crea_cliente))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel26, java.awt.BorderLayout.PAGE_END);

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(0, 51, 153));
        jLabel40.setText("Razon Social:");

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 51, 153));
        jLabel41.setText("R.U.C.:");

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 51, 153));
        jLabel42.setText("Dirección:");

        txt_razon_social_proveedor_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_razon_social_proveedor_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_razon_social_proveedor_crearKeyTyped(evt);
            }
        });

        txt_ruc_proveedor_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_ruc_proveedor_crear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_ruc_proveedor_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_ruc_proveedor_crearKeyTyped(evt);
            }
        });

        txt_direccion_proveedor_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_direccion_proveedor_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_direccion_proveedor_crearKeyTyped(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(0, 51, 153));
        jLabel43.setText("Teléfono:");

        txt_telefono_proveedor_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_telefono_proveedor_crear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_telefono_proveedor_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telefono_proveedor_crearKeyTyped(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(0, 51, 153));
        jLabel44.setText("Celular:");

        txt_celular_proveedor_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_celular_proveedor_crear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_celular_proveedor_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_celular_proveedor_crearKeyTyped(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(0, 51, 153));
        jLabel45.setText("Correo:");

        txt_correo_proveedor_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_correo_proveedor_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_correo_proveedor_crearKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_direccion_proveedor_crear)
                    .addComponent(txt_razon_social_proveedor_crear)
                    .addComponent(txt_correo_proveedor_crear, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_celular_proveedor_crear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                            .addComponent(txt_ruc_proveedor_crear, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_telefono_proveedor_crear, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(txt_razon_social_proveedor_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(txt_ruc_proveedor_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42)
                    .addComponent(txt_direccion_proveedor_crear, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(txt_telefono_proveedor_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(txt_celular_proveedor_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(txt_correo_proveedor_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel27, java.awt.BorderLayout.CENTER);

        dialog_crear_proveedor.getContentPane().add(jPanel25, java.awt.BorderLayout.CENTER);

        jPanel35.setBackground(new java.awt.Color(0, 110, 204));
        jPanel35.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/moneda_32_32.png"))); // NOI18N
        jLabel49.setText("Crear Moneda");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_crear_moneda.getContentPane().add(jPanel35, java.awt.BorderLayout.PAGE_START);

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));
        jPanel36.setLayout(new java.awt.BorderLayout());

        jPanel37.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_cancelar_crear_empresatransporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cancelar_crear_empresatransporte.setText("Cancelar");
        btn_cancelar_crear_empresatransporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_crear_empresatransporteActionPerformed(evt);
            }
        });

        btn_guardar_empresatransporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_guardar_empresatransporte.setText("Guardar");
        btn_guardar_empresatransporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardar_empresatransporteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                .addGap(0, 205, Short.MAX_VALUE)
                .addComponent(btn_guardar_empresatransporte)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar_crear_empresatransporte))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cancelar_crear_empresatransporte)
                    .addComponent(btn_guardar_empresatransporte))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel36.add(jPanel37, java.awt.BorderLayout.PAGE_END);

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 153));
        jLabel3.setText("Nombre:");

        txt_crear_moneda_nombre.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_crear_moneda_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_crear_moneda_nombreKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 51, 153));
        jLabel5.setText("Símbolo:");

        txt_crear_moneda_simbolo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_crear_moneda_simbolo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_crear_moneda_simboloKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 153));
        jLabel4.setText("Moneda Local:");

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_crear_moneda_nombre)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_crear_moneda_check_moneda_local)
                            .addComponent(txt_crear_moneda_simbolo, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 224, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_crear_moneda_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_crear_moneda_simbolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_crear_moneda_check_moneda_local))
                .addContainerGap())
        );

        jPanel36.add(jPanel38, java.awt.BorderLayout.CENTER);

        dialog_crear_moneda.getContentPane().add(jPanel36, java.awt.BorderLayout.CENTER);

        jPanel43.setBackground(new java.awt.Color(0, 110, 204));
        jPanel43.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/unidad_de_medida_32_32.png"))); // NOI18N
        jLabel50.setText("Crear Unidad de Medida");

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_crear_unidadmaterial.getContentPane().add(jPanel43, java.awt.BorderLayout.PAGE_START);

        jPanel44.setBackground(new java.awt.Color(255, 255, 255));
        jPanel44.setLayout(new java.awt.BorderLayout());

        jPanel45.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_cancelar_crear_empresatransporte1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cancelar_crear_empresatransporte1.setText("Cancelar");
        btn_cancelar_crear_empresatransporte1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_crear_empresatransporte1ActionPerformed(evt);
            }
        });

        btn_guardar_empresatransporte1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_guardar_empresatransporte1.setText("Guardar");
        btn_guardar_empresatransporte1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardar_empresatransporte1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel45Layout.createSequentialGroup()
                .addGap(0, 205, Short.MAX_VALUE)
                .addComponent(btn_guardar_empresatransporte1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar_crear_empresatransporte1))
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cancelar_crear_empresatransporte1)
                    .addComponent(btn_guardar_empresatransporte1))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel44.add(jPanel45, java.awt.BorderLayout.PAGE_END);

        jPanel46.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 153));
        jLabel6.setText("Código:");

        txt_crear_unidadmedida_codigo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_crear_unidadmedida_codigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_crear_unidadmedida_codigoKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 153));
        jLabel13.setText("Descripción:");

        txt_crear_unidadmedida_descripcion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_crear_unidadmedida_descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_crear_unidadmedida_descripcionKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_crear_unidadmedida_descripcion)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addComponent(txt_crear_unidadmedida_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 201, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_crear_unidadmedida_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txt_crear_unidadmedida_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(185, Short.MAX_VALUE))
        );

        jPanel44.add(jPanel46, java.awt.BorderLayout.CENTER);

        dialog_crear_unidadmaterial.getContentPane().add(jPanel44, java.awt.BorderLayout.CENTER);

        jPanel47.setBackground(new java.awt.Color(0, 110, 204));
        jPanel47.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/tipo_producto.png"))); // NOI18N
        jLabel51.setText("Tipo de Material");

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_crear_tipoproducto.getContentPane().add(jPanel47, java.awt.BorderLayout.PAGE_START);

        jPanel48.setBackground(new java.awt.Color(255, 255, 255));
        jPanel48.setLayout(new java.awt.BorderLayout());

        jPanel49.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_cancelar_crear_empresatransporte2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cancelar_crear_empresatransporte2.setText("Cancelar");
        btn_cancelar_crear_empresatransporte2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_crear_empresatransporte2ActionPerformed(evt);
            }
        });

        btn_guardar_empresatransporte2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_guardar_empresatransporte2.setText("Guardar");
        btn_guardar_empresatransporte2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardar_empresatransporte2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel49Layout.createSequentialGroup()
                .addGap(0, 205, Short.MAX_VALUE)
                .addComponent(btn_guardar_empresatransporte2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar_crear_empresatransporte2))
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel49Layout.createSequentialGroup()
                .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cancelar_crear_empresatransporte2)
                    .addComponent(btn_guardar_empresatransporte2))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel48.add(jPanel49, java.awt.BorderLayout.PAGE_END);

        jPanel50.setBackground(new java.awt.Color(255, 255, 255));

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 51, 153));
        jLabel21.setText("Descripción:");

        txt_crear_tipomaterial_descripcion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_crear_tipomaterial_descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_crear_tipomaterial_descripcionKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_crear_tipomaterial_descripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txt_crear_tipomaterial_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(185, Short.MAX_VALUE))
        );

        jPanel48.add(jPanel50, java.awt.BorderLayout.CENTER);

        dialog_crear_tipoproducto.getContentPane().add(jPanel48, java.awt.BorderLayout.CENTER);

        dialog_buscar_proveedor.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel39.setBackground(new java.awt.Color(255, 255, 255));
        jPanel39.setPreferredSize(new java.awt.Dimension(400, 60));
        jPanel39.setLayout(new java.awt.BorderLayout());

        jToolBar2.setFloatable(false);
        jToolBar2.setPreferredSize(new java.awt.Dimension(13, 25));

        jLabel2.setText("Buscar:");
        jLabel2.setMaximumSize(new java.awt.Dimension(50, 25));
        jLabel2.setMinimumSize(new java.awt.Dimension(50, 25));
        jLabel2.setPreferredSize(new java.awt.Dimension(50, 25));
        jToolBar2.add(jLabel2);

        txt_buscar_proveedor.setColumns(50);
        txt_buscar_proveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_buscar_proveedorKeyReleased(evt);
            }
        });
        jToolBar2.add(txt_buscar_proveedor);

        jPanel39.add(jToolBar2, java.awt.BorderLayout.CENTER);

        jPanel7.setBackground(new java.awt.Color(0, 110, 204));
        jPanel7.setPreferredSize(new java.awt.Dimension(418, 35));

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar2_32_32.png"))); // NOI18N
        jLabel36.setText("Buscar Proveedor");
        jLabel36.setPreferredSize(new java.awt.Dimension(144, 35));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel39.add(jPanel7, java.awt.BorderLayout.NORTH);

        dialog_buscar_proveedor.getContentPane().add(jPanel39, java.awt.BorderLayout.PAGE_START);

        jPanel40.setBackground(new java.awt.Color(255, 255, 255));
        jPanel40.setLayout(new java.awt.BorderLayout());

        jPanel41.setBackground(new java.awt.Color(255, 255, 255));
        jPanel41.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_cliente_cancelar_busqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cliente_cancelar_busqueda.setText("Cancelar");
        btn_cliente_cancelar_busqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cliente_cancelar_busquedaActionPerformed(evt);
            }
        });

        btn_cliente_seleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_cliente_seleccionar.setText("Seleccionar");
        btn_cliente_seleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cliente_seleccionarActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo_24_24.png"))); // NOI18N
        jButton2.setText("Nuevo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel41Layout.createSequentialGroup()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                .addComponent(btn_cliente_seleccionar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cliente_cancelar_busqueda))
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cliente_cancelar_busqueda)
                    .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_cliente_seleccionar)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel40.add(jPanel41, java.awt.BorderLayout.PAGE_END);

        jPanel42.setBackground(new java.awt.Color(255, 255, 255));

        tabla_cliente_buscar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabla_cliente_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabla_cliente_buscarKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(tabla_cliente_buscar);

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
        );

        jPanel40.add(jPanel42, java.awt.BorderLayout.CENTER);

        dialog_buscar_proveedor.getContentPane().add(jPanel40, java.awt.BorderLayout.CENTER);

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(0, 110, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/producto_32_32.png"))); // NOI18N
        jLabel1.setText("Materiales");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 992, Short.MAX_VALUE)
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

        jPanel1.add(jToolBar1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        barra_buscar.setBackground(new java.awt.Color(255, 255, 255));
        barra_buscar.setFloatable(false);
        barra_buscar.setPreferredSize(new java.awt.Dimension(13, 25));

        label_buscar.setText("Buscar:");
        barra_buscar.add(label_buscar);

        txt_buscar.setColumns(30);
        txt_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_buscarKeyReleased(evt);
            }
        });
        barra_buscar.add(txt_buscar);

        jLabel22.setForeground(new java.awt.Color(0, 51, 153));
        jLabel22.setText("     Familia:");
        barra_buscar.add(jLabel22);

        cbo_tipomaterial_buscar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_tipomaterial_buscarItemStateChanged(evt);
            }
        });
        barra_buscar.add(cbo_tipomaterial_buscar);

        jLabel18.setForeground(new java.awt.Color(0, 51, 153));
        jLabel18.setText("       Proveedor:");
        barra_buscar.add(jLabel18);

        cbo_proveedor_buscar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_proveedor_buscarItemStateChanged(evt);
            }
        });
        barra_buscar.add(cbo_proveedor_buscar);

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
        );

        jPanel5.add(panel_tabla, java.awt.BorderLayout.LINE_START);

        Panel_detalle.setLayout(new java.awt.BorderLayout());

        norte.setBackground(new java.awt.Color(255, 255, 255));
        norte.setPreferredSize(new java.awt.Dimension(458, 40));

        lbl_titulo.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_titulo.setForeground(new java.awt.Color(0, 110, 204));
        lbl_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_titulo.setText("Información del Material");

        javax.swing.GroupLayout norteLayout = new javax.swing.GroupLayout(norte);
        norte.setLayout(norteLayout);
        norteLayout.setHorizontalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
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

        centro.setBackground(new java.awt.Color(153, 255, 204));
        centro.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(840, 240));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setPreferredSize(new java.awt.Dimension(282, 55));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 51, 153));
        jLabel11.setText("Descripción:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 153));
        jLabel12.setText("Código:");

        txt_codigo.setEditable(false);
        txt_codigo.setBackground(new java.awt.Color(255, 255, 255));
        txt_codigo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_codigo.setToolTipText("Ingrese un Codigo para este Material");
        txt_codigo.setOpaque(false);
        txt_codigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_codigoKeyTyped(evt);
            }
        });

        txt_descripcion.setEditable(false);
        txt_descripcion.setBackground(new java.awt.Color(255, 255, 255));
        txt_descripcion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_descripcion.setToolTipText("Ingrese el Nombre del Material");
        txt_descripcion.setOpaque(false);
        txt_descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_descripcionKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_descripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txt_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txt_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 51, 153));
        jLabel14.setText("Marca:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 51, 153));
        jLabel15.setText("Modelo:");

        txt_modelo.setEditable(false);
        txt_modelo.setBackground(new java.awt.Color(255, 255, 255));
        txt_modelo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_modelo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_modelo.setToolTipText("Ingrese el Modelo del Material o Equipo");
        txt_modelo.setOpaque(false);
        txt_modelo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_modeloKeyTyped(evt);
            }
        });

        txt_marca.setEditable(false);
        txt_marca.setBackground(new java.awt.Color(255, 255, 255));
        txt_marca.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_marca.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_marca.setToolTipText("Ingrese la Marca del Material o Equipo");
        txt_marca.setOpaque(false);
        txt_marca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_marcaKeyTyped(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 51, 153));
        jLabel16.setText("Peso:");

        txt_peso.setEditable(false);
        txt_peso.setBackground(new java.awt.Color(255, 255, 255));
        txt_peso.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_peso.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_peso.setToolTipText("Ingrese el Peso de este Material. Este valor se usará en las Guias.");
        txt_peso.setOpaque(false);
        txt_peso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_pesoKeyTyped(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 51, 153));
        jLabel17.setText("Kg.");

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 51, 153));
        jLabel32.setText("Unidad:");

        cbo_unidad.setEditable(true);
        cbo_unidad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_unidad.setOpaque(false);
        cbo_unidad.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_unidadItemStateChanged(evt);
            }
        });

        txt_unidad_codigo.setEditable(false);
        txt_unidad_codigo.setBackground(new java.awt.Color(255, 255, 255));
        txt_unidad_codigo.setOpaque(false);

        btn_nueva_unidad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btn_nueva_unidad.setToolTipText("Crear Nueva Unidad de Medida");
        btn_nueva_unidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nueva_unidadActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 51, 153));
        jLabel33.setText("Tipo:");

        cbo_tipoproducto.setEditable(true);
        cbo_tipoproducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_tipoproducto.setToolTipText("Seleccione el Tipo o Familia al que pertenece el Material.");
        cbo_tipoproducto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_tipoproductoItemStateChanged(evt);
            }
        });

        btn_nuevo_tipo_producto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btn_nuevo_tipo_producto.setToolTipText("Crear Nuevo Tipo de Material");
        btn_nuevo_tipo_producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevo_tipo_productoActionPerformed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(0, 51, 153));
        jLabel37.setText("Nombre Coloquial:");

        txt_descripcion_coloquial.setEditable(false);
        txt_descripcion_coloquial.setBackground(new java.awt.Color(255, 255, 255));
        txt_descripcion_coloquial.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_descripcion_coloquial.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_descripcion_coloquial.setToolTipText("Ingrese el nombre con el que comúnmente se conoce a éste material.");
        txt_descripcion_coloquial.setOpaque(false);
        txt_descripcion_coloquial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_descripcion_coloquialKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_descripcion_coloquial))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_marca, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_modelo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                                .addComponent(txt_peso)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(cbo_unidad, 0, 1, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_unidad_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_nueva_unidad, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(cbo_tipoproducto, 0, 179, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_nuevo_tipo_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txt_marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txt_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txt_peso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel32)
                        .addComponent(cbo_unidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_unidad_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_nueva_unidad, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel33)
                        .addComponent(cbo_tipoproducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_nuevo_tipo_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(txt_descripcion_coloquial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 51, 153));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText("Moneda:");

        btn_nueva_moneda_material.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btn_nueva_moneda_material.setToolTipText("Crear Nueva Moneda");
        btn_nueva_moneda_material.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nueva_moneda_materialActionPerformed(evt);
            }
        });

        cbo_moneda_material.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_moneda_material.setOpaque(false);

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 51, 153));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel35.setText("Precio Unitario:");

        txt_unidad_precio_unitario.setEditable(false);
        txt_unidad_precio_unitario.setBackground(new java.awt.Color(255, 255, 255));
        txt_unidad_precio_unitario.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_unidad_precio_unitario.setToolTipText("Ingrese el Precio Unitario o Precio Promedio de Material. Este precio se usará como referencia en las Cotizaciones");
        txt_unidad_precio_unitario.setOpaque(false);
        txt_unidad_precio_unitario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_unidad_precio_unitarioKeyTyped(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(0, 51, 153));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel47.setText("Precio de Mano de Obra:");

        txt_unidad_precio_manoobra.setEditable(false);
        txt_unidad_precio_manoobra.setBackground(new java.awt.Color(255, 255, 255));
        txt_unidad_precio_manoobra.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_unidad_precio_manoobra.setToolTipText("Precio de Mano de Obra");
        txt_unidad_precio_manoobra.setOpaque(false);
        txt_unidad_precio_manoobra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_unidad_precio_manoobraKeyTyped(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 51, 153));
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel48.setText("Precio de Materiales:");

        txt_unidad_precio_material.setEditable(false);
        txt_unidad_precio_material.setBackground(new java.awt.Color(255, 255, 255));
        txt_unidad_precio_material.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_unidad_precio_material.setToolTipText("Precio de Materiales");
        txt_unidad_precio_material.setOpaque(false);
        txt_unidad_precio_material.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_unidad_precio_materialKeyTyped(evt);
            }
        });

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(0, 51, 153));
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel52.setText("Precio de Equipos:");

        txt_unidad_precio_equipo.setEditable(false);
        txt_unidad_precio_equipo.setBackground(new java.awt.Color(255, 255, 255));
        txt_unidad_precio_equipo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_unidad_precio_equipo.setToolTipText("Precio de Equipos");
        txt_unidad_precio_equipo.setOpaque(false);
        txt_unidad_precio_equipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_unidad_precio_equipoKeyTyped(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 51, 153));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("Referencia de los Precios:");

        txt_referencia_precio.setEditable(false);
        txt_referencia_precio.setBackground(new java.awt.Color(255, 255, 255));
        txt_referencia_precio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_referencia_precio.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_referencia_precio.setToolTipText("Ingrese el origen de estos precios. Por ejemplo: Revista Costos, Cotizacion Megaval, etc.");
        txt_referencia_precio.setOpaque(false);
        txt_referencia_precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_referencia_precioKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(cbo_moneda_material, 0, 151, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_nueva_moneda_material, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_unidad_precio_unitario)
                    .addComponent(txt_unidad_precio_manoobra)
                    .addComponent(txt_unidad_precio_material)
                    .addComponent(txt_unidad_precio_equipo)
                    .addComponent(txt_referencia_precio))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel34)
                        .addComponent(cbo_moneda_material, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_nueva_moneda_material, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(txt_unidad_precio_unitario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(txt_unidad_precio_manoobra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(txt_unidad_precio_material, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(txt_unidad_precio_equipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txt_referencia_precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        centro.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new java.awt.BorderLayout());

        panel_nuevo_detalle.setBackground(new java.awt.Color(204, 255, 255));
        panel_nuevo_detalle.setPreferredSize(new java.awt.Dimension(840, 80));

        btn_guardar_detalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/listo_10_10.png"))); // NOI18N
        btn_guardar_detalle.setToolTipText("Guardar Proveedor");
        btn_guardar_detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardar_detalleActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 51, 153));
        jLabel19.setText("Precio:");

        txt_precio.setEditable(false);
        txt_precio.setBackground(new java.awt.Color(255, 255, 255));
        txt_precio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_precio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_precio.setOpaque(false);
        txt_precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_precioKeyTyped(evt);
            }
        });

        btn_cancelar_guardar_detalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_10_10.png"))); // NOI18N
        btn_cancelar_guardar_detalle.setToolTipText("Cancelar");
        btn_cancelar_guardar_detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_guardar_detalleActionPerformed(evt);
            }
        });

        cbo_moneda_proveedor.setEditable(true);
        cbo_moneda_proveedor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_moneda_proveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_moneda_proveedorItemStateChanged(evt);
            }
        });

        btn_nuevo_moneda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btn_nuevo_moneda.setToolTipText("Crear Nueva Moneda");
        btn_nuevo_moneda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevo_monedaActionPerformed(evt);
            }
        });

        cbo_proveedor.setEditable(true);
        cbo_proveedor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_proveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_proveedorItemStateChanged(evt);
            }
        });

        jLabel63.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(0, 51, 153));
        jLabel63.setText("Proveedor:");

        btn_buscar_proveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_10_10.png"))); // NOI18N
        btn_buscar_proveedor.setToolTipText("Buscar Proveedor");
        btn_buscar_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar_proveedorActionPerformed(evt);
            }
        });

        btn_nuevo_proveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btn_nuevo_proveedor.setToolTipText("Nuevo Proveedor");
        btn_nuevo_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevo_proveedorActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 51, 153));
        jLabel20.setText("Moneda:");

        javax.swing.GroupLayout panel_nuevo_detalleLayout = new javax.swing.GroupLayout(panel_nuevo_detalle);
        panel_nuevo_detalle.setLayout(panel_nuevo_detalleLayout);
        panel_nuevo_detalleLayout.setHorizontalGroup(
            panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_buscar_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_nuevo_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbo_proveedor, 0, 263, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_nuevo_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbo_moneda_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_precio)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_guardar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar_guardar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panel_nuevo_detalleLayout.setVerticalGroup(
            panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_nuevo_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_buscar_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel63))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbo_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(btn_nuevo_moneda)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbo_moneda_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btn_guardar_detalle)
                            .addComponent(btn_cancelar_guardar_detalle))
                        .addGap(2, 2, 2)))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jPanel8.add(panel_nuevo_detalle, java.awt.BorderLayout.PAGE_START);

        jPanel17.setBackground(new java.awt.Color(255, 153, 153));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));

        tabla_detalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabla_detalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabla_detalle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_detalleMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabla_detalle);

        btn_nuevo_detalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btn_nuevo_detalle.setToolTipText("Nuevo Detalle");
        btn_nuevo_detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevo_detalleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_nuevo_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(btn_nuevo_detalle)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel17.add(jPanel19, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel17, java.awt.BorderLayout.CENTER);

        centro.add(jPanel8, java.awt.BorderLayout.CENTER);

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
                .addContainerGap(73, Short.MAX_VALUE)
                .addComponent(btn_vista_previa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_creacion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_imprimir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_guardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar)
                .addGap(34, 34, 34))
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
        dialog_fecha_creacion.setLocationRelativeTo(producto);
        dialog_fecha_creacion.setModal(true);
        dialog_fecha_creacion.setVisible(true);
    }//GEN-LAST:event_btn_creacionActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        dialog_fecha_creacion.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btn_guardar_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardar_detalleActionPerformed
        if (cbo_proveedor.getSelectedItem().toString().length() == 0 || cbo_moneda_proveedor.getSelectedItem().toString().length() == 0 || txt_precio.getText().length() == 0) {
            System.out.println("\nFalta ingresar o seleccionar los campos Proveedor, Moneda, o Precio");
            JOptionPane.showMessageDialog(null, "Por Favor Ingrese los campos Proveedor, Moneda, o Precio", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            //Campos usados para capturar los id's de los combos
            String tabla;
            String campocondicion;
            String campocapturar;

            int id_producto = id_producto_global;
            float precio = Float.parseFloat(txt_precio.getText());

            //Capturamos el id del proveedor
            int id_proveedor;
            campocapturar = "id_proveedor";
            tabla = "tproveedor";
            campocondicion = "razon_social";
            id_proveedor = CapturarId(campocapturar, tabla, campocondicion, cbo_proveedor.getSelectedItem().toString().trim());
            System.out.println("id capturado: " + id_proveedor);

            //Capturamos el id de la moneda
            int id_moneda;
            campocapturar = "id_moneda";
            tabla = "tmoneda";
            campocondicion = "nombre";
            id_moneda = CapturarId(campocapturar, tabla, campocondicion, cbo_moneda_proveedor.getSelectedItem().toString().trim());
            System.out.println("id capturado: " + id_moneda);

            System.out.println("Llamamo a la funcion crear Producto_detalle");

            if (crear0_modificar1_producto_detalle == 0) {
                crear_Producto_Detalle(id_producto, id_proveedor, id_moneda, precio, id_empresa_index, id_usuario_index);
            } else {
                int id_producto_detalle = id_producto_detalle_global;
                modificar_Producto_Detalle(id_producto_detalle, id_producto, id_proveedor, id_moneda, precio, id_empresa_index, id_usuario_index);
            }
        }
    }//GEN-LAST:event_btn_guardar_detalleActionPerformed

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        System.out.println("\ncrear Material");
        System.out.println("=================");

        System.out.println("inicializar ID's globales");
        inicializar_id_global();

        if (clase_producto.crear(id_empresa_index, id_usuario_index)) {
            System.out.println("\nEl Material se logró registrar exitosamente!");

            System.out.println("Ejecutando la consulta para saber cual es el ultimo id_producto generado");
            producto_id_ultimo();

            band_index = 1;
            crear0_modificar1_producto = 1;
            band_mantenimiento_producto_detalle = 0;
            System.out.println("Se procede a igualar el crear0_modificar1_producto: " + crear0_modificar1_producto);

            System.out.println("cambiando el titulo");
            lbl_titulo.setText("Nuevo Material");

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
            btn_guardar.setEnabled(true);

            System.out.println("desactivar barra de herramientas");
            activar_barra_herramientas("desactivar");

            System.out.println("mostrar botones");
            mostrar_botones("mostrar");

            System.out.println("Mostrar tabla producto_detalle_vacia");
            mostrar_tabla_producto_detalle_vacia();

            DefaultComboBoxModel modelocombo = new DefaultComboBoxModel();
            System.out.println("Mostrar Combo Unidad");
            if (band_cbo_unidadmedida == 0) {
                cbo_unidad.setModel(modelocombo);
                mostrar_combo_unidadmedida();
            }

            System.out.println("Mostrar Combo Tipo de Material");
            if (band_cbo_tipoproducto == 0) {
                cbo_tipoproducto.setModel(modelocombo);
                mostrar_combo_tipomaterial();
            }

            System.out.println("Mostrar Combo Proveedor");
            if (band_cbo_proveedor == 0) {
                cbo_proveedor.setModel(modelocombo);
                mostrar_combo_proveedor();
            }

            System.out.println("Mostrar Combo Moneda Proveedor");
            if (band_cbo_moneda_proveedor == 0) {
                cbo_moneda_proveedor.setModel(modelocombo);
                mostrar_combo_moneda_proveedor();
            }

            System.out.println("Mostrar Combo Moneda Material");
            if (band_cbo_moneda_material == 0) {
                cbo_moneda_material.setModel(modelocombo);
                mostrar_combo_moneda_material();
            }

            System.out.println("mostrar el panel de detalle de datos");
            Panel_detalle.setVisible(true);
            panel_nuevo_detalle.setVisible(false);

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear el Material. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModificarActionPerformed
        int fil;
        fil = tabla_detalle.getSelectedRow();
        if (fil == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar el registro a Modificar");
        } else {
            int id_producto_detalle;
            String razon_social;
            String nombre;
            String precio;

            m = (DefaultTableModel) tabla_detalle.getModel();
            id_producto_detalle = Integer.parseInt((String) m.getValueAt(fil, 0));
            razon_social = (String) m.getValueAt(fil, 1);
            nombre = (String) m.getValueAt(fil, 2);
            precio = (String) m.getValueAt(fil, 3);

            id_producto_detalle_global = id_producto_detalle;
            txt_precio.setText(precio);
            mostrar_combo_moneda_proveedor_buscar(nombre);
            mostrar_combo_proveedor_buscar(razon_social);

            crear0_modificar1_producto_detalle = 1;

            panel_nuevo_detalle.setVisible(true);
            btn_guardar_detalle.setVisible(true);
            btn_nuevo_detalle.setVisible(false);
        }
    }//GEN-LAST:event_ModificarActionPerformed

    private void tabla_detalleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_detalleMouseClicked
        if (band_mantenimiento_producto_detalle == 0) {
            if (evt.getButton() == 3) {
                mantenimiento_tabla_detalle_factura.show(tabla_detalle, evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_tabla_detalleMouseClicked

    private void btn_nuevo_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_detalleActionPerformed
        limpiar_caja_texto_crear_detalle_producto();
        panel_nuevo_detalle.setVisible(true);
        btn_guardar_detalle.setVisible(true);
        btn_nuevo_detalle.setVisible(false);
    }//GEN-LAST:event_btn_nuevo_detalleActionPerformed

    private void btn_cancelar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_clienteActionPerformed
        System.out.println("limpiar cajas de texto");
        limpiar_caja_texto_crear_proveedor();

        System.out.println("actualizamos tabla proveedor");
        mostrar_tabla_proveedor("");

        dialog_crear_proveedor.dispose();
    }//GEN-LAST:event_btn_cancelar_clienteActionPerformed

    private void btn_nueva_unidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nueva_unidadActionPerformed
        dialog_crear_unidadmaterial.setSize(429, 350);
        dialog_crear_unidadmaterial.setLocationRelativeTo(producto);
        dialog_crear_unidadmaterial.setModal(true);
        dialog_crear_unidadmaterial.setVisible(true);
    }//GEN-LAST:event_btn_nueva_unidadActionPerformed

    private void txt_razon_social_proveedor_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_razon_social_proveedor_crearKeyTyped
        JTextField caja = txt_razon_social_proveedor_crear;
        int limite = 200;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_razon_social_proveedor_crearKeyTyped

    private void txt_ruc_proveedor_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ruc_proveedor_crearKeyTyped
        JTextField caja = txt_ruc_proveedor_crear;
        ingresar_solo_numeros(caja, evt);
        int limite = 11;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_ruc_proveedor_crearKeyTyped

    private void txt_direccion_proveedor_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_direccion_proveedor_crearKeyTyped
        JTextField caja = txt_direccion_proveedor_crear;
        int limite = 200;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_direccion_proveedor_crearKeyTyped

    private void txt_telefono_proveedor_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefono_proveedor_crearKeyTyped
        JTextField caja = txt_telefono_proveedor_crear;
        ingresar_solo_numeros(caja, evt);
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_telefono_proveedor_crearKeyTyped

    private void txt_celular_proveedor_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_celular_proveedor_crearKeyTyped
        JTextField caja = txt_celular_proveedor_crear;
        ingresar_solo_numeros(caja, evt);
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_celular_proveedor_crearKeyTyped

    private void txt_correo_proveedor_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_correo_proveedor_crearKeyTyped
        JTextField caja = txt_correo_proveedor_crear;
        int limite = 50;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_correo_proveedor_crearKeyTyped

    private void btn_cancelar_crear_empresatransporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_crear_empresatransporteActionPerformed
        System.out.println("limpiar cajas de texto");
        limpiar_caja_texto_crear_moneda();

        dialog_crear_moneda.dispose();
    }//GEN-LAST:event_btn_cancelar_crear_empresatransporteActionPerformed

    private void txt_buscar_proveedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_proveedorKeyReleased
        ResultSet r;
        String bus = txt_buscar_proveedor.getText();
        String consulta;

        consulta = "select id_proveedor, razon_social, direccion, ruc from tproveedor where (razon_social like '%" + bus + "%'  or ruc like '%" + bus + "%' or direccion like '%" + bus + "%' or telefono like '%" + bus + "%' or celular like '%" + bus + "%' or correo like '%" + bus + "%') order by razon_social asc";
        mostrar_tabla_proveedor(consulta);
    }//GEN-LAST:event_txt_buscar_proveedorKeyReleased

    private void btn_cliente_cancelar_busquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_cancelar_busquedaActionPerformed
        txt_buscar_proveedor.setText("");
        dialog_buscar_proveedor.dispose();
    }//GEN-LAST:event_btn_cliente_cancelar_busquedaActionPerformed

    private void btn_cliente_seleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_seleccionarActionPerformed
//        int fila;
//        int id_cliente;
//        String razon_social;
//        String direccion;
//        String ruc;
//
//        fila = tabla_cliente_buscar.getSelectedRow();
//        if (fila == -1) {
//            JOptionPane.showMessageDialog(null, "Debes seleccionar un Cliente");
//        } else {
//            m = (DefaultTableModel) tabla_cliente_buscar.getModel();
//            id_cliente = Integer.parseInt((String) m.getValueAt(fila, 0));
//            razon_social = (String) m.getValueAt(fila, 1);
//            direccion = (String) m.getValueAt(fila, 2);
//            ruc = (String) m.getValueAt(fila, 3);
//
//            id_cliente_global = id_cliente;
//            mostrar_combo_proveedor_buscar(razon_social);
//            txt_codigo.setText(direccion);
////            txt_ruc_cliente.setText(ruc);
//
//            txt_buscar_proveedor.setText("");
//            dialog_buscar_proveedor.dispose();
//        }
    }//GEN-LAST:event_btn_cliente_seleccionarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        txt_buscar_proveedor.setText("");
        dialog_buscar_proveedor.dispose();

        dialog_crear_proveedor.setSize(429, 350);
        dialog_crear_proveedor.setLocationRelativeTo(producto);
        dialog_crear_proveedor.setModal(true);
        dialog_crear_proveedor.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tabla_cliente_buscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_cliente_buscarKeyPressed
//        int fila = tabla_cliente_buscar.getSelectedRow();
//        int id_cliente;
//        String razon_social;
//        String direccion;
//        String ruc;
//
//        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            m = (DefaultTableModel) tabla_cliente_buscar.getModel();
//            id_cliente = Integer.parseInt((String) m.getValueAt(fila, 0));
//            razon_social = (String) m.getValueAt(fila, 1);
//            direccion = (String) m.getValueAt(fila, 2);
//            ruc = (String) m.getValueAt(fila, 3);
//
//            id_cliente_global = id_cliente;
//            System.out.println("El id_cliente_global es: " + id_cliente_global);
//
//            mostrar_combo_proveedor_buscar(razon_social);
//            txt_codigo.setText(direccion);
////            txt_ruc_cliente.setText(ruc);
//
//            txt_buscar_proveedor.setText("");
//            dialog_buscar_proveedor.dispose();
//        }
    }//GEN-LAST:event_tabla_cliente_buscarKeyPressed

    private void btn_crea_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_crea_clienteActionPerformed
        System.out.println("\npresionó boton Guardar_Proveedor");
        System.out.println("==================================");

        System.out.println("capturando datos ingresados");

        String razon_social = txt_razon_social_proveedor_crear.getText().trim();
        String ruc = txt_ruc_proveedor_crear.getText().trim();
        String direccion = txt_direccion_proveedor_crear.getText().trim();
        String telefono = txt_telefono_proveedor_crear.getText().trim();
        String celular = txt_celular_proveedor_crear.getText().trim();
        String correo = txt_correo_proveedor_crear.getText().trim();
        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;

        if (txt_razon_social_proveedor_crear.getText().length() == 0 || txt_ruc_proveedor_crear.getText().length() == 0 || txt_direccion_proveedor_crear.getText().length() == 0) {
            System.out.println("\nFalta ingresar los campos Razon Social, R.U.C. y Dirección");
            JOptionPane.showMessageDialog(null, "Los Razon Social, R.U.C. y Dirección son necesarios \n Por favor ingrese estos campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            if (txt_ruc_proveedor_crear.getText().length() != 11) {
                System.out.println("\n el R.U.C. tiene un tamaño diferente a 11");
                JOptionPane.showMessageDialog(null, "El R.U.C. debe tener 11 digitos.\n Por favor un R.U.C. válido.", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {

                if (correo.length() > 0) {
                    if (!ValidarCorreo(correo)) {
                        System.out.println("\n el correo ingresado no es correcto");
                        JOptionPane.showMessageDialog(null, "El correo ingresado no es correcto.\n Por favor ingrese un Correo válido.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (clase_proveedor.razon_social_existente(razon_social) > 0) {
                            System.out.println("La Razon social ya se encuentra registrada.");
                            JOptionPane.showMessageDialog(null, "La Razon Social ya se encuentra Registra.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (clase_proveedor.ruc_existente(ruc) > 0) {
                                System.out.println("El R.U.C. ya se encuentra registrado.");
                                JOptionPane.showMessageDialog(null, "El R.U.C. ya se encuentra Registrado.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                            } else {
                                System.out.println("llamamos a la funcion crear_proveedor");
                                crear_proveedor(razon_social, ruc, direccion, telefono, celular, correo, id_empresa, id_usuario);
                            }
                        }
                    }
                } else {
                    if (clase_proveedor.razon_social_existente(razon_social) > 0) {
                        System.out.println("La Razon social ya se encuentra registrada.");
                        JOptionPane.showMessageDialog(null, "La Razon Social ya se encuentra Registra.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (clase_proveedor.ruc_existente(ruc) > 0) {
                            System.out.println("El R.U.C. ya se encuentra registrado.");
                            JOptionPane.showMessageDialog(null, "El R.U.C. ya se encuentra Registrado.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            System.out.println("llamamos a la funcion crear_proveedor");
                            crear_proveedor(razon_social, ruc, direccion, telefono, celular, correo, id_empresa, id_usuario);
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_btn_crea_clienteActionPerformed

    private void cbo_unidadItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_unidadItemStateChanged
        System.out.println("\nSe ejecuta la busqueda de la Unidad de Material");

        if (cbo_unidad.getSelectedItem().toString().trim().length() > 0) {
            String descripcion = cbo_unidad.getSelectedItem().toString().trim();
            String codigo;

            try {
                ResultSet r = sentencia.executeQuery("select codigo from tunidad_producto where descripcion='" + descripcion + "'");
                while (r.next()) {
                    codigo = r.getString("codigo").trim();
                    txt_unidad_codigo.setText(codigo);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrio al Extraer los Datos de la Unidad de Medida", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            txt_unidad_codigo.setText("");
        }
    }//GEN-LAST:event_cbo_unidadItemStateChanged

    private void btn_guardar_empresatransporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardar_empresatransporteActionPerformed
        System.out.println("\npresionó boton Guardar_Moneda");
        System.out.println("===============================");

        System.out.println("capturando datos ingresados");

        String nombre = txt_crear_moneda_nombre.getText().trim();
        String simbolo = txt_crear_moneda_simbolo.getText().trim();
        String moneda_local;
        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;
        int band;

        if (txt_crear_moneda_check_moneda_local.isSelected() == true) {
            moneda_local = "1";
        } else {
            moneda_local = "0";
        }

        System.out.println("\nDATOS INGRESADOS DE LA MONEDA");
        System.out.println("==============================");
        System.out.println("Moneda:       " + nombre);
        System.out.println("Símbolo:      " + simbolo);
        System.out.println("Moneda Local: " + moneda_local);
        System.out.println("id_empresa:   " + id_empresa);
        System.out.println("id_usuario:   " + id_usuario);

        if (txt_crear_moneda_nombre.getText().length() == 0 || txt_crear_moneda_simbolo.getText().length() == 0) {
            band = 2;
        } else {
            if (clase_moneda.descripcion_existente(nombre) > 0) {
                band = 3;
            } else {
                if (clase_moneda.simbolo_existente(simbolo) > 0) {
                    band = 4;
                } else {
                    if (moneda_local.equals("1") && clase_moneda.monedalocal_existente(moneda_local) > 0) {
                        band = 5;
                    } else {
                        band = 0;
                    }
                }
            }

        }

        switch (band) {

            case 0:
                System.out.println("llamamos a la funcion crear_moneda");
                crear_moneda(nombre, simbolo, moneda_local, id_empresa, id_usuario);
                break;

            case 2:
                JOptionPane.showMessageDialog(null, "Por favor ingrese los campos Nombre y Simbolo de la Moneda", "ERROR", JOptionPane.ERROR_MESSAGE);
                break;

            case 3:
                JOptionPane.showMessageDialog(null, "Ya existe una Moneda con el mismo Nombre.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                break;

            case 4:
                JOptionPane.showMessageDialog(null, "Ya existe una Moneda con el mismo Simbolo.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                break;

            case 5:
                JOptionPane.showMessageDialog(null, "Ya existe una Moneda Local.\n Acceda a la Moneda Local y quite el check de la casilla MONEDA LOCAL.", "ERROR", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }//GEN-LAST:event_btn_guardar_empresatransporteActionPerformed

    private void btn_cancelar_guardar_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_guardar_detalleActionPerformed
        limpiar_caja_texto_crear_detalle_producto();
        panel_nuevo_detalle.setVisible(false);
        btn_guardar_detalle.setVisible(false);
        btn_nuevo_detalle.setVisible(true);
    }//GEN-LAST:event_btn_cancelar_guardar_detalleActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        System.out.println("Ejecutandose CANCELAR MATERIAL");
        System.out.println("==============================");

        int id_producto = id_producto_global;
        int id_producto_detalle;
        ResultSet r;
        int respuesta;

        if (band_modificar != 1) {
            //Se ejecuta si estamos ejecutando una creacion    
            respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea CANCELAR la CREACION de este MATERIAL?", "Cancelar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                try {
                    r = sentencia.executeQuery("select id_producto_detalle from TProducto_detalle where id_producto = '" + id_producto + "'");
                    while (r.next()) {
                        id_producto_detalle = Integer.parseInt(r.getString("id_producto_detalle"));
                        clase_producto_detalle.eliminar(id_producto_detalle);
                    }

                    if (clase_producto.eliminar(id_producto)) {

                        System.out.println("El registro del Material se logró cancelar exitosamente!");
                        operaciones_postModificacion();

                        JOptionPane.showMessageDialog(null, "La CREACION del MATERIAL se logro exitosamente");

                    } else {
                        JOptionPane.showMessageDialog(null, "Ocurrio al CANCELAR la creacion del MATERIAL.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos del MATERIAL", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            //Se ejecuta si estamos ejecutando una modificacion    
            respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea CANCELAR la MODIFICACIÓN de este MATERIAL?", "Cancelar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                System.out.println("La creacion del Material se logró cancelar exitosamente!");
                operaciones_postModificacion();
                //JOptionPane.showMessageDialog(null, "La Guia se Cancelo exitosamente");
            }
        }
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void txt_precioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_precioKeyTyped
        JTextField caja = txt_precio;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_precioKeyTyped

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarActionPerformed
        System.out.println("\nEliminar Detalle Producto");
        System.out.println("==========================");

        int fila;
        int id_producto_detalle;
        int respuesta;
        try {
            fila = tabla_detalle.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar el registro a borrar");
            } else {
                respuesta = JOptionPane.showConfirmDialog(null, "¿Desea eliminar?", "Eliminar", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    m = (DefaultTableModel) tabla_detalle.getModel();
                    id_producto_detalle = Integer.parseInt((String) m.getValueAt(fila, 0));
                    System.out.println("el id_producto_detalle que se eliminara es: " + id_producto_detalle);
                    if (clase_producto_detalle.eliminar(id_producto_detalle)) {
                        System.out.println("el detalle se logró eliminar exitosamente!");

                        JOptionPane.showMessageDialog(null, "El Detalle se Eliminó exitosamente!");

                        System.out.println("actualizamos tabla detalle producto");
                        mostrar_tabla_producto_detalle(id_producto_global);
                    } else {
                        JOptionPane.showMessageDialog(null, "Ocurrio al eliminar el detalle.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al eliminar el detalle.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_EliminarActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        int band = 0;

        //Campos usados para capturar los id's de los combos
        String tabla;
        String campocondicion;
        String campocapturar;

        //Campos a guardar
        int id_producto = id_producto_global;
        String codigo = txt_codigo.getText().trim();

        String descripcion = "";
        if (txt_descripcion.getText().trim().length() < 1) {
            band = 2;
        } else {
            descripcion = txt_descripcion.getText().trim();
        }

        String modelo = txt_modelo.getText().trim();

        float peso = 0;
        if (txt_peso.getText().trim().length() > 0) {
            peso = Float.parseFloat(txt_peso.getText().trim());
        }

        String marca = txt_marca.getText().trim();

        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;

        int id_unidad = 0;
        if (cbo_unidad.getSelectedItem().toString().trim().length() < 1) {
            band = 3;
        } else {
            campocapturar = "id_unidad";
            tabla = "tunidad_producto";
            campocondicion = "descripcion";
            id_unidad = CapturarId(campocapturar, tabla, campocondicion, cbo_unidad.getSelectedItem().toString().trim());
            System.out.println("id capturado: " + id_unidad);
        }

        int id_productotipo = 0;
        if (cbo_tipoproducto.getSelectedItem().toString().trim().length() < 1) {
            band = 4;
        } else {
            campocapturar = "id_productotipo";
            tabla = "tproducto_tipo";
            campocondicion = "descripcion";
            id_productotipo = CapturarId(campocapturar, tabla, campocondicion, cbo_tipoproducto.getSelectedItem().toString().trim());
            System.out.println("id capturado: " + id_productotipo);
        }

        String moneda_material = cbo_moneda_material.getSelectedItem().toString().trim();
        String precio_material_aux = txt_unidad_precio_unitario.getText().trim();
        int id_moneda = 0;
        float precio_promedio = 0;

        float precio_manoobra = 0;
        float precio_material = 0;
        float precio_equipo = 0;

        System.out.println("moneda_material: " + moneda_material.length());
        System.out.println("precio_material: " + precio_material_aux.length());

        if (moneda_material.length() == 0 && (txt_unidad_precio_unitario.getText().trim().length() == 0 || txt_unidad_precio_manoobra.getText().trim().length() == 0 || txt_unidad_precio_material.getText().trim().length() == 0 || txt_unidad_precio_equipo.getText().trim().length() == 0)) {
            id_moneda = 0;
        } else {
            if (moneda_material.length() == 0 && ((txt_unidad_precio_unitario.getText().trim().length() >= 1 || txt_unidad_precio_manoobra.getText().trim().length() >= 1 || txt_unidad_precio_material.getText().trim().length() >= 1 || txt_unidad_precio_equipo.getText().trim().length() >= 1))) {
                band = 5;
            } else {
                if (moneda_material.length() >= 1 && (txt_unidad_precio_unitario.getText().trim().length() == 0 || txt_unidad_precio_manoobra.getText().trim().length() == 0 || txt_unidad_precio_material.getText().trim().length() == 0 || txt_unidad_precio_equipo.getText().trim().length() == 0)) {
                    band = 6;
                } else {
                    campocapturar = "id_moneda";
                    tabla = "tmoneda";
                    campocondicion = "nombre";
                    id_moneda = CapturarId(campocapturar, tabla, campocondicion, cbo_moneda_material.getSelectedItem().toString().trim());
                    System.out.println("id capturado: " + id_moneda);
                    precio_promedio = Float.parseFloat(txt_unidad_precio_unitario.getText().trim());

                    precio_manoobra = Float.parseFloat(txt_unidad_precio_manoobra.getText().trim());
                    precio_material = Float.parseFloat(txt_unidad_precio_material.getText().trim());
                    precio_equipo = Float.parseFloat(txt_unidad_precio_equipo.getText().trim());
                }
            }
        }

        int guardado = 1;

        String referencia_precio = txt_referencia_precio.getText().trim();
        String descripcion_coloquial = txt_descripcion_coloquial.getText().trim();

        switch (band) {
//            case 1:
//                JOptionPane.showMessageDialog(null, "Falta ingresar el campo: CODIGO.\n Por favor ingrese el campo en mención.", "ERROR: 1", JOptionPane.ERROR_MESSAGE);
//                break;

            case 2:
                JOptionPane.showMessageDialog(null, "Falta ingresar el campo: DESCRIPCION.\n Por favor ingrese el campo en mención.", "ERROR: 2", JOptionPane.ERROR_MESSAGE);
                break;

            case 3:
                JOptionPane.showMessageDialog(null, "Falta seleccionar el campo: UNIDAD DE MEDIDA.\n Por favor seleccione el campo en mención.", "ERROR: 3", JOptionPane.ERROR_MESSAGE);
                break;

            case 4:
                JOptionPane.showMessageDialog(null, "Falta seleccionar el campo: TIPO DE MATERIAL.\n Por favor seleccione el campo en mención.", "ERROR: 4", JOptionPane.ERROR_MESSAGE);
                break;

            case 5:
                JOptionPane.showMessageDialog(null, "Para poder Registrar el Precio Ingresa, es necesario que seleccione una Moneda", "ERROR: 5", JOptionPane.ERROR_MESSAGE);
                break;

            case 6:
                JOptionPane.showMessageDialog(null, "Se seleccionó una Moneda, por favor ingrese el precio al que hace referencia la moneda seleccionada.", "ERROR: 6", JOptionPane.ERROR_MESSAGE);
                break;

            case 0:
                System.out.println("\nSe presionó el boton GUARDAR/MODIFICAR MATERIAL");
                System.out.println("=============================================");
                System.out.println("id_producto     : " + id_producto);
                System.out.println("codigo          : " + codigo);
                System.out.println("descripcion     : " + descripcion);
                System.out.println("modelo          : " + modelo);
                System.out.println("peso            : " + peso);
                System.out.println("marca           : " + marca);
                System.out.println("id_unidad       : " + id_unidad);
                System.out.println("id_productotipo : " + id_productotipo);
                System.out.println("id_empresa      : " + id_empresa);
                System.out.println("id_usuario      : " + id_usuario);
                System.out.println("guardado        : " + guardado);
                System.out.println("id_moneda       : " + id_moneda);
                System.out.println("precio_promedio : " + precio_promedio);
                System.out.println("precio_mano_de_obra : " + precio_manoobra);
                System.out.println("precio_material : " + precio_material);
                System.out.println("precio_equipo : " + precio_equipo);

                System.out.println("Se llama a la funcion Modificar_Producto");
                modificar_Producto(id_producto, codigo, descripcion, modelo, peso, marca, id_unidad, id_productotipo, id_empresa, id_usuario, guardado, id_moneda, precio_promedio, precio_manoobra, precio_material, precio_equipo, referencia_precio, descripcion_coloquial);

                break;
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void tabla_generalMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_generalMouseReleased
        if (band_index == 0) {
            int fila;
            int id_producto;
            fila = tabla_general.getSelectedRow();
            m = (DefaultTableModel) tabla_general.getModel();
            id_producto = Integer.parseInt((String) m.getValueAt(fila, 0));
            mostrar_datos_producto(id_producto);
        }
    }//GEN-LAST:event_tabla_generalMouseReleased

    private void tabla_generalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_generalKeyReleased
        if (band_index == 0) {
            int fila;
            int id_producto;
            fila = tabla_general.getSelectedRow();
            m = (DefaultTableModel) tabla_general.getModel();
            id_producto = Integer.parseInt((String) m.getValueAt(fila, 0));
            mostrar_datos_producto(id_producto);
        }
    }//GEN-LAST:event_tabla_generalKeyReleased

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
        int fil = tabla_general.getSelectedRow();
        if (fil == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un Material para Modificar");
            band_index = 1;
        } else {
            System.out.println("\nModifar Material");
            System.out.println("==================");

            System.out.println("inicializar la bandera crear en uno");
            band_index = 1;
            crear0_modificar1_producto = 0;
            band_mantenimiento_producto_detalle = 0;
            band_modificar = 1; //Indicamos que es una modificacion y que se tiene que activar el boton cancelar y no eliminara la factura

            DefaultComboBoxModel modelocombo = new DefaultComboBoxModel();
            System.out.println("Mostrar Combo Proveedor");
            if (band_cbo_proveedor == 0) {
                cbo_proveedor.setModel(modelocombo);
                mostrar_combo_proveedor();
            }

            System.out.println("Mostrar Combo Moneda Proveedor");
            if (band_cbo_moneda_proveedor == 0) {
                cbo_moneda_proveedor.setModel(modelocombo);
                mostrar_combo_moneda_proveedor();
            }

            System.out.println("cambiando el titulo");
            lbl_titulo.setText("Modificar Material");

            System.out.println("activar las cajas de texto para el registro");
            activar_caja_texto("editable");

            System.out.println("ocultar botones: vista previa, imprimir, creacion");
            btn_vista_previa.setVisible(false);
            btn_imprimir.setVisible(false);
            btn_creacion.setVisible(false);

            System.out.println("mostrar botones: guardar, cancelar y otros");
            btn_cancelar.setVisible(true);
            btn_guardar.setVisible(true);
            btn_guardar.setText("Modificar");
            btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_actualizar_32_32.png")));

            mostrar_botones("mostrar");

            System.out.println("desactivar barra de herramientas");
            activar_barra_herramientas("desactivar");
        }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        System.out.println("Ejecutandose ELIMINAR MATERIAL");
        System.out.println("===========================");

        int fila = tabla_general.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un Material para Eliminar");
        } else {
            int id_producto = Integer.parseInt((String) m.getValueAt(fila, 0));
            int id_producto_detalle;
            ResultSet r;
            int respuesta;
            respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea ELIMINAR este MATERIAL?", "Eliminar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                System.out.println("el id_producto que se eliminar es: " + id_producto);

                try {
                    r = sentencia.executeQuery("select id_producto_detalle from TProducto_detalle where id_producto = '" + id_producto + "'");
                    while (r.next()) {
                        id_producto_detalle = Integer.parseInt(r.getString("id_producto_detalle"));
                        clase_producto_detalle.eliminar(id_producto_detalle);
                    }

                    if (clase_producto.eliminar(id_producto)) {

                        System.out.println("El registro del Material se logró Eliminar exitosamente!");
                        operaciones_postModificacion();
                        mostrar_tabla_producto("");

                    } else {
                        JOptionPane.showMessageDialog(null, "Ocurrio al CANCELAR la creacion del MATERIAL.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos del MATERIAL", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

            }
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imprimirActionPerformed
//        String numero_factura = "";
//        String tipo_comprobante = "";
//        if (txt_seriefactura.getText().trim().length() > 0) {
//            tipo_comprobante = "FACTURA";
//            numero_factura = "" + txt_seriefactura.getText().trim() + " - " + txt_numerofactura.getText().trim();
//        }
//
//        if (clase_guia.imprimir(id_guia_global)) {
//            try {
//                String rutaInforme = "reportes\\Guia.jasper";
//                Map parametros = new HashMap();
//                parametros.put("id_guia", id_guia_global);
//                parametros.put("numero_factura", numero_factura);
//                parametros.put("tipo_comprobante", tipo_comprobante);
//                JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
//                JasperViewer view = new JasperViewer(print, false);
//                view.setTitle("GUIA -  SERIE: " + txt_serie.getText() + " - NUMERO: " + txt_numero_guia.getText());
//                view.setVisible(true);
//                btn_modificar.setEnabled(false);
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: " + e, "ERROR", JOptionPane.ERROR_MESSAGE);
//            }
//        } else {
//            JOptionPane.showMessageDialog(null, "Ocurrio al guarar la Impresion", "ERROR", JOptionPane.ERROR_MESSAGE);
//        }
    }//GEN-LAST:event_btn_imprimirActionPerformed

    private void txt_buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ejecucion_de_buscador();
        }
    }//GEN-LAST:event_txt_buscarKeyReleased

    private void btn_vista_previaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vista_previaActionPerformed
//        String numero_factura = "";
//        String tipo_comprobante = "";
//        if (txt_seriefactura.getText().trim().length() > 0) {
//            tipo_comprobante = "FACTURA";
//            numero_factura = "" + txt_seriefactura.getText().trim() + " - " + txt_numerofactura.getText().trim();
//        }
//
//        try {
//            String rutaInforme = "reportes\\Guia_vista_previa.jasper";
//            Map parametros = new HashMap();
//            parametros.put("id_guia", id_guia_global);
//            parametros.put("numero_factura", numero_factura);
//            parametros.put("tipo_comprobante", tipo_comprobante);
//            JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
//            JasperViewer view = new JasperViewer(print, false);
//            view.setTitle("GUIA -  SERIE: " + txt_serie.getText() + " - NUMERO: " + txt_numero_guia.getText());
//            view.setVisible(true);
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: " + e, "ERROR", JOptionPane.ERROR_MESSAGE);
//        }
    }//GEN-LAST:event_btn_vista_previaActionPerformed

    private void cbo_moneda_proveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_moneda_proveedorItemStateChanged
    }//GEN-LAST:event_cbo_moneda_proveedorItemStateChanged

    private void btn_nuevo_monedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_monedaActionPerformed
        cbo_moneda = "cbo_moneda_proveedor";
        dialog_crear_moneda.setSize(429, 350);
        dialog_crear_moneda.setLocationRelativeTo(producto);
        dialog_crear_moneda.setModal(true);
        dialog_crear_moneda.setVisible(true);
    }//GEN-LAST:event_btn_nuevo_monedaActionPerformed

    private void cbo_proveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_proveedorItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_proveedorItemStateChanged

    private void btn_buscar_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_proveedorActionPerformed
        mostrar_tabla_proveedor("");
        dialog_buscar_proveedor.setSize(700, 400);
        dialog_buscar_proveedor.setLocationRelativeTo(producto);
        dialog_buscar_proveedor.setModal(true);
        dialog_buscar_proveedor.setVisible(true);
    }//GEN-LAST:event_btn_buscar_proveedorActionPerformed

    private void btn_nuevo_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_proveedorActionPerformed
        dialog_crear_proveedor.setSize(429, 350);
        dialog_crear_proveedor.setLocationRelativeTo(producto);
        dialog_crear_proveedor.setModal(true);
        dialog_crear_proveedor.setVisible(true);
    }//GEN-LAST:event_btn_nuevo_proveedorActionPerformed

    private void btn_nuevo_tipo_productoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_tipo_productoActionPerformed
        dialog_crear_tipoproducto.setSize(429, 350);
        dialog_crear_tipoproducto.setLocationRelativeTo(producto);
        dialog_crear_tipoproducto.setModal(true);
        dialog_crear_tipoproducto.setVisible(true);
    }//GEN-LAST:event_btn_nuevo_tipo_productoActionPerformed

    private void cbo_tipoproductoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_tipoproductoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_tipoproductoItemStateChanged

    private void txt_crear_moneda_nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_crear_moneda_nombreKeyTyped
        JTextField caja = txt_crear_moneda_nombre;
        int limite = 100;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_crear_moneda_nombreKeyTyped

    private void txt_crear_moneda_simboloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_crear_moneda_simboloKeyTyped
        JTextField caja = txt_crear_moneda_simbolo;
        int limite = 10;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_crear_moneda_simboloKeyTyped

    private void btn_cancelar_crear_empresatransporte1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_crear_empresatransporte1ActionPerformed
        System.out.println("limpiar cajas de texto");
        limpiar_caja_texto_crear_unidadmedida();
        dialog_crear_unidadmaterial.dispose();
    }//GEN-LAST:event_btn_cancelar_crear_empresatransporte1ActionPerformed

    private void btn_guardar_empresatransporte1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardar_empresatransporte1ActionPerformed
        System.out.println("\npresionó boton Guardar Unidad de Medida");
        System.out.println("=========================================");

        System.out.println("capturando datos ingresados");

        String descripcion = txt_crear_unidadmedida_descripcion.getText().trim();
        String codigo = txt_crear_unidadmedida_codigo.getText().trim();
        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;

        if (txt_crear_unidadmedida_descripcion.getText().length() == 0 || txt_crear_unidadmedida_codigo.getText().length() == 0) {
            System.out.println("\nFalta ingresar Codigo o Descripcion");
            JOptionPane.showMessageDialog(null, "Los campos Código y Descripcion son necesarios\n Por favor ingrese estos campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            if (clase_unidadMedida.codigo_existente(codigo) > 0) {
                JOptionPane.showMessageDialog(null, "Codigo de la Unidad de Medida existente.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("llamamos a la funcion crear");
                crear_unidadmedida(codigo, descripcion, id_empresa, id_usuario);
            }
        }
    }//GEN-LAST:event_btn_guardar_empresatransporte1ActionPerformed

    private void txt_crear_unidadmedida_codigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_crear_unidadmedida_codigoKeyTyped
        JTextField caja = txt_codigo;
        int limite = 4;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_crear_unidadmedida_codigoKeyTyped

    private void txt_crear_unidadmedida_descripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_crear_unidadmedida_descripcionKeyTyped
        JTextField caja = txt_descripcion;
        int limite = 100;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_crear_unidadmedida_descripcionKeyTyped

    private void btn_cancelar_crear_empresatransporte2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_crear_empresatransporte2ActionPerformed
        txt_crear_tipomaterial_descripcion.setText("");
        dialog_crear_tipoproducto.dispose();
    }//GEN-LAST:event_btn_cancelar_crear_empresatransporte2ActionPerformed

    private void btn_guardar_empresatransporte2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardar_empresatransporte2ActionPerformed
        System.out.println("\npresionó boton Guardar Tipo de Material");
        System.out.println("=========================================");

        System.out.println("capturando datos ingresados");

        String descripcion = txt_crear_tipomaterial_descripcion.getText().trim();
        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;

        if (descripcion.length() == 0) {
            JOptionPane.showMessageDialog(null, "El campo Descripcion es necesarios\n Por favor ingrese estos campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            if (clase_productotipo.descripcion_existente(descripcion) > 0) {
                JOptionPane.showMessageDialog(null, "Descripcion del Tipo de Material existente.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("llamamos a la funcion crear");
                crear_tipoproducto(descripcion, id_empresa, id_usuario);
            }
        }
    }//GEN-LAST:event_btn_guardar_empresatransporte2ActionPerformed

    private void txt_crear_tipomaterial_descripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_crear_tipomaterial_descripcionKeyTyped
        JTextField caja = txt_descripcion;
        int limite = 50;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_crear_tipomaterial_descripcionKeyTyped

    private void btn_nueva_moneda_materialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nueva_moneda_materialActionPerformed
        cbo_moneda = "cbo_moneda_material";
        dialog_crear_moneda.setSize(429, 350);
        dialog_crear_moneda.setLocationRelativeTo(producto);
        dialog_crear_moneda.setModal(true);
        dialog_crear_moneda.setVisible(true);
    }//GEN-LAST:event_btn_nueva_moneda_materialActionPerformed

    private void txt_unidad_precio_unitarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_unidad_precio_unitarioKeyTyped
        JTextField caja = txt_unidad_precio_unitario;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_unidad_precio_unitarioKeyTyped

    private void txt_codigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_codigoKeyTyped
        JTextField caja = txt_codigo;
        int limite = 4;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_codigoKeyTyped

    private void txt_descripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_descripcionKeyTyped
        JTextField caja = txt_descripcion;
        int limite = 500;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_descripcionKeyTyped

    private void txt_marcaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_marcaKeyTyped
        JTextField caja = txt_marca;
        int limite = 50;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_marcaKeyTyped

    private void txt_modeloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_modeloKeyTyped
        JTextField caja = txt_modelo;
        int limite = 50;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_modeloKeyTyped

    private void txt_pesoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pesoKeyTyped
        JTextField caja = txt_peso;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_pesoKeyTyped

    private void txt_unidad_precio_manoobraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_unidad_precio_manoobraKeyTyped
        JTextField caja = txt_unidad_precio_manoobra;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_unidad_precio_manoobraKeyTyped

    private void txt_unidad_precio_materialKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_unidad_precio_materialKeyTyped
        JTextField caja = txt_unidad_precio_material;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_unidad_precio_materialKeyTyped

    private void txt_unidad_precio_equipoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_unidad_precio_equipoKeyTyped
        JTextField caja = txt_unidad_precio_equipo;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_unidad_precio_equipoKeyTyped

    private void txt_referencia_precioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_referencia_precioKeyTyped
        JTextField caja = txt_referencia_precio;
        int limite = 500;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_referencia_precioKeyTyped

    private void txt_descripcion_coloquialKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_descripcion_coloquialKeyTyped
        JTextField caja = txt_descripcion_coloquial;
        int limite = 500;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_descripcion_coloquialKeyTyped

    private void cbo_tipomaterial_buscarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_tipomaterial_buscarItemStateChanged
        ejecucion_de_buscador();
    }//GEN-LAST:event_cbo_tipomaterial_buscarItemStateChanged

    private void cbo_proveedor_buscarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_proveedor_buscarItemStateChanged
        ejecucion_de_buscador();
    }//GEN-LAST:event_cbo_proveedor_buscarItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Eliminar;
    private javax.swing.JMenuItem Modificar;
    private javax.swing.JPanel Panel_detalle;
    private javax.swing.JToolBar barra_buscar;
    private javax.swing.JButton btn_buscar_proveedor;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_cancelar_cliente;
    private javax.swing.JButton btn_cancelar_crear_empresatransporte;
    private javax.swing.JButton btn_cancelar_crear_empresatransporte1;
    private javax.swing.JButton btn_cancelar_crear_empresatransporte2;
    private javax.swing.JButton btn_cancelar_guardar_detalle;
    private javax.swing.JButton btn_cliente_cancelar_busqueda;
    private javax.swing.JButton btn_cliente_seleccionar;
    private javax.swing.JButton btn_crea_cliente;
    private javax.swing.JButton btn_creacion;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_guardar_detalle;
    private javax.swing.JButton btn_guardar_empresatransporte;
    private javax.swing.JButton btn_guardar_empresatransporte1;
    private javax.swing.JButton btn_guardar_empresatransporte2;
    private javax.swing.JButton btn_imprimir;
    private javax.swing.JButton btn_modificar;
    private javax.swing.JButton btn_nueva_moneda_material;
    private javax.swing.JButton btn_nueva_unidad;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JButton btn_nuevo_detalle;
    private javax.swing.JButton btn_nuevo_moneda;
    private javax.swing.JButton btn_nuevo_proveedor;
    private javax.swing.JButton btn_nuevo_tipo_producto;
    private javax.swing.JButton btn_vista_previa;
    private javax.swing.JComboBox cbo_moneda_material;
    private javax.swing.JComboBox cbo_moneda_proveedor;
    private javax.swing.JComboBox cbo_proveedor;
    private javax.swing.JComboBox cbo_proveedor_buscar;
    private javax.swing.JComboBox cbo_tipomaterial_buscar;
    private javax.swing.JComboBox cbo_tipoproducto;
    private javax.swing.JComboBox cbo_unidad;
    private javax.swing.JPanel centro;
    private javax.swing.JDialog dialog_buscar_proveedor;
    private javax.swing.JDialog dialog_crear_moneda;
    private javax.swing.JDialog dialog_crear_proveedor;
    private javax.swing.JDialog dialog_crear_tipoproducto;
    private javax.swing.JDialog dialog_crear_unidadmaterial;
    private javax.swing.JDialog dialog_fecha_creacion;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel label_buscar;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JPopupMenu mantenimiento_tabla_detalle_factura;
    private javax.swing.JPanel norte;
    private javax.swing.JPanel panel_nuevo_detalle;
    private javax.swing.JPanel panel_tabla;
    private javax.swing.JTable tabla_cliente_buscar;
    private javax.swing.JTable tabla_detalle;
    private javax.swing.JTable tabla_general;
    private javax.swing.JTextField txt_buscar;
    private javax.swing.JTextField txt_buscar_proveedor;
    private javax.swing.JTextField txt_celular_proveedor_crear;
    private javax.swing.JTextField txt_codigo;
    private javax.swing.JTextField txt_correo_proveedor_crear;
    private javax.swing.JCheckBox txt_crear_moneda_check_moneda_local;
    private javax.swing.JTextField txt_crear_moneda_nombre;
    private javax.swing.JTextField txt_crear_moneda_simbolo;
    private javax.swing.JTextField txt_crear_tipomaterial_descripcion;
    private javax.swing.JTextField txt_crear_unidadmedida_codigo;
    private javax.swing.JTextField txt_crear_unidadmedida_descripcion;
    private javax.swing.JTextField txt_descripcion;
    private javax.swing.JTextField txt_descripcion_coloquial;
    private javax.swing.JTextField txt_direccion_proveedor_crear;
    private javax.swing.JTextField txt_f_creacion;
    private javax.swing.JTextField txt_f_modificacion;
    private javax.swing.JTextField txt_marca;
    private javax.swing.JTextField txt_modelo;
    private javax.swing.JTextField txt_peso;
    private javax.swing.JTextField txt_precio;
    private javax.swing.JTextField txt_razon_social_proveedor_crear;
    private javax.swing.JTextField txt_referencia_precio;
    private javax.swing.JTextField txt_ruc_proveedor_crear;
    private javax.swing.JTextField txt_telefono_proveedor_crear;
    private javax.swing.JTextField txt_unidad_codigo;
    private javax.swing.JTextField txt_unidad_precio_equipo;
    private javax.swing.JTextField txt_unidad_precio_manoobra;
    private javax.swing.JTextField txt_unidad_precio_material;
    private javax.swing.JTextField txt_unidad_precio_unitario;
    private javax.swing.JTextField txt_usuario;
    // End of variables declaration//GEN-END:variables
}
