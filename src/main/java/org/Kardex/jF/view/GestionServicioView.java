package org.Kardex.jF.view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.Kardex.jF.bean.entity.Servicio;
import org.Kardex.jF.model.ServiciosModel;

import java.awt.*;

public class GestionServicioView extends JFrame{

	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	   private JTextField campoId        = new JTextField();
	    private JTextField campoDesc      = new JTextField();
	    private JTextField campoPrecio    = new JTextField();
	    private JComboBox<String> comboEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
	    private ServiciosModel dao = new ServiciosModel(); 
	    private Integer idServicio = null;
	    private DefaultTableModel modelo;
	 
	    private JButton btnNuevo    = new JButton("Nuevo");
	    private JButton btnGuardar  = new JButton("Guardar");
	    private JButton btnEditar   = new JButton("Editar");
	    private JButton btnEliminar = new JButton("Eliminar");
	    private JButton btnBuscar   = new JButton("Buscar");
	    private JTable tabla;
	    
	    public GestionServicioView() {
	        setTitle("Gestión de Servicios");
	        setSize(520, 550);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLayout(new BorderLayout(10, 10));
	 
	        // ── Panel principal con margen ──────────────────────────────────
	        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
	        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
	 
	        // ── Título ──────────────────────────────────────────────────────
	        JLabel titulo = new JLabel("GESTIÓN DE SERVICIOS", SwingConstants.CENTER);
	        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
	        panelPrincipal.add(titulo, BorderLayout.NORTH);
	 
	        // ── Formulario (GridLayout 5 filas x 2 columnas) ────────────────
	        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 12));
	        panelForm.setBorder(new EmptyBorder(10, 0, 10, 0));
	 
	        panelForm.add(new JLabel("Código:"));
	        panelForm.add(campoId);
	 
	        panelForm.add(new JLabel("Descripción:"));
	        panelForm.add(campoDesc);
	 
	        panelForm.add(new JLabel("Precio:"));
	        panelForm.add(campoPrecio);
	 
	        panelForm.add(new JLabel("Estado:"));
	        panelForm.add(comboEstado);
	 
	        // ── Botones (2 filas) ───────────────────────────────────────────
	        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));

	        panelBotones.add(btnNuevo);
	        panelBotones.add(btnGuardar);
	        panelBotones.add(btnEditar);
	        panelBotones.add(btnEliminar);
	        panelBotones.add(btnBuscar);// celda vacía para completar la grilla
	 
	        // ── Tabla ───────────────────────────────────────────────────────
	        
	        modelo = new DefaultTableModel(
	            new String[]{"ID", "Código", "Servicio", "Precio", "Estado"}, 0) {
	            @Override
	            public boolean isCellEditable(int r, int c) { return false; }
	        };
	        tabla = new JTable(modelo);
	        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        JScrollPane scrollTabla = new JScrollPane(tabla);
	        scrollTabla.setPreferredSize(new Dimension(480, 150));
	 
	        // ── Ensamblado ──────────────────────────────────────────────────
	        JPanel panelCentro = new JPanel(new BorderLayout(10, 20));
	        panelCentro.add(panelForm,     BorderLayout.NORTH);
	        panelCentro.add(panelBotones,  BorderLayout.CENTER);
	        panelCentro.add(scrollTabla,   BorderLayout.SOUTH);
	 
	        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
	        add(panelPrincipal);
	 
	        // ── Listeners ───────────────────────────────────────────────────
	        btnNuevo.addActionListener(e -> nuevo());
	        btnGuardar.addActionListener(e -> guardar());
	        btnEditar.addActionListener(e -> editar());
	        btnEliminar.addActionListener(e -> eliminar());
	        btnBuscar.addActionListener(e -> buscar());
	        
	        cargarDatos();
	        
	        setLocationRelativeTo(null);
	        setVisible(true);
	    }
	    private void nuevo()    { 
	        campoId.setText("");
	        campoDesc.setText("");
	        campoPrecio.setText("");
	        comboEstado.setSelectedIndex(0);
	        idServicio = null;
	    }
	    
	    private void guardar()  {
	    	Servicio s = new Servicio();

	    	s.setCodigo(campoId.getText().trim());
	    	s.setDescripcion(campoDesc.getText().trim());
	    	s.setPrecio(Double.parseDouble(campoPrecio.getText().trim()));
	    	s.setEstado((String)comboEstado.getSelectedItem());

	    	if (dao.insertar(s)) {
	    	    JOptionPane.showMessageDialog(this, "Servicio registrado correctamente",
	    	            "Éxito", JOptionPane.INFORMATION_MESSAGE);
	    	    nuevo();
	    	} else {
	    	    JOptionPane.showMessageDialog(this, "Error al registrar servicio",
	    	            "Error", JOptionPane.ERROR_MESSAGE);
	    }
	    }
	    
	    private void editar()   {
	    	if (idServicio == null) {
	    	    JOptionPane.showMessageDialog(this, "Primero busque un servicio.");
	    	    return;
	    	}

	    	Servicio s = new Servicio();
	    	s.setIdServicio(idServicio);
	    	s.setCodigo(campoId.getText().trim());
	    	s.setDescripcion(campoDesc.getText().trim());

	    	try {
	    	    if (!campoPrecio.getText().trim().isEmpty())
	    	        s.setPrecio(Double.parseDouble(campoPrecio.getText().trim()));
	    	} catch (NumberFormatException ex) {
	    	    JOptionPane.showMessageDialog(this, "Precio inválido.");
	    	    return;
	    	}

	    	s.setEstado((String) comboEstado.getSelectedItem());

	    	if (dao.actualizar(s)) {
	    	    JOptionPane.showMessageDialog(this, "Servicio actualizado.");
	    	    nuevo();
	    	} else {
	    	    JOptionPane.showMessageDialog(this, "Error al actualizar.");
	    	}
	    	
	    	cargarDatos();
	    }
	    
	    private void eliminar() {
	    	int fila = tabla.getSelectedRow();
	    	if (fila == -1) {
	    	    JOptionPane.showMessageDialog(this, "Seleccione un servicio");
	    	    return;
	    	}

	    	int idServicio = Integer.parseInt(modelo.getValueAt(fila, 0).toString());

	    	int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el servicio seleccionado?",
	    	        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

	    	if (respuesta == JOptionPane.YES_OPTION) {
	    	    dao.eliminar(idServicio);
	    	    cargarDatos();
	    	    JOptionPane.showMessageDialog(this, "Servicio eliminado correctamente");
	    	}
	    }
	    private void cargarDatos() {
	        modelo.setRowCount(0);
	        for (Servicio s : dao.listar()) {
	            modelo.addRow(new Object[]{
	                s.getIdServicio(),
	                s.getCodigo(),
	                s.getDescripcion(),
	                s.getPrecio(),
	                s.getEstado()
	            });
	        }
	    }
	    
	    private void buscar()   {
	    	Servicio s = dao.buscarPorCodigo(campoId.getText().trim());
	    	if (s == null) {
	    	    JOptionPane.showMessageDialog(this, "Servicio no encontrado");
	    	    return;
	    	}
	    	idServicio = s.getIdServicio();
	    	campoId.setText(s.getCodigo());
	    	campoDesc.setText(s.getDescripcion());
	    	campoPrecio.setText(String.valueOf(s.getPrecio()));
	    	comboEstado.setSelectedItem(s.getEstado());
	    }
}
