package org.Kardex.jF.model;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import org.Kardex.jF.bean.entity.Equipo;
import org.Kardex.jF.persistence.ConexionRepository;
import org.Kardex.jF.usecase.CRUDUsecase;

public class EquipoModel implements CRUDUsecase<Equipo> {

    @Override
    public boolean insertar(Equipo e) {
        String sql = """
            INSERT INTO equipo (codigo,marca,modelo,numero_serie,tipo_equipo,estado,fecha_ingreso,id_cliente)
            VALUES (?,?,?,?,?,?,?,?)
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, e.getCodigo());
            ps.setString(2, e.getMarca());
            ps.setString(3, e.getModelo());
            ps.setString(4, e.getNumeroSerie());
            ps.setString(5, e.getTipoEquipo());
            ps.setBoolean(6, e.getEstado() != null ? e.getEstado() : true);
            ps.setDate(7, e.getFechaIngreso() != null
                    ? Date.valueOf(e.getFechaIngreso()) : Date.valueOf(java.time.LocalDate.now()));
            ps.setInt(8, e.getIdCliente());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) { ex.printStackTrace(); }
        return false;
    }

    @Override
    public List<Equipo> listar() {
        List<Equipo> lista = new ArrayList<>();
        String sql = """
            SELECT eq.*, c.nombre||' '||c.apellido AS nombre_cliente
            FROM equipo eq
            JOIN cliente c ON eq.id_cliente = c.id_cliente
            ORDER BY eq.id_equipo
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (Exception ex) { ex.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean actualizar(Equipo e) {
        String sql = """
            UPDATE equipo SET codigo=?,marca=?,modelo=?,numero_serie=?,
            tipo_equipo=?,estado=?,id_cliente=? WHERE id_equipo=?
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, e.getCodigo());
            ps.setString(2, e.getMarca());
            ps.setString(3, e.getModelo());
            ps.setString(4, e.getNumeroSerie());
            ps.setString(5, e.getTipoEquipo());
            ps.setBoolean(6, e.getEstado() != null ? e.getEstado() : true);
            ps.setInt(7, e.getIdCliente());
            ps.setInt(8, Integer.parseInt(e.getId()));
            return ps.executeUpdate() > 0;
        } catch (Exception ex) { ex.printStackTrace(); }
        return false;
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM equipo WHERE id_equipo=?";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) { ex.printStackTrace(); }
        return false;
    }

    public Equipo buscarPorCodigo(String codigo) {
        String sql = """
            SELECT eq.*, c.nombre||' '||c.apellido AS nombre_cliente
            FROM equipo eq JOIN cliente c ON eq.id_cliente=c.id_cliente
            WHERE eq.codigo=?
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        return null;
    }

    public String generarSiguienteCodigo() {
        return CodigoAutomaticoModel.generarSiguienteCodigo("equipo", "codigo", "E");
    }

    private Equipo mapear(ResultSet rs) throws Exception {
        Equipo e = new Equipo();
        e.setId(String.valueOf(rs.getInt("id_equipo")));
        e.setCodigo(rs.getString("codigo"));
        e.setMarca(rs.getString("marca"));
        e.setModelo(rs.getString("modelo"));
        e.setNumeroSerie(rs.getString("numero_serie"));
        e.setTipoEquipo(rs.getString("tipo_equipo"));
        e.setEstado(rs.getBoolean("estado"));
        Date d = rs.getDate("fecha_ingreso");
        if (d != null) e.setFechaIngreso(d.toLocalDate());
        e.setIdCliente(rs.getInt("id_cliente"));
        try { e.setNombreCliente(rs.getString("nombre_cliente")); } catch (Exception ignored) {}
        return e;
    }
    
    public static Equipo obtenerEquipoPorCliente(String idCliente) {
    	// Si el ID está vacío (como el cliente por defecto), no buscamos nada
        if (idCliente == null || idCliente.isEmpty()) {
            return null;
        }

        // Tu consulta adaptada a los nombres exactos de tu tabla 'equipo'
        String sql = "SELECT marca, modelo, numero_serie FROM equipo WHERE id_cliente = ? LIMIT 1";
        
        try (Connection cn = ConexionRepository.getConexion(); // Usa tu clase de conexión aquí
             PreparedStatement pst = cn.prepareStatement(sql)) {
            
            // 🔄 Convertimos el String de Java al INTEGER que requiere tu BD
            int idClienteInt = Integer.parseInt(idCliente);
            pst.setInt(1, idClienteInt);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    // Creamos el objeto Equipo con las columnas exactas de tu tabla
                    return new Equipo(rs.getString("marca"), rs.getString("modelo"), rs.getString("numero_serie"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error SQL al buscar equipo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("El ID del cliente no es un número válido: " + e.getMessage());
        }
        return null;
    }
}
