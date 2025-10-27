package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que representa a los vehículos disponibles para arriendo.
 */
public class Vehiculo {

    private String patente;
    private int idModelo;
    private LocalDate anio;
    private String tipoCombustible;
    private int kilometraje;
    private String color;
    private int numeroAsientos;
    private String tipoVehiculo;
    private BigDecimal tarifaDiaria;
    private String estadoMantenimiento;
    private boolean disponibilidad;
    private LocalDate fechaRegistro;
    private LocalDate fechaUltimaRevision;

    public Vehiculo() {
    }

    public Vehiculo(String patente, int idModelo, LocalDate anio,
            String tipoCombustible, int kilometraje, String color,
            int numeroAsientos, String tipoVehiculo, BigDecimal tarifaDiaria,
            String estadoMantenimiento, boolean disponibilidad,
            LocalDate fechaRegistro, LocalDate fechaUltimaRevision) {
        this.patente = patente;
        this.idModelo = idModelo;
        this.anio = anio;
        this.tipoCombustible = tipoCombustible;
        this.kilometraje = kilometraje;
        this.color = color;
        this.numeroAsientos = numeroAsientos;
        this.tipoVehiculo = tipoVehiculo;
        this.tarifaDiaria = tarifaDiaria;
        this.estadoMantenimiento = estadoMantenimiento;
        this.disponibilidad = disponibilidad;
        this.fechaRegistro = fechaRegistro;
        this.fechaUltimaRevision = fechaUltimaRevision;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public int getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(int idModelo) {
        this.idModelo = idModelo;
    }

    public LocalDate getAnio() {
        return anio;
    }

    public void setAnio(LocalDate anio) {
        this.anio = anio;
    }

    public String getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(String tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(int kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getNumeroAsientos() {
        return numeroAsientos;
    }

    public void setNumeroAsientos(int numeroAsientos) {
        this.numeroAsientos = numeroAsientos;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public BigDecimal getTarifaDiaria() {
        return tarifaDiaria;
    }

    public void setTarifaDiaria(BigDecimal tarifaDiaria) {
        this.tarifaDiaria = tarifaDiaria;
    }

    public String getEstadoMantenimiento() {
        return estadoMantenimiento;
    }

    public void setEstadoMantenimiento(String estadoMantenimiento) {
        this.estadoMantenimiento = estadoMantenimiento;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDate getFechaUltimaRevision() {
        return fechaUltimaRevision;
    }

    public void setFechaUltimaRevision(LocalDate fechaUltimaRevision) {
        this.fechaUltimaRevision = fechaUltimaRevision;
    }

    @Override
    public String toString() {
        return "Vehiculo{"
                + "patente='" + patente + '\''
                + ", idModelo=" + idModelo
                + ", anio=" + anio
                + ", tipoCombustible='" + tipoCombustible + '\''
                + ", kilometraje=" + kilometraje
                + ", color='" + color + '\''
                + ", numeroAsientos=" + numeroAsientos
                + ", tipoVehiculo='" + tipoVehiculo + '\''
                + ", tarifaDiaria=" + tarifaDiaria
                + ", estadoMantenimiento='" + estadoMantenimiento + '\''
                + ", disponibilidad=" + disponibilidad
                + ", fechaRegistro=" + fechaRegistro
                + ", fechaUltimaRevision=" + fechaUltimaRevision
                + '}';
    }
}
