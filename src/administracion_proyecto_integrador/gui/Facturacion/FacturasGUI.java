package administracion_proyecto_integrador.gui.Facturacion;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import administracion_proyecto_integrador.dp.Facturacion.Facturas;
import java.util.List;
import java.util.ArrayList;

public class FacturasGUI extends JFrame {

    // Colores similares a la captura
    private static final Color NAVY = new Color(8, 26, 43);
    private static final Color NAVY_BTN = new Color(14, 33, 55);

    private static final Color AZUL_VER = new Color(30, 86, 198);
    private static final Color NARANJA_EDITAR = new Color(244, 150, 30);
    private static final Color ROJO_INH = new Color(217, 64, 64);
    private static final Color AZUL_LABEL = new Color(30, 86, 198);

    // Componentes principales
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblPagina;
    private int paginaActual = 1;
    private int registrosPorPagina = 13;
    private int totalRegistros = 0;
    private int totalPaginas = 0;
    private List<Facturas> todasLasFacturas = new ArrayList<>();
    
    public FacturasGUI() {
        setTitle("Catálogo de Facturas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 650);
        setLocationRelativeTo(null);

        // Layout principal
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        // Barra superior
        root.add(crearNavbar(), BorderLayout.NORTH);

        // Centro (contenido)
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(18, 18, 18, 18));
        root.add(content, BorderLayout.CENTER);

        // Título + botones superiores
        content.add(crearHeaderContenido(), BorderLayout.NORTH);

        // Tabla
        content.add(crearTabla(), BorderLayout.CENTER);

        // Paginación
        content.add(crearPaginacion(), BorderLayout.SOUTH);

        // Cargar datos desde la base de datos
        cargarDatosMock();
    }

    private JComponent crearNavbar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(NAVY);
        nav.setBorder(new EmptyBorder(8, 16, 8, 16));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        left.setOpaque(false);

        // Logo
        try {
            ImageIcon logoIcon = new ImageIcon("src/recursos/imagenes/logo.png");
            Image img = logoIcon.getImage();
            Image imgScaled = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            ImageIcon logoScaled = new ImageIcon(imgScaled);
            
            JLabel logo = new JLabel(logoScaled);
            logo.setPreferredSize(new Dimension(50, 50));
            left.add(logo);
        } catch (Exception e) {
            JLabel logo = new JLabel("C");
            logo.setOpaque(true);
            logo.setBackground(new Color(12, 45, 78));
            logo.setForeground(Color.WHITE);
            logo.setHorizontalAlignment(SwingConstants.CENTER);
            logo.setPreferredSize(new Dimension(50, 50));
            logo.setFont(new Font("SansSerif", Font.BOLD, 18));
            left.add(logo);
        }

        // Menús
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(false);
        menuBar.setBackground(NAVY);
        menuBar.setBorder(BorderFactory.createEmptyBorder());

        // Menú Principal
        JMenu menuInicio = crearMenu("Menú Principal");
        menuInicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Navegar al menú principal si existe
                System.out.println("Navegar a Menú Principal");
            }
        });

        // Menú Administración con submenús
        JMenu menuAdmin = crearMenu("Administración");

        JMenu subBodega = crearSubMenu("Bodega ▸");
        subBodega.add(crearMenuItem("Productos", null));
        subBodega.add(crearMenuItem("Recepciones", null));

        JMenu subFacturacion = crearSubMenu("Facturación ▸");
        subFacturacion.add(crearMenuItem("Clientes", null));
        subFacturacion.add(crearMenuItem("Facturas", e -> abrirFacturasGUI()));

        JMenu subCompras = crearSubMenu("Compras ▸");
        subCompras.add(crearMenuItem("Proveedores", null));
        subCompras.add(crearMenuItem("Órdenes de Compra", null));

        menuAdmin.add(subBodega);
        menuAdmin.add(subFacturacion);
        menuAdmin.add(subCompras);

        // Menú Salir
        JMenu menuSalir = crearMenu("Salir");
        menuSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.exit(0);
            }
        });

        menuBar.add(menuInicio);
        menuBar.add(menuAdmin);
        menuBar.add(menuSalir);

        left.add(menuBar);
        nav.add(left, BorderLayout.WEST);
        
        return nav;
    }

    private JMenu crearMenu(String texto) {
        JMenu menu = new JMenu(texto);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("SansSerif", Font.BOLD, 14));
        menu.setOpaque(false);
        menu.setBorderPainted(false);
        menu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Popup con fondo oscuro
        JPopupMenu popup = menu.getPopupMenu();
        popup.setBackground(new Color(14, 33, 55));
        popup.setBorder(BorderFactory.createLineBorder(new Color(40, 60, 80), 1));

        return menu;
    }

    private JMenu crearSubMenu(String texto) {
        JMenu submenu = new JMenu(texto);
        submenu.setForeground(Color.WHITE);
        submenu.setFont(new Font("SansSerif", Font.PLAIN, 13));
        submenu.setOpaque(true);
        submenu.setBackground(new Color(14, 33, 55));
        submenu.setBorderPainted(false);

        // Popup del submenú
        JPopupMenu popup = submenu.getPopupMenu();
        popup.setBackground(new Color(20, 40, 65));
        popup.setBorder(BorderFactory.createLineBorder(new Color(40, 60, 80), 1));

        return submenu;
    }

    private JMenuItem crearMenuItem(String texto, ActionListener listener) {
        JMenuItem item = new JMenuItem(texto);
        item.setForeground(Color.WHITE);
        item.setFont(new Font("SansSerif", Font.PLAIN, 13));
        item.setOpaque(true);
        item.setBackground(new Color(20, 40, 65));
        item.setBorderPainted(false);
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Efecto hover
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(30, 86, 198));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(20, 40, 65));
            }
        });

        // Agregar el listener personalizado
        if (listener != null) {
            item.addActionListener(listener);
        } else {
            // Listener por defecto
            item.addActionListener(e -> {
                System.out.println("Click en: " + texto);
            });
        }

        return item;
    }
    
    private void abrirFacturasGUI() {
        new FacturasGUI().setVisible(true);
        this.dispose();
    }

    private JComponent crearHeaderContenido() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        // Panel izquierdo con barra azul y título
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        // Barra azul vertical
        JPanel barra = new JPanel();
        barra.setBackground(AZUL_VER);
        barra.setPreferredSize(new Dimension(5, 30));

        JLabel titulo = new JLabel(" Catálogo de Facturas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(20, 20, 20));

        leftPanel.add(barra);
        leftPanel.add(titulo);

        top.add(leftPanel, BorderLayout.WEST);

        // Panel derecho con botones de acción
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        acciones.setOpaque(false);

        JButton btnConsulta = crearBotonSuperior("Consulta Por Parametro");
        JButton btnCrear = crearBotonSuperior("Crear Factura");

        btnConsulta.addActionListener(e -> onConsultaPorParametro());
        btnCrear.addActionListener(e -> onCrearFactura());

        acciones.add(btnConsulta);
        acciones.add(btnCrear);

        top.add(acciones, BorderLayout.EAST);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(top, BorderLayout.NORTH);
        wrapper.add(Box.createVerticalStrut(14), BorderLayout.SOUTH);

        return wrapper;
    }
    private JButton crearBotonSuperior(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(NAVY_BTN);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JComponent crearTabla() {
        String[] cols = {
                "Factura", "Cliente", "Descripción", "Fecha Emisión", "Fecha Pago",
                "Subtotal", "IVA", "Total",
                "Ver", "Editar", "Inhabilitar"
        };

        modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                // Solo columnas de botones editables para capturar click
                return col >= 8;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Header
        JTableHeader th = tabla.getTableHeader();
        th.setFont(new Font("SansSerif", Font.BOLD, 12));
        th.setReorderingAllowed(false);

        // Alineación números (Subtotal/IVA/Total)
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(5).setCellRenderer(center);
        tabla.getColumnModel().getColumn(6).setCellRenderer(center);
        tabla.getColumnModel().getColumn(7).setCellRenderer(center);

        // Render + Editor para botones por columna
        configurarColumnaBoton(8, "Ver", AZUL_VER, (row) -> onVer(row));
        configurarColumnaBoton(9, "Editar", NARANJA_EDITAR, (row) -> onEditar(row));
        configurarColumnaBoton(10, "Inhabilitar", ROJO_INH, (row) -> onInhabilitar(row));

        // Tamaños aproximados
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(220);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(7).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(8).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(9).setPreferredWidth(75);
        tabla.getColumnModel().getColumn(10).setPreferredWidth(95);

        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        return sp;
    }

    private void configurarColumnaBoton(int colIndex, String texto, Color color, RowAction action) {
        TableColumn col = tabla.getColumnModel().getColumn(colIndex);
        col.setCellRenderer(new ButtonRenderer(texto, color));
        col.setCellEditor(new ButtonEditor(new JCheckBox(), texto, color, action));
    }

    private JComponent crearPaginacion() {
        JPanel pag = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pag.setOpaque(false);

        JButton btnPrimera = crearBotonPaginacion("|< Primera");
        JButton btnAnterior = crearBotonPaginacion("< Anterior");
        lblPagina = new JLabel("Página 1 de 5");
        lblPagina.setFont(new Font("SansSerif", Font.BOLD, 12));
        JButton btnSiguiente = crearBotonPaginacion("Siguiente >");
        JButton btnUltima = crearBotonPaginacion("Última >|");

        btnPrimera.addActionListener(e -> onPrimeraPagina());
        btnAnterior.addActionListener(e -> onPaginaAnterior());
        btnSiguiente.addActionListener(e -> onPaginaSiguiente());
        btnUltima.addActionListener(e -> onUltimaPagina());

        pag.add(btnPrimera);
        pag.add(btnAnterior);
        pag.add(lblPagina);
        pag.add(btnSiguiente);
        pag.add(btnUltima);

        return pag;
    }

    private JButton crearBotonPaginacion(String texto) {
        JButton b = new JButton(texto);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.PLAIN, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ---------------------------
    // LISTENERS (STUBS)
    // ---------------------------

    private void onMenuPrincipal() {
        // TODO: conectar navegación
        System.out.println("Click: Menú Principal");
    }

    private void onAdministracion() {
        // TODO: conectar navegación
        System.out.println("Click: Administración");
    }

    private void onSalir() {
        // TODO: confirmar y cerrar sesión
        System.out.println("Click: Salir");
    }
    

    private void onConsultaPorParametro() {
        // TODO: abrir modal/buscador
        System.out.println("Click: Consulta Por Parametro");
    }

    private void onCrearFactura() {
        new CrearFacturaGUI().setVisible(true);
        this.dispose(); // Cerrar FacturasGUI actual
    }

    private void onPrimeraPagina() {
        if (paginaActual != 1) {
            paginaActual = 1;
            actualizarTablaPaginada();
            lblPagina.setText("Página " + paginaActual + " de " + totalPaginas);
        }
    }

    private void onPaginaAnterior() {
        if (paginaActual > 1) {
            paginaActual--;
            actualizarTablaPaginada();
            lblPagina.setText("Página " + paginaActual + " de " + totalPaginas);
        }
    }

    private void onPaginaSiguiente() {
        if (paginaActual < totalPaginas) {
            paginaActual++;
            actualizarTablaPaginada();
            lblPagina.setText("Página " + paginaActual + " de " + totalPaginas);
        }
    }

    private void onUltimaPagina() {
        if (paginaActual != totalPaginas) {
            paginaActual = totalPaginas;
            actualizarTablaPaginada();
            lblPagina.setText("Página " + paginaActual + " de " + totalPaginas);
        }
    }

    private void onVer(int row) {
        // Obtener el ID de la factura de la fila seleccionada
        String idFactura = String.valueOf(modelo.getValueAt(row, 0));

        // Abrir la ventana DetalleFacturaGUI con el ID de la factura
        SwingUtilities.invokeLater(() -> {
            DetalleFacturaGUI detalleVentana = new DetalleFacturaGUI(idFactura);
            detalleVentana.setVisible(true);
        });
    }

    private void onEditar(int row) {
        // TODO: abrir formulario edición
        String idFactura = String.valueOf(modelo.getValueAt(row, 0));
        System.out.println("Acción EDITAR - fila " + row + " | idFactura=" + idFactura);
    }

    private void onInhabilitar(int row) {
        // TODO: confirmar y anular (estado_fac/estado_pxf = 'ANU')
        String idFactura = String.valueOf(modelo.getValueAt(row, 0));
        System.out.println("Acción INHABILITAR - fila " + row + " | idFactura=" + idFactura);
    }

    private void cargarDatosMock() {
        try {
            // Obtener TODAS las facturas desde el DP
            todasLasFacturas = Facturas.obtenerFacturas();
            totalRegistros = todasLasFacturas.size();

            totalPaginas = (int) Math.ceil((double) totalRegistros / registrosPorPagina);
            if (totalPaginas == 0) totalPaginas = 1; // Mínimo 1 página

            // Asegurar que la página actual esté en rango válido
            if (paginaActual > totalPaginas) paginaActual = totalPaginas;
            if (paginaActual < 1) paginaActual = 1;

            // Actualizar la tabla con los datos de la página actual
            actualizarTablaPaginada();

            // Actualizar el label de paginación
            lblPagina.setText("Página " + paginaActual + " de " + totalPaginas);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar las facturas: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();

            // En caso de error, mostrar página 1 de 1
            lblPagina.setText("Página 1 de 1");
        }
    }
    
    private void actualizarTablaPaginada() {
        modelo.setRowCount(0);

        // Calcular índices de inicio y fin para la página actual
        int inicio = (paginaActual - 1) * registrosPorPagina;
        int fin = Math.min(inicio + registrosPorPagina, totalRegistros);

        // Llenar la tabla solo con los registros de la página actual
        for (int i = inicio; i < fin; i++) {
            Facturas f = todasLasFacturas.get(i);
            modelo.addRow(new Object[]{
                    f.getIdFactura(),
                    f.getIdCliente(),
                    f.getFacDescripcion(),
                    f.getFacFechaHora(),
                    f.getFacFechaPago(),
                    String.format("%.1f", f.getFacSubtotal()),
                    String.format("%.2f", f.getFacIva()),
                    String.format("%.1f", f.getFacTotal()),
                    "Ver", "Editar", "Inhabilitar"
            });
        }
    }
    
    public void recargarDatos() {
        cargarDatosMock();
    }

    // ---------------------------
    // Helpers: Botones en JTable
    // ---------------------------

    interface RowAction {
        void run(int row);
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text, Color bg) {
            setText(text);
            setOpaque(true);
            setBackground(bg);
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
            setFont(new Font("SansSerif", Font.BOLD, 11));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            return this;
        }
    }

    static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private boolean clicked;
        private int row;
        private final RowAction action;

        public ButtonEditor(JCheckBox checkBox, String text, Color bg, RowAction action) {
            super(checkBox);
            this.action = action;

            button = new JButton(text);
            button.setOpaque(true);
            button.setBackground(bg);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
            button.setFont(new Font("SansSerif", Font.BOLD, 11));
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            button.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    clicked = true;
                    fireEditingStopped(); // importante para que dispare getCellEditorValue()
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            this.clicked = false;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                action.run(row);
            }
            clicked = false;
            return button.getText();
        }
    }

    // MAIN de prueba
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FacturasGUI().setVisible(true));
    }
}