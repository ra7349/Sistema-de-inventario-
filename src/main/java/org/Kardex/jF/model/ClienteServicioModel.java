package org.Kardex.jF.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;

import org.Kardex.jF.bean.entity.Cliente;
import org.Kardex.jF.bean.entity.Servicio;
import org.Kardex.jF.persistence.ConexionRepository;

public class ClienteServicioModel {

    public boolean aplicarServicio(Cliente cliente, Servicio servicio, String numeroOrden, String equipo,
            String falla, String estado) {
        String sql = """
                INSERT INTO cliente_servicio
                    (id_cliente, id_servicio, numero_orden, equipo, falla, estado, precio_unitario)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(cliente.getId()));
            ps.setInt(2, servicio.getIdServicio());
            ps.setString(3, numeroOrden);
            ps.setString(4, equipo);
            ps.setString(5, falla);
            ps.setString(6, estado);

            if (servicio.getPrecio() != null) {
                ps.setDouble(7, servicio.getPrecio());
            } else {
                ps.setNull(7, Types.NUMERIC);
            }

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean actualizarEstado(String numeroOrden, String estado) {
        String sql = """
                UPDATE cliente_servicio
                SET estado = ?
                WHERE numero_orden = ?
                """;

        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setString(2, numeroOrden);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
