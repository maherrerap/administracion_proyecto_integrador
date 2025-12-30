package administracion_proyecto_integrador.md.Compras;

import administracion_proyecto_integrador.dp.Compras.Pro_x_Oc;
import administracion_proyecto_integrador.conexion.ConexionPostgreSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class Pro_x_OcMD {

    // ===================== MAPEO =====================
    private static Pro_x_Oc mapearDetalle(ResultSet rs) throws SQLException {
        Pro_x_Oc detalle = new Pro_x_Oc();

        detalle.setIdCompra(rs.getString("id_compra"));
        detalle.setIdProducto(rs.getString("id_producto"));
        detalle.setPxoCantidad(rs.getInt("pxo_cantidad"));
        detalle.setPxoValor(rs.getDouble("pxo_valor"));
        detalle.setPxoSubtotal(rs.getDouble("pxo_subtotal"));
        detalle.setEstadoPxo(rs.getString("estado_pxoc"));

        return detalle;
    }

    // ===================== LISTAR DETALLES =====================
    public static List<Pro_x_Oc> obtenerListadoDetallesCompra(String idCompra) {
        List<Pro_x_Oc> lista = new ArrayList<>();

        String sql = "SELECT * FROM pro_x_oc " +
                     "WHERE id_compra = ? AND estado_pxoc = 'ACT' " +
                     "ORDER BY id_producto";

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

    // ===================== DETALLE ESPECÍFICO =====================
    public static Pro_x_Oc obtenerDetalle(String idCompra, String idProducto) {
        String sql = "SELECT * FROM pro_x_oc " +
                     "WHERE id_compra = ? AND id_producto = ? AND estado_pxoc = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCompra);
            ps.setString(2, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearDetalle(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return null;
    }

    // ===================== REGISTRAR DETALLE =====================
    public static boolean registrarDetalle(Pro_x_Oc detalle) {
        String sql = "INSERT INTO pro_x_oc " +
                     "(id_compra, id_producto, pxo_cantidad, pxo_valor, pxo_subtotal, estado_pxoc) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexionPostgreSQL.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, detalle.getIdCompra());
                ps.setString(2, detalle.getIdProducto());
                ps.setInt(3, detalle.getPxoCantidad());
                ps.setDouble(4, detalle.getPxoValor());
                ps.setDouble(5, detalle.getPxoSubtotal());
                ps.setString(6, detalle.getEstadoPxo());

                int result = ps.executeUpdate();

                if (result > 0) {
                    boolean cabeceraActualizada =
                            actualizarTotalesCabecera(detalle.getIdCompra());

                    if (cabeceraActualizada) {
                        conn.commit();
                        return true;
                    }
                }

                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Error al revertir transacción: " + ex.getMessage());
            }
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;

        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    // ===================== ACTUALIZAR CANTIDAD =====================
    public static boolean actualizarCantidadYSubtotal(
            String idCompra,
            String idProducto,
            int nuevaCantidad,
            double nuevoSubtotal) {

        String sql = "UPDATE pro_x_oc SET " +
                     "pxo_cantidad = ?, pxo_subtotal = ? " +
                     "WHERE id_compra = ? AND id_producto = ? AND estado_pxoc = 'ACT'";

        Connection conn = null;
        try {
            conn = ConexionPostgreSQL.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, nuevaCantidad);
                ps.setDouble(2, nuevoSubtotal);
                ps.setString(3, idCompra);
                ps.setString(4, idProducto);

                int result = ps.executeUpdate();

                if (result > 0 && actualizarTotalesCabecera(idCompra)) {
                    conn.commit();
                    return true;
                }

                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Error al revertir transacción: " + ex.getMessage());
            }
            return false;

        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    // ===================== INHABILITAR DETALLE =====================
    public static boolean inhabilitarDetalle(String idCompra, String idProducto) {
        String sql = "UPDATE pro_x_oc SET estado_pxoc = 'INA' " +
                     "WHERE id_compra = ? AND id_producto = ? AND estado_pxoc = 'ACT'";

        Connection conn = null;
        try {
            conn = ConexionPostgreSQL.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, idCompra);
                ps.setString(2, idProducto);

                int result = ps.executeUpdate();

                if (result > 0 && actualizarTotalesCabecera(idCompra)) {
                    conn.commit();
                    return true;
                }

                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            return false;

        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    // ===================== ACTUALIZAR CABECERA =====================
    private static boolean actualizarTotalesCabecera(String idCompra) {
        String sqlCalcular =
            "SELECT COALESCE(SUM(pxo_subtotal), 0) AS total_subtotal " +
            "FROM pro_x_oc WHERE id_compra = ? AND estado_pxoc = 'ACT'";

        String sqlActualizar =
            "UPDATE compras SET oc_subtotal = ?, oc_iva = ?, oc_total = ?, oc_saldo = ? " +
            "WHERE id_compra = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement psCalc = conn.prepareStatement(sqlCalcular)) {

            psCalc.setString(1, idCompra);

            try (ResultSet rs = psCalc.executeQuery()) {
                if (rs.next()) {
                    double subtotal = rs.getDouble("total_subtotal");
                    double iva = subtotal * 0.15;
                    double total = subtotal + iva;

                    try (PreparedStatement psUpdate = conn.prepareStatement(sqlActualizar)) {
                        psUpdate.setDouble(1, subtotal);
                        psUpdate.setDouble(2, iva);
                        psUpdate.setDouble(3, total);
                        psUpdate.setDouble(4, total); // saldo inicial = total
                        psUpdate.setString(5, idCompra);

                        return psUpdate.executeUpdate() > 0;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar totales de compra: " + e.getMessage());
        }

        return false;
    }
    
    // ===================== VALOR DE COMPRA DEL PRODUCTO =====================
    public static double obtenerValorCompraProducto(String idProducto) {
        String sql =
            "SELECT pro_precio_compra " +
            "FROM productos " +
            "WHERE id_producto = ? AND estado_prod = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return 0.0;
    }
    
    // ===================== VALOR DEL DETALLE =====================
    public static double obtenerValorDetalle(String idCompra, String idProducto) {
        String sql =
            "SELECT pxo_valor " +
            "FROM pro_x_oc " +
            "WHERE id_compra = ? AND id_producto = ? AND estado_pxoc = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCompra);
            ps.setString(2, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return 0.0;
    }
}