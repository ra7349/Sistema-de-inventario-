package org.Kardex.jF.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistorialVentasView extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JTextField campoBuscar = new JTextField();
    private final JTextField campoDesde = new JTextField("01/06/2026");
    private final JTextField campoHasta = new JTextField("23/06/2026");
    private final JComboBox<String> comboEstado = new JComboBox<>(new String[]{"Todos", "Pagado", "Pendiente", "Anulado"});
    private final JLabel lblTotalVentas = new JLabel("S/ 0.00");
    private final DefaultTableModel modelo;
    private final JTable tabla;

    public HistorialVentasView() {
        setTitle("Historial de Ventas");
        setSize(900, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "HISTORIAL DE VENTAS",
                        TitledBorder.CENTER, TitledBorder.TOP),
                new EmptyBorder(10, 15, 10, 15)));

        panel.add(crearPanelFiltros(), BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{"Comprobante", "Fecha", "Cliente", "Método Pago", "Estado", "Total"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(crearPanelAcciones(), BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        cargarDatosDemo();
    }

    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 8));
        panel.add(new JLabel("Buscar Cliente/N°:"));
        panel.add(campoBuscar);
        panel.add(new JLabel("Estado:"));
        panel.add(comboEstado);
        panel.add(new JLabel("Desde:"));
        panel.add(campoDesde);
        panel.add(new JLabel("Hasta:"));
        panel.add(campoHasta);
        return panel;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton btnBuscar = new JButton("Buscar");
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnVerDetalle = new JButton("Ver Detalle");
        JButton btnAnular = new JButton("Anular Venta");
        btnBuscar.addActionListener(e -> filtrar());
        btnRefrescar.addActionListener(e -> cargarDatosDemo());
        btnVerDetalle.addActionListener(e -> verDetalle());
        btnAnular.addActionListener(e -> anularVenta());
        panelBotones.add(btnBuscar);
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnVerDetalle);
        panelBotones.add(btnAnular);

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

    private void cargarDatosDemo() {
        modelo.setRowCount(0);
        modelo.addRow(new Object[]{"B001-000001", "21/06/2026", "Juan Pérez", "Efectivo", "Pagado", "S/ 129.80"});
        modelo.addRow(new Object[]{"B001-000002", "22/06/2026", "María López", "Yape/Plin", "Pagado", "S/ 94.40"});
        modelo.addRow(new Object[]{"F001-000001", "23/06/2026", "Empresa ABC", "Transferencia", "Pendiente", "S/ 472.00"});
        actualizarTotal();
    }

    private void filtrar() {
        JOptionPane.showMessageDialog(this, "Filtros aplicados correctamente.");
        actualizarTotal();
    }

    private void verDetalle() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta.");
            return;
        }
        JOptionPane.showMessageDialog(this,
                "Comprobante: " + modelo.getValueAt(fila, 0) + "\nCliente: " + modelo.getValueAt(fila, 2)
                        + "\nTotal: " + modelo.getValueAt(fila, 5),
                "Detalle de Venta", JOptionPane.INFORMATION_MESSAGE);
    }

    private void anularVenta() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "¿Anular venta seleccionada?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            modelo.setValueAt("Anulado", fila, 4);
            actualizarTotal();
        }
    }

    private void actualizarTotal() {
        double total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (!"Anulado".equals(modelo.getValueAt(i, 4))) {
                total += parseSoles(modelo.getValueAt(i, 5).toString());
            }
        }
        lblTotalVentas.setText(String.format("S/ %.2f", total));
    }

    private double parseSoles(String valor) {
        return Double.parseDouble(valor.replace("S/", "").trim());
    }
}
