package administracion_proyecto_integrador.md.Compras;

import administracion_proyecto_integrador.dp.Compras.Compras;
import administracion_proyecto_integrador.conexion.ConexionPostgreSQL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ComprasMD {

    // =========================
    // MAPEO ResultSet → DP
    // =========================
    private static Compras mapearCompras(ResultSet rs) throws SQLException {
        Compras compra = new Compras();

        compra.setIdCompra(rs.getString("id_compra"));
        compra.setIdProveedor(rs.getString("id_proveedor"));
        compra.setOcSubtotal(rs.getDouble("oc_subtotal"));
        compra.setOcIva(rs.getDouble("oc_iva"));
        compra.setEstadoOc(rs.getString("estado_oc"));

        Date fechaHora = rs.getDate("oc_fecha_hora");
        if (fechaHora != null) {
            compra.setOcFechaHora(fechaHora.toLocalDate());
        }

        Date fechaVenc = rs.getDate("oc_fecha_venc");
        if (fechaVenc != null) {
            compra.setOcFechaVenc(fechaVenc.toLocalDate());
        }

        Date fechaPronto = rs.getDate("oc_fecha_pronto");
        if (fechaPronto != null) {
            compra.setOcFechaPronto(fechaPronto.toLocalDate());
        }

        compra.setOcPorDescPronto(rs.getDouble("oc_por_desc_pronto"));
        compra.setOcSaldo(rs.getDouble("oc_saldo"));
        compra.setOcTotal(rs.getDouble("oc_total"));

        return compra;
    }

    // --------------------
    // RF2.1: CREAR COMPRA
    // --------------------
    public boolean crearCompra(Compras compra) {
        String sql = "INSERT INTO compras "
                   + "(id_compra, id_proveedor, oc_subtotal, oc_iva, estado_oc, "
                   + "oc_fecha_hora, oc_fecha_venc, oc_fecha_pronto, oc_por_desc_pronto, oc_saldo, oc_total) "
                   + "VALUES (?, ?, 0, 0, 'ABI', CURRENT_TIMESTAMP, ?, ?, ?, 0, 0)";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, compra.getIdCompra());
            ps.setString(2, compra.getIdProveedor());

            if (compra.getOcFechaVenc() != null) {
                ps.setDate(3, Date.valueOf(compra.getOcFechaVenc()));
            } else {
                ps.setDate(3, null);
            }

            if (compra.getOcFechaPronto() != null) {
                ps.setDate(4, Date.valueOf(compra.getOcFechaPronto()));
            } else {
                ps.setDate(4, null);
            }

            ps.setDouble(5, compra.getOcPorDescPronto());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------
    // CLASE DE UTILIDAD: VERIFICAR EXISTENCIA
    // ---------------------------------------
    public static boolean verificarExistencia(String idCompra) {
        String sql = "SELECT 1 FROM compras WHERE id_compra = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCompra);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }

    // -------------------------
    // RF2.4.1: CONSULTA GENERAL
    // -------------------------
    public static List<Compras> obtenerListadoCompras() {
        List<Compras> lista = new ArrayList<>();

        String sql = "SELECT * FROM compras WHERE estado_oc = 'ABI' ORDER BY id_compra";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCompras(rs));
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return lista;
    }

    // --------------------
    // RF2.2: MODIFICAR
    // --------------------
    public boolean modificarCompra(Compras compra) {
        String sql = "UPDATE compras SET "
                   + "oc_fecha_venc = ?, "
                   + "oc_fecha_pronto = ?, "
                   + "oc_por_desc_pronto = ? "
                   + "WHERE id_compra = ? AND estado_oc = 'ABI'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (compra.getOcFechaVenc() != null) {
                ps.setDate(1, Date.valueOf(compra.getOcFechaVenc()));
            } else {
                ps.setNull(1, java.sql.Types.DATE);
            }

            if (compra.getOcFechaPronto() != null) {
                ps.setDate(2, Date.valueOf(compra.getOcFechaPronto()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }

            ps.setDouble(3, compra.getOcPorDescPronto());
            ps.setString(4, compra.getIdCompra());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            e.printStackTrace();
            return false;
        }
    }

    // --------------------
    // RF2.3: INHABILITAR
    // --------------------
    public boolean eliminarCompra(String idCompra) {
        String sql = "CALL anular_compra(?)";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCompra);
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }

    // ---------------------------------------------
    // RF2.4.2: CONSULTA POR PARÁMETROS
    // ---------------------------------------------
    public List<Compras> obtenerComprasPorParametro(
            String idCompra,
            String idProveedor,
            String estadoOc) {

        List<Compras> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM compras WHERE 1=1");
        List<Object> parametros = new ArrayList<>();

        if (idCompra != null && !idCompra.trim().isEmpty()) {
            sql.append(" AND id_compra = ?");
            parametros.add(idCompra.trim());
        }

        if (idProveedor != null && !idProveedor.trim().isEmpty()) {
            sql.append(" AND id_proveedor = ?");
            parametros.add(idProveedor.trim());
        }

        if (estadoOc != null && !estadoOc.trim().isEmpty()) {
            sql.append(" AND estado_oc = ?");
            parametros.add(estadoOc.trim());
        }

        sql.append(" ORDER BY id_compra");

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearCompras(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return lista;
    }

    /**
     * Genera el siguiente ID de compra en formato OC0001, OC0002, etc.
     * MISMA lógica que FacturasMD.
     */
    public static String generarSiguienteIdCompra() {
        String sql = "SELECT generar_id_compra()";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString(1);
            }

            return "OC0001";

        } catch (SQLException e) {
            System.out.println("Error al generar ID de compra: " + e.getMessage());
            e.printStackTrace();
            return "OC0001";
        }
    }
}