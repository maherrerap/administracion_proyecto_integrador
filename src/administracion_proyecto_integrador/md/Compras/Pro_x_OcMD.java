package administracion_proyecto_integrador.md.Compras;

import administracion_proyecto_integrador.conexion.ConexionPostgreSQL;
import administracion_proyecto_integrador.dp.Compras.Pro_x_Oc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Pro_x_OcMD {
    
    // Metodo de Mapeo de Tabla Pro_x_Oc (Detalle de Compras)

    private static Pro_x_Oc mapearDetalle(ResultSet rs) throws SQLException {
        Pro_x_Oc detalle = new Pro_x_Oc();

        detalle.setIdCompra(rs.getString("id_compra"));
        detalle.setIdProducto(rs.getString("id_producto"));
        detalle.setPxoCantidad(rs.getInt("pxo_cantidad"));
        detalle.setPxoValor(rs.getDouble("pxo_valor"));
        detalle.setPxoSubtotal(rs.getDouble("pxo_subtotal"));
        detalle.setEstadoPxoc(rs.getString("estado_pxoc")); 

        return detalle;
    }

    // -------------------------------------------
    // VERIFICAR QUE COMPRA ESTA ACTIVA (CABECERA)
    // -------------------------------------------
    
    private static boolean compraEstaActiva(Connection conn, String idCompra) throws SQLException {
        String sql = "SELECT 1 FROM compras WHERE id_compra = ? AND estado_oc = 'ACT'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idCompra);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // --------------------------------------------------------------------------
    // ACTUALIZACIÓN DE CABECERA EN CASO DE INSERCION / MODIFICACION DE PRODUCTOS
    // --------------------------------------------------------------------------
    
    private static boolean actualizarTotalesCabecera(Connection conn, String idCompra) throws SQLException {
        String sqlCalcular =
                "SELECT COALESCE(SUM(pxo_subtotal), 0) AS total_subtotal " +
                "FROM pro_x_oc " +
                "WHERE id_compra = ? AND estado_pxoc = 'ACT'";

        String sqlActualizar =
                "UPDATE compras SET " +
                "oc_subtotal = ?, " +
                "oc_iva = ?, " +
                "oc_total = ?, " +
                "oc_saldo = ? " +
                "WHERE id_compra = ?";

        double subtotal = 0.0;

        try (PreparedStatement psCalc = conn.prepareStatement(sqlCalcular)) {
            psCalc.setString(1, idCompra);

            try (ResultSet rs = psCalc.executeQuery()) {
                if (rs.next()) {
                    subtotal = rs.getDouble("total_subtotal");
                }
            }
        }

        double porcentajeIVA = 0.15;
        double iva = subtotal * porcentajeIVA;
        double total = subtotal + iva;
        double saldo = total;

        try (PreparedStatement psUpd = conn.prepareStatement(sqlActualizar)) {
            psUpd.setDouble(1, subtotal);
            psUpd.setDouble(2, iva);
            psUpd.setDouble(3, total);
            psUpd.setDouble(4, saldo);
            psUpd.setString(5, idCompra);

            return psUpd.executeUpdate() > 0;
        }
    }
    
    // -----------------
    // CONSULTA GENERAL
    // -----------------
    
    public static List<Pro_x_Oc> obtenerListadoDetallesCompra(String idCompra) {
        List<Pro_x_Oc> lista = new ArrayList<>();
        String sql = "SELECT * FROM pro_x_oc WHERE id_compra = ? AND estado_pxoc = 'ACT' ORDER BY id_producto";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCompra);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearDetalle(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return lista;
    }

    // ----------------------------------------------
    // OBTENER PRODUCTO ESPECÍFICO DE ORDEN DE COMPRA
    // ----------------------------------------------
    
    public static Pro_x_Oc obtenerDetalle(String idCompra, String idProducto) {
        String sql = "SELECT * FROM pro_x_oc WHERE id_compra = ? AND id_producto = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCompra);
            ps.setString(2, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearDetalle(rs);
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return null;
    }
    
    // ---------------------------------------------------------------
    // OBTENER VALOR DE PRODUCTO PARA EL DETALLE DE LA TABLA PRODUCTOS
    // ---------------------------------------------------------------
    
    public static double obtenerValorCompraProducto(String idProducto) {
        String sql = "SELECT pro_valor_compra FROM productos WHERE id_producto = ? AND estado_prod = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return 0.0;
    }
    
    // -----------------------------------------------------------------
    // OBTENER EL NOMBRE DEL PRODUCTO PARA FACILITAR LA SELECCIÓN EN GUI
    // -----------------------------------------------------------------
    
    public static String obtenerNombreProducto(String idProducto) {
        String sql = "SELECT pro_descripcion FROM productos WHERE id_producto = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("pro_descripcion");
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return "Producto no encontrado";
    }
    
    // -------------------------------------------------------
    // OBTENER EL VALOR DEL PRODUCTO PARA CALCULAR EL SUBTOTAL
    // -------------------------------------------------------
    
    public static double obtenerValorDetalle(String idCompra, String idProducto) {
        String sql = "SELECT pxo_valor FROM pro_x_oc WHERE id_compra = ? AND id_producto = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCompra);
            ps.setString(2, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return 0.0;
    }
    
    // -------------------------------------------------------
    // VERIFICAR EXISTENCIA DEL PRODUCTO EN LA ORDEN DE COMPRA
    // -------------------------------------------------------
    public static boolean verificarExistencia(String idCompra, String idProducto) {
        String sql = "SELECT 1 FROM pro_x_oc WHERE id_compra = ? AND id_producto = ? AND estado_pxoc = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCompra);
            ps.setString(2, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    // ------------------------------------------------------------------------
    // OBTENER CANTIDAD ACTUAL DE PRODUCTO EN EL DETALLE DE LA ORDEN DE COMPRA
    // ------------------------------------------------------------------------
    
    public static int obtenerCantidadActual(String idCompra, String idProducto) {
        String sql = "SELECT pxo_cantidad FROM pro_x_oc WHERE id_compra = ? AND id_producto = ? AND estado_pxoc = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCompra);
            ps.setString(2, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("pxo_cantidad");
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return 0;
    }

    // ----------------------------------------
    // REGISTRAR PRODUCTO EN LA ORDEN DE COMPRA
    // ----------------------------------------
    
    public static boolean registrarDetalle(Pro_x_Oc detalle) {
        String sql = "INSERT INTO pro_x_oc (id_compra, id_producto, pxo_cantidad, pxo_valor, pxo_subtotal, estado_pxoc) " +
                     "VALUES (?,?,?,?,?,?)";

        Connection conn = null;
        try {
            conn = ConexionPostgreSQL.getConnection();
            conn.setAutoCommit(false);

            if (!compraEstaActiva(conn, detalle.getIdCompra())) {
                conn.rollback();
                System.out.println("No se pudo completar la operación. Intente de nuevo.");
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, detalle.getIdCompra());
                ps.setString(2, detalle.getIdProducto());
                ps.setInt(3, detalle.getPxoCantidad());
                ps.setDouble(4, detalle.getPxoValor());
                ps.setDouble(5, detalle.getPxoSubtotal());
                ps.setString(6, detalle.getEstadoPxoc());

                int r = ps.executeUpdate();

                if (r > 0) {
                    boolean cabeceraOk = actualizarTotalesCabecera(conn, detalle.getIdCompra());
                    if (cabeceraOk) {
                        conn.commit();
                        return true;
                    }
                }

                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("No se pudo completar la operación. Intente de nuevo.");
            }
        }
    }
    
    // ----------------------------------------------------------------------------------
    // ACTUALIZAR EL DETALLE DE LA ORDEN DE COMPRA POR INGRESO DE PRODUCTO / MODIFICACION
    // ----------------------------------------------------------------------------------
    
    public static boolean actualizarCantidadYSubtotal(String idCompra, String idProducto, int nuevaCantidad, double nuevoSubtotal) {
        String sql =
                "UPDATE pro_x_oc SET " +
                "pxo_cantidad = ?, " +
                "pxo_subtotal = ? " +
                "WHERE id_compra = ? AND id_producto = ? AND estado_pxoc = 'ACT'";

        Connection conn = null;
        try {
            conn = ConexionPostgreSQL.getConnection();
            conn.setAutoCommit(false);

            if (!compraEstaActiva(conn, idCompra)) {
                conn.rollback();
                System.out.println("No se pudo completar la operación. Intente de nuevo.");
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, nuevaCantidad);
                ps.setDouble(2, nuevoSubtotal);
                ps.setString(3, idCompra);
                ps.setString(4, idProducto);

                int r = ps.executeUpdate();
                if (r > 0) {
                    boolean cabeceraOk = actualizarTotalesCabecera(conn, idCompra);
                    if (cabeceraOk) {
                        conn.commit();
                        return true;
                    }
                }

                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("No se pudo completar la operación. Intente de nuevo.");
            }
        }
    }
    
    public static boolean actualizarCantidadDetalle(String idCompra, String idProducto, int nuevaCantidad) {
        String sql = "UPDATE pro_x_oc SET " +
                     "pxo_cantidad = ?, " +
                     "pxo_subtotal = pxo_valor * ? " +
                     "WHERE id_compra = ? AND id_producto = ? AND estado_pxoc = 'ACT'";

        Connection conn = null;
        try {
            conn = ConexionPostgreSQL.getConnection();
            conn.setAutoCommit(false);

            if (!compraEstaActiva(conn, idCompra)) {
                conn.rollback();
                System.out.println("No se pudo completar la operación. Intente de nuevo.");
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, nuevaCantidad);
                ps.setInt(2, nuevaCantidad);
                ps.setString(3, idCompra);
                ps.setString(4, idProducto);

                int r = ps.executeUpdate();
                if (r > 0) {
                    boolean cabeceraOk = actualizarTotalesCabecera(conn, idCompra);
                    if (cabeceraOk) {
                        conn.commit();
                        return true;
                    }
                }

                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("No se pudo completar la operación. Intente de nuevo.");
            }
        }
    }
    
    // --------------------------------------------------------
    // METODOS AUXILIARES PARA CALCULAR CANTIDADES Y SUBTOTALES
    // --------------------------------------------------------
    
    public static boolean actualizarCantidad(String idCompra, String idProducto, int nuevaCantidad, double valorUnitario) {
        double nuevoSubtotal = nuevaCantidad * valorUnitario;
        return actualizarCantidadYSubtotal(idCompra, idProducto, nuevaCantidad, nuevoSubtotal);
    }

    public static boolean sumarCantidad(String idCompra, String idProducto, int cantidad, double valorUnitario) {
        int actual = obtenerCantidadActual(idCompra, idProducto);
        int nueva = actual + cantidad;
        return actualizarCantidad(idCompra, idProducto, nueva, valorUnitario);
    }

    public static boolean incrementarCantidad(String idCompra, String idProducto, int paso) {
        if (paso <= 0) paso = 1;
        int actual = obtenerCantidadActual(idCompra, idProducto);
        int nueva = actual + paso;

        double valorUnitario = obtenerValorDetalle(idCompra, idProducto);
        return actualizarCantidad(idCompra, idProducto, nueva, valorUnitario);
    }

    public static boolean decrementarCantidad(String idCompra, String idProducto, int paso) {
        if (paso <= 0) paso = 1;
        int actual = obtenerCantidadActual(idCompra, idProducto);
        int nueva = actual - paso;

        if (nueva <= 0) {
            return eliminarDetalle(idCompra, idProducto);
        }

        double valorUnitario = obtenerValorDetalle(idCompra, idProducto);
        return actualizarCantidad(idCompra, idProducto, nueva, valorUnitario);
    }
    
    // -----------------------------------------------
    // ELIMINAR PRODUCTO DE DETALLE DE ORDEN DE COMPRA
    // -----------------------------------------------
    
    public static boolean eliminarDetalle(String idCompra, String idProducto) {
        String sql = "DELETE FROM pro_x_oc WHERE id_compra = ? AND id_producto = ?";

        Connection conn = null;
        try {
            conn = ConexionPostgreSQL.getConnection();
            conn.setAutoCommit(false);

            if (!compraEstaActiva(conn, idCompra)) {
                conn.rollback();
                System.out.println("La orden de compra no está activa.");
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, idCompra);
                ps.setString(2, idProducto);

                int r = ps.executeUpdate();
                if (r > 0) {
                    boolean cabeceraOk = actualizarTotalesCabecera(conn, idCompra);
                    if (cabeceraOk) {
                        conn.commit();
                        return true;
                    }
                }

                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            return false;

        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println();
            }
        }
    }
}