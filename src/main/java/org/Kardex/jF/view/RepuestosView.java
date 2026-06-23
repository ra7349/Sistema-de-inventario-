package org.Kardex.jF.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.Kardex.jF.bean.entity.Repuesto;
import org.Kardex.jF.model.RepuestoModel;

public class RepuestosView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField campoCodigo = new JTextField();
    private JTextField campoNombre = new JTextField();
    private JTextField campoMarca = new JTextField();
    private JComboBox<String> comboCategoria = new JComboBox<>(new String[]{"Almacenamiento", "Memoria RAM", "Pantalla", "Batería", "Teclado", "Fuente", "Otros"});
    private JTextField campoStock = new JTextField();
    private JTextField campoPrecioCompra = new JTextField();
    private JTextField campoPrecioVenta = new JTextField();
    private JComboBox<String> comboEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});

    private JButton btnNuevo = new JButton("Nuevo");
    private JButton btnGuardar = new JButton("Guardar");
    private JButton btnEditar = new JButton("Editar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnBuscar = new JButton("Buscar");
    private JButton btnLimpiar = new JButton("Limpiar");

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private RepuestoModel dao = new RepuestoModel();
    private String idSeleccionado;

    public RepuestosView() {
        setTitle("Gestionar Repuestos");
        setSize(920, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "GESTIONAR REPUESTOS",
                        TitledBorder.CENTER, TitledBorder.TOP),
                new EmptyBorder(10, 15, 10, 15)));

        JPanel panelForm = new JPanel(new GridLayout(8, 2, 10, 8));
        panelForm.add(new JLabel("Código:"));
        panelForm.add(campoCodigo);
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(campoNombre);
        panelForm.add(new JLabel("Marca:"));
        panelForm.add(campoMarca);
        panelForm.add(new JLabel("Categoría:"));
        panelForm.add(comboCategoria);
        panelForm.add(new JLabel("Stock:"));
        panelForm.add(campoStock);
        panelForm.add(new JLabel("Precio Compra:"));
        panelForm.add(campoPrecioCompra);
        panelForm.add(new JLabel("Precio Venta:"));
        panelForm.add(campoPrecioVenta);
        panelForm.add(new JLabel("Estado:"));
        panelForm.add(comboEstado);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnLimpiar);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Código", "Nombre", "Marca", "Categoría", "Stock", "P.Compra", "P.Venta", "Estado"}, 0) {
            public boolean isCellEditable(int fila, int columna) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setPreferredSize(new Dimension(0, 240));

        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        panelCentro.add(panelForm, BorderLayout.NORTH);
        panelCentro.add(panelBotones, BorderLayout.CENTER);

        panel.add(panelCentro, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);

        add(panel);
        registrarEventos();
        cargarDatos();
        limpiarFormulario();
        setLocationRelativeTo(null);
    }

    private void registrarEventos() {
        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> buscar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) cargarSeleccion();
        });
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        for (Repuesto r : dao.listar()) {
            modeloTabla.addRow(new Object[]{
                r.getId(), r.getCodigo(), r.getNombre(), r.getMarca(), r.getCategoria(), r.getStock(),
                String.format("%.2f", r.getPrecioCompra()), String.format("%.2f", r.getPrecioVenta()), r.getEstado()
            });
        }
    }

    private void guardar() {
        Repuesto repuesto = leerFormulario(false);
        if (repuesto == null) return;
        if (dao.insertar(repuesto)) {
            cargarDatos();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Repuesto guardado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar. Verifique que el código no exista.");
        }
    }

    private void editar() {
        if (idSeleccionado == null) { JOptionPane.showMessageDialog(this, "Seleccione o busque un repuesto para editar."); return; }
        Repuesto repuesto = leerFormulario(true);
        if (repuesto == null) return;
        if (dao.actualizar(repuesto)) {
            cargarDatos();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Repuesto actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el repuesto.");
        }
    }

    private void eliminar() {
        if (idSeleccionado == null && tabla.getSelectedRow() != -1) cargarSeleccion();
        if (idSeleccionado == null) { JOptionPane.showMessageDialog(this, "Seleccione un repuesto para eliminar."); return; }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar repuesto seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (dao.eliminar(Integer.parseInt(idSeleccionado))) {
                cargarDatos();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Repuesto eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el repuesto.");
            }
        }
    }

    private void buscar() {
        String codigo = campoCodigo.getText().trim();
        if (codigo.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese el código del repuesto a buscar."); return; }
        Repuesto r = dao.buscarPorCodigo(codigo);
        if (r == null) { JOptionPane.showMessageDialog(this, "No se encontró un repuesto con el código indicado."); return; }
        cargarFormulario(r);
        seleccionarFilaPorId(r.getId());
    }

    private Repuesto leerFormulario(boolean incluirId) {
        try {
            String codigo = campoCodigo.getText().trim();
            String nombre = campoNombre.getText().trim();
            if (codigo.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Código y nombre son obligatorios.");
                return null;
            }
            Repuesto r = new Repuesto();
            if (incluirId) r.setId(idSeleccionado);
            r.setCodigo(codigo);
            r.setNombre(nombre);
            r.setMarca(campoMarca.getText().trim());
            r.setCategoria(comboCategoria.getSelectedItem().toString());
            int stock = Integer.parseInt(campoStock.getText().trim());
            double precioCompra = Double.parseDouble(campoPrecioCompra.getText().trim());
            double precioVenta = Double.parseDouble(campoPrecioVenta.getText().trim());
            if (stock < 0 || precioCompra < 0 || precioVenta < 0) {
                JOptionPane.showMessageDialog(this, "Stock y precios no pueden ser negativos.");
                return null;
            }
            r.setStock(stock);
            r.setPrecioCompra(precioCompra);
            r.setPrecioVenta(precioVenta);
            r.setEstado(comboEstado.getSelectedItem().toString());
            return r;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock debe ser entero y los precios deben ser numéricos.");
            return null;
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        idSeleccionado = modeloTabla.getValueAt(fila, 0).toString();
        campoCodigo.setText(modeloTabla.getValueAt(fila, 1).toString());
        campoNombre.setText(modeloTabla.getValueAt(fila, 2).toString());
        campoMarca.setText(valor(fila, 3));
        comboCategoria.setSelectedItem(modeloTabla.getValueAt(fila, 4).toString());
        campoStock.setText(modeloTabla.getValueAt(fila, 5).toString());
        campoPrecioCompra.setText(modeloTabla.getValueAt(fila, 6).toString());
        campoPrecioVenta.setText(modeloTabla.getValueAt(fila, 7).toString());
        comboEstado.setSelectedItem(modeloTabla.getValueAt(fila, 8).toString());
    }

    private void cargarFormulario(Repuesto r) {
        idSeleccionado = r.getId();
        campoCodigo.setText(r.getCodigo());
        campoNombre.setText(r.getNombre());
        campoMarca.setText(r.getMarca());
        comboCategoria.setSelectedItem(r.getCategoria());
        campoStock.setText(String.valueOf(r.getStock()));
        campoPrecioCompra.setText(String.format("%.2f", r.getPrecioCompra()));
        campoPrecioVenta.setText(String.format("%.2f", r.getPrecioVenta()));
        comboEstado.setSelectedItem(r.getEstado());
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        campoCodigo.setText("");
        campoNombre.setText("");
        campoMarca.setText("");
        comboCategoria.setSelectedIndex(0);
        campoStock.setText("0");
        campoPrecioCompra.setText("0.00");
        campoPrecioVenta.setText("0.00");
        comboEstado.setSelectedIndex(0);
        tabla.clearSelection();
        campoCodigo.requestFocus();
    }

    private void seleccionarFilaPorId(String id) {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if (modeloTabla.getValueAt(i, 0).toString().equals(id)) {
                tabla.setRowSelectionInterval(i, i);
                tabla.scrollRectToVisible(tabla.getCellRect(i, 0, true));
                return;
            }
        }
    }

    private String valor(int fila, int columna) {
        Object valor = modeloTabla.getValueAt(fila, columna);
        return valor != null ? valor.toString() : "";
    }

}
