import conexion.Conexion;
import javax.swing.SwingUtilities;
import vista.FrmLogin;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Conexion conexion = new Conexion();
            new FrmLogin(conexion).setVisible(true);
        });
    }
}
