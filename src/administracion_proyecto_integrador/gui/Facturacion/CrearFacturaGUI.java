package administracion_proyecto_integrador.gui.Facturacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import administracion_proyecto_integrador.dp.Facturacion.Facturas;
import administracion_proyecto_integrador.dp.Facturacion.Clientes;
import administracion_proyecto_integrador.dp.Facturacion.Facturas.ErrorValidacion;
import java.time.LocalDate;
import java.util.List;

public class CrearFacturaGUI extends JFrame {

    // Colores Estandar en la Aplicación
    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color AZUL_LABEL = new Color(30, 86, 198);

    // Componentes
    private JTextField txtNumFactura;
    private JTextField txtFechaEmision;
    private JComboBox<String> cmbClientes;
    private JTextArea txtDescripcion;

    public CrearFacturaGUI() {
        setTitle("Crear Factura");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // MAXIMIZAR LA VENTANA AL INICIAR
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Panel principal
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

        // Título con barra lateral
        content.add(crearTitulo(), BorderLayout.NORTH);

        // Formulario
        content.add(crearFormulario(), BorderLayout.CENTER);
        
        // Cargar el ID automático
        cargarSiguienteIdFactura();
    }

    private JComponent crearTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Barra azul vertical
        JPanel barra = new JPanel();
        barra.setBackground(AZUL_LABEL);
        barra.setPreferredSize(new Dimension(5, 35));

        JLabel titulo = new JLabel(" Crear Factura");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(new Color(20, 20, 20));

        panel.add(barra);
        panel.add(titulo);

        return panel;
    }

    private JComponent crearFormulario() {
        // Panel con borde gris
        JPanel panelBorde = new JPanel(new BorderLayout());
        panelBorde.setBackground(Color.WHITE);
        panelBorde.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            new EmptyBorder(30, 40, 30, 40)
        ));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);

        // Fila 1: N° Factura y Fecha de Emisión
        JPanel fila1 = new JPanel(new GridLayout(1, 2, 40, 0));
        fila1.setOpaque(false);
        fila1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        fila1.add(crearCampoTexto("N.º Factura:", "txtNumFactura"));
        fila1.add(crearCampoFechaActual("Fecha de Emisión:"));

        form.add(fila1);
        form.add(Box.createVerticalStrut(25));

        // Fila 2: Seleccione el cliente
        JPanel fila2 = new JPanel(new BorderLayout());
        fila2.setOpaque(false);
        fila2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblCliente = new JLabel("Seleccione el cliente");
        lblCliente.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblCliente.setForeground(new Color(60, 60, 60));

        cmbClientes = new JComboBox<>();
        cmbClientes.addItem("Seleccione el cliente");
        cmbClientes.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbClientes.setPreferredSize(new Dimension(0, 40));
        cmbClientes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Cargar clientes desde BD pasando por DP
        cargarClientes();

        fila2.add(lblCliente, BorderLayout.NORTH);
        fila2.add(Box.createVerticalStrut(8), BorderLayout.CENTER);
        
        JPanel cmbWrapper = new JPanel(new BorderLayout());
        cmbWrapper.setOpaque(false);
        cmbWrapper.add(cmbClientes, BorderLayout.NORTH);
        fila2.add(cmbWrapper, BorderLayout.SOUTH);

        form.add(fila2);
        form.add(Box.createVerticalStrut(25));

        // Fila 3: Descripción
        JPanel fila3 = new JPanel(new BorderLayout());
        fila3.setOpaque(false);

        JLabel lblDescripcion = new JLabel("Descripción de la factura");
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(60, 60, 60));

        txtDescripcion = new JTextArea(6, 40);
        txtDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));

        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

        fila3.add(lblDescripcion, BorderLayout.NORTH);
        fila3.add(Box.createVerticalStrut(8));
        
        JPanel descWrapper = new JPanel(new BorderLayout());
        descWrapper.setOpaque(false);
        descWrapper.add(scrollDesc, BorderLayout.CENTER);
        fila3.add(descWrapper, BorderLayout.CENTER);

        form.add(fila3);
        form.add(Box.createVerticalGlue());

        // Botón Crear
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        panelBoton.setOpaque(false);

        JButton btnSalir = new JButton("Volver");
        JButton btnCrear = new JButton("Crear");
        
        btnCrear.setBackground(NAVY_BTN);
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCrear.setFocusPainted(false);
        btnCrear.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        btnCrear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCrear.addActionListener(e -> crearFactura());
        
        btnSalir.setBackground(NAVY_BTN);
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSalir.setFocusPainted(false);
        btnSalir.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Operación Cancelada.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );

            new FacturasGUI().setVisible(true);
            dispose();
        });


        panelBoton.add(btnSalir);
        panelBoton.add(btnCrear);
        form.add(panelBoton);

        panelBorde.add(form, BorderLayout.CENTER);
        return panelBorde;
    }

    private JPanel crearCampoTexto(String label, String nombre) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl.setForeground(AZUL_LABEL);

        txtNumFactura = new JTextField();
        txtNumFactura.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtNumFactura.setPreferredSize(new Dimension(0, 40));
        txtNumFactura.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        txtNumFactura.setEditable(false);
        txtNumFactura.setBackground(new Color(245, 245, 245));
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(txtNumFactura, BorderLayout.CENTER);

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
        
        // Establecer fecha actual en formato DD/MM/YYYY
        LocalDate fechaActual = LocalDate.now();
        String fechaFormateada = String.format("%02d/%02d/%d", 
            fechaActual.getDayOfMonth(), 
            fechaActual.getMonthValue(), 
            fechaActual.getYear());
        txtFechaEmision.setText(fechaFormateada);

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(txtFechaEmision, BorderLayout.CENTER);

        return panel;
    }

    private void cargarSiguienteIdFactura() {
        try {
            String siguienteId = Facturas.obtenerSiguienteIdFactura();
            txtNumFactura.setText(siguienteId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "No se pudo completar la operación. Intente de nuevo.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            // En caso de error, establecer un valor por defecto
            txtNumFactura.setText("FAC0001");
        }
    }
    
    private void cargarClientes() {
        try {
            cmbClientes.removeAllItems();
            cmbClientes.addItem("Seleccione el cliente");
            
            List<Clientes> listaClientes = Clientes.obtenerClientesActivos();
            
            for (Clientes cliente : listaClientes) {
                String item = cliente.getIdCliente() + " - " + cliente.getCliNombre();
                cmbClientes.addItem(item);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "No se pudo completar la operación. Intente de nuevo.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            
            cmbClientes.removeAllItems();
            cmbClientes.addItem("Seleccione el cliente");
        }
    }

    private void crearFactura() {
        if (cmbClientes.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Debe seleccionar un cliente", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Debe ingresar una descripción", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Facturas nuevaFactura = new Facturas();
            nuevaFactura.setIdFactura(txtNumFactura.getText().trim());

            String clienteSeleccionado = (String) cmbClientes.getSelectedItem();
            String idCliente = clienteSeleccionado.split(" - ")[0].trim();
            nuevaFactura.setIdCliente(idCliente);

            nuevaFactura.setFacDescripcion(txtDescripcion.getText().trim());
            nuevaFactura.setFacFechaHora(LocalDate.now());
            nuevaFactura.setFacFechaPago(null);
            nuevaFactura.setEstadoFac("ACT");
            nuevaFactura.setFacSubtotal(0.0);
            nuevaFactura.setFacIva(0.0);
            nuevaFactura.setFacTotal(0.01);

            List<ErrorValidacion> errores = nuevaFactura.verificarFac();

            if (!errores.isEmpty()) {
                StringBuilder mensaje = new StringBuilder("Se encontraron los siguientes errores:\n\n");
                for (ErrorValidacion error : errores) {
                    mensaje.append("• ").append(error.getMensaje()).append("\n");
                }

                JOptionPane.showMessageDialog(this, 
                    mensaje.toString(), 
                    "Errores de validación", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean exito = Facturas.grabarFactura(nuevaFactura);

            if (exito) {
                JOptionPane.showMessageDialog(this, 
                    "Registro creado correctamente.", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                dispose();
                
                SwingUtilities.invokeLater(() -> new FacturasGUI().setVisible(true));

            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se pudo completar la operación. Intente de nuevo.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "No se pudo completar la operación. Intente de nuevo.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarFormulario() {
        txtNumFactura.setText("");
        cmbClientes.setSelectedIndex(0);
        txtDescripcion.setText("");
    }    
}