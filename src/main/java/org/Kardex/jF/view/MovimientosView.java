package org.Kardex.jF.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MovimientosView extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JRadioButton rbEntrada = new JRadioButton("Entrada", true);
    private JRadioButton rbSalida  = new JRadioButton("Salida");
    private JComboBox<String> comboRepuesto = new JComboBox<>(new String[]{"SSD 240 GB", "RAM 8GB", "Batería HP"});
    private JTextField campoCantidad = new JTextField("5");
    private JTextField campoMotivo   = new JTextField("Compra de mercadería");
    private JTextField campoFecha    = new JTextField("21/06/2026");
    private JButton btnRegistrar     = new JButton("Registrar Movimiento");

    private JTable tabla;

    public MovimientosView() {
        setTitle("Movimientos de Inventario");
        setSize(560, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "MOVIMIENTOS DE INVENTARIO",
                        TitledBorder.CENTER, TitledBorder.TOP),
                new EmptyBorder(10, 15, 10, 15)));

        // ── Formulario ────────────────────────────────────────────────
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 10, 8));

        // Tipo movimiento con RadioButtons
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbEntrada);
        grupo.add(rbSalida);
        JPanel panelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelRadio.add(rbEntrada);
        panelRadio.add(rbSalida);

        panelForm.add(new JLabel("Tipo Movimiento:"));
        panelForm.add(panelRadio);

        panelForm.add(new JLabel("Repuesto:"));
        panelForm.add(comboRepuesto);

        panelForm.add(new JLabel("Cantidad:"));
        panelForm.add(campoCantidad);

        panelForm.add(new JLabel("Motivo:"));
        panelForm.add(campoMotivo);

        panelForm.add(new JLabel("Fecha:"));
        panelForm.add(campoFecha);

        panelForm.add(new JLabel());
        panelForm.add(btnRegistrar);

        // ── Tabla ─────────────────────────────────────────────────────
        String[] columnas = {"Fecha", "Tipo", "Repuesto", "Cantidad", "Motivo"};
        Object[][] datos = {
            {"21/06", "Entrada", "SSD 240GB", "10", "Compra"},
            {"22/06", "Salida",  "SSD 240GB",  "1", "Orden OS001"}
        };
        tabla = new JTable(datos, columnas);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setPreferredSize(new Dimension(0, 120));

        panel.add(panelForm,   BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
