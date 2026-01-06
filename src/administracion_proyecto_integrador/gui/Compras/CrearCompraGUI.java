package administracion_proyecto_integrador.gui.Compras;

import administracion_proyecto_integrador.dp.Compras.Compras;
import administracion_proyecto_integrador.dp.Compras.Proveedores;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CrearCompraGUI extends JFrame {
    
    // Colores Estandar en la Aplicación
    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color AZUL_LABEL = new Color(30, 86, 198);

    // Componentes principales
    private ComprasGUI parentGUI;
    
    private JTextField txtIdCompra;
    private JTextField txtFechaEmision;
    private JComboBox<ProveedorItem> cmbProveedor;

    private JButton btnVolver;
    private JButton btnCrear;

    private boolean compraCreada = false;

    private class ProveedorItem {
        private String id;
        private String nombre;

        public ProveedorItem(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public String getId() { return id; }

        @Override
        public String toString() { return id + " - " + nombre; }
    }

    public CrearCompraGUI(ComprasGUI parentGUI) {
        this.parentGUI = parentGUI;
        setTitle("Crear Orden de Compra");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        cargarDatosIniciales();
    }

    private void initComponents() {

        setTitle("Crear Orden de Compra");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        JPanel contentWrapper = new JPanel(new GridBagLayout());
        contentWrapper.setBackground(Color.WHITE);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(30, 40, 30, 40));
        content.setPreferredSize(new Dimension(900, 600));
        content.setMaximumSize(new Dimension(1100, 800));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 20, 20);

        contentWrapper.add(content, gbc);
        root.add(contentWrapper, BorderLayout.CENTER);

        content.add(crearTitulo(), BorderLayout.NORTH);
        content.add(crearFormulario(), BorderLayout.CENTER);
    }

    private JComponent crearTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel barra = new JPanel();
        barra.setBackground(AZUL_LABEL);
        barra.setPreferredSize(new Dimension(5, 35));

        JLabel titulo = new JLabel(" Crear Orden de Compra");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(new Color(20, 20, 20));

        panel.add(barra);
        panel.add(titulo);

        return panel;
    }

    private JComponent crearFormulario() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                new EmptyBorder(30, 40, 30, 40)
        ));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);

        JPanel fila1 = new JPanel(new GridLayout(1, 2, 40, 0));
        fila1.setOpaque(false);
        fila1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        fila1.add(crearCampoIdOrden("N.º Orden Compra:"));
        fila1.add(crearCampoFechaActual("Fecha de Emisión:"));

        form.add(fila1);
        form.add(Box.createVerticalStrut(25));

        JPanel fila2 = new JPanel(new BorderLayout());
        fila2.setOpaque(false);
        fila2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblProveedor = new JLabel("Seleccione el proveedor");
        lblProveedor.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblProveedor.setForeground(new Color(60, 60, 60));

        cmbProveedor = new JComboBox<>();
        cmbProveedor.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbProveedor.setPreferredSize(new Dimension(0, 40));
        cmbProveedor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cmbProveedor.setBackground(Color.WHITE);

        fila2.add(lblProveedor, BorderLayout.NORTH);
        fila2.add(Box.createVerticalStrut(8), BorderLayout.CENTER);

        JPanel cmbWrapper = new JPanel(new BorderLayout());
        cmbWrapper.setOpaque(false);
        cmbWrapper.add(cmbProveedor, BorderLayout.NORTH);
        fila2.add(cmbWrapper, BorderLayout.SOUTH);

        form.add(fila2);

        panelPrincipal.add(form, BorderLayout.NORTH);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelBoton.setOpaque(false);

        btnVolver = new JButton("Volver");
        btnCrear = new JButton("Crear");

        btnCrear.setBackground(NAVY_BTN);
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCrear.setFocusPainted(false);
        btnCrear.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        btnCrear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCrear.addActionListener(e -> crearCompra());

        btnVolver.setBackground(NAVY_BTN);
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnVolver.setFocusPainted(false);
        btnVolver.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> dispose());

        panelBoton.add(btnVolver);
        panelBoton.add(btnCrear);

        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);
        
        return panelPrincipal;
    }

    private JPanel crearCampoIdOrden(String label) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl.setForeground(AZUL_LABEL);

        txtIdCompra = new JTextField();
        txtIdCompra.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtIdCompra.setPreferredSize(new Dimension(0, 40));
        txtIdCompra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        txtIdCompra.setEditable(false);
        txtIdCompra.setBackground(new Color(245, 245, 245));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(txtIdCompra, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearCampoFechaActual(String label) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl.setForeground(AZUL_LABEL);

        txtFechaEmision = new JTextField();
        txtFechaEmision.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtFechaEmision.setPreferredSize(new Dimension(0, 40));
        txtFechaEmision.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        txtFechaEmision.setEditable(false);
        txtFechaEmision.setBackground(new Color(245, 245, 245));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(txtFechaEmision, BorderLayout.CENTER);

        return panel;
    }

    private void cargarDatosIniciales() {
        String nuevoId = Compras.obtenerSiguienteIdCompra();
        txtIdCompra.setText(nuevoId);

        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        txtFechaEmision.setText(hoy.format(formatter));

        cargarProveedores();
    }

    private void cargarProveedores() {
        try {
            List<Proveedores> proveedores = Proveedores.obtenerProveedores();

            cmbProveedor.removeAllItems();
            cmbProveedor.addItem(new ProveedorItem("", "-- Seleccione un proveedor --"));

            for (Proveedores prov : proveedores) {
                ProveedorItem item = new ProveedorItem(
                        prov.getIdProveedor(),
                        prov.getPrvNombre()
                );
                cmbProveedor.addItem(item);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo completar la operación. Intente de nuevo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearCompra() {
        ProveedorItem proveedorSeleccionado = (ProveedorItem) cmbProveedor.getSelectedItem();

        if (proveedorSeleccionado == null || proveedorSeleccionado.getId().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un proveedor",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Compras nuevaCompra = new Compras();
        nuevaCompra.setIdCompra(txtIdCompra.getText().trim());
        nuevaCompra.setIdProveedor(proveedorSeleccionado.getId());
        nuevaCompra.setOcFechaHora(LocalDate.now());
        nuevaCompra.setOcFechaPronto(LocalDate.now());
        nuevaCompra.setOcSubtotal(0.0);
        nuevaCompra.setOcIva(0.0);
        nuevaCompra.setOcTotal(0.0);
        nuevaCompra.setOcSaldo(0.0);
        nuevaCompra.setEstadoOc("ACT");
        nuevaCompra.setOcPorDescPronto(0.0);

        try {
            boolean exito = Compras.grabarCompra(nuevaCompra);

            if (exito) {
                compraCreada = true;

                JOptionPane.showMessageDialog(this,
                        "Registro creado correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                parentGUI.recargarDatosCompras();

                dispose();
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

    public boolean isCompraCreada() {
        return compraCreada;
    }
}