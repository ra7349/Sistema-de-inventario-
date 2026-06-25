package org.golocentro.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.golocentro.persistence.ConexionRepository;

/**
 * Utilidad centralizada para generar códigos correlativos consultando los datos
 * existentes en la tabla correspondiente.
 */
public final class CodigoAutomaticoModel {

    private CodigoAutomaticoModel() {
    }

    public static String generarSiguienteCodigo(String tabla, String columna, String prefijo) {
        return generarSiguienteCodigo(tabla, columna, prefijo, 3);
    }

    public static String generarSiguienteCodigo(String tabla, String columna, String prefijo, int digitos) {
        validarIdentificador(tabla);
        validarIdentificador(columna);
        validarPrefijo(prefijo);
        validarDigitos(digitos);

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
                    return String.format("%s%0" + digitos + "d", prefijo, rs.getInt("siguiente"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("%s%0" + digitos + "d", prefijo, 1);
    }

    private static void validarIdentificador(String valor) {
        if (valor == null || !valor.matches("[A-Za-z_][A-Za-z0-9_]*")) {
            throw new IllegalArgumentException("Identificador SQL inválido: " + valor);
        }
    }

    private static void validarPrefijo(String prefijo) {
        if (prefijo == null || !prefijo.matches("[A-Za-z][A-Za-z0-9-]*")) {
            throw new IllegalArgumentException("Prefijo de código inválido: " + prefijo);
        }
    }

    private static void validarDigitos(int digitos) {
        if (digitos < 1 || digitos > 12) {
            throw new IllegalArgumentException("Cantidad de dígitos inválida: " + digitos);
        }
    }
}
