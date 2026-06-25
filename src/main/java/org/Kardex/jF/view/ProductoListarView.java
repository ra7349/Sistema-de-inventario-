package org.Kardex.jF.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.Kardex.jF.bean.entity.Producto;
import org.Kardex.jF.model.ProductoModel;

public class ProductoListarView extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel modelo;
    private JTable tabla;
    private ProductoModel dao = new ProductoModel();

    public ProductoListarView() {
        setTitle("Inventario de productos Golocentro");
        setSize(900, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        this.setIconImage(new ImageIcon("image.png").getImage());
        modelo = new DefaultTableModel(
            new String[]{"ID","Código","Nombre","Descripción","Precio","Stock","Categoría"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnEliminar  = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        UiStyle.applyTo(this);

        btnEliminar .addActionListener(e -> eliminar());
        btnRefrescar.addActionListener(e -> cargarDatos());
        cargarDatos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        for (Producto p : dao.listar()) {
            modelo.addRow(new Object[]{
                p.getId(), p.getCodigo(), p.getNombre(), p.getDescripcion(),
                String.format("S/ %.2f", p.getPrecio()), p.getStock(), p.getCategoria()
            });
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione un producto."); return; }
        int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar producto?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (dao.eliminar(id)) { cargarDatos(); JOptionPane.showMessageDialog(this, "Producto eliminado."); }
            else JOptionPane.showMessageDialog(this, "No se pudo eliminar. Puede estar en uso en una orden.");
        }
    }
}
