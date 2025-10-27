package vista;

import conexion.Conexion;
import controlador.VehiculoController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import modelo.Vehiculo;

/**
 * Ventana para la gestión de vehículos disponibles en la flota.
 */
public class FrmVehiculos extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private final VehiculoController controller;
    private final DefaultTableModel modeloTabla;
    private final List<Vehiculo> vehiculosActuales = new ArrayList<>();

    private final JTextField txtPatente = new JTextField();
    private final JTextField txtIdModelo = new JTextField();
    private final JTextField txtAnio = new JTextField();
    private final JTextField txtTipoCombustible = new JTextField();
    private final JTextField txtKilometraje = new JTextField();
    private final JTextField txtColor = new JTextField();
    private final JTextField txtNumeroAsientos = new JTextField();
    private final JTextField txtTipoVehiculo = new JTextField();
    private final JTextField txtTarifaDiaria = new JTextField();
    private final JTextField txtEstadoMantenimiento = new JTextField();
    private final JCheckBox chkDisponibilidad = new JCheckBox("Disponible");
    private final JTextField txtFechaRegistro = new JTextField();
    private final JTextField txtFechaUltimaRevision = new JTextField();

    private final JTable tblVehiculos = new JTable();

    public FrmVehiculos(Conexion conexion) {
        this.controller = new VehiculoController(conexion);
        this.modeloTabla = new DefaultTableModel(
                new Object[]{"Patente", "Modelo", "Tipo", "Disponibilidad", "Tarifa diaria", "Estado mant."}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        initComponents();
        cargarVehiculos();
    }

    private void initComponents() {
        setTitle("Gestión de Vehículos");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 10, 8));
        panelFormulario.add(new JLabel("Patente:"));
        panelFormulario.add(txtPatente);

        panelFormulario.add(new JLabel("ID Modelo:"));
        panelFormulario.add(txtIdModelo);

        panelFormulario.add(new JLabel("Año (yyyy-MM-dd):"));
        panelFormulario.add(txtAnio);

        panelFormulario.add(new JLabel("Tipo combustible:"));
        panelFormulario.add(txtTipoCombustible);

        panelFormulario.add(new JLabel("Kilometraje:"));
        panelFormulario.add(txtKilometraje);

        panelFormulario.add(new JLabel("Color:"));
        panelFormulario.add(txtColor);

        panelFormulario.add(new JLabel("Número de asientos:"));
        panelFormulario.add(txtNumeroAsientos);

        panelFormulario.add(new JLabel("Tipo de vehículo:"));
        panelFormulario.add(txtTipoVehiculo);

        panelFormulario.add(new JLabel("Tarifa diaria:"));
        panelFormulario.add(txtTarifaDiaria);

        panelFormulario.add(new JLabel("Estado mantenimiento:"));
        panelFormulario.add(txtEstadoMantenimiento);

        panelFormulario.add(new JLabel("Disponibilidad:"));
        panelFormulario.add(chkDisponibilidad);

        panelFormulario.add(new JLabel("Fecha registro (yyyy-MM-dd):"));
        panelFormulario.add(txtFechaRegistro);

        panelFormulario.add(new JLabel("Última revisión (yyyy-MM-dd):"));
        panelFormulario.add(txtFechaUltimaRevision);

        add(panelFormulario, BorderLayout.NORTH);

        tblVehiculos.setModel(modeloTabla);
        tblVehiculos.setPreferredScrollableViewportSize(new Dimension(800, 260));
        tblVehiculos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarVehiculoSeleccionado();
            }
        });
        add(new JScrollPane(tblVehiculos), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        btnGuardar.addActionListener(e -> guardarVehiculo());
        btnActualizar.addActionListener(e -> actualizarVehiculo());
        btnEliminar.addActionListener(e -> eliminarVehiculo());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarVehiculos() {
        vehiculosActuales.clear();
        vehiculosActuales.addAll(controller.obtenerVehiculos());
        modeloTabla.setRowCount(0);
        for (Vehiculo v : vehiculosActuales) {
            modeloTabla.addRow(new Object[]{
                v.getPatente(),
                v.getIdModelo(),
                v.getTipoVehiculo(),
                v.isDisponibilidad() ? "Disponible" : "No disponible",
                v.getTarifaDiaria(),
                v.getEstadoMantenimiento()
            });
        }
    }

    private void cargarVehiculoSeleccionado() {
        int fila = tblVehiculos.getSelectedRow();
        if (fila >= 0 && fila < vehiculosActuales.size()) {
            Vehiculo vehiculo = vehiculosActuales.get(fila);
            txtPatente.setText(vehiculo.getPatente());
            txtIdModelo.setText(String.valueOf(vehiculo.getIdModelo()));
            txtAnio.setText(formatDate(vehiculo.getAnio()));
            txtTipoCombustible.setText(vehiculo.getTipoCombustible());
            txtKilometraje.setText(String.valueOf(vehiculo.getKilometraje()));
            txtColor.setText(vehiculo.getColor());
            txtNumeroAsientos.setText(String.valueOf(vehiculo.getNumeroAsientos()));
            txtTipoVehiculo.setText(vehiculo.getTipoVehiculo());
            txtTarifaDiaria.setText(vehiculo.getTarifaDiaria() != null ? vehiculo.getTarifaDiaria().toPlainString() : "");
            txtEstadoMantenimiento.setText(vehiculo.getEstadoMantenimiento());
            chkDisponibilidad.setSelected(vehiculo.isDisponibilidad());
            txtFechaRegistro.setText(formatDate(vehiculo.getFechaRegistro()));
            txtFechaUltimaRevision.setText(formatDate(vehiculo.getFechaUltimaRevision()));
        }
    }

    private void guardarVehiculo() {
        Vehiculo vehiculo = leerVehiculoDesdeFormulario();
        if (vehiculo == null) {
            return;
        }

        LocalDate hoy = LocalDate.now();
        if (vehiculo.getFechaRegistro() == null) {
            vehiculo.setFechaRegistro(hoy);
        }
        if (vehiculo.getFechaUltimaRevision() == null) {
            vehiculo.setFechaUltimaRevision(hoy);
        }

        if (controller.crearVehiculo(vehiculo)) {
            JOptionPane.showMessageDialog(this, "Vehículo registrado correctamente");
            cargarVehiculos();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible registrar el vehículo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarVehiculo() {
        int fila = tblVehiculos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un vehículo de la tabla", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Vehiculo vehiculo = leerVehiculoDesdeFormulario();
        if (vehiculo == null) {
            return;
        }

        if (controller.actualizarVehiculo(vehiculo)) {
            JOptionPane.showMessageDialog(this, "Vehículo actualizado correctamente");
            cargarVehiculos();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible actualizar el vehículo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarVehiculo() {
        int fila = tblVehiculos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un vehículo de la tabla", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el vehículo seleccionado?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            String patente = txtPatente.getText().trim();
            if (controller.eliminarVehiculo(patente)) {
                JOptionPane.showMessageDialog(this, "Vehículo eliminado");
                cargarVehiculos();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible eliminar el vehículo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Vehiculo leerVehiculoDesdeFormulario() {
        String patente = txtPatente.getText().trim();
        if (patente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La patente es obligatoria", "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPatente(patente);

        try {
            vehiculo.setIdModelo(Integer.parseInt(txtIdModelo.getText().trim()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID de modelo debe ser numérico", "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            vehiculo.setAnio(parseFecha(txtAnio.getText().trim(), "año"));
            vehiculo.setTipoCombustible(txtTipoCombustible.getText().trim());
            vehiculo.setKilometraje(parseEntero(txtKilometraje.getText().trim(), "kilometraje"));
            vehiculo.setColor(txtColor.getText().trim());
            vehiculo.setNumeroAsientos(parseEntero(txtNumeroAsientos.getText().trim(), "número de asientos"));
            vehiculo.setTipoVehiculo(txtTipoVehiculo.getText().trim());
            vehiculo.setTarifaDiaria(parseBigDecimal(txtTarifaDiaria.getText().trim(), "tarifa diaria"));
            vehiculo.setEstadoMantenimiento(txtEstadoMantenimiento.getText().trim());
            vehiculo.setDisponibilidad(chkDisponibilidad.isSelected());
            vehiculo.setFechaRegistro(parseFecha(txtFechaRegistro.getText().trim(), "fecha de registro"));
            vehiculo.setFechaUltimaRevision(parseFecha(txtFechaUltimaRevision.getText().trim(), "última revisión"));
        } catch (IllegalArgumentException ex) {
            return null;
        }

        return vehiculo;
    }

    private LocalDate parseFecha(String valor, String campo) {
        if (valor == null || valor.isEmpty()) {
            return null;
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

    private int parseEntero(String valor, String campo) {
        if (valor == null || valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo " + campo + " es obligatorio", "Validación", JOptionPane.WARNING_MESSAGE);
            throw new IllegalArgumentException("Entero requerido");
        }
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato numérico inválido en " + campo, "Validación", JOptionPane.WARNING_MESSAGE);
            throw new IllegalArgumentException("Número inválido");
        }
    }

    private String formatDate(LocalDate date) {
        return date != null ? date.toString() : "";
    }

    private void limpiarFormulario() {
        txtPatente.setText("");
        txtIdModelo.setText("");
        txtAnio.setText("");
        txtTipoCombustible.setText("");
        txtKilometraje.setText("");
        txtColor.setText("");
        txtNumeroAsientos.setText("");
        txtTipoVehiculo.setText("");
        txtTarifaDiaria.setText("");
        txtEstadoMantenimiento.setText("");
        chkDisponibilidad.setSelected(false);
        txtFechaRegistro.setText("");
        txtFechaUltimaRevision.setText("");
        tblVehiculos.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Conexion conexion = new Conexion();
            new FrmVehiculos(conexion).setVisible(true);
        });
    }
}
