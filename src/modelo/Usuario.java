package modelo;

import java.time.LocalDate;

/**
 * Representa a cualquier cuenta registrada en el sistema. Sirve como clase
 * base para {@link Cliente} y {@link Trabajador}, encapsulando los datos
 * comunes descritos en la tabla USUARIOS.
 */
public abstract class Usuario {

    private int idUsuario;
    private String nombre;
    private String apellido;
    private String rut;
    private String email;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    private String tipoUsuario;
    private String estado;
    private LocalDate fechaRegistro;
    private LocalDate fechaUltimaModificacion;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombre, String apellido, String rut,
            String email, String telefono, String direccion,
            LocalDate fechaNacimiento, String tipoUsuario, String estado,
            LocalDate fechaRegistro, LocalDate fechaUltimaModificacion) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoUsuario = tipoUsuario;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDate getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    public void setFechaUltimaModificacion(LocalDate fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

    /**
     * Debe devolver una descripción textual del usuario según el rol
     * específico de cada subclase.
     */
    public abstract String resumenPerfil();

    @Override
    public String toString() {
        return "Usuario{"
                + "idUsuario=" + idUsuario
                + ", nombre='" + nombre + '\''
                + ", apellido='" + apellido + '\''
                + ", rut='" + rut + '\''
                + ", email='" + email + '\''
                + ", telefono='" + telefono + '\''
                + ", direccion='" + direccion + '\''
                + ", fechaNacimiento=" + fechaNacimiento
                + ", tipoUsuario='" + tipoUsuario + '\''
                + ", estado='" + estado + '\''
                + ", fechaRegistro=" + fechaRegistro
                + ", fechaUltimaModificacion=" + fechaUltimaModificacion
                + '}';
    }
}
