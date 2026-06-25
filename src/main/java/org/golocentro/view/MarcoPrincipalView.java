package org.golocentro.view;
import javax.swing.*;
import java.awt.*;

public class MarcoPrincipalView extends JFrame {

    private static final long serialVersionUID = 1L;

    // Paleta de colores
    private static final Color COLOR_FONDO             = new Color(245, 245, 245);
    private static final Color COLOR_FONDO_MENU        = new Color(238, 238, 238);
    private static final Color COLOR_ACENTO            = new Color(51, 102, 153);
    private static final Color COLOR_TEXTO             = new Color(33, 33, 33);
    private static final Color COLOR_TEXTO_SUAVE       = new Color(88, 88, 88);

    public MarcoPrincipalView() {
        setTitle("Golocentro — Sistema de Inventario");
        setSize(1050, 680);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setIconImage(new ImageIcon("image.png").getImage());
        setJMenuBar(construirMenuBar());
        add(construirPanelCentro());
    }

    // ── Menú bar ──────────────────────────────────────────────────
    private JMenuBar construirMenuBar() {
        JMenuBar barra = new JMenuBar();
        barra.setBackground(COLOR_FONDO_MENU);
        barra.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_ACENTO));

        JMenu menuArchivo = construirMenu("Archivo");
        JMenuItem itemInicio = construirItem("Inicio");
        JMenuItem itemSalir = construirItem("Salir");
        menuArchivo.add(itemInicio);
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

        itemInicio.addActionListener(e -> mostrarInicio());
        itemRegistrarProducto.addActionListener(e -> new ProductoListarView().setVisible(true));
        itemListarProductos.addActionListener(e -> new ProductoListarView().setVisible(true));
        itemGestionarCategorias.addActionListener(e -> new CategoriaView().setVisible(true));
        itemRegistrarCliente.addActionListener(e -> new FormularioCliente(this).setVisible(true));
        itemModificarCliente.addActionListener(e -> new ModificarClienteView(this).setVisible(true));
        itemListarClientes.addActionListener(e -> new ClienteListarView().setVisible(true));
        itemProveedores.addActionListener(e -> new ProveedorListarView().setVisible(true));
        itemStock.addActionListener(e -> new ProductoListarView().setVisible(true));
        itemMovimiento.addActionListener(e -> new MovimientosView().setVisible(true));
        itemGenerarVenta.addActionListener(e -> new VentasView().setVisible(true));
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

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(55, 70, 70, 70));

        contenido.add(construirPanelPresentacion(), BorderLayout.CENTER);

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
        lblSub.setForeground(COLOR_ACENTO);
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
        lblImagen.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
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

    private void mostrarInicio() {
        getContentPane().removeAll();
        add(construirPanelCentro());
        revalidate();
        repaint();
    }

}
