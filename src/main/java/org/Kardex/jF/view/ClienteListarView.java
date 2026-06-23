package org.Kardex.jF.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.Kardex.jF.bean.entity.Cliente;
import org.Kardex.jF.model.ClienteModel;

public class ClienteListarView extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel modelo;
    private JTable tabla;
    private ClienteModel dao = new ClienteModel();

    public ClienteListarView() {
        setTitle("Listado de Clientes");
        setSize(1050, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UiStyle.BACKGROUND);
        setLayout(new BorderLayout(0, 10));

        modelo = new DefaultTableModel(
            new String[]{"ID","Código","Nombre","Apellido","Teléfono","Correo","Dirección","Tipo","RUC"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UiStyle.styleTable(tabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        panelBotones.setBackground(UiStyle.BACKGROUND);
        JButton btnEliminar  = UiStyle.dangerButton("Eliminar");
        JButton btnActualizar = UiStyle.secondaryButton("Refrescar");
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        btnEliminar .addActionListener(e -> eliminar());
        btnActualizar.addActionListener(e -> cargarDatos());
        cargarDatos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        for (Cliente c : dao.listar()) {
            modelo.addRow(new Object[]{
                c.getId(), c.getCodigo(), c.getNombre(), c.getApellido(),
                c.getTelefono(), c.getCorreo(), c.getDireccion(),
                c.getTipoCliente(), c.getRUC()
            });
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione un cliente."); return; }
        int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar cliente seleccionado?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (dao.eliminar(id)) {
                cargarDatos();
                JOptionPane.showMessageDialog(this, "Cliente eliminado.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar. Puede tener equipos asociados.");
            }
        }
    }
}
