package org.Kardex.jF.model;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import org.Kardex.jF.bean.entity.Cliente;
import org.Kardex.jF.bean.entity.OrdenServicio;
import org.Kardex.jF.bean.entity.Servicio;
import org.Kardex.jF.persistence.ConexionRepository;
import org.Kardex.jF.usecase.CRUDUsecase;

public class OrdenServicioModel implements CRUDUsecase<OrdenServicio> {

    @Override
    public boolean insertar(OrdenServicio o) {
        if (o.getIdCliente() == null || o.getIdServicio() == null) {
            return false;
        }

        Cliente cliente = new Cliente();
        cliente.setId(String.valueOf(o.getIdCliente()));

        Servicio servicio = new Servicio();
        servicio.setIdServicio(o.getIdServicio());
        servicio.setPrecio(o.getCostoEstimado());

        return new ClienteServicioModel().aplicarServicio(
                cliente,
                servicio,
                o.getCodigo(),
                o.getCodigoEquipo(),
                o.getDescripcionFalla(),
                normalizarEstado(o.getEstado()));
    }

    @Override
    public List<OrdenServicio> listar() {
        List<OrdenServicio> lista = new ArrayList<>();
        String sql = """
            SELECT cs.id_cliente_servicio,
                   cs.numero_orden,
                   cs.equipo,
                   cs.falla,
                   cs.estado,
                   cs.precio_unitario,
                   cs.fecha_aplicacion,
                   cs.fecha_facturacion,
                   cs.id_cliente,
                   cs.id_servicio,
                   c.nombre || ' ' || COALESCE(c.apellido, '') AS nombre_cliente,
                   s.codigo AS codigo_servicio,
                   s.servicio AS nombre_servicio
            FROM cliente_servicio cs
            JOIN cliente c ON cs.id_cliente = c.id_cliente
            JOIN servicio s ON cs.id_servicio = s.id_servicio
            ORDER BY cs.id_cliente_servicio DESC
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean actualizar(OrdenServicio o) {
        String sql = """
            UPDATE cliente_servicio
            SET numero_orden=?, equipo=?, falla=?, estado=?, precio_unitario=?, fecha_facturacion=?
            WHERE id_cliente_servicio=?
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, o.getCodigo());
            ps.setString(2, o.getCodigoEquipo());
            ps.setString(3, o.getDescripcionFalla());
            ps.setString(4, normalizarEstado(o.getEstado()));
            ps.setDouble(5, o.getCostoFinal() != null ? o.getCostoFinal() : o.getCostoEstimado());
            if (o.getFechaCierre() != null) ps.setDate(6, Date.valueOf(o.getFechaCierre()));
            else ps.setNull(6, Types.DATE);
            ps.setInt(7, Integer.parseInt(o.getId()));
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM cliente_servicio WHERE id_cliente_servicio=?";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public OrdenServicio buscarPorCodigo(String codigo) {
        String sql = """
            SELECT cs.id_cliente_servicio,
                   cs.numero_orden,
                   cs.equipo,
                   cs.falla,
                   cs.estado,
                   cs.precio_unitario,
                   cs.fecha_aplicacion,
                   cs.fecha_facturacion,
                   cs.id_cliente,
                   cs.id_servicio,
                   c.nombre || ' ' || COALESCE(c.apellido, '') AS nombre_cliente,
                   s.codigo AS codigo_servicio,
                   s.servicio AS nombre_servicio
            FROM cliente_servicio cs
            JOIN cliente c ON cs.id_cliente = c.id_cliente
            JOIN servicio s ON cs.id_servicio = s.id_servicio
            WHERE cs.numero_orden=?
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<String> listarCodigosOrden() {
        List<String> codigos = new ArrayList<>();
        String sql = """
            SELECT DISTINCT numero_orden
            FROM cliente_servicio
            WHERE numero_orden IS NOT NULL AND TRIM(numero_orden) <> ''
            ORDER BY numero_orden
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) codigos.add(rs.getString("numero_orden"));
        } catch (Exception e) { e.printStackTrace(); }
        return codigos;
    }

    public String generarSiguienteCodigo() {
        String sql = """
            SELECT COALESCE(MAX(CAST(SUBSTRING(numero_orden FROM 3) AS INTEGER)), 0) + 1 AS siguiente
            FROM cliente_servicio
            WHERE numero_orden ~ '^OS[0-9]+$'
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return String.format("OS%03d", rs.getInt("siguiente"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "OS001";
    }

    private OrdenServicio mapear(ResultSet rs) throws Exception {
        OrdenServicio o = new OrdenServicio();
        o.setId(String.valueOf(rs.getInt("id_cliente_servicio")));
        o.setCodigo(rs.getString("numero_orden"));
        o.setDescripcionFalla(rs.getString("falla"));
        o.setDiagnostico("");
        o.setSolucion("");
        o.setEstado(rs.getString("estado"));
        o.setCostoEstimado(rs.getDouble("precio_unitario"));
        o.setCostoFinal(rs.getObject("precio_unitario") != null ? rs.getDouble("precio_unitario") : null);
        Timestamp fa = rs.getTimestamp("fecha_aplicacion");
        if (fa != null) o.setFechaApertura(fa.toLocalDateTime().toLocalDate());
        Timestamp fc = rs.getTimestamp("fecha_facturacion");
        if (fc != null) o.setFechaCierre(fc.toLocalDateTime().toLocalDate());
        o.setIdCliente(rs.getInt("id_cliente"));
        o.setIdServicio(rs.getInt("id_servicio"));
        o.setCodigoEquipo(rs.getString("equipo"));
        o.setNombreCliente(rs.getString("nombre_cliente").trim());
        o.setNombreServicio(rs.getString("nombre_servicio"));
        o.setNombreTecnico(rs.getString("nombre_servicio"));
        return o;
    }

    private String normalizarEstado(String estado) {
        return estado != null && !estado.isBlank() ? estado : "Pendiente";
    }
}
