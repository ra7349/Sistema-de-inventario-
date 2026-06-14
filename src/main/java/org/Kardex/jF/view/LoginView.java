package org.Kardex.jF.view;

import javax.swing.*;

public class LoginView extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField userField;
    private JPasswordField passField;

    public LoginView() {
        setTitle("Iniciar Sesión");
        setSize(320, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(25, 25, 80, 25);
        add(lblUsuario);

        userField = new JTextField();
        userField.setBounds(110, 25, 150, 25);
        add(userField);

        JLabel lblClave = new JLabel("Contraseña:");
        lblClave.setBounds(25, 65, 80, 25);
        add(lblClave);

        passField = new JPasswordField();
        passField.setBounds(110, 65, 150, 25);
        add(passField);

        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(105, 110, 110, 30);
        add(btnIngresar);

        btnIngresar.addActionListener(e -> validarCredenciales());
    }

    private void validarCredenciales() {
        String inputUsuario = userField.getText().trim();
        String inputClave = new String(passField.getPassword());

        if (inputUsuario.equals("admin") && inputClave.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Bienvenido al sistema");
            new MarcoPrincipalView().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            passField.setText("");
        }
    }
}