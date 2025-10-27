package vista;

import conexion.Conexion;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Ventana principal del sistema RentaCarMVC. Sirve como punto de ingreso para
 * navegar hacia los módulos operativos.
 */
public class FrmPrincipal extends JFrame {

    private final Conexion conexion;

    public FrmPrincipal() {
        this.conexion = new Conexion();
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
        btnClientes.addActionListener(e -> new FrmClientes(conexion).setVisible(true));

        JButton btnVehiculos = new JButton("Módulo de Vehículos");
        btnVehiculos.addActionListener(e -> new FrmVehiculos(conexion).setVisible(true));

        JButton btnReservas = new JButton("Módulo de Reservas");
        btnReservas.addActionListener(e -> new FrmReservas(conexion).setVisible(true));

        JButton btnSalir = new JButton("Cerrar sesión");
        btnSalir.addActionListener(e -> {
            conexion.desconectar();
            dispose();
        });

        panelBotones.add(btnClientes);
        panelBotones.add(btnVehiculos);
        panelBotones.add(btnReservas);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.CENTER);
    }
}
