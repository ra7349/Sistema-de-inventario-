package org.golocentro.view;

import javax.swing.*;
import java.awt.*;
import org.golocentro.bean.entity.Cliente;
import org.golocentro.model.ClienteModel;

public class ModificarClienteView extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtCodigo    = new JTextField();
    private JTextField txtNombre    = new JTextField();
    private JTextField txtApellido  = new JTextField();
    private JTextField txtTelefono  = new JTextField();
    private JTextField txtCorreo    = new JTextField();
    private JTextField txtDireccion = new JTextField();
    private JTextField txtRuc       = new JTextField();
    private JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Minorista", "Mayorista"});

    private final ClienteModel dao = new ClienteModel();
    private String idCliente = null;

    public ModificarClienteView(JFrame parent) {
        super(parent, "Modificar Cliente", true);
        setSize(430, 440);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        add(crearPanel(), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);
        setIconImage(new ImageIcon("image.png").getImage());
    }

    private JPanel crearPanel() {
        JPanel p = new JPanel(new GridLayout(8, 2, 10, 8));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        JPanel panelCodigo = new JPanel(new BorderLayout(5, 0));
        JButton btnBuscar  = new JButton("Buscar");
        panelCodigo.add(txtCodigo, BorderLayout.CENTER);
        panelCodigo.add(btnBuscar, BorderLayout.EAST);
        btnBuscar.addActionListener(e -> buscar());

        p.add(new JLabel("Código:")); p.add(panelCodigo);
        p.add(new JLabel("Nombres *:")); p.add(txtNombre);
        p.add(new JLabel("Apellidos *:")); p.add(txtApellido);
        p.add(new JLabel("Teléfono:")); p.add(txtTelefono);
        p.add(new JLabel("Correo:")); p.add(txtCorreo);
        p.add(new JLabel("Dirección:")); p.add(txtDireccion);
        p.add(new JLabel("RUC/DNI:")); p.add(txtRuc);
        p.add(new JLabel("Tipo:")); p.add(cbTipo);
        return p;
    }

    private JPanel crearBotones() {
        JPanel p = new JPanel(new GridLayout(1, 2, 8, 0));
        p.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        JButton btnGuardar  = new JButton("Guardar cambios");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar .addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> limpiar());
        p.add(btnCancelar);
        p.add(btnGuardar);
        return p;
    }

    private void buscar() {
        String cod = txtCodigo.getText().trim();
        if (cod.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese un código."); return; }
        Cliente c = dao.buscarPorCodigo(cod);
        if (c == null) { JOptionPane.showMessageDialog(this, "Cliente no encontrado."); return; }
        idCliente = c.getId();
        txtNombre   .setText(c.getNombre());
        txtApellido .setText(c.getApellido());
        txtTelefono .setText(c.getTelefono() != null ? String.valueOf(c.getTelefono()) : "");
        txtCorreo   .setText(c.getCorreo()   != null ? c.getCorreo()   : "");
        txtDireccion.setText(c.getDireccion()!= null ? c.getDireccion(): "");
        txtRuc      .setText(c.getRUC()      != null ? String.valueOf(c.getRUC()) : "");
        cbTipo.setSelectedItem(c.getTipoCliente() != null ? c.getTipoCliente() : "Minorista");
    }

    private void guardar() {
        if (idCliente == null) { JOptionPane.showMessageDialog(this, "Primero busque un cliente."); return; }
        if (txtNombre.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Nombre y apellido son obligatorios."); return; }
        if (!txtTelefono.getText().trim().isEmpty() && !txtTelefono.getText().trim().matches("\\d{6,15}")) { JOptionPane.showMessageDialog(this, "El teléfono debe contener solo números (6 a 15 dígitos)."); return; }
        if (!txtCorreo.getText().trim().isEmpty() && !txtCorreo.getText().trim().matches("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) { JOptionPane.showMessageDialog(this, "Ingrese un correo válido."); return; }
        Cliente c = new Cliente();
        c.setId(idCliente);
        c.setCodigo(txtCodigo.getText().trim());
        c.setNombre(txtNombre.getText().trim());
        c.setApellido(txtApellido.getText().trim());
        if (!txtTelefono.getText().trim().isEmpty()) c.setTelefono(Long.parseLong(txtTelefono.getText().trim()));
        c.setCorreo(txtCorreo.getText().trim());
        c.setDireccion(txtDireccion.getText().trim());
        c.setTipoCliente((String) cbTipo.getSelectedItem());
        try {
            if (!txtRuc.getText().trim().isEmpty())
                c.setRUC(Long.parseLong(txtRuc.getText().trim()));
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this,"RUC inválido."); return; }
        if (dao.actualizar(c)) {
            JOptionPane.showMessageDialog(this, "Cliente actualizado.");
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar.");
        }
    }

    private void limpiar() {
        idCliente = null;
        txtCodigo.setText(""); txtNombre.setText(""); txtApellido.setText("");
        txtTelefono.setText(""); txtCorreo.setText(""); txtDireccion.setText("");
        txtRuc.setText(""); cbTipo.setSelectedIndex(0);
        txtCodigo.requestFocus();
    }
}
