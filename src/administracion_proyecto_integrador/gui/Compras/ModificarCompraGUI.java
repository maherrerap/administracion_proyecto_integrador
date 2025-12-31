package administracion_proyecto_integrador.gui.Compras;

import administracion_proyecto_integrador.dp.Compras.Compras;
import administracion_proyecto_integrador.dp.Compras.Pro_x_Oc;
import administracion_proyecto_integrador.dp.Compras.Proveedores;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class ModificarCompraGUI extends JFrame {

    // ===================== CABECERA =====================
    private JTextField txtIdCompra;
    private JComboBox<ProveedorItem> cmbProveedor;
    private JTextField txtFechaEmision;
    private JDateChooser dcFechaVenc;
    private JTextField txtEstado;

    // ===================== DETALLE =====================
    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;

    // ===================== RESUMEN =====================
    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblTotal;

    // ===================== CONTROL =====================
    private String idCompraActual;
    private List<Pro_x_Oc> detallesActuales;

    // ===================== ITEM PROVEEDOR =====================
    private static class ProveedorItem {
        private String id;
        private String nombre;

        public ProveedorItem(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public String getId() { return id; }

        @Override
        public String toString() {
            return nombre;
        }
    }

    // ===================== CONSTRUCTOR =====================
    public ModificarCompraGUI(String idCompra) {
        this.idCompraActual = idCompra;
        initComponents();
        cargarDatosCompra();
    }

    // ===================== INIT =====================
    private void initComponents() {
        setTitle("Administración de Compras (Modificar Orden de Compra)");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(root);

        root.add(crearCabecera(), BorderLayout.NORTH);
        root.add(crearPanelDetalles(), BorderLayout.CENTER);
        root.add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    // ===================== CABECERA =====================
    private JPanel crearCabecera() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Modificar Orden de Compra");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.WEST);

        JButton btnActualizar = new JButton("Actualizar Orden");
        btnActualizar.setBackground(new Color(15, 23, 42));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnActualizar.addActionListener(e -> actualizarCompra());
        panel.add(btnActualizar, BorderLayout.EAST);

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        campos.add(new JLabel("N° Orden:"), gbc);
        gbc.gridx = 1;
        txtIdCompra = crearTextField(false);
        campos.add(txtIdCompra, gbc);

        gbc.gridx = 2;
        campos.add(new JLabel("Proveedor:"), gbc);
        gbc.gridx = 3;
        cmbProveedor = new JComboBox<>();
        cargarProveedores();
        campos.add(cmbProveedor, gbc);

        gbc.gridx = 4;
        campos.add(new JLabel("Fecha Emisión:"), gbc);
        gbc.gridx = 5;
        txtFechaEmision = crearTextField(false);
        campos.add(txtFechaEmision, gbc);

        gbc.gridx = 6;
        campos.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 7;
        txtEstado = crearTextField(false);
        campos.add(txtEstado, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        campos.add(new JLabel("Fecha Vencimiento:"), gbc);
        gbc.gridx = 1;
        dcFechaVenc = new JDateChooser();
        dcFechaVenc.setDateFormatString("dd/MM/yyyy");
        campos.add(dcFechaVenc, gbc);

        panel.add(campos, BorderLayout.SOUTH);
        return panel;
    }

    private JTextField crearTextField(boolean editable) {
        JTextField t = new JTextField();
        t.setEditable(editable);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setBackground(editable ? Color.WHITE : new Color(245,245,245));
        return t;
    }

    // ===================== DETALLE =====================
    private JPanel crearPanelDetalles() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);

        String[] cols = {"Producto", "Cantidad", "Precio", "Subtotal", "+", "-", "Eliminar"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) {
                return c >= 4;
            }
        };

        tablaDetalles = new JTable(modeloTabla);
        tablaDetalles.setRowHeight(38);

        tablaDetalles.getColumnModel().getColumn(4)
                .setCellRenderer(new ButtonRenderer("+", new Color(59,130,246)));
        tablaDetalles.getColumnModel().getColumn(5)
                .setCellRenderer(new ButtonRenderer("-", new Color(59,130,246)));
        tablaDetalles.getColumnModel().getColumn(6)
                .setCellRenderer(new ButtonRenderer("Eliminar", new Color(239,68,68)));

        tablaDetalles.getColumnModel().getColumn(4)
                .setCellEditor(new ButtonEditor(new JCheckBox(), "+"));
        tablaDetalles.getColumnModel().getColumn(5)
                .setCellEditor(new ButtonEditor(new JCheckBox(), "-"));
        tablaDetalles.getColumnModel().getColumn(6)
                .setCellEditor(new ButtonEditor(new JCheckBox(), "Eliminar"));

        panel.add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);
        return panel;
    }

    // ===================== RESUMEN =====================
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel resumen = new JPanel(new GridLayout(3,2,10,10));
        resumen.setBorder(BorderFactory.createTitledBorder("Resumen"));

        resumen.add(new JLabel("Subtotal:"));
        lblSubtotal = new JLabel("$ 0.00");
        resumen.add(lblSubtotal);

        resumen.add(new JLabel("IVA:"));
        lblIva = new JLabel("$ 0.00");
        resumen.add(lblIva);

        resumen.add(new JLabel("Total:"));
        lblTotal = new JLabel("$ 0.00");
        resumen.add(lblTotal);

        panel.add(resumen, BorderLayout.WEST);

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> dispose());
        panel.add(btnVolver, BorderLayout.EAST);

        return panel;
    }

    // ===================== DATOS =====================
    private void cargarDatosCompra() {
        try {
            Compras compra = Compras.obtenerComprasPorParametro(idCompraActual, null, null).get(0);

            txtIdCompra.setText(compra.getIdCompra());
            txtEstado.setText(compra.getEstadoOc());

            txtFechaEmision.setText(
                compra.getOcFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );

            if (compra.getOcFechaVenc() != null)
                dcFechaVenc.setDate(java.sql.Date.valueOf(compra.getOcFechaVenc()));

            for (int i = 0; i < cmbProveedor.getItemCount(); i++) {
                if (cmbProveedor.getItemAt(i).getId().equals(compra.getIdProveedor())) {
                    cmbProveedor.setSelectedIndex(i);
                    break;
                }
            }

            detallesActuales = Pro_x_Oc.obtenerDetallesCompra(idCompraActual);
            modeloTabla.setRowCount(0);

            for (Pro_x_Oc d : detallesActuales) {
                modeloTabla.addRow(new Object[]{
                    d.getIdProducto(),
                    d.getPxoCantidad(),
                    String.format("%.2f", d.getPxoValor()),
                    String.format("%.2f", d.getPxoSubtotal()),
                    "+", "-", "Eliminar"
                });
            }

            recalcularTotales();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar la orden: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recalcularTotales() {
        double subtotal = 0;
        for (Pro_x_Oc d : detallesActuales)
            subtotal += d.getPxoSubtotal();

        double iva = subtotal * 0.15;
        lblSubtotal.setText(String.format("$ %.2f", subtotal));
        lblIva.setText(String.format("$ %.2f", iva));
        lblTotal.setText(String.format("$ %.2f", subtotal + iva));
    }

    // ===================== ACCIONES =====================
    private void actualizarCompra() {
        try {
            for (Pro_x_Oc d : detallesActuales) {
                Pro_x_Oc.modificarPxo(idCompraActual, d.getIdProducto(), d.getPxoCantidad());
            }

            Compras c = new Compras();
            c.setIdCompra(idCompraActual);
            c.setIdProveedor(((ProveedorItem)cmbProveedor.getSelectedItem()).getId());
            c.setOcFechaVenc(dcFechaVenc.getDate() == null ? null :
                new java.sql.Date(dcFechaVenc.getDate().getTime()).toLocalDate());
            c.setOcSubtotal(detallesActuales.stream().mapToDouble(Pro_x_Oc::getPxoSubtotal).sum());
            c.setOcIva(c.getOcSubtotal() * 0.15);
            c.setOcTotal(c.getOcSubtotal() + c.getOcIva());

            Compras.modificarCompra(c);

            JOptionPane.showMessageDialog(this,
                "Orden de compra actualizada correctamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al actualizar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===================== BOTONES TABLA =====================
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String t, Color c) {
            setText(t);
            setBackground(c);
            setForeground(Color.WHITE);
            setFocusPainted(false);
        }
        public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private final String tipo;
        public ButtonEditor(JCheckBox c, String tipo) {
            super(c);
            this.tipo = tipo;
        }
        @Override
        public Object getCellEditorValue() {
            int row = tablaDetalles.getSelectedRow();
            if (row >= 0) {
                Pro_x_Oc d = detallesActuales.get(row);
                if (tipo.equals("+")) d.setPxoCantidad(d.getPxoCantidad() + 1);
                if (tipo.equals("-") && d.getPxoCantidad() > 1) d.setPxoCantidad(d.getPxoCantidad() - 1);
                if (tipo.equals("Eliminar")) {
                    Pro_x_Oc.eliminarPxo(idCompraActual, d.getIdProducto());
                    detallesActuales.remove(row);
                    modeloTabla.removeRow(row);
                }
                d.recalcularSubtotal();
                modeloTabla.setValueAt(d.getPxoCantidad(), row, 1);
                modeloTabla.setValueAt(String.format("%.2f", d.getPxoSubtotal()), row, 3);
                recalcularTotales();
            }
            return tipo;
        }
    }

    private void cargarProveedores() {
        try {
            for (Proveedores p : Proveedores.obtenerProveedoresActivos()) {
                cmbProveedor.addItem(new ProveedorItem(p.getIdProveedor(), p.getProvNombre()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar proveedores", "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}