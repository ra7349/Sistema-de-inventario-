package org.Kardex.jF.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.Kardex.jF.bean.entity.Servicio;
import org.Kardex.jF.persistence.ConexionRepository;
import org.Kardex.jF.usecase.CRUDUsecase;

public class ServiciosModel implements CRUDUsecase<Servicio> {

	@Override
	public boolean insertar(Servicio s) {
		String sql = """
		        INSERT INTO servicio (codigo, servicio, precio, estado)
		        VALUES (?, ?, ?, ?)
		        """;
		try (Connection cn = ConexionRepository.getConexion();
		        PreparedStatement ps = cn.prepareStatement(sql)) {
		    ps.setString(1, s.getCodigo());
		    ps.setString(2, s.getDescripcion());
		    ps.setDouble(3, s.getPrecio());
		    ps.setString(4, s.getEstado());
		    return ps.executeUpdate() > 0;
		} catch (Exception e) { e.printStackTrace(); }
		return false;
	}

	@Override
	public List<Servicio> listar() {
	    List<Servicio> lista = new ArrayList<>();
	    String sql = "SELECT * FROM servicio";

	    try (Connection cn = ConexionRepository.getConexion();
	         PreparedStatement ps = cn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	    	while (rs.next()) {

	    	    Servicio s = new Servicio();

	    	    s.setIdServicio(rs.getInt("id_servicio"));
	    	    s.setCodigo(rs.getString("codigo"));
	    	    s.setDescripcion(rs.getString("servicio"));
	    	    s.setPrecio(rs.getDouble("precio"));
	    	    s.setEstado(rs.getString("estado"));

	    	    lista.add(s);
	    	}

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return lista;
	}

	@Override
	public boolean actualizar(Servicio s) {
		String sql = """
		        UPDATE servicio
		        SET codigo  = ?,
		            servicio = ?,
		            precio  = ?,
		            estado  = ?
		        WHERE id_servicio = ?
		        """;

		try (Connection cn = ConexionRepository.getConexion();
		     PreparedStatement ps = cn.prepareStatement(sql)) {

		    ps.setString(1, s.getCodigo());
		    ps.setString(2, s.getDescripcion());
		    ps.setDouble(3, s.getPrecio());
		    ps.setString(4, s.getEstado());
		    ps.setInt   (5, s.getIdServicio());

		    return ps.executeUpdate() > 0;

		} catch (Exception e) {
		    e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean eliminar(Integer id) {
		String sql = "DELETE FROM servicio WHERE id_servicio=?";
		try (Connection cn = ConexionRepository.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

    public Servicio buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM servicio WHERE codigo = ?";

        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Servicio s = new Servicio();
                    s.setIdServicio(rs.getInt("id_servicio"));
                    s.setCodigo(rs.getString("codigo"));
                    s.setDescripcion(rs.getString("servicio"));
                    s.setPrecio(rs.getDouble("precio"));
                    s.setEstado(rs.getString("estado"));
                    return s;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public String generarSiguienteCodigo() {
        String sql = """
            SELECT COALESCE(MAX(CAST(SUBSTRING(codigo FROM 2) AS INTEGER)), 0) + 1 AS siguiente
            FROM servicio
            WHERE codigo ~ '^S[0-9]+$'
            """;
        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return String.format("S%03d", rs.getInt("siguiente"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "S001";
    }

}
