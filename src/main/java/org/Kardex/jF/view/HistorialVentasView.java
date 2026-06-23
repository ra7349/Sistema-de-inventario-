package org.Kardex.jF.view;

import org.Kardex.jF.bean.entity.Cliente;
import org.Kardex.jF.model.BoletaModel;
import org.Kardex.jF.model.BoletaModel.VentaHistorial;
import org.Kardex.jF.model.ClienteModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistorialVentasView extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final JComboBox<OpcionCliente> comboClientes = new JComboBox<>();
    private final JLabel lblTotalVentas = new JLabel("S/ 0.00");
    private final DefaultTableModel modelo;
    private final JTable tabla;
    private final BoletaModel boletaModel = new BoletaModel();
    private final ClienteModel clienteModel = new ClienteModel();

    public HistorialVentasView() {
        setTitle("Historial de Ventas");
        setSize(1100, 540);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

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
        comboClientes.setPreferredSize(new Dimension(320, 28));
        panel.add(comboClientes);
        return panel;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton btnBuscar = new JButton("Buscar por cliente");
        JButton btnMostrarTodo = new JButton("Mostrar todo");
        JButton btnVerDetalle = new JButton("Ver Detalle");
        btnBuscar.addActionListener(e -> cargarVentasPorCliente());
        btnMostrarTodo.addActionListener(e -> mostrarTodo());
        btnVerDetalle.addActionListener(e -> verDetalle());
        panelBotones.add(btnBuscar);
        panelBotones.add(btnMostrarTodo);
        panelBotones.add(btnVerDetalle);

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        JLabel etiqueta = new JLabel("Total Ventas:");
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalVentas.setFont(new Font("Segoe UI", Font.BOLD, 16));
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
        llenarTabla(boletaModel.listarHistorialVentas(null));
    }

    private void cargarVentasPorCliente() {
        OpcionCliente opcion = (OpcionCliente) comboClientes.getSelectedItem();
        if (opcion == null || opcion.esTodos()) {
            cargarVentas();
            return;
        }
        llenarTabla(boletaModel.listarHistorialVentas(opcion.idCliente()));
    }

    private void mostrarTodo() {
        comboClientes.setSelectedIndex(0);
        cargarVentas();
    }

    private void llenarTabla(List<VentaHistorial> ventas) {
        modelo.setRowCount(0);
        for (VentaHistorial venta : ventas) {
            modelo.addRow(new Object[]{
                    venta.idBoleta(),
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
        JOptionPane.showMessageDialog(this,
                "Comprobante: " + modelo.getValueAt(fila, 1)
                        + "\nCliente: " + modelo.getValueAt(fila, 4)
                        + "\nFecha: " + modelo.getValueAt(fila, 7)
                        + "\nSubtotal: " + modelo.getValueAt(fila, 8)
                        + "\nIGV: " + modelo.getValueAt(fila, 9)
                        + "\nTotal: " + modelo.getValueAt(fila, 10),
                "Detalle de Venta", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarTotal() {
        double total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total += parseSoles(modelo.getValueAt(i, 10).toString());
        }
        lblTotalVentas.setText(formatoSoles(total));
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
