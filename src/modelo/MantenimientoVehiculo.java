package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Información histórica de mantenimientos aplicados a los vehículos.
 */
public class MantenimientoVehiculo {

    private int idMantenimiento;
    private String patente;
    private int idTipoMantenimiento;
    private BigDecimal costo;
    private LocalDate fechaMantenimiento;
    private int kilometrajeMantenimiento;
    private int proximoMantenimientoKm;
    private LocalDate proximoMantenimientoFecha;
    private int idTrabajador;

    public MantenimientoVehiculo() {
    }

    public MantenimientoVehiculo(int idMantenimiento, String patente,
            int idTipoMantenimiento, BigDecimal costo,
            LocalDate fechaMantenimiento, int kilometrajeMantenimiento,
            int proximoMantenimientoKm, LocalDate proximoMantenimientoFecha,
            int idTrabajador) {
        this.idMantenimiento = idMantenimiento;
        this.patente = patente;
        this.idTipoMantenimiento = idTipoMantenimiento;
        this.costo = costo;
        this.fechaMantenimiento = fechaMantenimiento;
        this.kilometrajeMantenimiento = kilometrajeMantenimiento;
        this.proximoMantenimientoKm = proximoMantenimientoKm;
        this.proximoMantenimientoFecha = proximoMantenimientoFecha;
        this.idTrabajador = idTrabajador;
    }

    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(int idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public int getIdTipoMantenimiento() {
        return idTipoMantenimiento;
    }

    public void setIdTipoMantenimiento(int idTipoMantenimiento) {
        this.idTipoMantenimiento = idTipoMantenimiento;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public LocalDate getFechaMantenimiento() {
        return fechaMantenimiento;
    }

    public void setFechaMantenimiento(LocalDate fechaMantenimiento) {
        this.fechaMantenimiento = fechaMantenimiento;
    }

    public int getKilometrajeMantenimiento() {
        return kilometrajeMantenimiento;
    }

    public void setKilometrajeMantenimiento(int kilometrajeMantenimiento) {
        this.kilometrajeMantenimiento = kilometrajeMantenimiento;
    }

    public int getProximoMantenimientoKm() {
        return proximoMantenimientoKm;
    }

    public void setProximoMantenimientoKm(int proximoMantenimientoKm) {
        this.proximoMantenimientoKm = proximoMantenimientoKm;
    }

    public LocalDate getProximoMantenimientoFecha() {
        return proximoMantenimientoFecha;
    }

    public void setProximoMantenimientoFecha(LocalDate proximoMantenimientoFecha) {
        this.proximoMantenimientoFecha = proximoMantenimientoFecha;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    @Override
    public String toString() {
        return "MantenimientoVehiculo{"
                + "idMantenimiento=" + idMantenimiento
                + ", patente='" + patente + '\''
                + ", idTipoMantenimiento=" + idTipoMantenimiento
                + ", costo=" + costo
                + ", fechaMantenimiento=" + fechaMantenimiento
                + ", kilometrajeMantenimiento=" + kilometrajeMantenimiento
                + ", proximoMantenimientoKm=" + proximoMantenimientoKm
                + ", proximoMantenimientoFecha=" + proximoMantenimientoFecha
                + ", idTrabajador=" + idTrabajador
                + '}';
    }
}
