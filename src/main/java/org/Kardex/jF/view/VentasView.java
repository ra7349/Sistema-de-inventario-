package org.Kardex.jF.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import org.Kardex.jF.bean.entity.Cliente;
import org.Kardex.jF.bean.entity.DetalleVenta;
import org.Kardex.jF.bean.entity.Producto;
import org.Kardex.jF.bean.entity.Venta;
import org.Kardex.jF.model.ClienteModel;
import org.Kardex.jF.model.ProductoModel;
import org.Kardex.jF.model.VentaModel;

public class VentasView extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final JTextField campoNumero = new JTextField();
    private final JTextField campoFecha = new JTextField();
    private final JComboBox<Cliente> comboCliente = new JComboBox<>();
    private final JTextField campoTipoCliente = new JTextField();
    private final JComboBox<String> comboComprobante = new JComboBox<>(new String[]{"Boleta", "Factura"});
    private final JComboBox<String> comboMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Yape/Plin", "Tarjeta", "Transferencia"});
    private final JComboBox<Producto> comboProducto = new JComboBox<>();
    private final JTextField campoCantidad = new JTextField("1");
    private final JLabel lblSubtotal = new JLabel("S/ 0.00", SwingConstants.RIGHT);
    private final JLabel lblIgv = new JLabel("S/ 0.00", SwingConstants.RIGHT);
    private final JLabel lblTotal = new JLabel("S/ 0.00", SwingConstants.RIGHT);
    private final DefaultTableModel modeloDetalle;
    private final JTable tablaDetalle;

    private final ClienteModel clienteModel = new ClienteModel();
    private final ProductoModel productoModel = new ProductoModel();
    private final VentaModel ventaModel = new VentaModel();

    public VentasView() {
        setTitle("Registrar Venta");
        setSize(920, 610);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));
        setIconImage(new ImageIcon("image.png").getImage());
        campoNumero.setEditable(false);
        campoFecha.setEditable(false);
        campoTipoCliente.setEditable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "MÓDULO DE VENTAS", TitledBorder.CENTER, TitledBorder.TOP),
                new EmptyBorder(10, 15, 10, 15)));
        panel.add(crearPanelDatos(), BorderLayout.NORTH);

        modeloDetalle = new DefaultTableModel(new String[]{"Producto", "Cantidad", "Precio", "Importe", "ID"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaDetalle = new JTable(modeloDetalle);
        tablaDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDetalle.removeColumn(tablaDetalle.getColumnModel().getColumn(4));
        panel.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);
        panel.add(crearPanelInferior(), BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        configurarRenderers();
        cargarClientes();
        cargarProductos();
        comboCliente.addActionListener(e -> actualizarTipoCliente());
        comboComprobante.addActionListener(e -> campoNumero.setText(ventaModel.generarNumeroVenta(String.valueOf(comboComprobante.getSelectedItem()))));
        prepararNuevaVenta();
    }

    private JPanel crearPanelDatos() {
        JPanel contenedor = new JPanel(new BorderLayout(5, 8));
        JPanel panelCliente = new JPanel(new GridLayout(3, 4, 10, 8));
        panelCliente.add(new JLabel("N° Venta:"));
        panelCliente.add(campoNumero);
        panelCliente.add(new JLabel("Fecha:"));
        panelCliente.add(campoFecha);
        panelCliente.add(new JLabel("Cliente:"));
        panelCliente.add(comboCliente);
        panelCliente.add(new JLabel("Tipo Cliente:"));
        panelCliente.add(campoTipoCliente);
        panelCliente.add(new JLabel("Comprobante:"));
        panelCliente.add(comboComprobante);
        panelCliente.add(new JLabel("Método Pago:"));
        panelCliente.add(comboMetodoPago);

        JPanel panelProducto = new JPanel(new GridLayout(2, 3, 10, 8));
        panelProducto.setBorder(BorderFactory.createTitledBorder("Agregar producto vendido"));
        panelProducto.add(new JLabel("Producto:"));
        panelProducto.add(new JLabel("Cantidad:"));
        panelProducto.add(new JLabel());
        panelProducto.add(comboProducto);
        panelProducto.add(campoCantidad);
        JButton btnAgregar = new JButton("Agregar Producto");
        btnAgregar.addActionListener(e -> agregarProducto());
        panelProducto.add(btnAgregar);

        contenedor.add(panelCliente, BorderLayout.NORTH);
        contenedor.add(panelProducto, BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel panelBotones = new JPanel();
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnQuitar = new JButton("Quitar Item");
        JButton btnGuardar = new JButton("Guardar Venta");
        btnNuevo.addActionListener(e -> prepararNuevaVenta());
        btnQuitar.addActionListener(e -> quitarItem());
        btnGuardar.addActionListener(e -> guardarVenta());
        panelBotones.add(btnNuevo);
        panelBotones.add(btnQuitar);
        panelBotones.add(btnGuardar);

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

    private void configurarRenderers() {
        comboProducto.setRenderer(new DefaultListCellRenderer() {
            @Override public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Producto p) setText(p.getIdProducto() == null ? "Seleccione un producto..." : p.getCodigo() + " - " + p.getNombre() + " (Stock: " + p.getStockActual() + ")");
                return this;
            }
        });
    }

    private void cargarClientes() {
        comboCliente.removeAllItems();
        comboCliente.addItem(new Cliente("", "Seleccione un cliente..."));
        for (Cliente cliente : clienteModel.listar()) comboCliente.addItem(cliente);
    }

    private void cargarProductos() {
        comboProducto.removeAllItems();
        comboProducto.addItem(new Producto());
        for (Producto producto : productoModel.listar()) {
            if ("Activo".equalsIgnoreCase(producto.getEstado())) comboProducto.addItem(producto);
        }
    }

    private void actualizarTipoCliente() {
        Cliente cliente = (Cliente) comboCliente.getSelectedItem();
        campoTipoCliente.setText(cliente == null || cliente.getId() == null || cliente.getId().isBlank() ? "" : cliente.getTipoCliente());
        modeloDetalle.setRowCount(0);
        actualizarTotales();
    }

    private void agregarProducto() {
        Cliente cliente = (Cliente) comboCliente.getSelectedItem();
        Producto producto = (Producto) comboProducto.getSelectedItem();
        if (cliente == null || cliente.getId() == null || cliente.getId().isBlank()) { JOptionPane.showMessageDialog(this, "Seleccione un cliente."); return; }
        if (producto == null || producto.getIdProducto() == null) { JOptionPane.showMessageDialog(this, "Seleccione un producto."); return; }
        try {
            int cantidad = Integer.parseInt(campoCantidad.getText().trim());
            if (cantidad <= 0) { JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero."); return; }
            int disponible = producto.getStockActual() == null ? 0 : producto.getStockActual();
            if (cantidadActualEnDetalle(producto.getIdProducto()) + cantidad > disponible) { JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponible: " + disponible); return; }
            double precio = esMayorista(cliente) ? producto.getPrecioMayorista() : producto.getPrecioMinorista();
            modeloDetalle.addRow(new Object[]{producto.getCodigo() + " - " + producto.getNombre(), cantidad, formatearSoles(precio), formatearSoles(cantidad * precio), producto.getIdProducto()});
            actualizarTotales();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad debe ser numérica.");
        }
    }

    private int cantidadActualEnDetalle(Integer idProducto) {
        int total = 0;
        for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
            if (idProducto.equals(modeloDetalle.getValueAt(i, 4))) total += Integer.parseInt(String.valueOf(modeloDetalle.getValueAt(i, 1)));
        }
        return total;
    }

    private boolean esMayorista(Cliente cliente) { return cliente.getTipoCliente() != null && cliente.getTipoCliente().equalsIgnoreCase("Mayorista"); }

    private void quitarItem() {
        int fila = tablaDetalle.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione un item."); return; }
        modeloDetalle.removeRow(tablaDetalle.convertRowIndexToModel(fila));
        actualizarTotales();
    }

    private void prepararNuevaVenta() {
        campoNumero.setText(ventaModel.generarNumeroVenta(String.valueOf(comboComprobante.getSelectedItem())));
        campoFecha.setText(LocalDateTime.now().format(FORMATO_FECHA));
        modeloDetalle.setRowCount(0);
        campoCantidad.setText("1");
        if (comboCliente.getItemCount() > 0) comboCliente.setSelectedIndex(0);
        if (comboProducto.getItemCount() > 0) comboProducto.setSelectedIndex(0);
        actualizarTotales();
    }

    private void guardarVenta() {
        Cliente cliente = (Cliente) comboCliente.getSelectedItem();
        if (cliente == null || cliente.getId() == null || cliente.getId().isBlank()) { JOptionPane.showMessageDialog(this, "Seleccione un cliente."); return; }
        if (modeloDetalle.getRowCount() == 0) { JOptionPane.showMessageDialog(this, "Agregue productos a la venta."); return; }
        Venta venta = new Venta();
        venta.setNumero(campoNumero.getText());
        venta.setTipoComprobante(String.valueOf(comboComprobante.getSelectedItem()));
        venta.setIdCliente(Integer.parseInt(cliente.getId()));
        venta.setMetodoPago(String.valueOf(comboMetodoPago.getSelectedItem()));
        venta.setFecha(LocalDateTime.now());
        venta.setSubtotal(parseSoles(lblSubtotal.getText()));
        venta.setIgv(parseSoles(lblIgv.getText()));
        venta.setTotal(parseSoles(lblTotal.getText()));
        venta.setEstado("Pagado");
        for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setDescripcion(String.valueOf(modeloDetalle.getValueAt(i, 0)));
            detalle.setCantidad(Integer.parseInt(String.valueOf(modeloDetalle.getValueAt(i, 1))));
            detalle.setPrecioUnitario(parseSoles(String.valueOf(modeloDetalle.getValueAt(i, 2))));
            detalle.setImporte(parseSoles(String.valueOf(modeloDetalle.getValueAt(i, 3))));
            detalle.setIdProducto(Integer.parseInt(String.valueOf(modeloDetalle.getValueAt(i, 4))));
            venta.getDetalles().add(detalle);
        }
        if (ventaModel.insertar(venta)) {
            JOptionPane.showMessageDialog(this, "Venta registrada correctamente.");
            cargarProductos();
            prepararNuevaVenta();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar la venta. Verifique el stock disponible.");
        }
    }

    private void actualizarTotales() {
        double subtotal = 0;
        for (int i = 0; i < modeloDetalle.getRowCount(); i++) subtotal += parseSoles(String.valueOf(modeloDetalle.getValueAt(i, 3)));
        double igv = subtotal * 0.18;
        lblSubtotal.setText(formatearSoles(subtotal));
        lblIgv.setText(formatearSoles(igv));
        lblTotal.setText(formatearSoles(subtotal + igv));
    }

    private double parseSoles(String valor) { return Double.parseDouble(valor.replace("S/", "").trim()); }
    private String formatearSoles(double valor) { return String.format("S/ %.2f", valor); }
}
