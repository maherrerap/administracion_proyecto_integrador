package administracion_proyecto_integrador.gui.Facturacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import administracion_proyecto_integrador.dp.Facturacion.Clientes;
import java.util.List;


public class NuevoClienteGUI extends JFrame {

    // Colores
    private static final Color NAVY = new Color(8, 26, 43);
    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color AZUL_LABEL = new Color(30, 86, 198);
    private static final Color GRIS_BORDE = new Color(200, 200, 200);
    private static final Color GRIS_FONDO = new Color(245, 245, 245);
    
    // Componentes del formulario
    private JLabel lblIdClienteValor;
    private JTextField txtNombre;
    private JTextField txtRucCed;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtCelular;
    private JTextField txtDireccion;
    private JComboBox<Clientes.ComboItem> cmbCiudad;
    
    public NuevoClienteGUI() {
        setTitle("Nuevo Cliente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Panel principal
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        // Contenido
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(30, 50, 30, 50));
        root.add(content, BorderLayout.CENTER);

        // Título
        content.add(crearTitulo(), BorderLayout.NORTH);
        
        content.add(crearFormulario(), BorderLayout.CENTER);

        // Botones
        content.add(crearBotones(), BorderLayout.SOUTH);
        
        cargarSiguienteId();
    }

    private void abrirClientesGUI() {
        new ClientesGUI().setVisible(true);
        this.dispose();
    }

    private JComponent crearTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);

        JPanel barra = new JPanel();
        barra.setBackground(AZUL_LABEL);
        barra.setPreferredSize(new Dimension(5, 35));

        JLabel titulo = new JLabel(" Nuevo Cliente");
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
        lblIdClienteValor = new JLabel();
        lblIdClienteValor.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblIdClienteValor.setForeground(new Color(80, 80, 80));
        
        txtNombre = new JTextField();
        txtRucCed = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtCelular = new JTextField();
        txtDireccion = new JTextField();
        cmbCiudad = new JComboBox<>();
        cmbCiudad.setFont(new Font("SansSerif", Font.PLAIN, 13)); 
        cargarCiudades();

        // Fila 1: ID Cliente y Nombre
        JPanel fila1 = new JPanel(new GridLayout(1, 2, 25, 0)); 
        fila1.setOpaque(false);
        fila1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); 
        fila1.add(crearCampoSoloLectura("ID Cliente:", lblIdClienteValor));
        fila1.add(crearCampo("Nombre Cliente:", txtNombre, true));

        // Fila 2: Cédula/RUC y Teléfono
        JPanel fila2 = new JPanel(new GridLayout(1, 2, 25, 0));
        fila2.setOpaque(false);
        fila2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        fila2.add(crearCampo("Cédula/RUC:", txtRucCed, true));
        fila2.add(crearCampo("Teléfono:", txtTelefono, true));

        // Fila 3: Correo y Celular
        JPanel fila3 = new JPanel(new GridLayout(1, 2, 25, 0));
        fila3.setOpaque(false);
        fila3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        fila3.add(crearCampo("Correo:", txtCorreo, true));
        fila3.add(crearCampo("Celular:", txtCelular, false));

        // Fila 4: Dirección
        JPanel fila4 = new JPanel(new BorderLayout());
        fila4.setOpaque(false);
        fila4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        fila4.add(crearCampo("Dirección:", txtDireccion, true), BorderLayout.CENTER);

        // Fila 5: Ciudad
        JPanel fila5 = new JPanel(new GridLayout(1, 2, 25, 0));
        fila5.setOpaque(false);
        fila5.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

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

        fila5.add(panelCiudad);
        fila5.add(new JPanel() {{ setOpaque(false); }}); // Panel vacío

        //Filas con menos espacio vertical
        form.add(fila1);
        form.add(Box.createVerticalStrut(10)); 
        form.add(fila2);
        form.add(Box.createVerticalStrut(10));
        form.add(fila3);
        form.add(Box.createVerticalStrut(10));
        form.add(fila4);
        form.add(Box.createVerticalStrut(10));
        form.add(fila5);
        form.add(Box.createVerticalStrut(15)); 

        contenedor.add(form, BorderLayout.CENTER);
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
  
    private void cargarCiudades() {
        cmbCiudad.addItem(new Clientes.ComboItem("", "Seleccione una ciudad"));
        try {
            List<Clientes.ComboItem> ciudades = Clientes.obtenerCiudades();
            for (Clientes.ComboItem ciudad : ciudades) {
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
            String siguienteId = Clientes.obtenerSiguienteId();
            lblIdClienteValor.setText(siguienteId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "No se pudo completar la operación. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            lblIdClienteValor.setText("CLI0001"); // Valor por defecto
        }
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
                    "Operación Cancelada.",
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

    private void onCancelar() {
        abrirClientesGUI();
    }

    private void onGuardar() {
        // Obtener datos del formulario
        String idCliente = lblIdClienteValor.getText().trim();
        String nombre = txtNombre.getText().trim();
        String rucCed = txtRucCed.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String celular = txtCelular.getText().trim();
        String direccion = txtDireccion.getText().trim();
        
        Clientes.ComboItem ciudadSeleccionada = (Clientes.ComboItem) cmbCiudad.getSelectedItem();
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

        // Crear objeto Cliente
        Clientes nuevoCliente = new Clientes();
        nuevoCliente.setIdCliente(idCliente);
        nuevoCliente.setCliNombre(nombre);
        nuevoCliente.setCliRucCed(rucCed);
        nuevoCliente.setCliTelefono(telefono);
        nuevoCliente.setCliMail(correo);
        nuevoCliente.setCliCelular(celular);
        nuevoCliente.setCliDireccion(direccion);
        nuevoCliente.setEstadoCli("ACT");
        nuevoCliente.setIdCiudad(idCiudad);

        // Validar datos
        List<Clientes.ErrorValidacion> errores = nuevoCliente.verificarCli();
        
        if (!errores.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("Se encontraron los siguientes errores:\n\n");
            for (Clientes.ErrorValidacion error : errores) {
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

        // Verificar si el ID ya existe
        if (Clientes.verificarExistencia(idCliente)) {
            JOptionPane.showMessageDialog(
                this,
                "Ya existe un cliente con el ID: " + idCliente,
                "ID Duplicado",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Guardar en la base de datos
        boolean exito = Clientes.grabarCliente(nuevoCliente);
        
        if (exito) {
            JOptionPane.showMessageDialog(
                this,
                "Registro creado correctamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );
            abrirClientesGUI();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo guardar el registro. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}