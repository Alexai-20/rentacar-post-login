package controlador;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import modelo.Administrador;
import util.SeguridadUtil;

/**
 * Gestiona la autenticación de usuarios administradores contra la base de
 * datos.
 */
public class AutenticacionController {

    private final Conexion conexion;

    public AutenticacionController(Conexion conexion) {
        this.conexion = conexion;
    }

    public Optional<Administrador> autenticar(String email, char[] contrasena) {
        String sql = "SELECT id_usuario, nombre, apellido, rut, email, telefono, direccion, fecha_nacimiento, tipo_usuario, estado, fecha_registro, fecha_ultima_modificacion, contrasena_hash "
                + "FROM USUARIOS WHERE tipo_usuario = 'ADMINISTRADOR' AND email = ?";

        try (Connection conn = conexion.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashAlmacenado = rs.getString("contrasena_hash");
                    String hashIngresado = SeguridadUtil.sha256(contrasena);
                    if (hashAlmacenado != null && hashAlmacenado.equalsIgnoreCase(hashIngresado)) {
                        Administrador administrador = mapearAdministrador(rs);
                        return Optional.of(administrador);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    private Administrador mapearAdministrador(ResultSet rs) throws SQLException {
        LocalDate fechaNacimiento = toLocalDate(rs.getDate("fecha_nacimiento"));
        LocalDate fechaRegistro = toLocalDate(rs.getDate("fecha_registro"));
        LocalDate fechaUltimaModificacion = toLocalDate(rs.getDate("fecha_ultima_modificacion"));
        return new Administrador(
                rs.getInt("id_usuario"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("rut"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("direccion"),
                fechaNacimiento,
                rs.getString("tipo_usuario"),
                rs.getString("estado"),
                fechaRegistro,
                fechaUltimaModificacion,
                rs.getString("contrasena_hash")
        );
    }

    private LocalDate toLocalDate(java.sql.Date date) {
        return date != null ? date.toLocalDate() : null;
    }
}
