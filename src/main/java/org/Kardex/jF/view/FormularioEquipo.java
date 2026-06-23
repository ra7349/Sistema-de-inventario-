package org.Kardex.jF.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.Kardex.jF.bean.entity.Cliente;
import org.Kardex.jF.bean.entity.Equipo;
import org.Kardex.jF.model.ClienteModel;
import org.Kardex.jF.model.EquipoModel;

public class FormularioEquipo extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtCodigo      = new JTextField();
    private JTextField txtMarca       = new JTextField();
    private JTextField txtModelo      = new JTextField();
    private JTextArea  txtProblema    = new JTextArea(3, 20);
    private JComboBox<String> cbTipo  = new JComboBox<>(
        new String[]{"Laptop","PC Escritorio","Impresora","Tablet","Monitor","Otro"});
    private JComboBox<String> cbEstado = new JComboBox<>(new String[]{"Activo","Inactivo"});
    private JComboBox<String> cbCliente;

    private final EquipoModel  daoE = new EquipoModel();
    private final ClienteModel daoC = new ClienteModel();
    private List<Cliente> clientes;

    public FormularioEquipo(JFrame parent) {
        super(parent, "Registrar Equipo", true);
        setSize(500, 430);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(UiStyle.BACKGROUND);
        setLayout(new BorderLayout(0, 10));

        clientes = daoC.listar();
        String[] items = clientes.stream()
            .map(c -> c.getId() + " - " + c.getNombre() + " " + c.getApellido())
            .toArray(String[]::new);
        cbCliente = new JComboBox<>(items);

        add(crearPanel(), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);
        asignarSiguienteCodigo();
    }

    private JPanel crearPanel() {
        JPanel p = UiStyle.cardPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        for (JTextField field : new JTextField[]{txtCodigo, txtMarca, txtModelo}) {
            UiStyle.styleField(field);
        }
        txtCodigo.setEditable(false);
        UiStyle.styleCombo(cbTipo);
        UiStyle.styleCombo(cbEstado);
        UiStyle.styleCombo(cbCliente);
        UiStyle.styleTextArea(txtProblema);
        txtProblema.setRows(2);
        txtProblema.setToolTipText("Escribe palabras clave: mantenimiento, actualización, reparación, pantalla, disco, etc.");

        int row = 0;
        addRow(p, g, row++, "Código *:", txtCodigo);
        addRow(p, g, row++, "Marca:", txtMarca);
        addRow(p, g, row++, "Modelo:", txtModelo);
        addRow(p, g, row++, "Tipo equipo:", cbTipo);
        addRow(p, g, row++, "Estado:", cbEstado);
        addRow(p, g, row++, "Cliente *:", cbCliente);

        g.gridx = 0;
        g.gridy = row;
        g.weightx = 0;
        g.anchor = GridBagConstraints.NORTHWEST;
        p.add(UiStyle.label("Falla / palabras clave:"), g);

        g.gridx = 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.BOTH;
        g.weighty = 0.35;
        p.add(new JScrollPane(txtProblema), g);
        return p;
    }

    private void addRow(JPanel panel, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridx = 0;
        g.gridy = row;
        g.weightx = 0;
        g.weighty = 0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        panel.add(UiStyle.label(label), g);

        g.gridx = 1;
        g.weightx = 1;
        panel.add(field, g);
    }

    private JPanel crearBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        p.setBackground(UiStyle.BACKGROUND);
        JButton btnGuardar  = UiStyle.primaryButton("Guardar");
        JButton btnCancelar = UiStyle.secondaryButton("Limpiar");
        btnGuardar .addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> limpiar());
        p.add(btnCancelar);
        p.add(btnGuardar);
        return p;
    }

    private void guardar() {
        if (txtCodigo.getText().trim().isEmpty()) asignarSiguienteCodigo();
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay clientes registrados."); return;
        }
        Equipo eq = new Equipo();
        eq.setCodigo(txtCodigo.getText().trim());
        eq.setMarca(txtMarca.getText().trim());
        eq.setModelo(txtModelo.getText().trim());
        eq.setNumeroSerie(txtProblema.getText().trim());
        eq.setTipoEquipo((String) cbTipo.getSelectedItem());
        eq.setEstado(cbEstado.getSelectedIndex() == 0);
        eq.setFechaIngreso(java.time.LocalDate.now());
        int idx = cbCliente.getSelectedIndex();
        eq.setIdCliente(Integer.parseInt(clientes.get(idx).getId()));
        if (daoE.insertar(eq)) {
            JOptionPane.showMessageDialog(this, "Equipo registrado.");
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar. ¿Código duplicado?");
        }
    }

    private void limpiar() {
        txtMarca.setText(""); txtModelo.setText("");
        txtProblema.setText(""); cbTipo.setSelectedIndex(0);
        cbEstado.setSelectedIndex(0); cbCliente.setSelectedIndex(0);
        asignarSiguienteCodigo();
        txtMarca.requestFocus();
    }

    private void asignarSiguienteCodigo() {
        txtCodigo.setText(daoE.generarSiguienteCodigo());
    }
}
