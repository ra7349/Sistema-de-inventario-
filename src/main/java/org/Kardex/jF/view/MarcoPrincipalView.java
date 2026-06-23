package org.Kardex.jF.view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MarcoPrincipalView extends JFrame {

    private static final long serialVersionUID = 1L;

    // Paleta de colores
    private static final Color COLOR_FONDO             = new Color(15, 23, 42);
    private static final Color COLOR_FONDO_CARD         = new Color(30, 41, 59); 
    private static final Color COLOR_FONDO_CARD_HOVER   = new Color(42, 56, 80);
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

        JMenu menuTecnicos = construirMenu("Técnicos");
        JMenuItem itemRegistrarTecnico = construirItem("Registrar Técnico");
        JMenuItem itemListarTecnicos   = construirItem("Listar Técnicos");
        menuTecnicos.add(itemRegistrarTecnico);
        menuTecnicos.add(itemListarTecnicos);

        JMenu menuEquipos = construirMenu("Equipos");
        JMenuItem itemRegistrarEquipo = construirItem("Registrar Equipo");
        JMenuItem itemListarEquipos   = construirItem("Listar Equipos");
        menuEquipos.add(itemRegistrarEquipo);
        menuEquipos.add(itemListarEquipos);

        JMenu menuServicios = construirMenu("Servicios");
        JMenuItem itemGestionarServicio = construirItem("GestionarServicio");
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
        JMenuItem itemGenerarRportes   = construirItem("Generar Reportes");
        menuReportes.add(itemGenerarRportes);
      

        JMenu menuAyuda = construirMenu("Ayuda");
        JMenuItem itemAcercaDe = construirItem("Acerca del Sistema");
        JMenuItem itemManual   = construirItem("Manual del Usuario");
        menuAyuda.add(itemAcercaDe);
        menuAyuda.addSeparator();
        menuAyuda.add(itemManual);

        barra.add(menuArchivo);
        barra.add(menuClientes);
        barra.add(menuTecnicos);
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

        itemRegistrarTecnico.addActionListener(e -> new FormularioTecnico(this).setVisible(true));
        itemListarTecnicos  .addActionListener(e -> new TecnicoListarView().setVisible(true));

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
        
        itemGenerarRportes.addActionListener(e -> new ReporteView().setVisible(true));
        
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

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setOpaque(false);
        panelSuperior.add(construirPanelBienvenida());
        panelSuperior.add(construirPanelIndicadores());

        panelCentro.add(panelSuperior, BorderLayout.NORTH);
        panelCentro.add(construirPanelCards(), BorderLayout.CENTER);
        return panelCentro;
    }

    private JPanel construirPanelBienvenida() {
        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);
        panelTexto.setBorder(BorderFactory.createEmptyBorder(55, 0, 25, 0));

        JLabel lblTitulo = new JLabel("JF Technology & Services");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Sistema de Gestión para Soporte Técnico");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSub.setForeground(COLOR_ACENTO_SUAVE);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JLabel lblVersion = new JLabel("v1.0  ·  2026");
        lblVersion.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblVersion.setForeground(COLOR_TEXTO_SUAVE);
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblVersion.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        panelTexto.add(lblTitulo);
        panelTexto.add(lblSub);
        panelTexto.add(lblVersion);
        return panelTexto;
    }

    // ── Indicadores (KPIs) ──────────────────────────────────────
    private JPanel construirPanelIndicadores() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 14, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 70, 25, 70));

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

    /** Tarjeta compacta de un solo indicador: ícono + valor grande + etiqueta. */
    private JPanel construirIndicador(String icono, String etiqueta, JLabel lblValor, Color colorValor) {
        TarjetaRedondeada card = new TarjetaRedondeada();
        card.setBackground(COLOR_FONDO_CARD);
        card.setBorder(new EmptyBorder(14, 8, 14, 8));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValor.setForeground(colorValor);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTexto = new JLabel(etiqueta, SwingConstants.CENTER);
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTexto.setForeground(COLOR_TEXTO_SUAVE);
        lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblIcono);
        card.add(Box.createVerticalStrut(6));
        card.add(lblValor);
        card.add(Box.createVerticalStrut(2));
        card.add(lblTexto);

        return card;
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

    private JPanel construirPanelCards() {
        JPanel panelCards = new JPanel(new GridLayout(2, 3, 18, 18));
        panelCards.setOpaque(false);
        panelCards.setBorder(BorderFactory.createEmptyBorder(15, 70, 70, 70));

        String[][] modulos = {
            {"👥", "Clientes",   "Registro y gestión de clientes"},
            {"🔧", "Técnicos",   "Control de técnicos asignados"},
            {"💻", "Equipos",    "Equipos ingresados a reparación"},
            {"📋", "Órdenes",    "Apertura y seguimiento de tickets"},
            {"📦", "Inventario", "Repuestos y materiales"},
            {"📊", "Reportes",   "KPIs y estadísticas"}
        };

        for (String[] mod : modulos) {
            panelCards.add(construirTarjeta(mod[0], mod[1], mod[2]));
        }
        return panelCards;
    }

    private JPanel construirTarjeta(String icono, String titulo, String descripcion) {
        TarjetaRedondeada card = new TarjetaRedondeada();
        card.setLayout(new BorderLayout(0, 6));
        card.setBackground(COLOR_FONDO_CARD);
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTit.setForeground(COLOR_TEXTO);

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelTitulo.setOpaque(false);
        panelTitulo.add(lblIcono);
        panelTitulo.add(lblTit);

        JLabel lblDesc = new JLabel("<html><div style='width:200px'>" + descripcion + "</div></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(COLOR_TEXTO_SUAVE);

        card.add(panelTitulo, BorderLayout.NORTH);
        card.add(lblDesc, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(COLOR_FONDO_CARD_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(COLOR_FONDO_CARD);
            }
        });

        return card;
    }

    /** Panel con esquinas redondeadas y borde de acento, usado para las tarjetas de módulos. */
    private static class TarjetaRedondeada extends JPanel {
        private static final long serialVersionUID = 1L;

        TarjetaRedondeada() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.setColor(COLOR_ACENTO);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}