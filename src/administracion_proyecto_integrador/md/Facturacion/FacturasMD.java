package administracion_proyecto_integrador.md.Facturacion;

import administracion_proyecto_integrador.dp.Facturacion.Facturas;
import administracion_proyecto_integrador.conexion.ConexionPostgreSQL;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.util.ArrayList;
import java.util.List;

public class FacturasMD {
    private static Facturas mapearFacturas (ResultSet rs) throws SQLException {
        Facturas factura = new Facturas();
        
        factura.setIdFactura(rs.getString("id_factura"));
        factura.setIdCliente(rs.getString("id_cliente"));
        factura.setFacSubtotal(rs.getDouble("fac_subtotal"));
        factura.setFacIva(rs.getDouble("fac_iva"));
        factura.setEstadoFac(rs.getString("estado_fac"));
        factura.setFacDescripcion(rs.getString("fac_descripcion"));
        
        Date fechaSql = rs.getDate("fac_fecha_hora");
        if (fechaSql != null) {
            factura.setFacFechaHora(fechaSql.toLocalDate());
        }
        
        Date fechaSql2 = rs.getDate("fac_fecha_pago");
        if (fechaSql2 != null) {
            factura.setFacFechaHora(fechaSql2.toLocalDate());
        }
        
        factura.setFacTotal(rs.getDouble("fac_total"));
        return factura;
    }
    
    // --------------------
    // RF5.1: CREAR FACTURA
    // --------------------
    
    public boolean crearFactura(Facturas factura) {
        String sql = "INSERT INTO facturas "
           + "(id_factura, id_cliente, fac_subtotal, fac_iva, estado_fac, "
           + "fac_descripcion, fac_fecha_hora, fac_fecha_pago, fac_total) "
           + "VALUES (?, ?, 0, 0, 'APR', ?, CURRENT_TIMESTAMP, ?, 0)";
        
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, factura.getIdFactura());
            ps.setString(2, factura.getIdCliente());
            ps.setString(3, factura.getFacDescripcion());
            
            if (factura.getFacFechaPago() != null) {
                ps.setDate(4, Date.valueOf(factura.getFacFechaPago()));
            } else {
                ps.setDate(4,null);
            }
            
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    // ---------------------------------------
    // CLASE DE UTILIDAD: VERIFICAR EXISTENCIA
    // ---------------------------------------
    
    public static boolean verificarExistencia(String idFactura) {
        String sql = "SELECT 1 FROM facturas WHERE id_factura = ?";
        
        try ( Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, idFactura);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Devuelve true si encontro al menos un registro
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }

    // -------------------------
    // RF5.4.1: CONSULTA GENERAL
    // -------------------------
    
    public static List<Facturas> obtenerListadoFacturas () {
        List<Facturas> lista = new ArrayList<>();
        
        String sql = "SELECT * FROM facturas " + " WHERE estado_fac = 'APR' " + "ORDER BY id_factura";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Facturas f = mapearFacturas(rs);
                lista.add(f);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        return lista;
    }
    
    //-----------------------------------------------
    // METODO PARA GUI PARA MOSTRAR UNA UNICA FACTURA
    //-----------------------------------------------
    
    public static Facturas obtenerFacturaPorId(String idFactura) {
        String sql = "SELECT * FROM facturas WHERE id_factura = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idFactura);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearFacturas(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        return null;
    }
    

    // ------------------------
    // RF5.2: MODIFICAR FACTURA
    // ------------------------
    
    public boolean modificarFactura (Facturas factura) { 
        String sql = "UPDATE facturas SET fac_descripcion = ? WHERE id_factura = ?";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, factura.getFacDescripcion());
            ps.setString(2, factura.getIdFactura());
            
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    // ---------------------------
    // RF5.3: INHABILITAR FACTURA
    // ---------------------------
    
    public boolean eliminarFactura(String idFactura) {
        String sql = "CALL anular_factura(?)";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idFactura);

            ps.execute(); 
            return true;

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    // ---------------------------------------------
    // RF5.4.2: CONSULTA POR PARÁMETROS DE FACTURAS
    // ---------------------------------------------
    
    public List<Facturas> obtenerFacturasPorParametro (
            String idFactura,
            String idCliente,
            String facDescripcion) {
        
        List<Facturas> lista = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder("SELECT * FROM facturas WHERE 1=1");
        List<Object> parametros = new ArrayList<>();
        
        
        // Filtro por idFactura
        if (idFactura != null && !idFactura.trim().isEmpty()) {
            sql.append(" AND id_factura = ?");
            parametros.add(idFactura.trim());
        }
        
        // Filtro por idCliente
        if (idCliente != null && !idCliente.trim().isEmpty()) {
            sql.append(" AND id_cliente = ?");
            parametros.add(idCliente.trim());
        }
        
        // Filtro por Descripcion
        if (facDescripcion != null && !facDescripcion.trim().isEmpty()) {
            sql.append(" AND (fac_descripcion ILIKE ?)");
            String patron = "%" + facDescripcion.trim() + "%";
            parametros.add(patron);
        }
        
        // Orden
        sql.append(" ORDER BY id_factura");
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            // Asignar Parametros Dinamicamente
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i+1,parametros.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearFacturas(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        return lista;
    }
}
