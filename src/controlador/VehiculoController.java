package controlador;

import conexion.Conexion;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Vehiculo;

/**
 * Gestiona las operaciones CRUD sobre la tabla VEHICULOS.
 */
public class VehiculoController {

    private final Conexion conexion;

    public VehiculoController(Conexion conexion) {
        this.conexion = conexion;
    }

    public boolean crearVehiculo(Vehiculo vehiculo) {
        String sql = "INSERT INTO VEHICULOS(patente, id_modelo, `año`, tipo_combustible, kilometraje, color, numero_asientos, tipo_vehiculo, tarifa_diaria, estado_mantenimiento, disponibilidad, fecha_registro, fecha_ultima_revision) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = conexion.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, vehiculo.getPatente());
                ps.setInt(2, vehiculo.getIdModelo());
                ps.setDate(3, toSqlDate(vehiculo.getAnio()));
                ps.setString(4, vehiculo.getTipoCombustible());
                ps.setInt(5, vehiculo.getKilometraje());
                ps.setString(6, vehiculo.getColor());
                ps.setInt(7, vehiculo.getNumeroAsientos());
                ps.setString(8, vehiculo.getTipoVehiculo());
                ps.setBigDecimal(9, safeBigDecimal(vehiculo.getTarifaDiaria()));
                ps.setString(10, vehiculo.getEstadoMantenimiento());
                ps.setString(11, disponibilidadToDb(vehiculo.isDisponibilidad()));
                ps.setDate(12, toSqlDate(vehiculo.getFechaRegistro()));
                ps.setDate(13, toSqlDate(vehiculo.getFechaUltimaRevision()));
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

    public List<Vehiculo> obtenerVehiculos() {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT patente, id_modelo, `año`, tipo_combustible, kilometraje, color, numero_asientos, tipo_vehiculo, tarifa_diaria, estado_mantenimiento, disponibilidad, fecha_registro, fecha_ultima_revision FROM VEHICULOS";

        try (Connection conn = conexion.conectar();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vehiculo vehiculo = new Vehiculo(
                        rs.getString("patente"),
                        rs.getInt("id_modelo"),
                        toLocalDate(rs.getDate("año")),
                        rs.getString("tipo_combustible"),
                        rs.getInt("kilometraje"),
                        rs.getString("color"),
                        rs.getInt("numero_asientos"),
                        rs.getString("tipo_vehiculo"),
                        rs.getBigDecimal("tarifa_diaria"),
                        rs.getString("estado_mantenimiento"),
                        disponibilidadFromDb(rs.getString("disponibilidad")),
                        toLocalDate(rs.getDate("fecha_registro")),
                        toLocalDate(rs.getDate("fecha_ultima_revision"))
                );
                vehiculos.add(vehiculo);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return vehiculos;
    }

    public boolean actualizarVehiculo(Vehiculo vehiculo) {
        String sql = "UPDATE VEHICULOS SET id_modelo = ?, `año` = ?, tipo_combustible = ?, kilometraje = ?, color = ?, numero_asientos = ?, tipo_vehiculo = ?, tarifa_diaria = ?, estado_mantenimiento = ?, disponibilidad = ?, fecha_registro = ?, fecha_ultima_revision = ? WHERE patente = ?";

        Connection conn = null;
        try {
            conn = conexion.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, vehiculo.getIdModelo());
                ps.setDate(2, toSqlDate(vehiculo.getAnio()));
                ps.setString(3, vehiculo.getTipoCombustible());
                ps.setInt(4, vehiculo.getKilometraje());
                ps.setString(5, vehiculo.getColor());
                ps.setInt(6, vehiculo.getNumeroAsientos());
                ps.setString(7, vehiculo.getTipoVehiculo());
                ps.setBigDecimal(8, safeBigDecimal(vehiculo.getTarifaDiaria()));
                ps.setString(9, vehiculo.getEstadoMantenimiento());
                ps.setString(10, disponibilidadToDb(vehiculo.isDisponibilidad()));
                ps.setDate(11, toSqlDate(vehiculo.getFechaRegistro()));
                ps.setDate(12, toSqlDate(vehiculo.getFechaUltimaRevision()));
                ps.setString(13, vehiculo.getPatente());
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

    public boolean eliminarVehiculo(String patente) {
        String sql = "DELETE FROM VEHICULOS WHERE patente = ?";

        Connection conn = null;
        try {
            conn = conexion.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, patente);
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

    private String disponibilidadToDb(boolean disponible) {
        return disponible ? "DISPONIBLE" : "NO_DISPONIBLE";
    }

    private boolean disponibilidadFromDb(String raw) {
        if (raw == null) {
            return false;
        }
        String normalized = raw.trim().toLowerCase();
        return normalized.equals("disponible")
                || normalized.equals("si")
                || normalized.equals("sí")
                || normalized.equals("true")
                || normalized.equals("1");
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
