package org.Kardex.jF.view;

import javax.swing.*;
import java.awt.*;
import org.Kardex.jF.bean.entity.Producto;
import org.Kardex.jF.model.ProductoModel;

public class FormularioProducto extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtCodigo      = new JTextField();
    private JTextField txtNombre      = new JTextField();
    private JTextField txtDescripcion = new JTextField();
    private JTextField txtPrecio      = new JTextField("0.00");
    private JTextField txtStock       = new JTextField("0");
    private JComboBox<String> cbCategoria = new JComboBox<>(
        new String[]{"Repuesto","Insumo","Accesorio"});

    private final ProductoModel dao = new ProductoModel();

    public FormularioProducto(JFrame parent) {
        super(parent, "Registrar Producto / Repuesto", true);
        setSize(420, 340);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        add(crearPanel(), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanel() {
        JPanel p = new JPanel(new GridLayout(6, 2, 10, 8));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        p.add(new JLabel("Código *:")); p.add(txtCodigo);
        p.add(new JLabel("Nombre *:")); p.add(txtNombre);
        p.add(new JLabel("Descripción:")); p.add(txtDescripcion);
        p.add(new JLabel("Precio:")); p.add(txtPrecio);
        p.add(new JLabel("Stock:")); p.add(txtStock);
        p.add(new JLabel("Categoría:")); p.add(cbCategoria);
        return p;
    }

    private JPanel crearBotones() {
        JPanel p = new JPanel(new GridLayout(1, 2, 8, 0));
        p.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Limpiar");
        btnGuardar .addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> limpiar());
        p.add(btnCancelar);
        p.add(btnGuardar);
        return p;
    }

    private void guardar() {
        if (txtCodigo.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Código y Nombre son obligatorios."); return;
        }
        Producto prod = new Producto();
        prod.setCodigo(txtCodigo.getText().trim());
        prod.setNombre(txtNombre.getText().trim());
        prod.setDescripcion(txtDescripcion.getText().trim());
        try {
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            if (precio < 0 || stock < 0) {
                JOptionPane.showMessageDialog(this, "Precio y stock no pueden ser negativos.");
                return;
            }
            prod.setPrecio(precio);
            prod.setStock(stock);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y stock deben ser numéricos.");
            return;
        }
        prod.setCategoria((String) cbCategoria.getSelectedItem());
        if (dao.insertar(prod)) {
            JOptionPane.showMessageDialog(this, "Producto registrado.");
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar. ¿Código duplicado?");
        }
    }

    private void limpiar() {
        txtCodigo.setText(""); txtNombre.setText(""); txtDescripcion.setText("");
        txtPrecio.setText("0.00"); txtStock.setText("0"); cbCategoria.setSelectedIndex(0);
        txtCodigo.requestFocus();
    }
}
