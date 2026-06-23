package org.Kardex.jF.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.Kardex.jF.persistence.ConexionRepository;

public class BoletaModel {

    public record DetallePendiente(int idClienteServicio, String descripcion, double precio) {}
    public record VentaHistorial(int idBoleta, String numero, String tipoComprobante, int idCliente,
            String cliente, String dniRuc, String metodoPago, LocalDateTime fecha, double subtotal,
            double igv, double total) {}

    public BoletaModel() {
        crearTablasSiNoExisten();
    }

    private void crearTablasSiNoExisten() {
        String sqlBoleta = """
            CREATE TABLE IF NOT EXISTS boleta (
                id_boleta SERIAL PRIMARY KEY,
                numero VARCHAR(30) NOT NULL UNIQUE,
                tipo_comprobante VARCHAR(20) NOT NULL,
                id_cliente INTEGER NOT NULL REFERENCES cliente(id_cliente),
                dni_ruc VARCHAR(20),
                metodo_pago VARCHAR(40) NOT NULL,
                fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                subtotal NUMERIC(10, 2) NOT NULL DEFAULT 0,
                igv NUMERIC(10, 2) NOT NULL DEFAULT 0,
                total NUMERIC(10, 2) NOT NULL DEFAULT 0,
                estado VARCHAR(20) NOT NULL DEFAULT 'Pagado'
            )
            """;
        String sqlDetalle = """
            CREATE TABLE IF NOT EXISTS boleta_detalle (
                id_boleta_detalle SERIAL PRIMARY KEY,
                id_boleta INTEGER NOT NULL REFERENCES boleta(id_boleta) ON DELETE CASCADE,
                tipo_item VARCHAR(20) NOT NULL,
                descripcion TEXT NOT NULL,
                cantidad INTEGER NOT NULL DEFAULT 1,
                precio_unitario NUMERIC(10, 2) NOT NULL DEFAULT 0,
                importe NUMERIC(10, 2) NOT NULL DEFAULT 0,
                id_cliente_servicio INTEGER REFERENCES cliente_servicio(id_cliente_servicio),
                id_repuesto INTEGER REFERENCES repuesto(id_repuesto)
            )
            """;
        try (Connection cn = ConexionRepository.getConexion();
             Statement st = cn.createStatement()) {
            st.execute(sqlBoleta);
            st.execute(sqlDetalle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<VentaHistorial> listarHistorialVentas(Integer idCliente) {
        List<VentaHistorial> ventas = new ArrayList<>();
        String sql = """
            SELECT b.id_boleta, b.numero, b.tipo_comprobante, b.id_cliente,
                   TRIM(c.nombre || ' ' || COALESCE(c.apellido, '')) AS cliente,
                   b.dni_ruc, b.metodo_pago, b.fecha, b.subtotal, b.igv, b.total
            FROM boleta b
            JOIN cliente c ON b.id_cliente = c.id_cliente
            """
            + (idCliente == null ? "" : " WHERE b.id_cliente = ?")
            + " ORDER BY b.fecha DESC, b.id_boleta DESC";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            if (idCliente != null) {
                ps.setInt(1, idCliente);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ventas.add(new VentaHistorial(
                            rs.getInt("id_boleta"),
                            rs.getString("numero"),
                            rs.getString("tipo_comprobante"),
                            rs.getInt("id_cliente"),
                            rs.getString("cliente"),
                            rs.getString("dni_ruc"),
                            rs.getString("metodo_pago"),
                            rs.getTimestamp("fecha").toLocalDateTime(),
                            rs.getDouble("subtotal"),
                            rs.getDouble("igv"),
                            rs.getDouble("total")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ventas;
    }

    public List<DetallePendiente> listarServiciosPendientes(int idCliente) {
        List<DetallePendiente> detalles = new ArrayList<>();
        String sql = """
            SELECT cs.id_cliente_servicio,
                   cs.numero_orden || ' - ' || s.servicio AS descripcion,
                   cs.precio_unitario
            FROM cliente_servicio cs
            JOIN servicio s ON cs.id_servicio = s.id_servicio
            WHERE cs.id_cliente = ? AND cs.facturado = FALSE
            ORDER BY cs.fecha_aplicacion
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detalles.add(new DetallePendiente(
                            rs.getInt("id_cliente_servicio"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio_unitario")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detalles;
    }

    public String generarNumeroComprobante(String tipoComprobante) {
        String serie = "Factura".equals(tipoComprobante) ? "F001" : "B001";
        String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING(numero FROM 6) AS INTEGER)), 0) + 1 FROM boleta WHERE numero LIKE ?";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, serie + "-%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return String.format("%s-%06d", serie, rs.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serie + "-000001";
    }

    public boolean guardarBoleta(String numero, String tipoComprobante, int idCliente, String dniRuc, String metodoPago,
            double subtotal, double igv, double total, DefaultTableModel modeloDetalle) {
        String sqlBoleta = """
            INSERT INTO boleta (numero, tipo_comprobante, id_cliente, dni_ruc, metodo_pago, fecha, subtotal, igv, total)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        String sqlDetalle = """
            INSERT INTO boleta_detalle
                (id_boleta, tipo_item, descripcion, cantidad, precio_unitario, importe, id_cliente_servicio, id_repuesto)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        String sqlFacturarServicio = """
            UPDATE cliente_servicio
            SET facturado = TRUE, fecha_facturacion = CURRENT_TIMESTAMP
            WHERE id_cliente_servicio = ?
            """;
        String sqlDescontarStock = """
            UPDATE repuesto
            SET stock = stock - ?
            WHERE id_repuesto = ? AND stock >= ?
            """;
        try (Connection cn = ConexionRepository.getConexion()) {
            cn.setAutoCommit(false);
            try (PreparedStatement psBoleta = cn.prepareStatement(sqlBoleta, Statement.RETURN_GENERATED_KEYS)) {
                psBoleta.setString(1, numero);
                psBoleta.setString(2, tipoComprobante);
                psBoleta.setInt(3, idCliente);
                psBoleta.setString(4, dniRuc);
                psBoleta.setString(5, metodoPago);
                psBoleta.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                psBoleta.setDouble(7, subtotal);
                psBoleta.setDouble(8, igv);
                psBoleta.setDouble(9, total);
                psBoleta.executeUpdate();
                try (ResultSet keys = psBoleta.getGeneratedKeys()) {
                    if (!keys.next()) throw new IllegalStateException("No se generó ID de boleta.");
                    int idBoleta = keys.getInt(1);
                    guardarDetalles(cn, sqlDetalle, sqlFacturarServicio, sqlDescontarStock, idBoleta, modeloDetalle);
                }
            }
            cn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void guardarDetalles(Connection cn, String sqlDetalle, String sqlFacturarServicio, String sqlDescontarStock,
            int idBoleta, DefaultTableModel modeloDetalle) throws Exception {
        try (PreparedStatement psDetalle = cn.prepareStatement(sqlDetalle);
             PreparedStatement psFacturar = cn.prepareStatement(sqlFacturarServicio);
             PreparedStatement psStock = cn.prepareStatement(sqlDescontarStock)) {
            for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
                String tipo = String.valueOf(modeloDetalle.getValueAt(i, 0));
                int cantidad = Integer.parseInt(String.valueOf(modeloDetalle.getValueAt(i, 2)));
                double precio = parseSoles(String.valueOf(modeloDetalle.getValueAt(i, 3)));
                double importe = parseSoles(String.valueOf(modeloDetalle.getValueAt(i, 4)));
                Integer idReferencia = Integer.parseInt(String.valueOf(modeloDetalle.getValueAt(i, 5)));

                psDetalle.setInt(1, idBoleta);
                psDetalle.setString(2, tipo);
                psDetalle.setString(3, String.valueOf(modeloDetalle.getValueAt(i, 1)));
                psDetalle.setInt(4, cantidad);
                psDetalle.setDouble(5, precio);
                psDetalle.setDouble(6, importe);
                if ("SERVICIO".equals(tipo)) {
                    psDetalle.setInt(7, idReferencia);
                    psDetalle.setNull(8, Types.INTEGER);
                    psFacturar.setInt(1, idReferencia);
                    psFacturar.addBatch();
                } else {
                    psDetalle.setNull(7, Types.INTEGER);
                    psDetalle.setInt(8, idReferencia);
                    psStock.setInt(1, cantidad);
                    psStock.setInt(2, idReferencia);
                    psStock.setInt(3, cantidad);
                    psStock.addBatch();
                }
                psDetalle.addBatch();
            }
            psDetalle.executeBatch();
            psFacturar.executeBatch();
            psStock.executeBatch();
        }
    }

    private double parseSoles(String valor) {
        return Double.parseDouble(valor.replace("S/", "").trim());
    }
}
