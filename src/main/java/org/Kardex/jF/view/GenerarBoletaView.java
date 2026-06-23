package org.Kardex.jF.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.Kardex.jF.bean.entity.Cliente;
import org.Kardex.jF.bean.entity.Repuesto;
import org.Kardex.jF.model.BoletaModel;
import org.Kardex.jF.model.ClienteModel;
import org.Kardex.jF.model.RepuestoModel;

public class GenerarBoletaView extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final JTextField campoNumero = new JTextField();
    private final JTextField campoFecha = new JTextField();
    private final JComboBox<Cliente> comboCliente = new JComboBox<>();
    private final JTextField campoDniRuc = new JTextField();
    private final JComboBox<String> comboComprobante = new JComboBox<>(new String[]{"Boleta", "Factura"});
    private final JComboBox<String> comboMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Yape/Plin", "Tarjeta", "Transferencia"});

    private final JComboBox<Repuesto> comboRepuesto = new JComboBox<>();
    private final JTextField campoCantidad = new JTextField("1");
    private final JLabel lblSubtotal = new JLabel("S/ 0.00", SwingConstants.RIGHT);
    private final JLabel lblIgv = new JLabel("S/ 0.00", SwingConstants.RIGHT);
    private final JLabel lblTotal = new JLabel("S/ 0.00", SwingConstants.RIGHT);

    private final DefaultTableModel modeloDetalle;
    private final JTable tablaDetalle;
    private final ClienteModel clienteDao = new ClienteModel();
    private final RepuestoModel repuestoDao = new RepuestoModel();
    private final BoletaModel boletaDao = new BoletaModel();

    public GenerarBoletaView() {
        setTitle("Generar Boleta");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        campoNumero.setEditable(false);
        campoFecha.setEditable(false);
        campoDniRuc.setEditable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "GENERAR BOLETA",
                        TitledBorder.CENTER, TitledBorder.TOP),
                new EmptyBorder(10, 15, 10, 15)));

        panel.add(crearPanelDatos(), BorderLayout.NORTH);

        modeloDetalle = new DefaultTableModel(new String[]{"Tipo", "Descripción", "Cantidad", "Precio", "Importe", "ID Ref."}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDetalle = new JTable(modeloDetalle);
        tablaDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDetalle.removeColumn(tablaDetalle.getColumnModel().getColumn(5));
        panel.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);
        panel.add(crearPanelInferior(), BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        cargarClientes();
        cargarRepuestos();
        comboComprobante.addActionListener(e -> campoNumero.setText(boletaDao.generarNumeroComprobante(String.valueOf(comboComprobante.getSelectedItem()))));
        prepararNuevaBoleta();
    }

    private JPanel crearPanelDatos() {
        JPanel contenedor = new JPanel(new BorderLayout(5, 8));

        JPanel panelCliente = new JPanel(new GridLayout(3, 4, 10, 8));
        panelCliente.add(new JLabel("N° Comprobante:"));
        panelCliente.add(campoNumero);
        panelCliente.add(new JLabel("Fecha:"));
        panelCliente.add(campoFecha);
        panelCliente.add(new JLabel("Cliente:"));
        panelCliente.add(comboCliente);
        panelCliente.add(new JLabel("DNI/RUC:"));
        panelCliente.add(campoDniRuc);
        panelCliente.add(new JLabel("Tipo:"));
        panelCliente.add(comboComprobante);
        panelCliente.add(new JLabel("Método Pago:"));
        panelCliente.add(comboMetodoPago);

        JPanel panelDetalle = new JPanel(new GridLayout(2, 3, 10, 8));
        panelDetalle.setBorder(BorderFactory.createTitledBorder("Agregar repuesto comprado"));
        panelDetalle.add(new JLabel("Repuesto:"));
        panelDetalle.add(new JLabel("Cantidad:"));
        panelDetalle.add(new JLabel());
        panelDetalle.add(comboRepuesto);
        panelDetalle.add(campoCantidad);
        JButton btnAgregar = new JButton("Agregar Repuesto");
        btnAgregar.addActionListener(e -> agregarRepuesto());
        panelDetalle.add(btnAgregar);

        contenedor.add(panelCliente, BorderLayout.NORTH);
        contenedor.add(panelDetalle, BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnQuitar = new JButton("Quitar Item");
        JButton btnGuardar = new JButton("Guardar Boleta");
        JButton btnImprimir = new JButton("Imprimir");
        btnNuevo.addActionListener(e -> prepararNuevaBoleta());
        btnQuitar.addActionListener(e -> quitarItem());
        btnGuardar.addActionListener(e -> guardarBoleta());
        btnImprimir.addActionListener(e -> JOptionPane.showMessageDialog(this, "Vista de impresión generada."));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnQuitar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnImprimir);

        JPanel panelTotales = new JPanel(new GridLayout(3, 2, 8, 5));
        panelTotales.add(new JLabel("Subtotal:"));
        panelTotales.add(lblSubtotal);
        panelTotales.add(new JLabel("IGV (18%):"));
        panelTotales.add(lblIgv);
        panelTotales.add(new JLabel("Total:"));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelTotales.add(lblTotal);
        panel.add(panelBotones, BorderLayout.CENTER);
        panel.add(panelTotales, BorderLayout.EAST);
        return panel;
    }

    private void cargarClientes() {
        comboCliente.removeAllItems();
        comboCliente.addItem(new Cliente("", "Seleccione un cliente..."));
        for (Cliente c : clienteDao.listar()) comboCliente.addItem(c);
        comboCliente.addActionListener(e -> cargarDatosCliente());
    }

    private void cargarRepuestos() {
        comboRepuesto.removeAllItems();
        Repuesto placeholder = new Repuesto();
        placeholder.setId("");
        placeholder.setNombre("Seleccione un repuesto...");
        comboRepuesto.addItem(placeholder);
        for (Repuesto r : repuestoDao.listar()) comboRepuesto.addItem(r);
    }

    private void cargarDatosCliente() {
        Cliente cliente = (Cliente) comboCliente.getSelectedItem();
        modeloDetalle.setRowCount(0);
        if (cliente == null || cliente.getId() == null || cliente.getId().isBlank()) {
            campoDniRuc.setText("");
            actualizarTotales();
            return;
        }
        campoDniRuc.setText(cliente.getRUC() != null ? String.valueOf(cliente.getRUC()) : "");
        List<BoletaModel.DetallePendiente> pendientes = boletaDao.listarServiciosPendientes(Integer.parseInt(cliente.getId()));
        for (BoletaModel.DetallePendiente p : pendientes) {
            modeloDetalle.addRow(new Object[]{"SERVICIO", p.descripcion(), 1, formatearSoles(p.precio()), formatearSoles(p.precio()), p.idClienteServicio()});
        }
        actualizarTotales();
    }

    private void agregarRepuesto() {
        Repuesto repuesto = (Repuesto) comboRepuesto.getSelectedItem();
        if (repuesto == null || repuesto.getId() == null || repuesto.getId().isBlank()) {
            JOptionPane.showMessageDialog(this, "Seleccione un repuesto.");
            return;
        }
        try {
            int cantidad = Integer.parseInt(campoCantidad.getText().trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.");
                return;
            }
            int cantidadEnDetalle = cantidadActualEnDetalle(repuesto.getId());
            if (repuesto.getStock() == null || cantidadEnDetalle + cantidad > repuesto.getStock()) {
                JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponible: " + (repuesto.getStock() == null ? 0 : repuesto.getStock()));
                return;
            }
            double precio = repuesto.getPrecioVenta();
            modeloDetalle.addRow(new Object[]{"REPUESTO", repuesto.toString(), cantidad, formatearSoles(precio), formatearSoles(cantidad * precio), repuesto.getId()});
            actualizarTotales();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad debe ser numérica.");
        }
    }

    private int cantidadActualEnDetalle(String idRepuesto) {
        int total = 0;
        for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
            if ("REPUESTO".equals(modeloDetalle.getValueAt(i, 0))
                    && idRepuesto.equals(String.valueOf(modeloDetalle.getValueAt(i, 5)))) {
                total += Integer.parseInt(String.valueOf(modeloDetalle.getValueAt(i, 2)));
            }
        }
        return total;
    }

    private void quitarItem() {
        int fila = tablaDetalle.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un item.");
            return;
        }
        modeloDetalle.removeRow(tablaDetalle.convertRowIndexToModel(fila));
        actualizarTotales();
    }

    private void prepararNuevaBoleta() {
        campoNumero.setText(boletaDao.generarNumeroComprobante(String.valueOf(comboComprobante.getSelectedItem())));
        campoFecha.setText(LocalDateTime.now().format(FORMATO_FECHA));
        modeloDetalle.setRowCount(0);
        if (comboCliente.getItemCount() > 0) comboCliente.setSelectedIndex(0);
        actualizarTotales();
    }

    private void guardarBoleta() {
        Cliente cliente = (Cliente) comboCliente.getSelectedItem();
        if (cliente == null || cliente.getId() == null || cliente.getId().isBlank()) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
            return;
        }
        if (modeloDetalle.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agregue servicios o repuestos a la boleta.");
            return;
        }
        boolean guardado = boletaDao.guardarBoleta(campoNumero.getText(), String.valueOf(comboComprobante.getSelectedItem()),
                Integer.parseInt(cliente.getId()), campoDniRuc.getText(), String.valueOf(comboMetodoPago.getSelectedItem()),
                parseSoles(lblSubtotal.getText()), parseSoles(lblIgv.getText()), parseSoles(lblTotal.getText()), modeloDetalle);
        if (guardado) {
            JOptionPane.showMessageDialog(this, "Boleta registrada correctamente.");
            prepararNuevaBoleta();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar la boleta.");
        }
    }

    private void actualizarTotales() {
        double subtotal = 0;
        for (int i = 0; i < modeloDetalle.getRowCount(); i++) subtotal += parseSoles(modeloDetalle.getValueAt(i, 4).toString());
        double igv = subtotal * 0.18;
        lblSubtotal.setText(formatearSoles(subtotal));
        lblIgv.setText(formatearSoles(igv));
        lblTotal.setText(formatearSoles(subtotal + igv));
    }

    private double parseSoles(String valor) { return Double.parseDouble(valor.replace("S/", "").trim()); }
    private String formatearSoles(double valor) { return String.format("S/ %.2f", valor); }
}
