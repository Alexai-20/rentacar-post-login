package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Extiende los datos de mantenimiento del vehículo con la descripción del
 * tipo de mantenimiento ejecutado.
 */
public class MantenimientoVehiculoDetalle extends MantenimientoVehiculo {

    private String nombreTipoMantenimiento;
    private String descripcionTipoMantenimiento;

    public MantenimientoVehiculoDetalle() {
    }

    public MantenimientoVehiculoDetalle(int idMantenimiento, String patente,
            int idTipoMantenimiento, BigDecimal costo,
            LocalDate fechaMantenimiento, int kilometrajeMantenimiento,
            int proximoMantenimientoKm, LocalDate proximoMantenimientoFecha,
            int idTrabajador, String nombreTipoMantenimiento,
            String descripcionTipoMantenimiento) {
        super(idMantenimiento, patente, idTipoMantenimiento, costo,
                fechaMantenimiento, kilometrajeMantenimiento,
                proximoMantenimientoKm, proximoMantenimientoFecha, idTrabajador);
        this.nombreTipoMantenimiento = nombreTipoMantenimiento;
        this.descripcionTipoMantenimiento = descripcionTipoMantenimiento;
    }

    public String getNombreTipoMantenimiento() {
        return nombreTipoMantenimiento;
    }

    public void setNombreTipoMantenimiento(String nombreTipoMantenimiento) {
        this.nombreTipoMantenimiento = nombreTipoMantenimiento;
    }

    public String getDescripcionTipoMantenimiento() {
        return descripcionTipoMantenimiento;
    }

    public void setDescripcionTipoMantenimiento(String descripcionTipoMantenimiento) {
        this.descripcionTipoMantenimiento = descripcionTipoMantenimiento;
    }

    @Override
    public String toString() {
        return "MantenimientoVehiculoDetalle{"
                + "nombreTipoMantenimiento='" + nombreTipoMantenimiento + '\''
                + ", descripcionTipoMantenimiento='" + descripcionTipoMantenimiento + '\''
                + ", base=" + super.toString()
                + '}';
    }
}
