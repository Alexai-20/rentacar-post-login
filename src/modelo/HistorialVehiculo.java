package modelo;

import java.time.LocalDate;

/**
 * DTO que consolida la información de historial de un vehículo.
 */
public class HistorialVehiculo {

    private final String patente;
    private final String facturas;
    private final String tiposMantenimiento;
    private final LocalDate ultimoMantenimiento;

    public HistorialVehiculo(String patente, String facturas,
            String tiposMantenimiento, LocalDate ultimoMantenimiento) {
        this.patente = patente;
        this.facturas = facturas;
        this.tiposMantenimiento = tiposMantenimiento;
        this.ultimoMantenimiento = ultimoMantenimiento;
    }

    public String getPatente() {
        return patente;
    }

    public String getFacturas() {
        return facturas;
    }

    public String getTiposMantenimiento() {
        return tiposMantenimiento;
    }

    public LocalDate getUltimoMantenimiento() {
        return ultimoMantenimiento;
    }
}
