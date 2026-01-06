package administracion_proyecto_integrador.gui.Compras;

import administracion_proyecto_integrador.dp.Compras.Compras;
import administracion_proyecto_integrador.dp.Compras.Proveedores;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;

public class ConsultaComprasGUI extends JDialog {
    
    // Colores Estandar en la Aplicación
    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color AZUL_LABEL = new Color(30, 86, 198);
    
    // Componentes principales
    private JComboBox<CompraItem> cmbIdCompra;
    private JComboBox<ProveedorItem> cmbProveedor;
    private JTextField txtFechaEmision;
    
    private JButton btnBuscar;
    private JButton btnLimpiar;
    private JButton btnCancelar;
    
    private ComprasGUI parentGUI;
    
    private static class CompraItem {
        private String id;
        
        public CompraItem(String id) {
            this.id = id;
        }
        
        public String getId() { return id; }
        
        @Override
        public String toString() {
            return id.isEmpty() ? "-- Todas las órdenes --" : id;
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
        public String getNombre() { return nombre; }
        
        @Override
        public String toString() {
            return nombre;
        }
    }
    
    public ConsultaComprasGUI(ComprasGUI parent) {
        super(parent, "Consulta de Órdenes de Compra por Parámetros", true);
        this.parentGUI = parent;
        initComponents();
        cargarDatos();
    }
    
    private void initComponents() {
        setSize(600, 450);
        setLocationRelativeTo(parentGUI);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel panelTitulo = crearPanelTitulo();
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        
        JPanel panelCampos = crearPanelCampos();
        panelPrincipal.add(panelCampos, BorderLayout.CENTER);
        
        JPanel panelBotones = crearPanelBotones();
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
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
        lblTitulo.setForeground(new Color(20, 20, 20));
        
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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panel.add(crearLabel("ID Orden Compra:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cmbIdCompra = new JComboBox<>();
        cmbIdCompra.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbIdCompra.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        cmbIdCompra.setBackground(Color.WHITE);
        panel.add(cmbIdCompra, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        panel.add(crearLabel("Proveedor:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cmbProveedor = new JComboBox<>();
        cmbProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbProveedor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        cmbProveedor.setBackground(Color.WHITE);
        panel.add(cmbProveedor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        panel.add(crearLabel("Fecha de Emisión:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.7;
        txtFechaEmision = crearTextField();
        panel.add(txtFechaEmision, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.7;

        JLabel lblFormatoFecha = new JLabel("Formato: DD/MM/YYYY");
        lblFormatoFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFormatoFecha.setForeground(new Color(120, 120, 120));

        panel.add(lblFormatoFecha, gbc);
        
        return panel;
    }
    
    private JTextField crearTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        textField.setBackground(Color.WHITE);
        return textField;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        btnBuscar = crearBoton("Buscar", NAVY_BTN);
        btnLimpiar = crearBoton("Limpiar", NAVY_BTN);
        btnCancelar = crearBoton("Volver", NAVY_BTN);
        
        btnBuscar.addActionListener(e -> realizarBusqueda());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnCancelar.addActionListener(e -> dispose());
        
        panel.add(btnBuscar);
        panel.add(btnLimpiar);
        panel.add(btnCancelar);
        
        return panel;
    }
    
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void cargarDatos() {
        try {
            
            cmbIdCompra.removeAllItems();
            cmbIdCompra.addItem(new CompraItem(""));
            
            List<Compras> compras = Compras.obtenerCompras();
            for (Compras compra : compras) {
                cmbIdCompra.addItem(new CompraItem(compra.getIdCompra()));
            }

            cmbProveedor.removeAllItems();
            cmbProveedor.addItem(new ProveedorItem("", "-- Todos los proveedores --"));
            
            List<Proveedores> proveedores = Proveedores.obtenerProveedoresActivos();
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
    
    private void realizarBusqueda() {
        try {
            CompraItem compraSeleccionada = (CompraItem) cmbIdCompra.getSelectedItem();
            String idCompra = (compraSeleccionada != null && !compraSeleccionada.getId().isEmpty()) 
                               ? compraSeleccionada.getId() 
                               : null;

            ProveedorItem proveedorSeleccionado = (ProveedorItem) cmbProveedor.getSelectedItem();
            String idProveedor = (proveedorSeleccionado != null && !proveedorSeleccionado.getId().isEmpty()) 
                               ? proveedorSeleccionado.getId() 
                               : null;

            String fechaEmision = null;
            String fechaTexto = txtFechaEmision.getText().trim();
            if (!fechaTexto.isEmpty()) {
                if (!fechaTexto.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
                    JOptionPane.showMessageDialog(this,
                        "El formato de fecha debe ser DD/MM/YYYY",
                        "Formato incorrecto",
                        JOptionPane.WARNING_MESSAGE);
                    txtFechaEmision.requestFocus();
                    return;
                }

                DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateTimeFormatter formatoSalida = DateTimeFormatter.ISO_LOCAL_DATE;

                LocalDate fecha = LocalDate.parse(fechaTexto, formatoEntrada);
                fechaEmision = fecha.format(formatoSalida);
            }

            if (idCompra == null && idProveedor == null && fechaEmision == null) {
                JOptionPane.showMessageDialog(this,
                    "Debe ingresar al menos un criterio de búsqueda",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Compras> resultados = Compras.obtenerOrdenCompraPorParametro(
                idCompra, 
                idProveedor, 
                fechaEmision
            );

            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No se encontraron resultados para la búsqueda",
                    "Sin resultados",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            parentGUI.actualizarTablaConResultados(resultados);
            dispose();

            JOptionPane.showMessageDialog(parentGUI,
                "Se encontraron " + resultados.size() + " resultado(s).",
                "Búsqueda exitosa",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "No se pudo completar la operación. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        cmbIdCompra.setSelectedIndex(0);
        cmbProveedor.setSelectedIndex(0);
        txtFechaEmision.setText("");
    }
}