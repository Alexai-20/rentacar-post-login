package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {
    private final String bd = "car";
    private final String url = "jdbc:mysql://localhost:3306/";
    private final String user = "ian";
    private final String password = "BN/A7R3.pkLtUOYB";
    private final String driver = "com.mysql.cj.jdbc.Driver";
    private Connection cx;

    public Connection conectar() {
        try {
            if (cx == null || cx.isClosed()) {
                Class.forName(driver);
                cx = DriverManager.getConnection(url + bd, user, password);
                System.out.println("✅ Conexión establecida con la base de datos " + bd);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("❌ Error de conexión: " + ex.getMessage());
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cx;
    }

    public void desconectar() {
        try {
            if (cx != null && !cx.isClosed()) {
                cx.close();
                System.out.println("🔌 Conexión cerrada correctamente.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args ){
    Conexion conexion=new Conexion();
    conexion.conectar();
    }
}
