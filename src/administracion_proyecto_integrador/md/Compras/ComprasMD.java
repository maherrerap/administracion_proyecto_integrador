package administracion_proyecto_integrador.md.Compras;

import administracion_proyecto_integrador.conexion.ConexionPostgreSQL;
import administracion_proyecto_integrador.dp.Compras.Compras;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ComprasMD {

    // Metodo de Mapeo de Tabla Compras
    private static Compras mapearCompras(ResultSet rs) throws SQLException {
        Compras compra = new Compras();

        compra.setIdCompra(rs.getString("id_compra"));
        compra.setIdProveedor(rs.getString("id_proveedor"));
        compra.setOcSubtotal(rs.getDouble("oc_subtotal"));
        compra.setOcIva(rs.getDouble("oc_iva"));
        compra.setOcTotal(rs.getDouble("oc_total"));
        compra.setOcSaldo(rs.getDouble("oc_saldo"));
        compra.setEstadoOc(rs.getString("estado_oc"));

        Timestamp fh = rs.getTimestamp("oc_fecha_hora");
        if (fh != null) {
            compra.setOcFechaHora(fh.toLocalDateTime().toLocalDate());
        }

        Timestamp fv = rs.getTimestamp("oc_fecha_venc");
        if (fv != null) {
            compra.setOcFechaVenc(fv.toLocalDateTime().toLocalDate());
        }

        Timestamp fp = rs.getTimestamp("oc_fecha_pronto");
        if (fp != null) {
            compra.setOcFechaPronto(fp.toLocalDateTime().toLocalDate());
        }

        compra.setOcPorDescPronto(rs.getInt("oc_por_desc_pronto"));

        return compra;
    }

    // --------------------------------
    // RF2.1: REGISTRAR ORDEN DE COMPRA
    // --------------------------------
    public static boolean crearCompra(Compras compra) {
        String sql =
            "INSERT INTO compras " +
            "(id_compra, id_proveedor, oc_subtotal, oc_iva, estado_oc, " +
            " oc_fecha_hora, oc_fecha_venc, oc_fecha_pronto, oc_por_desc_pronto, oc_saldo, oc_total) " +
            "VALUES (?, ?, ?, ?, 'ACT', CURRENT_TIMESTAMP, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, compra.getIdCompra());
            ps.setString(2, compra.getIdProveedor());
            ps.setDouble(3, compra.getOcSubtotal());
            ps.setDouble(4, compra.getOcIva());

            ps.setDate(5, compra.getOcFechaVenc() != null
                    ? Date.valueOf(compra.getOcFechaVenc()) : null);

            ps.setDate(6, compra.getOcFechaPronto() != null
                    ? Date.valueOf(compra.getOcFechaPronto()) : null);

            ps.setInt(7, (int) Math.round(compra.getOcPorDescPronto()));
            ps.setDouble(8, compra.getOcSaldo());
            ps.setDouble(9, compra.getOcTotal());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }

    // ----------------------------------------
    // METODO DE UTILIDAD: VERIFICAR EXISTENCIA
    // ----------------------------------------
    public static boolean verificarExistencia(String idCompra) {
        String sql = "SELECT 1 FROM compras WHERE id_compra = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCompra);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            return false;
        }
    }

    
    // -------------------------
    // RF2.4.1: CONSULTA GENERAL
    // -------------------------
    
    public static List<Compras> obtenerListadoCompras() {
        List<Compras> lista = new ArrayList<>();
        String sql = "SELECT * FROM compras WHERE estado_oc = 'ACT' ORDER BY id_compra";

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

    //-------------------------------------------------------
    // METODO PARA GUI PARA MOSTRAR UNA UNICA ORDEN DE COMPRA
    //-------------------------------------------------------
    
    public static Compras obtenerCompraPorId(String idCompra) {
        String sql = "SELECT * FROM compras WHERE id_compra = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCompra);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCompras(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return null;
    }

    // --------------------------------
    // RF2.2: MODIFICAR ORDEN DE COMPRA
    // --------------------------------
    
    public static boolean modificarCompra(Compras compra) {
        String sql =
            "UPDATE compras SET " +
            " oc_fecha_venc = ?, " +           
            " oc_subtotal = ?, " +             
            " oc_iva = ?, " +                  
            " oc_total = ?, " +                
            " oc_saldo = ?, " +                
            " id_proveedor = ? " +             
            "WHERE id_compra = ?";             

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, compra.getOcFechaVenc() != null
                    ? Date.valueOf(compra.getOcFechaVenc()) : null);
            ps.setDouble(2, compra.getOcSubtotal());
            ps.setDouble(3, compra.getOcIva());
            ps.setDouble(4, compra.getOcTotal());
            ps.setDouble(5, compra.getOcSaldo());
            ps.setString(6, compra.getIdProveedor());
            ps.setString(7, compra.getIdCompra());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }

    // ----------------------------------
    // RF2.3: INHABILITAR ORDEN DE COMPRA
    // ----------------------------------
    
    // Cabecera
    public static boolean inhabilitarOrdenCompra(String idCompra) {
        String sql = "UPDATE compras SET estado_oc = 'INA' WHERE id_compra = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCompra);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    // Detalles
    public static boolean inhabilitarOrdenCompraCompleta(String idCompra) {

        String sqlDetalle =
            "UPDATE pro_x_oc " +
            "SET estado_pxoc = 'INA' " +
            "WHERE id_compra = ? AND estado_pxoc = 'ACT'";

        String sqlCabecera =
            "UPDATE compras " +
            "SET estado_oc = 'INA' " +
            "WHERE id_compra = ? AND estado_oc = 'ACT'";

        Connection conn = null;

        try {
            conn = ConexionPostgreSQL.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psDet = conn.prepareStatement(sqlDetalle)) {
                psDet.setString(1, idCompra);
                psDet.executeUpdate();
            }

            try (PreparedStatement psCab = conn.prepareStatement(sqlCabecera)) {
                psCab.setString(1, idCompra);
                int r = psCab.executeUpdate();

                if (r > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) {}
            return false;

        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ignored) {}
        }
    }
    
    // -----------------------------------------------------
    // RF2.4.2: CONSULTA POR PARÁMETROS DE ÓRDENES DE COMPRA
    // -----------------------------------------------------
    public static List<Compras> obtenerOrdenCompraPorParametros(
            String idCompra,
            String idProveedor,
            String fechaEmision
    ) {
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

        if (fechaEmision != null && !fechaEmision.trim().isEmpty()) {
            sql.append(" AND DATE(oc_fecha_hora) = ?");
            parametros.add(Date.valueOf(fechaEmision));
        }

        sql.append(" AND estado_oc = 'ACT' ");
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
     * Genera el siguiente ID de orden de compra en formato OC00001, OC00002 , etc.
     */
    public static String generarSiguienteIdCompra() {
        String sql = "SELECT generar_id_compra()";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return "OC00001";
    }

    /**
     *  Query para actualizar la cabecera de la Orden de Compra en caso de 
     * modificación de detalles
     */

    public static boolean actualizarTotalesCabecera(
            String idCompra,
            double subtotal,
            double iva,
            double total,
            double saldo
    ) {
        String sql =
            "UPDATE compras SET " +
            " oc_subtotal = ?, " +
            " oc_iva = ?, " +
            " oc_total = ?, " +
            " oc_saldo = ? " +
            "WHERE id_compra = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, subtotal);
            ps.setDouble(2, iva);
            ps.setDouble(3, total);
            ps.setDouble(4, saldo);
            ps.setString(5, idCompra);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
}