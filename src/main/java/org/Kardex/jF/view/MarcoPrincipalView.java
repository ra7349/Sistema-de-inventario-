package org.Kardex.jF.view;
import javax.swing.*;
import java.awt.*;
import org.Kardex.jF.bean.entity.Indicadores;
import org.Kardex.jF.model.IndicadoresModel;
import org.Kardex.jF.usecase.IndicadoresUsecase;

public class MarcoPrincipalView extends JFrame {

    private static final long serialVersionUID = 1L;

    // Paleta de colores
    private static final Color COLOR_FONDO             = new Color(15, 23, 42);
    private static final Color COLOR_FONDO_CARD         = new Color(30, 41, 59); 
    private static final Color COLOR_ACENTO             = new Color(59, 130, 246); 
    private static final Color COLOR_ACENTO_SUAVE       = new Color(96, 165, 250);
    private static final Color COLOR_TEXTO              = Color.WHITE;
    private static final Color COLOR_TEXTO_SUAVE        = new Color(148, 163, 184); 
    private JLabel lblProductosBajoStock;
    private JLabel lblVentasRegistradas;
    private JLabel lblMovimientosMes;
    private JLabel lblClientesRegistrados;
    private JLabel lblIngresosMes;
    private final IndicadoresUsecase indicadoresUsecase;

    public MarcoPrincipalView() {
        this.indicadoresUsecase = new IndicadoresModel();
        setTitle("Golocentro — Sistema de Inventario");
        setSize(1050, 680);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        this.setIconImage(new ImageIcon("image.png").getImage());
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
        JMenuItem itemCambiarContraseña = construirItem("Cambiar contraseña");
        JMenuItem itemSalir = construirItem("Salir");
        menuArchivo.add(itemInicio);
        menuArchivo.add(itemCambiarContraseña);
        menuArchivo.add(itemSalir);

        JMenu menuProductos = construirMenu("Productos");
        JMenuItem itemRegistrarProducto = construirItem("Registrar producto");
        JMenuItem itemListarProductos = construirItem("Listar productos");
        menuProductos.add(itemRegistrarProducto);
        menuProductos.add(itemListarProductos);

        JMenu menuCategorias = construirMenu("Categorías");
        JMenuItem itemGestionarCategorias = construirItem("Gestionar categorías");
        menuCategorias.add(itemGestionarCategorias);

        JMenu menuClientes = construirMenu("Clientes");
        JMenuItem itemRegistrarCliente = construirItem("Registrar cliente");
        JMenuItem itemModificarCliente = construirItem("Modificar cliente");
        JMenuItem itemListarClientes = construirItem("Listar clientes");
        menuClientes.add(itemRegistrarCliente);
        menuClientes.add(itemModificarCliente);
        menuClientes.add(itemListarClientes);

        JMenu menuProveedores = construirMenu("Proveedores");
        JMenuItem itemProveedores = construirItem("Directorio de proveedores");
        menuProveedores.add(itemProveedores);

        JMenu menuInventario = construirMenu("Inventario");
        JMenuItem itemStock = construirItem("Stock de productos");
        JMenuItem itemMovimiento = construirItem("Entradas y salidas");
        menuInventario.add(itemStock);
        menuInventario.add(itemMovimiento);

        JMenu menuVentas = construirMenu("Ventas");
        JMenuItem itemGenerarVenta = construirItem("Registrar venta");
        JMenuItem itemHistorialVentas = construirItem("Historial de ventas");
        menuVentas.add(itemGenerarVenta);
        menuVentas.add(itemHistorialVentas);

        JMenu menuReportes = construirMenu("Reportes");
        JMenuItem itemAcercaDe = construirItem("Acerca de Golocentro");
        menuReportes.add(itemAcercaDe);

        barra.add(menuArchivo);
        barra.add(menuProductos);
        barra.add(menuCategorias);
        barra.add(menuClientes);
        barra.add(menuProveedores);
        barra.add(menuInventario);
        barra.add(menuVentas);
        barra.add(menuReportes);

        itemInicio.addActionListener(e -> cargarIndicadores());
        itemRegistrarProducto.addActionListener(e -> new ProductoListarView().setVisible(true));
        itemListarProductos.addActionListener(e -> new ProductoListarView().setVisible(true));
        itemGestionarCategorias.addActionListener(e -> new CategoriaView().setVisible(true));
        itemRegistrarCliente.addActionListener(e -> new FormularioCliente(this).setVisible(true));
        itemModificarCliente.addActionListener(e -> new ModificarClienteView(this).setVisible(true));
        itemListarClientes.addActionListener(e -> new ClienteListarView().setVisible(true));
        itemProveedores.addActionListener(e -> JOptionPane.showMessageDialog(this, "Módulo de proveedores preparado para registrar abastecedores de golosinas y bebidas."));
        itemStock.addActionListener(e -> new RepuestosView().setVisible(true));
        itemMovimiento.addActionListener(e -> new MovimientosView().setVisible(true));
        itemCambiarContraseña.addActionListener(e -> new ActualizarcontraseñaView().setVisible(true));
        itemGenerarVenta.addActionListener(e -> new GenerarBoletaView().setVisible(true));
        itemHistorialVentas.addActionListener(e -> new HistorialVentasView().setVisible(true));
        itemAcercaDe.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Golocentro\nSistema de Inventario para distribución mayorista y minorista v1.0\n2026",
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

        JLabel lblTitulo = new JLabel("Golocentro");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Sistema de Inventario de golosinas y bebidas");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSub.setForeground(COLOR_ACENTO_SUAVE);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblSub.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel lblVersion = new JLabel("v1.0  ·  2026");
        lblVersion.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblVersion.setForeground(COLOR_TEXTO_SUAVE);
        lblVersion.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblVersion.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JLabel lblImagen = new JLabel("");
        lblImagen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblImagen.setForeground(COLOR_TEXTO_SUAVE);
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setBorder(BorderFactory.createDashedBorder(COLOR_ACENTO_SUAVE, 1.4f, 8, 4, true));
        lblImagen.setPreferredSize(new Dimension(330, 330));
        lblImagen.setMaximumSize(new Dimension(335, 335));
        lblImagen.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Sección reservada para tu imagen:
        // Reemplaza el texto del JLabel anterior por un ImageIcon cuando tengas el archivo, por ejemplo:
        lblImagen.setText("");
        lblImagen.setIcon(new ImageIcon("image.png"));

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

        lblProductosBajoStock   = new JLabel("--", SwingConstants.CENTER);
        lblVentasRegistradas   = new JLabel("--", SwingConstants.CENTER);
        lblMovimientosMes        = new JLabel("--", SwingConstants.CENTER);
        lblClientesRegistrados = new JLabel("--", SwingConstants.CENTER);
        lblIngresosMes         = new JLabel("--", SwingConstants.CENTER);

        panel.add(construirIndicador("📦", "Productos con bajo stock", lblProductosBajoStock, COLOR_ACENTO));
        panel.add(construirIndicador("🧾", "Ventas registradas", lblVentasRegistradas, new Color(34, 197, 94)));
        panel.add(construirIndicador("🔁", "Movimientos este mes", lblMovimientosMes, new Color(168, 85, 247)));
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
            Indicadores indicadores = indicadoresUsecase.obtenerIndicadores();

            lblProductosBajoStock.setText(String.valueOf(indicadores.getProductosBajoStock()));
            lblVentasRegistradas.setText(String.valueOf(indicadores.getVentasRegistradas()));
            lblMovimientosMes.setText(String.valueOf(indicadores.getMovimientosEsteMes()));
            lblClientesRegistrados.setText(String.valueOf(indicadores.getClientesRegistrados()));
            lblIngresosMes.setText(String.format("S/ %,.2f", indicadores.getIngresosDelMes()));
        } catch (Exception ex) {
            // En caso de error de conexión/consulta, deja los indicadores en "--"
            // en vez de romper la pantalla principal.
            ex.printStackTrace();
        }
    }

}
