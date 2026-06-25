package org.Kardex.jF.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.Kardex.jF.bean.entity.Proveedor;
import org.Kardex.jF.model.ProveedorModel;

public class ProveedorListarView extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String[] ESTADOS_PROVEEDOR = {"Activo", "Inactivo"};

    private final ProveedorModel dao = new ProveedorModel();
    private final DefaultTableModel modelo;
    private final JTable tabla;
    private final JTextField txtId = new JTextField();
    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtRazonSocial = new JTextField();
    private final JTextField txtRuc = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtCorreo = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JTextField txtContacto = new JTextField();
    private final JComboBox<String> cbEstado = new JComboBox<>(ESTADOS_PROVEEDOR);
    private final JTextField txtFiltro = new JTextField();

    public ProveedorListarView() {
        setTitle("Proveedores Golocentro");
        setSize(1120, 620);
        setMinimumSize(new Dimension(980, 560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout(10, 10));
        setIconImage(new ImageIcon("image.png").getImage());

        modelo = new DefaultTableModel(
            new String[]{"ID", "Código", "Razón social", "RUC", "Teléfono", "Correo", "Dirección", "Contacto", "Estado"}, 0) {
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
        contenedor.setBorder(BorderFactory.createTitledBorder("Registro de proveedor"));

        JPanel formulario = new JPanel(new GridLayout(3, 6, 8, 8));
        formulario.setOpaque(false);
        txtId.setEditable(false);
        txtCodigo.setEditable(false);

        formulario.add(new JLabel("ID:")); formulario.add(txtId);
        formulario.add(new JLabel("Código *:")); formulario.add(txtCodigo);
        formulario.add(new JLabel("Estado *:")); formulario.add(cbEstado);
        formulario.add(new JLabel("Razón social *:")); formulario.add(txtRazonSocial);
        formulario.add(new JLabel("RUC *:")); formulario.add(txtRuc);
        formulario.add(new JLabel("Teléfono:")); formulario.add(txtTelefono);
        formulario.add(new JLabel("Correo:")); formulario.add(txtCorreo);
        formulario.add(new JLabel("Dirección:")); formulario.add(txtDireccion);
        formulario.add(new JLabel("Contacto:")); formulario.add(txtContacto);


        JPanel busqueda = new JPanel(new BorderLayout(8, 8));
        busqueda.setOpaque(false);
        busqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda por código, razón social, RUC, teléfono, correo, dirección, contacto o estado"));
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
        for (Proveedor p : dao.buscar(filtro)) {
            modelo.addRow(new Object[]{
                p.getId(), p.getCodigo(), p.getRazonSocial(), p.getRuc(),
                p.getTelefono(), p.getCorreo(), p.getDireccion(), p.getContacto(), p.getEstado()
            });
        }
    }

    private void guardar() {
        Proveedor p = leerFormulario(false);
        if (p == null) return;
        if (dao.insertar(p)) {
            JOptionPane.showMessageDialog(this, "Proveedor registrado correctamente.");
            limpiarTodo();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar. Verifique si el código ya existe.");
        }
    }

    private void actualizar() {
        Proveedor p = leerFormulario(true);
        if (p == null) return;
        if (dao.actualizar(p)) {
            JOptionPane.showMessageDialog(this, "Proveedor actualizado correctamente.");
            limpiarTodo();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el proveedor.");
        }
    }

    private void eliminar() {
        if (txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione un proveedor."); return; }
        int id = Integer.parseInt(txtId.getText().trim());
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar proveedor seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (dao.eliminar(id)) { JOptionPane.showMessageDialog(this, "Proveedor eliminado."); limpiarTodo(); }
            else JOptionPane.showMessageDialog(this, "No se pudo eliminar el proveedor.");
        }
    }

    private Proveedor leerFormulario(boolean requiereId) {
        String codigo = txtCodigo.getText().trim();
        String razonSocial = txtRazonSocial.getText().trim();
        String ruc = txtRuc.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String contacto = txtContacto.getText().trim();
        String estado = String.valueOf(cbEstado.getSelectedItem()).trim();

        if (requiereId && txtId.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione un proveedor para actualizar."); return null; }
        if (codigo.isEmpty()) { JOptionPane.showMessageDialog(this, "El código es obligatorio."); return null; }
        if (razonSocial.isEmpty()) { JOptionPane.showMessageDialog(this, "La razón social es obligatoria."); return null; }
        if (ruc.isEmpty()) { JOptionPane.showMessageDialog(this, "El RUC es obligatorio."); return null; }
        if (!ruc.matches("\\d{8,15}")) { JOptionPane.showMessageDialog(this, "El RUC debe contener solo números (8 a 15 dígitos)."); return null; }
        if (!telefono.isEmpty() && !telefono.matches("\\d{6,15}")) { JOptionPane.showMessageDialog(this, "El teléfono debe contener solo números (6 a 15 dígitos)."); return null; }
        if (!correo.isEmpty() && !correo.matches("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) { JOptionPane.showMessageDialog(this, "Ingrese un correo válido."); return null; }
        if (!"Activo".equals(estado) && !"Inactivo".equals(estado)) { JOptionPane.showMessageDialog(this, "Seleccione un estado válido."); return null; }

        Proveedor p = new Proveedor();
        if (requiereId) p.setId(txtId.getText().trim());
        p.setCodigo(codigo);
        p.setRazonSocial(razonSocial);
        p.setRuc(Long.parseLong(ruc));
        p.setTelefono(telefono.isEmpty() ? null : Long.parseLong(telefono));
        p.setCorreo(correo);
        p.setDireccion(direccion);
        p.setContacto(contacto);
        p.setEstado(estado);
        return p;
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;
        txtId.setText(valor(fila, 0));
        txtCodigo.setText(valor(fila, 1));
        txtRazonSocial.setText(valor(fila, 2));
        txtRuc.setText(valor(fila, 3));
        txtTelefono.setText(valor(fila, 4));
        txtCorreo.setText(valor(fila, 5));
        txtDireccion.setText(valor(fila, 6));
        txtContacto.setText(valor(fila, 7));
        cbEstado.setSelectedItem(normalizarEstado(valor(fila, 8)));
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
        txtRazonSocial.setText("");
        txtRuc.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtContacto.setText("");
        cbEstado.setSelectedIndex(0);
        tabla.clearSelection();
        txtRazonSocial.requestFocus();
    }

    private String normalizarEstado(String estado) {
        if (estado == null) return "Activo";
        if (estado.equalsIgnoreCase("Inactivo")) return "Inactivo";
        return "Activo";
    }
}
