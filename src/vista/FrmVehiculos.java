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
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.Vehiculo;
import modelo.Factura;
import modelo.MantenimientoVehiculoDetalle;
import modelo.UsuarioAutenticado;

/**
 * Ventana para la gestión de vehículos disponibles en la flota.
 */
public class FrmVehiculos extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private final VehiculoController controller;
    private final UsuarioAutenticado usuario;
    private final DefaultTableModel modeloTabla;
    private final DefaultTableModel modeloFacturas = new DefaultTableModel(
            new Object[]{"Factura", "Fecha emisión", "Monto total", "Estado"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final DefaultTableModel modeloMantenimientos = new DefaultTableModel(
            new Object[]{"Tipo", "Fecha", "Costo", "Próximo"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
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
    private final JTextField txtTarifaMin = new JTextField();
    private final JTextField txtTarifaMax = new JTextField();

    private final JTable tblVehiculos = new JTable();
    private final JTable tblFacturas = new JTable(modeloFacturas);
    private final JTable tblMantenimientos = new JTable(modeloMantenimientos);
    private final JTextField txtBuscarPatente = new JTextField();
    private final JLabel lblUltimoMantenimiento = new JLabel("Sin datos de mantenimiento");

    public FrmVehiculos(Conexion conexion, UsuarioAutenticado usuario) {
        this.usuario = Objects.requireNonNull(usuario, "Debe existir un usuario autenticado");
        if (!this.usuario.esAdministrador() && !this.usuario.esTrabajador()) {
            throw new IllegalStateException("El usuario actual no posee permisos para gestionar vehículos");
        }
        this.controller = new VehiculoController(Objects.requireNonNull(conexion, "La conexión no puede ser nula"));
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
        limpiarHistorial();
    }

    private void initComponents() {
        setTitle("Gestión de Vehículos - " + usuario.getNombre());
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

        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        JPanel panelFiltroTarifa = new JPanel();
        panelFiltroTarifa.setBorder(BorderFactory.createTitledBorder("Filtrar por tarifa diaria"));

        txtTarifaMin.setColumns(6);
        txtTarifaMax.setColumns(6);

        panelFiltroTarifa.add(new JLabel("Mínima:"));
        panelFiltroTarifa.add(txtTarifaMin);
        panelFiltroTarifa.add(new JLabel("Máxima:"));
        panelFiltroTarifa.add(txtTarifaMax);

        JButton btnFiltrarTarifa = new JButton("Filtrar");
        btnFiltrarTarifa.addActionListener(e -> aplicarFiltroTarifa());
        panelFiltroTarifa.add(btnFiltrarTarifa);

        JButton btnLimpiarFiltro = new JButton("Limpiar filtro");
        btnLimpiarFiltro.addActionListener(e -> restablecerFiltroTarifa());
        panelFiltroTarifa.add(btnLimpiarFiltro);

        panelTabla.add(panelFiltroTarifa, BorderLayout.NORTH);
        panelTabla.add(new JScrollPane(tblVehiculos), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        JPanel panelHistorial = construirPanelHistorial();
        add(panelHistorial, BorderLayout.EAST);

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

    private JPanel construirPanelHistorial() {
        JPanel panelHistorial = new JPanel(new BorderLayout(10, 10));
        panelHistorial.setBorder(BorderFactory.createTitledBorder("Historial del Vehículo"));
        panelHistorial.setPreferredSize(new Dimension(380, 0));

        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 5));
        panelBusqueda.add(new JLabel("Patente:"), BorderLayout.WEST);
        panelBusqueda.add(txtBuscarPatente, BorderLayout.CENTER);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarHistorialPorPatente());
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        panelHistorial.add(panelBusqueda, BorderLayout.NORTH);

        JPanel panelTablas = new JPanel(new GridLayout(2, 1, 5, 10));

        JScrollPane scrollFacturas = new JScrollPane(tblFacturas);
        scrollFacturas.setBorder(BorderFactory.createTitledBorder("Facturas"));
        panelTablas.add(scrollFacturas);

        JScrollPane scrollMantenimientos = new JScrollPane(tblMantenimientos);
        scrollMantenimientos.setBorder(BorderFactory.createTitledBorder("Mantenimientos"));
        panelTablas.add(scrollMantenimientos);

        panelHistorial.add(panelTablas, BorderLayout.CENTER);

        lblUltimoMantenimiento.setVerticalAlignment(JLabel.TOP);
        lblUltimoMantenimiento.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelHistorial.add(lblUltimoMantenimiento, BorderLayout.SOUTH);

        return panelHistorial;
    }

    private void cargarVehiculos() {
        mostrarVehiculos(controller.obtenerVehiculos());
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
            txtBuscarPatente.setText(vehiculo.getPatente());
            mostrarHistorialVehiculo(vehiculo.getPatente());
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
            limpiarHistorial();
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
            limpiarFormulario();
            limpiarHistorial();
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
                limpiarHistorial();
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

    private void mostrarVehiculos(List<Vehiculo> vehiculos) {
        vehiculosActuales.clear();
        vehiculosActuales.addAll(vehiculos);
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
        tblVehiculos.clearSelection();
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

    private void limpiarHistorial() {
        modeloFacturas.setRowCount(0);
        modeloMantenimientos.setRowCount(0);
        lblUltimoMantenimiento.setText("Sin datos de mantenimiento");
    }

    private void aplicarFiltroTarifa() {
        String minimaTexto = txtTarifaMin.getText().trim();
        String maximaTexto = txtTarifaMax.getText().trim();

        BigDecimal minima = null;
        BigDecimal maxima = null;

        try {
            if (!minimaTexto.isEmpty()) {
                minima = new BigDecimal(minimaTexto);
                if (minima.compareTo(BigDecimal.ZERO) < 0) {
                    throw new NumberFormatException();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido para la tarifa mínima", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (!maximaTexto.isEmpty()) {
                maxima = new BigDecimal(maximaTexto);
                if (maxima.compareTo(BigDecimal.ZERO) < 0) {
                    throw new NumberFormatException();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido para la tarifa máxima", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (minima != null && maxima != null && minima.compareTo(maxima) > 0) {
            JOptionPane.showMessageDialog(this, "La tarifa mínima no puede ser mayor que la máxima", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Vehiculo> filtrados = controller.obtenerVehiculosPorTarifa(minima, maxima);
        mostrarVehiculos(filtrados);

        if (filtrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron vehículos en el rango de tarifas especificado", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void restablecerFiltroTarifa() {
        txtTarifaMin.setText("");
        txtTarifaMax.setText("");
        cargarVehiculos();
    }

    private void buscarHistorialPorPatente() {
        String patente = txtBuscarPatente.getText().trim().toUpperCase();
        if (patente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la patente a buscar", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Vehiculo vehiculo = controller.buscarVehiculoPorPatente(patente);
        if (vehiculo == null) {
            JOptionPane.showMessageDialog(this, "No se encontró un vehículo con la patente especificada", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            limpiarHistorial();
            return;
        }

        txtBuscarPatente.setText(vehiculo.getPatente());
        mostrarHistorialVehiculo(vehiculo.getPatente());
    }

    private void mostrarHistorialVehiculo(String patente) {
        cargarFacturas(patente);
        List<MantenimientoVehiculoDetalle> mantenimientos = controller.obtenerMantenimientosPorPatente(patente);
        cargarMantenimientos(mantenimientos);
        actualizarUltimoMantenimiento(mantenimientos);
    }

    private void cargarFacturas(String patente) {
        modeloFacturas.setRowCount(0);
        List<Factura> facturas = controller.obtenerFacturasPorPatente(patente);
        for (Factura factura : facturas) {
            modeloFacturas.addRow(new Object[]{
                factura.getNumeroFactura(),
                formatDate(factura.getFechaEmision()),
                factura.getMontoTotal(),
                factura.getEstadoPago()
            });
        }
    }

    private void cargarMantenimientos(List<MantenimientoVehiculoDetalle> mantenimientos) {
        modeloMantenimientos.setRowCount(0);
        for (MantenimientoVehiculoDetalle mantenimiento : mantenimientos) {
            String proximo;
            if (mantenimiento.getProximoMantenimientoFecha() != null) {
                proximo = mantenimiento.getProximoMantenimientoFecha().toString();
            } else if (mantenimiento.getProximoMantenimientoKm() > 0) {
                proximo = mantenimiento.getProximoMantenimientoKm() + " km";
            } else {
                proximo = "-";
            }
            modeloMantenimientos.addRow(new Object[]{
                mantenimiento.getNombreTipoMantenimiento(),
                formatDate(mantenimiento.getFechaMantenimiento()),
                mantenimiento.getCosto(),
                proximo
            });
        }
    }

    private void actualizarUltimoMantenimiento(List<MantenimientoVehiculoDetalle> mantenimientos) {
        if (mantenimientos.isEmpty()) {
            lblUltimoMantenimiento.setText("Sin datos de mantenimiento");
            return;
        }

        MantenimientoVehiculoDetalle ultimo = mantenimientos.get(0);
        if (ultimo == null) {
            lblUltimoMantenimiento.setText("Sin datos de mantenimiento");
            return;
        }

        StringBuilder detalle = new StringBuilder("<html><b>Último mantenimiento:</b> ");
        detalle.append(ultimo.getNombreTipoMantenimiento());
        if (ultimo.getFechaMantenimiento() != null) {
            detalle.append(" - ").append(ultimo.getFechaMantenimiento());
        }
        if (ultimo.getCosto() != null) {
            detalle.append(" | Costo: ").append(ultimo.getCosto());
        }
        if (ultimo.getKilometrajeMantenimiento() > 0) {
            detalle.append(" | Kilometraje: ").append(ultimo.getKilometrajeMantenimiento()).append(" km");
        }
        if (ultimo.getDescripcionTipoMantenimiento() != null && !ultimo.getDescripcionTipoMantenimiento().isBlank()) {
            detalle.append("<br><i>").append(ultimo.getDescripcionTipoMantenimiento()).append("</i>");
        }
        detalle.append("</html>");
        lblUltimoMantenimiento.setText(detalle.toString());
    }

}
