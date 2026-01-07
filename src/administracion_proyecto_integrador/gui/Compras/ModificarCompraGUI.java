package administracion_proyecto_integrador.gui.Compras;

import administracion_proyecto_integrador.dp.Compras.Compras;
import administracion_proyecto_integrador.dp.Compras.Pro_x_Oc;
import administracion_proyecto_integrador.dp.Compras.Proveedores;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class ModificarCompraGUI extends JFrame {
    
    // Componentes principales
    private JTextField txtIdCompra;
    private JComboBox<ProveedorItem> cmbProveedor;
    private JTextField txtFechaEmision;
    private JDateChooser dcFechaVenc;
    private JTextField txtEstado;

    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;

    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblTotal;

    private JButton btnActualizar;
    private JButton btnSalir;

    private String idCompraActual;
    private List<Pro_x_Oc> detallesActuales;
    private String idProveedorOriginal;

    private static class ProveedorItem {
        private String id;
        private String nombre;

        public ProveedorItem(String id, String nombre) {
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

    public ModificarCompraGUI(String idCompra) {
        this.idCompraActual = idCompra;
        initComponents();
        cargarDatosCompra();
    }

    private void initComponents() {
        setTitle("Administración de Compras (Modificar Orden de Compra)");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel panelCabecera = crearPanelCabecera();
        panelPrincipal.add(panelCabecera, BorderLayout.NORTH);

        JPanel panelDetalles = crearPanelDetalles();
        panelPrincipal.add(panelDetalles, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(Color.WHITE);

        JPanel panelResumen = crearPanelResumen();
        panelInferior.add(panelResumen, BorderLayout.NORTH);

        JPanel panelBotonSalir = crearPanelBotonSalir();
        panelInferior.add(panelBotonSalir, BorderLayout.SOUTH);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    private JPanel crearPanelCabecera() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Modificar Orden de Compra");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelTop.add(lblTitulo, BorderLayout.WEST);

        btnActualizar = new JButton("Actualizar Orden de Compra");
        btnActualizar.setBackground(new Color(15, 23, 42));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnActualizar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnActualizar.addActionListener(e -> actualizarCompra());
        panelTop.add(btnActualizar, BorderLayout.EAST);

        panel.add(panelTop, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 15);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelCampos.add(crearLabel("N.° Orden de Compra:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.5;
        txtIdCompra = crearTextField(false);
        panelCampos.add(txtIdCompra, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelCampos.add(crearLabel("Proveedor:"), gbc);

        gbc.gridx = 3; gbc.weightx = 0.5;
        cmbProveedor = new JComboBox<>();
        cmbProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbProveedor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        cmbProveedor.setBackground(Color.WHITE);
        cargarProveedores();
        panelCampos.add(cmbProveedor, gbc);

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

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelCampos.add(crearLabel("Fecha de Vencimiento:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.5;
        dcFechaVenc = new JDateChooser();
        dcFechaVenc.setDateFormatString("dd/MM/yyyy");
        dcFechaVenc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dcFechaVenc.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panelCampos.add(dcFechaVenc, gbc);

        panel.add(panelCampos, BorderLayout.CENTER);

        return panel;
    }

    private void cargarProveedores() {
        try {
            List<Proveedores> proveedores = Proveedores.obtenerProveedoresActivos();
            cmbProveedor.removeAllItems();

            for (Proveedores proveedor : proveedores) {
                cmbProveedor.addItem(new ProveedorItem(proveedor.getIdProveedor(), proveedor.getPrvNombre()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo completar la operación. Intente de nuevo.",
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

        JLabel lblTitulo = new JLabel("Detalle de Productos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(37, 99, 235));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        String[] columnas = {"Código", "Nombre Producto", "Cantidad", "Precio Unitario", "Subtotal", "+", "-", "Eliminar"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 5; 
            }
        };

        tablaDetalles = new JTable(modeloTabla);
        tablaDetalles.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaDetalles.setRowHeight(40);
        tablaDetalles.setShowGrid(true);
        tablaDetalles.setGridColor(new Color(230, 230, 230));
        tablaDetalles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDetalles.setSelectionBackground(new Color(240, 245, 255));

        JTableHeader header = tablaDetalles.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(248, 248, 248));
        header.setForeground(new Color(80, 80, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.CENTER);

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

        JLabel lblTitulo = new JLabel("Resumen");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(37, 99, 235));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelValores = new JPanel();
        panelValores.setLayout(new BoxLayout(panelValores, BoxLayout.Y_AXIS));
        panelValores.setBackground(new Color(245, 247, 250));
        panelValores.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JPanel filaSubtotal = crearFilaResumen("Subtotal:", "$ ...");
        JPanel filaIva = crearFilaResumen("IVA (15%):", "$ ...");
        JPanel filaTotal = crearFilaResumen("Total:", "$ ...");

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

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private ModificarCompraGUI parent;
        private int editingRow = -1;

        public ButtonEditor(JCheckBox checkBox, ModificarCompraGUI parent, String label) {
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
            button.addActionListener(e -> {
            final int row = editingRow;
            isPushed = true;
            ButtonEditor.super.stopCellEditing();
            SwingUtilities.invokeLater(() -> executeAction(row));
        });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            editingRow = row;
            isPushed = false;
            return button;
        }

        private void executeAction(int row) {
            if (row >= 0 && row < tablaDetalles.getRowCount()) {
                if (label.equals("+")) {
                    parent.incrementarCantidad(row);
                } else if (label.equals("-")) {
                    parent.decrementarCantidad(row);
                } else if (label.equals("Eliminar")) {
                    parent.eliminarProducto(row);
                }
            }
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            editingRow = -1;
            return super.stopCellEditing();
        }
    }

    private void cargarDatosCompra() {
        try {
            Compras compra = Compras.obtenerOrdenCompraPorId(idCompraActual);

            if (compra != null) {
                txtIdCompra.setText(compra.getIdCompra());
                txtEstado.setText(compra.getEstadoOc());

                idProveedorOriginal = compra.getIdProveedor();

                for (int i = 0; i < cmbProveedor.getItemCount(); i++) {
                    ProveedorItem item = cmbProveedor.getItemAt(i);
                    if (item.getId().equals(compra.getIdProveedor())) {
                        cmbProveedor.setSelectedIndex(i);
                        break;
                    }
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                if (compra.getOcFechaHora() != null) {
                    txtFechaEmision.setText(compra.getOcFechaHora().format(formatter));
                }

                if (compra.getOcFechaVenc() != null) {
                    java.util.Date date = java.sql.Date.valueOf(compra.getOcFechaVenc());
                    dcFechaVenc.setDate(date);
                }
            }

            cargarDetalles();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo completar la operación. Intente de nuevo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDetalles() {
        try {
            detallesActuales = Pro_x_Oc.obtenerDetallesCompra(idCompraActual);
            modeloTabla.setRowCount(0);

            for (Pro_x_Oc detalle : detallesActuales) {
                String nombreProducto = Pro_x_Oc.obtenerNombreProducto(detalle.getIdProducto());

                Object[] fila = {
                        detalle.getIdProducto(),
                        nombreProducto,
                        detalle.getPxoCantidad(),
                        String.format("$ %.1f", detalle.getPxoValor()),
                        String.format("$ %.1f", detalle.getPxoSubtotal()),
                        "+",
                        "-",
                        "Eliminar"
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

        for (Pro_x_Oc detalle : detallesActuales) {
            subtotal += detalle.getPxoSubtotal();
        }

        double iva = subtotal * 0.15;
        double total = subtotal + iva;

        lblSubtotal.setText(String.format("$ %.1f", subtotal));
        lblIva.setText(String.format("$ %.1f", iva));
        lblTotal.setText(String.format("$ %.1f", total));
    }
    
    public void incrementarCantidad(int filaSeleccionada) {
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
        int nuevaCantidad = cantidadActual + 1;
        
        for (Pro_x_Oc detalle : detallesActuales) {
            if (detalle.getIdProducto().equals(idProducto)) {
                double precioUnitario = detalle.getPxoValor();
                double nuevoSubtotal = nuevaCantidad * precioUnitario;

                detalle.setPxoCantidad(nuevaCantidad);
                detalle.setPxoSubtotal(nuevoSubtotal);

                modeloTabla.setValueAt(nuevaCantidad, filaSeleccionada, 2);
                modeloTabla.setValueAt(String.format("$ %.1f", nuevoSubtotal), filaSeleccionada, 4);

                break;
            }
        }

        calcularTotales();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "No se pudo completar la operación. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}

    public void decrementarCantidad(int filaSeleccionada) {

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un producto de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int cantidadActual = (int) modeloTabla.getValueAt(filaSeleccionada, 2);

            if (cantidadActual <= 1) {
                JOptionPane.showMessageDialog(this,
                        "La cantidad mínima es 1. Use el botón 'Eliminar' para quitar el producto.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String idProducto = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
            int nuevaCantidad = cantidadActual - 1;

            for (Pro_x_Oc detalle : detallesActuales) {
                if (detalle.getIdProducto().equals(idProducto)) {
                    double precioUnitario = detalle.getPxoValor();
                    double nuevoSubtotal = nuevaCantidad * precioUnitario;

                    detalle.setPxoCantidad(nuevaCantidad);
                    detalle.setPxoSubtotal(nuevoSubtotal);

                    modeloTabla.setValueAt(nuevaCantidad, filaSeleccionada, 2);
                    modeloTabla.setValueAt(String.format("$ %.1f", nuevoSubtotal), filaSeleccionada, 4);

                    break;
                }
            }

            calcularTotales();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo completar la operación. Intente de nuevo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarProducto(int filaSeleccionada) {
        if (filaSeleccionada == -1 || filaSeleccionada >= modeloTabla.getRowCount()) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un producto válido de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (modeloTabla.getRowCount() == 1) {
            JOptionPane.showMessageDialog(this,
                    "No se puede eliminar el único producto de la orden de compra.\n" +
                            "Una orden de compra debe tener al menos un producto.",
                    "Operación no permitida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idProducto = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreProducto = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el registro?\n" + nombreProducto,
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                detallesActuales.removeIf(d -> d.getIdProducto().equals(idProducto));
                modeloTabla.removeRow(filaSeleccionada);
                calcularTotales();

                JOptionPane.showMessageDialog(this,
                        "Producto eliminado de la vista. Los cambios se aplicarán al presionar 'Actualizar Orden de Compra'.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo completar la operación. Intente de nuevo.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarCompra() {
        try {
            if (cmbProveedor.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un proveedor",
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

            if (dcFechaVenc.getDate() != null) {
                Compras compraOriginal = Compras.obtenerOrdenCompraPorId(idCompraActual);

                if (compraOriginal != null && compraOriginal.getOcFechaHora() != null) {
                    java.util.Date fechaUtil = dcFechaVenc.getDate();
                    LocalDate fechaVencimientoSeleccionada = new java.sql.Date(fechaUtil.getTime()).toLocalDate();
                    LocalDate fechaEmision = compraOriginal.getOcFechaHora();

                    if (fechaVencimientoSeleccionada.isBefore(fechaEmision)) {
                        JOptionPane.showMessageDialog(this,
                                "La fecha de vencimiento no puede ser anterior a la fecha de emisión\n" +
                                        "Fecha de emisión: " + fechaEmision.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                                        "Fecha de vencimiento seleccionada: " + fechaVencimientoSeleccionada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                "Error de Validación",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            List<Pro_x_Oc> detallesOriginales = Pro_x_Oc.obtenerDetallesCompra(idCompraActual);

            for (Pro_x_Oc original : detallesOriginales) {
                boolean existe = false;
                for (Pro_x_Oc actual : detallesActuales) {
                    if (actual.getIdProducto().equals(original.getIdProducto())) {
                        existe = true;
                        break;
                    }
                }

                if (!existe) {
                    Pro_x_Oc.eliminarPxo(idCompraActual, original.getIdProducto());
                }
            }

            for (Pro_x_Oc actual : detallesActuales) {
                Pro_x_Oc original = null;
                for (Pro_x_Oc orig : detallesOriginales) {
                    if (orig.getIdProducto().equals(actual.getIdProducto())) {
                        original = orig;
                        break;
                    }
                }

                if (original != null) {
                    int cantidadOriginal = original.getPxoCantidad();
                    int cantidadNueva = actual.getPxoCantidad();

                    if (cantidadOriginal != cantidadNueva) {
                        Pro_x_Oc.modificarPxo(idCompraActual, actual.getIdProducto(), cantidadNueva);
                    }
                }
            }

            Compras compraActualizada = new Compras();
            compraActualizada.setIdCompra(idCompraActual);

            ProveedorItem proveedorSeleccionado = (ProveedorItem) cmbProveedor.getSelectedItem();
            compraActualizada.setIdProveedor(proveedorSeleccionado.getId());

            if (dcFechaVenc.getDate() != null) {
                java.util.Date fechaUtil = dcFechaVenc.getDate();
                LocalDate fechaVenc = new java.sql.Date(fechaUtil.getTime()).toLocalDate();
                compraActualizada.setOcFechaVenc(fechaVenc);
            }

            double subtotal = 0.0;
            for (Pro_x_Oc detalle : detallesActuales) {
                subtotal += detalle.getPxoSubtotal();
            }
            double iva = subtotal * 0.15;
            double total = subtotal + iva;

            compraActualizada.setOcSubtotal(subtotal);
            compraActualizada.setOcIva(iva);
            compraActualizada.setOcTotal(total);

            boolean resultado = Compras.modificarCompra(compraActualizada);

            if (resultado) {
                JOptionPane.showMessageDialog(this,
                        "Registro modificado correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                volverAComprasGUI();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo completar la operación. Intente de nuevo.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo completar la operación. Intente de nuevo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearPanelBotonSalir() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        btnSalir = new JButton("Volver");
        btnSalir.setBackground(new Color(15, 23, 42));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSalir.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Operación Cancelada.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

            SwingUtilities.invokeLater(() -> {
                ComprasGUI comprasGUI = new ComprasGUI();
                comprasGUI.setVisible(true);
            });
        });

        panel.add(btnSalir);

        return panel;
    }

    private void volverAComprasGUI() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            ComprasGUI comprasGUI = new ComprasGUI();
            comprasGUI.setVisible(true);
        });
    }
}