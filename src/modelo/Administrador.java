package modelo;

import java.time.LocalDate;

/**
 * Representa a los usuarios de tipo ADMINISTRADOR que pueden autenticarse en
 * el sistema.
 */
public class Administrador extends Usuario {

    public Administrador() {
    }

    public Administrador(int idUsuario, String nombre, String apellido, String rut,
            String email, String telefono, String direccion,
            LocalDate fechaNacimiento, String tipoUsuario, String estado,
            LocalDate fechaRegistro, LocalDate fechaUltimaModificacion,
            String contrasenaHash) {
        super(idUsuario, nombre, apellido, rut, email, telefono, direccion,
                fechaNacimiento, tipoUsuario, estado, fechaRegistro,
                fechaUltimaModificacion, contrasenaHash);
    }

    @Override
    public String resumenPerfil() {
        return String.format("Administrador %s %s", getNombre(), getApellido());
    }
}
