package administracion_proyecto_integrador.gui.Compras;

import administracion_proyecto_integrador.dp.Compras.Compras;
import administracion_proyecto_integrador.dp.Compras.Pro_x_Oc;
import administracion_proyecto_integrador.dp.Compras.Proveedores;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class DetalleCompraGUI extends JFrame {

    // ===================== COMPONENTES =====================
    private JTextField txtNumCompra, txtProveedor, txtFechaEmision,
                       txtFechaVenc, txtEstado;

    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;

    private JTextField txtSubtotal, txtIVA;
    private JButton btnRegistrarProducto, btnVolver;

    private String idCompraActual;
    private final DecimalFormat formato = new DecimalFormat("#,##0.00");

    // ===================== CONSTRUCTOR =====================
    public DetalleCompraGUI(String idCompra) {
        this.idCompraActual = idCompra;
        initComponents();
        cargarDatosCompra();
        cargarDetalles();
    }

    // ===================== INIT UI =====================
    private void initComponents() {
        setTitle("Detalle de Orden de Compra");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(root);

        // ---------- SUPERIOR ----------
        JPanel top = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Detalle de Orden de Compra");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        btnRegistrarProducto = new JButton("Registrar Producto");
        btnRegistrarProducto.setBackground(new Color(44, 62, 80));
        btnRegistrarProducto.setForeground(Color.WHITE);
        btnRegistrarProducto.addActionListener(e -> registrarProducto());

        top.add(titulo, BorderLayout.WEST);
        top.add(btnRegistrarProducto, BorderLayout.EAST);

        root.add(top, BorderLayout.NORTH);

        // ---------- INFO ----------
        JPanel panelInfo = new JPanel(new GridBagLayout());
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNumCompra = crearCampo(panelInfo, "N° OC:", 0, 0, gbc);
        txtProveedor = crearCampo(panelInfo, "Proveedor:", 2, 0, gbc);
        txtFechaEmision = crearCampo(panelInfo, "Fecha Emisión:", 4, 0, gbc);

        txtFechaVenc = crearCampo(panelInfo, "Fecha Vencimiento:", 0, 1, gbc);
        txtEstado = crearCampo(panelInfo, "Estado:", 2, 1, gbc);

        root.add(panelInfo, BorderLayout.CENTER);

        // ---------- TABLA ----------
        String[] cols = {"Producto", "Cantidad", "Precio Compra", "Subtotal"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tablaDetalles = new JTable(modeloTabla);
        tablaDetalles.setRowHeight(40);
        centrarColumnas();

        JScrollPane sp = new JScrollPane(tablaDetalles);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.add(new JLabel("Detalle de Productos"), BorderLayout.NORTH);
        panelTabla.add(sp, BorderLayout.CENTER);

        root.add(panelTabla, BorderLayout.SOUTH);

        // ---------- RESUMEN ----------
        JPanel resumen = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        resumen.setBackground(new Color(240, 240, 240));
        resumen.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        resumen.add(new JLabel("Subtotal:"));
        txtSubtotal = crearCampoTexto();
        resumen.add(txtSubtotal);

        resumen.add(new JLabel("IVA (15%):"));
        txtIVA = crearCampoTexto();
        resumen.add(txtIVA);

        btnVolver = new JButton("Volver");
        btnVolver.setBackground(new Color(44, 62, 80));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.addActionListener(e -> {
            new ComprasGUI().setVisible(true);
            dispose();
        });

        JPanel south = new JPanel(new BorderLayout());
        south.add(resumen, BorderLayout.CENTER);
        south.add(btnVolver, BorderLayout.EAST);

        root.add(south, BorderLayout.PAGE_END);
    }

    // ===================== HELPERS =====================
    private JTextField crearCampo(JPanel p, String lbl, int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x; gbc.gridy = y;
        p.add(new JLabel(lbl), gbc);
        gbc.gridx = x + 1;
        JTextField t = crearCampoTexto();
        p.add(t, gbc);
        return t;
    }

    private JTextField crearCampoTexto() {
        JTextField t = new JTextField(12);
        t.setEditable(false);
        t.setBackground(Color.WHITE);
        return t;
    }

    private void centrarColumnas() {
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 1; i < modeloTabla.getColumnCount(); i++) {
            tablaDetalles.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }

    // ===================== DATOS =====================
    private void cargarDatosCompra() {
        try {
            Compras c = Compras.obtenerComprasPorParametro(idCompraActual, null, null).get(0);

            txtNumCompra.setText(c.getIdCompra());
            txtProveedor.setText(
                Proveedores.obtenerNombreProveedor(c.getIdProveedor())
            );
            txtFechaEmision.setText(String.valueOf(c.getOcFechaHora()));
            txtFechaVenc.setText(String.valueOf(c.getOcFechaVenc()));
            txtEstado.setText(c.getEstadoOc());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar la orden: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDetalles() {
        modeloTabla.setRowCount(0);
        double subtotal = 0;

        try {
            List<Pro_x_Oc> detalles = Pro_x_Oc.obtenerDetallesCompra(idCompraActual);

            for (Pro_x_Oc d : detalles) {
                modeloTabla.addRow(new Object[]{
                    d.getIdProducto(),
                    d.getPxoCantidad(),
                    "$ " + formato.format(d.getPxoValor()),
                    "$ " + formato.format(d.getPxoSubtotal())
                });
                subtotal += d.getPxoSubtotal();
            }

            double iva = subtotal * 0.15;
            txtSubtotal.setText("$ " + formato.format(subtotal));
            txtIVA.setText("$ " + formato.format(iva));

        } catch (Exception e) {
            txtSubtotal.setText("$ 0.00");
            txtIVA.setText("$ 0.00");
        }
    }

    // ===================== ACCIÓN =====================
    private void registrarProducto() {
        SeleccionProductoCompraGUI dlg =
            new SeleccionProductoCompraGUI(this, idCompraActual);
        dlg.setVisible(true);

        if (dlg.isProductoAgregado()) {
            cargarDetalles();
        }
    }
}
