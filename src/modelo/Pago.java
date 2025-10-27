package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Pagos registrados contra las facturas emitidas.
 */
public class Pago {

    private int idPago;
    private int idFactura;
    private BigDecimal montoPago;
    private String metodoPago;
    private LocalDate fechaPago;
    private String numeroTransaccion;
    private int idTrabajador;

    public Pago() {
    }

    public Pago(int idPago, int idFactura, BigDecimal montoPago,
            String metodoPago, LocalDate fechaPago, String numeroTransaccion,
            int idTrabajador) {
        this.idPago = idPago;
        this.idFactura = idFactura;
        this.montoPago = montoPago;
        this.metodoPago = metodoPago;
        this.fechaPago = fechaPago;
        this.numeroTransaccion = numeroTransaccion;
        this.idTrabajador = idTrabajador;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public BigDecimal getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(BigDecimal montoPago) {
        this.montoPago = montoPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    @Override
    public String toString() {
        return "Pago{"
                + "idPago=" + idPago
                + ", idFactura=" + idFactura
                + ", montoPago=" + montoPago
                + ", metodoPago='" + metodoPago + '\''
                + ", fechaPago=" + fechaPago
                + ", numeroTransaccion='" + numeroTransaccion + '\''
                + ", idTrabajador=" + idTrabajador
                + '}';
    }
}
