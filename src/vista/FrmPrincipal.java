package vista;

import conexion.Conexion;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import modelo.UsuarioAutenticado;

/**
 * Ventana principal del sistema RentaCarMVC. Sirve como punto de ingreso para
 * navegar hacia los módulos operativos.
 */
public class FrmPrincipal extends JFrame {

    private final Conexion conexion;
    private final UsuarioAutenticado usuario;

    public FrmPrincipal(Conexion conexion, UsuarioAutenticado usuario) {
        this.conexion = Objects.requireNonNull(conexion, "La conexión no puede ser nula");
        this.usuario = Objects.requireNonNull(usuario, "Debe existir un usuario autenticado");
        this.conexion.conectar();
        initComponents();
    }

    private void initComponents() {
        setTitle("RentaCarMVC - Panel Principal");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                conexion.desconectar();
            }
        });

        JLabel lblTitulo = new JLabel("Sistema de Gestión RentaCarMVC", JLabel.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 10, 10));

        JButton btnClientes = new JButton("Módulo de Clientes");
        btnClientes.addActionListener(e -> {
            if (!usuario.esAdministrador()) {
                JOptionPane.showMessageDialog(this, "El rol actual no puede acceder al módulo de clientes", "Acceso restringido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            new FrmClientes(conexion, usuario).setVisible(true);
        });

        JButton btnVehiculos = new JButton("Módulo de Vehículos");
        btnVehiculos.addActionListener(e -> new FrmVehiculos(conexion, usuario).setVisible(true));

        JButton btnReservas = new JButton("Módulo de Reservas");
        btnReservas.addActionListener(e -> new FrmReservas(conexion, usuario).setVisible(true));

        JButton btnSalir = new JButton("Cerrar sesión");
        btnSalir.addActionListener(e -> {
            conexion.desconectar();
            dispose();
            SwingUtilities.invokeLater(() -> new FrmLogin(conexion).setVisible(true));
        });

        configurarAccesosPorRol(btnClientes, btnVehiculos, btnReservas);
        panelBotones.add(btnClientes);
        panelBotones.add(btnVehiculos);
        panelBotones.add(btnReservas);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.CENTER);
    }

    private void configurarAccesosPorRol(JButton btnClientes, JButton btnVehiculos, JButton btnReservas) {
        String rol = usuario.getTipoUsuario();
        JLabel lblUsuario = new JLabel("Sesión iniciada como: " + usuario.getNombre() + " - " + rol, JLabel.CENTER);
        add(lblUsuario, BorderLayout.SOUTH);

        if (usuario.esAdministrador()) {
            return;
        }

        if (usuario.esTrabajador()) {
            btnClientes.setEnabled(false);
            btnClientes.setToolTipText("Disponible solo para Administrador");
        } else {
            btnClientes.setEnabled(false);
            btnClientes.setToolTipText("Rol sin permisos para gestión de clientes");
            btnVehiculos.setEnabled(false);
            btnReservas.setEnabled(false);
            btnVehiculos.setToolTipText("Rol sin permisos para gestión de vehículos");
            btnReservas.setToolTipText("Rol sin permisos para gestión de reservas");
        }
    }
}
