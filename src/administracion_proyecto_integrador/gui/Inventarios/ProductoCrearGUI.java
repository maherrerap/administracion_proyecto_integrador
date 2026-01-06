package administracion_proyecto_integrador.gui.Inventarios;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import administracion_proyecto_integrador.dp.Inventarios.Productos;

public class ProductoCrearGUI extends JPanel {

    // Colores Estandar en la Aplicación
    private static final Color AZUL_LABEL = new Color(30, 86, 198);
    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color FONDO_CAMPO = new Color(249, 250, 251);
    private static final Color BORDE_CAMPO = new Color(209, 213, 219);

    // Componentes del formulario
    private JTextField txtIdProducto;
    private JTextField txtDescripcion;
    private JComboBox<Productos.UnidadMedida> cmbUmCompra;
    private JComboBox<Productos.UnidadMedida> cmbUmVenta;
    private JTextField txtValorCompra;
    private JTextField txtPrecioVenta;
    private JTextField txtSaldoInicial;
    private JComboBox<Productos.Categoria> cmbCategoria;

    private ProductosGUI ventanaPadre;

    public ProductoCrearGUI(ProductosGUI padre) {
        this.ventanaPadre = padre;
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Título con barra azul
        add(crearTitulo(), BorderLayout.NORTH);

        // Contenido del formulario
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(Color.WHITE);
        contenido.setBorder(new EmptyBorder(20, 30, 20, 30));
        add(contenido, BorderLayout.CENTER);

        // Formulario con campos
        contenido.add(crearFormulario(), BorderLayout.CENTER);

        // Botones inferiores
        contenido.add(crearPanelBotones(), BorderLayout.SOUTH);

        // Cargar datos iniciales
        cargarDatosIniciales();
    }

    private JComponent crearTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Barra azul vertical
        JPanel barra = new JPanel();
        barra.setBackground(AZUL_LABEL);
        barra.setPreferredSize(new Dimension(5, 35));

        JLabel titulo = new JLabel(" Crear Producto");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(new Color(20, 20, 20));

        panel.add(barra);
        panel.add(titulo);

        return panel;
    }

    private JComponent crearFormulario() {
        JPanel form = new JPanel(new BorderLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        int row = 0;

        // ID Producto (solo lectura)
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.5;
        campos.add(crearCampoTexto("ID Producto:", txtIdProducto = new JTextField(), false), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        campos.add(crearCampoCombo("Categoría:", cmbCategoria = new JComboBox<>()), gbc);

        row++;

        // Descripción
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        campos.add(crearCampoTexto("Descripción del Producto:", txtDescripcion = new JTextField(), true), gbc);
        gbc.gridwidth = 1; // Reset

        row++;

        // Unidad de Medida Compra y Venta
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.5;
        campos.add(crearCampoCombo("Unidad de Medida (Compra):", cmbUmCompra = new JComboBox<>()), gbc);

        gbc.gridx = 1;
        campos.add(crearCampoCombo("Unidad de Medida (Venta):", cmbUmVenta = new JComboBox<>()), gbc);

        row++;

        // Valor Compra y Precio Venta
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.5;
        campos.add(crearCampoTexto("Valor de Compra:", txtValorCompra = new JTextField(), true), gbc);

        gbc.gridx = 1;
        campos.add(crearCampoTexto("Precio de Venta:", txtPrecioVenta = new JTextField(), true), gbc);

        row++;

        // Saldo Inicial 
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        campos.add(crearCampoTexto("Saldo Inicial:", txtSaldoInicial = new JTextField(), true), gbc);

        form.add(campos, BorderLayout.CENTER);

        return form;
    }

    private JPanel crearCampoTexto(String label, JTextField campo, boolean editable) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(new Color(55, 65, 81));

        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campo.setBackground(editable ? FONDO_CAMPO : new Color(229, 231, 235));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE_CAMPO, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        campo.setEditable(editable);

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearCampoCombo(String label, JComboBox<?> combo) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(new Color(55, 65, 81));

        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setBackground(FONDO_CAMPO);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE_CAMPO, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(combo, BorderLayout.CENTER);

        return panel;
    }

    private JComponent crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 15));
        panel.setBackground(Color.WHITE);

        JButton btnVolver = crearBoton("Volver");
        JButton btnCrear = crearBoton("Crear");
        btnVolver.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Operación Cancelada.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );

            volverAlCatalogo();
        });
        btnCrear.addActionListener(e -> onCrearProducto());

        panel.add(btnVolver);
        panel.add(btnCrear);

        return panel;
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(NAVY_BTN);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(20, 45, 75));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(NAVY_BTN);
            }
        });
        
        return btn;
    }

    private void cargarDatosIniciales() {
        try {
            // Generar y mostrar el nuevo ID automáticamente
            String nuevoId = Productos.generarNuevoId();
            txtIdProducto.setText(nuevoId);

            // Cargar Unidades de Medida
            List<Productos.UnidadMedida> unidades = Productos.obtenerUnidadesMedida();
            DefaultComboBoxModel<Productos.UnidadMedida> modeloUmCompra = 
                new DefaultComboBoxModel<>();
            DefaultComboBoxModel<Productos.UnidadMedida> modeloUmVenta = 
                new DefaultComboBoxModel<>();

            modeloUmCompra.addElement(null);
            modeloUmVenta.addElement(null);

            for (Productos.UnidadMedida um : unidades) {
                modeloUmCompra.addElement(um);
                modeloUmVenta.addElement(um);
            }

            cmbUmCompra.setModel(modeloUmCompra);
            cmbUmVenta.setModel(modeloUmVenta);

            // Cargar Categorías
            List<Productos.Categoria> categorias = Productos.obtenerCategorias();
            DefaultComboBoxModel<Productos.Categoria> modeloCat = 
                new DefaultComboBoxModel<>();

            modeloCat.addElement(null);

            for (Productos.Categoria cat : categorias) {
                modeloCat.addElement(cat);
            }

            cmbCategoria.setModel(modeloCat);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "No se pudo completar la operación. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAlCatalogo() {
        if (ventanaPadre != null) {
            ventanaPadre.mostrarCatalogo();
        }
    }

    private void onCrearProducto() {
        try {
            // Validar campos obligatorios
            if (!validarCampos()) {
                return;
            }

            // Crear objeto Productos
            Productos nuevoProducto = new Productos();
            
            // ID Producto generado automaticamente
            nuevoProducto.setIdProducto(txtIdProducto.getText().trim());
            
            // Descripción
            nuevoProducto.setProDescripcion(txtDescripcion.getText().trim());
            
            // Unidades de Medida 
            Productos.UnidadMedida umCompra = (Productos.UnidadMedida) cmbUmCompra.getSelectedItem();
            Productos.UnidadMedida umVenta = (Productos.UnidadMedida) cmbUmVenta.getSelectedItem();
            nuevoProducto.setProUmCompra(umCompra.getId());
            nuevoProducto.setProUmVenta(umVenta.getId());
            
            // Precios
            nuevoProducto.setProValorCompra(Double.parseDouble(txtValorCompra.getText().trim()));
            nuevoProducto.setProPrecioVenta(Double.parseDouble(txtPrecioVenta.getText().trim()));
            
            // Saldo Inicial
            int saldoInicial = Integer.parseInt(txtSaldoInicial.getText().trim());
            nuevoProducto.setProSaldoInicial(saldoInicial);
            
            // Categoría 
            Productos.Categoria categoria = (Productos.Categoria) cmbCategoria.getSelectedItem();
            nuevoProducto.setIdcategoria(categoria.getId());
            
            // Inicializar valores automáticos
            nuevoProducto.inicializarNuevoProducto();
            
            // Validar producto usando el método verificarProd()
            List<Productos.ErrorValidacion> errores = nuevoProducto.verificarProd();
            
            if (!errores.isEmpty()) {
                StringBuilder mensaje = new StringBuilder("Se encontraron los siguientes errores:\n\n");
                for (Productos.ErrorValidacion error : errores) {
                    mensaje.append("• ").append(error.getMensaje()).append("\n");
                }
                
                JOptionPane.showMessageDialog(this,
                    mensaje.toString(),
                    "Errores de Validación",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Guardar en la base de datos
            boolean guardado = Productos.grabarProducto(nuevoProducto);
            
            if (guardado) {
                JOptionPane.showMessageDialog(this,
                    "Registro creado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Volver al catálogo y recargar datos
                volverAlCatalogo();
                
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo completar la operación. Intente de nuevo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Error en formato de números.\nVerifique los campos numéricos.",
                "Error de Formato",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "No se pudo completar la operación. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        // Descripción
        if (txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La descripción del producto es obligatoria.",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            txtDescripcion.requestFocus();
            return false;
        }

        // Unidad de Medida Compra
        if (cmbUmCompra.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar una Unidad de Medida de Compra.",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            cmbUmCompra.requestFocus();
            return false;
        }

        // Unidad de Medida Venta
        if (cmbUmVenta.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar una Unidad de Medida de Venta.",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            cmbUmVenta.requestFocus();
            return false;
        }

        // Valor Compra
        if (txtValorCompra.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El valor de compra es obligatorio.",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            txtValorCompra.requestFocus();
            return false;
        }

        // Precio Venta
        if (txtPrecioVenta.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El precio de venta es obligatorio.",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            txtPrecioVenta.requestFocus();
            return false;
        }

        // Saldo Inicial
        if (txtSaldoInicial.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El saldo inicial es obligatorio.",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            txtSaldoInicial.requestFocus();
            return false;
        }

        // Categoría
        if (cmbCategoria.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar una categoría.",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            cmbCategoria.requestFocus();
            return false;
        }

        // Validar formato de números
        try {
            double valorCompra = Double.parseDouble(txtValorCompra.getText().trim());
            if (valorCompra <= 0) {
                JOptionPane.showMessageDialog(this,
                    "El valor de compra debe ser mayor a 0.",
                    "Valor Inválido",
                    JOptionPane.WARNING_MESSAGE);
                txtValorCompra.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "El valor de compra debe ser un número válido.",
                "Formato Incorrecto",
                JOptionPane.WARNING_MESSAGE);
            txtValorCompra.requestFocus();
            return false;
        }

        try {
            double precioVenta = Double.parseDouble(txtPrecioVenta.getText().trim());
            if (precioVenta <= 0) {
                JOptionPane.showMessageDialog(this,
                    "El precio de venta debe ser mayor a 0.",
                    "Valor Inválido",
                    JOptionPane.WARNING_MESSAGE);
                txtPrecioVenta.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "El precio de venta debe ser un número válido.",
                "Formato Incorrecto",
                JOptionPane.WARNING_MESSAGE);
            txtPrecioVenta.requestFocus();
            return false;
        }

        try {
            int saldoInicial = Integer.parseInt(txtSaldoInicial.getText().trim());
            if (saldoInicial < 0) {
                JOptionPane.showMessageDialog(this,
                    "El saldo inicial no puede ser negativo.",
                    "Valor Inválido",
                    JOptionPane.WARNING_MESSAGE);
                txtSaldoInicial.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "El saldo inicial debe ser un número entero válido.",
                "Formato Incorrecto",
                JOptionPane.WARNING_MESSAGE);
            txtSaldoInicial.requestFocus();
            return false;
        }

        return true;
    }
}