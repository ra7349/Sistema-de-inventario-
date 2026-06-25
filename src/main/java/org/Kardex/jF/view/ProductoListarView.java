package org.Kardex.jF.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.Kardex.jF.bean.entity.Categoria;
import org.Kardex.jF.bean.entity.Producto;
import org.Kardex.jF.model.CategoriaModel;
import org.Kardex.jF.model.ProductoModel;

public class ProductoListarView extends JFrame {

    private static final long serialVersionUID = 1L;

    private final ProductoModel dao = new ProductoModel();
    private final CategoriaModel categoriaDao = new CategoriaModel();
    private final DefaultTableModel modelo;
    private final JTable tabla;
    private final JTextField txtId = new JTextField();
    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JComboBox<String> cbCategoria = new JComboBox<>();
    private final JTextField txtPresentacion = new JTextField();
    private final JTextField txtUnidadMedida = new JTextField();
    private final JTextField txtPrecioCompra = new JTextField("0.00");
    private final JTextField txtPrecioMinorista = new JTextField("0.00");
    private final JTextField txtPrecioMayorista = new JTextField("0.00");
    private final JTextField txtStockActual = new JTextField("0");
    private final JTextField txtStockMinimo = new JTextField("0");
    private final JComboBox<String> cbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
    private final JTextField txtFiltro = new JTextField();

    public ProductoListarView() {
        setTitle("Productos Golocentro");
        setSize(1180, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));
        setIconImage(new ImageIcon("image.png").getImage());

        modelo = new DefaultTableModel(new String[]{"ID", "Código", "Nombre", "Categoría", "Presentación", "Unidad", "Compra", "Minorista", "Mayorista", "Stock", "Mínimo", "Estado"}, 0) {
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
        cargarCategorias();
        nuevo();
        cargarDatos(null);
    }

    private JPanel crearFormulario() {
        JPanel contenedor = new JPanel(new BorderLayout(8, 8));
        contenedor.setBorder(BorderFactory.createTitledBorder("Registro de producto"));

        JPanel formulario = new JPanel(new GridLayout(4, 6, 8, 8));
        txtId.setEditable(false);
        formulario.add(new JLabel("ID:")); formulario.add(txtId);
        formulario.add(new JLabel("Código *:")); formulario.add(txtCodigo);
        formulario.add(new JLabel("Nombre *:")); formulario.add(txtNombre);
        formulario.add(new JLabel("Categoría *:")); formulario.add(cbCategoria);
        formulario.add(new JLabel("Presentación:")); formulario.add(txtPresentacion);
        formulario.add(new JLabel("Unidad medida:")); formulario.add(txtUnidadMedida);
        formulario.add(new JLabel("Precio compra:")); formulario.add(txtPrecioCompra);
        formulario.add(new JLabel("Precio minorista *:")); formulario.add(txtPrecioMinorista);
        formulario.add(new JLabel("Precio mayorista *:")); formulario.add(txtPrecioMayorista);
        formulario.add(new JLabel("Stock actual *:")); formulario.add(txtStockActual);
        formulario.add(new JLabel("Stock mínimo:")); formulario.add(txtStockMinimo);
        formulario.add(new JLabel("Estado:")); formulario.add(cbEstado);

        JPanel busqueda = new JPanel(new BorderLayout(8, 8));
        busqueda.setBorder(BorderFactory.createTitledBorder("Filtro por nombre, código o categoría"));
        JButton btnBuscar = new JButton("Buscar");
        busqueda.add(txtFiltro, BorderLayout.CENTER);
        busqueda.add(btnBuscar, BorderLayout.EAST);
        btnBuscar.addActionListener(e -> cargarDatos(txtFiltro.getText()));
        txtFiltro.addActionListener(e -> cargarDatos(txtFiltro.getText()));

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
        btnBuscar.addActionListener(e -> cargarDatos(txtFiltro.getText()));
        btnLimpiar.addActionListener(e -> limpiarTodo());
        p.add(btnNuevo); p.add(btnGuardar); p.add(btnActualizar); p.add(btnEliminar); p.add(btnBuscar); p.add(btnLimpiar);
        return p;
    }

    private void cargarDatos(String filtro) {
        modelo.setRowCount(0);
        for (Producto p : dao.buscar(filtro)) {
            modelo.addRow(new Object[]{p.getIdProducto(), p.getCodigo(), p.getNombre(), p.getCategoria(), p.getPresentacion(), p.getUnidadMedida(), p.getPrecioCompra(), p.getPrecioMinorista(), p.getPrecioMayorista(), p.getStockActual(), p.getStockMinimo(), p.getEstado()});
        }
    }

    private void guardar() {
        Producto p = leerFormulario(false);
        if (p == null) return;
        if (dao.insertar(p)) { JOptionPane.showMessageDialog(this, "Producto registrado."); limpiarTodo(); }
        else JOptionPane.showMessageDialog(this, "No se pudo registrar. Verifique si el código ya existe.");
    }

    private void actualizar() {
        Producto p = leerFormulario(true);
        if (p == null) return;
        if (dao.actualizar(p)) { JOptionPane.showMessageDialog(this, "Producto actualizado."); limpiarTodo(); }
        else JOptionPane.showMessageDialog(this, "No se pudo actualizar el producto.");
    }

    private void eliminar() {
        if (txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione un producto."); return; }
        int id = Integer.parseInt(txtId.getText().trim());
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar producto?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (dao.eliminar(id)) { JOptionPane.showMessageDialog(this, "Producto eliminado."); limpiarTodo(); }
            else JOptionPane.showMessageDialog(this, "No se pudo eliminar. Puede estar en uso.");
        }
    }

    private Producto leerFormulario(boolean requiereId) {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        if (cbCategoria.getSelectedItem() == null) { JOptionPane.showMessageDialog(this, "Registre una categoría activa antes de crear productos."); return null; }
        String categoria = String.valueOf(cbCategoria.getSelectedItem()).trim();
        if (requiereId && txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione un producto para actualizar."); return null; }
        if (codigo.isEmpty() || nombre.isEmpty() || categoria.isEmpty()) { JOptionPane.showMessageDialog(this, "Código, nombre y categoría son obligatorios."); return null; }
        try {
            double compra = parseDouble(txtPrecioCompra, "precio de compra");
            double minorista = parseDouble(txtPrecioMinorista, "precio minorista");
            double mayorista = parseDouble(txtPrecioMayorista, "precio mayorista");
            int stockActual = parseInt(txtStockActual, "stock actual");
            int stockMinimo = parseInt(txtStockMinimo, "stock mínimo");
            if (compra < 0 || minorista < 0 || mayorista < 0 || stockActual < 0 || stockMinimo < 0) {
                JOptionPane.showMessageDialog(this, "Los precios y el stock no pueden ser negativos."); return null;
            }
            Producto p = new Producto();
            if (requiereId) p.setIdProducto(Integer.parseInt(txtId.getText().trim()));
            p.setCodigo(codigo);
            p.setNombre(nombre);
            p.setCategoria(categoria);
            p.setPresentacion(txtPresentacion.getText().trim());
            p.setUnidadMedida(txtUnidadMedida.getText().trim());
            p.setPrecioCompra(compra);
            p.setPrecioMinorista(minorista);
            p.setPrecioMayorista(mayorista);
            p.setStockActual(stockActual);
            p.setStockMinimo(stockMinimo);
            p.setEstado(String.valueOf(cbEstado.getSelectedItem()));
            return p;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return null;
        }
    }

    private double parseDouble(JTextField campo, String nombre) {
        String valor = campo.getText().trim();
        if (valor.isEmpty()) throw new NumberFormatException("Ingrese " + nombre + ".");
        return Double.parseDouble(valor.replace(',', '.'));
    }

    private int parseInt(JTextField campo, String nombre) {
        String valor = campo.getText().trim();
        if (valor.isEmpty()) throw new NumberFormatException("Ingrese " + nombre + ".");
        return Integer.parseInt(valor);
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;
        txtId.setText(valor(fila, 0));
        txtCodigo.setText(valor(fila, 1));
        txtNombre.setText(valor(fila, 2));
        cbCategoria.setSelectedItem(valor(fila, 3));
        txtPresentacion.setText(valor(fila, 4));
        txtUnidadMedida.setText(valor(fila, 5));
        txtPrecioCompra.setText(valor(fila, 6));
        txtPrecioMinorista.setText(valor(fila, 7));
        txtPrecioMayorista.setText(valor(fila, 8));
        txtStockActual.setText(valor(fila, 9));
        txtStockMinimo.setText(valor(fila, 10));
        cbEstado.setSelectedItem(valor(fila, 11));
    }

    private String valor(int fila, int columna) {
        Object valor = modelo.getValueAt(fila, columna);
        return valor == null ? "" : valor.toString();
    }

    private void limpiarTodo() {
        nuevo();
        txtFiltro.setText("");
        cargarDatos(null);
    }

    private void cargarCategorias() {
        cbCategoria.removeAllItems();
        for (Categoria categoria : categoriaDao.listar()) {
            cbCategoria.addItem(categoria.getNombre());
        }
        cbCategoria.setEnabled(cbCategoria.getItemCount() > 0);
    }

    private void nuevo() {
        txtId.setText("");
        txtCodigo.setText(dao.generarSiguienteCodigo());
        txtNombre.setText("");
        if (cbCategoria.getItemCount() > 0) cbCategoria.setSelectedIndex(0);
        txtPresentacion.setText("");
        txtUnidadMedida.setText("");
        txtPrecioCompra.setText("0.00");
        txtPrecioMinorista.setText("0.00");
        txtPrecioMayorista.setText("0.00");
        txtStockActual.setText("0");
        txtStockMinimo.setText("0");
        cbEstado.setSelectedIndex(0);
        tabla.clearSelection();
        txtCodigo.requestFocus();
    }
}
