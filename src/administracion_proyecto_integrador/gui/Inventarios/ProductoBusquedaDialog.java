package administracion_proyecto_integrador.gui.Inventarios;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import administracion_proyecto_integrador.dp.Inventarios.Productos;

public class ProductoBusquedaDialog extends JDialog {

    // Colores Estandar en la Aplicación
    private static final Color AZUL_LABEL = new Color(30, 86, 198);
    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color FONDO_CAMPO = new Color(249, 250, 251);
    private static final Color BORDE_CAMPO = new Color(209, 213, 219);

    // Componentes del formulario
    private JComboBox<String> cmbIdProducto;
    private JTextField txtDescripcion;
    private JComboBox<Productos.Categoria> cmbCategoria;

    private ProductosGUI ventanaPadre;

    public ProductoBusquedaDialog(ProductosGUI padre) {
        super(padre, "Consulta de Productos por Parámetros", true);
        this.ventanaPadre = padre;

        setSize(650, 500);
        setLocationRelativeTo(padre);
        setResizable(false);

        // Panel principal
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(20, 30, 20, 30));
        setContentPane(root);

        // Título
        root.add(crearTitulo(), BorderLayout.NORTH);

        // Formulario
        root.add(crearFormulario(), BorderLayout.CENTER);

        // Botones
        root.add(crearPanelBotones(), BorderLayout.SOUTH);

        // Cargar datos iniciales
        cargarDatosIniciales();
    }

    private JComponent crearTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Barra azul vertical
        JPanel barra = new JPanel();
        barra.setBackground(AZUL_LABEL);
        barra.setPreferredSize(new Dimension(5, 28));

        JLabel titulo = new JLabel(" Búsqueda de Productos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
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
            new EmptyBorder(30, 30, 30, 30)
        ));

        // Panel de campos
        JPanel campos = new JPanel();
        campos.setLayout(new BoxLayout(campos, BoxLayout.Y_AXIS));
        campos.setBackground(Color.WHITE);

        // Campo ID Producto
        campos.add(crearCampoCombo("ID Producto:", cmbIdProducto = new JComboBox<>()));
        campos.add(Box.createVerticalStrut(20));

        // Campo Categoría (ComboBox)
        campos.add(crearCampoCombo("Categoría:", cmbCategoria = new JComboBox<>()));
        campos.add(Box.createVerticalStrut(20));

        // Campo Descripción
        campos.add(crearCampoTexto("Descripción:", txtDescripcion = new JTextField()));

        form.add(campos, BorderLayout.NORTH);

        return form;
    }

    private JPanel crearCampoTexto(String label, JTextField campo) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(new Color(55, 65, 81));

        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setBackground(FONDO_CAMPO);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE_CAMPO, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        campo.setPreferredSize(new Dimension(0, 45));
        campo.setMinimumSize(new Dimension(0, 45));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearCampoCombo(String label, JComboBox<?> combo) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(new Color(55, 65, 81));

        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setBackground(FONDO_CAMPO);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE_CAMPO, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        combo.setPreferredSize(new Dimension(0, 45));
        combo.setMinimumSize(new Dimension(0, 45));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(combo, BorderLayout.CENTER);

        return panel;
    }

    private JComponent crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 20));
        panel.setBackground(Color.WHITE);

        JButton btnBuscar = crearBoton("Buscar");
        JButton btnLimpiar = crearBoton("Limpiar");
        JButton btnVolver = crearBoton("Volver");

        btnBuscar.addActionListener(e -> onBuscar());
        btnLimpiar.addActionListener(e -> onLimpiar());
        btnVolver.addActionListener(e -> dispose());

        panel.add(btnBuscar);
        panel.add(btnLimpiar);
        panel.add(btnVolver);

        return panel;
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(NAVY_BTN);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
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
            // Cargar Categorías 
            List<Productos.Categoria> categorias = Productos.obtenerCategorias();
            DefaultComboBoxModel<Productos.Categoria> modeloCat = 
                new DefaultComboBoxModel<>();

            // Opción "Todos"
            modeloCat.addElement(new Productos.Categoria("", "-- Todas las categorías --"));

            for (Productos.Categoria cat : categorias) {
                modeloCat.addElement(cat);
            }

            cmbCategoria.setModel(modeloCat);
            
            List<String> idsProductos = Productos.obtenerIdsProductos();
            DefaultComboBoxModel<String> modeloIds = new DefaultComboBoxModel<>();

            // Opción "Todos"
            modeloIds.addElement("-- Todos los productos --");

            for (String id : idsProductos) {
                modeloIds.addElement(id);
            }

            cmbIdProducto.setModel(modeloIds);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "No se pudo completar la operación. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onBuscar() {
        try {
            // Obtener valores de los campos
            String idProducto = "";
            String idSeleccionado = (String) cmbIdProducto.getSelectedItem();
            
            if (idSeleccionado != null && !idSeleccionado.startsWith("--")) {
                idProducto = idSeleccionado.trim();
            }
            
            String descripcion = txtDescripcion.getText().trim();
            
            // Ahora usando Productos.Categoria
            Productos.Categoria categoriaSeleccionada = 
                (Productos.Categoria) cmbCategoria.getSelectedItem();
            String idCategoria = "";
            
            if (categoriaSeleccionada != null && 
                !categoriaSeleccionada.getId().isEmpty()) {
                idCategoria = categoriaSeleccionada.getId();
            }

            // Validar que al menos un campo tenga valor
            if (idProducto.isEmpty() && descripcion.isEmpty() && idCategoria.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Debe ingresar al menos un criterio de búsqueda.",
                    "Búsqueda Vacía",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Realizar la búsqueda
            List<Productos> resultados = Productos.obtenerProductosPorParametro(
                idProducto.isEmpty() ? null : idProducto,
                descripcion.isEmpty() ? null : descripcion,
                idCategoria.isEmpty() ? null : idCategoria
            );

            // Verificar resultados
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No se encontraron productos con los criterios especificados.",
                    "Sin Resultados",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Actualizar tabla en la ventana padre
            if (ventanaPadre != null) {
                ventanaPadre.actualizarTablaConResultados(resultados);
                
                JOptionPane.showMessageDialog(this,
                    "Se encontraron " + resultados.size() + " producto(s).",
                    "Búsqueda Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            }

            // Cerrar el diálogo
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "No se pudo completar la operación. Intente de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onLimpiar() {
        cmbIdProducto.setSelectedIndex(0);
        txtDescripcion.setText("");
        cmbCategoria.setSelectedIndex(0);
    }
}