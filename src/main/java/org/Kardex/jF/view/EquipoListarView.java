package org.Kardex.jF.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.regex.Pattern;
import org.Kardex.jF.bean.entity.Equipo;
import org.Kardex.jF.model.EquipoModel;

public class EquipoListarView extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel modelo;
    private JTable tabla;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtBuscar = new JTextField(16);
    private EquipoModel dao = new EquipoModel();

    public EquipoListarView() {
        setTitle("Listado de Equipos");
        setSize(1000, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UiStyle.BACKGROUND);
        setLayout(new BorderLayout(0, 10));

        modelo = new DefaultTableModel(
            new String[]{"ID","Código","Marca","Modelo","Problema / Falla","Tipo","Estado","Fecha Ingreso","Cliente"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UiStyle.styleTable(tabla);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(220);
        tabla.getColumnModel().getColumn(8).setPreferredWidth(170);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setBackground(UiStyle.BACKGROUND);
        panelFiltro.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        panelFiltro.add(UiStyle.label("Buscar equipo:"));
        UiStyle.styleField(txtBuscar);
        txtBuscar.setPreferredSize(new Dimension(190, 34));
        txtBuscar.setToolTipText("Ingresa palabras clave como mantenimiento, actualización, reparación, marca, modelo o cliente.");
        panelFiltro.add(txtBuscar);
        add(panelFiltro, BorderLayout.NORTH);

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
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });
        cargarDatos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        for (Equipo e : dao.listar()) {
            modelo.addRow(new Object[]{
                e.getId(), e.getCodigo(), e.getMarca(), e.getModelo(),
                e.getNumeroSerie(), e.getTipoEquipo(),
                Boolean.TRUE.equals(e.getEstado()) ? "Activo" : "Inactivo",
                e.getFechaIngreso(), e.getNombreCliente()
            });
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione un equipo."); return; }
        fila = tabla.convertRowIndexToModel(fila);
        int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar equipo?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (dao.eliminar(id)) { cargarDatos(); JOptionPane.showMessageDialog(this, "Equipo eliminado."); }
            else JOptionPane.showMessageDialog(this, "No se pudo eliminar. Puede tener órdenes activas.");
        }
    }

    private void filtrar() {
        String texto = txtBuscar.getText().trim();
        sorter.setRowFilter(texto.isEmpty() ? null : RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
    }
}
