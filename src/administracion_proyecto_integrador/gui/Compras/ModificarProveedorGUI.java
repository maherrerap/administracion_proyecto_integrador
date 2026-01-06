package administracion_proyecto_integrador.gui.Compras;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import administracion_proyecto_integrador.dp.Compras.Proveedores;
import java.util.List;

public class ModificarProveedorGUI extends JFrame {
    
    // Colores Estandar en la Aplicación
    private static final Color NAVY = new Color(8, 26, 43);
    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color AZUL_LABEL = new Color(30, 86, 198);
    private static final Color AZUL_OSCURO_LABELS = new Color(8, 26, 43);
    private static final Color GRIS_BORDE = new Color(200, 200, 200);
    private static final Color GRIS_FONDO = new Color(245, 245, 245);
    
    // Componentes principales
    private JTextField txtIdProveedor;
    private JTextField txtNombre;
    private JTextField txtRucCed;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtCelular;
    private JTextField txtPais;
    private JTextField txtDireccion;
    private JComboBox<Proveedores.ComboItem> cmbCiudad;
    
    private String idProveedorOriginal;
    private Proveedores proveedorActual;
    
    public ModificarProveedorGUI(String idProveedor) {
        this.idProveedorOriginal = idProveedor;
        
        setTitle("Modificar Proveedor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(30, 50, 30, 50));
        root.add(content, BorderLayout.CENTER);

        content.add(crearTitulo(), BorderLayout.NORTH);
        content.add(crearFormulario(), BorderLayout.CENTER);
        content.add(crearBotones(), BorderLayout.SOUTH);
        
        cargarCiudades();
        cargarDatosProveedor();
        aplicarFiltrosNumericos();
    }

    private void abrirProveedoresGUI() {
        new ProveedoresGUI().setVisible(true);
        this.dispose();
    }

    private JComponent crearTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);

        JPanel barra = new JPanel();
        barra.setBackground(AZUL_LABEL);
        barra.setPreferredSize(new Dimension(5, 30));

        JLabel titulo = new JLabel(" Modificar Proveedor");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(new Color(20, 20, 20));

        panel.add(barra);
        panel.add(titulo);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(panel, BorderLayout.WEST);
        wrapper.add(Box.createVerticalStrut(20), BorderLayout.SOUTH);

        return wrapper;
    }

    private JComponent crearFormulario() {
        JPanel panelConBorde = new JPanel(new BorderLayout());
        panelConBorde.setBackground(Color.WHITE);
        panelConBorde.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRIS_BORDE, 1),
            new EmptyBorder(25, 35, 25, 35)
        ));
        
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        // Inicializar campos
        txtIdProveedor = new JTextField();
        txtIdProveedor.setEnabled(false);
        txtIdProveedor.setBackground(GRIS_FONDO);
        
        txtNombre = new JTextField();
        txtRucCed = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtCelular = new JTextField();
        txtPais = new JTextField();
        txtDireccion = new JTextField();
        
        cmbCiudad = new JComboBox<>();
        cmbCiudad.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel fila1 = new JPanel(new GridLayout(1, 2, 20, 0));
        fila1.setOpaque(false);
        fila1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        fila1.add(crearCampo("ID Proveedor:", txtIdProveedor, false));
        fila1.add(crearCampo("Nombre Proveedor:", txtNombre, true));

        JPanel fila2 = new JPanel(new GridLayout(1, 2, 20, 0));
        fila2.setOpaque(false);
        fila2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        fila2.add(crearCampo("Cédula/RUC:", txtRucCed, true));
        fila2.add(crearCampo("Teléfono:", txtTelefono, true));

        JPanel fila3 = new JPanel(new GridLayout(1, 2, 20, 0));
        fila3.setOpaque(false);
        fila3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        fila3.add(crearCampo("Correo:", txtCorreo, true));
        fila3.add(crearCampo("Celular:", txtCelular, false));

        JPanel fila4 = new JPanel(new GridLayout(1, 2, 20, 0));
        fila4.setOpaque(false);
        fila4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        fila4.add(crearCampo("País:", txtPais, true));
        fila4.add(new JPanel() {{ setOpaque(false); }}); // Panel vacío

        JPanel fila5 = new JPanel(new GridLayout(1, 1, 20, 0));
        fila5.setOpaque(false);
        fila5.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        fila5.add(crearCampo("Dirección:", txtDireccion, true));

        // Fila 6: Ciudad con ComboBox
        JPanel fila6 = new JPanel(new GridLayout(1, 2, 20, 0));
        fila6.setOpaque(false);
        fila6.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        JPanel panelCiudad = new JPanel(new BorderLayout(5, 5));
        panelCiudad.setOpaque(false);

        JLabel lblCiudad = new JLabel("Ciudad: *");
        lblCiudad.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblCiudad.setForeground(AZUL_OSCURO_LABELS);

        cmbCiudad.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRIS_BORDE),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        panelCiudad.add(lblCiudad, BorderLayout.NORTH);
        panelCiudad.add(cmbCiudad, BorderLayout.CENTER);

        fila6.add(panelCiudad);
        fila6.add(new JPanel() {{ setOpaque(false); }}); // Panel vacío

        form.add(fila1);
        form.add(Box.createVerticalStrut(10));
        form.add(fila2);
        form.add(Box.createVerticalStrut(10));
        form.add(fila3);
        form.add(Box.createVerticalStrut(10));
        form.add(fila4);
        form.add(Box.createVerticalStrut(10));
        form.add(fila5);
        form.add(Box.createVerticalStrut(10));
        form.add(fila6);
        
        panelConBorde.add(form, BorderLayout.NORTH);
        
        return panelConBorde;
    }

    private JPanel crearCampo(String label, JTextField campo, boolean obligatorio) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);

        String labelText = obligatorio ? label + " *" : label;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(AZUL_OSCURO_LABELS);

        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRIS_BORDE),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Aplica filtros numéricos a los campos de teléfono, celular y cédula/RUC
     */
    private void aplicarFiltrosNumericos() {
        // Teléfono: solo números, máximo 9 dígitos
        ((AbstractDocument) txtTelefono.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                    throws BadLocationException {
                if (string != null && string.matches("\\d+")) {
                    if ((fb.getDocument().getLength() + string.length()) <= 9) {
                        super.insertString(fb, offset, string, attr);
                    }
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                if (text != null && text.matches("\\d*")) {
                    int newLength = fb.getDocument().getLength() - length + text.length();
                    if (newLength <= 9) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            }
        });
        
        // Celular: solo números, máximo 10 dígitos
        ((AbstractDocument) txtCelular.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                    throws BadLocationException {
                if (string != null && string.matches("\\d+")) {
                    if ((fb.getDocument().getLength() + string.length()) <= 10) {
                        super.insertString(fb, offset, string, attr);
                    }
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                if (text != null && text.matches("\\d*")) {
                    int newLength = fb.getDocument().getLength() - length + text.length();
                    if (newLength <= 10) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            }
        });
        
        // Cédula/RUC: solo números, máximo 13 dígitos
        ((AbstractDocument) txtRucCed.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                    throws BadLocationException {
                if (string != null && string.matches("\\d+")) {
                    if ((fb.getDocument().getLength() + string.length()) <= 13) {
                        super.insertString(fb, offset, string, attr);
                    }
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                if (text != null && text.matches("\\d*")) {
                    int newLength = fb.getDocument().getLength() - length + text.length();
                    if (newLength <= 13) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            }
        });
    }

    private void cargarCiudades() {
        cmbCiudad.addItem(new Proveedores.ComboItem("", "Seleccione una ciudad"));
        try {
            List<Proveedores.ComboItem> ciudades = Proveedores.obtenerCiudades();
            for (Proveedores.ComboItem ciudad : ciudades) {
                cmbCiudad.addItem(ciudad);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "No se pudo completar la operación. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosProveedor() {
        try {
            List<Proveedores> proveedores = Proveedores.obtenerProveedores();
            
            for (Proveedores proveedor : proveedores) {
                if (proveedor.getIdProveedor().equals(idProveedorOriginal)) {
                    proveedorActual = proveedor;
                    break;
                }
            }
            
            if (proveedorActual == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el proveedor con ID: " + idProveedorOriginal,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                abrirProveedoresGUI();
                return;
            }
            
            txtIdProveedor.setText(proveedorActual.getIdProveedor());
            txtNombre.setText(proveedorActual.getPrvNombre());
            txtRucCed.setText(proveedorActual.getPrvRucCed());
            txtTelefono.setText(proveedorActual.getPrvTelefono());
            txtCorreo.setText(proveedorActual.getPrvMail());
            txtCelular.setText(proveedorActual.getPrvCelular());
            txtPais.setText(proveedorActual.getPrvPais());
            txtDireccion.setText(proveedorActual.getPrvDireccion());
            
            // Seleccionar la ciudad correspondiente en el ComboBox
            String idCiudadActual = proveedorActual.getIdCiudad();
            for (int i = 0; i < cmbCiudad.getItemCount(); i++) {
                Proveedores.ComboItem item = cmbCiudad.getItemAt(i);
                if (item.getId().equals(idCiudadActual)) {
                    cmbCiudad.setSelectedIndex(i);
                    break;
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo completar la operación. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            abrirProveedoresGUI();
        }
    }

    private JComponent crearBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setOpaque(false);

        JButton btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnVolver.setBackground(NAVY_BTN);
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> onVolver());

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnActualizar.setBackground(NAVY_BTN);
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnActualizar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnActualizar.addActionListener(e -> onActualizar());

        panel.add(btnVolver);
        panel.add(btnActualizar);

        return panel;
    }

    private void onVolver() {
        JOptionPane.showMessageDialog(
            this,
            "Operacion cancelada.",
            "Información",
            JOptionPane.INFORMATION_MESSAGE
        );
        abrirProveedoresGUI();
    }

    private void onActualizar() {
        String nombre = txtNombre.getText().trim();
        String rucCed = txtRucCed.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String celular = txtCelular.getText().trim();
        String pais = txtPais.getText().trim();
        String direccion = txtDireccion.getText().trim();
        
        Proveedores.ComboItem ciudadSeleccionada = (Proveedores.ComboItem) cmbCiudad.getSelectedItem();
        String idCiudad = ciudadSeleccionada != null ? ciudadSeleccionada.getId() : "";

        // Validar que se haya seleccionado una ciudad válida
        if (idCiudad == null || idCiudad.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Debe seleccionar una ciudad.",
                "Campo Obligatorio",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        Proveedores proveedorModificado = new Proveedores();
        proveedorModificado.setIdProveedor(idProveedorOriginal);
        proveedorModificado.setPrvNombre(nombre);
        proveedorModificado.setPrvRucCed(rucCed);
        proveedorModificado.setPrvTelefono(telefono);
        proveedorModificado.setPrvMail(correo);
        proveedorModificado.setPrvCelular(celular);
        proveedorModificado.setPrvPais(pais);
        proveedorModificado.setPrvDireccion(direccion);
        proveedorModificado.setEstadoPrv("ACT");
        proveedorModificado.setIdCiudad(idCiudad);

        List<Proveedores.ErrorValidacion> errores = proveedorModificado.verificarPrv();
        
        if (!errores.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("Se encontraron los siguientes errores:\n\n");
            for (Proveedores.ErrorValidacion error : errores) {
                mensaje.append("• ").append(error.getMensaje()).append("\n");
            }
            
            JOptionPane.showMessageDialog(
                this,
                mensaje.toString(),
                "Errores de Validación",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean exito = Proveedores.modificarProveedor(proveedorModificado);
        
        if (exito) {
            JOptionPane.showMessageDialog(
                this,
                "Registro modificado correctamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );
            abrirProveedoresGUI();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo completar la operación. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}