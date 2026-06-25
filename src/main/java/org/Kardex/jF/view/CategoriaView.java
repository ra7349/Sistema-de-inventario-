package org.Kardex.jF.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
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
import javax.swing.table.DefaultTableModel;

import org.Kardex.jF.bean.entity.Categoria;
import org.Kardex.jF.model.CategoriaModel;

public class CategoriaView extends JFrame {

    private static final long serialVersionUID = 1L;

    private final CategoriaModel dao = new CategoriaModel();
    private final DefaultTableModel modelo;
    private final JTable tabla;
    private final JTextField txtId = new JTextField();
    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtDescripcion = new JTextField();
    private final JComboBox<String> cbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
    private final JTextField txtFiltroNombre = new JTextField();

    public CategoriaView() {
        setTitle("Gestión de Categorías");
        setSize(850, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));
        setIconImage(new ImageIcon("image.png").getImage());

        modelo = new DefaultTableModel(new String[]{"ID", "Código", "Nombre", "Descripción", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });

        add(crearFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(crearBotonera(), BorderLayout.SOUTH);
        nuevo();
        cargarDatos(null);
    }

    private JPanel crearFormulario() {
        JPanel contenedor = new JPanel(new BorderLayout(8, 8));
        contenedor.setBorder(BorderFactory.createTitledBorder("Registro de categoría"));

        JPanel formulario = new JPanel(new GridLayout(2, 5, 8, 8));
        txtId.setEditable(false);
        txtCodigo.setEditable(false);
        formulario.add(new JLabel("ID:")); formulario.add(txtId);
        formulario.add(new JLabel("Código *:")); formulario.add(txtCodigo);
        formulario.add(new JLabel("Nombre *:")); formulario.add(txtNombre);
        formulario.add(new JLabel("Descripción:")); formulario.add(txtDescripcion);
        formulario.add(new JLabel("Estado:")); formulario.add(cbEstado);

        JPanel busqueda = new JPanel(new BorderLayout(8, 8));
        busqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda por nombre"));
        JButton btnBuscar = new JButton("Buscar");
        busqueda.add(txtFiltroNombre, BorderLayout.CENTER);
        busqueda.add(btnBuscar, BorderLayout.EAST);
        btnBuscar.addActionListener(e -> cargarDatos(txtFiltroNombre.getText()));
        txtFiltroNombre.addActionListener(e -> cargarDatos(txtFiltroNombre.getText()));

        contenedor.add(formulario, BorderLayout.CENTER);
        contenedor.add(busqueda, BorderLayout.SOUTH);
        return contenedor;
    }

    private JPanel crearBotonera() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");
        btnNuevo.addActionListener(e -> nuevo());
        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> cargarDatos(txtFiltroNombre.getText()));
        btnLimpiar.addActionListener(e -> limpiarTodo());
        p.add(btnNuevo); p.add(btnGuardar); p.add(btnActualizar); p.add(btnEliminar); p.add(btnBuscar); p.add(btnLimpiar);
        return p;
    }

    private void cargarDatos(String filtro) {
        modelo.setRowCount(0);
        for (Categoria c : dao.buscarPorNombre(filtro)) {
            modelo.addRow(new Object[]{c.getIdCategoria(), c.getCodigo(), c.getNombre(), c.getDescripcion(), c.getEstado()});
        }
    }

    private void guardar() {
        Categoria c = leerFormulario(false);
        if (c == null) return;
        if (dao.insertar(c)) { JOptionPane.showMessageDialog(this, "Categoría registrada."); limpiarTodo(); }
        else JOptionPane.showMessageDialog(this, "No se pudo registrar. Verifique si el código ya existe.");
    }

    private void actualizar() {
        Categoria c = leerFormulario(true);
        if (c == null) return;
        if (dao.actualizar(c)) { JOptionPane.showMessageDialog(this, "Categoría actualizada."); limpiarTodo(); }
        else JOptionPane.showMessageDialog(this, "No se pudo actualizar la categoría.");
    }

    private void eliminar() {
        if (txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione una categoría."); return; }
        int id = Integer.parseInt(txtId.getText().trim());
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar categoría?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (dao.eliminar(id)) { JOptionPane.showMessageDialog(this, "Categoría eliminada."); limpiarTodo(); }
            else JOptionPane.showMessageDialog(this, "No se pudo eliminar. Puede estar en uso.");
        }
    }

    private Categoria leerFormulario(boolean requiereId) {
        if (requiereId && txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione una categoría para actualizar."); return null; }
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        if (codigo.isEmpty()) codigo = dao.generarSiguienteCodigo();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Nombre es obligatorio."); return null; }
        Categoria c = new Categoria();
        if (requiereId) c.setIdCategoria(Integer.parseInt(txtId.getText().trim()));
        c.setCodigo(codigo);
        c.setNombre(nombre);
        c.setDescripcion(txtDescripcion.getText().trim());
        c.setEstado(String.valueOf(cbEstado.getSelectedItem()));
        return c;
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;
        txtId.setText(valor(fila, 0));
        txtCodigo.setText(valor(fila, 1));
        txtNombre.setText(valor(fila, 2));
        txtDescripcion.setText(valor(fila, 3));
        cbEstado.setSelectedItem(valor(fila, 4));
    }

    private String valor(int fila, int columna) {
        Object valor = modelo.getValueAt(fila, columna);
        return valor == null ? "" : valor.toString();
    }

    private void limpiarTodo() {
        nuevo();
        txtFiltroNombre.setText("");
        cargarDatos(null);
    }

    private void nuevo() {
        txtId.setText("");
        txtCodigo.setText(dao.generarSiguienteCodigo());
        txtNombre.setText("");
        txtDescripcion.setText("");
        cbEstado.setSelectedIndex(0);
        tabla.clearSelection();
        txtNombre.requestFocus();
    }
}
