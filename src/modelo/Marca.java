package modelo;

/**
 * Representa a la tabla MARCAS.
 */
public class Marca {

    private int idMarca;
    private String nombreMarca;
    private String paisOrigen;

    public Marca() {
    }

    public Marca(int idMarca, String nombreMarca, String paisOrigen) {
        this.idMarca = idMarca;
        this.nombreMarca = nombreMarca;
        this.paisOrigen = paisOrigen;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    @Override
    public String toString() {
        return "Marca{"
                + "idMarca=" + idMarca
                + ", nombreMarca='" + nombreMarca + '\''
                + ", paisOrigen='" + paisOrigen + '\''
                + '}';
    }
}
