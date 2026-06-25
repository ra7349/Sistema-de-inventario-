package org.Kardex.jF.model;

import java.sql.*;
import java.util.*;
import org.Kardex.jF.bean.entity.Categoria;
import org.Kardex.jF.persistence.ConexionRepository;
import org.Kardex.jF.usecase.CRUDUsecase;

public class CategoriaModel implements CRUDUsecase<Categoria> {
    public boolean insertar(Categoria c) { return ejecutar(c, "INSERT INTO categoria (codigo,nombre,descripcion,estado) VALUES (?,?,?,?)", false); }
    public List<Categoria> listar() {
        List<Categoria> lista = new ArrayList<>();
        try (Connection cn = ConexionRepository.getConexion(); PreparedStatement ps = cn.prepareStatement("SELECT * FROM categoria ORDER BY id_categoria"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { Categoria c = new Categoria(); c.setIdCategoria(rs.getInt("id_categoria")); c.setCodigo(rs.getString("codigo")); c.setNombre(rs.getString("nombre")); c.setDescripcion(rs.getString("descripcion")); c.setEstado(rs.getString("estado")); lista.add(c); }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }
    public boolean actualizar(Categoria c) { return ejecutar(c, "UPDATE categoria SET codigo=?,nombre=?,descripcion=?,estado=? WHERE id_categoria=?", true); }
    public boolean eliminar(Integer id) {
        try (Connection cn = ConexionRepository.getConexion(); PreparedStatement ps = cn.prepareStatement("DELETE FROM categoria WHERE id_categoria=?")) { ps.setInt(1, id); return ps.executeUpdate() > 0; } catch (Exception e) { e.printStackTrace(); return false; }
    }
    private boolean ejecutar(Categoria c, String sql, boolean id) {
        try (Connection cn = ConexionRepository.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1,c.getCodigo()); ps.setString(2,c.getNombre()); ps.setString(3,c.getDescripcion()); ps.setString(4,c.getEstado()); if (id) ps.setInt(5,c.getIdCategoria()); return ps.executeUpdate()>0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}
