package modelo;

import java.time.LocalDate;

/**
 * Registro histórico del desempeño de los clientes.
 */
public class HistorialCliente {

    private int idHistorial;
    private int idCliente;
    private int idAlquiler;
    private int valoracion;
    private LocalDate fechaRegistro;

    public HistorialCliente() {
    }

    public HistorialCliente(int idHistorial, int idCliente, int idAlquiler,
            int valoracion, LocalDate fechaRegistro) {
        this.idHistorial = idHistorial;
        this.idCliente = idCliente;
        this.idAlquiler = idAlquiler;
        this.valoracion = valoracion;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(int idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "HistorialCliente{"
                + "idHistorial=" + idHistorial
                + ", idCliente=" + idCliente
                + ", idAlquiler=" + idAlquiler
                + ", valoracion=" + valoracion
                + ", fechaRegistro=" + fechaRegistro
                + '}';
    }
}
