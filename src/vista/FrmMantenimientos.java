package vista;

import conexion.Conexion;
import controlador.MantenimientoVehiculoController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import modelo.MantenimientoVehiculo;
import vista.componentes.RoundedPanel;
import vista.estilos.ModernUIHelper;

/**
 * Ventana para registrar y visualizar el historial de mantenimiento de los
 * vehículos.
 */
public class FrmMantenimientos extends JFrame {

    private static final long serialVersionUID = 1L;

    private final MantenimientoVehiculoController controller;
    private final DefaultTableModel modeloTabla;
    private final List<MantenimientoVehiculo> mantenimientos = new ArrayList<>();

    private final JTextField txtIdMantenimiento = new JTextField();
    private final JTextField txtPatente = new JTextField();
    private final JTextField txtIdTipoMantenimiento = new JTextField();
    private final JTextField txtCosto = new JTextField();
    private final JTextField txtFecha = new JTextField();
    private final JTextField txtKilometraje = new JTextField();
    private final JTextField txtProximoKm = new JTextField();
    private final JTextField txtProximaFecha = new JTextField();
    private final JTextField txtIdTrabajador = new JTextField();

    private final JTextField txtFiltroPatente = new JTextField();
    private final JTable tblMantenimientos = new JTable();

    public FrmMantenimientos(Conexion conexion) {
        this.controller = new MantenimientoVehiculoController(conexion);
        this.modeloTabla = new DefaultTableModel(new Object[]{
            "ID", "Patente", "Tipo", "Costo", "Fecha", "Km", "Próximo Km", "Próxima fecha", "Trabajador"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        initComponents();
        cargarMantenimientos();
    }

    private void initComponents() {
        setTitle("Historial de Mantenimiento");
        setSize(1020, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        ModernUIHelper.registerBackground(getContentPane());

        JPanel contenido = new JPanel(new BorderLayout(20, 20));
        contenido.setOpaque(false);
        contenido.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(contenido, BorderLayout.CENTER);

        RoundedPanel panelFormulario = new RoundedPanel(24);
        ModernUIHelper.applyCardStyle(panelFormulario);
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setPreferredSize(new Dimension(940, 260));

        txtIdMantenimiento.setEditable(false);
        txtIdMantenimiento.setBackground(ModernUIHelper.SURFACE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int fila = 0;
        agregarCampo(panelFormulario, gbc, fila++, "ID Mantenimiento", txtIdMantenimiento);
        agregarCampo(panelFormulario, gbc, fila++, "Patente", txtPatente);
        agregarCampo(panelFormulario, gbc, fila++, "ID Tipo mantenimiento", txtIdTipoMantenimiento);
        agregarCampo(panelFormulario, gbc, fila++, "Costo", txtCosto);
        agregarCampo(panelFormulario, gbc, fila++, "Fecha (yyyy-MM-dd)", txtFecha);
        agregarCampo(panelFormulario, gbc, fila++, "Kilometraje", txtKilometraje);
        agregarCampo(panelFormulario, gbc, fila++, "Próximo mantenimiento (km)", txtProximoKm);
        agregarCampo(panelFormulario, gbc, fila++, "Próximo mantenimiento (fecha)", txtProximaFecha);
        agregarCampo(panelFormulario, gbc, fila++, "ID Trabajador", txtIdTrabajador);

        contenido.add(panelFormulario, BorderLayout.NORTH);

        RoundedPanel panelTabla = new RoundedPanel(24);
        ModernUIHelper.applyCardStyle(panelTabla);
        panelTabla.setLayout(new BorderLayout(10, 10));

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltro.setOpaque(false);
        JLabel lblFiltro = new JLabel("Filtrar por patente:");
        lblFiltro.setFont(ModernUIHelper.DEFAULT_FONT.deriveFont(Font.BOLD, 14f));
        txtFiltroPatente.setColumns(8);
        JButton btnFiltrar = ModernUIHelper.createSecondaryButton("Aplicar filtro");
        JButton btnLimpiarFiltro = ModernUIHelper.createSecondaryButton("Ver todos");

        btnFiltrar.addActionListener(e -> aplicarFiltro());
        btnLimpiarFiltro.addActionListener(e -> {
            txtFiltroPatente.setText("");
            cargarMantenimientos();
        });

        panelFiltro.add(lblFiltro);
        panelFiltro.add(txtFiltroPatente);
        panelFiltro.add(btnFiltrar);
        panelFiltro.add(btnLimpiarFiltro);

        panelTabla.add(panelFiltro, BorderLayout.NORTH);

        tblMantenimientos.setModel(modeloTabla);
        tblMantenimientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ModernUIHelper.styleTable(tblMantenimientos);
        tblMantenimientos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarMantenimientoSeleccionado();
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tblMantenimientos);
        scrollTabla.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        contenido.add(panelTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotones.setOpaque(false);

        JButton btnRegistrar = ModernUIHelper.createPrimaryButton("Registrar");
        JButton btnActualizar = ModernUIHelper.createSecondaryButton("Actualizar");
        JButton btnEliminar = ModernUIHelper.createSecondaryButton("Eliminar");
        JButton btnLimpiar = ModernUIHelper.createSecondaryButton("Limpiar");

        btnRegistrar.addActionListener(e -> registrarMantenimiento());
        btnActualizar.addActionListener(e -> actualizarMantenimiento());
        btnEliminar.addActionListener(e -> eliminarMantenimiento());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        contenido.add(panelBotones, BorderLayout.SOUTH);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JComponent componente) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.35;
        JLabel label = new JLabel(etiqueta);
        label.setFont(ModernUIHelper.DEFAULT_FONT.deriveFont(15f));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        if (componente instanceof JTextField textField) {
            textField.setColumns(18);
            textField.setBorder(new EmptyBorder(10, 12, 10, 12));
        }
        panel.add(componente, gbc);
    }

    private void cargarMantenimientos() {
        mantenimientos.clear();
        mantenimientos.addAll(controller.obtenerMantenimientos());
        refrescarTabla();
    }

    private void aplicarFiltro() {
        String patente = txtFiltroPatente.getText().trim();
        if (patente.isEmpty()) {
            cargarMantenimientos();
            return;
        }

        mantenimientos.clear();
        mantenimientos.addAll(controller.obtenerMantenimientosPorPatente(patente));
        refrescarTabla();
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        for (MantenimientoVehiculo mantenimiento : mantenimientos) {
            modeloTabla.addRow(new Object[]{
                mantenimiento.getIdMantenimiento(),
                mantenimiento.getPatente(),
                mantenimiento.getIdTipoMantenimiento(),
                mantenimiento.getCosto(),
                mantenimiento.getFechaMantenimiento(),
                mantenimiento.getKilometrajeMantenimiento(),
                mantenimiento.getProximoMantenimientoKm(),
                mantenimiento.getProximoMantenimientoFecha(),
                mantenimiento.getIdTrabajador() > 0 ? mantenimiento.getIdTrabajador() : "-"
            });
        }
    }

    private void cargarMantenimientoSeleccionado() {
        int fila = tblMantenimientos.getSelectedRow();
        if (fila >= 0 && fila < mantenimientos.size()) {
            MantenimientoVehiculo mantenimiento = mantenimientos.get(fila);
            txtIdMantenimiento.setText(String.valueOf(mantenimiento.getIdMantenimiento()));
            txtPatente.setText(mantenimiento.getPatente());
            txtIdTipoMantenimiento.setText(String.valueOf(mantenimiento.getIdTipoMantenimiento()));
            txtCosto.setText(mantenimiento.getCosto() != null ? mantenimiento.getCosto().toPlainString() : "");
            txtFecha.setText(mantenimiento.getFechaMantenimiento() != null ? mantenimiento.getFechaMantenimiento().toString() : "");
            txtKilometraje.setText(String.valueOf(mantenimiento.getKilometrajeMantenimiento()));
            txtProximoKm.setText(String.valueOf(mantenimiento.getProximoMantenimientoKm()));
            txtProximaFecha.setText(mantenimiento.getProximoMantenimientoFecha() != null
                    ? mantenimiento.getProximoMantenimientoFecha().toString() : "");
            txtIdTrabajador.setText(mantenimiento.getIdTrabajador() > 0 ? String.valueOf(mantenimiento.getIdTrabajador()) : "");
        }
    }

    private void registrarMantenimiento() {
        MantenimientoVehiculo mantenimiento = leerFormulario(false);
        if (mantenimiento == null) {
            return;
        }

        if (controller.crearMantenimiento(mantenimiento)) {
            JOptionPane.showMessageDialog(this, "Mantenimiento registrado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarMantenimientos();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible registrar el mantenimiento", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarMantenimiento() {
        if (txtIdMantenimiento.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Seleccione un mantenimiento de la tabla", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MantenimientoVehiculo mantenimiento = leerFormulario(true);
        if (mantenimiento == null) {
            return;
        }

        if (controller.actualizarMantenimiento(mantenimiento)) {
            JOptionPane.showMessageDialog(this, "Mantenimiento actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarMantenimientos();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible actualizar el mantenimiento", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarMantenimiento() {
        int fila = tblMantenimientos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un mantenimiento de la tabla", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MantenimientoVehiculo mantenimiento = mantenimientos.get(fila);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar el mantenimiento seleccionado?", "Confirmación",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.eliminarMantenimiento(mantenimiento.getIdMantenimiento())) {
                JOptionPane.showMessageDialog(this, "Mantenimiento eliminado", "Información", JOptionPane.INFORMATION_MESSAGE);
                cargarMantenimientos();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible eliminar el mantenimiento", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private MantenimientoVehiculo leerFormulario(boolean incluirId) {
        try {
            String patente = txtPatente.getText().trim();
            if (patente.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese la patente", "Validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            int idTipo = Integer.parseInt(txtIdTipoMantenimiento.getText().trim());
            BigDecimal costo = new BigDecimal(txtCosto.getText().trim());
            LocalDate fecha = txtFecha.getText().isBlank() ? LocalDate.now() : LocalDate.parse(txtFecha.getText().trim());
            int kilometraje = Integer.parseInt(txtKilometraje.getText().trim());
            int proximoKm = txtProximoKm.getText().isBlank() ? 0 : Integer.parseInt(txtProximoKm.getText().trim());
            LocalDate proximaFecha = txtProximaFecha.getText().isBlank() ? null : LocalDate.parse(txtProximaFecha.getText().trim());
            int idTrabajador = txtIdTrabajador.getText().isBlank() ? 0 : Integer.parseInt(txtIdTrabajador.getText().trim());

            MantenimientoVehiculo mantenimiento = new MantenimientoVehiculo();
            if (incluirId) {
                mantenimiento.setIdMantenimiento(Integer.parseInt(txtIdMantenimiento.getText().trim()));
            }
            mantenimiento.setPatente(patente.toUpperCase());
            mantenimiento.setIdTipoMantenimiento(idTipo);
            mantenimiento.setCosto(costo);
            mantenimiento.setFechaMantenimiento(fecha);
            mantenimiento.setKilometrajeMantenimiento(kilometraje);
            mantenimiento.setProximoMantenimientoKm(proximoKm);
            mantenimiento.setProximoMantenimientoFecha(proximaFecha);
            mantenimiento.setIdTrabajador(idTrabajador);
            return mantenimiento;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique los campos numéricos", "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use yyyy-MM-dd", "Validación", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }

    private void limpiarFormulario() {
        txtIdMantenimiento.setText("");
        txtPatente.setText("");
        txtIdTipoMantenimiento.setText("");
        txtCosto.setText("");
        txtFecha.setText("");
        txtKilometraje.setText("");
        txtProximoKm.setText("");
        txtProximaFecha.setText("");
        txtIdTrabajador.setText("");
        tblMantenimientos.clearSelection();
    }
}
