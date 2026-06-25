package org.golocentro.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.golocentro.bean.entity.DetalleVenta;
import org.golocentro.bean.entity.Venta;
import org.golocentro.persistence.ConexionRepository;
import org.golocentro.usecase.VentaUsecase;

public class VentaModel implements VentaUsecase {

    @Override
    public boolean insertar(Venta venta) {
        String sqlVenta = """
            INSERT INTO ventas (numero, tipo_comprobante, id_cliente, dni_ruc, metodo_pago, fecha, subtotal, igv, total, estado)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        String sqlDetalle = """
            INSERT INTO detalle_ventas (id_venta, id_producto, descripcion, cantidad, precio_unitario, importe)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        String sqlDescontarStock = """
            UPDATE producto
            SET stock_actual = stock_actual - ?
            WHERE id_producto = ? AND stock_actual >= ?
            """;

        try (Connection cn = ConexionRepository.getConexion()) {
            cn.setAutoCommit(false);
            try (PreparedStatement psVenta = cn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psDetalle = cn.prepareStatement(sqlDetalle);
                 PreparedStatement psStock = cn.prepareStatement(sqlDescontarStock)) {
                psVenta.setString(1, venta.getNumero());
                psVenta.setString(2, venta.getTipoComprobante());
                psVenta.setInt(3, venta.getIdCliente());
                psVenta.setString(4, null);
                psVenta.setString(5, venta.getMetodoPago());
                psVenta.setTimestamp(6, Timestamp.valueOf(venta.getFecha() == null ? LocalDateTime.now() : venta.getFecha()));
                psVenta.setDouble(7, venta.getSubtotal());
                psVenta.setDouble(8, venta.getIgv());
                psVenta.setDouble(9, venta.getTotal());
                psVenta.setString(10, venta.getEstado() == null ? "Pagado" : venta.getEstado());
                psVenta.executeUpdate();

                try (ResultSet keys = psVenta.getGeneratedKeys()) {
                    if (!keys.next()) throw new IllegalStateException("No se generó ID de venta.");
                    int idVenta = keys.getInt(1);
                    for (DetalleVenta detalle : venta.getDetalles()) {
                        psStock.setInt(1, detalle.getCantidad());
                        psStock.setInt(2, detalle.getIdProducto());
                        psStock.setInt(3, detalle.getCantidad());
                        if (psStock.executeUpdate() == 0) {
                            throw new IllegalStateException("Stock insuficiente para el producto ID " + detalle.getIdProducto());
                        }

                        psDetalle.setInt(1, idVenta);
                        psDetalle.setInt(2, detalle.getIdProducto());
                        psDetalle.setString(3, detalle.getDescripcion());
                        psDetalle.setInt(4, detalle.getCantidad());
                        psDetalle.setDouble(5, detalle.getPrecioUnitario());
                        psDetalle.setDouble(6, detalle.getImporte());
                        psDetalle.addBatch();
                    }
                    psDetalle.executeBatch();
                }
                cn.commit();
                return true;
            } catch (Exception e) {
                cn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Venta> listar() { throw new UnsupportedOperationException("Use HistorialVentasView para consultar ventas."); }

    @Override
    public boolean actualizar(Venta venta) { throw new UnsupportedOperationException("La edición de ventas no está disponible."); }

    @Override
    public boolean eliminar(Integer id) { throw new UnsupportedOperationException("La eliminación de ventas no está disponible."); }

    @Override
    public String generarNumeroVenta(String tipoComprobante) {
        String prefijo = "Factura".equals(tipoComprobante) ? "F" : "B";
        String sql = "SELECT COALESCE(MAX(id_venta), 0) + 1 FROM ventas";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return String.format("%s%04d", prefijo, rs.getInt(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prefijo + "0001";
    }
}
