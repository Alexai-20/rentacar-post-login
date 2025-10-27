package vista;

import conexion.Conexion;
import controlador.ReservaController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import modelo.Reserva;

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
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 10, 8));
        txtIdReserva.setEditable(false);

        panelFormulario.add(new JLabel("ID Reserva:"));
        panelFormulario.add(txtIdReserva);

        panelFormulario.add(new JLabel("ID Cliente:"));
        panelFormulario.add(txtIdCliente);

        panelFormulario.add(new JLabel("Patente vehículo:"));
        panelFormulario.add(txtPatente);

        panelFormulario.add(new JLabel("Fecha reserva (yyyy-MM-dd):"));
        panelFormulario.add(txtFechaReserva);

        panelFormulario.add(new JLabel("Fecha inicio (yyyy-MM-dd):"));
        panelFormulario.add(txtFechaInicio);

        panelFormulario.add(new JLabel("Fecha fin (yyyy-MM-dd):"));
        panelFormulario.add(txtFechaFin);

        panelFormulario.add(new JLabel("Estado reserva:"));
        panelFormulario.add(txtEstadoReserva);

        panelFormulario.add(new JLabel("Monto estimado:"));
        panelFormulario.add(txtMontoEstimado);

        panelFormulario.add(new JLabel("ID Trabajador:"));
        panelFormulario.add(txtIdTrabajador);

        add(panelFormulario, BorderLayout.NORTH);

        tblReservas.setModel(modeloTabla);
        tblReservas.setPreferredScrollableViewportSize(new Dimension(800, 260));
        tblReservas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarReservaSeleccionada();
            }
        });
        add(new JScrollPane(tblReservas), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        btnGuardar.addActionListener(e -> guardarReserva());
        btnActualizar.addActionListener(e -> actualizarReserva());
        btnEliminar.addActionListener(e -> eliminarReserva());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        add(panelBotones, BorderLayout.SOUTH);
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
