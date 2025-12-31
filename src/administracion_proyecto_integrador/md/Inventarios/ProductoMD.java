
package administracion_proyecto_integrador.md.Inventarios;

import administracion_proyecto_integrador.dp.Inventarios.Productos;
import administracion_proyecto_integrador.conexion.ConexionPostgreSQL;

import java.sql.Connection;
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

        // Agregar descripción de unidad de medida de VENTA 
        try {
            producto.setProUmVentaDescripcion(rs.getString("um_venta_descripcion"));
        } catch (SQLException e) {
            producto.setProUmVentaDescripcion("Unidad"); // Valor por defecto si no existe
        }

        // Agregar descripción de unidad de medida de COMPRA 
        try {
            producto.setProUmCompraDescripcion(rs.getString("um_compra_descripcion"));
        } catch (SQLException e) {
            producto.setProUmCompraDescripcion("Unidad"); // Valor por defecto si no existe
        }

        // Agregar descripción de CATEGORÍA 
        try {
            producto.setCategoriaDescripcion(rs.getString("cat_descripcion"));
        } catch (SQLException e) {
            producto.setCategoriaDescripcion("Sin categoría"); // Valor por defecto si no existe
        }

        return producto;
    }
    
    // --------------------------------
    // RF4.1: Creación Producto (CREATE)
    // --------------------------------
    
    public boolean crearProducto(Productos producto) {
        String sql = "INSERT INTO productos " +
                     "(id_producto, pro_descripcion, pro_um_compra, pro_um_venta, " +
                     "pro_valor_compra, pro_precio_venta, pro_saldo_inicial, " +
                     "pro_qty_ingresos, pro_qty_egresos, pro_qty_ajustes, " +
                     "pro_saldo_final, estado_prod, id_categoria, pro_imagen) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, 0, ?, 'ACT', ?, null)";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getIdProducto());
            ps.setString(2, producto.getProDescripcion());
            ps.setString(3, producto.getProUmCompra());
            ps.setString(4, producto.getProUmVenta());
            ps.setDouble(5, producto.getProValorCompra());
            ps.setDouble(6, producto.getProPrecioVenta());
            ps.setInt(7, producto.getProSaldoInicial());
            ps.setInt(8, producto.getProQtyIngresos()); // Mismo valor que saldo inicial
            ps.setInt(9, producto.getProSaldoFinal());   // Calculado previamente
            ps.setString(10, producto.getIdcategoria());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    // ---------------------------------------
    // VERIFICAR EXISTENCIA
    // ---------------------------------------
    
    public static boolean verificarExistencia(String idProducto) {
        String sql = "SELECT 1 FROM productos WHERE id_producto = ?";
        
        try ( Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, idProducto);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    
    // ACTUALIZAR STOCK DEL PRODUCTO POR VENTA (incrementar egresos y recalcular saldo final)
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
                    System.out.println("No se pudo completar la operación. Intente de nuevo.");
                    return false;
                }
            } else {
                System.out.println("No se pudo completar la operación. Intente de nuevo.");
                return false;
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }

        // SEGUNDO: Actualizar stock
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
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }

    // REVERTIR STOCK DEL PRODUCTO (decrementar egresos y recalcular saldo final)
    public static boolean revertirStockPorVenta(String idProducto, int cantidadARevertir) {
        
        // OPERACION INVERSA A actualizarStockPorVenta()
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
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }

    // METODO PARA RECALCULAR EL STOCK FINAL
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

        String sql = "SELECT p.*, " +
                     "umv.um_descripcion as um_venta_descripcion, " +
                     "umc.um_descripcion as um_compra_descripcion, " +
                     "c.cat_descripcion " +
                     "FROM productos p " +
                     "LEFT JOIN unidades_medidas umv ON p.pro_um_venta = umv.id_unidad_medida " +
                     "LEFT JOIN unidades_medidas umc ON p.pro_um_compra = umc.id_unidad_medida " +
                     "LEFT JOIN categoria c ON p.id_categoria = c.id_categoria " +
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
    

    // OBTENER PRODUCTO ESPECIFICO POR ID
    public static Productos obtenerProductoPorId(String idProducto) {
        String sql = "SELECT p.*, " +
                     "umv.um_descripcion as um_venta_descripcion, " +
                     "umc.um_descripcion as um_compra_descripcion, " +
                     "c.cat_descripcion " +
                     "FROM productos p " +
                     "LEFT JOIN unidades_medidas umv ON p.pro_um_venta = umv.id_unidad_medida " +
                     "LEFT JOIN unidades_medidas umc ON p.pro_um_compra = umc.id_unidad_medida " +
                     "LEFT JOIN categoria c ON p.id_categoria = c.id_categoria " +
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
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return null;
    }
    
    // OBTENER STOCK DISPONIBLE DE UN PRODUCTO
    public static int obtenerStockDisponible(String idProducto) {
        String sql = "SELECT pro_saldo_final FROM productos " +
                     "WHERE id_producto = ? AND estado_prod = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("pro_saldo_final");
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return 0;
    }
    
    // GENERAR ID AUTOMÁTICO DE PRODUCTO
    public static String generarIdProducto() {
        String sql = "SELECT generar_id_producto()";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return null;
    }

    // OBTENER UNIDADES DE MEDIDA PARA EL COMBOBOX
    // CREACIÓN DE CLASES INTERNAS PARA UNIDADES DE MEDIDA Y CATEGORIAS
    
    public static class UnidadMedida {
        private String id;
        private String descripcion;

        public UnidadMedida(String id, String descripcion) {
            this.id = id;
            this.descripcion = descripcion;
        }

        public String getId() { return id; }
        public String getDescripcion() { return descripcion; }

        @Override
        public String toString() {
            return descripcion; 
        }
    }


    // OBTIENE LAS UNIDADES DE MEDIDA ACTIVAS
    public static List<UnidadMedida> obtenerUnidadesMedida() {
        List<UnidadMedida> lista = new ArrayList<>();

        String sql = "SELECT id_unidad_medida, um_descripcion " +
                     "FROM unidades_medidas " +
                     "ORDER BY um_descripcion";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UnidadMedida um = new UnidadMedida(
                    rs.getString("id_unidad_medida"),
                    rs.getString("um_descripcion")
                );
                lista.add(um);
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }

        return lista;
    }

    // OBTENER CATEGORÍAS PARA COMBOBOX (CLASE INTERNA)
    public static class Categoria {
        private String id;
        private String descripcion;

        public Categoria(String id, String descripcion) {
            this.id = id;
            this.descripcion = descripcion;
        }

        public String getId() { return id; }
        public String getDescripcion() { return descripcion; }

        @Override
        public String toString() {
            return descripcion; 
        }
    }

    // OBTIENE LAS CATEGORIAS ACTIVAS PARA EL COMBOBOX
    public static List<Categoria> obtenerCategorias() {
        List<Categoria> lista = new ArrayList<>();

        String sql = "SELECT id_categoria, cat_descripcion " +
                     "FROM categoria " +
                     "ORDER BY cat_descripcion";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria cat = new Categoria(
                    rs.getString("id_categoria"),
                    rs.getString("cat_descripcion")
                );
                lista.add(cat);
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
                     "pro_descripcion = ?, " +
                     "pro_um_compra = ?, " +
                     "pro_um_venta = ?, " +
                     "pro_valor_compra = ?, " +
                     "pro_precio_venta = ?, " +
                     "id_categoria = ? " +
                     "WHERE id_producto = ?";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, producto.getProDescripcion());
            ps.setString(2, producto.getProUmCompra());
            ps.setString(3, producto.getProUmVenta());
            ps.setDouble(4, producto.getProValorCompra());
            ps.setDouble(5, producto.getProPrecioVenta());
            ps.setString(6, producto.getIdcategoria());
            ps.setString(7, producto.getIdProducto());
            
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
                     "SET estado_prod = ? " +
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

        // Se incluyen los joins a las tablas de categorias y unidades de medida.
        StringBuilder sql = new StringBuilder(
            "SELECT p.*, " +
            "umv.um_descripcion as um_venta_descripcion, " +
            "umc.um_descripcion as um_compra_descripcion, " +
            "c.cat_descripcion " +
            "FROM productos p " +
            "LEFT JOIN unidades_medidas umv ON p.pro_um_venta = umv.id_unidad_medida " +
            "LEFT JOIN unidades_medidas umc ON p.pro_um_compra = umc.id_unidad_medida " +
            "LEFT JOIN categoria c ON p.id_categoria = c.id_categoria " +
            "WHERE p.estado_prod = 'ACT'"
        );

        List<Object> parametros = new ArrayList<>();

        // Filtro por Id
        if (idProducto != null && !idProducto.trim().isEmpty()) {
            sql.append(" AND p.id_producto = ?");
            parametros.add(idProducto.trim());
        }

        // Filtro por Descripcion
        if (proDescripcion != null && !proDescripcion.trim().isEmpty()) {
            sql.append(" AND p.pro_descripcion ILIKE ?");
            String patron = "%" + proDescripcion.trim() + "%";
            parametros.add(patron);
        }

        // Filtro por Categoria
        if (idCategoria != null && !idCategoria.trim().isEmpty()) {
            sql.append(" AND p.id_categoria = ?");
            parametros.add(idCategoria.trim());
        }

        // Orden
        sql.append(" ORDER BY p.pro_descripcion");

        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            // Asignar Parametros Dinamicamente
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i+1, parametros.get(i));
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
