package modelo;

/**
 * Representa al usuario que logró autenticarse exitosamente. Contiene los
 * datos mínimos para validar sus permisos de navegación dentro del sistema.
 */
public class UsuarioAutenticado {

    private final int idUsuario;
    private final String nombre;
    private final String tipoUsuario;

    public UsuarioAutenticado(int idUsuario, String nombre, String tipoUsuario) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.tipoUsuario = tipoUsuario != null ? tipoUsuario : "";
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public boolean esAdministrador() {
        return "Administrador".equalsIgnoreCase(tipoUsuario);
    }

    public boolean esTrabajador() {
        return "Trabajador".equalsIgnoreCase(tipoUsuario);
    }
}
