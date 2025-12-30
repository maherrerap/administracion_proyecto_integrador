package administracion_proyecto_integrador.gui.Compras;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import administracion_proyecto_integrador.dp.Compras.Compras;
import java.util.List;
import java.util.ArrayList;

public class ComprasGUI extends JFrame {

    private static final Color NAVY = new Color(8, 26, 43);
    private static final Color NAVY_BTN = new Color(14, 33, 55);
    private static final Color AZUL_VER = new Color(30, 86, 198);
    private static final Color NARANJA_EDITAR = new Color(244, 150, 30);
    private static final Color ROJO_INH = new Color(217, 64, 64);

    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblPagina;

    private int paginaActual = 1;
    private int registrosPorPagina = 13;
    private int totalRegistros = 0;
    private int totalPaginas = 0;

    private List<Compras> todasLasCompras = new ArrayList<>();

    public ComprasGUI() {
        setTitle("Catálogo de Órdenes de Compra");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        cargarDatos();
    }

    private JComponent crearNavbar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(NAVY);
        nav.setBorder(new EmptyBorder(8, 16, 8, 16));

        JLabel titulo = new JLabel("  Módulo de Compras");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));

        nav.add(titulo, BorderLayout.WEST);
        return nav;
    }

    private JComponent crearHeaderContenido() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel titulo = new JLabel(" Catálogo de Órdenes de Compra");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        acciones.setOpaque(false);

        JButton btnRecargar = crearBotonSuperior("Recargar");
        JButton btnConsulta = crearBotonSuperior("Consulta Por Parámetro");
        JButton btnCrear = crearBotonSuperior("Crear Orden");

        btnRecargar.addActionListener(e -> cargarDatos());
        btnConsulta.addActionListener(e -> new ConsultaComprasGUI(this).setVisible(true));
        btnCrear.addActionListener(e -> {
            new CrearCompraGUI().setVisible(true);
            dispose();
        });

        acciones.add(btnRecargar);
        acciones.add(btnConsulta);
        acciones.add(btnCrear);

        top.add(titulo, BorderLayout.WEST);
        top.add(acciones, BorderLayout.EAST);

        return top;
    }

    private JButton crearBotonSuperior(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(NAVY_BTN);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        return b;
    }

    private JComponent crearTabla() {
        String[] cols = {
            "Orden Compra", "Proveedor", "Fecha Emisión", "Fecha Venc.",
            "Subtotal", "IVA", "Total", "Ver", "Editar", "Inhabilitar"
        };

        modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                return col >= 7;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(28);

        configurarColumnaBoton(7, "Ver", AZUL_VER, this::onVer);
        configurarColumnaBoton(8, "Editar", NARANJA_EDITAR, this::onEditar);
        configurarColumnaBoton(9, "Inhabilitar", ROJO_INH, this::onInhabilitar);

        return new JScrollPane(tabla);
    }

    private void configurarColumnaBoton(int colIndex, String texto, Color color, RowAction action) {
        TableColumn col = tabla.getColumnModel().getColumn(colIndex);
        col.setCellRenderer(new ButtonRenderer(texto, color));
        col.setCellEditor(new ButtonEditor(new JCheckBox(), texto, color, action));
    }

    private JComponent crearPaginacion() {
        JPanel pag = new JPanel(new FlowLayout());
        lblPagina = new JLabel();

        JButton ant = new JButton("<");
        JButton sig = new JButton(">");

        ant.addActionListener(e -> paginaAnterior());
        sig.addActionListener(e -> paginaSiguiente());

        pag.add(ant);
        pag.add(lblPagina);
        pag.add(sig);

        return pag;
    }

    private void cargarDatos() {
        try {
            todasLasCompras = Compras.obtenerCompras();
            totalRegistros = todasLasCompras.size();
            totalPaginas = (int) Math.ceil((double) totalRegistros / registrosPorPagina);
            if (totalPaginas == 0) totalPaginas = 1;
            paginaActual = 1;
            actualizarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar órdenes", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTabla() {
        modelo.setRowCount(0);
        int inicio = (paginaActual - 1) * registrosPorPagina;
        int fin = Math.min(inicio + registrosPorPagina, totalRegistros);

        for (int i = inicio; i < fin; i++) {
            Compras c = todasLasCompras.get(i);
            modelo.addRow(new Object[]{
                c.getIdCompra(),
                c.getIdProveedor(),
                c.getOcFechaHora(),
                c.getOcFechaVenc(),
                c.getOcSubtotal(),
                c.getOcIva(),
                c.getOcTotal(),
                "Ver", "Editar", "Inhabilitar"
            });
        }

        lblPagina.setText("Página " + paginaActual + " de " + totalPaginas);
    }

    private void paginaAnterior() {
        if (paginaActual > 1) {
            paginaActual--;
            actualizarTabla();
        }
    }

    private void paginaSiguiente() {
        if (paginaActual < totalPaginas) {
            paginaActual++;
            actualizarTabla();
        }
    }

    private void onVer(int row) {
        String id = String.valueOf(modelo.getValueAt(row, 0));
        new DetalleCompraGUI(id).setVisible(true);
        dispose();
    }

    private void onEditar(int row) {
        String id = String.valueOf(modelo.getValueAt(row, 0));
        new ModificarCompraGUI(id).setVisible(true);
        dispose();
    }

    private void onInhabilitar(int row) {
        String id = String.valueOf(modelo.getValueAt(row, 0));
        if (Compras.eliminarCompra(id)) {
            cargarDatos();
        }
    }
    
    public void actualizarTablaConResultados(List<administracion_proyecto_integrador.dp.Compras.Compras> resultados) {
        todasLasCompras = resultados;
        totalRegistros = resultados.size();
        totalPaginas = (int) Math.ceil((double) totalRegistros / registrosPorPagina);
        if (totalPaginas == 0) totalPaginas = 1;

        paginaActual = 1;
        enModoBusqueda = true;

        actualizarTablaPaginada();
        lblPagina.setText("Página " + paginaActual + " de " + totalPaginas + " (Búsqueda)");
    }

    interface RowAction {
        void run(int row);
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text, Color bg) {
            setText(text);
            setBackground(bg);
            setForeground(Color.WHITE);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final RowAction action;
        private int row;

        public ButtonEditor(JCheckBox checkBox, String text, Color bg, RowAction action) {
            super(checkBox);
            this.action = action;
            button = new JButton(text);
            button.setBackground(bg);
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            action.run(row);
            return button.getText();
        }
    }
}
