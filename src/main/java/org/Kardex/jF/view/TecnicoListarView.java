package org.Kardex.jF.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.Kardex.jF.bean.entity.Tecnico;
import org.Kardex.jF.model.TecnicoModel;

public class TecnicoListarView extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel modelo;
    private JTable tabla;
    private TecnicoModel dao = new TecnicoModel();

    public TecnicoListarView() {
        setTitle("Listado de Técnicos");
        setSize(850, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UiStyle.BACKGROUND);
        setLayout(new BorderLayout(0, 10));

        modelo = new DefaultTableModel(
            new String[]{"ID","Código","Nombre","Apellido","Teléfono","Correo","Especialidad"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UiStyle.styleTable(tabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        panelBotones.setBackground(UiStyle.BACKGROUND);
        JButton btnEliminar  = UiStyle.dangerButton("Eliminar");
        JButton btnRefrescar = UiStyle.secondaryButton("Refrescar");
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        btnEliminar .addActionListener(e -> eliminar());
        btnRefrescar.addActionListener(e -> cargarDatos());
        cargarDatos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        for (Tecnico t : dao.listar()) {
            modelo.addRow(new Object[]{
                t.getId(), t.getCodigo(), t.getNombre(), t.getApellido(),
                t.getTelefono(), t.getCorreo(), t.getEspecialidad()
            });
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione un técnico."); return; }
        int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar técnico?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (dao.eliminar(id)) { cargarDatos(); JOptionPane.showMessageDialog(this, "Técnico eliminado."); }
            else JOptionPane.showMessageDialog(this, "No se pudo eliminar. Puede tener órdenes asignadas.");
        }
    }
}
