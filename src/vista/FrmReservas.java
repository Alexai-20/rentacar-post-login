package vista;

import conexion.Conexion;
import controlador.ReservaController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import modelo.Reserva;
import vista.componentes.RoundedPanel;
import vista.estilos.ModernUIHelper;

/**
 * Ventana para el mantenimiento de reservas realizadas por los clientes.
 */
public class FrmReservas extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private final ReservaController controller;
    private final DefaultTableModel modeloTabla;
    private final List<Reserva> reservasActuales = new ArrayList<>();

    private final JTextField txtIdReserva = new JTextField();
    private final JTextField txtIdCliente = new JTextField();
    private final JTextField txtPatente = new JTextField();
    private final JTextField txtFechaReserva = new JTextField();
    private final JTextField txtFechaInicio = new JTextField();
    private final JTextField txtFechaFin = new JTextField();
    private final JTextField txtEstadoReserva = new JTextField();
    private final JTextField txtMontoEstimado = new JTextField();
    private final JTextField txtIdTrabajador = new JTextField();

    private final JTable tblReservas = new JTable();

    public FrmReservas(Conexion conexion) {
        this.controller = new ReservaController(conexion);
        this.modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Cliente", "Patente", "Inicio", "Fin", "Estado", "Monto", "Trabajador"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        initComponents();
        cargarReservas();
    }

    private void initComponents() {
        setTitle("Gestión de Reservas");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        ModernUIHelper.registerBackground(getContentPane());

        JPanel contenedor = new JPanel(new BorderLayout(20, 20));
        contenedor.setOpaque(false);
        contenedor.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
        add(contenedor, BorderLayout.CENTER);

        RoundedPanel panelFormulario = new RoundedPanel(24);
        panelFormulario.setLayout(new GridBagLayout());
        ModernUIHelper.applyCardStyle(panelFormulario);
        txtIdReserva.setEditable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.35;

        agregarCampo(panelFormulario, gbc, 0, "ID Reserva:", txtIdReserva);
        agregarCampo(panelFormulario, gbc, 1, "ID Cliente:", txtIdCliente);
        agregarCampo(panelFormulario, gbc, 2, "Patente vehículo:", txtPatente);
        agregarCampo(panelFormulario, gbc, 3, "Fecha reserva (yyyy-MM-dd):", txtFechaReserva);
        agregarCampo(panelFormulario, gbc, 4, "Fecha inicio (yyyy-MM-dd):", txtFechaInicio);
        agregarCampo(panelFormulario, gbc, 5, "Fecha fin (yyyy-MM-dd):", txtFechaFin);
        agregarCampo(panelFormulario, gbc, 6, "Estado reserva:", txtEstadoReserva);
        agregarCampo(panelFormulario, gbc, 7, "Monto estimado:", txtMontoEstimado);
        agregarCampo(panelFormulario, gbc, 8, "ID Trabajador:", txtIdTrabajador);

        contenedor.add(panelFormulario, BorderLayout.NORTH);

        tblReservas.setModel(modeloTabla);
        tblReservas.setPreferredScrollableViewportSize(new Dimension(800, 260));
        tblReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ModernUIHelper.styleTable(tblReservas);
        tblReservas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarReservaSeleccionada();
            }
        });
        JScrollPane scroll = new JScrollPane(tblReservas);
        scroll.setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
        RoundedPanel panelTabla = new RoundedPanel(24);
        panelTabla.setLayout(new BorderLayout());
        ModernUIHelper.applyCardStyle(panelTabla);
        panelTabla.add(scroll, BorderLayout.CENTER);
        contenedor.add(panelTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotones.setOpaque(false);
        JButton btnGuardar = ModernUIHelper.createPrimaryButton("Registrar");
        JButton btnActualizar = ModernUIHelper.createSecondaryButton("Actualizar");
        JButton btnEliminar = ModernUIHelper.createSecondaryButton("Eliminar");
        JButton btnLimpiar = ModernUIHelper.createSecondaryButton("Limpiar");

        btnGuardar.addActionListener(e -> guardarReserva());
        btnActualizar.addActionListener(e -> actualizarReserva());
        btnEliminar.addActionListener(e -> eliminarReserva());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        contenedor.add(panelBotones, BorderLayout.SOUTH);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, java.awt.Component componente) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel label = new JLabel(etiqueta);
        label.setFont(ModernUIHelper.DEFAULT_FONT);
        label.setForeground(ModernUIHelper.TEXT);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        if (componente instanceof JTextField textField) {
            textField.setColumns(18);
            textField.setBorder(new javax.swing.border.EmptyBorder(10, 12, 10, 12));
        }
        panel.add(componente, gbc);
        gbc.weightx = 0.35;
    }

    private void cargarReservas() {
        reservasActuales.clear();
        reservasActuales.addAll(controller.obtenerReservas());
        modeloTabla.setRowCount(0);
        for (Reserva r : reservasActuales) {
            modeloTabla.addRow(new Object[]{
                r.getIdReserva(),
                r.getIdCliente(),
                r.getPatente(),
                formatDate(r.getFechaInicio()),
                formatDate(r.getFechaFin()),
                r.getEstadoReserva(),
                r.getMontoEstimado(),
                r.getIdTrabajador()
            });
        }
    }

    private void cargarReservaSeleccionada() {
        int fila = tblReservas.getSelectedRow();
        if (fila >= 0 && fila < reservasActuales.size()) {
            Reserva reserva = reservasActuales.get(fila);
            txtIdReserva.setText(String.valueOf(reserva.getIdReserva()));
            txtIdCliente.setText(String.valueOf(reserva.getIdCliente()));
            txtPatente.setText(reserva.getPatente());
            txtFechaReserva.setText(formatDate(reserva.getFechaReserva()));
            txtFechaInicio.setText(formatDate(reserva.getFechaInicio()));
            txtFechaFin.setText(formatDate(reserva.getFechaFin()));
            txtEstadoReserva.setText(reserva.getEstadoReserva());
            txtMontoEstimado.setText(reserva.getMontoEstimado() != null ? reserva.getMontoEstimado().toPlainString() : "");
            txtIdTrabajador.setText(String.valueOf(reserva.getIdTrabajador()));
        }
    }

    private void guardarReserva() {
        Reserva reserva = leerReservaDesdeFormulario(false);
        if (reserva == null) {
            return;
        }

        if (controller.crearReserva(reserva)) {
            JOptionPane.showMessageDialog(this, "Reserva registrada correctamente");
            cargarReservas();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible registrar la reserva", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarReserva() {
        int fila = tblReservas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva de la tabla", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Reserva reserva = leerReservaDesdeFormulario(true);
        if (reserva == null) {
            return;
        }

        if (controller.actualizarReserva(reserva)) {
            JOptionPane.showMessageDialog(this, "Reserva actualizada correctamente");
            cargarReservas();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible actualizar la reserva", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarReserva() {
        int fila = tblReservas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva de la tabla", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar la reserva seleccionada?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            int idReserva = Integer.parseInt(txtIdReserva.getText());
            if (controller.eliminarReserva(idReserva)) {
                JOptionPane.showMessageDialog(this, "Reserva eliminada");
                cargarReservas();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible eliminar la reserva", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Reserva leerReservaDesdeFormulario(boolean incluirId) {
        Reserva reserva = new Reserva();

        if (incluirId) {
            try {
                reserva.setIdReserva(Integer.parseInt(txtIdReserva.getText().trim()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID de la reserva no es válido", "Validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        }

        String patente = txtPatente.getText().trim();
        if (patente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La patente es obligatoria", "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String estado = txtEstadoReserva.getText().trim();
        if (estado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El estado de la reserva es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            reserva.setIdCliente(Integer.parseInt(txtIdCliente.getText().trim()));
            reserva.setPatente(patente);
            reserva.setFechaReserva(parseFecha(txtFechaReserva.getText().trim(), "fecha de reserva"));
            reserva.setFechaInicio(parseFecha(txtFechaInicio.getText().trim(), "fecha de inicio"));
            reserva.setFechaFin(parseFecha(txtFechaFin.getText().trim(), "fecha de fin"));
            reserva.setEstadoReserva(estado);
            reserva.setMontoEstimado(parseBigDecimal(txtMontoEstimado.getText().trim(), "monto estimado"));
            reserva.setIdTrabajador(Integer.parseInt(txtIdTrabajador.getText().trim()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Los campos numéricos deben contener valores válidos", "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (IllegalArgumentException ex) {
            return null;
        }

        return reserva;
    }

    private LocalDate parseFecha(String valor, String campo) {
        if (valor == null || valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo " + campo + " es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            throw new IllegalArgumentException("Fecha requerida");
        }
        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido en " + campo + ". Use yyyy-MM-dd.", "Validación", JOptionPane.WARNING_MESSAGE);
            throw new IllegalArgumentException("Fecha inválida");
        }
    }

    private BigDecimal parseBigDecimal(String valor, String campo) {
        if (valor == null || valor.isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(valor);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato numérico inválido en " + campo, "Validación", JOptionPane.WARNING_MESSAGE);
            throw new IllegalArgumentException("Número inválido");
        }
    }

    private String formatDate(LocalDate date) {
        return date != null ? date.toString() : "";
    }

    private void limpiarFormulario() {
        txtIdReserva.setText("");
        txtIdCliente.setText("");
        txtPatente.setText("");
        txtFechaReserva.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        txtEstadoReserva.setText("");
        txtMontoEstimado.setText("");
        txtIdTrabajador.setText("");
        tblReservas.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Conexion conexion = new Conexion();
            new FrmReservas(conexion).setVisible(true);
        });
    }
}
