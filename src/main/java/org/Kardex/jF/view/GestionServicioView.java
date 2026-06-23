package org.Kardex.jF.view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.Kardex.jF.bean.entity.Servicio;
import org.Kardex.jF.model.ServiciosModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GestionServicioView extends JFrame{

	private static final Color COLOR_PRIMARIO = new Color(38, 70, 83);
	private static final Color COLOR_ACENTO = new Color(42, 157, 143);
	private static final Color COLOR_FONDO = new Color(245, 247, 250);
	private static final Color COLOR_PANEL = Color.WHITE;
	private static final Color COLOR_TEXTO = new Color(33, 37, 41);
	private static final Font FUENTE_BASE = new Font("Segoe UI", Font.PLAIN, 13);

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
	        setSize(760, 620);
	        setMinimumSize(new Dimension(700, 560));
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLayout(new BorderLayout());
	        getContentPane().setBackground(COLOR_FONDO);
	 
	        // ── Panel principal con margen ──────────────────────────────────
	        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 18));
	        panelPrincipal.setBackground(COLOR_FONDO);
	        panelPrincipal.setBorder(new EmptyBorder(24, 28, 24, 28));
	 
	        // ── Título ──────────────────────────────────────────────────────
	        JPanel panelEncabezado = new JPanel(new BorderLayout(8, 4));
	        panelEncabezado.setOpaque(false);
	        JLabel titulo = new JLabel("Gestión de Servicios", SwingConstants.LEFT);
	        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
	        titulo.setForeground(COLOR_PRIMARIO);
	        JLabel subtitulo = new JLabel("Administra el catálogo, precios y disponibilidad de servicios");
	        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
	        subtitulo.setForeground(new Color(108, 117, 125));
	        panelEncabezado.add(titulo, BorderLayout.NORTH);
	        panelEncabezado.add(subtitulo, BorderLayout.SOUTH);
	        panelPrincipal.add(panelEncabezado, BorderLayout.NORTH);
	 
	        // ── Formulario (GridLayout 5 filas x 2 columnas) ────────────────
	        JPanel panelForm = new JPanel(new GridBagLayout());
	        panelForm.setBackground(COLOR_PANEL);
	        panelForm.setBorder(BorderFactory.createCompoundBorder(
	        		new LineBorder(new Color(226, 232, 240), 1, true),
	        		new EmptyBorder(20, 22, 20, 22)));
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(0, 0, 14, 16);
	        gbc.anchor = GridBagConstraints.WEST;
	        gbc.fill = GridBagConstraints.HORIZONTAL;
	        gbc.weightx = 1;
	 
	        agregarCampo(panelForm, gbc, 0, 0, "Código", campoId);
	        agregarCampo(panelForm, gbc, 0, 1, "Descripción", campoDesc);
	        agregarCampo(panelForm, gbc, 1, 0, "Precio", campoPrecio);
	        agregarCampo(panelForm, gbc, 1, 1, "Estado", comboEstado);
	 
	        // ── Botones (2 filas) ───────────────────────────────────────────
	        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
	        panelBotones.setOpaque(false);
	        estilizarBoton(btnNuevo, new Color(108, 117, 125));
	        estilizarBoton(btnGuardar, COLOR_ACENTO);
	        estilizarBoton(btnEditar, new Color(33, 150, 243));
	        estilizarBoton(btnEliminar, new Color(220, 53, 69));
	        estilizarBoton(btnBuscar, COLOR_PRIMARIO);

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
	        tabla.setFont(FUENTE_BASE);
	        tabla.setRowHeight(34);
	        tabla.setShowVerticalLines(false);
	        tabla.setGridColor(new Color(233, 236, 239));
	        tabla.setSelectionBackground(new Color(209, 236, 241));
	        tabla.setSelectionForeground(COLOR_TEXTO);
	        JTableHeader header = tabla.getTableHeader();
	        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
	        header.setBackground(COLOR_PRIMARIO);
	        header.setForeground(Color.WHITE);
	        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 38));
	        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
	        centrado.setHorizontalAlignment(SwingConstants.CENTER);
	        tabla.getColumnModel().getColumn(0).setCellRenderer(centrado);
	        tabla.getColumnModel().getColumn(1).setCellRenderer(centrado);
	        tabla.getColumnModel().getColumn(3).setCellRenderer(centrado);
	        tabla.getColumnModel().getColumn(4).setCellRenderer(centrado);
	        JScrollPane scrollTabla = new JScrollPane(tabla);
	        scrollTabla.setPreferredSize(new Dimension(680, 240));
	        scrollTabla.setBorder(BorderFactory.createCompoundBorder(
	        		new LineBorder(new Color(226, 232, 240), 1, true),
	        		new EmptyBorder(0, 0, 0, 0)));
	 
	        // ── Ensamblado ──────────────────────────────────────────────────
	        JPanel panelCentro = new JPanel(new BorderLayout(10, 18));
	        panelCentro.setOpaque(false);
	        JLabel tituloTabla = new JLabel("Servicios registrados");
	        tituloTabla.setFont(new Font("Segoe UI", Font.BOLD, 15));
	        tituloTabla.setForeground(COLOR_PRIMARIO);
	        JPanel panelTabla = new JPanel(new BorderLayout(0, 10));
	        panelTabla.setOpaque(false);
	        panelTabla.add(tituloTabla, BorderLayout.NORTH);
	        panelTabla.add(scrollTabla, BorderLayout.CENTER);
	        panelCentro.add(panelForm,     BorderLayout.NORTH);
	        panelCentro.add(panelBotones,  BorderLayout.CENTER);
	        panelCentro.add(panelTabla,   BorderLayout.SOUTH);
	 
	        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
	        add(panelPrincipal);
	 
	        // ── Listeners ───────────────────────────────────────────────────
	        btnNuevo.addActionListener(e -> nuevo());
	        btnGuardar.addActionListener(e -> guardar());
	        btnEditar.addActionListener(e -> editar());
	        btnEliminar.addActionListener(e -> eliminar());
	        btnBuscar.addActionListener(e -> buscar());
	        
	        cargarDatos();
	        asignarSiguienteCodigo();
	        
	        setLocationRelativeTo(null);
	        setVisible(true);
	    }
	    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, int columna, String texto, JComponent campo) {
	    	gbc.gridx = columna;
	    	gbc.gridy = fila * 2;
	    	gbc.insets = new Insets(0, 0, 6, columna == 0 ? 18 : 0);
	    	JLabel etiqueta = new JLabel(texto);
	    	etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 12));
	    	etiqueta.setForeground(COLOR_PRIMARIO);
	    	panel.add(etiqueta, gbc);

	    	gbc.gridy = fila * 2 + 1;
	    	gbc.insets = new Insets(0, 0, 16, columna == 0 ? 18 : 0);
	    	campo.setFont(FUENTE_BASE);
	    	campo.setPreferredSize(new Dimension(220, 34));
	    	campo.setBorder(BorderFactory.createCompoundBorder(
	    			new LineBorder(new Color(206, 212, 218), 1, true),
	    			new EmptyBorder(6, 10, 6, 10)));
	    	panel.add(campo, gbc);
	    }

	    private void estilizarBoton(JButton boton, Color color) {
	    	boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
	    	boton.setForeground(Color.WHITE);
	    	boton.setBackground(color);
	    	boton.setFocusPainted(false);
	    	boton.setBorder(new EmptyBorder(10, 16, 10, 16));
	    	boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    	boton.addMouseListener(new MouseAdapter() {
	    		@Override
	    		public void mouseEntered(MouseEvent e) {
	    			boton.setBackground(color.brighter());
	    		}

	    		@Override
	    		public void mouseExited(MouseEvent e) {
	    			boton.setBackground(color);
	    		}
	    	});
	    }

	    private void nuevo()    { 
	        campoDesc.setText("");
	        campoPrecio.setText("");
	        comboEstado.setSelectedIndex(0);
	        idServicio = null;
	        asignarSiguienteCodigo();
	    }
	    
	    private void guardar()  {
	        if (campoId.getText().trim().isEmpty()) asignarSiguienteCodigo();
	    	Servicio s = new Servicio();

	    	s.setCodigo(campoId.getText().trim());
	    	s.setDescripcion(campoDesc.getText().trim());
	    	s.setPrecio(Double.parseDouble(campoPrecio.getText().trim()));
	    	s.setEstado((String)comboEstado.getSelectedItem());

	    	if (dao.insertar(s)) {
	    	    JOptionPane.showMessageDialog(this, "Servicio registrado correctamente",
	    	            "Éxito", JOptionPane.INFORMATION_MESSAGE);
	            cargarDatos();
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

	    private void asignarSiguienteCodigo() {
	        campoId.setText(dao.generarSiguienteCodigo());
	    }
}
