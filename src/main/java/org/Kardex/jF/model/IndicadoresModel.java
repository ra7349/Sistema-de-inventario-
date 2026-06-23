package org.Kardex.jF.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import org.Kardex.jF.bean.entity.Indicadores;
import org.Kardex.jF.persistence.ConexionRepository;
import org.Kardex.jF.usecase.IndicadoresUsecase;

public class IndicadoresModel implements IndicadoresUsecase {

    @Override
    public Indicadores obtenerIndicadores() {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate inicioMesSiguiente = inicioMes.plusMonths(1);

        try (Connection cn = ConexionRepository.getConexion()) {
            if (cn == null) {
                return new Indicadores();
            }

            return new Indicadores(
                    contarEquiposEnReparacion(cn),
                    contarEquiposEntregados(cn),
                    contarServiciosDelMes(cn, inicioMes, inicioMesSiguiente),
                    contarClientesRegistrados(cn),
                    sumarIngresosDelMes(cn, inicioMes, inicioMesSiguiente));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Indicadores();
    }

    private int contarEquiposEnReparacion(Connection cn) throws Exception {
        String sql = """
            SELECT COUNT(*)
            FROM cliente_servicio
            WHERE UPPER(REPLACE(estado, ' ', '_')) IN ('EN_DIAGNOSTICO', 'EN_REPARACION', 'ESPERANDO_REPUESTO')
            """;
        return consultarEntero(cn, sql);
    }

    private int contarEquiposEntregados(Connection cn) throws Exception {
        String sql = """
            SELECT COUNT(*)
            FROM cliente_servicio
            WHERE UPPER(REPLACE(estado, ' ', '_')) = 'ENTREGADO'
            """;
        return consultarEntero(cn, sql);
    }

    private int contarServiciosDelMes(Connection cn, LocalDate inicioMes, LocalDate inicioMesSiguiente) throws Exception {
        String sql = """
            SELECT COUNT(*)
            FROM cliente_servicio
            WHERE fecha_aplicacion >= ? AND fecha_aplicacion < ?
            """;
        return consultarEnteroPorRangoFechas(cn, sql, inicioMes, inicioMesSiguiente);
    }

    private int contarClientesRegistrados(Connection cn) throws Exception {
        return consultarEntero(cn, "SELECT COUNT(*) FROM cliente");
    }

    private double sumarIngresosDelMes(Connection cn, LocalDate inicioMes, LocalDate inicioMesSiguiente) throws Exception {
        String sql = """
            SELECT COALESCE(SUM(total), 0)
            FROM boleta
            WHERE fecha >= ? AND fecha < ? AND UPPER(COALESCE(estado, '')) <> 'ANULADO'
            """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(inicioMes));
            ps.setDate(2, java.sql.Date.valueOf(inicioMesSiguiente));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }
        }
    }

    private int consultarEntero(Connection cn, String sql) throws Exception {
        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private int consultarEnteroPorRangoFechas(Connection cn, String sql, LocalDate inicio, LocalDate fin) throws Exception {
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(inicio));
            ps.setDate(2, java.sql.Date.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}
