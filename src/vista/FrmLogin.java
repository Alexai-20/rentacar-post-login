package vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import controlador.AutenticacionController;
import conexion.Conexion;
import java.util.Arrays;
import java.util.Optional;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import modelo.Administrador;

/**
 * Ventana de inicio de sesión básica para validar el acceso al sistema.
 */
public class FrmLogin extends JFrame {

    private final JTextField txtUsuario;
    private final JPasswordField txtContrasena;
    private final Conexion conexion;
    private final AutenticacionController autenticacionController;

    public FrmLogin() {
        this.conexion = new Conexion();
        this.autenticacionController = new AutenticacionController(conexion);
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
        panelCampos.add(new JLabel("Correo electrónico:"));
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

        if (usuario.isEmpty() || contrasenaIngresada.length == 0) {
            JOptionPane.showMessageDialog(this, "Ingrese correo y contraseña", "Validación", JOptionPane.WARNING_MESSAGE);
            Arrays.fill(contrasenaIngresada, '\0');
            return;
        }

        Optional<Administrador> administrador = autenticacionController.autenticar(usuario, contrasenaIngresada);
        Arrays.fill(contrasenaIngresada, '\0');

        if (administrador.isPresent()) {
            JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso", "Acceso permitido",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            SwingUtilities.invokeLater(() -> new FrmPrincipal(conexion, administrador.get()).setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales inválidas o usuario sin permisos", "Acceso denegado",
                    JOptionPane.ERROR_MESSAGE);
            txtContrasena.setText("");
            txtContrasena.requestFocusInWindow();
        }
    }
}