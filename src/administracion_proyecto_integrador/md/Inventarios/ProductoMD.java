
package administracion_proyecto_integrador.md.Inventarios;

import administracion_proyecto_integrador.dp.Inventarios.Productos;
import administracion_proyecto_integrador.conexion.ConexionPostgreSQL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.util.ArrayList;
import java.util.List;

public class ProductoMD {
    
    private static Productos mapearProductos (ResultSet rs) throws SQLException {
        Productos producto = new Productos();
        producto.setIdProducto(rs.getString("id_producto"));
        producto.setProDescripcion(rs.getString("pro_descripcion"));
        producto.setProUmCompra(rs.getString("pro_um_compra"));
        producto.setProUmVenta(rs.getString("pro_um_venta"));
        producto.setProValorCompra(rs.getDouble("pro_valor_compra"));
        producto.setProPrecioVenta(rs.getDouble("pro_precio_venta"));
        producto.setProSaldoInicial(rs.getInt("pro_saldo_inicial"));
        producto.setProQtyIngresos(rs.getInt("pro_qty_ingresos"));
        producto.setProQtyEgresos(rs.getInt("pro_qty_egresos"));
        producto.setProQtyAjustes(rs.getInt("pro_qty_ajustes"));
        producto.setProSaldoFinal(rs.getInt("pro_saldo_final"));
        producto.setEstadoProd(rs.getString("estado_prod"));
        producto.setIdcategoria(rs.getString("id_categoria"));
        producto.setProImagen(rs.getString("pro_imagen"));

        // Agregar descripción de unidad de medida si existe en el ResultSet
        try {
            producto.setProUmVentaDescripcion(rs.getString("um_descripcion"));
        } catch (SQLException e) {
            producto.setProUmVentaDescripcion("Unidad"); // Valor por defecto si no existe
        }

        return producto;
    }
    
    // --------------------------------
    // RF4.1: Creación Producto (CREATE)
    // --------------------------------
    
    public boolean crearProducto (Productos producto) {
        String sql = "INSERT INTO productos" +
                     "(id_producto, pro_descripcion, pro_um_compra, pro_um_venta, pro_valor_compra, pro_precio_venta, pro_saldo_iniical, pro_qty_ingresos, pro_qty_egresos, pro_qty_ajustes, pro_saldo_final, estado_prod, id_categoria, pro_imagen)" +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement (sql)) {
            ps.setString(1, producto.getIdProducto());
            ps.setString(2, producto.getProDescripcion());
            ps.setString(3, producto.getProUmCompra());
            ps.setString(4, producto.getProUmVenta());
            ps.setDouble(5, producto.getProValorCompra());
            ps.setDouble(6, producto.getProPrecioVenta());
            ps.setInt(7, producto.getProSaldoInicial());
            ps.setInt(8, producto.getProQtyIngresos());
            ps.setInt(9, producto.getProQtyEgresos());
            ps.setInt(10, producto.getProQtyAjustes());
            ps.setInt(11, producto.getProSaldoFinal());
            ps.setString(12, producto.getEstadoProd());
            ps.setString(13, producto.getIdcategoria());
            ps.setString(14, producto.getProImagen());
            
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
    
    public static boolean verificarExistencia(String idProducto) {
        String sql = "SELECT 1 FROM productos WHERE id_producto = ?";
        
        try ( Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, idProducto);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Devuelve true si encontro al menos un registro
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    
    // ACTUALIZAR STOCK DEL PRODUCTO (incrementar egresos y recalcular saldo final)
    public static boolean actualizarStockPorVenta(String idProducto, int cantidadVendida) {
        // PRIMERO: Verificar stock disponible
        String sqlVerificar = "SELECT pro_saldo_final FROM productos " +
                              "WHERE id_producto = ? AND estado_prod = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement psVerif = conn.prepareStatement(sqlVerificar)) {

            psVerif.setString(1, idProducto);
            ResultSet rs = psVerif.executeQuery();

            if (rs.next()) {
                int stockActual = rs.getInt("pro_saldo_final");
                if (stockActual < cantidadVendida) {
                    System.out.println("ERROR: Stock insuficiente. Disponible: " + stockActual + ", Solicitado: " + cantidadVendida);
                    return false;
                }
            } else {
                System.out.println("ERROR: Producto no encontrado.");
                return false;
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error al verificar stock: " + e.getMessage());
            return false;
        }

        // SEGUNDO: Actualizar stock (FÓRMULA SIMPLIFICADA)
        String sql = "UPDATE productos SET " +
                     "pro_qty_egresos = pro_qty_egresos + ?, " +
                     "pro_saldo_final = pro_saldo_final - ? " +
                     "WHERE id_producto = ? AND estado_prod = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cantidadVendida);     // Incrementa egresos
            ps.setInt(2, cantidadVendida);     // Reduce saldo
            ps.setString(3, idProducto);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }

    // REVERTIR STOCK DEL PRODUCTO (decrementar egresos y recalcular saldo final)
    public static boolean revertirStockPorVenta(String idProducto, int cantidadARevertir) {
        // FÓRMULA SIMPLIFICADA: operación inversa
        String sql = "UPDATE productos SET " +
                     "pro_qty_egresos = pro_qty_egresos - ?, " +
                     "pro_saldo_final = pro_saldo_final + ? " +
                     "WHERE id_producto = ? AND estado_prod = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cantidadARevertir);  // Reduce egresos
            ps.setInt(2, cantidadARevertir);  // Aumenta saldo
            ps.setString(3, idProducto);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al revertir stock: " + e.getMessage());
            return false;
        }
    }

    // AJUSTAR STOCK
    public static boolean ajustarStockPorCambio(String idProducto) {
        String sql = "UPDATE PRODUCTOS SET PRO_SALDO_FINAL = PRO_SALDO_INICIAL + PRO_QTY_INGRESOS - PRO_QTY_EGRESOS WHERE ID_PRODUCTO = ?";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idProducto);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
        
    }
    
    // -------------------------
    // RF4.4.1: CONSULTA GENERAL
    // -------------------------
    
    public static List<Productos> obtenerListadoProductos () {
        List<Productos> lista = new ArrayList<>();

        String sql = "SELECT p.*, um.um_descripcion " +
                     "FROM productos p " +
                     "LEFT JOIN unidades_medidas um ON p.pro_um_venta = um.id_unidad_medida " +
                     "WHERE p.estado_prod = 'ACT' " +
                     "ORDER BY p.pro_descripcion";

        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Productos p = mapearProductos(rs);
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        return lista;
    }
    
    /**
     * Obtener un producto específico por su ID
     */
    public static Productos obtenerProductoPorId(String idProducto) {
        String sql = "SELECT p.*, um.um_descripcion " +
                     "FROM productos p " +
                     "LEFT JOIN unidades_medidas um ON p.pro_um_venta = um.id_unidad_medida " +
                     "WHERE p.id_producto = ? AND p.estado_prod = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProductos(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener producto: " + e.getMessage());
        }

        return null;
    }
    
    // -------------------------
    // RF4.2: MODIFICAR PRODUCTO
    // -------------------------
    
    public boolean modificarProducto (Productos producto) {
        String sql = "UPDATE productos SET " +
                     "pro_descripcion = ? " +
                     "pro_um_compra = ? " +
                     "pro_um_venta = ? " +
                     "pro_valor_compra = ? " +
                     "pro_precio_venta = ? " +
                     "pro_saldo_inicial = ? " +
                     "id_categoria = ? " +
                     "WHERE id_producto = ?";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, producto.getIdProducto());
            ps.setString(2, producto.getProDescripcion());
            ps.setString(3, producto.getProUmCompra());
            ps.setString(4, producto.getProUmVenta());
            ps.setDouble(5, producto.getProValorCompra());
            ps.setDouble(6, producto.getProPrecioVenta());
            ps.setInt(7, producto.getProSaldoInicial());
            ps.setString(8, producto.getIdcategoria());
            ps.setString(9, producto.getIdProducto());
            
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    // -----------------------------
    // RF4.3: INHABILITAR PRODUCTOS
    // -----------------------------
    
    public boolean eliminarProducto(String idProducto) {
        String sql = "UPDATE productos " +
                     "SET estado_prod = ?" +
                     "WHERE id_producto = ?";
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "INA"); //Estado Inhabilitado
            ps.setString(2, idProducto);
            
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    // ---------------------------------------------
    // RF4.4.2: CONSULTA POR PARÁMETROS DE PRODUCTOS
    // ---------------------------------------------
    
    public List<Productos> obtenerProductosPorParametro (
            String idProducto,
            String proDescripcion,
            String idCategoria) {
        
        List<Productos> lista = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder("SELECT * FROM productos WHERE 1=1");
        List<Object> parametros = new ArrayList<>();
        
        
        // Filtro por Id
        if (idProducto != null && !idProducto.trim().isEmpty()) {
            sql.append(" AND id_producto = ?");
            parametros.add(idProducto.trim());
        }
        
        // Filtro por Descripcion
        if (proDescripcion != null && !proDescripcion.trim().isEmpty()) {
            sql.append(" AND (pro_descripcion ILIKE ?)");
            String patron = "%" + proDescripcion.trim() + "%";
            parametros.add(patron);
        }
        
        // Filtro por Categoria
        if (idCategoria != null && !idCategoria.trim().isEmpty()) {
            sql.append(" AND id_categoria = ?");
            parametros.add(idCategoria.trim());
        }
        
        // Orden
        sql.append(" ORDER BY pro_descripcion");
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            // Asignar Parametros Dinamicamente
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i+1,parametros.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProductos(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        return lista;
    }
}
