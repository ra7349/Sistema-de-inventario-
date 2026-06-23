package org.Kardex.jF.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GenerarBoletaView extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JTextField campoNumero = new JTextField("B001-000001");
    private final JTextField campoFecha = new JTextField("23/06/2026");
    private final JTextField campoCliente = new JTextField("Cliente general");
    private final JTextField campoDniRuc = new JTextField("00000000");
    private final JComboBox<String> comboComprobante = new JComboBox<>(new String[]{"Boleta", "Factura"});
    private final JComboBox<String> comboMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Yape/Plin", "Tarjeta", "Transferencia"});

    private final JTextField campoDescripcion = new JTextField("Servicio técnico");
    private final JTextField campoCantidad = new JTextField("1");
    private final JTextField campoPrecio = new JTextField("50.00");
    private final JLabel lblSubtotal = new JLabel("S/ 0.00", SwingConstants.RIGHT);
    private final JLabel lblIgv = new JLabel("S/ 0.00", SwingConstants.RIGHT);
    private final JLabel lblTotal = new JLabel("S/ 0.00", SwingConstants.RIGHT);

    private final DefaultTableModel modeloDetalle;
    private final JTable tablaDetalle;

    public GenerarBoletaView() {
        setTitle("Generar Boleta");
        setSize(780, 560);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "GENERAR BOLETA",
                        TitledBorder.CENTER, TitledBorder.TOP),
                new EmptyBorder(10, 15, 10, 15)));

        panel.add(crearPanelDatos(), BorderLayout.NORTH);

        modeloDetalle = new DefaultTableModel(new String[]{"Descripción", "Cantidad", "Precio", "Importe"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDetalle = new JTable(modeloDetalle);
        tablaDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);
        panel.add(crearPanelInferior(), BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        agregarItemDemo();
    }

    private JPanel crearPanelDatos() {
        JPanel contenedor = new JPanel(new BorderLayout(5, 8));

        JPanel panelCliente = new JPanel(new GridLayout(3, 4, 10, 8));
        panelCliente.add(new JLabel("N° Comprobante:"));
        panelCliente.add(campoNumero);
        panelCliente.add(new JLabel("Fecha:"));
        panelCliente.add(campoFecha);
        panelCliente.add(new JLabel("Cliente:"));
        panelCliente.add(campoCliente);
        panelCliente.add(new JLabel("DNI/RUC:"));
        panelCliente.add(campoDniRuc);
        panelCliente.add(new JLabel("Tipo:"));
        panelCliente.add(comboComprobante);
        panelCliente.add(new JLabel("Método Pago:"));
        panelCliente.add(comboMetodoPago);

        JPanel panelDetalle = new JPanel(new GridLayout(2, 4, 10, 8));
        panelDetalle.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        panelDetalle.add(new JLabel("Descripción:"));
        panelDetalle.add(new JLabel("Cantidad:"));
        panelDetalle.add(new JLabel("Precio Unitario:"));
        panelDetalle.add(new JLabel());
        panelDetalle.add(campoDescripcion);
        panelDetalle.add(campoCantidad);
        panelDetalle.add(campoPrecio);
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> agregarDetalle());
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
        btnNuevo.addActionListener(e -> limpiar());
        btnQuitar.addActionListener(e -> quitarItem());
        btnGuardar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Boleta registrada correctamente."));
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

    private void agregarItemDemo() {
        modeloDetalle.addRow(new Object[]{"Diagnóstico de laptop", 1, "S/ 30.00", "S/ 30.00"});
        modeloDetalle.addRow(new Object[]{"Mantenimiento preventivo", 1, "S/ 80.00", "S/ 80.00"});
        actualizarTotales();
    }

    private void agregarDetalle() {
        try {
            String descripcion = campoDescripcion.getText().trim();
            int cantidad = Integer.parseInt(campoCantidad.getText().trim());
            double precio = Double.parseDouble(campoPrecio.getText().trim());
            if (descripcion.isEmpty() || cantidad <= 0 || precio < 0) {
                JOptionPane.showMessageDialog(this, "Ingrese datos válidos para el detalle.");
                return;
            }
            double importe = cantidad * precio;
            modeloDetalle.addRow(new Object[]{descripcion, cantidad, formatearSoles(precio), formatearSoles(importe)});
            actualizarTotales();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser numéricos.");
        }
    }

    private void quitarItem() {
        int fila = tablaDetalle.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un item.");
            return;
        }
        modeloDetalle.removeRow(fila);
        actualizarTotales();
    }

    private void limpiar() {
        campoCliente.setText("");
        campoDniRuc.setText("");
        modeloDetalle.setRowCount(0);
        actualizarTotales();
        campoCliente.requestFocus();
    }

    private void actualizarTotales() {
        double subtotal = 0;
        for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
            subtotal += parseSoles(modeloDetalle.getValueAt(i, 3).toString());
        }
        double igv = subtotal * 0.18;
        double total = subtotal + igv;
        lblSubtotal.setText(formatearSoles(subtotal));
        lblIgv.setText(formatearSoles(igv));
        lblTotal.setText(formatearSoles(total));
    }

    private double parseSoles(String valor) {
        return Double.parseDouble(valor.replace("S/", "").trim());
    }

    private String formatearSoles(double valor) {
        return String.format("S/ %.2f", valor);
    }
}
