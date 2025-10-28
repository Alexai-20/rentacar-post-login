package controlador;

import conexion.Conexion;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Reserva;

/**
 * Controlador encargado de las operaciones sobre la tabla RESERVAS.
 */
public class ReservaController {

    private final Conexion conexion;

    public ReservaController(Conexion conexion) {
        this.conexion = conexion;
    }

    public boolean crearReserva(Reserva reserva) {
        String sql = "INSERT INTO RESERVAS(id_cliente, patente, fecha_reserva, fecha_inicio, fecha_fin, estado_reserva, monto_estimado, id_trabajador) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = conexion.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, reserva.getIdCliente());
                ps.setString(2, reserva.getPatente());
                ps.setDate(3, toSqlDate(reserva.getFechaReserva()));
                ps.setDate(4, toSqlDate(reserva.getFechaInicio()));
                ps.setDate(5, toSqlDate(reserva.getFechaFin()));
                ps.setString(6, reserva.getEstadoReserva());
                ps.setBigDecimal(7, safeBigDecimal(reserva.getMontoEstimado()));
                ps.setInt(8, reserva.getIdTrabajador());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        reserva.setIdReserva(rs.getInt(1));
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

    public List<Reserva> obtenerReservas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT id_reserva, id_cliente, patente, fecha_reserva, fecha_inicio, fecha_fin, estado_reserva, monto_estimado, id_trabajador FROM RESERVAS";

        try (Connection conn = conexion.conectar();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reserva reserva = new Reserva(
                        rs.getInt("id_reserva"),
                        rs.getInt("id_cliente"),
                        rs.getString("patente"),
                        toLocalDate(rs.getDate("fecha_reserva")),
                        toLocalDate(rs.getDate("fecha_inicio")),
                        toLocalDate(rs.getDate("fecha_fin")),
                        rs.getString("estado_reserva"),
                        rs.getBigDecimal("monto_estimado"),
                        rs.getInt("id_trabajador")
                );
                reservas.add(reserva);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return reservas;
    }

    public boolean actualizarReserva(Reserva reserva) {
        String sql = "UPDATE RESERVAS SET id_cliente = ?, patente = ?, fecha_reserva = ?, fecha_inicio = ?, fecha_fin = ?, estado_reserva = ?, monto_estimado = ?, id_trabajador = ? WHERE id_reserva = ?";

        Connection conn = null;
        try {
            conn = conexion.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, reserva.getIdCliente());
                ps.setString(2, reserva.getPatente());
                ps.setDate(3, toSqlDate(reserva.getFechaReserva()));
                ps.setDate(4, toSqlDate(reserva.getFechaInicio()));
                ps.setDate(5, toSqlDate(reserva.getFechaFin()));
                ps.setString(6, reserva.getEstadoReserva());
                ps.setBigDecimal(7, safeBigDecimal(reserva.getMontoEstimado()));
                ps.setInt(8, reserva.getIdTrabajador());
                ps.setInt(9, reserva.getIdReserva());
                ps.executeUpdate();
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

    public boolean eliminarReserva(int idReserva) {
        String sql = "DELETE FROM RESERVAS WHERE id_reserva = ?";

        Connection conn = null;
        try {
            conn = conexion.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idReserva);
                ps.executeUpdate();
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

    private BigDecimal safeBigDecimal(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
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
