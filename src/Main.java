import javax.swing.SwingUtilities;
import vista.FrmPrincipal;
import vista.FrmLogin;
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmPrincipal().setVisible(true));
        SwingUtilities.invokeLater(() -> new FrmLogin().setVisible(true));
    }
}