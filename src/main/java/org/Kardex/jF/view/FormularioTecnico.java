package org.Kardex.jF.view;

import javax.swing.*;
import java.awt.*;
import org.Kardex.jF.bean.entity.Tecnico;
import org.Kardex.jF.model.TecnicoModel;

public class FormularioTecnico extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtCodigo       = new JTextField();
    private JTextField txtNombre       = new JTextField();
    private JTextField txtApellido     = new JTextField();
    private JTextField txtTelefono     = new JTextField();
    private JTextField txtCorreo       = new JTextField();
    private JTextField txtEspecialidad = new JTextField();

    private final TecnicoModel dao = new TecnicoModel();

    public FormularioTecnico(JFrame parent) {
        super(parent, "Registrar Técnico", true);
        setSize(400, 340);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(UiStyle.BACKGROUND);
        setLayout(new BorderLayout(0, 10));
        add(crearPanel(), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanel() {
        JPanel p = UiStyle.cardPanel(new GridLayout(6, 2, 10, 8));
        p.add(UiStyle.label("Código *:")); p.add(txtCodigo);
        p.add(UiStyle.label("Nombres *:")); p.add(txtNombre);
        p.add(UiStyle.label("Apellidos *:")); p.add(txtApellido);
        p.add(UiStyle.label("Teléfono:")); p.add(txtTelefono);
        p.add(UiStyle.label("Correo:")); p.add(txtCorreo);
        p.add(UiStyle.label("Especialidad:")); p.add(txtEspecialidad);
        for (JTextField field : new JTextField[]{txtCodigo, txtNombre, txtApellido, txtTelefono, txtCorreo, txtEspecialidad}) {
            UiStyle.styleField(field);
        }
        return p;
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
        if (txtCodigo.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Código y Nombre son obligatorios."); return;
        }
        Tecnico t = new Tecnico();
        t.setCodigo(txtCodigo.getText().trim());
        t.setNombre(txtNombre.getText().trim());
        t.setApellido(txtApellido.getText().trim());
        try {
            if (!txtTelefono.getText().trim().isEmpty())
                t.setTelefono(Long.parseLong(txtTelefono.getText().trim()));
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this,"Teléfono inválido."); return; }
        t.setCorreo(txtCorreo.getText().trim());
        t.setEspecialidad(txtEspecialidad.getText().trim());
        if (dao.insertar(t)) {
            JOptionPane.showMessageDialog(this, "Técnico registrado.");
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar. ¿Código duplicado?");
        }
    }

    private void limpiar() {
        txtCodigo.setText(""); txtNombre.setText(""); txtApellido.setText("");
        txtTelefono.setText(""); txtCorreo.setText(""); txtEspecialidad.setText("");
        txtCodigo.requestFocus();
    }
}
