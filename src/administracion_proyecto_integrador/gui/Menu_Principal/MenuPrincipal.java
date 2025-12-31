package administracion_proyecto_integrador.gui.Menu_Principal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {

    // Colores del tema
    private static final Color NAVY = new Color(14, 33, 55);
    private static final Color AZUL_PRIMARY = new Color(30, 86, 198);
    private static final Color AZUL_LIGHT = new Color(59, 130, 246);
    private static final Color GRAY_LIGHT = new Color(243, 244, 246);
    private static final Color GRAY_TEXT = new Color(55, 65, 81);
    private static final Color WHITE = Color.WHITE;

    public MenuPrincipal() {
        setTitle("Menú Principal - ColdMarket");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);

        // Navbar
        mainPanel.add(crearNavbar(), BorderLayout.NORTH);

        // Contenido con scroll
        JScrollPane scrollPane = new JScrollPane(crearContenido());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
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
            JLabel logo = new JLabel("❄");
            logo.setOpaque(true);
            logo.setBackground(new Color(12, 45, 78));
            logo.setForeground(Color.WHITE);
            logo.setHorizontalAlignment(SwingConstants.CENTER);
            logo.setPreferredSize(new Dimension(50, 50));
            logo.setFont(new Font("SansSerif", Font.BOLD, 28));
            left.add(logo);
        }

        // Menús
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(false);
        menuBar.setBackground(NAVY);
        menuBar.setBorder(BorderFactory.createEmptyBorder());

        // Menú Principal - Ya estamos aquí, así que no hace nada
        JMenu menuInicio = crearMenu("Menú Principal");
        menuInicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Ya estamos en el menú principal
                System.out.println("Ya estás en el Menú Principal");
            }
        });

        // Menú Administración con submenús
        JMenu menuAdmin = crearMenu("Administración");

        JMenu subBodega = crearSubMenu("Bodega ▸");
        subBodega.add(crearMenuItem("Productos", e -> abrirProductos()));
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
                int respuesta = JOptionPane.showConfirmDialog(
                    MenuPrincipal.this,
                    "¿Está seguro de que desea salir del sistema?",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (respuesta == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        menuBar.add(menuInicio);
        menuBar.add(menuAdmin);
        menuBar.add(menuSalir);

        left.add(menuBar);
        nav.add(left, BorderLayout.WEST);
        
        return nav;
    }
    
    private void abrirFacturasGUI() {
        // Implementar cuando tengas la clase FacturasGUI
        System.out.println("Abriendo módulo de Facturas...");
        // new FacturasGUI().setVisible(true);
        // this.dispose();
    }
    
    private void abrirProductos() {
        this.dispose();
        
        SwingUtilities.invokeLater(() -> {
            // Asegúrate de tener esta clase importada
            // administracion_proyecto_integrador.gui.Inventarios.ProductosGUI
            try {
                Class<?> productosClass = Class.forName("administracion_proyecto_integrador.gui.Inventarios.ProductosGUI");
                JFrame productosGUI = (JFrame) productosClass.getDeclaredConstructor().newInstance();
                productosGUI.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                    "Error al abrir módulo de Productos: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                // Reabrir el menú principal si falla
                new MenuPrincipal().setVisible(true);
            }
        });
    }
    
    private JMenu crearMenu(String texto) {
        JMenu menu = new JMenu(texto);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("SansSerif", Font.BOLD, 14));
        menu.setOpaque(false);
        menu.setBorderPainted(false);
        menu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPopupMenu popup = menu.getPopupMenu();
        popup.setBackground(NAVY);
        popup.setBorder(BorderFactory.createLineBorder(new Color(40, 60, 80), 1));

        return menu;
    }

    private JMenu crearSubMenu(String texto) {
        JMenu submenu = new JMenu(texto);
        submenu.setForeground(Color.WHITE);
        submenu.setFont(new Font("SansSerif", Font.PLAIN, 13));
        submenu.setOpaque(true);
        submenu.setBackground(NAVY);
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
                item.setBackground(AZUL_PRIMARY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(20, 40, 65));
            }
        });

        if (listener != null) {
            item.addActionListener(listener);
        } else {
            item.addActionListener(e -> {
                JOptionPane.showMessageDialog(MenuPrincipal.this,
                    "Módulo '" + texto + "' en desarrollo",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            });
        }

        return item;
    }

    private JPanel crearContenido() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(40, 60, 40, 60));

        // Hero Section - Bienvenida
        panel.add(crearSeccionBienvenida());
        panel.add(Box.createVerticalStrut(50));

        // Sección Acerca de
        panel.add(crearSeccionAcerca());
        panel.add(Box.createVerticalStrut(40));

        // Sección Fundadores
        panel.add(crearSeccionFundadores());
        panel.add(Box.createVerticalStrut(40));

        // Sección Módulos del Sistema
        panel.add(crearSeccionModulos());
        panel.add(Box.createVerticalStrut(40));

        // Footer
        panel.add(crearFooter());

        return panel;
    }

    private JPanel crearSeccionBienvenida() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // Logo/Ícono (simulado con texto estilizado)
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(WHITE);
        
        JLabel iconoLabel = new JLabel("❄");
        iconoLabel.setFont(new Font("SansSerif", Font.BOLD, 80));
        iconoLabel.setForeground(AZUL_PRIMARY);
        logoPanel.add(iconoLabel);
        
        panel.add(logoPanel);
        panel.add(Box.createVerticalStrut(20));

        // Nombre de la empresa
        JLabel lblEmpresa = new JLabel("ColdMarket");
        lblEmpresa.setFont(new Font("SansSerif", Font.BOLD, 48));
        lblEmpresa.setForeground(NAVY);
        lblEmpresa.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblEmpresa);

        panel.add(Box.createVerticalStrut(15));

        // Slogan
        JLabel lblSlogan = new JLabel("Manteniendo la Calidad al Punto Perfecto");
        lblSlogan.setFont(new Font("SansSerif", Font.ITALIC, 20));
        lblSlogan.setForeground(AZUL_PRIMARY);
        lblSlogan.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblSlogan);

        panel.add(Box.createVerticalStrut(20));

        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Sistema Integral de Administración Empresarial");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblSubtitulo.setForeground(GRAY_TEXT);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblSubtitulo);

        return panel;
    }

    private JPanel crearSeccionAcerca() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(GRAY_LIGHT);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(30, 40, 30, 40)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // Título de la sección
        JLabel titulo = new JLabel("Acerca de ColdMarket");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(NAVY);
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Descripción
        JTextArea descripcion = new JTextArea(
            "ColdMarket es una solución empresarial innovadora diseñada para optimizar " +
            "la gestión de inventarios, facturación y administración de productos. " +
            "Nuestro sistema permite a las empresas mantener un control preciso de sus " +
            "operaciones, garantizando eficiencia y calidad en cada proceso.\n\n" +
            "Con una interfaz intuitiva y herramientas poderosas, facilitamos la toma " +
            "de decisiones estratégicas y el crecimiento sostenible de tu negocio."
        );
        descripcion.setFont(new Font("SansSerif", Font.PLAIN, 15));
        descripcion.setForeground(GRAY_TEXT);
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setEditable(false);
        descripcion.setOpaque(false);
        descripcion.setBorder(null);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(GRAY_LIGHT);
        contenedor.add(titulo, BorderLayout.NORTH);
        contenedor.add(descripcion, BorderLayout.CENTER);

        panel.add(contenedor, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearSeccionFundadores() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        // Título
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setBackground(WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 25, 0));

        JPanel barra = new JPanel();
        barra.setBackground(AZUL_PRIMARY);
        barra.setPreferredSize(new Dimension(5, 32));

        JLabel titulo = new JLabel(" Nuestros Fundadores");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(NAVY);

        headerPanel.add(barra);
        headerPanel.add(titulo);

        // Grid de fundadores
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setBackground(WHITE);

        String[] fundadores = {
            "Matheo Leonardo Iza Proaño",
            "María Paulina Astudillo",
            "José Daniel Zumárraga",
            "Martín Herrera"
        };

        String[] roles = {
            "CEO & Fundador",
            "CFO & Cofundadora",
            "CTO & Cofundador",
            "COO & Cofundador"
        };

        for (int i = 0; i < fundadores.length; i++) {
            gridPanel.add(crearTarjetaFundador(fundadores[i], roles[i]));
        }

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(gridPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearTarjetaFundador(String nombre, String rol) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Ícono de perfil
        JLabel icono = new JLabel("👤");
        icono.setFont(new Font("SansSerif", Font.PLAIN, 40));
        icono.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nombre
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblNombre.setForeground(NAVY);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Rol
        JLabel lblRol = new JLabel(rol);
        lblRol.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblRol.setForeground(AZUL_PRIMARY);
        lblRol.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjeta.add(icono);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(lblNombre);
        tarjeta.add(Box.createVerticalStrut(5));
        tarjeta.add(lblRol);

        // Efecto hover
        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tarjeta.setBackground(GRAY_LIGHT);
                tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tarjeta.setBackground(WHITE);
                tarjeta.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return tarjeta;
    }

    private JPanel crearSeccionModulos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

        // Título
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setBackground(WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 25, 0));

        JPanel barra = new JPanel();
        barra.setBackground(AZUL_PRIMARY);
        barra.setPreferredSize(new Dimension(5, 32));

        JLabel titulo = new JLabel(" Módulos del Sistema");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(NAVY);

        headerPanel.add(barra);
        headerPanel.add(titulo);

        // Grid de módulos
        JPanel gridPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        gridPanel.setBackground(WHITE);

        gridPanel.add(crearTarjetaModulo("📦", "Inventarios", 
            "Gestión completa de productos, stock y movimientos"));
        gridPanel.add(crearTarjetaModulo("🧾", "Facturación", 
            "Emisión y control de facturas de venta"));
        gridPanel.add(crearTarjetaModulo("⚙️", "Administración", 
            "Configuración y gestión del sistema"));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(gridPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearTarjetaModulo(String icono, String nombre, String descripcion) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(AZUL_LIGHT);
        tarjeta.setBorder(new EmptyBorder(25, 20, 25, 20));

        // Ícono
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 48));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nombre
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblNombre.setForeground(WHITE);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Descripción
        JTextArea txtDesc = new JTextArea(descripcion);
        txtDesc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtDesc.setForeground(WHITE);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setOpaque(false);
        txtDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtDesc.setBorder(null);

        tarjeta.add(lblIcono);
        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(lblNombre);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(txtDesc);

        return tarjeta;
    }

    private JPanel crearFooter() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(NAVY);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Línea separadora
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(75, 85, 99));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(25));

        // Información del sistema
        JLabel lblVersion = new JLabel("ColdMarket v1.0 - Sistema Integral de Administración");
        lblVersion.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblVersion.setForeground(WHITE);
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblCopyright = new JLabel("© 2024 ColdMarket. Todos los derechos reservados.");
        lblCopyright.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblCopyright.setForeground(new Color(156, 163, 175));
        lblCopyright.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblContacto = new JLabel("Contacto: info@coldmarket.com | +593 99 999 9999");
        lblContacto.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblContacto.setForeground(new Color(156, 163, 175));
        lblContacto.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblVersion);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblCopyright);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblContacto);

        return panel;
    }

    // Método main para testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MenuPrincipal().setVisible(true);
        });
    }
}