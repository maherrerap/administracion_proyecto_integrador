package administracion_proyecto_integrador.gui.Compras;

import administracion_proyecto_integrador.dp.Inventarios.Productos;
import administracion_proyecto_integrador.dp.Compras.Pro_x_Oc;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class SeleccionProductoCompraGUI extends JDialog {

    private JLabel lblCantidad;
    private JComboBox<ProductoItem> cmbProductos;
    private JTextField txtPrecioCompra;
    private JTextField txtCantidad;
    private JButton btnAnadir;
    private JButton btnCancelar;

    private boolean productoAgregado = false;
    private String idCompra;
    private DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00");

    // ===================== ITEM PRODUCTO =====================
    private class ProductoItem {
        private String id;
        private String nombre;
        private double precioCompra;
        private String unidadMedida;

        public ProductoItem(String id, String nombre, double precioCompra, String unidadMedida) {
            this.id = id;
            this.nombre = nombre;
            this.precioCompra = precioCompra;
            this.unidadMedida = unidadMedida;
        }

        public String getId() { return id; }
        public double getPrecioCompra() { return precioCompra; }
        public String getUnidadMedida() { return unidadMedida; }

        @Override
        public String toString() {
            return nombre;
        }
    }

    // ===================== CONSTRUCTOR =====================
    public SeleccionProductoCompraGUI(Frame parent, String idCompra) {
        super(parent, "Selección de Producto (Orden de Compra)", true);
        this.idCompra = idCompra;
        initComponents();
        cargarProductos();
    }

    // ===================== UI =====================
    private void initComponents() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Selección de Producto");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Producto
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Seleccione el producto"), gbc);

        gbc.gridy = 1;
        cmbProductos = new JComboBox<>();
        cmbProductos.setPreferredSize(new Dimension(0, 35));
        cmbProductos.addActionListener(e -> onProductoSeleccionado());
        panelFormulario.add(cmbProductos, gbc);

        // Precio compra
        gbc.gridy = 2;
        panelFormulario.add(new JLabel("Precio de Compra"), gbc);

        gbc.gridy = 3;
        txtPrecioCompra = new JTextField("$.....");
        txtPrecioCompra.setEditable(false);
        txtPrecioCompra.setBackground(new Color(240, 240, 240));
        panelFormulario.add(txtPrecioCompra, gbc);

        // Cantidad
        gbc.gridy = 4;
        lblCantidad = new JLabel("Cantidad a comprar");
        panelFormulario.add(lblCantidad, gbc);

        gbc.gridy = 5;
        txtCantidad = new JTextField();
        txtCantidad.setPreferredSize(new Dimension(0, 35));
        txtCantidad.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validarFormulario(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validarFormulario(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validarFormulario(); }
        });
        panelFormulario.add(txtCantidad, gbc);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);

        btnCancelar = new JButton("Volver");
        btnCancelar.addActionListener(e -> dispose());

        btnAnadir = new JButton("Añadir");
        btnAnadir.setEnabled(false);
        btnAnadir.setBackground(new Color(44, 62, 80));
        btnAnadir.setForeground(Color.WHITE);
        btnAnadir.addActionListener(e -> agregarProducto());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnAnadir);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        add(panelPrincipal);
    }

    // ===================== CARGAR PRODUCTOS =====================
    private void cargarProductos() {
        try {
            List<Productos> productos = Productos.obtenerProductos();

            cmbProductos.removeAllItems();
            cmbProductos.addItem(new ProductoItem("", "Seleccione el producto", 0.0, ""));

            for (Productos prod : productos) {
                ProductoItem item = new ProductoItem(
                        prod.getIdProducto(),
                        prod.getProDescripcion(),
                        prod.getProPrecioCompra(),
                        prod.getProUmCompraDescripcion() != null
                                ? prod.getProUmCompraDescripcion()
                                : "Unidad"
                );
                cmbProductos.addItem(item);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar productos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onProductoSeleccionado() {
        ProductoItem sel = (ProductoItem) cmbProductos.getSelectedItem();

        if (sel != null && !sel.getId().isEmpty()) {
            txtPrecioCompra.setText("$ " + formatoDecimal.format(sel.getPrecioCompra()));
            lblCantidad.setText("Cantidad a comprar (" + sel.getUnidadMedida() + ")");
            txtCantidad.setEnabled(true);
            txtCantidad.requestFocus();
        } else {
            txtPrecioCompra.setText("$.....");
            lblCantidad.setText("Cantidad a comprar");
            txtCantidad.setText("");
            txtCantidad.setEnabled(false);
        }

        validarFormulario();
    }

    private void validarFormulario() {
        ProductoItem sel = (ProductoItem) cmbProductos.getSelectedItem();

        if (sel == null || sel.getId().isEmpty()) {
            btnAnadir.setEnabled(false);
            return;
        }

        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            btnAnadir.setEnabled(cantidad > 0);
        } catch (Exception e) {
            btnAnadir.setEnabled(false);
        }
    }

    // ===================== AGREGAR =====================
    private void agregarProducto() {
        ProductoItem sel = (ProductoItem) cmbProductos.getSelectedItem();

        if (sel == null || sel.getId().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un producto",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Cantidad inválida",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean exito = Pro_x_Oc.agregarProducto(idCompra, sel.getId(), cantidad);

            if (exito) {
                productoAgregado = true;
                JOptionPane.showMessageDialog(this,
                        "Producto agregado a la orden de compra",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo agregar el producto",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al agregar producto: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isProductoAgregado() {
        return productoAgregado;
    }
}
