package controlador;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;

/**
 * Controlador responsable de las operaciones CRUD para los clientes.
 */
public class ClienteController {

    private final Conexion conexion;

    public ClienteController(Conexion conexion) {
        this.conexion = conexion;
    }

    public boolean crearCliente(Cliente cliente) {
        String sqlUsuario = "INSERT INTO USUARIOS(nombre, apellido, rut, email, telefono, direccion, fecha_nacimiento, tipo_usuario, estado, fecha_registro, fecha_ultima_modificacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlCliente = "INSERT INTO CLIENTES_INFO(id_usuario, numero_licencia, fecha_vencimiento_licencia, tipo_cliente, empresa) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = conexion.conectar();
            conn.setAutoCommit(false);

            LocalDate ahora = LocalDate.now();
            if (cliente.getFechaRegistro() == null) {
                cliente.setFechaRegistro(ahora);
            }
            if (cliente.getFechaUltimaModificacion() == null) {
                cliente.setFechaUltimaModificacion(ahora);
            }

            try (PreparedStatement psUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                psUsuario.setString(1, cliente.getNombre());
                psUsuario.setString(2, cliente.getApellido());
                psUsuario.setString(3, cliente.getRut());
                psUsuario.setString(4, cliente.getEmail());
                psUsuario.setString(5, cliente.getTelefono());
                psUsuario.setString(6, cliente.getDireccion());
                psUsuario.setDate(7, toSqlDate(cliente.getFechaNacimiento()));
                psUsuario.setString(8, cliente.getTipoUsuario());
                psUsuario.setString(9, cliente.getEstado());
                psUsuario.setDate(10, toSqlDate(cliente.getFechaRegistro()));
                psUsuario.setDate(11, toSqlDate(cliente.getFechaUltimaModificacion()));
                psUsuario.executeUpdate();

                try (ResultSet rs = psUsuario.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idUsuario = rs.getInt(1);
                        cliente.setIdUsuario(idUsuario);

                        try (PreparedStatement psCliente = conn.prepareStatement(sqlCliente)) {
                            psCliente.setInt(1, idUsuario);
                            psCliente.setString(2, cliente.getNumeroLicencia());
                            psCliente.setDate(3, toSqlDate(cliente.getFechaVencimientoLicencia()));
                            psCliente.setString(4, cliente.getTipoCliente());
                            psCliente.setString(5, cliente.getEmpresa());
                            psCliente.executeUpdate();
                        }
                    }
                }
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            rollbackQuietly(conn);
            ex.printStackTrace();
            return false;
        } finally {
            closeQuietly(conn);
        }
    }

    public List<Cliente> obtenerClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT c.id_cliente, c.numero_licencia, c.fecha_vencimiento_licencia, c.tipo_cliente, c.empresa, "
                + "u.id_usuario, u.nombre, u.apellido, u.rut, u.email, u.telefono, u.direccion, u.fecha_nacimiento, u.tipo_usuario, u.estado, u.fecha_registro, u.fecha_ultima_modificacion "
                + "FROM CLIENTES_INFO c INNER JOIN USUARIOS u ON c.id_usuario = u.id_usuario";

        try (Connection conn = conexion.conectar();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("rut"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        toLocalDate(rs.getDate("fecha_nacimiento")),
                        rs.getString("tipo_usuario"),
                        rs.getString("estado"),
                        toLocalDate(rs.getDate("fecha_registro")),
                        toLocalDate(rs.getDate("fecha_ultima_modificacion")),
                        rs.getInt("id_cliente"),
                        rs.getString("numero_licencia"),
                        toLocalDate(rs.getDate("fecha_vencimiento_licencia")),
                        rs.getString("tipo_cliente"),
                        rs.getString("empresa")
                );
                clientes.add(cliente);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return clientes;
    }

    public boolean actualizarCliente(Cliente cliente) {
        String sqlUsuario = "UPDATE USUARIOS SET nombre = ?, apellido = ?, rut = ?, email = ?, telefono = ?, direccion = ?, fecha_nacimiento = ?, tipo_usuario = ?, estado = ?, fecha_registro = ?, fecha_ultima_modificacion = ? WHERE id_usuario = ?";
        String sqlCliente = "UPDATE CLIENTES_INFO SET numero_licencia = ?, fecha_vencimiento_licencia = ?, tipo_cliente = ?, empresa = ? WHERE id_cliente = ?";

        Connection conn = null;
        try {
            conn = conexion.conectar();
            conn.setAutoCommit(false);

            LocalDate ahora = LocalDate.now();
            cliente.setFechaUltimaModificacion(ahora);

            try (PreparedStatement psUsuario = conn.prepareStatement(sqlUsuario)) {
                psUsuario.setString(1, cliente.getNombre());
                psUsuario.setString(2, cliente.getApellido());
                psUsuario.setString(3, cliente.getRut());
                psUsuario.setString(4, cliente.getEmail());
                psUsuario.setString(5, cliente.getTelefono());
                psUsuario.setString(6, cliente.getDireccion());
                psUsuario.setDate(7, toSqlDate(cliente.getFechaNacimiento()));
                psUsuario.setString(8, cliente.getTipoUsuario());
                psUsuario.setString(9, cliente.getEstado());
                psUsuario.setDate(10, toSqlDate(cliente.getFechaRegistro()));
                psUsuario.setDate(11, toSqlDate(cliente.getFechaUltimaModificacion()));
                psUsuario.setInt(12, cliente.getIdUsuario());
                psUsuario.executeUpdate();
            }

            try (PreparedStatement psCliente = conn.prepareStatement(sqlCliente)) {
                psCliente.setString(1, cliente.getNumeroLicencia());
                psCliente.setDate(2, toSqlDate(cliente.getFechaVencimientoLicencia()));
                psCliente.setString(3, cliente.getTipoCliente());
                psCliente.setString(4, cliente.getEmpresa());
                psCliente.setInt(5, cliente.getIdCliente());
                psCliente.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            rollbackQuietly(conn);
            ex.printStackTrace();
            return false;
        } finally {
            closeQuietly(conn);
        }
    }

    public boolean eliminarCliente(int idCliente, int idUsuario) {
        String sqlCliente = "DELETE FROM CLIENTES_INFO WHERE id_cliente = ?";
        String sqlUsuario = "DELETE FROM USUARIOS WHERE id_usuario = ?";

        Connection conn = null;
        try {
            conn = conexion.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement psCliente = conn.prepareStatement(sqlCliente)) {
                psCliente.setInt(1, idCliente);
                psCliente.executeUpdate();
            }

            try (PreparedStatement psUsuario = conn.prepareStatement(sqlUsuario)) {
                psUsuario.setInt(1, idUsuario);
                psUsuario.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            rollbackQuietly(conn);
            ex.printStackTrace();
            return false;
        } finally {
            closeQuietly(conn);
        }
    }

    private java.sql.Date toSqlDate(LocalDate date) {
        return date != null ? java.sql.Date.valueOf(date) : null;
    }

    private LocalDate toLocalDate(java.sql.Date date) {
        return date != null ? date.toLocalDate() : null;
    }

    private void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
