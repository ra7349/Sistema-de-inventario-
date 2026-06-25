package org.Kardex.jF.view;

import org.Kardex.jF.bean.entity.Cliente;
import org.Kardex.jF.model.HistorialVentasModel;
import org.Kardex.jF.model.HistorialVentasModel.DetalleHistorial;
import org.Kardex.jF.model.HistorialVentasModel.VentaHistorial;
import org.Kardex.jF.model.ClienteModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class HistorialVentasView extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final JComboBox<OpcionCliente> comboClientes = new JComboBox<>();
    private final JTextField txtFecha = new JTextField(10);
    private final JTextField txtComprobante = new JTextField(14);
    private final JLabel lblTotalVentas = new JLabel("S/ 0.00");
    private final JLabel lblCantidadVentas = new JLabel("0 ventas");
    private final DefaultTableModel modelo;
    private final JTable tabla;
    private final HistorialVentasModel historialVentasModel = new HistorialVentasModel();
    private final ClienteModel clienteModel = new ClienteModel();

    public HistorialVentasView() {
        setTitle("Historial de Ventas");
        setSize(1180, 620);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));
        setIconImage(new ImageIcon("image.png").getImage());
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "HISTORIAL DE VENTAS",
                        TitledBorder.CENTER, TitledBorder.TOP),
                new EmptyBorder(10, 15, 10, 15)));

        panel.add(crearPanelFiltros(), BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{
                "ID", "Comprobante", "Tipo", "ID Cliente", "Cliente", "DNI/RUC",
                "Método Pago", "Fecha", "Subtotal", "IGV", "Total"
        }, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(crearPanelAcciones(), BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        cargarClientes();
        cargarVentas();
    }

    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.add(new JLabel("Cliente:"));
        comboClientes.setPreferredSize(new Dimension(300, 28));
        panel.add(comboClientes);
        panel.add(new JLabel("Fecha:"));
        txtFecha.setToolTipText("Ingrese una fecha con formato yyyy-MM-dd");
        panel.add(txtFecha);
        panel.add(new JLabel("Comprobante:"));
        txtComprobante.setToolTipText("Ingrese todo o parte del número de comprobante");
        panel.add(txtComprobante);
        return panel;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton btnBuscar = new JButton("Buscar");
        JButton btnMostrarTodo = new JButton("Mostrar todo");
        JButton btnVerDetalle = new JButton("Ver Detalle");
        JButton btnImprimir = new JButton("Imprimir / Exportar");
        btnBuscar.addActionListener(e -> buscarVentas());
        btnMostrarTodo.addActionListener(e -> mostrarTodo());
        btnVerDetalle.addActionListener(e -> verDetalle());
        btnImprimir.addActionListener(e -> imprimirExportarPosteriormente());
        panelBotones.add(btnBuscar);
        panelBotones.add(btnMostrarTodo);
        panelBotones.add(btnVerDetalle);
        panelBotones.add(btnImprimir);

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        JLabel etiqueta = new JLabel("Total Ventas:");
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalVentas.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelTotal.add(lblCantidadVentas);
        panelTotal.add(etiqueta);
        panelTotal.add(lblTotalVentas);

        panel.add(panelBotones, BorderLayout.CENTER);
        panel.add(panelTotal, BorderLayout.EAST);
        return panel;
    }

    private void cargarClientes() {
        comboClientes.removeAllItems();
        comboClientes.addItem(OpcionCliente.todos());
        for (Cliente cliente : clienteModel.listar()) {
            comboClientes.addItem(new OpcionCliente(Integer.parseInt(cliente.getId()), nombreCompleto(cliente)));
        }
    }

    private void cargarVentas() {
        llenarTabla(historialVentasModel.listarHistorialVentas(null));
    }

    private void buscarVentas() {
        OpcionCliente opcion = (OpcionCliente) comboClientes.getSelectedItem();
        Integer idCliente = opcion == null || opcion.esTodos() ? null : opcion.idCliente();
        LocalDate fecha = obtenerFechaFiltro();
        if (fecha == null && !txtFecha.getText().trim().isEmpty()) {
            return;
        }
        llenarTabla(historialVentasModel.buscarHistorialVentas(idCliente, fecha, txtComprobante.getText()));
    }

    private LocalDate obtenerFechaFiltro() {
        String valor = txtFecha.getText().trim();
        if (valor.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese la fecha con formato yyyy-MM-dd. Ejemplo: 2026-06-25.",
                    "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    private void mostrarTodo() {
        comboClientes.setSelectedIndex(0);
        txtFecha.setText("");
        txtComprobante.setText("");
        cargarVentas();
    }

    private void llenarTabla(List<VentaHistorial> ventas) {
        modelo.setRowCount(0);
        for (VentaHistorial venta : ventas) {
            modelo.addRow(new Object[]{
                    venta.idVenta(),
                    venta.numero(),
                    venta.tipoComprobante(),
                    venta.idCliente(),
                    venta.cliente(),
                    venta.dniRuc(),
                    venta.metodoPago(),
                    venta.fecha().format(FORMATO_FECHA),
                    formatoSoles(venta.subtotal()),
                    formatoSoles(venta.igv()),
                    formatoSoles(venta.total())
            });
        }
        actualizarTotal();
    }

    private void verDetalle() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta.");
            return;
        }
        int idVenta = Integer.parseInt(String.valueOf(modelo.getValueAt(fila, 0)));
        List<DetalleHistorial> detalles = historialVentasModel.listarDetalleHistorial(idVenta);
        JTable tablaDetalle = new JTable(new DefaultTableModel(new String[]{
                "Tipo", "Descripción", "Cantidad", "Precio Unit.", "Importe"
        }, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        DefaultTableModel modeloDetalle = (DefaultTableModel) tablaDetalle.getModel();
        for (DetalleHistorial detalle : detalles) {
            modeloDetalle.addRow(new Object[]{
                    detalle.tipoItem(),
                    detalle.descripcion(),
                    detalle.cantidad(),
                    formatoSoles(detalle.precioUnitario()),
                    formatoSoles(detalle.importe())
            });
        }
        JPanel panelDetalle = new JPanel(new BorderLayout(8, 8));
        panelDetalle.add(new JLabel("<html><b>Comprobante:</b> " + modelo.getValueAt(fila, 1)
                + " &nbsp; <b>Cliente:</b> " + modelo.getValueAt(fila, 4)
                + "<br><b>Fecha:</b> " + modelo.getValueAt(fila, 7)
                + " &nbsp; <b>Total:</b> " + modelo.getValueAt(fila, 10) + "</html>"), BorderLayout.NORTH);
        panelDetalle.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);
        panelDetalle.setPreferredSize(new Dimension(720, 320));
        JOptionPane.showMessageDialog(this, panelDetalle, "Detalle de Venta", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarTotal() {
        double total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total += parseSoles(modelo.getValueAt(i, 10).toString());
        }
        lblTotalVentas.setText(formatoSoles(total));
        lblCantidadVentas.setText(modelo.getRowCount() + (modelo.getRowCount() == 1 ? " venta" : " ventas"));
    }

    private void imprimirExportarPosteriormente() {
        JOptionPane.showMessageDialog(this,
                "La impresión/exportación estará disponible posteriormente. Los filtros actuales ya preparan el historial para esa función.",
                "Función planificada", JOptionPane.INFORMATION_MESSAGE);
    }

    private String nombreCompleto(Cliente cliente) {
        String apellido = cliente.getApellido() == null ? "" : " " + cliente.getApellido();
        return cliente.getNombre() + apellido;
    }

    private String formatoSoles(double valor) {
        return String.format("S/ %.2f", valor);
    }

    private double parseSoles(String valor) {
        return Double.parseDouble(valor.replace("S/", "").trim());
    }

    private record OpcionCliente(Integer idCliente, String nombre) {
        static OpcionCliente todos() {
            return new OpcionCliente(null, "Todos los clientes");
        }

        boolean esTodos() {
            return idCliente == null;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}
