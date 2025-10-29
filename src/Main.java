import javax.swing.SwingUtilities;
import vista.FrmLogin;
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmLogin().setVisible(true));
    }
}