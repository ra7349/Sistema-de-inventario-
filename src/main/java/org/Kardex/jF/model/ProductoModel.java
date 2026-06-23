package org.Kardex.jF.model;

import java.sql.*;
import java.util.*;
import org.Kardex.jF.bean.entity.Producto;
import org.Kardex.jF.persistence.ConexionRepository;
import org.Kardex.jF.usecase.CRUDUsecase;

public class ProductoModel implements CRUDUsecase<Producto> {

    @Override
    public boolean insertar(Producto p) {
        String sql = """
            INSERT INTO producto (codigo,nombre,descripcion,precio,stock,categoria)
            VALUES (?,?,?,?,?,?)
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock() != null ? p.getStock() : 0);
            ps.setString(6, p.getCategoria());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto ORDER BY id_producto";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean actualizar(Producto p) {
        String sql = """
            UPDATE producto SET codigo=?,nombre=?,descripcion=?,precio=?,stock=?,categoria=?
            WHERE id_producto=?
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock() != null ? p.getStock() : 0);
            ps.setString(6, p.getCategoria());
            ps.setInt(7, Integer.parseInt(p.getId()));
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM producto WHERE id_producto=?";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public Producto buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM producto WHERE codigo=?";
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
        return CodigoAutomaticoModel.generarSiguienteCodigo("producto", "codigo", "P");
    }

    private Producto mapear(ResultSet rs) throws Exception {
        Producto p = new Producto();
        p.setId(String.valueOf(rs.getInt("id_producto")));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setCategoria(rs.getString("categoria"));
        return p;
    }
}
