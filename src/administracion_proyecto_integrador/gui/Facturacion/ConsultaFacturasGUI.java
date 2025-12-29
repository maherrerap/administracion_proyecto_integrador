package administracion_proyecto_integrador.gui.Facturacion;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import administracion_proyecto_integrador.dp.Facturacion.Facturas;
import administracion_proyecto_integrador.dp.Facturacion.Clientes;

public class ConsultaFacturasGUI extends JDialog {
    
    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color AZUL_LABEL = new Color(30, 86, 198);
    
    private JComboBox<FacturaItem> cmbIdFactura;
    private JComboBox<ClienteItem> cmbCliente;
    private JTextField txtDescripcion;
    
    private JButton btnBuscar;
    private JButton btnLimpiar;
    private JButton btnCancelar;
    
    private FacturasGUI parentGUI;
    
    // Clase interna para items de factura
    private static class FacturaItem {
        private String id;
        
        public FacturaItem(String id) {
            this.id = id;
        }
        
        public String getId() { return id; }
        
        @Override
        public String toString() {
            return id;
        }
    }
    
    // Clase interna para items de cliente
    private static class ClienteItem {
        private String id;
        private String nombre;
        
        public ClienteItem(String id, String nombre) {
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
    
    public ConsultaFacturasGUI(FacturasGUI parent) {
        super(parent, "Consulta de Facturas por Parámetros", true);
        this.parentGUI = parent;
        initComponents();
        cargarDatos();
    }
    
    private void initComponents() {
        setSize(600, 400);
        setLocationRelativeTo(parentGUI);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Panel del título
        JPanel panelTitulo = crearPanelTitulo();
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        
        // Panel de campos
        JPanel panelCampos = crearPanelCampos();
        panelPrincipal.add(panelCampos, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Barra azul decorativa
        JPanel barra = new JPanel();
        barra.setBackground(AZUL_LABEL);
        barra.setPreferredSize(new Dimension(5, 30));
        
        JLabel lblTitulo = new JLabel(" Búsqueda de Facturas");
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
        
        // Fila 1: ID Factura
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panel.add(crearLabel("ID Factura:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cmbIdFactura = new JComboBox<>();
        cmbIdFactura.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbIdFactura.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        cmbIdFactura.setBackground(Color.WHITE);
        panel.add(cmbIdFactura, gbc);
        
        // Fila 2: Cliente
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        panel.add(crearLabel("Cliente:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cmbCliente = new JComboBox<>();
        cmbCliente.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbCliente.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        cmbCliente.setBackground(Color.WHITE);
        panel.add(cmbCliente, gbc);
        
        // Fila 3: Descripción
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        panel.add(crearLabel("Descripción:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtDescripcion = crearTextField();
        panel.add(txtDescripcion, gbc);
        
        return panel;
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
        btnCancelar.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Operación Cancelada.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
        });
        
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
            // Cargar facturas para el ComboBox
            cmbIdFactura.removeAllItems();
            cmbIdFactura.addItem(new FacturaItem("")); // Opción vacía
            
            List<Facturas> facturas = Facturas.obtenerFacturas();
            for (Facturas factura : facturas) {
                cmbIdFactura.addItem(new FacturaItem(factura.getIdFactura()));
            }
            
            // Cargar clientes para el ComboBox
            cmbCliente.removeAllItems();
            cmbCliente.addItem(new ClienteItem("", "-- Todos los clientes --")); // Opción vacía
            
            List<Clientes> clientes = Clientes.obtenerClientesActivos();
            for (Clientes cliente : clientes) {
                cmbCliente.addItem(new ClienteItem(cliente.getIdCliente(), cliente.getCliNombre()));
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar los datos: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void realizarBusqueda() {
        try {
            // Obtener valores de los campos
            FacturaItem facturaSeleccionada = (FacturaItem) cmbIdFactura.getSelectedItem();
            String idFactura = (facturaSeleccionada != null && !facturaSeleccionada.getId().isEmpty()) 
                               ? facturaSeleccionada.getId() 
                               : null;
            
            ClienteItem clienteSeleccionado = (ClienteItem) cmbCliente.getSelectedItem();
            String idCliente = (clienteSeleccionado != null && !clienteSeleccionado.getId().isEmpty()) 
                               ? clienteSeleccionado.getId() 
                               : null;
            
            String descripcion = txtDescripcion.getText().trim();
            if (descripcion.isEmpty()) {
                descripcion = null;
            }
            
            // Validar que al menos un campo esté lleno
            if (idFactura == null && idCliente == null && descripcion == null) {
                JOptionPane.showMessageDialog(this,
                    "Debe ingresar al menos un criterio de búsqueda",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Realizar la búsqueda
            List<Facturas> resultados = Facturas.obtenerFacturasPorParametro(
                idFactura, 
                idCliente, 
                descripcion
            );
            
            // Verificar si hay resultados
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No se encontraron resultados para la búsqueda",
                    "Sin resultados",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Actualizar la tabla en FacturasGUI
            parentGUI.actualizarTablaConResultados(resultados);
            
            // Cerrar el diálogo
            dispose();
            
            JOptionPane.showMessageDialog(parentGUI,
                "Resultados encontrados.",
                "Búsqueda exitosa",
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al realizar la búsqueda: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void limpiarCampos() {
        cmbIdFactura.setSelectedIndex(0);
        cmbCliente.setSelectedIndex(0);
        txtDescripcion.setText("");
    }
}