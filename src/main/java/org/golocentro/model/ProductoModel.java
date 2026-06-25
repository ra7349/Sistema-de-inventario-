package org.golocentro.model;

import java.sql.*;
import java.util.*;
import org.golocentro.bean.entity.Producto;
import org.golocentro.persistence.ConexionRepository;
import org.golocentro.usecase.CRUDUsecase;

public class ProductoModel implements CRUDUsecase<Producto> {

    @Override
    public boolean insertar(Producto p) {
        String sql = """
            INSERT INTO producto (codigo,nombre,categoria,presentacion,unidad_medida,precio_compra,
                                  precio_minorista,precio_mayorista,stock_actual,stock_minimo,estado)
            VALUES (?,?,?,?,?,?,?,?,?,?,?)
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            prepararParametros(ps, p, false);
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

    public List<Producto> buscar(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) return listar();
        List<Producto> lista = new ArrayList<>();
        String sql = """
            SELECT * FROM producto
            WHERE UPPER(nombre) LIKE UPPER(?)
               OR UPPER(codigo) LIKE UPPER(?)
               OR UPPER(categoria) LIKE UPPER(?)
            ORDER BY id_producto
            """;
        String patron = "%" + filtro.trim() + "%";
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            ps.setString(3, patron);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean actualizar(Producto p) {
        String sql = """
            UPDATE producto SET codigo=?,nombre=?,categoria=?,presentacion=?,unidad_medida=?,precio_compra=?,
                                precio_minorista=?,precio_mayorista=?,stock_actual=?,stock_minimo=?,estado=?
            WHERE id_producto=?
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            prepararParametros(ps, p, true);
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
        String sql = "SELECT * FROM producto WHERE UPPER(codigo)=UPPER(?)";
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
        return CodigoAutomaticoModel.generarSiguienteCodigo("producto", "codigo", "PROD");
    }

    private void prepararParametros(PreparedStatement ps, Producto p, boolean incluirId) throws SQLException {
        ps.setString(1, p.getCodigo());
        ps.setString(2, p.getNombre());
        ps.setString(3, p.getCategoria());
        ps.setString(4, p.getPresentacion());
        ps.setString(5, p.getUnidadMedida());
        ps.setDouble(6, p.getPrecioCompra());
        ps.setDouble(7, p.getPrecioMinorista());
        ps.setDouble(8, p.getPrecioMayorista());
        ps.setInt(9, p.getStockActual() != null ? p.getStockActual() : 0);
        ps.setInt(10, p.getStockMinimo() != null ? p.getStockMinimo() : 0);
        ps.setString(11, p.getEstado());
        if (incluirId) ps.setInt(12, p.getIdProducto());
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("id_producto"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setCategoria(rs.getString("categoria"));
        p.setPresentacion(rs.getString("presentacion"));
        p.setUnidadMedida(rs.getString("unidad_medida"));
        p.setPrecioCompra(rs.getDouble("precio_compra"));
        p.setPrecioMinorista(rs.getDouble("precio_minorista"));
        p.setPrecioMayorista(rs.getDouble("precio_mayorista"));
        p.setStockActual(rs.getInt("stock_actual"));
        p.setStockMinimo(rs.getInt("stock_minimo"));
        p.setEstado(rs.getString("estado"));
        return p;
    }
}
