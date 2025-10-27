package modelo;

/**
 * Representa a la tabla MODELOS.
 */
public class Modelo {

    private int idModelo;
    private int idMarca;
    private String nombreModelo;

    public Modelo() {
    }

    public Modelo(int idModelo, int idMarca, String nombreModelo) {
        this.idModelo = idModelo;
        this.idMarca = idMarca;
        this.nombreModelo = nombreModelo;
    }

    public int getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(int idModelo) {
        this.idModelo = idModelo;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombreModelo() {
        return nombreModelo;
    }

    public void setNombreModelo(String nombreModelo) {
        this.nombreModelo = nombreModelo;
    }

    @Override
    public String toString() {
        return "Modelo{"
                + "idModelo=" + idModelo
                + ", idMarca=" + idMarca
                + ", nombreModelo='" + nombreModelo + '\''
                + '}';
    }
}
