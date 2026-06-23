package org.Kardex.jF.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.Kardex.jF.bean.entity.Cliente;
import org.Kardex.jF.bean.entity.Equipo;
import org.Kardex.jF.bean.entity.Servicio;
import org.Kardex.jF.model.ClienteModel;
import org.Kardex.jF.model.ClienteServicioModel;
import org.Kardex.jF.model.OrdenServicioModel;
import org.Kardex.jF.model.EquipoModel;
import org.Kardex.jF.model.ServiciosModel;

public class NuevaOrdenView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JComboBox<Cliente> comboCliente = new JComboBox<>();
    private JTextField campoNumOrden       = new JTextField("OS001");
    private JTextField campoEquipo         = new JTextField("");
    private JTextField campoFalla          = new JTextField();
    private JComboBox<Servicio> comboServicios = new JComboBox<>();
    private JComboBox<String> comboEstado  = new JComboBox<>(new String[]{"Recibido", "Reparacion", "Listo", "Entregado"});

    private JButton btnGuardar             = new JButton("Guardar");
    private JButton btnAplicarServicio     = new JButton("Aplicar");
    private JButton btnActualizarEstado    = new JButton("Actualizar Estado");

    // ✅ MEJORA 2: Usar DefaultTableModel para poder agregar/eliminar filas dinámicamente
    private DefaultTableModel modelo;
    private JTable tabla;

    private ClienteModel dao = new ClienteModel();
    private ServiciosModel serviciosDao = new ServiciosModel();
    private ClienteServicioModel clienteServicioDao = new ClienteServicioModel();
    private OrdenServicioModel ordenServicioDao = new OrdenServicioModel();

	public NuevaOrdenView() {
		setTitle("Gestión de Órdenes");
		setSize(1050, 550); // Más ancho para que la tabla de la derecha se estire en el eje X
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
		panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

		// ── TÍTULO ──────────────────────────────────────────────────────
		JLabel titulo = new JLabel("GESTIÓN DE ÓRDENES", SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 18));
		panelPrincipal.add(titulo, BorderLayout.NORTH);

		// ── PANEL IZQUIERDO: FORMULARIO ─────────────────────────────────
		// 6 filas, 2 columnas: se eliminó diagnóstico y servicios ahora se elige desde catálogo.
		JPanel panelForm = new JPanel(new GridLayout(6, 2, 10, 15));
		panelForm.setPreferredSize(new Dimension(350, 0)); // Fijamos un ancho cómodo para el formulario

		panelForm.add(new JLabel("N° Orden:"));
		campoNumOrden.setEditable(false); // ✅ MEJORA 4: El número de orden no debería ser editable manualmente
		panelForm.add(campoNumOrden);

		panelForm.add(new JLabel("Cliente:"));
		panelForm.add(comboCliente);

		panelForm.add(new JLabel("Equipo:"));
		campoEquipo.setEditable(false);
		panelForm.add(campoEquipo);

		panelForm.add(new JLabel("Problema / Falla:"));
		panelForm.add(campoFalla);

		panelForm.add(new JLabel("Servicio a realizar:"));
		panelForm.add(comboServicios);

		panelForm.add(new JLabel("Estado:")); // ✅ CORRECCIÓN 5: Faltaba este Label
		panelForm.add(comboEstado);

		panelPrincipal.add(panelForm, BorderLayout.WEST); // El formulario se queda a la izquierda

		// ── PANEL DERECHO: TABLA DE ÓRDENES ─────────────────────────────
        modelo = new DefaultTableModel(
	            new String[]{"Orden", "Cliente", "Equipo", "Problema", "Servicio", "Estado"}, 0) {
	            @Override
	            public boolean isCellEditable(int r, int c) { return false; }
	        };
	        tabla = new JTable(modelo);
	        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        tabla.setFillsViewportHeight(true);
	        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	        JScrollPane scrollTabla = new JScrollPane(tabla);
	        scrollTabla.setPreferredSize(new Dimension(650, 150));

		// ✅ CORRECCIÓN 6: La tabla va en el CENTER para que se expanda profesionalmente
		panelPrincipal.add(scrollTabla, BorderLayout.CENTER);

		// ── PANEL INFERIOR: BOTONES ─────────────────────────────────────
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		panelBotones.add(btnActualizarEstado);
		panelBotones.add(btnAplicarServicio);
		panelBotones.add(btnGuardar);
		panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

		// Cargar datos
		cargarClientesCombo();
		cargarServiciosCombo();
		asignarSiguienteNumeroOrden();

		comboCliente.addActionListener(e -> {
		    Cliente seleccionado = (Cliente) comboCliente.getSelectedItem();

		    if (seleccionado != null && !seleccionado.getId().isEmpty()) {
		        Equipo equipo = EquipoModel.obtenerEquipoPorCliente(seleccionado.getId());

		        if (equipo != null) {
		            campoEquipo.setText(equipo.getMarca() + " " + equipo.getModelo());
		            campoFalla.setText(equipo.getNumeroSerie());
		        } else {
		            campoEquipo.setText("Sin equipo registrado");
		            campoFalla.setText("");
		        }
		    } else {
		        campoEquipo.setText("");
		        campoFalla.setText("");
		    }
		});
		btnAplicarServicio.addActionListener(e -> aplicarServicioCliente());
		btnActualizarEstado.addActionListener(e -> actualizarEstadoSeleccionado());
		btnGuardar.addActionListener(e -> agregarOrdenATabla());
		add(panelPrincipal);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void cargarClientesCombo() {
	    comboCliente.removeAllItems();
	    comboCliente.addItem(new Cliente("", "Seleccione un cliente..."));

	    for (Cliente c : dao.listar()) {
	        comboCliente.addItem(c);
	    }
	}

	private void cargarServiciosCombo() {
	    comboServicios.removeAllItems();
	    comboServicios.addItem(new Servicio(null, "", "Seleccione un servicio...", 0.0, "Activo"));

	    for (Servicio s : serviciosDao.listar()) {
	        comboServicios.addItem(s);
	    }
	}

	private void aplicarServicioCliente() {
	    Cliente cliente = (Cliente) comboCliente.getSelectedItem();
	    Servicio servicio = (Servicio) comboServicios.getSelectedItem();

	    if (!validarSeleccion(cliente, servicio)) {
	        return;
	    }

	    boolean guardado = clienteServicioDao.aplicarServicio(
	            cliente,
	            servicio,
	            campoNumOrden.getText(),
	            campoEquipo.getText(),
	            campoFalla.getText(),
	            String.valueOf(comboEstado.getSelectedItem()));

	    if (guardado) {
	        agregarFilaTabla(cliente, servicio);
	        asignarSiguienteNumeroOrden();
	        JOptionPane.showMessageDialog(this, "Servicio aplicado al cliente para facturación.");
	    } else {
	        JOptionPane.showMessageDialog(this, "No se pudo aplicar el servicio al cliente.");
	    }
	}

	private void actualizarEstadoSeleccionado() {
	    int fila = tabla.getSelectedRow();
	    if (fila == -1) {
	        JOptionPane.showMessageDialog(this, "Seleccione una orden de la tabla.");
	        return;
	    }

	    int filaModelo = tabla.convertRowIndexToModel(fila);
	    String numeroOrden = String.valueOf(modelo.getValueAt(filaModelo, 0));
	    String estadoActual = String.valueOf(modelo.getValueAt(filaModelo, 5));
	    String nuevoEstado = String.valueOf(comboEstado.getSelectedItem());

	    if (nuevoEstado.equals(estadoActual)) {
	        JOptionPane.showMessageDialog(this, "La orden ya tiene el estado seleccionado.");
	        return;
	    }

	    if (clienteServicioDao.actualizarEstado(numeroOrden, nuevoEstado)) {
	        modelo.setValueAt(nuevoEstado, filaModelo, 5);
	        JOptionPane.showMessageDialog(this, "Estado actualizado a: " + nuevoEstado);
	    } else {
	        JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado en la base de datos.");
	    }
	}

	private void agregarOrdenATabla() {
	    Cliente cliente = (Cliente) comboCliente.getSelectedItem();
	    Servicio servicio = (Servicio) comboServicios.getSelectedItem();

	    if (!validarSeleccion(cliente, servicio)) {
	        return;
	    }

	    agregarFilaTabla(cliente, servicio);
	}

	private boolean validarSeleccion(Cliente cliente, Servicio servicio) {
	    if (cliente == null || cliente.getId().isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
	        return false;
	    }
	    if (servicio == null || servicio.getIdServicio() == null) {
	        JOptionPane.showMessageDialog(this, "Seleccione un servicio.");
	        return false;
	    }
	    return true;
	}

	private void asignarSiguienteNumeroOrden() {
	    campoNumOrden.setText(ordenServicioDao.generarSiguienteCodigo());
	}

	private void agregarFilaTabla(Cliente cliente, Servicio servicio) {
	    modelo.addRow(new Object[]{
	        campoNumOrden.getText(),
	        cliente,
	        campoEquipo.getText(),
	        campoFalla.getText(),
	        servicio,
	        comboEstado.getSelectedItem()
	    });
	}

}
