package vista;

import conexion.Conexion;
import controlador.ClienteController;
import java.awt.BorderLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;
import vista.componentes.RoundedPanel;
import vista.estilos.ModernUIHelper;

/**
 * Ventana para la administración de clientes. Permite ejecutar operaciones
 * CRUD de forma visual interactuando con {@link ClienteController}.
 */
public class FrmClientes extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private final ClienteController controller;
    private final DefaultTableModel modeloTabla;
    private final List<Cliente> clientesActuales = new ArrayList<>();

    private final JTextField txtIdCliente = new JTextField();
    private final JTextField txtIdUsuario = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtApellido = new JTextField();
    private final JTextField txtRut = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JTextField txtFechaNacimiento = new JTextField();
    private final JTextField txtTipoUsuario = new JTextField("CLIENTE");
    private final JTextField txtEstado = new JTextField("ACTIVO");
    private final JTextField txtFechaRegistro = new JTextField();
    private final JTextField txtFechaUltimaModificacion = new JTextField();
    private final JTextField txtNumeroLicencia = new JTextField();
    private final JTextField txtFechaVencimientoLicencia = new JTextField();
    private final JComboBox<String> cmbTipoCliente = new JComboBox<>(new String[]{"PARTICULAR", "EMPRESA"});
    private final JTextField txtEmpresa = new JTextField();

    private final JTable tblClientes = new JTable();

    public FrmClientes(Conexion conexion) {
        this.controller = new ClienteController(conexion);
        this.modeloTabla = new DefaultTableModel(
                new Object[]{"ID Cliente", "ID Usuario", "Nombre", "Apellido", "RUT", "Email", "Teléfono", "Tipo Cliente", "Empresa", "N° Licencia", "Vence", "Estado"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        initComponents();
        cargarClientes();
    }

    private void initComponents() {
        setTitle("Gestión de Clientes");
        setSize(1100, 650);
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
        txtIdCliente.setEditable(false);
        txtIdUsuario.setEditable(false);
        txtFechaRegistro.setEditable(false);
        txtFechaUltimaModificacion.setEditable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.35;

        agregarCampo(panelFormulario, gbc, 0, "ID Cliente:", txtIdCliente);
        agregarCampo(panelFormulario, gbc, 1, "ID Usuario:", txtIdUsuario);
        agregarCampo(panelFormulario, gbc, 2, "Nombre:", txtNombre);
        agregarCampo(panelFormulario, gbc, 3, "Apellido:", txtApellido);
        agregarCampo(panelFormulario, gbc, 4, "RUT:", txtRut);
        agregarCampo(panelFormulario, gbc, 5, "Email:", txtEmail);
        agregarCampo(panelFormulario, gbc, 6, "Teléfono:", txtTelefono);
        agregarCampo(panelFormulario, gbc, 7, "Dirección:", txtDireccion);
        agregarCampo(panelFormulario, gbc, 8, "Fecha nacimiento (yyyy-MM-dd):", txtFechaNacimiento);
        agregarCampo(panelFormulario, gbc, 9, "Tipo usuario:", txtTipoUsuario);
        agregarCampo(panelFormulario, gbc, 10, "Estado:", txtEstado);
        agregarCampo(panelFormulario, gbc, 11, "Fecha registro:", txtFechaRegistro);
        agregarCampo(panelFormulario, gbc, 12, "Última modificación:", txtFechaUltimaModificacion);
        agregarCampo(panelFormulario, gbc, 13, "Número licencia:", txtNumeroLicencia);
        agregarCampo(panelFormulario, gbc, 14, "Vencimiento licencia (yyyy-MM-dd):", txtFechaVencimientoLicencia);
        agregarCampo(panelFormulario, gbc, 15, "Tipo cliente:", cmbTipoCliente);
        txtEmpresa.setEditable(false);
        agregarCampo(panelFormulario, gbc, 16, "Empresa:", txtEmpresa);

        cmbTipoCliente.addItemListener(e -> actualizarEstadoEmpresa());
        actualizarEstadoEmpresa();

        contenedor.add(panelFormulario, BorderLayout.NORTH);

        tblClientes.setModel(modeloTabla);
        tblClientes.setPreferredScrollableViewportSize(new Dimension(800, 260));
        tblClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ModernUIHelper.styleTable(tblClientes);
        tblClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarClienteSeleccionado();
            }
        });
        JScrollPane scroll = new JScrollPane(tblClientes);
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

        btnGuardar.addActionListener(e -> guardarCliente());
        btnActualizar.addActionListener(e -> actualizarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
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
        } else if (componente instanceof JComboBox<?> combo) {
            combo.setPreferredSize(new Dimension(220, 36));
        }
        panel.add(componente, gbc);
        gbc.weightx = 0.35;
    }

    private void cargarClientes() {
        clientesActuales.clear();
        clientesActuales.addAll(controller.obtenerClientes());
        modeloTabla.setRowCount(0);
        for (Cliente c : clientesActuales) {
            modeloTabla.addRow(new Object[]{
                c.getIdCliente(),
                c.getIdUsuario(),
                c.getNombre(),
                c.getApellido(),
                c.getRut(),
                c.getEmail(),
                c.getTelefono(),
                c.getTipoCliente(),
                c.getEmpresa(),
                c.getNumeroLicencia(),
                formatDate(c.getFechaVencimientoLicencia()),
                c.getEstado()
            });
        }
    }

    private void cargarClienteSeleccionado() {
        int fila = tblClientes.getSelectedRow();
        if (fila >= 0 && fila < clientesActuales.size()) {
            Cliente cliente = clientesActuales.get(fila);
            txtIdCliente.setText(String.valueOf(cliente.getIdCliente()));
            txtIdUsuario.setText(String.valueOf(cliente.getIdUsuario()));
            txtNombre.setText(cliente.getNombre());
            txtApellido.setText(cliente.getApellido());
            txtRut.setText(cliente.getRut());
            txtEmail.setText(cliente.getEmail());
            txtTelefono.setText(cliente.getTelefono());
            txtDireccion.setText(cliente.getDireccion());
            txtFechaNacimiento.setText(formatDate(cliente.getFechaNacimiento()));
            txtTipoUsuario.setText(cliente.getTipoUsuario());
            txtEstado.setText(cliente.getEstado());
            txtFechaRegistro.setText(formatDate(cliente.getFechaRegistro()));
            txtFechaUltimaModificacion.setText(formatDate(cliente.getFechaUltimaModificacion()));
            txtNumeroLicencia.setText(cliente.getNumeroLicencia());
            txtFechaVencimientoLicencia.setText(formatDate(cliente.getFechaVencimientoLicencia()));
            cmbTipoCliente.setSelectedItem(cliente.getTipoCliente() != null ? cliente.getTipoCliente().toUpperCase() : "PARTICULAR");
            txtEmpresa.setText(cliente.getEmpresa());
            actualizarEstadoEmpresa();
        }
    }

    private void guardarCliente() {
        Cliente cliente = leerClienteDesdeFormulario(true);
        if (cliente == null) {
            return;
        }

        if (controller.crearCliente(cliente)) {
            JOptionPane.showMessageDialog(this, "Cliente registrado correctamente");
            cargarClientes();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible registrar al cliente", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCliente() {
        int fila = tblClientes.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cliente = leerClienteDesdeFormulario(false);
        if (cliente == null) {
            return;
        }

        if (controller.actualizarCliente(cliente)) {
            JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente");
            cargarClientes();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible actualizar al cliente", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        int fila = tblClientes.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el cliente seleccionado?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            Cliente cliente = clientesActuales.get(fila);
            if (controller.eliminarCliente(cliente.getIdCliente(), cliente.getIdUsuario())) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado");
                cargarClientes();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible eliminar al cliente", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Cliente leerClienteDesdeFormulario(boolean esNuevo) {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String rut = txtRut.getText().trim();
        if (nombre.isEmpty() || apellido.isEmpty() || rut.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre, apellido y RUT son obligatorios", "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Cliente cliente = new Cliente();
        if (!esNuevo) {
            try {
                cliente.setIdCliente(Integer.parseInt(txtIdCliente.getText()));
                cliente.setIdUsuario(Integer.parseInt(txtIdUsuario.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Los identificadores no son válidos", "Validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        }

        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setRut(rut);
        cliente.setEmail(txtEmail.getText().trim());
        cliente.setTelefono(txtTelefono.getText().trim());
        cliente.setDireccion(txtDireccion.getText().trim());
        try {
            cliente.setFechaNacimiento(parseFecha(txtFechaNacimiento.getText().trim(), "fecha de nacimiento"));
            cliente.setTipoUsuario(txtTipoUsuario.getText().trim());
            cliente.setEstado(txtEstado.getText().trim());
            cliente.setFechaRegistro(parseFecha(txtFechaRegistro.getText().trim(), "fecha de registro"));
            cliente.setFechaUltimaModificacion(parseFecha(txtFechaUltimaModificacion.getText().trim(), "última modificación"));
            cliente.setNumeroLicencia(txtNumeroLicencia.getText().trim());
            cliente.setFechaVencimientoLicencia(parseFecha(txtFechaVencimientoLicencia.getText().trim(), "vencimiento de licencia"));
        } catch (IllegalArgumentException ex) {
            return null;
        }
        String tipoCliente = ((String) cmbTipoCliente.getSelectedItem());
        cliente.setTipoCliente(tipoCliente);
        cliente.setEmpresa("EMPRESA".equalsIgnoreCase(tipoCliente) ? txtEmpresa.getText().trim() : "");

        int filaSeleccionada = tblClientes.getSelectedRow();
        if (!esNuevo && filaSeleccionada >= 0 && filaSeleccionada < clientesActuales.size()) {
            cliente.setContrasenaHash(clientesActuales.get(filaSeleccionada).getContrasenaHash());
        }

        if (esNuevo) {
            LocalDate hoy = LocalDate.now();
            cliente.setFechaRegistro(hoy);
            cliente.setFechaUltimaModificacion(hoy);
        }
        return cliente;
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

    private String formatDate(LocalDate date) {
        return date != null ? date.toString() : "";
    }

    private void limpiarFormulario() {
        txtIdCliente.setText("");
        txtIdUsuario.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtRut.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtFechaNacimiento.setText("");
        txtTipoUsuario.setText("CLIENTE");
        txtEstado.setText("ACTIVO");
        txtFechaRegistro.setText("");
        txtFechaUltimaModificacion.setText("");
        txtNumeroLicencia.setText("");
        txtFechaVencimientoLicencia.setText("");
        cmbTipoCliente.setSelectedIndex(0);
        txtEmpresa.setText("");
        actualizarEstadoEmpresa();
        tblClientes.clearSelection();
    }

    private void actualizarEstadoEmpresa() {
        String tipo = (String) cmbTipoCliente.getSelectedItem();
        boolean esEmpresa = "EMPRESA".equalsIgnoreCase(tipo);
        txtEmpresa.setEditable(esEmpresa);
        if (!esEmpresa) {
            txtEmpresa.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Conexion conexion = new Conexion();
            new FrmClientes(conexion).setVisible(true);
        });
    }
}
