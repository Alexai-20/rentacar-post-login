package vista;

import conexion.Conexion;
import controlador.PagoController;
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
import javax.swing.JComboBox;
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
import modelo.Pago;
import vista.componentes.RoundedPanel;
import vista.estilos.ModernUIHelper;

/**
 * Ventana para la gestión de pagos asociados a facturas.
 */
public class FrmPagos extends JFrame {

    private static final long serialVersionUID = 1L;

    private final PagoController controller;
    private final DefaultTableModel modeloTabla;
    private final List<Pago> pagos = new ArrayList<>();

    private final JTextField txtIdPago = new JTextField();
    private final JTextField txtIdFactura = new JTextField();
    private final JTextField txtMonto = new JTextField();
    private final JComboBox<String> cboMetodo = new JComboBox<>(new String[]{
        "Efectivo", "Transferencia", "Tarjeta crédito", "Tarjeta débito", "Cheque", "Otro"
    });
    private final JTextField txtFecha = new JTextField();
    private final JTextField txtNumeroTransaccion = new JTextField();
    private final JTextField txtIdTrabajador = new JTextField();

    private final JTable tblPagos = new JTable();

    public FrmPagos(Conexion conexion) {
        this.controller = new PagoController(conexion);
        this.modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Factura", "Monto", "Método", "Fecha", "Transacción", "Trabajador"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        initComponents();
        cargarPagos();
    }

    private void initComponents() {
        setTitle("Gestión de Pagos");
        setSize(980, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        ModernUIHelper.registerBackground(getContentPane());

        JPanel contenido = new JPanel(new BorderLayout(20, 20));
        contenido.setOpaque(false);
        contenido.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(contenido, BorderLayout.CENTER);

        RoundedPanel panelFormulario = new RoundedPanel(24);
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setPreferredSize(new Dimension(900, 220));
        ModernUIHelper.applyCardStyle(panelFormulario);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        txtIdPago.setEditable(false);
        txtIdPago.setBackground(ModernUIHelper.SURFACE);

        int fila = 0;
        agregarCampo(panelFormulario, gbc, fila++, "ID Pago", txtIdPago);
        agregarCampo(panelFormulario, gbc, fila++, "ID Factura", txtIdFactura);
        agregarCampo(panelFormulario, gbc, fila++, "Monto Pago", txtMonto);
        agregarCampo(panelFormulario, gbc, fila++, "Método de pago", cboMetodo);
        agregarCampo(panelFormulario, gbc, fila++, "Fecha pago (yyyy-MM-dd)", txtFecha);
        agregarCampo(panelFormulario, gbc, fila++, "Número de transacción", txtNumeroTransaccion);
        agregarCampo(panelFormulario, gbc, fila++, "ID Trabajador", txtIdTrabajador);

        contenido.add(panelFormulario, BorderLayout.NORTH);

        tblPagos.setModel(modeloTabla);
        tblPagos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ModernUIHelper.styleTable(tblPagos);
        tblPagos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarPagoSeleccionado();
            }
        });

        JScrollPane scroll = new JScrollPane(tblPagos);
        scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        ModernUIHelper.applyCardStyle(scroll);
        contenido.add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotones.setOpaque(false);

        var btnRegistrar = ModernUIHelper.createPrimaryButton("Registrar");
        var btnActualizar = ModernUIHelper.createSecondaryButton("Actualizar");
        var btnEliminar = ModernUIHelper.createSecondaryButton("Eliminar");
        var btnLimpiar = ModernUIHelper.createSecondaryButton("Limpiar");

        btnRegistrar.addActionListener(e -> registrarPago());
        btnActualizar.addActionListener(e -> actualizarPago());
        btnEliminar.addActionListener(e -> eliminarPago());
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
        gbc.weightx = 0.3;
        JLabel label = new JLabel(etiqueta);
        label.setFont(ModernUIHelper.DEFAULT_FONT.deriveFont(15f));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        if (componente instanceof JTextField textField) {
            textField.setColumns(20);
            textField.setBorder(new EmptyBorder(10, 12, 10, 12));
        } else if (componente instanceof JComboBox) {
            componente.setPreferredSize(new Dimension(220, 36));
        }
        panel.add(componente, gbc);
    }

    private void cargarPagos() {
        pagos.clear();
        pagos.addAll(controller.obtenerPagos());
        modeloTabla.setRowCount(0);
        for (Pago pago : pagos) {
            modeloTabla.addRow(new Object[]{
                pago.getIdPago(),
                pago.getIdFactura(),
                pago.getMontoPago(),
                pago.getMetodoPago(),
                pago.getFechaPago(),
                pago.getNumeroTransaccion(),
                pago.getIdTrabajador() > 0 ? pago.getIdTrabajador() : "-"
            });
        }
    }

    private void cargarPagoSeleccionado() {
        int fila = tblPagos.getSelectedRow();
        if (fila >= 0 && fila < pagos.size()) {
            Pago pago = pagos.get(fila);
            txtIdPago.setText(String.valueOf(pago.getIdPago()));
            txtIdFactura.setText(String.valueOf(pago.getIdFactura()));
            txtMonto.setText(pago.getMontoPago() != null ? pago.getMontoPago().toPlainString() : "");
            cboMetodo.setSelectedItem(pago.getMetodoPago());
            txtFecha.setText(pago.getFechaPago() != null ? pago.getFechaPago().toString() : "");
            txtNumeroTransaccion.setText(pago.getNumeroTransaccion());
            txtIdTrabajador.setText(pago.getIdTrabajador() > 0 ? String.valueOf(pago.getIdTrabajador()) : "");
        }
    }

    private void registrarPago() {
        Pago pago = leerFormulario(false);
        if (pago == null) {
            return;
        }

        if (controller.crearPago(pago)) {
            JOptionPane.showMessageDialog(this, "Pago registrado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarPagos();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible registrar el pago", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarPago() {
        if (txtIdPago.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Seleccione un pago de la tabla", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pago pago = leerFormulario(true);
        if (pago == null) {
            return;
        }

        if (controller.actualizarPago(pago)) {
            JOptionPane.showMessageDialog(this, "Pago actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarPagos();
        } else {
            JOptionPane.showMessageDialog(this, "No fue posible actualizar el pago", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPago() {
        int fila = tblPagos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un pago de la tabla", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pago pago = pagos.get(fila);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar el pago seleccionado?", "Confirmación",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.eliminarPago(pago.getIdPago())) {
                JOptionPane.showMessageDialog(this, "Pago eliminado", "Información", JOptionPane.INFORMATION_MESSAGE);
                cargarPagos();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible eliminar el pago", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Pago leerFormulario(boolean incluirId) {
        try {
            int idFactura = Integer.parseInt(txtIdFactura.getText().trim());
            if (txtMonto.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Ingrese un monto válido", "Validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            BigDecimal monto = new BigDecimal(txtMonto.getText().trim());
            String metodo = (String) cboMetodo.getSelectedItem();
            LocalDate fecha = txtFecha.getText().isBlank() ? LocalDate.now() : LocalDate.parse(txtFecha.getText().trim());
            String numeroTransaccion = txtNumeroTransaccion.getText().trim();
            int idTrabajador = txtIdTrabajador.getText().isBlank() ? 0 : Integer.parseInt(txtIdTrabajador.getText().trim());

            Pago pago = new Pago();
            if (incluirId) {
                pago.setIdPago(Integer.parseInt(txtIdPago.getText().trim()));
            }
            pago.setIdFactura(idFactura);
            pago.setMontoPago(monto);
            pago.setMetodoPago(metodo);
            pago.setFechaPago(fecha);
            pago.setNumeroTransaccion(numeroTransaccion.isEmpty() ? null : numeroTransaccion);
            pago.setIdTrabajador(idTrabajador);
            return pago;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique los campos numéricos", "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use yyyy-MM-dd", "Validación", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }

    private void limpiarFormulario() {
        txtIdPago.setText("");
        txtIdFactura.setText("");
        txtMonto.setText("");
        cboMetodo.setSelectedIndex(0);
        txtFecha.setText("");
        txtNumeroTransaccion.setText("");
        txtIdTrabajador.setText("");
        tblPagos.clearSelection();
    }
}
