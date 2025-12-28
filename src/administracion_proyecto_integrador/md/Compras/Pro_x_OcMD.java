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

        String sql =
            "SELECT * FROM pro_x_oc " +
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
        String sql =
            "SELECT * FROM pro_x_oc " +
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

    // ===================== PRECIO DE COMPRA =====================

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

    // ===================== PRECIO DEL DETALLE =====================

    public static double obtenerValorDetalle(String idCompra, String idProducto) {
        String sql =
            "SELECT pxo_valor FROM pro_x_oc " +
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

    // ===================== ACTUALIZAR =====================

    public static boolean actualizarCantidadYSubtotal(
            String idCompra,
            String idProducto,
            int nuevaCantidad,
            double nuevoSubtotal) {

        String sql =
            "UPDATE pro_x_oc SET " +
            "pxo_cantidad = ?, pxo_subtotal = ? " +
            "WHERE id_compra = ? AND id_producto = ? AND estado_pxoc = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, nuevaCantidad);
            ps.setDouble(2, nuevoSubtotal);
            ps.setString(3, idCompra);
            ps.setString(4, idProducto);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }

    // ===================== REGISTRAR =====================

    public static boolean registrarDetalle(Pro_x_Oc detalle) {
        String sql =
            "INSERT INTO pro_x_oc " +
            "(id_compra, id_producto, pxo_cantidad, pxo_valor, pxo_subtotal, estado_pxoc) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, detalle.getIdCompra());
            ps.setString(2, detalle.getIdProducto());
            ps.setInt(3, detalle.getPxoCantidad());
            ps.setDouble(4, detalle.getPxoValor());
            ps.setDouble(5, detalle.getPxoSubtotal());
            ps.setString(6, detalle.getEstadoPxo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }

    // ===================== BORRADO LÓGICO =====================

    public static boolean inhabilitarDetalle(String idCompra, String idProducto) {
        String sql =
            "UPDATE pro_x_oc SET estado_pxoc = 'INA' " +
            "WHERE id_compra = ? AND id_producto = ? AND estado_pxoc = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCompra);
            ps.setString(2, idProducto);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
}
