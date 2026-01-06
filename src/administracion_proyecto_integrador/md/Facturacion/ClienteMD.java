package administracion_proyecto_integrador.md.Facturacion;

import administracion_proyecto_integrador.dp.Facturacion.Clientes;
import administracion_proyecto_integrador.conexion.ConexionPostgreSQL;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.util.ArrayList;
import java.util.List;


public class ClienteMD {
    
    // Método de Mapeo de Tabla Clientes
    private static Clientes mapearClientes (ResultSet rs) throws SQLException {
        Clientes cliente = new Clientes();
        cliente.setIdCliente(rs.getString("id_cliente"));
        cliente.setCliNombre(rs.getString("cli_nombre"));
        cliente.setCliRucCed(rs.getString("cli_ruc_ced"));
        cliente.setCliTelefono(rs.getString("cli_telefono"));
        cliente.setCliMail(rs.getString("cli_mail"));
        cliente.setCliCelular(rs.getString("cli_celular"));
        cliente.setCliDireccion(rs.getString("cli_direccion"));
        cliente.setEstadoCli(rs.getString("estado_cli"));
        cliente.setIdCiudad(rs.getString("id_ciudad"));
        return cliente;
    }
    
    // -----------------------
    // RF6.1: Creación Cliente 
    // -----------------------
    public boolean crearCliente (Clientes cliente) {
        String sql = "INSERT INTO clientes" +
                     "(id_cliente, cli_nombre, cli_ruc_ced, cli_telefono, "
                    + "cli_mail, cli_celular, cli_direccion, estado_cli, "
                    + "id_ciudad)" +
                     "VALUES (?,?,?,?,?,?,?,?,?)";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getIdCliente());
            ps.setString(2, cliente.getCliNombre());
            ps.setString(3, cliente.getCliRucCed());
            ps.setString(4, cliente.getCliTelefono());
            ps.setString(5, cliente.getCliMail());
            ps.setString(6, cliente.getCliCelular());
            ps.setString(7, cliente.getCliDireccion());
            ps.setString(8, cliente.getEstadoCli());
            ps.setString(9, cliente.getIdCiudad());
            
            int filas = ps.executeUpdate();
            return filas > 0;
            
        } catch(SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    /**
     * Genera el siguiente ID de clientes en formato CLI0001, CLI0002 , etc.
     */
    public static String obtenerSiguienteIdCliente() {
        String sql = "SELECT generar_id_cliente() AS nuevo_id";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("nuevo_id");
            } else {
                return "CLI0001"; 
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return "CLI0001"; 
        }
    }
    
    // ----------------------------------------
    // METODO DE UTILIDAD: VERIFICAR EXISTENCIA
    // ----------------------------------------
    
    public static boolean verificarExistencia(String idCliente) {
        String sql = "SELECT 1 FROM clientes WHERE id_cliente = ?";
        
        try ( Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, idCliente);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    // -------------------------
    // RF6.4.1: CONSULTA GENERAL
    // -------------------------
    
    public static List<Clientes> obtenerListadoClientes () {
        List<Clientes> lista = new ArrayList<>();
        
        String sql = "SELECT * FROM clientes " + " WHERE estado_cli = 'ACT' " + "ORDER BY id_cliente";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Clientes a = mapearClientes(rs);
                lista.add(a);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        return lista;
    }
    
    
    // ----------------------------
    // METODOS AUXILIARES PARA GUI
    // ----------------------------
    
    public static List<Clientes> obtenerListadoNombresClientes () {
        List<Clientes> lista = new ArrayList<>();

        String sql = "SELECT id_cliente, cli_nombre FROM clientes " + 
                     "WHERE estado_cli = 'ACT' " + 
                     "ORDER BY cli_nombre";

        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Clientes cliente = new Clientes();
                cliente.setIdCliente(rs.getString("id_cliente"));
                cliente.setCliNombre(rs.getString("cli_nombre"));
                lista.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        return lista;
    }
    
    public static String obtenerNombreCliente(String idCliente) {
        String sql = "SELECT cli_nombre FROM clientes WHERE id_cliente = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("cli_nombre");
                } else {
                    System.out.println("No se encontró el cliente con ID: " + idCliente);
                    return "Cliente no encontrado";
                }
            }

        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return "No se pudo completar la operación. Intente de nuevo.";
        }
    }
    
    // OBTENER DESCRIPCIÓN DE CIUDAD
    public static String obtenerNombreCiudad(String idCiudad) {
        String sql = "SELECT ciu_descripcion FROM ciudades WHERE id_ciudad = ?";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCiudad);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("ciu_descripcion");
                } else {
                    return "N/A";
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener nombre de ciudad: " + e.getMessage());
            return "N/A";
        }
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

    // Clase auxiliar para ComboBox
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
    
    // -------------------------
    // RF6.2: MODIFICAR CLIENTE
    // -------------------------
    
    public boolean modificarCliente (Clientes cliente) {
        String sql = "UPDATE clientes SET " +
                     "cli_nombre = ?, " +
                     "cli_ruc_ced = ?, " +
                     "cli_telefono = ?, " +
                     "cli_mail = ?, " +
                     "cli_celular = ?, " +
                     "cli_direccion = ?, " +
                     "estado_cli = ?, " +
                     "id_ciudad = ? " +
                     "WHERE id_cliente = ?";
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getCliNombre());
            ps.setString(2, cliente.getCliRucCed());
            ps.setString(3, cliente.getCliTelefono());
            ps.setString(4, cliente.getCliMail());
            ps.setString(5, cliente.getCliCelular());
            ps.setString(6, cliente.getCliDireccion());
            ps.setString(7, cliente.getEstadoCli());
            ps.setString(8, cliente.getIdCiudad());
            ps.setString(9, cliente.getIdCliente());
            
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    // ---------------------------
    // RF6.3: INHABILITAR CLIENTE
    // ---------------------------
    
    public boolean eliminarCliente(String idCliente) {
        String sql = "UPDATE clientes " +
                     "SET estado_cli = ? " +
                     "WHERE id_cliente = ?";
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "INA");
            ps.setString(2, idCliente);

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
            return false;
        }
    }
    
    // -------------------------------------------
    // RF6.4.2: CONSULTA POR PARÁMETROS DE CLIENTE
    // -------------------------------------------
    public List<Clientes> obtenerClientesPorParametro (
            String idCliente,
            String cliRucCed,
            String cliNombre,
            String cliMail,
            String cliCelular,
            String cliTelefono) {
        List<Clientes> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM clientes WHERE estado_cli = 'ACT'");
        List<Object> parametros = new ArrayList<>();

        //ID del cliente
        if (idCliente != null && !idCliente.trim().isEmpty()) {
            sql.append(" AND id_cliente ILIKE ?");
            String patron = "%" + idCliente.trim() + "%";
            parametros.add(patron);
        }

        //C[edula o Ruc
        if (cliRucCed != null && !cliRucCed.trim().isEmpty()) {
            sql.append(" AND cli_ruc_ced = ?");
            parametros.add(cliRucCed.trim());
        }

        //Nombre
        if (cliNombre != null && !cliNombre.trim().isEmpty()) {
            sql.append(" AND cli_nombre ILIKE ?");
            String patron = "%" + cliNombre.trim() + "%";
            parametros.add(patron);
        }

        //Correo
        if (cliMail != null && !cliMail.trim().isEmpty()) {
            sql.append(" AND cli_mail ILIKE ?");
            String patron = "%" + cliMail.trim() + "%";
            parametros.add(patron);
        }

        //Celular
        if (cliCelular != null && !cliCelular.trim().isEmpty()) {
            sql.append(" AND cli_celular = ?");
            parametros.add(cliCelular.trim());
        }

        //Teléfono
        if (cliTelefono != null && !cliTelefono.trim().isEmpty()) {
            sql.append(" AND cli_telefono = ?");
            parametros.add(cliTelefono.trim());
        }
        sql.append(" ORDER BY cli_nombre");

        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i+1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearClientes(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo completar la operación. Intente de nuevo.");
        }
        return lista;
    }    

    
    // ----------------------------------
    // METODOS AUXILIARES DE VERIFICACION
    // ----------------------------------
    
    //Verifica si un correo ya existe en la base de datos (para creación)
    public static boolean verificarCorreoExiste(String correo) {
        String sql = "SELECT 1 FROM clientes WHERE cli_mail = ? AND estado_cli = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Devuelve true si encontró al menos un registro
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar correo: " + e.getMessage());
            return false;
        }
    }

    //Verifica si un correo está siendo usado por otro cliente (para modificación)
    public static boolean verificarCorreoDuplicado(String correo, String idClienteActual) {
        String sql = "SELECT 1 FROM clientes " +
                     "WHERE cli_mail = ? AND id_cliente != ? AND estado_cli = 'ACT'";

        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, idClienteActual);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Devuelve true si encontró otro cliente con ese correo
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar correo duplicado: " + e.getMessage());
            return false;
        }
    }
    
    // Verificar si un RUC/Cédula ya existe (para creación)
    public static boolean verificarRucCedExiste(String rucCed) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE cliRucCed = ?";
        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, rucCed);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    // Verificar si un RUC/Cédula ya existe en otro cliente (para modificación)
    public static boolean verificarRucCedDuplicado(String rucCed, String idCliente) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE cli_ruc_ced = ? AND id_cliente != ?";
        try (Connection conn = ConexionPostgreSQL.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, rucCed);
            pst.setString(2, idCliente);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
        }
        return false;
    }
}


