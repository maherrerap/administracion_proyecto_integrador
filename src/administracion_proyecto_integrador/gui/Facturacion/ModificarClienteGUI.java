package administracion_proyecto_integrador.gui.Facturacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import administracion_proyecto_integrador.dp.Facturacion.Clientes;
import java.util.List;

public class ModificarClienteGUI extends JFrame {

    // Colores
    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color AZUL_LABEL = new Color(30, 86, 198);
    private static final Color AZUL_OSCURO_LABELS = new Color(8, 26, 43);
    
    // Componentes del formulario
    private JTextField txtIdCliente;
    private JTextField txtNombre;
    private JTextField txtRucCed;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtCelular;
    private JTextField txtDireccion;
    private JComboBox<Clientes.ComboItem> cmbCiudad;
    
    private String idClienteOriginal;
    private Clientes clienteActual;
    
    public ModificarClienteGUI(String idCliente) {
        this.idClienteOriginal = idCliente;
        
        setTitle("Modificar Cliente");
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

        // Formulario
        content.add(crearFormulario(), BorderLayout.CENTER);

        // Botones
        content.add(crearBotones(), BorderLayout.SOUTH);
        
        // Cargar datos del cliente
        cargarDatosCliente();
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
        barra.setPreferredSize(new Dimension(5, 30));

        JLabel titulo = new JLabel(" Modificar Cliente");
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
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(25, 35, 25, 35)
        ));
        
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        // Fila 1: ID Cliente y Nombre
        JPanel fila1 = new JPanel(new GridLayout(1, 2, 20, 0));
        fila1.setOpaque(false);
        fila1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        txtIdCliente = new JTextField();
        txtIdCliente.setEnabled(false); // ID no se puede modificar
        txtIdCliente.setBackground(new Color(240, 240, 240));
        txtNombre = new JTextField();
        
        fila1.add(crearCampo("ID Cliente:", txtIdCliente, false));
        fila1.add(crearCampo("Nombre Cliente:", txtNombre, true));

        // Fila 2: Cédula/RUC y Teléfono
        JPanel fila2 = new JPanel(new GridLayout(1, 2, 20, 0));
        fila2.setOpaque(false);
        fila2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        txtRucCed = new JTextField();
        txtTelefono = new JTextField();
        
        fila2.add(crearCampo("Cédula/RUC:", txtRucCed, true));
        fila2.add(crearCampo("Teléfono:", txtTelefono, true));

        // Fila 3: Correo y Celular
        JPanel fila3 = new JPanel(new GridLayout(1, 2, 20, 0));
        fila3.setOpaque(false);
        fila3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        txtCorreo = new JTextField();
        txtCelular = new JTextField();
        
        fila3.add(crearCampo("Correo:", txtCorreo, true));
        fila3.add(crearCampo("Celular:", txtCelular, false));

        // Fila 4: Dirección
        JPanel fila4 = new JPanel(new GridLayout(1, 1, 20, 0));
        fila4.setOpaque(false);
        fila4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        txtDireccion = new JTextField();
        fila4.add(crearCampo("Dirección:", txtDireccion, true));

        // Fila 5: Ciudad
        JPanel fila5 = new JPanel(new GridLayout(1, 2, 20, 0));
        fila5.setOpaque(false);
        fila5.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        cmbCiudad = new JComboBox<>();
        cmbCiudad.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cargarCiudades();
        
        fila5.add(crearCampoCombo("Ciudad:", cmbCiudad, true));
        fila5.add(new JPanel()); // Panel vacío para mantener el grid

        form.add(fila1);
        form.add(Box.createVerticalStrut(10));
        form.add(fila2);
        form.add(Box.createVerticalStrut(10));
        form.add(fila3);
        form.add(Box.createVerticalStrut(10));
        form.add(fila4);
        form.add(Box.createVerticalStrut(10));
        form.add(fila5);
        
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
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearCampoCombo(String label, JComboBox<Clientes.ComboItem> combo, boolean obligatorio) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);

        String labelText = obligatorio ? label + " *" : label;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(AZUL_OSCURO_LABELS);

        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(combo, BorderLayout.CENTER);

        return panel;
    }

    private void cargarCiudades() {
        // Agregar item por defecto
        cmbCiudad.addItem(new Clientes.ComboItem("", "Seleccione una ciudad"));
        
        // Cargar ciudades desde la capa DP
        List<Clientes.ComboItem> ciudades = Clientes.obtenerCiudades();
        for (Clientes.ComboItem ciudad : ciudades) {
            cmbCiudad.addItem(ciudad);
        }
    }

    private void cargarDatosCliente() {
        try {
            // Obtiene todos los clientes y buscar el que corresponde
            List<Clientes> clientes = Clientes.obtenerClientes();
            
            for (Clientes cliente : clientes) {
                if (cliente.getIdCliente().equals(idClienteOriginal)) {
                    clienteActual = cliente;
                    break;
                }
            }
            
            if (clienteActual == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el cliente con ID: " + idClienteOriginal,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                abrirClientesGUI();
                return;
            }
            
            //Llena los campos con los datos del cleinte
            txtIdCliente.setText(clienteActual.getIdCliente());
            txtNombre.setText(clienteActual.getCliNombre());
            txtRucCed.setText(clienteActual.getCliRucCed());
            txtTelefono.setText(clienteActual.getCliTelefono());
            txtCorreo.setText(clienteActual.getCliMail());
            txtCelular.setText(clienteActual.getCliCelular());
            txtDireccion.setText(clienteActual.getCliDireccion());
            
            //Selecciona una ciudad en el ComboBox
            String idCiudad = clienteActual.getIdCiudad();
            for (int i = 0; i < cmbCiudad.getItemCount(); i++) {
                Clientes.ComboItem item = cmbCiudad.getItemAt(i);
                if (item.getId().equals(idCiudad)) {
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
            abrirClientesGUI();
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
        btnVolver.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Operación Cancelada.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );

            onVolver();
        });
      

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
        abrirClientesGUI();
    }

    private void onActualizar() {
        // Obtener datos del formulario (ID no se modifica)
        String nombre = txtNombre.getText().trim();
        String rucCed = txtRucCed.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String celular = txtCelular.getText().trim();
        String direccion = txtDireccion.getText().trim();
        
        Clientes.ComboItem ciudadSeleccionada = (Clientes.ComboItem) cmbCiudad.getSelectedItem();
        String idCiudad = ciudadSeleccionada != null ? ciudadSeleccionada.getId() : "";

        // Crear objeto Cliente con los datos modificados
        Clientes clienteModificado = new Clientes();
        clienteModificado.setIdCliente(idClienteOriginal); 
        clienteModificado.setCliNombre(nombre);
        clienteModificado.setCliRucCed(rucCed);
        clienteModificado.setCliTelefono(telefono);
        clienteModificado.setCliMail(correo);
        clienteModificado.setCliCelular(celular);
        clienteModificado.setCliDireccion(direccion);
        clienteModificado.setEstadoCli("ACT");
        clienteModificado.setIdCiudad(idCiudad);

        // Validar datos
        List<Clientes.ErrorValidacion> errores = clienteModificado.verificarCli();
        
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

        //Actualiza en la BD
        boolean exito = Clientes.modificarCliente(clienteModificado);
        
        if (exito) {
            JOptionPane.showMessageDialog(
                this,
                "Registro modificado correctamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );
            abrirClientesGUI();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo actualizar el registro. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}