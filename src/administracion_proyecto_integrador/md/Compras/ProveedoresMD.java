package administracion_proyecto_integrador.md.Compras;

import administracion_proyecto_integrador.dp.Compras.Proveedores;
import administracion_proyecto_integrador.conexion.ConexionPostgreSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ProveedoresMD {
    
    // Método de Mapeo de Tabla Proveedores
    private static Proveedores mapearProveedores(ResultSet rs) throws SQLException {
        Proveedores proveedor = new Proveedores();
        proveedor.setIdProveedor(rs.getString("id_proveedor"));
        proveedor.setPrvNombre(rs.getString("prv_nombre"));
        proveedor.setPrvRucCed(rs.getString("prv_ruc_ced"));
        proveedor.setPrvTelefono(rs.getString("prv_telefono"));
        proveedor.setPrvMail(rs.getString("prv_mail"));
        proveedor.setPrvCelular(rs.getString("prv_celular"));
        proveedor.setPrvDireccion(rs.getString("prv_direccion"));
        proveedor.setEstadoPrv(rs.getString("estado_prv"));
        proveedor.setIdCiudad(rs.getString("id_ciudad"));
        proveedor.setPrvPais(rs.getString("prv_pais"));
        return proveedor;
    }

    // ----------------------
    // RF1.1: CREAR PROVEEDOR
    // ----------------------
    public boolean crearProveedor(Proveedores proveedor) {
        String sql = "INSERT INTO proveedores " +
                     "(id_proveedor, prv_nombre, prv_ruc_ced, prv_telefono, " +
                     "prv_mail, prv_celular, prv_direccion, estado_prv, id_ciudad, prv_pais) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?)";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, proveedor.getIdProveedor());
            ps.setString(2, proveedor.getPrvNombre());
            ps.setString(3, proveedor.getPrvRucCed());
            ps.setString(4, proveedor.getPrvTelefono());
            ps.setString(5, proveedor.getPrvMail());
            ps.setString(6, proveedor.getPrvCelular());
            ps.setString(7, proveedor.getPrvDireccion());
            ps.setString(8, proveedor.getEstadoPrv());
            ps.setString(9, proveedor.getIdCiudad());
            ps.setString(10, proveedor.getPrvPais());
            
            int filas = ps.executeUpdate();
            return filas > 0;
            
        } catch (SQLException e) {
            // Log interno para debugging
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }

    // ----------------------------------------
    // METODO DE UTILIDAD: VERIFICAR EXISTENCIA
    // ----------------------------------------
    
    public static boolean verificarExistencia(String idProveedor) {
        String sql = "SELECT 1 FROM proveedores WHERE TRIM(id_proveedor) = ?";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, idProveedor.trim());
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
            
        } catch (SQLException e) {
            // Log interno para debugging
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    // -------------------------
    // RF1.4.1: CONSULTA GENERAL
    // -------------------------
    
    public static List<Proveedores> obtenerListadoProveedores() {
        List<Proveedores> lista = new ArrayList<>();
        
        String sql = "SELECT * FROM proveedores " + 
                     "WHERE estado_prv = 'ACT' " + 
                     "ORDER BY id_proveedor";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapearProveedores(rs));
            }
            
        } catch (SQLException e) {
            // Log interno para debugging
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        
        return lista;
    }
    
    // ----------------------------
    // METODOS AUXILIARES PARA GUI
    // ----------------------------
    
    public static List<Proveedores> obtenerListadoNombresProveedores() {
        List<Proveedores> lista = new ArrayList<>();

        String sql = "SELECT id_proveedor, prv_nombre FROM proveedores " + 
                     "WHERE estado_prv = 'ACT' " + 
                     "ORDER BY prv_nombre";

        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Proveedores proveedor = new Proveedores();
                proveedor.setIdProveedor(rs.getString("id_proveedor"));
                proveedor.setPrvNombre(rs.getString("prv_nombre"));
                lista.add(proveedor);
            }
            
        } catch (SQLException e) {
            // Log interno para debugging
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        
        return lista;
    }
    
    // OBTENER LISTADO DE CIUDADES
    public static List<ComboItem> obtenerListadoCiudades() {
        List<ComboItem> lista = new ArrayList<>();
        String sql = "SELECT id_ciudad, ciu_descripcion FROM ciudades ORDER BY ciu_descripcion";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id_ciudad");
                String descripcion = rs.getString("ciu_descripcion");
                lista.add(new ComboItem(id, descripcion));
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        return lista;
    }

    // CLASE AUXILIAR PARA COMBOBOX
    public static class ComboItem {
        private final String id;
        private final String descripcion;

        public ComboItem(String id, String descripcion) {
            this.id = id;
            this.descripcion = descripcion;
        }

        public String getId() {
            return id;
        }

        public String getDescripcion() {
            return descripcion;
        }

        @Override
        public String toString() {
            return descripcion;
        }
    }
    public static String obtenerNombreProveedor(String idProveedor) {
        String sql = "SELECT prv_nombre FROM proveedores WHERE id_proveedor = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idProveedor);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("prv_nombre");
                }
            }

        } catch (SQLException e) {
            // Log interno para debugging
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        
        return "Proveedor no encontrado";
    }
    
    // --------------------------
    // RF1.2: MODIFICAR PROVEEDOR
    // --------------------------
    
    public boolean modificarProveedor(Proveedores proveedor) {
        String sql = "UPDATE proveedores SET " +
                     "prv_nombre = ?, " +
                     "prv_ruc_ced = ?, " +
                     "prv_telefono = ?, " +
                     "prv_mail = ?, " +
                     "prv_celular = ?, " +
                     "prv_direccion = ?, " +
                     "estado_prv = ?, " +
                     "id_ciudad = ?, " +
                     "prv_pais = ? " +
                     "WHERE id_proveedor = ?";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, proveedor.getPrvNombre());
            ps.setString(2, proveedor.getPrvRucCed());
            ps.setString(3, proveedor.getPrvTelefono());
            ps.setString(4, proveedor.getPrvMail());
            ps.setString(5, proveedor.getPrvCelular());
            ps.setString(6, proveedor.getPrvDireccion());
            ps.setString(7, proveedor.getEstadoPrv());
            ps.setString(8, proveedor.getIdCiudad());
            ps.setString(9, proveedor.getPrvPais());
            ps.setString(10, proveedor.getIdProveedor());
            
            int filas = ps.executeUpdate();
            return filas > 0;
            
        } catch (SQLException e) {
            // Log interno para debugging
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    // ----------------------------
    // RF1.3: INHABILITAR PROVEEDOR
    // ----------------------------
    
    public boolean eliminarProveedor(String idProveedor) {
        String sql = "UPDATE proveedores " +
                     "SET estado_prv = 'INA' " +
                     "WHERE id_proveedor = ?";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, idProveedor);
            
            int filas = ps.executeUpdate();
            return filas > 0;
            
        } catch (SQLException e) {
            // Log interno para debugging
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    // -----------------------------------------------
    // RF1.4.2: CONSULTA POR PARÁMETROS DE PROVEEDORES
    // -----------------------------------------------
    public List<Proveedores> obtenerProveedoresPorParametro(
            String prvRucCed,
            String prvNombre,
            String prvTelefono,
            String prvMail,
            String prvCelular,
            String prvPais) {
        
        List<Proveedores> lista = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(
            "SELECT * FROM proveedores WHERE estado_prv = 'ACT'"
        );
        List<Object> parametros = new ArrayList<>();
        
        if (prvRucCed != null && !prvRucCed.trim().isEmpty()) {
            sql.append(" AND prv_ruc_ced = ?");
            parametros.add(prvRucCed.trim());
        }
        
        if (prvNombre != null && !prvNombre.trim().isEmpty()) {
            sql.append(" AND prv_nombre ILIKE ?");
            parametros.add("%" + prvNombre.trim() + "%");
        }
        
        if (prvTelefono != null && !prvTelefono.trim().isEmpty()) {
            sql.append(" AND prv_telefono = ?");
            parametros.add(prvTelefono.trim());
        }
        
        if (prvMail != null && !prvMail.trim().isEmpty()) {
            sql.append(" AND prv_mail ILIKE ?");
            parametros.add("%" + prvMail.trim() + "%");
        }
        
        if (prvCelular != null && !prvCelular.trim().isEmpty()) {
            sql.append(" AND prv_celular = ?");
            parametros.add(prvCelular.trim());
        }
        
        if (prvPais != null && !prvPais.trim().isEmpty()) {
            sql.append(" AND prv_pais ILIKE ?");
            parametros.add("%" + prvPais.trim() + "%");
        }
        
        sql.append(" ORDER BY prv_nombre");
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProveedores(rs));
                }
            }
            
        } catch (SQLException e) {
            // Log interno para debugging
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        
        return lista;
    }
    
    /**
     * Genera el siguiente ID de proveedor en formato PR00001, PR00002 , etc.
     */
    public static String obtenerSiguienteIdProveedor() {
        String sql = "SELECT TRIM(id_proveedor) as id_proveedor " +
                     "FROM proveedores " +
                     "ORDER BY CAST(SUBSTRING(TRIM(id_proveedor), 3) AS INTEGER) DESC " +
                     "LIMIT 1";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String ultimoId = rs.getString("id_proveedor");
                
                if (ultimoId == null || ultimoId.length() < 3) {
                    return "PR00001";
                }
                
                String parteNumerica = ultimoId.substring(2);
                
                try {
                    int numero = Integer.parseInt(parteNumerica);
                    numero++;
                    return String.format("PR%05d", numero);
                    
                } catch (NumberFormatException e) {
                    return "PR00001";
                }
                
            } else {
                return "PR00001";
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return "PR00001";
        } catch (Exception e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return "PR00001";
        }
    }
}