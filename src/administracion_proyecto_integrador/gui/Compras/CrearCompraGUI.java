package administracion_proyecto_integrador.gui.Compras;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

import administracion_proyecto_integrador.dp.Compras.Compras;
import administracion_proyecto_integrador.dp.Compras.Proveedores;
import administracion_proyecto_integrador.dp.Compras.Compras.ErrorValidacion;

public class CrearCompraGUI extends JFrame {

    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color AZUL_LABEL = new Color(30, 86, 198);

    private JTextField txtNumCompra;
    private JTextField txtFechaEmision;
    private JComboBox<String> cmbProveedores;

    public CrearCompraGUI() {
        setTitle("Crear Orden de Compra");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        JPanel contentWrapper = new JPanel(new GridBagLayout());
        contentWrapper.setBackground(Color.WHITE);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(30, 40, 30, 40));
        content.setPreferredSize(new Dimension(900, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 20, 20);

        contentWrapper.add(content, gbc);
        root.add(contentWrapper, BorderLayout.CENTER);

        content.add(crearTitulo(), BorderLayout.NORTH);
        content.add(crearFormulario(), BorderLayout.CENTER);

        cargarSiguienteIdCompra();
    }

    private JComponent crearTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel barra = new JPanel();
        barra.setBackground(AZUL_LABEL);
        barra.setPreferredSize(new Dimension(5, 35));

        JLabel titulo = new JLabel(" Crear Orden de Compra");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));

        panel.add(barra);
        panel.add(titulo);
        return panel;
    }

    private JComponent crearFormulario() {
        JPanel panelBorde = new JPanel(new BorderLayout());
        panelBorde.setBackground(Color.WHITE);
        panelBorde.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                new EmptyBorder(30, 40, 30, 40)
        ));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);

        // Fila 1
        JPanel fila1 = new JPanel(new GridLayout(1, 2, 40, 0));
        fila1.setOpaque(false);

        fila1.add(crearCampoTexto("N.º Orden Compra:"));
        fila1.add(crearCampoFechaActual("Fecha de Emisión:"));

        form.add(fila1);
        form.add(Box.createVerticalStrut(25));

        // Fila 2
        JPanel fila2 = new JPanel(new BorderLayout());
        fila2.setOpaque(false);

        JLabel lblProveedor = new JLabel("Seleccione el proveedor");
        lblProveedor.setFont(new Font("SansSerif", Font.PLAIN, 14));

        cmbProveedores = new JComboBox<>();
        cmbProveedores.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbProveedores.setPreferredSize(new Dimension(0, 40));

        cargarProveedores();

        fila2.add(lblProveedor, BorderLayout.NORTH);
        fila2.add(Box.createVerticalStrut(8), BorderLayout.CENTER);
        fila2.add(cmbProveedores, BorderLayout.SOUTH);

        form.add(fila2);
        form.add(Box.createVerticalGlue());

        // Botones
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        panelBoton.setOpaque(false);

        JButton btnVolver = crearBoton("Volver");
        JButton btnCrear = crearBoton("Crear");

        btnCrear.addActionListener(e -> crearCompra());
        btnVolver.addActionListener(e -> {
            new ComprasGUI().setVisible(true);
            dispose();
        });

        panelBoton.add(btnVolver);
        panelBoton.add(btnCrear);

        form.add(panelBoton);

        panelBorde.add(form, BorderLayout.CENTER);
        return panelBorde;
    }

    private JPanel crearCampoTexto(String label) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(AZUL_LABEL);

        txtNumCompra = new JTextField();
        txtNumCompra.setEditable(false);
        txtNumCompra.setBackground(new Color(245, 245, 245));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(txtNumCompra, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearCampoFechaActual(String label) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(AZUL_LABEL);

        txtFechaEmision = new JTextField();
        txtFechaEmision.setEditable(false);
        txtFechaEmision.setBackground(new Color(245, 245, 245));

        LocalDate hoy = LocalDate.now();
        txtFechaEmision.setText(String.format("%02d/%02d/%d",
                hoy.getDayOfMonth(), hoy.getMonthValue(), hoy.getYear()));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(txtFechaEmision, BorderLayout.CENTER);
        return panel;
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(NAVY_BTN);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }

    private void cargarSiguienteIdCompra() {
        try {
            txtNumCompra.setText(Compras.obtenerSiguienteIdCompra());
        } catch (Exception e) {
            txtNumCompra.setText("OC0001");
        }
    }

    private void cargarProveedores() {
        try {
            cmbProveedores.removeAllItems();
            cmbProveedores.addItem("Seleccione el proveedor");

            List<Proveedores> proveedores = Proveedores.obtenerProveedoresActivos();
            for (Proveedores p : proveedores) {
                cmbProveedores.addItem(
                        p.getIdProveedor() + " - " + p.getProvNombre()
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar proveedores",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearCompra() {
        if (cmbProveedores.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un proveedor",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Compras compra = new Compras();
            compra.setIdCompra(txtNumCompra.getText());
            compra.setEstadoOc("ABI");
            compra.setOcFechaHora(LocalDate.now());

            String proveedorSel = (String) cmbProveedores.getSelectedItem();
            compra.setIdProveedor(proveedorSel.split(" - ")[0]);

            List<ErrorValidacion> errores = compra.verificarOc();
            if (!errores.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        errores.get(0).getMensaje(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (Compras.grabarCompra(compra)) {
                JOptionPane.showMessageDialog(this,
                        "Orden de compra creada correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new ComprasGUI().setVisible(true);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear la orden de compra",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}