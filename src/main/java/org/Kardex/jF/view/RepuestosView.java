package org.Kardex.jF.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class RepuestosView extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField campoCodigo       = new JTextField("RP001");
    private JTextField campoNombre       = new JTextField("SSD 240 GB");
    private JTextField campoMarca        = new JTextField("Kingston");
    private JComboBox<String> comboCategoria = new JComboBox<>(new String[]{"Almacenamiento", "Memoria RAM", "Pantalla", "Batería"});
    private JTextField campoStock        = new JTextField("15");
    private JTextField campoPrecioCompra = new JTextField("120.00");
    private JTextField campoPrecioVenta  = new JTextField("150.00");
    private JComboBox<String> comboEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});

    private JButton btnNuevo    = new JButton("Nuevo");
    private JButton btnGuardar  = new JButton("Guardar");
    private JButton btnEditar   = new JButton("Editar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnBuscar   = new JButton("Buscar");
    private JButton btnLimpiar  = new JButton("Limpiar");

    private JTable tabla;

    public RepuestosView() {
        setTitle("Gestionar Repuestos");
        setSize(580, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "GESTIONAR REPUESTOS",
                        TitledBorder.CENTER, TitledBorder.TOP),
                new EmptyBorder(10, 15, 10, 15)));

        // ── Formulario ────────────────────────────────────────────────
        JPanel panelForm = new JPanel(new GridLayout(8, 2, 10, 8));

        panelForm.add(new JLabel("Código:"));
        panelForm.add(campoCodigo);

        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(campoNombre);

        panelForm.add(new JLabel("Marca:"));
        panelForm.add(campoMarca);

        panelForm.add(new JLabel("Categoría:"));
        panelForm.add(comboCategoria);

        panelForm.add(new JLabel("Stock:"));
        panelForm.add(campoStock);

        panelForm.add(new JLabel("Precio Compra:"));
        panelForm.add(campoPrecioCompra);

        panelForm.add(new JLabel("Precio Venta:"));
        panelForm.add(campoPrecioVenta);

        panelForm.add(new JLabel("Estado:"));
        panelForm.add(comboEstado);

        // ── Botones ───────────────────────────────────────────────────
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnLimpiar);

        // ── Tabla ─────────────────────────────────────────────────────
        String[] columnas = {"Código", "Nombre", "Marca", "Stock", "P.Compra", "P.Venta"};
        Object[][] datos = {
            {"RP001", "SSD 240GB", "Kingston", "15", "120.00", "150.00"},
            {"RP002", "RAM 8GB",   "Corsair",  "20",  "80.00", "100.00"}
        };
        tabla = new JTable(datos, columnas);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setPreferredSize(new Dimension(0, 120));

        // ── Centro: formulario + botones ──────────────────────────────
        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        panelCentro.add(panelForm,    BorderLayout.NORTH);
        panelCentro.add(panelBotones, BorderLayout.CENTER);

        panel.add(panelCentro,  BorderLayout.NORTH);
        panel.add(scrollTabla,  BorderLayout.CENTER);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new RepuestosView();
    }
}