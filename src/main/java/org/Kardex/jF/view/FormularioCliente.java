package org.Kardex.jF.view;

import javax.swing.*;
import java.awt.*;
import org.Kardex.jF.bean.entity.Cliente;
import org.Kardex.jF.model.ClienteModel;

public class FormularioCliente extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtCodigo      = new JTextField();
    private JTextField txtNombre      = new JTextField();
    private JTextField txtApellido    = new JTextField();
    private JTextField txtTelefono    = new JTextField();
    private JTextField txtCorreo      = new JTextField();
    private JTextField txtDireccion   = new JTextField();
    private JTextField txtRuc         = new JTextField();
    private JComboBox<String> cbTipo  = new JComboBox<>(new String[]{"Minorista", "Mayorista"});

    private final ClienteModel dao = new ClienteModel();

    public FormularioCliente(JFrame parent) {
        super(parent, "Registrar Cliente", true);
        setSize(420, 420);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout(0, 10));
        add(crearPanel(), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);
        setIconImage(new ImageIcon("image.png").getImage());
        asignarSiguienteCodigo();
    }

    private JPanel crearPanel() {
        JPanel p = new JPanel(new GridLayout(8, 2, 10, 8));
        p.add(new JLabel("Código *:")); p.add(txtCodigo);
        p.add(new JLabel("Tipo:")); p.add(cbTipo);
        p.add(new JLabel("RUC/DNI:")); p.add(txtRuc);
        p.add(new JLabel("Nombres *:")); p.add(txtNombre);
        p.add(new JLabel("Apellidos *:")); p.add(txtApellido);
        p.add(new JLabel("Teléfono:")); p.add(txtTelefono);
        p.add(new JLabel("Correo:")); p.add(txtCorreo);
        p.add(new JLabel("Dirección:")); p.add(txtDireccion);
        txtCodigo.setEditable(false);
        return p;
    }

    private JPanel crearBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        p.setBackground(new Color(245, 245, 245));
        JButton btnGuardar   = new JButton("Guardar");
        JButton btnCancelar  = new JButton("Limpiar");
        btnGuardar .addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> limpiar());
        p.add(btnCancelar);
        p.add(btnGuardar);
        return p;
    }

    private void guardar() {
        if (txtNombre.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y apellido son obligatorios.");
            return;
        }
        if (!txtCorreo.getText().trim().isEmpty()
                && !txtCorreo.getText().trim().matches("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Ingrese un correo válido.");
            return;
        }
        if (txtCodigo.getText().trim().isEmpty()) asignarSiguienteCodigo();
        Cliente c = new Cliente();
        c.setCodigo(txtCodigo.getText().trim());
        c.setNombre(txtNombre.getText().trim());
        c.setApellido(txtApellido.getText().trim());
        if (!txtTelefono.getText().trim().isEmpty() && !txtTelefono.getText().trim().matches("\\d{6,15}")) {
            JOptionPane.showMessageDialog(this, "El teléfono debe contener solo números (6 a 15 dígitos)."); return;
        }
        if (!txtTelefono.getText().trim().isEmpty()) c.setTelefono(Long.parseLong(txtTelefono.getText().trim()));
        c.setCorreo(txtCorreo.getText().trim());
        c.setDireccion(txtDireccion.getText().trim());
        c.setTipoCliente((String) cbTipo.getSelectedItem());
        try {
            if (!txtRuc.getText().trim().isEmpty())
                c.setRUC(Long.parseLong(txtRuc.getText().trim()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "RUC inválido."); return;
        }
        if (dao.insertar(c)) {
            JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar. ¿El código ya existe?");
        }
    }

    private void limpiar() {
        txtNombre.setText(""); txtApellido.setText("");
        txtTelefono.setText(""); txtCorreo.setText(""); txtDireccion.setText("");
        txtRuc.setText(""); cbTipo.setSelectedIndex(0);
        asignarSiguienteCodigo();
        txtNombre.requestFocus();
    }

    private void asignarSiguienteCodigo() {
        txtCodigo.setText(dao.generarSiguienteCodigo());
    }
}
