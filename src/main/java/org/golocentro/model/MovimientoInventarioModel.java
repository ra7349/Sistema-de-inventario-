package org.golocentro.model;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.golocentro.bean.entity.MovimientoInventario;
import org.golocentro.persistence.ConexionRepository;

public class MovimientoInventarioModel {

    public boolean registrar(MovimientoInventario movimiento) {
        String insertar = """
            INSERT INTO movimiento_inventario (id_producto, tipo_movimiento, cantidad, motivo, fecha, observacion)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        String actualizarStock = """
            UPDATE producto
            SET stock_actual = stock_actual + ?
            WHERE id_producto = ? AND (? > 0 OR stock_actual >= ?)
            """;
        int variacion = calcularVariacion(movimiento);

        try (Connection cn = ConexionRepository.getConexion()) {
            cn.setAutoCommit(false);
            try (PreparedStatement psStock = cn.prepareStatement(actualizarStock);
                 PreparedStatement psInsertar = cn.prepareStatement(insertar)) {
                psStock.setInt(1, variacion);
                psStock.setInt(2, movimiento.getIdProducto());
                psStock.setInt(3, variacion);
                psStock.setInt(4, movimiento.getCantidad());

                if (psStock.executeUpdate() == 0) {
                    cn.rollback();
                    return false;
                }

                psInsertar.setInt(1, movimiento.getIdProducto());
                psInsertar.setString(2, movimiento.getTipoMovimiento());
                psInsertar.setInt(3, movimiento.getCantidad());
                psInsertar.setString(4, movimiento.getMotivo());
                psInsertar.setDate(5, Date.valueOf(movimiento.getFecha()));
                psInsertar.setString(6, movimiento.getObservacion());
                boolean ok = psInsertar.executeUpdate() > 0;
                if (ok) cn.commit(); else cn.rollback();
                return ok;
            } catch (Exception e) {
                cn.rollback();
                e.printStackTrace();
            } finally {
                cn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<MovimientoInventario> listar() {
        List<MovimientoInventario> lista = new ArrayList<>();
        String sql = """
            SELECT m.id_movimiento, m.tipo_movimiento, m.cantidad, m.motivo, m.fecha, m.observacion,
                   p.id_producto, p.codigo, p.nombre
            FROM movimiento_inventario m
            JOIN producto p ON m.id_producto = p.id_producto
            ORDER BY m.fecha DESC, m.id_movimiento DESC
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MovimientoInventario m = new MovimientoInventario();
                m.setId(String.valueOf(rs.getInt("id_movimiento")));
                m.setTipoMovimiento(rs.getString("tipo_movimiento"));
                m.setCantidad(rs.getInt("cantidad"));
                m.setMotivo(rs.getString("motivo"));
                m.setFecha(rs.getDate("fecha").toLocalDate());
                m.setObservacion(rs.getString("observacion"));
                m.setIdProducto(rs.getInt("id_producto"));
                m.setCodigoProducto(rs.getString("codigo"));
                m.setNombreProducto(rs.getString("nombre"));
                lista.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    private int calcularVariacion(MovimientoInventario movimiento) {
        int cantidad = movimiento.getCantidad() == null ? 0 : movimiento.getCantidad();
        String tipo = movimiento.getTipoMovimiento() == null ? "" : movimiento.getTipoMovimiento();
        String motivo = movimiento.getMotivo() == null ? "" : movimiento.getMotivo();
        if ("Salida".equalsIgnoreCase(tipo)) return -cantidad;
        if ("Ajuste".equalsIgnoreCase(tipo) && motivo.toLowerCase().contains("negativo")) return -cantidad;
        return cantidad;
    }
}
