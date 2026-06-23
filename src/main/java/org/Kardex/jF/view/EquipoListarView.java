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
    private JTextField txtBuscar = new JTextField(24);
    private EquipoModel dao = new EquipoModel();

    public EquipoListarView() {
        setTitle("Listado de Equipos");
        setSize(1000, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(
            new String[]{"ID","Código","Marca","Modelo","Problema / Falla","Tipo","Estado","Fecha Ingreso","Cliente"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(220);
        tabla.getColumnModel().getColumn(8).setPreferredWidth(170);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
        panelFiltro.add(new JLabel("Buscar equipo:"));
        txtBuscar.setToolTipText("Filtra por código, marca, modelo, problema, tipo, estado, fecha o cliente.");
        panelFiltro.add(txtBuscar);
        add(panelFiltro, BorderLayout.NORTH);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnEliminar  = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");
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
