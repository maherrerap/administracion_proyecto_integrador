package administracion_proyecto_integrador.gui.Compras;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import administracion_proyecto_integrador.dp.Compras.Proveedores;

public class ConsultaProveedoresGUI extends JDialog {

    // Colores Estandar en la Aplicación
    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color AZUL_LABEL = new Color(30, 86, 198);
    private static final Color AZUL_OSCURO_LABELS = new Color(8, 26, 43);
    
    // Componentes principales
    private JComboBox<String> cmbIdProveedor;
    private JComboBox<String> cmbNombre;
    private JTextField txtRucCed;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtCelular;
    private JTextField txtPais;
    
    private ProveedoresGUI ventanaPadre;
    private List<Proveedores> listaProveedores;
    
    public ConsultaProveedoresGUI(ProveedoresGUI padre) {
        super(padre, "Consulta de Proveedores por Parámetros", true);
        this.ventanaPadre = padre;
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(padre);
        setResizable(false);
        
        // Cargar lista de proveedores para los ComboBox
        cargarProveedores();
        
        // Panel principal
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        // Contenido
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(20, 30, 20, 30));
        root.add(content, BorderLayout.CENTER);

        // Título
        content.add(crearTitulo(), BorderLayout.NORTH);

        // Formulario de búsqueda
        content.add(crearFormulario(), BorderLayout.CENTER);

        // Botones
        content.add(crearBotones(), BorderLayout.SOUTH);
    }

    private void cargarProveedores() {
        try {
            listaProveedores = Proveedores.obtenerProveedores();
        } catch (Exception e) {
            listaProveedores = new java.util.ArrayList<>();
            System.err.println("No se pudo completar la operación. Intente de nuevo.");
        }
    }

    private JComponent crearTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);

        JPanel barra = new JPanel();
        barra.setBackground(AZUL_LABEL);
        barra.setPreferredSize(new Dimension(5, 26));

        JLabel titulo = new JLabel(" Búsqueda de Proveedores");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(new Color(20, 20, 20));

        panel.add(barra);
        panel.add(titulo);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(panel, BorderLayout.WEST);
        wrapper.add(Box.createVerticalStrut(15), BorderLayout.SOUTH);

        return wrapper;
    }

    private JComponent crearFormulario() {
        JPanel panelConBorde = new JPanel(new BorderLayout());
        panelConBorde.setBackground(Color.WHITE);
        panelConBorde.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 25, 20, 25)
        ));
        
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        // Fila 1: ID Proveedor (ComboBox)
        JPanel fila1 = new JPanel(new GridLayout(1, 1, 15, 0));
        fila1.setOpaque(false);
        fila1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        cmbIdProveedor = crearComboBoxProveedores(true); // true = por ID
        fila1.add(crearCampoCombo("ID Proveedor:", cmbIdProveedor));

        // Fila 2: Nombre (ComboBox)
        JPanel fila2 = new JPanel(new GridLayout(1, 1, 15, 0));
        fila2.setOpaque(false);
        fila2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        cmbNombre = crearComboBoxProveedores(false); // false = por nombre
        fila2.add(crearCampoCombo("Proveedor:", cmbNombre));

        // Fila 3: RUC/Cédula y Teléfono
        JPanel fila3 = new JPanel(new GridLayout(1, 2, 15, 0));
        fila3.setOpaque(false);
        fila3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        txtRucCed = new JTextField();
        txtTelefono = new JTextField();
        
        fila3.add(crearCampo("Cédula/RUC:", txtRucCed));
        fila3.add(crearCampo("Teléfono:", txtTelefono));

        // Fila 4: Correo y Celular
        JPanel fila4 = new JPanel(new GridLayout(1, 2, 15, 0));
        fila4.setOpaque(false);
        fila4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        txtCorreo = new JTextField();
        txtCelular = new JTextField();
        
        fila4.add(crearCampo("Correo:", txtCorreo));
        fila4.add(crearCampo("Celular:", txtCelular));

        // Fila 5: País
        JPanel fila5 = new JPanel(new GridLayout(1, 2, 15, 0));
        fila5.setOpaque(false);
        fila5.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        txtPais = new JTextField();
        
        fila5.add(crearCampo("País:", txtPais));
        fila5.add(new JPanel() {{ setOpaque(false); }}); // Panel vacío

        form.add(fila1);
        form.add(Box.createVerticalStrut(8));
        form.add(fila2);
        form.add(Box.createVerticalStrut(8));
        form.add(fila3);
        form.add(Box.createVerticalStrut(8));
        form.add(fila4);
        form.add(Box.createVerticalStrut(8));
        form.add(fila5);
        
        panelConBorde.add(form, BorderLayout.NORTH);
        
        return panelConBorde;
    }

    private JComboBox<String> crearComboBoxProveedores(boolean porId) {
        JComboBox<String> combo = new JComboBox<>();
        combo.addItem(""); // Opción vacía

        for (Proveedores proveedor : listaProveedores) {
            String item = porId ? proveedor.getIdProveedor() : proveedor.getPrvNombre();
            combo.addItem(item);
        }

        combo.setEditable(false);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 12));

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return label;
            }
        });

        return combo;
    }

    private JPanel crearCampoCombo(String label, JComboBox<String> combo) {
        JPanel panel = new JPanel(new BorderLayout(3, 3));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(AZUL_OSCURO_LABELS);

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(combo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearCampo(String label, JTextField campo) {
        JPanel panel = new JPanel(new BorderLayout(3, 3));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(AZUL_OSCURO_LABELS);

        campo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);

        return panel;
    }

    private JComponent crearBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        panel.setOpaque(false);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnBuscar.setBackground(NAVY_BTN);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(e -> onBuscar());

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnLimpiar.setBackground(NAVY_BTN);
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnLimpiar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(e -> onLimpiar());

        JButton btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnVolver.setBackground(NAVY_BTN);
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> onVolver());

        panel.add(btnBuscar);
        panel.add(btnLimpiar);
        panel.add(btnVolver);

        return panel;
    }

    private void onBuscar() {
        // Obtener valores de los campos
        String idProveedor = obtenerTextoCombo(cmbIdProveedor);
        String nombre = obtenerTextoCombo(cmbNombre);
        String rucCed = txtRucCed.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String celular = txtCelular.getText().trim();
        String pais = txtPais.getText().trim();
        
        // Validar que al menos un campo tenga valor
        if (idProveedor.isEmpty() && nombre.isEmpty() && rucCed.isEmpty() && 
            telefono.isEmpty() && correo.isEmpty() && celular.isEmpty() && pais.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Debe ingresar al menos un criterio de búsqueda.",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        try {
            // Realizar búsqueda por parámetros
            List<Proveedores> resultados = Proveedores.obtenerProveedoresPorParametro(
                rucCed.isEmpty() ? null : rucCed,
                nombre.isEmpty() ? null : nombre,
                telefono.isEmpty() ? null : telefono,
                correo.isEmpty() ? null : correo,
                celular.isEmpty() ? null : celular,
                pais.isEmpty() ? null : pais
            );
            
            // Si se seleccionó un ID específico, filtrar por él
            if (!idProveedor.isEmpty()) {
                final String idBuscado = idProveedor;
                resultados.removeIf(p -> !p.getIdProveedor().equals(idBuscado));
            }
            
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "No se encontraron resultados para la búsqueda.",
                    "Sin resultados",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                ventanaPadre.actualizarTablaConResultados(resultados);
                
                JOptionPane.showMessageDialog(
                    this,
                    "Resultados encontrados.",
                    "Búsqueda exitosa",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                dispose();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo completar la operación. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String obtenerTextoCombo(JComboBox<String> combo) {
        Object selected = combo.getSelectedItem();
        if (selected == null) return "";
        return selected.toString().trim();
    }

    private void onLimpiar() {
        cmbIdProveedor.setSelectedIndex(0);
        cmbNombre.setSelectedIndex(0);
        txtRucCed.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtCelular.setText("");
        txtPais.setText("");
        
        cmbIdProveedor.requestFocus();
    }

    private void onVolver() {
        dispose();
    }
}