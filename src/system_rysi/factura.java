package system_rysi;

//Clases
import Clases.RenderFactura;
import Clases.TextAreaEditor;
import Clases.TextAreaRenderer;

import Clases.cCliente;
import Clases.cConvertir_Numero_Letra;
import Clases.cIGV;
import Clases.cDocumentos;
import Clases.cFactura;
import Clases.cFactura_Detalle;
import database.AccesoDB;

//Otros
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class factura extends javax.swing.JPanel {

    private Component factura;
    String controlador_index;
    String DSN_index;
    String user_index;
    String password_index;
    private Connection conexion;
    private Statement sentencia;
    cCliente clase_cliente = new cCliente();
    cIGV clase_igv = new cIGV();
    cDocumentos clase_documento = new cDocumentos();
    cFactura clase_factura = new cFactura();
    cFactura_Detalle clase_factura_detalle = new cFactura_Detalle();
    cConvertir_Numero_Letra clase_numero_letra = new cConvertir_Numero_Letra();
    DefaultTableModel m;
    int band_index = 0; //sive para saber si estamos creando o mostrando la informacion de la factura, para desactivar dunciones de visualizacion de un nuevo detalle
    int band_anulado;
    int band_impreso;
    int band_pagado;
    int band_cbo_cliente = 0;
    int band_mantenimiento_factura_detalle = 0;
    int crear0_modificar1_factura = 0;
    int crear0_modificar1_factura_detalle = 0;
    int band_crear = 0;         //Sirve para saber si se preciono el boton crear
    int band_modificar = 0;     //Sirve para saber si se preciono el boton modificar
    //id globales
    int id_cliente_global;
    int id_igv_global;
    int id_documento_global;
    int id_factura_global;
    int id_factura_detalle_global;
    int id_empresa_index;
    int id_usuario_index;
    String perfil_usuario_index = "";
    String nombre_usuario_index = "";
    String nombre_empresa_index = "";
    String ventana_buscar_cliente = "";
    //valores globlales
    String numero_inicial_global;
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public factura(String controlador, String DSN, String user, String password, int id_empresa, int id_usuario, String perfil_usuario, String nombre_usuario, String nombre_empresa) {
        controlador_index = controlador;
        DSN_index = DSN;
        user_index = user;
        password_index = password;
        id_empresa_index = id_empresa;
        id_usuario_index = id_usuario;
        perfil_usuario_index = perfil_usuario;
        nombre_usuario_index = nombre_usuario;
        nombre_empresa_index = nombre_empresa;


        System.out.println("\n\nconectando con Factura");
        initComponents();

        System.out.println("iniciando conexion");
        conexion();

        System.out.println("ocultar el panel de detalle de datos");
        Panel_detalle.setVisible(false);
        lbl_estado.setVisible(false);

        System.out.println("activando la función de letra Mayúsculas");
        Activar_letras_Mayusculas();

        System.out.println("completar combos cliente");
        AutoCompleteDecorator.decorate(this.cbo_razon_social);

        System.out.println("completar combos igv");
        AutoCompleteDecorator.decorate(this.cbo_igv);
        cbo_igv.setEditable(false);

        if (perfil_usuario_index.equals("Solo Lectura")) {
            btn_nuevo.setVisible(false);
            btn_modificar.setVisible(false);
            btn_anular.setVisible(false);
            btn_pagar.setVisible(false);
            btn_listar_por.setVisible(false);
        }

        System.out.println("mostrar tabla factura");
        Mostrar_tabla_factura("");
        mostrar_combo_cliente_buscar_barra();
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

    private void Mostrar_numero_factura() {
        String numero_factura = clase_factura.generar_Numero(id_empresa_index);
        System.out.println("El numero de Factura generado es: " + numero_factura);
        System.out.println("El numero inicla del documento Factura es: " + numero_inicial_global);

        if (Integer.parseInt(numero_factura) > Integer.parseInt(numero_inicial_global)) {
            System.out.println("El NUMERO FACTURA GENERADO es mayor al  NUMERO INICIAL DEL DOCUMENTO");
            System.out.println("Se imprime el NUMERO FACTURA GENERADO en el label");
            txt_numero_factura.setText(numero_factura);
        } else {
            System.out.println("El NUMERO FACTURA GENERADO es MENOR o IGUAL al  NUMERO INICIAL DEL DOCUMENTO");
            System.out.println("Se imprime el NUMERO INICIAL DEL DOCUMENTO FACTURA en el label");
            txt_numero_factura.setText(numero_inicial_global);
        }
    }

    private static boolean ValidarCorreo(String email) {
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void inicializar_id_global() {
        crear0_modificar1_factura = 0;
        crear0_modificar1_factura_detalle = 0;

        //id globales
        id_cliente_global = 0;
        id_igv_global = 0;
        id_documento_global = 0;
        id_factura_global = 0;
        id_factura_detalle_global = 0;
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

    private void mostrar_datos_factura(int id_factura) {
        System.out.println("\nMostrar datos de FACTURA");
        System.out.println("===========================");

        System.out.println("Limpiar cajas de texto");
        limpiar_caja_texto();
        lbl_estado.setVisible(false);
        System.out.println("el id_factura seleccionado es: " + id_factura);

        id_factura_global = id_factura;

        ResultSet r;
        String numero_factura = "";
        String fecha = "";
        String moneda = "";
        String id_cliente = "";
        String razon_social = "";
        String direccion = "";
        String ruc = "";
        String id_igv = "";
        String igv = "";
        String id_documento = "";
        String serie = "";
        String f_creacion = "";
        String f_modificacion = "";
        String nombre = "";

        String id_tipopago = "";
        String tipopago = "";
        String avisar_por_vencer = "";
        String vencimiento = "";
        String ordencompra = "";
        String id_guia = "";
        String numero_guia;
        String serie_guia;
        String observaciones = "";

        String impreso = "";
        String anulado = "";
        String pagado = "";

        band_impreso = 0;
        band_anulado = 0;
        band_pagado = 0;


        try {
//            r = sentencia.executeQuery("select f.numero_factura, convert(varchar, f.fecha, 103) as fecha, f.moneda, f.id_cliente, c.razon_social, c.direccion, c.ruc, f.id_igv, i.igv, f.id_documento, d.serie, f.f_creacion, f.f_modificacion, u.nombre,  f.impreso,  f.anulado,  f.pagado,  f.id_tipopago,  convert(varchar, f.vencimiento, 103) as vencimiento,  f.ordencompra,  f.id_guia,  f.observaciones from tfactura f, tcliente c, tigv i, tdocumentos d, tusuario u where  f.id_cliente=c.id_cliente and  f.id_igv=i.id_igv and  f.id_documento=d.id_documento and  f.id_usuario=u.id_usuario and  f.id_factura='" + id_factura + "'");

            r = sentencia.executeQuery("select\n"
                    + "f.numero_factura,\n"
                    + "convert(varchar, f.fecha, 103) as fecha,\n"
                    + "f.moneda,\n"
                    + "f.id_cliente,\n"
                    + "c.razon_social,\n"
                    + "c.direccion,\n"
                    + "c.ruc,\n"
                    + "f.id_igv,\n"
                    + "i.igv,\n"
                    + "f.id_documento,\n"
                    + "d.serie,\n"
                    + "f.f_creacion,\n"
                    + "f.f_modificacion,\n"
                    + "u.nombre,\n"
                    + "f.impreso,\n"
                    + "f.anulado,\n"
                    + "f.pagado,\n"
                    + "f.id_tipopago,\n"
                    + "tp.descripcion,\n"
                    + "tp.avisar_por_vencer,\n"
                    + "convert(varchar, f.vencimiento, 103) as vencimiento,\n"
                    + "f.ordencompra,\n"
                    + "f.id_guia,\n"
                    + "f.observaciones\n"
                    + "from tfactura f, tcliente c, tigv i, tdocumentos d, tusuario u, TTipopago tp\n"
                    + "where\n"
                    + "f.id_cliente=c.id_cliente and\n"
                    + "f.id_igv=i.id_igv and\n"
                    + "f.id_documento=d.id_documento and\n"
                    + "f.id_usuario=u.id_usuario and\n"
                    + "f.id_tipopago = tp.id_tipopago and\n"
                    + "f.id_factura='" + id_factura + "'");

            while (r.next()) {
                numero_factura = r.getString("numero_factura").trim();
                fecha = r.getString("fecha").trim();
                moneda = r.getString("moneda").trim();
                id_cliente = r.getString("id_cliente").trim();
                razon_social = r.getString("razon_social").trim();
                direccion = r.getString("direccion").trim();
                ruc = r.getString("ruc").trim();
                id_igv = r.getString("id_igv").trim();
                igv = r.getString("igv").trim();
                id_documento = r.getString("id_documento").trim();
                serie = r.getString("serie").trim();
                f_creacion = r.getString("f_creacion").trim();
                f_modificacion = r.getString("f_modificacion");
                nombre = r.getString("nombre").trim();
                impreso = r.getString("impreso").trim();
                anulado = r.getString("anulado").trim();
                pagado = r.getString("pagado").trim();
                id_tipopago = r.getString("id_tipopago").trim();
                tipopago = r.getString("descripcion").trim();
                avisar_por_vencer = r.getString("avisar_por_vencer").trim();
                vencimiento = r.getString("vencimiento").trim();
                ordencompra = r.getString("ordencompra").trim();
                id_guia = r.getString("id_guia").trim();
                observaciones = r.getString("observaciones").trim();
            }

            //da formato a la caja de fecha
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            Date setfecha = null;

            try {
                System.out.println("Fecha:" + fecha);
                setfecha = formato.parse(fecha);
            } catch (ParseException ex) {
                Logger.getLogger(factura.class.getName()).log(Level.SEVERE, null, ex);
            }


            System.out.println("Cambiando el titulo");
            lbl_titulo.setText("Información de Factura");

            id_cliente_global = Integer.parseInt(id_cliente);
            id_documento_global = Integer.parseInt(id_documento);
            id_igv_global = Integer.parseInt(id_igv);
            txt_fecha.setDate(setfecha);
            txt_numero_factura.setText(numero_factura);
            txt_serie.setText(serie);
            txt_direccion.setText(direccion);
            txt_ruc.setText(ruc);
            txt_f_creacion.setText(f_creacion);
            txt_f_modificacion.setText(f_modificacion);
            txt_usuario.setText(nombre);
            txt_vencimiento.setText(vencimiento);
            txt_ordencompra.setText(ordencompra);
            txt_observaciones.setText(observaciones);


            Mostrar_combo_cliente_buscar(razon_social);
            Mostrar_combo_igv_buscar(igv);
            Mostrar_combo_moneda(moneda.trim());
//            tipopago = Capturartipopago(id_tipopago);
            Mostrar_combo_tipopago(tipopago);
            Mostrar_tabla_factura_detalle(id_factura);

            if (Integer.parseInt(id_guia) > 0) {
                numero_guia = Obtener_numeroGuia(id_guia);
                serie_guia = Obtener_serieGuia(id_guia);
                txt_guia.setText("" + serie_guia + " - " + numero_guia + "");
            }

            band_mantenimiento_factura_detalle = 1;
            band_impreso = Integer.parseInt(impreso);
            band_anulado = Integer.parseInt(anulado);
            band_pagado = Integer.parseInt(pagado);

            System.out.println("Desactivar las cajas de texto para el registro");
            activar_caja_texto("no_editable");

            System.out.println("Ocultar botones: guardar, cancelar y otros");
            btn_guardar.setVisible(false);
            btn_cancelar.setVisible(false);
            btn_nuevo_detalle.setVisible(false);
            btn_guardar_detalle.setVisible(false);
            btn_importar.setVisible(false);
            btn_nuevo_cliente.setVisible(false);
            btn_buscar_cliente.setVisible(false);


            System.out.println("Mostrar botones: vista previa, imprimir, creacion");
            btn_vista_previa.setVisible(true);
            btn_imprimir.setVisible(true);
            btn_creacion.setVisible(true);
//            btn_ver_pago.setVisible(false);

            System.out.println("Activar barra de herramientas");
            activar_barra_herramientas("activar");

            if (band_anulado == 1) {
                lbl_estado.setVisible(true);
                txt_estado.setText("ANULADO");
                btn_modificar.setEnabled(false);
                btn_anular.setEnabled(false);
                btn_pagar.setEnabled(false);

            } else {
                if (band_pagado == 1) {
                    lbl_estado.setVisible(true);
                    txt_estado.setText("COBRADO");
                    btn_modificar.setEnabled(false);
                    btn_anular.setEnabled(false);
                    btn_pagar.setEnabled(false);
                } else {
                    if (band_impreso == 1) {
                        btn_modificar.setEnabled(false);
                    }
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

    //Mostrar tabla
    private void Mostrar_tabla_factura(String consulta) {
        ResultSet r;
        String pagado;
        String anulado;
        String estado;
        String fecha_vencimiento;
        String fecha_actual;
        String avisar_por_vencer_string;
        int avisar_por_vencer = 0;
        long diferencia_entre_fechas;
        String id_factura;
        String numero_factura;
        String fecha;


        //da formato a la caja de fecha
        SimpleDateFormat formato_fecha_vencimiento = new SimpleDateFormat("dd/MM/yyyy");
        Date setfecha_vencimiento = null;

        String estado_combo = cbo_estado.getSelectedItem().toString().trim();
        System.out.println("El estado del combo es:" + estado_combo);


        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Estado");
        modelo.addColumn("");
        modelo.addColumn("Numero");
        modelo.addColumn("Fecha");

        String fila[] = new String[4];

        try {
            if (consulta.equals("")) {
                r = sentencia.executeQuery("select \n"
                        + "convert(varchar, f.vencimiento, 103) as vencimiento,\n"
                        + "tp.avisar_por_vencer,\n"
                        + "f.anulado, \n"
                        + "f.pagado, \n"
                        + "f.id_factura,\n"
                        + "f.numero_factura,\n"
                        + "convert(varchar, f.fecha, 103) as fecha \n"
                        + "from tfactura f, TTipopago tp\n"
                        + "where \n"
                        + "f.id_tipopago = tp.id_tipopago and\n"
                        + "f.numero_factura is not null and f.id_empresa='" + id_empresa_index + "' \n"
                        + "order by f.numero_factura desc");
            } else {
                System.out.println("Consulta a Enviar:" + consulta);
                r = sentencia.executeQuery(consulta);
            }

            if (r.next()) {
                System.out.println("La lista no esta vacia");
                r.beforeFirst();

                while (r.next()) {
                    System.out.println("Empezo el recorrido");

                    fecha_vencimiento = r.getString("vencimiento");
                    avisar_por_vencer_string = r.getString("avisar_por_vencer");
                    anulado = r.getString("anulado");
                    pagado = r.getString("pagado");
                    id_factura = r.getString("id_factura");
                    numero_factura = r.getString("numero_factura");
                    fecha = r.getString("fecha");

                    System.out.println("Fecha de vencimiento    : " + fecha_vencimiento);
                    System.out.println("avisar por vencer       : " + avisar_por_vencer);
                    System.out.println("anulado                 : " + anulado);
                    System.out.println("pagado                  : " + pagado);
                    System.out.println("id_factura              : " + id_factura);
                    System.out.println("numero_factura          : " + numero_factura);
                    System.out.println("Fecha                   : " + fecha);


                    if (fecha_vencimiento.trim().length() > 0 && pagado.trim().length() > 0 && anulado.trim().length() > 0 && id_factura.trim().length() > 0 && numero_factura.trim().length() > 0 && fecha.trim().length() > 0) {
                        System.out.println("Si cumple con la condicion de carga");
                        fecha_vencimiento = fecha_vencimiento.trim();
                        avisar_por_vencer = Integer.parseInt(avisar_por_vencer_string);
                        diferencia_entre_fechas = restarFecha(fecha_vencimiento);

                        estado = "";

                        if (diferencia_entre_fechas <= avisar_por_vencer && diferencia_entre_fechas >= 0) {
                            estado = "POR VENCER";
                        } else {
                            if (diferencia_entre_fechas < 0) {
                                estado = "VENCIDO";
                            }
                        }

                        if (anulado.trim().equals("1")) {
                            estado = "ANULADO";
                        } else {
                            if (pagado.trim().equals("1")) {
                                estado = "COBRADO";
                            } else {
                                if (estado.equals("")) {
                                    estado = "POR COBRAR";
                                }
                            }
                        }

                        if (estado.equals(estado_combo) || estado_combo.equals("TODOS")) {
                            fila[0] = estado;
                            fila[1] = id_factura.trim();
                            fila[2] = numero_factura.trim();
                            fila[3] = fecha.trim();
                            modelo.addRow(fila);
                        }
                    }
                }
                r.close();
                tabla_general.setRowHeight(35);
                tabla_general.setModel(modelo);
                TableColumn columna1 = tabla_general.getColumn("Estado");
                columna1.setPreferredWidth(130);
                TableColumn columna2 = tabla_general.getColumn("");
                columna2.setPreferredWidth(0);
                TableColumn columna3 = tabla_general.getColumn("Numero");
                columna3.setPreferredWidth(100);
                TableColumn columna4 = tabla_general.getColumn("Fecha");
                columna4.setPreferredWidth(100);

                tabla_general.setDefaultRenderer(Object.class, new RenderFactura());

            }else{
                JOptionPane.showMessageDialog(null, "No existen Datos que cumplan con esta condición", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Factura - \n" + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Mostrar_tabla_factura_detalle_vacia() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Cant.");
        modelo.addColumn("Unid.");
        modelo.addColumn("Descripción");
        modelo.addColumn("P. Unit.");
        modelo.addColumn("Total");
        tabla_detalle.setModel(modelo);
        TableColumn columna1 = tabla_detalle.getColumn("ID");
        columna1.setPreferredWidth(0);
        TableColumn columna2 = tabla_detalle.getColumn("Cant.");
        columna2.setPreferredWidth(20);
        TableColumn columna3 = tabla_detalle.getColumn("Unid.");
        columna3.setPreferredWidth(20);
        TableColumn columna4 = tabla_detalle.getColumn("Descripción");
        columna4.setPreferredWidth(669);
        TableColumn columna5 = tabla_detalle.getColumn("P. Unit.");
        columna5.setPreferredWidth(40);
        TableColumn columna6 = tabla_detalle.getColumn("Total");
        columna6.setPreferredWidth(40);
    }

    private void Mostrar_tabla_factura_detalle(int id_factura) {
        System.out.println("\nEjecutandose Mostrar_tabla_factura_detalle");

        BigDecimal sub_total = new BigDecimal(0);

        try {
            ResultSet r = sentencia.executeQuery("select id_detalle_factura, descripcion, unidad, cantidad, precio_unitario, descuento_por, precio_total, descuento_val from tdetalle_factura where id_factura='" + id_factura + "'");
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("");
            modelo.addColumn("Descripción");
            modelo.addColumn("Unid.");
            modelo.addColumn("Cant.");
            modelo.addColumn("P. Unit.");
            modelo.addColumn("Dscto Tasa%");
            modelo.addColumn("Valor Neto");

            BigDecimal cantidad;
            BigDecimal precio_unitario;
            BigDecimal precio_total;
            BigDecimal descuento_por;
            BigDecimal descuento_val;
            BigDecimal descuento_val_cont;
            BigDecimal descuento = new BigDecimal(0);

            String fila[] = new String[7];
            while (r.next()) {

                descuento_val = new BigDecimal(0);

                fila[0] = r.getString("id_detalle_factura").trim();
                fila[1] = r.getString("descripcion").trim();
                fila[2] = r.getString("unidad").trim();

                cantidad = new BigDecimal(r.getString("cantidad").trim());
                BigDecimal cantidad_decimal = cantidad;
                cantidad_decimal = cantidad_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                cantidad = cantidad_decimal;
                fila[3] = String.valueOf(cantidad);

                precio_unitario = new BigDecimal(r.getString("precio_unitario").trim());
                BigDecimal precio_unitario_decimal = precio_unitario;
                precio_unitario_decimal = precio_unitario_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                precio_unitario = precio_unitario_decimal;
                fila[4] = String.valueOf(precio_unitario);


                descuento_por = new BigDecimal(r.getString("descuento_por").trim());
                BigDecimal descuento_por_decimal = descuento_por;
                descuento_por_decimal = descuento_por_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                descuento_por = descuento_por_decimal;
                fila[5] = String.valueOf(descuento_por);


                precio_total = new BigDecimal(r.getString("precio_total").trim());
                BigDecimal precio_total_decimal = precio_total;
                precio_total_decimal = precio_total_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                precio_total = precio_total_decimal;
                fila[6] = String.valueOf(precio_total);

                descuento_val = new BigDecimal(r.getString("descuento_val").trim());
                BigDecimal descuento_val_decimal = descuento_val;
                descuento_val_decimal = descuento_val_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                descuento_val = descuento_val_decimal;

                sub_total = sub_total.add(precio_total);
                descuento = descuento.add(descuento_val);
                tabla_detalle.setRowHeight(35);
                modelo.addRow(fila);
            }
            tabla_detalle.setModel(modelo);
            txt_descuentoglobal.setText("" + descuento + "");

            TableColumn columna1 = tabla_detalle.getColumn("");
            columna1.setPreferredWidth(0);

            TableColumn columna2 = tabla_detalle.getColumn("Descripción");
            columna2.setPreferredWidth(700);

            TableColumn columna3 = tabla_detalle.getColumn("Unid.");
            columna3.setPreferredWidth(50);

            TableColumn columna4 = tabla_detalle.getColumn("Cant.");
            columna4.setPreferredWidth(50);

            TableColumn columna5 = tabla_detalle.getColumn("P. Unit.");
            columna5.setPreferredWidth(100);

            TableColumn columna6 = tabla_detalle.getColumn("Dscto Tasa%");
            columna6.setPreferredWidth(100);

            TableColumn columna7 = tabla_detalle.getColumn("Valor Neto");
            columna7.setPreferredWidth(100);


            //Hace que la altura de la celda se modifique segun al contenido
            TableColumnModel cmodel = tabla_detalle.getColumnModel();
            TextAreaRenderer textAreaRenderer = new TextAreaRenderer();
            cmodel.getColumn(1).setCellRenderer(textAreaRenderer);
            TextAreaEditor textEditor = new TextAreaEditor();
            cmodel.getColumn(1).setCellEditor(textEditor);

            //Alinear a la derecha
            DefaultTableCellRenderer alineacion1 = new DefaultTableCellRenderer();
            alineacion1.setHorizontalAlignment(SwingConstants.RIGHT);
            tabla_detalle.getColumnModel().getColumn(3).setCellRenderer(alineacion1);
            tabla_detalle.getColumnModel().getColumn(4).setCellRenderer(alineacion1);
            tabla_detalle.getColumnModel().getColumn(5).setCellRenderer(alineacion1);
            tabla_detalle.getColumnModel().getColumn(6).setCellRenderer(alineacion1);

            //Alinear al centro
            DefaultTableCellRenderer alineacion2 = new DefaultTableCellRenderer();
            alineacion2.setHorizontalAlignment(SwingConstants.CENTER);
            tabla_detalle.getColumnModel().getColumn(2).setCellRenderer(alineacion2);

            calcular_totales(sub_total);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar la tabla Factura - " + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calcular_totales(BigDecimal sub_total) {
        
        System.out.println("subtotal es : "+sub_total);
        
        BigDecimal igv = new BigDecimal(cbo_igv.getSelectedItem().toString());
        //float calculo_igv = sub_total * (igv / 100);
        BigDecimal cien = new BigDecimal(100);
        BigDecimal igvCien = igv.divide(cien);
        
        BigDecimal calculo_igv = sub_total.multiply(igvCien);
        BigDecimal total = sub_total.add(calculo_igv);

        
        System.out.println("igv es : "+igv);
        System.out.println("calculo_igv es : "+calculo_igv);
        System.out.println("total es : "+total);
        
        
        
        BigDecimal sub_total_decimal = sub_total;
        sub_total_decimal = sub_total_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        sub_total = sub_total_decimal;

        System.out.println("sub_total_decimal es : "+sub_total_decimal);
        
        BigDecimal calculo_igv_decimal = calculo_igv;
        calculo_igv_decimal = calculo_igv_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        calculo_igv = calculo_igv_decimal;

        System.out.println("sub_total_decimal es : "+sub_total_decimal);

        BigDecimal total_decimal = total;
        total_decimal = total_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        total = total_decimal;

        System.out.println("total_decimal es : "+total_decimal);

        
        String moneda = cbo_moneda.getSelectedItem().toString();
        
        float totalLetra = (float) total_decimal.doubleValue();
        
        System.out.println("total letra es: "+totalLetra);
        String total_letras = clase_numero_letra.getStringOfNumber(totalLetra, moneda);

        txt_sub_total.setText("" + sub_total);
        txt_igv_calculo.setText("" + calculo_igv);
        txt_total.setText("" + total);
        txt_total_letras.setText(total_letras);
        btn_guardar.setEnabled(true);

    }

    private void Mostrar_tabla_cliente() {
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

    private void mostrar_tabla_importar_factura(String consulta) {
        try {

            ResultSet r;

            if (consulta.equals("")) {
                r = sentencia.executeQuery("select f.id_factura, d.serie, f.numero_factura, convert(varchar, f.fecha, 103) as fecha, c.razon_social from tfactura f, tcliente c, tdocumentos d where f.id_cliente = c.id_cliente and f.id_documento = d.id_documento and f.numero_factura is not null and f.id_empresa = '" + id_empresa_index + "' order by f.numero_factura desc");
            } else {
                r = sentencia.executeQuery(consulta);
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

    private void importar_detalle_factura(int id_factura) {
        int respuesta;
        respuesta = JOptionPane.showConfirmDialog(null, "¿Está usted seguro de Importar los Detalles de la Factura seleccionada?", "IMPORTAR DETALLES DE FACTURA", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {

            insertar_detalle_factura(id_factura);
            Mostrar_tabla_factura_detalle(id_factura_global);
            txt_buscar_factura.setText("");
            dialog_buscar_factura.dispose();

        }
    }

    private void insertar_detalle_factura(int id_factura) {
        System.out.println("Ejecutandose Insertar_detalle_factura");
        System.out.println("=====================================");

        ResultSet r;

        try {
            r = sentencia.executeQuery("select id_detalle_factura, cantidad, unidad, descripcion, precio_unitario, precio_total, descuento_por, descuento_val from tdetalle_factura where id_factura = '" + id_factura + "'");
            while (r.next()) {
                int id_detalle_factura = Integer.parseInt(r.getString("id_detalle_factura").trim());
                BigDecimal cantidad = new BigDecimal(r.getString("cantidad").trim());
                String unidad = r.getString("unidad").trim();
                String descripcion = r.getString("descripcion").trim();
                BigDecimal precio_unitario = new BigDecimal(r.getString("precio_unitario").trim());
                BigDecimal precio_total = new BigDecimal(r.getString("precio_total").trim());
                BigDecimal descuento_por = new BigDecimal(r.getString("descuento_por").trim());
                BigDecimal descuento_val = new BigDecimal(r.getString("descuento_val").trim());

                if (clase_factura_detalle.crear(id_factura_global, cantidad, unidad, descripcion, precio_unitario, precio_total, descuento_por, descuento_val)) {

                    System.out.println("Creacion de Detalle de Factura exitosa. Nro de detalle Factura insertado:" + id_detalle_factura);
                } else {
                    JOptionPane.showMessageDialog(null, "Ocurrio al Insertar el Detalle de Factura\n N°:" + id_detalle_factura, "ERROR", JOptionPane.ERROR_MESSAGE);
                }

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos de Detalle FACTURA", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Mostrar combos
    //Mostrar combos buscar
    private void mostrar_combo_cliente_buscar_barra() {
        try {
            ResultSet r = sentencia.executeQuery("select cl.razon_social from tcliente cl, TFactura co where co.id_cliente= cl.id_cliente group by cl.razon_social order by razon_social asc");
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

    private void Mostrar_combo_cliente() {
        System.out.println("Ejecuntandose Mostrar_combo_cliente");
        try {
            ResultSet r = sentencia.executeQuery("select razon_social from tcliente order by razon_social asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement("");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("razon_social").trim());
                modelo1.addElement(resultado);
            }
            cbo_razon_social.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Cliente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Mostrar_combo_cliente_buscar(String razon_social) {
        try {
            ResultSet r = sentencia.executeQuery("select razon_social from tcliente order by razon_social asc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement(razon_social);
            String resultado;
            while (r.next()) {
                resultado = (r.getString("razon_social").trim());
                if (!resultado.equals(razon_social)) {
                    modelo1.addElement(resultado);
                }
            }
            cbo_razon_social.setModel(modelo1);
            band_cbo_cliente = 1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Cliente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Mostrar_combo_igv() {
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

    private void Mostrar_combo_tipopago() {
        try {
            ResultSet r = sentencia.executeQuery("select descripcion from ttipopago where id_empresa='" + id_empresa_index + "'");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            String resultado;
            while (r.next()) {
                resultado = (r.getString("descripcion").trim());
                modelo1.addElement(resultado);
            }
            cbo_tipopago.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Tipo de pago", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("Calcular fecha de vencimiento");
        System.out.println("Capturar el valor de la cantidad de dias de pago");
        int cantdias = 0;

        try {
            ResultSet r = sentencia.executeQuery("select cantdias from ttipopago where descripcion = '" + cbo_tipopago.getSelectedItem().toString() + "' and  id_empresa='" + id_empresa_index + "'");
            while (r.next()) {
                cantdias = Integer.parseInt(r.getString("cantdias").trim());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al obtener la cantidad de dias de vencimiento de pago", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("La cantidad de dias de pago es: " + cantdias);

        Date fecha = txt_fecha.getDate();
        sumarRestarDiasFecha(fecha, cantdias);

    }

    private void Mostrar_combo_igv_buscar(String igv) {
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

    private void Mostrar_combo_moneda(String moneda) {
        System.out.println("Se esta ejecutando el mostrar_combo_moneda");
        
        DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
        modelo1.addElement(moneda);
        
        try {
            ResultSet r = sentencia.executeQuery("select nombre from tmoneda where id_empresa='" + id_empresa_index + "' order by moneda_local desc");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("nombre").trim());                
                if (!resultado.equals(moneda)) {
                    modelo1.addElement(resultado);
                }
            }
            
            cbo_moneda.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Moneda", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Mostrar_combo_tipopago(String tipopago) {
        try {
            ResultSet r = sentencia.executeQuery("select descripcion from ttipopago where id_empresa='" + id_empresa_index + "' order by descripcion desc");
            DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
            modelo1.addElement(tipopago);
            String resultado;
            while (r.next()) {
                resultado = (r.getString("descripcion").trim());
                if (!resultado.equals(tipopago)) {
                    modelo1.addElement(resultado);
                }
            }
            cbo_tipopago.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Tipo de Pago", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializar_cbo_moneda() {
        DefaultComboBoxModel modelo1 = new DefaultComboBoxModel();
        
        try {
            ResultSet r = sentencia.executeQuery("select nombre from tmoneda where id_empresa='" + id_empresa_index + "' order by moneda_local desc");
            String resultado;
            while (r.next()) {
                resultado = (r.getString("nombre").trim());
                modelo1.addElement(resultado);                
            }
            cbo_moneda.setModel(modelo1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Moneda", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        cbo_moneda.setModel(modelo1);
    }

    private String CapturarSimboloMoneda(String moneda){
        String simbolo = "";
        
        try {
            String query = "select simbolo from TMoneda where nombre = '"+ moneda +"' and id_empresa='" + id_empresa_index + "'";
            System.out.println(query);
            ResultSet r = sentencia.executeQuery(query);
            
            while (r.next()) {
                simbolo = (r.getString("simbolo").trim());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Cargar el combo Moneda", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        return simbolo;
    }
    
    private String Capturartipopago(String id_tipopago) {
        String tipopago = "";

        try {
            ResultSet r = sentencia.executeQuery("select descripcion from ttipopago where id_tipopago = '" + id_tipopago + "' and id_empresa='" + id_empresa_index + "'");
            while (r.next()) {
                tipopago = (r.getString("descripcion").trim());
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Obtener la escripcion del Tipo de Pago", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        return tipopago;
    }

    //Activar
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
            txt_fecha.setEditable(true);
            txt_fecha.setEnabled(true);
            cbo_razon_social.setEditable(true);
            cbo_razon_social.setEnabled(true);
            cbo_igv.setEnabled(true);
            cbo_igv.setEditable(false);
            cbo_moneda.setEnabled(true);
            cbo_moneda.setEditable(false);

            txt_observaciones.setEditable(true);
            txt_ordencompra.setEditable(true);
            cbo_tipopago.setEnabled(true);

            txt_cantidad.setEditable(true);
            txt_unidad.setEditable(true);
            txt_precio_unitario.setEditable(true);
            txt_detalle.setEditable(true);

            txt_razon_social_cliente_crear.setEditable(true);
            txt_ruc_cliente_crear.setEditable(true);
            txt_direccion_cliente_crear.setEditable(true);
            txt_telefono_cliente_crear.setEditable(true);
            txt_celular_cliente_crear.setEditable(true);
            txt_correo_cliente_crear.setEditable(true);
        }

        if (funcion.equals("no_editable")) {
            txt_direccion.setEditable(false);
            txt_ruc.setEditable(false);
            txt_total_letras.setEditable(false);
            txt_sub_total.setEditable(false);
            txt_igv_calculo.setEditable(false);
            txt_total.setEditable(false);

            txt_observaciones.setEditable(false);
            txt_ordencompra.setEditable(false);
            cbo_tipopago.setEnabled(false);


            txt_fecha.setEditable(false);
            txt_fecha.setEnabled(false);
            cbo_razon_social.setEditable(false);
            cbo_razon_social.setEnabled(false);
            cbo_igv.setEditable(false);
            cbo_igv.setEnabled(false);
            cbo_moneda.setEnabled(false);
            cbo_igv.setEditable(false);

            txt_cantidad.setEditable(false);
            txt_unidad.setEditable(false);
            txt_precio_unitario.setEditable(false);
            txt_detalle.setEditable(false);

            txt_razon_social_cliente_crear.setEditable(false);
            txt_ruc_cliente_crear.setEditable(false);
            txt_direccion_cliente_crear.setEditable(false);
            txt_telefono_cliente_crear.setEditable(false);
            txt_celular_cliente_crear.setEditable(false);
            txt_correo_cliente_crear.setEditable(false);

            txt_f_creacion.setEditable(false);
            txt_f_modificacion.setEditable(false);
            txt_usuario.setEditable(false);
        }
    }

    private void activar_barra_herramientas(String funcion) {
        if (funcion.equals("activar")) {
            btn_nuevo.setEnabled(true);
            btn_modificar.setEnabled(true);
            btn_anular.setEnabled(true);
            btn_pagar.setEnabled(true);
            btn_listar_por.setEnabled(true);
            txt_buscar.setEnabled(true);
        }

        if (funcion.equals("desactivar")) {
            btn_nuevo.setEnabled(false);
            btn_modificar.setEnabled(false);
            btn_anular.setEnabled(false);
            txt_buscar.setEnabled(false);
            btn_pagar.setEnabled(false);
            btn_listar_por.setEnabled(false);
        }
    }

    //Limpiar
    private void limpiar_caja_texto() {
        //da formato a la caja de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("");
        Date d = new Date();
        String s = sdf.format(d);
        txt_fecha.setDate(d);

        //Cabecera
        txt_serie.setText("");
        txt_numero_factura.setText("");
        txt_ordencompra.setText("");
        txt_vencimiento.setText("");
        txt_guia.setText("");
        txt_direccion.setText("");
        txt_ruc.setText("");


        //Pie
        txt_total_letras.setText("");
        txt_observaciones.setText("");
        txt_estado.setText("");
        txt_descuentoglobal.setText("");
        txt_sub_total.setText("");
        txt_igv_calculo.setText("");
        txt_total.setText("");


        //Detalle factura
        txt_cantidad.setText("");
        txt_unidad.setText("UNID.");
        txt_precio_unitario.setText("");
        txt_detalle.setText("");
        txt_descuento.setText("");

        //Crear Cliente
        txt_razon_social_cliente_crear.setText("");
        txt_ruc_cliente_crear.setText("");
        txt_direccion_cliente_crear.setText("");
        txt_telefono_cliente_crear.setText("");
        txt_celular_cliente_crear.setText("");
        txt_correo_cliente_crear.setText("");

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

    private void limpiar_caja_texto_crear_detalle_factura() {
        txt_cantidad.setText("");
        txt_unidad.setText("UNID.");
        txt_precio_unitario.setText("");
        txt_detalle.setText("");
        txt_descuento.setText("");
    }

    //Id Ultimo
    private void Factura_id_ultimo() {
        try {
            String consulta = "SELECT id_factura FROM tfactura WHERE id_factura = (SELECT MAX(id_factura) from tfactura)";
            ResultSet r = sentencia.executeQuery(consulta);
            System.out.println("consulta enviada: " + consulta);

            while (r.next()) {
                id_factura_global = Integer.parseInt(r.getString("id_factura"));
                System.out.println("El ultimo id_factura generado es " + id_factura_global);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el ultimo Id de Factura Creado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Cliente_id_ultimo() {
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

    private String serie_documento_factura() {
        String serie = "";
        try {
            ResultSet r = sentencia.executeQuery("select id_documento, serie, numero_inicial from tdocumentos where nombre='FACTURA' and id_empresa='" + id_empresa_index + "'");
            while (r.next()) {
                id_documento_global = Integer.parseInt(r.getString("id_documento"));
                serie = r.getString("serie");
                numero_inicial_global = r.getString("numero_inicial");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el Id y Serie  del Documento FACTURA", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return serie;
    }

    private String Obtener_numeroGuia(String id_guia) {
        String numero_guia = "";
        try {
            ResultSet r = sentencia.executeQuery("select numero_guia from tguia where id_guia ='" + id_guia + "' and id_empresa='" + id_empresa_index + "'");
            while (r.next()) {
                numero_guia = r.getString("numero_guia");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el numero de Guia", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return numero_guia;
    }

    private String Obtener_serieGuia(String id_guia) {
        String serie = "";
        try {
            ResultSet r = sentencia.executeQuery("select serie from tdocumentos where nombre='GUIA' and id_empresa='" + id_empresa_index + "'");
            while (r.next()) {
                serie = r.getString("serie");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al Extraer la Serie  del Documento GUIA", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return serie;
    }

    //Crear
    private void crear_cliente(String razon_social, String ruc, String direccion, String telefono, String celular, String correo, int id_empresa, int id_usuario) {
        System.out.println("\nejecutandose la función: crear_cliente");

        if (clase_cliente.Cliente_crear(razon_social, ruc, direccion, telefono, celular, correo, id_empresa, id_usuario)) {
            System.out.println("\nEl cliente se logró registrar exitosamente!");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_cliente();

            System.out.println("actualizamos tabla cliente");
            Mostrar_tabla_cliente();

            System.out.println("Ejecutando la consulta para saber cual es el ultimo id_cliente generado");

            Cliente_id_ultimo();
            Mostrar_combo_cliente_buscar(razon_social);
            txt_direccion.setText(direccion);
            txt_ruc.setText(ruc);

            txt_buscar_cliente.setText("");
            dialog_crear_cliente.dispose();

            JOptionPane.showMessageDialog(null, "El cliente se registro exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear el cliente. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crear_Factura_Detalle(int id_factura, BigDecimal cantidad, String unidad, String descripcion, BigDecimal precio_unitario, BigDecimal precio_total, BigDecimal descuento_por, BigDecimal descuento_val) {
        System.out.println("\nejecutandose la función: crear_factura_detalle");

        if (clase_factura_detalle.crear(id_factura, cantidad, unidad, descripcion, precio_unitario, precio_total, descuento_por, descuento_val)) {
            System.out.println("\nEl Detalle de Factura se logró registrar exitosamente!");

            panel_nuevo_detalle.setVisible(false);
            btn_nuevo_detalle.setVisible(true);
            btn_importar.setVisible(true);

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_detalle_factura();

            System.out.println("actualizamos tabla factura_detalle");
            Mostrar_tabla_factura_detalle(id_factura);

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear la Factyra_Detalle. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Modificar
    private void modificar_Factura_Detalle(int id_detalle_factura, int id_factura, BigDecimal cantidad, String unidad, String descripcion, BigDecimal precio_unitario, BigDecimal precio_total, BigDecimal descuento_por, BigDecimal descuento_val) {
        System.out.println("\nejecutandose la función: crear_factura_detalle");

        if (clase_factura_detalle.modificar(id_detalle_factura, id_factura, cantidad, unidad, descripcion, precio_unitario, precio_total, descuento_por, descuento_val)) {
            System.out.println("\nEl Detalle de Factura se logró Modificar exitosamente!");
            JOptionPane.showMessageDialog(null, "El Detalle se Modificó exitosamente!");
            id_factura_detalle_global = 0;
            id_detalle_factura = 0;
            crear0_modificar1_factura_detalle = 0;

            panel_nuevo_detalle.setVisible(false);
            btn_nuevo_detalle.setVisible(true);

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto_crear_detalle_factura();

            System.out.println("actualizamos tabla factura_detalle");
            Mostrar_tabla_factura_detalle(id_factura);

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear la Factura_Detalle. \nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar_Factura(int id_factura, String numero_factura, String fecha, BigDecimal calculo_igv, BigDecimal sub_total, BigDecimal total, String total_letras, int id_cliente, String moneda, int id_igv, int id_documento, String simbolo_moneda, int id_empresa, int id_usuario, int id_tipopago, String vencimiento, String ordencompra, int id_guia, BigDecimal descuento, String observaciones) {
        System.out.println("ejecutandose la función: modificar_Factura");

        if (clase_factura.modificar(id_factura, numero_factura, fecha, calculo_igv, sub_total, total, total_letras, id_cliente, moneda, id_igv, id_documento, simbolo_moneda, id_empresa, id_usuario, id_tipopago, vencimiento, ordencompra, id_guia, descuento, observaciones)) {
            System.out.println("La Factura se logró modificar exitosamente!");

            System.out.println("Volver los ID's a su valor inicial");

            System.out.println("limpiar cajas de texto");
            limpiar_caja_texto();

            System.out.println("desactivar las cajas de texto para el registro");
            activar_caja_texto("no_editable");

            System.out.println("ocultar botones: guardar, cancelar y otros");
            btn_guardar.setVisible(false);
            btn_cancelar.setVisible(false);
            btn_nuevo_detalle.setVisible(false);
            btn_nuevo_cliente.setVisible(false);
            btn_buscar_cliente.setVisible(false);

            System.out.println("mostrar botones: vista previa, imprimir, creacion");
            btn_vista_previa.setVisible(true);
            btn_imprimir.setVisible(true);
            btn_creacion.setVisible(true);
//            btn_ver_pago.setVisible(false);

            System.out.println("activar barra de herramientas");
            activar_barra_herramientas("activar");

            System.out.println("Mostrar tabla Factura_detalle_vacia");
            Mostrar_tabla_factura_detalle_vacia();

            System.out.println("ocultar el panel de detalle de datos");
            Panel_detalle.setVisible(false);

            System.out.println("actualizamos tabla Factura");
            Mostrar_tabla_factura("");

            System.out.println("Inicializamos lo id_globales");
            inicializar_id_global();

            band_index = 0;
            band_cbo_cliente = 1;
            band_mantenimiento_factura_detalle = 0;
            JOptionPane.showMessageDialog(null, "La Factura se Guardó exitosamente!");

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al Guardar/Modificar La Factura.\nPor favor inténtelo nuevamente.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Otros
    public void accionCombo(ActionEvent evt) {
        System.out.println("Calcular fecha de vencimiento");
        System.out.println("Capturar el valor de la cantidad de dias de pago");
        int cantdias = 0;

        try {
            ResultSet r = sentencia.executeQuery("select cantdias from ttipopago where descripcion = '" + cbo_tipopago.getSelectedItem().toString() + "' and  id_empresa='" + id_empresa_index + "'");
            while (r.next()) {
                cantdias = Integer.parseInt(r.getString("cantdias").trim());
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al obtener la cantidad de dias de vencimiento de pago", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("La cantidad de dias de pago es: " + cantdias);

        Date fecha = txt_fecha.getDate();
        sumarRestarDiasFecha(fecha, cantdias);
    }

    public void sumarRestarDiasFecha(Date fecha, int dias) {
        System.out.println("Fecha recibida: " + fecha);
        System.out.println("Dias a sumar: " + dias);

        Calendar calendar = Calendar.getInstance();
        // Configuramos la fecha que se recibe
        calendar.setTime(fecha);
        // numero de días a añadir, o restar en caso de días<0
        calendar.add(Calendar.DAY_OF_YEAR, dias);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechavencimiento = sdf.format(calendar.getTime());
        txt_vencimiento.setText(fechavencimiento);
    }

    public long restarFecha(String fecha_vencimiento) {
//        System.out.println("\nEjecutando RESTARFECHA");
//        System.out.println("======================");

        //capturamos la fecha actual
        SimpleDateFormat formato_act = new SimpleDateFormat("dd/MM/yyyy");
        Date dia = new Date();
        String fecha_actual = formato_act.format(dia);
//        System.out.println("Fecha actual:" + fecha_actual);
//        System.out.println("Fecha de Vencimiento:" + fecha_vencimiento);

        int Y1 = Integer.parseInt(fecha_vencimiento.substring(6));
        int M1 = Integer.parseInt(fecha_vencimiento.substring(3, 5));
        int D1 = Integer.parseInt(fecha_vencimiento.substring(0, 2));
//        System.out.println("Vencimiento: Año: " + Y1 + " Mes: " + M1 + " Dia:" + D1 + "");


        int Y2 = Integer.parseInt(fecha_actual.substring(6));
        int M2 = Integer.parseInt(fecha_actual.substring(3, 5));
        int D2 = Integer.parseInt(fecha_actual.substring(0, 2));
//        System.out.println("ACTUAL: Año: " + Y2 + " Mes: " + M2 + " Dia:" + D2 + "");

        java.util.GregorianCalendar date1 = new java.util.GregorianCalendar(Y1, M1, D1);
        java.util.GregorianCalendar date2 = new java.util.GregorianCalendar(Y2, M2, D2);
        long difms = date1.getTimeInMillis() - date2.getTimeInMillis();
        long difd = difms / (1000 * 60 * 60 * 24);
//        System.out.println("Retorna diferencia:" + difd);
        return difd;
    }

    private int CapturarId(String campocapturar, String tabla, String campocondicion, String valorcondicion, int empresa) {
        System.out.println("Iniciando metodo Capturar id");

//        System.out.println("Datos recibidos");
//        System.out.println("Valor          :" + campocapturar);
//        System.out.println("Tabla          :" + tabla);
//        System.out.println("Campo condicion:" + campocondicion);
//        System.out.println("Valor condicion:" + valorcondicion);
//        System.out.println("Empresa        :" + empresa);

        int valor = 0;

        try {
            System.out.println("select " + campocapturar + " from " + tabla + " where " + campocondicion + " = '" + valorcondicion + "' and  id_empresa = '" + empresa + "'");
            ResultSet r = sentencia.executeQuery("select " + campocapturar + " from " + tabla + " where " + campocondicion + " = '" + valorcondicion + "' and  id_empresa = '" + empresa + "'");
            while (r.next()) {
                valor = Integer.parseInt(r.getString("" + campocapturar + "").trim());
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio al obtener el valor del campo " + campocapturar + " de la tabla " + tabla + "", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("Valor obtenido:" + valor);
        return valor;

    }

    private String obtener_codigoreporte() {
        String codigo_reporte = "";

        if (rdio_cliente_todos.isSelected() == true) {
            codigo_reporte = codigo_reporte.concat("1");
        } else {
            codigo_reporte = codigo_reporte.concat("0");
        }

        if (rdio_cliente_especifico.isSelected() == true) {
            codigo_reporte = codigo_reporte.concat("1");
        } else {
            codigo_reporte = codigo_reporte.concat("0");
        }

        if (rdio_factura_todos.isSelected() == true) {
            codigo_reporte = codigo_reporte.concat("1");
        } else {
            codigo_reporte = codigo_reporte.concat("0");
        }

        if (rdio_factura_por_pagar.isSelected() == true) {
            codigo_reporte = codigo_reporte.concat("1");
        } else {
            codigo_reporte = codigo_reporte.concat("0");
        }

        if (rdio_factura_anulados.isSelected() == true) {
            codigo_reporte = codigo_reporte.concat("1");
        } else {
            codigo_reporte = codigo_reporte.concat("0");
        }

        if (rdio_factura_pagados.isSelected() == true) {
            codigo_reporte = codigo_reporte.concat("1");
        } else {
            codigo_reporte = codigo_reporte.concat("0");
        }

        return codigo_reporte;
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
                Mostrar_tabla_factura(consulta);
                break;

            case 1:
                consulta = "select \n"
                        + "convert(varchar, f.vencimiento, 103) as vencimiento,\n"
                        + "tp.avisar_por_vencer,\n"
                        + "f.anulado, \n"
                        + "f.pagado, \n"
                        + "f.id_factura,\n"
                        + "f.numero_factura,\n"
                        + "convert(varchar, f.fecha, 103) as fecha \n"
                        + "from tfactura f, TTipopago tp, TCliente cl\n"
                        + "where\n"
                        + "f.id_tipopago = tp.id_tipopago and\n"
                        + "f.id_cliente = cl.id_cliente and\n"
                        + "cl.razon_social = '" + bus_cliente + "' and \n"
                        + "f.numero_factura is not null and "
                        + "f.id_empresa='" + id_empresa_index + "' \n"
                        + "order by f.numero_factura desc";

                System.out.println(consulta);
                Mostrar_tabla_factura(consulta);
                break;

            case 2:
                consulta = "select \n"
                        + "convert(varchar, f.vencimiento, 103) as vencimiento,\n"
                        + "tp.avisar_por_vencer,\n"
                        + "f.anulado,\n"
                        + "f.pagado,\n"
                        + "f.id_factura,\n"
                        + "f.numero_factura,\n"
                        + "convert(varchar, f.fecha, 103) as fecha \n"
                        + "from tfactura f, tcliente c, TTipopago tp, TDetalle_factura df\n"
                        + "where \n"
                        + "(f.numero_factura like '%" + bus_txt + "%'  or \n"
//                        + "f.fecha like '%" + bus_txt + "%' or \n"
                        + "f.ordencompra like '%" + bus_txt + "%' or \n"
                        + "f.calculo_igv like '%" + bus_txt + "%' or \n"
                        + "f.sub_total like '%" + bus_txt + "%' or \n"
                        + "f.total like '%" + bus_txt + "%' or \n"
                        + "f.total_letras like '%" + bus_txt + "%' or \n"
                        + "f.moneda like '%" + bus_txt + "%' or \n"
                        + "df.descripcion like '%" + bus_txt + "%' or \n"
                        + "c.ruc like '%" + bus_txt + "%' or \n"
                        + "c.direccion like '%" + bus_txt + "%') \n"
                        + "and f.id_cliente = c.id_cliente and \n"
                        + "f.id_tipopago = tp.id_tipopago and \n"
                        + "df.id_factura = f.id_factura and \n"
                        + "f.numero_factura is not null and \n"
                        + "f.id_empresa='" + id_empresa_index + "' \n"
                        + "order by f.numero_factura desc";


                System.out.println(consulta);
                Mostrar_tabla_factura(consulta);
                break;

            case 3:
                consulta = "select \n"
                        + "convert(varchar, f.vencimiento, 103) as vencimiento,\n"
                        + "tp.avisar_por_vencer,\n"
                        + "f.anulado,\n"
                        + "f.pagado,\n"
                        + "f.id_factura,\n"
                        + "f.numero_factura,\n"
                        + "convert(varchar, f.fecha, 103) as fecha \n"
                        + "from tfactura f, tcliente c, TTipopago tp, TDetalle_factura df\n"
                        + "where \n"
                        + "(f.numero_factura like '%" + bus_txt + "%'  or \n"
//                        + "f.fecha like '%" + bus_txt + "%' or \n"
                        + "f.ordencompra like '%" + bus_txt + "%' or \n"
                        + "f.calculo_igv like '%" + bus_txt + "%' or \n"
                        + "f.sub_total like '%" + bus_txt + "%' or \n"
                        + "f.total like '%" + bus_txt + "%' or \n"
                        + "f.total_letras like '%" + bus_txt + "%' or \n"
                        + "f.moneda like '%" + bus_txt + "%' or \n"
                        + "df.descripcion like '%" + bus_txt + "%' or \n"
                        + "c.ruc like '%" + bus_txt + "%' or \n"
                        + "c.direccion like '%" + bus_txt + "%') \n"
                        + "and f.id_cliente = c.id_cliente and \n"
                        + "f.id_tipopago = tp.id_tipopago and \n"
                        + "df.id_factura = f.id_factura and \n"
                        + "c.razon_social = '" + bus_cliente + "' and \n"
                        + "f.numero_factura is not null and \n"
                        + "f.id_empresa='" + id_empresa_index + "' \n"
                        + "order by f.numero_factura desc";

//                System.out.println(consulta);
                Mostrar_tabla_factura(consulta);
                break;
        }
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
        mantenimiento_tabla_detalle_factura = new javax.swing.JPopupMenu();
        Modificar = new javax.swing.JMenuItem();
        Eliminar = new javax.swing.JMenuItem();
        dialog_crear_cliente = new javax.swing.JDialog();
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
        txt_razon_social_cliente_crear = new javax.swing.JTextField();
        txt_ruc_cliente_crear = new javax.swing.JTextField();
        txt_direccion_cliente_crear = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txt_telefono_cliente_crear = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txt_celular_cliente_crear = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txt_correo_cliente_crear = new javax.swing.JTextField();
        dialog_buscar_cliente = new javax.swing.JDialog();
        jPanel24 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        txt_buscar_cliente = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        btn_cliente_cancelar_busqueda = new javax.swing.JButton();
        btn_cliente_seleccionar = new javax.swing.JButton();
        btn_nuevo_cliente_buscar = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabla_cliente_buscar = new javax.swing.JTable();
        dialog_buscar_factura = new javax.swing.JDialog();
        jPanel47 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jLabel35 = new javax.swing.JLabel();
        txt_buscar_factura = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jPanel48 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        btn_cliente_cancelar_busqueda2 = new javax.swing.JButton();
        btn_cliente_seleccionar2 = new javax.swing.JButton();
        jPanel50 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabla_factura_buscar = new javax.swing.JTable();
        dialogo_reporte_factura = new javax.swing.JDialog();
        panel_norte = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        panel_cuerpo = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        panel_fechas = new javax.swing.JPanel();
        lb_fecha1 = new javax.swing.JLabel();
        txt_fecha_inicio = new org.jdesktop.swingx.JXDatePicker();
        lb_fecha2 = new javax.swing.JLabel();
        txt_fecha_fin = new org.jdesktop.swingx.JXDatePicker();
        panel_seleccione_cliente = new javax.swing.JPanel();
        rdio_cliente_todos = new javax.swing.JRadioButton();
        rdio_cliente_especifico = new javax.swing.JRadioButton();
        txt_cliente_filtro = new javax.swing.JTextField();
        btn_buscar_cliente_filtro = new javax.swing.JButton();
        panel_mostrar_solo_facturas = new javax.swing.JPanel();
        rdio_factura_todos = new javax.swing.JRadioButton();
        rdio_factura_por_pagar = new javax.swing.JRadioButton();
        rdio_factura_anulados = new javax.swing.JRadioButton();
        rdio_factura_pagados = new javax.swing.JRadioButton();
        jPanel10 = new javax.swing.JPanel();
        btn_generar_reporte = new javax.swing.JButton();
        btn_cancelar_reporte = new javax.swing.JButton();
        seleccione_el_cliente = new javax.swing.ButtonGroup();
        mostrar_solo_las_facturas = new javax.swing.ButtonGroup();
        ordenar_por = new javax.swing.ButtonGroup();
        dialog_pago = new javax.swing.JDialog();
        jPanel28 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jPanel31 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        txt_serie_pago = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        txt_serie_pago1 = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        cbo_banco = new javax.swing.JComboBox();
        jLabel41 = new javax.swing.JLabel();
        txt_nro_cuenta = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        txt_nro_operacion = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        txt_fecha1 = new org.jdesktop.swingx.JXDatePicker();
        jPanel32 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        txt_monto_dolar = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        txt_tipo_cambio = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        txt_tipo_cambio1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btn_nuevo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btn_modificar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btn_anular = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        btn_pagar = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        btn_listar_por = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        barra_buscar = new javax.swing.JToolBar();
        label_buscar = new javax.swing.JLabel();
        txt_buscar = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbo_estado = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        cbo_buscar_cliente = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        panel_tabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_general = new javax.swing.JTable();
        Panel_detalle = new javax.swing.JPanel();
        norte = new javax.swing.JPanel();
        lbl_titulo = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        txt_numero_factura = new javax.swing.JLabel();
        lbl_numero = new javax.swing.JLabel();
        txt_serie = new javax.swing.JLabel();
        lbl_serie = new javax.swing.JLabel();
        centro = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        Cliente = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cbo_razon_social = new javax.swing.JComboBox();
        txt_direccion = new javax.swing.JTextField();
        txt_ruc = new javax.swing.JTextField();
        btn_nuevo_cliente = new javax.swing.JButton();
        btn_buscar_cliente = new javax.swing.JButton();
        Datos = new javax.swing.JPanel();
        lb_fecha = new javax.swing.JLabel();
        lb_moneda = new javax.swing.JLabel();
        txt_fecha = new org.jdesktop.swingx.JXDatePicker();
        cbo_moneda = new javax.swing.JComboBox();
        lb_ordendecompra = new javax.swing.JLabel();
        txt_ordencompra = new javax.swing.JTextField();
        condiciones_pago = new javax.swing.JPanel();
        lb_tipopago = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        cbo_tipopago = new javax.swing.JComboBox();
        txt_vencimiento = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txt_guia = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txt_sub_total = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbo_igv = new javax.swing.JComboBox();
        txt_igv_calculo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_total = new javax.swing.JTextField();
        txt_estado = new javax.swing.JLabel();
        lbl_estado = new javax.swing.JLabel();
        txt_descuentoglobal = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
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
        txt_precio_unitario = new javax.swing.JTextField();
        btn_cancelar_guardar_detalle = new javax.swing.JButton();
        txt_descuento = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txt_total_letras = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txt_observaciones = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabla_detalle = new javax.swing.JTable();
        btn_nuevo_detalle = new javax.swing.JButton();
        btn_importar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btn_cancelar = new javax.swing.JButton();
        btn_guardar = new javax.swing.JButton();
        btn_imprimir = new javax.swing.JButton();
        btn_creacion = new javax.swing.JButton();
        btn_vista_previa = new javax.swing.JButton();

        dialog_fecha_creacion.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

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
        txt_f_creacion.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        txt_f_modificacion.setEditable(false);
        txt_f_modificacion.setBackground(new java.awt.Color(255, 255, 255));
        txt_f_modificacion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_f_modificacion.setHorizontalAlignment(javax.swing.JTextField.LEFT);

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

        dialog_crear_cliente.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel20.setBackground(new java.awt.Color(0, 110, 204));
        jPanel20.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cliente_32_32.png"))); // NOI18N
        jLabel23.setText("Crear Cliente");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_crear_cliente.getContentPane().add(jPanel20, java.awt.BorderLayout.PAGE_START);

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
                .addGap(0, 205, Short.MAX_VALUE)
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
        jLabel24.setText("Razon Social:");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 51, 153));
        jLabel25.setText("R.U.C.:");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 51, 153));
        jLabel26.setText("Dirección:");

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

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 51, 153));
        jLabel27.setText("Teléfono:");

        txt_telefono_cliente_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_telefono_cliente_crear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_telefono_cliente_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telefono_cliente_crearKeyTyped(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 51, 153));
        jLabel28.setText("Celular:");

        txt_celular_cliente_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_celular_cliente_crear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_celular_cliente_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_celular_cliente_crearKeyTyped(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 51, 153));
        jLabel29.setText("Correo:");

        txt_correo_cliente_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_correo_cliente_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_correo_cliente_crearKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_direccion_cliente_crear)
                    .addComponent(txt_razon_social_cliente_crear)
                    .addComponent(txt_correo_cliente_crear, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_celular_cliente_crear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                            .addComponent(txt_ruc_cliente_crear, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_telefono_cliente_crear, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txt_razon_social_cliente_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txt_ruc_cliente_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txt_direccion_cliente_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txt_telefono_cliente_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txt_celular_cliente_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txt_correo_cliente_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel21.add(jPanel23, java.awt.BorderLayout.CENTER);

        dialog_crear_cliente.getContentPane().add(jPanel21, java.awt.BorderLayout.CENTER);

        dialog_buscar_cliente.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setPreferredSize(new java.awt.Dimension(400, 60));
        jPanel24.setLayout(new java.awt.BorderLayout());

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

        jPanel24.add(jToolBar2, java.awt.BorderLayout.CENTER);

        jPanel34.setBackground(new java.awt.Color(0, 110, 204));
        jPanel34.setPreferredSize(new java.awt.Dimension(418, 35));

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_32_32.png"))); // NOI18N
        jLabel30.setText("Buscar Cliente");
        jLabel30.setPreferredSize(new java.awt.Dimension(144, 35));

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
            .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
            .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel24.add(jPanel34, java.awt.BorderLayout.NORTH);

        dialog_buscar_cliente.getContentPane().add(jPanel24, java.awt.BorderLayout.PAGE_START);

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setLayout(new java.awt.BorderLayout());

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setPreferredSize(new java.awt.Dimension(418, 40));

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

        btn_nuevo_cliente_buscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo_24_24.png"))); // NOI18N
        btn_nuevo_cliente_buscar.setText("Nuevo");
        btn_nuevo_cliente_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevo_cliente_buscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addComponent(btn_nuevo_cliente_buscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                .addComponent(btn_cliente_seleccionar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cliente_cancelar_busqueda))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cliente_cancelar_busqueda)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_cliente_seleccionar)
                        .addComponent(btn_nuevo_cliente_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel26, java.awt.BorderLayout.PAGE_END);

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));

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

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
        );

        jPanel25.add(jPanel27, java.awt.BorderLayout.CENTER);

        dialog_buscar_cliente.getContentPane().add(jPanel25, java.awt.BorderLayout.CENTER);

        dialog_buscar_factura.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel47.setBackground(new java.awt.Color(255, 255, 255));
        jPanel47.setPreferredSize(new java.awt.Dimension(400, 60));
        jPanel47.setLayout(new java.awt.BorderLayout());

        jToolBar3.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar3.setFloatable(false);
        jToolBar3.setPreferredSize(new java.awt.Dimension(13, 25));

        jLabel35.setText("Buscar:");
        jToolBar3.add(jLabel35);

        txt_buscar_factura.setColumns(50);
        txt_buscar_factura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_buscar_facturaKeyReleased(evt);
            }
        });
        jToolBar3.add(txt_buscar_factura);

        jPanel47.add(jToolBar3, java.awt.BorderLayout.CENTER);

        jPanel33.setBackground(new java.awt.Color(0, 110, 204));
        jPanel33.setPreferredSize(new java.awt.Dimension(418, 35));

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar2_32_32.png"))); // NOI18N
        jLabel38.setText("Buscar Factura");
        jLabel38.setPreferredSize(new java.awt.Dimension(144, 35));

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
            .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel33Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
            .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel33Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel47.add(jPanel33, java.awt.BorderLayout.NORTH);

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

        panel_norte.setBackground(new java.awt.Color(0, 110, 204));

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/reporte_usuario.png"))); // NOI18N
        jLabel36.setText("Reporte de Facturas");

        javax.swing.GroupLayout panel_norteLayout = new javax.swing.GroupLayout(panel_norte);
        panel_norte.setLayout(panel_norteLayout);
        panel_norteLayout.setHorizontalGroup(
            panel_norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );
        panel_norteLayout.setVerticalGroup(
            panel_norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialogo_reporte_factura.getContentPane().add(panel_norte, java.awt.BorderLayout.PAGE_START);

        panel_cuerpo.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        panel_fechas.setBackground(new java.awt.Color(255, 255, 255));
        panel_fechas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Intervalo de tiempo del Reporte", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        lb_fecha1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb_fecha1.setForeground(new java.awt.Color(0, 51, 153));
        lb_fecha1.setText("Fecha inicio:");

        txt_fecha_inicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txt_fecha_inicioMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txt_fecha_inicioMouseReleased(evt);
            }
        });

        lb_fecha2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb_fecha2.setForeground(new java.awt.Color(0, 51, 153));
        lb_fecha2.setText("Fecha fin:");

        txt_fecha_fin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txt_fecha_finMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout panel_fechasLayout = new javax.swing.GroupLayout(panel_fechas);
        panel_fechas.setLayout(panel_fechasLayout);
        panel_fechasLayout.setHorizontalGroup(
            panel_fechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_fechasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_fecha1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_fecha_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                .addComponent(lb_fecha2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_fecha_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panel_fechasLayout.setVerticalGroup(
            panel_fechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_fechasLayout.createSequentialGroup()
                .addGroup(panel_fechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_fechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lb_fecha2)
                        .addComponent(txt_fecha_fin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_fechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lb_fecha1)
                        .addComponent(txt_fecha_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_seleccione_cliente.setBackground(new java.awt.Color(255, 255, 255));
        panel_seleccione_cliente.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Seleccione el Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        rdio_cliente_todos.setBackground(new java.awt.Color(255, 255, 255));
        seleccione_el_cliente.add(rdio_cliente_todos);
        rdio_cliente_todos.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rdio_cliente_todos.setForeground(new java.awt.Color(0, 51, 153));
        rdio_cliente_todos.setText("Todos");
        rdio_cliente_todos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdio_cliente_todosItemStateChanged(evt);
            }
        });

        rdio_cliente_especifico.setBackground(new java.awt.Color(255, 255, 255));
        seleccione_el_cliente.add(rdio_cliente_especifico);
        rdio_cliente_especifico.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rdio_cliente_especifico.setForeground(new java.awt.Color(0, 51, 153));
        rdio_cliente_especifico.setText("Específico");
        rdio_cliente_especifico.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdio_cliente_especificoItemStateChanged(evt);
            }
        });

        btn_buscar_cliente_filtro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_10_10.png"))); // NOI18N
        btn_buscar_cliente_filtro.setToolTipText("Buscar Cliente");
        btn_buscar_cliente_filtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar_cliente_filtroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_seleccione_clienteLayout = new javax.swing.GroupLayout(panel_seleccione_cliente);
        panel_seleccione_cliente.setLayout(panel_seleccione_clienteLayout);
        panel_seleccione_clienteLayout.setHorizontalGroup(
            panel_seleccione_clienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_seleccione_clienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdio_cliente_todos)
                .addGap(18, 18, 18)
                .addComponent(rdio_cliente_especifico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_cliente_filtro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_buscar_cliente_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panel_seleccione_clienteLayout.setVerticalGroup(
            panel_seleccione_clienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_seleccione_clienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_seleccione_clienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_buscar_cliente_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_seleccione_clienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdio_cliente_todos)
                        .addComponent(rdio_cliente_especifico)
                        .addComponent(txt_cliente_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_mostrar_solo_facturas.setBackground(new java.awt.Color(255, 255, 255));
        panel_mostrar_solo_facturas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Mostrar sólo las Facturas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        rdio_factura_todos.setBackground(new java.awt.Color(255, 255, 255));
        mostrar_solo_las_facturas.add(rdio_factura_todos);
        rdio_factura_todos.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rdio_factura_todos.setForeground(new java.awt.Color(0, 51, 153));
        rdio_factura_todos.setText("Todos");

        rdio_factura_por_pagar.setBackground(new java.awt.Color(255, 255, 255));
        mostrar_solo_las_facturas.add(rdio_factura_por_pagar);
        rdio_factura_por_pagar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rdio_factura_por_pagar.setForeground(new java.awt.Color(0, 51, 153));
        rdio_factura_por_pagar.setText("Por Cobrar");

        rdio_factura_anulados.setBackground(new java.awt.Color(255, 255, 255));
        mostrar_solo_las_facturas.add(rdio_factura_anulados);
        rdio_factura_anulados.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rdio_factura_anulados.setForeground(new java.awt.Color(0, 51, 153));
        rdio_factura_anulados.setText("Anulados");

        rdio_factura_pagados.setBackground(new java.awt.Color(255, 255, 255));
        mostrar_solo_las_facturas.add(rdio_factura_pagados);
        rdio_factura_pagados.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rdio_factura_pagados.setForeground(new java.awt.Color(0, 51, 153));
        rdio_factura_pagados.setText("Cobrados");

        javax.swing.GroupLayout panel_mostrar_solo_facturasLayout = new javax.swing.GroupLayout(panel_mostrar_solo_facturas);
        panel_mostrar_solo_facturas.setLayout(panel_mostrar_solo_facturasLayout);
        panel_mostrar_solo_facturasLayout.setHorizontalGroup(
            panel_mostrar_solo_facturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_mostrar_solo_facturasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdio_factura_todos)
                .addGap(18, 18, 18)
                .addComponent(rdio_factura_por_pagar)
                .addGap(18, 18, 18)
                .addComponent(rdio_factura_anulados)
                .addGap(18, 18, 18)
                .addComponent(rdio_factura_pagados)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_mostrar_solo_facturasLayout.setVerticalGroup(
            panel_mostrar_solo_facturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_mostrar_solo_facturasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_mostrar_solo_facturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdio_factura_todos)
                    .addComponent(rdio_factura_por_pagar)
                    .addComponent(rdio_factura_anulados)
                    .addComponent(rdio_factura_pagados))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_fechas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_seleccione_cliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_mostrar_solo_facturas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_fechas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_seleccione_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_mostrar_solo_facturas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(99, Short.MAX_VALUE))
        );

        panel_cuerpo.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel10.setPreferredSize(new java.awt.Dimension(600, 40));

        btn_generar_reporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/generar_reporte_32_32.png"))); // NOI18N
        btn_generar_reporte.setText("Generar");
        btn_generar_reporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_generar_reporteActionPerformed(evt);
            }
        });

        btn_cancelar_reporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cancelar_reporte.setText("Cancelar");
        btn_cancelar_reporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_reporteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 376, Short.MAX_VALUE)
                .addComponent(btn_generar_reporte)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar_reporte))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cancelar_reporte)
                    .addComponent(btn_generar_reporte))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panel_cuerpo.add(jPanel10, java.awt.BorderLayout.SOUTH);

        dialogo_reporte_factura.getContentPane().add(panel_cuerpo, java.awt.BorderLayout.CENTER);

        dialog_pago.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel28.setBackground(new java.awt.Color(0, 110, 204));
        jPanel28.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pagar_32_32.png"))); // NOI18N
        jLabel37.setText("Pago");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_pago.getContentPane().add(jPanel28, java.awt.BorderLayout.PAGE_START);

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setLayout(new java.awt.BorderLayout());

        jPanel30.setPreferredSize(new java.awt.Dimension(418, 40));

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        jButton12.setText("Cancelar");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        jButton13.setText("Guardar");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        jButton14.setText("Modificar");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        jButton15.setText("Eliminar");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addComponent(jButton14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(jButton13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton12)
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton13)
                        .addComponent(jButton14)
                        .addComponent(jButton15)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel29.add(jPanel30, java.awt.BorderLayout.PAGE_END);

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Factura", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(0, 51, 153));
        jLabel39.setText("Serie:");

        txt_serie_pago.setEditable(false);
        txt_serie_pago.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(0, 51, 153));
        jLabel45.setText("Número:");

        txt_serie_pago1.setEditable(false);
        txt_serie_pago1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_serie_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_serie_pago1)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(txt_serie_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45)
                    .addComponent(txt_serie_pago1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Banco", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(0, 51, 153));
        jLabel40.setText("Banco:");

        cbo_banco.setEditable(true);
        cbo_banco.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_banco.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_bancoItemStateChanged(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 51, 153));
        jLabel41.setText("Nro. de cuenta:");

        txt_nro_cuenta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_nro_cuenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nro_cuentaKeyTyped(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(0, 51, 153));
        jLabel46.setText("Nro. de Operación:");

        txt_nro_operacion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_nro_operacion.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_nro_operacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nro_operacionKeyTyped(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(0, 51, 153));
        jLabel47.setText("Fecha de Pago:");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbo_banco, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_nro_cuenta, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_nro_operacion)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(txt_fecha1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 146, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(txt_fecha1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(cbo_banco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(txt_nro_cuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(txt_nro_operacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pago", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(0, 51, 153));
        jLabel43.setText("Monto Dolar:");
        jLabel43.setPreferredSize(new java.awt.Dimension(113, 15));

        txt_monto_dolar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_monto_dolar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_monto_dolarKeyTyped(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 51, 153));
        jLabel48.setText("Tipo de cambio:");
        jLabel48.setPreferredSize(new java.awt.Dimension(113, 15));

        txt_tipo_cambio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_tipo_cambio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_tipo_cambioKeyTyped(evt);
            }
        });

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(0, 51, 153));
        jLabel49.setText("Monto Depositado:");
        jLabel49.setPreferredSize(new java.awt.Dimension(113, 15));

        txt_tipo_cambio1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_tipo_cambio1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_tipo_cambio1KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txt_tipo_cambio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(txt_monto_dolar, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_tipo_cambio1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_monto_dolar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_tipo_cambio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_tipo_cambio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel29.add(jPanel31, java.awt.BorderLayout.CENTER);

        dialog_pago.getContentPane().add(jPanel29, java.awt.BorderLayout.CENTER);

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(0, 110, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/factura_32_32.png"))); // NOI18N
        jLabel1.setText("Factura");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1025, Short.MAX_VALUE)
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
        jToolBar1.add(jSeparator5);

        btn_pagar.setBackground(new java.awt.Color(255, 255, 255));
        btn_pagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pagar_24_24.png"))); // NOI18N
        btn_pagar.setText("Pagar");
        btn_pagar.setFocusable(false);
        btn_pagar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_pagar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_pagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pagarActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_pagar);
        jToolBar1.add(jSeparator6);

        btn_listar_por.setBackground(new java.awt.Color(255, 255, 255));
        btn_listar_por.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ver_por_24_24.png"))); // NOI18N
        btn_listar_por.setText("Reporte");
        btn_listar_por.setFocusable(false);
        btn_listar_por.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_listar_por.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_listar_por.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_listar_porActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_listar_por);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        barra_buscar.setBackground(new java.awt.Color(255, 255, 255));
        barra_buscar.setFloatable(false);
        barra_buscar.setOpaque(false);
        barra_buscar.setPreferredSize(new java.awt.Dimension(13, 25));

        label_buscar.setBackground(new java.awt.Color(255, 255, 255));
        label_buscar.setText("Buscar:");
        label_buscar.setMaximumSize(new java.awt.Dimension(50, 25));
        label_buscar.setMinimumSize(new java.awt.Dimension(50, 25));
        label_buscar.setPreferredSize(new java.awt.Dimension(50, 25));
        label_buscar.setRequestFocusEnabled(false);
        barra_buscar.add(label_buscar);

        txt_buscar.setColumns(30);
        txt_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_buscarKeyReleased(evt);
            }
        });
        barra_buscar.add(txt_buscar);

        jLabel3.setText("      Estado:");
        barra_buscar.add(jLabel3);

        cbo_estado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TODOS", "POR VENCER", "VENCIDO", "POR COBRAR", "COBRADO", "ANULADO" }));
        cbo_estado.setToolTipText("Seleccione un estado de Factura");
        cbo_estado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_estadoItemStateChanged(evt);
            }
        });
        barra_buscar.add(cbo_estado);

        jLabel15.setText("     Cliente:");
        barra_buscar.add(jLabel15);

        cbo_buscar_cliente.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_buscar_clienteItemStateChanged(evt);
            }
        });
        barra_buscar.add(cbo_buscar_cliente);

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
        //tabla_general.setDefaultRenderer(Object.class, new RenderFactura());

        javax.swing.GroupLayout panel_tablaLayout = new javax.swing.GroupLayout(panel_tabla);
        panel_tabla.setLayout(panel_tablaLayout);
        panel_tablaLayout.setHorizontalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        panel_tablaLayout.setVerticalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
        );

        jPanel5.add(panel_tabla, java.awt.BorderLayout.LINE_START);

        Panel_detalle.setLayout(new java.awt.BorderLayout());

        norte.setBackground(new java.awt.Color(255, 255, 255));
        norte.setPreferredSize(new java.awt.Dimension(458, 40));

        lbl_titulo.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_titulo.setForeground(new java.awt.Color(0, 110, 204));
        lbl_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_titulo.setText("Información de Factura");

        txt_numero_factura.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_numero_factura.setForeground(new java.awt.Color(0, 110, 204));
        txt_numero_factura.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        lbl_numero.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_numero.setForeground(new java.awt.Color(0, 110, 204));
        lbl_numero.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_numero.setText("Número:");

        txt_serie.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txt_serie.setForeground(new java.awt.Color(0, 110, 204));
        txt_serie.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        lbl_serie.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_serie.setForeground(new java.awt.Color(0, 110, 204));
        lbl_serie.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_serie.setText("Serie:");

        javax.swing.GroupLayout norteLayout = new javax.swing.GroupLayout(norte);
        norte.setLayout(norteLayout);
        norteLayout.setHorizontalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
            .addGroup(norteLayout.createSequentialGroup()
                .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_serie)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_numero)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_numero_factura, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        norteLayout.setVerticalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(norteLayout.createSequentialGroup()
                .addGroup(norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(txt_numero_factura, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(lbl_numero, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(txt_serie, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(lbl_serie))
                .addGap(1, 1, 1)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Panel_detalle.add(norte, java.awt.BorderLayout.PAGE_START);

        centro.setBackground(new java.awt.Color(153, 255, 204));
        centro.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(771, 140));

        Cliente.setBackground(new java.awt.Color(255, 255, 255));
        Cliente.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 51, 153));
        jLabel11.setText("Razon Social:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 153));
        jLabel12.setText("Dirección:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 153));
        jLabel13.setText("R.U.C.:");

        cbo_razon_social.setEditable(true);
        cbo_razon_social.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_razon_social.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_razon_socialItemStateChanged(evt);
            }
        });

        txt_direccion.setEditable(false);
        txt_direccion.setBackground(new java.awt.Color(255, 255, 255));
        txt_direccion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txt_ruc.setEditable(false);
        txt_ruc.setBackground(new java.awt.Color(255, 255, 255));
        txt_ruc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_ruc.setHorizontalAlignment(javax.swing.JTextField.LEFT);

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

        javax.swing.GroupLayout ClienteLayout = new javax.swing.GroupLayout(Cliente);
        Cliente.setLayout(ClienteLayout);
        ClienteLayout.setHorizontalGroup(
            ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ClienteLayout.createSequentialGroup()
                        .addComponent(txt_ruc, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_nuevo_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_direccion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(ClienteLayout.createSequentialGroup()
                        .addComponent(cbo_razon_social, 0, 1, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_buscar_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        ClienteLayout.setVerticalGroup(
            ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ClienteLayout.createSequentialGroup()
                .addGroup(ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(cbo_razon_social, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_buscar_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txt_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(txt_ruc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_nuevo_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        Datos.setBackground(new java.awt.Color(255, 255, 255));
        Datos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        lb_fecha.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb_fecha.setForeground(new java.awt.Color(0, 51, 153));
        lb_fecha.setText("Fecha:");

        lb_moneda.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb_moneda.setForeground(new java.awt.Color(0, 51, 153));
        lb_moneda.setText("Moneda:");

        cbo_moneda.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_moneda.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_monedaItemStateChanged(evt);
            }
        });

        lb_ordendecompra.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb_ordendecompra.setForeground(new java.awt.Color(0, 51, 153));
        lb_ordendecompra.setText("O.C.:");

        javax.swing.GroupLayout DatosLayout = new javax.swing.GroupLayout(Datos);
        Datos.setLayout(DatosLayout);
        DatosLayout.setHorizontalGroup(
            DatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lb_ordendecompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_moneda, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_fecha, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(DatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_fecha, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                    .addComponent(cbo_moneda, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_ordencompra)))
        );
        DatosLayout.setVerticalGroup(
            DatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DatosLayout.createSequentialGroup()
                .addGroup(DatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_fecha)
                    .addComponent(txt_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(DatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_moneda)
                    .addComponent(cbo_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(DatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_ordendecompra)
                    .addComponent(txt_ordencompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        condiciones_pago.setBackground(new java.awt.Color(255, 255, 255));
        condiciones_pago.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Condiciones de pago", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        lb_tipopago.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb_tipopago.setForeground(new java.awt.Color(0, 51, 153));
        lb_tipopago.setText("Pago:");

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 51, 153));
        jLabel33.setText("Vencimiento:");

        cbo_tipopago.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_tipopago.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                accionCombo(evt);
            }
        });

        txt_vencimiento.setEditable(false);
        txt_vencimiento.setBackground(new java.awt.Color(255, 255, 255));

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 51, 153));
        jLabel34.setText("Guia:");

        txt_guia.setEditable(false);
        txt_guia.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout condiciones_pagoLayout = new javax.swing.GroupLayout(condiciones_pago);
        condiciones_pago.setLayout(condiciones_pagoLayout);
        condiciones_pagoLayout.setHorizontalGroup(
            condiciones_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(condiciones_pagoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(condiciones_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_tipopago, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(condiciones_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbo_tipopago, 0, 174, Short.MAX_VALUE)
                    .addComponent(txt_vencimiento)
                    .addComponent(txt_guia)))
        );
        condiciones_pagoLayout.setVerticalGroup(
            condiciones_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(condiciones_pagoLayout.createSequentialGroup()
                .addGroup(condiciones_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_tipopago)
                    .addComponent(cbo_tipopago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(condiciones_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(txt_vencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(condiciones_pagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(txt_guia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Datos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(condiciones_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Cliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Datos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Cliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(condiciones_pago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        centro.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setPreferredSize(new java.awt.Dimension(725, 50));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 153));
        jLabel4.setText("Valor Neto:");

        txt_sub_total.setEditable(false);
        txt_sub_total.setBackground(new java.awt.Color(255, 255, 255));
        txt_sub_total.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_sub_total.setForeground(new java.awt.Color(204, 0, 0));
        txt_sub_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 51, 153));
        jLabel5.setText("Impuesto:");

        cbo_igv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_igv.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_igvItemStateChanged(evt);
            }
        });

        txt_igv_calculo.setEditable(false);
        txt_igv_calculo.setBackground(new java.awt.Color(255, 255, 255));
        txt_igv_calculo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_igv_calculo.setForeground(new java.awt.Color(204, 0, 0));
        txt_igv_calculo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 153));
        jLabel6.setText("Precio de Venta:");

        txt_total.setEditable(false);
        txt_total.setBackground(new java.awt.Color(255, 255, 255));
        txt_total.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_total.setForeground(new java.awt.Color(204, 0, 0));
        txt_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txt_estado.setBackground(new java.awt.Color(204, 0, 102));
        txt_estado.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        txt_estado.setForeground(new java.awt.Color(204, 0, 0));
        txt_estado.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_estado.setText("PAGADO");

        lbl_estado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_estado.setForeground(new java.awt.Color(0, 51, 153));
        lbl_estado.setText("Estado de Factura:");

        txt_descuentoglobal.setEditable(false);
        txt_descuentoglobal.setBackground(new java.awt.Color(255, 255, 255));
        txt_descuentoglobal.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txt_descuentoglobal.setForeground(new java.awt.Color(204, 0, 0));
        txt_descuentoglobal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 51, 153));
        jLabel14.setText("Descuento:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_estado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_estado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_descuentoglobal, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_sub_total, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(cbo_igv, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_igv_calculo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_total, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jLabel5)
                        .addComponent(jLabel4)
                        .addComponent(lbl_estado)
                        .addComponent(jLabel14))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_sub_total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_igv_calculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_estado)
                            .addComponent(txt_total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbo_igv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_descuentoglobal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        centro.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new java.awt.BorderLayout());

        panel_nuevo_detalle.setBackground(new java.awt.Color(204, 255, 255));
        panel_nuevo_detalle.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Detalle", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N
        panel_nuevo_detalle.setPreferredSize(new java.awt.Dimension(840, 130));

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
        jLabel19.setText("Precio Unitario");

        txt_precio_unitario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_precio_unitario.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_precio_unitario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_precio_unitarioKeyTyped(evt);
            }
        });

        btn_cancelar_guardar_detalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_10_10.png"))); // NOI18N
        btn_cancelar_guardar_detalle.setToolTipText("Cancelar");
        btn_cancelar_guardar_detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_guardar_detalleActionPerformed(evt);
            }
        });

        txt_descuento.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_descuento.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_descuento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_descuentoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_descuentoKeyTyped(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 51, 153));
        jLabel20.setText("Descuento %");

        javax.swing.GroupLayout panel_nuevo_detalleLayout = new javax.swing.GroupLayout(panel_nuevo_detalle);
        panel_nuevo_detalle.setLayout(panel_nuevo_detalleLayout);
        panel_nuevo_detalleLayout.setHorizontalGroup(
            panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_unidad, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_nuevo_detalleLayout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_nuevo_detalleLayout.createSequentialGroup()
                        .addComponent(txt_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_precio_unitario, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addComponent(txt_descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_guardar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_cancelar_guardar_detalle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel20))
                .addContainerGap())
        );
        panel_nuevo_detalleLayout.setVerticalGroup(
            panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_unidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_precio_unitario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_descuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addComponent(btn_guardar_detalle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cancelar_guardar_detalle)))
                .addGap(0, 31, Short.MAX_VALUE))
        );

        jPanel8.add(panel_nuevo_detalle, java.awt.BorderLayout.PAGE_START);

        jPanel17.setBackground(new java.awt.Color(255, 153, 153));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setPreferredSize(new java.awt.Dimension(840, 35));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 51, 153));
        jLabel22.setText("SON:");

        txt_total_letras.setEditable(false);
        txt_total_letras.setBackground(new java.awt.Color(255, 255, 255));
        txt_total_letras.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 51, 153));
        jLabel32.setText("OBSERVACIONES:");

        txt_observaciones.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_total_letras, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_observaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txt_total_letras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(txt_observaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel17.add(jPanel18, java.awt.BorderLayout.PAGE_END);

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

        btn_importar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/importar_10_10.png"))); // NOI18N
        btn_importar.setToolTipText("Importar Detalles");
        btn_importar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_importarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_nuevo_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_importar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(btn_nuevo_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_importar, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 50, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
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
                .addGap(0, 120, Short.MAX_VALUE)
                .addComponent(btn_vista_previa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_creacion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_imprimir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_guardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar)
                .addGap(20, 20, 20))
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
        dialog_fecha_creacion.setLocationRelativeTo(factura);
        dialog_fecha_creacion.setModal(true);
        dialog_fecha_creacion.setVisible(true);
    }//GEN-LAST:event_btn_creacionActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        dialog_fecha_creacion.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btn_guardar_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardar_detalleActionPerformed
        if (txt_cantidad.getText().length() == 0 || txt_unidad.getText().length() == 0 || txt_precio_unitario.getText().length() == 0 || txt_detalle.getText().length() == 0) {
            System.out.println("\nFalta ingresar los campos Cantidad, Unidad, Precio Unitario y Descripcion");
            JOptionPane.showMessageDialog(null, "Por Favor Ingrese los campos Cantidad, Unidad, Precio Unitario y Descripcion", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            int id_factura = id_factura_global;
            BigDecimal cantidad = new BigDecimal(txt_cantidad.getText().trim());
            String unidad = txt_unidad.getText();
            String descripcion = txt_detalle.getText();
            BigDecimal precio_unitario = new BigDecimal(txt_precio_unitario.getText().trim());
            BigDecimal descuento_por = new BigDecimal(0);
            BigDecimal descuento_val = new BigDecimal(0);

            if (txt_descuento.getText().equals("0") || txt_descuento.getText().equals("")) {
                descuento_por = new BigDecimal(0);
                descuento_val = new BigDecimal(0);
            } else {
                descuento_por = new BigDecimal(txt_descuento.getText().trim());
            }

            BigDecimal cero = new BigDecimal(0);
            BigDecimal cien = new BigDecimal(100);
            
            if (descuento_por.doubleValue() > 0) {
                System.out.println("aplicara el descuento");
                descuento_val = (cantidad.multiply(precio_unitario).multiply(descuento_por)).divide(cien);
                System.out.println("El descuento valor es: "+descuento_val);
                
            }

            BigDecimal precio_total = (precio_unitario.multiply(cantidad)).subtract(descuento_val);
            System.out.println("Llamamo a la funcion crear Factura_detalle");

            System.out.println("Cantidad: "+cantidad);
            System.out.println("precio_unitario: "+precio_unitario);
            System.out.println("descuento_por: "+descuento_por);
            System.out.println("descuento_val: "+descuento_val);
            System.out.println("precio_total: "+precio_total);
            
            
            if (crear0_modificar1_factura_detalle == 0) {
                crear_Factura_Detalle(id_factura, cantidad, unidad, descripcion, precio_unitario, precio_total, descuento_por, descuento_val);
            } else {
                int id_detalle_factura = id_factura_detalle_global;
                modificar_Factura_Detalle(id_detalle_factura, id_factura, cantidad, unidad, descripcion, precio_unitario, precio_total, descuento_por, descuento_val);
            }

        }
    }//GEN-LAST:event_btn_guardar_detalleActionPerformed

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        System.out.println("\ncrear Factura");
        System.out.println("=================");

        System.out.println("inicializar ID's globales");
        crear0_modificar1_factura = 0;
        numero_inicial_global = "";

        //JOptionPane.showMessageDialog(null, "Crear nueva factura", "", JOptionPane.INFORMATION_MESSAGE);

        if (clase_factura.crear(id_empresa_index, id_usuario_index)) {
            System.out.println("\nLa Factura se logró registrar exitosamente!");

            band_index = 1;
            crear0_modificar1_factura = 1;
            band_mantenimiento_factura_detalle = 0;
            System.out.println("Se procede a igualar el crear0_modificar1_factura: " + crear0_modificar1_factura);

            System.out.println("cambiando el titulo");
            lbl_titulo.setText("Nueva Factura");

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
//            btn_ver_pago.setVisible(false);

            System.out.println("mostrar botones: guardar, cancelar");
            btn_guardar.setVisible(true);
            btn_cancelar.setVisible(true);
            btn_guardar.setText("Guardar");
            //btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("imagenes\\guardar_32_32.png")));
            btn_guardar.setEnabled(false);

            System.out.println("desactivar barra de herramientas");
            activar_barra_herramientas("desactivar");

            System.out.println("mostrar botones");
            btn_nuevo_detalle.setVisible(true);
            btn_importar.setVisible(true);
            btn_buscar_cliente.setVisible(true);
            btn_nuevo_cliente.setVisible(true);


            System.out.println("Ejecutando la consulta para saber cual es el ultimo id_factura generado");
            Factura_id_ultimo();

            System.out.println("Mostrar tabla Factura_detalle_vacia");
            Mostrar_tabla_factura_detalle_vacia();

            System.out.println("Mostrar Combo Cliente");
            if (band_cbo_cliente == 0) {
                Mostrar_combo_cliente();
            }

            System.out.println("Mostrar Combo IGV");
            Mostrar_combo_igv();

            System.out.println("Mostrar Combo Moneda");
            inicializar_cbo_moneda();

            System.out.println("Mostrar Combo Tipo de pago");
            Mostrar_combo_tipopago();


            System.out.println("Ejecutando consulta de id y serie de documento FACTURA");
            String serie = serie_documento_factura();
            System.out.println("La serie del Documento Factura es: " + serie);

            System.out.println("Mostrar la serie en el lbl serie");
            txt_serie.setText(serie);

            System.out.println("Mostrar Numero de Factura generada");
            Mostrar_numero_factura();

            System.out.println("mostrar el panel de detalle de datos");
            Panel_detalle.setVisible(true);
            panel_nuevo_detalle.setVisible(false);
            lbl_estado.setVisible(false);

        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al crear la Factura. \nPor favor inténtelo nuevamente.", "ERROR: desde el boton", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModificarActionPerformed
        int fil;
        fil = tabla_detalle.getSelectedRow();
        if (fil == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar el registro a Modificar");
        } else {

            int id_detalle_factura;
            String Cantidad;
            String Unidad;
            String detalle;
            String precio_unitario;
            String descuento_por;

            m = (DefaultTableModel) tabla_detalle.getModel();
            id_detalle_factura = Integer.parseInt((String) m.getValueAt(fil, 0));
            detalle = (String) m.getValueAt(fil, 1);
            Unidad = (String) m.getValueAt(fil, 2);
            Cantidad = (String) m.getValueAt(fil, 3);
            precio_unitario = (String) m.getValueAt(fil, 4);
            descuento_por = (String) m.getValueAt(fil, 5);

            id_factura_detalle_global = id_detalle_factura;
            txt_cantidad.setText(Cantidad);
            txt_unidad.setText(Unidad);
            txt_precio_unitario.setText(precio_unitario);
            txt_detalle.setText(detalle);
            txt_descuento.setText(descuento_por);

            crear0_modificar1_factura_detalle = 1;

            panel_nuevo_detalle.setVisible(true);
            btn_guardar_detalle.setVisible(true);
            btn_nuevo_detalle.setVisible(false);
            btn_importar.setVisible(false);
        }
    }//GEN-LAST:event_ModificarActionPerformed

    private void tabla_detalleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_detalleMouseClicked
        if (band_mantenimiento_factura_detalle == 0) {
            if (evt.getButton() == 3) {
                mantenimiento_tabla_detalle_factura.show(tabla_detalle, evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_tabla_detalleMouseClicked

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        System.out.println("limpiar cajas de texto");
        limpiar_caja_texto_crear_cliente();

        System.out.println("actualizamos tabla cliente");
        Mostrar_tabla_cliente();

        dialog_crear_cliente.dispose();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void btn_nuevo_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_clienteActionPerformed
        dialog_crear_cliente.setSize(429, 350);
        dialog_crear_cliente.setLocationRelativeTo(factura);
        dialog_crear_cliente.setModal(true);
        dialog_crear_cliente.setVisible(true);
    }//GEN-LAST:event_btn_nuevo_clienteActionPerformed

    private void txt_cantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cantidadKeyTyped
        JTextField caja = txt_cantidad;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_cantidadKeyTyped

    private void txt_unidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_unidadKeyTyped
        JTextField caja = txt_unidad;
        int limite = 10;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_unidadKeyTyped

    private void txt_precio_unitarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_precio_unitarioKeyTyped
        JTextField caja = txt_precio_unitario;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_precio_unitarioKeyTyped

    private void txt_detalleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_detalleKeyTyped
        JTextArea caja = txt_detalle;
        int limite = 1000;
        tamaño_de_caja_jtextarea(caja, evt, limite);
    }//GEN-LAST:event_txt_detalleKeyTyped

    private void btn_buscar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_clienteActionPerformed
        Mostrar_tabla_cliente();
        dialog_buscar_cliente.setSize(700, 400);
        dialog_buscar_cliente.setLocationRelativeTo(factura);
        dialog_buscar_cliente.setModal(true);
        dialog_buscar_cliente.setVisible(true);
    }//GEN-LAST:event_btn_buscar_clienteActionPerformed

    private void btn_nuevo_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_detalleActionPerformed
        panel_nuevo_detalle.setVisible(true);
        btn_guardar_detalle.setVisible(true);
        btn_nuevo_detalle.setVisible(false);
        btn_importar.setVisible(false);
    }//GEN-LAST:event_btn_nuevo_detalleActionPerformed

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



            if (ventana_buscar_cliente.equals("")) {
                id_cliente_global = id_cliente;
                Mostrar_combo_cliente_buscar(razon_social);
                txt_direccion.setText(direccion);
                txt_ruc.setText(ruc);
            }

            if (ventana_buscar_cliente.equals("dialogo_reporte_factura")) {
                ventana_buscar_cliente = "";
                txt_cliente_filtro.setText(razon_social);
            }

            txt_buscar_cliente.setText("");
            dialog_buscar_cliente.dispose();
        }
    }//GEN-LAST:event_btn_cliente_seleccionarActionPerformed

    private void txt_buscar_clienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_clienteKeyReleased
        ResultSet r;
        String bus = txt_buscar_cliente.getText();

        try {
            System.out.println("select id_cliente, razon_social, direccion, ruc from tcliente where (razon_social like '%" + bus + "%'  or ruc like '%" + bus + "%' or direccion like '%" + bus + "%' or telefono like '%" + bus + "%' or celular like '%" + bus + "%' or correo like '%" + bus + "%')  order by razon_social asc");
            r = sentencia.executeQuery("select id_cliente, razon_social, direccion, ruc from tcliente where (razon_social like '%" + bus + "%'  or ruc like '%" + bus + "%' or direccion like '%" + bus + "%' or telefono like '%" + bus + "%' or celular like '%" + bus + "%' or correo like '%" + bus + "%') order by razon_social asc");

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

            Mostrar_combo_cliente_buscar(razon_social);
            txt_direccion.setText(direccion);
            txt_ruc.setText(ruc);

            txt_buscar_cliente.setText("");
            dialog_buscar_cliente.dispose();
        }
    }//GEN-LAST:event_tabla_cliente_buscarKeyPressed

    private void cbo_razon_socialItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_razon_socialItemStateChanged
        System.out.println("\nSe ejecuta la busqueda del cliente");

        if (cbo_razon_social.getSelectedItem().toString().trim().length() >= 1) {
            String razon_social = cbo_razon_social.getSelectedItem().toString().trim();
            String id_cliente;
            String direccion;
            String ruc;

            try {
                ResultSet r = sentencia.executeQuery("select id_cliente, direccion, ruc from tcliente where razon_social = '" + razon_social + "'");
                while (r.next()) {
                    id_cliente = r.getString("id_cliente").trim();
                    System.out.println("El id_cliente es: " + id_cliente);

                    direccion = r.getString("direccion").trim();
                    System.out.println("La direcion es: " + direccion);

                    ruc = r.getString("ruc").trim();
                    System.out.println("El RUC es: " + ruc);

                    System.out.println("El id_cliente_global es: " + id_cliente);
                    id_cliente_global = Integer.parseInt(id_cliente);
                    txt_direccion.setText(direccion);
                    txt_ruc.setText(ruc);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrio al Extraer el los Datos del Cliente", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_cbo_razon_socialItemStateChanged

    private void btn_nuevo_cliente_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevo_cliente_buscarActionPerformed
        txt_buscar_cliente.setText("");
        dialog_buscar_cliente.dispose();

        dialog_crear_cliente.setSize(429, 350);
        dialog_crear_cliente.setLocationRelativeTo(factura);
        dialog_crear_cliente.setModal(true);
        dialog_crear_cliente.setVisible(true);
    }//GEN-LAST:event_btn_nuevo_cliente_buscarActionPerformed

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

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
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
    }//GEN-LAST:event_jButton11ActionPerformed

    private void cbo_monedaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_monedaItemStateChanged
        System.out.println("Se seleccionó una moneda, se procede a actualizar los datos");

        if (txt_sub_total.getText().length() > 0) {
            BigDecimal sub_total = new BigDecimal(txt_sub_total.getText().trim());

            BigDecimal sub_total_decimal = sub_total;
            sub_total_decimal = sub_total_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            sub_total = sub_total_decimal;

            calcular_totales(sub_total);
        }
    }//GEN-LAST:event_cbo_monedaItemStateChanged

    private void cbo_igvItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_igvItemStateChanged
        System.out.println("Se seleccionó un IGV, se procede a actualizar los datos");

        if (txt_sub_total.getText().length() > 0) {
            BigDecimal sub_total = new BigDecimal(txt_sub_total.getText());

            BigDecimal sub_total_decimal = sub_total;
            sub_total_decimal = sub_total_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            sub_total = sub_total_decimal;

            calcular_totales(sub_total);
        }
    }//GEN-LAST:event_cbo_igvItemStateChanged

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        String numero_factura = txt_numero_factura.getText();
        String total_letras = txt_total_letras.getText();
        String moneda = cbo_moneda.getSelectedItem().toString().trim();

        BigDecimal calculo_igv = new BigDecimal(txt_igv_calculo.getText().trim());
        BigDecimal sub_total = new BigDecimal(txt_sub_total.getText().trim());
        BigDecimal total = new BigDecimal(txt_total.getText().trim());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dia = txt_fecha.getDate();
        String fecha = sdf.format(dia);

        int id_factura = id_factura_global;
        int id_cliente = id_cliente_global;
        int id_documento = id_documento_global;
        int id_empresa = id_empresa_index;
        int id_usuario = id_usuario_index;
        String simbolo_moneda = "";


        BigDecimal descuento = new BigDecimal(txt_descuentoglobal.getText().trim());
        String vencimiento = txt_vencimiento.getText();
        String ordencompra = txt_ordencompra.getText();
        String observaciones = txt_observaciones.getText();

        int id_igv;
        int id_tipopago;
        int id_guia = 0;

        String tabla;
        String campocondicion;
        String campocapturar;

        System.out.println("\nCapturar el id_igv");
        System.out.println("==================");
        campocapturar = "id_igv";
        tabla = "tigv";
        campocondicion = "igv";
        id_igv = CapturarId(campocapturar, tabla, campocondicion, cbo_igv.getSelectedItem().toString(), id_empresa);
        System.out.println("id_igv capturado: " + id_igv);

        System.out.println("\nCapturar el id_tipopago");
        System.out.println("=======================");
        campocapturar = "id_tipopago";
        tabla = "ttipopago";
        campocondicion = "descripcion";
        id_tipopago = CapturarId(campocapturar, tabla, campocondicion, cbo_tipopago.getSelectedItem().toString(), id_empresa);
        System.out.println("id_tipopago capturado: " + id_tipopago);

        if (txt_guia.getText().length() > 0) {
            System.out.println("\nCapturar el id_guia");
            System.out.println("===================");
            campocapturar = "id_guia";
            tabla = "tguia";
            campocondicion = "numero_guia";
            id_guia = CapturarId(campocapturar, tabla, campocondicion, txt_guia.getText(), id_empresa);
            System.out.println("id_guia capturado: " + id_guia);
        }

        simbolo_moneda = CapturarSimboloMoneda(moneda);
        
        if (id_igv > 0 && id_tipopago > 0) {
            System.out.println("\nSe presionó el boton GUARDAR/MODIFICAR Factura");
            System.out.println("==============================================");
            System.out.println("id_factura    : " + id_factura);
            System.out.println("numero_factura: " + numero_factura);
            System.out.println("fecha         : " + fecha);
            System.out.println("calculo_igv   : " + calculo_igv);
            System.out.println("sub_total     : " + sub_total);
            System.out.println("total         : " + total);
            System.out.println("total_letras  : " + total_letras);
            System.out.println("id_cliente    : " + id_cliente);
            System.out.println("moneda        : " + moneda);
            System.out.println("simbolo_moneda: " + simbolo_moneda);
            System.out.println("id_igv        : " + id_igv);
            System.out.println("id_documento  : " + id_documento);
            System.out.println("id_empresa    : " + id_empresa);
            System.out.println("id_usuario    : " + id_usuario);
            System.out.println("id_tipopago   : " + id_tipopago);
            System.out.println("vencimiento   : " + vencimiento);
            System.out.println("ordencompra   : " + ordencompra);
            System.out.println("id_guia       : " + id_guia);
            System.out.println("descuento     : " + descuento);
            System.out.println("observaciones : " + observaciones);

            if (id_cliente == 0) {
                JOptionPane.showMessageDialog(null, "Falta ingresar los campos Cliente. \n Por favor seleccione un Cliente", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("Se llama a la funcion Modificar_Factura");
                modificar_Factura(id_factura, numero_factura, fecha, calculo_igv, sub_total, total, total_letras, id_cliente, moneda, id_igv, id_documento, simbolo_moneda, id_empresa, id_usuario, id_tipopago, vencimiento, ordencompra, id_guia, descuento, observaciones);
            }
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void tabla_generalMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_generalMouseReleased
        if (band_index == 0) {
            int fila;
            int id_factura;
            fila = tabla_general.getSelectedRow();
            m = (DefaultTableModel) tabla_general.getModel();
            id_factura = Integer.parseInt((String) m.getValueAt(fila, 1));
            mostrar_datos_factura(id_factura);
        }
    }//GEN-LAST:event_tabla_generalMouseReleased

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
        int fil = tabla_general.getSelectedRow();
        if (fil == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una Factura para Modificar");
            band_index = 1;
        } else {
            System.out.println("\nModifar Factura");
            System.out.println("=================");

            System.out.println("inicializar la bandera crear en uno");
            band_index = 1;
            crear0_modificar1_factura = 0;
            band_mantenimiento_factura_detalle = 0;
            band_modificar = 1; //Indicamos que es una modificacion y que se tiene que activar el boton cancelar y no eliminara la factura

            System.out.println("cambiando el titulo");
            lbl_titulo.setText("Modificar Factura");

            System.out.println("activar las cajas de texto para el registro");
            activar_caja_texto("editable");

            System.out.println("ocultar botones: vista previa, imprimir, creacion");
            btn_vista_previa.setVisible(false);
            btn_imprimir.setVisible(false);
            btn_creacion.setVisible(false);
//            btn_ver_pago.setVisible(false);


            System.out.println("mostrar botones: guardar, cancelar y otros");
            btn_cancelar.setVisible(true);
            btn_guardar.setVisible(true);
            btn_guardar.setText("Modificar");
            //btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("imagenes\\guardar_actualizar_32_32.png")));

            btn_nuevo_detalle.setVisible(true);
            btn_nuevo_cliente.setVisible(true);
            btn_importar.setVisible(true);
            btn_buscar_cliente.setVisible(true);
            lbl_estado.setVisible(false);

            System.out.println("desactivar barra de herramientas");
            activar_barra_herramientas("desactivar");
        }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void btn_anularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_anularActionPerformed
        System.out.println("Ejecutandose ANULAR FACTURA");
        System.out.println("===========================");

        int fila = tabla_general.getSelectedRow();

        ResultSet r;
        String pagado = "";

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una Factura para Anular");
        } else {
            int id_factura = Integer.parseInt((String) m.getValueAt(fila, 1));

            try {
                r = sentencia.executeQuery("select pagado from tfactura where id_factura='" + id_factura + "'");
                while (r.next()) {
                    pagado = r.getString("pagado");
                }
                if (pagado.equals("1")) {
                    JOptionPane.showMessageDialog(null, "La FACTURA ya está PAGADA.", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {
                    int respuesta;
                    respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea ANULAR esta FACTURA?", "Anular", JOptionPane.YES_NO_OPTION);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        int id_detalle_factura;
                        BigDecimal cantidad = new BigDecimal(0);
                        BigDecimal precio_unitario = new BigDecimal(0);
                        BigDecimal precio_total = new BigDecimal(0);
                        BigDecimal descuento_por = new BigDecimal(0);
                        BigDecimal descuento_val = new BigDecimal(0);
                        String unidad;
                        String descripcion;

                        BigDecimal calculo_igv = new BigDecimal(0);
                        BigDecimal sub_total = new BigDecimal(0);
                        BigDecimal total = new BigDecimal(0);
                        BigDecimal descuento = new BigDecimal(0);
                        String total_letras = "";

                        System.out.println("el id_factura que se anulará es: " + id_factura);

                        if (clase_factura.anular(id_factura, calculo_igv, sub_total, total, descuento, total_letras)) {

                            r = sentencia.executeQuery("select id_detalle_factura, unidad, descripcion from tdetalle_factura where id_factura = '" + id_factura + "'");
                            while (r.next()) {
                                id_detalle_factura = Integer.parseInt(r.getString("id_detalle_factura"));
                                unidad = r.getString("unidad");
                                descripcion = r.getString("descripcion");

                                clase_factura_detalle.modificar(id_detalle_factura, id_factura, cantidad, unidad, descripcion, precio_unitario, precio_total, descuento_por, descuento_val);
                            }

                            System.out.println("La factura se logró anular exitosamente");
                            System.out.println("ocultar el panel de detalle de datos");
                            Panel_detalle.setVisible(false);
                            JOptionPane.showMessageDialog(null, "La FACTURA se anuló exitosamente!");
                            System.out.println("actualizamos tabla FACTURA");

                            Mostrar_tabla_factura("");
                            band_index = 0;


                        } else {
                            JOptionPane.showMessageDialog(null, "Ocurrio al ANULAR la FACTURA.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }

                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos de la FACTURA", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_anularActionPerformed

    private void btn_pagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pagarActionPerformed
        int respuesta;
        int id_factura;
        ResultSet r;
        int fila = tabla_general.getSelectedRow();
        m = (DefaultTableModel) tabla_general.getModel();
        id_factura = Integer.parseInt((String) m.getValueAt(fila, 1));
        String anulado = "";

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una Factura para PAGAR");
        } else {

            try {
                r = sentencia.executeQuery("select anulado from tfactura where id_factura='" + id_factura + "'");
                while (r.next()) {
                    anulado = r.getString("anulado").trim();
                    System.out.println("anulado: " + anulado);
                }

                if (anulado.equals("1")) {
                    JOptionPane.showMessageDialog(null, "La FACTURA está ANULADA.\n No se puede efectuar el PAGO a esta FACTURA", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {
                    respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea dar como PAGADO a esta FACTURA?", "PAGAR", JOptionPane.YES_NO_OPTION);
                    if (respuesta == JOptionPane.YES_OPTION) {

                        System.out.println("el id_factura que se pagara es: " + id_factura);
                        String valor = "1";
                        if (clase_factura.pagar(id_factura, valor)) {
                            System.out.println("La factura se logró PAGAR exitosamente!");
                            System.out.println("ocultar el panel de detalle de datos");
                            Panel_detalle.setVisible(false);
                            JOptionPane.showMessageDialog(null, "La FACTURA se dio como PAGADO exitosamente!");
                            System.out.println("actualizamos tabla FACTURA");
                            Mostrar_tabla_factura("");
                            band_index = 0;
                        } else {
                            JOptionPane.showMessageDialog(null, "Ocurrio al PAGAR la FACTURA.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos de la FACTURA", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_pagarActionPerformed

    private void txt_buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ejecucion_de_buscador();
        }
    }//GEN-LAST:event_txt_buscarKeyReleased

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        System.out.println("Ejecutandose CANCELAR FACTURA");
        System.out.println("=============================");

        int id_factura = id_factura_global;
        int id_detalle_factura;
        ResultSet r;


        int respuesta;

        if (band_modificar != 1) {
            //Se ejecuta si estamos ejecutando una creacion    
            respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea CANCELAR la CREACION de esta FACTURA?", "Cancelar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {

                try {
                    r = sentencia.executeQuery("select id_detalle_factura from tdetalle_factura where id_factura = '" + id_factura + "'");
                    while (r.next()) {
                        id_detalle_factura = Integer.parseInt(r.getString("id_detalle_factura"));
                        clase_factura_detalle.eliminar(id_detalle_factura);
                    }

                    if (clase_factura.eliminar(id_factura)) {

                        System.out.println("La Factura se logró cancelar exitosamente!");

                        System.out.println("Volver los ID's a su valor inicial");

                        System.out.println("limpiar cajas de texto");
                        limpiar_caja_texto();

                        System.out.println("desactivar las cajas de texto para el registro");
                        activar_caja_texto("no_editable");

                        System.out.println("ocultar botones: guardar, cancelar y otros");
                        btn_guardar.setVisible(false);
                        btn_cancelar.setVisible(false);
                        btn_nuevo_detalle.setVisible(false);
                        btn_nuevo_cliente.setVisible(false);
                        btn_buscar_cliente.setVisible(false);

                        System.out.println("mostrar botones: vista previa, imprimir, creacion");
                        btn_vista_previa.setVisible(true);
                        btn_imprimir.setVisible(true);
                        btn_creacion.setVisible(true);

                        System.out.println("activar barra de herramientas");
                        activar_barra_herramientas("activar");

                        System.out.println("Mostrar tabla Factura_detalle_vacia");
                        Mostrar_tabla_factura_detalle_vacia();

                        System.out.println("ocultar el panel de detalle de datos");
                        Panel_detalle.setVisible(false);

                        System.out.println("actualizamos tabla Factura");
                        Mostrar_tabla_factura("");

                        System.out.println("Inicializamos lo id_globales");
                        inicializar_id_global();

                        band_index = 0;

                        band_cbo_cliente = 1;
                        band_mantenimiento_factura_detalle = 0;
                        JOptionPane.showMessageDialog(null, "La Factura se Canceló exitosamente!");

                    } else {
                        JOptionPane.showMessageDialog(null, "Ocurrio al ELIMINAR la FACTURA.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Ocurrio al cargar los datos de la FACTURA", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            //Se ejecuta si estamos ejecutando una modificacion    
            respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea CANCELAR la MODIFICACIÓN de esta FACTURA?", "Cancelar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {

                System.out.println("La Modificación se logró cancelar exitosamente!");

                System.out.println("Volver los ID's a su valor inicial");

                System.out.println("limpiar cajas de texto");
                limpiar_caja_texto();

                System.out.println("desactivar las cajas de texto para el registro");
                activar_caja_texto("no_editable");

                System.out.println("ocultar botones: guardar, cancelar y otros");
                btn_guardar.setVisible(false);
                btn_cancelar.setVisible(false);
                btn_nuevo_detalle.setVisible(false);
                btn_nuevo_cliente.setVisible(false);
                btn_buscar_cliente.setVisible(false);

                System.out.println("mostrar botones: vista previa, imprimir, creacion");
                btn_vista_previa.setVisible(true);
                btn_imprimir.setVisible(true);
                btn_creacion.setVisible(true);

                System.out.println("activar barra de herramientas");
                activar_barra_herramientas("activar");

                System.out.println("Mostrar tabla Factura_detalle_vacia");
                Mostrar_tabla_factura_detalle_vacia();

                System.out.println("ocultar el panel de detalle de datos");
                Panel_detalle.setVisible(false);

                System.out.println("actualizamos tabla Factura");
                Mostrar_tabla_factura("");

                System.out.println("Inicializamos lo id_globales");
                inicializar_id_global();

                band_index = 0;

                band_cbo_cliente = 1;
                band_mantenimiento_factura_detalle = 0;
                band_modificar = 0;
                JOptionPane.showMessageDialog(null, "La Modificacion se Canceló exitosamente!");

            }
        }
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarActionPerformed
        System.out.println("\nEliminar Detalle Factura");
        System.out.println("==========================");

        int fila;
        int id_detalle_factura;
        int respuesta;
        try {
            fila = tabla_detalle.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar el registro a borrar");
            } else {
                respuesta = JOptionPane.showConfirmDialog(null, "¿Desea eliminar?", "Eliminar", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    m = (DefaultTableModel) tabla_detalle.getModel();
                    id_detalle_factura = Integer.parseInt((String) m.getValueAt(fila, 0));
                    System.out.println("el id_detalle_factura que se eliminara es: " + id_detalle_factura);
                    if (clase_factura_detalle.eliminar(id_detalle_factura)) {
                        System.out.println("el detalle se logró eliminar exitosamente!");

                        JOptionPane.showMessageDialog(null, "El Detalle se Eliminó exitosamente!");

                        System.out.println("actualizamos tabla detalle factura");
                        Mostrar_tabla_factura_detalle(id_factura_global);
                    } else {
                        JOptionPane.showMessageDialog(null, "Ocurrio al eliminar el detalle.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al eliminar el detalle.\n Por favor intentelo nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_EliminarActionPerformed

    private void btn_vista_previaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vista_previaActionPerformed
        try {
            String rutaInforme = "reportes\\Factura_vista_previa.jasper";
            Map parametros = new HashMap();
            parametros.put("id_factura", id_factura_global);
            parametros.put("numero_guia", txt_guia.getText().trim());
            parametros.put(JRParameter.REPORT_LOCALE, Locale.US);
            JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
            JasperViewer view = new JasperViewer(print, false);
            view.setTitle("FACTURA -  SERIE: " + txt_serie.getText() + " - NUMERO: " + txt_numero_factura.getText());
            view.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: " + e, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_vista_previaActionPerformed

    private void btn_imprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imprimirActionPerformed
        if (clase_factura.imprimir(id_factura_global)) {
            try {
                String rutaInforme = "reportes\\Factura.jasper";
                Map parametros = new HashMap();
                parametros.put("id_factura", id_factura_global);
                parametros.put("numero_guia", txt_guia.getText().trim());
                parametros.put(JRParameter.REPORT_LOCALE, Locale.US);
                
                JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
                JasperViewer view = new JasperViewer(print, false);
                view.setTitle("FACTURA -  SERIE: " + txt_serie.getText() + " - NUMERO: " + txt_numero_factura.getText());
                view.setVisible(true);
                btn_modificar.setEnabled(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: " + e, "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ocurrio al guarar la Impresion", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_imprimirActionPerformed

    private void tabla_generalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_generalKeyReleased
        if (band_index == 0) {
            int fila;
            int id_factura;
            fila = tabla_general.getSelectedRow();
            m = (DefaultTableModel) tabla_general.getModel();
            id_factura = Integer.parseInt((String) m.getValueAt(fila, 1));
            mostrar_datos_factura(id_factura);
        }
    }//GEN-LAST:event_tabla_generalKeyReleased

    private void btn_cancelar_guardar_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_guardar_detalleActionPerformed
        limpiar_caja_texto_crear_detalle_factura();
        panel_nuevo_detalle.setVisible(false);
        btn_guardar_detalle.setVisible(false);
        btn_nuevo_detalle.setVisible(true);
        btn_importar.setVisible(true);
    }//GEN-LAST:event_btn_cancelar_guardar_detalleActionPerformed

    private void txt_descuentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_descuentoKeyTyped
        JTextField caja = txt_descuento;
        ingresar_solo_numeros(caja, evt);
    }//GEN-LAST:event_txt_descuentoKeyTyped

    private void txt_descuentoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_descuentoKeyReleased
        if (Float.parseFloat(txt_descuento.getText()) > 100) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un descuento mayor al 100%", "ERROR", JOptionPane.ERROR_MESSAGE);
            txt_descuento.setText("");
        }
    }//GEN-LAST:event_txt_descuentoKeyReleased

    private void btn_importarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_importarActionPerformed
        mostrar_tabla_importar_factura("");
        dialog_buscar_factura.setSize(700, 400);
        dialog_buscar_factura.setLocationRelativeTo(factura);
        dialog_buscar_factura.setModal(true);
        dialog_buscar_factura.setVisible(true);
    }//GEN-LAST:event_btn_importarActionPerformed

    private void txt_buscar_facturaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_facturaKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String bus = txt_buscar_factura.getText();
            String consulta = "";

            if (bus.equals("ANULADO")) {
                bus = "1";
                consulta = "select f.id_factura, d.serie, f.numero_factura, convert(varchar, f.fecha, 103) as fecha, c.razon_social from tfactura f, tcliente c, tdocumentos d where f.anulado =  '" + bus + "'  and f.id_cliente=c.id_cliente and f.id_documento = d.id_documento and f.numero_factura is not null andf.id_empresa='" + id_empresa_index + "' order by f.numero_factura desc";
                mostrar_tabla_importar_factura(consulta);
            } else {
                if (bus.equals("PAGADO")) {
                    bus = "1";
                    consulta = "select f.id_factura, d.serie, f.numero_factura, convert(varchar, f.fecha, 103) as fecha, c.razon_social from tfactura f, tcliente c, tdocumentos d where f.pagado =  '" + bus + "'  and f.id_cliente=c.id_cliente and f.id_documento = d.id_documento and f.numero_factura is not null and f.id_empresa='" + id_empresa_index + "' order by f.numero_factura desc";
                    mostrar_tabla_importar_factura(consulta);
                } else {
                    consulta = "select f.id_factura, d.serie, f.numero_factura, convert(varchar, f.fecha, 103) as fecha, c.razon_social from tfactura f, tcliente c, tdocumentos d where (f.numero_factura like '%" + bus + "%'  or f.fecha like '%" + bus + "%' or f.calculo_igv like '%" + bus + "%' or f.sub_total like '%" + bus + "%' or f.total like '%" + bus + "%' or f.total_letras like '%" + bus + "%' or f.total_letras like '%" + bus + "%' or f.moneda like '%" + bus + "%' or f.simbolo_moneda like '%" + bus + "%' or c.razon_social like '%" + bus + "%' or c.ruc like '%" + bus + "%' or c.direccion like '%" + bus + "%') and f.id_cliente=c.id_cliente and f.id_documento = d.id_documento and f.numero_factura is not null and f.id_empresa='" + id_empresa_index + "' order by f.numero_factura desc";
                    mostrar_tabla_importar_factura(consulta);
                }
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

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una Factura para Importar sus Detalles");
        } else {
            m = (DefaultTableModel) tabla_factura_buscar.getModel();
            id_factura = Integer.parseInt((String) m.getValueAt(fila, 0));
            importar_detalle_factura(id_factura);
        }

    }//GEN-LAST:event_btn_cliente_seleccionar2ActionPerformed

    private void tabla_factura_buscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_factura_buscarKeyPressed
        int fila = tabla_factura_buscar.getSelectedRow();
        int id_factura;

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            m = (DefaultTableModel) tabla_factura_buscar.getModel();
            id_factura = Integer.parseInt((String) m.getValueAt(fila, 0));

            importar_detalle_factura(id_factura);
        }
    }//GEN-LAST:event_tabla_factura_buscarKeyPressed

    private void btn_generar_reporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_generar_reporteActionPerformed
        String codigo_reporte = "";
        String fecha_ini = "";
        String fecha_ini_consulta = "";
        String fecha_fin = "";
        String fecha_fin_consulta = "";

        String tabla = "";
        String campocondicion = "";
        String campocapturar = "";
        String id_cliente = "";

        int band = 0;

        if (txt_cliente_filtro.getText().trim().length() >= 0) {

            try {
                System.out.println("select id_cliente from tcliente where razon_social  = '" + txt_cliente_filtro.getText().trim() + "'");
                ResultSet r = sentencia.executeQuery("select id_cliente from tcliente where razon_social  = '" + txt_cliente_filtro.getText().trim() + "'");
                while (r.next()) {
                    id_cliente = r.getString("id_cliente").trim();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ocurrio al obtener el id_cliente del cliente" + txt_cliente_filtro.getText().trim(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }

//            campocapturar = "id_cliente";
//            tabla = "tcliente";
//            campocondicion = "razon_social";
//            id_cliente = Integer.toString(CapturarId(campocapturar, tabla, campocondicion, txt_cliente_filtro.getText().trim(), id_empresa_index));

        }

        //Obtencion de fechas
        try {
            SimpleDateFormat formato_fecha = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formato_consulta = new SimpleDateFormat("yyyyMMdd");

            Date dia_inicio = txt_fecha_inicio.getDate();
            fecha_ini = formato_fecha.format(dia_inicio);
            fecha_ini_consulta = formato_consulta.format(dia_inicio);
            Date fecha_inicio = formato_fecha.parse(fecha_ini);

            Date dia_fin = txt_fecha_fin.getDate();
            fecha_fin = formato_fecha.format(dia_fin);
            fecha_fin_consulta = formato_consulta.format(dia_fin);
            Date fecha_final = formato_fecha.parse(fecha_fin);

            if (fecha_inicio.after(fecha_final)) {
                band = 1;
            } else {
                if (fecha_final.before(fecha_inicio)) {
                    band = 2;
                }
            }
        } catch (ParseException e) {
            System.out.println("Se Produjo un Error!!!  " + e.getMessage());
        }

        codigo_reporte = obtener_codigoreporte();

        if (codigo_reporte.equals(
                "101000")) {
            band = 3;
        }

        if (codigo_reporte.equals(
                "100100")) {
            band = 4;
        }

        if (codigo_reporte.equals(
                "100010")) {
            band = 5;
        }

        if (codigo_reporte.equals(
                "100001")) {
            band = 6;
        }

        if (codigo_reporte.equals(
                "011000")) {
            band = 7;
        }

        if (codigo_reporte.equals(
                "010100")) {
            band = 8;
        }

        if (codigo_reporte.equals(
                "010010")) {
            band = 9;
        }

        if (codigo_reporte.equals(
                "010001")) {
            band = 10;
        }

        switch (band) {
            case 1:
                JOptionPane.showMessageDialog(null, "La fecha FIN es MENOR a la fecha de INICIO\n Por favor seleccione otra fecha", "ERROR", JOptionPane.ERROR_MESSAGE);
                break;

            case 2:
                JOptionPane.showMessageDialog(null, "La fecha INICIO es MAYOR a la fecha FIN\n Por favor seleccione otra fecha", "ERROR", JOptionPane.ERROR_MESSAGE);
                break;

            case 3:
                try {
                    String rutaInforme = "reportes\\Reporte_Factura_1.jasper";
                    Map parametros = new HashMap();
                    parametros.put("fecha_calendario_inicio", fecha_ini);
                    parametros.put("fecha_calendario_fin", fecha_fin);
                    parametros.put("nombre_empresa", nombre_empresa_index);
                    parametros.put("nombre_usuario", nombre_usuario_index);
                    parametros.put("fecha_inicio", fecha_ini_consulta);
                    parametros.put("fecha_fin", fecha_fin_consulta);
                    parametros.put("id_empresa", id_empresa_index);

                    JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
                    JasperViewer view = new JasperViewer(print, false);
                    view.setTitle("FACTURAS DEL: " + fecha_ini + " AL " + fecha_fin);
                    dialogo_reporte_factura.dispose();
                    view.setVisible(true);
                } catch (Exception e) {
                    System.out.println("Ocurrio al mostrar informe: \n" + e.getMessage());
                    JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: \n" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case 4:
                try {
                    String rutaInforme = "reportes\\Reporte_Factura_2.jasper";
                    Map parametros = new HashMap();
                    parametros.put("fecha_calendario_inicio", fecha_ini);
                    parametros.put("fecha_calendario_fin", fecha_fin);
                    parametros.put("nombre_empresa", nombre_empresa_index);
                    parametros.put("nombre_usuario", nombre_usuario_index);
                    parametros.put("fecha_inicio", fecha_ini_consulta);
                    parametros.put("fecha_fin", fecha_fin_consulta);
                    parametros.put("id_empresa", id_empresa_index);

                    JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
                    JasperViewer view = new JasperViewer(print, false);
                    view.setTitle("FACTURAS DEL: " + fecha_ini + " AL " + fecha_fin);
                    dialogo_reporte_factura.dispose();
                    view.setVisible(true);
                } catch (Exception e) {
                    System.out.println("Ocurrio al mostrar informe: \n" + e.getMessage());
                    JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: \n" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case 5:
                try {
                    String rutaInforme = "reportes\\Reporte_Factura_3.jasper";
                    Map parametros = new HashMap();
                    parametros.put("fecha_calendario_inicio", fecha_ini);
                    parametros.put("fecha_calendario_fin", fecha_fin);
                    parametros.put("nombre_empresa", nombre_empresa_index);
                    parametros.put("nombre_usuario", nombre_usuario_index);
                    parametros.put("fecha_inicio", fecha_ini_consulta);
                    parametros.put("fecha_fin", fecha_fin_consulta);
                    parametros.put("id_empresa", id_empresa_index);

                    JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
                    JasperViewer view = new JasperViewer(print, false);
                    view.setTitle("FACTURAS DEL: " + fecha_ini + " AL " + fecha_fin);
                    dialogo_reporte_factura.dispose();
                    view.setVisible(true);
                } catch (Exception e) {
                    System.out.println("Ocurrio al mostrar informe: \n" + e.getMessage());
                    JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: \n" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case 6:
                try {
                    String rutaInforme = "reportes\\Reporte_Factura_4.jasper";
                    Map parametros = new HashMap();
                    parametros.put("fecha_calendario_inicio", fecha_ini);
                    parametros.put("fecha_calendario_fin", fecha_fin);
                    parametros.put("nombre_empresa", nombre_empresa_index);
                    parametros.put("nombre_usuario", nombre_usuario_index);
                    parametros.put("fecha_inicio", fecha_ini_consulta);
                    parametros.put("fecha_fin", fecha_fin_consulta);
                    parametros.put("id_empresa", id_empresa_index);

                    JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
                    JasperViewer view = new JasperViewer(print, false);
                    view.setTitle("FACTURAS DEL: " + fecha_ini + " AL " + fecha_fin);
                    dialogo_reporte_factura.dispose();
                    view.setVisible(true);
                } catch (Exception e) {
                    System.out.println("Ocurrio al mostrar informe: \n" + e.getMessage());
                    JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: \n" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case 7:
                try {
                    String rutaInforme = "reportes\\Reporte_Factura_5.jasper";
                    Map parametros = new HashMap();
                    parametros.put("fecha_calendario_inicio", fecha_ini);
                    parametros.put("fecha_calendario_fin", fecha_fin);
                    parametros.put("nombre_empresa", nombre_empresa_index);
                    parametros.put("nombre_usuario", nombre_usuario_index);
                    parametros.put("fecha_inicio", fecha_ini_consulta);
                    parametros.put("fecha_fin", fecha_fin_consulta);
                    parametros.put("id_empresa", id_empresa_index);
                    parametros.put("id_cliente", id_cliente);

                    JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
                    JasperViewer view = new JasperViewer(print, false);
                    view.setTitle("FACTURAS DEL: " + fecha_ini + " AL " + fecha_fin);
                    dialogo_reporte_factura.dispose();
                    view.setVisible(true);
                } catch (Exception e) {
                    System.out.println("Ocurrio al mostrar informe: \n" + e.getMessage());
                    JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: \n" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case 8:
                try {
                    String rutaInforme = "reportes\\Reporte_Factura_6.jasper";
                    Map parametros = new HashMap();
                    parametros.put("fecha_calendario_inicio", fecha_ini);
                    parametros.put("fecha_calendario_fin", fecha_fin);
                    parametros.put("nombre_empresa", nombre_empresa_index);
                    parametros.put("nombre_usuario", nombre_usuario_index);
                    parametros.put("fecha_inicio", fecha_ini_consulta);
                    parametros.put("fecha_fin", fecha_fin_consulta);
                    parametros.put("id_empresa", id_empresa_index);
                    parametros.put("id_cliente", id_cliente);

                    JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
                    JasperViewer view = new JasperViewer(print, false);
                    view.setTitle("FACTURAS DEL: " + fecha_ini + " AL " + fecha_fin);
                    dialogo_reporte_factura.dispose();
                    view.setVisible(true);
                } catch (Exception e) {
                    System.out.println("Ocurrio al mostrar informe: \n" + e.getMessage());
                    JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: \n" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case 9:
                try {
                    String rutaInforme = "reportes\\Reporte_Factura_7.jasper";
                    Map parametros = new HashMap();
                    parametros.put("fecha_calendario_inicio", fecha_ini);
                    parametros.put("fecha_calendario_fin", fecha_fin);
                    parametros.put("nombre_empresa", nombre_empresa_index);
                    parametros.put("nombre_usuario", nombre_usuario_index);
                    parametros.put("fecha_inicio", fecha_ini_consulta);
                    parametros.put("fecha_fin", fecha_fin_consulta);
                    parametros.put("id_empresa", id_empresa_index);
                    parametros.put("id_cliente", id_cliente);

                    JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
                    JasperViewer view = new JasperViewer(print, false);
                    view.setTitle("FACTURAS DEL: " + fecha_ini + " AL " + fecha_fin);
                    dialogo_reporte_factura.dispose();
                    view.setVisible(true);
                } catch (Exception e) {
                    System.out.println("Ocurrio al mostrar informe: \n" + e.getMessage());
                    JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: \n" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case 10:
                try {
                    String rutaInforme = "reportes\\Reporte_Factura_8.jasper";
                    Map parametros = new HashMap();
                    parametros.put("fecha_calendario_inicio", fecha_ini);
                    parametros.put("fecha_calendario_fin", fecha_fin);
                    parametros.put("nombre_empresa", nombre_empresa_index);
                    parametros.put("nombre_usuario", nombre_usuario_index);
                    parametros.put("fecha_inicio", fecha_ini_consulta);
                    parametros.put("fecha_fin", fecha_fin_consulta);
                    parametros.put("id_empresa", id_empresa_index);
                    parametros.put("id_cliente", id_cliente);

                    JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, conexion);
                    JasperViewer view = new JasperViewer(print, false);
                    view.setTitle("FACTURAS DEL: " + fecha_ini + " AL " + fecha_fin);
                    dialogo_reporte_factura.dispose();
                    view.setVisible(true);
                } catch (Exception e) {
                    System.out.println("Ocurrio al mostrar informe: \n" + e.getMessage());
                    JOptionPane.showMessageDialog(null, "Ocurrio al mostrar informe: \n" + e, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                break;

            default:
                dialogo_reporte_factura.dispose();
                break;
        }
    }//GEN-LAST:event_btn_generar_reporteActionPerformed

    private void btn_cancelar_reporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_reporteActionPerformed
        dialogo_reporte_factura.dispose();
    }//GEN-LAST:event_btn_cancelar_reporteActionPerformed

    private void btn_buscar_cliente_filtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_cliente_filtroActionPerformed
        ventana_buscar_cliente = "dialogo_reporte_factura";
        btn_nuevo_cliente_buscar.setVisible(false);

        Mostrar_tabla_cliente();
        dialog_buscar_cliente.setSize(700, 400);
        dialog_buscar_cliente.setLocationRelativeTo(factura);
        dialog_buscar_cliente.setModal(true);
        dialog_buscar_cliente.setVisible(true);
    }//GEN-LAST:event_btn_buscar_cliente_filtroActionPerformed

    private void btn_listar_porActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_listar_porActionPerformed
        System.out.println("capturar fecha y poner en caja de texto");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();
        String s = sdf.format(d);
        txt_fecha_inicio.setDate(d);
        txt_fecha_fin.setDate(d);

        txt_cliente_filtro.setText("");
        txt_cliente_filtro.setEditable(false);
        txt_cliente_filtro.setVisible(false);
        btn_buscar_cliente_filtro.setVisible(false);

        rdio_cliente_todos.setSelected(true);
        rdio_factura_todos.setSelected(true);

        dialogo_reporte_factura.setSize(600, 400);
        dialogo_reporte_factura.setLocationRelativeTo(factura);
        dialogo_reporte_factura.setModal(true);
        dialogo_reporte_factura.setVisible(true);
    }//GEN-LAST:event_btn_listar_porActionPerformed

    private void rdio_cliente_especificoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdio_cliente_especificoItemStateChanged
        txt_cliente_filtro.setVisible(true);
        btn_buscar_cliente_filtro.setVisible(true);
    }//GEN-LAST:event_rdio_cliente_especificoItemStateChanged

    private void rdio_cliente_todosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdio_cliente_todosItemStateChanged
        txt_cliente_filtro.setVisible(false);
        btn_buscar_cliente_filtro.setVisible(false);
    }//GEN-LAST:event_rdio_cliente_todosItemStateChanged

    private void txt_fecha_inicioMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_fecha_inicioMouseReleased
    }//GEN-LAST:event_txt_fecha_inicioMouseReleased

    private void txt_fecha_finMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_fecha_finMouseReleased
    }//GEN-LAST:event_txt_fecha_finMouseReleased

    private void txt_fecha_inicioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_fecha_inicioMousePressed
        try {
            SimpleDateFormat formato_fecha = new SimpleDateFormat("dd/MM/yyyy");

            Date dia_inicio = txt_fecha_inicio.getDate();
            String fecha_ini = formato_fecha.format(dia_inicio);
            Date fecha_inicio = formato_fecha.parse(fecha_ini);

            Date dia_fin = txt_fecha_fin.getDate();
            String fecha_fin = formato_fecha.format(dia_fin);
            Date fecha_final = formato_fecha.parse(fecha_fin);

            if (fecha_inicio.before(fecha_final)) {
                System.out.println("La Fecha 1 es menor");
            } else {
                if (fecha_final.before(fecha_inicio)) {
                    System.out.println("La Fecha 1 es Mayor");
                    JOptionPane.showMessageDialog(null, "La fecha de inicio es MAYOR a la fecha fin\n Por favor seleccione otra fecha", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println("Las Fechas Son iguales");
                }
            }
        } catch (ParseException e) {
            System.out.println("Se Produjo un Error!!!  " + e.getMessage());
        }// TODO add your handling code here:
    }//GEN-LAST:event_txt_fecha_inicioMousePressed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void txt_nro_cuentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nro_cuentaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nro_cuentaKeyTyped

    private void txt_nro_operacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nro_operacionKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nro_operacionKeyTyped

    private void txt_monto_dolarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_monto_dolarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_monto_dolarKeyTyped

    private void cbo_bancoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_bancoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_bancoItemStateChanged

    private void txt_tipo_cambioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_tipo_cambioKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tipo_cambioKeyTyped

    private void txt_tipo_cambio1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_tipo_cambio1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tipo_cambio1KeyTyped

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void cbo_estadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_estadoItemStateChanged
        ejecucion_de_buscador();
    }//GEN-LAST:event_cbo_estadoItemStateChanged

    private void cbo_buscar_clienteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_buscar_clienteItemStateChanged
        ejecucion_de_buscador();
    }//GEN-LAST:event_cbo_buscar_clienteItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Cliente;
    private javax.swing.JPanel Datos;
    private javax.swing.JMenuItem Eliminar;
    private javax.swing.JMenuItem Modificar;
    private javax.swing.JPanel Panel_detalle;
    private javax.swing.JToolBar barra_buscar;
    private javax.swing.JButton btn_anular;
    private javax.swing.JButton btn_buscar_cliente;
    private javax.swing.JButton btn_buscar_cliente_filtro;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_cancelar_guardar_detalle;
    private javax.swing.JButton btn_cancelar_reporte;
    private javax.swing.JButton btn_cliente_cancelar_busqueda;
    private javax.swing.JButton btn_cliente_cancelar_busqueda2;
    private javax.swing.JButton btn_cliente_seleccionar;
    private javax.swing.JButton btn_cliente_seleccionar2;
    private javax.swing.JButton btn_creacion;
    private javax.swing.JButton btn_generar_reporte;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_guardar_detalle;
    private javax.swing.JButton btn_importar;
    private javax.swing.JButton btn_imprimir;
    private javax.swing.JButton btn_listar_por;
    private javax.swing.JButton btn_modificar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JButton btn_nuevo_cliente;
    private javax.swing.JButton btn_nuevo_cliente_buscar;
    private javax.swing.JButton btn_nuevo_detalle;
    private javax.swing.JButton btn_pagar;
    private javax.swing.JButton btn_vista_previa;
    private javax.swing.JComboBox cbo_banco;
    private javax.swing.JComboBox cbo_buscar_cliente;
    private javax.swing.JComboBox cbo_estado;
    private javax.swing.JComboBox cbo_igv;
    private javax.swing.JComboBox cbo_moneda;
    private javax.swing.JComboBox cbo_razon_social;
    private javax.swing.JComboBox cbo_tipopago;
    private javax.swing.JPanel centro;
    private javax.swing.JPanel condiciones_pago;
    private javax.swing.JDialog dialog_buscar_cliente;
    private javax.swing.JDialog dialog_buscar_factura;
    private javax.swing.JDialog dialog_crear_cliente;
    private javax.swing.JDialog dialog_fecha_creacion;
    private javax.swing.JDialog dialog_pago;
    private javax.swing.JDialog dialogo_reporte_factura;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
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
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JPanel jPanel4;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JLabel label_buscar;
    private javax.swing.JLabel lb_fecha;
    private javax.swing.JLabel lb_fecha1;
    private javax.swing.JLabel lb_fecha2;
    private javax.swing.JLabel lb_moneda;
    private javax.swing.JLabel lb_ordendecompra;
    private javax.swing.JLabel lb_tipopago;
    private javax.swing.JLabel lbl_estado;
    private javax.swing.JLabel lbl_numero;
    private javax.swing.JLabel lbl_serie;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JPopupMenu mantenimiento_tabla_detalle_factura;
    private javax.swing.ButtonGroup mostrar_solo_las_facturas;
    private javax.swing.JPanel norte;
    private javax.swing.ButtonGroup ordenar_por;
    private javax.swing.JPanel panel_cuerpo;
    private javax.swing.JPanel panel_fechas;
    private javax.swing.JPanel panel_mostrar_solo_facturas;
    private javax.swing.JPanel panel_norte;
    private javax.swing.JPanel panel_nuevo_detalle;
    private javax.swing.JPanel panel_seleccione_cliente;
    private javax.swing.JPanel panel_tabla;
    private javax.swing.JRadioButton rdio_cliente_especifico;
    private javax.swing.JRadioButton rdio_cliente_todos;
    private javax.swing.JRadioButton rdio_factura_anulados;
    private javax.swing.JRadioButton rdio_factura_pagados;
    private javax.swing.JRadioButton rdio_factura_por_pagar;
    private javax.swing.JRadioButton rdio_factura_todos;
    private javax.swing.ButtonGroup seleccione_el_cliente;
    private javax.swing.JTable tabla_cliente_buscar;
    private javax.swing.JTable tabla_detalle;
    private javax.swing.JTable tabla_factura_buscar;
    private javax.swing.JTable tabla_general;
    private javax.swing.JTextField txt_buscar;
    private javax.swing.JTextField txt_buscar_cliente;
    private javax.swing.JTextField txt_buscar_factura;
    private javax.swing.JTextField txt_cantidad;
    private javax.swing.JTextField txt_celular_cliente_crear;
    private javax.swing.JTextField txt_cliente_filtro;
    private javax.swing.JTextField txt_correo_cliente_crear;
    private javax.swing.JTextField txt_descuento;
    private javax.swing.JTextField txt_descuentoglobal;
    private javax.swing.JTextArea txt_detalle;
    private javax.swing.JTextField txt_direccion;
    private javax.swing.JTextField txt_direccion_cliente_crear;
    private javax.swing.JLabel txt_estado;
    private javax.swing.JTextField txt_f_creacion;
    private javax.swing.JTextField txt_f_modificacion;
    private org.jdesktop.swingx.JXDatePicker txt_fecha;
    private org.jdesktop.swingx.JXDatePicker txt_fecha1;
    private org.jdesktop.swingx.JXDatePicker txt_fecha_fin;
    private org.jdesktop.swingx.JXDatePicker txt_fecha_inicio;
    private javax.swing.JTextField txt_guia;
    private javax.swing.JTextField txt_igv_calculo;
    private javax.swing.JTextField txt_monto_dolar;
    private javax.swing.JTextField txt_nro_cuenta;
    private javax.swing.JTextField txt_nro_operacion;
    private javax.swing.JLabel txt_numero_factura;
    private javax.swing.JTextField txt_observaciones;
    private javax.swing.JTextField txt_ordencompra;
    private javax.swing.JTextField txt_precio_unitario;
    private javax.swing.JTextField txt_razon_social_cliente_crear;
    private javax.swing.JTextField txt_ruc;
    private javax.swing.JTextField txt_ruc_cliente_crear;
    private javax.swing.JLabel txt_serie;
    private javax.swing.JTextField txt_serie_pago;
    private javax.swing.JTextField txt_serie_pago1;
    private javax.swing.JTextField txt_sub_total;
    private javax.swing.JTextField txt_telefono_cliente_crear;
    private javax.swing.JTextField txt_tipo_cambio;
    private javax.swing.JTextField txt_tipo_cambio1;
    private javax.swing.JTextField txt_total;
    private javax.swing.JTextField txt_total_letras;
    private javax.swing.JTextField txt_unidad;
    private javax.swing.JTextField txt_usuario;
    private javax.swing.JTextField txt_vencimiento;
    // End of variables declaration//GEN-END:variables
}
