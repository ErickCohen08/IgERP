package system_rysi;

import Clases.cConvertir_Numero_Letra;
import Controller.ClienteBL;
import Controller.CompraMaterialBL;
import Controller.CompraMaterialDetalleBL;
import Controller.DatoComunBL;
import Controller.IgvBL;
import Controller.MonedaBL;
import Controller.ObrasBL;
import Controller.PersonalBL;
import Controller.ProductoBL;
import Controller.ProductoDetalleBL;
import Controller.ProveedorBL;
import Controller.ValidacionBL;
import database.AccesoDB;
import entity.ClienteBE;
import entity.CompraMaterialBE;
import entity.CompraMaterialDetalleBE;
import entity.DatoComunBE;
import entity.IgvBE;
import entity.MonedaBE;
import entity.ObrasBE;
import entity.PersonalBE;
import entity.ProductoBE;
import entity.ProductoDetalleBE;
import entity.ProveedorBE;
import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author ErC
 */
public class CompraMaterialView extends javax.swing.JPanel {

    //datos de conexion
    String controlador_index;
    String user_index;
    String password_index;
    int id_empresa_index;
    int id_usuario_index;
    String perfil_usuario_index = "";
    String aliasUsuarioIndex;
    
    //Banderas
    DefaultTableModel m;
    
    //New
    ProductoBL objProductoBL = new ProductoBL();
    MonedaBL objMonedaBL = new MonedaBL();
    DatoComunBL objDatoComunBL = new DatoComunBL();
    ProveedorBL objProveedorBL = new ProveedorBL();
    ValidacionBL objValidacionBL = new ValidacionBL();
    ProductoDetalleBL objProductoDetalleBL = new ProductoDetalleBL();
    CompraMaterialBL objCompraMaterialBL = new CompraMaterialBL();
    CompraMaterialDetalleBL objCompraMaterialDetalleBL = new CompraMaterialDetalleBL();
    PersonalBL objPersonalBL = new PersonalBL();
    ObrasBL objObrasBL = new ObrasBL();
    ClienteBL objClienteBL = new ClienteBL();
    IgvBL objIgvBL = new IgvBL();
    cConvertir_Numero_Letra clase_numero_letra = new cConvertir_Numero_Letra();
    
    
    //variables globales
    boolean limpiarCboFiltros = true;
    int crear0_modificar1_producto = 0;
    int id_salidamaterial_global;
    int bandBusProveedor = 0;
    private Component ventana;
    
    public CompraMaterialView(String controlador, String DSN, String user, String password, int id_empresa, int id_usuario, String perfil_usuario, String alias_usuario) {
        controlador_index = controlador;
        user_index = user;
        password_index = password;
        id_empresa_index = id_empresa;
        id_usuario_index = id_usuario;
        perfil_usuario_index = perfil_usuario;
        aliasUsuarioIndex = alias_usuario;
        
        System.out.println("\n\nconectando con Compra de materiales");
        initComponents();

        if (perfil_usuario_index.equals("Solo Lectura")) {
            btnNuevo.setVisible(false);
            btnModificar.setVisible(false);
            btnEliminar.setVisible(false);
        }

        System.out.println("Mostrar Tabla Compra de materiales");
        mostrar_tabla_general();
        MostrarObjetos(false);        
        
    }

    private void Activar_letras_Mayusculas() {
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

    private void tamaño_de_caja_jtextarea(JTextArea caja, KeyEvent evt, int limite) {
        if (caja.getText().length() == limite) {
            evt.consume();
        }
    }

    //Mostrar tablas
    private void mostrar_tabla_general() {
        CompraMaterialBE pbe = ObtenerFiltrosBusqueda();
        
        try {
            List<CompraMaterialBE> list = objCompraMaterialBL.read(pbe);
            if (list != null) {
                tablaGeneral(list);
                lblTotal.setText("Total: "+list.size()+" registros.");
            } else {
                lblTotal.setText("No se encontraron resultados");
            }
            
            MotrarBotonesControl(false, false, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tablaGeneral(List<CompraMaterialBE> list) {
        
        try {
            DefaultTableModel tabla = (DefaultTableModel) tabla_general.getModel();
            tabla.setRowCount(0);
            for (CompraMaterialBE obj : list) {
                Object[] fila = {
                    obj.getFila(),
                    insertarCeros(obj.getIdCompra()), 
                    obj.getFechaCompra(), 
                    obj.getDesProveedor(), 
                    obj.getDesDocumento(), 
                    obj.getNumeroCompra(),
                    obj.getDesMoneda(),
                    obj.getTotal(),
                    obj.getDesEstadoAbierto()
                    };
                tabla.addRow(fila);
            }

            tabla_general.setRowHeight(35);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }        
    }
    
    private void mostrar_tabla_proveedor() {
        String buscar = null;
        
        if(txt_buscar_proveedor.getText() != null && txt_buscar_proveedor.getText().trim().length()>0)
            buscar = txt_buscar_proveedor.getText().trim();
        
        try {
            List<ProveedorBE> list = objProveedorBL.ProveedorBuscar(buscar);
            if (list != null) {
                tablaProveedorBuscar(list);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //Limpiar
    private void limpiar_caja_texto_crear_salidamaterial() {
        txtCodigo.setText("");
        txtNumeroCompra.setText("");
        txtDesProveedor.setText("");
        txtRucProveedorCompra.setText("");
        txtProducto.setText("");
        txtUnidad.setText("");
        txtCantidad.setText("");
        txtPrecioUnitario.setText("");
        txtTotalLetras.setText("");
        txtSubTotal.setText("");
        txtCalculoIgv.setText("");
        txtTotal.setText("");
    }

    private void limpiar_caja_texto_crear_obra() {
        txt_buscar_proveedor.setText("");
        txt_nombre_obra.setText("");
    }

    private void limpiar_caja_texto_crear_detalle_producto() {
        txtCantidad.setText("");
        txtProducto.setText("");
        txtUnidad.setText("");
    }

    //Creaciones
    private void crear_obra() {
        String nombre = txt_nombre_obra.getText().trim();
        String direccion = txt_direccion_obra.getText().trim();
        String cliente = cboCliente.getSelectedItem().toString().trim();
        
        ObrasBE pbe = new ObrasBE();
        int ban = 0;
        
        if(ban == 0){
            if(nombre != null && nombre.length() > 0 )
                pbe.setDescripcion(nombre);
            else
            {
                JOptionPane.showMessageDialog(null, "El campo Nombre de la Obra es obligatorio");
                ban++;
            } 
        }
        
        if(ban == 0){
            if(cliente != null && cliente.length() >0 )
                pbe.setDesCliente(cliente);
            else
            {
                JOptionPane.showMessageDialog(null, "El campo Cliente es obligatorio"); 
                ban++;
            }                
        }
        
        if(ban == 0){
            if(direccion != null && direccion.length() >0 )
                pbe.setDireccion(direccion);
        
            pbe.setId_empresa(id_empresa_index);
            pbe.setUsuarioInserta(aliasUsuarioIndex);

            try {
                objObrasBL.create(pbe);
                MostrarComboObras(cboObra, true, true, nombre);
                CerrarDialogoCrearObras();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mantenimiento_tabla_detalle_salida = new javax.swing.JPopupMenu();
        Eliminar = new javax.swing.JMenuItem();
        dialog_crear_obra = new javax.swing.JDialog();
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
        txt_nombre_obra = new javax.swing.JTextField();
        cboCliente = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        txt_direccion_obra = new javax.swing.JTextArea();
        dialog_buscar_proveedor = new javax.swing.JDialog();
        jPanel39 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        txt_buscar_proveedor = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jPanel40 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        btn_cliente_cancelar_busqueda = new javax.swing.JButton();
        btn_cliente_seleccionar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel42 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaProveedor = new javax.swing.JTable();
        dialog_crear_compra = new javax.swing.JDialog();
        centro = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tabla_detalle_compre_material = new javax.swing.JTable();
        panel_nuevo_detalle = new javax.swing.JPanel();
        btn_guardar_detalle = new javax.swing.JButton();
        jLabel47 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        btn_cancelar_guardar_detalle = new javax.swing.JButton();
        jLabel64 = new javax.swing.JLabel();
        btn_buscar_proveedor = new javax.swing.JButton();
        txtProducto = new javax.swing.JTextField();
        txtUnidad = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        lblIdProducto = new javax.swing.JLabel();
        txtPrecioUnitario = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txtCodigo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtFechaCompra = new org.jdesktop.swingx.JXDatePicker();
        jLabel14 = new javax.swing.JLabel();
        cboDocumento = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        txtNumeroCompra = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        cboMoneda = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        cboObra = new javax.swing.JComboBox();
        btnNuevoObra = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtRucProveedorCompra = new javax.swing.JTextField();
        btnNuevoProveedorCompra = new javax.swing.JButton();
        btnBuscarProveedorCompra = new javax.swing.JButton();
        txtDesProveedor = new javax.swing.JTextField();
        jSeparator7 = new javax.swing.JSeparator();
        norte = new javax.swing.JPanel();
        lbl_titulo = new javax.swing.JLabel();
        sur = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        btn_guardar = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        txtCalculoIgv = new javax.swing.JTextField();
        cboIgv = new javax.swing.JComboBox();
        jLabel61 = new javax.swing.JLabel();
        txtSubTotal = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        txtTotalLetras = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        dialog_filtrar_compra = new javax.swing.JDialog();
        jPanel18 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtNumDocumentoBus = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        cboObraBus = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();
        txtFechaCompraBus = new org.jdesktop.swingx.JXDatePicker();
        jLabel45 = new javax.swing.JLabel();
        cboEstadoBus = new javax.swing.JComboBox();
        cboMonedaBus = new javax.swing.JComboBox();
        jLabel50 = new javax.swing.JLabel();
        cboDocumentoBus = new javax.swing.JComboBox();
        jLabel44 = new javax.swing.JLabel();
        txtCodigoBus = new javax.swing.JTextField();
        txtProveedorBus = new javax.swing.JTextField();
        btnBuscarProvedorFiltro = new javax.swing.JButton();
        dialog_filtrar_producto = new javax.swing.JDialog();
        jPanel23 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jButton12 = new javax.swing.JButton();
        btnSeleccionProducto = new javax.swing.JButton();
        lblTotalProductos = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtCodigo_bus1 = new javax.swing.JTextField();
        txtDescripcion_bus = new javax.swing.JTextField();
        txtModelo_bus = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtMarca_bus = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtNombreComun_bus = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        cboMoneda_bus = new javax.swing.JComboBox();
        jLabel37 = new javax.swing.JLabel();
        cboUnidad_bus = new javax.swing.JComboBox();
        jLabel38 = new javax.swing.JLabel();
        cboCategoria_bus = new javax.swing.JComboBox();
        cbo_referencia_bus = new javax.swing.JComboBox();
        cboAlmacen_bus = new javax.swing.JComboBox();
        jLabel46 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        tabla_producto = new javax.swing.JTable();
        dialog_crear_proveedor = new javax.swing.JDialog();
        jPanel31 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        btn_cancelar_cliente1 = new javax.swing.JButton();
        btn_crea_cliente1 = new javax.swing.JButton();
        jPanel34 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        txt_razon_social_proveedor_crear = new javax.swing.JTextField();
        txt_ruc_proveedor_crear = new javax.swing.JTextField();
        txt_direccion_proveedor_crear = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        txt_telefono_proveedor_crear = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        txt_celular_proveedor_crear = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txt_correo_proveedor_crear = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btnFiltrar = new javax.swing.JButton();
        spFiltrar = new javax.swing.JToolBar.Separator();
        btnNuevo = new javax.swing.JButton();
        spNuevo = new javax.swing.JToolBar.Separator();
        btnModificar = new javax.swing.JButton();
        spModificar = new javax.swing.JToolBar.Separator();
        btnEliminar = new javax.swing.JButton();
        spEliminar = new javax.swing.JToolBar.Separator();
        btnImprimirSalida = new javax.swing.JButton();
        spImprimirSalida = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        panel_tabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_general = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        lblTotal = new javax.swing.JLabel();

        Eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar_24_24.png"))); // NOI18N
        Eliminar.setText("Eliminar");
        Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarActionPerformed(evt);
            }
        });
        mantenimiento_tabla_detalle_salida.add(Eliminar);

        dialog_crear_obra.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialog_crear_obra.setTitle("Provedor");

        jPanel24.setBackground(new java.awt.Color(0, 110, 204));
        jPanel24.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/obra_32_32.png"))); // NOI18N
        jLabel39.setText("Crear Obra");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_crear_obra.getContentPane().add(jPanel24, java.awt.BorderLayout.PAGE_START);

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
        btn_cancelar_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btn_cancelar_clienteKeyReleased(evt);
            }
        });

        btn_crea_cliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_crea_cliente.setText("Guardar");
        btn_crea_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_crea_clienteActionPerformed(evt);
            }
        });
        btn_crea_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btn_crea_clienteKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addGap(0, 377, Short.MAX_VALUE)
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
        jLabel40.setText("Nombre de la Obra:*");

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 51, 153));
        jLabel41.setText("Cliente:*");

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 51, 153));
        jLabel42.setText("Dirección:");

        txt_nombre_obra.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_nombre_obra.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_nombre_obra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_nombre_obraKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nombre_obraKeyTyped(evt);
            }
        });

        cboCliente.setEditable(true);
        cboCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboCliente.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboClienteItemStateChanged(evt);
            }
        });

        txt_direccion_obra.setColumns(20);
        txt_direccion_obra.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_direccion_obra.setLineWrap(true);
        txt_direccion_obra.setRows(2);
        txt_direccion_obra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_direccion_obraKeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(txt_direccion_obra);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_nombre_obra)
                    .addComponent(cboCliente, 0, 446, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(txt_nombre_obra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(cboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel27, java.awt.BorderLayout.CENTER);

        dialog_crear_obra.getContentPane().add(jPanel25, java.awt.BorderLayout.CENTER);

        dialog_buscar_proveedor.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_24_24.png"))); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton4);

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
            .addGap(0, 505, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 43, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 44, Short.MAX_VALUE)))
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
        btn_cliente_cancelar_busqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btn_cliente_cancelar_busquedaKeyReleased(evt);
            }
        });

        btn_cliente_seleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/listo_32_32.png"))); // NOI18N
        btn_cliente_seleccionar.setText("Seleccionar");
        btn_cliente_seleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cliente_seleccionarActionPerformed(evt);
            }
        });
        btn_cliente_seleccionar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btn_cliente_seleccionarKeyReleased(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 174, Short.MAX_VALUE)
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

        tablaProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "RUC", "Razón Social"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProveedorMouseClicked(evt);
            }
        });
        tablaProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaProveedorKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(tablaProveedor);
        if (tablaProveedor.getColumnModel().getColumnCount() > 0) {
            tablaProveedor.getColumnModel().getColumn(0).setResizable(false);
            tablaProveedor.getColumnModel().getColumn(0).setPreferredWidth(0);
            tablaProveedor.getColumnModel().getColumn(1).setPreferredWidth(300);
            tablaProveedor.getColumnModel().getColumn(2).setPreferredWidth(700);
        }

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
        );

        jPanel40.add(jPanel42, java.awt.BorderLayout.CENTER);

        dialog_buscar_proveedor.getContentPane().add(jPanel40, java.awt.BorderLayout.CENTER);

        dialog_crear_compra.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialog_crear_compra.setTitle("Compra de Materiales");
        dialog_crear_compra.setResizable(false);

        centro.setBackground(new java.awt.Color(255, 255, 255));

        tabla_detalle_compre_material.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabla_detalle_compre_material.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdProducto", "Material", "Unidad", "Cantidad", "Precio Unitario", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla_detalle_compre_material.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_detalle_compre_materialMouseClicked(evt);
            }
        });
        tabla_detalle_compre_material.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tabla_detalle_compre_materialKeyReleased(evt);
            }
        });
        jScrollPane6.setViewportView(tabla_detalle_compre_material);
        if (tabla_detalle_compre_material.getColumnModel().getColumnCount() > 0) {
            tabla_detalle_compre_material.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabla_detalle_compre_material.getColumnModel().getColumn(1).setPreferredWidth(1000);
            tabla_detalle_compre_material.getColumnModel().getColumn(2).setPreferredWidth(200);
            tabla_detalle_compre_material.getColumnModel().getColumn(3).setPreferredWidth(200);
            tabla_detalle_compre_material.getColumnModel().getColumn(4).setPreferredWidth(200);
            tabla_detalle_compre_material.getColumnModel().getColumn(5).setPreferredWidth(200);
        }

        panel_nuevo_detalle.setBackground(new java.awt.Color(255, 255, 255));
        panel_nuevo_detalle.setPreferredSize(new java.awt.Dimension(840, 60));

        btn_guardar_detalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/listo_10_10.png"))); // NOI18N
        btn_guardar_detalle.setToolTipText("Agregar");
        btn_guardar_detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardar_detalleActionPerformed(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(0, 51, 153));
        jLabel47.setText("Cantidad:");

        txtCantidad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCantidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCantidad.setOpaque(false);
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadKeyTyped(evt);
            }
        });

        btn_cancelar_guardar_detalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_10_10.png"))); // NOI18N
        btn_cancelar_guardar_detalle.setToolTipText("Cancelar");
        btn_cancelar_guardar_detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_guardar_detalleActionPerformed(evt);
            }
        });

        jLabel64.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(0, 51, 153));
        jLabel64.setText("Material:");

        btn_buscar_proveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_10_10.png"))); // NOI18N
        btn_buscar_proveedor.setToolTipText("Buscar Proveedor");
        btn_buscar_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar_proveedorActionPerformed(evt);
            }
        });

        txtProducto.setEditable(false);
        txtProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtProducto.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtProducto.setOpaque(false);
        txtProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProductoKeyPressed(evt);
            }
        });

        txtUnidad.setEditable(false);
        txtUnidad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtUnidad.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtUnidad.setOpaque(false);
        txtUnidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUnidadKeyPressed(evt);
            }
        });

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(0, 51, 153));
        jLabel49.setText("Unidad:");

        txtPrecioUnitario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPrecioUnitario.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPrecioUnitario.setOpaque(false);
        txtPrecioUnitario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPrecioUnitarioKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioUnitarioKeyTyped(evt);
            }
        });

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(0, 51, 153));
        jLabel58.setText("Precio Unitario:");

        javax.swing.GroupLayout panel_nuevo_detalleLayout = new javax.swing.GroupLayout(panel_nuevo_detalle);
        panel_nuevo_detalle.setLayout(panel_nuevo_detalleLayout);
        panel_nuevo_detalleLayout.setHorizontalGroup(
            panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_buscar_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtProducto)
                    .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtUnidad)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCantidad)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPrecioUnitario)
                    .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_guardar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar_guardar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                    .addGap(0, 276, Short.MAX_VALUE)
                    .addComponent(lblIdProducto)
                    .addGap(0, 275, Short.MAX_VALUE)))
        );
        panel_nuevo_detalleLayout.setVerticalGroup(
            panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(jLabel47)
                    .addComponent(jLabel49)
                    .addComponent(jLabel58))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCantidad)
                        .addComponent(txtPrecioUnitario))
                    .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtProducto)
                        .addComponent(txtUnidad))
                    .addComponent(btn_buscar_proveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_cancelar_guardar_detalle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_guardar_detalle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(lblIdProducto)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        txtCodigo.setEditable(false);
        txtCodigo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCodigo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
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

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 153));
        jLabel12.setText("Código:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Generales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 153));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Fecha de Compra:*");

        txtFechaCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaCompraActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 51, 153));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("Documento:*");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 51, 153));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setText("Serie - Numero:*");

        txtNumeroCompra.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNumeroCompra.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtNumeroCompra.setOpaque(false);
        txtNumeroCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumeroCompraKeyTyped(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 51, 153));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("Moneda:*");

        cboMoneda.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboMonedaItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNumeroCompra, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboMoneda, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFechaCompra, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(cboDocumento, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFechaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumeroCompra))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Obra y Proveedor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 110, 204))); // NOI18N

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 51, 153));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText("Obra:");

        cboObra.setEditable(true);
        cboObra.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnNuevoObra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btnNuevoObra.setToolTipText("Crear Nueva Unidad de Medida");
        btnNuevoObra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoObraActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 51, 153));
        jLabel11.setText("Proveedor:*");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 51, 153));
        jLabel20.setText("R.U.C.:");

        txtRucProveedorCompra.setEditable(false);
        txtRucProveedorCompra.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtRucProveedorCompra.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtRucProveedorCompra.setOpaque(false);

        btnNuevoProveedorCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btnNuevoProveedorCompra.setToolTipText("Crear Nueva Unidad de Medida");
        btnNuevoProveedorCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoProveedorCompraActionPerformed(evt);
            }
        });

        btnBuscarProveedorCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_10_10.png"))); // NOI18N
        btnBuscarProveedorCompra.setToolTipText("Crear Nueva Unidad de Medida");
        btnBuscarProveedorCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProveedorCompraActionPerformed(evt);
            }
        });

        txtDesProveedor.setEditable(false);
        txtDesProveedor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDesProveedor.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtDesProveedor.setOpaque(false);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboObra, 0, 274, Short.MAX_VALUE)
                    .addComponent(txtDesProveedor)
                    .addComponent(txtRucProveedorCompra))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscarProveedorCompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNuevoObra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNuevoProveedorCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnNuevoObra, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtDesProveedor))
                    .addComponent(btnBuscarProveedorCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNuevoProveedorCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRucProveedorCompra))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout centroLayout = new javax.swing.GroupLayout(centro);
        centro.setLayout(centroLayout);
        centroLayout.setHorizontalGroup(
            centroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6)
            .addComponent(panel_nuevo_detalle, javax.swing.GroupLayout.DEFAULT_SIZE, 816, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator6)
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator7)
        );
        centroLayout.setVerticalGroup(
            centroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(centroLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_nuevo_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
        );

        dialog_crear_compra.getContentPane().add(centro, java.awt.BorderLayout.CENTER);

        norte.setBackground(new java.awt.Color(0, 110, 204));
        norte.setPreferredSize(new java.awt.Dimension(458, 40));

        lbl_titulo.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_titulo.setForeground(new java.awt.Color(255, 255, 255));
        lbl_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_titulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ver_detalle_32_32.png"))); // NOI18N
        lbl_titulo.setText("Compra de Material");

        javax.swing.GroupLayout norteLayout = new javax.swing.GroupLayout(norte);
        norte.setLayout(norteLayout);
        norteLayout.setHorizontalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 816, Short.MAX_VALUE)
        );
        norteLayout.setVerticalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_crear_compra.getContentPane().add(norte, java.awt.BorderLayout.NORTH);

        sur.setPreferredSize(new java.awt.Dimension(458, 100));
        sur.setLayout(new java.awt.BorderLayout());

        jPanel10.setPreferredSize(new java.awt.Dimension(111, 41));

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

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(578, Short.MAX_VALUE)
                .addComponent(btn_guardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_cancelar)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_guardar)
                    .addComponent(btn_cancelar))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        sur.add(jPanel10, java.awt.BorderLayout.SOUTH);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(0, 51, 153));
        jLabel59.setText("Impuesto:");

        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtTotal.setOpaque(false);
        txtTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTotalKeyPressed(evt);
            }
        });

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(0, 51, 153));
        jLabel60.setText("Total");

        txtCalculoIgv.setEditable(false);
        txtCalculoIgv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCalculoIgv.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtCalculoIgv.setOpaque(false);
        txtCalculoIgv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCalculoIgvKeyPressed(evt);
            }
        });

        cboIgv.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboIgvItemStateChanged(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(0, 51, 153));
        jLabel61.setText("I.G.V.:");

        txtSubTotal.setEditable(false);
        txtSubTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSubTotal.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtSubTotal.setOpaque(false);
        txtSubTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSubTotalKeyPressed(evt);
            }
        });

        jLabel62.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(0, 51, 153));
        jLabel62.setText("Sub Total:");

        txtTotalLetras.setEditable(false);
        txtTotalLetras.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTotalLetras.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtTotalLetras.setOpaque(false);
        txtTotalLetras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTotalLetrasKeyPressed(evt);
            }
        });

        jLabel63.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(0, 51, 153));
        jLabel63.setText("Son:");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalLetras)
                    .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSubTotal)
                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboIgv, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCalculoIgv)
                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTotal)
                    .addComponent(jLabel60, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal)
                    .addComponent(txtCalculoIgv)
                    .addComponent(cboIgv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSubTotal)
                    .addComponent(txtTotalLetras))
                .addContainerGap())
        );

        sur.add(jPanel12, java.awt.BorderLayout.CENTER);

        dialog_crear_compra.getContentPane().add(sur, java.awt.BorderLayout.SOUTH);

        dialog_filtrar_compra.setResizable(false);

        jPanel18.setBackground(new java.awt.Color(0, 110, 204));
        jPanel18.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/filtro_24_24.png"))); // NOI18N
        jLabel18.setText("Filtro");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_filtrar_compra.getContentPane().add(jPanel18, java.awt.BorderLayout.PAGE_START);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setLayout(new java.awt.BorderLayout());

        jPanel21.setPreferredSize(new java.awt.Dimension(418, 40));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cerrar_ventana_32_32.png"))); // NOI18N
        jButton7.setText("Cerrar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jButton7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton7KeyReleased(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_24_24.png"))); // NOI18N
        jButton8.setText("Filtrar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jButton8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton8KeyReleased(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/filter_delete_24_24.png"))); // NOI18N
        jButton10.setText("Quitar Filtro");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jButton10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton10KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(0, 251, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10)
                .addGap(37, 37, 37)
                .addComponent(jButton7))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel20.add(jPanel21, java.awt.BorderLayout.PAGE_END);

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 51, 153));
        jLabel22.setText("N° Documento:");

        txtNumDocumentoBus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNumDocumentoBus.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtNumDocumentoBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumDocumentoBusActionPerformed(evt);
            }
        });
        txtNumDocumentoBus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNumDocumentoBusKeyReleased(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 51, 153));
        jLabel27.setText("Obra:");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 51, 153));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("Moneda:");

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 51, 153));
        jLabel31.setText("Proveedor:");

        cboObraBus.setEditable(true);
        cboObraBus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboObraBus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboObraBusKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboObraBusKeyReleased(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 51, 153));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("Fecha:");

        txtFechaCompraBus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFechaCompraBusKeyReleased(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(0, 51, 153));
        jLabel45.setText("Estado:");

        cboEstadoBus.setEditable(true);
        cboEstadoBus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboEstadoBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEstadoBusActionPerformed(evt);
            }
        });
        cboEstadoBus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboEstadoBusKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboEstadoBusKeyReleased(evt);
            }
        });

        cboMonedaBus.setEditable(true);
        cboMonedaBus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboMonedaBus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboMonedaBusKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboMonedaBusKeyReleased(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 51, 153));
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel50.setText("Documento:");

        cboDocumentoBus.setEditable(true);
        cboDocumentoBus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboDocumentoBus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboDocumentoBusKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboDocumentoBusKeyReleased(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(0, 51, 153));
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel44.setText("Codigo:");

        txtCodigoBus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCodigoBus.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtCodigoBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoBusActionPerformed(evt);
            }
        });
        txtCodigoBus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoBusKeyReleased(evt);
            }
        });

        txtProveedorBus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtProveedorBus.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtProveedorBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProveedorBusActionPerformed(evt);
            }
        });
        txtProveedorBus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProveedorBusKeyReleased(evt);
            }
        });

        btnBuscarProvedorFiltro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_10_10.png"))); // NOI18N
        btnBuscarProvedorFiltro.setToolTipText("Buscar Proveedor");
        btnBuscarProvedorFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProvedorFiltroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                            .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(cboMonedaBus, 0, 160, Short.MAX_VALUE))
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboEstadoBus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(txtCodigoBus, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtFechaCompraBus, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE))
                            .addComponent(cboObraBus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(cboDocumentoBus, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNumDocumentoBus))
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(txtProveedorBus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarProvedorFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txtFechaCompraBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44)
                    .addComponent(txtCodigoBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(txtProveedorBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(btnBuscarProvedorFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(cboObraBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(cboDocumentoBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(txtNumDocumentoBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(cboMonedaBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(cboEstadoBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        jPanel20.add(jPanel22, java.awt.BorderLayout.CENTER);

        dialog_filtrar_compra.getContentPane().add(jPanel20, java.awt.BorderLayout.CENTER);

        dialog_filtrar_producto.setResizable(false);

        jPanel23.setBackground(new java.awt.Color(0, 110, 204));
        jPanel23.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/filtro_24_24.png"))); // NOI18N
        jLabel21.setText("Filtro");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_filtrar_producto.getContentPane().add(jPanel23, java.awt.BorderLayout.PAGE_START);

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setLayout(new java.awt.BorderLayout());

        jPanel29.setPreferredSize(new java.awt.Dimension(418, 40));

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cerrar_ventana_32_32.png"))); // NOI18N
        jButton12.setText("Cerrar");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jButton12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton12KeyReleased(evt);
            }
        });

        btnSeleccionProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/listo_24_24.png"))); // NOI18N
        btnSeleccionProducto.setText("Seleccionar");
        btnSeleccionProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionProductoActionPerformed(evt);
            }
        });
        btnSeleccionProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btnSeleccionProductoKeyReleased(evt);
            }
        });

        lblTotalProductos.setForeground(new java.awt.Color(0, 51, 153));

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTotalProductos, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSeleccionProducto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12)
                    .addComponent(btnSeleccionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel28.add(jPanel29, java.awt.BorderLayout.PAGE_END);

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 51, 153));
        jLabel23.setText("Código:");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 51, 153));
        jLabel24.setText("Descripción:");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 51, 153));
        jLabel25.setText("Modelo:");

        txtCodigo_bus1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCodigo_bus1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtCodigo_bus1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigo_bus1KeyReleased(evt);
            }
        });

        txtDescripcion_bus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDescripcion_bus.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtDescripcion_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDescripcion_busKeyReleased(evt);
            }
        });

        txtModelo_bus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtModelo_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtModelo_busKeyReleased(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 51, 153));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Marca:");

        txtMarca_bus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMarca_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMarca_busKeyReleased(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 51, 153));
        jLabel30.setText("Referencia:");

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 51, 153));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Nombre Común:");

        txtNombreComun_bus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNombreComun_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreComun_busKeyReleased(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 51, 153));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Moneda:");

        cboMoneda_bus.setEditable(true);
        cboMoneda_bus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboMoneda_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboMoneda_busKeyReleased(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(0, 51, 153));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel37.setText("Unidad:");

        cboUnidad_bus.setEditable(true);
        cboUnidad_bus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboUnidad_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboUnidad_busKeyReleased(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 51, 153));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("Categoría:");

        cboCategoria_bus.setEditable(true);
        cboCategoria_bus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboCategoria_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboCategoria_busKeyReleased(evt);
            }
        });

        cbo_referencia_bus.setEditable(true);
        cbo_referencia_bus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbo_referencia_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbo_referencia_busKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cbo_referencia_busKeyReleased(evt);
            }
        });

        cboAlmacen_bus.setEditable(true);
        cboAlmacen_bus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboAlmacen_bus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAlmacen_busActionPerformed(evt);
            }
        });
        cboAlmacen_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboAlmacen_busKeyReleased(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(0, 51, 153));
        jLabel46.setText("Almacen:");

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar_24_24.png"))); // NOI18N
        jButton14.setText("Buscar");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jButton14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton14KeyReleased(evt);
            }
        });

        tabla_producto.setAutoCreateRowSorter(true);
        tabla_producto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "N°", "Código", "Descripción", "Unidad", "Cantidad", "Marca", "Modelo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla_producto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabla_productoMouseReleased(evt);
            }
        });
        tabla_producto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabla_productoKeyPressed(evt);
            }
        });
        jScrollPane8.setViewportView(tabla_producto);
        if (tabla_producto.getColumnModel().getColumnCount() > 0) {
            tabla_producto.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabla_producto.getColumnModel().getColumn(2).setPreferredWidth(100);
            tabla_producto.getColumnModel().getColumn(3).setPreferredWidth(600);
            tabla_producto.getColumnModel().getColumn(4).setPreferredWidth(200);
            tabla_producto.getColumnModel().getColumn(5).setPreferredWidth(200);
            tabla_producto.getColumnModel().getColumn(6).setPreferredWidth(300);
            tabla_producto.getColumnModel().getColumn(7).setPreferredWidth(300);
        }

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cboAlmacen_bus, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtModelo_bus, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigo_bus1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbo_referencia_bus, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDescripcion_bus)
                            .addGroup(jPanel30Layout.createSequentialGroup()
                                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cboUnidad_bus, javax.swing.GroupLayout.Alignment.LEADING, 0, 190, Short.MAX_VALUE)
                                    .addComponent(txtMarca_bus, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNombreComun_bus)
                                    .addComponent(cboMoneda_bus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(cboCategoria_bus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton14))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtCodigo_bus1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(txtDescripcion_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txtModelo_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(txtMarca_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(txtNombreComun_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(cbo_referencia_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37)
                    .addComponent(cboUnidad_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(cboMoneda_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(cboAlmacen_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38)
                    .addComponent(cboCategoria_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel28.add(jPanel30, java.awt.BorderLayout.CENTER);

        dialog_filtrar_producto.getContentPane().add(jPanel28, java.awt.BorderLayout.CENTER);

        dialog_crear_proveedor.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialog_crear_proveedor.setTitle("Provedor");

        jPanel31.setBackground(new java.awt.Color(0, 110, 204));
        jPanel31.setPreferredSize(new java.awt.Dimension(400, 40));

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/proveedor_32_32.png"))); // NOI18N
        jLabel51.setText("Crear Proveedor");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_crear_proveedor.getContentPane().add(jPanel31, java.awt.BorderLayout.PAGE_START);

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.setLayout(new java.awt.BorderLayout());

        jPanel33.setPreferredSize(new java.awt.Dimension(418, 40));

        btn_cancelar_cliente1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_32_32.png"))); // NOI18N
        btn_cancelar_cliente1.setText("Cancelar");
        btn_cancelar_cliente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_cliente1ActionPerformed(evt);
            }
        });
        btn_cancelar_cliente1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btn_cancelar_cliente1KeyReleased(evt);
            }
        });

        btn_crea_cliente1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_crea_cliente1.setText("Guardar");
        btn_crea_cliente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_crea_cliente1ActionPerformed(evt);
            }
        });
        btn_crea_cliente1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btn_crea_cliente1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                .addGap(0, 205, Short.MAX_VALUE)
                .addComponent(btn_crea_cliente1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar_cliente1))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cancelar_cliente1)
                    .addComponent(btn_crea_cliente1))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel32.add(jPanel33, java.awt.BorderLayout.PAGE_END);

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(0, 51, 153));
        jLabel52.setText("Razon Social:");

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(0, 51, 153));
        jLabel53.setText("R.U.C.:");

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(0, 51, 153));
        jLabel54.setText("Dirección:");

        txt_razon_social_proveedor_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_razon_social_proveedor_crear.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_razon_social_proveedor_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_razon_social_proveedor_crearKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_razon_social_proveedor_crearKeyTyped(evt);
            }
        });

        txt_ruc_proveedor_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_ruc_proveedor_crear.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_ruc_proveedor_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_ruc_proveedor_crearKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_ruc_proveedor_crearKeyTyped(evt);
            }
        });

        txt_direccion_proveedor_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_direccion_proveedor_crear.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_direccion_proveedor_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_direccion_proveedor_crearKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_direccion_proveedor_crearKeyTyped(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(0, 51, 153));
        jLabel55.setText("Teléfono:");

        txt_telefono_proveedor_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_telefono_proveedor_crear.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_telefono_proveedor_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_telefono_proveedor_crearKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telefono_proveedor_crearKeyTyped(evt);
            }
        });

        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(0, 51, 153));
        jLabel56.setText("Celular:");

        txt_celular_proveedor_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_celular_proveedor_crear.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_celular_proveedor_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_celular_proveedor_crearKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_celular_proveedor_crearKeyTyped(evt);
            }
        });

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(0, 51, 153));
        jLabel57.setText("Correo:");

        txt_correo_proveedor_crear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_correo_proveedor_crear.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_correo_proveedor_crear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_correo_proveedor_crearKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_correo_proveedor_crearKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_direccion_proveedor_crear)
                    .addComponent(txt_razon_social_proveedor_crear)
                    .addComponent(txt_correo_proveedor_crear, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_celular_proveedor_crear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                            .addComponent(txt_ruc_proveedor_crear, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_telefono_proveedor_crear, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(txt_razon_social_proveedor_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(txt_ruc_proveedor_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel54)
                    .addComponent(txt_direccion_proveedor_crear, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(txt_telefono_proveedor_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(txt_celular_proveedor_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(txt_correo_proveedor_crear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel32.add(jPanel34, java.awt.BorderLayout.CENTER);

        dialog_crear_proveedor.getContentPane().add(jPanel32, java.awt.BorderLayout.CENTER);

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(0, 110, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compraMaterial_32_32.png"))); // NOI18N
        jLabel1.setText("Compra de Materiales");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1010, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);

        jToolBar1.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar1.setFloatable(false);
        jToolBar1.setPreferredSize(new java.awt.Dimension(13, 55));

        btnFiltrar.setBackground(new java.awt.Color(255, 255, 255));
        btnFiltrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/filtro_24_24.png"))); // NOI18N
        btnFiltrar.setText("Filtrar");
        btnFiltrar.setFocusable(false);
        btnFiltrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFiltrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnFiltrar);
        jToolBar1.add(spFiltrar);

        btnNuevo.setBackground(new java.awt.Color(255, 255, 255));
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo_24_24.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setFocusable(false);
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNuevo);
        jToolBar1.add(spNuevo);

        btnModificar.setBackground(new java.awt.Color(255, 255, 255));
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/actualizar_24_24.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.setFocusable(false);
        btnModificar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModificar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnModificar);
        jToolBar1.add(spModificar);

        btnEliminar.setBackground(new java.awt.Color(255, 255, 255));
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar_24_24.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.setFocusable(false);
        btnEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEliminar);
        jToolBar1.add(spEliminar);

        btnImprimirSalida.setBackground(new java.awt.Color(255, 255, 255));
        btnImprimirSalida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Salida_Imprimir_24_24.png"))); // NOI18N
        btnImprimirSalida.setText("Imprimir Compra");
        btnImprimirSalida.setFocusable(false);
        btnImprimirSalida.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImprimirSalida.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImprimirSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirSalidaActionPerformed(evt);
            }
        });
        jToolBar1.add(btnImprimirSalida);
        jToolBar1.add(spImprimirSalida);

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
                "N°", "Código", "Fecha", "Proveedor", "Documento", "Número", "Moneda", "Monto", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true, true, true, true, true, true, true
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
            tabla_general.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabla_general.getColumnModel().getColumn(1).setResizable(false);
            tabla_general.getColumnModel().getColumn(1).setPreferredWidth(200);
            tabla_general.getColumnModel().getColumn(2).setPreferredWidth(150);
            tabla_general.getColumnModel().getColumn(3).setPreferredWidth(500);
            tabla_general.getColumnModel().getColumn(4).setPreferredWidth(150);
            tabla_general.getColumnModel().getColumn(5).setPreferredWidth(200);
            tabla_general.getColumnModel().getColumn(6).setPreferredWidth(200);
            tabla_general.getColumnModel().getColumn(7).setPreferredWidth(200);
            tabla_general.getColumnModel().getColumn(8).setPreferredWidth(300);
        }

        javax.swing.GroupLayout panel_tablaLayout = new javax.swing.GroupLayout(panel_tabla);
        panel_tabla.setLayout(panel_tablaLayout);
        panel_tablaLayout.setHorizontalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1010, Short.MAX_VALUE)
        );
        panel_tablaLayout.setVerticalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
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
            .addGap(0, 1010, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1010, Short.MAX_VALUE))
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

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        crear0_modificar1_producto = 0;
        id_salidamaterial_global = 0;
        MostrarObjetos(true);
        mostrarSalidaMaterial(crear0_modificar1_producto, 0);         
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btn_cancelar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_clienteActionPerformed
        CerrarDialogoCrearObras();
    }//GEN-LAST:event_btn_cancelar_clienteActionPerformed

    private void txt_nombre_obraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nombre_obraKeyTyped
        JTextField caja = txt_nombre_obra;
        int limite = 200;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_nombre_obraKeyTyped

    private void txt_buscar_proveedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_buscar_proveedorKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            mostrar_tabla_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cerrarDialogoBuscarProveedor();            
        }
    }//GEN-LAST:event_txt_buscar_proveedorKeyReleased

    private void btn_cliente_cancelar_busquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_cancelar_busquedaActionPerformed
        cerrarDialogoBuscarProveedor();                
    }//GEN-LAST:event_btn_cliente_cancelar_busquedaActionPerformed

    private void btn_cliente_seleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cliente_seleccionarActionPerformed
        SeleccionProveedor();
    }//GEN-LAST:event_btn_cliente_seleccionarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        txt_buscar_proveedor.setText("");
        dialog_buscar_proveedor.dispose();
        
        limpiar_caja_texto_crear_proveedor();
        
        dialog_crear_proveedor.setSize(429, 350);
        dialog_crear_proveedor.setLocationRelativeTo(ventana);
        dialog_crear_proveedor.setModal(true);
        dialog_crear_proveedor.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tablaProveedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaProveedorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            SeleccionProveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cerrarDialogoBuscarProveedor();            
        }
    }//GEN-LAST:event_tablaProveedorKeyPressed

    private void btn_crea_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_crea_clienteActionPerformed
        crear_obra();
    }//GEN-LAST:event_btn_crea_clienteActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        CerrarDialogoCrearSalidaMaterial();
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarActionPerformed
        int fila = tabla_detalle_compre_material.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro.");
        } else {
            DefaultTableModel tabla = (DefaultTableModel) tabla_detalle_compre_material.getModel();
            tabla.removeRow(fila);
            calcularTotales(obtenerSubTotal());
        }
    }//GEN-LAST:event_EliminarActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        crearmodificarSalidaMaterial();
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        int fila = tabla_general.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro.");
        } else {
            int band = 0;
            DefaultTableModel tm = (DefaultTableModel) tabla_general.getModel();
                
            if(band == 0){                
                int id_salida = Integer.parseInt((String) tm.getValueAt(fila, 1));

                crear0_modificar1_producto = 1;
                id_salidamaterial_global = id_salida;
                MostrarObjetos(true);
                mostrarSalidaMaterial(crear0_modificar1_producto, id_salida);
            }                        
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int fila = tabla_general.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro.");
        } else {
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este registro?", "Eliminar", JOptionPane.YES_NO_OPTION);
            
            if (respuesta == JOptionPane.YES_OPTION) {
                DefaultTableModel tm = (DefaultTableModel) tabla_general.getModel();
                
                CompraMaterialBE obj = new CompraMaterialBE();
                obj.setIdCompra(Integer.parseInt((String) tm.getValueAt(fila, 1)));
                obj.setId_empresa(id_empresa_index);

                try
                {
                    objCompraMaterialBL.delete(obj);
                    mostrar_tabla_general();
                    JOptionPane.showMessageDialog(null, "Registro eliminado con éxito.");                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        dialog_filtrar_compra.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        mostrar_tabla_general();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        if(limpiarCboFiltros){
            MostrarComboObras(cboObraBus, true, true, null);
            MostrarCombo(cboEstadoBus, 10, true, true, null);
            MostrarCombo(cboMonedaBus, 2, true, true, null);
            MostrarCombo(cboDocumentoBus, 11, true, true, null);
            limpiarCboFiltros = false;
        }
        
        dialog_filtrar_compra.setSize(690, 354);
        dialog_filtrar_compra.setLocationRelativeTo(ventana);
        dialog_filtrar_compra.setModal(true);
        dialog_filtrar_compra.setVisible(true);
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        mostrar_tabla_proveedor();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txt_nombre_obraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nombre_obraKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_obra();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearObras(); 
        }
    }//GEN-LAST:event_txt_nombre_obraKeyReleased

    private void tablaProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProveedorMouseClicked
       if (evt.getClickCount() == 2) {
           SeleccionProveedor();
       }
    }//GEN-LAST:event_tablaProveedorMouseClicked

    private void btn_cliente_seleccionarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_cliente_seleccionarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            SeleccionProveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cerrarDialogoBuscarProveedor();            
        }
    }//GEN-LAST:event_btn_cliente_seleccionarKeyReleased

    private void btn_cliente_cancelar_busquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_cliente_cancelar_busquedaKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cerrarDialogoBuscarProveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cerrarDialogoBuscarProveedor();            
        }
    }//GEN-LAST:event_btn_cliente_cancelar_busquedaKeyReleased

    private void btn_crea_clienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_crea_clienteKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_obra();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearObras(); 
        }
    }//GEN-LAST:event_btn_crea_clienteKeyReleased

    private void btn_cancelar_clienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_cancelar_clienteKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CerrarDialogoCrearObras();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearObras(); 
        }
    }//GEN-LAST:event_btn_cancelar_clienteKeyReleased

    private void txtNumDocumentoBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumDocumentoBusKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_compra.dispose(); 
        }
    }//GEN-LAST:event_txtNumDocumentoBusKeyReleased

    private void cboObraBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboObraBusKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_compra.dispose(); 
        }
    }//GEN-LAST:event_cboObraBusKeyReleased

    private void jButton8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton8KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_compra.dispose(); 
        }
    }//GEN-LAST:event_jButton8KeyReleased

    private void jButton7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton7KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            dialog_filtrar_compra.dispose(); 
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_compra.dispose(); 
        }
    }//GEN-LAST:event_jButton7KeyReleased

    private void cboObraBusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboObraBusKeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_compra.dispose(); 
        }
    }//GEN-LAST:event_cboObraBusKeyPressed

    private void btn_cancelarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_cancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CerrarDialogoCrearSalidaMaterial();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearSalidaMaterial();
        }
    }//GEN-LAST:event_btn_cancelarKeyReleased

    private void btn_guardarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_guardarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearmodificarSalidaMaterial();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearSalidaMaterial();
        }
    }//GEN-LAST:event_btn_guardarKeyReleased

    private void txtFechaCompraBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaCompraBusKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_compra.dispose(); 
        }
    }//GEN-LAST:event_txtFechaCompraBusKeyReleased

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        cerrarDialogouscarProducto();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton12KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            dialog_filtrar_producto.dispose();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_jButton12KeyReleased

    private void btnSeleccionProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionProductoActionPerformed
        SeleccionProducto();
    }//GEN-LAST:event_btnSeleccionProductoActionPerformed

    private void btnSeleccionProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSeleccionProductoKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_btnSeleccionProductoKeyReleased

    private void txtCodigo_bus1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigo_bus1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_txtCodigo_bus1KeyReleased

    private void txtDescripcion_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcion_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_txtDescripcion_busKeyReleased

    private void txtModelo_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtModelo_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_txtModelo_busKeyReleased

    private void txtMarca_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMarca_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_txtMarca_busKeyReleased

    private void txtNombreComun_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreComun_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_txtNombreComun_busKeyReleased

    private void cboMoneda_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboMoneda_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_cboMoneda_busKeyReleased

    private void cboUnidad_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboUnidad_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_cboUnidad_busKeyReleased

    private void cboCategoria_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboCategoria_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_cboCategoria_busKeyReleased

    private void cbo_referencia_busKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbo_referencia_busKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_cbo_referencia_busKeyPressed

    private void cbo_referencia_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbo_referencia_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_cbo_referencia_busKeyReleased

    private void cboAlmacen_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboAlmacen_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_cboAlmacen_busKeyReleased

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        filtrarProducto();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton14KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton14KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_jButton14KeyReleased

    private void tabla_productoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_productoMouseReleased
        /*if (band_index == 0) {
            int fila;
            int id_producto;
            fila = tabla_general.getSelectedRow();
            m = (DefaultTableModel) tabla_general.getModel();
            id_producto = Integer.parseInt((String) m.getValueAt(fila, 0));
            mostrar_datos_producto(id_producto);
        }*/
    }//GEN-LAST:event_tabla_productoMouseReleased

    private void btnNuevoObraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoObraActionPerformed
        limpiar_caja_texto_crear_obra();
        MostrarComboCliente(cboCliente, true, true, null);
        dialog_crear_obra.setSize(700,300);
        dialog_crear_obra.setLocationRelativeTo(ventana);
        dialog_crear_obra.setModal(true);
        dialog_crear_obra.setVisible(true);
    }//GEN-LAST:event_btnNuevoObraActionPerformed

    private void txtCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyTyped
        JTextField caja = txtCodigo;
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txtCodigoKeyTyped

    private void txtCodigoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearmodificarSalidaMaterial();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearSalidaMaterial();
        }
    }//GEN-LAST:event_txtCodigoKeyReleased

    private void tabla_detalle_compre_materialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_detalle_compre_materialMouseClicked
        if (evt.getButton() == 3 && crear0_modificar1_producto != 2) {
            mantenimiento_tabla_detalle_salida.show(tabla_detalle_compre_material, evt.getX(), evt.getY());                        
        }
    }//GEN-LAST:event_tabla_detalle_compre_materialMouseClicked

    private void btn_guardar_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardar_detalleActionPerformed
        crearDetalleSalida();
    }//GEN-LAST:event_btn_guardar_detalleActionPerformed

    private void txtCantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyTyped
        JTextField caja = txtCantidad;
        ingresar_solo_numeros(caja, evt);
        int limite = 18;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txtCantidadKeyTyped

    private void btn_cancelar_guardar_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_guardar_detalleActionPerformed
        limpiarCajaTextoCrearDetalleSalidaMaterial();
    }//GEN-LAST:event_btn_cancelar_guardar_detalleActionPerformed

    private void btn_buscar_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_proveedorActionPerformed
        MostrarCombo(cbo_referencia_bus, 4, true, true, null);
        MostrarCombo(cboMoneda_bus, 2, true, true, null);
        MostrarCombo(cboUnidad_bus, 1, true, true, null);
        MostrarCombo(cboAlmacen_bus, 3, true, true, null);
        MostrarCombo(cboCategoria_bus, 6, true, true, null);
        
        DefaultTableModel tabla = (DefaultTableModel) tabla_producto.getModel();
        tabla.setRowCount(0);
            
        dialog_filtrar_producto.setSize(850, 500);
        dialog_filtrar_producto.setLocationRelativeTo(ventana);
        dialog_filtrar_producto.setModal(true);
        dialog_filtrar_producto.setVisible(true);                
                
    }//GEN-LAST:event_btn_buscar_proveedorActionPerformed

    private void cboClienteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboClienteItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboClienteItemStateChanged

    private void txt_direccion_obraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_direccion_obraKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_direccion_obraKeyTyped

    private void tabla_productoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_productoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
             SeleccionProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_tabla_productoKeyPressed

    private void cboAlmacen_busActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAlmacen_busActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboAlmacen_busActionPerformed

    private void tabla_detalle_compre_materialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_detalle_compre_materialKeyReleased
        
    }//GEN-LAST:event_tabla_detalle_compre_materialKeyReleased

    private void txtCantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearDetalleSalida();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearSalidaMaterial();        
        }
    }//GEN-LAST:event_txtCantidadKeyPressed

    private void txtProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearDetalleSalida();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearSalidaMaterial();        
        }
    }//GEN-LAST:event_txtProductoKeyPressed

    private void txtUnidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnidadKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearDetalleSalida();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearSalidaMaterial();        
        }
    }//GEN-LAST:event_txtUnidadKeyPressed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        limpiarCboFiltros = true;
        txtCodigoBus.setText("");
        txtProveedorBus.setText("");
        txtNumDocumentoBus.setText("");
        txtFechaCompraBus.setDate(null);
        
        incializarCombo(cboObraBus);
        incializarCombo(cboDocumentoBus);
        incializarCombo(cboEstadoBus);
        incializarCombo(cboMonedaBus);
        
        mostrar_tabla_general();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton10KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10KeyReleased

    private void btnImprimirSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirSalidaActionPerformed
        int fila = tabla_general.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro.");
        } else {
            try
            {
                int band = 0;
                DefaultTableModel tm = (DefaultTableModel) tabla_general.getModel();
                int idSalidaMaterial = Integer.parseInt((String) tm.getValueAt(fila, 1));
                int estadoAbierto = objCompraMaterialBL.readId(idSalidaMaterial, id_empresa_index).getEstadoAbierto();
        
                if(band == 0){                    
                    switch (estadoAbierto){
                        case 100001: 
                            int respuesta = JOptionPane.showConfirmDialog(null, "Una vez impreso el documento, ya no se podrán efectuar cambios.\n\n"
                                    + "¿Desea continuar con esta operación?", "Confirmar Compra de material e Imprimir", JOptionPane.YES_NO_OPTION);
            
                            if (respuesta == JOptionPane.YES_OPTION) {
                                CompraMaterialBE obj = new CompraMaterialBE();
                                obj.setIdCompra(idSalidaMaterial);
                                obj.setId_empresa(id_empresa_index);
                                obj.setUsuarioModifica(aliasUsuarioIndex);

                                objCompraMaterialBL.confirmarCompra(obj);
                                imprimirSalidaMaterial(idSalidaMaterial);    
                                mostrar_tabla_general();
                            }
                            
                            break;
                        default:
                            imprimirSalidaMaterial(idSalidaMaterial);
                            break;
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }            
        }
    }//GEN-LAST:event_btnImprimirSalidaActionPerformed

    private void tabla_generalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_generalKeyReleased
        tablaGeneralClick();
    }//GEN-LAST:event_tabla_generalKeyReleased

    private void cboEstadoBusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboEstadoBusKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboEstadoBusKeyPressed

    private void cboEstadoBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboEstadoBusKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cboEstadoBusKeyReleased

    private void tabla_generalMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_generalMouseReleased
        tablaGeneralClick();
    }//GEN-LAST:event_tabla_generalMouseReleased

    private void cboMonedaBusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboMonedaBusKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboMonedaBusKeyPressed

    private void cboMonedaBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboMonedaBusKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cboMonedaBusKeyReleased

    private void cboDocumentoBusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboDocumentoBusKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboDocumentoBusKeyPressed

    private void cboDocumentoBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboDocumentoBusKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cboDocumentoBusKeyReleased

    private void txtNumDocumentoBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumDocumentoBusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumDocumentoBusActionPerformed

    private void cboEstadoBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEstadoBusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboEstadoBusActionPerformed

    private void txtCodigoBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoBusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoBusActionPerformed

    private void txtCodigoBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoBusKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoBusKeyReleased

    private void txtProveedorBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProveedorBusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProveedorBusActionPerformed

    private void txtProveedorBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProveedorBusKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProveedorBusKeyReleased

    private void btnBuscarProvedorFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProvedorFiltroActionPerformed
        bandBusProveedor = 0;
        mostrarDialogoBuscarProveedor();
    }//GEN-LAST:event_btnBuscarProvedorFiltroActionPerformed

    private void btn_cancelar_cliente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_cliente1ActionPerformed
        CerrarDialogoCrearProveedor();
    }//GEN-LAST:event_btn_cancelar_cliente1ActionPerformed

    private void btn_cancelar_cliente1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_cancelar_cliente1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CerrarDialogoCrearProveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor();
        }
    }//GEN-LAST:event_btn_cancelar_cliente1KeyReleased

    private void btn_crea_cliente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_crea_cliente1ActionPerformed
        crear_proveedor();
    }//GEN-LAST:event_btn_crea_cliente1ActionPerformed

    private void btn_crea_cliente1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_crea_cliente1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor();
        }
    }//GEN-LAST:event_btn_crea_cliente1KeyReleased

    private void txt_razon_social_proveedor_crearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_razon_social_proveedor_crearKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor();
        }
    }//GEN-LAST:event_txt_razon_social_proveedor_crearKeyReleased

    private void txt_razon_social_proveedor_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_razon_social_proveedor_crearKeyTyped
        JTextField caja = txt_razon_social_proveedor_crear;
        int limite = 200;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_razon_social_proveedor_crearKeyTyped

    private void txt_ruc_proveedor_crearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ruc_proveedor_crearKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor();
        }
    }//GEN-LAST:event_txt_ruc_proveedor_crearKeyReleased

    private void txt_ruc_proveedor_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ruc_proveedor_crearKeyTyped
        JTextField caja = txt_ruc_proveedor_crear;
        ingresar_solo_numeros(caja, evt);
        int limite = 11;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_ruc_proveedor_crearKeyTyped

    private void txt_direccion_proveedor_crearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_direccion_proveedor_crearKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor();
        }
    }//GEN-LAST:event_txt_direccion_proveedor_crearKeyReleased

    private void txt_direccion_proveedor_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_direccion_proveedor_crearKeyTyped
        JTextField caja = txt_direccion_proveedor_crear;
        int limite = 200;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_direccion_proveedor_crearKeyTyped

    private void txt_telefono_proveedor_crearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefono_proveedor_crearKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor();
        }
    }//GEN-LAST:event_txt_telefono_proveedor_crearKeyReleased

    private void txt_telefono_proveedor_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefono_proveedor_crearKeyTyped
        JTextField caja = txt_telefono_proveedor_crear;
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_telefono_proveedor_crearKeyTyped

    private void txt_celular_proveedor_crearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_celular_proveedor_crearKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor();
        }
    }//GEN-LAST:event_txt_celular_proveedor_crearKeyReleased

    private void txt_celular_proveedor_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_celular_proveedor_crearKeyTyped
        JTextField caja = txt_celular_proveedor_crear;
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_celular_proveedor_crearKeyTyped

    private void txt_correo_proveedor_crearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_correo_proveedor_crearKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor();
        }
    }//GEN-LAST:event_txt_correo_proveedor_crearKeyReleased

    private void txt_correo_proveedor_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_correo_proveedor_crearKeyTyped
        JTextField caja = txt_correo_proveedor_crear;
        int limite = 50;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_correo_proveedor_crearKeyTyped

    private void btnNuevoProveedorCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoProveedorCompraActionPerformed
        txt_buscar_proveedor.setText("");
        limpiar_caja_texto_crear_proveedor();
        bandBusProveedor = 1;
        dialog_crear_proveedor.setSize(429, 350);
        dialog_crear_proveedor.setLocationRelativeTo(ventana);
        dialog_crear_proveedor.setModal(true);
        dialog_crear_proveedor.setVisible(true);
    }//GEN-LAST:event_btnNuevoProveedorCompraActionPerformed

    private void btnBuscarProveedorCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProveedorCompraActionPerformed
        bandBusProveedor = 1;
        mostrarDialogoBuscarProveedor();
    }//GEN-LAST:event_btnBuscarProveedorCompraActionPerformed

    private void txtPrecioUnitarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioUnitarioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearDetalleSalida();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearSalidaMaterial();        
        }
    }//GEN-LAST:event_txtPrecioUnitarioKeyPressed

    private void txtPrecioUnitarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioUnitarioKeyTyped
        JTextField caja = txtCantidad;
        ingresar_solo_numeros(caja, evt);
        int limite = 18;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txtPrecioUnitarioKeyTyped

    private void txtTotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalKeyPressed

    private void txtCalculoIgvKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCalculoIgvKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCalculoIgvKeyPressed

    private void txtSubTotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSubTotalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubTotalKeyPressed

    private void txtTotalLetrasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalLetrasKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalLetrasKeyPressed

    private void cboMonedaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboMonedaItemStateChanged
        calcularTotales(obtenerSubTotal());
    }//GEN-LAST:event_cboMonedaItemStateChanged

    private void cboIgvItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboIgvItemStateChanged
        calcularTotales(obtenerSubTotal());
    }//GEN-LAST:event_cboIgvItemStateChanged

    private void txtFechaCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaCompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaCompraActionPerformed

    private void txtNumeroCompraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroCompraKeyTyped
        JTextField caja = txtNumeroCompra;
        int limite = 15;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txtNumeroCompraKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Eliminar;
    private javax.swing.JButton btnBuscarProvedorFiltro;
    private javax.swing.JButton btnBuscarProveedorCompra;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnImprimirSalida;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnNuevoObra;
    private javax.swing.JButton btnNuevoProveedorCompra;
    private javax.swing.JButton btnSeleccionProducto;
    private javax.swing.JButton btn_buscar_proveedor;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_cancelar_cliente;
    private javax.swing.JButton btn_cancelar_cliente1;
    private javax.swing.JButton btn_cancelar_guardar_detalle;
    private javax.swing.JButton btn_cliente_cancelar_busqueda;
    private javax.swing.JButton btn_cliente_seleccionar;
    private javax.swing.JButton btn_crea_cliente;
    private javax.swing.JButton btn_crea_cliente1;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_guardar_detalle;
    private javax.swing.JComboBox cboAlmacen_bus;
    private javax.swing.JComboBox cboCategoria_bus;
    private javax.swing.JComboBox cboCliente;
    private javax.swing.JComboBox cboDocumento;
    private javax.swing.JComboBox cboDocumentoBus;
    private javax.swing.JComboBox cboEstadoBus;
    private javax.swing.JComboBox cboIgv;
    private javax.swing.JComboBox cboMoneda;
    private javax.swing.JComboBox cboMonedaBus;
    private javax.swing.JComboBox cboMoneda_bus;
    private javax.swing.JComboBox cboObra;
    private javax.swing.JComboBox cboObraBus;
    private javax.swing.JComboBox cboUnidad_bus;
    private javax.swing.JComboBox cbo_referencia_bus;
    private javax.swing.JPanel centro;
    private javax.swing.JDialog dialog_buscar_proveedor;
    private javax.swing.JDialog dialog_crear_compra;
    private javax.swing.JDialog dialog_crear_obra;
    private javax.swing.JDialog dialog_crear_proveedor;
    private javax.swing.JDialog dialog_filtrar_compra;
    private javax.swing.JDialog dialog_filtrar_producto;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
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
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel18;
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
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel lblIdProducto;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalProductos;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JPopupMenu mantenimiento_tabla_detalle_salida;
    private javax.swing.JPanel norte;
    private javax.swing.JPanel panel_nuevo_detalle;
    private javax.swing.JPanel panel_tabla;
    private javax.swing.JToolBar.Separator spEliminar;
    private javax.swing.JToolBar.Separator spFiltrar;
    private javax.swing.JToolBar.Separator spImprimirSalida;
    private javax.swing.JToolBar.Separator spModificar;
    private javax.swing.JToolBar.Separator spNuevo;
    private javax.swing.JPanel sur;
    private javax.swing.JTable tablaProveedor;
    private javax.swing.JTable tabla_detalle_compre_material;
    private javax.swing.JTable tabla_general;
    private javax.swing.JTable tabla_producto;
    private javax.swing.JTextField txtCalculoIgv;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtCodigoBus;
    private javax.swing.JTextField txtCodigo_bus1;
    private javax.swing.JTextField txtDesProveedor;
    private javax.swing.JTextField txtDescripcion_bus;
    private org.jdesktop.swingx.JXDatePicker txtFechaCompra;
    private org.jdesktop.swingx.JXDatePicker txtFechaCompraBus;
    private javax.swing.JTextField txtMarca_bus;
    private javax.swing.JTextField txtModelo_bus;
    private javax.swing.JTextField txtNombreComun_bus;
    private javax.swing.JTextField txtNumDocumentoBus;
    private javax.swing.JTextField txtNumeroCompra;
    private javax.swing.JTextField txtPrecioUnitario;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JTextField txtProveedorBus;
    private javax.swing.JTextField txtRucProveedorCompra;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTotalLetras;
    private javax.swing.JTextField txtUnidad;
    private javax.swing.JTextField txt_buscar_proveedor;
    private javax.swing.JTextField txt_celular_proveedor_crear;
    private javax.swing.JTextField txt_correo_proveedor_crear;
    private javax.swing.JTextArea txt_direccion_obra;
    private javax.swing.JTextField txt_direccion_proveedor_crear;
    private javax.swing.JTextField txt_nombre_obra;
    private javax.swing.JTextField txt_razon_social_proveedor_crear;
    private javax.swing.JTextField txt_ruc_proveedor_crear;
    private javax.swing.JTextField txt_telefono_proveedor_crear;
    // End of variables declaration//GEN-END:variables

    private void MostrarComboMoneda(JComboBox cbo) {
        MonedaBE pbe = new MonedaBE();
        
        try {
            List<MonedaBE> lmoneda = objMonedaBL.MonedaListar(pbe);
            if (lmoneda != null && !lmoneda.isEmpty()) {
                AutoCompleteDecorator.decorate(cbo);
                DefaultComboBoxModel cboModel = new DefaultComboBoxModel();
                //cboModel.addElement("");
                
                for (MonedaBE monedaBE : lmoneda) {
                    cboModel.addElement(monedaBE.getNombre());                    
                }
                
                cbo.setModel(cboModel);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }   
    }

    private CompraMaterialBE ObtenerFiltrosBusqueda() {
        CompraMaterialBE pbe = new CompraMaterialBE();
        
        if(txtCodigoBus.getText().trim().length() > 0)
            pbe.setIdCompra(Integer.parseInt(txtCodigoBus.getText().trim()));
        
        if(txtFechaCompraBus.getDate() != null){
            java.util.Date utilDate = txtFechaCompraBus.getDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            pbe.setFechaCompra(sqlDate);
        }
        
        if(txtProveedorBus.getText().trim().length() > 0)
            pbe.setDesProveedor(txtProveedorBus.getText().trim());
        
        if(cboObraBus.getSelectedItem()!= null && cboObraBus.getSelectedItem().toString().length() > 0)
            pbe.setDesObra(cboObraBus.getSelectedItem().toString().trim());
        
        if(cboDocumentoBus.getSelectedItem()!= null && cboDocumentoBus.getSelectedItem().toString().length() > 0)
            pbe.setDesDocumento(cboDocumentoBus.getSelectedItem().toString().trim());
        
        if(cboMonedaBus.getSelectedItem()!= null && cboMonedaBus.getSelectedItem().toString().length() > 0)
            pbe.setDesMoneda(cboMonedaBus.getSelectedItem().toString().trim());
        
        if(cboEstadoBus.getSelectedItem()!= null && cboEstadoBus.getSelectedItem().toString().length() > 0)
            pbe.setDesEstadoAbierto(cboEstadoBus.getSelectedItem().toString().trim());
        
        if(txtNumDocumentoBus.getText().trim().length() > 0)
            pbe.setNumeroCompra(txtNumDocumentoBus.getText().trim());
        
        pbe.setId_empresa(id_empresa_index);
        
        return pbe;
    }

    private void incializarCombo(JComboBox cbo) {
        DefaultComboBoxModel cboModel = new DefaultComboBoxModel();
        cbo.setModel(cboModel);
    }

    private void mostrarSalidaMaterial(int accion, int id_salida) {
        if(accion == 0){
            System.out.println("capturar fecha y poner en caja de texto");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d = new Date();
            String s = sdf.format(d);
            txtFechaCompra.setDate(d);
            cargarCombos();
            limpiar_caja_texto_crear_salidamaterial();
        }            
        else{
            limpiar_caja_texto_crear_salidamaterial();
            modificarMaterial(id_salida);
        }            
        
        dialog_crear_compra.setSize(925, 625);
        dialog_crear_compra.setLocationRelativeTo(ventana);
        dialog_crear_compra.setModal(true);
        dialog_crear_compra.setVisible(true);       
    }

    private void cargarCombos() {
        MostrarComboObras(cboObra, true, true, null);
        MostrarComboIgv(cboIgv, false, false, null);
        MostrarCombo(cboMoneda, 2, false, true, null);
        MostrarCombo(cboDocumento, 11, false, true, null);
            
    }

    private void mostrarVentanaCrearDC(int codTabla, String titulo, String labelDescripcion, boolean mostrarLblDescripcion, String labelValor, boolean mostrarLblValor) {
//        gCodigoTabla = codTabla;
//        
//        lblTituloDatoComun.setText(titulo);
//        lblDescripcion.setText(labelDescripcion);
//        lblValor.setText(labelValor);
//        
//        if(mostrarLblDescripcion){
//            lblDescripcion.setVisible(true);
//            txtDescripcionCorta.setVisible(true);
//        }else{
//            lblDescripcion.setVisible(false);
//            txtDescripcionCorta.setVisible(false);
//        }
//        
//        if(mostrarLblValor){
//            lblValor.setVisible(true);
//            txtValor1.setVisible(true);
//        }else{
//            lblValor.setVisible(false);
//            txtValor1.setVisible(false);
//        }
//        
//        dialog_crear_datoComun.setSize(430, 300);
//        dialog_crear_datoComun.setLocationRelativeTo(ventana);
//        dialog_crear_datoComun.setModal(true);
//        dialog_crear_datoComun.setVisible(true);
    }

    private void tablaProveedorBuscar(List<ProveedorBE> list) {
        DefaultTableModel tabla = (DefaultTableModel) tablaProveedor.getModel();
        tabla.setRowCount(0);
        for (ProveedorBE obj : list) {
            Object[] fila = {
                obj.getId_provedor(), 
                obj.getRuc(),
                obj.getRazon_social()
                };
            tabla.addRow(fila);
        }
        
        tablaProveedor.setRowHeight(35);
    }

    private void tablaSalidaMaterialDetalle(List<CompraMaterialDetalleBE> lista) {
        DefaultTableModel tabla = (DefaultTableModel) tabla_detalle_compre_material.getModel();
        tabla.setRowCount(0);
        for (CompraMaterialDetalleBE obj : lista) {
            Object[] fila = {
                obj.getId_producto(),
                obj.getDesMaterial(),
                obj.getDesUnidad(),
                obj.getCantidad(),
                obj.getPrecioUnitario(),
                obj.getPrecioTotal()
                };
            tabla.addRow(fila);
        }
        
        tabla_detalle_compre_material.setRowHeight(35);
        calcularTotales(obtenerSubTotal());
    }
    
    private void tablaProveedorDetalle(List<ProductoDetalleBE> listaProDet) {
        DefaultTableModel tabla = (DefaultTableModel) tabla_detalle_compre_material.getModel();
        tabla.setRowCount(0);
        for (ProductoDetalleBE obj : listaProDet) {
            Object[] fila = {
                obj.getRucProveedor(),
                obj.getRazonsocialProveedor(),
                obj.getPrecio()
                };
            tabla.addRow(fila);
        }
        
        tabla_detalle_compre_material.setRowHeight(35);
    }

    private void crearDatoComun() {
//        
//        if(gCodigoTabla != 0){
//            String descripcion = txtDescripcionCorta.getText().trim();
//            String valor = txtValor1.getText().trim();
//            int band = 0;
//            
//            DatoComunBE objdc  = new DatoComunBE();
//            
//            if(band == 0)
//            {
//                if(txtDescripcionCorta.isVisible()){
//                    if(descripcion != null && descripcion.length()>0)
//                        objdc.setDescripcionCorta(descripcion);
//                    else{
//                        JOptionPane.showMessageDialog(null, "Ingrese el valor de todos los campos.");    
//                        band++;
//                    }
//                }   
//            }
//            
//            if(band == 0)
//            {
//                if(txtValor1.isVisible()){
//                    if(valor != null && valor.length()>0)
//                        objdc.setValorTexto1(valor);
//                    else{
//                        JOptionPane.showMessageDialog(null, "Ingrese el valor de todos los campos.");    
//                        band++;
//                    }
//                }   
//            }
//            
//            if(band == 0){
//                objdc.setId_empresa(id_empresa_index);
//                objdc.setUsuarioDes(user_index);
//                objdc.setCodigoTabla(gCodigoTabla);
//                
//                try {
//                 
//                    int resp = objDatoComunBL.crear(objdc);
//                    
//                    switch (gCodigoTabla) {
//                        //Unidad
//                        case 1:
//                            //MostrarCombo(cboUnidad, 1, false, true, descripcion);
//                            break;
//                        
//                        //Referencia
//                        case 4:
//                            //MostrarCombo(cboReferencia, 4, false, true, descripcion);
//                            break;
//                            
//                        //Categoria
//                        case 6:
//                            //MostrarCombo(cboCategoria, 6, false, true, descripcion);
//                            break;
//                    }
//                
//                    cerrarDialogoCrearDatoComun();  
//                    
//                } catch (Exception e) {
//                    JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//            
//            
//        }else{
//            JOptionPane.showMessageDialog(null, "No se enconró el codigo de tabla");
//        }
    }

    private void SeleccionProveedor() {
        int fila;
        fila = tablaProveedor.getSelectedRow();
        
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        } else {
            m = (DefaultTableModel) tablaProveedor.getModel();
            
            int id_proveedor = (Integer) m.getValueAt(fila, 0);
            String ruc = (String) m.getValueAt(fila, 1);
            String razon_social = (String) m.getValueAt(fila, 2);
            
            if(bandBusProveedor == 0){
                txtProveedorBus.setText(razon_social);
            }else{
                txtDesProveedor.setText(razon_social);
                txtRucProveedorCompra.setText(ruc);
            }
            
            cerrarDialogoBuscarProveedor();
        }
    }
    
    private void SeleccionProducto() {
        int fila;
        fila = tabla_producto.getSelectedRow();
        
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        } else {
            m = (DefaultTableModel) tabla_producto.getModel();
            
            int id = (Integer) m.getValueAt(fila, 0);
            String descripcion = (String) m.getValueAt(fila, 3);
            String unidad = (String) m.getValueAt(fila, 4);
            double stock = (Double) m.getValueAt(fila, 5);
            
            lblIdProducto.setVisible(false);
            lblIdProducto.setText(""+id);    
            txtProducto.setText(descripcion);
            txtUnidad.setText(unidad);
            cerrarDialogouscarProducto();
        }
    }
    
    private void cerrarDialogouscarProducto(){
        txtCodigo_bus1.setText("");
        txtDescripcion_bus.setText("");
        txtModelo_bus.setText("");
        txtMarca_bus.setText("");
        txtNombreComun_bus.setText("");
        
        incializarCombo(cbo_referencia_bus);
        incializarCombo(cboUnidad_bus);
        incializarCombo(cboMoneda_bus);
        incializarCombo(cboAlmacen_bus);
        incializarCombo(cboCategoria_bus);
        
        dialog_filtrar_producto.dispose();
    }

    private void cerrarDialogoBuscarProveedor() {
        txt_buscar_proveedor.setText("");
        dialog_buscar_proveedor.dispose();
    }

    private void cerrarDialogoCrearDatoComun() {
//        limpiar_caja_texto_datocomun();
//        dialog_crear_datoComun.dispose();
    }

    private void CerrarDialogoCrearObras() {
        limpiar_caja_texto_crear_obra();
        dialog_crear_obra.dispose();
    }

    private void filtrarProducto() {
        mostrar_tabla_productos(); 
        dialog_filtrar_compra.dispose();
    }

    private void CerrarDialogoCrearSalidaMaterial() {
        limpiarCajaTextoCrearDetalleSalidaMaterial();
        limpiar_caja_texto_crear_salidamaterial();
        
        crear0_modificar1_producto = 0;
        id_salidamaterial_global = 0;
        
        DefaultTableModel tablaDetalle = (DefaultTableModel) tabla_detalle_compre_material.getModel();
        tablaDetalle.setRowCount(0);
        
        MostrarObjetos(true);
        dialog_crear_compra.dispose();
    }

    private CompraMaterialBE capturarValorSalidaMaterial() {
        
        CompraMaterialBE sm = null;
        
        String IdCompra = txtCodigo.getText().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date FechaCompra = txtFechaCompra.getDate();
        
        String NumeroCompra = txtNumeroCompra.getText();
        String CalculoIgv = txtCalculoIgv.getText().trim();
        String SubTotal = txtSubTotal.getText().trim();
        String Total = txtTotal.getText().trim();
        String TotalLetras = txtTotalLetras.getText().trim();
        
        String DesProveedor = txtDesProveedor.getText();        
        String DesMoneda = cboMoneda.getSelectedItem().toString();
        String DesIgv = cboIgv.getSelectedItem().toString();
        String DesObra = cboObra.getSelectedItem().toString();
        String DesDocumento = cboDocumento.getSelectedItem().toString();
        
        int band = 0;
        
        if(FechaCompra == null){
            JOptionPane.showMessageDialog(null, "Seleccione una fecha.");
            band++;
        }
        
        if(band == 0){
            if(DesProveedor.length() == 0){
                JOptionPane.showMessageDialog(null, "Seleccione un Proveedor.");
                band++;
            }
        }
        
        if(band == 0){
            if(DesMoneda.length() == 0){
                JOptionPane.showMessageDialog(null, "Seleccione una Moneda.");
                band++;
            }
        }
        
        if(band == 0){
            if(DesDocumento.length() == 0){
                JOptionPane.showMessageDialog(null, "Seleccione una Documento.");
                band++;
            }
        }
        
        if(band == 0){
            if(DesIgv.length() == 0){
                JOptionPane.showMessageDialog(null, "Seleccione el I.G.V.");
                band++;
            }
        }
    
        if(band == 0){
            if(NumeroCompra.length() == 0){
                JOptionPane.showMessageDialog(null, "Ingrese el Número de compra.");
                band++;
            }
        }
        
        if(band == 0){
            DefaultTableModel tabla = (DefaultTableModel) tabla_detalle_compre_material.getModel();
            
            if(tabla.getRowCount() == 0){
                JOptionPane.showMessageDialog(null, "Ingrese los materiales a comprados.");
                band++;
            }            
        }
        
        if(band == 0){
            sm = new CompraMaterialBE();
            
            if(IdCompra.length() > 0)
                sm.setIdCompra(Integer.parseInt(IdCompra));
            
            sm.setFechaCompra(FechaCompra);
            sm.setCalculoIgv(new BigDecimal(CalculoIgv));
            sm.setSubTotal(new BigDecimal(SubTotal));
            sm.setTotal(new BigDecimal(Total));
            sm.setTotalLetras(TotalLetras);
            sm.setDesProveedor(DesProveedor);
            sm.setDesMoneda(DesMoneda);
            sm.setDesIgv(Double.parseDouble(DesIgv));
            
            if(DesObra.length() > 0)
                sm.setDesObra(DesObra);
            
            sm.setDesDocumento(DesDocumento);
            sm.setNumeroCompra(NumeroCompra);
            sm.setId_empresa(id_empresa_index);
            sm.setUsuarioInserta(aliasUsuarioIndex);
            sm.setTipoOperacion(crear0_modificar1_producto);
        }
        
        return sm;
    }

    private void crearmodificarSalidaMaterial() {
        try {
            int band = 0;
            int filas =  tabla_detalle_compre_material.getRowCount();
        
            /*verifica si se agrego materiales a la salida*/
            if(filas < 1){
                JOptionPane.showMessageDialog(null, "Ingrese los materiales comprados.", "ERROR", JOptionPane.ERROR_MESSAGE);
                band++;
            }
        
            /*crea la salida y el detalle*/
            if(band == 0){
                CompraMaterialBE sm = capturarValorSalidaMaterial();

                if(sm != null){
                    int id_salida = objCompraMaterialBL.create(sm);
                    List<CompraMaterialDetalleBE> listaSalidaDet = ObtenerRegistrosSalidaDetalle(id_salida);

                    if(listaSalidaDet != null && !listaSalidaDet.isEmpty()){
                        int i = 0;
                        for (CompraMaterialDetalleBE obj : listaSalidaDet) {
                            if(i == 0){
                                objCompraMaterialDetalleBL.deleteAll(id_salida, obj.getId_empresa());
                            }                                

                            objCompraMaterialDetalleBL.create(obj);
                            i++;
                        }
                    }

                    CerrarDialogoCrearSalidaMaterial();
                    JOptionPane.showMessageDialog(null, "Operación exitosa.");
                    mostrar_tabla_general();
                }            
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarMaterial(int id) {
        try {
            CompraMaterialBE obj = objCompraMaterialBL.readId(id, id_empresa_index);
            
            if(obj != null){
                CompraMaterialDetalleBE objDet = new CompraMaterialDetalleBE();
                objDet.setIdCompra(id);
                objDet.setId_empresa(id_empresa_index);
                List<CompraMaterialDetalleBE> listaProDet = objCompraMaterialDetalleBL.read(objDet);
            
                MostrarComboObras(cboObra, true, true, obj.getDesObra());
                MostrarComboIgv(cboIgv, false, false, String.valueOf(obj.getDesIgv()));
                MostrarCombo(cboMoneda, 2, false, false, obj.getDesMoneda());
                MostrarCombo(cboDocumento, 11, false, false, obj.getDesDocumento());
                mostrarDatosCajaTexto(obj);

                tablaSalidaMaterialDetalle(listaProDet);
            }            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDatosCajaTexto(CompraMaterialBE obj) {
        txtCodigo.setText(insertarCeros(obj.getIdCompra()));
        txtFechaCompra.setDate(obj.getFechaCompra());
        
        if(obj.getNumeroCompra()!= null && obj.getNumeroCompra().length() > 0)
            txtNumeroCompra.setText(obj.getNumeroCompra());
        
        if(obj.getDesProveedor()!= null && obj.getDesProveedor().length() > 0)
            txtDesProveedor.setText(obj.getDesProveedor());
        
        if(obj.getRucProveedor()!= null && obj.getRucProveedor().length() > 0)
            txtRucProveedorCompra.setText(obj.getRucProveedor());        
    }

    private void MostrarObjetos(boolean mostrar) {
        btnNuevoObra.setVisible(mostrar);
        btnBuscarProveedorCompra.setVisible(mostrar);
        btnNuevoProveedorCompra.setVisible(mostrar);
        btn_guardar.setVisible(mostrar);
        panel_nuevo_detalle.setVisible(mostrar);
    }

    private String insertarCeros(int idSalidaMaterial) {
        String valor = String.valueOf(idSalidaMaterial);
        
        while(valor.length() <= 10){
            valor = "0" + valor;
        }
        
        return valor;
    }

    private void MostrarComboPersonal(JComboBox cbo, boolean MostrarFilaVacia, boolean Autocompletado, String cadenaDefault) 
    {
        try {
            List<PersonalBE> list = objPersonalBL.read(null);
            if (!list.isEmpty()) {
                DefaultComboBoxModel cboModel = new DefaultComboBoxModel();
                
                if(Autocompletado)
                    AutoCompleteDecorator.decorate(cbo);
                
                if(MostrarFilaVacia && (cadenaDefault == null || cadenaDefault.isEmpty())){
                    cboModel.addElement("");
                }
                
                if(cadenaDefault != null && cadenaDefault.length() > 0)
                    cboModel.addElement(cadenaDefault);
                
                String valor; 
                
                for (PersonalBE obj : list) {
                    valor = obj.getNombre();
                    
                    if(cadenaDefault != null){
                        if(!valor.equals(cadenaDefault))
                            cboModel.addElement(valor);
                    }else{
                        cboModel.addElement(valor);
                    }                    
                }
                
                cbo.setModel(cboModel);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void MostrarComboObras(JComboBox cbo, boolean MostrarFilaVacia, boolean Autocompletado, String cadenaDefault) {
        try {
            ObrasBE objObras = new ObrasBE();
            objObras.setId_empresa(id_empresa_index);
            List<ObrasBE> list = objObrasBL.read(objObras);
            if (!list.isEmpty()) {
                DefaultComboBoxModel cboModel = new DefaultComboBoxModel();
                
                if(Autocompletado)
                    AutoCompleteDecorator.decorate(cbo);
                
                if(MostrarFilaVacia && (cadenaDefault == null || cadenaDefault.isEmpty())){
                    cboModel.addElement("");
                }
                
                if(cadenaDefault != null && cadenaDefault.length() > 0)
                    cboModel.addElement(cadenaDefault);
                
                String valor; 
                
                for (ObrasBE obj : list) {
                    valor = obj.getDescripcion();
                    
                    if(cadenaDefault != null){
                        if(!valor.equals(cadenaDefault))
                            cboModel.addElement(valor);
                    }else{
                        cboModel.addElement(valor);
                    }                    
                }
                
                cbo.setModel(cboModel);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void MostrarComboIgv(JComboBox cbo, boolean MostrarFilaVacia, boolean Autocompletado, String cadenaDefault) {
        try {
            IgvBE objIgv = new IgvBE();
            objIgv.setId_empresa(id_empresa_index);
            List<IgvBE> list = objIgvBL.read(objIgv);
            if (!list.isEmpty()) {
                DefaultComboBoxModel cboModel = new DefaultComboBoxModel();
                
                if(Autocompletado)
                    AutoCompleteDecorator.decorate(cbo);
                
                if(MostrarFilaVacia && (cadenaDefault == null || cadenaDefault.isEmpty())){
                    cboModel.addElement("");
                }
                
                if(cadenaDefault != null && cadenaDefault.length() > 0)
                    cboModel.addElement(cadenaDefault);
                
                String valor; 
                
                for (IgvBE obj : list) {
                    valor = String.valueOf(obj.getIgv());
                    
                    if(cadenaDefault != null){
                        if(!valor.equals(cadenaDefault))
                            cboModel.addElement(valor);
                    }else{
                        cboModel.addElement(valor);
                    }                    
                }
                
                cbo.setModel(cboModel);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void MostrarComboCliente(JComboBox cbo, boolean MostrarFilaVacia, boolean Autocompletado, String cadenaDefault) 
    {
        try {
            List<ClienteBE> list = objClienteBL.read(null);
            if (!list.isEmpty()) {
                DefaultComboBoxModel cboModel = new DefaultComboBoxModel();
                
                if(Autocompletado)
                    AutoCompleteDecorator.decorate(cbo);
                
                if(MostrarFilaVacia && (cadenaDefault == null || cadenaDefault.isEmpty())){
                    cboModel.addElement("");
                }
                
                if(cadenaDefault != null && cadenaDefault.length() > 0)
                    cboModel.addElement(cadenaDefault);
                
                String valor; 
                
                for (ClienteBE obj : list) {
                    valor = obj.getRazon_social();
                    
                    if(cadenaDefault != null){
                        if(!valor.equals(cadenaDefault))
                            cboModel.addElement(valor);
                    }else{
                        cboModel.addElement(valor);
                    }                    
                }
                
                cbo.setModel(cboModel);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void inicializarFiltroProducto() {
        txtNumDocumentoBus.setText("");
        txtDescripcion_bus.setText("");
        txtModelo_bus.setText("");
        txtMarca_bus.setText("");
        txtNombreComun_bus.setText("");
        
        incializarCombo(cbo_referencia_bus);
        incializarCombo(cboMoneda_bus);
        incializarCombo(cboUnidad_bus);
        incializarCombo(cboAlmacen_bus);
        incializarCombo(cboCategoria_bus);
        
        //mostrar_tabla_producto();
    }
    
    private void MostrarCombo(JComboBox cbo, int CodigoTabla, boolean MostrarFilaVacia, boolean Autocompletado, String cadenaDefault) {
        try {
            List<DatoComunBE> list = objDatoComunBL.ReadDetalle(CodigoTabla);
            if (!list.isEmpty()) {
                DefaultComboBoxModel cboModel = new DefaultComboBoxModel();
                
                if(Autocompletado)
                    AutoCompleteDecorator.decorate(cbo);
                
                if(MostrarFilaVacia && (cadenaDefault == null || cadenaDefault.isEmpty())){
                    cboModel.addElement("");
                }
                
                if(cadenaDefault != null && cadenaDefault.length() > 0)
                    cboModel.addElement(cadenaDefault);
                
                String DescripcionCorta; 
                String codigoFila; 
                
                for (DatoComunBE obj : list) {
                    DescripcionCorta = obj.getDescripcionCorta();
                    codigoFila = String.valueOf(obj.getCodigoFila());
                    
                    if(cadenaDefault != null){
                        if(!DescripcionCorta.equals(cadenaDefault))
                            cboModel.addElement(DescripcionCorta);
                    }else{
                        cboModel.addElement(DescripcionCorta);
                    }                    
                }
                
                cbo.setModel(cboModel);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } 
    }

    private void mostrar_tabla_productos() {
        ProductoBE pbe = ObtenerFiltrosBusquedaProducto();
        
        try {
            List<ProductoBE> lProductos = objProductoBL.ProductoListar(pbe);
            DefaultTableModel tabla = (DefaultTableModel) tabla_producto.getModel();
            tabla.setRowCount(0);
            if (lProductos != null) {
                tablaProductos(lProductos);
                lblTotalProductos.setText("Total: "+lProductos.size()+" registros.");
            } else {
                lblTotalProductos.setText("No se encontraron resultados");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void tablaProductos(List<ProductoBE> list) {
        
        try {
            DefaultTableModel tabla = (DefaultTableModel) tabla_producto.getModel();
            tabla.setRowCount(0);
            for (ProductoBE obj : list) {
                Object[] fila = {
                    obj.getId_producto(), 
                    obj.getFila(),
                    obj.getCodigo(), 
                    obj.getDescripcion(), 
                    obj.getDesunidad(), 
                    obj.getCantidad().doubleValue(),
                    obj.getMarca(),
                    obj.getModelo(),
                    };
                tabla.addRow(fila);
            }

            tabla_producto.setRowHeight(35);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }        
    }
    
    private ProductoBE ObtenerFiltrosBusquedaProducto() {
        ProductoBE pbe = new ProductoBE();
        
        if(txtCodigo_bus1.getText().trim().length() > 0)
            pbe.setCodigo(txtCodigo_bus1.getText().trim());
        
        if(txtDescripcion_bus.getText().trim().length() > 0)
            pbe.setDescripcion(txtDescripcion_bus.getText().trim());
        
        if(txtModelo_bus.getText().trim().length() > 0)
            pbe.setModelo(txtModelo_bus.getText().trim());
        
        if(txtMarca_bus.getText().trim().length() > 0)
            pbe.setMarca(txtMarca_bus.getText().trim());
        
        if(txtNombreComun_bus.getText().trim().length() > 0)
            pbe.setDescripcion_coloquial(txtNombreComun_bus.getText().trim());
        
        if(cbo_referencia_bus.getSelectedItem()!= null && cbo_referencia_bus.getSelectedItem().toString().length() > 0)
            pbe.setDesReferencia_precio(cbo_referencia_bus.getSelectedItem().toString().trim());
        
        if(cboMoneda_bus.getSelectedItem()!= null && cboMoneda_bus.getSelectedItem().toString().length() > 0)
            pbe.setDesmoneda(cboMoneda_bus.getSelectedItem().toString().trim());
        
        if(cboUnidad_bus.getSelectedItem()!= null && cboUnidad_bus.getSelectedItem().toString().length() > 0)
            pbe.setDesunidad(cboUnidad_bus.getSelectedItem().toString().trim());
        
        if(cboAlmacen_bus.getSelectedItem()!= null && cboAlmacen_bus.getSelectedItem().toString().length() > 0)
            pbe.setDesAlmacen(cboAlmacen_bus.getSelectedItem().toString().trim());
        
        if(cboCategoria_bus.getSelectedItem()!= null && cboCategoria_bus.getSelectedItem().toString().length() > 0)
            pbe.setDesproductotipo(cboCategoria_bus.getSelectedItem().toString().trim());
        
        
        
        return pbe;
    }

    private void crearDetalleSalida() {
        String id = lblIdProducto.getText().trim();
        String material = txtProducto.getText().trim();
        String unidad = txtUnidad.getText().trim();
        String cantidad = txtCantidad.getText().trim();
        String precioUnitario = txtPrecioUnitario.getText().trim();
        
        CompraMaterialDetalleBE smd = new CompraMaterialDetalleBE();
        
        int ban = 0;
        
        if(ban == 0){
            if(id != null && id.length() > 0 )
            {
                smd.setId_producto(Integer.parseInt(id));
                smd.setDesMaterial(material);
                smd.setDesUnidad(unidad);
            }                
            else
            {
                JOptionPane.showMessageDialog(null, "Seleccione un material");
                ban++;
            } 
        }
        
        if(ban == 0){
            if(cantidad != null && cantidad.length() >0 )
            {
                smd.setCantidad(new BigDecimal(cantidad));
            }            
            else
            {
                JOptionPane.showMessageDialog(null, "Ingrese la cantidad de material solcitado"); 
                ban++;
            }                
        }
        
        if(ban == 0){
            if(precioUnitario != null && precioUnitario.length() >0 )
            {
                smd.setPrecioUnitario(new BigDecimal(precioUnitario));
            }            
            else
            {
                JOptionPane.showMessageDialog(null, "Ingrese el precio unitario del producto comprado."); 
                ban++;
            }                
        }
        
        if(ban == 0){
            smd.setPrecioTotal(smd.getCantidad().multiply(smd.getPrecioUnitario()));
            
            agegarFilaTalaDetalleSalida(smd);
            limpiarCajaTextoCrearDetalleSalidaMaterial();            
        }
    }

    private void limpiarCajaTextoCrearDetalleSalidaMaterial() {
        lblIdProducto.setText("");
        txtProducto.setText("");
        txtUnidad.setText("");
        txtPrecioUnitario.setText("");
        txtCantidad.setText("");        
    }

    private void agegarFilaTalaDetalleSalida(CompraMaterialDetalleBE smd) {
        DefaultTableModel tabla = (DefaultTableModel) tabla_detalle_compre_material.getModel();
        
        tabla.addRow(new Object[]{
            smd.getId_producto(), 
            smd.getDesMaterial(),
            smd.getDesUnidad(),
            smd.getCantidad(),
            smd.getPrecioUnitario(),
            smd.getPrecioTotal(),
        });
        
        tabla_detalle_compre_material.setRowHeight(35);
        calcularTotales(obtenerSubTotal());
    }

    
    private void calcularTotales(BigDecimal sub_total) {
        
        BigDecimal igv = new BigDecimal(cboIgv.getSelectedItem().toString());
        BigDecimal cien = new BigDecimal(100);
        BigDecimal igvCien = igv.divide(cien);
        
        BigDecimal calculo_igv = sub_total.multiply(igvCien);
        BigDecimal total = sub_total.add(calculo_igv);

        BigDecimal sub_total_decimal = sub_total;
        sub_total_decimal = sub_total_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        sub_total = sub_total_decimal;

        BigDecimal calculo_igv_decimal = calculo_igv;
        calculo_igv_decimal = calculo_igv_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        calculo_igv = calculo_igv_decimal;

        BigDecimal total_decimal = total;
        total_decimal = total_decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        total = total_decimal;

        String moneda = cboMoneda.getSelectedItem().toString();
        
        float totalLetra = (float) total_decimal.doubleValue();
        
        String total_letras = clase_numero_letra.getStringOfNumber(totalLetra, moneda);

        txtSubTotal.setText("" + sub_total);
        txtCalculoIgv.setText("" + calculo_igv);
        txtTotal.setText("" + total);
        txtTotalLetras.setText(total_letras);        
    }
    
    private List<CompraMaterialDetalleBE> ObtenerRegistrosSalidaDetalle(int id) {
        
        List<CompraMaterialDetalleBE> list = new ArrayList();
        int filas =  tabla_detalle_compre_material.getRowCount();
        
        if(filas > 0){
            CompraMaterialDetalleBE obj;
            DefaultTableModel tm = (DefaultTableModel) tabla_detalle_compre_material.getModel();
            
            for(int i = 0; i < filas; i++){
                obj = new CompraMaterialDetalleBE();
                obj.setId_producto((Integer) tm.getValueAt(i, 0));
                obj.setCantidad((BigDecimal) tm.getValueAt(i, 3));
                obj.setPrecioUnitario((BigDecimal) tm.getValueAt(i, 4));
                obj.setPrecioTotal((BigDecimal) tm.getValueAt(i, 5));
                
                obj.setIdCompra(id);
                obj.setUsuarioInserta(aliasUsuarioIndex);
                obj.setTipoOperacion(crear0_modificar1_producto);
                obj.setId_empresa(id_empresa_index);
                list.add(obj);          
            }
        }
        
        return list;        
    }

    private void MotrarBotonesControl(
            boolean modificar, 
            boolean eliminar, 
            boolean imprimirSalida
          ){
        btnModificar.setVisible(modificar);
        spModificar.setVisible(modificar);
        btnEliminar.setVisible(eliminar);
        spEliminar.setVisible(eliminar);
        btnImprimirSalida.setVisible(imprimirSalida);
        spImprimirSalida.setVisible(imprimirSalida);
    }

    private void ControlBotones(int estadoAbierto) {
        
        switch (estadoAbierto){
            
            case 100001: //Por confirmar Salida
                btnImprimirSalida.setText("Confirmar Compra");
                MotrarBotonesControl(true, true, true);
                break;
                
            case 100002: //Salida de Material Confirmado
                btnImprimirSalida.setText("Ver Compra");
                MotrarBotonesControl(false, false, true);
                break;
                        
        }
    }

    private void tablaGeneralClick() {
        try{
            int fila = tabla_general.getSelectedRow();
            DefaultTableModel tm = (DefaultTableModel) tabla_general.getModel();
            int idSalida = Integer.parseInt((String) tm.getValueAt(fila, 1));
            
            CompraMaterialBE obj = objCompraMaterialBL.readId(idSalida, id_empresa_index);
            
            ControlBotones(obj.getEstadoAbierto());
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void imprimirSalidaMaterial(int idSalidaMaterial) throws Exception{
        String rutaInforme = "reportes\\CompraMaterial.jasper";
        Map parametros = new HashMap();
        parametros.put("idCompra", idSalidaMaterial);
        parametros.put("idEmpresa", id_empresa_index);
        parametros.put(JRParameter.REPORT_LOCALE, Locale.US);
        Connection cn = AccesoDB.getConnection();
            
        JasperPrint print = JasperFillManager.fillReport(rutaInforme, parametros, cn);
        JasperViewer view = new JasperViewer(print, false);
        view.setTitle("COMPRA DE MATERIAL  N° " + insertarCeros(idSalidaMaterial));
        view.setVisible(true);
    }

    private void mostrarDialogoBuscarProveedor() {
        txt_buscar_proveedor.setText("");
        mostrar_tabla_proveedor();
        dialog_buscar_proveedor.setSize(700, 400);
        dialog_buscar_proveedor.setLocationRelativeTo(ventana);
        dialog_buscar_proveedor.setModal(true);
        dialog_buscar_proveedor.setVisible(true);
    }
    
    private void limpiar_caja_texto_crear_proveedor() {
        txt_buscar_proveedor.setText("");
        txt_razon_social_proveedor_crear.setText("");
        txt_ruc_proveedor_crear.setText("");
        txt_direccion_proveedor_crear.setText("");
        txt_telefono_proveedor_crear.setText("");
        txt_celular_proveedor_crear.setText("");
        txt_correo_proveedor_crear.setText("");
    }

    private void crear_proveedor() {
        String razon_social = txt_razon_social_proveedor_crear.getText().trim();
        String ruc = txt_ruc_proveedor_crear.getText().trim();
        String direccion = txt_direccion_proveedor_crear.getText().trim();
        String telefono = txt_telefono_proveedor_crear.getText().trim();
        String celular = txt_celular_proveedor_crear.getText().trim();
        String correo = txt_correo_proveedor_crear.getText().trim();
        
        ProveedorBE pbe = new ProveedorBE();
        int ban = 0;
        
        if(ban == 0){
            if(razon_social != null && razon_social.length() > 0 )
                pbe.setRazon_social(razon_social);
            else
            {
                JOptionPane.showMessageDialog(null, "El campo Razon Social es obligatorio");
                ban++;
            } 
        }
        
        if(ban == 0){
            if(ruc != null && ruc.length() >0 )
                pbe.setRuc(ruc);
            else
            {
                JOptionPane.showMessageDialog(null, "El campo R.U.C. es obligatorio"); 
                ban++;
            }                
        }
        
        if(ban == 0){
            if(direccion != null && direccion.length() >0 )
                pbe.setDireccion(direccion);
        
            if(telefono != null && telefono.length() >0 )
                pbe.setTelefono(telefono);

            if(celular != null && celular.length() >0 )
                pbe.setCelular(celular);

            if(correo != null && correo.length() >0 )
                pbe.setCorreo(correo);

            pbe.setId_empresa(id_empresa_index);
            pbe.setId_usuario(id_usuario_index);

            try {
                objProveedorBL.crear(pbe);
                
                if(bandBusProveedor == 0){
                    txtProveedorBus.setText(razon_social);
                }else{
                    txtDesProveedor.setText(razon_social);
                    txtRucProveedorCompra.setText(ruc);
                }
                
                CerrarDialogoCrearProveedor();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void CerrarDialogoCrearProveedor() {
        limpiar_caja_texto_crear_proveedor();
        dialog_crear_proveedor.dispose();
    }

    private BigDecimal obtenerSubTotal() {
        BigDecimal subtotal = new BigDecimal(0);
        
        int filas =  tabla_detalle_compre_material.getRowCount();
        
        if(filas > 0){
            DefaultTableModel tm = (DefaultTableModel) tabla_detalle_compre_material.getModel();
            
            for(int i = 0; i < filas; i++){
                subtotal = subtotal.add((BigDecimal) tm.getValueAt(i, 5));                
            }
        }
        
        return subtotal;
    }
}