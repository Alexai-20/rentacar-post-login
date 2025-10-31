package vista;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import vista.componentes.RoundedPanel;
import vista.estilos.ModernUIHelper;

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
        setSize(420, 360);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        ModernUIHelper.registerBackground(getContentPane());

        JLabel lblTitulo = new JLabel("Bienvenido a RentaCarMVC", JLabel.CENTER);
        lblTitulo.setFont(ModernUIHelper.DEFAULT_FONT.deriveFont(20f));
        lblTitulo.setForeground(ModernUIHelper.TEXT);
        lblTitulo.setBorder(new javax.swing.border.EmptyBorder(20, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        RoundedPanel panelFormulario = new RoundedPanel(24);
        panelFormulario.setLayout(new GridBagLayout());
        ModernUIHelper.applyCardStyle(panelFormulario);
        panelFormulario.setBorder(new javax.swing.border.EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel lblUsuario = new JLabel("Correo electrónico:");
        lblUsuario.setForeground(ModernUIHelper.TEXT);
        panelFormulario.add(lblUsuario, gbc);

        gbc.gridy = 1;
        txtUsuario.setColumns(20);
        txtUsuario.setBorder(new javax.swing.border.EmptyBorder(12, 12, 12, 12));
        panelFormulario.add(txtUsuario, gbc);

        gbc.gridy = 2;
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setForeground(ModernUIHelper.TEXT);
        panelFormulario.add(lblContrasena, gbc);

        gbc.gridy = 3;
        txtContrasena.setBorder(new javax.swing.border.EmptyBorder(12, 12, 12, 12));
        panelFormulario.add(txtContrasena, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(20, 0, 0, 0);
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        JButton btnIngresar = ModernUIHelper.createPrimaryButton("Ingresar");
        btnIngresar.addActionListener(e -> autenticar());

        JButton btnCancelar = ModernUIHelper.createSecondaryButton("Salir");
        btnCancelar.addActionListener(e -> System.exit(0));

        panelBotones.add(btnIngresar);
        panelBotones.add(btnCancelar);
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.CENTER);

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