package org.golocentro.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.golocentro.persistence.ConexionRepository;

public class HistorialVentasModel {

    public record VentaHistorial(int idVenta, String numero, String tipoComprobante, int idCliente,
            String cliente, String dniRuc, String metodoPago, LocalDateTime fecha, double subtotal,
            double igv, double total) {}
    public record DetalleHistorial(String tipoItem, String descripcion, int cantidad,
            double precioUnitario, double importe) {}

    public List<VentaHistorial> listarHistorialVentas(Integer idCliente) {
        return buscarHistorialVentas(idCliente, null, null);
    }

    public List<VentaHistorial> buscarHistorialVentas(Integer idCliente, LocalDate fecha, String comprobante) {
        List<VentaHistorial> ventas = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT v.id_venta, v.numero, v.tipo_comprobante, v.id_cliente,
                   TRIM(c.nombre || ' ' || COALESCE(c.apellido, '')) AS cliente,
                   v.dni_ruc, v.metodo_pago, v.fecha, v.subtotal, v.igv, v.total
            FROM ventas v
            JOIN cliente c ON v.id_cliente = c.id_cliente
            WHERE 1 = 1
            """);
        List<Object> parametros = new ArrayList<>();
        if (idCliente != null) {
            sql.append(" AND v.id_cliente = ?");
            parametros.add(idCliente);
        }
        if (fecha != null) {
            sql.append(" AND DATE(v.fecha) = ?");
            parametros.add(java.sql.Date.valueOf(fecha));
        }
        if (comprobante != null && !comprobante.isBlank()) {
            sql.append(" AND UPPER(v.numero) LIKE UPPER(?)");
            parametros.add("%" + comprobante.trim() + "%");
        }
        sql.append(" ORDER BY v.fecha DESC, v.id_venta DESC");
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) ps.setObject(i + 1, parametros.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ventas.add(new VentaHistorial(
                            rs.getInt("id_venta"),
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
        } catch (Exception e) { e.printStackTrace(); }
        return ventas;
    }

    public List<DetalleHistorial> listarDetalleHistorial(int idVenta) {
        List<DetalleHistorial> detalles = new ArrayList<>();
        String sql = """
            SELECT 'PRODUCTO' AS tipo_item, descripcion, cantidad, precio_unitario, importe
            FROM detalle_ventas
            WHERE id_venta = ?
            ORDER BY id_detalle_venta
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detalles.add(new DetalleHistorial(
                            rs.getString("tipo_item"),
                            rs.getString("descripcion"),
                            rs.getInt("cantidad"),
                            rs.getDouble("precio_unitario"),
                            rs.getDouble("importe")));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return detalles;
    }
}
