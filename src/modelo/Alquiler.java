package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representa el ciclo de vida del alquiler de un vehículo.
 */
public class Alquiler {

    private int idAlquiler;
    private int idReserva;
    private LocalDate fechaInicio;
    private LocalDate fechaFinProgramada;
    private LocalDate fechaFinReal;
    private int kilometrajeInicial;
    private int kilometrajeFinal;
    private String estadoAlquiler;
    private BigDecimal montoTotal;
    private int diasRetraso;
    private BigDecimal cargoRetraso;
    private int idTrabajadorEntrega;
    private int idTrabajadorDevolucion;

    public Alquiler() {
    }

    public Alquiler(int idAlquiler, int idReserva, LocalDate fechaInicio,
            LocalDate fechaFinProgramada, LocalDate fechaFinReal,
            int kilometrajeInicial, int kilometrajeFinal, String estadoAlquiler,
            BigDecimal montoTotal, int diasRetraso, BigDecimal cargoRetraso,
            int idTrabajadorEntrega, int idTrabajadorDevolucion) {
        this.idAlquiler = idAlquiler;
        this.idReserva = idReserva;
        this.fechaInicio = fechaInicio;
        this.fechaFinProgramada = fechaFinProgramada;
        this.fechaFinReal = fechaFinReal;
        this.kilometrajeInicial = kilometrajeInicial;
        this.kilometrajeFinal = kilometrajeFinal;
        this.estadoAlquiler = estadoAlquiler;
        this.montoTotal = montoTotal;
        this.diasRetraso = diasRetraso;
        this.cargoRetraso = cargoRetraso;
        this.idTrabajadorEntrega = idTrabajadorEntrega;
        this.idTrabajadorDevolucion = idTrabajadorDevolucion;
    }

    public int getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(int idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFinProgramada() {
        return fechaFinProgramada;
    }

    public void setFechaFinProgramada(LocalDate fechaFinProgramada) {
        this.fechaFinProgramada = fechaFinProgramada;
    }

    public LocalDate getFechaFinReal() {
        return fechaFinReal;
    }

    public void setFechaFinReal(LocalDate fechaFinReal) {
        this.fechaFinReal = fechaFinReal;
    }

    public int getKilometrajeInicial() {
        return kilometrajeInicial;
    }

    public void setKilometrajeInicial(int kilometrajeInicial) {
        this.kilometrajeInicial = kilometrajeInicial;
    }

    public int getKilometrajeFinal() {
        return kilometrajeFinal;
    }

    public void setKilometrajeFinal(int kilometrajeFinal) {
        this.kilometrajeFinal = kilometrajeFinal;
    }

    public String getEstadoAlquiler() {
        return estadoAlquiler;
    }

    public void setEstadoAlquiler(String estadoAlquiler) {
        this.estadoAlquiler = estadoAlquiler;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public int getDiasRetraso() {
        return diasRetraso;
    }

    public void setDiasRetraso(int diasRetraso) {
        this.diasRetraso = diasRetraso;
    }

    public BigDecimal getCargoRetraso() {
        return cargoRetraso;
    }

    public void setCargoRetraso(BigDecimal cargoRetraso) {
        this.cargoRetraso = cargoRetraso;
    }

    public int getIdTrabajadorEntrega() {
        return idTrabajadorEntrega;
    }

    public void setIdTrabajadorEntrega(int idTrabajadorEntrega) {
        this.idTrabajadorEntrega = idTrabajadorEntrega;
    }

    public int getIdTrabajadorDevolucion() {
        return idTrabajadorDevolucion;
    }

    public void setIdTrabajadorDevolucion(int idTrabajadorDevolucion) {
        this.idTrabajadorDevolucion = idTrabajadorDevolucion;
    }

    @Override
    public String toString() {
        return "Alquiler{"
                + "idAlquiler=" + idAlquiler
                + ", idReserva=" + idReserva
                + ", fechaInicio=" + fechaInicio
                + ", fechaFinProgramada=" + fechaFinProgramada
                + ", fechaFinReal=" + fechaFinReal
                + ", kilometrajeInicial=" + kilometrajeInicial
                + ", kilometrajeFinal=" + kilometrajeFinal
                + ", estadoAlquiler='" + estadoAlquiler + '\''
                + ", montoTotal=" + montoTotal
                + ", diasRetraso=" + diasRetraso
                + ", cargoRetraso=" + cargoRetraso
                + ", idTrabajadorEntrega=" + idTrabajadorEntrega
                + ", idTrabajadorDevolucion=" + idTrabajadorDevolucion
                + '}';
    }
}
