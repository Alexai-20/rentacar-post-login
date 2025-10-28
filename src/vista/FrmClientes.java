package vista;

import conexion.Conexion;
import controlador.ClienteController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;
import modelo.UsuarioAutenticado;

/**
 * Ventana para la administración de clientes. Permite ejecutar operaciones
 * CRUD de forma visual interactuando con {@link ClienteController}.
 */
public class FrmClientes extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private final ClienteController controller;
    private final UsuarioAutenticado usuario;
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
    private final JComboBox<String> cboTipoCliente = new JComboBox<>(new String[]{"Particular", "Empresa"});
    private final JTextField txtEmpresa = new JTextField();

    private final JTable tblClientes = new JTable();

    public FrmClientes(Conexion conexion, UsuarioAutenticado usuario) {
        this.usuario = Objects.requireNonNull(usuario, "Debe existir un usuario autenticado");
        if (!this.usuario.esAdministrador()) {
            throw new IllegalStateException("El usuario actual no posee permisos para gestionar clientes");
        }
        this.controller = new ClienteController(Objects.requireNonNull(conexion, "La conexión no puede ser nula"));
        this.modeloTabla = new DefaultTableModel(
                new Object[]{"ID Cliente", "ID Usuario", "Nombre", "Apellido", "RUT", "Email", "Teléfono", "Número licencia", "Vencimiento licencia", "Tipo Cliente", "Empresa", "Estado"}, 0
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
        setTitle("Gestión de Clientes - " + usuario.getNombre());
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 10, 8));
        txtIdCliente.setEditable(false);
        txtIdUsuario.setEditable(false);
        txtFechaRegistro.setEditable(false);
        txtFechaUltimaModificacion.setEditable(false);

        panelFormulario.add(new JLabel("ID Cliente:"));
        panelFormulario.add(txtIdCliente);

        panelFormulario.add(new JLabel("ID Usuario:"));
        panelFormulario.add(txtIdUsuario);

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Apellido:"));
        panelFormulario.add(txtApellido);

        panelFormulario.add(new JLabel("RUT:"));
        panelFormulario.add(txtRut);

        panelFormulario.add(new JLabel("Email:"));
        panelFormulario.add(txtEmail);

        panelFormulario.add(new JLabel("Teléfono:"));
        panelFormulario.add(txtTelefono);

        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(txtDireccion);

        panelFormulario.add(new JLabel("Fecha nacimiento (yyyy-MM-dd):"));
        panelFormulario.add(txtFechaNacimiento);

        panelFormulario.add(new JLabel("Tipo usuario:"));
        panelFormulario.add(txtTipoUsuario);

        panelFormulario.add(new JLabel("Estado:"));
        panelFormulario.add(txtEstado);

        panelFormulario.add(new JLabel("Fecha registro:"));
        panelFormulario.add(txtFechaRegistro);

        panelFormulario.add(new JLabel("Última modificación:"));
        panelFormulario.add(txtFechaUltimaModificacion);

        panelFormulario.add(new JLabel("Número licencia:"));
        panelFormulario.add(txtNumeroLicencia);

        panelFormulario.add(new JLabel("Vencimiento licencia (yyyy-MM-dd):"));
        panelFormulario.add(txtFechaVencimientoLicencia);

        cboTipoCliente.setSelectedItem(null);
        txtEmpresa.setEnabled(false);

        panelFormulario.add(new JLabel("Tipo cliente:"));
        panelFormulario.add(cboTipoCliente);

        panelFormulario.add(new JLabel("Empresa:"));
        panelFormulario.add(txtEmpresa);

        add(panelFormulario, BorderLayout.NORTH);

        tblClientes.setModel(modeloTabla);
        tblClientes.setPreferredScrollableViewportSize(new Dimension(800, 260));
        tblClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarClienteSeleccionado();
            }
        });
        add(new JScrollPane(tblClientes), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        btnGuardar.addActionListener(e -> guardarCliente());
        btnActualizar.addActionListener(e -> actualizarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        add(panelBotones, BorderLayout.SOUTH);

        cboTipoCliente.addActionListener(e -> actualizarEstadoEmpresa());
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
                c.getNumeroLicencia(),
                formatDate(c.getFechaVencimientoLicencia()),
                c.getTipoCliente(),
                c.getEmpresa(),
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
            seleccionarTipoCliente(cliente.getTipoCliente());
            txtEmpresa.setText(cliente.getEmpresa());
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
            limpiarFormulario();
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
        String tipoClienteSeleccionado = obtenerTipoClienteSeleccionado();
        if (tipoClienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione el tipo de cliente", "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        cliente.setTipoCliente(tipoClienteSeleccionado);
        cliente.setEmpresa(txtEmpresa.getText().trim());

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
        cboTipoCliente.setSelectedItem(null);
        txtEmpresa.setText("");
        txtEmpresa.setEnabled(false);
        tblClientes.clearSelection();
    }

    private void seleccionarTipoCliente(String tipoCliente) {
        if (tipoCliente == null || tipoCliente.isBlank()) {
            cboTipoCliente.setSelectedItem(null);
            actualizarEstadoEmpresa();
            return;
        }
        for (int i = 0; i < cboTipoCliente.getItemCount(); i++) {
            String item = cboTipoCliente.getItemAt(i);
            if (item.equalsIgnoreCase(tipoCliente)) {
                cboTipoCliente.setSelectedIndex(i);
                actualizarEstadoEmpresa();
                return;
            }
        }
        cboTipoCliente.setSelectedItem(null);
        actualizarEstadoEmpresa();
    }

    private String obtenerTipoClienteSeleccionado() {
        Object seleccionado = cboTipoCliente.getSelectedItem();
        return seleccionado != null ? seleccionado.toString() : null;
    }

    private void actualizarEstadoEmpresa() {
        Object seleccionado = cboTipoCliente.getSelectedItem();
        boolean esEmpresa = seleccionado != null && "Empresa".equalsIgnoreCase(seleccionado.toString());
        txtEmpresa.setEnabled(esEmpresa);
        if (!esEmpresa) {
            txtEmpresa.setText("");
        }
    }
}
