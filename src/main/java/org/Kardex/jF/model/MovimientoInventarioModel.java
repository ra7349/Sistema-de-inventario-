package org.Kardex.jF.model;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.Kardex.jF.bean.entity.MovimientoInventario;
import org.Kardex.jF.persistence.ConexionRepository;

public class MovimientoInventarioModel {


    public boolean registrar(MovimientoInventario movimiento) {
        String insertar = """
            INSERT INTO movimiento_inventario (id_repuesto, tipo, cantidad, motivo, fecha)
            VALUES (?, ?, ?, ?, ?)
            """;
        String actualizarStock = """
            UPDATE repuesto
            SET stock = stock + ?
            WHERE id_repuesto = ? AND (? > 0 OR stock >= ?)
            """;
        int variacion = "Entrada".equals(movimiento.getTipo()) ? movimiento.getCantidad() : -movimiento.getCantidad();

        try (Connection cn = ConexionRepository.getConexion()) {
            cn.setAutoCommit(false);
            try (PreparedStatement psStock = cn.prepareStatement(actualizarStock);
                 PreparedStatement psInsertar = cn.prepareStatement(insertar)) {
                psStock.setInt(1, variacion);
                psStock.setInt(2, movimiento.getIdRepuesto());
                psStock.setInt(3, variacion);
                psStock.setInt(4, movimiento.getCantidad());

                if (psStock.executeUpdate() == 0) {
                    cn.rollback();
                    return false;
                }

                psInsertar.setInt(1, movimiento.getIdRepuesto());
                psInsertar.setString(2, movimiento.getTipo());
                psInsertar.setInt(3, movimiento.getCantidad());
                psInsertar.setString(4, movimiento.getMotivo());
                psInsertar.setDate(5, Date.valueOf(movimiento.getFecha()));
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
            SELECT m.id_movimiento, m.tipo, m.cantidad, m.motivo, m.fecha,
                   r.id_repuesto, r.codigo, r.nombre
            FROM movimiento_inventario m
            JOIN repuesto r ON m.id_repuesto = r.id_repuesto
            ORDER BY m.fecha DESC, m.id_movimiento DESC
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MovimientoInventario m = new MovimientoInventario();
                m.setId(String.valueOf(rs.getInt("id_movimiento")));
                m.setTipo(rs.getString("tipo"));
                m.setCantidad(rs.getInt("cantidad"));
                m.setMotivo(rs.getString("motivo"));
                m.setFecha(rs.getDate("fecha").toLocalDate());
                m.setIdRepuesto(rs.getInt("id_repuesto"));
                m.setCodigoRepuesto(rs.getString("codigo"));
                m.setNombreRepuesto(rs.getString("nombre"));
                lista.add(m);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }
}
