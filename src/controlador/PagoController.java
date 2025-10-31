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
import modelo.Pago;

/**
 * Controlador para gestionar los pagos registrados en la base de datos.
 */
public class PagoController {

    private final Conexion conexion;

    public PagoController(Conexion conexion) {
        this.conexion = conexion;
    }

    public List<Pago> obtenerPagos() {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT id_pago, id_factura, monto_pago, metodo_pago, fecha_pago, numero_transaccion, id_trabajador "
                + "FROM PAGOS ORDER BY fecha_pago DESC, id_pago DESC";

        try (Connection conn = conexion.conectar();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pagos.add(mapearPago(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pagos;
    }

    public boolean crearPago(Pago pago) {
        String sql = "INSERT INTO PAGOS(id_factura, monto_pago, metodo_pago, fecha_pago, numero_transaccion, id_trabajador) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexion.conectar();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pago.getIdFactura());
            ps.setBigDecimal(2, pago.getMontoPago());
            ps.setString(3, pago.getMetodoPago());
            ps.setDate(4, toSqlDate(pago.getFechaPago()));
            ps.setString(5, pago.getNumeroTransaccion());

            if (pago.getIdTrabajador() > 0) {
                ps.setInt(6, pago.getIdTrabajador());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pago.setIdPago(rs.getInt(1));
                }
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean actualizarPago(Pago pago) {
        String sql = "UPDATE PAGOS SET id_factura = ?, monto_pago = ?, metodo_pago = ?, fecha_pago = ?, numero_transaccion = ?, "
                + "id_trabajador = ? WHERE id_pago = ?";

        try (Connection conn = conexion.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pago.getIdFactura());
            ps.setBigDecimal(2, pago.getMontoPago());
            ps.setString(3, pago.getMetodoPago());
            ps.setDate(4, toSqlDate(pago.getFechaPago()));
            ps.setString(5, pago.getNumeroTransaccion());

            if (pago.getIdTrabajador() > 0) {
                ps.setInt(6, pago.getIdTrabajador());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }

            ps.setInt(7, pago.getIdPago());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean eliminarPago(int idPago) {
        String sql = "DELETE FROM PAGOS WHERE id_pago = ?";

        try (Connection conn = conexion.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPago);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private Pago mapearPago(ResultSet rs) throws SQLException {
        Pago pago = new Pago();
        pago.setIdPago(rs.getInt("id_pago"));
        pago.setIdFactura(rs.getInt("id_factura"));
        pago.setMontoPago(rs.getBigDecimal("monto_pago"));
        pago.setMetodoPago(rs.getString("metodo_pago"));
        pago.setFechaPago(toLocalDate(rs.getDate("fecha_pago")));
        pago.setNumeroTransaccion(rs.getString("numero_transaccion"));
        int trabajador = rs.getInt("id_trabajador");
        pago.setIdTrabajador(rs.wasNull() ? 0 : trabajador);
        return pago;
    }

    private java.sql.Date toSqlDate(LocalDate date) {
        return date != null ? java.sql.Date.valueOf(date) : null;
    }

    private LocalDate toLocalDate(java.sql.Date date) {
        return date != null ? date.toLocalDate() : null;
    }
}
