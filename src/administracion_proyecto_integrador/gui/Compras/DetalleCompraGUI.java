package administracion_proyecto_integrador.gui.Compras;

import administracion_proyecto_integrador.dp.Compras.Compras;
import administracion_proyecto_integrador.dp.Compras.Pro_x_Oc;
import administracion_proyecto_integrador.dp.Compras.Proveedores;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DetalleCompraGUI extends JFrame {
    
    // Componentes principales
    private JLabel lblTitulo;
    private JLabel lblNumCompra, lblProveedor, lblFechaEmision;
    private JLabel lblFechaVenc, lblEstado;
    private JTextField txtNumCompra, txtProveedor, txtFechaEmision;
    private JTextField txtFechaVenc, txtEstado;

    private JButton btnRegistrarProducto;
    private JButton btnVolver;

    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;

    private JLabel lblSubtotal, lblIVA;
    private JTextField txtSubtotal, txtIVA;

    private String idCompraActual;
    private ComprasGUI parentGUI;
    private DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00");

    public DetalleCompraGUI(String idCompra) {
        this.idCompraActual = idCompra;
        initComponents();
        cargarDatosCompra();
        cargarDetalles();
    }

    private void initComponents() {
        setTitle("Detalle de Orden de Compra");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelSuperior = new JPanel(new BorderLayout());

        lblTitulo = new JLabel("Detalle de Orden de Compra");
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

        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));

        JPanel panelInfo = new JPanel(new GridBagLayout());
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        lblNumCompra = new JLabel("N° Orden Compra:");
        panelInfo.add(lblNumCompra, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.2;
        txtNumCompra = new JTextField();
        txtNumCompra.setEditable(false);
        txtNumCompra.setForeground(Color.GRAY);
        panelInfo.add(txtNumCompra, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        lblProveedor = new JLabel("Proveedor:");
        panelInfo.add(lblProveedor, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.3;
        txtProveedor = new JTextField();
        txtProveedor.setEditable(false);
        txtProveedor.setForeground(Color.GRAY);
        panelInfo.add(txtProveedor, gbc);

        gbc.gridx = 4;
        gbc.weightx = 0;
        lblFechaEmision = new JLabel("Fecha de Emisión:");
        panelInfo.add(lblFechaEmision, gbc);

        gbc.gridx = 5;
        gbc.weightx = 0.15;
        txtFechaEmision = new JTextField();
        txtFechaEmision.setEditable(false);
        txtFechaEmision.setForeground(Color.GRAY);
        panelInfo.add(txtFechaEmision, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        lblFechaVenc = new JLabel("Fecha de Vencimiento:");
        panelInfo.add(lblFechaVenc, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.2;
        txtFechaVenc = new JTextField();
        txtFechaVenc.setEditable(false);
        txtFechaVenc.setForeground(Color.GRAY);
        panelInfo.add(txtFechaVenc, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        lblEstado = new JLabel("Estado:");
        panelInfo.add(lblEstado, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.3;
        txtEstado = new JTextField();
        txtEstado.setEditable(false);
        txtEstado.setForeground(Color.GRAY);
        panelInfo.add(txtEstado, gbc);

        panelCentral.add(panelInfo, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));

        JLabel lblDetalleProductos = new JLabel("Detalle de Productos");
        lblDetalleProductos.setFont(new Font("Arial", Font.BOLD, 14));
        lblDetalleProductos.setForeground(new Color(52, 152, 219));
        panelTabla.add(lblDetalleProductos, BorderLayout.NORTH);

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

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        tablaDetalles.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Cantidad
        tablaDetalles.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Precio Unitario
        tablaDetalles.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Subtotal

        JScrollPane scrollPane = new JScrollPane(tablaDetalles);
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        panelCentral.add(panelTabla, BorderLayout.CENTER);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));

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

        JPanel panelBotonVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(120, 35));
        btnVolver.setBackground(new Color(44, 62, 80));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                ComprasGUI comprasGUI = new ComprasGUI();
                comprasGUI.setVisible(true);
            });
        });

        panelBotonVolver.add(btnVolver);
        panelInferior.add(panelBotonVolver, BorderLayout.SOUTH);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void cargarDatosCompra() {
        try {
            Compras c = Compras.obtenerOrdenCompraPorId(idCompraActual);

            if (c != null) {
                txtNumCompra.setText(c.getIdCompra());

                String idProveedor = c.getIdProveedor();
                String nombreProveedor = Proveedores.obtenerNombreProveedor(idProveedor);
                txtProveedor.setText(nombreProveedor);

                if (c.getOcFechaHora() != null) {
                    txtFechaEmision.setText(c.getOcFechaHora().toString());
                } else {
                    txtFechaEmision.setText("N/A");
                }

                if (c.getOcFechaVenc() != null) {
                    txtFechaVenc.setText(c.getOcFechaVenc().toString());
                } else {
                    txtFechaVenc.setText("N/A");
                }

                txtEstado.setText(c.getEstadoOc());

            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró la orden de compra con ID: " + idCompraActual,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los datos de la orden de compra: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDetalles() {
        modeloTabla.setRowCount(0);

        try {
            List<Pro_x_Oc> detalles = Pro_x_Oc.obtenerDetallesCompra(idCompraActual);

            for (Pro_x_Oc detalle : detalles) {
                String nombreProducto = Pro_x_Oc.obtenerNombreProducto(detalle.getIdProducto());

                Object[] fila = {
                        detalle.getIdProducto(),
                        nombreProducto,
                        detalle.getPxoCantidad(),
                        "$ " + formatoDecimal.format(detalle.getPxoValor()),
                        "$ " + formatoDecimal.format(detalle.getPxoSubtotal())
                };
                modeloTabla.addRow(fila);
            }

            calcularTotales();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo completar la operación. Intente de nuevo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calcularTotales() {
        double subtotal = 0.0;

        try {
            List<Pro_x_Oc> detalles = Pro_x_Oc.obtenerDetallesCompra(idCompraActual);

            for (Pro_x_Oc detalle : detalles) {
                subtotal += detalle.getPxoSubtotal();
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
        SeleccionProductoCompraGUI dialog = new SeleccionProductoCompraGUI(
                (Frame) SwingUtilities.getWindowAncestor(this),
                idCompraActual
        );
        dialog.setVisible(true);

        if (dialog.isProductoAgregado()) {
            cargarDetalles();
        }
    }
}