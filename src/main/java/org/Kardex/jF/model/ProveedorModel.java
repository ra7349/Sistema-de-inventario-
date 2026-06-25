package org.Kardex.jF.model;

import java.sql.*;
import java.util.*;
import org.Kardex.jF.bean.entity.Proveedor;
import org.Kardex.jF.persistence.ConexionRepository;
import org.Kardex.jF.usecase.CRUDUsecase;

public class ProveedorModel implements CRUDUsecase<Proveedor> {

    @Override
    public boolean insertar(Proveedor p) {
        String sql = """
            INSERT INTO proveedor (codigo,razon_social,ruc,telefono,correo,direccion,contacto,estado)
            VALUES (?,?,?,?,?,?,?,?)
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getRazonSocial());
            if (p.getRuc() != null) ps.setLong(3, p.getRuc());
            else ps.setNull(3, Types.BIGINT);
            if (p.getTelefono() != null) ps.setLong(4, p.getTelefono());
            else ps.setNull(4, Types.BIGINT);
            ps.setString(5, p.getCorreo());
            ps.setString(6, p.getDireccion());
            ps.setString(7, p.getContacto());
            ps.setString(8, p.getEstado());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public List<Proveedor> listar() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedor ORDER BY id_proveedor";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean actualizar(Proveedor p) {
        String sql = """
            UPDATE proveedor SET codigo=?,razon_social=?,ruc=?,telefono=?,
            correo=?,direccion=?,contacto=?,estado=? WHERE id_proveedor=?
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getRazonSocial());
            if (p.getRuc() != null) ps.setLong(3, p.getRuc());
            else ps.setNull(3, Types.BIGINT);
            if (p.getTelefono() != null) ps.setLong(4, p.getTelefono());
            else ps.setNull(4, Types.BIGINT);
            ps.setString(5, p.getCorreo());
            ps.setString(6, p.getDireccion());
            ps.setString(7, p.getContacto());
            ps.setString(8, p.getEstado());
            ps.setInt(9, Integer.parseInt(p.getId()));
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM proveedor WHERE id_proveedor=?";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public Proveedor buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM proveedor WHERE codigo=?";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<Proveedor> buscar(String filtro) {
        List<Proveedor> lista = new ArrayList<>();
        String texto = filtro == null ? "" : filtro.trim();
        String sql = """
            SELECT * FROM proveedor
            WHERE (? = ''
                OR LOWER(codigo) LIKE LOWER(?)
                OR LOWER(razon_social) LIKE LOWER(?)
                OR CAST(COALESCE(ruc, 0) AS TEXT) LIKE ?
                OR CAST(COALESCE(telefono, 0) AS TEXT) LIKE ?
                OR LOWER(COALESCE(correo, '')) LIKE LOWER(?)
                OR LOWER(COALESCE(direccion, '')) LIKE LOWER(?)
                OR LOWER(COALESCE(contacto, '')) LIKE LOWER(?)
                OR LOWER(estado) LIKE LOWER(?))
            ORDER BY id_proveedor
            """;
        String patron = "%" + texto + "%";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, texto);
            for (int i = 2; i <= 9; i++) ps.setString(i, patron);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public String generarSiguienteCodigo() {
        return CodigoAutomaticoModel.generarSiguienteCodigo("proveedor", "codigo", "P");
    }

    private Proveedor mapear(ResultSet rs) throws Exception {
        Proveedor p = new Proveedor();
        p.setId(String.valueOf(rs.getInt("id_proveedor")));
        p.setCodigo(rs.getString("codigo"));
        p.setRazonSocial(rs.getString("razon_social"));
        p.setRuc(rs.getObject("ruc") != null ? rs.getLong("ruc") : null);
        p.setTelefono(rs.getObject("telefono") != null ? rs.getLong("telefono") : null);
        p.setCorreo(rs.getString("correo"));
        p.setDireccion(rs.getString("direccion"));
        p.setContacto(rs.getString("contacto"));
        p.setEstado(rs.getString("estado"));
        return p;
    }
}
