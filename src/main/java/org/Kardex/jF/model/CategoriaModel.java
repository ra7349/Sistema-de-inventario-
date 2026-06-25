package org.Kardex.jF.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.Kardex.jF.bean.entity.Categoria;
import org.Kardex.jF.persistence.ConexionRepository;
import org.Kardex.jF.usecase.CRUDUsecase;

public class CategoriaModel implements CRUDUsecase<Categoria> {

    @Override
    public boolean insertar(Categoria c) {
        String sql = "INSERT INTO categoria (codigo, nombre, descripcion, estado) VALUES (?, ?, ?, ?)";
        return ejecutar(c, sql, false);
    }

    @Override
    public List<Categoria> listar() {
        return consultar("SELECT * FROM categoria ORDER BY id_categoria", null);
    }

    public List<Categoria> listarActivas() {
        return consultar("SELECT * FROM categoria WHERE UPPER(estado)=UPPER(?) ORDER BY nombre", "Activo");
    }

    public List<Categoria> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) return listar();
        return consultar("SELECT * FROM categoria WHERE UPPER(nombre) LIKE UPPER(?) ORDER BY nombre", "%" + nombre.trim() + "%");
    }

    public Categoria buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) return null;
        List<Categoria> resultados = consultar("SELECT * FROM categoria WHERE UPPER(codigo)=UPPER(?)", codigo.trim());
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    @Override
    public boolean actualizar(Categoria c) {
        String sql = "UPDATE categoria SET codigo=?, nombre=?, descripcion=?, estado=? WHERE id_categoria=?";
        return ejecutar(c, sql, true);
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM categoria WHERE id_categoria=?";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generarSiguienteCodigo() {
        return CodigoAutomaticoModel.generarSiguienteCodigo("categoria", "codigo", "CAT");
    }

    private boolean ejecutar(Categoria c, String sql, boolean incluirId) {
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getCodigo());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getDescripcion());
            ps.setString(4, c.getEstado());
            if (incluirId) ps.setInt(5, c.getIdCategoria());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Categoria> consultar(String sql, String parametro) {
        List<Categoria> lista = new ArrayList<>();
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            if (parametro != null) ps.setString(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setIdCategoria(rs.getInt("id_categoria"));
        c.setCodigo(rs.getString("codigo"));
        c.setNombre(rs.getString("nombre"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setEstado(rs.getString("estado"));
        return c;
    }
}
