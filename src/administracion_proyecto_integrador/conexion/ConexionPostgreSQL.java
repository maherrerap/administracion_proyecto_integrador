package administracion_proyecto_integrador.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionPostgreSQL {
    private static final String URL= ConfigReader.get("db.url");
    private static final String USER = ConfigReader.get("db.user");
    private static final String PASSWORD = ConfigReader.get("db.password");
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
    
    public static void probarConexion() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexion Exitosa");
            } else {
                System.out.println("No se pudo establecer la conexion.");
            }
        } catch (SQLException e){
            System.out.println("Error al conectar con PostgreSQL");
            e.printStackTrace();
        }
    }
}

