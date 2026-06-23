package org.Kardex.jF.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.Kardex.jF.bean.entity.Cliente;
import org.Kardex.jF.bean.entity.Equipo;
import org.Kardex.jF.model.ClienteModel;
import org.Kardex.jF.model.EquipoModel;

public class NuevaOrdenView extends JFrame {
	
	private static final long serialVersionUID = 1L;
    
	private JComboBox<Cliente> comboCliente = new JComboBox<>(); 
    private JTextField campoNumOrden       = new JTextField("OS001");
    private JTextField campoEquipo         = new JTextField("");
    private JTextField campoFalla          = new JTextField();
    private JComboBox<String> comboEstado  = new JComboBox<>(new String[]{"Recibido", "Diagnostico", "Reparacion", "Listo", "Entregado"});
    
    // ✅ CORRECCIÓN 1: Cambiados a 'private' para mantener la encapsulación
    private JTextField campoDiagnostico    = new JTextField();
    private JTextField campoServicios      = new JTextField();
    
    private JButton btnGuardar             = new JButton("Guardar");
    private JButton btnActualizarEstado    = new JButton("Actualizar Estado");
    
    // ✅ MEJORA 2: Usar DefaultTableModel para poder agregar/eliminar filas dinámicamente
    private DefaultTableModel modelo;
    private JTable tabla;
    
    private ClienteModel dao = new ClienteModel(); 
    
	public NuevaOrdenView() {
		setTitle("Gestión de Órdenes");
		setSize(800, 550); // ✅ MEJORA 3: Más ancho para que quepa la tabla al lado o abajo
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
		panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

		// ── TÍTULO ──────────────────────────────────────────────────────
		JLabel titulo = new JLabel("GESTIÓN DE ÓRDENES", SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 18));
		panelPrincipal.add(titulo, BorderLayout.NORTH);

		// ── PANEL IZQUIERDO: FORMULARIO ─────────────────────────────────
		// 7 filas, 2 columnas. Ahora sí sumamos 14 elementos exactos.
		JPanel panelForm = new JPanel(new GridLayout(7, 2, 10, 15)); 
		panelForm.setPreferredSize(new Dimension(350, 0)); // Fijamos un ancho cómodo para el formulario

		panelForm.add(new JLabel("N° Orden:"));
		campoNumOrden.setEditable(false); // ✅ MEJORA 4: El número de orden no debería ser editable manualmente
		panelForm.add(campoNumOrden);          

		panelForm.add(new JLabel("Cliente:"));
		panelForm.add(comboCliente);           

		panelForm.add(new JLabel("Equipo:"));
		panelForm.add(campoEquipo);            

		panelForm.add(new JLabel("Problema / Falla:"));
		panelForm.add(campoFalla);          

		panelForm.add(new JLabel("Diagnóstico:"));
		panelForm.add(campoDiagnostico);       

		panelForm.add(new JLabel("Servicios Realizados:"));
		panelForm.add(campoServicios);         

		panelForm.add(new JLabel("Estado:")); // ✅ CORRECCIÓN 5: Faltaba este Label
		panelForm.add(comboEstado);            

		panelPrincipal.add(panelForm, BorderLayout.WEST); // El formulario se queda a la izquierda

		// ── PANEL DERECHO: TABLA DE ÓRDENES ─────────────────────────────
        modelo = new DefaultTableModel(
	            new String[]{"Orden", "Cliente", "Equipo", "Estado"}, 0) {
	            @Override
	            public boolean isCellEditable(int r, int c) { return false; }
	        };
	        tabla = new JTable(modelo);
	        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        JScrollPane scrollTabla = new JScrollPane(tabla);
	        scrollTabla.setPreferredSize(new Dimension(480, 150));	
	        
		// ✅ CORRECCIÓN 6: La tabla va en el CENTER para que se expanda profesionalmente
		panelPrincipal.add(scrollTabla, BorderLayout.CENTER); 

		// ── PANEL INFERIOR: BOTONES ─────────────────────────────────────
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		panelBotones.add(btnActualizarEstado);
		panelBotones.add(btnGuardar);
		panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

		// Cargar datos
		cargarClientesCombo();
		
		comboCliente.addActionListener(e -> {
		    Cliente seleccionado = (Cliente) comboCliente.getSelectedItem();
		    
		    if (seleccionado != null && !seleccionado.getId().isEmpty()) {
		        Equipo equipo = EquipoModel.obtenerEquipoPorCliente(seleccionado.getId());
		        
		        if (equipo != null) {
		            campoEquipo.setText(equipo.getMarca() + " " + equipo.getModelo());
		            campoFalla.setText("");
		        } else {
		            campoEquipo.setText("Sin equipo registrado");
		            campoFalla.setText("");
		        }
		    } else {
		        campoEquipo.setText("");
		        campoFalla.setText("");
		    }
		});
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
}
