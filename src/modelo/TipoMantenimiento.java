package modelo;

/**
 * Datos maestros para los tipos de mantenimiento que pueden aplicarse a un
 * vehículo.
 */
public class TipoMantenimiento {

    private int idMantenimiento;
    private String nombre;
    private String descripcion;
    private int kilometrajeRecomendado;

    public TipoMantenimiento() {
    }

    public TipoMantenimiento(int idMantenimiento, String nombre,
            String descripcion, int kilometrajeRecomendado) {
        this.idMantenimiento = idMantenimiento;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.kilometrajeRecomendado = kilometrajeRecomendado;
    }

    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(int idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getKilometrajeRecomendado() {
        return kilometrajeRecomendado;
    }

    public void setKilometrajeRecomendado(int kilometrajeRecomendado) {
        this.kilometrajeRecomendado = kilometrajeRecomendado;
    }

    @Override
    public String toString() {
        return "TipoMantenimiento{"
                + "idMantenimiento=" + idMantenimiento
                + ", nombre='" + nombre + '\''
                + ", descripcion='" + descripcion + '\''
                + ", kilometrajeRecomendado=" + kilometrajeRecomendado
                + '}';
    }
}
