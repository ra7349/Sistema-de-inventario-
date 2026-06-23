package org.Kardex.jF.view;
import javax.swing.*;
import java.awt.*;

public class MarcoPrincipalView extends JFrame {

    private static final long serialVersionUID = 1L;

    // Paleta de colores
    private static final Color COLOR_FONDO             = new Color(15, 23, 42);
    private static final Color COLOR_FONDO_CARD         = new Color(30, 41, 59); 
    private static final Color COLOR_ACENTO             = new Color(59, 130, 246); 
    private static final Color COLOR_ACENTO_SUAVE       = new Color(96, 165, 250);
    private static final Color COLOR_TEXTO              = Color.WHITE;
    private static final Color COLOR_TEXTO_SUAVE        = new Color(148, 163, 184); 
    private JLabel lblEquiposReparacion;
    private JLabel lblEquiposEntregados;
    private JLabel lblServiciosMes;
    private JLabel lblClientesRegistrados;
    private JLabel lblIngresosMes;

    public MarcoPrincipalView() {
        setTitle("JF Technology & Services — Sistema de Soporte Técnico");
        setSize(1050, 680);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);

        setJMenuBar(construirMenuBar());
        add(construirPanelCentro());

        cargarIndicadores();
    }

    // ── Menú bar ──────────────────────────────────────────────────
    private JMenuBar construirMenuBar() {
        JMenuBar barra = new JMenuBar();
        barra.setBackground(COLOR_FONDO_CARD);
        barra.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_ACENTO));

        JMenu menuArchivo = construirMenu("Archivo");

        JMenuItem itemInicio = construirItem("Inicio");
        JMenuItem itemCambiarContraseña = construirItem("Cambiar Contraseña");
        JMenuItem itemConfiguracion = construirItem("Configuración");
        JMenuItem itemSalir = construirItem("Salir");

        menuArchivo.add(itemInicio);
        menuArchivo.add(itemCambiarContraseña);
        menuArchivo.add(itemConfiguracion);
        menuArchivo.add(itemSalir);

        
        JMenu menuClientes = construirMenu("Clientes");
        JMenuItem itemRegistrarCliente = construirItem("Registrar Cliente");
        JMenuItem itemModificarCliente = construirItem("Modificar Cliente");
        JMenuItem itemListarClientes   = construirItem("Listar Clientes");
        menuClientes.add(itemRegistrarCliente);
        menuClientes.add(itemModificarCliente);
        menuClientes.add(itemListarClientes);

        JMenu menuEquipos = construirMenu("Equipos");
        JMenuItem itemRegistrarEquipo = construirItem("Registrar Equipo");
        JMenuItem itemListarEquipos   = construirItem("Listar Equipos");
        menuEquipos.add(itemRegistrarEquipo);
        menuEquipos.add(itemListarEquipos);

        JMenu menuServicios = construirMenu("Servicios");
        JMenuItem itemGestionarServicio = construirItem("Gestionar servicios");
        menuServicios.add(itemGestionarServicio);
        
        JMenu menuOrdenes = construirMenu("Órdenes de Servicio");
        JMenuItem itemNuevaOrden       = construirItem("Gestion de ordenes");
        JMenuItem itemConsultaOrdenes  = construirItem("Consulta de Órdenes");
        menuOrdenes.add(itemNuevaOrden);
        menuOrdenes.add(itemConsultaOrdenes);


        JMenu menuInventario = construirMenu("Inventario");
        JMenuItem itemRepuestos    = construirItem("Repuestos");
        JMenuItem itemMovimiento     = construirItem("Movimientos");
        menuInventario.add(itemRepuestos);
        menuInventario.add(itemMovimiento);

        JMenu menuFacturacion = construirMenu("Facturación");
        JMenuItem itemGenerarBoleta = construirItem("Generar Boleta");
        JMenuItem itemHistorialVentas = construirItem("Historial de Ventas");
        menuFacturacion.add(itemGenerarBoleta);
        menuFacturacion.add(itemHistorialVentas);

        JMenu menuReportes = construirMenu("Reportes");
        JMenuItem itemGenerarReportes   = construirItem("Generar Reportes");
        menuReportes.add(itemGenerarReportes);
      

        JMenu menuAyuda = construirMenu("Ayuda");
        JMenuItem itemAcercaDe = construirItem("Acerca del Sistema");
        JMenuItem itemManual   = construirItem("Manual del Usuario");
        menuAyuda.add(itemAcercaDe);
        menuAyuda.addSeparator();
        menuAyuda.add(itemManual);

        barra.add(menuArchivo);
        barra.add(menuClientes);
        barra.add(menuEquipos);
        barra.add(menuServicios);
        barra.add(menuOrdenes);
        barra.add(menuInventario);
        barra.add(menuFacturacion);
        barra.add(menuReportes);
        barra.add(menuAyuda);

        // ── Acciones (sin cambios respecto a la lógica original) ──
        itemRegistrarCliente.addActionListener(e -> new FormularioCliente(this).setVisible(true));
        itemModificarCliente.addActionListener(e -> new ModificarClienteView(this).setVisible(true));
        itemListarClientes  .addActionListener(e -> new ClienteListarView().setVisible(true));

        itemRegistrarEquipo.addActionListener(e -> new FormularioEquipo(this).setVisible(true));
        itemListarEquipos  .addActionListener(e -> new EquipoListarView().setVisible(true));

        itemCambiarContraseña.addActionListener(e -> new ActualizarcontraseñaView().setVisible(true));
        itemGestionarServicio.addActionListener(e -> new GestionServicioView().setVisible(true));

        itemNuevaOrden.addActionListener(e -> new NuevaOrdenView().setVisible(true));
        itemConsultaOrdenes.addActionListener(e -> new ConsultaView().setVisible(true));
        
        itemRepuestos.addActionListener(e -> new RepuestosView().setVisible(true));
        itemMovimiento.addActionListener(e -> new MovimientosView().setVisible(true));
        
        itemGenerarBoleta.addActionListener(e -> new GenerarBoletaView().setVisible(true));
        itemHistorialVentas.addActionListener(e -> new HistorialVentasView().setVisible(true));
        
        itemGenerarReportes.addActionListener(e -> new ReporteView().setVisible(true));
        
        itemAcercaDe.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "JF Technology & Services\nSistema de Soporte Técnico v1.0\n2026",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE));
        itemSalir.addActionListener(e -> System.exit(0));

        return barra;
    }

    private JMenu construirMenu(String texto) {
        JMenu menu = new JMenu(texto);
        menu.setForeground(COLOR_TEXTO);
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return menu;
    }

    private JMenuItem construirItem(String texto) {
        JMenuItem item = new JMenuItem(texto);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return item;
    }

    // ── Panel central ────────────────────────────────────────────
    private JPanel construirPanelCentro() {
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(COLOR_FONDO);

        JPanel contenido = new JPanel(new BorderLayout(45, 0));
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(55, 70, 70, 70));

        contenido.add(construirPanelPresentacion(), BorderLayout.CENTER);
        contenido.add(construirPanelIndicadores(), BorderLayout.EAST);

        panelCentro.add(contenido, BorderLayout.CENTER);
        return panelCentro;
    }

    private JPanel construirPanelPresentacion() {
        JPanel panelPresentacion = new JPanel();
        panelPresentacion.setLayout(new BoxLayout(panelPresentacion, BoxLayout.Y_AXIS));
        panelPresentacion.setOpaque(false);
        panelPresentacion.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        JLabel lblTitulo = new JLabel("JF Technology & Services");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Sistema de Gestión para Soporte Técnico");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSub.setForeground(COLOR_ACENTO_SUAVE);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblSub.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel lblVersion = new JLabel("v1.0  ·  2026");
        lblVersion.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblVersion.setForeground(COLOR_TEXTO_SUAVE);
        lblVersion.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblVersion.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JLabel lblImagen = new JLabel("Espacio para imagen");
        lblImagen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblImagen.setForeground(COLOR_TEXTO_SUAVE);
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setBorder(BorderFactory.createDashedBorder(COLOR_ACENTO_SUAVE, 1.4f, 8, 4, true));
        lblImagen.setPreferredSize(new Dimension(420, 240));
        lblImagen.setMaximumSize(new Dimension(420, 240));
        lblImagen.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Sección reservada para tu imagen:
        // Reemplaza el texto del JLabel anterior por un ImageIcon cuando tengas el archivo, por ejemplo:
        // lblImagen.setText("");
        // lblImagen.setIcon(new ImageIcon("ruta/de/tu/imagen.png"));

        panelPresentacion.add(lblTitulo);
        panelPresentacion.add(lblSub);
        panelPresentacion.add(lblVersion);
        panelPresentacion.add(Box.createVerticalStrut(35));
        panelPresentacion.add(lblImagen);
        return panelPresentacion;
    }

    // ── Indicadores (KPIs) ──────────────────────────────────────
    private JPanel construirPanelIndicadores() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 0, 16));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panel.setPreferredSize(new Dimension(290, 0));

        lblEquiposReparacion   = new JLabel("--", SwingConstants.CENTER);
        lblEquiposEntregados   = new JLabel("--", SwingConstants.CENTER);
        lblServiciosMes        = new JLabel("--", SwingConstants.CENTER);
        lblClientesRegistrados = new JLabel("--", SwingConstants.CENTER);
        lblIngresosMes         = new JLabel("--", SwingConstants.CENTER);

        panel.add(construirIndicador("🛠", "Equipos en reparación", lblEquiposReparacion, COLOR_ACENTO));
        panel.add(construirIndicador("✅", "Equipos entregados", lblEquiposEntregados, new Color(34, 197, 94)));
        panel.add(construirIndicador("📋", "Servicios este mes", lblServiciosMes, new Color(168, 85, 247)));
        panel.add(construirIndicador("👥", "Clientes registrados", lblClientesRegistrados, new Color(234, 179, 8)));
        panel.add(construirIndicador("💰", "Ingresos del mes", lblIngresosMes, new Color(16, 185, 129)));

        return panel;
    }

    /** Indicador simple en una sola columna: ícono + valor + etiqueta, sin tarjetas. */
    private JPanel construirIndicador(String icono, String etiqueta, JLabel lblValor, Color colorValor) {
        JPanel indicador = new JPanel(new BorderLayout(14, 0));
        indicador.setOpaque(false);
        indicador.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_FONDO_CARD));

        JLabel lblIcono = new JLabel(icono, SwingConstants.CENTER);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        lblIcono.setPreferredSize(new Dimension(44, 44));

        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValor.setForeground(colorValor);
        lblValor.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel lblTexto = new JLabel(etiqueta);
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTexto.setForeground(COLOR_TEXTO_SUAVE);

        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);
        panelTexto.add(lblValor);
        panelTexto.add(Box.createVerticalStrut(2));
        panelTexto.add(lblTexto);

        indicador.add(lblIcono, BorderLayout.WEST);
        indicador.add(panelTexto, BorderLayout.CENTER);

        return indicador;
    }

    private void cargarIndicadores() {
        try {
            // TODO: sustituye estas líneas por llamadas a tus DAOs/Servicios reales, p. ej.:
            // int equiposEnReparacion = EquipoDAO.contarPorEstado("EN_REPARACION");
            int equiposEnReparacion   = 0;
            int equiposEntregados     = 0;
            int serviciosEsteMes      = 0;
            int clientesRegistrados   = 0;
            double ingresosDelMes     = 0.0;

            lblEquiposReparacion.setText(String.valueOf(equiposEnReparacion));
            lblEquiposEntregados.setText(String.valueOf(equiposEntregados));
            lblServiciosMes.setText(String.valueOf(serviciosEsteMes));
            lblClientesRegistrados.setText(String.valueOf(clientesRegistrados));
            lblIngresosMes.setText(String.format("S/ %,.2f", ingresosDelMes));
        } catch (Exception ex) {
            // En caso de error de conexión/consulta, deja los indicadores en "--"
            // en vez de romper la pantalla principal.
            ex.printStackTrace();
        }
    }

}
