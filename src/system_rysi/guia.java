package system_rysi;

//Clases
import Clases.RenderGuia;
import Clases.cGuia;
import Clases.cGuia_Detalle;
import Clases.cFactura;
import Clases.cDocumentos;

import Clases.cCliente;
import Clases.cVehiculo;
import Clases.cEmpresatransporte;
import Clases.cConductor;


//Otros
import Clases.TextAreaEditor;
import Clases.TextAreaRenderer;
import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author ErC
 */
public class guia extends javax.swing.JPanel {

    //datos de conexion
    String controlador_index;
    String DSN_index;
    String user_index;
    String password_index;
    private Connection conexion;
    private Statement sentencia;
    //Clases
    cGuia clase_guia = new cGuia();
    cGuia_Detalle clase_guia_detalle = new cGuia_Detalle();
    cFactura clase_factura = new cFactura();
    cDocumentos clase_documento = new cDocumentos();
    cCliente clase_cliente = new cCliente();
    cVehiculo clase_vehiculo = new cVehiculo();
    cEmpresatransporte clase_empresatransporte = new cEmpresatransporte();
    cConductor clase_conductor = new cConductor();
    //Banderas
    DefaultTableModel m;
    int band_index = 0; //sive para saber si estamos creando o mostrando la informacion de la factura, para desactivar funciones de visualizacion de un nuevo detalle
    int band_anulado;
    int band_impreso;
    int band_cbo_empresatransporte = 0;
    int band_cbo_cliente = 0;
    int band_cbo_conductor = 0;
    int band_mantenimiento_guia_detalle = 0;
    int crear0_modificar1_guia = 0;
    int crear0_modificar1_guia_detalle = 0;
    int band_crear = 0;         //Sirve para saber si se preciono el boton crear
    int band_modificar = 0;     //Sirve para saber si se preciono el boton modificar
    private Component guia;
    //id globales
    int id_cliente_global;
    int id_empresatransporte_global;
    int id_vehiculo_global;
    int id_conductor_global;
    int id_documento_global;
    int id_factura_global;
    int id_guia_global;
    int id_guia_detalle_global;
    String numero_inicial_global;
    int id_empresa_index;
    int id_usuario_index;
    String perfil_usuario_index = "";
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public guia(String controlador, String DSN, String user, String password, int id_empresa, int id_usuario, String perfil_usuario) {
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
        txt_otromotivo.setVisible(false);
        lbl_estado.setVisible(false);

        System.out.println("activando la función de letra Mayúsculas");
        Activar_letras_Mayusculas();

        System.out.println("Llenar combo cliente");
        AutoCompleteDecorator.decorate(this.cbo_cliente);

        System.out.println("Llenar combo empresa de transporte");
        AutoCompleteDecorator.decorate(this.cbo_empresatransporte);

        System.out.println("Llenar combo combos conductor");
        AutoCompleteDecorator.decorate(this.cbo_conductor);

        if (perfil_usuario_index.equals("Solo Lectura")) {
            btn_nuevo.setVisible(false);
            btn_modificar.setVisible(false);
            btn_anular.setVisible(false);
        }

        System.out.println("mostrar tabla guia");
        mostrar_tabla_guia("");
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
            //Guia
            txt_fecha.setEditable(true);
            txt_fecha.setEnabled(true);

            //Empresa de transporte
            cbo_empresatransporte.setEditable(true);
            cbo_empresatransporte.setEnabled(true);
            txt_partida.setEditable(true);

            //Cliente
            cbo_cliente.setEnabled(true);
            cbo_cliente.setEditable(true);
            txt_llegada.setEditable(true);

            //Detalle guia
            txt_cantidad.setEditable(true);
            txt_unidad.setEditable(true);
            txt_peso.setEditable(true);
            txt_detalle.setEditable(true);

            //Pie
            cbo_motivotraslado.setEnabled(true);
            cbo_conductor.setEnabled(true);
            cbo_conductor.setEditable(true);

            //Ventana Crear conductor
            txt_nombre_crear_conductor.setEditable(true);
            txt_apellido_crear_conductor.setEditable(true);
            txt_dni_crear_conductor.setEditable(true);
            txt_direcion_crear_conductor.setEditable(true);
            txt_telefono_crear_conductor.setEditable(true);
            txt_celular_crear_conductor.setEditable(true);
            txt_numero_licencia_crear_conductor.setEditable(true);

            //ventana Crear cliente
            txt_razon_social_cliente_crear.setEditable(true);
            txt_ruc_cliente_crear.setEditable(true);
            txt_direccion_cliente_crear.setEditable(true);
            txt_telefono_cliente_crear.setEditable(true);
            txt_celular_cliente_crear.setEditable(true);
            txt_correo_cliente_crear.setEditable(true);

            //Ventana Crear Empresa de transporte
            txt_razon_social_empresatransporte_crear.setEditable(true);
            txt_ruc_empresatransporte_crear.setEditable(true);
            txt_direccion_empresatransporte_crear.setEditable(true);
            txt_telefono_empresatransporte_crear.setEditable(true);
            txt_celular_empresatransporte_crear.setEditable(true);
            txt_correo_empresatransporte_crear.setEditable(true);

            //Buscar
            txt_buscar_cliente.setEditable(true);
            txt_buscar_empresatransporte.setEditable(true);
            txt_buscar_conductor.setEditable(true);
            txt_buscar_vehiculo.setEditable(true);
            txt_buscar_factura.setEditable(true);

        }

        if (funcion.equals("no_editable")) {
            //Guia
            txt_fecha.setEditable(false);
            txt_fecha.setEnabled(false);

            //Empresa de transporte
            cbo_empresatransporte.setEditable(false);
            cbo_empresatransporte.setEnabled(false);
            txt_partida.setEditable(false);

            //Cliente
            cbo_cliente.setEnabled(false);
            cbo_cliente.setEditable(false);
            txt_llegada.setEditable(false);

            //Detalle guia
            txt_cantidad.setEditable(false);
            txt_unidad.setEditable(false);
            txt_peso.setEditable(false);
            txt_detalle.setEditable(false);

            //Pie
            cbo_motivotraslado.setEnabled(false);
            cbo_conductor.setEnabled(false);
            cbo_conductor.setEditable(false);

            //Ventana Crear conductor
            txt_nombre_crear_conductor.setEditable(false);
            txt_apellido_crear_conductor.setEditable(false);
            txt_dni_crear_conductor.setEditable(false);
            txt_direcion_crear_conductor.setEditable(false);
            txt_telefono_crear_conductor.setEditable(false);
            txt_celular_crear_conductor.setEditable(false);
            txt_numero_licencia_crear_conductor.setEditable(false);

            //ventana Crear cliente
            txt_razon_social_cliente_crear.setEditable(false);
            txt_ruc_cliente_crear.setEditable(false);
            txt_direccion_cliente_crear.setEditable(false);
            txt_telefono_cliente_crear.setEditable(false);
            txt_celular_cliente_crear.setEditable(false);
            txt_correo_cliente_crear.setEditable(false);

            //Ventana Crear Empresa de transporte
            txt_razon_social_empresatransporte_crear.setEditable(false);
            txt_ruc_empresatransporte_crear.setEditable(false);
            txt_direccion_empresatransporte_crear.setEditable(false);
            txt_telefono_empresatransporte_crear.setEditable(false);
            txt_celular_empresatransporte_crear.setEditable(false);
            txt_correo_empresatransporte_crear.setEditable(false);

            //Buscar
            txt_buscar_cliente.setEditable(false);
            txt_buscar_empresatransporte.setEditable(false);
            txt_buscar_conductor.setEditable(false);
            txt_buscar_vehiculo.setEditable(false);
            txt_buscar_factura.setEditable(false);

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
            btn_anular.setEnabled(true);
            txt_buscar.setEnabled(true);
        }

        if (funcion.equals("desactivar")) {
            btn_nuevo.setEnabled(false);
            btn_modificar.setEnabled(false);
            btn_anular.setEnabled(false);
            txt_buscar.setEnabled(false);

        }
    }

    private void mostrar_botones(String funcion) {
        if (funcion.equals("mostrar")) {
            //Buscar
            btn_buscarvehiculo.setVisible(true);
            btn_buscar_empresatransporte.setVisible(true);
            btn_buscar_cliente.setVisible(true);
            btn_buscarfactura.setVisible(true);
            btn_desvincular_factura.setVisible(true);
            btn_buscar_conductor.setVisible(true);

            //Crear
            btn_nueva_empresatransporte.setVisible(true);
            btn_nuevo_cliente.setVisible(true);
            btn_nuevo_detalle.setVisible(true);
            btn_nuevo_conductor.setVisible(true);


        }

        if (funcion.equals("ocultar")) {
            //Buscar
            btn_buscarvehiculo.setVisible(false);
            btn_buscar_empresatransporte.setVisible(false);
            btn_buscar_cliente.setVisible(false);
            btn_buscarfactura.setVisible(false);
            btn_desvincular_factura.setVisible(false);
            btn_buscar_conductor.setVisible(false);

            //Crear
            btn_nueva_empresatransporte.setVisible(false);
            btn_nuevo_cliente.setVisible(false);
            btn_nuevo_detalle.setVisible(false);
            btn_nuevo_conductor.setVisible(false);

            //botones de control
            btn_guardar.setVisible(false);
            btn_cancelar.setVisible(false);
        }
    }

    private String serie_documento_guia() {
        String serie = "";
        try {
            ResultSet r = sentencia.executeQuery("select id_documento, serie, numero_inicial from tdocumentos where nombre='GUIA' and id_empresa='" + id_empresa_index + "'");
            while (r.next()) {
                id_documento_global = Integer.parseInt(r.getString("id_documento"));
                serie = r.getString("serie");
                numero_inicial_global = r.getString("numero_inicial");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el Id y Serie  del Documento GUIA", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return serie;
    }

    private void mostrar_numero_guia() {
        String numero_guia = clase_guia.Generar_Numero(id_empresa_index);
        System.out.println("El numero de Guia generado es: " + numero_guia);
        System.out.println("El numero inicial del documento Guia es: " + numero_inicial_global);

        if (Integer.parseInt(numero_guia) > Integer.parseInt(numero_inicial_global)) {
            System.out.println("El NUMERO GUIA GENERADO es mayor al  NUMERO INICIAL DEL DOCUMENTO");
            System.out.println("Se imprime el NUMERO GUIA GENERADO en el label");
            txt_numero_guia.setText(numero_guia);
        } else {
            System.out.println("El NUMERO GUIA GENERADO es MENOR o IGUAL al  NUMERO INICIAL DEL DOCUMENTO");
            System.out.println("Se imprime el NUMERO INICIAL DEL DOCUMENTO GUIA en el label");
            txt_numero_guia.setText(numero_inicial_global);
        }
    }

    private void tamaño_de_caja_jtextarea(JTextArea caja, KeyEvent evt, int limite) {
        if (caja.getText().length() == limite) {
            evt.consume();
        }
    }

    private void mostrar_datos_guia(int id_guia) {
        System.out.println("\nMostrar datos de Guia");
        System.out.println("=======================");

        System.out.println("Limpiar cajas de texto");
        limpiar_caja_texto();
        lbl_estado.setVisible(false);

        System.out.println("el id_guia seleccionado es: " + id_guia);
        id_guia_global = id_guia;

        ResultSet r;
        //Guia
        String id_documento = "";
        String serie_guia = "";
        String numero_guia = "";
        String fecha = "";

        //Vehiculo
        String id_vehiculo = "";
        String marca = "";
        String placa = "";
        String nro_inscripcion = "";

        //Empresa de transporte
        String id_empresatransporte = "";
        String razonsocial_transporte = "";
        String punto_partida = "";
        String ruc_transporte = "";

        //Cliente
        String id_cliente = "";
        String razonsocial_cliente = "";
        String punto_llegada = "";
        String ruc_cliente = "";

        //Motivo traslado
        String motivo_traslado = "";

        //estados            
        String impreso = "";
        String anulado = "";

        //Factura
        String id_factura = "";
        String serie_factura = "";
        String numero_factura = "";

        //Conductor
        String id_conductor = "";
        String nombre_conductor = "";

        //Datos de creacion
        String f_creacion = "";
        String f_modificacion = "";
        String nombre_usuario = "";

        band_impreso = 0;
        band_anulado = 0;

        try {
            r = sentencia.executeQuery("select \n"
                    + "g.id_documento,\n"
                    + "d.serie as serie_guia, \n"
                    + "g.numero_guia, \n"
                    + "convert(varchar, g.fecha, 103) as fecha,\n"
                    + "g.id_vehiculo,\n"
                    + "v.marca,\n"
                    + "v.placa,\n"
                    + "v.nro_inscripcion,\n"
                    + "g.id_empresatransporte,\n"
                    + "empt.razon_social as razonsocial_transporte,\n"
                    + "g.punto_partida,\n"
                    + "empt.ruc as ruc_transporte,\n"
                    + "g.id_cliente,\n"
                    + "cli.razon_social as razonsocial_cliente,\n"
                    + "g.punto_llegada,\n"
                    + "cli.ruc as ruc_cliente,\n"
                    + "g.motivo_traslado,\n"
                    + "g.impreso,\n"
                    + "g.anulado,\n"
                    + "g.id_factura,\n"
                    + "g.id_conductor,\n"
                    + "cond.Nombre as nombre_conductor,\n"
                    + "g.f_creacion, \n"
                    + "g.f_modificacion, \n"
                    + "u.nombre as nombre_usuario \n"
                    + "from tguia g, tdocumentos d, tvehiculo v, tempresatransporte empt, tcliente cli, tusuario u, tconductor cond \n"
                    + "where \n"
                    + "g.id_documento = d.id_documento and \n"
                    + "g.id_vehiculo = v.id_vehiculo and \n"
                    + "g.id_empresatransporte = empt.id_empresatransporte and \n"
                    + "g.id_cliente = cli.id_cliente and \n"
                    + "g.id_usuario = u.id_usuario and \n"
                    + "g.id_conductor = cond.id_conductor and  \n"
                    + "g.id_guia='" + id_guia + "'");
            while (r.next()) {

                //Guia
                id_documento = r.getString("id_documento").trim();
                serie_guia = r.getString("serie_guia").trim();
                numero_guia = r.getString("numero_guia").trim();
                fecha = r.getString("fecha").trim();

                //Vehiculo
                id_vehiculo = r.getString("id_vehiculo").trim();
                marca = r.getString("marca").trim();
                placa = r.getString("placa").trim();
                nro_inscripcion = r.getString("nro_inscripcion").trim();

                //Empresa de transporte
                id_empresatransporte = r.getString("id_empresatransporte").trim();
                razonsocial_transporte = r.getString("razonsocial_transporte").trim();
                punto_partida = r.getString("punto_partida").trim();
                ruc_transporte = r.getString("ruc_transporte").trim();

                //Cliente
                id_cliente = r.getString("id_cliente").trim();
                razonsocial_cliente = r.getString("razonsocial_cliente").trim();
                punto_llegada = r.getString("punto_llegada").trim();
                ruc_cliente = r.getString("ruc_cliente").trim();

                //Motivo traslado
                motivo_traslado = r.getString("motivo_traslado").trim();

                //estados            
                impreso = r.getString("impreso");
                anulado = r.getString("anulado");

                //Factura
                id_factura = r.getString("id_factura").trim();

                //Conductor
                id_conductor = r.getString("id_conductor").trim();
                nombre_conductor = r.getString("nombre_conductor").trim();

                //Datos de creacion
                f_creacion = r.getString("f_creacion").trim();
                f_modificacion = r.getString("f_modificacion").trim();
                nombre_usuario = r.getString("nombre_usuario").trim();

            }

            System.out.println("Cambiando el titulo");
            lbl_titulo.setText("Información de Guía");

            //Guia
            id_documento_global = Integer.parseInt(id_documento);
            txt_serie.setText(serie_guia);
            txt_numero_guia.setText(numero_guia);

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

            //Vehiculo
            id_vehiculo_global = Integer.parseInt(id_vehiculo);
            txt_marca.setText(marca);
            txt_placa.setText(placa);
            txt_nroinscripcion.setText(nro_inscripcion);

            //Empresa de transporte
            id_empresatransporte_global = Integer.parseInt(id_empresatransporte);
            mostrar_combo_empresatransporte_buscar(razonsocial_transporte);
            txt_partida.setText(punto_partida);
            txt_ruc_transporte.setText(ruc_transporte);

            //Cliente
            id_cliente_global = Integer.parseInt(id_cliente);
            mostrar_combo_cliente_buscar(razonsocial_cliente);
            txt_llegada.setText(punto_llegada);
            txt_ruc_cliente.setText(ruc_cliente);

            //Motivo traslado
            inicializar_cbo_motivotraslado_otros(motivo_traslado);

            //estados            
            band_impreso = Integer.parseInt(impreso);
            band_anulado = Integer.parseInt(anulado);

            //Factura
            id_factura_global = Integer.parseInt(id_factura);
            if (id_factura_global > 0) {

                serie_factura = obtener_seriefactura(id_factura);
                numero_factura = obtener_numerofactura(id_factura);

                txt_seriefactura.setText(serie_factura);
                txt_numerofactura.setText(numero_factura);
            }

            //Conductor
            id_conductor_global = Integer.parseInt(id_conductor);
            mostrar_combo_conductor_buscar(nombre_conductor);

            //Datos de creacion
            txt_f_creacion.setText(f_creacion);
            txt_f_modificacion.setText(f_modificacion);
            txt_usuario.setText(nombre_usuario);

            //activar banderas
            band_mantenimiento_guia_detalle = 1;

            //Mostrar tabla de detalle de guia
            mostrar_tabla_guia_detalle(id_guia);
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

            if (band_anulado == 1) {
                lbl_estado.setVisible(true);
                txt_estado.setText("ANULADO");
                btn_modificar.setEnabled(false);
                btn_anular.setEnabled(false);
            } else {
                if (band_impreso == 1) {
                    btn_modificar.setEnabled(false);
                }
            }

            System.out.println("Ocultar Panel Detalle");
            panel_nuevo_detalle.setVisible(false);

            System.out.println("Mostrar el panel de detalle de datos");
            Panel_detalle.setVisible(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos de la Factura", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Mostrar tablas
    private void mostrar_tabla_guia(String consulta) {
        ResultSet r;
        String estado = "";

        try {
            if (consulta.equals("")) {
                r = sentencia.executeQuery("select id_guia, numero_guia, convert(varchar, fecha, 103) as fecha, anulado from tguia where numero_guia is not null and id_empresa='" + id_empresa_index + "' order by numero_guia desc");
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
                fila[1] = r.getString("id_guia").trim();
                fila[2] = r.getString("numero_guia").trim();
                fila[3] = r.getString("fecha").trim();

                if (r.getString("anulado").equals("1")) {
                    estado = "ANULADO";
                } else {
                    estado = "GUIA";
                }

                fila[0] = estado;
                modelo.addRow(fila);
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

            tabla_general.setDefaultRenderer(Object.class, new RenderGuia());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Guia - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
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

    private void mostrar_tabla_empresatransporte() {
        try {
            ResultSet r = sentencia.executeQuery("select id_empresatransporte, razon_social, direccion, ruc from tempresatransporte where id_empresa = '" + id_empresa_index + "' order by razon_social asc");
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Razon Social");
            modelo.addColumn("Direccion");
            modelo.addColumn("R.U.C.");

            String fila[] = new String[4];
            while (r.next()) {
                fila[0] = r.getString("id_empresatransporte").trim();
                fila[1] = r.getString("razon_social").trim();
                fila[2] = r.getString("direccion").trim();
                fila[3] = r.getString("ruc").trim();
                tabla_empresatransporte_buscar.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_empresatransporte_buscar.setModel(modelo);
            TableColumn columna1 = tabla_empresatransporte_buscar.getColumn("");
            columna1.setPreferredWidth(0);
            TableColumn columna2 = tabla_empresatransporte_buscar.getColumn("Razon Social");
            columna2.setPreferredWidth(300);
            TableColumn columna3 = tabla_empresatransporte_buscar.getColumn("Direccion");
            columna3.setPreferredWidth(300);
            TableColumn columna4 = tabla_empresatransporte_buscar.getColumn("R.U.C.");
            columna4.setPreferredWidth(150);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Empresa Transporte - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_tabla_vehiculo() {
        try {
            ResultSet r = sentencia.executeQuery("select id_vehiculo, marca, placa, modelo, nro_inscripcion from tvehiculo where id_empresa = '" + id_empresa_index + "' order by marca asc");
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Marca");
            modelo.addColumn("Placa");
            modelo.addColumn("Modelo");
            modelo.addColumn("Nro Inscripcion");

            String fila[] = new String[5];
            while (r.next()) {
                fila[0] = r.getString("id_vehiculo").trim();
                fila[1] = r.getString("marca").trim();
                fila[2] = r.getString("placa").trim();
                fila[3] = r.getString("modelo").trim();
                fila[4] = r.getString("nro_inscripcion").trim();
                tabla_vehiculo_buscar.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_vehiculo_buscar.setModel(modelo);
            TableColumn columna1 = tabla_vehiculo_buscar.getColumn("");
            columna1.setPreferredWidth(1);
            TableColumn columna2 = tabla_vehiculo_buscar.getColumn("Marca");
            columna2.setPreferredWidth(150);
            TableColumn columna3 = tabla_vehiculo_buscar.getColumn("Placa");
            columna3.setPreferredWidth(150);
            TableColumn columna4 = tabla_vehiculo_buscar.getColumn("Modelo");
            columna4.setPreferredWidth(150);
            TableColumn columna5 = tabla_vehiculo_buscar.getColumn("Nro Inscripcion");
            columna5.setPreferredWidth(150);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla vehiculo - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_tabla_conductor() {
        try {
            ResultSet r = sentencia.executeQuery("select id_conductor, nombre, apellido, dni, nro_licencia from tconductor where id_empresa = '" + id_empresa_index + "' and nro_licencia is not null order by nombre asc");
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Nombre");
            modelo.addColumn("Apellido");
            modelo.addColumn("DNI");
            modelo.addColumn("Nro Licencia");

            String fila[] = new String[5];
            while (r.next()) {
                fila[0] = r.getString("id_conductor").trim();
                fila[1] = r.getString("nombre").trim();
                fila[2] = r.getString("apellido").trim();
                fila[3] = r.getString("dni").trim();
                fila[4] = r.getString("nro_licencia").trim();
                tabla_conductor_buscar.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_conductor_buscar.setModel(modelo);
            TableColumn columna1 = tabla_conductor_buscar.getColumn("");
            columna1.setPreferredWidth(1);
            TableColumn columna2 = tabla_conductor_buscar.getColumn("Nombre");
            columna2.setPreferredWidth(150);
            TableColumn columna3 = tabla_conductor_buscar.getColumn("Apellido");
            columna3.setPreferredWidth(150);
            TableColumn columna4 = tabla_conductor_buscar.getColumn("DNI");
            columna4.setPreferredWidth(150);
            TableColumn columna5 = tabla_conductor_buscar.getColumn("Nro Licencia");
            columna5.setPreferredWidth(150);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Conductor - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_tabla_guia_detalle_vacia() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("");
        modelo.addColumn("Descripción");
        modelo.addColumn("Cant.");
        modelo.addColumn("Unid.");
        modelo.addColumn("Peso");
        tabla_detalle.setModel(modelo);
        TableColumn columna1 = tabla_detalle.getColumn("");
        columna1.setPreferredWidth(0);
        TableColumn columna2 = tabla_detalle.getColumn("Descripción");
        columna2.setPreferredWidth(700);
        TableColumn columna3 = tabla_detalle.getColumn("Cant.");
        columna3.setPreferredWidth(100);
        TableColumn columna4 = tabla_detalle.getColumn("Unid.");
        columna4.setPreferredWidth(100);
        TableColumn columna5 = tabla_detalle.getColumn("Peso");
        columna5.setPreferredWidth(100);
    }

    private void mostrar_tabla_guia_detalle(int id_guia) {
        System.out.println("\nEjecutandose Mostrar_tabla_Guia_detalle");

        try {
            ResultSet r = sentencia.executeQuery("select id_detalle_guia, descripcion, cantidad, unidad, peso from tdetalle_guia where id_guia='" + id_guia + "'");
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Descripción");
            modelo.addColumn("Cant.");
            modelo.addColumn("Unid.");
            modelo.addColumn("Peso");

            float cantidad;
            float peso;

            String fila[] = new String[5];
            while (r.next()) {

                fila[0] = r.getString("id_detalle_guia").trim();
                fila[1] = r.getString("descripcion").trim();

                cantidad = Float.parseFloat(r.getString("cantidad").trim());
                BigDecimal cantidad_decimal = new BigDecimal(cantidad);
                cantidad_decimal = cantidad_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                cantidad = (float) cantidad_decimal.doubleValue();
                fila[2] = String.valueOf(cantidad);

                fila[3] = r.getString("unidad").trim();

                peso = Float.parseFloat(r.getString("peso").trim());
                BigDecimal peso_decimal = new BigDecimal(peso);
                peso_decimal = peso_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                peso = (float) peso_decimal.doubleValue();
                fila[4] = String.valueOf(peso);

                tabla_detalle.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_detalle.setModel(modelo);

            TableColumn columna1 = tabla_detalle.getColumn("");
            columna1.setPreferredWidth(0);

            TableColumn columna2 = tabla_detalle.getColumn("Descripción");
            columna2.setPreferredWidth(700);

            TableColumn columna3 = tabla_detalle.getColumn("Cant.");
            columna3.setPreferredWidth(100);

            TableColumn columna4 = tabla_detalle.getColumn("Unid.");
            columna4.setPreferredWidth(100);

            TableColumn columna5 = tabla_detalle.getColumn("Peso");
            columna5.setPreferredWidth(100);

            //Hace que la altura de la celda se modifique segun al contenido
            TableColumnModel cmodel = tabla_detalle.getColumnModel();
            TextAreaRenderer textAreaRenderer = new TextAreaRenderer();
            cmodel.getColumn(1).setCellRenderer(textAreaRenderer);
            TextAreaEditor textEditor = new TextAreaEditor();
            cmodel.getColumn(1).setCellEditor(textEditor);

            //Alinear a la derecha
            DefaultTableCellRenderer alineacion1 = new DefaultTableCellRenderer();
            alineacion1.setHorizontalAlignment(SwingConstants.RIGHT);
            tabla_detalle.getColumnModel().getColumn(2).setCellRenderer(alineacion1);
            tabla_detalle.getColumnModel().getColumn(4).setCellRenderer(alineacion1);

            //Alinear al centro
            DefaultTableCellRenderer alineacion2 = new DefaultTableCellRenderer();
            alineacion2.setHorizontalAlignment(SwingConstants.CENTER);
            tabla_detalle.getColumnModel().getColumn(3).setCellRenderer(alineacion2);

            btn_guardar.setEnabled(true);


        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Guia detalle - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_tabla_factura() {
        try {
            ResultSet r = sentencia.executeQuery("select f.id_factura, d.serie, f.numero_factura, convert(varchar, f.fecha, 103) as fecha, c.razon_social from tfactura f, tcliente c, tdocumentos d where f.id_cliente = c.id_cliente and f.id_documento = d.id_documento and f.numero_factura is not null and f.id_guia = '0' and f.anulado = '0' and f.impreso = '0' and f.pagado = '0' and f.id_empresa = '" + id_empresa_index + "' order by f.numero_factura desc");
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Serie");
            modelo.addColumn("Numero");
            modelo.addColumn("Fecha");
            modelo.addColumn("Cliente");

            String fila[] = new String[5];
            while (r.next()) {
                fila[0] = r.getString("id_factura").trim();
                fila[1] = r.getString("serie").trim();
                fila[2] = r.getString("numero_factura").trim();
                fila[3] = r.getString("fecha").trim();
                fila[4] = r.getString("razon_social").trim();
                tabla_factura_buscar.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_factura_buscar.setModel(modelo);
            TableColumn columna1 = tabla_factura_buscar.getColumn("");
            columna1.setPreferredWidth(0);
            TableColumn columna2 = tabla_factura_buscar.getColumn("Serie");
            columna2.setPreferredWidth(100);
            TableColumn columna3 = tabla_factura_buscar.getColumn("Numero");
            columna3.setPreferredWidth(100);
            TableColumn columna4 = tabla_factura_buscar.getColumn("Fecha");
            columna4.setPreferredWidth(150);
            TableColumn columna5 = tabla_factura_buscar.getColumn("Cliente");
            columna5.setPreferredWidth(700);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla FACTURA - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Limpiar
    private void limpiar_caja_texto() {
        //da formato a la caja de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("");
        Date d = new Date();
        String s = sdf.format(d);
        txt_fecha.setDate(d);

        //Guia
        txt_serie.setText("");
        txt_numero_guia.setText("");

        //Vehiculo
        txt_marca.setText("");
        txt_placa.setText("");
        txt_nroinscripcion.setText("");

        //Empresa de transportes
        txt_partida.setText("");
        txt_ruc_transporte.setText("");

        //Cliente
        txt_llegada.setText("");
        txt_ruc_cliente.setText("");

        //Detalle Guia
        txt_cantidad.setText("");
        txt_unidad.setText("UNID.");
        txt_peso.setText("");
        txt_detalle.setText("");

        //Crear Cliente
        txt_razon_social_cliente_crear.setText("");
        txt_ruc_cliente_crear.setText("");
        txt_direccion_cliente_crear.setText("");
        txt_telefono_cliente_crear.setText("");
        txt_celular_cliente_crear.setText("");
        txt_correo_cliente_crear.setText("");

        //Pie
        txt_otromotivo.setText("");
        txt_seriefactura.setText("");
        txt_numerofactura.setText("");
        txt_estado.setText("");

        //Ventana Crear conductor
        txt_nombre_crear_conductor.setText("");
        txt_apellido_crear_conductor.setText("");
        txt_dni_crear_conductor.setText("");
        txt_direcion_crear_conductor.setText("");
        txt_telefono_crear_conductor.setText("");
        txt_celular_crear_conductor.setText("");
        txt_numero_licencia_crear_conductor.setText("");

        //ventana Crear cliente
        txt_razon_social_cliente_crear.setText("");
        txt_ruc_cliente_crear.setText("");
        txt_direccion_cliente_crear.setText("");
        txt_telefono_cliente_crear.setText("");
        txt_celular_cliente_crear.setText("");
        txt_correo_cliente_crear.setText("");

        //Ventana Crear Empresa de transporte
        txt_razon_social_empresatransporte_crear.setText("");
        txt_ruc_empresatransporte_crear.setText("");
        txt_direccion_empresatransporte_crear.setText("");
        txt_telefono_empresatransporte_crear.setText("");
        txt_celular_empresatransporte_crear.setText("");
        txt_correo_empresatransporte_crear.setText("");

        //Buscar
        txt_buscar_cliente.setText("");
        txt_buscar_empresatransporte.setText("");
        txt_buscar_conductor.setText("");
        txt_buscar_vehiculo.setText("");
        txt_buscar_factura.setText("");

        //Detalle de modificacion
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

    private void limpiar_caja_texto_crear_empresatransporte() {
        txt_razon_social_empresatransporte_crear.setText("");
        txt_ruc_empresatransporte_crear.setText("");
        txt_direccion_empresatransporte_crear.setText("");
        txt_telefono_empresatransporte_crear.setText("");
        txt_celular_empresatransporte_crear.setText("");
        txt_correo_empresatransporte_crear.setText("");
    }

    private void limpiar_caja_texto_crear_vehiculo() {
        txt_marca_vehiculo_crear.setText("");
        txt_placa_vehiculo_crear.setText("");
        txt_modelo_vehiculo_crear.setText("");
        txt_nro_inscripcion_vehiculo_crear.setText("");
    }

    private void limpiar_caja_texto_crear_conductor() {
        txt_nombre_crear_conductor.setText("");
        txt_apellido_crear_conductor.setText("");
        txt_dni_crear_conductor.setText("");
        txt_direcion_crear_conductor.setText("");
        txt_telefono_crear_conductor.setText("");
        txt_celular_crear_conductor.setText("");
        txt_numero_licencia_crear_conductor.setText("");
    }

    private void limpiar_caja_texto_crear_detalle_guia() {
        txt_cantidad.setText("");
        txt_unidad.setText("UNID.");
        txt_peso.setText("");
        txt_detalle.setText("");
    }

    //Validaciones
    private static boolean ValidarCorreo(String email) {
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Creaciones
    private void crear_cliente(String razon_social, String ruc, String direccion, String telefono, String celular, String correo, int id_empresa, int id_usuario) {
        System.out.println("\nejecutandose la función: crear_cliente");

        if (clase_cliente.Cliente_crear(razon_social, ruc, direccion, telefono, celular, correo, id_empresa, id_usuario)) {
            System.out.println("\nEl cliente se logró registrar exitosamente!");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_cliente();

            System.out.println("actualizamos tabla cliente");
            mostrar_tabla_cliente();

            System.out.println("Ejecutando la consulta para saber cual es el ultimo id_cliente generado");

            cliente_id_ultimo();
            mostrar_combo_cliente_buscar(razon_social);
            txt_llegada.setText(direccion);
            txt_ruc_cliente.setText(ruc);

            txt_buscar_cliente.setText("");
            dialog_crear_cliente.dispose();

            JOptionPane.showMessageDialog(null, "El cliente se registro exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear el cliente. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear_empresatransporte(String razon_social, String ruc, String direccion, String telefono, String celular, String correo, int id_empresa, int id_usuario) {
        System.out.println("\nEjecutandose la función: crear_empresatransporte");
        System.out.println("\n================================================");

        if (clase_empresatransporte.crear(razon_social, ruc, direccion, telefono, celular, correo, id_empresa, id_usuario)) {
            System.out.println("\nLa Empresa de Transportes se logró registrar exitosamente!");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_empresatransporte();

            System.out.println("actualizamos tabla empresa transporte");
            mostrar_tabla_empresatransporte();

            System.out.println("Ejecutando la consulta para saber cual es el ultimo id_empresatransporte generado");

            empresatransporte_id_ultimo();
            mostrar_combo_empresatransporte_buscar(razon_social);
            txt_partida.setText(direccion);
            txt_ruc_transporte.setText(ruc);

            txt_buscar_empresatransporte.setText("");
            dialog_crear_empresatransporte.dispose();

            JOptionPane.showMessageDialog(null, "La Empresa de Transportes se registro exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear la Empresa de Transportes. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear_vehiculo(String marca, String placa, String modelo, String nro_inscripcion, int id_empresa, int id_usuario) {
        System.out.println("\nEjecutandose la función: crear_vehiculo");
        System.out.println("\n================================================");

        if (clase_vehiculo.crear(marca, placa, modelo, nro_inscripcion, id_empresa, id_usuario)) {
            System.out.println("\nEl Vehiculo se logró registrar exitosamente!");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_vehiculo();

            System.out.println("actualizamos tabla vehiculo");
            mostrar_tabla_vehiculo();

            System.out.println("Ejecutando la consulta para saber cual es el ultimo id_vehiculo generado");

            vehiculo_id_ultimo();
            txt_marca.setText(marca);
            txt_placa.setText(placa);
            txt_nroinscripcion.setText(nro_inscripcion);

            txt_buscar_vehiculo.setText("");
            dialog_crear_vehiculo.dispose();

            JOptionPane.showMessageDialog(null, "El Vehiculo se registro exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear El Vehiculo. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear_conductor(String Nombre, String Apellido, String dni, String direccion, String telefono, String celular, String nro_licencia, int id_empresa, int id_usuario) {
        System.out.println("\nEjecutandose la función: crear_conductor");
        System.out.println("========================================");

        if (clase_conductor.crear(Nombre, Apellido, dni, direccion, telefono, celular, nro_licencia, id_empresa, id_usuario)) {
            System.out.println("\nEl Conductor se logró registrar exitosamente!");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_conductor();

            System.out.println("actualizamos tabla conductor");
            mostrar_tabla_conductor();

            System.out.println("Ejecutando la consulta para saber cual es el ultimo id_conductor generado");

            conductor_id_ultimo();
            mostrar_combo_conductor_buscar(Nombre);

            txt_buscar_conductor.setText("");
            dialog_crear_conductor.dispose();

            JOptionPane.showMessageDialog(null, "El Conductor se registro exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear El Conductor. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear_Guia_Detalle(int id_guia, String descripcion, float cantidad, String unidad, float peso) {
        System.out.println("\nejecutandose la función: crear_guia_detalle");

        if (clase_guia_detalle.crear(id_guia, descripcion, cantidad, unidad, peso)) {
            System.out.println("\nEl Detalle de Guia se logró registrar exitosamente!");

            panel_nuevo_detalle.setVisible(false);
            btn_nuevo_detalle.setVisible(true);

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_detalle_guia();

            System.out.println("actualizamos tabla guia_detalle");
            mostrar_tabla_guia_detalle(id_guia);

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear la Guia_Detalle. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar_Guia_Detalle(int id_detalle_guia, int id_guia, String descripcion, float cantidad, String unidad, float peso) {
        System.out.println("\nejecutandose la función: crear_guia_detalle");

        if (clase_guia_detalle.modificar(id_detalle_guia, id_guia, descripcion, cantidad, unidad, peso)) {
            System.out.println("\nEl Detalle de Guia se logró registrar exitosamente!");
            id_guia_detalle_global = 0;
            id_detalle_guia = 0;
            crear0_modificar1_guia_detalle = 0;

            panel_nuevo_detalle.setVisible(false);
            btn_nuevo_detalle.setVisible(true);

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_detalle_guia();

            System.out.println("actualizamos tabla guia_detalle");
            mostrar_tabla_guia_detalle(id_guia);

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al modificar la Guia_Detalle. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar_Guia(int id_guia, String numero_guia, String fecha, String motivo_traslado, String punto_partida, String punto_llegada, int id_documento, int id_cliente, int id_factura, int id_vehiculo, int id_empresatransporte, int id_conductor, int id_empresa, int id_usuario) {
        System.out.println("ejecutandose la función: modificar_Guia");

        if (clase_guia.modificar(id_guia, numero_guia, fecha, motivo_traslado, punto_partida, punto_llegada, id_documento, id_cliente, id_factura, id_vehiculo, id_empresatransporte, id_conductor, id_empresa, id_usuario)) {
            System.out.println("La Guia se logró modificar exitosamente!");

            operaciones_postModificacion();

            JOptionPane.showMessageDialog(null, "La Guia se Guardó exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al Guardar/Modificar La Guia.\nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
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
        lbl_estado.setVisible(false);
        System.out.println("activar barra de herramientas");
        activar_barra_herramientas("activar");

        System.out.println("Mostrar tabla guia_detalle_vacia");
        mostrar_tabla_guia_detalle_vacia();

        System.out.println("ocultar el panel de detalle de datos");
        Panel_detalle.setVisible(false);

        System.out.println("actualizamos tabla Guia");
        mostrar_tabla_guia("");

        System.out.println("Inicializamos lo id_globales");
        inicializar_id_global();

        band_index = 0;
        band_cbo_cliente = 1;
        band_cbo_conductor = 1;
        band_cbo_empresatransporte = 1;
        band_mantenimiento_guia_detalle = 0;
    }

    //Mostrar Ultimos Id's
    private void guia_id_ultimo() {
        try {
            String consulta = "SELECT id_guia FROM tguia WHERE id_guia = (SELECT MAX(id_guia) from tguia)";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                id_guia_global = Integer.parseInt(r.getString("id_guia"));
                System.out.println("El ultimo id_guia generado es " + id_guia_global);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el ultimo Id de Guia Creado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void empresatransporte_id_ultimo() {
        try {
            String consulta = "SELECT id_empresatransporte FROM tempresatransporte WHERE id_empresatransporte = (SELECT MAX(id_empresatransporte) from tempresatransporte)";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                id_empresatransporte_global = Integer.parseInt(r.getString("id_empresatransporte"));
                System.out.println("El ultimo id_empresatransporte generado es " + id_empresatransporte_global);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el ultimo Id de Empresa de transporte Creado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void vehiculo_id_ultimo() {
        try {
            String consulta = "SELECT id_vehiculo FROM tvehiculo WHERE id_vehiculo = (SELECT MAX(id_vehiculo) from tvehiculo)";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                id_vehiculo_global = Integer.parseInt(r.getString("id_vehiculo"));
                System.out.println("El ultimo id_vehiculo generado es " + id_vehiculo_global);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el ultimo id_vehiculo de transporte Creado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cliente_id_ultimo() {
        try {
            String consulta = "SELECT id_cliente FROM tcliente WHERE id_cliente = (SELECT MAX(id_cliente) from tcliente)";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                id_cliente_global = Integer.parseInt(r.getString("id_cliente"));
                System.out.println("El ultimo id_factura generado es " + id_cliente_global);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el ultimo Id de Cliente Creado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void conductor_id_ultimo() {
        try {
            String consulta = "SELECT id_conductor FROM tconductor WHERE id_conductor = (SELECT MAX(id_conductor) from tconductor)";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                id_conductor_global = Integer.parseInt(r.getString("id_conductor"));
                System.out.println("El ultimo id_conductor generado es " + id_conductor_global);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el ultimo Id de Cliente Creado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //private int CapturarId(String campocapturar, String tabla, String campocondicion, String valorcondicion, int empresa) {
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

    private void inicializar_id_global() {
        crear0_modificar1_guia = 0;
        crear0_modificar1_guia_detalle = 0;

        //id globales
        id_cliente_global = 0;
        id_empresatransporte_global = 0;
        id_vehiculo_global = 0;
        id_conductor_global = 0;
        id_documento_global = 0;
        id_factura_global = 0;
        id_guia_global = 0;
        id_guia_detalle_global = 0;
        numero_inicial_global = "";
    }

    //Mostrar combos
    private void inicializar_cbo_motivotraslado() {
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        modelo.addElement("SERVICIOS");
        modelo.addElement("VENTA");
        modelo.addElement("VENTA SUJETO A CONFIRMAR");
        modelo.addElement("COMPRA");
        modelo.addElement("CONSIGNACIÓN");
        modelo.addElement("DEVOLUCIÓN");
        modelo.addElement("TRASLADO ENTRE ESTABLECIMIENTOS");
        modelo.addElement("PARA TRANSFORMACIÓN");
        modelo.addElement("RECOJO DE BIENES TRANSFORMADOS");
        modelo.addElement("EMISOR ITINERANTE");
        modelo.addElement("ZONA PRIMARIA");
        modelo.addElement("IMPORTACION");
        modelo.addElement("EXPORTACION");
        modelo.addElement("OTROS");

        cbo_motivotraslado.setModel(modelo);
    }

    private void inicializar_cbo_motivotraslado_otros(String motivo_traslado) {
        int band = 0;
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        modelo.addElement(motivo_traslado);

        if (motivo_traslado.equals("SERVICIOS")) {
            band = 1;
        }

        if (motivo_traslado.equals("VENTA")) {
            band = 2;
        }

        if (motivo_traslado.equals("VENTA SUJETO A CONFIRMAR")) {
            band = 3;
        }

        if (motivo_traslado.equals("COMPRA")) {
            band = 4;
        }

        if (motivo_traslado.equals("CONSIGNACIÓN")) {
            band = 5;
        }

        if (motivo_traslado.equals("DEVOLUCIÓN")) {
            band = 6;
        }

        if (motivo_traslado.equals("TRASLADO ENTRE ESTABLECIMIENTOS")) {
            band = 7;
        }

        if (motivo_traslado.equals("PARA TRANSFORMACIÓN")) {
            band = 8;
        }

        if (motivo_traslado.equals("RECOJO DE BIENES TRANSFORMADOS")) {
            band = 9;
        }

        if (motivo_traslado.equals("EMISOR ITINERANTE")) {
            band = 10;
        }

        if (motivo_traslado.equals("ZONA PRIMARIA")) {
            band = 11;
        }

        if (motivo_traslado.equals("IMPORTACION")) {
            band = 12;
        }

        if (motivo_traslado.equals("EXPORTACION")) {
            band = 13;
        }

        switch (band) {
            case 1:
                modelo.addElement("VENTA");
                modelo.addElement("VENTA SUJETO A CONFIRMAR");
                modelo.addElement("COMPRA");
                modelo.addElement("CONSIGNACIÓN");
                modelo.addElement("DEVOLUCIÓN");
                modelo.addElement("TRASLADO ENTRE ESTABLECIMIENTOS");
                modelo.addElement("PARA TRANSFORMACIÓN");
                modelo.addElement("RECOJO DE BIENES TRANSFORMADOS");
                modelo.addElement("EMISOR ITINERANTE");
                modelo.addElement("ZONA PRIMARIA");
                modelo.addElement("IMPORTACION");
                modelo.addElement("EXPORTACION");
                modelo.addElement("OTROS");
                cbo_motivotraslado.setModel(modelo);
                break;

            case 2:
                modelo.addElement("SERVICIOS");
                modelo.addElement("VENTA SUJETO A CONFIRMAR");
                modelo.addElement("COMPRA");
                modelo.addElement("CONSIGNACIÓN");
                modelo.addElement("DEVOLUCIÓN");
                modelo.addElement("TRASLADO ENTRE ESTABLECIMIENTOS");
                modelo.addElement("PARA TRANSFORMACIÓN");
                modelo.addElement("RECOJO DE BIENES TRANSFORMADOS");
                modelo.addElement("EMISOR ITINERANTE");
                modelo.addElement("ZONA PRIMARIA");
                modelo.addElement("IMPORTACION");
                modelo.addElement("EXPORTACION");
                modelo.addElement("OTROS");
                cbo_motivotraslado.setModel(modelo);
                break;

            case 3:
                modelo.addElement("SERVICIOS");
                modelo.addElement("VENTA");
                modelo.addElement("COMPRA");
                modelo.addElement("CONSIGNACIÓN");
                modelo.addElement("DEVOLUCIÓN");
                modelo.addElement("TRASLADO ENTRE ESTABLECIMIENTOS");
                modelo.addElement("PARA TRANSFORMACIÓN");
                modelo.addElement("RECOJO DE BIENES TRANSFORMADOS");
                modelo.addElement("EMISOR ITINERANTE");
                modelo.addElement("ZONA PRIMARIA");
                modelo.addElement("IMPORTACION");
                modelo.addElement("EXPORTACION");
                modelo.addElement("OTROS");
                cbo_motivotraslado.setModel(modelo);
                break;

            case 4:
                modelo.addElement("SERVICIOS");
                modelo.addElement("VENTA");
                modelo.addElement("VENTA SUJETO A CONFIRMAR");
                modelo.addElement("CONSIGNACIÓN");
                modelo.addElement("DEVOLUCIÓN");
                modelo.addElement("TRASLADO ENTRE ESTABLECIMIENTOS");
                modelo.addElement("PARA TRANSFORMACIÓN");
                modelo.addElement("RECOJO DE BIENES TRANSFORMADOS");
                modelo.addElement("EMISOR ITINERANTE");
                modelo.addElement("ZONA PRIMARIA");
                modelo.addElement("IMPORTACION");
                modelo.addElement("EXPORTACION");
                modelo.addElement("OTROS");
                cbo_motivotraslado.setModel(modelo);
                break;

            case 5:
                modelo.addElement("SERVICIOS");
                modelo.addElement("VENTA");
                modelo.addElement("VENTA SUJETO A CONFIRMAR");
                modelo.addElement("COMPRA");
                modelo.addElement("DEVOLUCIÓN");
                modelo.addElement("TRASLADO ENTRE ESTABLECIMIENTOS");
                modelo.addElement("PARA TRANSFORMACIÓN");
                modelo.addElement("RECOJO DE BIENES TRANSFORMADOS");
                modelo.addElement("EMISOR ITINERANTE");
                modelo.addElement("ZONA PRIMARIA");
                modelo.addElement("IMPORTACION");
                modelo.addElement("EXPORTACION");
                modelo.addElement("OTROS");
                cbo_motivotraslado.setModel(modelo);
                break;

            case 6:
                modelo.addElement("SERVICIOS");
                modelo.addElement("VENTA");
                modelo.addElement("VENTA SUJETO A CONFIRMAR");
                modelo.addElement("COMPRA");
                modelo.addElement("CONSIGNACIÓN");
                modelo.addElement("TRASLADO ENTRE ESTABLECIMIENTOS");
                modelo.addElement("PARA TRANSFORMACIÓN");
                modelo.addElement("RECOJO DE BIENES TRANSFORMADOS");
                modelo.addElement("EMISOR ITINERANTE");
                modelo.addElement("ZONA PRIMARIA");
                modelo.addElement("IMPORTACION");
                modelo.addElement("EXPORTACION");
                modelo.addElement("OTROS");
                cbo_motivotraslado.setModel(modelo);
                break;

            case 7:
                modelo.addElement("SERVICIOS");
                modelo.addElement("VENTA");
                modelo.addElement("VENTA SUJETO A CONFIRMAR");
                modelo.addElement("COMPRA");
                modelo.addElement("CONSIGNACIÓN");
                modelo.addElement("DEVOLUCIÓN");
                modelo.addElement("PARA TRANSFORMACIÓN");
                modelo.addElement("RECOJO DE BIENES TRANSFORMADOS");
                modelo.addElement("EMISOR ITINERANTE");
                modelo.addElement("ZONA PRIMARIA");
                modelo.addElement("IMPORTACION");
                modelo.addElement("EXPORTACION");
                modelo.addElement("OTROS");
                cbo_motivotraslado.setModel(modelo);
                break;

            case 8:
                modelo.addElement("SERVICIOS");
                modelo.addElement("VENTA");
                modelo.addElement("VENTA SUJETO A CONFIRMAR");
                modelo.addElement("COMPRA");
                modelo.addElement("CONSIGNACIÓN");
                modelo.addElement("DEVOLUCIÓN");
                modelo.addElement("TRASLADO ENTRE ESTABLECIMIENTOS");
                modelo.addElement("RECOJO DE BIENES TRANSFORMADOS");
                modelo.addElement("EMISOR ITINERANTE");
                modelo.addElement("ZONA PRIMARIA");
                modelo.addElement("IMPORTACION");
                modelo.addElement("EXPORTACION");
                modelo.addElement("OTROS");
                cbo_motivotraslado.setModel(modelo);
                break;

            case 9:
                modelo.addElement("SERVICIOS");
                modelo.addElement("VENTA");
                modelo.addElement("VENTA SUJETO A CONFIRMAR");
                modelo.addElement("COMPRA");
                modelo.addElement("CONSIGNACIÓN");
                modelo.addElement("DEVOLUCIÓN");
                modelo.addElement("TRASLADO ENTRE ESTABLECIMIENTOS");
                modelo.addElement("PARA TRANSFORMACIÓN");
                modelo.addElement("EMISOR ITINERANTE");
                modelo.addElement("ZONA PRIMARIA");
                modelo.addElement("IMPORTACION");
                modelo.addElement("EXPORTACION");
                modelo.addElement("OTROS");
                cbo_motivotraslado.setModel(modelo);
                break;

            case 10:
                modelo.addElement("SERVICIOS");
                modelo.addElement("VENTA");
                modelo.addElement("VENTA SUJETO A CONFIRMAR");
                modelo.addElement("COMPRA");
                modelo.addElement("CONSIGNACIÓN");
                modelo.addElement("DEVOLUCIÓN");
                modelo.addElement("TRASLADO ENTRE ESTABLECIMIENTOS");
                modelo.addElement("PARA TRANSFORMACIÓN");
                modelo.addElement("RECOJO DE BIENES TRANSFORMADOS");
                modelo.addElement("ZONA PRIMARIA");
                modelo.addElement("IMPORTACION");
                modelo.addElement("EXPORTACION");
                modelo.addElement("OTROS");
                cbo_motivotraslado.setModel(modelo);
                break;

            case 11:
                modelo.addElement("SERVICIOS");
                modelo.addElement("VENTA");
                modelo.addElement("VENTA SUJETO A CONFIRMAR");
                modelo.addElement("COMPRA");
                modelo.addElement("CONSIGNACIÓN");
                modelo.addElement("DEVOLUCIÓN");
                modelo.addElement("TRASLADO ENTRE ESTABLECIMIENTOS");
                modelo.addElement("PARA TRANSFORMACIÓN");
                modelo.addElement("RECOJO DE BIENES TRANSFORMADOS");
                modelo.addElement("EMISOR ITINERANTE");
                modelo.addElement("IMPORTACION");
                modelo.addElement("EXPORTACION");
                modelo.addElement("OTROS");
                cbo_motivotraslado.setModel(modelo);
                break;
            case 12:
                modelo.addElement("SERVICIOS");
                modelo.addElement("VENTA");
                modelo.addElement("VENTA SUJETO A CONFIRMAR");
                modelo.addElement("COMPRA");
                modelo.addElement("CONSIGNACIÓN");
                modelo.addElement("DEVOLUCIÓN");
                modelo.addElement("TRASLADO ENTRE ESTABLECIMIENTOS");
                modelo.addElement("PARA TRANSFORMACIÓN");
                modelo.addElement("RECOJO DE BIENES TRANSFORMADOS");
                modelo.addElement("EMISOR ITINERANTE");
                modelo.addElement("ZONA PRIMARIA");
                modelo.addElement("EXPORTACION");
                modelo.addElement("OTROS");
                cbo_motivotraslado.setModel(modelo);
                break;
            case 13:
                modelo.addElement("SERVICIOS");
                modelo.addElement("VENTA");
                modelo.addElement("VENTA SUJETO A CONFIRMAR");
                modelo.addElement("COMPRA");
                modelo.addElement("CONSIGNACIÓN");
                modelo.addElement("DEVOLUCIÓN");
                modelo.addElement("TRASLADO ENTRE ESTABLECIMIENTOS");
                modelo.addElement("PARA TRANSFORMACIÓN");
                modelo.addElement("RECOJO DE BIENES TRANSFORMADOS");
                modelo.addElement("EMISOR ITINERANTE");
                modelo.addElement("ZONA PRIMARIA");
                modelo.addElement("IMPORTACION");
                modelo.addElement("OTROS");
                cbo_motivotraslado.setModel(modelo);
                break;

            case 0:
                modelo.addElement("SERVICIOS");
                modelo.addElement("VENTA");
                modelo.addElement("VENTA SUJETO A CONFIRMAR");
                modelo.addElement("COMPRA");
                modelo.addElement("CONSIGNACIÓN");
                modelo.addElement("DEVOLUCIÓN");
                modelo.addElement("TRASLADO ENTRE ESTABLECIMIENTOS");
                modelo.addElement("PARA TRANSFORMACIÓN");
                modelo.addElement("RECOJO DE BIENES TRANSFORMADOS");
                modelo.addElement("EMISOR ITINERANTE");
                modelo.addElement("ZONA PRIMARIA");
                modelo.addElement("IMPORTACION");
                modelo.addElement("EXPORTACION");
                modelo.addElement("OTROS");
                txt_otromotivo.setText(motivo_traslado);
                cbo_motivotraslado.setModel(modelo);
                break;
        }
    }

    private void mostrar_combo_cliente() {
        System.out.println("Ejecuntandose mostrar_combo_cliente");
        try {
            ResultSet r = sentencia.executeQuery("select razon_social from tcliente order by razon_social asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("razon_social").trim());
                modelo1.addElement(resultado);
            }
            cbo_cliente.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Cliente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_empresatransporte() {
        System.out.println("Ejecuntandose mostrar_combo_empresatransporte");
        try {
            ResultSet r = sentencia.executeQuery("select razon_social from tempresatransporte where id_empresa='" + id_empresa_index + "' order by razon_social asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("razon_social").trim());
                modelo1.addElement(resultado);
            }
            cbo_empresatransporte.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Empresa transporte", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_conductor() {
        System.out.println("Ejecuntandose mostrar_combo_conductor");
        try {
            ResultSet r = sentencia.executeQuery("select Nombre from tconductor where id_empresa='" + id_empresa_index + "' order by Nombre asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("Nombre").trim());
                modelo1.addElement(resultado);
            }
            cbo_conductor.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Nombre", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Mostrar combos buscar
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

    private void mostrar_combo_empresatransporte_buscar(String razon_social) {
        try {
            ResultSet r = sentencia.executeQuery("select razon_social from tempresatransporte where id_empresa='" + id_empresa_index + "' order by razon_social asc");
            DefaultComboBoxModel modelo = new DefaultComboBoxModel();
            modelo.addElement(razon_social);
            String resultado;
            while (r.next()) {
                resultado = (r.getString("razon_social").trim());
                if (!resultado.equals(razon_social)) {
                    modelo.addElement(resultado);
                }
            }
            cbo_empresatransporte.setModel(modelo);
            band_cbo_empresatransporte = 1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Empresa Transporte", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar_combo_conductor_buscar(String nombre) {
        try {
            ResultSet r = sentencia.executeQuery("select nombre from tconductor where id_empresa='" + id_empresa_index + "' order by nombre asc");
            DefaultComboBoxModel modelo = new DefaultComboBoxModel();
            modelo.addElement(nombre);
            String resultado;
            while (r.next()) {
                resultado = (r.getString("nombre").trim());
                if (!resultado.equals(nombre)) {
                    modelo.addElement(resultado);
                }
            }
            cbo_conductor.setModel(modelo);
            band_cbo_conductor = 1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Conductor", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Acciones de combos 
    private void accionComboMotivoTraslado(ActionEvent evt) {
        if (cbo_motivotraslado.getSelectedItem().equals("OTROS")) {
            txt_otromotivo.setVisible(true);
        } else {
            txt_otromotivo.setVisible(false);
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

    //Metodos de Vinculacion Guia - Factura
    private void eliminar_detalle_guia(int id_guia) {
        System.out.println("Ejecutandose Eliminar_detalle_guia");
        System.out.println("==================================");

        int id_detalle_guia;
        ResultSet r;

        try {
            r = sentencia.executeQuery("select id_detalle_guia from tdetalle_guia where id_guia = '" + id_guia + "'");
            while (r.next()) {
                id_detalle_guia = Integer.parseInt(r.getString("id_detalle_guia"));
                if (clase_guia_detalle.eliminar(id_detalle_guia)) {
                    System.out.println("Eliminacion de Detalle de Guia exitosa. Nro de detalle eliminado:" + id_detalle_guia);
                } else {
                    JOptionPane.showMessageDialog(null, "Ocurrio al Eliminar el Detalle de GUIA.\n N°:" + id_detalle_guia, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos de Detalle GUIA", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertar_detalle_factura(int id_factura) {
        System.out.println("Ejecutandose Insertar_detalle_factura");
        System.out.println("=====================================");

        int id_detalle_factura;
        float cantidad;
        String unidad;
        String descripcion;
        float peso = 0;

        ResultSet r;

        try {
            r = sentencia.executeQuery("select id_detalle_factura, cantidad, unidad, descripcion from tdetalle_factura where id_factura = '" + id_factura + "'");
            while (r.next()) {
                id_detalle_factura = Integer.parseInt(r.getString("id_detalle_factura").trim());
                cantidad = Float.parseFloat(r.getString("cantidad").trim());
                unidad = r.getString("unidad").trim();
                descripcion = r.getString("descripcion").trim();

                if (clase_guia_detalle.crear(id_guia_global, descripcion, cantidad, unidad, peso)) {
                    System.out.println("Creacion de Detalle de Guia exitosa. Nro de detalle Factura insertado:" + id_detalle_factura);
                } else {
                    JOptionPane.showMessageDialog(null, "Ocurrio al Insertar el Detalle de Factura\n N°:" + id_detalle_factura, "ERROR", JOptionPane.ERROR_MESSAGE);
                }

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos de Detalle FACTURA", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar_datos_cliente_vinculacion_factura(int id_factura, String serie_factura, String numero_factura) {
        System.out.println("Ejecutandose modificar_datos_cliente_vinculacion_factura");
        System.out.println("========================================================");

        String id_cliente = "";
        String razon_social = "";
        String direccion = "";
        String ruc = "";
        ResultSet r;

        try {
            r = sentencia.executeQuery("select "
                    + "f.id_cliente, "
                    + "c.razon_social, "
                    + "c.direccion, "
                    + "c.ruc "
                    + "from "
                    + "tfactura f, tcliente c "
                    + "where "
                    + "f.id_cliente = c.id_cliente and "
                    + "f.id_factura = '" + id_factura + "' and "
                    + "f.id_empresa = '" + id_empresa_index + "'");

            while (r.next()) {
                id_cliente = r.getString("id_cliente").trim();
                razon_social = r.getString("razon_social").trim();
                direccion = r.getString("direccion").trim();
                ruc = r.getString("ruc").trim();
            }

            id_cliente_global = Integer.parseInt(id_cliente);
            mostrar_combo_cliente_buscar(razon_social);
            txt_llegada.setText(direccion);
            txt_ruc_cliente.setText(ruc);

            System.out.println("actualizamos tabla guia_detalle");
            mostrar_tabla_guia_detalle(id_guia_global);

            id_factura_global = id_factura;
            System.out.println("El id_factura_global es: " + id_factura_global);
            txt_seriefactura.setText(serie_factura);
            txt_numerofactura.setText(numero_factura);

            //Ocultar botones
            btn_nuevo_cliente.setVisible(false);
            btn_buscar_cliente.setVisible(false);
            btn_nuevo_detalle.setVisible(false);

            //Desactivar cajas y combos
            cbo_cliente.setEnabled(false);
            cbo_cliente.setEditable(false);

            //mostrar botones
            btn_desvincular_factura.setVisible(true);

            txt_buscar_factura.setText("");
            dialog_buscar_factura.dispose();


        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos del Cliente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar_vinculo_factura(int id_factura, int id_guia, String accion) {
        System.out.println("Ejecutandose modificar_vinculo_factura");
        System.out.println("======================================");

        if (accion.equals("vincular")) {
            if (clase_factura.vincularGuia(id_factura, id_guia)) {
                System.out.println("Vinculacion: Modificacion del id_guia en tabla TFACTURA efectuada con exito.");
            } else {
                JOptionPane.showMessageDialog(null, "Ocurrio al modificar el id_guia en la tabla Factura. \n Por favor informe sobre este error al \n Administrador del sistema.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (accion.equals("desvincular")) {
            if (clase_factura.vincularGuia(id_factura, id_guia)) {
                System.out.println("Desvinculacion: Modificacion del id_guia en tabla TFACTURA efectuada con exito.");
                id_factura_global = 0;

                //Ocultar botones
                btn_nuevo_cliente.setVisible(true);
                btn_buscar_cliente.setVisible(true);
                btn_nuevo_detalle.setVisible(true);

                //Desactivar cajas y combos
                cbo_cliente.setEnabled(true);
                cbo_cliente.setEditable(true);

                //borrar serie y numero de factura
                txt_seriefactura.setText("");
                txt_numerofactura.setText("");


            } else {
                JOptionPane.showMessageDialog(null, "Ocurrio al modificar el id_guia en la tabla Factura. \n Por favor informe sobre este error al \n Administrador del sistema.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }


    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        radio_sexo = new javax.swing.ButtonGroup();
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
        dialog_crear_empresatransporte = new javax.swing.JDialog();
        jPanel35 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        btn_cancelar_crear_empresatransporte = new javax.swing.JButton();
        btn_guardar_empresatransporte = new javax.swing.JButton();
        jPanel38 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        txt_razon_social_empresatransporte_crear = new javax.swing.JTextField();
        txt_ruc_empresatransporte_crear = new javax.swing.JTextField();
        txt_direccion_empresatransporte_crear = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        txt_telefono_empresatransporte_crear = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        txt_celular_empresatransporte_crear = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        txt_correo_empresatransporte_crear = new javax.swing.JTextField();
        dialog_crear_conductor = new javax.swing.JDialog();
        jPanel20 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txt_nombre_crear_conductor = new javax.swing.JTextField();
        txt_apellido_crear_conductor = new javax.swing.JTextField();
        txt_dni_crear_conductor = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txt_direcion_crear_conductor = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txt_telefono_crear_conductor = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txt_celular_crear_conductor = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txt_numero_licencia_crear_conductor = new javax.swing.JTextField();
        dialog_crear_vehiculo = new javax.swing.JDialog();
        jPanel28 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        btn_cancelar_cliente1 = new javax.swing.JButton();
        btn_crea_cliente1 = new javax.swing.JButton();
        jPanel31 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        txt_marca_vehiculo_crear = new javax.swing.JTextField();
        txt_placa_vehiculo_crear = new javax.swing.JTextField();
        txt_modelo_vehiculo_crear = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        txt_nro_inscripcion_vehiculo_crear = new javax.swing.JTextField();
        dialog_buscar_cliente = new javax.swing.JDialog();
        jPanel39 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        txt_buscar_cliente = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jPanel40 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        btn_cliente_cancelar_busqueda = new javax.swing.JButton();
        btn_cliente_seleccionar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel42 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabla_cliente_buscar = new javax.swing.JTable();
        dialog_buscar_empresatransporte = new javax.swing.JDialog();
        jPanel51 = new javax.swing.JPanel();
        jToolBar5 = new javax.swing.JToolBar();
        jLabel47 = new javax.swing.JLabel();
        txt_buscar_empresatransporte = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jPanel52 = new javax.swing.JPanel();
        jPanel53 = new javax.swing.JPanel();
        btn_empresatransporte_cancelar_busqueda = new javax.swing.JButton();
        btn_cliente_seleccionar3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel54 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tabla_empresatransporte_buscar = new javax.swing.JTable();
        dialog_buscar_conductor = new javax.swing.JDialog();
        jPanel55 = new javax.swing.JPanel();
        jToolBar6 = new javax.swing.JToolBar();
        jLabel56 = new javax.swing.JLabel();
        txt_buscar_conductor = new javax.swing.JTextField();
        jPanel59 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jPanel56 = new javax.swing.JPanel();
        jPanel57 = new javax.swing.JPanel();
        btn_conductor_cancelar_busqueda = new javax.swing.JButton();
        btn_cliente_seleccionar4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel58 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tabla_conductor_buscar = new javax.swing.JTable();
        dialog_buscar_vehiculo = new javax.swing.JDialog();
        jPanel43 = new javax.swing.JPanel();
        jToolBar4 = new javax.swing.JToolBar();
        jLabel3 = new javax.swing.JLabel();
        txt_buscar_vehiculo = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        btn_vehiculo_cancelar_busqueda = new javax.swing.JButton();
        btn_vehiculo_seleccionar = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel46 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tabla_vehiculo_buscar = new javax.swing.JTable();
        dialog_buscar_factura = new javax.swing.JDialog();
        jPanel47 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jLabel22 = new javax.swing.JLabel();
        txt_buscar_factura = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        jPanel48 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        btn_cliente_cancelar_busqueda2 = new javax.swing.JButton();
        btn_cliente_seleccionar2 = new javax.swing.JButton();
        jPanel50 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabla_factura_buscar = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btn_nuevo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btn_modificar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btn_anular = new javax.swing.JButton();
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
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cbo_cliente = new javax.swing.JComboBox();
        txt_llegada = new javax.swing.JTextField();
        txt_ruc_cliente = new javax.swing.JTextField();
        btn_nuevo_cliente = new javax.swing.JButton();
        btn_buscar_cliente = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt_numero_guia = new javax.swing.JTextField();
        txt_serie = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txt_fecha = new org.jdesktop.swingx.JXDatePicker();
        jPanel13 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txt_marca = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txt_nroinscripcion = new javax.swing.JTextField();
        txt_placa = new javax.swing.JTextField();
        btn_buscarvehiculo = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txt_partida = new javax.swing.JTextField();
        txt_ruc_transporte = new javax.swing.JTextField();
        btn_nueva_empresatransporte = new javax.swing.JButton();
        cbo_empresatransporte = new javax.swing.JComboBox();
        btn_buscar_empresatransporte = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_otromotivo = new javax.swing.JTextField();
        txt_seriefactura = new javax.swing.JTextField();
        txt_numerofactura = new javax.swing.JTextField();
        btn_buscarfactura = new javax.swing.JButton();
        btn_nuevo_conductor = new javax.swing.JButton();
        cbo_conductor = new javax.swing.JComboBox();
        cbo_motivotraslado = new javax.swing.JComboBox();
        btn_buscar_conductor = new javax.swing.JButton();
        btn_desvincular_factura = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        panel_nuevo_detalle = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txt_cantidad = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txt_unidad = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_detalle = new javax.swing.JTextArea();
        btn_guardar_detalle = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        txt_peso = new javax.swing.JTextField();
        btn_cancelar_guardar_detalle = new javax.swing.JButton();
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
        lbl_estado = new javax.swing.JLabel();
        txt_estado = new javax.swing.JLabel();

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

        jPanel35.setBackground(new java.awt.Color(0, 110, 204));
        jPanel35.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/empresa_transportes_32_32.png"))); // NOI18N
        jLabel49.setText("Crear Empresa de Transporte");

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

        dialog_crear_empresatransporte.getContentPane().add(jPanel35, java.awt.BorderLayout.PAGE_START);

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

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 51, 153));
        jLabel50.setText("Razon Social:");

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(0, 51, 153));
        jLabel51.setText("R.U.C.:");

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(0, 51, 153));
        jLabel52.setText("Dirección:");

        txt_razon_social_empresatransporte_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_razon_social_empresatransporte_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_razon_social_empresatransporte_crearKeyTyped(evt);
            }
        });

        txt_ruc_empresatransporte_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_ruc_empresatransporte_crear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_ruc_empresatransporte_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_ruc_empresatransporte_crearKeyTyped(evt);
            }
        });

        txt_direccion_empresatransporte_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_direccion_empresatransporte_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_direccion_empresatransporte_crearKeyTyped(evt);
            }
        });

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(0, 51, 153));
        jLabel53.setText("Teléfono:");

        txt_telefono_empresatransporte_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_telefono_empresatransporte_crear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_telefono_empresatransporte_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telefono_empresatransporte_crearKeyTyped(evt);
            }
        });

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(0, 51, 153));
        jLabel54.setText("Celular:");

        txt_celular_empresatransporte_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_celular_empresatransporte_crear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_celular_empresatransporte_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_celular_empresatransporte_crearKeyTyped(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(0, 51, 153));
        jLabel55.setText("Correo:");

        txt_correo_empresatransporte_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_correo_empresatransporte_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_correo_empresatransporte_crearKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_direccion_empresatransporte_crear)
                    .addComponent(txt_razon_social_empresatransporte_crear)
                    .addComponent(txt_correo_empresatransporte_crear, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_celular_empresatransporte_crear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                            .addComponent(txt_ruc_empresatransporte_crear, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_telefono_empresatransporte_crear, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(txt_razon_social_empresatransporte_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(txt_ruc_empresatransporte_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(txt_direccion_empresatransporte_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(txt_telefono_empresatransporte_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(txt_celular_empresatransporte_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(txt_correo_empresatransporte_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel36.add(jPanel38, java.awt.BorderLayout.CENTER);

        dialog_crear_empresatransporte.getContentPane().add(jPanel36, java.awt.BorderLayout.CENTER);

        jPanel20.setBackground(new java.awt.Color(0, 110, 204));
        jPanel20.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/conductor_32_32.png"))); // NOI18N
        jLabel23.setText("Crear Conductor");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_crear_conductor.getContentPane().add(jPanel20, java.awt.BorderLayout.PAGE_START);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setLayout(new java.awt.BorderLayout());

        jPanel22.setPreferredSize(new java.awt.Dimension(418, 40));

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        jButton10.setText("Cancelar");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        jButton11.setText("Guardar");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addGap(0, 239, Short.MAX_VALUE)
                .addComponent(jButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10)
                    .addComponent(jButton11))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel21.add(jPanel22, java.awt.BorderLayout.PAGE_END);

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 51, 153));
        jLabel24.setText("Nombre:");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 51, 153));
        jLabel25.setText("Apellido:");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 51, 153));
        jLabel26.setText("D.N.I:");

        txt_nombre_crear_conductor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_nombre_crear_conductor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_nombre_crear_conductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nombre_crear_conductorKeyTyped(evt);
            }
        });

        txt_apellido_crear_conductor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_apellido_crear_conductor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_apellido_crear_conductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_apellido_crear_conductorKeyTyped(evt);
            }
        });

        txt_dni_crear_conductor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_dni_crear_conductor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_dni_crear_conductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_dni_crear_conductorKeyTyped(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 51, 153));
        jLabel27.setText("Dirección:");

        txt_direcion_crear_conductor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_direcion_crear_conductor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_direcion_crear_conductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_direcion_crear_conductorKeyTyped(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 51, 153));
        jLabel28.setText("Teléfono:");

        txt_telefono_crear_conductor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_telefono_crear_conductor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_telefono_crear_conductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telefono_crear_conductorKeyTyped(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 51, 153));
        jLabel29.setText("Celular:");

        txt_celular_crear_conductor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_celular_crear_conductor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_celular_crear_conductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_celular_crear_conductorKeyTyped(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 51, 153));
        jLabel35.setText("N° Licencia:");

        txt_numero_licencia_crear_conductor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_numero_licencia_crear_conductor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_numero_licencia_crear_conductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_numero_licencia_crear_conductorKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(6, 6, 6))
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(10, 10, 10)))
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_apellido_crear_conductor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                            .addComponent(txt_nombre_crear_conductor)
                            .addComponent(txt_direcion_crear_conductor)
                            .addComponent(txt_dni_crear_conductor, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_numero_licencia_crear_conductor, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                            .addComponent(txt_celular_crear_conductor)
                            .addComponent(txt_telefono_crear_conductor)))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txt_nombre_crear_conductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txt_apellido_crear_conductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txt_dni_crear_conductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txt_direcion_crear_conductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txt_telefono_crear_conductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txt_celular_crear_conductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_numero_licencia_crear_conductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(129, Short.MAX_VALUE))
        );

        jPanel21.add(jPanel23, java.awt.BorderLayout.CENTER);

        dialog_crear_conductor.getContentPane().add(jPanel21, java.awt.BorderLayout.CENTER);

        jPanel28.setBackground(new java.awt.Color(0, 110, 204));
        jPanel28.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/vehiculo.png"))); // NOI18N
        jLabel57.setText("Crear Vehiculo");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_crear_vehiculo.getContentPane().add(jPanel28, java.awt.BorderLayout.PAGE_START);

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setLayout(new java.awt.BorderLayout());

        jPanel30.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_cancelar_cliente1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cancelar_cliente1.setText("Cancelar");
        btn_cancelar_cliente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_cliente1ActionPerformed(evt);
            }
        });

        btn_crea_cliente1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_crea_cliente1.setText("Guardar");
        btn_crea_cliente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_crea_cliente1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addGap(0, 205, Short.MAX_VALUE)
                .addComponent(btn_crea_cliente1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar_cliente1))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cancelar_cliente1)
                    .addComponent(btn_crea_cliente1))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel29.add(jPanel30, java.awt.BorderLayout.PAGE_END);

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(0, 51, 153));
        jLabel58.setText("Marca:");

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(0, 51, 153));
        jLabel59.setText("Placa:");

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(0, 51, 153));
        jLabel60.setText("Modelo:");

        txt_marca_vehiculo_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_marca_vehiculo_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_marca_vehiculo_crearKeyTyped(evt);
            }
        });

        txt_placa_vehiculo_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_placa_vehiculo_crear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_placa_vehiculo_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_placa_vehiculo_crearKeyTyped(evt);
            }
        });

        txt_modelo_vehiculo_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_modelo_vehiculo_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_modelo_vehiculo_crearKeyTyped(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(0, 51, 153));
        jLabel61.setText("N. de Inscripcion:");

        txt_nro_inscripcion_vehiculo_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_nro_inscripcion_vehiculo_crear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_nro_inscripcion_vehiculo_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nro_inscripcion_vehiculo_crearKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel59, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel58, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel60, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_modelo_vehiculo_crear, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addComponent(txt_marca_vehiculo_crear)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_placa_vehiculo_crear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                            .addComponent(txt_nro_inscripcion_vehiculo_crear, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(txt_marca_vehiculo_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(txt_placa_vehiculo_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel60)
                    .addComponent(txt_modelo_vehiculo_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel61)
                    .addComponent(txt_nro_inscripcion_vehiculo_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(90, Short.MAX_VALUE))
        );

        jPanel29.add(jPanel31, java.awt.BorderLayout.CENTER);

        dialog_crear_vehiculo.getContentPane().add(jPanel29, java.awt.BorderLayout.CENTER);

        dialog_buscar_cliente.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel39.setBackground(new java.awt.Color(255, 255, 255));
        jPanel39.setPreferredSize(new java.awt.Dimension(400, 60));
        jPanel39.setLayout(new java.awt.BorderLayout());

        jToolBar2.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar2.setFloatable(false);
        jToolBar2.setPreferredSize(new java.awt.Dimension(13, 25));

        jLabel2.setText("Buscar:");
        jLabel2.setMaximumSize(new java.awt.Dimension(50, 25));
        jLabel2.setMinimumSize(new java.awt.Dimension(50, 25));
        jLabel2.setPreferredSize(new java.awt.Dimension(50, 25));
        jToolBar2.add(jLabel2);

        txt_buscar_cliente.setColumns(50);
        txt_buscar_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_buscar_clienteKeyReleased(evt);
            }
        });
        jToolBar2.add(txt_buscar_cliente);

        jPanel39.add(jToolBar2, java.awt.BorderLayout.CENTER);

        jPanel34.setBackground(new java.awt.Color(0, 110, 204));
        jPanel34.setPreferredSize(new java.awt.Dimension(418, 35));

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar2_32_32.png"))); // NOI18N
        jLabel36.setText("Buscar Cliente");
        jLabel36.setPreferredSize(new java.awt.Dimension(144, 35));

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
            .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
            .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel39.add(jPanel34, java.awt.BorderLayout.NORTH);

        dialog_buscar_cliente.getContentPane().add(jPanel39, java.awt.BorderLayout.PAGE_START);

        jPanel40.setBackground(new java.awt.Color(255, 255, 255));
        jPanel40.setLayout(new java.awt.BorderLayout());

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

        dialog_buscar_empresatransporte.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel51.setBackground(new java.awt.Color(255, 255, 255));
        jPanel51.setPreferredSize(new java.awt.Dimension(400, 60));
        jPanel51.setLayout(new java.awt.BorderLayout());

        jToolBar5.setFloatable(false);
        jToolBar5.setPreferredSize(new java.awt.Dimension(13, 25));

        jLabel47.setText("Buscar:");
        jLabel47.setMaximumSize(new java.awt.Dimension(50, 25));
        jLabel47.setMinimumSize(new java.awt.Dimension(50, 25));
        jLabel47.setPreferredSize(new java.awt.Dimension(50, 25));
        jToolBar5.add(jLabel47);

        txt_buscar_empresatransporte.setColumns(50);
        txt_buscar_empresatransporte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_buscar_empresatransporteKeyReleased(evt);
            }
        });
        jToolBar5.add(txt_buscar_empresatransporte);

        jPanel51.add(jToolBar5, java.awt.BorderLayout.CENTER);

        jPanel33.setBackground(new java.awt.Color(0, 110, 204));
        jPanel33.setPreferredSize(new java.awt.Dimension(418, 35));

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar2_32_32.png"))); // NOI18N
        jLabel46.setText("Buscar Empresa Transporte");
        jLabel46.setPreferredSize(new java.awt.Dimension(144, 35));

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
            .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
            .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel51.add(jPanel33, java.awt.BorderLayout.NORTH);

        dialog_buscar_empresatransporte.getContentPane().add(jPanel51, java.awt.BorderLayout.PAGE_START);

        jPanel52.setBackground(new java.awt.Color(255, 255, 255));
        jPanel52.setLayout(new java.awt.BorderLayout());

        jPanel53.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_empresatransporte_cancelar_busqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_empresatransporte_cancelar_busqueda.setText("Cancelar");
        btn_empresatransporte_cancelar_busqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_empresatransporte_cancelar_busquedaActionPerformed(evt);
            }
        });

        btn_cliente_seleccionar3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_cliente_seleccionar3.setText("Seleccionar");
        btn_cliente_seleccionar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cliente_seleccionar3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo_24_24.png"))); // NOI18N
        jButton4.setText("Nuevo");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel53Layout = new javax.swing.GroupLayout(jPanel53);
        jPanel53.setLayout(jPanel53Layout);
        jPanel53Layout.setHorizontalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel53Layout.createSequentialGroup()
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                .addComponent(btn_cliente_seleccionar3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_empresatransporte_cancelar_busqueda))
        );
        jPanel53Layout.setVerticalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel53Layout.createSequentialGroup()
                .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_empresatransporte_cancelar_busqueda)
                    .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_cliente_seleccionar3)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel52.add(jPanel53, java.awt.BorderLayout.PAGE_END);

        jPanel54.setBackground(new java.awt.Color(255, 255, 255));

        tabla_empresatransporte_buscar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabla_empresatransporte_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabla_empresatransporte_buscarKeyPressed(evt);
            }
        });
        jScrollPane7.setViewportView(tabla_empresatransporte_buscar);

        javax.swing.GroupLayout jPanel54Layout = new javax.swing.GroupLayout(jPanel54);
        jPanel54.setLayout(jPanel54Layout);
        jPanel54Layout.setHorizontalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel54Layout.setVerticalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
        );

        jPanel52.add(jPanel54, java.awt.BorderLayout.CENTER);

        dialog_buscar_empresatransporte.getContentPane().add(jPanel52, java.awt.BorderLayout.CENTER);

        dialog_buscar_conductor.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel55.setBackground(new java.awt.Color(255, 255, 255));
        jPanel55.setPreferredSize(new java.awt.Dimension(400, 60));
        jPanel55.setLayout(new java.awt.BorderLayout());

        jToolBar6.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar6.setFloatable(false);
        jToolBar6.setPreferredSize(new java.awt.Dimension(13, 25));

        jLabel56.setText("Buscar:");
        jLabel56.setMaximumSize(new java.awt.Dimension(50, 25));
        jLabel56.setMinimumSize(new java.awt.Dimension(50, 25));
        jLabel56.setPreferredSize(new java.awt.Dimension(50, 25));
        jToolBar6.add(jLabel56);

        txt_buscar_conductor.setColumns(50);
        txt_buscar_conductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_buscar_conductorKeyReleased(evt);
            }
        });
        jToolBar6.add(txt_buscar_conductor);

        jPanel55.add(jToolBar6, java.awt.BorderLayout.CENTER);

        jPanel59.setBackground(new java.awt.Color(0, 110, 204));
        jPanel59.setPreferredSize(new java.awt.Dimension(418, 35));

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar2_32_32.png"))); // NOI18N
        jLabel48.setText("Buscar Conductor");
        jLabel48.setPreferredSize(new java.awt.Dimension(144, 35));

        javax.swing.GroupLayout jPanel59Layout = new javax.swing.GroupLayout(jPanel59);
        jPanel59.setLayout(jPanel59Layout);
        jPanel59Layout.setHorizontalGroup(
            jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
            .addGroup(jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
        );
        jPanel59Layout.setVerticalGroup(
            jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
            .addGroup(jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel55.add(jPanel59, java.awt.BorderLayout.NORTH);

        dialog_buscar_conductor.getContentPane().add(jPanel55, java.awt.BorderLayout.PAGE_START);

        jPanel56.setBackground(new java.awt.Color(255, 255, 255));
        jPanel56.setLayout(new java.awt.BorderLayout());

        jPanel57.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_conductor_cancelar_busqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_conductor_cancelar_busqueda.setText("Cancelar");
        btn_conductor_cancelar_busqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_conductor_cancelar_busquedaActionPerformed(evt);
            }
        });

        btn_cliente_seleccionar4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_cliente_seleccionar4.setText("Seleccionar");
        btn_cliente_seleccionar4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cliente_seleccionar4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo_24_24.png"))); // NOI18N
        jButton5.setText("Nuevo");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel57Layout = new javax.swing.GroupLayout(jPanel57);
        jPanel57.setLayout(jPanel57Layout);
        jPanel57Layout.setHorizontalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel57Layout.createSequentialGroup()
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                .addComponent(btn_cliente_seleccionar4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_conductor_cancelar_busqueda))
        );
        jPanel57Layout.setVerticalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel57Layout.createSequentialGroup()
                .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_conductor_cancelar_busqueda)
                    .addGroup(jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_cliente_seleccionar4)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel56.add(jPanel57, java.awt.BorderLayout.PAGE_END);

        jPanel58.setBackground(new java.awt.Color(255, 255, 255));

        tabla_conductor_buscar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabla_conductor_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabla_conductor_buscarKeyPressed(evt);
            }
        });
        jScrollPane8.setViewportView(tabla_conductor_buscar);

        javax.swing.GroupLayout jPanel58Layout = new javax.swing.GroupLayout(jPanel58);
        jPanel58.setLayout(jPanel58Layout);
        jPanel58Layout.setHorizontalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel58Layout.setVerticalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
        );

        jPanel56.add(jPanel58, java.awt.BorderLayout.CENTER);

        dialog_buscar_conductor.getContentPane().add(jPanel56, java.awt.BorderLayout.CENTER);

        dialog_buscar_vehiculo.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel43.setBackground(new java.awt.Color(255, 255, 255));
        jPanel43.setPreferredSize(new java.awt.Dimension(400, 60));
        jPanel43.setLayout(new java.awt.BorderLayout());

        jToolBar4.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar4.setFloatable(false);
        jToolBar4.setPreferredSize(new java.awt.Dimension(13, 25));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Buscar:");
        jLabel3.setMaximumSize(new java.awt.Dimension(50, 25));
        jLabel3.setMinimumSize(new java.awt.Dimension(50, 25));
        jLabel3.setPreferredSize(new java.awt.Dimension(50, 25));
        jToolBar4.add(jLabel3);

        txt_buscar_vehiculo.setColumns(50);
        txt_buscar_vehiculo.setMinimumSize(new java.awt.Dimension(650, 25));
        txt_buscar_vehiculo.setPreferredSize(new java.awt.Dimension(650, 25));
        txt_buscar_vehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_buscar_vehiculoKeyTyped(evt);
            }
        });
        jToolBar4.add(txt_buscar_vehiculo);

        jPanel43.add(jToolBar4, java.awt.BorderLayout.CENTER);

        jPanel32.setBackground(new java.awt.Color(0, 110, 204));
        jPanel32.setPreferredSize(new java.awt.Dimension(418, 35));

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar2_32_32.png"))); // NOI18N
        jLabel37.setText("Buscar Vehiculo");
        jLabel37.setPreferredSize(new java.awt.Dimension(144, 35));

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 702, Short.MAX_VALUE)
            .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
            .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel43.add(jPanel32, java.awt.BorderLayout.NORTH);

        dialog_buscar_vehiculo.getContentPane().add(jPanel43, java.awt.BorderLayout.PAGE_START);

        jPanel44.setBackground(new java.awt.Color(255, 255, 255));
        jPanel44.setLayout(new java.awt.BorderLayout());

        jPanel45.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_vehiculo_cancelar_busqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_vehiculo_cancelar_busqueda.setText("Cancelar");
        btn_vehiculo_cancelar_busqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_vehiculo_cancelar_busquedaActionPerformed(evt);
            }
        });

        btn_vehiculo_seleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_vehiculo_seleccionar.setText("Seleccionar");
        btn_vehiculo_seleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_vehiculo_seleccionarActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo_24_24.png"))); // NOI18N
        jButton3.setText("Nuevo");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel45Layout.createSequentialGroup()
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 371, Short.MAX_VALUE)
                .addComponent(btn_vehiculo_seleccionar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_vehiculo_cancelar_busqueda))
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_vehiculo_cancelar_busqueda)
                    .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_vehiculo_seleccionar)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel44.add(jPanel45, java.awt.BorderLayout.PAGE_END);

        jPanel46.setBackground(new java.awt.Color(255, 255, 255));

        tabla_vehiculo_buscar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabla_vehiculo_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabla_vehiculo_buscarKeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(tabla_vehiculo_buscar);

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
        );

        jPanel44.add(jPanel46, java.awt.BorderLayout.CENTER);

        dialog_buscar_vehiculo.getContentPane().add(jPanel44, java.awt.BorderLayout.CENTER);

        dialog_buscar_factura.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel47.setBackground(new java.awt.Color(255, 255, 255));
        jPanel47.setPreferredSize(new java.awt.Dimension(400, 60));
        jPanel47.setLayout(new java.awt.BorderLayout());

        jToolBar3.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar3.setFloatable(false);
        jToolBar3.setPreferredSize(new java.awt.Dimension(13, 25));

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Buscar:");
        jLabel22.setMaximumSize(new java.awt.Dimension(36, 25));
        jLabel22.setMinimumSize(new java.awt.Dimension(36, 25));
        jLabel22.setOpaque(true);
        jLabel22.setPreferredSize(new java.awt.Dimension(50, 25));
        jToolBar3.add(jLabel22);

        txt_buscar_factura.setColumns(30);
        txt_buscar_factura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_buscar_facturaKeyReleased(evt);
            }
        });
        jToolBar3.add(txt_buscar_factura);

        jPanel47.add(jToolBar3, java.awt.BorderLayout.CENTER);

        jPanel18.setBackground(new java.awt.Color(0, 110, 204));
        jPanel18.setPreferredSize(new java.awt.Dimension(418, 35));

        jLabel62.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar2_32_32.png"))); // NOI18N
        jLabel62.setText("Buscar Factura");
        jLabel62.setPreferredSize(new java.awt.Dimension(144, 35));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel62, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jPanel47.add(jPanel18, java.awt.BorderLayout.NORTH);

        dialog_buscar_factura.getContentPane().add(jPanel47, java.awt.BorderLayout.PAGE_START);

        jPanel48.setBackground(new java.awt.Color(255, 255, 255));
        jPanel48.setLayout(new java.awt.BorderLayout());

        jPanel49.setBackground(new java.awt.Color(255, 255, 255));
        jPanel49.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_cliente_cancelar_busqueda2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cliente_cancelar_busqueda2.setText("Cancelar");
        btn_cliente_cancelar_busqueda2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cliente_cancelar_busqueda2ActionPerformed(evt);
            }
        });

        btn_cliente_seleccionar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_cliente_seleccionar2.setText("Seleccionar");
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
                .addContainerGap(178, Short.MAX_VALUE)
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

        tabla_factura_buscar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabla_factura_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabla_factura_buscarKeyPressed(evt);
            }
        });
        jScrollPane5.setViewportView(tabla_factura_buscar);

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
        );

        jPanel48.add(jPanel50, java.awt.BorderLayout.CENTER);

        dialog_buscar_factura.getContentPane().add(jPanel48, java.awt.BorderLayout.CENTER);

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(0, 110, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guia_32_32.png"))); // NOI18N
        jLabel1.setText("Guía");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1128, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);

        jToolBar1.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar1.setBorder(null);
        jToolBar1.setFloatable(false);
        jToolBar1.setOpaque(false);
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

        btn_anular.setBackground(new java.awt.Color(255, 255, 255));
        btn_anular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar_24_24.png"))); // NOI18N
        btn_anular.setText("Anular");
        btn_anular.setFocusable(false);
        btn_anular.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_anular.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_anular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_anularActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_anular);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        barra_buscar.setBackground(new java.awt.Color(255, 255, 255));
        barra_buscar.setFloatable(false);
        barra_buscar.setPreferredSize(new java.awt.Dimension(13, 25));

        label_buscar.setBackground(new java.awt.Color(255, 255, 255));
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
        panel_tabla.setPreferredSize(new java.awt.Dimension(250, 461));

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );
        panel_tablaLayout.setVerticalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
        );

        jPanel5.add(panel_tabla, java.awt.BorderLayout.LINE_START);

        Panel_detalle.setLayout(new java.awt.BorderLayout());

        norte.setBackground(new java.awt.Color(255, 255, 255));
        norte.setPreferredSize(new java.awt.Dimension(458, 40));

        lbl_titulo.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_titulo.setForeground(new java.awt.Color(0, 110, 204));
        lbl_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_titulo.setText("Información de Guía");

        javax.swing.GroupLayout norteLayout = new javax.swing.GroupLayout(norte);
        norte.setLayout(norteLayout);
        norteLayout.setHorizontalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 878, Short.MAX_VALUE)
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
        jPanel6.setPreferredSize(new java.awt.Dimension(771, 140));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 51, 153));
        jLabel11.setText("Razon Social:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 153));
        jLabel12.setText("Pto. de Llegada:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 153));
        jLabel13.setText("R.U.C.:");

        cbo_cliente.setEditable(true);
        cbo_cliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_cliente.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_clienteItemStateChanged(evt);
            }
        });

        txt_llegada.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txt_ruc_cliente.setEditable(false);
        txt_ruc_cliente.setBackground(new java.awt.Color(255, 255, 255));
        txt_ruc_cliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_ruc_cliente.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        btn_nuevo_cliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btn_nuevo_cliente.setToolTipText("Nuevo Cliente");
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
                        .addComponent(txt_ruc_cliente, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_nuevo_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(cbo_cliente, 0, 1, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_buscar_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_llegada))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(cbo_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_buscar_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txt_llegada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txt_ruc_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_nuevo_cliente))
                .addContainerGap())
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Guía", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 51, 153));
        jLabel14.setText("Serie:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 51, 153));
        jLabel15.setText("Número:");

        txt_numero_guia.setEditable(false);
        txt_numero_guia.setBackground(new java.awt.Color(255, 255, 255));
        txt_numero_guia.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_numero_guia.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txt_serie.setEditable(false);
        txt_serie.setBackground(new java.awt.Color(255, 255, 255));
        txt_serie.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_serie.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 51, 153));
        jLabel30.setText("Fecha:");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_fecha, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                    .addComponent(txt_serie)
                    .addComponent(txt_numero_guia, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txt_serie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txt_numero_guia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txt_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Vehículo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 51, 153));
        jLabel20.setText("Marca:");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 51, 153));
        jLabel21.setText("Placa:");

        txt_marca.setEditable(false);
        txt_marca.setBackground(new java.awt.Color(255, 255, 255));
        txt_marca.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_marca.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 51, 153));
        jLabel31.setText("N° Insc.");

        txt_nroinscripcion.setEditable(false);
        txt_nroinscripcion.setBackground(new java.awt.Color(255, 255, 255));
        txt_nroinscripcion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_nroinscripcion.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txt_placa.setEditable(false);
        txt_placa.setBackground(new java.awt.Color(255, 255, 255));
        txt_placa.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_placa.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        btn_buscarvehiculo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_10_10.png"))); // NOI18N
        btn_buscarvehiculo.setToolTipText("Buscar Vehiculo");
        btn_buscarvehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscarvehiculoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_nroinscripcion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_placa, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(txt_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_buscarvehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20)
                        .addComponent(txt_marca))
                    .addComponent(btn_buscarvehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txt_placa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txt_nroinscripcion))
                .addGap(2, 2, 2))
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Empresa de Transporte", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 51, 153));
        jLabel32.setText("Razon Social:");

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 51, 153));
        jLabel33.setText("Pto. Partida:");

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 51, 153));
        jLabel34.setText("R.U.C.:");

        txt_partida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txt_ruc_transporte.setEditable(false);
        txt_ruc_transporte.setBackground(new java.awt.Color(255, 255, 255));
        txt_ruc_transporte.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_ruc_transporte.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        btn_nueva_empresatransporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btn_nueva_empresatransporte.setToolTipText("Nueva Empresa de Transporte");
        btn_nueva_empresatransporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nueva_empresatransporteActionPerformed(evt);
            }
        });

        cbo_empresatransporte.setEditable(true);
        cbo_empresatransporte.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_empresatransporte.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_empresatransporteItemStateChanged(evt);
            }
        });

        btn_buscar_empresatransporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_10_10.png"))); // NOI18N
        btn_buscar_empresatransporte.setToolTipText("Buscar Empresa de Transporte");
        btn_buscar_empresatransporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar_empresatransporteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(txt_ruc_transporte)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_nueva_empresatransporte, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(cbo_empresatransporte, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_buscar_empresatransporte, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_partida, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel32)
                        .addComponent(cbo_empresatransporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_buscar_empresatransporte, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(txt_partida))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel34)
                        .addComponent(txt_ruc_transporte))
                    .addComponent(btn_nueva_empresatransporte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2))
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
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        centro.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setPreferredSize(new java.awt.Dimension(725, 50));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 153));
        jLabel4.setText("Motivo de Traslado:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 51, 153));
        jLabel5.setText("Factura:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 153));
        jLabel6.setText("Conductor:");

        txt_otromotivo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txt_seriefactura.setEditable(false);
        txt_seriefactura.setBackground(new java.awt.Color(255, 255, 255));
        txt_seriefactura.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_seriefactura.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txt_numerofactura.setEditable(false);
        txt_numerofactura.setBackground(new java.awt.Color(255, 255, 255));
        txt_numerofactura.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_numerofactura.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        btn_buscarfactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_10_10.png"))); // NOI18N
        btn_buscarfactura.setToolTipText("Buscar Factura");
        btn_buscarfactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscarfacturaActionPerformed(evt);
            }
        });

        btn_nuevo_conductor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btn_nuevo_conductor.setToolTipText("Nuevo Conductor");
        btn_nuevo_conductor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevo_conductorActionPerformed(evt);
            }
        });

        cbo_conductor.setEditable(true);
        cbo_conductor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cbo_motivotraslado.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                accionComboMotivoTraslado(evt);
            }
        });
        cbo_motivotraslado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_motivotrasladoItemStateChanged(evt);
            }
        });

        btn_buscar_conductor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_10_10.png"))); // NOI18N
        btn_buscar_conductor.setToolTipText("Buscar Conductor");
        btn_buscar_conductor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar_conductorActionPerformed(evt);
            }
        });

        btn_desvincular_factura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_10_10.png"))); // NOI18N
        btn_desvincular_factura.setToolTipText("Desvincular Factura");
        btn_desvincular_factura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_desvincular_facturaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_motivotraslado, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_otromotivo, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_seriefactura, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_numerofactura, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_buscarfactura, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_desvincular_factura, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbo_conductor, 0, 49, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_buscar_conductor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(btn_nuevo_conductor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(jLabel6)
                        .addComponent(txt_otromotivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_seriefactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_numerofactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbo_conductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbo_motivotraslado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_buscarfactura, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_nuevo_conductor, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_buscar_conductor, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_desvincular_factura, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        centro.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new java.awt.BorderLayout());

        panel_nuevo_detalle.setBackground(new java.awt.Color(204, 255, 255));
        panel_nuevo_detalle.setPreferredSize(new java.awt.Dimension(840, 90));
        panel_nuevo_detalle.setRequestFocusEnabled(false);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 51, 153));
        jLabel16.setText("Cantidad");

        txt_cantidad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_cantidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cantidadKeyTyped(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 51, 153));
        jLabel17.setText("Unidad");

        txt_unidad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_unidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_unidadKeyTyped(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 51, 153));
        jLabel18.setText("Detalle");

        txt_detalle.setColumns(20);
        txt_detalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_detalle.setRows(3);
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
        jLabel19.setText("Peso");

        txt_peso.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_peso.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_peso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_pesoKeyTyped(evt);
            }
        });

        btn_cancelar_guardar_detalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_10_10.png"))); // NOI18N
        btn_cancelar_guardar_detalle.setToolTipText("Cancelar");
        btn_cancelar_guardar_detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_guardar_detalleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_nuevo_detalleLayout = new javax.swing.GroupLayout(panel_nuevo_detalle);
        panel_nuevo_detalle.setLayout(panel_nuevo_detalleLayout);
        panel_nuevo_detalleLayout.setHorizontalGroup(
            panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_cantidad))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(txt_unidad))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_peso, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
                        .addGap(61, 61, 61))
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_guardar_detalle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_cancelar_guardar_detalle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        panel_nuevo_detalleLayout.setVerticalGroup(
            panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_cantidad)
                            .addComponent(txt_unidad)
                            .addComponent(txt_peso)
                            .addComponent(btn_guardar_detalle))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cancelar_guardar_detalle))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_nuevo_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lbl_estado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(189, 189, 189))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txt_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_cancelar)
                        .addComponent(btn_guardar)
                        .addComponent(btn_imprimir)
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
        dialog_fecha_creacion.setLocationRelativeTo(guia);
        dialog_fecha_creacion.setModal(true);
        dialog_fecha_creacion.setVisible(true);
    }//GEN-LAST:event_btn_creacionActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        dialog_fecha_creacion.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btn_guardar_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardar_detalleActionPerformed
        if (txt_cantidad.getText().length() == 0 || txt_unidad.getText().length() == 0 || txt_peso.getText().length() == 0 || txt_detalle.getText().length() == 0) {
            System.out.println("\nFalta ingresar los campos Cantidad, Unidad, Peso y Descripcion");
            JOptionPane.showMessageDialog(null, "Por Favor Ingrese los campos Cantidad, Unidad, Peso y Descripcion", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            int id_guia = id_guia_global;
            String descripcion = txt_detalle.getText();
            float cantidad = Float.parseFloat(txt_cantidad.getText());
            String unidad = txt_unidad.getText();
            float peso = Float.parseFloat(txt_peso.getText());

            System.out.println("Llamamo a la funcion crear Guia_detalle");

            if (crear0_modificar1_guia_detalle == 0) {
                crear_Guia_Detalle(id_guia, descripcion, cantidad, unidad, peso);

            } else {
                int id_detalle_guia = id_guia_detalle_global;
                modificar_Guia_Detalle(id_detalle_guia, id_guia, descripcion, cantidad, unidad, peso);
            }
        }
    }//GEN-LAST:event_btn_guardar_detalleActionPerformed

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        System.out.println("\ncrear Guia");
        System.out.println("=================");

        System.out.println("inicializar ID's globales");
        crear0_modificar1_guia = 0;
        numero_inicial_global = "";
        inicializar_id_global();


        if (clase_guia.crear(id_empresa_index, id_usuario_index)) {
            System.out.println("\nLa Guia se logró registrar exitosamente!");

            band_index = 1;
            crear0_modificar1_guia = 1;
            band_mantenimiento_guia_detalle = 0;
            System.out.println("Se procede a igualar el crear0_modificar1_factura: " + crear0_modificar1_guia);

            System.out.println("cambiando el titulo");
            lbl_titulo.setText("Nueva Guia");

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
            btn_imprimir.setVisible(false);
            btn_creacion.setVisible(false);

            System.out.println("mostrar botones: guardar, cancelar");
            btn_guardar.setVisible(true);
            btn_cancelar.setVisible(true);
            btn_guardar.setText("Guardar");
            btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png")));
            btn_guardar.setEnabled(false);
            btn_buscarfactura.setVisible(true);

            System.out.println("desactivar barra de herramientas");
            activar_barra_herramientas("desactivar");

            System.out.println("mostrar botones");
            mostrar_botones("mostrar");

            System.out.println("Ejecutando la consulta para saber cual es el ultimo id_guia generado");
            guia_id_ultimo();

            System.out.println("Mostrar tabla guia_detalle_vacia");
            mostrar_tabla_guia_detalle_vacia();

            DefaultComboBoxModel modelocombo = new DefaultComboBoxModel();
            System.out.println("Mostrar Combo Conductor");
            if (band_cbo_conductor == 0) {
                cbo_conductor.setModel(modelocombo);
                mostrar_combo_conductor();
            }

            System.out.println("Mostrar Combo Cliente");
            if (band_cbo_cliente == 0) {
                cbo_cliente.setModel(modelocombo);
                mostrar_combo_cliente();
            }

            System.out.println("Mostrar Combo Empresa Transporte");
            if (band_cbo_empresatransporte == 0) {
                cbo_empresatransporte.setModel(modelocombo);
                mostrar_combo_empresatransporte();
            }

            System.out.println("Mostrar Combo Motivo traslado");
            inicializar_cbo_motivotraslado();

            System.out.println("Ejecutando consulta de id y serie de documento GUIA");
            String serie = serie_documento_guia();
            System.out.println("La serie del Documento GUIA es: " + serie);

            System.out.println("Mostrar la serie en el lbl serie");
            txt_serie.setText(serie);

            System.out.println("Mostrar Numero de Guia generada");
            mostrar_numero_guia();

            System.out.println("mostrar el panel de detalle de datos");
            Panel_detalle.setVisible(true);
            panel_nuevo_detalle.setVisible(false);
            lbl_estado.setVisible(false);

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

            int id_detalle_guia;
            String descripcion;
            String cantidad;
            String unidad;
            String peso;

            m = (DefaultTableModel) tabla_detalle.getModel();
            id_detalle_guia = Integer.parseInt((String) m.getValueAt(fil, 0));
            descripcion = (String) m.getValueAt(fil, 1);
            cantidad = (String) m.getValueAt(fil, 2);
            unidad = (String) m.getValueAt(fil, 3);
            peso = (String) m.getValueAt(fil, 4);

            id_guia_detalle_global = id_detalle_guia;
            txt_cantidad.setText(cantidad);
            txt_unidad.setText(unidad);
            txt_peso.setText(peso);
            txt_detalle.setText(descripcion);

            crear0_modificar1_guia_detalle = 1;

            panel_nuevo_detalle.setVisible(true);
            btn_guardar_detalle.setVisible(true);
            btn_nuevo_detalle.setVisible(false);
        }
    }//GEN-LAST:event_ModificarActionPerformed

    private void tabla_detalleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_detalleMouseClicked
        if (band_mantenimiento_guia_detalle == 0) {
            if (evt.getButton() == 3) {
                mantenimiento_tabla_detalle_factura.show(tabla_detalle, evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_tabla_detalleMouseClicked

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        System.out.println("limpiar cajas de texto");
        limpiar_caja_texto_crear_conductor();

        System.out.println("actualizamos tabla cliente");
        mostrar_tabla_conductor();

        dialog_crear_conductor.dispose();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void btn_nuevo_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_clienteActionPerformed
        dialog_crear_cliente.setSize(429, 350);
        dialog_crear_cliente.setLocationRelativeTo(guia);
        dialog_crear_cliente.setModal(true);
        dialog_crear_cliente.setVisible(true);
    }//GEN-LAST:event_btn_nuevo_clienteActionPerformed

    private void btn_buscarfacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarfacturaActionPerformed
        mostrar_tabla_factura();
        dialog_buscar_factura.setSize(700, 400);
        dialog_buscar_factura.setLocationRelativeTo(guia);
        dialog_buscar_factura.setModal(true);
        dialog_buscar_factura.setVisible(true);
    }//GEN-LAST:event_btn_buscarfacturaActionPerformed

    private void btn_nuevo_conductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_conductorActionPerformed
        dialog_crear_conductor.setSize(429, 450);
        dialog_crear_conductor.setLocationRelativeTo(guia);
        dialog_crear_conductor.setModal(true);
        dialog_crear_conductor.setVisible(true);
    }//GEN-LAST:event_btn_nuevo_conductorActionPerformed

    private void btn_nuevo_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_detalleActionPerformed
        limpiar_caja_texto_crear_detalle_guia();
        panel_nuevo_detalle.setVisible(true);
        btn_guardar_detalle.setVisible(true);
        btn_nuevo_detalle.setVisible(false);
    }//GEN-LAST:event_btn_nuevo_detalleActionPerformed

    private void btn_cancelar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_clienteActionPerformed
        System.out.println("limpiar cajas de texto");
        limpiar_caja_texto_crear_cliente();

        System.out.println("actualizamos tabla cliente");
        mostrar_tabla_cliente();

        dialog_crear_cliente.dispose();
    }//GEN-LAST:event_btn_cancelar_clienteActionPerformed

    private void btn_buscarvehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarvehiculoActionPerformed
        mostrar_tabla_vehiculo();
        dialog_buscar_vehiculo.setSize(700, 400);
        dialog_buscar_vehiculo.setLocationRelativeTo(guia);
        dialog_buscar_vehiculo.setModal(true);
        dialog_buscar_vehiculo.setVisible(true);
    }//GEN-LAST:event_btn_buscarvehiculoActionPerformed

    private void btn_nueva_empresatransporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nueva_empresatransporteActionPerformed
        dialog_crear_empresatransporte.setSize(429, 350);
        dialog_crear_empresatransporte.setLocationRelativeTo(guia);
        dialog_crear_empresatransporte.setModal(true);
        dialog_crear_empresatransporte.setVisible(true);
    }//GEN-LAST:event_btn_nueva_empresatransporteActionPerformed

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

    private void btn_buscar_empresatransporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_empresatransporteActionPerformed
        mostrar_tabla_empresatransporte();
        dialog_buscar_empresatransporte.setSize(700, 400);
        dialog_buscar_empresatransporte.setLocationRelativeTo(guia);
        dialog_buscar_empresatransporte.setModal(true);
        dialog_buscar_empresatransporte.setVisible(true);
    }//GEN-LAST:event_btn_buscar_empresatransporteActionPerformed

    private void btn_buscar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_clienteActionPerformed
        mostrar_tabla_cliente();
        dialog_buscar_cliente.setSize(700, 400);
        dialog_buscar_cliente.setLocationRelativeTo(guia);
        dialog_buscar_cliente.setModal(true);
        dialog_buscar_cliente.setVisible(true);
    }//GEN-LAST:event_btn_buscar_clienteActionPerformed

    private void txt_nombre_crear_conductorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nombre_crear_conductorKeyTyped
        JTextField caja = txt_nombre_crear_conductor;
        int limite = 100;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_nombre_crear_conductorKeyTyped

    private void txt_apellido_crear_conductorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_apellido_crear_conductorKeyTyped
        JTextField caja = txt_apellido_crear_conductor;
        int limite = 100;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_apellido_crear_conductorKeyTyped

    private void txt_dni_crear_conductorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_dni_crear_conductorKeyTyped
        JTextField caja = txt_dni_crear_conductor;
        ingresar_solo_numeros(caja, evt);
        int limite = 8;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_dni_crear_conductorKeyTyped

    private void txt_direcion_crear_conductorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_direcion_crear_conductorKeyTyped
        JTextField caja = txt_direcion_crear_conductor;
        int limite = 200;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_direcion_crear_conductorKeyTyped

    private void txt_telefono_crear_conductorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefono_crear_conductorKeyTyped
        JTextField caja = txt_telefono_crear_conductor;
        ingresar_solo_numeros(caja, evt);
        int limite = 15;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_telefono_crear_conductorKeyTyped

    private void txt_celular_crear_conductorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_celular_crear_conductorKeyTyped
        JTextField caja = txt_celular_crear_conductor;
        ingresar_solo_numeros(caja, evt);
        int limite = 15;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_celular_crear_conductorKeyTyped

    private void txt_numero_licencia_crear_conductorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_numero_licencia_crear_conductorKeyTyped
        JTextField caja = txt_numero_licencia_crear_conductor;
        int limite = 15;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_numero_licencia_crear_conductorKeyTyped

    private void btn_cancelar_crear_empresatransporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_crear_empresatransporteActionPerformed
        System.out.println("limpiar cajas de texto");
        limpiar_caja_texto_crear_cliente();
        limpiar_caja_texto_crear_empresatransporte();

        System.out.println("actualizamos tabla empresatransporte");
        mostrar_tabla_empresatransporte();

        dialog_crear_empresatransporte.dispose();
    }//GEN-LAST:event_btn_cancelar_crear_empresatransporteActionPerformed

    private void txt_razon_social_empresatransporte_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_razon_social_empresatransporte_crearKeyTyped
        JTextField caja = txt_razon_social_empresatransporte_crear;
        int limite = 200;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_razon_social_empresatransporte_crearKeyTyped

    private void txt_ruc_empresatransporte_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ruc_empresatransporte_crearKeyTyped
        JTextField caja = txt_ruc_empresatransporte_crear;
        ingresar_solo_numeros(caja, evt);
        int limite = 11;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_ruc_empresatransporte_crearKeyTyped

    private void txt_direccion_empresatransporte_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_direccion_empresatransporte_crearKeyTyped
        JTextField caja = txt_direccion_empresatransporte_crear;
        int limite = 200;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_direccion_empresatransporte_crearKeyTyped

    private void txt_telefono_empresatransporte_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefono_empresatransporte_crearKeyTyped
        JTextField caja = txt_telefono_empresatransporte_crear;
        ingresar_solo_numeros(caja, evt);
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_telefono_empresatransporte_crearKeyTyped

    private void txt_celular_empresatransporte_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_celular_empresatransporte_crearKeyTyped
        JTextField caja = txt_celular_empresatransporte_crear;
        ingresar_solo_numeros(caja, evt);
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_celular_empresatransporte_crearKeyTyped

    private void txt_correo_empresatransporte_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_correo_empresatransporte_crearKeyTyped
        JTextField caja = txt_correo_empresatransporte_crear;
        int limite = 50;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_correo_empresatransporte_crearKeyTyped

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

    private void btn_cliente_cancelar_busquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_cancelar_busquedaActionPerformed
        txt_buscar_cliente.setText("");
        dialog_buscar_cliente.dispose();
    }//GEN-LAST:event_btn_cliente_cancelar_busquedaActionPerformed

    private void btn_cliente_seleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_seleccionarActionPerformed
        int fila;
        int id_cliente;
        String razon_social;
        String direccion;
        String ruc;

        fila = tabla_cliente_buscar.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un Cliente");
        } else {
            m = (DefaultTableModel) tabla_cliente_buscar.getModel();
            id_cliente = Integer.parseInt((String) m.getValueAt(fila, 0));
            razon_social = (String) m.getValueAt(fila, 1);
            direccion = (String) m.getValueAt(fila, 2);
            ruc = (String) m.getValueAt(fila, 3);

            id_cliente_global = id_cliente;
            mostrar_combo_cliente_buscar(razon_social);
            txt_llegada.setText(direccion);
            txt_ruc_cliente.setText(ruc);

            txt_buscar_cliente.setText("");
            dialog_buscar_cliente.dispose();
        }
    }//GEN-LAST:event_btn_cliente_seleccionarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        txt_buscar_cliente.setText("");
        dialog_buscar_cliente.dispose();

        dialog_crear_cliente.setSize(429, 350);
        dialog_crear_cliente.setLocationRelativeTo(guia);
        dialog_crear_cliente.setModal(true);
        dialog_crear_cliente.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tabla_cliente_buscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_cliente_buscarKeyPressed
        int fila = tabla_cliente_buscar.getSelectedRow();
        int id_cliente;
        String razon_social;
        String direccion;
        String ruc;

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            m = (DefaultTableModel) tabla_cliente_buscar.getModel();
            id_cliente = Integer.parseInt((String) m.getValueAt(fila, 0));
            razon_social = (String) m.getValueAt(fila, 1);
            direccion = (String) m.getValueAt(fila, 2);
            ruc = (String) m.getValueAt(fila, 3);

            id_cliente_global = id_cliente;
            System.out.println("El id_cliente_global es: " + id_cliente_global);

            mostrar_combo_cliente_buscar(razon_social);
            txt_llegada.setText(direccion);
            txt_ruc_cliente.setText(ruc);

            txt_buscar_cliente.setText("");
            dialog_buscar_cliente.dispose();
        }
    }//GEN-LAST:event_tabla_cliente_buscarKeyPressed

    private void btn_vehiculo_cancelar_busquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vehiculo_cancelar_busquedaActionPerformed
        txt_buscar_vehiculo.setText("");
        dialog_buscar_vehiculo.dispose();
    }//GEN-LAST:event_btn_vehiculo_cancelar_busquedaActionPerformed

    private void btn_vehiculo_seleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vehiculo_seleccionarActionPerformed
        int fila;
        int id_vehiculo;
        String marca;
        String placa;
        String nro_inscripcion;

        fila = tabla_vehiculo_buscar.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un Vehiculo");
        } else {
            m = (DefaultTableModel) tabla_vehiculo_buscar.getModel();
            id_vehiculo = Integer.parseInt((String) m.getValueAt(fila, 0));
            marca = (String) m.getValueAt(fila, 1);
            placa = (String) m.getValueAt(fila, 2);
            nro_inscripcion = (String) m.getValueAt(fila, 4);

            id_vehiculo_global = id_vehiculo;
            System.out.println("El id_vehiculo_global es: " + id_vehiculo_global);

            txt_marca.setText(marca);
            txt_placa.setText(placa);
            txt_nroinscripcion.setText(nro_inscripcion);

            txt_buscar_vehiculo.setText("");
            dialog_buscar_vehiculo.dispose();
        }
    }//GEN-LAST:event_btn_vehiculo_seleccionarActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        txt_buscar_vehiculo.setText("");
        dialog_buscar_vehiculo.dispose();

        dialog_crear_vehiculo.setSize(429, 350);
        dialog_crear_vehiculo.setLocationRelativeTo(guia);
        dialog_crear_vehiculo.setModal(true);
        dialog_crear_vehiculo.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tabla_vehiculo_buscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_vehiculo_buscarKeyPressed
        int fila = tabla_vehiculo_buscar.getSelectedRow();
        int id_vehiculo;
        String marca;
        String placa;
        String nro_inscripcion;

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            m = (DefaultTableModel) tabla_vehiculo_buscar.getModel();
            id_vehiculo = Integer.parseInt((String) m.getValueAt(fila, 0));
            marca = (String) m.getValueAt(fila, 1);
            placa = (String) m.getValueAt(fila, 2);
            nro_inscripcion = (String) m.getValueAt(fila, 4);

            id_vehiculo_global = id_vehiculo;
            System.out.println("El id_vehiculo_global es: " + id_vehiculo_global);

            txt_marca.setText(marca);
            txt_placa.setText(placa);
            txt_nroinscripcion.setText(nro_inscripcion);

            txt_buscar_vehiculo.setText("");
            dialog_buscar_vehiculo.dispose();
        }
    }//GEN-LAST:event_tabla_vehiculo_buscarKeyPressed

    private void txt_buscar_facturaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_facturaKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ResultSet r;
            String bus = txt_buscar_factura.getText();

            try {
                if (bus.equals("ANULADO")) {
                    bus = "1";
                    System.out.println("select f.id_factura, d.serie, f.numero_factura, convert(varchar, f.fecha, 103) as fecha, c.razon_social from tfactura f, tcliente c, tdocumentos d where f.anulado =  '" + bus + "'  and f.id_cliente=c.id_cliente and f.id_documento = d.id_documento and f.numero_factura is not null and f.id_guia = '0' and f.id_empresa='" + id_empresa_index + "' order by f.numero_factura desc");
                    r = sentencia.executeQuery("select f.id_factura, d.serie, f.numero_factura, convert(varchar, f.fecha, 103) as fecha, c.razon_social from tfactura f, tcliente c, tdocumentos d where f.anulado =  '" + bus + "'  and f.id_cliente=c.id_cliente and f.id_documento = d.id_documento and f.numero_factura is not null and f.id_guia = '0' and f.id_empresa='" + id_empresa_index + "' order by f.numero_factura desc");

                } else {
                    if (bus.equals("PAGADO")) {
                        bus = "1";
                        System.out.println("select f.id_factura, d.serie, f.numero_factura, convert(varchar, f.fecha, 103) as fecha, c.razon_social from tfactura f, tcliente c, tdocumentos d where f.pagado =  '" + bus + "'  and f.id_cliente=c.id_cliente and f.id_documento = d.id_documento and f.numero_factura is not null and f.id_guia = '0' and f.id_empresa='" + id_empresa_index + "' order by f.numero_factura desc");
                        r = sentencia.executeQuery("select f.id_factura, d.serie, f.numero_factura, convert(varchar, f.fecha, 103) as fecha, c.razon_social from tfactura f, tcliente c, tdocumentos d where f.pagado =  '" + bus + "'  and f.id_cliente=c.id_cliente and f.id_documento = d.id_documento and f.numero_factura is not null and f.id_guia = '0' and f.id_empresa='" + id_empresa_index + "' order by f.numero_factura desc");
                    } else {
                        System.out.println("select f.id_factura, d.serie, f.numero_factura, convert(varchar, f.fecha, 103) as fecha, c.razon_social from tfactura f, tcliente c, tdocumentos d where (f.numero_factura like '%" + bus + "%'  or f.fecha like '%" + bus + "%' or f.calculo_igv like '%" + bus + "%' or f.sub_total like '%" + bus + "%' or f.total like '%" + bus + "%' or f.total_letras like '%" + bus + "%' or f.total_letras like '%" + bus + "%' or f.moneda like '%" + bus + "%' or f.simbolo_moneda like '%" + bus + "%' or c.razon_social like '%" + bus + "%' or c.ruc like '%" + bus + "%' or c.direccion like '%" + bus + "%') and f.id_cliente=c.id_cliente and f.id_documento = d.id_documento and f.numero_factura is not null and f.id_guia = '0' and f.id_empresa='" + id_empresa_index + "' order by f.numero_factura desc");
                        r = sentencia.executeQuery("select f.id_factura, d.serie, f.numero_factura, convert(varchar, f.fecha, 103) as fecha, c.razon_social from tfactura f, tcliente c, tdocumentos d where (f.numero_factura like '%" + bus + "%'  or f.fecha like '%" + bus + "%' or f.calculo_igv like '%" + bus + "%' or f.sub_total like '%" + bus + "%' or f.total like '%" + bus + "%' or f.total_letras like '%" + bus + "%' or f.total_letras like '%" + bus + "%' or f.moneda like '%" + bus + "%' or f.simbolo_moneda like '%" + bus + "%' or c.razon_social like '%" + bus + "%' or c.ruc like '%" + bus + "%' or c.direccion like '%" + bus + "%') and f.id_cliente=c.id_cliente and f.id_documento = d.id_documento and f.numero_factura is not null and f.id_guia = '0' and f.id_empresa='" + id_empresa_index + "' order by f.numero_factura desc");
                    }
                }

                DefaultTableModel modelo = new DefaultTableModel();
                modelo.addColumn("");
                modelo.addColumn("Serie");
                modelo.addColumn("Numero");
                modelo.addColumn("Fecha");
                modelo.addColumn("Cliente");

                String fila[] = new String[5];
                while (r.next()) {
                    fila[0] = r.getString("id_factura").trim();
                    fila[1] = r.getString("serie").trim();
                    fila[2] = r.getString("numero_factura").trim();
                    fila[3] = r.getString("fecha").trim();
                    fila[4] = r.getString("razon_social").trim();
                    tabla_factura_buscar.setRowHeight(35);
                    modelo.addRow(fila);
                }
                tabla_factura_buscar.setModel(modelo);
                TableColumn columna1 = tabla_factura_buscar.getColumn("");
                columna1.setPreferredWidth(0);
                TableColumn columna2 = tabla_factura_buscar.getColumn("Serie");
                columna2.setPreferredWidth(100);
                TableColumn columna3 = tabla_factura_buscar.getColumn("Numero");
                columna3.setPreferredWidth(100);
                TableColumn columna4 = tabla_factura_buscar.getColumn("Fecha");
                columna4.setPreferredWidth(150);
                TableColumn columna5 = tabla_factura_buscar.getColumn("Cliente");
                columna5.setPreferredWidth(700);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla FACTURA - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_txt_buscar_facturaKeyReleased

    private void btn_cliente_cancelar_busqueda2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_cancelar_busqueda2ActionPerformed
        txt_buscar_factura.setText("");
        dialog_buscar_factura.dispose();
    }//GEN-LAST:event_btn_cliente_cancelar_busqueda2ActionPerformed

    private void btn_cliente_seleccionar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_seleccionar2ActionPerformed
        int fila = tabla_factura_buscar.getSelectedRow();
        int id_factura;
        String serie_factura;
        String numero_factura;

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una Factura");
        } else {
            int respuesta;
            respuesta = JOptionPane.showConfirmDialog(null, "Usted está vinculando la Guía a una Factura, tenga en cuenta estas consideraciones:. \n"
                    + "\n1.- Se procederá a eliminar los detalles de la Guía registrados anteriormente y se insertará los detalles de la Factura a vincular."
                    + "\n2.- Los nuevos detalles de la Factura insertados, no podrán ser modificados y/o eliminados."
                    + "\n3.- Así mismo se procederá a modificar los  datos del cliente y punto de llegada."
                    + "\nBajo estas consideraciones,"
                    + "\n\n¿Desea vincular la Guía con la FACTURA seleccionada?", "ATENCIÓN", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {

                m = (DefaultTableModel) tabla_factura_buscar.getModel();
                id_factura = Integer.parseInt((String) m.getValueAt(fila, 0));
                serie_factura = (String) m.getValueAt(fila, 1);
                numero_factura = (String) m.getValueAt(fila, 2);

                eliminar_detalle_guia(id_guia_global);
                insertar_detalle_factura(id_factura);

                //Procedemos a modificar el campo id_guia de la factura
                //modificar_vinculo_factura(id_factura, id_guia_global, "vincular");

                modificar_datos_cliente_vinculacion_factura(id_factura, serie_factura, numero_factura);
            }
        }
    }//GEN-LAST:event_btn_cliente_seleccionar2ActionPerformed

    private void tabla_factura_buscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_factura_buscarKeyPressed
        int fila = tabla_factura_buscar.getSelectedRow();
        int id_factura;
        String serie_factura;
        String numero_factura;

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int respuesta;
            respuesta = JOptionPane.showConfirmDialog(null, "Usted está vinculando la Guía a una Factura, tenga en cuenta estas consideraciones:. \n"
                    + "\n1.- Se procederá a eliminar los detalles de la Guía registrados anteriormente y se insertará los detalles de la Factura a vincular."
                    + "\n2.- Los nuevos detalles de la Factura insertados, no podrán ser modificados y/o eliminados."
                    + "\n3.- Así mismo se procederá a modificar los  datos del cliente y punto de llegada."
                    + "\nBajo estas consideraciones,"
                    + "\n\n¿Desea vincular la Guía con la FACTURA seleccionada?", "ATENCIÓN", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {

                m = (DefaultTableModel) tabla_factura_buscar.getModel();
                id_factura = Integer.parseInt((String) m.getValueAt(fila, 0));
                serie_factura = (String) m.getValueAt(fila, 1);
                numero_factura = (String) m.getValueAt(fila, 2);

                eliminar_detalle_guia(id_guia_global);
                insertar_detalle_factura(id_factura);

                //Procedemos a modificar el campo id_guia de la factura
                modificar_vinculo_factura(id_factura, id_guia_global, "vincular");

                modificar_datos_cliente_vinculacion_factura(id_factura, serie_factura, numero_factura);
            }
        }
    }//GEN-LAST:event_tabla_factura_buscarKeyPressed

    private void txt_buscar_empresatransporteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_empresatransporteKeyReleased
        ResultSet r;
        String bus = txt_buscar_empresatransporte.getText();

        try {
            System.out.println("select id_empresatransporte, razon_social, direccion, ruc from tempresatransporte where (razon_social like '%" + bus + "%'  or ruc like '%" + bus + "%' or direccion like '%" + bus + "%' or telefono like '%" + bus + "%' or celular like '%" + bus + "%' or correo like '%" + bus + "%') and id_empresa = '" + id_empresa_index + "' order by razon_social asc");
            r = sentencia.executeQuery("select id_empresatransporte, razon_social, direccion, ruc from tempresatransporte where (razon_social like '%" + bus + "%'  or ruc like '%" + bus + "%' or direccion like '%" + bus + "%' or telefono like '%" + bus + "%' or celular like '%" + bus + "%' or correo like '%" + bus + "%') and id_empresa = '" + id_empresa_index + "' order by razon_social asc");

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Razon Social");
            modelo.addColumn("Direccion");
            modelo.addColumn("R.U.C.");

            String fila[] = new String[4];
            while (r.next()) {
                fila[0] = r.getString("id_empresatransporte").trim();
                fila[1] = r.getString("razon_social").trim();
                fila[2] = r.getString("direccion").trim();
                fila[3] = r.getString("ruc").trim();
                tabla_empresatransporte_buscar.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_empresatransporte_buscar.setModel(modelo);
            TableColumn columna1 = tabla_empresatransporte_buscar.getColumn("");
            columna1.setPreferredWidth(0);
            TableColumn columna2 = tabla_empresatransporte_buscar.getColumn("Razon Social");
            columna2.setPreferredWidth(300);
            TableColumn columna3 = tabla_empresatransporte_buscar.getColumn("Direccion");
            columna3.setPreferredWidth(300);
            TableColumn columna4 = tabla_empresatransporte_buscar.getColumn("R.U.C.");
            columna4.setPreferredWidth(150);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Empresa de transporte - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txt_buscar_empresatransporteKeyReleased

    private void btn_empresatransporte_cancelar_busquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_empresatransporte_cancelar_busquedaActionPerformed
        txt_buscar_empresatransporte.setText("");
        dialog_buscar_empresatransporte.dispose();
    }//GEN-LAST:event_btn_empresatransporte_cancelar_busquedaActionPerformed

    private void btn_cliente_seleccionar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_seleccionar3ActionPerformed
        int fila;
        int id_empresatransporte;
        String razon_social;
        String direccion;
        String ruc;

        fila = tabla_empresatransporte_buscar.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una Empresa de Transporte");
        } else {
            m = (DefaultTableModel) tabla_empresatransporte_buscar.getModel();
            id_empresatransporte = Integer.parseInt((String) m.getValueAt(fila, 0));
            razon_social = (String) m.getValueAt(fila, 1);
            direccion = (String) m.getValueAt(fila, 2);
            ruc = (String) m.getValueAt(fila, 3);

            id_cliente_global = id_empresatransporte;
            mostrar_combo_empresatransporte_buscar(razon_social);
            txt_partida.setText(direccion);
            txt_ruc_transporte.setText(ruc);

            txt_buscar_empresatransporte.setText("");
            dialog_buscar_empresatransporte.dispose();
        }
    }//GEN-LAST:event_btn_cliente_seleccionar3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        txt_buscar_empresatransporte.setText("");
        dialog_buscar_empresatransporte.dispose();

        dialog_crear_empresatransporte.setSize(429, 350);
        dialog_crear_empresatransporte.setLocationRelativeTo(guia);
        dialog_crear_empresatransporte.setModal(true);
        dialog_crear_empresatransporte.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void tabla_empresatransporte_buscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_empresatransporte_buscarKeyPressed
        int fila = tabla_empresatransporte_buscar.getSelectedRow();
        int id_empresatransporte;
        String razon_social;
        String direccion;
        String ruc;

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            m = (DefaultTableModel) tabla_empresatransporte_buscar.getModel();
            id_empresatransporte = Integer.parseInt((String) m.getValueAt(fila, 0));
            razon_social = (String) m.getValueAt(fila, 1);
            direccion = (String) m.getValueAt(fila, 2);
            ruc = (String) m.getValueAt(fila, 3);

            id_empresatransporte_global = id_empresatransporte;
            System.out.println("El id_empresatransporte_global es: " + id_empresatransporte_global);

            mostrar_combo_empresatransporte_buscar(razon_social);
            txt_partida.setText(direccion);
            txt_ruc_transporte.setText(ruc);

            txt_buscar_empresatransporte.setText("");
            dialog_buscar_empresatransporte.dispose();
        }
    }//GEN-LAST:event_tabla_empresatransporte_buscarKeyPressed

    private void txt_buscar_conductorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_conductorKeyReleased
        ResultSet r;
        String bus = txt_buscar_conductor.getText();

        try {
            System.out.println("select id_conductor, nombre, apellido, dni, nro_licencia from tconductor where (nombre like '%" + bus + "%'  or apellido like '%" + bus + "%' or dni like '%" + bus + "%' or direccion like '%" + bus + "%' or telefono like '%" + bus + "%' or celular like '%" + bus + "%' or nro_licencia like '%" + bus + "%') and id_empresa = '" + id_empresa_index + "' and nro_licencia is not null order by nombre asc");
            r = sentencia.executeQuery("select id_conductor, nombre, apellido, dni, nro_licencia from tconductor where (nombre like '%" + bus + "%'  or apellido like '%" + bus + "%' or dni like '%" + bus + "%' or direccion like '%" + bus + "%' or telefono like '%" + bus + "%' or celular like '%" + bus + "%' or nro_licencia like '%" + bus + "%') and id_empresa = '" + id_empresa_index + "' and nro_licencia is not null order by nombre asc");

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Nombre");
            modelo.addColumn("Apellido");
            modelo.addColumn("DNI");
            modelo.addColumn("Nro Licencia");

            String fila[] = new String[5];
            while (r.next()) {
                fila[0] = r.getString("id_conductor").trim();
                fila[1] = r.getString("nombre").trim();
                fila[2] = r.getString("apellido").trim();
                fila[3] = r.getString("dni").trim();
                fila[4] = r.getString("nro_licencia").trim();
                tabla_conductor_buscar.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_conductor_buscar.setModel(modelo);
            TableColumn columna1 = tabla_conductor_buscar.getColumn("");
            columna1.setPreferredWidth(1);
            TableColumn columna2 = tabla_conductor_buscar.getColumn("Nombre");
            columna2.setPreferredWidth(150);
            TableColumn columna3 = tabla_conductor_buscar.getColumn("Apellido");
            columna3.setPreferredWidth(150);
            TableColumn columna4 = tabla_conductor_buscar.getColumn("DNI");
            columna4.setPreferredWidth(150);
            TableColumn columna5 = tabla_conductor_buscar.getColumn("Nro Licencia");
            columna5.setPreferredWidth(150);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Conductor - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txt_buscar_conductorKeyReleased

    private void btn_conductor_cancelar_busquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_conductor_cancelar_busquedaActionPerformed
        txt_buscar_conductor.setText("");
        dialog_buscar_conductor.dispose();
    }//GEN-LAST:event_btn_conductor_cancelar_busquedaActionPerformed

    private void btn_cliente_seleccionar4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_seleccionar4ActionPerformed
        int fila;
        int id_conductor;
        String nombre;

        fila = tabla_conductor_buscar.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un Conductor");
        } else {
            m = (DefaultTableModel) tabla_conductor_buscar.getModel();
            id_conductor = Integer.parseInt((String) m.getValueAt(fila, 0));
            nombre = (String) m.getValueAt(fila, 1);

            id_conductor_global = id_conductor;
            mostrar_combo_conductor_buscar(nombre);

            txt_buscar_conductor.setText("");
            dialog_buscar_conductor.dispose();
        }
    }//GEN-LAST:event_btn_cliente_seleccionar4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        txt_buscar_conductor.setText("");
        dialog_buscar_conductor.dispose();

        dialog_crear_conductor.setSize(429, 450);
        dialog_crear_conductor.setLocationRelativeTo(guia);
        dialog_crear_conductor.setModal(true);
        dialog_crear_conductor.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void tabla_conductor_buscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_conductor_buscarKeyPressed
        int fila = tabla_conductor_buscar.getSelectedRow();
        int id_conductor;
        String nombre;

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            m = (DefaultTableModel) tabla_conductor_buscar.getModel();
            id_conductor = Integer.parseInt((String) m.getValueAt(fila, 0));
            nombre = (String) m.getValueAt(fila, 1);

            id_conductor_global = id_conductor;
            System.out.println("El id_conductor_global es: " + id_conductor_global);

            mostrar_combo_conductor_buscar(nombre);

            txt_buscar_conductor.setText("");
            dialog_buscar_conductor.dispose();
        }
    }//GEN-LAST:event_tabla_conductor_buscarKeyPressed

    private void btn_crea_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_crea_clienteActionPerformed
        System.out.println("\npresionó boton Guardar_Cliente");
        System.out.println("========================");

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

    private void cbo_clienteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_clienteItemStateChanged
        System.out.println("\nSe ejecuta la busqueda del cliente");


        if (cbo_cliente.getSelectedItem().toString().trim().length() >= 1) {
            String razon_social = cbo_cliente.getSelectedItem().toString().trim();
            String id_cliente;
            String direccion;
            String ruc;

            try {
                ResultSet r = sentencia.executeQuery("select id_cliente, direccion, ruc from tcliente where razon_social='" + razon_social + "'");
                while (r.next()) {
                    id_cliente = r.getString("id_cliente").trim();
                    System.out.println("El id_cliente es: " + id_cliente);

                    direccion = r.getString("direccion").trim();
                    System.out.println("La direcion es: " + direccion);

                    ruc = r.getString("ruc").trim();
                    System.out.println("El RUC es: " + ruc);

                    System.out.println("El id_cliente_global es: " + id_cliente);
                    id_cliente_global = Integer.parseInt(id_cliente);
                    txt_llegada.setText(direccion);
                    txt_ruc_cliente.setText(ruc);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el los Datos del Cliente", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            id_cliente_global = 0;
            txt_llegada.setText("");
            txt_ruc_cliente.setText("");
        }
    }//GEN-LAST:event_cbo_clienteItemStateChanged

    private void cbo_empresatransporteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_empresatransporteItemStateChanged
        System.out.println("\nSe ejecuta la busqueda del Empresa de Transporte");

        if (cbo_empresatransporte.getSelectedItem().toString().trim().length() > 0) {
            String razon_social = cbo_empresatransporte.getSelectedItem().toString().trim();
            String id_empresatransporte;
            String direccion;
            String ruc;

            try {
                ResultSet r = sentencia.executeQuery("select id_empresatransporte, direccion, ruc from tempresatransporte where razon_social='" + razon_social + "' and id_empresa='" + id_empresa_index + "'");
                while (r.next()) {
                    id_empresatransporte = r.getString("id_empresatransporte").trim();
                    System.out.println("El id_empresatransporte es: " + id_empresatransporte);

                    direccion = r.getString("direccion").trim();
                    System.out.println("La direcion es: " + direccion);

                    ruc = r.getString("ruc").trim();
                    System.out.println("El RUC es: " + ruc);

                    System.out.println("El id_empresatransporte_global es: " + id_empresatransporte);
                    id_empresatransporte_global = Integer.parseInt(id_empresatransporte);
                    txt_partida.setText(direccion);
                    txt_ruc_transporte.setText(ruc);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el los Datos de la Empresa de Transporte", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            id_empresatransporte_global = 0;
            txt_partida.setText("");
            txt_ruc_transporte.setText("");
        }
    }//GEN-LAST:event_cbo_empresatransporteItemStateChanged

    private void btn_guardar_empresatransporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardar_empresatransporteActionPerformed
        System.out.println("\npresionó boton Guardar_EmpresaTransporte");
        System.out.println("==========================================");

        System.out.println("capturando datos ingresados");

        String razon_social = txt_razon_social_empresatransporte_crear.getText().trim();
        String ruc = txt_ruc_empresatransporte_crear.getText().trim();
        String direccion = txt_direccion_empresatransporte_crear.getText().trim();
        String telefono = txt_telefono_empresatransporte_crear.getText().trim();
        String celular = txt_celular_empresatransporte_crear.getText().trim();
        String correo = txt_correo_empresatransporte_crear.getText().trim();

        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;

        if (txt_razon_social_empresatransporte_crear.getText().length() == 0 || txt_ruc_empresatransporte_crear.getText().length() == 0 || txt_direccion_empresatransporte_crear.getText().length() == 0) {
            System.out.println("\nFalta ingresar los campos Razon Social, R.U.C. y Dirección");
            JOptionPane.showMessageDialog(null, "Los Razon Social, R.U.C. y Dirección son necesarios \n Por favor ingrese estos campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            if (txt_ruc_empresatransporte_crear.getText().length() != 11) {
                System.out.println("\n el R.U.C. tiene un tamaño diferente a 11");
                JOptionPane.showMessageDialog(null, "El R.U.C. debe tener 11 digitos.\n Por favor un R.U.C. válido.", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {

                if (correo.length() > 0) {
                    if (!ValidarCorreo(correo)) {
                        System.out.println("\n el correo ingresado no es correcto");
                        JOptionPane.showMessageDialog(null, "El correo ingresado no es correcto.\n Por favor ingrese un Correo válido.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (clase_empresatransporte.razon_social_existente(razon_social, id_empresa) > 0) {
                            System.out.println("La Razon social ya se encuentra registrada.");
                            JOptionPane.showMessageDialog(null, "La Razon Social ya se encuentra Registra.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (clase_empresatransporte.ruc_existente(ruc, id_empresa) > 0) {
                                System.out.println("El R.U.C. ya se encuentra registrado.");
                                JOptionPane.showMessageDialog(null, "El R.U.C. ya se encuentra Registrado.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                            } else {
                                System.out.println("llamamos a la funcion crear_empresatransporte");
                                crear_empresatransporte(razon_social, ruc, direccion, telefono, celular, correo, id_empresa, id_usuario);
                            }
                        }
                    }
                } else {
                    if (clase_empresatransporte.razon_social_existente(razon_social, id_empresa) > 0) {
                        System.out.println("La Razon social ya se encuentra registrada.");
                        JOptionPane.showMessageDialog(null, "La Razon Social ya se encuentra Registra.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (clase_empresatransporte.ruc_existente(ruc, id_empresa) > 0) {
                            System.out.println("El R.U.C. ya se encuentra registrado.");
                            JOptionPane.showMessageDialog(null, "El R.U.C. ya se encuentra Registrado.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            System.out.println("llamamos a la funcion crear_Empresa de transporte");
                            crear_empresatransporte(razon_social, ruc, direccion, telefono, celular, correo, id_empresa, id_usuario);
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_btn_guardar_empresatransporteActionPerformed

    private void btn_cancelar_guardar_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_guardar_detalleActionPerformed
        limpiar_caja_texto_crear_detalle_guia();
        panel_nuevo_detalle.setVisible(false);
        btn_guardar_detalle.setVisible(false);
        btn_nuevo_detalle.setVisible(true);
    }//GEN-LAST:event_btn_cancelar_guardar_detalleActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        System.out.println("Ejecutandose CANCELAR GUIA");
        System.out.println("==========================");

        int id_guia = id_guia_global;
        int id_detalle_guia;
        ResultSet r;
        int respuesta;

        if (band_modificar != 1) {
            //Se ejecuta si estamos ejecutando una creacion    
            respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea CANCELAR la CREACION de esta GUIA?", "Cancelar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {

                try {
                    r = sentencia.executeQuery("select id_detalle_guia from tdetalle_guia where id_guia = '" + id_guia + "'");
                    while (r.next()) {
                        id_detalle_guia = Integer.parseInt(r.getString("id_detalle_guia"));
                        clase_guia_detalle.eliminar(id_detalle_guia);
                    }

                    int id_factura;
                    id_factura = id_factura_global;
                    int id_guia_desvincular = 0;

                    System.out.println("\nProcedo de desvinculacion de Factura");
                    System.out.println("id_factura: " + id_factura);
                    System.out.println("id_guia: " + id_guia);


                    //Procedemos a modificar el campo id_guia de la factura
                    modificar_vinculo_factura(id_factura, id_guia_desvincular, "desvincular");

                    //inicializando el id_factura
                    id_factura_global = 0;

                    if (clase_guia.eliminar(id_guia)) {

                        System.out.println("La GUIA se logró cancelar exitosamente!");
                        operaciones_postModificacion();

                        JOptionPane.showMessageDialog(null, "La Guia se Cancelo exitosamente");

                    } else {
                        JOptionPane.showMessageDialog(null, "Ocurrio al CANCELAR la GUIA.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos de la GUIA", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            //Se ejecuta si estamos ejecutando una modificacion    
            respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea CANCELAR la MODIFICACIÓN de esta FACTURA?", "Cancelar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                System.out.println("La GUIA se logró cancelar exitosamente!");
                operaciones_postModificacion();
                JOptionPane.showMessageDialog(null, "La Guia se Cancelo exitosamente");
            }
        }
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void txt_buscar_vehiculoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_vehiculoKeyTyped
        ResultSet r;
        String bus = txt_buscar_vehiculo.getText();

        try {
            System.out.println("select id_vehiculo, marca, placa, modelo, nro_inscripcion from tvehiculo where (marca like '%" + bus + "%'  or placa like '%" + bus + "%' or modelo like '%" + bus + "%' or nro_inscripcion like '%" + bus + "%') and id_empresa = '" + id_empresa_index + "' order by marca asc");
            r = sentencia.executeQuery("select id_vehiculo, marca, placa, modelo, nro_inscripcion from tvehiculo where (marca like '%" + bus + "%'  or placa like '%" + bus + "%' or modelo like '%" + bus + "%' or nro_inscripcion like '%" + bus + "%') and id_empresa = '" + id_empresa_index + "' order by marca asc");
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Marca");
            modelo.addColumn("Placa");
            modelo.addColumn("Modelo");
            modelo.addColumn("Nro Inscripcion");

            String fila[] = new String[5];
            while (r.next()) {
                fila[0] = r.getString("id_vehiculo").trim();
                fila[1] = r.getString("marca").trim();
                fila[2] = r.getString("placa").trim();
                fila[3] = r.getString("modelo").trim();
                fila[4] = r.getString("nro_inscripcion").trim();
                tabla_vehiculo_buscar.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_vehiculo_buscar.setModel(modelo);
            TableColumn columna1 = tabla_vehiculo_buscar.getColumn("");
            columna1.setPreferredWidth(1);
            TableColumn columna2 = tabla_vehiculo_buscar.getColumn("Marca");
            columna2.setPreferredWidth(150);
            TableColumn columna3 = tabla_vehiculo_buscar.getColumn("Placa");
            columna3.setPreferredWidth(150);
            TableColumn columna4 = tabla_vehiculo_buscar.getColumn("Modelo");
            columna4.setPreferredWidth(150);
            TableColumn columna5 = tabla_vehiculo_buscar.getColumn("Nro Inscripcion");
            columna5.setPreferredWidth(150);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla vehiculo - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txt_buscar_vehiculoKeyTyped

    private void btn_cancelar_cliente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_cliente1ActionPerformed
        System.out.println("limpiar cajas de texto");
        limpiar_caja_texto_crear_vehiculo();

        System.out.println("actualizamos tabla vehiculo");
        mostrar_tabla_vehiculo();

        dialog_crear_vehiculo.dispose();
    }//GEN-LAST:event_btn_cancelar_cliente1ActionPerformed

    private void btn_crea_cliente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_crea_cliente1ActionPerformed
        System.out.println("\npresionó boton Guardar Vehiculo");
        System.out.println("=================================");

        System.out.println("capturando datos ingresados");

        String marca = txt_marca_vehiculo_crear.getText().trim();
        String placa = txt_placa_vehiculo_crear.getText().trim();
        String modelo = txt_modelo_vehiculo_crear.getText().trim();
        String nro_inscripcion = txt_nro_inscripcion_vehiculo_crear.getText().trim();
        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;

        if (txt_marca_vehiculo_crear.getText().length() == 0 || txt_placa_vehiculo_crear.getText().length() == 0 || txt_modelo_vehiculo_crear.getText().length() == 0 || txt_nro_inscripcion_vehiculo_crear.getText().length() == 0) {
            System.out.println("\nFalta ingresar los campos Marca, Placa, Modelo y Numero de Inscripcion");
            JOptionPane.showMessageDialog(null, "Los Campos Marca, Placa, Modelo y Numero de Inscripcion son necesarios \n Por favor ingrese estos campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {

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
        }
    }//GEN-LAST:event_btn_crea_cliente1ActionPerformed

    private void txt_marca_vehiculo_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_marca_vehiculo_crearKeyTyped
        JTextField caja = txt_marca_vehiculo_crear;
        int limite = 100;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_marca_vehiculo_crearKeyTyped

    private void txt_placa_vehiculo_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_placa_vehiculo_crearKeyTyped
        JTextField caja = txt_placa_vehiculo_crear;
        int limite = 30;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_placa_vehiculo_crearKeyTyped

    private void txt_modelo_vehiculo_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_modelo_vehiculo_crearKeyTyped
        JTextField caja = txt_modelo_vehiculo_crear;
        int limite = 30;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_modelo_vehiculo_crearKeyTyped

    private void txt_nro_inscripcion_vehiculo_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nro_inscripcion_vehiculo_crearKeyTyped
        JTextField caja = txt_nro_inscripcion_vehiculo_crear;
        int limite = 50;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_nro_inscripcion_vehiculo_crearKeyTyped

    private void btn_buscar_conductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_conductorActionPerformed
        mostrar_tabla_conductor();
        dialog_buscar_conductor.setSize(700, 400);
        dialog_buscar_conductor.setLocationRelativeTo(guia);
        dialog_buscar_conductor.setModal(true);
        dialog_buscar_conductor.setVisible(true);
    }//GEN-LAST:event_btn_buscar_conductorActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        System.out.println("\npresionó boton Guardar Conductor");
        System.out.println("==================================");

        System.out.println("capturando datos ingresados");

        String nombre = txt_nombre_crear_conductor.getText().trim();
        String apellido = txt_apellido_crear_conductor.getText().trim();
        String dni = txt_dni_crear_conductor.getText().trim();
        String direccion = txt_direcion_crear_conductor.getText().trim();
        String telefono = txt_telefono_crear_conductor.getText().trim();
        String celular = txt_celular_crear_conductor.getText().trim();
        String nro_licencia = txt_numero_licencia_crear_conductor.getText().trim();
        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;

        if (txt_nombre_crear_conductor.getText().length() == 0 || txt_apellido_crear_conductor.getText().length() == 0 || txt_dni_crear_conductor.getText().length() == 0 || txt_direcion_crear_conductor.getText().length() == 0) {
            System.out.println("\nFalta ingresar los campos Nombre, Apellido, DNI y Dirección");
            JOptionPane.showMessageDialog(null, "Los campos: \nNombre, \nApellido, \nDNI y \nDirección son necesarios \n\n Por favor ingrese estos campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            if (txt_dni_crear_conductor.getText().length() != 8) {
                System.out.println("\n el dni tiene un tamaño diferente a 8");
                JOptionPane.showMessageDialog(null, "El D.N.I. debe tener 8 digitos.\n Por favor ingrese un D.N.I. válido.", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {


                if (clase_conductor.dni_existente(dni, id_empresa) > 0) {
                    System.out.println("El DNI ya se encuentra registrado.");
                    JOptionPane.showMessageDialog(null, "El D.N.I. ya se encuentra Registrdo.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {

                    if (nro_licencia.length() == 0) {
                        System.out.println("llamamos a la funcion crear_Conductor");
                        crear_conductor(nombre, apellido, dni, direccion, telefono, celular, nro_licencia, id_empresa, id_usuario);
                    } else {
                        if (clase_conductor.nro_licencia_existente(nro_licencia, id_empresa) > 0) {
                            System.out.println("El Número de Licencia ya se encuentra registrado.");
                            JOptionPane.showMessageDialog(null, "El Número de Licencia ya se encuentra Registrado.\n Por favor ingrese otro.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            System.out.println("llamamos a la funcion crear_Conductor");
                            crear_conductor(nombre, apellido, dni, direccion, telefono, celular, nro_licencia, id_empresa, id_usuario);
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void txt_unidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_unidadKeyTyped
        JTextField caja = txt_unidad;
        int limite = 10;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_unidadKeyTyped

    private void txt_cantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cantidadKeyTyped
        JTextField caja = txt_cantidad;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_cantidadKeyTyped

    private void txt_pesoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pesoKeyTyped
        JTextField caja = txt_peso;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_pesoKeyTyped

    private void txt_detalleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_detalleKeyTyped
        JTextArea caja = txt_detalle;
        int limite = 1000;
        tamaño_de_caja_jtextarea(caja, evt, limite);
    }//GEN-LAST:event_txt_detalleKeyTyped

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarActionPerformed
        System.out.println("\nEliminar Detalle Guia");
        System.out.println("==========================");

        int fila;
        int id_detalle_guia;
        int respuesta;
        try {
            fila = tabla_detalle.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar el registro a borrar");
            } else {
                respuesta = JOptionPane.showConfirmDialog(null, "¿Desea eliminar?", "Eliminar", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    m = (DefaultTableModel) tabla_detalle.getModel();
                    id_detalle_guia = Integer.parseInt((String) m.getValueAt(fila, 0));
                    System.out.println("el id_detalle_guia que se eliminara es: " + id_detalle_guia);
                    if (clase_guia_detalle.eliminar(id_detalle_guia)) {
                        System.out.println("el detalle se logró eliminar exitosamente!");

                        JOptionPane.showMessageDialog(null, "El Detalle se Eliminó exitosamente!");

                        System.out.println("actualizamos tabla detalle guia");
                        mostrar_tabla_guia_detalle(id_guia_global);
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
        int id_guia = id_guia_global;
        String numero_guia = txt_numero_guia.getText().trim();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dia = txt_fecha.getDate();
        String fecha = sdf.format(dia);

        String motivo_traslado = "";
        if (cbo_motivotraslado.getSelectedItem().toString().equals("OTROS")) {

            if (txt_otromotivo.getText().trim().length() < 1) {
                band = 7;
            } else {
                motivo_traslado = txt_otromotivo.getText().trim();
            }

        } else {
            motivo_traslado = cbo_motivotraslado.getSelectedItem().toString().trim();
        }

        String punto_partida = "";
        if (txt_partida.getText().length() < 1) {
            band = 1;
        } else {
            punto_partida = txt_partida.getText().trim();
        }

        String punto_llegada = "";
        if (txt_llegada.getText().length() < 1) {
            band = 2;
        } else {
            punto_llegada = txt_llegada.getText().trim();
        }

        int id_documento = id_documento_global;

        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;

        int id_cliente = 0;
        if (txt_ruc_cliente.getText().length() < 1) {
            band = 3;
        } else {
            campocapturar = "id_cliente";
            tabla = "tcliente";
            campocondicion = "ruc";
//            id_cliente = CapturarId(campocapturar, tabla, campocondicion, txt_ruc_cliente.getText().trim(), id_empresa);
            id_cliente = CapturarId(campocapturar, tabla, campocondicion, txt_ruc_cliente.getText().trim());
            System.out.println("id capturado: " + id_cliente);
        }

        int id_factura = 0;
        if (txt_numerofactura.getText().length() > 0) {
            id_factura = id_factura_global;
        }

        int id_vehiculo = 0;
        if (txt_marca.getText().length() < 1) {
            band = 4;
        } else {
            id_vehiculo = id_vehiculo_global;
        }

        int id_empresatransporte = 0;
        if (txt_ruc_transporte.getText().length() < 1) {
            band = 5;
        } else {
            campocapturar = "id_empresatransporte";
            tabla = "tempresatransporte";
            campocondicion = "ruc";
//            id_empresatransporte = CapturarId(campocapturar, tabla, campocondicion, txt_ruc_transporte.getText().trim(), id_empresa);
            id_empresatransporte = CapturarId(campocapturar, tabla, campocondicion, txt_ruc_transporte.getText().trim());
            System.out.println("id capturado: " + id_empresatransporte);
        }

        int id_conductor = 0;
        if (cbo_conductor.getSelectedItem().toString().trim().length() < 1) {
            band = 6;
        } else {
            campocapturar = "id_conductor";
            tabla = "tconductor";
            campocondicion = "Nombre";
            //id_conductor = CapturarId(campocapturar, tabla, campocondicion, cbo_conductor.getSelectedItem().toString().trim(), id_empresa);
            id_conductor = CapturarId(campocapturar, tabla, campocondicion, cbo_conductor.getSelectedItem().toString().trim());
            System.out.println("id capturado: " + id_conductor);
        }

        switch (band) {
            case 1:
                JOptionPane.showMessageDialog(null, "Falta ingresar el campo: PUNTO DE PARTIDA.\n Por favor ingrese el campo en mención.", "ERROR: 1", JOptionPane.ERROR_MESSAGE);
                break;

            case 2:
                JOptionPane.showMessageDialog(null, "Falta ingresar el campo: PUNTO DE LLEGADA.\n Por favor ingrese el campo en mención.", "ERROR: 2", JOptionPane.ERROR_MESSAGE);
                break;

            case 3:
                JOptionPane.showMessageDialog(null, "Falta seleccionar el campo: RAZON SOCIAL DEL CLIENTE.\n Por favor seleccione el campo en mención.", "ERROR: 3", JOptionPane.ERROR_MESSAGE);
                break;

            case 4:
                JOptionPane.showMessageDialog(null, "Falta seleccionar el campo: VEHICULO.\n Por favor seleccione el campo en mención.", "ERROR: 4", JOptionPane.ERROR_MESSAGE);
                break;

            case 5:
                JOptionPane.showMessageDialog(null, "Falta seleccionar el campo: RAZON SOCIAL DE LA EMPRESA DE TRANSPORTES.\n Por favor seleccione el campo en mención.", "ERROR: 5", JOptionPane.ERROR_MESSAGE);
                break;

            case 6:
                JOptionPane.showMessageDialog(null, "Falta seleccionar el campo: CONDUCTOR.\n Por favor seleccione el campo en mención.", "ERROR: 6", JOptionPane.ERROR_MESSAGE);
                break;

            case 7:
                JOptionPane.showMessageDialog(null, "Falta ingresar el campo: MOTIVO DE TRASLADO - OTROS.\n Por favor seleccione el campo en mención.", "ERROR: 7", JOptionPane.ERROR_MESSAGE);
                break;

            case 0:
                System.out.println("\nSe presionó el boton GUARDAR/MODIFICAR GUIA");
                System.out.println("=============================================");
                System.out.println("id_guia             : " + id_guia);
                System.out.println("numero_guia         : " + numero_guia);
                System.out.println("fecha               : " + fecha);
                System.out.println("motivo_traslado     : " + motivo_traslado);
                System.out.println("punto_partida       : " + punto_partida);
                System.out.println("punto_llegada       : " + punto_llegada);
                System.out.println("id_documento        : " + id_documento);
                System.out.println("id_cliente          : " + id_cliente);
                System.out.println("id_factura          : " + id_factura);
                System.out.println("id_vehiculo         : " + id_vehiculo);
                System.out.println("id_empresatransporte: " + id_empresatransporte);
                System.out.println("id_conductor        : " + id_conductor);
                System.out.println("id_empresa          : " + id_empresa);
                System.out.println("id_usuario          : " + id_usuario);

                //Procedemos a modificar el campo id_guia de la factura
                modificar_vinculo_factura(id_factura, id_guia_global, "vincular");

                System.out.println("Se llama a la funcion Modificar_Guia");
                modificar_Guia(id_guia, numero_guia, fecha, motivo_traslado, punto_partida, punto_llegada, id_documento, id_cliente, id_factura, id_vehiculo, id_empresatransporte, id_conductor, id_empresa, id_usuario);

                break;
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void tabla_generalMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_generalMouseReleased
        if (band_index == 0) {
            int fila;
            int id_guia;
            fila = tabla_general.getSelectedRow();
            m = (DefaultTableModel) tabla_general.getModel();
            id_guia = Integer.parseInt((String) m.getValueAt(fila, 1));
            mostrar_datos_guia(id_guia);
        }
    }//GEN-LAST:event_tabla_generalMouseReleased

    private void tabla_generalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_generalKeyReleased
        if (band_index == 0) {
            int fila;
            int id_guia;
            fila = tabla_general.getSelectedRow();
            m = (DefaultTableModel) tabla_general.getModel();
            id_guia = Integer.parseInt((String) m.getValueAt(fila, 1));
            mostrar_datos_guia(id_guia);
        }
    }//GEN-LAST:event_tabla_generalKeyReleased

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
        int fil = tabla_general.getSelectedRow();
        if (fil == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una Guia para Modificar");
            band_index = 1;
        } else {
            System.out.println("\nModifar Guia");
            System.out.println("=================");

            System.out.println("inicializar la bandera crear en uno");
            band_index = 1;
            crear0_modificar1_guia = 0;
            band_mantenimiento_guia_detalle = 0;
            band_modificar = 1; //Indicamos que es una modificacion y que se tiene que activar el boton cancelar y no eliminara la factura

            System.out.println("cambiando el titulo");
            lbl_titulo.setText("Modificar Guía");

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
            lbl_estado.setVisible(false);

            if (id_factura_global > 0) {

                //Ocultar botones
                btn_nuevo_cliente.setVisible(false);
                btn_buscar_cliente.setVisible(false);
                btn_nuevo_detalle.setVisible(false);

                //mostrar boton
                btn_desvincular_factura.setVisible(true);

                //Desactivar cajas y combos
                cbo_cliente.setEnabled(false);
                cbo_cliente.setEditable(false);
            } else {
                btn_desvincular_factura.setVisible(false);
            }


            System.out.println("desactivar barra de herramientas");
            activar_barra_herramientas("desactivar");
        }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void btn_anularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_anularActionPerformed
        System.out.println("Ejecutandose ANULAR GUIA");
        System.out.println("===========================");

        int fila = tabla_general.getSelectedRow();
        int id_guia = Integer.parseInt((String) m.getValueAt(fila, 1));
        ResultSet r;

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una GUIA para Anular");
        } else {
            int respuesta;
            respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea ANULAR esta GUIA?", "Anular", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                System.out.println("el id_guia que se anulará es: " + id_guia);

                if (id_factura_global > 0) {
                    String impreso = "";
                    try {
                        r = sentencia.executeQuery("select f.impreso from tfactura f where f.id_factura = '" + id_factura_global + "' and f.id_empresa = '" + id_empresa_index + "'");

                        while (r.next()) {
                            impreso = r.getString("impreso").trim();
                        }

                        if (impreso.equals("0")) {
                            int desid_guia = 0;
                            if (clase_factura.vincularGuia(id_factura_global, desid_guia)) {
                                System.out.println("La guia se logró desvincular exitosamente!");

                                if (clase_guia.anular(id_guia)) {
                                    System.out.println("La guia se logró anular exitosamente!");
                                    System.out.println("ocultar el panel de detalle de datos");
                                    Panel_detalle.setVisible(false);
                                    JOptionPane.showMessageDialog(null, "La GUIA se anuló exitosamente!");
                                    System.out.println("actualizamos tabla GUIA");
                                    mostrar_tabla_guia("");
                                    band_index = 0;

                                } else {
                                    JOptionPane.showMessageDialog(null, "Ocurrio al ANULAR la GUIA.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                                }

                            } else {
                                JOptionPane.showMessageDialog(null, "Ocurrio al desvincularla Factura con la guia", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }

                        } else {
                            if (clase_guia.anular(id_guia)) {
                                System.out.println("La guia se logró anular exitosamente!");
                                System.out.println("ocultar el panel de detalle de datos");
                                Panel_detalle.setVisible(false);
                                JOptionPane.showMessageDialog(null, "La GUIA se anuló exitosamente!");
                                System.out.println("actualizamos tabla GUIA");
                                mostrar_tabla_guia("");
                                band_index = 0;

                            } else {
                                JOptionPane.showMessageDialog(null, "Ocurrio al ANULAR la GUIA.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Ocurrio al obtener el estado Impreso de la Factura: " + id_factura_global + "" + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    if (clase_guia.anular(id_guia)) {
                        System.out.println("La guia se logró anular exitosamente!");
                        System.out.println("ocultar el panel de detalle de datos");
                        Panel_detalle.setVisible(false);
                        JOptionPane.showMessageDialog(null, "La GUIA se anuló exitosamente!");
                        System.out.println("actualizamos tabla GUIA");
                        mostrar_tabla_guia("");
                        band_index = 0;

                    } else {
                        JOptionPane.showMessageDialog(null, "Ocurrio al ANULAR la GUIA.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        }

    }//GEN-LAST:event_btn_anularActionPerformed

    private void btn_imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imprimirActionPerformed
        String numero_factura = "";
        String tipo_comprobante = "";
        if (txt_seriefactura.getText().trim().length() > 0) {
            tipo_comprobante = "FACTURA";
            numero_factura = "" + txt_seriefactura.getText().trim() + " - " + txt_numerofactura.getText().trim();
        }

        if (clase_guia.imprimir(id_guia_global)) {
            try {
                String rutaInforme = "reportes\\Guia.jasper";
                Map parametros = new HashMap();
                parametros.put("id_guia", id_guia_global);
                parametros.put("numero_factura", numero_factura);
                parametros.put("tipo_comprobante", tipo_comprobante);
                JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
                JasperViewer view = new JasperViewer(print, false);
                view.setTitle("GUIA -  SERIE: " + txt_serie.getText() + " - NUMERO: " + txt_numero_guia.getText());
                view.setVisible(true);
                btn_modificar.setEnabled(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: " + e, "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al guarar la Impresion", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_imprimirActionPerformed

    private void btn_desvincular_facturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_desvincular_facturaActionPerformed
        int id_factura;
        int id_guia = 0;


        int respuesta;
        respuesta = JOptionPane.showConfirmDialog(null, "Usted está desvinculando la Factura: " + txt_seriefactura.getText().trim() + " - " + txt_numerofactura.getText().trim() + ", tenga en cuenta estas consideraciones:. \n"
                + "\n1.- Se procederá a eliminar los detalles de la Factura registrados anteriormente"
                + "\nBajo estas consideraciones,"
                + "\n\n¿Desea desvincular FACTURA seleccionada?", "ATENCIÓN", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {

            id_factura = id_factura_global;

            eliminar_detalle_guia(id_guia_global);

            //Procedemos a modificar el campo id_guia de la factura
            modificar_vinculo_factura(id_factura, id_guia, "desvincular");

            //inicializando el id_factura
            id_factura_global = 0;

            //Ocultar botones
            btn_nuevo_cliente.setVisible(true);
            btn_buscar_cliente.setVisible(true);
            btn_nuevo_detalle.setVisible(true);

            //mostrar boton
            btn_desvincular_factura.setVisible(false);

            //Desactivar cajas y combos
            cbo_cliente.setEnabled(true);
            cbo_cliente.setEditable(true);

            mostrar_tabla_guia_detalle(id_guia);

        }

    }//GEN-LAST:event_btn_desvincular_facturaActionPerformed

    private void txt_buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String bus = txt_buscar.getText();
            String consulta;

            if (bus.equals("ANULADO")) {
                bus = "1";
                System.out.println("select g.id_guia, g.numero_guia, convert(varchar, g.fecha, 103) as fecha, anulado from tguia g, tcliente c, tvehiculo v, tempresatransporte et, tconductor co where(g.numero_guia like '%" + bus + "%' or g.motivo_traslado like '%" + bus + "%' or g.punto_partida like '%" + bus + "%' or g.punto_llegada like '%" + bus + "%' or g.punto_llegada like '%" + bus + "%' or c.razon_social like '%" + bus + "%' or c.ruc like '%" + bus + "%' or v.marca like '%" + bus + "%' or v.placa like '%" + bus + "%' or v.modelo like '%" + bus + "%' or v.nro_inscripcion like '%" + bus + "%' or et.razon_social like '%" + bus + "%' or et.ruc like '%" + bus + "%' or co.nombre like '%" + bus + "%') and g.id_cliente = c.id_cliente and g.id_vehiculo = v.id_vehiculo and g.id_empresatransporte = et.id_empresatransporte and g.id_conductor = co.id_conductor and g.anulado = '" + bus + "' and g.numero_guia is not null and g.id_empresa = '" + id_empresa_index + "' order by numero_guia desc");
                consulta = "select g.id_guia, g.numero_guia, convert(varchar, g.fecha, 103) as fecha, anulado from tguia g, tcliente c, tvehiculo v, tempresatransporte et, tconductor co where(g.numero_guia like '%" + bus + "%' or g.motivo_traslado like '%" + bus + "%' or g.punto_partida like '%" + bus + "%' or g.punto_llegada like '%" + bus + "%' or g.punto_llegada like '%" + bus + "%' or c.razon_social like '%" + bus + "%' or c.ruc like '%" + bus + "%' or v.marca like '%" + bus + "%' or v.placa like '%" + bus + "%' or v.modelo like '%" + bus + "%' or v.nro_inscripcion like '%" + bus + "%' or et.razon_social like '%" + bus + "%' or et.ruc like '%" + bus + "%' or co.nombre like '%" + bus + "%') and g.id_cliente = c.id_cliente and g.id_vehiculo = v.id_vehiculo and g.id_empresatransporte = et.id_empresatransporte and g.id_conductor = co.id_conductor and g.anulado = '" + bus + "' and g.numero_guia is not null and g.id_empresa = '" + id_empresa_index + "' order by numero_guia desc";
                mostrar_tabla_guia(consulta);
            } else {
                System.out.println("select g.id_guia, g.numero_guia, convert(varchar, g.fecha, 103) as fecha, anulado from tguia g, tcliente c, tvehiculo v, tempresatransporte et, tconductor co where(g.numero_guia like '%" + bus + "%' or g.motivo_traslado like '%" + bus + "%' or g.punto_partida like '%" + bus + "%' or g.punto_llegada like '%" + bus + "%' or g.punto_llegada like '%" + bus + "%' or c.razon_social like '%" + bus + "%' or c.ruc like '%" + bus + "%' or v.marca like '%" + bus + "%' or v.placa like '%" + bus + "%' or v.modelo like '%" + bus + "%' or v.nro_inscripcion like '%" + bus + "%' or et.razon_social like '%" + bus + "%' or et.ruc like '%" + bus + "%' or co.nombre like '%" + bus + "%') and g.id_cliente = c.id_cliente and g.id_vehiculo = v.id_vehiculo and g.id_empresatransporte = et.id_empresatransporte and g.id_conductor = co.id_conductor and g.numero_guia is not null and g.id_empresa = '" + id_empresa_index + "' order by numero_guia desc");
                consulta = "select g.id_guia, g.numero_guia, convert(varchar, g.fecha, 103) as fecha, anulado from tguia g, tcliente c, tvehiculo v, tempresatransporte et, tconductor co where(g.numero_guia like '%" + bus + "%' or g.motivo_traslado like '%" + bus + "%' or g.punto_partida like '%" + bus + "%' or g.punto_llegada like '%" + bus + "%' or g.punto_llegada like '%" + bus + "%' or c.razon_social like '%" + bus + "%' or c.ruc like '%" + bus + "%' or v.marca like '%" + bus + "%' or v.placa like '%" + bus + "%' or v.modelo like '%" + bus + "%' or v.nro_inscripcion like '%" + bus + "%' or et.razon_social like '%" + bus + "%' or et.ruc like '%" + bus + "%' or co.nombre like '%" + bus + "%') and g.id_cliente = c.id_cliente and g.id_vehiculo = v.id_vehiculo and g.id_empresatransporte = et.id_empresatransporte and g.id_conductor = co.id_conductor and g.numero_guia is not null and g.id_empresa = '" + id_empresa_index + "' order by numero_guia desc";
                mostrar_tabla_guia(consulta);

            }
        }
    }//GEN-LAST:event_txt_buscarKeyReleased

    private void btn_vista_previaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vista_previaActionPerformed
        String numero_factura = "";
        String tipo_comprobante = "";
        if (txt_seriefactura.getText().trim().length() > 0) {
            tipo_comprobante = "FACTURA";
            numero_factura = "" + txt_seriefactura.getText().trim() + " - " + txt_numerofactura.getText().trim();
        }

        try {
            String rutaInforme = "reportes\\Guia_vista_previa.jasper";
            Map parametros = new HashMap();
            parametros.put("id_guia", id_guia_global);
            parametros.put("numero_factura", numero_factura);
            parametros.put("tipo_comprobante", tipo_comprobante);
            JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
            JasperViewer view = new JasperViewer(print, false);
            view.setTitle("GUIA -  SERIE: " + txt_serie.getText() + " - NUMERO: " + txt_numero_guia.getText());
            view.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: " + e, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_vista_previaActionPerformed

    private void cbo_motivotrasladoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_motivotrasladoItemStateChanged
        if (cbo_motivotraslado.getSelectedItem().equals("OTROS")) {
            txt_otromotivo.setVisible(true);
        } else {
            txt_otromotivo.setVisible(false);
        }
    }//GEN-LAST:event_cbo_motivotrasladoItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Eliminar;
    private javax.swing.JMenuItem Modificar;
    private javax.swing.JPanel Panel_detalle;
    private javax.swing.JToolBar barra_buscar;
    private javax.swing.JButton btn_anular;
    private javax.swing.JButton btn_buscar_cliente;
    private javax.swing.JButton btn_buscar_conductor;
    private javax.swing.JButton btn_buscar_empresatransporte;
    private javax.swing.JButton btn_buscarfactura;
    private javax.swing.JButton btn_buscarvehiculo;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_cancelar_cliente;
    private javax.swing.JButton btn_cancelar_cliente1;
    private javax.swing.JButton btn_cancelar_crear_empresatransporte;
    private javax.swing.JButton btn_cancelar_guardar_detalle;
    private javax.swing.JButton btn_cliente_cancelar_busqueda;
    private javax.swing.JButton btn_cliente_cancelar_busqueda2;
    private javax.swing.JButton btn_cliente_seleccionar;
    private javax.swing.JButton btn_cliente_seleccionar2;
    private javax.swing.JButton btn_cliente_seleccionar3;
    private javax.swing.JButton btn_cliente_seleccionar4;
    private javax.swing.JButton btn_conductor_cancelar_busqueda;
    private javax.swing.JButton btn_crea_cliente;
    private javax.swing.JButton btn_crea_cliente1;
    private javax.swing.JButton btn_creacion;
    private javax.swing.JButton btn_desvincular_factura;
    private javax.swing.JButton btn_empresatransporte_cancelar_busqueda;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_guardar_detalle;
    private javax.swing.JButton btn_guardar_empresatransporte;
    private javax.swing.JButton btn_imprimir;
    private javax.swing.JButton btn_modificar;
    private javax.swing.JButton btn_nueva_empresatransporte;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JButton btn_nuevo_cliente;
    private javax.swing.JButton btn_nuevo_conductor;
    private javax.swing.JButton btn_nuevo_detalle;
    private javax.swing.JButton btn_vehiculo_cancelar_busqueda;
    private javax.swing.JButton btn_vehiculo_seleccionar;
    private javax.swing.JButton btn_vista_previa;
    private javax.swing.JComboBox cbo_cliente;
    private javax.swing.JComboBox cbo_conductor;
    private javax.swing.JComboBox cbo_empresatransporte;
    private javax.swing.JComboBox cbo_motivotraslado;
    private javax.swing.JPanel centro;
    private javax.swing.JDialog dialog_buscar_cliente;
    private javax.swing.JDialog dialog_buscar_conductor;
    private javax.swing.JDialog dialog_buscar_empresatransporte;
    private javax.swing.JDialog dialog_buscar_factura;
    private javax.swing.JDialog dialog_buscar_vehiculo;
    private javax.swing.JDialog dialog_crear_cliente;
    private javax.swing.JDialog dialog_crear_conductor;
    private javax.swing.JDialog dialog_crear_empresatransporte;
    private javax.swing.JDialog dialog_crear_vehiculo;
    private javax.swing.JDialog dialog_fecha_creacion;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
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
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
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
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
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
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
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
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JLabel label_buscar;
    private javax.swing.JLabel lbl_estado;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JPopupMenu mantenimiento_tabla_detalle_factura;
    private javax.swing.JPanel norte;
    private javax.swing.JPanel panel_nuevo_detalle;
    private javax.swing.JPanel panel_tabla;
    private javax.swing.ButtonGroup radio_sexo;
    private javax.swing.JTable tabla_cliente_buscar;
    private javax.swing.JTable tabla_conductor_buscar;
    private javax.swing.JTable tabla_detalle;
    private javax.swing.JTable tabla_empresatransporte_buscar;
    private javax.swing.JTable tabla_factura_buscar;
    private javax.swing.JTable tabla_general;
    private javax.swing.JTable tabla_vehiculo_buscar;
    private javax.swing.JTextField txt_apellido_crear_conductor;
    private javax.swing.JTextField txt_buscar;
    private javax.swing.JTextField txt_buscar_cliente;
    private javax.swing.JTextField txt_buscar_conductor;
    private javax.swing.JTextField txt_buscar_empresatransporte;
    private javax.swing.JTextField txt_buscar_factura;
    private javax.swing.JTextField txt_buscar_vehiculo;
    private javax.swing.JTextField txt_cantidad;
    private javax.swing.JTextField txt_celular_cliente_crear;
    private javax.swing.JTextField txt_celular_crear_conductor;
    private javax.swing.JTextField txt_celular_empresatransporte_crear;
    private javax.swing.JTextField txt_correo_cliente_crear;
    private javax.swing.JTextField txt_correo_empresatransporte_crear;
    private javax.swing.JTextArea txt_detalle;
    private javax.swing.JTextField txt_direccion_cliente_crear;
    private javax.swing.JTextField txt_direccion_empresatransporte_crear;
    private javax.swing.JTextField txt_direcion_crear_conductor;
    private javax.swing.JTextField txt_dni_crear_conductor;
    private javax.swing.JLabel txt_estado;
    private javax.swing.JTextField txt_f_creacion;
    private javax.swing.JTextField txt_f_modificacion;
    private org.jdesktop.swingx.JXDatePicker txt_fecha;
    private javax.swing.JTextField txt_llegada;
    private javax.swing.JTextField txt_marca;
    private javax.swing.JTextField txt_marca_vehiculo_crear;
    private javax.swing.JTextField txt_modelo_vehiculo_crear;
    private javax.swing.JTextField txt_nombre_crear_conductor;
    private javax.swing.JTextField txt_nro_inscripcion_vehiculo_crear;
    private javax.swing.JTextField txt_nroinscripcion;
    private javax.swing.JTextField txt_numero_guia;
    private javax.swing.JTextField txt_numero_licencia_crear_conductor;
    private javax.swing.JTextField txt_numerofactura;
    private javax.swing.JTextField txt_otromotivo;
    private javax.swing.JTextField txt_partida;
    private javax.swing.JTextField txt_peso;
    private javax.swing.JTextField txt_placa;
    private javax.swing.JTextField txt_placa_vehiculo_crear;
    private javax.swing.JTextField txt_razon_social_cliente_crear;
    private javax.swing.JTextField txt_razon_social_empresatransporte_crear;
    private javax.swing.JTextField txt_ruc_cliente;
    private javax.swing.JTextField txt_ruc_cliente_crear;
    private javax.swing.JTextField txt_ruc_empresatransporte_crear;
    private javax.swing.JTextField txt_ruc_transporte;
    private javax.swing.JTextField txt_serie;
    private javax.swing.JTextField txt_seriefactura;
    private javax.swing.JTextField txt_telefono_cliente_crear;
    private javax.swing.JTextField txt_telefono_crear_conductor;
    private javax.swing.JTextField txt_telefono_empresatransporte_crear;
    private javax.swing.JTextField txt_unidad;
    private javax.swing.JTextField txt_usuario;
    // End of variables declaration//GEN-END:variables
}
