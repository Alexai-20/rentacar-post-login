import javax.swing.SwingUtilities;
import vista.FrmPrincipal;

/**
 * Punto de entrada del sistema RentaCarMVC. Lanza la interfaz gráfica principal
 * construida bajo arquitectura MVC.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmPrincipal().setVisible(true));
    }
}
