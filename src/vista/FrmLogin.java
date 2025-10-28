package vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Ventana de inicio de sesión básica para validar el acceso al sistema.
 */
public class FrmLogin extends JFrame {

    private final JTextField txtUsuario;
    private final JPasswordField txtContrasena;

    public FrmLogin() {
        txtUsuario = new JTextField();
        txtContrasena = new JPasswordField();
        initComponents();
    }

    private void initComponents() {
        setTitle("RentaCarMVC - Inicio de sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelCampos = new JPanel(new GridLayout(0, 1, 5, 5));
        panelCampos.add(new JLabel("Usuario:"));
        panelCampos.add(txtUsuario);
        panelCampos.add(new JLabel("Contraseña:"));
        panelCampos.add(txtContrasena);

        JPanel panelBotones = new JPanel();
        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.addActionListener(e -> autenticar());

        JButton btnCancelar = new JButton("Salir");
        btnCancelar.addActionListener(e -> System.exit(0));

        panelBotones.add(btnIngresar);
        panelBotones.add(btnCancelar);

        add(new JLabel("Bienvenido a RentaCarMVC", JLabel.CENTER), BorderLayout.NORTH);
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnIngresar);
    }

    private void autenticar() {
        String usuario = txtUsuario.getText().trim();
        char[] contrasenaIngresada = txtContrasena.getPassword();

        boolean credencialesCorrectas = "admin".equals(usuario)
                && "1234".equals(new String(contrasenaIngresada));

        if (credencialesCorrectas) {
            Arrays.fill(contrasenaIngresada, '\0');
            JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso", "Acceso permitido",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            SwingUtilities.invokeLater(() -> new FrmPrincipal().setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Acceso denegado",
                    JOptionPane.ERROR_MESSAGE);
            Arrays.fill(contrasenaIngresada, '\0');
            txtContrasena.setText("");
            txtContrasena.requestFocusInWindow();
        }
    }
}