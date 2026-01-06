package administracion_proyecto_integrador.gui.Compras;

import administracion_proyecto_integrador.dp.Compras.Pro_x_Oc;
import administracion_proyecto_integrador.dp.Inventarios.Productos;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.*;

public class SeleccionProductoCompraGUI extends JDialog {
    
    // Componentes principales
    private JLabel lblCantidad; 
    private JComboBox<ProductoItem> cmbProductos;
    private JTextField txtValorCompra;
    private JTextField txtCantidad;
    private JButton btnAnadir;
    private JButton btnCancelar;
    
    private boolean productoAgregado = false;
    private String idCompra;
    private DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00");
    
    private class ProductoItem {
        private String id;
        private String nombre;
        private double valorCompra;
        private String unidadMedida; 
        

        public ProductoItem(String id, String nombre, double valorCompra, String unidadMedida) {
            this.id = id;
            this.nombre = nombre;
            this.valorCompra = valorCompra;
            this.unidadMedida = unidadMedida; 
        }

        public String getId() { return id; }
        public double getValorCompra() { return valorCompra; }
        public String getUnidadMedida() { return unidadMedida; } 

        @Override
        public String toString() {
            return nombre;
        }
    }
    
    public SeleccionProductoCompraGUI(Frame parent, String idCompra) {
        
        super(parent, "Selección de Producto", true);
        this.idCompra = idCompra;
        initComponents();
        cargarProductos();
    }
    
    private void initComponents() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("Selección de Producto");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel lblProducto = new JLabel("Seleccione el producto");
        lblProducto.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(lblProducto, gbc);
        
        gbc.gridy = 1; gbc.weightx = 1;
        cmbProductos = new JComboBox<>();
        cmbProductos.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbProductos.setPreferredSize(new Dimension(0, 35));
        cmbProductos.addActionListener(e -> onProductoSeleccionado());
        panelFormulario.add(cmbProductos, gbc);
        
        gbc.gridy = 2; gbc.weightx = 0;
        JLabel lblValor = new JLabel("Valor de Compra de Producto");
        lblValor.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(lblValor, gbc);
        
        gbc.gridy = 3; gbc.weightx = 1;
        txtValorCompra = new JTextField("$.....");
        txtValorCompra.setFont(new Font("Arial", Font.PLAIN, 14));
        txtValorCompra.setPreferredSize(new Dimension(0, 35));
        txtValorCompra.setEditable(false);
        txtValorCompra.setBackground(new Color(240, 240, 240));
        panelFormulario.add(txtValorCompra, gbc);
        
        gbc.gridy = 4; gbc.weightx = 0;
        lblCantidad = new JLabel("Cantidad a comprar"); 
        lblCantidad.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(lblCantidad, gbc);
        
        gbc.gridy = 5; gbc.weightx = 1;
        txtCantidad = new JTextField();
        txtCantidad.setFont(new Font("Arial", Font.PLAIN, 14));
        txtCantidad.setPreferredSize(new Dimension(0, 35));

        txtCantidad.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                validarFormulario();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                validarFormulario();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                validarFormulario();
            }
        });

        panelFormulario.add(txtCantidad, gbc);
        
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        
        btnCancelar = new JButton("Volver");
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCancelar.setBackground(new Color(200, 200, 200));
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());
        
        btnAnadir = new JButton("Añadir");
        btnAnadir.setPreferredSize(new Dimension(100, 35));
        btnAnadir.setFont(new Font("Arial", Font.BOLD, 14));
        btnAnadir.setBackground(new Color(44, 62, 80));
        btnAnadir.setForeground(Color.WHITE);
        btnAnadir.setFocusPainted(false);
        btnAnadir.setEnabled(false);
        btnAnadir.addActionListener(e -> agregarProducto());
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnAnadir);
        
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private void cargarProductos() {
        try {
            List<Productos> productos = Productos.obtenerProductos();

            cmbProductos.removeAllItems();
            cmbProductos.addItem(new ProductoItem("", "Seleccione el producto", 0.0, ""));

            for (Productos prod : productos) {
                ProductoItem item = new ProductoItem(
                    prod.getIdProducto(),
                    prod.getProDescripcion(),
                    prod.getProValorCompra(),
                    prod.getProUmCompraDescripcion() != null ? prod.getProUmCompraDescripcion() : "Unidad" 
                );
                cmbProductos.addItem(item);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "No se pudo completar la operación. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void onProductoSeleccionado() {
        ProductoItem selected = (ProductoItem) cmbProductos.getSelectedItem();

        if (selected != null && !selected.getId().isEmpty()) {
            txtValorCompra.setText("$ " + formatoDecimal.format(selected.getValorCompra()));
            txtCantidad.setText("");
            txtCantidad.setEnabled(true);
            txtCantidad.requestFocus();

            String unidad = selected.getUnidadMedida();
            lblCantidad.setText("Cantidad a comprar (" + unidad + ")"); 
        } else {
            txtValorCompra.setText("$.....");
            txtCantidad.setText("");
            txtCantidad.setEnabled(false);
            lblCantidad.setText("Cantidad a comprar");
        }

        validarFormulario();
    }
    
    
    private void validarFormulario() {
        ProductoItem selected = (ProductoItem) cmbProductos.getSelectedItem();
        
        if (selected == null || selected.getId().isEmpty()) {
            btnAnadir.setEnabled(false);
            return;
        }
        
        String cantidadStr = txtCantidad.getText().trim();
        if (cantidadStr.isEmpty()) {
            btnAnadir.setEnabled(false);
            return;
        }
        
        try {
            int cantidad = Integer.parseInt(cantidadStr);
            btnAnadir.setEnabled(cantidad > 0);
        } catch (NumberFormatException e) {
            btnAnadir.setEnabled(false);
        }
    }
    

    private void agregarProducto() {
        ProductoItem selected = (ProductoItem) cmbProductos.getSelectedItem();

        if (selected == null || selected.getId().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un producto",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cantidadStr = txtCantidad.getText().trim();

        if (cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe ingresar una cantidad",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtCantidad.requestFocus();
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "La cantidad debe ser un número válido",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtCantidad.requestFocus();
            return;
        }

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this,
                "La cantidad debe ser mayor a 0",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtCantidad.requestFocus();
            return;
        }

        try {
            boolean exito = Pro_x_Oc.agregarProducto(idCompra, selected.getId(), cantidad);

            if (exito) {
                productoAgregado = true;
                JOptionPane.showMessageDialog(this,
                    "Detalle Agregado Correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
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
    
    public boolean isProductoAgregado() {
        return productoAgregado;
    }
}