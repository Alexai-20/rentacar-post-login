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
import modelo.MantenimientoVehiculo;

/**
 * Controlador para las operaciones CRUD del historial de mantenimiento de
 * vehículos.
 */
public class MantenimientoVehiculoController {

    private final Conexion conexion;

    public MantenimientoVehiculoController(Conexion conexion) {
        this.conexion = conexion;
    }

    public List<MantenimientoVehiculo> obtenerMantenimientos() {
        String sql = "SELECT id_mantenimiento, patente, id_tipo_mantenimiento, costo, fecha_mantenimiento, "
                + "kilometraje_mantenimiento, proximo_mantenimiento_km, proximo_mantenimiento_fecha, id_trabajador "
                + "FROM MANTENIMIENTOS_VEHICULOS ORDER BY fecha_mantenimiento DESC, id_mantenimiento DESC";
        return ejecutarConsulta(sql, null);
    }

    public List<MantenimientoVehiculo> obtenerMantenimientosPorPatente(String patente) {
        String sql = "SELECT id_mantenimiento, patente, id_tipo_mantenimiento, costo, fecha_mantenimiento, "
                + "kilometraje_mantenimiento, proximo_mantenimiento_km, proximo_mantenimiento_fecha, id_trabajador "
                + "FROM MANTENIMIENTOS_VEHICULOS WHERE patente = ? ORDER BY fecha_mantenimiento DESC, id_mantenimiento DESC";
        return ejecutarConsulta(sql, patente);
    }

    public boolean crearMantenimiento(MantenimientoVehiculo mantenimiento) {
        String sql = "INSERT INTO MANTENIMIENTOS_VEHICULOS(patente, id_tipo_mantenimiento, costo, fecha_mantenimiento, "
                + "kilometraje_mantenimiento, proximo_mantenimiento_km, proximo_mantenimiento_fecha, id_trabajador) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexion.conectar();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, mantenimiento.getPatente());
            ps.setInt(2, mantenimiento.getIdTipoMantenimiento());
            ps.setBigDecimal(3, mantenimiento.getCosto());
            ps.setDate(4, toSqlDate(mantenimiento.getFechaMantenimiento()));
            ps.setInt(5, mantenimiento.getKilometrajeMantenimiento());
            ps.setInt(6, mantenimiento.getProximoMantenimientoKm());
            ps.setDate(7, toSqlDate(mantenimiento.getProximoMantenimientoFecha()));

            if (mantenimiento.getIdTrabajador() > 0) {
                ps.setInt(8, mantenimiento.getIdTrabajador());
            } else {
                ps.setNull(8, java.sql.Types.INTEGER);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    mantenimiento.setIdMantenimiento(rs.getInt(1));
                }
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean actualizarMantenimiento(MantenimientoVehiculo mantenimiento) {
        String sql = "UPDATE MANTENIMIENTOS_VEHICULOS SET patente = ?, id_tipo_mantenimiento = ?, costo = ?, fecha_mantenimiento = ?, "
                + "kilometraje_mantenimiento = ?, proximo_mantenimiento_km = ?, proximo_mantenimiento_fecha = ?, id_trabajador = ? "
                + "WHERE id_mantenimiento = ?";

        try (Connection conn = conexion.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mantenimiento.getPatente());
            ps.setInt(2, mantenimiento.getIdTipoMantenimiento());
            ps.setBigDecimal(3, mantenimiento.getCosto());
            ps.setDate(4, toSqlDate(mantenimiento.getFechaMantenimiento()));
            ps.setInt(5, mantenimiento.getKilometrajeMantenimiento());
            ps.setInt(6, mantenimiento.getProximoMantenimientoKm());
            ps.setDate(7, toSqlDate(mantenimiento.getProximoMantenimientoFecha()));

            if (mantenimiento.getIdTrabajador() > 0) {
                ps.setInt(8, mantenimiento.getIdTrabajador());
            } else {
                ps.setNull(8, java.sql.Types.INTEGER);
            }

            ps.setInt(9, mantenimiento.getIdMantenimiento());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean eliminarMantenimiento(int idMantenimiento) {
        String sql = "DELETE FROM MANTENIMIENTOS_VEHICULOS WHERE id_mantenimiento = ?";

        try (Connection conn = conexion.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMantenimiento);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private List<MantenimientoVehiculo> ejecutarConsulta(String sql, String patente) {
        List<MantenimientoVehiculo> mantenimientos = new ArrayList<>();

        try (Connection conn = conexion.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            if (patente != null) {
                ps.setString(1, patente);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mantenimientos.add(mapearMantenimiento(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return mantenimientos;
    }

    private MantenimientoVehiculo mapearMantenimiento(ResultSet rs) throws SQLException {
        MantenimientoVehiculo mantenimiento = new MantenimientoVehiculo();
        mantenimiento.setIdMantenimiento(rs.getInt("id_mantenimiento"));
        mantenimiento.setPatente(rs.getString("patente"));
        mantenimiento.setIdTipoMantenimiento(rs.getInt("id_tipo_mantenimiento"));
        mantenimiento.setCosto(rs.getBigDecimal("costo"));
        mantenimiento.setFechaMantenimiento(toLocalDate(rs.getDate("fecha_mantenimiento")));
        mantenimiento.setKilometrajeMantenimiento(rs.getInt("kilometraje_mantenimiento"));
        mantenimiento.setProximoMantenimientoKm(rs.getInt("proximo_mantenimiento_km"));
        mantenimiento.setProximoMantenimientoFecha(toLocalDate(rs.getDate("proximo_mantenimiento_fecha")));
        int trabajador = rs.getInt("id_trabajador");
        mantenimiento.setIdTrabajador(rs.wasNull() ? 0 : trabajador);
        return mantenimiento;
    }

    private java.sql.Date toSqlDate(LocalDate date) {
        return date != null ? java.sql.Date.valueOf(date) : null;
    }

    private LocalDate toLocalDate(java.sql.Date date) {
        return date != null ? date.toLocalDate() : null;
    }
}
