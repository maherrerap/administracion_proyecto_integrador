
package administracion_proyecto_integrador.md.Facturacion;

import administracion_proyecto_integrador.dp.Facturacion.Clientes;
import administracion_proyecto_integrador.conexion.ConexionPostgreSQL;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.util.ArrayList;
import java.util.List;


public class ClienteMD {
    
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
    
    // --------------------------------
    // RF6.1: Creación Cliente (CREATE)
    // --------------------------------
    
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
    
    // ---------------------------------------
    // CLASE DE UTILIDAD: VERIFICAR EXISTENCIA
    // ---------------------------------------
    
    public static boolean verificarExistencia(String idCliente) {
        String sql = "SELECT 1 FROM clientes WHERE id_cliente = ?";
        
        try ( Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, idCliente);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Devuelve true si encontro al menos un registro
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
        
        String sql = "SELECT * FROM clientes " + " WHERE estado_cli = 'ACT' " + "ORDER BY cli_nombre";
        
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
    // PARA COMBOBOX DE FACTURACION
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
    // -------------------------
    // RF6.2: MODIFICAR CLIENTE
    // -------------------------
    
    public boolean modificarCliente (Clientes cliente) {
        String sql = "UPDATE clientes SET " +
                     "cli_nombre = ? " +
                     "cli_ruc_ced = ? " +
                     "cli_telefono = ? " +
                     "cli_mail = ? " +
                     "cli_celular = ? " +
                     "cli_direccion = ? " +
                     "estado_cli = ? " +
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
                     "SET estado_cli = ?" +
                     "WHERE idCliente = ?";
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "INA"); //Estado Inhabilitado
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
            String cliRucCed,
            String cliNombre,
            String cliCelular,
            String cliTelefono) {
        List<Clientes> lista = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder("SELECT * FROM clientes WHERE 1=1");
        List<Object> parametros = new ArrayList<>();
        
        
        // Filtro por Cedula
        if (cliRucCed != null && !cliRucCed.trim().isEmpty()) {
            sql.append(" AND cli_ruc_ced = ?");
            parametros.add(cliRucCed.trim());
        }
        
        // Filtro por Nombre
        if (cliNombre != null && !cliNombre.trim().isEmpty()) {
            sql.append(" AND (cli_nombre ILIKE ?)");
            String patron = "%" + cliNombre.trim() + "%";
            parametros.add(patron);
        }
        
        // Filtro por Celular
        if (cliCelular != null && !cliCelular.trim().isEmpty()) {
            sql.append(" AND cli_celular = ?");
            parametros.add(cliCelular.trim());
        }
        
        // Filtro por Teléfono
        if (cliTelefono != null && !cliTelefono.trim().isEmpty()) {
            sql.append(" AND cli_telefono = ?");
            parametros.add(cliTelefono.trim());
        }
        
        // Orden
        sql.append(" ORDER BY cli_nombre");
        
        try (Connection conn = ConexionPostgreSQL.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            // Asignar Parametros Dinamicamente
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i+1,parametros.get(i));
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
}
