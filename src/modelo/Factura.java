package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Facturación asociada a los alquileres.
 */
public class Factura {

    private int idFactura;
    private int idAlquiler;
    private String numeroFactura;
    private LocalDate fechaEmision;
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal montoTotal;
    private String estadoPago;
    private LocalDate fechaVencimiento;

    public Factura() {
    }

    public Factura(int idFactura, int idAlquiler, String numeroFactura,
            LocalDate fechaEmision, BigDecimal subtotal, BigDecimal impuestos,
            BigDecimal montoTotal, String estadoPago, LocalDate fechaVencimiento) {
        this.idFactura = idFactura;
        this.idAlquiler = idAlquiler;
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.montoTotal = montoTotal;
        this.estadoPago = estadoPago;
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(int idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public String toString() {
        return "Factura{"
                + "idFactura=" + idFactura
                + ", idAlquiler=" + idAlquiler
                + ", numeroFactura='" + numeroFactura + '\''
                + ", fechaEmision=" + fechaEmision
                + ", subtotal=" + subtotal
                + ", impuestos=" + impuestos
                + ", montoTotal=" + montoTotal
                + ", estadoPago='" + estadoPago + '\''
                + ", fechaVencimiento=" + fechaVencimiento
                + '}';
    }
}
