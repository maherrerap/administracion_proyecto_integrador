package administracion_proyecto_integrador.gui.Facturacion;

import administracion_proyecto_integrador.dp.Facturacion.Facturas;
import administracion_proyecto_integrador.dp.Facturacion.Pro_x_Fac;
import administracion_proyecto_integrador.dp.Facturacion.Clientes;
import administracion_proyecto_integrador.dp.Inventarios.Productos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class ModificarFacturaGUI extends JFrame {
    
    // Componentes de la cabecera
    private JTextField txtIdFactura;
    private JComboBox<ClienteItem> cmbCliente;
    private JTextField txtDescripcion;
    private JTextField txtFechaEmision;
    private JDateChooser dcFechaPago;
    private JTextField txtEstado;
    
    // Componentes del detalle
    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;
    
    // Componentes del resumen
    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblTotal;
    
    // Botones
    private JButton btnActualizar;
    
    // Variables de control
    private String idFacturaActual;
    private List<Pro_x_Fac> detallesActuales;
    private String idClienteOriginal;
    
    // Clase interna para el ComboBox de clientes
    private static class ClienteItem {
        private String id;
        private String nombre;
        
        public ClienteItem(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
        
        public String getId() { return id; }
        public String getNombre() { return nombre; }
        
        @Override
        public String toString() {
            return nombre;
        }
    }
    
    public ModificarFacturaGUI(String idFactura) {
        this.idFacturaActual = idFactura;
        initComponents();
        cargarDatosFactura();
    }
    
    private void initComponents() {
        setTitle("Administración de Facturación (Modificar Factura)");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Panel principal con fondo blanco
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Panel superior - Cabecera
        JPanel panelCabecera = crearPanelCabecera();
        panelPrincipal.add(panelCabecera, BorderLayout.NORTH);
        
        // Panel central - Detalles
        JPanel panelDetalles = crearPanelDetalles();
        panelPrincipal.add(panelDetalles, BorderLayout.CENTER);
        
        // Panel inferior - Resumen
        JPanel panelResumen = crearPanelResumen();
        panelPrincipal.add(panelResumen, BorderLayout.SOUTH);
        
        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelCabecera() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Panel título y botón
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(Color.WHITE);
        
        // Título
        JLabel lblTitulo = new JLabel("Modificar Factura");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelTop.add(lblTitulo, BorderLayout.WEST);
        
        // Botón Actualizar
        btnActualizar = new JButton("Actualizar Factura");
        btnActualizar.setBackground(new Color(15, 23, 42));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnActualizar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnActualizar.addActionListener(e -> actualizarFactura());
        panelTop.add(btnActualizar, BorderLayout.EAST);
        
        panel.add(panelTop, BorderLayout.NORTH);
        
        // Panel de campos en grid
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 15);
        
        // Fila 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelCampos.add(crearLabel("N.° Factura:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtIdFactura = crearTextField(false);
        panelCampos.add(txtIdFactura, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0;
        panelCampos.add(crearLabel("Cliente:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.5;
        cmbCliente = new JComboBox<>();
        cmbCliente.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbCliente.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        cmbCliente.setBackground(Color.WHITE);
        cargarClientes();
        panelCampos.add(cmbCliente, gbc);
        
        gbc.gridx = 4; gbc.weightx = 0;
        panelCampos.add(crearLabel("Fecha de Emisión:"), gbc);
        
        gbc.gridx = 5; gbc.weightx = 0.3;
        txtFechaEmision = crearTextField(false);
        panelCampos.add(txtFechaEmision, gbc);
        
        gbc.gridx = 6; gbc.weightx = 0;
        panelCampos.add(crearLabel("Estado:"), gbc);
        
        gbc.gridx = 7; gbc.weightx = 0.2;
        txtEstado = crearTextField(false);
        panelCampos.add(txtEstado, gbc);
        
        // Fila 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelCampos.add(crearLabel("Descripción:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtDescripcion = crearTextField(true);
        panelCampos.add(txtDescripcion, gbc);
        
        gbc.gridx = 4; gbc.weightx = 0;
        panelCampos.add(crearLabel("Fecha de Pago:"), gbc);
        
        gbc.gridx = 5; gbc.weightx = 0.3;
        dcFechaPago = new JDateChooser();
        dcFechaPago.setDateFormatString("dd/MM/yyyy");
        dcFechaPago.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dcFechaPago.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panelCampos.add(dcFechaPago, gbc);
        
        panel.add(panelCampos, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void cargarClientes() {
        try {
            List<Clientes> clientes = Clientes.obtenerClientesActivos();
            cmbCliente.removeAllItems();
            
            for (Clientes cliente : clientes) {
                cmbCliente.addItem(new ClienteItem(cliente.getIdCliente(), cliente.getCliNombre()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar clientes: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }
    
    private JTextField crearTextField(boolean editable) {
        JTextField textField = new JTextField();
        textField.setEditable(editable);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        if (!editable) {
            textField.setBackground(new Color(249, 249, 249));
            textField.setForeground(new Color(150, 150, 150));
        } else {
            textField.setBackground(Color.WHITE);
        }
        
        return textField;
    }
    
    private JPanel crearPanelDetalles() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        // Título
        JLabel lblTitulo = new JLabel("Detalle de Productos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(37, 99, 235));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        // Crear tabla
        String[] columnas = {"Código", "Nombre Producto", "Cantidad", "Precio Unitario", "Subtotal", "+", "-", "Eliminar"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 5; // Solo los botones son "editables"
            }
        };
        
        tablaDetalles = new JTable(modeloTabla);
        tablaDetalles.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaDetalles.setRowHeight(40);
        tablaDetalles.setShowGrid(true);
        tablaDetalles.setGridColor(new Color(230, 230, 230));
        tablaDetalles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDetalles.setSelectionBackground(new Color(240, 245, 255));
        
        // Estilo del header
        JTableHeader header = tablaDetalles.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(248, 248, 248));
        header.setForeground(new Color(80, 80, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        
        // Renderizador para centrar las columnas de botones
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Renderizador para columnas de precio
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        
        tablaDetalles.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaDetalles.getColumnModel().getColumn(1).setPreferredWidth(250);
        tablaDetalles.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaDetalles.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tablaDetalles.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaDetalles.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tablaDetalles.getColumnModel().getColumn(4).setPreferredWidth(120);
        tablaDetalles.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        tablaDetalles.getColumnModel().getColumn(5).setPreferredWidth(50);
        tablaDetalles.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        tablaDetalles.getColumnModel().getColumn(6).setPreferredWidth(50);
        tablaDetalles.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        tablaDetalles.getColumnModel().getColumn(7).setPreferredWidth(100);
        tablaDetalles.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        
        // Agregar renderizador de botones
        tablaDetalles.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("+", new Color(59, 130, 246)));
        tablaDetalles.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("-", new Color(59, 130, 246)));
        tablaDetalles.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer("Eliminar", new Color(239, 68, 68)));
        
        tablaDetalles.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), this, "+"));
        tablaDetalles.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), this, "-"));
        tablaDetalles.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox(), this, "Eliminar"));
        
        JScrollPane scrollPane = new JScrollPane(tablaDetalles);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Título
        JLabel lblTitulo = new JLabel("Resumen");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(37, 99, 235));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        // Panel de valores
        JPanel panelValores = new JPanel();
        panelValores.setLayout(new BoxLayout(panelValores, BoxLayout.Y_AXIS));
        panelValores.setBackground(new Color(245, 247, 250));
        panelValores.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Crear filas de resumen
        JPanel filaSubtotal = crearFilaResumen("Subtotal:", "$ ...");
        JPanel filaIva = crearFilaResumen("IVA (15%):", "$ ...");
        JPanel filaTotal = crearFilaResumen("Total:", "$ ...");
        
        // Inicializar los labels
        lblSubtotal = (JLabel) filaSubtotal.getComponent(1);
        lblIva = (JLabel) filaIva.getComponent(1);
        lblTotal = (JLabel) filaTotal.getComponent(1);
        
        panelValores.add(filaSubtotal);
        panelValores.add(Box.createVerticalStrut(8));
        panelValores.add(filaIva);
        panelValores.add(Box.createVerticalStrut(8));
        panelValores.add(filaTotal);
        
        JPanel panelContenedor = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        panelContenedor.setBackground(Color.WHITE);
        panelContenedor.add(panelValores);
        
        panel.add(panelContenedor, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearFilaResumen(String titulo, String valor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(new Color(245, 247, 250));
        panel.setMaximumSize(new Dimension(300, 25));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitulo.setForeground(new Color(80, 80, 80));
        lblTitulo.setPreferredSize(new Dimension(100, 25));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblValor.setForeground(new Color(100, 100, 100));
        
        panel.add(lblTitulo);
        panel.add(lblValor);
        
        return panel;
    }
    
    // Clase para renderizar botones en la tabla
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer(String text, Color bgColor) {
            setText(text);
            setOpaque(true);
            setBackground(bgColor);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    // Clase para editar (hacer clic en) botones en la tabla
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private ModificarFacturaGUI parent;
        
        public ButtonEditor(JCheckBox checkBox, ModificarFacturaGUI parent, String label) {
            super(checkBox);
            this.parent = parent;
            this.label = label;
            
            Color bgColor = label.equals("Eliminar") ? new Color(239, 68, 68) : new Color(59, 130, 246);
            
            button = new JButton(label);
            button.setOpaque(true);
            button.setBackground(bgColor);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            button.addActionListener(e -> fireEditingStopped());
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            isPushed = true;
            return button;
        }
        
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = tablaDetalles.getSelectedRow();
                if (row != -1) {
                    if (label.equals("+")) {
                        parent.incrementarCantidad();
                    } else if (label.equals("-")) {
                        parent.decrementarCantidad();
                    } else if (label.equals("Eliminar")) {
                        parent.eliminarProducto();
                    }
                }
            }
            isPushed = false;
            return label;
        }
        
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
    
    private void cargarDatosFactura() {
        try {
            // Cargar cabecera
            Facturas factura = Facturas.obtenerFacturaPorId(idFacturaActual);
            
            if (factura != null) {
                txtIdFactura.setText(factura.getIdFactura());
                txtDescripcion.setText(factura.getFacDescripcion());
                txtEstado.setText(factura.getEstadoFac());
                
                // Guardar el ID del cliente original
                idClienteOriginal = factura.getIdCliente();
                
                // Seleccionar el cliente en el ComboBox
                for (int i = 0; i < cmbCliente.getItemCount(); i++) {
                    ClienteItem item = cmbCliente.getItemAt(i);
                    if (item.getId().equals(factura.getIdCliente())) {
                        cmbCliente.setSelectedIndex(i);
                        break;
                    }
                }
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                if (factura.getFacFechaHora() != null) {
                    txtFechaEmision.setText(factura.getFacFechaHora().format(formatter));
                }
                
                if (factura.getFacFechaPago() != null) {
                    // Convertir LocalDate a java.util.Date para JDateChooser
                    java.util.Date date = java.sql.Date.valueOf(factura.getFacFechaPago());
                    dcFechaPago.setDate(date);
                }
            }
            
            // Cargar detalles
            cargarDetalles();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar los datos de la factura: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarDetalles() {
        try {
            detallesActuales = Pro_x_Fac.obtenerDetallesFactura(idFacturaActual);
            modeloTabla.setRowCount(0);
            
            for (Pro_x_Fac detalle : detallesActuales) {
                String nombreProducto = Pro_x_Fac.obtenerNombreProducto(detalle.getIdProducto());
                
                Object[] fila = {
                    detalle.getIdProducto(),
                    nombreProducto,
                    detalle.getPxfCantidad(),
                    String.format("$ %.2f", detalle.getPxfPrecio()),
                    String.format("$ %.2f", detalle.getPxfSubtotal()),
                    "+",
                    "-",
                    "Eliminar"
                };
                
                modeloTabla.addRow(fila);
            }
            
            calcularTotales();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar los detalles: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void calcularTotales() {
        double subtotal = 0.0;
        
        for (Pro_x_Fac detalle : detallesActuales) {
            subtotal += detalle.getPxfSubtotal();
        }
        
        double iva = subtotal * 0.15;
        double total = subtotal + iva;
        
        lblSubtotal.setText(String.format("$ %.2f", subtotal));
        lblIva.setText(String.format("$ %.2f", iva));
        lblTotal.setText(String.format("$ %.2f", total));
    }
    
    public void incrementarCantidad() {
        int filaSeleccionada = tablaDetalles.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un producto de la tabla",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String idProducto = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
            int cantidadActual = (int) modeloTabla.getValueAt(filaSeleccionada, 2);
            
            // Actualizar stock: descontar 1 unidad más
            boolean stockActualizado = Productos.actualizarStockPorVenta(idProducto, 1);
            
            if (!stockActualizado) {
                JOptionPane.showMessageDialog(this,
                    "Stock insuficiente para incrementar la cantidad",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Incrementar cantidad en la base de datos
            boolean resultado = Pro_x_Fac.incrementarCantidad(idFacturaActual, idProducto, 1);
            
            if (resultado) {
                cargarDetalles();
            } else {
                // Revertir el stock si falla la actualización
                Productos.revertirStockPorVenta(idProducto, 1);
                JOptionPane.showMessageDialog(this,
                    "No se pudo incrementar la cantidad",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al incrementar la cantidad: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void decrementarCantidad() {
        int filaSeleccionada = tablaDetalles.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un producto de la tabla",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String idProducto = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
            int cantidadActual = (int) modeloTabla.getValueAt(filaSeleccionada, 2);
            
            // Revertir stock: devolver 1 unidad
            boolean stockRevertido = Productos.revertirStockPorVenta(idProducto, 1);
            
            if (!stockRevertido) {
                JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el stock",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Decrementar cantidad en la base de datos
            boolean resultado = Pro_x_Fac.decrementarCantidad(idFacturaActual, idProducto, 1);
            
            if (resultado) {
                cargarDetalles();
            } else {
                // Revertir el cambio de stock si falla
                Productos.actualizarStockPorVenta(idProducto, 1);
                JOptionPane.showMessageDialog(this,
                    "No se pudo decrementar la cantidad",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al decrementar la cantidad: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void eliminarProducto() {
        int filaSeleccionada = tablaDetalles.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un producto de la tabla",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idProducto = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreProducto = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar el registro?\n" + nombreProducto,
            "Botón eliminar producto confirmación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // IMPORTANTE: Detener la edición de la celda antes de modificar la tabla
                if (tablaDetalles.isEditing()) {
                    tablaDetalles.getCellEditor().stopCellEditing();
                }

                // Eliminar el detalle (esto ya maneja la reversión del stock internamente)
                boolean resultado = Pro_x_Fac.eliminarPxf(idFacturaActual, idProducto);

                if (resultado) {
                    JOptionPane.showMessageDialog(this,
                        "Detalle eliminado correctamente",
                        "Botón eliminar producto opción si",
                        JOptionPane.INFORMATION_MESSAGE);
                    cargarDetalles();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el producto",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar el producto: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void actualizarFactura() {
        try {
            // Validar campos
            if (txtDescripcion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "La descripción es obligatoria",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (txtDescripcion.getText().length() > 100) {
                JOptionPane.showMessageDialog(this,
                    "La descripción excede la longitud permitida (máx. 100 caracteres)",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (detallesActuales == null || detallesActuales.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Debe haber al menos un producto en el detalle",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (cmbCliente.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un cliente",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Crear objeto Factura con los datos actualizados
            Facturas facturaActualizada = new Facturas();
            facturaActualizada.setIdFactura(idFacturaActual);
            facturaActualizada.setFacDescripcion(txtDescripcion.getText().trim());
            
            // Obtener el ID del cliente seleccionado
            ClienteItem clienteSeleccionado = (ClienteItem) cmbCliente.getSelectedItem();
            facturaActualizada.setIdCliente(clienteSeleccionado.getId());
            
            // Parsear fecha de pago si existe
            if (dcFechaPago.getDate() != null) {
                java.util.Date fechaUtil = dcFechaPago.getDate();
                LocalDate fechaPago = new java.sql.Date(fechaUtil.getTime()).toLocalDate();
                facturaActualizada.setFacFechaPago(fechaPago);
            }
            
            // Calcular totales
            double subtotal = 0.0;
            for (Pro_x_Fac detalle : detallesActuales) {
                subtotal += detalle.getPxfSubtotal();
            }
            double iva = subtotal * 0.15;
            double total = subtotal + iva;
            
            facturaActualizada.setFacSubtotal(subtotal);
            facturaActualizada.setFacIva(iva);
            facturaActualizada.setFacTotal(total);
            
            // Actualizar factura
            boolean resultado = Facturas.modificarFactura(facturaActualizada);
            
            if (resultado) {
                JOptionPane.showMessageDialog(this,
                    "Registro modificado correctamente",
                    "Botón actualizar",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar la factura",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al actualizar la factura: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método main para pruebas
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ModificarFacturaGUI gui = new ModificarFacturaGUI("FACP020");
            gui.setVisible(true);
        });
    }
}