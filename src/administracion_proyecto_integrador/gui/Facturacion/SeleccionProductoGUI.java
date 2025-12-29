package administracion_proyecto_integrador.gui.Facturacion;

import administracion_proyecto_integrador.dp.Inventarios.Productos;
import administracion_proyecto_integrador.dp.Facturacion.Pro_x_Fac;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.event.DocumentListener;

public class SeleccionProductoGUI extends JDialog {
    private JLabel lblCantidad; 
    private JComboBox<ProductoItem> cmbProductos;
    private JTextField txtPrecioVenta;
    private JTextField txtCantidad;
    private JTextField txtSubtotal;
    private JButton btnAnadir;
    private JButton btnCancelar;
    
    private boolean productoAgregado = false;
    private String idFactura;
    private DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00");
    
    // Clase interna para manejar items del ComboBox
    private class ProductoItem {
        private String id;
        private String nombre;
        private double precio;
        private int stockDisponible;
        private String unidadMedida; // NUEVO
        

        public ProductoItem(String id, String nombre, double precio, int stockDisponible, String unidadMedida) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.stockDisponible = stockDisponible;
            this.unidadMedida = unidadMedida; // NUEVO
        }

        public String getId() { return id; }
        public double getPrecio() { return precio; }
        public int getStockDisponible() { return stockDisponible; }
        public String getUnidadMedida() { return unidadMedida; } // NUEVO

        @Override
        public String toString() {
            return nombre + (stockDisponible > 0 ? " (Stock: " + stockDisponible + ")" : " (Sin stock)");
        }
    }
    
    public SeleccionProductoGUI(Frame parent, String idFactura) {
        
        super(parent, "Selección de Producto", true);
        this.idFactura = idFactura;
        initComponents();
        cargarProductos();
    }
    
    private void initComponents() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(Color.WHITE);
        
        // Título
        JLabel lblTitulo = new JLabel("Selección de Producto");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        
        // Panel central con formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Seleccione el producto
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
        
        // Precio de Venta de Producto
        gbc.gridy = 2; gbc.weightx = 0;
        JLabel lblPrecio = new JLabel("Precio de Venta de Producto");
        lblPrecio.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(lblPrecio, gbc);
        
        gbc.gridy = 3; gbc.weightx = 1;
        txtPrecioVenta = new JTextField("$.....");
        txtPrecioVenta.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPrecioVenta.setPreferredSize(new Dimension(0, 35));
        txtPrecioVenta.setEditable(false);
        txtPrecioVenta.setBackground(new Color(240, 240, 240));
        panelFormulario.add(txtPrecioVenta, gbc);
        
        // Cantidad a comprar - AHORA ES UN ATRIBUTO DE CLASE
        gbc.gridy = 4; gbc.weightx = 0;
        lblCantidad = new JLabel("Cantidad a comprar"); // Sin "JLabel" porque ahora es atributo
        lblCantidad.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(lblCantidad, gbc);
        
        gbc.gridy = 5; gbc.weightx = 1;
        txtCantidad = new JTextField();
        txtCantidad.setFont(new Font("Arial", Font.PLAIN, 14));
        txtCantidad.setPreferredSize(new Dimension(0, 35));
        txtCantidad.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                calcularSubtotal();
                validarFormulario();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calcularSubtotal();
                validarFormulario();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calcularSubtotal();
                validarFormulario();
            }
        });
        panelFormulario.add(txtCantidad, gbc);
        
        // Subtotal calculado
        gbc.gridy = 6; gbc.weightx = 0;
        JLabel lblSubtotal = new JLabel("Subtotal calculado");
        lblSubtotal.setFont(new Font("Arial", Font.PLAIN, 14));
        panelFormulario.add(lblSubtotal, gbc);
        
        gbc.gridy = 7; gbc.weightx = 1;
        txtSubtotal = new JTextField("$.....");
        txtSubtotal.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSubtotal.setPreferredSize(new Dimension(0, 35));
        txtSubtotal.setEditable(false);
        txtSubtotal.setBackground(new Color(240, 240, 240));
        panelFormulario.add(txtSubtotal, gbc);
        
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        
        btnCancelar = new JButton("Cancelar");
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
        btnAnadir.setEnabled(false); // Inicialmente deshabilitado
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
            cmbProductos.addItem(new ProductoItem("", "Seleccione el producto", 0.0, 0, ""));

            for (Productos prod : productos) {
                // Solo agregar productos con stock disponible
                if (prod.getProSaldoFinal() > 0) {
                    ProductoItem item = new ProductoItem(
                        prod.getIdProducto(),
                        prod.getProDescripcion(),
                        prod.getProPrecioVenta(),
                        prod.getProSaldoFinal(),
                        prod.getProUmVentaDescripcion() != null ? prod.getProUmVentaDescripcion() : "Unidad" // NUEVO
                    );
                    cmbProductos.addItem(item);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar los productos: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void onProductoSeleccionado() {
        ProductoItem selected = (ProductoItem) cmbProductos.getSelectedItem();

        if (selected != null && !selected.getId().isEmpty()) {
            txtPrecioVenta.setText("$ " + formatoDecimal.format(selected.getPrecio()));
            txtCantidad.setText("");
            txtCantidad.setEnabled(true);
            txtCantidad.requestFocus();

            // Actualizar el label con la unidad de medida
            String unidad = selected.getUnidadMedida();
            lblCantidad.setText("Cantidad a comprar (" + unidad + ")"); // NUEVO
        } else {
            txtPrecioVenta.setText("$.....");
            txtSubtotal.setText("$.....");
            txtCantidad.setText("");
            txtCantidad.setEnabled(false);
            lblCantidad.setText("Cantidad a comprar"); // Volver al texto original
        }

        calcularSubtotal();
        validarFormulario();
    }
    
    private void calcularSubtotal() {
        ProductoItem selected = (ProductoItem) cmbProductos.getSelectedItem();
        
        if (selected == null || selected.getId().isEmpty()) {
            txtSubtotal.setText("$.....");
            return;
        }
        
        try {
            String cantidadStr = txtCantidad.getText().trim();
            
            if (cantidadStr.isEmpty()) {
                txtSubtotal.setText("$.....");
                return;
            }
            
            int cantidad = Integer.parseInt(cantidadStr);
            
            if (cantidad <= 0) {
                txtSubtotal.setText("$.....");
                return;
            }
            
            double subtotal = selected.getPrecio() * cantidad;
            txtSubtotal.setText("$ " + formatoDecimal.format(subtotal));
            
        } catch (NumberFormatException e) {
            txtSubtotal.setText("$.....");
        }
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
            btnAnadir.setEnabled(cantidad > 0 && cantidad <= selected.getStockDisponible());
        } catch (NumberFormatException e) {
            btnAnadir.setEnabled(false);
        }
    }
    

    private void agregarProducto() {
        // Validar que se haya seleccionado un producto
        ProductoItem selected = (ProductoItem) cmbProductos.getSelectedItem();

        if (selected == null || selected.getId().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un producto",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar cantidad
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

        // ============================================================
        // NUEVA VALIDACIÓN: Obtener stock REAL directamente de la BD
        // ============================================================
        try {
            // 1. Obtener el stock ACTUAL del producto (sin considerar esta factura)
            Productos producto = Productos.obtenerProductoPorId(selected.getId());
            if (producto == null) {
                JOptionPane.showMessageDialog(this,
                    "No se pudo obtener información del producto",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int stockRealDisponible = producto.getProSaldoFinal();

            // 2. Obtener cuánto ya está en el detalle de ESTA factura
            Pro_x_Fac existente = Pro_x_Fac.obtenerDetalle(idFactura, selected.getId());
            int cantidadYaEnDetalle = (existente != null) ? existente.getPxfCantidad() : 0;

            // 3. Calcular el stock disponible para esta operación
            // Stock disponible = Stock actual + lo que ya tengo en el detalle
            int stockDisponibleParaEstaFactura = stockRealDisponible + cantidadYaEnDetalle;

            // 4. Calcular el total que tendría después de agregar
            int cantidadTotalDespuesDeAgregar = cantidadYaEnDetalle + cantidad;

            // 5. Validar
            if (cantidadTotalDespuesDeAgregar > stockDisponibleParaEstaFactura) {
                String mensaje = cantidadYaEnDetalle > 0 
                    ? "Ya tiene " + cantidadYaEnDetalle + " unidades en el detalle.\n" +
                      "Stock disponible total: " + stockDisponibleParaEstaFactura + "\n" +
                      "Cantidad total después de agregar: " + cantidadTotalDespuesDeAgregar + "\n" +
                      "No puede agregar " + cantidad + " unidades más."
                    : "La cantidad solicitada (" + cantidad + ") excede el stock disponible (" + 
                      stockDisponibleParaEstaFactura + ")";

                JOptionPane.showMessageDialog(this,
                    mensaje,
                    "Stock Insuficiente",
                    JOptionPane.WARNING_MESSAGE);
                txtCantidad.requestFocus();
                txtCantidad.selectAll();
                return;
            }

            // Debug info (opcional - remover en producción)
            System.out.println("=== DEBUG STOCK ===");
            System.out.println("Stock real en BD: " + stockRealDisponible);
            System.out.println("Ya en detalle: " + cantidadYaEnDetalle);
            System.out.println("Stock disponible para esta factura: " + stockDisponibleParaEstaFactura);
            System.out.println("Intentando agregar: " + cantidad);
            System.out.println("Total después: " + cantidadTotalDespuesDeAgregar);
            System.out.println("==================");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al validar stock: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Intentar agregar el producto
        try {
            boolean exito = Pro_x_Fac.agregarProducto(idFactura, selected.getId(), cantidad);

            if (exito) {
                productoAgregado = true;
                JOptionPane.showMessageDialog(this,
                    "Detalle Agregado Correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo agregar el producto. Verifique que la factura esté en estado 'pendiente'.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al agregar el producto: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isProductoAgregado() {
        return productoAgregado;
    }
}