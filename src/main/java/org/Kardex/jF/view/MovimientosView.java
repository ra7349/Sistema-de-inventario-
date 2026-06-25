package org.Kardex.jF.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.Kardex.jF.bean.entity.MovimientoInventario;
import org.Kardex.jF.bean.entity.Producto;
import org.Kardex.jF.model.MovimientoInventarioModel;
import org.Kardex.jF.model.ProductoModel;

public class MovimientosView extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JRadioButton rbEntrada = new JRadioButton("Entrada", true);
    private JRadioButton rbSalida  = new JRadioButton("Salida");
    private JRadioButton rbAjuste  = new JRadioButton("Ajuste");
    private JComboBox<Producto> comboProducto = new JComboBox<>();
    private JTextField campoCantidad = new JTextField("1");
    private JComboBox<String> comboMotivo = new JComboBox<>();
    private JTextField campoFecha = new JTextField(LocalDate.now().format(FORMATO_FECHA));
    private JTextArea campoObservacion = new JTextArea(3, 20);
    private JButton btnRegistrar = new JButton("Registrar Movimiento");

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private ProductoModel productoDao = new ProductoModel();
    private MovimientoInventarioModel movimientoDao = new MovimientoInventarioModel();

    public MovimientosView() {
        setTitle("Movimientos de Inventario");
        setSize(820, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setIconImage(new ImageIcon("image.png").getImage());
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "MOVIMIENTOS DE INVENTARIO",
                        TitledBorder.CENTER, TitledBorder.TOP),
                new EmptyBorder(10, 15, 10, 15)));

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbEntrada);
        grupo.add(rbSalida);
        grupo.add(rbAjuste);
        JPanel panelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelRadio.add(rbEntrada);
        panelRadio.add(rbSalida);
        panelRadio.add(rbAjuste);

        agregarFila(panelForm, gbc, 0, "Tipo Movimiento:", panelRadio);
        agregarFila(panelForm, gbc, 1, "Producto:", comboProducto);
        agregarFila(panelForm, gbc, 2, "Cantidad:", campoCantidad);
        agregarFila(panelForm, gbc, 3, "Motivo:", comboMotivo);
        agregarFila(panelForm, gbc, 4, "Fecha:", campoFecha);
        campoObservacion.setLineWrap(true);
        campoObservacion.setWrapStyleWord(true);
        agregarFila(panelForm, gbc, 5, "Observación:", new JScrollPane(campoObservacion));
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1;
        panelForm.add(btnRegistrar, gbc);

        modeloTabla = new DefaultTableModel(new String[]{"Fecha", "Tipo", "Producto", "Cantidad", "Motivo", "Observación"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) { return false; }
        };
        tabla = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setPreferredSize(new Dimension(0, 220));

        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);

        add(panel);
        registrarEventos();
        cargarProductos();
        cargarMotivos();
        cargarMovimientos();
        UiStyle.applyTo(this);
        setLocationRelativeTo(null);
    }

    private void agregarFila(JPanel panelForm, GridBagConstraints gbc, int fila, String etiqueta, Component componente) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panelForm.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panelForm.add(componente, gbc);
    }

    private void registrarEventos() {
        rbEntrada.addActionListener(e -> cargarMotivos());
        rbSalida.addActionListener(e -> cargarMotivos());
        rbAjuste.addActionListener(e -> cargarMotivos());
        btnRegistrar.addActionListener(e -> registrarMovimiento());
    }

    private void cargarProductos() {
        comboProducto.removeAllItems();
        for (Producto p : productoDao.listar()) {
            if ("Activo".equalsIgnoreCase(p.getEstado())) comboProducto.addItem(p);
        }
    }

    private void cargarMotivos() {
        comboMotivo.removeAllItems();
        if (rbSalida.isSelected()) {
            comboMotivo.addItem("Venta");
            comboMotivo.addItem("Merma");
            comboMotivo.addItem("Consumo interno");
        } else if (rbAjuste.isSelected()) {
            comboMotivo.addItem("Ajuste positivo");
            comboMotivo.addItem("Ajuste negativo");
            comboMotivo.addItem("Corrección de inventario");
        } else {
            comboMotivo.addItem("Compra de mercadería");
            comboMotivo.addItem("Devolución de cliente");
            comboMotivo.addItem("Ingreso inicial");
        }
    }

    private void cargarMovimientos() {
        modeloTabla.setRowCount(0);
        for (MovimientoInventario m : movimientoDao.listar()) {
            modeloTabla.addRow(new Object[]{
                m.getFecha().format(FORMATO_FECHA),
                m.getTipoMovimiento(),
                m.getCodigoProducto() + " - " + m.getNombreProducto(),
                m.getCantidad(),
                m.getMotivo(),
                m.getObservacion()
            });
        }
    }

    private void registrarMovimiento() {
        Producto producto = (Producto) comboProducto.getSelectedItem();
        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Registre primero un producto activo.");
            return;
        }

        try {
            MovimientoInventario movimiento = new MovimientoInventario();
            movimiento.setIdProducto(producto.getIdProducto());
            movimiento.setTipoMovimiento(obtenerTipoMovimiento());
            movimiento.setCantidad(Integer.parseInt(campoCantidad.getText().trim()));
            movimiento.setMotivo(String.valueOf(comboMotivo.getSelectedItem()));
            movimiento.setFecha(LocalDate.parse(campoFecha.getText().trim(), FORMATO_FECHA));
            movimiento.setObservacion(campoObservacion.getText().trim());

            if (movimiento.getCantidad() <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que cero.");
                return;
            }

            if (movimientoDao.registrar(movimiento)) {
                cargarProductos();
                cargarMovimientos();
                campoObservacion.setText("");
                JOptionPane.showMessageDialog(this, "Movimiento registrado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar. Verifique el stock disponible para salidas o ajustes negativos.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero.");
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "La fecha debe tener el formato dd/MM/yyyy.");
        }
    }

    private String obtenerTipoMovimiento() {
        if (rbSalida.isSelected()) return "Salida";
        if (rbAjuste.isSelected()) return "Ajuste";
        return "Entrada";
    }
}
