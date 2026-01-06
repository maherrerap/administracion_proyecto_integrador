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

public class NuevoProveedorGUI extends JFrame {
    
    // Colores Estandar en la Aplicación
    private static final Color NAVY = new Color(8, 26, 43);
    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color AZUL_LABEL = new Color(30, 86, 198);
    private static final Color GRIS_BORDE = new Color(200, 200, 200);
    private static final Color GRIS_FONDO = new Color(245, 245, 245);
    
    // Componentes principales
    private JLabel lblIdProveedorValor;
    private JTextField txtNombre;
    private JTextField txtRucCed;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtCelular;
    private JTextField txtPais;
    private JTextField txtDireccion;
    private JComboBox<Proveedores.ComboItem> cmbCiudad;
    
    public NuevoProveedorGUI() {
        setTitle("Nuevo Proveedor");
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
        
        cargarSiguienteId();
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
        barra.setPreferredSize(new Dimension(5, 35));

        JLabel titulo = new JLabel(" Nuevo Proveedor");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(new Color(20, 20, 20));

        panel.add(barra);
        panel.add(titulo);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(panel, BorderLayout.WEST);
        wrapper.add(Box.createVerticalStrut(25), BorderLayout.SOUTH);

        return wrapper;
    } 

    private JComponent crearFormulario() {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(Color.WHITE);
        contenedor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRIS_BORDE, 2),
            new EmptyBorder(20, 30, 20, 30) 
        ));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);
        
        // Inicializar campos
        lblIdProveedorValor = new JLabel();
        lblIdProveedorValor.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblIdProveedorValor.setForeground(new Color(80, 80, 80));
        
        txtNombre = new JTextField();
        txtRucCed = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtCelular = new JTextField();
        txtPais = new JTextField();
        txtDireccion = new JTextField();
        cmbCiudad = new JComboBox<>();
        cmbCiudad.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cargarCiudades();

        JPanel fila1 = new JPanel(new GridLayout(1, 2, 25, 0)); 
        fila1.setOpaque(false);
        fila1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); 
        fila1.add(crearCampoSoloLectura("ID Proveedor:", lblIdProveedorValor));
        fila1.add(crearCampo("Nombre Proveedor:", txtNombre, true));

        JPanel fila2 = new JPanel(new GridLayout(1, 2, 25, 0));
        fila2.setOpaque(false);
        fila2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        fila2.add(crearCampo("Cédula/RUC:", txtRucCed, true));
        fila2.add(crearCampo("Teléfono:", txtTelefono, true));

        JPanel fila3 = new JPanel(new GridLayout(1, 2, 25, 0));
        fila3.setOpaque(false);
        fila3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        fila3.add(crearCampo("Correo:", txtCorreo, true));
        fila3.add(crearCampo("Celular:", txtCelular, false));

        JPanel fila4 = new JPanel(new GridLayout(1, 2, 25, 0));
        fila4.setOpaque(false);
        fila4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        fila4.add(crearCampo("País:", txtPais, true));
        fila4.add(new JPanel() {{ setOpaque(false); }}); // Panel vacío

        JPanel fila5 = new JPanel(new BorderLayout());
        fila5.setOpaque(false);
        fila5.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        fila5.add(crearCampo("Dirección:", txtDireccion, true), BorderLayout.CENTER);

        // Fila 6: Ciudad con ComboBox
        JPanel fila6 = new JPanel(new GridLayout(1, 2, 25, 0));
        fila6.setOpaque(false);
        fila6.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel panelCiudad = new JPanel(new BorderLayout(0, 6));
        panelCiudad.setOpaque(false);

        JLabel lblCiudad = new JLabel("Ciudad:");
        lblCiudad.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblCiudad.setForeground(NAVY);

        cmbCiudad.setPreferredSize(new Dimension(cmbCiudad.getPreferredSize().width, 32));
        cmbCiudad.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRIS_BORDE, 1),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
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
        form.add(Box.createVerticalStrut(15)); 

        JScrollPane scrollPane = new JScrollPane(form);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        contenedor.add(scrollPane, BorderLayout.CENTER);
        contenedor.setPreferredSize(new Dimension(contenedor.getPreferredSize().width, 380));

        return contenedor;
    }

    private JPanel crearCampo(String label, JTextField campo, boolean obligatorio) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);

        String labelText = label;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(NAVY);

        campo.setFont(new Font("SansSerif", Font.PLAIN, 13)); 
        campo.setPreferredSize(new Dimension(campo.getPreferredSize().width, 32));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRIS_BORDE, 1),
            BorderFactory.createEmptyBorder(4, 10, 4, 10) 
        ));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearCampoSoloLectura(String label, JLabel campoValor) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(NAVY);

        JPanel panelValor = new JPanel(new BorderLayout());
        panelValor.setBackground(GRIS_FONDO);
        panelValor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRIS_BORDE, 1),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        panelValor.setPreferredSize(new Dimension(200, 32));

        campoValor.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campoValor.setForeground(new Color(80, 80, 80));
        panelValor.add(campoValor, BorderLayout.WEST);

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(panelValor, BorderLayout.CENTER);

        return panel;
    }

    private JComponent crearBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        panel.setOpaque(false);

        JButton btnCancelar = new JButton("Volver");
        btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancelar.setBackground(NAVY_BTN);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                this,
                "Operacion cancelada.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE
            );
            onCancelar();
        });

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setBackground(NAVY_BTN);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> onGuardar());

        panel.add(btnCancelar);
        panel.add(btnGuardar);

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
    
    private void cargarSiguienteId() {
        try {
            String siguienteId = Proveedores.obtenerSiguienteIdProveedor();

            if (siguienteId != null) {
                lblIdProveedorValor.setText(siguienteId);
            } else {
                lblIdProveedorValor.setText("PR00001");
                JOptionPane.showMessageDialog(this,
                    "No se pudo generar el ID automaticamente.\nSe asignara PR00001 por defecto.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            lblIdProveedorValor.setText("PR00001");
            JOptionPane.showMessageDialog(this,
                "No se pudo completar la operación. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancelar() {
        abrirProveedoresGUI();
    }

    private void onGuardar() {
        String idProveedor = lblIdProveedorValor.getText().trim();
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

        Proveedores nuevoProveedor = new Proveedores();
        nuevoProveedor.setIdProveedor(idProveedor);
        nuevoProveedor.setPrvNombre(nombre);
        nuevoProveedor.setPrvRucCed(rucCed);
        nuevoProveedor.setPrvTelefono(telefono);
        nuevoProveedor.setPrvMail(correo);
        nuevoProveedor.setPrvCelular(celular);
        nuevoProveedor.setPrvPais(pais);
        nuevoProveedor.setPrvDireccion(direccion);
        nuevoProveedor.setEstadoPrv("ACT");
        nuevoProveedor.setIdCiudad(idCiudad);

        List<Proveedores.ErrorValidacion> errores = nuevoProveedor.verificarPrv();
        
        if (!errores.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("Se encontraron los siguientes errores:\n\n");
            for (Proveedores.ErrorValidacion error : errores) {
                mensaje.append("• ").append(error.getMensaje()).append("\n");
            }
            
            JOptionPane.showMessageDialog(
                this,
                mensaje.toString(),
                "Errores de Validacion",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        boolean exito = Proveedores.grabarProveedor(nuevoProveedor);
        
        if (exito) {
            JOptionPane.showMessageDialog(
                this,
                "Registro creado correctamente.",
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