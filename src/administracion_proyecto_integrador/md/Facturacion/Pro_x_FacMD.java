
package administracion_proyecto_integrador.md.Facturacion;

import administracion_proyecto_integrador.dp.Facturacion.Pro_x_Fac;
import administracion_proyecto_integrador.conexion.ConexionPostgreSQL;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.util.ArrayList;
import java.util.List;

public class Pro_x_FacMD {
    
    private static Pro_x_Fac mapearDetalle (ResultSet rs) throws SQLException {
        Pro_x_Fac detalle = new Pro_x_Fac();
        
        detalle.setIdFactura(rs.getString("id_factura"));
        detalle.setIdProducto(rs.getString("id_producto"));
        detalle.setPxfCantidad(rs.getInt("pxf_cantidad"));
        detalle.setPxfPrecio(rs.getDouble("pxf_precio"));
        detalle.setPxfSubtotal(rs.getDouble("pxf_subtotal"));
        detalle.setEstadoPxf(rs.getString("estado_pxf"));
        
        return detalle;
    }
    
    
    // CONSULTA DETALLES POR FACTURA
    
    public static List<Pro_x_Fac> obtenerListadoDetallesFactura(String idFactura) {
        List<Pro_x_Fac> lista = new ArrayList<>();
        
        String sql = "SELECT * FROM pro_x_fac WHERE id_factura = ? AND estado_pxf = 'APR' ORDER BY id_producto";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idFactura);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearDetalle(rs));
                }
            }
        } catch(SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        
        return lista;
    }
    
    // CONSULTA ESPECIFICA DE DETALLE
    
    public static Pro_x_Fac obtenerDetalle(String idFactura, String idProducto) {
        String sql =
            "SELECT * FROM pro_x_fac " +
            "WHERE id_factura = ? AND id_producto = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idFactura);
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
    
    // OBTENER PRECIO DE VENTA DEL PRODUCTO
    
    public static double obtenerPrecioVentaProducto(String idProducto) {
        String sql = "SELECT pro_precio_venta FROM productos WHERE id_producto = ? AND estado_prod = 'ACT'";

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
    
    
    // OBTENER NOMBRE DEL PRODUCTO

    public static String obtenerNombreProducto(String idProducto) {
        String sql = "SELECT pro_descripcion FROM productos WHERE id_producto = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("pro_descripcion");
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        return "Producto no encontrado";
    }
    
    
    // OBTENER EL PRECIO DEL DETALLE
    
    public static double obtenerPrecioDetalle(String idFactura, String idProducto) {
        String sql =
            "SELECT pxf_precio " +
            "FROM pro_x_fac " +
            "WHERE id_factura = ? AND id_producto = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idFactura);
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
    
    // ACTUALIZAR LA CANTIDAD Y EL SUBTOTAL
    
    public static boolean actualizarCantidadYSubtotal(String idFactura, String idProducto, int nuevaCantidad, double nuevoSubtotal) {
        // Primero obtener la cantidad anterior para calcular la diferencia
        int cantidadAnterior = obtenerCantidadActual(idFactura, idProducto);

        String sql =
            "UPDATE pro_x_fac SET " +
            "pxf_cantidad = ?, " +
            "pxf_subtotal = ? " +
            "WHERE id_factura = ? AND id_producto = ?";

        Connection conn = null;
        try {
            conn = ConexionPostgreSQL.getConnection();
            conn.setAutoCommit(false);

            // 1. Actualizar el detalle
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, nuevaCantidad);
                ps.setDouble(2, nuevoSubtotal);
                ps.setString(3, idFactura);
                ps.setString(4, idProducto);

                int result = ps.executeUpdate();

                if (result > 0) {
                    // 2. Ajustar stock del producto
                    boolean stockAjustado = administracion_proyecto_integrador.md.Inventarios.ProductoMD
                            .ajustarStockPorCambio(idProducto);

                    if (stockAjustado) {
                        // 3. Actualizar cabecera
                        actualizarTotalesCabecera(idFactura);

                        conn.commit();
                        return true;
                    } else {
                        conn.rollback();
                        System.out.println("No se pudo completar la operación. Intente de nuevo.");
                        return false;
                    }
                }

                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.out.println("No se pudo completar la operación. Intente de nuevo.");
            }
            
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
    
    // VERIFICAR EXISTENCIA
    
    public static boolean verificarExistencia(String idFactura, String idProducto) {
        String sql = "SELECT 1 FROM pro_x_fac WHERE id_factura =  ? AND id_producto = ? AND estado_pxf = 'APR'";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idFactura);
            ps.setString(2, idProducto);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    
    // OBTENER CANTIDAD ACTUAL DE PRODUCTO
    
    public static int obtenerCantidadActual(String idFactura, String idProducto) {
        String sql = "SELECT pxf_cantidad FROM pro_x_fac WHERE id_factura = ? AND id_producto = ? AND estado_pxf = 'APR'";
        
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idFactura);
            ps.setString(2, idProducto);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt("pxf_cantidad");
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        
        return 0;
    }
    
    // CREAR DETALLE
    // PARA ACTUALIZAR LA CABECERA
    
    // CALCULAR TOTALES DE LA FACTURA A PARTIR DE LOS DETALLES
    

    private static boolean actualizarTotalesCabecera(String idFactura) {
        // Primero calculamos el subtotal sumando todos los detalles
        String sqlCalcular = "SELECT COALESCE(SUM(pxf_subtotal), 0) as total_subtotal " +
                           "FROM pro_x_fac " +
                           "WHERE id_factura = ? AND estado_pxf = 'APR'";

        String sqlActualizar = "UPDATE facturas SET " +
                             "fac_subtotal = ?, " +
                             "fac_iva = ?, " +
                             "fac_total = ? " +
                             "WHERE id_factura = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement psCalc = conn.prepareStatement(sqlCalcular)) {

            psCalc.setString(1, idFactura);

            try (ResultSet rs = psCalc.executeQuery()) {
                if (rs.next()) {
                    double subtotal = rs.getDouble("total_subtotal");

                    // Calcula el IVA (15%)
                    double porcentajeIVA = 0.15;
                    double iva = subtotal * porcentajeIVA;
                    double total = subtotal + iva;

                    // Actualizar la cabecera
                    try (PreparedStatement psUpdate = conn.prepareStatement(sqlActualizar)) {
                        psUpdate.setDouble(1, subtotal);
                        psUpdate.setDouble(2, iva);
                        psUpdate.setDouble(3, total);
                        psUpdate.setString(4, idFactura);

                        int filasActualizadas = psUpdate.executeUpdate();
                        return filasActualizadas > 0;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }

        return false;
    }
    
    public static boolean registrarDetalle(Pro_x_Fac detalle) {
        String sql = "INSERT INTO pro_x_fac (id_factura, id_producto, "
                + "pxf_cantidad, pxf_precio, pxf_subtotal, estado_pxf) "
                + "VALUES (?,?,?,?,?,?)";

        Connection conn = null;
        try {
            conn = ConexionPostgreSQL.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Registrar el detalle
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, detalle.getIdFactura());
                ps.setString(2, detalle.getIdProducto());
                ps.setInt(3, detalle.getPxfCantidad());
                ps.setDouble(4, detalle.getPxfPrecio());
                ps.setDouble(5, detalle.getPxfSubtotal());
                ps.setString(6, detalle.getEstadoPxf());

                int result = ps.executeUpdate();

                if (result > 0) {
                    // 2. Actualizar stock del producto
                    boolean stockActualizado = administracion_proyecto_integrador.md.Inventarios.ProductoMD
                            .actualizarStockPorVenta(detalle.getIdProducto(), detalle.getPxfCantidad());

                    if (stockActualizado) {

                        // IMPORTANTE: Hacer flush/commit parcial para que el detalle esté visible
                        conn.commit();
                        conn.setAutoCommit(false); // Continuar con transacción

                        // 3. Actualizar totales de la cabecera
                        boolean cabeceraActualizada = actualizarTotalesCabecera(detalle.getIdFactura());

                        if (cabeceraActualizada) {
                            conn.commit(); // Confirmar transacción final
                            return true;
                        } else {
                            System.out.println("No se pudo completar la operación. Intente de nuevo.");
                            conn.rollback();
                            return false;
                        }
                    } else {
                        conn.rollback(); // Revertir si falla actualización de stock
                        System.out.println("No se pudo completar la operación. Intente de nuevo.");
                        return false;
                    }
                }
                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.out.println("No se pudo completar la operación. Intente de nuevo.");
            }
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
    
    // ACTUALIZAR LA CANTIDAD CON LOS BOTONES DE '+' O '-' Y RECALCULAR SUBTOTAL
    
    public boolean actualizarCantidad(String idFactura, String idProducto, int nuevaCantidad, double precioUnitario) {
        String sql = "UPDATE pro_x_fac SET pxf_cantidad = ?, pxf_subtotal = ? "
                + "WHERE id_factura = ? AND id_producto = ? AND estado_pxf = 'APR'";
        
        double nuevoSubtotal = nuevaCantidad * precioUnitario;
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, nuevaCantidad);
            ps.setDouble(2, nuevoSubtotal);
            ps.setString(3, idFactura);
            ps.setString(4, idProducto);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    // Sumar Cantidad (si ya existe el producto)
    
    public boolean sumarCantidad(String idFactura, String idProducto, int cantidad, double precioUnitario) {
        int actual = obtenerCantidadActual(idFactura, idProducto);
        int nueva = actual + cantidad;
        return actualizarCantidad(idFactura, idProducto, nueva, precioUnitario);
    }
    
    // Eliminar Producto de Detalle
    
    public static boolean eliminarDetalle(String idFactura, String idProducto) {
        // Primero obtener la cantidad para revertir el stock
        int cantidadARevertir = obtenerCantidadActual(idFactura, idProducto);

        String sql = "DELETE FROM pro_x_fac WHERE id_factura = ? "
                + "AND id_producto = ? AND estado_pxf = 'APR'";

        Connection conn = null;
        try {
            conn = ConexionPostgreSQL.getConnection();
            conn.setAutoCommit(false);

            // 1. Eliminar el detalle
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, idFactura);
                ps.setString(2, idProducto);

                int result = ps.executeUpdate();

                if (result > 0) {
                    // 2. Revertir stock del producto
                    boolean stockRevertido = administracion_proyecto_integrador.md.Inventarios.ProductoMD
                            .revertirStockPorVenta(idProducto, cantidadARevertir);

                    if (stockRevertido) {
                        // 3. Actualizar cabecera
                        actualizarTotalesCabecera(idFactura);

                        conn.commit();
                        return true;
                    } else {
                        conn.rollback();
                        System.out.println("No se pudo completar la operación. Intente de nuevo.");
                        return false;
                    }
                }

                conn.rollback();
                return false;
            }

        } catch(SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.out.println("No se pudo completar la operación. Intente de nuevo.");
            }
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
    
    /**
     * Actualizar solo la cantidad del detalle sin tocar el stock
     */
    public static boolean actualizarCantidadDetalle(String idFactura, String idProducto, int nuevaCantidad) {
        String sql = "UPDATE pro_x_fac SET " +
                     "pxf_cantidad = ?, " +
                     "pxf_subtotal = pxf_precio * ? " +
                     "WHERE id_factura = ? AND id_producto = ? AND estado_pxf = 'APR'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, nuevaCantidad);
            ps.setString(3, idFactura);
            ps.setString(4, idProducto);

            int result = ps.executeUpdate();

            // Actualizar totales de la cabecera
            if (result > 0) {
                actualizarTotalesCabecera(idFactura);
            }

            return result > 0;

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
}
