package org.Kardex.jF.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.Kardex.jF.bean.entity.Repuesto;
import org.Kardex.jF.persistence.ConexionRepository;
import org.Kardex.jF.usecase.CRUDUsecase;

public class RepuestoModel implements CRUDUsecase<Repuesto> {


    @Override
    public boolean insertar(Repuesto r) {
        String sql = """
            INSERT INTO repuesto (codigo,nombre,marca,categoria,stock,precio_compra,precio_venta,estado)
            VALUES (?,?,?,?,?,?,?,?)
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            prepararParametros(ps, r, false);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public List<Repuesto> listar() {
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT * FROM repuesto ORDER BY id_repuesto";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean actualizar(Repuesto r) {
        String sql = """
            UPDATE repuesto SET codigo=?,nombre=?,marca=?,categoria=?,stock=?,precio_compra=?,precio_venta=?,estado=?
            WHERE id_repuesto=?
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            prepararParametros(ps, r, true);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM repuesto WHERE id_repuesto=?";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public Repuesto buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM repuesto WHERE UPPER(codigo)=UPPER(?)";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public String generarSiguienteCodigo() {
        String sql = """
            SELECT COALESCE(MAX(CAST(SUBSTRING(codigo FROM 2) AS INTEGER)), 0) + 1 AS siguiente
            FROM repuesto
            WHERE codigo ~ '^R[0-9]+$'
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return String.format("R%03d", rs.getInt("siguiente"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "R001";
    }

    private void prepararParametros(PreparedStatement ps, Repuesto r, boolean incluirId) throws SQLException {
        ps.setString(1, r.getCodigo());
        ps.setString(2, r.getNombre());
        ps.setString(3, r.getMarca());
        ps.setString(4, r.getCategoria());
        ps.setInt(5, r.getStock() != null ? r.getStock() : 0);
        ps.setDouble(6, r.getPrecioCompra());
        ps.setDouble(7, r.getPrecioVenta());
        ps.setString(8, r.getEstado());
        if (incluirId) ps.setInt(9, Integer.parseInt(r.getId()));
    }

    private Repuesto mapear(ResultSet rs) throws SQLException {
        Repuesto r = new Repuesto();
        r.setId(String.valueOf(rs.getInt("id_repuesto")));
        r.setCodigo(rs.getString("codigo"));
        r.setNombre(rs.getString("nombre"));
        r.setMarca(rs.getString("marca"));
        r.setCategoria(rs.getString("categoria"));
        r.setStock(rs.getInt("stock"));
        r.setPrecioCompra(rs.getDouble("precio_compra"));
        r.setPrecioVenta(rs.getDouble("precio_venta"));
        r.setEstado(rs.getString("estado"));
        return r;
    }
}
