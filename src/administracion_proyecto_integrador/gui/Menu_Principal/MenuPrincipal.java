package administracion_proyecto_integrador.gui.Menu_Principal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import administracion_proyecto_integrador.gui.Inventarios.ProductosGUI;
import administracion_proyecto_integrador.gui.Facturacion.ClientesGUI;
import administracion_proyecto_integrador.gui.Facturacion.FacturasGUI;
import administracion_proyecto_integrador.gui.Compras.ComprasGUI;
import administracion_proyecto_integrador.gui.Compras.ProveedoresGUI;

public class MenuPrincipal extends JFrame {

    // Colores Estandar en la Aplicación
    private static final Color NAVY = new Color(8, 26, 43);
    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color AZUL_VER = new Color(30, 86, 198);
    private static final Color AZUL_LABEL = new Color(30, 86, 198);

    public MenuPrincipal() {
        setTitle("Menú Principal - ColdMarket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Layout principal
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        setContentPane(root);

        // Barra superior
        root.add(crearNavbar(), BorderLayout.NORTH);

        // Centro (contenido)
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(40, 40, 40, 40));
        root.add(content, BorderLayout.CENTER);

        // Contenido principal
        content.add(crearContenidoPrincipal(), BorderLayout.CENTER);
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
    
    private void abrirClientesGUI() {
        new ClientesGUI().setVisible(true);
        this.dispose();
    }

    private void abrirProductos() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            ProductosGUI productosGUI = new ProductosGUI();
            productosGUI.setVisible(true);
        });
    }

    private JComponent crearContenidoPrincipal() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Espaciador superior
        panel.add(Box.createVerticalStrut(60));

        // Logo/Título principal
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("ColdMarket");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 48));
        lblTitulo.setForeground(NAVY);
        headerPanel.add(lblTitulo);
        panel.add(headerPanel);

        panel.add(Box.createVerticalStrut(10));

        // Subtítulo
        JPanel subtituloPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        subtituloPanel.setOpaque(false);
        
        JLabel lblSubtitulo = new JLabel("Sistema de Administración Empresarial");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblSubtitulo.setForeground(new Color(80, 80, 80));
        subtituloPanel.add(lblSubtitulo);
        panel.add(subtituloPanel);

        panel.add(Box.createVerticalStrut(10));

        // Frase descriptiva
        JPanel frasePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        frasePanel.setOpaque(false);
        
        JLabel lblFrase = new JLabel("\"Gestionando el futuro con eficiencia y precisión\"");
        lblFrase.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lblFrase.setForeground(AZUL_VER);
        frasePanel.add(lblFrase);
        panel.add(frasePanel);

        panel.add(Box.createVerticalStrut(50));

        // Sección de información
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(true);
        infoPanel.setBackground(new Color(245, 248, 250));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(30, 40, 30, 40)
        ));
        infoPanel.setMaximumSize(new Dimension(700, 300));

        // Título de información
        JPanel tituloInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tituloInfoPanel.setOpaque(false);
        
        JPanel barraInfo = new JPanel();
        barraInfo.setBackground(AZUL_VER);
        barraInfo.setPreferredSize(new Dimension(5, 24));
        
        JLabel lblTituloInfo = new JLabel(" Acerca de ColdMarket");
        lblTituloInfo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTituloInfo.setForeground(NAVY);
        
        tituloInfoPanel.add(barraInfo);
        tituloInfoPanel.add(lblTituloInfo);
        infoPanel.add(tituloInfoPanel);

        infoPanel.add(Box.createVerticalStrut(20));

        // Descripción
        JLabel lblDescripcion = new JLabel("<html><div style='width: 600px;'>Sistema integral de administración " +
                "empresarial diseñado para optimizar la gestión de inventarios, facturación, " +
                "compras y relaciones comerciales. Desarrollado con tecnología Java Swing.</div></html>");
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(60, 60, 60));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(lblDescripcion);

        infoPanel.add(Box.createVerticalStrut(25));

        // Fundadores
        JLabel lblFundadores = new JLabel("Fundadores:");
        lblFundadores.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblFundadores.setForeground(NAVY);
        lblFundadores.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(lblFundadores);

        infoPanel.add(Box.createVerticalStrut(8));

        String[] fundadores = {
            "• Matheo Iza",
            "• José Daniel Zumárraga", 
            "• María Paulina Astudillo",
            "• Martín Herrera"
        };

        for (String fundador : fundadores) {
            JLabel lblFundador = new JLabel(fundador);
            lblFundador.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lblFundador.setForeground(new Color(60, 60, 60));
            lblFundador.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(lblFundador);
            infoPanel.add(Box.createVerticalStrut(4));
        }

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(infoPanel);
        panel.add(centerPanel);

        panel.add(Box.createVerticalGlue());

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        
        JLabel lblFooter = new JLabel("© 2026 ColdMarket - Todos los derechos reservados");
        lblFooter.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(120, 120, 120));
        footerPanel.add(lblFooter);
        panel.add(footerPanel);

        panel.add(Box.createVerticalStrut(20));

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}