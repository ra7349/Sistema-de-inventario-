package org.golocentro.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.golocentro.bean.entity.Cliente;
import org.golocentro.model.ClienteModel;

public class ClienteListarView extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String[] TIPOS_CLIENTE = {"Minorista", "Mayorista"};

    private final ClienteModel dao = new ClienteModel();
    private final DefaultTableModel modelo;
    private final JTable tabla;
    private final JTextField txtId = new JTextField();
    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtApellido = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtCorreo = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JComboBox<String> cbTipoCliente = new JComboBox<>(TIPOS_CLIENTE);
    private final JTextField txtFiltro = new JTextField();

    public ClienteListarView() {
        setTitle("Clientes Golocentro");
        setSize(1120, 620);
        setMinimumSize(new Dimension(980, 560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout(10, 10));
        setIconImage(new ImageIcon("image.png").getImage());

        modelo = new DefaultTableModel(
            new String[]{"ID", "Código", "Nombre", "Apellido", "Teléfono", "Correo", "Dirección", "Tipo cliente"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });

        add(crearFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(crearBotonera(), BorderLayout.SOUTH);

        nuevo();
        cargarDatos(null);
    }

    private JPanel crearFormulario() {
        JPanel contenedor = new JPanel(new BorderLayout(8, 8));
        contenedor.setBorder(BorderFactory.createTitledBorder("Registro de cliente"));

        JPanel formulario = new JPanel(new GridLayout(3, 6, 8, 8));
        formulario.setOpaque(false);
        txtId.setEditable(false);
        txtCodigo.setEditable(false);

        formulario.add(new JLabel("ID:")); formulario.add(txtId);
        formulario.add(new JLabel("Código *:")); formulario.add(txtCodigo);
        formulario.add(new JLabel("Tipo cliente *:")); formulario.add(cbTipoCliente);
        formulario.add(new JLabel("Nombre *:")); formulario.add(txtNombre);
        formulario.add(new JLabel("Apellido *:")); formulario.add(txtApellido);
        formulario.add(new JLabel("Teléfono:")); formulario.add(txtTelefono);
        formulario.add(new JLabel("Correo:")); formulario.add(txtCorreo);
        formulario.add(new JLabel("Dirección:")); formulario.add(txtDireccion);


        JPanel busqueda = new JPanel(new BorderLayout(8, 8));
        busqueda.setOpaque(false);
        busqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda por código, nombre, apellido, teléfono, correo, dirección o tipo"));
        JButton btnBuscar = new JButton("Buscar");
        busqueda.add(txtFiltro, BorderLayout.CENTER);
        busqueda.add(btnBuscar, BorderLayout.EAST);
        btnBuscar.addActionListener(e -> cargarDatos(txtFiltro.getText()));
        txtFiltro.addActionListener(e -> cargarDatos(txtFiltro.getText()));

        contenedor.add(formulario, BorderLayout.CENTER);
        contenedor.add(busqueda, BorderLayout.SOUTH);
        return contenedor;
    }

    private JPanel crearBotonera() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        p.setBackground(new Color(245, 245, 245));
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");
        btnNuevo.addActionListener(e -> nuevo());
        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> cargarDatos(txtFiltro.getText()));
        btnLimpiar.addActionListener(e -> limpiarTodo());
        p.add(btnNuevo); p.add(btnGuardar); p.add(btnActualizar); p.add(btnEliminar); p.add(btnBuscar); p.add(btnLimpiar);
        return p;
    }

    private void cargarDatos(String filtro) {
        modelo.setRowCount(0);
        for (Cliente c : dao.buscar(filtro)) {
            modelo.addRow(new Object[]{
                c.getId(), c.getCodigo(), c.getNombre(), c.getApellido(),
                c.getTelefono(), c.getCorreo(), c.getDireccion(), c.getTipoCliente()
            });
        }
    }

    private void guardar() {
        Cliente c = leerFormulario(false);
        if (c == null) return;
        if (dao.insertar(c)) {
            JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
            limpiarTodo();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar. Verifique si el código ya existe.");
        }
    }

    private void actualizar() {
        Cliente c = leerFormulario(true);
        if (c == null) return;
        if (dao.actualizar(c)) {
            JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.");
            limpiarTodo();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el cliente.");
        }
    }

    private void eliminar() {
        if (txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione un cliente."); return; }
        int id = Integer.parseInt(txtId.getText().trim());
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar cliente seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (dao.eliminar(id)) { JOptionPane.showMessageDialog(this, "Cliente eliminado."); limpiarTodo(); }
            else JOptionPane.showMessageDialog(this, "No se pudo eliminar. Puede tener ventas asociadas.");
        }
    }

    private Cliente leerFormulario(boolean requiereId) {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String tipoCliente = String.valueOf(cbTipoCliente.getSelectedItem()).trim();

        if (requiereId && txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione un cliente para actualizar."); return null; }
        if (codigo.isEmpty()) { JOptionPane.showMessageDialog(this, "El código es obligatorio."); return null; }
        if (nombre.isEmpty() || apellido.isEmpty()) { JOptionPane.showMessageDialog(this, "Nombre y apellido son obligatorios."); return null; }
        if (!telefono.isEmpty() && !telefono.matches("\\d{6,15}")) { JOptionPane.showMessageDialog(this, "El teléfono debe contener solo números (6 a 15 dígitos)."); return null; }
        if (!correo.isEmpty() && !correo.matches("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) { JOptionPane.showMessageDialog(this, "Ingrese un correo válido."); return null; }
        if (!"Minorista".equals(tipoCliente) && !"Mayorista".equals(tipoCliente)) { JOptionPane.showMessageDialog(this, "Seleccione un tipo de cliente válido."); return null; }

        Cliente c = new Cliente();
        if (requiereId) c.setId(txtId.getText().trim());
        c.setCodigo(codigo);
        c.setNombre(nombre);
        c.setApellido(apellido);
        c.setTelefono(telefono.isEmpty() ? null : Long.parseLong(telefono));
        c.setCorreo(correo);
        c.setDireccion(direccion);
        c.setTipoCliente(tipoCliente);
        c.setRUC(null);
        return c;
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;
        txtId.setText(valor(fila, 0));
        txtCodigo.setText(valor(fila, 1));
        txtNombre.setText(valor(fila, 2));
        txtApellido.setText(valor(fila, 3));
        txtTelefono.setText(valor(fila, 4));
        txtCorreo.setText(valor(fila, 5));
        txtDireccion.setText(valor(fila, 6));
        cbTipoCliente.setSelectedItem(normalizarTipo(valor(fila, 7)));
    }

    private String valor(int fila, int columna) {
        Object valor = modelo.getValueAt(fila, columna);
        return valor == null ? "" : valor.toString();
    }

    private void limpiarTodo() {
        nuevo();
        txtFiltro.setText("");
        cargarDatos(null);
    }

    private void nuevo() {
        txtId.setText("");
        txtCodigo.setText(dao.generarSiguienteCodigo());
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        cbTipoCliente.setSelectedIndex(0);
        tabla.clearSelection();
        txtNombre.requestFocus();
    }

    private String normalizarTipo(String tipo) {
        if (tipo == null) return "Minorista";
        if (tipo.equalsIgnoreCase("Mayorista") || tipo.equalsIgnoreCase("Jurídico") || tipo.equalsIgnoreCase("Empresa")) return "Mayorista";
        return "Minorista";
    }
}
