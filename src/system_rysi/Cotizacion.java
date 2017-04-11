package system_rysi;

//Clases
import Clases.RenderCotizacion;
import Clases.cDocumentos;
import Clases.cCliente;
import Clases.cCotizacion;
import Clases.cCotizacion_detalle;
import Clases.cFormaPago;
import Clases.cIGV;
import Clases.cMoneda;
import Clases.cTipoCotizacion;

//Otros
import Clases.TextAreaEditor;
import Clases.TextAreaRenderer;
import Clases.cBanco_detalles_cotizacion;
import Clases.cConvertir_Numero_Letra;
import Clases.cCotizacion_cuentabanco;
import Clases.cCuentas_Bancos;
import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.table.TableColumnModel;
//import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author ErC
 */
public class Cotizacion extends javax.swing.JPanel {

    //datos de conexion
    String controlador_index;
    String DSN_index;
    String user_index;
    String password_index;
    private Connection conexion;
    private Statement sentencia;
    //Clases
    cCotizacion clase_cotizacion = new cCotizacion();
    cCotizacion_detalle clase_cotizacion_detalle = new cCotizacion_detalle();
    cCliente clase_cliente = new cCliente();
    cIGV clase_igv = new cIGV();
    cDocumentos clase_documento = new cDocumentos();
    cMoneda clase_moneda = new cMoneda();
    cTipoCotizacion clase_tipocotizacion = new cTipoCotizacion();
    cFormaPago clase_formapago = new cFormaPago();
    cCotizacion_cuentabanco clase_cotizacion_cuentabanco = new cCotizacion_cuentabanco();
    cBanco_detalles_cotizacion clase_banco_detalles_cotizacion = new cBanco_detalles_cotizacion();
    cCuentas_Bancos clase_cuentas_Bancos = new cCuentas_Bancos();
    cConvertir_Numero_Letra clase_numero_letra = new cConvertir_Numero_Letra();
    //Banderas
    DefaultTableModel m;
    int band_index = 0; //sive para saber si estamos creando o mostrando la informacion de la factura, para desactivar funciones de visualizacion de un nuevo detalle
    int band_edicion;
    int band_aprobado;
    int band_rechazado;
    int band_cbo_moneda = 0;
    int band_cbo_tipo_cotizacion = 0;
    int band_cbo_cliente = 0;
    int band_cbo_formapago = 0;
    int band_cbo_igv = 0;
    int band_cbo_atencion = 0;
    int band_cbo_unidadmedida = 0;
    int band_cbo_itempadre = 0;
    int band_mantenimiento_cotizacion_detalle = 0;
    int band_mantenimiento_cotizacion_cuentabanco = 0;
    int crear0_modificar1_cotizacion = 0;
    int crear0_modificar1_cotizacion_detalle = 0;
    int band_crear = 0;         //Sirve para saber si se preciono el boton crear
    int band_modificar = 0;     //Sirve para saber si se preciono el boton modificar
    private Component cotizacion;
    //id globales
    int id_cliente_global;
    int id_igv_global;
    int id_moneda_global;
    int id_tipocotizacion_global;
    int id_documento_global;
    int id_formapago_global;
    int id_cotizacion_global;
    int id_cotizacion_detalle_global;
    int id_cotizacion_cuentabanco_global;
    String numero_inicial_global;
    int id_empresa_index;
    int id_usuario_index;
    String perfil_usuario_index = "";
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public Cotizacion(String controlador, String DSN, String user, String password, int id_empresa, int id_usuario, String perfil_usuario) {
        controlador_index = controlador;
        DSN_index = DSN;
        user_index = user;
        password_index = password;
        id_empresa_index = id_empresa;
        id_usuario_index = id_usuario;
        perfil_usuario_index = perfil_usuario;

        System.out.println("\n\nconectando con Guia");
        initComponents();

        System.out.println("iniciando conexion");
        conexion();

        System.out.println("ocultar el panel de detalle de datos");
        Panel_detalle.setVisible(false);
        lbl_estado.setVisible(false);

//        System.out.println("activando la función de letra Mayúsculas");
//        Activar_letras_Mayusculas();

        System.out.println("Llenar combo cliente");
        AutoCompleteDecorator.decorate(this.cbo_cliente);
        AutoCompleteDecorator.decorate(this.cbo_formapago);
        AutoCompleteDecorator.decorate(this.cbo_unidadmedida);
        AutoCompleteDecorator.decorate(this.cbo_atencion);
        AutoCompleteDecorator.decorate(this.cbo_tipo_cotizacion);

        if (perfil_usuario_index.equals("Solo Lectura")) {
            btn_nuevo.setVisible(false);
            btn_modificar.setVisible(false);
        }

        System.out.println("mostrar tabla Cotizacion");
        mostrar_tabla_cotizacion("");
        mostrar_combo_cliente_buscar();
        band_index = 0;
    }

    //Utilidades
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

    private void activar_letras_Mayusculas() {
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
            //Datos
            txt_fecha.setEditable(true);
            txt_fecha.setEnabled(true);
            cbo_moneda.setEnabled(true);
            cbo_tipo_cotizacion.setEnabled(true);

            //Proyecto
            txt_proyecto.setEditable(true);
            txt_ubicacion.setEditable(true);
            txt_duracion.setEditable(true);


            //Cliente
            cbo_cliente.setEnabled(true);
            cbo_cliente.setEditable(true);
            txt_ruc_cliente.setEditable(false);
            cbo_atencion.setEditable(true);
            cbo_atencion.setEnabled(true);

            //Detalle Cotizacion
            cbo_unidadmedida.setEnabled(true);
            cbo_unidadmedida.setEditable(true);
            txt_cantidad.setEditable(true);
            txt_precio.setEditable(true);
            txt_detalle.setEditable(true);
            txt_unidad_codigo.setEditable(false);

            //Pie
            cbo_formapago.setEnabled(true);
            cbo_formapago.setEditable(true);

            //Totales
            txt_costoneto.setEditable(false);
            txt_ganancia_monto.setEditable(false);
            txt_utilidad_monto.setEditable(false);
            txt_costo_de_venta.setEditable(false);
            txt_descuento_monto.setEditable(false);
            txt_sub_total.setEditable(false);
            txt_igv_monto.setEditable(false);
            txt_costo_total.setEditable(false);

            txt_ganancia_porcentaje.setEditable(true);
            txt_utilidad_porcentaje.setEditable(true);
            txt_descuento_porcentaje.setEditable(true);
            cbo_igv.setEnabled(true);
            cbo_igv.setEditable(false);


            //ventana Crear cliente
            txt_razon_social_cliente_crear.setEditable(true);
            txt_ruc_cliente_crear.setEditable(true);
            txt_direccion_cliente_crear.setEditable(true);
            txt_telefono_cliente_crear.setEditable(true);
            txt_celular_cliente_crear.setEditable(true);
            txt_correo_cliente_crear.setEditable(true);


            //Buscar
            txt_buscar_cliente.setEditable(true);

            //Numero de Cuentas de Bancos
            txt_crear_numerodecuenta.setEditable(true);

            //Moneda
            txt_crear_moneda_nombre.setEditable(true);
            txt_crear_moneda_simbolo.setEditable(true);
            txt_crear_moneda_check_moneda_local.setEnabled(true);

            //Fecha de creacion
            txt_f_creacion.setEditable(false);
            txt_f_modificacion.setEditable(false);
            txt_usuario.setEditable(false);
        }

        if (funcion.equals("no_editable")) {
            //Datos
            txt_fecha.setEditable(false);
            txt_fecha.setEnabled(false);
            cbo_moneda.setEnabled(false);

            cbo_tipo_cotizacion.setEnabled(false);

            //Proyecto
            txt_proyecto.setEditable(false);
            txt_ubicacion.setEditable(false);
            txt_duracion.setEditable(false);

            //Cliente
            cbo_cliente.setEnabled(false);
            txt_ruc_cliente.setEditable(false);
            cbo_atencion.setEnabled(false);


            //Detalle Cotizacion
            cbo_unidadmedida.setEnabled(false);
            txt_cantidad.setEditable(false);
            txt_precio.setEditable(false);
            txt_detalle.setEditable(false);
            txt_unidad_codigo.setEditable(false);

            //Pie
            cbo_formapago.setEnabled(false);

            //Totales
            txt_costoneto.setEditable(false);
            txt_ganancia_monto.setEditable(false);
            txt_utilidad_monto.setEditable(false);
            txt_costo_de_venta.setEditable(false);
            txt_descuento_monto.setEditable(false);
            txt_sub_total.setEditable(false);
            txt_igv_monto.setEditable(false);
            txt_costo_total.setEditable(false);

            txt_ganancia_porcentaje.setEditable(false);
            txt_utilidad_porcentaje.setEditable(false);
            txt_descuento_porcentaje.setEditable(false);
            cbo_igv.setEnabled(false);


            //ventana Crear cliente
            txt_razon_social_cliente_crear.setEditable(false);
            txt_ruc_cliente_crear.setEditable(false);
            txt_direccion_cliente_crear.setEditable(false);
            txt_telefono_cliente_crear.setEditable(false);
            txt_celular_cliente_crear.setEditable(false);
            txt_correo_cliente_crear.setEditable(false);


            //Buscar
            txt_buscar_cliente.setEditable(false);

            //Numero de Cuentas de Bancos
            txt_crear_numerodecuenta.setEditable(false);

            //Moneda
            txt_crear_moneda_nombre.setEditable(false);
            txt_crear_moneda_simbolo.setEditable(false);
            txt_crear_moneda_check_moneda_local.setEnabled(false);

            //Fecha de creacion
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
            btn_aprobar.setEnabled(true);
            btn_rechazar.setEnabled(true);
            txt_buscar.setEnabled(true);
        }

        if (funcion.equals("desactivar")) {
            btn_nuevo.setEnabled(false);
            btn_modificar.setEnabled(false);
            btn_aprobar.setEnabled(false);
            btn_rechazar.setEnabled(false);
            txt_buscar.setEnabled(false);
        }
    }

    private String serie_documento_cotizacion() {
        String serie = "";
        try {
            ResultSet r = sentencia.executeQuery("select id_documento, serie, numero_inicial from tdocumentos where nombre='COTIZACION' and id_empresa='" + id_empresa_index + "'");
            while (r.next()) {
                id_documento_global = Integer.parseInt(r.getString("id_documento"));
                serie = r.getString("serie");
                numero_inicial_global = r.getString("numero_inicial");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el Id y Serie  del Documento COTIZACION", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return serie;
    }

    private void tamaño_de_caja_jtextarea(JTextArea caja, KeyEvent evt, int limite) {
        if (caja.getText().length() == limite) {
            evt.consume();
        }
    }

    //agregar
    private void agregar_cuentas_bancos_cotizacion() {
        ResultSet r;
        int id_cuentabanco;

        try {
            r = sentencia.executeQuery("select id_cuentabanco from TCuentas_Bancos where id_empresa = '" + id_empresa_index + "' and estado = '0'");
            while (r.next()) {
                id_cuentabanco = Integer.parseInt(r.getString("id_cuentabanco"));
                clase_cotizacion_cuentabanco.crear(id_cotizacion_global, id_cuentabanco, id_empresa_index, id_usuario_index);
            }

            mostrar_tabla_cotizacion_cuentabanco();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos de la tabla TCuentas_Bancos", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Mostrar
    private void mostrar_datos_cotizacion(int id_cotizacion) {
        System.out.println("\nMostrar datos de Cotizacion");
        System.out.println("=============================");

        System.out.println("Limpiar cajas de texto");
        limpiar_caja_texto();
        lbl_estado.setVisible(true);
        txt_estado.setVisible(true);

        System.out.println("el id_cotizacion seleccionado es: " + id_cotizacion);
        id_cotizacion_global = id_cotizacion;

        ResultSet r;

        //Numero
        String numero = "";
        String serie = "";
        String id_documento = "";

        //Datos
        String fecha = "";
        String moneda = "";
        String categoria = "";

        //Proyecto
        String proyecto = "";
        String ubicacion = "";
        String duracion = "";

        //Cliente
        String cliente = "";
        String ruc = "";
        String atencion = "";

        //Pago
        String forma_pago = "";

        //Precio
        String costo_neto = "";
        String gasto_gen_por = "";
        String gasto_gen_monto = "";
        String utilidad_por = "";
        String utilidad_monto = "";
        String subtotal = "";
        String descuento_por = "";
        String descuento_monto = "";
        String subtotal_neto = "";
        String igv = "";
        String igv_monto = "";
        String total = "";

        //Creacion
        String f_creacion = "";
        String f_modificacion = "";
        String nombre_usuario = "";

        //estados
        String edicion = "";
        String aprobado = "";
        String rechazado = "";

        try {
            r = sentencia.executeQuery("select \n"
                    + "c.numero,\n"
                    + "d.serie,\n"
                    + "c.id_documento,\n"
                    + "convert(varchar, c.fecha, 103) as fecha,\n"
                    + "m.nombre as moneda,\n"
                    + "tc.descripcion as categoria,\n"
                    + "c.proyecto,\n"
                    + "c.ubicacion,\n"
                    + "c.tiempo_duracion  as duracion,\n"
                    + "cl.razon_social as cliente,\n"
                    + "cl.ruc,\n"
                    + "c.atencion,\n"
                    + "fp.descripcion as forma_pago,\n"
                    + "c.costo_neto,\n"
                    + "c.gasto_gen_por,\n"
                    + "c.gasto_gen_monto,\n"
                    + "c.utilidad_por,\n"
                    + "c.utilidad_monto,\n"
                    + "c.subtotal,\n"
                    + "c.descuento_por,\n"
                    + "c.descuento_monto,\n"
                    + "c.subtotal_neto,\n"
                    + "i.igv,\n"
                    + "c.igv_monto,\n"
                    + "c.total,\n"
                    + "c.f_creacion,\n"
                    + "c.f_modificacion,\n"
                    + "u.nombre as nombre_usuario,\n"
                    + "c.edicion,\n"
                    + "c.aprobado,\n"
                    + "c.rechazado\n"
                    + "from TCotizacion c, TDocumentos d, TMoneda m, TTipoCotizacion tc, TCliente cl, TFormaPago fp, TIgv i, TUsuario u\n"
                    + "where\n"
                    + "c.id_documento = d.id_documento and\n"
                    + "c.id_moneda = m.id_moneda and\n"
                    + "c.id_tipocotizacion = tc.id_tipocotizacion and\n"
                    + "c.id_cliente = cl.id_cliente and\n"
                    + "c.id_formapago = fp.id_formapago and\n"
                    + "c.id_igv = i.id_igv and\n"
                    + "c.id_usuario = u.id_usuario and\n"
                    + "c.id_cotizacion = '" + id_cotizacion + "'");
            while (r.next()) {
                //Numero
                numero = r.getString("numero").trim();
                serie = r.getString("serie").trim();
                id_documento = r.getString("id_documento").trim();

                //Datos
                fecha = r.getString("fecha").trim();
                moneda = r.getString("moneda").trim();
                categoria = r.getString("categoria").trim();

                //Proyecto
                proyecto = r.getString("proyecto").trim();
                ubicacion = r.getString("ubicacion").trim();
                duracion = r.getString("duracion").trim();

                //Cliente
                cliente = r.getString("cliente").trim();
                ruc = r.getString("ruc").trim();
                atencion = r.getString("atencion").trim();

                //Pago
                forma_pago = r.getString("forma_pago").trim();

                //Precio
                costo_neto = r.getString("costo_neto");
                gasto_gen_por = r.getString("gasto_gen_por");
                gasto_gen_monto = r.getString("gasto_gen_monto");
                utilidad_por = r.getString("utilidad_por");
                utilidad_monto = r.getString("utilidad_monto");
                subtotal = r.getString("subtotal");
                descuento_por = r.getString("descuento_por");
                descuento_monto = r.getString("descuento_monto");
                subtotal_neto = r.getString("subtotal_neto");
                igv = r.getString("igv");
                igv_monto = r.getString("igv_monto");
                total = r.getString("total");

                //Creacion
                f_creacion = r.getString("f_creacion");
                f_modificacion = r.getString("f_modificacion").trim();
                nombre_usuario = r.getString("nombre_usuario").trim();

                //estados
                edicion = r.getString("edicion");
                aprobado = r.getString("aprobado");
                rechazado = r.getString("rechazado");
            }

            System.out.println("Cambiando el titulo");
            lbl_titulo.setText("Cotizacion");

            //Numero
            txt_numero_cotizacion.setText(numero);
            txt_serie.setText(serie);
            id_documento_global = Integer.parseInt(id_documento);

            //Datos
            //da formato a la caja de fecha
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            Date setfecha = null;
            try {
                System.out.println("Fecha:" + fecha);
                setfecha = formato.parse(fecha);
            } catch (ParseException ex) {
                Logger.getLogger(factura.class.getName()).log(Level.SEVERE, null, ex);
            }
            txt_fecha.setDate(setfecha);

            mostrar_combo_moneda_buscar(moneda);
            mostrar_combo_tipo_cotizacion_buscar(categoria);

            //Proyecto
            txt_proyecto.setText(proyecto);
            txt_ubicacion.setText(ubicacion);
            txt_duracion.setText(duracion);

            //Cliente
            mostrar_combo_cliente_buscar(cliente);
            txt_ruc_cliente.setText(ruc);
            mostrar_combo_atencion_buscar(atencion);

            //Pago
            mostrar_combo_formapago_buscar(forma_pago);

            //Precio
            txt_costoneto.setText(costo_neto);
            txt_ganancia_porcentaje.setText(gasto_gen_por);
            txt_ganancia_monto.setText(gasto_gen_monto);
            txt_utilidad_porcentaje.setText(utilidad_por);
            txt_utilidad_monto.setText(utilidad_monto);
            txt_costo_de_venta.setText(subtotal);
            txt_descuento_porcentaje.setText(descuento_por);
            txt_descuento_monto.setText(descuento_monto);
            txt_sub_total.setText(subtotal_neto);
            txt_igv_monto.setText(igv_monto);
            txt_costo_total.setText(total);

            mostrar_combo_igv_buscar(igv);

            //Datos de creacion
            txt_f_creacion.setText(f_creacion);
            txt_f_modificacion.setText(f_modificacion);
            txt_usuario.setText(nombre_usuario);

            //activar banderas
            band_mantenimiento_cotizacion_detalle = 1;
            band_mantenimiento_cotizacion_cuentabanco = 1;

            //Mostrar tabla de detalle de guia
            mostrar_tabla_cotizacion_detalle(id_cotizacion);
            System.out.println("Desactivar las cajas de texto para el registro");
            activar_caja_texto("no_editable");

            System.out.println("Ocultar botones: guardar, cancelar y otros");
            mostrar_botones("ocultar");

            System.out.println("Mostrar botones: vista previa, imprimir, creacion");
            btn_vista_previa.setVisible(true);
            btn_creacion.setVisible(true);

            System.out.println("Activar barra de herramientas");
            activar_barra_herramientas("activar");


            band_edicion = Integer.parseInt(edicion);
            band_aprobado = Integer.parseInt(aprobado);
            band_rechazado = Integer.parseInt(rechazado);


            if (band_edicion == 1 && band_aprobado == 0 && band_rechazado == 0) {
                lbl_estado.setVisible(true);
                txt_estado.setText("EDICION");
                btn_modificar.setEnabled(true);
                btn_aprobar.setEnabled(true);
                btn_rechazar.setEnabled(true);
                btn_aprobar.setText("Aprobar");
                btn_rechazar.setText("Rechazar");
            } else {

                if (band_edicion == 1 && band_aprobado == 1 && band_rechazado == 0) {
                    lbl_estado.setVisible(true);
                    txt_estado.setText("APROBADO");
                    btn_modificar.setEnabled(false);
                    btn_aprobar.setEnabled(true);
                    btn_rechazar.setEnabled(false);
                    btn_aprobar.setText("Ver datos de Aprobacion");
                    btn_rechazar.setText("Rechazar");
                } else {
                    if (band_edicion == 1 && band_aprobado == 0 && band_rechazado == 1) {
                        lbl_estado.setVisible(true);
                        txt_estado.setText("RECHAZADO");
                        btn_modificar.setEnabled(false);
                        btn_aprobar.setEnabled(false);
                        btn_rechazar.setEnabled(true);
                        btn_aprobar.setText("Aprobar");
                        btn_rechazar.setText("Ver por que fue Rechazado");
                    } else {
                    }
                }
            }

            mostrar_tabla_cotizacion_cuentabanco();

            System.out.println("Ocultar Panel Detalle");
            panel_nuevo_detalle.setVisible(false);

            System.out.println("Mostrar el panel de detalle de datos");
            Panel_detalle.setVisible(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos de la Factura", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_numero_cotizacion() {
        String numero_cotizacion = clase_cotizacion.Generar_Numero(id_empresa_index);
        System.out.println("El numero de Cotizacion generado es: " + numero_cotizacion);
        System.out.println("El numero inicial del documento COTIZACION es: " + numero_inicial_global);

        if (Integer.parseInt(numero_cotizacion) > Integer.parseInt(numero_inicial_global)) {
            System.out.println("El NUMERO COTIZACION GENERADO es mayor al  NUMERO INICIAL DEL DOCUMENTO");
            System.out.println("Se imprime el NUMERO COTIZACION GENERADO en el label");
            txt_numero_cotizacion.setText(numero_cotizacion);
        } else {
            System.out.println("El NUMERO GUIA GENERADO es MENOR o IGUAL al  NUMERO INICIAL DEL DOCUMENTO");
            System.out.println("Se imprime el NUMERO INICIAL DEL DOCUMENTO GUIA en el label");
            txt_numero_cotizacion.setText(numero_inicial_global);
        }
    }

    private void mostrar_botones(String funcion) {
        if (funcion.equals("mostrar")) {
            //Buscar
            btn_buscar_cliente.setVisible(true);
            btn_buscar_detalle.setVisible(true);
            btn_buscar_materiales.setVisible(true);
            btn_nuevo_numerocuentabanco.setVisible(true);
            btn_importar.setVisible(true);

            //Crear
            btn_nuevo_cliente.setVisible(true);
            btn_nuevo_detalle.setVisible(true);
            btn_nuevo_moneda.setVisible(true);
            btn_nuevo_detalle.setVisible(true);


        }

        if (funcion.equals("ocultar")) {
            //Buscar
            btn_buscar_cliente.setVisible(false);
            btn_buscar_detalle.setVisible(false);
            btn_buscar_materiales.setVisible(false);
            btn_nuevo_numerocuentabanco.setVisible(false);
            btn_importar.setVisible(false);

            //Crear
            btn_nuevo_cliente.setVisible(false);
            btn_nuevo_detalle.setVisible(false);
            btn_nuevo_moneda.setVisible(false);
            btn_nuevo_detalle.setVisible(false);

            //botones de control
            btn_guardar.setVisible(false);
            btn_cancelar.setVisible(false);
        }
    }

    //Obtener
    private String obtener_idunidad(String codigo_unidad) {
        String id_unidad = "";

        try {
            ResultSet r = sentencia.executeQuery("select id_unidad from tunidad_producto where codigo = '" + codigo_unidad + "'");
            while (r.next()) {
                id_unidad = (r.getString("id_unidad").trim());
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al obtener el id_unidad", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        return id_unidad;
    }

    private String obtener_descripcion_material(String codigo_unidad) {
        String descripcion_unidad = "";

        try {
            ResultSet r = sentencia.executeQuery("select descripcion from tunidad_producto where codigo='" + codigo_unidad + "'");
            while (r.next()) {
                descripcion_unidad = r.getString("descripcion").trim();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer los Datos de la Unidad de Medida", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        return descripcion_unidad;

    }

    private String obtener_categoria_padre_modificar(int id_cotizaciondetalle) {
        String categ_padre = "";

        try {
            ResultSet r = sentencia.executeQuery("select categ_padre from TCotizacion_detalle where id_cotizaciondetalle='" + id_cotizaciondetalle + "'");
            while (r.next()) {
                categ_padre = r.getString("categ_padre").trim();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer los Datos de Categoria Padre", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        return categ_padre;
    }

    //Mostrar tablas
    private void mostrar_tabla_cotizacion_cuentabanco() {
        ResultSet r;
        try {
            r = sentencia.executeQuery("select ccb.id_cotizacion_cuentabanco, cb.descripcion from TCotizacion_cuentabanco ccb, TCuentas_Bancos cb where  ccb.id_cuentabanco = cb.id_cuentabanco and ccb.id_cotizacion ='" + id_cotizacion_global + "' order by cb.descripcion desc");

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("N° Cuenta");

            String fila[] = new String[2];
            while (r.next()) {
                fila[0] = r.getString("id_cotizacion_cuentabanco").trim();
                fila[1] = r.getString("descripcion").trim();
                modelo.addRow(fila);
            }

            tabla_cuenta_banco.setRowHeight(35);
            tabla_cuenta_banco.setModel(modelo);

            TableColumn columna1 = tabla_cuenta_banco.getColumn("");
            columna1.setPreferredWidth(0);
            TableColumn columna2 = tabla_cuenta_banco.getColumn("N° Cuenta");
            columna2.setPreferredWidth(900);


        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Cotizacion - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_tabla_cotizacion(String consulta) {
        ResultSet r;
        String estado = "";

        String edicion;
        String aprobado;
        String rechazado;

        String estado_combo = cbo_estado.getSelectedItem().toString().trim();

        try {
            if (consulta.equals("")) {
                r = sentencia.executeQuery(""
                        + "select "
                        + "edicion, "
                        + "aprobado, "
                        + "rechazado, "
                        + "id_cotizacion, "
                        + "numero, "
                        + "convert(varchar, fecha, 103) as fecha "
                        + "from TCotizacion "
                        + "where "
                        + "numero is not null and "
                        + "id_empresa='" + id_empresa_index + "' "
                        + "order by numero desc");
            } else {
                r = sentencia.executeQuery(consulta);
            }

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Estado");
            modelo.addColumn("");
            modelo.addColumn("Numero");
            modelo.addColumn("Fecha");

            String fila[] = new String[4];
            while (r.next()) {
                edicion = r.getString("edicion").trim();
                aprobado = r.getString("aprobado").trim();
                rechazado = r.getString("rechazado").trim();

                if (edicion.equals("1") && aprobado.equals("0") && rechazado.equals("0")) {
                    estado = "EDICION";
                } else {

                    if (edicion.equals("1") && aprobado.equals("1") && rechazado.equals("0")) {
                        estado = "APROBADO";
                    } else {
                        if (edicion.equals("1") && aprobado.equals("0") && rechazado.equals("1")) {
                            estado = "RECHAZADO";
                        }
                    }
                }

                if (estado.equals(estado_combo) || estado_combo.equals("TODOS")) {
                    fila[0] = estado;
                    fila[1] = r.getString("id_cotizacion").trim();
                    fila[2] = r.getString("numero").trim();
                    fila[3] = r.getString("fecha").trim();
                    modelo.addRow(fila);
                }
            }

            tabla_general.setRowHeight(35);
            tabla_general.setModel(modelo);

            TableColumn columna1 = tabla_general.getColumn("Estado");
            columna1.setPreferredWidth(110);
            TableColumn columna2 = tabla_general.getColumn("");
            columna2.setPreferredWidth(0);
            TableColumn columna3 = tabla_general.getColumn("Numero");
            columna3.setPreferredWidth(100);
            TableColumn columna4 = tabla_general.getColumn("Fecha");
            columna4.setPreferredWidth(100);

            tabla_general.setDefaultRenderer(Object.class, new RenderCotizacion());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Cotizacion - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_tabla_CuentaBanco() {
        ResultSet r;

        try {
            r = sentencia.executeQuery("select id_cuentabanco, descripcion from TCuentas_Bancos where id_empresa = '" + id_empresa_index + "' and estado = '0' order by descripcion asc");

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Descripcion");

            String fila[] = new String[2];
            while (r.next()) {
                fila[0] = r.getString("id_cuentabanco").trim();
                fila[1] = r.getString("descripcion").trim();
                modelo.addRow(fila);
            }

            tabla_cuentabanco_seleccionar.setRowHeight(35);
            tabla_cuentabanco_seleccionar.setModel(modelo);
            TableColumn columna1 = tabla_cuentabanco_seleccionar.getColumn("");
            columna1.setPreferredWidth(0);
            TableColumn columna2 = tabla_cuentabanco_seleccionar.getColumn("Descripcion");
            columna2.setPreferredWidth(900);


        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Nro de Cuenta" + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_tabla_cliente() {
        try {
            ResultSet r = sentencia.executeQuery("select id_cliente, razon_social, direccion, ruc from tcliente order by razon_social asc");
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Razon Social");
            modelo.addColumn("Direccion");
            modelo.addColumn("R.U.C.");

            String fila[] = new String[4];
            while (r.next()) {
                fila[0] = r.getString("id_cliente").trim();
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
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Cliente - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_tabla_cotizacion_detalle_vacia() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("");
        modelo.addColumn("Item");
        modelo.addColumn("Descripción");
        modelo.addColumn("Medida");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio");
        modelo.addColumn("Total");
        tabla_detalle.setModel(modelo);
        TableColumn columna1 = tabla_detalle.getColumn("");
        columna1.setPreferredWidth(0);
        TableColumn columna2 = tabla_detalle.getColumn("Item");
        columna2.setPreferredWidth(100);
        TableColumn columna3 = tabla_detalle.getColumn("Descripción");
        columna3.setPreferredWidth(700);
        TableColumn columna4 = tabla_detalle.getColumn("Medida");
        columna4.setPreferredWidth(100);
        TableColumn columna5 = tabla_detalle.getColumn("Cantidad");
        columna5.setPreferredWidth(100);
        TableColumn columna6 = tabla_detalle.getColumn("Precio");
        columna6.setPreferredWidth(100);
        TableColumn columna7 = tabla_detalle.getColumn("Total");
        columna7.setPreferredWidth(100);
    }

    private void mostrar_tabla_cotizacion_detalle(int id_cotizacion) {
        System.out.println("\nEjecutandose Mostrar_tabla_Guia_detalle");

        try {
            ResultSet r = sentencia.executeQuery("select cd.id_cotizaciondetalle, cd.item, cd.descripcion,  u.codigo, cd.cantidad, cd.precio_unitario, cd.precio_total, cd.no_afecta_total from TCotizacion_detalle cd, TUnidad_producto u  where  cd.id_cotizacion = '" + id_cotizacion + "' and u.id_unidad = cd.id_unidad order by cd.item");

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("ID");
            modelo.addColumn("Item");
            modelo.addColumn("Descripción");
            modelo.addColumn("Unid.");
            modelo.addColumn("Cant.");
            modelo.addColumn("Pre. Unit.");
            modelo.addColumn("Total");

            float item;
            float cantidad;
            float precio_unitario;
            float precio_total = 0;
            float costo_neto = 0;
            String no_afecta_total = "";

            String fila[] = new String[7];
            while (r.next()) {

                if (r.getString("id_cotizaciondetalle").length() > 0) {
                    fila[0] = r.getString("id_cotizaciondetalle");
                }

                if (r.getString("item").length() > 0) {
                    item = Float.parseFloat(r.getString("item"));
                    BigDecimal item_decimal = new BigDecimal(item);
                    item_decimal = item_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                    item = (float) item_decimal.doubleValue();
                    fila[1] = String.valueOf(item);
                }

                if (r.getString("descripcion").length() > 0) {
                    fila[2] = r.getString("descripcion").trim();
                }

                if (r.getString("codigo").length() > 0) {
                    fila[3] = r.getString("codigo").trim();
                }

                if (r.getString("cantidad").length() > 0) {
                    cantidad = Float.parseFloat(r.getString("cantidad"));
                    BigDecimal cantidad_decimal = new BigDecimal(cantidad);
                    cantidad_decimal = cantidad_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                    cantidad = (float) cantidad_decimal.doubleValue();
                    fila[4] = String.valueOf(cantidad);
                }

                if (r.getString("precio_unitario").length() > 0) {
                    precio_unitario = Float.parseFloat(r.getString("precio_unitario"));
                    BigDecimal precio_unitario_decimal = new BigDecimal(precio_unitario);
                    precio_unitario_decimal = precio_unitario_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                    precio_unitario = (float) precio_unitario_decimal.doubleValue();
                    fila[5] = String.valueOf(precio_unitario);
                }

                if (r.getString("precio_total").length() > 0) {
                    precio_total = Float.parseFloat(r.getString("precio_total"));
                    BigDecimal precio_total_decimal = new BigDecimal(precio_total);
                    precio_total_decimal = precio_total_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                    precio_total = (float) precio_total_decimal.doubleValue();
                    fila[6] = String.valueOf(precio_total);
                }

                if (r.getString("no_afecta_total").length() > 0) {
                    no_afecta_total = r.getString("no_afecta_total").trim();
                }

                if (!no_afecta_total.equals("1")) {
                    costo_neto = costo_neto + precio_total;
                }

                modelo.addRow(fila);
            }
            tabla_detalle.setRowHeight(35);
            tabla_detalle.setModel(modelo);

            TableColumn columna1 = tabla_detalle.getColumn("ID");
            columna1.setPreferredWidth(50);

            TableColumn columna2 = tabla_detalle.getColumn("Item");
            columna2.setPreferredWidth(50);

            TableColumn columna3 = tabla_detalle.getColumn("Descripción");
            columna3.setPreferredWidth(700);

            TableColumn columna4 = tabla_detalle.getColumn("Unid.");
            columna4.setPreferredWidth(50);

            TableColumn columna5 = tabla_detalle.getColumn("Cant.");
            columna5.setPreferredWidth(100);

            TableColumn columna6 = tabla_detalle.getColumn("Pre. Unit.");
            columna6.setPreferredWidth(100);

            TableColumn columna7 = tabla_detalle.getColumn("Total");
            columna7.setPreferredWidth(100);

            //Hace que la altura de la celda se modifique segun al contenido
            TableColumnModel cmodel = tabla_detalle.getColumnModel();
            TextAreaRenderer textAreaRenderer = new TextAreaRenderer();
            cmodel.getColumn(2).setCellRenderer(textAreaRenderer);
            TextAreaEditor textEditor = new TextAreaEditor();
            cmodel.getColumn(2).setCellEditor(textEditor);

            //Alinear a la derecha
            DefaultTableCellRenderer derecha = new DefaultTableCellRenderer();
            derecha.setHorizontalAlignment(SwingConstants.RIGHT);
            tabla_detalle.getColumnModel().getColumn(4).setCellRenderer(derecha);
            tabla_detalle.getColumnModel().getColumn(5).setCellRenderer(derecha);
            tabla_detalle.getColumnModel().getColumn(6).setCellRenderer(derecha);
            tabla_detalle.getColumnModel().getColumn(1).setCellRenderer(derecha);

            //Alinear al centro
            DefaultTableCellRenderer centrar = new DefaultTableCellRenderer();
            centrar.setHorizontalAlignment(SwingConstants.CENTER);
            tabla_detalle.getColumnModel().getColumn(3).setCellRenderer(centrar);

            calcular_totales(costo_neto);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Cotizacion detalle - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calcular_totales(float costo_neto) {
        float ganancia_porcentaje;
        float utilidad_porcentaje;
        float descuento_porcentaje;

        if (txt_ganancia_porcentaje.getText().trim().equals("")) {
            ganancia_porcentaje = 0;
        } else {
            ganancia_porcentaje = Float.parseFloat(txt_ganancia_porcentaje.getText().trim());
        }

        if (txt_utilidad_porcentaje.getText().trim().equals("")) {
            utilidad_porcentaje = 0;
        } else {
            utilidad_porcentaje = Float.parseFloat(txt_utilidad_porcentaje.getText().trim());
        }

        if (txt_descuento_porcentaje.getText().trim().equals("")) {
            descuento_porcentaje = 0;
        } else {
            descuento_porcentaje = Float.parseFloat(txt_descuento_porcentaje.getText().trim());
        }

        float ganancia_monto = costo_neto * (ganancia_porcentaje / 100);
        float utilidad_monto = costo_neto * (utilidad_porcentaje / 100);
        float costo_de_venta = costo_neto + ganancia_monto + utilidad_monto;
        float descuento_monto = costo_de_venta * (descuento_porcentaje / 100);

        float sub_total = costo_de_venta - descuento_monto;

        float igv = Float.parseFloat(cbo_igv.getSelectedItem().toString());
        float calculo_igv = sub_total * (igv / 100);
        float total = sub_total + calculo_igv;


        System.out.println("\nMontos Canculados");
        System.out.println("===================");
        System.out.println("Costo Neto          :" + costo_neto);
        System.out.println("Ganancia Porcentaje :" + ganancia_porcentaje);
        System.out.println("Ganancia Monto      :" + ganancia_monto);
        System.out.println("Utilidad Porcentaje :" + utilidad_porcentaje);
        System.out.println("Utilidad Monto      :" + utilidad_monto);
        System.out.println("Costo de venta      :" + costo_de_venta);
        System.out.println("Descuento Porcentaje:" + descuento_porcentaje);
        System.out.println("Descuento Monto     :" + descuento_monto);
        System.out.println("SubTotal            :" + sub_total);
        System.out.println("IGV Porcentaje      :" + igv);
        System.out.println("IGV Monto           :" + calculo_igv);
        System.out.println("Total               :" + total);

        BigDecimal costo_neto_decimal = new BigDecimal(costo_neto);
        costo_neto_decimal = costo_neto_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        costo_neto = (float) costo_neto_decimal.doubleValue();

        BigDecimal ganancia_monto_decimal = new BigDecimal(ganancia_monto);
        ganancia_monto_decimal = ganancia_monto_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        ganancia_monto = (float) ganancia_monto_decimal.doubleValue();

        BigDecimal utilidad_monto_decimal = new BigDecimal(utilidad_monto);
        utilidad_monto_decimal = utilidad_monto_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        utilidad_monto = (float) utilidad_monto_decimal.doubleValue();

        BigDecimal costo_de_venta_decimal = new BigDecimal(costo_de_venta);
        costo_de_venta_decimal = costo_de_venta_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        costo_de_venta = (float) costo_de_venta_decimal.doubleValue();

        BigDecimal descuento_monto_decimal = new BigDecimal(descuento_monto);
        descuento_monto_decimal = descuento_monto_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        descuento_monto = (float) descuento_monto_decimal.doubleValue();

        BigDecimal sub_total_decimal = new BigDecimal(sub_total);
        sub_total_decimal = sub_total_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        sub_total = (float) sub_total_decimal.doubleValue();

        BigDecimal calculo_igv_decimal = new BigDecimal(calculo_igv);
        calculo_igv_decimal = calculo_igv_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        calculo_igv = (float) calculo_igv_decimal.doubleValue();

        BigDecimal total_decimal = new BigDecimal(total);
        total_decimal = total_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        total = (float) total_decimal.doubleValue();

//        String moneda = cbo_moneda.getSelectedItem().toString();
//        String total_letras = clase_numero_letra.getStringOfNumber(total, moneda);

        txt_costoneto.setText("" + costo_neto);
        txt_ganancia_monto.setText("" + ganancia_monto);
        txt_utilidad_monto.setText("" + utilidad_monto);
        txt_costo_de_venta.setText("" + costo_de_venta);
        txt_descuento_monto.setText("" + descuento_monto);
        txt_sub_total.setText("" + sub_total);
        txt_igv_monto.setText("" + calculo_igv);
        txt_costo_total.setText("" + total);
        btn_guardar.setEnabled(true);

    }

    //Limpiar
    private void limpiar_caja_texto() {

        //Datos
        SimpleDateFormat sdf = new SimpleDateFormat("");
        Date d = new Date();
        String s = sdf.format(d);
        txt_fecha.setDate(d);

        //Proyecto
        txt_proyecto.setText("");
        txt_ubicacion.setText("");
        txt_duracion.setText("");

        //Cliente
        txt_ruc_cliente.setText("");

        //Detalle Cotizacion
        txt_cantidad.setText("");
        txt_precio.setText("");
        txt_detalle.setText("");
//        txt_unidad_codigo.setText("");

        //Totales
        txt_costoneto.setText("");
        txt_ganancia_monto.setText("");
        txt_utilidad_monto.setText("");
        txt_costo_de_venta.setText("");
        txt_descuento_monto.setText("");
        txt_sub_total.setText("");
        txt_igv_monto.setText("");
        txt_costo_total.setText("");

        txt_ganancia_porcentaje.setText("0.0");
        txt_utilidad_porcentaje.setText("0.0");
        txt_descuento_porcentaje.setText("0.0");

        //ventana Crear cliente
        txt_razon_social_cliente_crear.setText("");
        txt_ruc_cliente_crear.setText("");
        txt_direccion_cliente_crear.setText("");
        txt_telefono_cliente_crear.setText("");
        txt_celular_cliente_crear.setText("");
        txt_correo_cliente_crear.setText("");

        //Buscar
        txt_buscar_cliente.setText("");

        //Numero de Cuentas de Bancos
        txt_crear_numerodecuenta.setText("");

        //Moneda
        txt_crear_moneda_nombre.setText("");
        txt_crear_moneda_simbolo.setText("");
        txt_crear_moneda_check_moneda_local.setText("");

        //Fecha de creacion
        txt_f_creacion.setText("");
        txt_f_modificacion.setText("");
        txt_usuario.setText("");
    }

    private void limpiar_caja_texto_crear_cliente() {
        txt_razon_social_cliente_crear.setText("");
        txt_ruc_cliente_crear.setText("");
        txt_direccion_cliente_crear.setText("");
        txt_telefono_cliente_crear.setText("");
        txt_celular_cliente_crear.setText("");
        txt_correo_cliente_crear.setText("");
    }

    private void limpiar_caja_texto_crear_moneda() {
        txt_crear_moneda_nombre.setText("");
        txt_crear_moneda_simbolo.setText("");
    }

    private void limpiar_caja_texto_crear_detalle_cotizacion() {
        txt_cantidad.setText("");
        txt_precio.setText("");
        txt_detalle.setText("");
        check_no_afecta_total.setSelected(false);
    }

    //Validaciones
    private static boolean ValidarCorreo(String email) {
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Creaciones
    private void crear_moneda(String nombre, String simbolo, String moneda_local, int id_empresa, int id_usuario) {
        System.out.println("\nejecutandose la función: crear_moneda");

        if (clase_moneda.crear(nombre, simbolo, moneda_local, id_empresa, id_usuario)) {
            System.out.println("\nLa Moneda se logró registrar exitosamente!");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_moneda();
            mostrar_combo_moneda_material_buscar(nombre);

            dialog_crear_moneda.dispose();
            JOptionPane.showMessageDialog(null, "La Moneda se registró exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear la Moneda.\nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear_cliente(String razon_social, String ruc, String direccion, String telefono, String celular, String correo, int id_empresa, int id_usuario) {
        System.out.println("\nejecutandose la función: crear_cliente");

        if (clase_cliente.Cliente_crear(razon_social, ruc, direccion, telefono, celular, correo, id_empresa, id_usuario)) {
            System.out.println("\nEl cliente se logró registrar exitosamente!");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_cliente();

            System.out.println("actualizamos tabla cliente");
            mostrar_tabla_cliente();

            System.out.println("Ejecutando la consulta para saber cual es el ultimo id_cliente generado");

            mostrar_combo_cliente_buscar(razon_social);
            txt_ruc_cliente.setText(ruc);

            txt_buscar_cliente.setText("");
            dialog_crear_cliente.dispose();

            JOptionPane.showMessageDialog(null, "El cliente se registro exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear el cliente. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear_CuentaBanco(String descripcion, String estado, int id_empresa, int id_usuario) {
        System.out.println("\nejecutandose la función: crear_CuentaBanco");

        if (clase_cuentas_Bancos.crear(descripcion, estado, id_empresa, id_usuario)) {
            System.out.println("\nCuenta de Banco se logró registrar exitosamente");

            System.out.println("limpiar cajas de texto");
            txt_crear_numerodecuenta.setText("");

            System.out.println("Agregamos la Nueva Cuenta a la Cotizacion");
            int id_cuentabanco = cuentabanco_id_ultimo();
            clase_cotizacion_cuentabanco.crear(id_cotizacion_global, id_cuentabanco, id_empresa_index, id_usuario_index);
            jPanel11.setVisible(false);
            txt_crear_numerodecuenta.setText("");
            btn_nuevo_numerocuentabanco.setVisible(true);
            mostrar_tabla_cotizacion_cuentabanco();
            JOptionPane.showMessageDialog(null, "La Cuenta se registro exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear La Cuenta de Banco. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear_TipoCotizacion(String descripcion, String estado, int id_empresa, int id_usuario) {
        System.out.println("\nejecutandose la función: crear_TipoCotizacion");

        if (clase_tipocotizacion.crear(descripcion, estado, id_empresa, id_usuario)) {
            System.out.println("\n El Tipo de Cotizacion se logró registrar exitosamente");
        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear TipoCotizacion. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear_FormaPago(String descripcion, String estado, int id_empresa, int id_usuario) {
        System.out.println("\nejecutandose la función: crear_FormaPago");

        if (clase_formapago.crear(descripcion, estado, id_empresa, id_usuario)) {
            System.out.println("\n La Forma de Pago se logró registrar exitosamente");
        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear forma de Pago. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear_cotizacion_detalle(int id_cotizacion, String categ_padre, float item, String descripcion, float cantidad, String id_unidad, float precio_unitario, float precio_total, int id_empresa, int id_usuario, String no_afecta_total) {
        System.out.println("\nejecutandose la función: crear_ctizacion_detalle");

        if (clase_cotizacion_detalle.crear(id_cotizacion, categ_padre, item, descripcion, cantidad, id_unidad, precio_unitario, precio_total, id_empresa, id_usuario, no_afecta_total)) {
            System.out.println("\nEl Detalle de Cotizacion se logró registrar exitosamente!");


            //Obtenemos cual fue el ultimo id de cotizacion detalle creado
            int id_cotizaciondetalle = cotizacion_detalle_id_ultimo();
            System.out.println("\nEl ultimo id_cotizaciondetalle es: " + id_cotizaciondetalle);

            //Modificar la Categoria padre
            if (categ_padre.length() == 0) {
                categ_padre = Integer.toString(id_cotizaciondetalle);
                System.out.println("\nLa categoria padre es " + categ_padre);


                System.out.println("Llamamos a la funcion que modificara a la categoria padre del ultimo cotizacion detalle creado");
                clase_cotizacion_detalle.modificar_categoriapadre(id_cotizaciondetalle, categ_padre, id_usuario);

                System.out.println("Llamamos a la funcion que nos dira cual fue el ultimo item de categoria padre generado");
                item = ultimo_item_categoriapadre(id_cotizacion);


                System.out.println("Aumentamos el valor de item en 1");
                item = item + 1;

                System.out.println("El item generado es: " + item);

                System.out.println("Llamamos a la funcion que modificara el item del detalle.");
                clase_cotizacion_detalle.modificar_item(id_cotizaciondetalle, item, id_usuario);

            } else {
                System.out.println("Llamamos a la funcion que nos dira cual fue el ultimo item de categoria padre generado");
                item = ultimo_item_hijo(id_cotizacion, categ_padre);

                System.out.println("El ultimo item generado es: " + item);

                System.out.println("Aumentamos el valor de item en 0.01");
                double suma = item + 0.01;
                item = (float) suma;

                System.out.println("el nuevo item generado es: " + item);

                System.out.println("Llamamos a la funcion que modificara el item del detalle");
                clase_cotizacion_detalle.modificar_item(id_cotizaciondetalle, item, id_usuario);
            }

            panel_nuevo_detalle.setVisible(false);
            btn_nuevo_detalle.setVisible(true);

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_detalle_cotizacion();

            //Cargar combos
            mostrar_combo_itempadre();

            System.out.println("actualizamos tabla cotizacion_detalle");
            mostrar_tabla_cotizacion_detalle(id_cotizacion);

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear la Guia_Detalle. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private float ultimo_item_categoriapadre(int id_cotizacion) {
        float item = 0;
        try {
            String consulta = "select MAX(item) as item from TCotizacion_detalle where categ_padre = id_cotizaciondetalle and id_cotizacion = '" + id_cotizacion + "'";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                item = Float.parseFloat(r.getString("item"));
                System.out.println("El ultimo item de categoria padre es" + item);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el ultimo Item Padre generado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        return item;
    }

    private float ultimo_item_hijo(int id_cotizacion, String categ_padre) {
        float item = 0;
        try {
            String consulta = "SELECT MAX(item) as item from TCotizacion_detalle where id_cotizacion = '" + id_cotizacion + "' and categ_padre = '" + categ_padre + "'";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                item = Float.parseFloat(r.getString("item"));
                System.out.println("El ultimo item hijo es" + item);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el ultimo Id Hijo generado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        return item;
    }

    private void modificar_cotizacion_detalle(int id_cotizaciondetalle, int id_cotizacion, String categ_padre, float item, String descripcion, float cantidad, String id_unidad, float precio_unitario, float precio_total, int id_empresa, int id_usuario, String no_afecta_total) {
        System.out.println("\nejecutandose la función: modificar_cotizacion_detalle");

        if (clase_cotizacion_detalle.modificar(id_cotizaciondetalle, id_cotizacion, categ_padre, item, descripcion, cantidad, id_unidad, precio_unitario, precio_total, id_empresa, id_usuario, no_afecta_total)) {
            System.out.println("\nEl Detalle de Cotizacion se logró registrar exitosamente!");

            //Modificar la Categoria padre
            if (categ_padre.length() == 0) {
                categ_padre = Integer.toString(id_cotizaciondetalle);
                System.out.println("\nLa categoria padre es " + categ_padre);


                System.out.println("Llamamos a la funcion que modificara a la categoria padre del ultimo cotizacion detalle creado");
                clase_cotizacion_detalle.modificar_categoriapadre(id_cotizaciondetalle, categ_padre, id_usuario);

                System.out.println("Llamamos a la funcion que nos dira cual fue el ultimo item de categoria padre generado");
                item = ultimo_item_categoriapadre(id_cotizacion);


                System.out.println("Aumentamos el valor de item en 1");
                item = item + 1;

                System.out.println("El item generado es: " + item);

                System.out.println("Llamamos a la funcion que modificara el item del detalle.");
                clase_cotizacion_detalle.modificar_item(id_cotizaciondetalle, item, id_usuario);

            } else {
                System.out.println("Llamamos a la funcion que nos dira cual fue el ultimo item de categoria padre generado");
                item = ultimo_item_hijo(id_cotizacion, categ_padre);

                System.out.println("El ultimo item generado es: " + item);

                System.out.println("Aumentamos el valor de item en 0.01");
                double suma = item + 0.01;
                item = (float) suma;

                System.out.println("el nuevo item generado es: " + item);

                System.out.println("Llamamos a la funcion que modificara el item del detalle");
                clase_cotizacion_detalle.modificar_item(id_cotizaciondetalle, item, id_usuario);
            }

            id_cotizacion_detalle_global = 0;
            crear0_modificar1_cotizacion_detalle = 0;

            panel_nuevo_detalle.setVisible(false);
            btn_nuevo_detalle.setVisible(true);

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_detalle_cotizacion();

            System.out.println("actualizamos tabla guia_detalle");
            mostrar_tabla_cotizacion_detalle(id_cotizacion);

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al modificar la Guia_Detalle. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar_Cotizacion(int id_cotizacion, String fecha, String numero, String atencion, String proyecto, String ubicación, String tiempo_duracion, float costo_neto, float gasto_gen_por, float gasto_gen_monto, float utilidad_por, float utilidad_monto, float subtotal, float descuento_por, float descuento_monto, float subtotal_neto, float igv_monto, float total, String total_letras, int id_cliente, int id_igv, int id_moneda, int id_tipocotizacion, int id_documento, int id_formapago, int id_empresa, int id_usuario) {
        System.out.println("ejecutandose la función: modificar_cotizacion");

        if (clase_cotizacion.modificar(id_cotizacion, fecha, numero, atencion, proyecto, ubicación, tiempo_duracion, costo_neto, gasto_gen_por, gasto_gen_monto, utilidad_por, utilidad_monto, subtotal, descuento_por, descuento_monto, subtotal_neto, igv_monto, total, total_letras, id_cliente, id_igv, id_moneda, id_tipocotizacion, id_documento, id_formapago, id_empresa, id_usuario)) {
            System.out.println("La Cotizacion se logró modificar exitosamente!");

            operaciones_postModificacion();

            JOptionPane.showMessageDialog(null, "La Cotizacion se Guardó exitosamente");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al Guardar/Modificar la Cotizacion.\nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
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
        btn_creacion.setVisible(true);
        lbl_estado.setVisible(false);
        txt_estado.setVisible(false);
        System.out.println("activar barra de herramientas");
        activar_barra_herramientas("activar");

        System.out.println("Mostrar tabla guia_detalle_vacia");
        mostrar_tabla_cotizacion_detalle_vacia();

        System.out.println("ocultar el panel de detalle de datos");
        Panel_detalle.setVisible(false);

        System.out.println("actualizamos tabla Guia");
        mostrar_tabla_cotizacion("");

        System.out.println("Inicializamos lo id_globales");
        inicializar_id_global();

        band_index = 0;
//        band_cbo_cliente = 1;
//        band_cbo_conductor = 1;
//        band_cbo_empresatransporte = 1;
        band_mantenimiento_cotizacion_detalle = 0;
        band_mantenimiento_cotizacion_cuentabanco = 0;
    }

    //Mostrar Ultimos Id's
    private void cotizacion_id_ultimo() {
        try {
            String consulta = "SELECT id_cotizacion FROM TCotizacion WHERE id_cotizacion = (SELECT MAX(id_cotizacion) from TCotizacion)";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                id_cotizacion_global = Integer.parseInt(r.getString("id_cotizacion"));
                System.out.println("El ultimo id_cotizacion generado es " + id_cotizacion_global);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el ultimo Id de Cotizacion Creado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int cuentabanco_id_ultimo() {
        int id_cuentabanco = 0;
        try {
            String consulta = "SELECT id_cuentabanco FROM TCuentas_Bancos WHERE id_cuentabanco = (SELECT MAX(id_cuentabanco) from TCuentas_Bancos)";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                id_cuentabanco = Integer.parseInt(r.getString("id_cuentabanco"));
                System.out.println("El ultimo id_cuentabanco generado es " + id_cuentabanco);
            }


        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el ultimo Id de Cuenta Banco", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        return id_cuentabanco;
    }

    private int cotizacion_detalle_id_ultimo() {
        int id_cotizaciondetalle = 0;
        try {
            String consulta = "SELECT id_cotizaciondetalle FROM TCotizacion_detalle WHERE id_cotizaciondetalle = (SELECT MAX(id_cotizaciondetalle) from TCotizacion_detalle)";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                id_cotizaciondetalle = Integer.parseInt(r.getString("id_cotizaciondetalle"));
                System.out.println("El ultimo id_cotizaciondetalle generado es " + id_cotizaciondetalle);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el ultimo Id de Cotizacion - Detalle Creado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        return id_cotizaciondetalle;
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
        //System.out.println("Empresa        :" + empresa);

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

    private int CapturarId_conEmpresa(String campocapturar, String tabla, String campocondicion, String valorcondicion) {
        System.out.println("\nCapturar el " + campocapturar + "");
        System.out.println("===============================");

        System.out.println("Iniciando metodo Capturar id");

        System.out.println("Datos recibidos");
        System.out.println("Valor          :" + campocapturar);
        System.out.println("Tabla          :" + tabla);
        System.out.println("Campo condicion:" + campocondicion);
        System.out.println("Valor condicion:" + valorcondicion);
        System.out.println("Empresa        :" + id_empresa_index);

        int valor = 0;

        try {
            System.out.println("select " + campocapturar + " from " + tabla + " where " + campocondicion + " = '" + valorcondicion + "' and id_empresa = '" + id_empresa_index + "'");
            ResultSet r = sentencia.executeQuery("select " + campocapturar + " from " + tabla + " where " + campocondicion + " = '" + valorcondicion + "' and id_empresa = '" + id_empresa_index + "'");
            while (r.next()) {
                valor = Integer.parseInt(r.getString("" + campocapturar + "").trim());
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al obtener el valor del campo " + campocapturar + " de la tabla " + tabla + "", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("Valor obtenido:" + valor);
        return valor;

    }

    private int cantidad_veces_categoria(String campocapturar, String tabla, String campocondicion, String valorcondicion) {
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
            System.out.println("select count(" + campocapturar + ") as cantidad from " + tabla + " where " + campocondicion + " = '" + valorcondicion + "'");
            ResultSet r = sentencia.executeQuery("select count(" + campocapturar + ") as cantidad from " + tabla + " where " + campocondicion + " = '" + valorcondicion + "'");
            while (r.next()) {
                valor = Integer.parseInt(r.getString("cantidad").trim());
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al obtener el valor del campo " + campocapturar + " de la tabla " + tabla + "", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("Valor obtenido:" + valor);
        return valor;

    }

    private void inicializar_id_global() {
        crear0_modificar1_cotizacion = 0;
        crear0_modificar1_cotizacion_detalle = 0;

        //id globales
        id_cliente_global = 0;
        id_igv_global = 0;
        id_moneda_global = 0;
        id_tipocotizacion_global = 0;
        id_documento_global = 0;
        id_formapago_global = 0;
        id_cotizacion_global = 0;
        id_cotizacion_detalle_global = 0;
        id_cotizacion_cuentabanco_global = 0;
        numero_inicial_global = "";
    }

    //Mostrar combos
    private void mostrar_combo_igv_buscar(String igv) {
        try {
            ResultSet r = sentencia.executeQuery("select igv from tigv where id_empresa='" + id_empresa_index + "' order by predeterminado desc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement(igv);
            String resultado;
            while (r.next()) {
                resultado = (r.getString("igv").trim());
                if (!resultado.equals(igv)) {
                    modelo1.addElement(resultado);
                }
            }
            cbo_igv.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo IGV", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_cliente() {
        System.out.println("Ejecuntandose mostrar_combo_cliente");
        try {
            ResultSet r = sentencia.executeQuery("select razon_social from tcliente order by razon_social asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            DefaultComboBoxModel modelo2 = new DefaultComboBoxModel();
            modelo1.addElement("");
            modelo2.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("razon_social").trim());
                modelo1.addElement(resultado);
                modelo2.addElement(resultado);
            }
            cbo_cliente.setModel(modelo1);
            cbo_cliente_buscar_detalle.setModel(modelo2);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Cliente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Mostrar combos buscar
    private void mostrar_combo_cliente_buscar() {
        try {
            ResultSet r = sentencia.executeQuery("select cl.razon_social from tcliente cl, TCotizacion co where co.id_cliente= cl.id_cliente group by cl.razon_social order by razon_social asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("razon_social").trim());
                modelo1.addElement(resultado);
            }
            cbo_buscar_cliente.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Cliente Buscar", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_formapago() {
        System.out.println("Ejecuntandose mostrar_combo_formapago");
        try {
            ResultSet r = sentencia.executeQuery("select descripcion from TFormaPago order by descripcion asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("descripcion").trim());
                modelo1.addElement(resultado);
            }
            cbo_formapago.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Forma de Pago", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_formapago_buscar(String forma_pago) {
        System.out.println("Ejecuntandose mostrar_combo_formapago");
        try {
            ResultSet r = sentencia.executeQuery("select descripcion from TFormaPago order by descripcion asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement(forma_pago);
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("descripcion").trim());
                if (!resultado.equals(forma_pago)) {
                    modelo1.addElement(resultado);
                }
            }
            cbo_formapago.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Forma de Pago", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_moneda() {
        System.out.println("Ejecuntandose mostrar_combo_moneda");
        try {
            ResultSet r = sentencia.executeQuery("select nombre from tmoneda order by nombre asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            DefaultComboBoxModel modelo2 = new DefaultComboBoxModel();
            modelo1.addElement("");
            modelo2.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("nombre").trim());
                modelo1.addElement(resultado);
                modelo2.addElement(resultado);
            }
            cbo_moneda.setModel(modelo1);
            cbo_moneda_buscar_detalle.setModel(modelo2);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Moneda", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_moneda_buscar(String nombre) {
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
            cbo_moneda.setModel(modelo);
            band_cbo_moneda = 1;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Moneda", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_tipo_cotizacion() {
        System.out.println("Ejecuntandose mostrar_combo_tipo_cotizacion");
        try {
            ResultSet r = sentencia.executeQuery("select descripcion from TTipoCotizacion order by descripcion asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            DefaultComboBoxModel modelo2 = new DefaultComboBoxModel();
            modelo1.addElement("");
            modelo2.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("descripcion").trim());
                modelo1.addElement(resultado);
                modelo2.addElement(resultado);
            }
            cbo_tipo_cotizacion.setModel(modelo1);
            cbo_categoria_buscar_detalle.setModel(modelo2);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Categoría", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_tipo_cotizacion_buscar(String descripcion) {
        System.out.println("Ejecuntandose mostrar_combo_tipo_cotizacion");
        try {
            ResultSet r = sentencia.executeQuery("select descripcion from TTipoCotizacion order by descripcion asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement(descripcion);
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("descripcion").trim());
                if (!resultado.equals(descripcion)) {
                    modelo1.addElement(resultado);
                }
            }
            cbo_tipo_cotizacion.setModel(modelo1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Categoría", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_igv() {
        try {
            ResultSet r = sentencia.executeQuery("select igv from tigv where id_empresa='" + id_empresa_index + "' order by predeterminado desc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            String resultado;
            while (r.next()) {
                resultado = (r.getString("igv").trim());
                modelo1.addElement(resultado);
            }
            cbo_igv.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo IGV", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_atencion() {
        try {
            ResultSet r = sentencia.executeQuery("select atencion from TCotizacion where atencion is not null  group by atencion order by atencion");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("atencion").trim());
                modelo1.addElement(resultado);
            }
            cbo_atencion.setModel(modelo1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Atencion", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_atencion_buscar(String atencion) {
        try {
            ResultSet r = sentencia.executeQuery("select atencion from TCotizacion where atencion is not null  group by atencion order by atencion");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement(atencion);
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("atencion").trim());
                if (!resultado.equals(atencion)) {
                    modelo1.addElement(resultado);
                }
            }
            cbo_atencion.setModel(modelo1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Atencion", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_unidadmedida() {
        try {
            ResultSet r = sentencia.executeQuery("select descripcion from TUnidad_producto order by descripcion");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            String resultado;
            while (r.next()) {
                resultado = (r.getString("descripcion").trim());
                modelo1.addElement(resultado);
            }
            cbo_unidadmedida.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Unidad de Medida", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_unidadmedida_modificar(String descripcion) {
        try {
            ResultSet r = sentencia.executeQuery("select descripcion from TUnidad_producto order by descripcion");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement(descripcion);
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("descripcion").trim());
                if (!resultado.equals(descripcion)) {
                    modelo1.addElement(resultado);
                }
            }
            cbo_unidadmedida.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Unidad de Medida", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_unidadmedida_buscar() {
        try {
            ResultSet r = sentencia.executeQuery("select descripcion from TUnidad_producto order by descripcion");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("descripcion").trim());
                if (resultado.trim().length() > 1) {
                    modelo1.addElement(resultado);
                }
            }
            cbo_unidadmedida_buscar_detalle.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Unidad de Medida", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_itempadre() {
        try {
            ResultSet r = sentencia.executeQuery("select id_cotizaciondetalle from TCotizacion_detalle where id_cotizacion = '" + id_cotizacion_global + "' and categ_padre = id_cotizaciondetalle");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("id_cotizaciondetalle").trim());
                if (resultado.trim().length() > 1) {
                    modelo1.addElement(resultado);
                }
            }
            cbo_itempadre.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Item Padre", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_itempadre_buscar(String categ_padre) {
        try {
            ResultSet r = sentencia.executeQuery("select id_cotizaciondetalle from TCotizacion_detalle where id_cotizacion = '" + id_cotizacion_global + "' and categ_padre = id_cotizaciondetalle");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement(categ_padre);
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("id_cotizaciondetalle").trim());
                if (!resultado.equals(categ_padre) && resultado.trim().length() > 1) {
                    modelo1.addElement(resultado);
                }
            }
            cbo_itempadre.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Item Padre", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_numero_cotizacion() {
        try {
            ResultSet r = sentencia.executeQuery("select numero from TCotizacion where numero is not null and id_empresa='" + id_empresa_index + "' order by numero desc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("numero").trim());
                if (resultado.trim().length() > 1) {
                    modelo1.addElement(resultado);
                }
            }
            cbo_nrocotizacion_buscar_detalle.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Unidad de Medida", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_estado_cotizacion() {
        DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
        modelo1.addElement("");
        modelo1.addElement("EDICION");
        modelo1.addElement("ENVIADO");
        modelo1.addElement("APROBADO");
        modelo1.addElement("RECHAZADO");
        cbo_estado_buscar_detalle.setModel(modelo1);
    }

    //Mostrar combos buscar
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
            cbo_moneda.setModel(modelo);
            band_cbo_moneda = 1;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Moneda", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_cliente_buscar(String razon_social) {
        try {
            ResultSet r = sentencia.executeQuery("select razon_social from tcliente order by razon_social asc");
            DefaultComboBoxModel modelo = new DefaultComboBoxModel();
            modelo.addElement(razon_social);
            String resultado;
            while (r.next()) {
                resultado = (r.getString("razon_social").trim());
                if (!resultado.equals(razon_social)) {
                    modelo.addElement(resultado);
                }
            }
            cbo_cliente.setModel(modelo);
            band_cbo_cliente = 1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Cliente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Metodos
    private void eliminar_detalle_cotizacion(int id_cotizacion) {
        System.out.println("Ejecutandose Eliminar_detalle_guia");
        System.out.println("==================================");

        int id_detalle_cotizacion;
        ResultSet r;

        try {
            r = sentencia.executeQuery("select id_cotizaciondetalle from TCotizacion_detalle where id_cotizacion = '" + id_cotizacion + "'");
            while (r.next()) {
                id_detalle_cotizacion = Integer.parseInt(r.getString("id_detalle_cotizacion"));
                if (clase_cotizacion_detalle.eliminar(id_detalle_cotizacion)) {
                    System.out.println("Eliminacion de Detalle de Cotización exitosa. Nro de detalle eliminado:" + id_detalle_cotizacion);
                } else {
                    JOptionPane.showMessageDialog(null, "Ocurrio al Eliminar el Detalle de COTIZACION.\n N°:" + id_detalle_cotizacion, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos de Detalle COTIZACION", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int cantidad_atencion() {
        int cantidad = 0;
        try {
            String consulta = "select count(atencion) as cantidad from TCotizacion";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                cantidad = Integer.parseInt(r.getString("cantidad"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al obtener la cantidad de Atencion", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        return cantidad;
    }

    private String obtener_no_afecta(int id_cotizaciondetalle) {
        String no_afecta_total = "";

        try {
            String consulta = "select no_afecta_total from TCotizacion_detalle where id_cotizaciondetalle = '" + id_cotizaciondetalle + "'";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                no_afecta_total = r.getString("no_afecta_total");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al obtener la no_afecta_total de el Detalle de Cotizacion", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        return no_afecta_total;
    }

    private boolean verificar_sies_categoriapadre(int id_cotizaciondetalle) {
        boolean valor;
        int categ_padre = 0;

        try {
            String consulta = "select categ_padre from TCotizacion_detalle where id_cotizaciondetalle = '" + id_cotizaciondetalle + "'";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                categ_padre = Integer.parseInt(r.getString("categ_padre").trim());
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió al consultar si es categoria Padre", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        if (id_cotizaciondetalle == categ_padre) {
            valor = true;
        } else {
            valor = false;
        }

        return valor;
    }

    private String obtener_codigobusqueda() {
        String codigo_busqueda = "";

        if (txt_buscar.getText().trim().length() >= 1) {
            codigo_busqueda = codigo_busqueda.concat("1");
        } else {
            codigo_busqueda = codigo_busqueda.concat("0");
        }

        if (cbo_buscar_cliente.getSelectedItem().toString().length() >= 1) {
            codigo_busqueda = codigo_busqueda.concat("1");
        } else {
            codigo_busqueda = codigo_busqueda.concat("0");
        }

        return codigo_busqueda;
    }

    private void ejecucion_de_buscador() {
        String bus_txt = txt_buscar.getText().trim();
        String bus_cliente = cbo_buscar_cliente.getSelectedItem().toString().trim();
        String consulta = "";

        //Documentacion (1 es cuando esta seleccionado)
        //TXT - CLIENTE
        //0         0
        //0         1
        //1         0
        //1         1

        int band = 0;
        String codigo_busqueda;
        codigo_busqueda = obtener_codigobusqueda();

        if (codigo_busqueda.equals("01")) {
            System.out.println("codigo_busqueda: " + codigo_busqueda);
            band = 1;
        }

        if (codigo_busqueda.equals("10")) {
            System.out.println("codigo_busqueda: " + codigo_busqueda);
            band = 2;
        }

        if (codigo_busqueda.equals("11")) {
            System.out.println("codigo_busqueda: " + codigo_busqueda);
            band = 3;
        }

        switch (band) {
            case 0:
                mostrar_tabla_cotizacion(consulta);
                break;

            case 1:

                consulta = "select \n"
                        + "co.edicion, \n"
                        + "co.aprobado, \n"
                        + "co.rechazado, \n"
                        + "co.id_cotizacion, \n"
                        + "co.numero, \n"
                        + "convert(varchar, co.fecha, 103) as fecha \n"
                        + "from TCotizacion co, TCliente cl\n"
                        + "where\n"
                        + "co.id_cliente = cl.id_cliente and\n"
                        + "cl.razon_social = '" + bus_cliente + "' and \n"
                        + "numero is not null and co.id_empresa='" + id_empresa_index + "' \n"
                        + "order by numero desc";

                System.out.println(consulta);
                mostrar_tabla_cotizacion(consulta);
                break;

            case 2:
                consulta = "select \n"
                        + "co.edicion, \n"
                        + "co.aprobado, \n"
                        + "co.rechazado, \n"
                        + "co.id_cotizacion, \n"
                        + "co.numero, \n"
                        + "convert(varchar, co.fecha, 103) as fecha \n"
                        + "from TCotizacion co, TCliente cl, TMoneda mo, TTipoCotizacion ti, TFormaPago fp\n"
                        + "where\n"
                        + "(co.proyecto like '%"+bus_txt+"%' or \n"
                        + "co.ubicacion like '%"+bus_txt+"%' or\n"
                        + "co.tiempo_duracion like '%"+bus_txt+"%' or\n"
                        + "co.atencion like '%"+bus_txt+"%' or\n"
                        + "co.numero like '%"+bus_txt+"%' or\n"
                        + "co.fecha like '%"+bus_txt+"%' or\n"
                        + "cl.ruc like '%"+bus_txt+"%' or\n"
                        + "mo.nombre like '%"+bus_txt+"%' or\n"
                        + "ti.descripcion like '%"+bus_txt+"%' or\n"
                        + "fp.descripcion like '%"+bus_txt+"%'\n"
                        + ")\n"
                        + "and \n"
                        + "co.id_moneda = mo.id_moneda and \n"
                        + "co.id_tipocotizacion = ti.id_tipocotizacion and\n"
                        + "co.id_formapago = fp.id_formapago and \n"
                        + "co.id_cliente = cl.id_cliente and\n"
                        + "numero is not null and co.id_empresa='" + id_empresa_index + "' \n"
                        + "order by numero desc";
                System.out.println(consulta);
                mostrar_tabla_cotizacion(consulta);
                break;

            case 3:
                consulta = "select \n"
                        + "co.edicion, \n"
                        + "co.aprobado, \n"
                        + "co.rechazado, \n"
                        + "co.id_cotizacion, \n"
                        + "co.numero, \n"
                        + "convert(varchar, co.fecha, 103) as fecha \n"
                        + "from TCotizacion co, TCliente cl, TMoneda mo, TTipoCotizacion ti, TFormaPago fp\n"
                        + "where\n"
                        + "(co.proyecto like '%"+bus_txt+"%' or \n"
                        + "co.ubicacion like '%"+bus_txt+"%' or\n"
                        + "co.tiempo_duracion like '%"+bus_txt+"%' or\n"
                        + "co.atencion like '%"+bus_txt+"%' or\n"
                        + "co.numero like '%"+bus_txt+"%' or\n"
                        + "co.fecha like '%"+bus_txt+"%' or\n"
                        + "cl.ruc like '%"+bus_txt+"%' or\n"
                        + "mo.nombre like '%"+bus_txt+"%' or\n"
                        + "ti.descripcion like '%"+bus_txt+"%' or\n"
                        + "fp.descripcion like '%"+bus_txt+"%'\n"
                        + ")\n"
                        + "and \n"
                        + "co.id_moneda = mo.id_moneda and \n"
                        + "co.id_tipocotizacion = ti.id_tipocotizacion and\n"
                        + "co.id_formapago = fp.id_formapago and \n"
                        + "co.id_cliente = cl.id_cliente and\n"
                        + "cl.razon_social = '" + bus_cliente + "' and \n"
                        + "numero is not null and co.id_empresa='" + id_empresa_index + "' \n"
                        + "order by numero desc";
                System.out.println(consulta);
                mostrar_tabla_cotizacion(consulta);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        radio_sexo = new javax.swing.ButtonGroup();
        mantenimiento_tabla_detalle_cotizacion = new javax.swing.JPopupMenu();
        Modificar = new javax.swing.JMenuItem();
        Eliminar = new javax.swing.JMenuItem();
        mantenimiento_tabla_cotizacion_cuentabanco = new javax.swing.JPopupMenu();
        Eliminar_cuenta = new javax.swing.JMenuItem();
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
        dialog_crear_moneda = new javax.swing.JDialog();
        jPanel35 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        btn_cancelar_crear_moneda = new javax.swing.JButton();
        btn_guardar_moneda = new javax.swing.JButton();
        jPanel38 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txt_crear_moneda_nombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_crear_moneda_simbolo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_crear_moneda_check_moneda_local = new javax.swing.JCheckBox();
        dialog_crear_cliente = new javax.swing.JDialog();
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
        txt_razon_social_cliente_crear = new javax.swing.JTextField();
        txt_ruc_cliente_crear = new javax.swing.JTextField();
        txt_direccion_cliente_crear = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txt_telefono_cliente_crear = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txt_celular_cliente_crear = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        txt_correo_cliente_crear = new javax.swing.JTextField();
        dialog_ver_crear_cuentabanco = new javax.swing.JDialog();
        jPanel51 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jPanel52 = new javax.swing.JPanel();
        jPanel53 = new javax.swing.JPanel();
        btn_empresatransporte_cancelar_busqueda = new javax.swing.JButton();
        jPanel54 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tabla_cuenta_banco = new javax.swing.JTable();
        btn_nuevo_numerocuentabanco = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        txt_crear_numerodecuenta = new javax.swing.JTextField();
        btn_crear_nuerocuantabanco = new javax.swing.JButton();
        btn_cancelar_crear_nuerocuantabanco = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        btn_importar = new javax.swing.JButton();
        dialog_buscar_cliente = new javax.swing.JDialog();
        jPanel39 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        txt_buscar_cliente = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jPanel40 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        btn_cliente_cancelar_busqueda = new javax.swing.JButton();
        btn_cliente_seleccionar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel42 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabla_cliente_buscar = new javax.swing.JTable();
        dialog_buscar_cuenta_banco = new javax.swing.JDialog();
        jPanel47 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jPanel48 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        btn_cliente_cancelar_busqueda2 = new javax.swing.JButton();
        btn_cliente_seleccionar2 = new javax.swing.JButton();
        jPanel50 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tabla_cuentabanco_seleccionar = new javax.swing.JTable();
        dialog_buscar_detalle_cotizacion = new javax.swing.JDialog();
        jPanel43 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jLabel3 = new javax.swing.JLabel();
        txt_buscar_cotizacion = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        btn_cliente_cancelar_busqueda1 = new javax.swing.JButton();
        btn_cliente_seleccionar1 = new javax.swing.JButton();
        jPanel46 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabla_cliente_buscar1 = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        cbo_nrocotizacion_buscar_detalle = new javax.swing.JComboBox();
        cbo_categoria_buscar_detalle = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        cbo_moneda_buscar_detalle = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();
        cbo_unidadmedida_buscar_detalle = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        cbo_cliente_buscar_detalle = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        cbo_estado_buscar_detalle = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        dialog_aprobacion = new javax.swing.JDialog();
        jPanel28 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        btn_cerrar_aprobracion = new javax.swing.JButton();
        btn_aceptar_aprobacion = new javax.swing.JButton();
        btn_modificar_aprobacion = new javax.swing.JButton();
        jPanel31 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        txt_aprobado_por = new javax.swing.JTextField();
        txt_area_responsable = new javax.swing.JTextField();
        txt_fecha_aprobacion = new org.jdesktop.swingx.JXDatePicker();
        dialogo_rechazar = new javax.swing.JDialog();
        jPanel32 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        btn_cerrar_rechazar = new javax.swing.JButton();
        btn_aceptar_rechazar = new javax.swing.JButton();
        btn_modificar_rechazar = new javax.swing.JButton();
        jPanel55 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        txt_rechazado_por = new javax.swing.JTextField();
        txt_area_responsable_rechazar = new javax.swing.JTextField();
        txt_fecha_rechazar = new org.jdesktop.swingx.JXDatePicker();
        jLabel56 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        txt_motivo_rechazo = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btn_nuevo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btn_modificar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btn_aprobar = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        btn_rechazar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        barra_buscar = new javax.swing.JToolBar();
        label_buscar = new javax.swing.JLabel();
        txt_buscar = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        cbo_estado = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        cbo_buscar_cliente = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        panel_tabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_general = new javax.swing.JTable();
        Panel_detalle = new javax.swing.JPanel();
        norte = new javax.swing.JPanel();
        lbl_titulo = new javax.swing.JLabel();
        txt_numero_cotizacion = new javax.swing.JLabel();
        lbl_numero = new javax.swing.JLabel();
        txt_serie = new javax.swing.JLabel();
        lbl_serie = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        centro = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cbo_cliente = new javax.swing.JComboBox();
        txt_ruc_cliente = new javax.swing.JTextField();
        btn_nuevo_cliente = new javax.swing.JButton();
        btn_buscar_cliente = new javax.swing.JButton();
        cbo_atencion = new javax.swing.JComboBox();
        jPanel10 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        txt_fecha = new org.jdesktop.swingx.JXDatePicker();
        jLabel32 = new javax.swing.JLabel();
        cbo_moneda = new javax.swing.JComboBox();
        jLabel33 = new javax.swing.JLabel();
        btn_nuevo_moneda = new javax.swing.JButton();
        cbo_tipo_cotizacion = new javax.swing.JComboBox();
        jPanel13 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txt_proyecto = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txt_duracion = new javax.swing.JTextField();
        txt_ubicacion = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        txt_costoneto = new javax.swing.JTextField();
        lbl_estado1 = new javax.swing.JLabel();
        txt_ganancia_monto = new javax.swing.JTextField();
        txt_utilidad_monto = new javax.swing.JTextField();
        txt_costo_de_venta = new javax.swing.JTextField();
        txt_descuento_monto = new javax.swing.JTextField();
        txt_sub_total = new javax.swing.JTextField();
        txt_igv_monto = new javax.swing.JTextField();
        txt_costo_total = new javax.swing.JTextField();
        txt_ganancia_porcentaje = new javax.swing.JTextField();
        txt_utilidad_porcentaje = new javax.swing.JTextField();
        txt_descuento_porcentaje = new javax.swing.JTextField();
        lbl_estado2 = new javax.swing.JLabel();
        lbl_estado3 = new javax.swing.JLabel();
        lbl_estado4 = new javax.swing.JLabel();
        lbl_estado5 = new javax.swing.JLabel();
        lbl_estado6 = new javax.swing.JLabel();
        lbl_estado7 = new javax.swing.JLabel();
        cbo_igv = new javax.swing.JComboBox();
        cbo_formapago = new javax.swing.JComboBox();
        lbl_estado8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lbl_estado9 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        panel_nuevo_detalle = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_cantidad = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_detalle = new javax.swing.JTextArea();
        btn_guardar_detalle = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        txt_precio = new javax.swing.JTextField();
        btn_cancelar_guardar_detalle = new javax.swing.JButton();
        btn_buscar_detalle = new javax.swing.JButton();
        cbo_unidadmedida = new javax.swing.JComboBox();
        txt_unidad_codigo = new javax.swing.JTextField();
        btn_buscar_materiales = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        cbo_itempadre = new javax.swing.JComboBox();
        check_no_afecta_total = new javax.swing.JCheckBox();
        jPanel17 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabla_detalle = new javax.swing.JTable();
        btn_nuevo_detalle = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btn_cancelar = new javax.swing.JButton();
        btn_guardar = new javax.swing.JButton();
        btn_creacion = new javax.swing.JButton();
        btn_vista_previa = new javax.swing.JButton();
        lbl_estado = new javax.swing.JLabel();
        txt_estado = new javax.swing.JLabel();

        Modificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/actualizar_24_24.png"))); // NOI18N
        Modificar.setText("Modificar");
        Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModificarActionPerformed(evt);
            }
        });
        mantenimiento_tabla_detalle_cotizacion.add(Modificar);

        Eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar_24_24.png"))); // NOI18N
        Eliminar.setText("Eliminar");
        Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarActionPerformed(evt);
            }
        });
        mantenimiento_tabla_detalle_cotizacion.add(Eliminar);

        Eliminar_cuenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar_24_24.png"))); // NOI18N
        Eliminar_cuenta.setText("Eliminar");
        Eliminar_cuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Eliminar_cuentaActionPerformed(evt);
            }
        });
        mantenimiento_tabla_cotizacion_cuentabanco.add(Eliminar_cuenta);

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

        dialog_crear_moneda.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog_crear_moneda.setResizable(false);

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
            .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_crear_moneda.getContentPane().add(jPanel35, java.awt.BorderLayout.PAGE_START);

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));
        jPanel36.setLayout(new java.awt.BorderLayout());

        jPanel37.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_cancelar_crear_moneda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cancelar_crear_moneda.setText("Cancelar");
        btn_cancelar_crear_moneda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_crear_monedaActionPerformed(evt);
            }
        });

        btn_guardar_moneda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_guardar_moneda.setText("Guardar");
        btn_guardar_moneda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardar_monedaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                .addGap(0, 206, Short.MAX_VALUE)
                .addComponent(btn_guardar_moneda)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar_crear_moneda))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cancelar_crear_moneda)
                    .addComponent(btn_guardar_moneda))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel36.add(jPanel37, java.awt.BorderLayout.PAGE_END);

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 153));
        jLabel4.setText("Nombre:");

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

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 153));
        jLabel6.setText("Moneda Local:");

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_crear_moneda_nombre)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_crear_moneda_check_moneda_local)
                            .addComponent(txt_crear_moneda_simbolo, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 225, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_crear_moneda_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_crear_moneda_simbolo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_crear_moneda_check_moneda_local))
                .addContainerGap())
        );

        jPanel36.add(jPanel38, java.awt.BorderLayout.CENTER);

        dialog_crear_moneda.getContentPane().add(jPanel36, java.awt.BorderLayout.CENTER);

        dialog_crear_cliente.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel24.setBackground(new java.awt.Color(0, 110, 204));
        jPanel24.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cliente_32_32.png"))); // NOI18N
        jLabel39.setText("Crear Cliente");

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

        dialog_crear_cliente.getContentPane().add(jPanel24, java.awt.BorderLayout.PAGE_START);

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

        txt_razon_social_cliente_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_razon_social_cliente_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_razon_social_cliente_crearKeyTyped(evt);
            }
        });

        txt_ruc_cliente_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_ruc_cliente_crear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_ruc_cliente_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_ruc_cliente_crearKeyTyped(evt);
            }
        });

        txt_direccion_cliente_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_direccion_cliente_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_direccion_cliente_crearKeyTyped(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(0, 51, 153));
        jLabel43.setText("Teléfono:");

        txt_telefono_cliente_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_telefono_cliente_crear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_telefono_cliente_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telefono_cliente_crearKeyTyped(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(0, 51, 153));
        jLabel44.setText("Celular:");

        txt_celular_cliente_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_celular_cliente_crear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_celular_cliente_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_celular_cliente_crearKeyTyped(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(0, 51, 153));
        jLabel45.setText("Correo:");

        txt_correo_cliente_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_correo_cliente_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_correo_cliente_crearKeyTyped(evt);
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
                    .addComponent(txt_direccion_cliente_crear)
                    .addComponent(txt_razon_social_cliente_crear)
                    .addComponent(txt_correo_cliente_crear, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_celular_cliente_crear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                            .addComponent(txt_ruc_cliente_crear, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_telefono_cliente_crear, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(txt_razon_social_cliente_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(txt_ruc_cliente_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(txt_direccion_cliente_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(txt_telefono_cliente_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(txt_celular_cliente_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(txt_correo_cliente_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel27, java.awt.BorderLayout.CENTER);

        dialog_crear_cliente.getContentPane().add(jPanel25, java.awt.BorderLayout.CENTER);

        dialog_ver_crear_cuentabanco.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel51.setBackground(new java.awt.Color(0, 110, 204));
        jPanel51.setPreferredSize(new java.awt.Dimension(400, 40));
        jPanel51.setLayout(new java.awt.BorderLayout());

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar2_32_32.png"))); // NOI18N
        jLabel46.setText("Lista de Numeros de Cuentas");
        jLabel46.setPreferredSize(new java.awt.Dimension(144, 35));
        jPanel51.add(jLabel46, java.awt.BorderLayout.NORTH);

        dialog_ver_crear_cuentabanco.getContentPane().add(jPanel51, java.awt.BorderLayout.PAGE_START);

        jPanel52.setBackground(new java.awt.Color(255, 255, 255));
        jPanel52.setLayout(new java.awt.BorderLayout());

        jPanel53.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_empresatransporte_cancelar_busqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cerrar_ventana_32_32.png"))); // NOI18N
        btn_empresatransporte_cancelar_busqueda.setText("Salir");
        btn_empresatransporte_cancelar_busqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_empresatransporte_cancelar_busquedaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel53Layout = new javax.swing.GroupLayout(jPanel53);
        jPanel53.setLayout(jPanel53Layout);
        jPanel53Layout.setHorizontalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel53Layout.createSequentialGroup()
                .addContainerGap(491, Short.MAX_VALUE)
                .addComponent(btn_empresatransporte_cancelar_busqueda))
        );
        jPanel53Layout.setVerticalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel53Layout.createSequentialGroup()
                .addComponent(btn_empresatransporte_cancelar_busqueda)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel52.add(jPanel53, java.awt.BorderLayout.PAGE_END);

        jPanel54.setBackground(new java.awt.Color(255, 255, 255));

        tabla_cuenta_banco.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabla_cuenta_banco.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_cuenta_bancoMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tabla_cuenta_banco);

        btn_nuevo_numerocuentabanco.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btn_nuevo_numerocuentabanco.setToolTipText("Crear un Numero de Cuenta");
        btn_nuevo_numerocuentabanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevo_numerocuentabancoActionPerformed(evt);
            }
        });

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        btn_crear_nuerocuantabanco.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/listo_10_10.png"))); // NOI18N
        btn_crear_nuerocuantabanco.setToolTipText("Guardar Numero de Cuenta");
        btn_crear_nuerocuantabanco.setFocusable(false);
        btn_crear_nuerocuantabanco.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_crear_nuerocuantabanco.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_crear_nuerocuantabanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_crear_nuerocuantabancoActionPerformed(evt);
            }
        });

        btn_cancelar_crear_nuerocuantabanco.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_10_10.png"))); // NOI18N
        btn_cancelar_crear_nuerocuantabanco.setToolTipText("Cancelar");
        btn_cancelar_crear_nuerocuantabanco.setFocusable(false);
        btn_cancelar_crear_nuerocuantabanco.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_cancelar_crear_nuerocuantabanco.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_cancelar_crear_nuerocuantabanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_crear_nuerocuantabancoActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 51, 153));
        jLabel14.setText("Descripción:");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_crear_numerodecuenta, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_crear_nuerocuantabanco, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar_crear_nuerocuantabanco, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txt_crear_numerodecuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel14))
            .addComponent(btn_crear_nuerocuantabanco)
            .addComponent(btn_cancelar_crear_nuerocuantabanco)
        );

        btn_importar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/importar_10_10.png"))); // NOI18N
        btn_importar.setToolTipText("Importar Numero de Cuenta existente");
        btn_importar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_importarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel54Layout = new javax.swing.GroupLayout(jPanel54);
        jPanel54.setLayout(jPanel54Layout);
        jPanel54Layout.setHorizontalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel54Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_nuevo_numerocuentabanco, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_importar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel54Layout.setVerticalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel54Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel54Layout.createSequentialGroup()
                        .addComponent(btn_nuevo_numerocuentabanco)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_importar, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 163, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel52.add(jPanel54, java.awt.BorderLayout.CENTER);

        dialog_ver_crear_cuentabanco.getContentPane().add(jPanel52, java.awt.BorderLayout.CENTER);

        dialog_buscar_cliente.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel39.setBackground(new java.awt.Color(255, 255, 255));
        jPanel39.setPreferredSize(new java.awt.Dimension(400, 60));
        jPanel39.setLayout(new java.awt.BorderLayout());

        jToolBar2.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar2.setFloatable(false);
        jToolBar2.setPreferredSize(new java.awt.Dimension(13, 25));

        jLabel2.setText("Buscar:");
        jToolBar2.add(jLabel2);

        txt_buscar_cliente.setColumns(50);
        txt_buscar_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_buscar_clienteKeyReleased(evt);
            }
        });
        jToolBar2.add(txt_buscar_cliente);

        jPanel39.add(jToolBar2, java.awt.BorderLayout.CENTER);

        jPanel18.setBackground(new java.awt.Color(0, 110, 204));
        jPanel18.setPreferredSize(new java.awt.Dimension(418, 35));

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar2_32_32.png"))); // NOI18N
        jLabel36.setText("Buscar Cliente");
        jLabel36.setPreferredSize(new java.awt.Dimension(144, 35));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel39.add(jPanel18, java.awt.BorderLayout.NORTH);

        dialog_buscar_cliente.getContentPane().add(jPanel39, java.awt.BorderLayout.PAGE_START);

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

        dialog_buscar_cliente.getContentPane().add(jPanel40, java.awt.BorderLayout.CENTER);

        dialog_buscar_cuenta_banco.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel47.setBackground(new java.awt.Color(0, 110, 204));
        jPanel47.setPreferredSize(new java.awt.Dimension(400, 35));
        jPanel47.setLayout(new java.awt.BorderLayout());

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/listo_32_32.png"))); // NOI18N
        jLabel38.setText("Agregar Nro de Cuenta");
        jLabel38.setPreferredSize(new java.awt.Dimension(144, 35));
        jPanel47.add(jLabel38, java.awt.BorderLayout.NORTH);

        dialog_buscar_cuenta_banco.getContentPane().add(jPanel47, java.awt.BorderLayout.PAGE_START);

        jPanel48.setBackground(new java.awt.Color(255, 255, 255));
        jPanel48.setLayout(new java.awt.BorderLayout());

        jPanel49.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_cliente_cancelar_busqueda2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cliente_cancelar_busqueda2.setText("Cancelar");
        btn_cliente_cancelar_busqueda2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cliente_cancelar_busqueda2ActionPerformed(evt);
            }
        });

        btn_cliente_seleccionar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_cliente_seleccionar2.setText("Agregar");
        btn_cliente_seleccionar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cliente_seleccionar2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel49Layout.createSequentialGroup()
                .addContainerGap(194, Short.MAX_VALUE)
                .addComponent(btn_cliente_seleccionar2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cliente_cancelar_busqueda2))
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel49Layout.createSequentialGroup()
                .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cliente_cancelar_busqueda2)
                    .addComponent(btn_cliente_seleccionar2))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel48.add(jPanel49, java.awt.BorderLayout.PAGE_END);

        jPanel50.setBackground(new java.awt.Color(255, 255, 255));

        tabla_cuentabanco_seleccionar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabla_cuentabanco_seleccionar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabla_cuentabanco_seleccionarKeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(tabla_cuentabanco_seleccionar);

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
        );

        jPanel48.add(jPanel50, java.awt.BorderLayout.CENTER);

        dialog_buscar_cuenta_banco.getContentPane().add(jPanel48, java.awt.BorderLayout.CENTER);

        dialog_buscar_detalle_cotizacion.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel43.setBackground(new java.awt.Color(255, 255, 255));
        jPanel43.setPreferredSize(new java.awt.Dimension(400, 60));
        jPanel43.setLayout(new java.awt.BorderLayout());

        jToolBar3.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar3.setFloatable(false);
        jToolBar3.setPreferredSize(new java.awt.Dimension(13, 25));

        jLabel3.setText("Buscar:");
        jToolBar3.add(jLabel3);

        txt_buscar_cotizacion.setColumns(50);
        txt_buscar_cotizacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_buscar_cotizacionKeyReleased(evt);
            }
        });
        jToolBar3.add(txt_buscar_cotizacion);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_10_10.png"))); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton3);

        jPanel43.add(jToolBar3, java.awt.BorderLayout.CENTER);

        jPanel20.setBackground(new java.awt.Color(0, 110, 204));
        jPanel20.setPreferredSize(new java.awt.Dimension(900, 35));

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar2_32_32.png"))); // NOI18N
        jLabel37.setText("Buscar Detalle de Cotización");
        jLabel37.setPreferredSize(new java.awt.Dimension(144, 35));

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel43.add(jPanel20, java.awt.BorderLayout.NORTH);

        dialog_buscar_detalle_cotizacion.getContentPane().add(jPanel43, java.awt.BorderLayout.PAGE_START);

        jPanel44.setBackground(new java.awt.Color(255, 255, 255));
        jPanel44.setLayout(new java.awt.BorderLayout());

        jPanel45.setBackground(new java.awt.Color(255, 255, 255));
        jPanel45.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_cliente_cancelar_busqueda1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cliente_cancelar_busqueda1.setText("Cancelar");
        btn_cliente_cancelar_busqueda1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cliente_cancelar_busqueda1ActionPerformed(evt);
            }
        });

        btn_cliente_seleccionar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_cliente_seleccionar1.setText("Seleccionar");
        btn_cliente_seleccionar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cliente_seleccionar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel45Layout.createSequentialGroup()
                .addContainerGap(660, Short.MAX_VALUE)
                .addComponent(btn_cliente_seleccionar1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cliente_cancelar_busqueda1))
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cliente_cancelar_busqueda1)
                    .addComponent(btn_cliente_seleccionar1))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel44.add(jPanel45, java.awt.BorderLayout.PAGE_END);

        jPanel46.setBackground(new java.awt.Color(255, 255, 255));

        tabla_cliente_buscar1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabla_cliente_buscar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabla_cliente_buscar1KeyPressed(evt);
            }
        });
        jScrollPane5.setViewportView(tabla_cliente_buscar1);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 51, 153));
        jLabel15.setText("N° de Cotización:");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 51, 153));
        jLabel22.setText("Categoría:");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 51, 153));
        jLabel23.setText("Moneda:");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 51, 153));
        jLabel24.setText("Unidad de Medida:");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 51, 153));
        jLabel25.setText("Cliente:");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 51, 153));
        jLabel26.setText("Estado:");

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbo_nrocotizacion_buscar_detalle, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbo_categoria_buscar_detalle, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbo_moneda_buscar_detalle, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbo_estado_buscar_detalle, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbo_unidadmedida_buscar_detalle, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbo_cliente_buscar_detalle, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel46Layout.createSequentialGroup()
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo_nrocotizacion_buscar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_categoria_buscar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_moneda_buscar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_unidadmedida_buscar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_cliente_buscar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_estado_buscar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE))
        );

        jPanel44.add(jPanel46, java.awt.BorderLayout.CENTER);

        dialog_buscar_detalle_cotizacion.getContentPane().add(jPanel44, java.awt.BorderLayout.CENTER);

        dialog_aprobacion.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel28.setBackground(new java.awt.Color(0, 110, 204));
        jPanel28.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/aprobar_24_24.png"))); // NOI18N
        jLabel47.setText("Aprobación");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_aprobacion.getContentPane().add(jPanel28, java.awt.BorderLayout.PAGE_START);

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setLayout(new java.awt.BorderLayout());

        jPanel30.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_cerrar_aprobracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cerrar_aprobracion.setText("Cancelar");
        btn_cerrar_aprobracion.setToolTipText("Cerrar");
        btn_cerrar_aprobracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cerrar_aprobracionActionPerformed(evt);
            }
        });

        btn_aceptar_aprobacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_aceptar_aprobacion.setText("Guardar");
        btn_aceptar_aprobacion.setToolTipText("Guardar los datos Ingresados");
        btn_aceptar_aprobacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_aceptar_aprobacionActionPerformed(evt);
            }
        });

        btn_modificar_aprobacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/actualizar_24_24.png"))); // NOI18N
        btn_modificar_aprobacion.setText("Modificar");
        btn_modificar_aprobacion.setToolTipText("Modificar los Datos de Aprobación");
        btn_modificar_aprobacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modificar_aprobacionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addComponent(btn_modificar_aprobacion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addComponent(btn_aceptar_aprobacion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cerrar_aprobracion))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cerrar_aprobracion)
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_aceptar_aprobacion)
                        .addComponent(btn_modificar_aprobacion, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel29.add(jPanel30, java.awt.BorderLayout.PAGE_END);

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 51, 153));
        jLabel48.setText("Fecha:");

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 51, 153));
        jLabel50.setText("Aprobado por:");

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(0, 51, 153));
        jLabel51.setText("Área Responsable:");

        txt_aprobado_por.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_aprobado_por.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_aprobado_por.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_aprobado_porKeyTyped(evt);
            }
        });

        txt_area_responsable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_area_responsable.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_area_responsable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_area_responsableKeyTyped(evt);
            }
        });

        txt_fecha_aprobacion.setToolTipText("Seleccione una Fecha");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_aprobado_por)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(txt_fecha_aprobacion, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 116, Short.MAX_VALUE))
                    .addComponent(txt_area_responsable))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(txt_fecha_aprobacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(txt_aprobado_por, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(txt_area_responsable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(162, Short.MAX_VALUE))
        );

        jPanel29.add(jPanel31, java.awt.BorderLayout.CENTER);

        dialog_aprobacion.getContentPane().add(jPanel29, java.awt.BorderLayout.CENTER);

        dialogo_rechazar.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel32.setBackground(new java.awt.Color(0, 110, 204));
        jPanel32.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/rechazar_24_24.png"))); // NOI18N
        jLabel52.setText("Rechazar");

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialogo_rechazar.getContentPane().add(jPanel32, java.awt.BorderLayout.PAGE_START);

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));
        jPanel33.setLayout(new java.awt.BorderLayout());

        jPanel34.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_cerrar_rechazar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cerrar_rechazar.setText("Cancelar");
        btn_cerrar_rechazar.setToolTipText("Cerrar");
        btn_cerrar_rechazar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cerrar_rechazarActionPerformed(evt);
            }
        });

        btn_aceptar_rechazar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_aceptar_rechazar.setText("Guardar");
        btn_aceptar_rechazar.setToolTipText("Guardar los datos Ingresados");
        btn_aceptar_rechazar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_aceptar_rechazarActionPerformed(evt);
            }
        });

        btn_modificar_rechazar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/actualizar_24_24.png"))); // NOI18N
        btn_modificar_rechazar.setText("Modificar");
        btn_modificar_rechazar.setToolTipText("Modificar los Datos de Aprobación");
        btn_modificar_rechazar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modificar_rechazarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                .addComponent(btn_modificar_rechazar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addComponent(btn_aceptar_rechazar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cerrar_rechazar))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cerrar_rechazar)
                    .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_aceptar_rechazar)
                        .addComponent(btn_modificar_rechazar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel33.add(jPanel34, java.awt.BorderLayout.PAGE_END);

        jPanel55.setBackground(new java.awt.Color(255, 255, 255));

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(0, 51, 153));
        jLabel53.setText("Fecha:");

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(0, 51, 153));
        jLabel54.setText("Rechazado por:");

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(0, 51, 153));
        jLabel55.setText("Área Responsable:");

        txt_rechazado_por.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_rechazado_por.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_rechazado_por.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_rechazado_porKeyTyped(evt);
            }
        });

        txt_area_responsable_rechazar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_area_responsable_rechazar.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_area_responsable_rechazar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_area_responsable_rechazarKeyTyped(evt);
            }
        });

        txt_fecha_rechazar.setToolTipText("Seleccione una Fecha");

        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(0, 51, 153));
        jLabel56.setText("Motivo:");

        txt_motivo_rechazo.setColumns(20);
        txt_motivo_rechazo.setRows(5);
        jScrollPane8.setViewportView(txt_motivo_rechazo);

        javax.swing.GroupLayout jPanel55Layout = new javax.swing.GroupLayout(jPanel55);
        jPanel55.setLayout(jPanel55Layout);
        jPanel55Layout.setHorizontalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel55Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_rechazado_por)
                    .addComponent(txt_area_responsable_rechazar)
                    .addGroup(jPanel55Layout.createSequentialGroup()
                        .addComponent(txt_fecha_rechazar, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel55Layout.setVerticalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel55Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(txt_fecha_rechazar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(txt_rechazado_por, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(txt_area_responsable_rechazar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel55Layout.createSequentialGroup()
                        .addComponent(jLabel56)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel33.add(jPanel55, java.awt.BorderLayout.CENTER);

        dialogo_rechazar.getContentPane().add(jPanel33, java.awt.BorderLayout.CENTER);

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(0, 110, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cotizacion.png"))); // NOI18N
        jLabel1.setText("Cotización");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1138, Short.MAX_VALUE)
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

        btn_aprobar.setBackground(new java.awt.Color(255, 255, 255));
        btn_aprobar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/aprobar_24_24.png"))); // NOI18N
        btn_aprobar.setText("Aprobar");
        btn_aprobar.setFocusable(false);
        btn_aprobar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_aprobar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_aprobar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_aprobarActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_aprobar);
        jToolBar1.add(jSeparator6);

        btn_rechazar.setBackground(new java.awt.Color(255, 255, 255));
        btn_rechazar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/rechazar_24_24.png"))); // NOI18N
        btn_rechazar.setText("Rechazar");
        btn_rechazar.setFocusable(false);
        btn_rechazar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_rechazar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_rechazar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_rechazarActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_rechazar);

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

        jLabel28.setText("    Estado:");
        barra_buscar.add(jLabel28);

        cbo_estado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TODOS", "EDICION", "APROBADO", "RECHAZADO" }));
        cbo_estado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_estadoItemStateChanged(evt);
            }
        });
        barra_buscar.add(cbo_estado);

        jLabel29.setText("       Cliente:");
        barra_buscar.add(jLabel29);

        cbo_buscar_cliente.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_buscar_clienteItemStateChanged(evt);
            }
        });
        barra_buscar.add(cbo_buscar_cliente);

        jPanel2.add(barra_buscar, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.BorderLayout());

        panel_tabla.setBackground(new java.awt.Color(255, 255, 255));
        panel_tabla.setPreferredSize(new java.awt.Dimension(280, 461));

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
        );
        panel_tablaLayout.setVerticalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
        );

        jPanel5.add(panel_tabla, java.awt.BorderLayout.LINE_START);

        Panel_detalle.setLayout(new java.awt.BorderLayout());

        norte.setBackground(new java.awt.Color(255, 255, 255));
        norte.setPreferredSize(new java.awt.Dimension(458, 40));

        lbl_titulo.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_titulo.setForeground(new java.awt.Color(0, 110, 204));
        lbl_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_titulo.setText("Información de la Cotización");

        txt_numero_cotizacion.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_numero_cotizacion.setForeground(new java.awt.Color(0, 110, 204));
        txt_numero_cotizacion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_numero_cotizacion.setText("000001");

        lbl_numero.setBackground(new java.awt.Color(153, 102, 0));
        lbl_numero.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_numero.setForeground(new java.awt.Color(0, 110, 204));
        lbl_numero.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_numero.setText("Número:");

        txt_serie.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_serie.setForeground(new java.awt.Color(0, 110, 204));
        txt_serie.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_serie.setText("0001");

        lbl_serie.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_serie.setForeground(new java.awt.Color(0, 110, 204));
        lbl_serie.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_serie.setText("Serie:");

        javax.swing.GroupLayout norteLayout = new javax.swing.GroupLayout(norte);
        norte.setLayout(norteLayout);
        norteLayout.setHorizontalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(norteLayout.createSequentialGroup()
                .addComponent(lbl_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(lbl_serie)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbl_numero)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_numero_cotizacion, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        norteLayout.setVerticalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(norteLayout.createSequentialGroup()
                .addGroup(norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, norteLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lbl_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txt_numero_cotizacion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_numero, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_serie, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_serie, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Panel_detalle.add(norte, java.awt.BorderLayout.PAGE_START);

        centro.setBackground(new java.awt.Color(153, 255, 204));
        centro.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(771, 120));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 51, 153));
        jLabel11.setText("Razon Social:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 153));
        jLabel12.setText("R.U.C.:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 153));
        jLabel13.setText("Atención:");

        cbo_cliente.setEditable(true);
        cbo_cliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_cliente.setToolTipText("Ingrese o Seleccione la Razón Social del Cliente.");
        cbo_cliente.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_clienteItemStateChanged(evt);
            }
        });

        txt_ruc_cliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_ruc_cliente.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_ruc_cliente.setToolTipText("R.U.C. del Cliente");

        btn_nuevo_cliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btn_nuevo_cliente.setToolTipText("Crear Cliente");
        btn_nuevo_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevo_clienteActionPerformed(evt);
            }
        });

        btn_buscar_cliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_10_10.png"))); // NOI18N
        btn_buscar_cliente.setToolTipText("Buscar Cliente");
        btn_buscar_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar_clienteActionPerformed(evt);
            }
        });

        cbo_atencion.setEditable(true);
        cbo_atencion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_atencion.setToolTipText("Ingrese o Seleccione la Razón Social del Cliente.");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(txt_ruc_cliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_buscar_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_nuevo_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbo_atencion, 0, 250, Short.MAX_VALUE)
                    .addComponent(cbo_cliente, 0, 1, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cbo_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(txt_ruc_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btn_buscar_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_nuevo_cliente)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo_atencion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addContainerGap())
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 51, 153));
        jLabel30.setText("Fecha:");

        txt_fecha.setToolTipText("Seleccione una Fecha");

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 51, 153));
        jLabel32.setText("Moneda:");

        cbo_moneda.setToolTipText("Seleccionar Moneda");

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 51, 153));
        jLabel33.setText("Categ.:");

        btn_nuevo_moneda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btn_nuevo_moneda.setToolTipText("Crear Moneda");
        btn_nuevo_moneda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevo_monedaActionPerformed(evt);
            }
        });

        cbo_tipo_cotizacion.setEditable(true);
        cbo_tipo_cotizacion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_tipo_cotizacion.setToolTipText("Ingrese o Seleccione la Forma de Pago.");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbo_tipo_cotizacion, 0, 1, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(cbo_moneda, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_nuevo_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_fecha, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txt_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_nuevo_moneda)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel32)
                        .addComponent(cbo_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(cbo_tipo_cotizacion))
                .addGap(0, 45, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 51, 153));
        jLabel20.setText("Proyecto:");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 51, 153));
        jLabel21.setText("Ubicación:");

        txt_proyecto.setEditable(false);
        txt_proyecto.setBackground(new java.awt.Color(255, 255, 255));
        txt_proyecto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_proyecto.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_proyecto.setToolTipText("Ingrese el nombre del Proyecto");
        txt_proyecto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_proyectoKeyTyped(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 51, 153));
        jLabel31.setText("Duración:");

        txt_duracion.setEditable(false);
        txt_duracion.setBackground(new java.awt.Color(255, 255, 255));
        txt_duracion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_duracion.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_duracion.setToolTipText("Ingrese el tiempo que Durará el Proyecto.");
        txt_duracion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_duracionKeyTyped(evt);
            }
        });

        txt_ubicacion.setEditable(false);
        txt_ubicacion.setBackground(new java.awt.Color(255, 255, 255));
        txt_ubicacion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_ubicacion.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_ubicacion.setToolTipText("Ingrese el lugar en donde se llevará acabo el Proyecto");
        txt_ubicacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_ubicacionKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_proyecto, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .addComponent(txt_ubicacion)
                    .addComponent(txt_duracion)))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txt_proyecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txt_ubicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txt_duracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        centro.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setPreferredSize(new java.awt.Dimension(725, 120));

        txt_costoneto.setEditable(false);
        txt_costoneto.setBackground(new java.awt.Color(255, 255, 255));
        txt_costoneto.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_costoneto.setForeground(new java.awt.Color(204, 0, 0));
        txt_costoneto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_costoneto.setToolTipText("Costo Neto: Es igual a la Suma de todos los ITEM de la Cotización.");

        lbl_estado1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_estado1.setForeground(new java.awt.Color(0, 51, 153));
        lbl_estado1.setText("Costo Neto:");

        txt_ganancia_monto.setEditable(false);
        txt_ganancia_monto.setBackground(new java.awt.Color(255, 255, 255));
        txt_ganancia_monto.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_ganancia_monto.setForeground(new java.awt.Color(204, 0, 0));
        txt_ganancia_monto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_ganancia_monto.setToolTipText("Ganancia: Es el Valor del Porcentaje de Ganancia que se le aplica al COSTO NETO.");

        txt_utilidad_monto.setEditable(false);
        txt_utilidad_monto.setBackground(new java.awt.Color(255, 255, 255));
        txt_utilidad_monto.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_utilidad_monto.setForeground(new java.awt.Color(204, 0, 0));
        txt_utilidad_monto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_utilidad_monto.setToolTipText("Utilidad: Es el Valor del Porcentaje de Utiliad que se le aplica al COSTO NETO.");

        txt_costo_de_venta.setEditable(false);
        txt_costo_de_venta.setBackground(new java.awt.Color(255, 255, 255));
        txt_costo_de_venta.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_costo_de_venta.setForeground(new java.awt.Color(204, 0, 0));
        txt_costo_de_venta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_costo_de_venta.setToolTipText("Resultado: Es el Valor de la suma del COSTO NETO + GANANCIA + UTILIDAD");

        txt_descuento_monto.setEditable(false);
        txt_descuento_monto.setBackground(new java.awt.Color(255, 255, 255));
        txt_descuento_monto.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_descuento_monto.setForeground(new java.awt.Color(204, 0, 0));
        txt_descuento_monto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_descuento_monto.setToolTipText("Descuento: Es el Valor del Porcentaje de Descuento que se le aplica al RESULTADO.");

        txt_sub_total.setEditable(false);
        txt_sub_total.setBackground(new java.awt.Color(255, 255, 255));
        txt_sub_total.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_sub_total.setForeground(new java.awt.Color(204, 0, 0));
        txt_sub_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_sub_total.setToolTipText("Sub-Total: Es el resultado del Descuento aplicado al resultado.");

        txt_igv_monto.setEditable(false);
        txt_igv_monto.setBackground(new java.awt.Color(255, 255, 255));
        txt_igv_monto.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_igv_monto.setForeground(new java.awt.Color(204, 0, 0));
        txt_igv_monto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_igv_monto.setToolTipText("IGV: Es el Valor del Porcentaje de I.G.V. que se le aplica al SUB-TOTAL.");

        txt_costo_total.setEditable(false);
        txt_costo_total.setBackground(new java.awt.Color(255, 255, 255));
        txt_costo_total.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_costo_total.setForeground(new java.awt.Color(204, 0, 0));
        txt_costo_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_costo_total.setToolTipText("Costo Total: Es el Valor de Venta  de la Cotizacion.");

        txt_ganancia_porcentaje.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_ganancia_porcentaje.setForeground(new java.awt.Color(204, 0, 0));
        txt_ganancia_porcentaje.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_ganancia_porcentaje.setText("0");
        txt_ganancia_porcentaje.setToolTipText("Ingrese el Porcentaje de Gastos Generales que se le aplicará al Costo Neto.");
        txt_ganancia_porcentaje.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_ganancia_porcentajeKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_ganancia_porcentajeKeyTyped(evt);
            }
        });

        txt_utilidad_porcentaje.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_utilidad_porcentaje.setForeground(new java.awt.Color(204, 0, 0));
        txt_utilidad_porcentaje.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_utilidad_porcentaje.setText("0");
        txt_utilidad_porcentaje.setToolTipText("Ingrese el Porcentaje de Utilidad que se le aplicará al Costo Neto.");
        txt_utilidad_porcentaje.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_utilidad_porcentajeKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_utilidad_porcentajeKeyTyped(evt);
            }
        });

        txt_descuento_porcentaje.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_descuento_porcentaje.setForeground(new java.awt.Color(204, 0, 0));
        txt_descuento_porcentaje.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_descuento_porcentaje.setText("0");
        txt_descuento_porcentaje.setToolTipText("Ingrese el Porcentaje de Descuento a aplicar.");
        txt_descuento_porcentaje.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_descuento_porcentajeKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_descuento_porcentajeKeyTyped(evt);
            }
        });

        lbl_estado2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_estado2.setForeground(new java.awt.Color(0, 51, 153));
        lbl_estado2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_estado2.setText("%G.Gen.");
        lbl_estado2.setPreferredSize(new java.awt.Dimension(50, 15));

        lbl_estado3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_estado3.setForeground(new java.awt.Color(0, 51, 153));
        lbl_estado3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_estado3.setText("% Util.");

        lbl_estado4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_estado4.setForeground(new java.awt.Color(0, 51, 153));
        lbl_estado4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_estado4.setText("% Desc.");

        lbl_estado5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_estado5.setForeground(new java.awt.Color(0, 51, 153));
        lbl_estado5.setText("SubTotal:");

        lbl_estado6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_estado6.setForeground(new java.awt.Color(0, 51, 153));
        lbl_estado6.setText("%IGV");

        lbl_estado7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_estado7.setForeground(new java.awt.Color(0, 51, 153));
        lbl_estado7.setText("Costo Total:");

        cbo_igv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_igv.setToolTipText("Seleccione el I.G.V. que se aplicará a la Cotización.");
        cbo_igv.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_igvItemStateChanged(evt);
            }
        });

        cbo_formapago.setEditable(true);
        cbo_formapago.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_formapago.setToolTipText("Ingrese o Seleccione la Forma de Pago.");

        lbl_estado8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_estado8.setForeground(new java.awt.Color(0, 51, 153));
        lbl_estado8.setText("Forma de Pago:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/lista_16_16.png"))); // NOI18N
        jButton1.setText("Ver lista");
        jButton1.setToolTipText("Edite la Lista de Cuentas en que se podrán hacer el pago o Deposito.");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lbl_estado9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_estado9.setForeground(new java.awt.Color(0, 51, 153));
        lbl_estado9.setText("Lista de cuentas y bancos:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lbl_estado9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl_estado8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbo_formapago, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_estado1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_costoneto, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_ganancia_monto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lbl_estado2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_ganancia_porcentaje, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_utilidad_monto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lbl_estado3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_utilidad_porcentaje, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_costo_de_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(txt_descuento_porcentaje, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_descuento_monto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_estado4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(txt_sub_total, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lbl_estado5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(33, 33, 33)))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_igv_monto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lbl_estado6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbo_igv, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_costo_total, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lbl_estado7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_estado9)
                    .addComponent(jButton1)
                    .addComponent(lbl_estado8)
                    .addComponent(cbo_formapago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_estado1)
                    .addComponent(txt_ganancia_porcentaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_utilidad_porcentaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_descuento_porcentaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_estado2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_estado3)
                    .addComponent(lbl_estado4)
                    .addComponent(lbl_estado5)
                    .addComponent(lbl_estado6)
                    .addComponent(lbl_estado7)
                    .addComponent(cbo_igv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_costoneto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_ganancia_monto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_utilidad_monto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_costo_de_venta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_descuento_monto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_sub_total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_igv_monto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_costo_total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        centro.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new java.awt.BorderLayout());

        panel_nuevo_detalle.setBackground(new java.awt.Color(204, 255, 255));
        panel_nuevo_detalle.setPreferredSize(new java.awt.Dimension(840, 110));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 51, 153));
        jLabel16.setText("Unidad:");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 51, 153));
        jLabel17.setText("Cantidad:");

        txt_cantidad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_cantidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cantidadKeyTyped(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 51, 153));
        jLabel18.setText("Descripción:");

        txt_detalle.setColumns(20);
        txt_detalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_detalle.setLineWrap(true);
        txt_detalle.setRows(3);
        txt_detalle.setWrapStyleWord(true);
        txt_detalle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_detalleKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(txt_detalle);

        btn_guardar_detalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/listo_10_10.png"))); // NOI18N
        btn_guardar_detalle.setToolTipText("Guardar Detalle");
        btn_guardar_detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardar_detalleActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 51, 153));
        jLabel19.setText("Precio:");
        jLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txt_precio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_precio.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
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

        btn_buscar_detalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_10_10.png"))); // NOI18N
        btn_buscar_detalle.setText("Buscar Detalles");
        btn_buscar_detalle.setToolTipText("Buscar Detalles");
        btn_buscar_detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar_detalleActionPerformed(evt);
            }
        });

        cbo_unidadmedida.setEditable(true);
        cbo_unidadmedida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_unidadmedida.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_unidadmedidaItemStateChanged(evt);
            }
        });

        txt_unidad_codigo.setEditable(false);
        txt_unidad_codigo.setBackground(new java.awt.Color(255, 255, 255));

        btn_buscar_materiales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_10_10.png"))); // NOI18N
        btn_buscar_materiales.setText("Buscar Materiales");
        btn_buscar_materiales.setToolTipText("Buscar Detalles");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 51, 153));
        jLabel27.setText("Cat. Padre:");
        jLabel27.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        check_no_afecta_total.setBackground(new java.awt.Color(204, 255, 255));
        check_no_afecta_total.setForeground(new java.awt.Color(0, 51, 153));
        check_no_afecta_total.setText("El precio no afecta al Costo Neto");
        check_no_afecta_total.setToolTipText("Seleccione esta opción cuando el precio no afectará al costo neto de la cotización.");

        javax.swing.GroupLayout panel_nuevo_detalleLayout = new javax.swing.GroupLayout(panel_nuevo_detalle);
        panel_nuevo_detalle.setLayout(panel_nuevo_detalleLayout);
        panel_nuevo_detalleLayout.setHorizontalGroup(
            panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addComponent(btn_buscar_detalle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_buscar_materiales)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(check_no_afecta_total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbo_itempadre, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbo_unidadmedida, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_unidad_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                                .addComponent(txt_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_precio)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_guardar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cancelar_guardar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        panel_nuevo_detalleLayout.setVerticalGroup(
            panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                                .addComponent(btn_guardar_detalle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_cancelar_guardar_detalle))))
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbo_unidadmedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_unidad_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbo_itempadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_buscar_detalle)
                            .addComponent(btn_buscar_materiales)
                            .addComponent(check_no_afecta_total))))
                .addContainerGap(38, Short.MAX_VALUE))
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
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_nuevo_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
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

        lbl_estado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_estado.setForeground(new java.awt.Color(0, 51, 153));
        lbl_estado.setText("Estado de Guía:");

        txt_estado.setBackground(new java.awt.Color(204, 0, 102));
        txt_estado.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        txt_estado.setForeground(new java.awt.Color(204, 0, 0));
        txt_estado.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_estado.setText("PAGADO");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_estado, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(lbl_estado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 231, Short.MAX_VALUE)
                .addComponent(btn_vista_previa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_creacion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_guardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_cancelar)
                        .addComponent(btn_guardar)
                        .addComponent(btn_creacion)
                        .addComponent(btn_vista_previa))
                    .addComponent(lbl_estado))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txt_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Panel_detalle.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jPanel5.add(Panel_detalle, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_creacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_creacionActionPerformed
        dialog_fecha_creacion.setSize(429, 270);
        dialog_fecha_creacion.setLocationRelativeTo(cotizacion);
        dialog_fecha_creacion.setModal(true);
        dialog_fecha_creacion.setVisible(true);
    }//GEN-LAST:event_btn_creacionActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        dialog_fecha_creacion.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btn_guardar_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardar_detalleActionPerformed
        float cantidad;
        float precio_unitario;
        float precio_total;
        String id_unidad;

        //Capturamos valores ingresado
        int id_cotizacion = id_cotizacion_global;
        String descripcion = txt_detalle.getText().trim();
        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;
        String categ_padre = cbo_itempadre.getSelectedItem().toString().trim();
        String no_afecta_total = "";

        float item = 0;

        if (txt_unidad_codigo.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione una unidad de medida.", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {

            id_unidad = obtener_idunidad(txt_unidad_codigo.getText().trim());

            if (txt_cantidad.getText().trim().equals("")) {
                cantidad = 0;
            } else {
                cantidad = Float.parseFloat(txt_cantidad.getText().trim());
            }

            if (txt_precio.getText().trim().equals("")) {
                precio_unitario = 0;
            } else {
                precio_unitario = Float.parseFloat(txt_precio.getText().trim());
            }

            if (check_no_afecta_total.isSelected() == true) {
                no_afecta_total = "1";
            } else {
                no_afecta_total = "0";
            }

            precio_total = cantidad * precio_unitario;

            System.out.println("\nDatos recibidos");
            System.out.println("=================");
            System.out.println("id_cotizacion  :" + id_cotizacion);
            System.out.println("categ_padre    :" + categ_padre);
            System.out.println("item           :" + item);
            System.out.println("descripcion    :" + descripcion);
            System.out.println("cantidad       :" + cantidad);
            System.out.println("id_unidad      :" + id_unidad);
            System.out.println("precio_unitario:" + precio_unitario);
            System.out.println("precio_total   :" + precio_total);
            System.out.println("id_empresa     :" + id_empresa);
            System.out.println("id_usuario     :" + id_usuario);

            if (crear0_modificar1_cotizacion_detalle == 0) {
                System.out.println("Llamamos a la funcion crear_cotizacion_detalle");
                crear_cotizacion_detalle(id_cotizacion, categ_padre, item, descripcion, cantidad, id_unidad, precio_unitario, precio_total, id_empresa, id_usuario, no_afecta_total);
            } else {
                System.out.println("Llamamos a la funcion modificar_cotizacion_detalle");
                int id_detalle_cotizacion = id_cotizacion_detalle_global;
                modificar_cotizacion_detalle(id_detalle_cotizacion, id_cotizacion, categ_padre, item, descripcion, cantidad, id_unidad, precio_unitario, precio_total, id_empresa, id_usuario, no_afecta_total);
            }
        }



    }//GEN-LAST:event_btn_guardar_detalleActionPerformed

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        System.out.println("\ncrear Cotización");
        System.out.println("==================");

        System.out.println("inicializar ID's globales");
        crear0_modificar1_cotizacion = 0;
        numero_inicial_global = "";
        inicializar_id_global();


        if (clase_cotizacion.crear(id_empresa_index, id_usuario_index)) {
            System.out.println("\nLa Cotización se logró registrar exitosamente!");

            band_index = 1;
            crear0_modificar1_cotizacion = 1;
            band_mantenimiento_cotizacion_detalle = 0;
            band_mantenimiento_cotizacion_cuentabanco = 0;
            System.out.println("Se procede a igualar el crear0_modificar1_cotizacion: " + crear0_modificar1_cotizacion);

            System.out.println("cambiando el titulo");
            lbl_titulo.setText("Nueva Cotización");

            System.out.println("activar las cajas de texto para el registro");
            activar_caja_texto("editable");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto();

            System.out.println("capturar fecha y poner en caja de texto");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d = new Date();
            String s = sdf.format(d);
            txt_fecha.setDate(d);

            System.out.println("ocultar botones: vista previa, imprimir, creacion");
            btn_vista_previa.setVisible(false);
            btn_creacion.setVisible(false);

            System.out.println("mostrar botones: guardar, cancelar");
            btn_guardar.setVisible(true);
            btn_cancelar.setVisible(true);
            btn_guardar.setText("Guardar");
            btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png")));
            btn_guardar.setEnabled(false);

            System.out.println("desactivar barra de herramientas");
            activar_barra_herramientas("desactivar");

            System.out.println("mostrar botones");
            mostrar_botones("mostrar");

            System.out.println("Ejecutando la consulta para saber cual es el ultimo id_cotizacion generado");
            cotizacion_id_ultimo();

            System.out.println("Mostrar tabla cotizacion_detalle_vacia");
            mostrar_tabla_cotizacion_detalle_vacia();

//            DefaultComboBoxModel modelocombo = new DefaultComboBoxModel();
            System.out.println("Mostrar Combo Moneda");
//            if (band_cbo_moneda == 0) {
//                cbo_moneda.setModel(modelocombo);
            mostrar_combo_moneda();
//            }

            System.out.println("Mostrar Combo Tipo de Cotizacion");
//            if (band_cbo_tipo_cotizacion == 0) {
//                cbo_tipo_cotizacion.setModel(modelocombo);
            mostrar_combo_tipo_cotizacion();
//            }

            System.out.println("Mostrar Combo Cliente");
            if (band_cbo_cliente == 0) {
//                cbo_cliente.setModel(modelocombo);
                mostrar_combo_cliente();
            }

            System.out.println("Mostrar Combo Forma de Pago");
//            if (band_cbo_formapago == 0) {
//                cbo_formapago.setModel(modelocombo);
            mostrar_combo_formapago();
//            }

            System.out.println("Mostrar Combo I.G.V.");
//            if (band_cbo_igv == 0) {
//                cbo_igv.setModel(modelocombo);
            mostrar_combo_igv();
//            }

            System.out.println("Mostrar Combo Atencion");
            int cantidad_atencion = cantidad_atencion();

            if (cantidad_atencion > 0) {
//                if (band_cbo_atencion == 0) {
//                    cbo_atencion.setModel(modelocombo);
                mostrar_combo_atencion();
//                }
            }

            System.out.println("Mostrar Combo Unidad Medida");
//            if (band_cbo_unidadmedida == 0) {
//                cbo_unidadmedida.setModel(modelocombo);
            mostrar_combo_unidadmedida();
//            }

            System.out.println("Mostrar Combo Item Padre");
//            if (band_cbo_itempadre == 0) {
//                cbo_itempadre.setModel(modelocombo);
            mostrar_combo_itempadre();
//            }

            lbl_estado.setVisible(false);
            txt_estado.setVisible(false);

            System.out.println("Mostrar Combo Estado de Cotizacion");
            mostrar_combo_estado_cotizacion();

            System.out.println("Mostrar Combo Numero de Cotizacion");
            mostrar_combo_numero_cotizacion();

            //Agregar las cuentas de los bancos a Pagar
            System.out.println("Agregar las cuentas de Bancos de la empresa y enlazarlo con la cotizacion");
            agregar_cuentas_bancos_cotizacion();

            System.out.println("Ejecutando consulta de id y serie de documento COTIZACION");
            String serie = serie_documento_cotizacion();
            System.out.println("La serie del Documento COTIZACION es: " + serie);

            System.out.println("Mostrar la serie en el lbl serie");
            txt_serie.setText(serie);

            System.out.println("Mostrar Numero de Cotizacion generada");
            mostrar_numero_cotizacion();

            float costo_neto = 0;
            calcular_totales(costo_neto);

            System.out.println("mostrar el panel de detalle de datos");
            Panel_detalle.setVisible(true);
            panel_nuevo_detalle.setVisible(false);
            lbl_estado.setVisible(false);
            txt_estado.setVisible(false);

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear la Guia. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModificarActionPerformed
        int fil;
        fil = tabla_detalle.getSelectedRow();
        if (fil == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar el registro a Modificar");
        } else {

            int id_cotizaciondetalle;
            String categ_padre;
            String descripcion;
            String cantidad;
            String codigo_unidad;
            String descripcion_unidad;
            String precio;

            m = (DefaultTableModel) tabla_detalle.getModel();
            id_cotizaciondetalle = Integer.parseInt((String) m.getValueAt(fil, 0));
            descripcion = (String) m.getValueAt(fil, 2);
            codigo_unidad = (String) m.getValueAt(fil, 3);
            cantidad = (String) m.getValueAt(fil, 4);
            precio = (String) m.getValueAt(fil, 5);

            id_cotizacion_detalle_global = id_cotizaciondetalle;
            txt_cantidad.setText(cantidad);
            txt_unidad_codigo.setText(codigo_unidad);
            txt_precio.setText(precio);
            txt_detalle.setText(descripcion);

            String no_afecta_total = "";
            no_afecta_total = obtener_no_afecta(id_cotizaciondetalle);

            if (no_afecta_total.equals("1")) {
                check_no_afecta_total.setSelected(true);
            }

            if (no_afecta_total.equals("0")) {
                check_no_afecta_total.setSelected(false);
            }

            //Obtener la descripcion de la unida de medida
            descripcion_unidad = obtener_descripcion_material(codigo_unidad);

            //Llenar el combo de unida de medida
            mostrar_combo_unidadmedida_modificar(descripcion_unidad);

            //Obtener la categoria padre
            categ_padre = obtener_categoria_padre_modificar(id_cotizaciondetalle);

            //Mostrar combo categoria padre;
            mostrar_combo_itempadre_buscar(categ_padre);

            crear0_modificar1_cotizacion_detalle = 1;

            panel_nuevo_detalle.setVisible(true);
            btn_guardar_detalle.setVisible(true);
            btn_nuevo_detalle.setVisible(false);
        }
    }//GEN-LAST:event_ModificarActionPerformed

    private void tabla_detalleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_detalleMouseClicked
        if (band_mantenimiento_cotizacion_detalle == 0) {
            if (evt.getButton() == 3) {
                mantenimiento_tabla_detalle_cotizacion.show(tabla_detalle, evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_tabla_detalleMouseClicked

    private void btn_nuevo_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_clienteActionPerformed
        dialog_crear_cliente.setSize(429, 350);
        dialog_crear_cliente.setLocationRelativeTo(cotizacion);
        dialog_crear_cliente.setModal(true);
        dialog_crear_cliente.setVisible(true);
    }//GEN-LAST:event_btn_nuevo_clienteActionPerformed

    private void btn_nuevo_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_detalleActionPerformed
        //limpiar_caja_texto_crear_detalle_cotizacion();
        mostrar_combo_itempadre();
        mostrar_combo_unidadmedida();
        panel_nuevo_detalle.setVisible(true);
        btn_guardar_detalle.setVisible(true);
        btn_nuevo_detalle.setVisible(false);
        btn_buscar_detalle.setVisible(true);
        check_no_afecta_total.setSelected(false);
    }//GEN-LAST:event_btn_nuevo_detalleActionPerformed

    private void btn_cancelar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_clienteActionPerformed
        System.out.println("limpiar cajas de texto");
        limpiar_caja_texto_crear_cliente();

        System.out.println("actualizamos tabla cliente");
        mostrar_tabla_cliente();

        dialog_crear_cliente.dispose();
    }//GEN-LAST:event_btn_cancelar_clienteActionPerformed

    private void txt_razon_social_cliente_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_razon_social_cliente_crearKeyTyped
        JTextField caja = txt_razon_social_cliente_crear;
        int limite = 200;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_razon_social_cliente_crearKeyTyped

    private void txt_ruc_cliente_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ruc_cliente_crearKeyTyped
        JTextField caja = txt_ruc_cliente_crear;
        ingresar_solo_numeros(caja, evt);
        int limite = 11;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_ruc_cliente_crearKeyTyped

    private void txt_direccion_cliente_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_direccion_cliente_crearKeyTyped
        JTextField caja = txt_direccion_cliente_crear;
        int limite = 200;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_direccion_cliente_crearKeyTyped

    private void txt_telefono_cliente_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefono_cliente_crearKeyTyped
        JTextField caja = txt_telefono_cliente_crear;
        ingresar_solo_numeros(caja, evt);
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_telefono_cliente_crearKeyTyped

    private void txt_celular_cliente_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_celular_cliente_crearKeyTyped
        JTextField caja = txt_celular_cliente_crear;
        ingresar_solo_numeros(caja, evt);
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_celular_cliente_crearKeyTyped

    private void txt_correo_cliente_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_correo_cliente_crearKeyTyped
        JTextField caja = txt_correo_cliente_crear;
        int limite = 50;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_correo_cliente_crearKeyTyped

    private void btn_buscar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_clienteActionPerformed
        mostrar_tabla_cliente();
        dialog_buscar_cliente.setSize(700, 400);
        dialog_buscar_cliente.setLocationRelativeTo(cotizacion);
        dialog_buscar_cliente.setModal(true);
        dialog_buscar_cliente.setVisible(true);
    }//GEN-LAST:event_btn_buscar_clienteActionPerformed

    private void btn_cliente_cancelar_busquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_cancelar_busquedaActionPerformed
        txt_buscar_cliente.setText("");
        dialog_buscar_cliente.dispose();
    }//GEN-LAST:event_btn_cliente_cancelar_busquedaActionPerformed

    private void btn_cliente_seleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_seleccionarActionPerformed
        int fila;
        String razon_social;
        String ruc;

        fila = tabla_cliente_buscar.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un Cliente");
        } else {
            m = (DefaultTableModel) tabla_cliente_buscar.getModel();
            razon_social = (String) m.getValueAt(fila, 1);
            ruc = (String) m.getValueAt(fila, 3);

            mostrar_combo_cliente_buscar(razon_social);
            txt_ruc_cliente.setText(ruc);

            txt_buscar_cliente.setText("");
            dialog_buscar_cliente.dispose();
        }
    }//GEN-LAST:event_btn_cliente_seleccionarActionPerformed

    private void tabla_cliente_buscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_cliente_buscarKeyPressed
        int fila = tabla_cliente_buscar.getSelectedRow();
        int id_cliente;
        String razon_social;
        String ruc;

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            m = (DefaultTableModel) tabla_cliente_buscar.getModel();
            id_cliente = Integer.parseInt((String) m.getValueAt(fila, 0));
            razon_social = (String) m.getValueAt(fila, 1);
            ruc = (String) m.getValueAt(fila, 3);

            mostrar_combo_cliente_buscar(razon_social);
            txt_ruc_cliente.setText(ruc);

            txt_buscar_cliente.setText("");
            dialog_buscar_cliente.dispose();
        }
    }//GEN-LAST:event_tabla_cliente_buscarKeyPressed

    private void btn_empresatransporte_cancelar_busquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_empresatransporte_cancelar_busquedaActionPerformed
        txt_crear_numerodecuenta.setText("");
        dialog_ver_crear_cuentabanco.dispose();
    }//GEN-LAST:event_btn_empresatransporte_cancelar_busquedaActionPerformed

    private void btn_crea_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_crea_clienteActionPerformed
        System.out.println("\npresionó boton Guardar_Cliente");
        System.out.println("================================");

        System.out.println("capturando datos ingresados");

        String razon_social = txt_razon_social_cliente_crear.getText().trim();
        String ruc = txt_ruc_cliente_crear.getText().trim();
        String direccion = txt_direccion_cliente_crear.getText().trim();
        String telefono = txt_telefono_cliente_crear.getText().trim();
        String celular = txt_celular_cliente_crear.getText().trim();
        String correo = txt_correo_cliente_crear.getText().trim();
        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;

        if (txt_razon_social_cliente_crear.getText().length() == 0 || txt_ruc_cliente_crear.getText().length() == 0 || txt_direccion_cliente_crear.getText().length() == 0) {
            System.out.println("\nFalta ingresar los campos Razon Social, R.U.C. y Dirección");
            JOptionPane.showMessageDialog(null, "Los Razon Social, R.U.C. y Dirección son necesarios \n Por favor ingrese estos campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            if (txt_ruc_cliente_crear.getText().length() != 11) {
                System.out.println("\n el R.U.C. tiene un tamaño diferente a 11");
                JOptionPane.showMessageDialog(null, "El R.U.C. debe tener 11 digitos.\n Por favor un R.U.C. válido.", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {

                if (correo.length() > 0) {
                    if (!ValidarCorreo(correo)) {
                        System.out.println("\n el correo ingresado no es correcto");
                        JOptionPane.showMessageDialog(null, "El correo ingresado no es correcto.\n Por favor ingrese un Correo válido.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (clase_cliente.razon_social_existente(razon_social, id_empresa) > 0) {
                            System.out.println("La Razon social ya se encuentra registrada.");
                            JOptionPane.showMessageDialog(null, "La Razon Social ya se encuentra Registra.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (clase_cliente.ruc_existente(ruc, id_empresa) > 0) {
                                System.out.println("El R.U.C. ya se encuentra registrado.");
                                JOptionPane.showMessageDialog(null, "El R.U.C. ya se encuentra Registrado.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                            } else {
                                System.out.println("llamamos a la funcion crear_cliente");
                                crear_cliente(razon_social, ruc, direccion, telefono, celular, correo, id_empresa, id_usuario);
                            }
                        }
                    }
                } else {
                    if (clase_cliente.razon_social_existente(razon_social, id_empresa) > 0) {
                        System.out.println("La Razon social ya se encuentra registrada.");
                        JOptionPane.showMessageDialog(null, "La Razon Social ya se encuentra Registra.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (clase_cliente.ruc_existente(ruc, id_empresa) > 0) {
                            System.out.println("El R.U.C. ya se encuentra registrado.");
                            JOptionPane.showMessageDialog(null, "El R.U.C. ya se encuentra Registrado.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            System.out.println("llamamos a la funcion crear_cliente");
                            crear_cliente(razon_social, ruc, direccion, telefono, celular, correo, id_empresa, id_usuario);
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_btn_crea_clienteActionPerformed

    private void btn_cancelar_guardar_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_guardar_detalleActionPerformed
        limpiar_caja_texto_crear_detalle_cotizacion();
        panel_nuevo_detalle.setVisible(false);
        btn_guardar_detalle.setVisible(false);
        btn_nuevo_detalle.setVisible(true);
    }//GEN-LAST:event_btn_cancelar_guardar_detalleActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        System.out.println("Ejecutandose CANCELAR GUIA");
        System.out.println("==========================");

        int id_cotizacion = id_cotizacion_global;
        int id_cotizaciondetalle;
        int id_cotizacion_cuentabanco;
        ResultSet r;
        int respuesta;

        if (band_modificar != 1) {
            //Se ejecuta si estamos ejecutando una creacion    
            respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea CANCELAR la CREACION de esta COTIZACION?", "Cancelar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {

                try {
                    r = sentencia.executeQuery("select id_cotizaciondetalle from TCotizacion_detalle where id_cotizacion = '" + id_cotizacion + "'");
                    while (r.next()) {
                        id_cotizaciondetalle = Integer.parseInt(r.getString("id_cotizaciondetalle"));
                        clase_cotizacion_detalle.eliminar(id_cotizaciondetalle);
                    }


                    r = sentencia.executeQuery("select id_cotizacion_cuentabanco from TCotizacion_cuentabanco where id_cotizacion = '" + id_cotizacion + "'");
                    while (r.next()) {
                        id_cotizacion_cuentabanco = Integer.parseInt(r.getString("id_cotizacion_cuentabanco"));
                        clase_cotizacion_cuentabanco.eliminar(id_cotizacion_cuentabanco);
                    }

                    if (clase_cotizacion.eliminar(id_cotizacion)) {

                        System.out.println("Se logró cancelar exitosamente!");
                        operaciones_postModificacion();

                        JOptionPane.showMessageDialog(null, "La Cotizacion se Cancelo exitosamente");

                    } else {
                        JOptionPane.showMessageDialog(null, "Ocurrio al CANCELAR la COTIZACION.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos de la COTIZACION", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            //Se ejecuta si estamos ejecutando una modificacion    
            respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea CANCELAR la MODIFICACIÓN de esta COTIZACION?", "Cancelar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                System.out.println("La cotizacion se logró cancelar exitosamente!");
                operaciones_postModificacion();
                //JOptionPane.showMessageDialog(null, "La Guia se Cancelo exitosamente");
            }
        }
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void txt_cantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cantidadKeyTyped
        JTextField caja = txt_cantidad;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_cantidadKeyTyped

    private void txt_precioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_precioKeyTyped
        JTextField caja = txt_precio;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_precioKeyTyped

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarActionPerformed
        System.out.println("\nEliminar Detalle Cotizacion");
        System.out.println("=============================");

        int fila;
        int id_cotizaciondetalle;
        int id_cotizaciondetalle_eliminar;
        int respuesta;
        ResultSet r;

        fila = tabla_detalle.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar el registro a borrar");
        } else {
            respuesta = JOptionPane.showConfirmDialog(null, "¿Desea eliminar?", "Eliminar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                m = (DefaultTableModel) tabla_detalle.getModel();
                id_cotizaciondetalle = Integer.parseInt((String) m.getValueAt(fila, 0));
                System.out.println("el id_cotizaciondetalle que se eliminara es: " + id_cotizaciondetalle);

                respuesta = 0;

                if (verificar_sies_categoriapadre(id_cotizaciondetalle)) {
                    respuesta = JOptionPane.showConfirmDialog(null, "El Item que desea ELIMINAR es un Item PADRE. Si elimina este Item, tambíen se eliminarán los Item hijos. ¿Desea seguir con la Eliminación de este Item?", "Eliminar", JOptionPane.YES_NO_OPTION);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            r = sentencia.executeQuery("select id_cotizaciondetalle from TCotizacion_detalle where categ_padre = '" + id_cotizaciondetalle + "'");
                            while (r.next()) {
                                id_cotizaciondetalle_eliminar = Integer.parseInt(r.getString("id_cotizaciondetalle"));
                                clase_cotizacion_detalle.eliminar(id_cotizaciondetalle_eliminar);
                            }

                            System.out.println("el detalle se logró eliminar exitosamente!");
                            mostrar_tabla_cotizacion_detalle(id_cotizacion_global);
                            JOptionPane.showMessageDialog(null, "Eliminación exitosa");

                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Ocurrio al Eliminar la Categoria Padre", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    if (clase_cotizacion_detalle.eliminar(id_cotizaciondetalle)) {
                        System.out.println("el detalle se logró eliminar exitosamente!");
                        mostrar_tabla_cotizacion_detalle(id_cotizacion_global);
                        JOptionPane.showMessageDialog(null, "Eliminación exitosa");
                    } else {
                        JOptionPane.showMessageDialog(null, "Ocurrio al eliminar el detalle.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }//GEN-LAST:event_EliminarActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        //inicializacion de bandera
        int band = 0;

        //Campos usados para capturar los id's de los combos
        String tabla;
        String campocondicion;
        String campocapturar;

        //Campos a guardar
        int id_cotizacion = id_cotizacion_global;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dia = txt_fecha.getDate();
        String fecha = sdf.format(dia);

        String numero = txt_numero_cotizacion.getText().trim();

        String atencion = "";
        if (cbo_atencion.getSelectedItem().toString().trim().length() < 1) {
            band = 1;
        } else {
            atencion = cbo_atencion.getSelectedItem().toString().trim();
        }

        String proyecto = "";
        if (txt_proyecto.getText().trim().length() < 1) {
            band = 2;
        } else {
            proyecto = txt_proyecto.getText().trim();
        }

        String ubicacion = "";
        if (txt_ubicacion.getText().length() < 1) {
            band = 3;
        } else {
            ubicacion = txt_ubicacion.getText().trim();
        }


        String tiempo_duracion = "";
        if (txt_duracion.getText().length() < 1) {
            band = 4;
        } else {
            tiempo_duracion = txt_duracion.getText().trim();
        }

        float costo_neto = Float.parseFloat(txt_costoneto.getText().trim());
        float gasto_gen_por = Float.parseFloat(txt_ganancia_porcentaje.getText().trim());
        float gasto_gen_monto = Float.parseFloat(txt_ganancia_monto.getText().trim());
        float utilidad_por = Float.parseFloat(txt_utilidad_porcentaje.getText().trim());
        float utilidad_monto = Float.parseFloat(txt_utilidad_monto.getText().trim());
        float subtotal = Float.parseFloat(txt_costo_de_venta.getText().trim());
        float descuento_por = Float.parseFloat(txt_descuento_porcentaje.getText().trim());
        float descuento_monto = Float.parseFloat(txt_descuento_monto.getText().trim());
        float subtotal_neto = Float.parseFloat(txt_sub_total.getText().trim());
        float igv_monto = Float.parseFloat(txt_igv_monto.getText().trim());
        float total = Float.parseFloat(txt_costo_total.getText().trim());
        String total_letras = "";
        int id_moneda = 0;

        String moneda = cbo_moneda.getSelectedItem().toString().trim();
        if (moneda.trim().length() < 1) {
            band = 5;
        } else {
            total_letras = clase_numero_letra.getStringOfNumber(total, moneda);

            campocapturar = "id_moneda";
            tabla = "TMoneda";
            campocondicion = "nombre";
            id_moneda = CapturarId(campocapturar, tabla, campocondicion, cbo_moneda.getSelectedItem().toString().trim());
            System.out.println("id_moneda capturado: " + id_moneda);
        }

        int id_cliente = 0;
        if (txt_ruc_cliente.getText().length() < 1) {
            band = 6;
        } else {
            campocapturar = "id_cliente";
            tabla = "tcliente";
            campocondicion = "ruc";
            id_cliente = CapturarId(campocapturar, tabla, campocondicion, txt_ruc_cliente.getText().trim());
            System.out.println("id_cliente capturado: " + id_cliente);
        }

        int id_igv = 0;
        campocapturar = "id_igv";
        tabla = "TIgv";
        campocondicion = "igv";
        id_igv = CapturarId_conEmpresa(campocapturar, tabla, campocondicion, cbo_igv.getSelectedItem().toString().trim());
        System.out.println("id_igv capturado: " + id_igv);

        int id_documento = id_documento_global;
        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;
        int id_tipocotizacion = 0;


        int cantidad_veces;
        if (cbo_tipo_cotizacion.getSelectedItem().toString().trim().length() < 1) {
            band = 7;
        } else {
            //Verifica si ya existe la descripcion seleccionada o ingresada
            campocapturar = "id_tipocotizacion";
            tabla = "TTipoCotizacion";
            campocondicion = "descripcion";
            String descripcion_categoria = cbo_tipo_cotizacion.getSelectedItem().toString().trim();
            cantidad_veces = cantidad_veces_categoria(campocapturar, tabla, campocondicion, descripcion_categoria);

            if (cantidad_veces > 0) {
                campocapturar = "id_tipocotizacion";
                tabla = "TTipoCotizacion";
                campocondicion = "descripcion";
                id_tipocotizacion = CapturarId(campocapturar, tabla, campocondicion, descripcion_categoria);
                System.out.println("id_tipocotizacion capturado: " + id_tipocotizacion);

            } else {
                System.out.println("llamamos a la funcion crear_TipoCotizacion");
                String estado = "0";
                crear_TipoCotizacion(descripcion_categoria, estado, id_empresa, id_usuario);

                campocapturar = "id_tipocotizacion";
                tabla = "TTipoCotizacion";
                campocondicion = "descripcion";
                id_tipocotizacion = CapturarId(campocapturar, tabla, campocondicion, descripcion_categoria);
                System.out.println("id_tipocotizacion capturado: " + id_tipocotizacion);

            }
        }

        int id_formapago = 0;
        cantidad_veces = 0;

        if (cbo_formapago.getSelectedItem().toString().trim().length() < 1) {
            band = 8;
        } else {
            //Verifica si ya existe la descripcion seleccionada o ingresada
            campocapturar = "id_formapago";
            tabla = "TFormaPago";
            campocondicion = "descripcion";
            String descripcion_formapago = cbo_formapago.getSelectedItem().toString().trim();
            cantidad_veces = cantidad_veces_categoria(campocapturar, tabla, campocondicion, descripcion_formapago);

            if (cantidad_veces > 0) {
                campocapturar = "id_formapago";
                tabla = "TFormaPago";
                campocondicion = "descripcion";
                id_formapago = CapturarId(campocapturar, tabla, campocondicion, descripcion_formapago);
                System.out.println("id_formapago capturado: " + id_formapago);

            } else {
                System.out.println("llamamos a la funcion crear_TipoCotizacion");
                String estado = "0";
                crear_FormaPago(descripcion_formapago, estado, id_empresa, id_usuario);

                campocapturar = "id_formapago";
                tabla = "TFormaPago";
                campocondicion = "descripcion";
                id_formapago = CapturarId(campocapturar, tabla, campocondicion, descripcion_formapago);
                System.out.println("id_formapago capturado: " + id_formapago);

            }
        }

        switch (band) {
            case 1:
                JOptionPane.showMessageDialog(null, "Falta ingresar el campo: ATENCIÓN.\n Por favor ingrese el campo en mención.", "ERROR: 1", JOptionPane.ERROR_MESSAGE);
                break;

            case 2:
                JOptionPane.showMessageDialog(null, "Falta ingresar el campo: PROYECTO.\n Por favor ingrese el campo en mención.", "ERROR: 2", JOptionPane.ERROR_MESSAGE);
                break;

            case 3:
                JOptionPane.showMessageDialog(null, "Falta seleccionar el campo: UBICACIÓN.\n Por favor seleccione el campo en mención.", "ERROR: 3", JOptionPane.ERROR_MESSAGE);
                break;

            case 4:
                JOptionPane.showMessageDialog(null, "Falta seleccionar el campo: DURACION.\n Por favor seleccione el campo en mención.", "ERROR: 4", JOptionPane.ERROR_MESSAGE);
                break;

            case 5:
                JOptionPane.showMessageDialog(null, "Falta seleccionar el campo: MONEDA.\n Por favor seleccione el campo en mención.", "ERROR: 5", JOptionPane.ERROR_MESSAGE);
                break;

            case 6:
                JOptionPane.showMessageDialog(null, "Falta seleccionar el campo: CLIENTE.\n Por favor seleccione el campo en mención.", "ERROR: 6", JOptionPane.ERROR_MESSAGE);
                break;

            case 7:
                JOptionPane.showMessageDialog(null, "Falta Seleccionar el campo: CATEGORIA.\n Por favor seleccione el campo en mención.", "ERROR: 7", JOptionPane.ERROR_MESSAGE);
                break;

            case 8:
                JOptionPane.showMessageDialog(null, "Falta Seleccionar el campo: FORMA DE PAGO.\n Por favor seleccione el campo en mención.", "ERROR: 7", JOptionPane.ERROR_MESSAGE);
                break;

            case 0:
                System.out.println("\nSe presionó el boton GUARDAR/MODIFICAR GUIA");
                System.out.println("=============================================");
                System.out.println("fecha               : " + fecha);
                System.out.println("numero              : " + numero);
                System.out.println("fecha               : " + fecha);
                System.out.println("atencion            : " + atencion);
                System.out.println("proyecto            : " + proyecto);
                System.out.println("ubicacion           : " + ubicacion);
                System.out.println("tiempo_duracion     : " + tiempo_duracion);
                System.out.println("costo_neto          : " + costo_neto);
                System.out.println("gasto_gen_por       : " + gasto_gen_por);
                System.out.println("gasto_gen_monto     : " + gasto_gen_monto);
                System.out.println("utilidad_por        : " + utilidad_por);
                System.out.println("utilidad_monto      : " + utilidad_monto);
                System.out.println("subtotal            : " + subtotal);
                System.out.println("descuento_por       : " + descuento_por);
                System.out.println("descuento_monto     : " + descuento_monto);
                System.out.println("subtotal_neto       : " + subtotal_neto);
                System.out.println("igv_monto           : " + igv_monto);
                System.out.println("total               : " + total);
                System.out.println("total_letras        : " + total_letras);
                System.out.println("id_cliente          : " + id_cliente);
                System.out.println("id_igv              : " + id_igv);
                System.out.println("id_moneda           : " + id_moneda);
                System.out.println("id_tipocotizacion   : " + id_tipocotizacion);
                System.out.println("id_documento        : " + id_documento);
                System.out.println("id_formapago        : " + id_formapago);
                System.out.println("id_empresa          : " + id_empresa);
                System.out.println("id_usuario          : " + id_usuario);

                System.out.println("Se llama a la funcion Modificar_Cotizacion");
                modificar_Cotizacion(id_cotizacion, fecha, numero, atencion, proyecto, ubicacion, tiempo_duracion, costo_neto, gasto_gen_por, gasto_gen_monto, utilidad_por, utilidad_monto, subtotal, descuento_por, descuento_monto, subtotal_neto, igv_monto, total, total_letras, id_cliente, id_igv, id_moneda, id_tipocotizacion, id_documento, id_formapago, id_empresa, id_usuario);

                break;
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void tabla_generalMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_generalMouseReleased
        if (band_index == 0) {
            int fila;
            int id_cotizacion;
            fila = tabla_general.getSelectedRow();
            m = (DefaultTableModel) tabla_general.getModel();
            id_cotizacion = Integer.parseInt((String) m.getValueAt(fila, 1));
            mostrar_datos_cotizacion(id_cotizacion);
        }
    }//GEN-LAST:event_tabla_generalMouseReleased

    private void tabla_generalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_generalKeyReleased
        if (band_index == 0) {
            int fila;
            int id_cotizacion;
            fila = tabla_general.getSelectedRow();
            m = (DefaultTableModel) tabla_general.getModel();
            id_cotizacion = Integer.parseInt((String) m.getValueAt(fila, 1));
            mostrar_datos_cotizacion(id_cotizacion);
        }
    }//GEN-LAST:event_tabla_generalKeyReleased

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
        int fil = tabla_general.getSelectedRow();
        if (fil == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una Cotizacion para Modificar");
            band_index = 1;
        } else {
            System.out.println("\nModifar Cotización");
            System.out.println("====================");

            System.out.println("inicializar la bandera crear en uno");
            band_index = 1;
            crear0_modificar1_cotizacion = 0;
            band_mantenimiento_cotizacion_detalle = 0;
            band_mantenimiento_cotizacion_cuentabanco = 0;
            band_modificar = 1; //Indicamos que es una modificacion y que se tiene que activar el boton cancelar y no eliminara la factura

            System.out.println("cambiando el titulo");
            lbl_titulo.setText("Modificar Cotización");

            System.out.println("activar las cajas de texto para el registro");
            activar_caja_texto("editable");

            System.out.println("ocultar botones: vista previa, imprimir, creacion");
            btn_vista_previa.setVisible(false);
            btn_creacion.setVisible(false);

            System.out.println("mostrar botones: guardar, cancelar y otros");
            btn_cancelar.setVisible(true);
            btn_guardar.setVisible(true);
            btn_guardar.setText("Modificar");
            btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_actualizar_32_32.png")));

            mostrar_botones("mostrar");
            lbl_estado.setVisible(false);
            txt_estado.setVisible(false);

            System.out.println("desactivar barra de herramientas");
            activar_barra_herramientas("desactivar");
        }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void txt_buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ejecucion_de_buscador();
        }
    }//GEN-LAST:event_txt_buscarKeyReleased

    private void btn_vista_previaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vista_previaActionPerformed
        try {
            String rutaInforme = "reportes\\Cotizacion.jasper";
            Map parametros = new HashMap();
            parametros.put("id_cotizacion", id_cotizacion_global);
            JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
            JasperViewer view = new JasperViewer(print, false);
            view.setTitle("COTIZACION  N° " + txt_serie.getText() + " - " + txt_numero_cotizacion.getText());
            view.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: " + e, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_vista_previaActionPerformed

    private void btn_nuevo_monedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_monedaActionPerformed
        dialog_crear_moneda.setSize(429, 250);
        dialog_crear_moneda.setLocationRelativeTo(cotizacion);
        dialog_crear_moneda.setModal(true);
        dialog_crear_moneda.setVisible(true);
    }//GEN-LAST:event_btn_nuevo_monedaActionPerformed

    private void txt_detalleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_detalleKeyTyped
        JTextArea caja = txt_detalle;
        int limite = 1000;
        tamaño_de_caja_jtextarea(caja, evt, limite);
    }//GEN-LAST:event_txt_detalleKeyTyped

    private void cbo_igvItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_igvItemStateChanged
        System.out.println("Se seleccionó un IGV, se procede a actualizar los datos");

        if (txt_costoneto.getText().trim().length() > 0) {
            float costo_neto = Float.parseFloat(txt_costoneto.getText());

            BigDecimal costo_neto_decimal = new BigDecimal(costo_neto);
            costo_neto_decimal = costo_neto_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            costo_neto = (float) costo_neto_decimal.doubleValue();

            calcular_totales(costo_neto);
        }
    }//GEN-LAST:event_cbo_igvItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jPanel11.setVisible(false);

        dialog_ver_crear_cuentabanco.setSize(580, 340);
        dialog_ver_crear_cuentabanco.setLocationRelativeTo(cotizacion);
        dialog_ver_crear_cuentabanco.setModal(true);
        dialog_ver_crear_cuentabanco.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btn_cancelar_crear_monedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_crear_monedaActionPerformed
        System.out.println("limpiar cajas de texto");
        limpiar_caja_texto_crear_moneda();

        dialog_crear_moneda.dispose();
    }//GEN-LAST:event_btn_cancelar_crear_monedaActionPerformed

    private void btn_guardar_monedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardar_monedaActionPerformed
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
    }//GEN-LAST:event_btn_guardar_monedaActionPerformed

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

    private void btn_crear_nuerocuantabancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_crear_nuerocuantabancoActionPerformed
        System.out.println("\npresionó boton Guardar");
        System.out.println("========================");

        System.out.println("capturando datos ingresados");

        String descripcion = txt_crear_numerodecuenta.getText().trim();
        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;

        if (txt_crear_numerodecuenta.getText().length() == 0) {
            System.out.println("\nFalta ingresar Descripcion");
            JOptionPane.showMessageDialog(null, "El campo Descripcion es necesarios\n Por favor ingrese estos campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            if (clase_cuentas_Bancos.descripcion_existente(descripcion) > 0) {
                JOptionPane.showMessageDialog(null, "Descripcion de la Cuenta existente.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("llamamos a la funcion crear");
                String estado = "0";
                crear_CuentaBanco(descripcion, estado, id_empresa, id_usuario);
            }
        }
    }//GEN-LAST:event_btn_crear_nuerocuantabancoActionPerformed

    private void btn_nuevo_numerocuentabancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_numerocuentabancoActionPerformed
        jPanel11.setVisible(true);
        btn_nuevo_numerocuentabanco.setVisible(false);
    }//GEN-LAST:event_btn_nuevo_numerocuentabancoActionPerformed

    private void btn_cancelar_crear_nuerocuantabancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_crear_nuerocuantabancoActionPerformed
        jPanel11.setVisible(false);
        txt_crear_numerodecuenta.setText("");
        btn_nuevo_numerocuentabanco.setVisible(true);
    }//GEN-LAST:event_btn_cancelar_crear_nuerocuantabancoActionPerformed

    private void txt_proyectoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_proyectoKeyTyped
        JTextField caja = txt_proyecto;
        int limite = 500;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_proyectoKeyTyped

    private void txt_ubicacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ubicacionKeyTyped
        JTextField caja = txt_ubicacion;
        int limite = 250;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_ubicacionKeyTyped

    private void txt_duracionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_duracionKeyTyped
        JTextField caja = txt_duracion;
        int limite = 30;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_duracionKeyTyped

    private void txt_ganancia_porcentajeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ganancia_porcentajeKeyTyped
        JTextField caja = txt_ganancia_porcentaje;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_ganancia_porcentajeKeyTyped

    private void txt_utilidad_porcentajeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_utilidad_porcentajeKeyTyped
        JTextField caja = txt_utilidad_porcentaje;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_utilidad_porcentajeKeyTyped

    private void txt_descuento_porcentajeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_descuento_porcentajeKeyTyped
        JTextField caja = txt_descuento_porcentaje;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_descuento_porcentajeKeyTyped

    private void cbo_unidadmedidaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_unidadmedidaItemStateChanged
        System.out.println("\nSe ejecuta la busqueda de la Unidad de Material");

        if (cbo_unidadmedida.getSelectedItem().toString().length() > 0) {
            String descripcion = cbo_unidadmedida.getSelectedItem().toString().trim();
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
        }
    }//GEN-LAST:event_cbo_unidadmedidaItemStateChanged

    private void txt_buscar_cotizacionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_cotizacionKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_buscar_cotizacionKeyReleased

    private void btn_cliente_cancelar_busqueda1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_cancelar_busqueda1ActionPerformed
        txt_buscar_cotizacion.setText("");
        dialog_buscar_detalle_cotizacion.dispose();
    }//GEN-LAST:event_btn_cliente_cancelar_busqueda1ActionPerformed

    private void btn_cliente_seleccionar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_seleccionar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cliente_seleccionar1ActionPerformed

    private void tabla_cliente_buscar1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_cliente_buscar1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tabla_cliente_buscar1KeyPressed

    private void btn_buscar_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_detalleActionPerformed
        mostrar_combo_unidadmedida_buscar();
        dialog_buscar_detalle_cotizacion.setSize(1100, 600);
        dialog_buscar_detalle_cotizacion.setLocationRelativeTo(cotizacion);
        dialog_buscar_detalle_cotizacion.setModal(true);
        dialog_buscar_detalle_cotizacion.setVisible(true);
    }//GEN-LAST:event_btn_buscar_detalleActionPerformed

    private void txt_ganancia_porcentajeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ganancia_porcentajeKeyReleased
        System.out.println("Se cambio la ganancia, se procede a actualizar los datos");

        if (txt_ganancia_porcentaje.getText().trim().length() > 0) {
            float costo_neto = Float.parseFloat(txt_costoneto.getText());

            BigDecimal costo_neto_decimal = new BigDecimal(costo_neto);
            costo_neto_decimal = costo_neto_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            costo_neto = (float) costo_neto_decimal.doubleValue();

            calcular_totales(costo_neto);
        } else {
            txt_ganancia_porcentaje.setText("0.00");
        }
    }//GEN-LAST:event_txt_ganancia_porcentajeKeyReleased

    private void txt_utilidad_porcentajeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_utilidad_porcentajeKeyReleased
        System.out.println("Se cambio la utilidad, se procede a actualizar los datos");

        if (txt_utilidad_porcentaje.getText().trim().length() > 0) {
            float costo_neto = Float.parseFloat(txt_costoneto.getText());

            BigDecimal costo_neto_decimal = new BigDecimal(costo_neto);
            costo_neto_decimal = costo_neto_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            costo_neto = (float) costo_neto_decimal.doubleValue();

            calcular_totales(costo_neto);
        } else {
            txt_utilidad_porcentaje.setText("0.00");
        }
    }//GEN-LAST:event_txt_utilidad_porcentajeKeyReleased

    private void txt_descuento_porcentajeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_descuento_porcentajeKeyReleased
        System.out.println("Se cambio la utilidad, se procede a actualizar los datos");

        if (txt_descuento_porcentaje.getText().trim().length() > 0) {
            float costo_neto = Float.parseFloat(txt_costoneto.getText());

            BigDecimal costo_neto_decimal = new BigDecimal(costo_neto);
            costo_neto_decimal = costo_neto_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            costo_neto = (float) costo_neto_decimal.doubleValue();

            calcular_totales(costo_neto);
        } else {
            txt_descuento_porcentaje.setText("0.00");
        }
    }//GEN-LAST:event_txt_descuento_porcentajeKeyReleased

    private void cbo_clienteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_clienteItemStateChanged
        System.out.println("\nSe ejecuta la busqueda del cliente");

        if (cbo_cliente.getSelectedItem().toString().trim().length() > 0) {
            String razon_social = cbo_cliente.getSelectedItem().toString().trim();
            String ruc;

            try {
                ResultSet r = sentencia.executeQuery("select ruc from tcliente where razon_social = '" + razon_social + "'");
                while (r.next()) {
                    ruc = r.getString("ruc").trim();
                    System.out.println("El RUC es: " + ruc);
                    txt_ruc_cliente.setText(ruc);
                }
//                AutoCompleteDecorator.decorate(this.cbo_cliente);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el los Datos del Cliente", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            AutoCompleteDecorator.decorate(this.cbo_cliente);
            txt_ruc_cliente.setText("");
        }
    }//GEN-LAST:event_cbo_clienteItemStateChanged

    private void btn_aprobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_aprobarActionPerformed
        int fil = tabla_general.getSelectedRow();
        if (fil == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione la Cotizacion que será Aprobada");
        } else {
            System.out.println("\nAprobar Cotización");
            System.out.println("====================");

            int fila;
            int id_cotizacion;
            fila = tabla_general.getSelectedRow();
            m = (DefaultTableModel) tabla_general.getModel();
            id_cotizacion = Integer.parseInt((String) m.getValueAt(fila, 1));

            String aprobado_fecha = "";
            String aprobado_persona = "";
            String aprobado_area = "";

            String consulta;

            try {
                consulta = "select \n"
                        + "convert(varchar, c.aprobado_fecha, 103) as aprobado_fecha,\n"
                        + "c.aprobado_persona,\n"
                        + "c.aprobado_area\n"
                        + "from TCotizacion c\n"
                        + "where c.id_cotizacion = '" + id_cotizacion + "'";

                ResultSet r = sentencia.executeQuery(consulta);
                System.out.println("consulta enviada: " + consulta);

                while (r.next()) {
                    aprobado_fecha = r.getString("aprobado_fecha");
                    aprobado_persona = r.getString("aprobado_persona").trim();
                    aprobado_area = r.getString("aprobado_area").trim();
                }

                if (!aprobado_persona.equals("") || !aprobado_area.equals("")) {

                    //da formato a la caja de fecha
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    Date setfecha = null;
                    try {
                        System.out.println("Fecha:" + aprobado_fecha);
                        setfecha = formato.parse(aprobado_fecha);
                    } catch (ParseException ex) {
                        Logger.getLogger(factura.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    txt_fecha_aprobacion.setDate(setfecha);

                    txt_aprobado_por.setText(aprobado_persona);
                    txt_area_responsable.setText(aprobado_area);

                    txt_fecha_aprobacion.setEditable(false);
                    txt_fecha_aprobacion.setEnabled(false);

                    txt_aprobado_por.setEditable(false);
                    txt_area_responsable.setEditable(false);

                    btn_modificar_aprobacion.setVisible(true);
                    btn_aceptar_aprobacion.setVisible(false);
                    btn_cerrar_aprobracion.setVisible(true);

                    dialog_aprobacion.setSize(434, 350);
                    dialog_aprobacion.setLocationRelativeTo(cotizacion);
                    dialog_aprobacion.setModal(true);
                    dialog_aprobacion.setVisible(true);

                } else {

                    System.out.println("capturar fecha y poner en caja de texto");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date d = new Date();
                    String s = sdf.format(d);

                    txt_fecha_aprobacion.setDate(d);
                    txt_aprobado_por.setText("");
                    txt_area_responsable.setText("");

                    btn_modificar_aprobacion.setVisible(false);
                    btn_aceptar_aprobacion.setVisible(true);
                    btn_cerrar_aprobracion.setVisible(true);

                    dialog_aprobacion.setSize(434, 350);
                    dialog_aprobacion.setLocationRelativeTo(cotizacion);
                    dialog_aprobacion.setModal(true);
                    dialog_aprobacion.setVisible(true);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrio al Extraer los datos de Aprobacion de la Cotizacion", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_aprobarActionPerformed

    private void btn_rechazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_rechazarActionPerformed
        int fil = tabla_general.getSelectedRow();
        if (fil == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione la Cotizacion que será Rechazada");
        } else {
            System.out.println("\nRechazar Cotización");
            System.out.println("====================");

            int fila;
            int id_cotizacion;
            fila = tabla_general.getSelectedRow();
            m = (DefaultTableModel) tabla_general.getModel();
            id_cotizacion = Integer.parseInt((String) m.getValueAt(fila, 1));

            String rechazado_fecha = "";
            String rechazado_persona = "";
            String rechazado_area = "";
            String rechazado_motivo = "";

            String consulta;

            try {
                consulta = "select \n"
                        + "convert(varchar, c.rechazado_fecha, 103) as rechazado_fecha,\n"
                        + "c.rechazado_persona,\n"
                        + "c.rechazado_area,\n"
                        + "c.rechazado_motivo\n"
                        + "from TCotizacion c\n"
                        + "where c.id_cotizacion = '" + id_cotizacion + "'";

                ResultSet r = sentencia.executeQuery(consulta);
                System.out.println("consulta enviada: " + consulta);

                while (r.next()) {
                    rechazado_fecha = r.getString("rechazado_fecha");
                    rechazado_persona = r.getString("rechazado_persona").trim();
                    rechazado_area = r.getString("rechazado_area").trim();
                    rechazado_motivo = r.getString("rechazado_motivo").trim();
                }

                if (!rechazado_persona.equals("") || !rechazado_area.equals("") || !rechazado_motivo.equals("")) {

                    //da formato a la caja de fecha
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    Date setfecha = null;
                    try {
                        System.out.println("Fecha:" + rechazado_fecha);
                        setfecha = formato.parse(rechazado_fecha);
                    } catch (ParseException ex) {
                        Logger.getLogger(factura.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    txt_fecha_rechazar.setDate(setfecha);

                    txt_rechazado_por.setText(rechazado_persona);
                    txt_area_responsable_rechazar.setText(rechazado_area);
                    txt_motivo_rechazo.setText(rechazado_motivo);

                    txt_fecha_rechazar.setEditable(false);
                    txt_fecha_rechazar.setEnabled(false);

                    txt_rechazado_por.setEditable(false);
                    txt_area_responsable_rechazar.setEditable(false);
                    txt_motivo_rechazo.setEditable(false);

                    btn_modificar_rechazar.setVisible(true);
                    btn_aceptar_rechazar.setVisible(false);
                    btn_cerrar_rechazar.setVisible(true);

                    dialogo_rechazar.setSize(434, 350);
                    dialogo_rechazar.setLocationRelativeTo(cotizacion);
                    dialogo_rechazar.setModal(true);
                    dialogo_rechazar.setVisible(true);

                } else {

                    System.out.println("capturar fecha y poner en caja de texto");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date d = new Date();
                    String s = sdf.format(d);

                    txt_fecha_rechazar.setDate(d);
                    txt_rechazado_por.setText("");
                    txt_area_responsable_rechazar.setText("");
                    txt_motivo_rechazo.setText("");

                    btn_modificar_rechazar.setVisible(false);
                    btn_aceptar_rechazar.setVisible(true);
                    btn_cerrar_rechazar.setVisible(true);

                    dialogo_rechazar.setSize(434, 350);
                    dialogo_rechazar.setLocationRelativeTo(cotizacion);
                    dialogo_rechazar.setModal(true);
                    dialogo_rechazar.setVisible(true);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrio al Extraer los datos de RECHAZO de la Cotizacion", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_rechazarActionPerformed

    private void Eliminar_cuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Eliminar_cuentaActionPerformed
        System.out.println("\nEliminar Detalle de Cotizacion_Cuenta_Banco");
        System.out.println("=============================================");

        int id_cotizacion_cuentabanco;
        int respuesta;

        int fila = tabla_cuenta_banco.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar el registro a borrar");
        } else {
            respuesta = JOptionPane.showConfirmDialog(null, "¿Desea eliminar este Nro de Cuenta?", "Eliminar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                m = (DefaultTableModel) tabla_cuenta_banco.getModel();
                id_cotizacion_cuentabanco = Integer.parseInt((String) m.getValueAt(fila, 0));
                System.out.println("el id_cotizacion_cuentabanco que se eliminara es: " + id_cotizacion_cuentabanco);
                if (clase_cotizacion_cuentabanco.eliminar(id_cotizacion_cuentabanco)) {
                    System.out.println("el detalle se logró eliminar exitosamente!");
                    JOptionPane.showMessageDialog(null, "Eliminación exitosa");
                    mostrar_tabla_cotizacion_cuentabanco();
                } else {
                    JOptionPane.showMessageDialog(null, "Ocurrio al eliminar El Banco.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_Eliminar_cuentaActionPerformed

    private void tabla_cuenta_bancoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_cuenta_bancoMouseClicked
        if (band_mantenimiento_cotizacion_cuentabanco == 0) {
            if (evt.getButton() == 3) {
                mantenimiento_tabla_cotizacion_cuentabanco.show(tabla_cuenta_banco, evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_tabla_cuenta_bancoMouseClicked

    private void btn_importarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_importarActionPerformed
        mostrar_tabla_CuentaBanco();
        dialog_buscar_cuenta_banco.setSize(700, 400);
        dialog_buscar_cuenta_banco.setLocationRelativeTo(cotizacion);
        dialog_buscar_cuenta_banco.setModal(true);
        dialog_buscar_cuenta_banco.setVisible(true);
    }//GEN-LAST:event_btn_importarActionPerformed

    private void btn_cliente_cancelar_busqueda2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_cancelar_busqueda2ActionPerformed
        dialog_buscar_cuenta_banco.dispose();
    }//GEN-LAST:event_btn_cliente_cancelar_busqueda2ActionPerformed

    private void btn_cliente_seleccionar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_seleccionar2ActionPerformed
        int fila;
        int id_cuentabanco;

        fila = tabla_cuentabanco_seleccionar.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un Nro de Cuenta");
        } else {
            m = (DefaultTableModel) tabla_cuentabanco_seleccionar.getModel();
            id_cuentabanco = Integer.parseInt((String) m.getValueAt(fila, 0));

            if (clase_cotizacion_cuentabanco.crear(id_cotizacion_global, id_cuentabanco, id_empresa_index, id_usuario_index)) {
                dialog_buscar_cuenta_banco.dispose();
                mostrar_tabla_cotizacion_cuentabanco();
            } else {
                JOptionPane.showMessageDialog(null, "Ocurrio al agregar el Nro de Cuenta a la Cotizacion", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_cliente_seleccionar2ActionPerformed

    private void tabla_cuentabanco_seleccionarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_cuentabanco_seleccionarKeyPressed
        int fila = tabla_cuentabanco_seleccionar.getSelectedRow();
        int id_cuentabanco;

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            m = (DefaultTableModel) tabla_cuentabanco_seleccionar.getModel();
            id_cuentabanco = Integer.parseInt((String) m.getValueAt(fila, 0));

            if (clase_cotizacion_cuentabanco.crear(id_cotizacion_global, id_cuentabanco, id_empresa_index, id_usuario_index)) {
                dialog_buscar_cuenta_banco.dispose();
                mostrar_tabla_cotizacion_cuentabanco();
            } else {
                JOptionPane.showMessageDialog(null, "Ocurrio al agregar el Nro de Cuenta a la Cotizacion", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_tabla_cuentabanco_seleccionarKeyPressed

    private void txt_buscar_clienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_clienteKeyReleased
        ResultSet r;
        String bus = txt_buscar_cliente.getText();

        try {
            System.out.println("select id_cliente, razon_social, direccion, ruc from tcliente where razon_social like '%" + bus + "%'  or ruc like '%" + bus + "%' or direccion like '%" + bus + "%' or telefono like '%" + bus + "%' or celular like '%" + bus + "%' or correo like '%" + bus + "%'  order by razon_social asc");
            r = sentencia.executeQuery("select id_cliente, razon_social, direccion, ruc from tcliente where razon_social like '%" + bus + "%'  or ruc like '%" + bus + "%' or direccion like '%" + bus + "%' or telefono like '%" + bus + "%' or celular like '%" + bus + "%' or correo like '%" + bus + "%'  order by razon_social asc");

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Razon Social");
            modelo.addColumn("Direccion");
            modelo.addColumn("R.U.C.");

            String fila[] = new String[4];
            while (r.next()) {
                fila[0] = r.getString("id_cliente").trim();
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
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Cliente - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txt_buscar_clienteKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        txt_buscar_cliente.setText("");
        dialog_buscar_cliente.dispose();

        dialog_crear_cliente.setSize(429, 350);
        dialog_crear_cliente.setLocationRelativeTo(cotizacion);
        dialog_crear_cliente.setModal(true);
        dialog_crear_cliente.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btn_cerrar_aprobracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cerrar_aprobracionActionPerformed
        txt_aprobado_por.setText("");
        txt_area_responsable.setText("");
        dialog_aprobacion.dispose();
        mostrar_tabla_cotizacion("");
    }//GEN-LAST:event_btn_cerrar_aprobracionActionPerformed

    private void btn_aceptar_aprobacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_aceptar_aprobacionActionPerformed
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dia = txt_fecha_aprobacion.getDate();
        int id_cotizacion = id_cotizacion_global;
        String aprobado = "1";
        String aprobado_fecha = sdf.format(dia);
        String aprobado_persona = txt_aprobado_por.getText().trim();
        String aprobado_area = txt_area_responsable.getText().trim();
        int aprobado_id_usuario = id_usuario_index;

        if (clase_cotizacion.aprobar(id_cotizacion, aprobado, aprobado_fecha, aprobado_persona, aprobado_area, aprobado_id_usuario)) {
            txt_aprobado_por.setText("");
            txt_area_responsable.setText("");
            dialog_aprobacion.dispose();
            mostrar_tabla_cotizacion("");
        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al Aprobar la Cotizacion", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_aceptar_aprobacionActionPerformed

    private void txt_aprobado_porKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_aprobado_porKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_aprobado_porKeyTyped

    private void txt_area_responsableKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_area_responsableKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_area_responsableKeyTyped

    private void btn_modificar_aprobacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificar_aprobacionActionPerformed
        txt_fecha_aprobacion.setEditable(true);
        txt_fecha_aprobacion.setEnabled(true);
        txt_aprobado_por.setEditable(true);
        txt_area_responsable.setEditable(true);
        btn_aceptar_aprobacion.setVisible(true);
        btn_modificar_aprobacion.setVisible(false);
    }//GEN-LAST:event_btn_modificar_aprobacionActionPerformed

    private void btn_cerrar_rechazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cerrar_rechazarActionPerformed
        txt_rechazado_por.setText("");
        txt_area_responsable_rechazar.setText("");
        txt_motivo_rechazo.setText("");
        dialogo_rechazar.dispose();
        mostrar_tabla_cotizacion("");
    }//GEN-LAST:event_btn_cerrar_rechazarActionPerformed

    private void btn_aceptar_rechazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_aceptar_rechazarActionPerformed
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dia = txt_fecha_rechazar.getDate();
        int id_cotizacion = id_cotizacion_global;
        String rechazado = "1";
        String rechazado_fecha = sdf.format(dia);
        String rechazado_persona = txt_rechazado_por.getText().trim();
        String rechazado_area = txt_area_responsable_rechazar.getText().trim();
        String rechazado_motivo = txt_motivo_rechazo.getText().trim();
        int rechazado_id_usuario = id_usuario_index;


        if (clase_cotizacion.rechazar(id_cotizacion, rechazado, rechazado_fecha, rechazado_persona, rechazado_area, rechazado_motivo, rechazado_id_usuario)) {
            txt_rechazado_por.setText("");
            txt_area_responsable_rechazar.setText("");
            txt_motivo_rechazo.setText("");
            dialogo_rechazar.dispose();
            mostrar_tabla_cotizacion("");
        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al RECLAZAR la Cotizacion", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_aceptar_rechazarActionPerformed

    private void btn_modificar_rechazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificar_rechazarActionPerformed
        txt_fecha_rechazar.setEditable(true);
        txt_fecha_rechazar.setEnabled(true);
        txt_rechazado_por.setEditable(true);
        txt_area_responsable_rechazar.setEditable(true);
        txt_motivo_rechazo.setEditable(true);

        btn_aceptar_rechazar.setVisible(true);
        btn_modificar_rechazar.setVisible(false);
    }//GEN-LAST:event_btn_modificar_rechazarActionPerformed

    private void txt_rechazado_porKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_rechazado_porKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_rechazado_porKeyTyped

    private void txt_area_responsable_rechazarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_area_responsable_rechazarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_area_responsable_rechazarKeyTyped

    private void cbo_estadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_estadoItemStateChanged
        ejecucion_de_buscador();
    }//GEN-LAST:event_cbo_estadoItemStateChanged

    private void cbo_buscar_clienteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_buscar_clienteItemStateChanged
        ejecucion_de_buscador();
    }//GEN-LAST:event_cbo_buscar_clienteItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Eliminar;
    private javax.swing.JMenuItem Eliminar_cuenta;
    private javax.swing.JMenuItem Modificar;
    private javax.swing.JPanel Panel_detalle;
    private javax.swing.JToolBar barra_buscar;
    private javax.swing.JButton btn_aceptar_aprobacion;
    private javax.swing.JButton btn_aceptar_rechazar;
    private javax.swing.JButton btn_aprobar;
    private javax.swing.JButton btn_buscar_cliente;
    private javax.swing.JButton btn_buscar_detalle;
    private javax.swing.JButton btn_buscar_materiales;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_cancelar_cliente;
    private javax.swing.JButton btn_cancelar_crear_moneda;
    private javax.swing.JButton btn_cancelar_crear_nuerocuantabanco;
    private javax.swing.JButton btn_cancelar_guardar_detalle;
    private javax.swing.JButton btn_cerrar_aprobracion;
    private javax.swing.JButton btn_cerrar_rechazar;
    private javax.swing.JButton btn_cliente_cancelar_busqueda;
    private javax.swing.JButton btn_cliente_cancelar_busqueda1;
    private javax.swing.JButton btn_cliente_cancelar_busqueda2;
    private javax.swing.JButton btn_cliente_seleccionar;
    private javax.swing.JButton btn_cliente_seleccionar1;
    private javax.swing.JButton btn_cliente_seleccionar2;
    private javax.swing.JButton btn_crea_cliente;
    private javax.swing.JButton btn_creacion;
    private javax.swing.JButton btn_crear_nuerocuantabanco;
    private javax.swing.JButton btn_empresatransporte_cancelar_busqueda;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_guardar_detalle;
    private javax.swing.JButton btn_guardar_moneda;
    private javax.swing.JButton btn_importar;
    private javax.swing.JButton btn_modificar;
    private javax.swing.JButton btn_modificar_aprobacion;
    private javax.swing.JButton btn_modificar_rechazar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JButton btn_nuevo_cliente;
    private javax.swing.JButton btn_nuevo_detalle;
    private javax.swing.JButton btn_nuevo_moneda;
    private javax.swing.JButton btn_nuevo_numerocuentabanco;
    private javax.swing.JButton btn_rechazar;
    private javax.swing.JButton btn_vista_previa;
    private javax.swing.JComboBox cbo_atencion;
    private javax.swing.JComboBox cbo_buscar_cliente;
    private javax.swing.JComboBox cbo_categoria_buscar_detalle;
    private javax.swing.JComboBox cbo_cliente;
    private javax.swing.JComboBox cbo_cliente_buscar_detalle;
    private javax.swing.JComboBox cbo_estado;
    private javax.swing.JComboBox cbo_estado_buscar_detalle;
    private javax.swing.JComboBox cbo_formapago;
    private javax.swing.JComboBox cbo_igv;
    private javax.swing.JComboBox cbo_itempadre;
    private javax.swing.JComboBox cbo_moneda;
    private javax.swing.JComboBox cbo_moneda_buscar_detalle;
    private javax.swing.JComboBox cbo_nrocotizacion_buscar_detalle;
    private javax.swing.JComboBox cbo_tipo_cotizacion;
    private javax.swing.JComboBox cbo_unidadmedida;
    private javax.swing.JComboBox cbo_unidadmedida_buscar_detalle;
    private javax.swing.JPanel centro;
    private javax.swing.JCheckBox check_no_afecta_total;
    private javax.swing.JDialog dialog_aprobacion;
    private javax.swing.JDialog dialog_buscar_cliente;
    private javax.swing.JDialog dialog_buscar_cuenta_banco;
    private javax.swing.JDialog dialog_buscar_detalle_cotizacion;
    private javax.swing.JDialog dialog_crear_cliente;
    private javax.swing.JDialog dialog_crear_moneda;
    private javax.swing.JDialog dialog_fecha_creacion;
    private javax.swing.JDialog dialog_ver_crear_cuentabanco;
    private javax.swing.JDialog dialogo_rechazar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
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
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JLabel label_buscar;
    private javax.swing.JLabel lbl_estado;
    private javax.swing.JLabel lbl_estado1;
    private javax.swing.JLabel lbl_estado2;
    private javax.swing.JLabel lbl_estado3;
    private javax.swing.JLabel lbl_estado4;
    private javax.swing.JLabel lbl_estado5;
    private javax.swing.JLabel lbl_estado6;
    private javax.swing.JLabel lbl_estado7;
    private javax.swing.JLabel lbl_estado8;
    private javax.swing.JLabel lbl_estado9;
    private javax.swing.JLabel lbl_numero;
    private javax.swing.JLabel lbl_serie;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JPopupMenu mantenimiento_tabla_cotizacion_cuentabanco;
    private javax.swing.JPopupMenu mantenimiento_tabla_detalle_cotizacion;
    private javax.swing.JPanel norte;
    private javax.swing.JPanel panel_nuevo_detalle;
    private javax.swing.JPanel panel_tabla;
    private javax.swing.ButtonGroup radio_sexo;
    private javax.swing.JTable tabla_cliente_buscar;
    private javax.swing.JTable tabla_cliente_buscar1;
    private javax.swing.JTable tabla_cuenta_banco;
    private javax.swing.JTable tabla_cuentabanco_seleccionar;
    private javax.swing.JTable tabla_detalle;
    private javax.swing.JTable tabla_general;
    private javax.swing.JTextField txt_aprobado_por;
    private javax.swing.JTextField txt_area_responsable;
    private javax.swing.JTextField txt_area_responsable_rechazar;
    private javax.swing.JTextField txt_buscar;
    private javax.swing.JTextField txt_buscar_cliente;
    private javax.swing.JTextField txt_buscar_cotizacion;
    private javax.swing.JTextField txt_cantidad;
    private javax.swing.JTextField txt_celular_cliente_crear;
    private javax.swing.JTextField txt_correo_cliente_crear;
    private javax.swing.JTextField txt_costo_de_venta;
    private javax.swing.JTextField txt_costo_total;
    private javax.swing.JTextField txt_costoneto;
    private javax.swing.JCheckBox txt_crear_moneda_check_moneda_local;
    private javax.swing.JTextField txt_crear_moneda_nombre;
    private javax.swing.JTextField txt_crear_moneda_simbolo;
    private javax.swing.JTextField txt_crear_numerodecuenta;
    private javax.swing.JTextField txt_descuento_monto;
    private javax.swing.JTextField txt_descuento_porcentaje;
    private javax.swing.JTextArea txt_detalle;
    private javax.swing.JTextField txt_direccion_cliente_crear;
    private javax.swing.JTextField txt_duracion;
    private javax.swing.JLabel txt_estado;
    private javax.swing.JTextField txt_f_creacion;
    private javax.swing.JTextField txt_f_modificacion;
    private org.jdesktop.swingx.JXDatePicker txt_fecha;
    private org.jdesktop.swingx.JXDatePicker txt_fecha_aprobacion;
    private org.jdesktop.swingx.JXDatePicker txt_fecha_rechazar;
    private javax.swing.JTextField txt_ganancia_monto;
    private javax.swing.JTextField txt_ganancia_porcentaje;
    private javax.swing.JTextField txt_igv_monto;
    private javax.swing.JTextArea txt_motivo_rechazo;
    private javax.swing.JLabel txt_numero_cotizacion;
    private javax.swing.JTextField txt_precio;
    private javax.swing.JTextField txt_proyecto;
    private javax.swing.JTextField txt_razon_social_cliente_crear;
    private javax.swing.JTextField txt_rechazado_por;
    private javax.swing.JTextField txt_ruc_cliente;
    private javax.swing.JTextField txt_ruc_cliente_crear;
    private javax.swing.JLabel txt_serie;
    private javax.swing.JTextField txt_sub_total;
    private javax.swing.JTextField txt_telefono_cliente_crear;
    private javax.swing.JTextField txt_ubicacion;
    private javax.swing.JTextField txt_unidad_codigo;
    private javax.swing.JTextField txt_usuario;
    private javax.swing.JTextField txt_utilidad_monto;
    private javax.swing.JTextField txt_utilidad_porcentaje;
    // End of variables declaration//GEN-END:variables
}
