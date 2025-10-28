package controlador;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.UsuarioAutenticado;

/**
 * Gestiona las operaciones de autenticación contra la tabla USUARIOS.
 */
public class UsuarioController {

    private static final Logger LOGGER = Logger.getLogger(UsuarioController.class.getName());

    private final Conexion conexion;

    public UsuarioController(Conexion conexion) {
        this.conexion = conexion;
    }

    /**
     * Intenta autenticar al usuario usando email y password. Devuelve un
     * {@link Optional} con los datos básicos si las credenciales son válidas.
     */
    public Optional<UsuarioAutenticado> autenticar(String email, String password) {
        if (email == null || password == null) {
            return Optional.empty();
        }

        final String sql = "SELECT id_usuario, nombre, tipo_usuario FROM USUARIOS WHERE email = ? AND password = ?";

        Connection conn = conexion.conectar();
        if (conn == null) {
            return Optional.empty();
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idUsuario = rs.getInt("id_usuario");
                    String nombre = rs.getString("nombre");
                    String tipoUsuario = rs.getString("tipo_usuario");
                    return Optional.of(new UsuarioAutenticado(idUsuario, nombre, tipoUsuario));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al autenticar usuario", ex);
        }

        return Optional.empty();
    }
}
