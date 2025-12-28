
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
        
        return producto;
    }
    
    // --------------------------------
    // RF4.1: Creación Producto (CREATE)
    // --------------------------------
    
    public boolean crearProducto (Productos producto) {
        String sql = "INSERT INTO productos" +
                     "(id_producto, pro_descripcion, pro_um_compra, pro_um_venta, pro_valor_compra, pro_precio_venta, pro_saldo_iniical, pro_qty_ingresos, pro_qty_egresos, pro_qty_ajustes, pro_salod_final, estado_prod, id_categoria, pro_imagen)" +
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
    // -------------------------
    // RF4.4.1: CONSULTA GENERAL
    // -------------------------
    
    public static List<Productos> obtenerListadoProductos () {
        List<Productos> lista = new ArrayList<>();
        
        String sql = "SELECT * FROM productos " + " WHERE estado_prod = 'ACT' " + "ORDER BY pro_descripcion";
        
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
            ps.setString(1, producto.getProDescripcion());
            ps.setString(2, producto.getProUmCompra());
            ps.setString(3, producto.getProUmVenta());
            ps.setDouble(4, producto.getProValorCompra());
            ps.setDouble(5, producto.getProPrecioVenta());
            ps.setInt(6, producto.getProSaldoInicial());
            ps.setString(7, producto.getIdcategoria());
            ps.setString(8, producto.getIdProducto());
            
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
