package org.Kardex.jF.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.Kardex.jF.persistence.ConexionRepository;

/**
 * Utilidad centralizada para generar códigos correlativos consultando los datos
 * existentes en la tabla correspondiente.
 */
public final class CodigoAutomaticoModel {

    private CodigoAutomaticoModel() {
    }

    public static String generarSiguienteCodigo(String tabla, String columna, String prefijo) {
        validarIdentificador(tabla);
        validarIdentificador(columna);
        validarPrefijo(prefijo);

        String sql = String.format("""
            SELECT COALESCE(MAX(CAST(SUBSTRING(%1$s FROM ?) AS INTEGER)), 0) + 1 AS siguiente
            FROM %2$s
            WHERE %1$s ~ ?
            """, columna, tabla);
        String patron = "^" + prefijo + "[0-9]+$";
        int inicioNumeros = prefijo.length() + 1;

        try (Connection cn = ConexionRepository.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, inicioNumeros);
            ps.setString(2, patron);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return String.format("%s%03d", prefijo, rs.getInt("siguiente"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prefijo + "001";
    }

    private static void validarIdentificador(String valor) {
        if (valor == null || !valor.matches("[A-Za-z_][A-Za-z0-9_]*")) {
            throw new IllegalArgumentException("Identificador SQL inválido: " + valor);
        }
    }

    private static void validarPrefijo(String prefijo) {
        if (prefijo == null || !prefijo.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("Prefijo de código inválido: " + prefijo);
        }
    }
}
