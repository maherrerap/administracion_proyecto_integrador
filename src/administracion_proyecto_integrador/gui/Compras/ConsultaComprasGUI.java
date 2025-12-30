package administracion_proyecto_integrador.gui.Compras;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import administracion_proyecto_integrador.dp.Compras.Compras;
import administracion_proyecto_integrador.dp.Compras.Proveedores;

public class ConsultaComprasGUI extends JDialog {

    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color AZUL_LABEL = new Color(30, 86, 198);

    private JComboBox<CompraItem> cmbIdCompra;
    private JComboBox<ProveedorItem> cmbProveedor;
    private JComboBox<String> cmbEstado;

    private JButton btnBuscar;
    private JButton btnLimpiar;
    private JButton btnCancelar;

    private ComprasGUI parentGUI;

    // ===================== CLASES INTERNAS =====================

    private static class CompraItem {
        private String id;

        public CompraItem(String id) {
            this.id = id;
        }

        public String getId() { return id; }

        @Override
        public String toString() {
            return id;
        }
    }

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

    public ConsultaComprasGUI(ComprasGUI parent) {
        super(parent, "Consulta de Órdenes de Compra", true);
        this.parentGUI = parent;
        initComponents();
        cargarDatos();
    }

    // ===================== UI =====================

    private void initComponents() {
        setSize(600, 380);
        setLocationRelativeTo(parentGUI);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panelPrincipal.add(crearPanelTitulo(), BorderLayout.NORTH);
        panelPrincipal.add(crearPanelCampos(), BorderLayout.CENTER);
        panelPrincipal.add(crearPanelBotones(), BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel barra = new JPanel();
        barra.setBackground(AZUL_LABEL);
        barra.setPreferredSize(new Dimension(5, 30));

        JLabel lblTitulo = new JLabel(" Búsqueda de Órdenes de Compra");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        panel.add(barra);
        panel.add(lblTitulo);
        return panel;
    }

    private JPanel crearPanelCampos() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID Compra
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(crearLabel("ID Orden Compra:"), gbc);

        gbc.gridx = 1;
        cmbIdCompra = new JComboBox<>();
        panel.add(cmbIdCompra, gbc);

        // Proveedor
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(crearLabel("Proveedor:"), gbc);

        gbc.gridx = 1;
        cmbProveedor = new JComboBox<>();
        panel.add(cmbProveedor, gbc);

        // Estado
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(crearLabel("Estado:"), gbc);

        gbc.gridx = 1;
        cmbEstado = new JComboBox<>(new String[]{
                "", "ABI", "CER", "INA"
        });
        panel.add(cmbEstado, gbc);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        btnBuscar = crearBoton("Buscar");
        btnLimpiar = crearBoton("Limpiar");
        btnCancelar = crearBoton("Volver");

        btnBuscar.addActionListener(e -> realizarBusqueda());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnCancelar.addActionListener(e -> dispose());

        panel.add(btnBuscar);
        panel.add(btnLimpiar);
        panel.add(btnCancelar);

        return panel;
    }

    // ===================== COMPONENTES =====================

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(NAVY_BTN);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        return btn;
    }

    // ===================== LÓGICA =====================

    private void cargarDatos() {
        try {
            cmbIdCompra.removeAllItems();
            cmbIdCompra.addItem(new CompraItem(""));

            List<Compras> compras = Compras.obtenerCompras();
            for (Compras c : compras) {
                cmbIdCompra.addItem(new CompraItem(c.getIdCompra()));
            }

            cmbProveedor.removeAllItems();
            cmbProveedor.addItem(new ProveedorItem("", "-- Todos --"));

            List<Proveedores> proveedores = Proveedores.obtenerProveedoresActivos();
            for (Proveedores p : proveedores) {
                cmbProveedor.addItem(new ProveedorItem(p.getIdProveedor(), p.getProvNombre()));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarBusqueda() {
        try {
            CompraItem compraItem = (CompraItem) cmbIdCompra.getSelectedItem();
            String idCompra = (compraItem != null && !compraItem.getId().isEmpty())
                    ? compraItem.getId() : null;

            ProveedorItem proveedorItem = (ProveedorItem) cmbProveedor.getSelectedItem();
            String idProveedor = (proveedorItem != null && !proveedorItem.getId().isEmpty())
                    ? proveedorItem.getId() : null;

            String estado = cmbEstado.getSelectedItem().toString();
            if (estado.isEmpty()) estado = null;

            if (idCompra == null && idProveedor == null && estado == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar al menos un criterio de búsqueda",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Compras> resultados =
                    Compras.obtenerComprasPorParametro(idCompra, idProveedor, estado);

            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron resultados",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            parentGUI.actualizarTablaConResultados(resultados);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        cmbIdCompra.setSelectedIndex(0);
        cmbProveedor.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
    }
}