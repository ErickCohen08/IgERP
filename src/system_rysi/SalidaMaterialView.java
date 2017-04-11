package system_rysi;

import Controller.DatoComunBL;
import Controller.MonedaBL;
import Controller.ObrasBL;
import Controller.PersonalBL;
import Controller.ProductoBL;
import Controller.ProductoDetalleBL;
import Controller.ProveedorBL;
import Controller.SalidaMaterialBL;
import Controller.ValidacionBL;
import entity.DatoComunBE;
import entity.MonedaBE;
import entity.ObrasBE;
import entity.PersonalBE;
import entity.ProductoBE;
import entity.ProductoDetalleBE;
import entity.ProveedorBE;
import entity.SalidaMaterialBE;
import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author ErC
 */
public class SalidaMaterialView extends javax.swing.JPanel {

    //datos de conexion
    String controlador_index;
    String user_index;
    String password_index;
    int id_empresa_index;
    int id_usuario_index;
    String perfil_usuario_index = "";
    
    //Banderas
    DefaultTableModel m;
    
    //New
    ProductoBL objProductoBL = new ProductoBL();
    MonedaBL objMonedaBL = new MonedaBL();
    DatoComunBL objDatoComunBL = new DatoComunBL();
    ProveedorBL objProveedorBL = new ProveedorBL();
    ValidacionBL objValidacionBL = new ValidacionBL();
    ProductoDetalleBL objProductoDetalleBL = new ProductoDetalleBL();
    SalidaMaterialBL objSalidaMaterialBL = new SalidaMaterialBL();
    PersonalBL objPersonalBL = new PersonalBL();
    ObrasBL objObrasBL = new ObrasBL();
    
    
    //variables globales
    boolean limpiarCboFiltros = true;
    int gCodigoTabla = 0;
    int crear0_modificar1_producto = 0;
    int id_producto_global;
    private Component ventana;
    
    public SalidaMaterialView(String controlador, String DSN, String user, String password, int id_empresa, int id_usuario, String perfil_usuario) {
        controlador_index = controlador;
        user_index = user;
        password_index = password;
        id_empresa_index = id_empresa;
        id_usuario_index = id_usuario;
        perfil_usuario_index = perfil_usuario;

        System.out.println("\n\nconectando con Producto");
        initComponents();

        if (perfil_usuario_index.equals("Solo Lectura")) {
            btn_nuevo.setVisible(false);
            btn_modificar.setVisible(false);
            btn_eliminar.setVisible(false);
        }

        System.out.println("Mostrar Tabla Producto");
        mostrar_tabla_general();
        ocultarObjetos(false);
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
        SalidaMaterialBE pbe = ObtenerFiltrosBusqueda();
        
        try {
            List<SalidaMaterialBE> list = objSalidaMaterialBL.read(pbe);
            if (list != null) {
                tablaGeneral(list);
                lblTotal.setText("Total: "+list.size()+" registros.");
            } else {
                lblTotal.setText("No se encontraron resultados");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tablaGeneral(List<SalidaMaterialBE> list) {
        
        try {
            DefaultTableModel tabla = (DefaultTableModel) tabla_general.getModel();
            tabla.setRowCount(0);
            for (SalidaMaterialBE obj : list) {
                Object[] fila = {
                    obj.getFila(),
                    insertarCeros(obj.getIdSalidaMaterial()), 
                    obj.getFechaSalida(), 
                    obj.getDesPersonal(), 
                    obj.getDesObra(), 
                    obj.getDireccion(),
                    obj.getEstadoAbierto(),
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
    private void limpiar_caja_texto_crear_material() {
        //Material
        txt_codigo.setText("");
        txtDireccion.setText("");
        txtMotivo.setText("");
        txtProducto.setText("");
        txtUnidad.setText("");
        txt_cantidad.setText("");
    }

    private void limpiar_caja_texto_datocomun(){
        txtDescripcionCorta.setText("");
        txtValor1.setText("");
        gCodigoTabla = 0;
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

    private void limpiar_caja_texto_crear_detalle_producto() {
        txt_cantidad.setText("");
        txtProducto.setText("");
        txtUnidad.setText("");
    }

    //Creaciones
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
                int id_proveedor = objProveedorBL.crear(pbe);
                txtProducto.setText(razon_social); 
                txtUnidad.setText(String.valueOf(id_proveedor)); 
                
                CerrarDialogoCrearProveedor();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void crear_Producto_Detalle() {
        String razonSocialProveedor = txtProducto.getText().trim();
        String rucProveedor = txtUnidad.getText().trim();
        String precio = txt_cantidad.getText().trim();
        
        ProductoDetalleBE pdbe = new ProductoDetalleBE();
        
        int ban = 0;
        
        if(ban == 0){
            if(rucProveedor != null && rucProveedor.length() > 0 )
                pdbe.setRucProveedor(rucProveedor);
            else
            {
                JOptionPane.showMessageDialog(null, "Seleccione un proveedor");
                ban++;
            } 
        }
        
        if(ban == 0){
            if(precio != null && precio.length() >0 )
                pdbe.setPrecio(new BigDecimal(precio));
            else
            {
                JOptionPane.showMessageDialog(null, "El campo precio es obligatorio."); 
                ban++;
            }                
        }
        
        if(ban == 0){
            pdbe.setId_empresa(id_empresa_index);
            pdbe.setId_usuario(id_usuario_index);
            pdbe.setRazonsocialProveedor(razonSocialProveedor);
            pdbe.setRucProveedor(rucProveedor);
            
            List<ProductoDetalleBE> listaProDet = ObtenerRegistrosProductoDetalle();
            listaProDet.add(pdbe);
            
            limpiar_caja_texto_crear_detalle_producto();
            tablaProveedorDetalle(listaProDet);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mantenimiento_tabla_detalle_factura = new javax.swing.JPopupMenu();
        Eliminar = new javax.swing.JMenuItem();
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
        dialog_crear_datoComun = new javax.swing.JDialog();
        jPanel43 = new javax.swing.JPanel();
        lblTituloDatoComun = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        btn_cancelar_crear_empresatransporte1 = new javax.swing.JButton();
        btn_guardar_empresatransporte1 = new javax.swing.JButton();
        jPanel46 = new javax.swing.JPanel();
        lblDescripcion = new javax.swing.JLabel();
        txtDescripcionCorta = new javax.swing.JTextField();
        lblValor = new javax.swing.JLabel();
        txtValor1 = new javax.swing.JTextField();
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
        dialog_crear_salida = new javax.swing.JDialog();
        norte = new javax.swing.JPanel();
        lbl_titulo = new javax.swing.JLabel();
        Centro = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txt_codigo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        cboObra = new javax.swing.JComboBox();
        btn_nueva_unidad = new javax.swing.JButton();
        txt_fecha_salida = new org.jdesktop.swingx.JXDatePicker();
        jLabel13 = new javax.swing.JLabel();
        cboResponsable = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMotivo = new javax.swing.JTextArea();
        DatosProveedor = new javax.swing.JPanel();
        panel_nuevo_detalle = new javax.swing.JPanel();
        btn_guardar_detalle = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        txt_cantidad = new javax.swing.JTextField();
        btn_cancelar_guardar_detalle = new javax.swing.JButton();
        jLabel63 = new javax.swing.JLabel();
        btn_buscar_proveedor = new javax.swing.JButton();
        txtProducto = new javax.swing.JTextField();
        txtUnidad = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabla_detalle = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabla_detalle1 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tabla_detalle2 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        sur = new javax.swing.JPanel();
        btn_cancelar = new javax.swing.JButton();
        btn_guardar = new javax.swing.JButton();
        dialog_filtrar_salida = new javax.swing.JDialog();
        jPanel18 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtCodigo_bus = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtDireccion_bus = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        cboResponsable_bus = new javax.swing.JComboBox();
        cboObra_bus = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();
        txt_fechasalida_bus = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtMotivo_bus = new javax.swing.JTextArea();
        dialog_filtrar_producto = new javax.swing.JDialog();
        jPanel23 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
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
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btn_nuevo = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btn_modificar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btn_eliminar = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton5 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
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
        mantenimiento_tabla_detalle_factura.add(Eliminar);

        dialog_crear_proveedor.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialog_crear_proveedor.setTitle("Provedor");

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

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(0, 51, 153));
        jLabel43.setText("Teléfono:");

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

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(0, 51, 153));
        jLabel44.setText("Celular:");

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

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(0, 51, 153));
        jLabel45.setText("Correo:");

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

        dialog_crear_datoComun.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel43.setBackground(new java.awt.Color(0, 110, 204));
        jPanel43.setPreferredSize(new java.awt.Dimension(400, 40));

        lblTituloDatoComun.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblTituloDatoComun.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloDatoComun.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloDatoComun.setText("Titulo");

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTituloDatoComun, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTituloDatoComun, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_crear_datoComun.getContentPane().add(jPanel43, java.awt.BorderLayout.PAGE_START);

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
        btn_cancelar_crear_empresatransporte1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btn_cancelar_crear_empresatransporte1KeyReleased(evt);
            }
        });

        btn_guardar_empresatransporte1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar_32_32.png"))); // NOI18N
        btn_guardar_empresatransporte1.setText("Guardar");
        btn_guardar_empresatransporte1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardar_empresatransporte1ActionPerformed(evt);
            }
        });
        btn_guardar_empresatransporte1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btn_guardar_empresatransporte1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel45Layout.createSequentialGroup()
                .addGap(0, 227, Short.MAX_VALUE)
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

        lblDescripcion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDescripcion.setForeground(new java.awt.Color(0, 51, 153));
        lblDescripcion.setText("Descripcion:");

        txtDescripcionCorta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDescripcionCorta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDescripcionCortaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionCortaKeyTyped(evt);
            }
        });

        lblValor.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblValor.setForeground(new java.awt.Color(0, 51, 153));
        lblValor.setText("Valor:");

        txtValor1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtValor1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtValor1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtValor1KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                    .addComponent(lblValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtValor1, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                    .addComponent(txtDescripcionCorta))
                .addContainerGap())
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDescripcion)
                    .addComponent(txtDescripcionCorta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblValor)
                    .addComponent(txtValor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        jPanel44.add(jPanel46, java.awt.BorderLayout.CENTER);

        dialog_crear_datoComun.getContentPane().add(jPanel44, java.awt.BorderLayout.CENTER);

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

        dialog_crear_salida.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialog_crear_salida.setResizable(false);

        norte.setBackground(new java.awt.Color(0, 110, 204));
        norte.setPreferredSize(new java.awt.Dimension(458, 40));

        lbl_titulo.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lbl_titulo.setForeground(new java.awt.Color(255, 255, 255));
        lbl_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_titulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ver_detalle_32_32.png"))); // NOI18N
        lbl_titulo.setText("Salida de Material");

        javax.swing.GroupLayout norteLayout = new javax.swing.GroupLayout(norte);
        norte.setLayout(norteLayout);
        norteLayout.setHorizontalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 845, Short.MAX_VALUE)
        );
        norteLayout.setVerticalGroup(
            norteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_crear_salida.getContentPane().add(norte, java.awt.BorderLayout.NORTH);

        Centro.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTabbedPane1KeyReleased(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 153));
        jLabel12.setText("Código:");

        txt_codigo.setEditable(false);
        txt_codigo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_codigo.setToolTipText("Ingrese un Codigo para este Material");
        txt_codigo.setOpaque(false);
        txt_codigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_codigoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_codigoKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 51, 153));
        jLabel11.setText("Responsable:*");

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 51, 153));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText("Motivo/Comentario:");

        cboObra.setEditable(true);
        cboObra.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboObra.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboObraItemStateChanged(evt);
            }
        });

        btn_nueva_unidad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_10_10.png"))); // NOI18N
        btn_nueva_unidad.setToolTipText("Crear Nueva Unidad de Medida");
        btn_nueva_unidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nueva_unidadActionPerformed(evt);
            }
        });

        txt_fecha_salida.setEditable(false);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 153));
        jLabel13.setText("Fecha de Salida:*");

        cboResponsable.setEditable(true);
        cboResponsable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboResponsable.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboResponsableItemStateChanged(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 51, 153));
        jLabel17.setText("Obra:");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 51, 153));
        jLabel20.setText("Dirección/Destino:");

        txtDireccion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDireccion.setToolTipText("Ingrese un Codigo para este Material");
        txtDireccion.setOpaque(false);
        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDireccionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDireccionKeyTyped(evt);
            }
        });

        txtMotivo.setColumns(20);
        txtMotivo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMotivo.setLineWrap(true);
        txtMotivo.setRows(5);
        txtMotivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMotivoKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(txtMotivo);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboResponsable, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDireccion)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(cboObra, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_nueva_unidad, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_codigo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_fecha_salida, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_fecha_salida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboResponsable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btn_nueva_unidad, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Datos Generales", jPanel4);

        DatosProveedor.setBackground(new java.awt.Color(255, 255, 255));
        DatosProveedor.setLayout(new java.awt.BorderLayout());

        panel_nuevo_detalle.setBackground(new java.awt.Color(255, 255, 255));
        panel_nuevo_detalle.setPreferredSize(new java.awt.Dimension(840, 60));

        btn_guardar_detalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/listo_10_10.png"))); // NOI18N
        btn_guardar_detalle.setToolTipText("Agregar");
        btn_guardar_detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardar_detalleActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 51, 153));
        jLabel19.setText("Cantidad:");

        txt_cantidad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_cantidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_cantidad.setOpaque(false);
        txt_cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_cantidadKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cantidadKeyTyped(evt);
            }
        });

        btn_cancelar_guardar_detalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar_10_10.png"))); // NOI18N
        btn_cancelar_guardar_detalle.setToolTipText("Cancelar");
        btn_cancelar_guardar_detalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelar_guardar_detalleActionPerformed(evt);
            }
        });

        jLabel63.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(0, 51, 153));
        jLabel63.setText("Producto:");

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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProductoKeyReleased(evt);
            }
        });

        txtUnidad.setEditable(false);
        txtUnidad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtUnidad.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtUnidad.setOpaque(false);
        txtUnidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUnidadKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panel_nuevo_detalleLayout = new javax.swing.GroupLayout(panel_nuevo_detalle);
        panel_nuevo_detalle.setLayout(panel_nuevo_detalleLayout);
        panel_nuevo_detalleLayout.setHorizontalGroup(
            panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(22, 22, 22))
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addComponent(txtProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(btn_buscar_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                        .addComponent(txt_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_guardar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cancelar_guardar_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel19))
                .addContainerGap())
        );
        panel_nuevo_detalleLayout.setVerticalGroup(
            panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_nuevo_detalleLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_guardar_detalle)
                    .addComponent(txt_cantidad)
                    .addComponent(btn_cancelar_guardar_detalle)
                    .addGroup(panel_nuevo_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtProducto)
                        .addComponent(txtUnidad))
                    .addComponent(btn_buscar_proveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        DatosProveedor.add(panel_nuevo_detalle, java.awt.BorderLayout.NORTH);

        jPanel17.setBackground(new java.awt.Color(255, 153, 153));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setLayout(new java.awt.BorderLayout());

        tabla_detalle.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabla_detalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdDetalle", "IdProducto", "Material", "Unidad", "Cantidad Salida", "Comentario"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla_detalle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_detalleMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabla_detalle);
        if (tabla_detalle.getColumnModel().getColumnCount() > 0) {
            tabla_detalle.getColumnModel().getColumn(0).setResizable(false);
            tabla_detalle.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabla_detalle.getColumnModel().getColumn(1).setResizable(false);
            tabla_detalle.getColumnModel().getColumn(1).setPreferredWidth(0);
            tabla_detalle.getColumnModel().getColumn(2).setPreferredWidth(600);
            tabla_detalle.getColumnModel().getColumn(3).setPreferredWidth(200);
            tabla_detalle.getColumnModel().getColumn(4).setPreferredWidth(200);
            tabla_detalle.getColumnModel().getColumn(5).setPreferredWidth(300);
        }

        jPanel19.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel17.add(jPanel19, java.awt.BorderLayout.CENTER);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(615, 5));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 840, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel17.add(jPanel6, java.awt.BorderLayout.PAGE_END);

        DatosProveedor.add(jPanel17, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Datos de salida de Material", DatosProveedor);

        tabla_detalle1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabla_detalle1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdDetalle", "IdProducto", "Material"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla_detalle1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_detalle1MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tabla_detalle1);
        if (tabla_detalle1.getColumnModel().getColumnCount() > 0) {
            tabla_detalle1.getColumnModel().getColumn(0).setResizable(false);
            tabla_detalle1.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabla_detalle1.getColumnModel().getColumn(1).setResizable(false);
            tabla_detalle1.getColumnModel().getColumn(1).setPreferredWidth(0);
            tabla_detalle1.getColumnModel().getColumn(2).setPreferredWidth(600);
        }

        tabla_detalle2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabla_detalle2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdDetalle", "IdProducto", "Material", "Cantidad Salida", "Cantidad Retorno"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla_detalle2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_detalle2MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tabla_detalle2);
        if (tabla_detalle2.getColumnModel().getColumnCount() > 0) {
            tabla_detalle2.getColumnModel().getColumn(0).setResizable(false);
            tabla_detalle2.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabla_detalle2.getColumnModel().getColumn(1).setResizable(false);
            tabla_detalle2.getColumnModel().getColumn(1).setPreferredWidth(0);
            tabla_detalle2.getColumnModel().getColumn(2).setPreferredWidth(600);
            tabla_detalle2.getColumnModel().getColumn(3).setPreferredWidth(200);
            tabla_detalle2.getColumnModel().getColumn(4).setPreferredWidth(200);
        }

        jButton6.setText(">>");

        jButton9.setText(">");

        jButton10.setText("<");

        jButton11.setText("<<");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Datos de Retorno de Material", jPanel8);

        javax.swing.GroupLayout CentroLayout = new javax.swing.GroupLayout(Centro);
        Centro.setLayout(CentroLayout);
        CentroLayout.setHorizontalGroup(
            CentroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        CentroLayout.setVerticalGroup(
            CentroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
        );

        dialog_crear_salida.getContentPane().add(Centro, java.awt.BorderLayout.CENTER);

        sur.setPreferredSize(new java.awt.Dimension(458, 40));

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

        javax.swing.GroupLayout surLayout = new javax.swing.GroupLayout(sur);
        sur.setLayout(surLayout);
        surLayout.setHorizontalGroup(
            surLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, surLayout.createSequentialGroup()
                .addContainerGap(611, Short.MAX_VALUE)
                .addComponent(btn_guardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cancelar)
                .addContainerGap())
        );
        surLayout.setVerticalGroup(
            surLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(surLayout.createSequentialGroup()
                .addGroup(surLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cancelar)
                    .addComponent(btn_guardar))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        dialog_crear_salida.getContentPane().add(sur, java.awt.BorderLayout.SOUTH);

        dialog_filtrar_salida.setResizable(false);

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
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        dialog_filtrar_salida.getContentPane().add(jPanel18, java.awt.BorderLayout.PAGE_START);

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
        jButton8.setText("Buscar");
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

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(0, 437, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel20.add(jPanel21, java.awt.BorderLayout.PAGE_END);

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 51, 153));
        jLabel22.setText("Código:");

        txtCodigo_bus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCodigo_bus.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtCodigo_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigo_busKeyReleased(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 51, 153));
        jLabel27.setText("Obra:");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 51, 153));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("Dirección:");

        txtDireccion_bus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDireccion_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDireccion_busKeyReleased(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 51, 153));
        jLabel29.setText("Motivo:");

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 51, 153));
        jLabel31.setText("Responsable:");

        cboResponsable_bus.setEditable(true);
        cboResponsable_bus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboResponsable_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboResponsable_busKeyReleased(evt);
            }
        });

        cboObra_bus.setEditable(true);
        cboObra_bus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cboObra_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboObra_busKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboObra_busKeyReleased(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 51, 153));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("Fecha Salida:");

        txt_fechasalida_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_fechasalida_busKeyReleased(evt);
            }
        });

        txtMotivo_bus.setColumns(20);
        txtMotivo_bus.setRows(5);
        txtMotivo_bus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMotivo_busKeyReleased(evt);
            }
        });
        jScrollPane7.setViewportView(txtMotivo_bus);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboObra_bus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDireccion_bus)
                    .addComponent(jScrollPane7)
                    .addComponent(cboResponsable_bus, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(txtCodigo_bus, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_fechasalida_bus, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtCodigo_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(txt_fechasalida_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(cboResponsable_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(cboObra_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txtDireccion_bus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel20.add(jPanel22, java.awt.BorderLayout.CENTER);

        dialog_filtrar_salida.getContentPane().add(jPanel20, java.awt.BorderLayout.CENTER);

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
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
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

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/listo_24_24.png"))); // NOI18N
        jButton13.setText("Seleccionar");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jButton13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton13KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addGap(0, 548, Short.MAX_VALUE)
                .addComponent(jButton13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                "Id", "Código", "Descripción", "Unidad", "Cantidad", "Marca", "Modelo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, false, true, false, false
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tabla_productoKeyReleased(evt);
            }
        });
        jScrollPane8.setViewportView(tabla_producto);
        if (tabla_producto.getColumnModel().getColumnCount() > 0) {
            tabla_producto.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabla_producto.getColumnModel().getColumn(1).setPreferredWidth(100);
            tabla_producto.getColumnModel().getColumn(2).setPreferredWidth(600);
            tabla_producto.getColumnModel().getColumn(3).setPreferredWidth(200);
            tabla_producto.getColumnModel().getColumn(4).setPreferredWidth(200);
            tabla_producto.getColumnModel().getColumn(5).setPreferredWidth(300);
            tabla_producto.getColumnModel().getColumn(6).setPreferredWidth(300);
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
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE))
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
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel28.add(jPanel30, java.awt.BorderLayout.CENTER);

        dialog_filtrar_producto.getContentPane().add(jPanel28, java.awt.BorderLayout.CENTER);

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(0, 110, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salida_material_32_32.png"))); // NOI18N
        jLabel1.setText("Salida de Materiales");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
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
        jToolBar1.add(jSeparator4);

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ver_24_24.png"))); // NOI18N
        jButton5.setText("Ver");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);
        jToolBar1.add(jSeparator3);

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/filtro_24_24.png"))); // NOI18N
        jButton1.setText("Filtrar");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jToolBar1.add(jSeparator5);

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/filter_delete_24_24.png"))); // NOI18N
        jButton3.setText("Quitar Filtro");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        panel_tabla.setBackground(new java.awt.Color(255, 255, 255));
        panel_tabla.setPreferredSize(new java.awt.Dimension(300, 461));

        tabla_general.setAutoCreateRowSorter(true);
        tabla_general.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N°", "Código", "Fecha Salida", "Responsable", "Obra", "Direccion", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
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
            tabla_general.getColumnModel().getColumn(0).setPreferredWidth(100);
            tabla_general.getColumnModel().getColumn(1).setPreferredWidth(200);
            tabla_general.getColumnModel().getColumn(2).setPreferredWidth(200);
            tabla_general.getColumnModel().getColumn(3).setPreferredWidth(400);
            tabla_general.getColumnModel().getColumn(4).setPreferredWidth(400);
            tabla_general.getColumnModel().getColumn(5).setPreferredWidth(400);
            tabla_general.getColumnModel().getColumn(6).setPreferredWidth(200);
        }

        javax.swing.GroupLayout panel_tablaLayout = new javax.swing.GroupLayout(panel_tabla);
        panel_tabla.setLayout(panel_tablaLayout);
        panel_tablaLayout.setHorizontalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
        );
        panel_tablaLayout.setVerticalGroup(
            panel_tablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
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
            .addGap(0, 835, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE))
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

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        crear0_modificar1_producto = 0;
        id_producto_global = 0;
        ocultarObjetos(true);
        mostrarMaterial(crear0_modificar1_producto, 0);         
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_cancelar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_clienteActionPerformed
        CerrarDialogoCrearProveedor();
    }//GEN-LAST:event_btn_cancelar_clienteActionPerformed

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
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_telefono_proveedor_crearKeyTyped

    private void txt_celular_proveedor_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_celular_proveedor_crearKeyTyped
        JTextField caja = txt_celular_proveedor_crear;
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_celular_proveedor_crearKeyTyped

    private void txt_correo_proveedor_crearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_correo_proveedor_crearKeyTyped
        JTextField caja = txt_correo_proveedor_crear;
        int limite = 50;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_correo_proveedor_crearKeyTyped

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
        crear_proveedor();
    }//GEN-LAST:event_btn_crea_clienteActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        CerrarDialogoCrearProducto();
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarActionPerformed
        int fila = tabla_detalle.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro.");
        } else {
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este registro?", "Eliminar", JOptionPane.YES_NO_OPTION);
            
            if (respuesta == JOptionPane.YES_OPTION) {
                DefaultTableModel tm = (DefaultTableModel) tabla_detalle.getModel();
                
                List<ProductoDetalleBE> listaProDet = ObtenerRegistrosProductoDetalle();
                listaProDet.remove(fila);
                tablaProveedorDetalle(listaProDet);                
            }
        }
    }//GEN-LAST:event_EliminarActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        crearmodificarProducto();
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void tabla_generalMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_generalMouseReleased
        /*if (band_index == 0) {
            int fila;
            int id_producto;
            fila = tabla_general.getSelectedRow();
            m = (DefaultTableModel) tabla_general.getModel();
            id_producto = Integer.parseInt((String) m.getValueAt(fila, 0));
            mostrar_datos_producto(id_producto);
        }*/
    }//GEN-LAST:event_tabla_generalMouseReleased

    private void tabla_generalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_generalKeyReleased
        /*if (band_index == 0) {
            int fila;
            int id_producto;
            fila = tabla_general.getSelectedRow();
            m = (DefaultTableModel) tabla_general.getModel();
            id_producto = Integer.parseInt((String) m.getValueAt(fila, 0));
            mostrar_datos_producto(id_producto);
        }*/
    }//GEN-LAST:event_tabla_generalKeyReleased

    private void btn_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modificarActionPerformed
        int fila = tabla_general.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro.");
        } else {
            DefaultTableModel tm = (DefaultTableModel) tabla_general.getModel();
            int id_producto = (Integer) tm.getValueAt(fila, 0);
                
            crear0_modificar1_producto = 1;
            id_producto_global = id_producto;
            ocultarObjetos(true);
            mostrarMaterial(crear0_modificar1_producto, id_producto);            
        }
    }//GEN-LAST:event_btn_modificarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        int fila = tabla_general.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro.");
        } else {
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este registro?", "Eliminar", JOptionPane.YES_NO_OPTION);
            
            if (respuesta == JOptionPane.YES_OPTION) {
                DefaultTableModel tm = (DefaultTableModel) tabla_general.getModel();
                int id_producto = (Integer) tm.getValueAt(fila, 0);
                
                ProductoBE obj = new ProductoBE();
                obj.setId_producto(id_producto);

                try
                {
                    if(objProductoBL.ProductoEliminar(obj) > 0){
                        mostrar_tabla_general();
                        JOptionPane.showMessageDialog(null, "Registro eliminado con éxito.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_cancelar_crear_empresatransporte1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_crear_empresatransporte1ActionPerformed
        cerrarDialogoCrearDatoComun();        
    }//GEN-LAST:event_btn_cancelar_crear_empresatransporte1ActionPerformed

    private void btn_guardar_empresatransporte1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardar_empresatransporte1ActionPerformed
        crearDatoComun();        
    }//GEN-LAST:event_btn_guardar_empresatransporte1ActionPerformed

    private void txtDescripcionCortaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionCortaKeyTyped
        JTextField caja = txt_codigo;
        int limite = 100;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txtDescripcionCortaKeyTyped

    private void txtValor1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValor1KeyTyped
        
    }//GEN-LAST:event_txtValor1KeyTyped

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        dialog_filtrar_salida.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        filtrarProducto();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(limpiarCboFiltros){
            MostrarComboPersonal(cboResponsable_bus, true, true, null);
            MostrarComboObras(cboObra_bus, true, true, null);
            limpiarCboFiltros = false;
        }
        
        dialog_filtrar_salida.setSize(600, 400);
        dialog_filtrar_salida.setLocationRelativeTo(ventana);
        dialog_filtrar_salida.setModal(true);
        dialog_filtrar_salida.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        limpiarCboFiltros = true;
        txtCodigo_bus.setText("");
        txtDireccion_bus.setText("");
        txtMotivo_bus.setText("");
        txt_fechasalida_bus.setDate(null);
        
        incializarCombo(cboObra_bus);
        incializarCombo(cboResponsable_bus);
        
        mostrar_tabla_general(); 
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tabla_detalleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_detalleMouseClicked
        if (evt.getButton() == 3) {
            mantenimiento_tabla_detalle_factura.show(tabla_detalle, evt.getX(), evt.getY());
        }        
    }//GEN-LAST:event_tabla_detalleMouseClicked

    private void btn_buscar_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_proveedorActionPerformed
        inicializarFiltroProducto();
        
        dialog_filtrar_producto.setSize(900, 800);
        dialog_filtrar_producto.setLocationRelativeTo(ventana);
        dialog_filtrar_producto.setModal(true);
        dialog_filtrar_producto.setVisible(true);
    }//GEN-LAST:event_btn_buscar_proveedorActionPerformed

    private void btn_cancelar_guardar_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelar_guardar_detalleActionPerformed
        limpiar_caja_texto_crear_detalle_producto();
    }//GEN-LAST:event_btn_cancelar_guardar_detalleActionPerformed

    private void txt_cantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cantidadKeyTyped
        JTextField caja = txt_cantidad;
        ingresar_solo_numeros(caja, evt);
        int limite = 18;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_cantidadKeyTyped

    private void btn_guardar_detalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardar_detalleActionPerformed
        crear_Producto_Detalle();
    }//GEN-LAST:event_btn_guardar_detalleActionPerformed

    private void btn_nueva_unidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nueva_unidadActionPerformed
        limpiar_caja_texto_datocomun();
        mostrarVentanaCrearDC(1,"Crear Unidad de Medida", "Nombre", true, "Código", true);
    }//GEN-LAST:event_btn_nueva_unidadActionPerformed

    private void cboObraItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboObraItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboObraItemStateChanged

    private void txt_codigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_codigoKeyTyped
        JTextField caja = txt_codigo;
        int limite = 20;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txt_codigoKeyTyped

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        mostrar_tabla_proveedor();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txt_razon_social_proveedor_crearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_razon_social_proveedor_crearKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor(); 
        }
    }//GEN-LAST:event_txt_razon_social_proveedor_crearKeyReleased

    private void txt_ruc_proveedor_crearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ruc_proveedor_crearKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor(); 
        }
    }//GEN-LAST:event_txt_ruc_proveedor_crearKeyReleased

    private void txt_direccion_proveedor_crearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_direccion_proveedor_crearKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor(); 
        }
    }//GEN-LAST:event_txt_direccion_proveedor_crearKeyReleased

    private void txt_telefono_proveedor_crearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefono_proveedor_crearKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor(); 
        }
    }//GEN-LAST:event_txt_telefono_proveedor_crearKeyReleased

    private void txt_celular_proveedor_crearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_celular_proveedor_crearKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor(); 
        }
    }//GEN-LAST:event_txt_celular_proveedor_crearKeyReleased

    private void txt_correo_proveedor_crearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_correo_proveedor_crearKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor(); 
        }
    }//GEN-LAST:event_txt_correo_proveedor_crearKeyReleased

    private void txtDescripcionCortaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionCortaKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearDatoComun();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cerrarDialogoCrearDatoComun(); 
        }        
    }//GEN-LAST:event_txtDescripcionCortaKeyReleased

    private void txtValor1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValor1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearDatoComun();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cerrarDialogoCrearDatoComun(); 
        }
    }//GEN-LAST:event_txtValor1KeyReleased

    private void txt_cantidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cantidadKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_Producto_Detalle();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProducto();
        }
    }//GEN-LAST:event_txt_cantidadKeyReleased

    private void txtProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductoKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_Producto_Detalle();
        }
    }//GEN-LAST:event_txtProductoKeyReleased

    private void tablaProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProveedorMouseClicked
       if (evt.getClickCount() == 2) {
           SeleccionProveedor();
       }
    }//GEN-LAST:event_tablaProveedorMouseClicked

    private void btn_guardar_empresatransporte1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_guardar_empresatransporte1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearDatoComun();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cerrarDialogoCrearDatoComun(); 
        }
    }//GEN-LAST:event_btn_guardar_empresatransporte1KeyReleased

    private void btn_cancelar_crear_empresatransporte1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_cancelar_crear_empresatransporte1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cerrarDialogoCrearDatoComun();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cerrarDialogoCrearDatoComun(); 
        }
    }//GEN-LAST:event_btn_cancelar_crear_empresatransporte1KeyReleased

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
            crear_proveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor(); 
        }
    }//GEN-LAST:event_btn_crea_clienteKeyReleased

    private void btn_cancelar_clienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_cancelar_clienteKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CerrarDialogoCrearProveedor();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProveedor(); 
        }
    }//GEN-LAST:event_btn_cancelar_clienteKeyReleased

    private void txtCodigo_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigo_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_salida.dispose(); 
        }
    }//GEN-LAST:event_txtCodigo_busKeyReleased

    private void cboObra_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboObra_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_salida.dispose(); 
        }
    }//GEN-LAST:event_cboObra_busKeyReleased

    private void txtDireccion_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccion_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_salida.dispose(); 
        }
    }//GEN-LAST:event_txtDireccion_busKeyReleased

    private void cboResponsable_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboResponsable_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_salida.dispose(); 
        }
    }//GEN-LAST:event_cboResponsable_busKeyReleased

    private void jButton8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton8KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_salida.dispose(); 
        }
    }//GEN-LAST:event_jButton8KeyReleased

    private void jButton7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton7KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            dialog_filtrar_salida.dispose(); 
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_salida.dispose(); 
        }
    }//GEN-LAST:event_jButton7KeyReleased

    private void cboObra_busKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboObra_busKeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_salida.dispose(); 
        }
    }//GEN-LAST:event_cboObra_busKeyPressed

    private void btn_cancelarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_cancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CerrarDialogoCrearProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProducto();
        }
    }//GEN-LAST:event_btn_cancelarKeyReleased

    private void btn_guardarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_guardarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearmodificarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProducto();
        }
    }//GEN-LAST:event_btn_guardarKeyReleased

    private void txt_codigoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_codigoKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearmodificarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProducto();
        }
    }//GEN-LAST:event_txt_codigoKeyReleased

    private void jTabbedPane1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabbedPane1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crearmodificarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            CerrarDialogoCrearProducto();
        }
    }//GEN-LAST:event_jTabbedPane1KeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int fila = tabla_general.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro.");
        } else {
            DefaultTableModel tm = (DefaultTableModel) tabla_general.getModel();
            int id_producto = (Integer) tm.getValueAt(fila, 0);
                
            crear0_modificar1_producto = 1;
            id_producto_global = id_producto;
            ocultarObjetos(false);
            mostrarMaterial(crear0_modificar1_producto, id_producto);            
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void cboResponsableItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboResponsableItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboResponsableItemStateChanged

    private void txtDireccionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionKeyReleased

    private void txtDireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyTyped
        JTextField caja = txtDireccion;
        int limite = 500;
        tamaño_de_caja(caja, evt, limite);
    }//GEN-LAST:event_txtDireccionKeyTyped

    private void txtUnidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnidadKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            crear_Producto_Detalle();
        }
    }//GEN-LAST:event_txtUnidadKeyReleased

    private void tabla_detalle1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_detalle1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabla_detalle1MouseClicked

    private void tabla_detalle2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_detalle2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabla_detalle2MouseClicked

    private void txtMotivoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMotivoKeyTyped
        JTextArea caja = txtMotivo;
        int limite = 1000;
        tamaño_de_caja_jtextarea(caja, evt, limite);
    }//GEN-LAST:event_txtMotivoKeyTyped

    private void txt_fechasalida_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_fechasalida_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_salida.dispose(); 
        }
    }//GEN-LAST:event_txt_fechasalida_busKeyReleased

    private void txtMotivo_busKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMotivo_busKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_salida.dispose(); 
        }
    }//GEN-LAST:event_txtMotivo_busKeyReleased

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        dialog_filtrar_producto.dispose();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton12KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            dialog_filtrar_producto.dispose();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_jButton12KeyReleased

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        filtrarProducto();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton13KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton13KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filtrarProducto();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dialog_filtrar_producto.dispose();
        }
    }//GEN-LAST:event_jButton13KeyReleased

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
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton14KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton14KeyReleased
        // TODO add your handling code here:
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

    private void tabla_productoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_productoKeyReleased
        /*if (band_index == 0) {
            int fila;
            int id_producto;
            fila = tabla_general.getSelectedRow();
            m = (DefaultTableModel) tabla_general.getModel();
            id_producto = Integer.parseInt((String) m.getValueAt(fila, 0));
            mostrar_datos_producto(id_producto);
        }*/
    }//GEN-LAST:event_tabla_productoKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Centro;
    private javax.swing.JPanel DatosProveedor;
    private javax.swing.JMenuItem Eliminar;
    private javax.swing.JButton btn_buscar_proveedor;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_cancelar_cliente;
    private javax.swing.JButton btn_cancelar_crear_empresatransporte1;
    private javax.swing.JButton btn_cancelar_guardar_detalle;
    private javax.swing.JButton btn_cliente_cancelar_busqueda;
    private javax.swing.JButton btn_cliente_seleccionar;
    private javax.swing.JButton btn_crea_cliente;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_guardar_detalle;
    private javax.swing.JButton btn_guardar_empresatransporte1;
    private javax.swing.JButton btn_modificar;
    private javax.swing.JButton btn_nueva_unidad;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JComboBox cboAlmacen_bus;
    private javax.swing.JComboBox cboCategoria_bus;
    private javax.swing.JComboBox cboMoneda_bus;
    private javax.swing.JComboBox cboObra;
    private javax.swing.JComboBox cboObra_bus;
    private javax.swing.JComboBox cboResponsable;
    private javax.swing.JComboBox cboResponsable_bus;
    private javax.swing.JComboBox cboUnidad_bus;
    private javax.swing.JComboBox cbo_referencia_bus;
    private javax.swing.JDialog dialog_buscar_proveedor;
    private javax.swing.JDialog dialog_crear_datoComun;
    private javax.swing.JDialog dialog_crear_proveedor;
    private javax.swing.JDialog dialog_crear_salida;
    private javax.swing.JDialog dialog_filtrar_producto;
    private javax.swing.JDialog dialog_filtrar_salida;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
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
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
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
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblTituloDatoComun;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblValor;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JPopupMenu mantenimiento_tabla_detalle_factura;
    private javax.swing.JPanel norte;
    private javax.swing.JPanel panel_nuevo_detalle;
    private javax.swing.JPanel panel_tabla;
    private javax.swing.JPanel sur;
    private javax.swing.JTable tablaProveedor;
    private javax.swing.JTable tabla_detalle;
    private javax.swing.JTable tabla_detalle1;
    private javax.swing.JTable tabla_detalle2;
    private javax.swing.JTable tabla_general;
    private javax.swing.JTable tabla_producto;
    private javax.swing.JTextField txtCodigo_bus;
    private javax.swing.JTextField txtCodigo_bus1;
    private javax.swing.JTextField txtDescripcionCorta;
    private javax.swing.JTextField txtDescripcion_bus;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDireccion_bus;
    private javax.swing.JTextField txtMarca_bus;
    private javax.swing.JTextField txtModelo_bus;
    private javax.swing.JTextArea txtMotivo;
    private javax.swing.JTextArea txtMotivo_bus;
    private javax.swing.JTextField txtNombreComun_bus;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JTextField txtUnidad;
    private javax.swing.JTextField txtValor1;
    private javax.swing.JTextField txt_buscar_proveedor;
    private javax.swing.JTextField txt_cantidad;
    private javax.swing.JTextField txt_celular_proveedor_crear;
    private javax.swing.JTextField txt_codigo;
    private javax.swing.JTextField txt_correo_proveedor_crear;
    private javax.swing.JTextField txt_direccion_proveedor_crear;
    private org.jdesktop.swingx.JXDatePicker txt_fecha_salida;
    private org.jdesktop.swingx.JXDatePicker txt_fechasalida_bus;
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

    private SalidaMaterialBE ObtenerFiltrosBusqueda() {
        SalidaMaterialBE pbe = new SalidaMaterialBE();
        
        if(txtCodigo_bus.getText().trim().length() > 0)
            pbe.setIdSalidaMaterial(Integer.parseInt(txtCodigo_bus.getText().trim()));
        
        if(txt_fechasalida_bus.getDate() != null){
            java.util.Date utilDate = txt_fechasalida_bus.getDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            pbe.setFechaSalida(sqlDate);
        }
        
        if(cboResponsable_bus.getSelectedItem()!= null && cboResponsable_bus.getSelectedItem().toString().length() > 0)
            pbe.setDesPersonal(cboResponsable_bus.getSelectedItem().toString().trim());
        
        if(cboObra_bus.getSelectedItem()!= null && cboObra_bus.getSelectedItem().toString().length() > 0)
            pbe.setDesObra(cboObra_bus.getSelectedItem().toString().trim());
        
        if(txtDireccion_bus.getText().trim().length() > 0)
            pbe.setDireccion(txtDireccion_bus.getText().trim());
        
        if(txtMotivo_bus.getText().trim().length() > 0)
            pbe.setMotivo(txtMotivo_bus.getText().trim());
        
        return pbe;
    }

    private void incializarCombo(JComboBox cbo) {
        DefaultComboBoxModel cboModel = new DefaultComboBoxModel();
        cbo.setModel(cboModel);
    }

    private void mostrarMaterial(int accion, int id_producto) {
        jTabbedPane1.setSelectedIndex(0);
        limpiar_caja_texto_crear_material();
        
        if(accion == 0)
            cargarCombos();
        else
            modificarMaterial(id_producto);
        
        dialog_crear_salida.setSize(750, 500);
        dialog_crear_salida.setLocationRelativeTo(ventana);
        dialog_crear_salida.setModal(true);
        dialog_crear_salida.setVisible(true);
        
    }

    private void cargarCombos() {
        //MostrarCombo(cboReferencia, 4, true, true, null);
        //MostrarCombo(cboObra, 2, true, true, null);
        //MostrarCombo(cboUnidad, 1, true, true, null);
        //MostrarCombo(cboAlmacen, 3, true, true, null);
        //MostrarCombo(cboCategoria, 6, true, true, null);        
    }

    private void mostrarVentanaCrearDC(int codTabla, String titulo, String labelDescripcion, boolean mostrarLblDescripcion, String labelValor, boolean mostrarLblValor) {
        gCodigoTabla = codTabla;
        
        lblTituloDatoComun.setText(titulo);
        lblDescripcion.setText(labelDescripcion);
        lblValor.setText(labelValor);
        
        if(mostrarLblDescripcion){
            lblDescripcion.setVisible(true);
            txtDescripcionCorta.setVisible(true);
        }else{
            lblDescripcion.setVisible(false);
            txtDescripcionCorta.setVisible(false);
        }
        
        if(mostrarLblValor){
            lblValor.setVisible(true);
            txtValor1.setVisible(true);
        }else{
            lblValor.setVisible(false);
            txtValor1.setVisible(false);
        }
        
        dialog_crear_datoComun.setSize(430, 300);
        dialog_crear_datoComun.setLocationRelativeTo(ventana);
        dialog_crear_datoComun.setModal(true);
        dialog_crear_datoComun.setVisible(true);
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

    private void tablaProveedorDetalle(List<ProductoDetalleBE> listaProDet) {
        DefaultTableModel tabla = (DefaultTableModel) tabla_detalle.getModel();
        tabla.setRowCount(0);
        for (ProductoDetalleBE obj : listaProDet) {
            Object[] fila = {
                obj.getRucProveedor(),
                obj.getRazonsocialProveedor(),
                obj.getPrecio()
                };
            tabla.addRow(fila);
        }
        
        tabla_detalle.setRowHeight(35);
    }

    private void crearDatoComun() {
        
        if(gCodigoTabla != 0){
            String descripcion = txtDescripcionCorta.getText().trim();
            String valor = txtValor1.getText().trim();
            int band = 0;
            
            DatoComunBE objdc  = new DatoComunBE();
            
            if(band == 0)
            {
                if(txtDescripcionCorta.isVisible()){
                    if(descripcion != null && descripcion.length()>0)
                        objdc.setDescripcionCorta(descripcion);
                    else{
                        JOptionPane.showMessageDialog(null, "Ingrese el valor de todos los campos.");    
                        band++;
                    }
                }   
            }
            
            if(band == 0)
            {
                if(txtValor1.isVisible()){
                    if(valor != null && valor.length()>0)
                        objdc.setValorTexto1(valor);
                    else{
                        JOptionPane.showMessageDialog(null, "Ingrese el valor de todos los campos.");    
                        band++;
                    }
                }   
            }
            
            if(band == 0){
                objdc.setId_empresa(id_empresa_index);
                objdc.setUsuarioDes(user_index);
                objdc.setCodigoTabla(gCodigoTabla);
                
                try {
                 
                    int resp = objDatoComunBL.crear(objdc);
                    
                    switch (gCodigoTabla) {
                        //Unidad
                        case 1:
                            //MostrarCombo(cboUnidad, 1, false, true, descripcion);
                            break;
                        
                        //Referencia
                        case 4:
                            //MostrarCombo(cboReferencia, 4, false, true, descripcion);
                            break;
                            
                        //Categoria
                        case 6:
                            //MostrarCombo(cboCategoria, 6, false, true, descripcion);
                            break;
                    }
                
                    cerrarDialogoCrearDatoComun();  
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            
        }else{
            JOptionPane.showMessageDialog(null, "No se enconró el codigo de tabla");
        }
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
            
            txtUnidad.setText(ruc);
            txtProducto.setText(razon_social);
            
            cerrarDialogoBuscarProveedor();
        }
    }

    private void cerrarDialogoBuscarProveedor() {
        txt_buscar_proveedor.setText("");
        dialog_buscar_proveedor.dispose();
    }

    private void cerrarDialogoCrearDatoComun() {
        limpiar_caja_texto_datocomun();
        dialog_crear_datoComun.dispose();
    }

    private void CerrarDialogoCrearProveedor() {
        limpiar_caja_texto_crear_proveedor();
        dialog_crear_proveedor.dispose();
    }

    private void filtrarProducto() {
        mostrar_tabla_general(); 
        dialog_filtrar_salida.dispose();
    }

    private void CerrarDialogoCrearProducto() {
        gCodigoTabla = 0;
        crear0_modificar1_producto = 0;
        id_producto_global = 0;
        
        DefaultTableModel tablaDetalle = (DefaultTableModel) tabla_detalle.getModel();
        tablaDetalle.setRowCount(0);
        
        ocultarObjetos(true);
        dialog_crear_salida.dispose();
    }

    private ProductoBE capturarValorProducto() {
        
        ProductoBE pbe = null;
        
        /*String codigo = txt_codigo.getText().trim();
        String descripcion = txt_descripcion.getText().trim();
        String modelo = txt_modelo.getText().trim();
        String marca = txt_marca.getText().trim();
        String descripcion_coloquial = txt_descripcion_coloquial.getText().trim();
        String peso = txt_peso.getText().trim();
        String precio_promedio = txt_precio_unitario.getText().trim();
        String cantidad = txtCantidad.getText().trim();
        
        String Desmoneda = cboObra.getSelectedItem().toString().trim();
        String Desunidad = cboUnidad.getSelectedItem().toString().trim();
        String Desproductotipo = cboCategoria.getSelectedItem().toString().trim();
        String DesAlmacen = cboAlmacen.getSelectedItem().toString().trim();
        String DesReferencia_precio = cboReferencia.getSelectedItem().toString().trim();
        
        int band = 0;
        
        if(descripcion.length() == 0){
            JOptionPane.showMessageDialog(null, "El campo Nombre es obligarotio");
            band++;
        }
        
        if(band == 0){
            pbe = new ProductoBE();
            
            if(descripcion.length() > 0)
                pbe.setDescripcion(descripcion);
            
            if(codigo.length() > 0)
                pbe.setCodigo(codigo);
            
            if(modelo.length() > 0)
                pbe.setModelo(modelo);
            
            if(marca.length() > 0)
                pbe.setMarca(marca);
            
            if(descripcion_coloquial.length() > 0)
                pbe.setDescripcion_coloquial(descripcion_coloquial);
            
            if(peso.length() > 0)
                pbe.setPeso(new BigDecimal(peso));
            
            if(precio_promedio.length() > 0)
                pbe.setPrecio_promedio(new BigDecimal(precio_promedio));
            
            if(cantidad.length() > 0)
                pbe.setCantidad(new BigDecimal(cantidad));
            
            if(Desmoneda.length() > 0)
                pbe.setDesmoneda(Desmoneda);
            
            if(Desunidad.length() > 0)
                pbe.setDesunidad(Desunidad);
            
            if(Desproductotipo.length() > 0)
                pbe.setDesproductotipo(Desproductotipo);
            
            if(DesAlmacen.length() > 0)
                pbe.setDesAlmacen(DesAlmacen);
            
            if(DesReferencia_precio.length() > 0)
                pbe.setDesReferencia_precio(DesReferencia_precio);
            
            pbe.setId_empresa(id_empresa_index);
            pbe.setUsuarioInserta(user_index);
            pbe.setTipoOperacion(crear0_modificar1_producto);
            pbe.setId_producto(id_producto_global);
        }
        */
        return pbe;
    }

    private void crearmodificarProducto() {
        ProductoBE pbe = capturarValorProducto();
        
        if(pbe != null){
            try {
                
                int id_producto = objProductoBL.crear(pbe);
                
                List<ProductoDetalleBE> listaProDet = ObtenerRegistrosProductoDetalle();
                
                if(listaProDet != null && !listaProDet.isEmpty()){
                    int i = 0;
                    
                    for (ProductoDetalleBE obj : listaProDet) {
                        obj.setId_producto(id_producto);
                        
                        if(i == 0)
                            objProductoDetalleBL.Eliminar(obj);
                        
                        objProductoDetalleBL.crear(obj);
                        i++;
                    }
                }
                
                CerrarDialogoCrearProducto();
                JOptionPane.showMessageDialog(null, "Operación exitosa.");
                mostrar_tabla_general();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }    
        }
    }

    private void modificarMaterial(int id_producto) {
        try {
            ProductoBE obj = objProductoBL.GetData(id_producto);
            
            ProductoDetalleBE objDet = new ProductoDetalleBE();
            objDet.setId_producto(id_producto);
            
            List<ProductoDetalleBE> listaProDet;
            listaProDet = objProductoDetalleBL.Listar(objDet);
            
            if(obj != null){
                mostrarDatosCajaTexto(obj);
                tablaProveedorDetalle(listaProDet);
            }            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDatosCajaTexto(ProductoBE obj) {
        if(obj.getCodigo() != null && obj.getCodigo().length() > 0)
            txt_codigo.setText(obj.getCodigo());
        
        /*if(obj.getDescripcion()!= null && obj.getDescripcion().length() > 0)
            txt_descripcion.setText(obj.getDescripcion());
        
        if(obj.getDescripcion_coloquial()!= null && obj.getDescripcion_coloquial().length() > 0)
            txt_descripcion_coloquial.setText(obj.getDescripcion_coloquial());
        
        txt_precio_unitario.setText(String.valueOf(obj.getPrecio_promedio()));
        
        if(obj.getMarca()!= null && obj.getMarca().length() > 0)
            txt_marca.setText(obj.getMarca());
        
        if(obj.getModelo()!= null && obj.getModelo().length() > 0)
            txt_modelo.setText(obj.getModelo());
        
        txt_peso.setText(String.valueOf(obj.getPeso()));
        txtCantidad.setText(String.valueOf(obj.getCantidad()));*/
    }

    private List<ProductoDetalleBE> ObtenerRegistrosProductoDetalle() {
        
        List<ProductoDetalleBE> list = new ArrayList();
        int filas =  tabla_detalle.getRowCount();
        
        if(filas > 0){
            ProductoDetalleBE obj;
            DefaultTableModel tm = (DefaultTableModel) tabla_detalle.getModel();
            
            for(int i = 0; i < filas; i++){
                obj = new ProductoDetalleBE();
                obj.setRucProveedor((String) tm.getValueAt(i, 0)); 
                obj.setRazonsocialProveedor((String) tm.getValueAt(i, 1)); 
                obj.setPrecio(new BigDecimal((Double) tm.getValueAt(i, 2))); 
                obj.setId_empresa(id_empresa_index);
                obj.setId_usuario(id_usuario_index);
                list.add(obj);                
            }
        }
        
        return list;
    }

    private void ocultarObjetos(boolean b) {
        btn_guardar.setVisible(b);
        panel_nuevo_detalle.setVisible(b);
    }

    private String insertarCeros(int idSalidaMaterial) {
        String valor = String.valueOf(idSalidaMaterial);
        
        while(valor.length() <= 10){
            valor = "0"+valor;
        }
        
        
        return valor;
    }

    private void MostrarComboPersonal(JComboBox cbo, boolean MostrarFilaVacia, boolean Autocompletado, String cadenaDefault) {
        try {
            List<PersonalBE> list = objPersonalBL.read(null);
            if (!list.isEmpty()) {
                DefaultComboBoxModel cboModel = new DefaultComboBoxModel();
                
                if(Autocompletado)
                    AutoCompleteDecorator.decorate(cbo);
                
                if(MostrarFilaVacia)
                    cboModel.addElement("");
                
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
            List<ObrasBE> list = objObrasBL.read(null);
            if (!list.isEmpty()) {
                DefaultComboBoxModel cboModel = new DefaultComboBoxModel();
                
                if(Autocompletado)
                    AutoCompleteDecorator.decorate(cbo);
                
                if(MostrarFilaVacia)
                    cboModel.addElement("");
                
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

    private void inicializarFiltroProducto() {
        txtCodigo_bus.setText("");
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
}
