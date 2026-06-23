package org.Kardex.jF.model;

import java.sql.*;

import java.util.*;
import org.Kardex.jF.bean.entity.Cliente;
import org.Kardex.jF.persistence.ConexionRepository;
import org.Kardex.jF.usecase.CRUDUsecase;

public class ClienteModel implements CRUDUsecase<Cliente> {

    @Override
    public boolean insertar(Cliente c) {
        String sql = """
            INSERT INTO cliente (codigo,nombre,apellido,telefono,correo,direccion,tipo_cliente,ruc)
            VALUES (?,?,?,?,?,?,?,?)
            """;
        try (Connection cn = ConexionRepository.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getCodigo());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getApellido());
            if (c.getTelefono() != null) ps.setLong(4, c.getTelefono());
            else ps.setNull(4, Types.BIGINT);
            ps.setString(5, c.getCorreo());
            ps.setString(6, c.getDireccion());
            ps.setString(7, c.getTipoCliente());
            if (c.getRUC() != null) ps.setLong(8, c.getRUC());
            else ps.setNull(8, Types.BIGINT);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente ORDER BY id_cliente";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean actualizar(Cliente c) {
        String sql = """
            UPDATE cliente SET codigo=?,nombre=?,apellido=?,telefono=?,
            correo=?,direccion=?,tipo_cliente=?,ruc=? WHERE id_cliente=?
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getCodigo());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getApellido());
            if (c.getTelefono() != null) ps.setLong(4, c.getTelefono());
            else ps.setNull(4, Types.BIGINT);
            ps.setString(5, c.getCorreo());
            ps.setString(6, c.getDireccion());
            ps.setString(7, c.getTipoCliente());
            if (c.getRUC() != null) ps.setLong(8, c.getRUC());
            else ps.setNull(8, Types.BIGINT);
            ps.setInt(9, Integer.parseInt(c.getId()));
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM cliente WHERE id_cliente=?";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public Cliente buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM cliente WHERE codigo=?";
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
        return CodigoAutomaticoModel.generarSiguienteCodigo("cliente", "codigo", "C");
    }

    private Cliente mapear(ResultSet rs) throws Exception {
        Cliente c = new Cliente();
        c.setId(String.valueOf(rs.getInt("id_cliente")));
        c.setCodigo(rs.getString("codigo"));
        c.setNombre(rs.getString("nombre"));
        c.setApellido(rs.getString("apellido"));
        c.setTelefono(rs.getObject("telefono") != null ? rs.getLong("telefono") : null);
        c.setCorreo(rs.getString("correo"));
        c.setDireccion(rs.getString("direccion"));
        c.setTipoCliente(rs.getString("tipo_cliente"));
        c.setRUC(rs.getObject("ruc") != null ? rs.getLong("ruc") : null);
        return c;
    }
}
