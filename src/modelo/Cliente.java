package modelo;

import java.time.LocalDate;

/**
 * Representa a los clientes registrados en la aplicación. Extiende a
 * {@link Usuario} para reutilizar los campos comunes definidos por la tabla
 * USUARIOS y complementarlos con los datos de CLIENTES_INFO.
 */
public class Cliente extends Usuario {

    private int idCliente;
    private String numeroLicencia;
    private LocalDate fechaVencimientoLicencia;
    private String tipoCliente;
    private String empresa;

    public Cliente() {
    }

    public Cliente(int idUsuario, String nombre, String apellido, String rut,
            String email, String telefono, String direccion,
            LocalDate fechaNacimiento, String tipoUsuario, String estado,
            LocalDate fechaRegistro, LocalDate fechaUltimaModificacion,
            int idCliente, String numeroLicencia,
            LocalDate fechaVencimientoLicencia, String tipoCliente,
            String empresa) {
        super(idUsuario, nombre, apellido, rut, email, telefono, direccion,
                fechaNacimiento, tipoUsuario, estado, fechaRegistro,
                fechaUltimaModificacion);
        this.idCliente = idCliente;
        this.numeroLicencia = numeroLicencia;
        this.fechaVencimientoLicencia = fechaVencimientoLicencia;
        this.tipoCliente = tipoCliente;
        this.empresa = empresa;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public LocalDate getFechaVencimientoLicencia() {
        return fechaVencimientoLicencia;
    }

    public void setFechaVencimientoLicencia(LocalDate fechaVencimientoLicencia) {
        this.fechaVencimientoLicencia = fechaVencimientoLicencia;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    @Override
    public String resumenPerfil() {
        return String.format("Cliente %s %s (%s)", getNombre(), getApellido(),
                getTipoCliente());
    }

    @Override
    public String toString() {
        return "Cliente{"
                + "idCliente=" + idCliente
                + ", numeroLicencia='" + numeroLicencia + '\''
                + ", fechaVencimientoLicencia=" + fechaVencimientoLicencia
                + ", tipoCliente='" + tipoCliente + '\''
                + ", empresa='" + empresa + '\''
                + ", datosUsuario=" + super.toString()
                + '}';
    }
}
