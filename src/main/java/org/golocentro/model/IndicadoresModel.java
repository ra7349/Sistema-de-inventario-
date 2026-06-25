package org.golocentro.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import org.golocentro.bean.entity.Indicadores;
import org.golocentro.persistence.ConexionRepository;
import org.golocentro.usecase.IndicadoresUsecase;

public class IndicadoresModel implements IndicadoresUsecase {
    @Override
    public Indicadores obtenerIndicadores() {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate inicioMesSiguiente = inicioMes.plusMonths(1);
        try (Connection cn = ConexionRepository.getConexion()) {
            if (cn == null) return new Indicadores();
            return new Indicadores(
                    consultarEntero(cn, "SELECT COUNT(*) FROM producto WHERE COALESCE(stock_actual,0) <= COALESCE(stock_minimo,10)"),
                    consultarEntero(cn, "SELECT COUNT(*) FROM ventas WHERE UPPER(COALESCE(estado,'')) <> 'ANULADO'"),
                    contarMovimientosDelMes(cn, inicioMes, inicioMesSiguiente),
                    consultarEntero(cn, "SELECT COUNT(*) FROM cliente"),
                    sumarIngresosDelMes(cn, inicioMes, inicioMesSiguiente));
        } catch (Exception e) { e.printStackTrace(); }
        return new Indicadores();
    }

    private int contarMovimientosDelMes(Connection cn, LocalDate inicioMes, LocalDate inicioMesSiguiente) throws Exception {
        String sql = "SELECT COUNT(*) FROM movimiento_inventario WHERE fecha >= ? AND fecha < ?";
        return consultarEnteroPorRangoFechas(cn, sql, inicioMes, inicioMesSiguiente);
    }

    private double sumarIngresosDelMes(Connection cn, LocalDate inicioMes, LocalDate inicioMesSiguiente) throws Exception {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM ventas WHERE fecha >= ? AND fecha < ? AND UPPER(COALESCE(estado, '')) <> 'ANULADO'";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(inicioMes));
            ps.setDate(2, java.sql.Date.valueOf(inicioMesSiguiente));
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getDouble(1) : 0.0; }
        }
    }
    private int consultarEntero(Connection cn, String sql) throws Exception {
        try (PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
    }
    private int consultarEnteroPorRangoFechas(Connection cn, String sql, LocalDate inicio, LocalDate fin) throws Exception {
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(inicio)); ps.setDate(2, java.sql.Date.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }
}
