package administracion_proyecto_integrador.gui.Compras;

import administracion_proyecto_integrador.gui.Compras.ConsultaProveedoresGUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import administracion_proyecto_integrador.dp.Compras.Proveedores;
import administracion_proyecto_integrador.gui.Inventarios.ProductosGUI;
import administracion_proyecto_integrador.gui.Facturacion.ClientesGUI;
import administracion_proyecto_integrador.gui.Menu_Principal.MenuPrincipal;
import administracion_proyecto_integrador.gui.Facturacion.FacturasGUI;
import java.util.List;
import java.util.ArrayList;

public class ProveedoresGUI extends JFrame {
    
    // Colores Estandar en la Aplicación
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
    private List<Proveedores> todosLosProveedores = new ArrayList<>();
    
    
    public ProveedoresGUI() {
        setTitle("Catálogo de Proveedores");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        root.add(crearNavbar(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(18, 18, 18, 18));
        root.add(content, BorderLayout.CENTER);

        content.add(crearHeaderContenido(), BorderLayout.NORTH);
        content.add(crearTabla(), BorderLayout.CENTER);
        content.add(crearPaginacion(), BorderLayout.SOUTH);
        cargarDatosProveedores();
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
                abrirMenuPrincipal();
            }
        });

        // Menú Administración con submenús
        JMenu menuAdmin = crearMenu("Administración");

        JMenu subBodega = crearSubMenu("Bodega ▸");
        subBodega.add(crearMenuItem("Productos", e -> abrirProductos()));
        subBodega.add(crearMenuItem("Recepciones", null));

        JMenu subFacturacion = crearSubMenu("Facturación ▸");
        subFacturacion.add(crearMenuItem("Clientes", e -> abrirClientesGUI()));
        subFacturacion.add(crearMenuItem("Facturas", e -> abrirFacturasGUI()));

        JMenu subCompras = crearSubMenu("Compras ▸");
        subCompras.add(crearMenuItem("Proveedores", e -> navegarAProveedores()));
        subCompras.add(crearMenuItem("Órdenes de Compra", e -> navegarACompras()));

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

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(30, 86, 198));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(20, 40, 65));
            }
        });

        if (listener != null) {
            item.addActionListener(listener);
        } else {
            item.addActionListener(e -> {
                System.out.println("Click en: " + texto);
            });
        }

        return item;
    }
    
    private void navegarAProveedores() {
        SwingUtilities.invokeLater(() -> {
            new ProveedoresGUI().setVisible(true);
            dispose();
        });
    }
    
    private void navegarACompras() {
        new ComprasGUI().setVisible(true);
        this.dispose();
    }
    
    private void abrirFacturasGUI() {
        new FacturasGUI().setVisible(true);
        this.dispose();
    }
    
    private void abrirProductos() {
        this.dispose();
        
        SwingUtilities.invokeLater(() -> {
            ProductosGUI productosGUI = new ProductosGUI();
            productosGUI.setVisible(true);
        });
    }
    
    private void abrirClientesGUI() {
        new ClientesGUI().setVisible(true);
        this.dispose();
    }
    
    private void abrirMenuPrincipal() {
        new MenuPrincipal().setVisible(true);
        this.dispose();
    }

    private JComponent crearHeaderContenido() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        JPanel barra = new JPanel();
        barra.setBackground(AZUL_VER);
        barra.setPreferredSize(new Dimension(5, 30));

        JLabel titulo = new JLabel(" Catálogo de Proveedores");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(20, 20, 20));

        leftPanel.add(barra);
        leftPanel.add(titulo);

        top.add(leftPanel, BorderLayout.WEST);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        acciones.setOpaque(false);

        JButton btnRecargar = crearBotonSuperior("Recargar");
        JButton btnConsulta = crearBotonSuperior("Consulta Por Parámetro");
        JButton btnNuevo = crearBotonSuperior("Nuevo Proveedor");

        btnRecargar.addActionListener(e -> recargarDatos());
        btnConsulta.addActionListener(e -> onConsultaPorParametro());
        btnNuevo.addActionListener(e -> onNuevoProveedor());

        acciones.add(btnRecargar);
        acciones.add(btnConsulta);
        acciones.add(btnNuevo);

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
                "ID Proveedor", "Nombre Proveedor", "Cédula/RUC", "Teléfono", 
                "Correo", "País", "Dirección", "Ciudad",
                "Editar", "Inhabilitar"
        };

        modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                return col >= 8; // Solo las columnas de botones son editables
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader th = tabla.getTableHeader();
        th.setFont(new Font("SansSerif", Font.BOLD, 12));
        th.setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(0).setCellRenderer(center);
        tabla.getColumnModel().getColumn(2).setCellRenderer(center);
        tabla.getColumnModel().getColumn(3).setCellRenderer(center);
        tabla.getColumnModel().getColumn(5).setCellRenderer(center);
        configurarColumnaBoton(8, "Editar", NARANJA_EDITAR, (row) -> onEditar(row));
        configurarColumnaBoton(9, "Inhabilitar", ROJO_INH, (row) -> onInhabilitar(row));

        tabla.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(220);
        tabla.getColumnModel().getColumn(7).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(8).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(9).setPreferredWidth(95);

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
        lblPagina = new JLabel("Página 1 de 1");
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

    private void onConsultaPorParametro() {
        System.out.println("Click: Consulta Por Parámetro");
        SwingUtilities.invokeLater(() -> {
            ConsultaProveedoresGUI consultaDialog = new ConsultaProveedoresGUI(this);
            consultaDialog.setVisible(true);
        });
    }

    private void onNuevoProveedor() {
        System.out.println("Click: Nuevo Proveedor");
        SwingUtilities.invokeLater(() -> {
            new NuevoProveedorGUI().setVisible(true);
            dispose();
        });
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

    private void onEditar(int row) {
        String idProveedor = String.valueOf(modelo.getValueAt(row, 0));
        System.out.println("Editar proveedor: " + idProveedor);
        SwingUtilities.invokeLater(() -> {
            ModificarProveedorGUI modificarVentana = new ModificarProveedorGUI(idProveedor);
            modificarVentana.setVisible(true);
            dispose();
        });
    }

    private void onInhabilitar(int row) {
        String idProveedor = String.valueOf(modelo.getValueAt(row, 0));
        String nombreProveedor = String.valueOf(modelo.getValueAt(row, 1));

        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea inhabilitar al proveedor: " + nombreProveedor + "?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean resultado = Proveedores.eliminarProveedor(idProveedor);

            if (resultado) {
                JOptionPane.showMessageDialog(
                    this,
                    "Registro eliminado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
                cargarDatosProveedores();
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

    private void cargarDatosProveedores() {
        try {
            todosLosProveedores = Proveedores.obtenerProveedores();

            totalRegistros = todosLosProveedores.size();
            totalPaginas = (int) Math.ceil((double) totalRegistros / registrosPorPagina);
            if (totalPaginas == 0) totalPaginas = 1;

            if (paginaActual > totalPaginas) paginaActual = totalPaginas;
            if (paginaActual < 1) paginaActual = 1;

            actualizarTablaPaginada();
            lblPagina.setText("Página " + paginaActual + " de " + totalPaginas);

            if (totalRegistros == 0) {
                JOptionPane.showMessageDialog(this,
                    "No se encontraron resultados para la búsqueda.",
                    "Sin Datos",
                    JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "No se pudo completar la operación. Intente de nuevo.",
                "Error", 
                JOptionPane.ERROR_MESSAGE);

            lblPagina.setText("Página 1 de 1");
            modelo.setRowCount(0);
        }
    }
    
    /**
     * Actualiza la tabla con los resultados de una búsqueda específica
     * Este método es llamado desde ConsultaProveedoresGUI
     */
    public void actualizarTablaConResultados(List<Proveedores> resultados) {
        todosLosProveedores = new ArrayList<>(resultados);
        
        totalRegistros = todosLosProveedores.size();
        totalPaginas = (int) Math.ceil((double) totalRegistros / registrosPorPagina);
        if (totalPaginas == 0) totalPaginas = 1;
        
        paginaActual = 1; // Resetear a la primera página
        
        actualizarTablaPaginada();
        lblPagina.setText("Página " + paginaActual + " de " + totalPaginas);
    }
    
    private void actualizarTablaPaginada() {
        modelo.setRowCount(0);
        int inicio = (paginaActual - 1) * registrosPorPagina;
        int fin = Math.min(inicio + registrosPorPagina, totalRegistros);

        for (int i = inicio; i < fin; i++) {
            Proveedores p = todosLosProveedores.get(i);
            modelo.addRow(new Object[]{
                    p.getIdProveedor(),
                    p.getPrvNombre(),
                    p.getPrvRucCed(),
                    p.getPrvTelefono(),
                    p.getPrvMail(),
                    p.getPrvPais(),
                    p.getPrvDireccion(),
                    p.getIdCiudad(),
                    "Editar", 
                    "Inhabilitar"
            });
        }
    }
    
    public void recargarDatos() {
        cargarDatosProveedores();
    }

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
                    fireEditingStopped();
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
}