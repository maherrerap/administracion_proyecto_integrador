package administracion_proyecto_integrador.gui.Facturacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import administracion_proyecto_integrador.dp.Facturacion.Clientes;

public class ConsultaClientesGUI extends JDialog {

    // Colores Estandar en la Aplicación
    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color AZUL_LABEL = new Color(30, 86, 198);
    private static final Color AZUL_OSCURO_LABELS = new Color(8, 26, 43);
    
    // Componentes principales
    private JComboBox<String> cmbIdCliente;
    private JComboBox<String> cmbNombre;
    private JTextField txtRucCed;
    private JTextField txtCorreo;
    private JTextField txtCelular;
    
    private ClientesGUI ventanaPadre;
    private List<Clientes> listaClientes;
    
    public ConsultaClientesGUI(ClientesGUI padre) {
        super(padre, "Consulta de Clientes por Parámetros", true);
        this.ventanaPadre = padre;
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(700, 480);
        setLocationRelativeTo(padre);
        setResizable(false);
        
        //Cargar lista de clientes para los ComboBox
        cargarClientes();
        
        //Panel principal
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        //Contenido
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(20, 30, 20, 30));
        root.add(content, BorderLayout.CENTER);

        //Título
        content.add(crearTitulo(), BorderLayout.NORTH);

        //Formulario de búsqueda
        content.add(crearFormulario(), BorderLayout.CENTER);

        //Botones
        content.add(crearBotones(), BorderLayout.SOUTH);
    }

    private void cargarClientes() {
        try {
            listaClientes = Clientes.obtenerClientes();
        } catch (Exception e) {
            listaClientes = new java.util.ArrayList<>();
            System.err.println("No se pudo completar la operación. Intente de nuevo.");
        }
    }

    private JComponent crearTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);

        JPanel barra = new JPanel();
        barra.setBackground(AZUL_LABEL);
        barra.setPreferredSize(new Dimension(5, 26));

        JLabel titulo = new JLabel(" Búsqueda de Clientes");
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

        // Fila 1: ID Cliente (ComboBox)
        JPanel fila1 = new JPanel(new GridLayout(1, 1, 15, 0));
        fila1.setOpaque(false);
        fila1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        cmbIdCliente = crearComboBoxClientes(true); // true = por ID
        fila1.add(crearCampoCombo("ID Cliente:", cmbIdCliente));

        // Fila 2: Nombre (ComboBox)
        JPanel fila2 = new JPanel(new GridLayout(1, 1, 15, 0));
        fila2.setOpaque(false);
        fila2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        cmbNombre = crearComboBoxClientes(false);
        fila2.add(crearCampoCombo("Cliente:", cmbNombre));

        // Fila 3: RUC/Cédula y Correo
        JPanel fila3 = new JPanel(new GridLayout(1, 2, 15, 0));
        fila3.setOpaque(false);
        fila3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        txtRucCed = new JTextField();
        txtCorreo = new JTextField();
        
        fila3.add(crearCampo("Cédula/RUC:", txtRucCed));
        fila3.add(crearCampo("Correo:", txtCorreo));

        // Fila 4: Celular
        JPanel fila4 = new JPanel(new GridLayout(1, 2, 15, 0));
        fila4.setOpaque(false);
        fila4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        txtCelular = new JTextField();
        
        fila4.add(crearCampo("Celular:", txtCelular));
        fila4.add(new JPanel());

        form.add(fila1);
        form.add(Box.createVerticalStrut(8));
        form.add(fila2);
        form.add(Box.createVerticalStrut(8));
        form.add(fila3);
        form.add(Box.createVerticalStrut(8));
        form.add(fila4);
        
        panelConBorde.add(form, BorderLayout.NORTH);
        
        return panelConBorde;
    }

    private JComboBox<String> crearComboBoxClientes(boolean porId) {
        JComboBox<String> combo = new JComboBox<>();
        combo.addItem(""); 

        for (Clientes cliente : listaClientes) {
            String item = porId ? cliente.getIdCliente() : cliente.getCliNombre();
            combo.addItem(item);
        }

        combo.setEditable(false);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Personalizar el renderizador para mejor apariencia
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
        String idCliente = obtenerTextoCombo(cmbIdCliente);
        String nombre = obtenerTextoCombo(cmbNombre);
        String rucCed = txtRucCed.getText().trim();
        String correo = txtCorreo.getText().trim();
        String celular = txtCelular.getText().trim();
        
        // Validar que al menos un campo tenga valor
        if (idCliente.isEmpty() && nombre.isEmpty() && rucCed.isEmpty() && 
            correo.isEmpty() && celular.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Debe ingresar al menos un criterio de búsqueda.",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        try {
            // Realiza búsqueda por parámetros
            List<Clientes> resultados = Clientes.obtenerClientesPorParametro(
                idCliente.isEmpty() ? null : idCliente,
                rucCed.isEmpty() ? null : rucCed,
                nombre.isEmpty() ? null : nombre,
                correo.isEmpty() ? null : correo,
                celular.isEmpty() ? null : celular,
                null
            );
            
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "No se encontraron clientes con los criterios especificados.",
                    "Sin resultados",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                ventanaPadre.actualizarTablaConResultados(resultados);
                
                JOptionPane.showMessageDialog(
                    this,
                    "Se encontraron " + resultados.size() + " cliente(s).",
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
        cmbIdCliente.setSelectedIndex(0);
        cmbNombre.setSelectedIndex(0);
        txtRucCed.setText("");
        txtCorreo.setText("");
        txtCelular.setText("");
        
        cmbIdCliente.requestFocus();
    }

    private void onVolver() {
        dispose();
    }
}