package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Información de reservas de vehículos.
 */
public class Reserva {

    private int idReserva;
    private int idCliente;
    private String patente;
    private LocalDate fechaReserva;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estadoReserva;
    private BigDecimal montoEstimado;
    private int idTrabajador;

    public Reserva() {
    }

    public Reserva(int idReserva, int idCliente, String patente,
            LocalDate fechaReserva, LocalDate fechaInicio, LocalDate fechaFin,
            String estadoReserva, BigDecimal montoEstimado, int idTrabajador) {
        this.idReserva = idReserva;
        this.idCliente = idCliente;
        this.patente = patente;
        this.fechaReserva = fechaReserva;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estadoReserva = estadoReserva;
        this.montoEstimado = montoEstimado;
        this.idTrabajador = idTrabajador;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    public BigDecimal getMontoEstimado() {
        return montoEstimado;
    }

    public void setMontoEstimado(BigDecimal montoEstimado) {
        this.montoEstimado = montoEstimado;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    @Override
    public String toString() {
        return "Reserva{"
                + "idReserva=" + idReserva
                + ", idCliente=" + idCliente
                + ", patente='" + patente + '\''
                + ", fechaReserva=" + fechaReserva
                + ", fechaInicio=" + fechaInicio
                + ", fechaFin=" + fechaFin
                + ", estadoReserva='" + estadoReserva + '\''
                + ", montoEstimado=" + montoEstimado
                + ", idTrabajador=" + idTrabajador
                + '}';
    }
}
