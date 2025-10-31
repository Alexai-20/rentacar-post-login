package vista;

import conexion.Conexion;
import controlador.VehiculoController;
import java.awt.BorderLayout;
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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import modelo.HistorialVehiculo;
import modelo.Vehiculo;
import vista.componentes.RoundedPanel;
import vista.estilos.ModernUIHelper;

/**
 * Ventana para la gestión de vehículos disponibles en la flota.
 */
public class FrmVehiculos extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private final VehiculoController controller;
    private final DefaultTableModel modeloTabla;
    private final List<Vehiculo> vehiculosActuales = new ArrayList<>();
    private final DefaultTableModel modeloHistorial;
    private final List<HistorialVehiculo> historialActual = new ArrayList<>();

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
    private final JTextField txtPrecioMin = new JTextField();
    private final JTextField txtPrecioMax = new JTextField();

    private final JTable tblVehiculos = new JTable();
    private final JTable tblHistorial = new JTable();

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
        this.modeloHistorial = new DefaultTableModel(
                new Object[]{"Patente", "Facturas", "Tipos mantenimiento", "Último mantenimiento"}, 0
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
        getContentPane().setLayout(new BorderLayout());
        ModernUIHelper.registerBackground(getContentPane());

        JPanel contenedor = new JPanel(new BorderLayout(20, 20));
        contenedor.setOpaque(false);
        contenedor.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
        add(contenedor, BorderLayout.CENTER);

        RoundedPanel panelFormulario = new RoundedPanel(24);
        panelFormulario.setLayout(new GridBagLayout());
        ModernUIHelper.applyCardStyle(panelFormulario);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.35;

        agregarCampo(panelFormulario, gbc, 0, "Patente:", txtPatente);
        agregarCampo(panelFormulario, gbc, 1, "ID Modelo:", txtIdModelo);
        agregarCampo(panelFormulario, gbc, 2, "Año (yyyy-MM-dd):", txtAnio);
        agregarCampo(panelFormulario, gbc, 3, "Tipo combustible:", txtTipoCombustible);
        agregarCampo(panelFormulario, gbc, 4, "Kilometraje:", txtKilometraje);
        agregarCampo(panelFormulario, gbc, 5, "Color:", txtColor);
        agregarCampo(panelFormulario, gbc, 6, "Número de asientos:", txtNumeroAsientos);
        agregarCampo(panelFormulario, gbc, 7, "Tipo de vehículo:", txtTipoVehiculo);
        agregarCampo(panelFormulario, gbc, 8, "Tarifa diaria:", txtTarifaDiaria);
        agregarCampo(panelFormulario, gbc, 9, "Estado mantenimiento:", txtEstadoMantenimiento);
        chkDisponibilidad.setOpaque(false);
        agregarCampo(panelFormulario, gbc, 10, "Disponibilidad:", chkDisponibilidad);
        agregarCampo(panelFormulario, gbc, 11, "Fecha registro (yyyy-MM-dd):", txtFechaRegistro);
        agregarCampo(panelFormulario, gbc, 12, "Última revisión (yyyy-MM-dd):", txtFechaUltimaRevision);

        contenedor.add(panelFormulario, BorderLayout.NORTH);

        tblVehiculos.setModel(modeloTabla);
        tblVehiculos.setPreferredScrollableViewportSize(new Dimension(800, 260));
        tblVehiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ModernUIHelper.styleTable(tblVehiculos);
        tblVehiculos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarVehiculoSeleccionado();
            }
        });

        tblHistorial.setModel(modeloHistorial);
        tblHistorial.setPreferredScrollableViewportSize(new Dimension(800, 200));
        tblHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ModernUIHelper.styleTable(tblHistorial);

        JPanel panelListado = new JPanel(new BorderLayout(10, 10));
        panelListado.setOpaque(false);
        panelListado.add(crearPanelFiltros(), BorderLayout.NORTH);
        JScrollPane scrollVehiculos = new JScrollPane(tblVehiculos);
        scrollVehiculos.setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
        RoundedPanel tablaVehiculos = new RoundedPanel(24);
        tablaVehiculos.setLayout(new BorderLayout());
        ModernUIHelper.applyCardStyle(tablaVehiculos);
        tablaVehiculos.add(scrollVehiculos, BorderLayout.CENTER);
        panelListado.add(tablaVehiculos, BorderLayout.CENTER);

        JTabbedPane pestanias = new JTabbedPane();
        pestanias.setFont(ModernUIHelper.DEFAULT_FONT);
        pestanias.addTab("Vehículos", panelListado);
        JScrollPane scrollHistorial = new JScrollPane(tblHistorial);
        scrollHistorial.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));
        RoundedPanel tablaHistorial = new RoundedPanel(24);
        tablaHistorial.setLayout(new BorderLayout());
        ModernUIHelper.applyCardStyle(tablaHistorial);
        tablaHistorial.add(scrollHistorial, BorderLayout.CENTER);
        pestanias.addTab("Historial", tablaHistorial);

        contenedor.add(pestanias, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotones.setOpaque(false);
        JButton btnGuardar = ModernUIHelper.createPrimaryButton("Registrar");
        JButton btnActualizar = ModernUIHelper.createSecondaryButton("Actualizar");
        JButton btnEliminar = ModernUIHelper.createSecondaryButton("Eliminar");
        JButton btnLimpiar = ModernUIHelper.createSecondaryButton("Limpiar");
        JButton btnFiltrar = ModernUIHelper.createSecondaryButton("Filtrar por precio");
        JButton btnQuitarFiltro = ModernUIHelper.createSecondaryButton("Quitar filtro");

        btnGuardar.addActionListener(e -> guardarVehiculo());
        btnActualizar.addActionListener(e -> actualizarVehiculo());
        btnEliminar.addActionListener(e -> eliminarVehiculo());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnFiltrar.addActionListener(e -> aplicarFiltroPrecio());
        btnQuitarFiltro.addActionListener(e -> {
            txtPrecioMin.setText("");
            txtPrecioMax.setText("");
            cargarVehiculos();
        });

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnFiltrar);
        panelBotones.add(btnQuitarFiltro);

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

    private void cargarVehiculos() {
        vehiculosActuales.clear();
        vehiculosActuales.addAll(controller.obtenerVehiculos());
        actualizarTablaVehiculos();
        cargarHistorialVehiculos();
    }

    private void actualizarTablaVehiculos() {
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

    private void cargarHistorialVehiculos() {
        historialActual.clear();
        historialActual.addAll(controller.obtenerHistorialVehiculos());
        modeloHistorial.setRowCount(0);
        for (HistorialVehiculo h : historialActual) {
            modeloHistorial.addRow(new Object[]{
                h.getPatente(),
                h.getFacturas(),
                h.getTiposMantenimiento(),
                formatDate(h.getUltimoMantenimiento())
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

    private JPanel crearPanelFiltros() {
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        panelFiltros.setOpaque(false);
        JLabel lblMin = new JLabel("Precio mínimo:");
        lblMin.setFont(ModernUIHelper.DEFAULT_FONT);
        lblMin.setForeground(ModernUIHelper.TEXT);
        JLabel lblMax = new JLabel("Precio máximo:");
        lblMax.setFont(ModernUIHelper.DEFAULT_FONT);
        lblMax.setForeground(ModernUIHelper.TEXT);
        txtPrecioMin.setColumns(8);
        txtPrecioMin.setBorder(new javax.swing.border.EmptyBorder(8, 10, 8, 10));
        txtPrecioMax.setColumns(8);
        txtPrecioMax.setBorder(new javax.swing.border.EmptyBorder(8, 10, 8, 10));
        panelFiltros.add(lblMin);
        panelFiltros.add(txtPrecioMin);
        panelFiltros.add(lblMax);
        panelFiltros.add(txtPrecioMax);
        return panelFiltros;
    }

    private void aplicarFiltroPrecio() {
        BigDecimal minimo;
        BigDecimal maximo;
        try {
            minimo = parseFiltroBigDecimal(txtPrecioMin.getText().trim());
            maximo = parseFiltroBigDecimal(txtPrecioMax.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos para el filtro de precio", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        vehiculosActuales.clear();
        vehiculosActuales.addAll(controller.obtenerVehiculosPorTarifa(minimo, maximo));
        actualizarTablaVehiculos();
    }

    private BigDecimal parseFiltroBigDecimal(String valor) {
        if (valor == null || valor.isEmpty()) {
            return null;
        }
        return new BigDecimal(valor);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Conexion conexion = new Conexion();
            new FrmVehiculos(conexion).setVisible(true);
        });
    }
}
