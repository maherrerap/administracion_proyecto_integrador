package administracion_proyecto_integrador.gui.Facturacion;

import administracion_proyecto_integrador.dp.Facturacion.Pro_x_Fac;
import administracion_proyecto_integrador.dp.Facturacion.Facturas;
import administracion_proyecto_integrador.dp.Facturacion.Clientes;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.text.DecimalFormat;
import java.util.List;
import java.text.SimpleDateFormat;

public class DetalleFacturaGUI extends JFrame {
    
    // Componentes de la interfaz
    private JLabel lblTitulo;
    private JLabel lblNumFactura, lblCliente, lblDescripcion;
    private JLabel lblFechaEmision, lblFechaPago, lblEstado;
    private JTextField txtNumFactura, txtCliente, txtDescripcion;
    private JTextField txtFechaEmision, txtFechaPago, txtEstado;
    
    private JButton btnRegistrarProducto;
    private JButton btnVolver;
    
    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;
    
    private JLabel lblSubtotal, lblIVA;
    private JTextField txtSubtotal, txtIVA;
    
    private String idFacturaActual;
    private DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00");
    
    public DetalleFacturaGUI(String idFactura) {
        this.idFacturaActual = idFactura;
        initComponents();
        cargarDatosFactura();
        cargarDetalles();
    }
    
    private void initComponents() {
        setTitle("Detalle de Factura");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel superior (título y botón)
        JPanel panelSuperior = new JPanel(new BorderLayout());
        
        lblTitulo = new JLabel("Detalle de Factura");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        
        btnRegistrarProducto = new JButton("Registrar Producto");
        btnRegistrarProducto.setPreferredSize(new Dimension(150, 35));
        btnRegistrarProducto.setBackground(new Color(44, 62, 80));
        btnRegistrarProducto.setForeground(Color.WHITE);
        btnRegistrarProducto.setFocusPainted(false);
        btnRegistrarProducto.addActionListener(e -> registrarProducto());
        panelSuperior.add(btnRegistrarProducto, BorderLayout.EAST);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        
        // Panel de información de la factura
        JPanel panelInfo = new JPanel(new GridBagLayout());
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Primera fila
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        lblNumFactura = new JLabel("N° Factura:");
        panelInfo.add(lblNumFactura, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.2;
        txtNumFactura = new JTextField();
        txtNumFactura.setEditable(false);
        txtNumFactura.setForeground(Color.GRAY);
        panelInfo.add(txtNumFactura, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0;
        lblCliente = new JLabel("Cliente:");
        panelInfo.add(lblCliente, gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.3;
        txtCliente = new JTextField();
        txtCliente.setEditable(false);
        txtCliente.setForeground(Color.GRAY);
        panelInfo.add(txtCliente, gbc);
        
        gbc.gridx = 4; gbc.weightx = 0;
        lblFechaEmision = new JLabel("Fecha de Emisión:");
        panelInfo.add(lblFechaEmision, gbc);
        
        gbc.gridx = 5; gbc.weightx = 0.15;
        txtFechaEmision = new JTextField();
        txtFechaEmision.setEditable(false);
        txtFechaEmision.setForeground(Color.GRAY);
        panelInfo.add(txtFechaEmision, gbc);
        
        // Segunda fila
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        lblDescripcion = new JLabel("Descripción:");
        panelInfo.add(lblDescripcion, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.2;
        txtDescripcion = new JTextField();
        txtDescripcion.setEditable(false);
        txtDescripcion.setForeground(Color.GRAY);
        panelInfo.add(txtDescripcion, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0;
        lblFechaPago = new JLabel("Fecha de Pago:");
        panelInfo.add(lblFechaPago, gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.3;
        txtFechaPago = new JTextField();
        txtFechaPago.setEditable(false);
        txtFechaPago.setForeground(Color.GRAY);
        panelInfo.add(txtFechaPago, gbc);
        
        gbc.gridx = 4; gbc.weightx = 0;
        lblEstado = new JLabel("Estado:");
        panelInfo.add(lblEstado, gbc);
        
        gbc.gridx = 5; gbc.weightx = 0.15;
        txtEstado = new JTextField();
        txtEstado.setEditable(false);
        txtEstado.setForeground(Color.GRAY);
        panelInfo.add(txtEstado, gbc);
        
        panelCentral.add(panelInfo, BorderLayout.NORTH);
        
        // Panel de tabla
        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        
        JLabel lblDetalleProductos = new JLabel("Detalle de Productos");
        lblDetalleProductos.setFont(new Font("Arial", Font.BOLD, 14));
        lblDetalleProductos.setForeground(new Color(52, 152, 219));
        panelTabla.add(lblDetalleProductos, BorderLayout.NORTH);
        
        // Tabla de detalles
        String[] columnas = {"Código", "Nombre Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaDetalles = new JTable(modeloTabla);
        tablaDetalles.setRowHeight(40);
        tablaDetalles.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaDetalles.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaDetalles.getTableHeader().setReorderingAllowed(false);
        tablaDetalles.getTableHeader().setPreferredSize(new Dimension(0, 35));
        
        // Centrar las columnas de Cantidad, Precio Unitario y Subtotal
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        tablaDetalles.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Cantidad
        tablaDetalles.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Precio Unitario
        tablaDetalles.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Subtotal
        
        JScrollPane scrollPane = new JScrollPane(tablaDetalles);
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        panelCentral.add(panelTabla, BorderLayout.CENTER);
        
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        
        // Panel Resumen
        JPanel panelResumenContainer = new JPanel(new BorderLayout(5, 5));
        
        JLabel lblResumen = new JLabel("Resumen");
        lblResumen.setFont(new Font("Arial", Font.BOLD, 14));
        lblResumen.setForeground(new Color(52, 152, 219));
        panelResumenContainer.add(lblResumen, BorderLayout.NORTH);
        
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        panelResumen.setBackground(new Color(240, 240, 240));
        panelResumen.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panelResumen.setPreferredSize(new Dimension(0, 60));
        
        lblSubtotal = new JLabel("Subtotal:");
        lblSubtotal.setFont(new Font("Arial", Font.PLAIN, 14));
        panelResumen.add(lblSubtotal);
        
        txtSubtotal = new JTextField("$ ...");
        txtSubtotal.setPreferredSize(new Dimension(120, 30));
        txtSubtotal.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSubtotal.setEditable(false);
        txtSubtotal.setBackground(Color.WHITE);
        panelResumen.add(txtSubtotal);
        
        lblIVA = new JLabel("IVA (15%):");
        lblIVA.setFont(new Font("Arial", Font.PLAIN, 14));
        panelResumen.add(lblIVA);
        
        txtIVA = new JTextField("$ ...");
        txtIVA.setPreferredSize(new Dimension(120, 30));
        txtIVA.setFont(new Font("Arial", Font.PLAIN, 14));
        txtIVA.setEditable(false);
        txtIVA.setBackground(Color.WHITE);
        panelResumen.add(txtIVA);
        
        panelResumenContainer.add(panelResumen, BorderLayout.CENTER);
        panelInferior.add(panelResumenContainer, BorderLayout.CENTER);
        
        // Botón Volver
        JPanel panelBotonVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(120, 35));
        btnVolver.setBackground(new Color(44, 62, 80));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Operación Cancelada.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );

            new FacturasGUI().setVisible(true);
            dispose();
        });

        panelBotonVolver.add(btnVolver);
        panelInferior.add(panelBotonVolver, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private void cargarDatosFactura() {
        try {
            // Cargar los datos de la factura desde la BD usando el DP
            Facturas factura = Facturas.obtenerFacturaPorId(idFacturaActual);

            if (factura != null) {
                txtNumFactura.setText(factura.getIdFactura());

                // Obtener el nombre del cliente en lugar de solo el ID
                String idCliente = factura.getIdCliente();
                String nombreCliente = Clientes.obtenerNombreCliente(idCliente);
                txtCliente.setText(nombreCliente);

                txtDescripcion.setText(factura.getFacDescripcion());

                // Manejar fecha de emisión
                if (factura.getFacFechaHora() != null) {
                    // Si ya viene como String desde la BD
                    txtFechaEmision.setText(factura.getFacFechaHora().toString());
                } else {
                    txtFechaEmision.setText("N/A");
                }

                // Manejar fecha de pago
                if (factura.getFacFechaPago() != null) {
                    txtFechaPago.setText(factura.getFacFechaPago().toString());
                } else {
                    txtFechaPago.setText("N/A");
                }

                txtEstado.setText(factura.getEstadoFac());

            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se encontró la factura con ID: " + idFacturaActual, 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar los datos de la factura: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void cargarDetalles() {
        modeloTabla.setRowCount(0);

        try {
            List<Pro_x_Fac> detalles = Pro_x_Fac.obtenerDetallesFactura(idFacturaActual);

            for (Pro_x_Fac detalle : detalles) {
                // Obtener el nombre real del producto desde la BD
                String nombreProducto = Pro_x_Fac.obtenerNombreProducto(detalle.getIdProducto());

                Object[] fila = {
                    detalle.getIdProducto(),
                    nombreProducto,
                    detalle.getPxfCantidad(),
                    "$ " + formatoDecimal.format(detalle.getPxfPrecio()),
                    "$ " + formatoDecimal.format(detalle.getPxfSubtotal())
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
        
        try {
            List<Pro_x_Fac> detalles = Pro_x_Fac.obtenerDetallesFactura(idFacturaActual);
            
            for (Pro_x_Fac detalle : detalles) {
                subtotal += detalle.getPxfSubtotal();
            }
            
            double iva = subtotal * 0.15;
            
            txtSubtotal.setText("$ " + formatoDecimal.format(subtotal));
            txtIVA.setText("$ " + formatoDecimal.format(iva));
            
        } catch (Exception e) {
            txtSubtotal.setText("$ 0.00");
            txtIVA.setText("$ 0.00");
        }
    }
    
    private void registrarProducto() {
        SeleccionProductoGUI dialog = new SeleccionProductoGUI(
            (Frame) SwingUtilities.getWindowAncestor(this), 
            idFacturaActual
        );
        dialog.setVisible(true);

        // Si se agregó un producto, recargar la tabla
        if (dialog.isProductoAgregado()) {
            cargarDetalles();
        }
    }
    
    private void incrementarCantidad(String idProducto) {
        try {
            boolean exito = Pro_x_Fac.incrementarCantidad(idFacturaActual, idProducto, 1);
            
            if (exito) {
                cargarDetalles();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se pudo incrementar la cantidad", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void decrementarCantidad(String idProducto) {
        try {
            boolean exito = Pro_x_Fac.decrementarCantidad(idFacturaActual, idProducto, 1);
            
            if (exito) {
                cargarDetalles();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se pudo decrementar la cantidad", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}