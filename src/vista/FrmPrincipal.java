package vista;

import conexion.Conexion;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import modelo.Administrador;
import vista.componentes.RoundedPanel;
import vista.estilos.ModernUIHelper;

/**
 * Ventana principal del sistema RentaCarMVC. Sirve como punto de ingreso para
 * navegar hacia los módulos operativos.
 */
public class FrmPrincipal extends JFrame {

    private final Conexion conexion;
    private final Administrador administrador;

    public FrmPrincipal(Conexion conexion, Administrador administrador) {
        this.conexion = conexion;
        this.administrador = administrador;
        this.conexion.conectar();
        initComponents();
    }

    private void initComponents() {
        setTitle("RentaCarMVC - Panel Principal");
        setSize(620, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        ModernUIHelper.registerBackground(getContentPane());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                conexion.desconectar();
            }
        });

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(new javax.swing.border.EmptyBorder(30, 30, 30, 30));
        add(contenedor, BorderLayout.CENTER);

        JLabel lblTitulo = new JLabel("Sistema de Gestión RentaCarMVC", JLabel.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(ModernUIHelper.TEXT);
        contenedor.add(lblTitulo, BorderLayout.NORTH);

        RoundedPanel panelBotones = new RoundedPanel(26);
        panelBotones.setLayout(new GridBagLayout());
        ModernUIHelper.applyCardStyle(panelBotones);
        contenedor.add(panelBotones, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JButton btnClientes = ModernUIHelper.createPrimaryButton("Gestión de Clientes");
        btnClientes.addActionListener(e -> new FrmClientes(conexion).setVisible(true));
        panelBotones.add(btnClientes, gbc);

        JButton btnVehiculos = ModernUIHelper.createPrimaryButton("Gestión de Vehículos");
        btnVehiculos.addActionListener(e -> new FrmVehiculos(conexion).setVisible(true));
        gbc.gridy = 1;
        panelBotones.add(btnVehiculos, gbc);

        JButton btnReservas = ModernUIHelper.createPrimaryButton("Gestión de Reservas");
        btnReservas.addActionListener(e -> new FrmReservas(conexion).setVisible(true));
        gbc.gridy = 2;
        panelBotones.add(btnReservas, gbc);

        JButton btnPagos = ModernUIHelper.createPrimaryButton("Gestión de Pagos");
        btnPagos.addActionListener(e -> new FrmPagos(conexion).setVisible(true));
        gbc.gridy = 3;
        panelBotones.add(btnPagos, gbc);

        JButton btnMantenimientos = ModernUIHelper.createPrimaryButton("Historial de Mantenimiento");
        btnMantenimientos.addActionListener(e -> new FrmMantenimientos(conexion).setVisible(true));
        gbc.gridy = 4;
        panelBotones.add(btnMantenimientos, gbc);

        JButton btnSalir = ModernUIHelper.createSecondaryButton("Cerrar sesión");
        btnSalir.addActionListener(e -> {
            conexion.desconectar();
            dispose();
        });
        gbc.gridy = 5;
        panelBotones.add(btnSalir, gbc);

        JLabel lblUsuario = new JLabel(
                "Administrador: " + administrador.getNombre() + " " + administrador.getApellido(),
                JLabel.CENTER);
        lblUsuario.setForeground(ModernUIHelper.TEXT);
        lblUsuario.setFont(ModernUIHelper.DEFAULT_FONT.deriveFont(Font.BOLD, 16f));
        lblUsuario.setBorder(new javax.swing.border.EmptyBorder(20, 0, 0, 0));
        contenedor.add(lblUsuario, BorderLayout.SOUTH);
    }
}
