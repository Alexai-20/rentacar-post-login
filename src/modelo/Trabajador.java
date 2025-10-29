package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modela a los trabajadores del sistema según la estructura de
 * TRABAJADORES_INFO.
 */
public class Trabajador extends Usuario {

    private int idTrabajador;
    private String cargo;
    private String departamento;
    private LocalDate fechaContratacion;
    private BigDecimal salario;

    public Trabajador() {
    }

    public Trabajador(int idUsuario, String nombre, String apellido, String rut,
            String email, String telefono, String direccion,
            LocalDate fechaNacimiento, String tipoUsuario, String estado,
            LocalDate fechaRegistro, LocalDate fechaUltimaModificacion,
            String contrasenaHash, int idTrabajador, String cargo, String departamento,
            LocalDate fechaContratacion, BigDecimal salario) {
        super(idUsuario, nombre, apellido, rut, email, telefono, direccion,
                fechaNacimiento, tipoUsuario, estado, fechaRegistro,
                fechaUltimaModificacion, contrasenaHash);
        this.idTrabajador = idTrabajador;
        this.cargo = cargo;
        this.departamento = departamento;
        this.fechaContratacion = fechaContratacion;
        this.salario = salario;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    @Override
    public String resumenPerfil() {
        return String.format("Trabajador %s %s - %s", getNombre(),
                getApellido(), cargo);
    }

    @Override
    public String toString() {
        return "Trabajador{"
                + "idTrabajador=" + idTrabajador
                + ", cargo='" + cargo + '\''
                + ", departamento='" + departamento + '\''
                + ", fechaContratacion=" + fechaContratacion
                + ", salario=" + salario
                + ", datosUsuario=" + super.toString()
                + '}';
    }
}
