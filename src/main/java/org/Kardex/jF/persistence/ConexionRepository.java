package org.Kardex.jF.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton de conexión a PostgreSQL.
 * 
 */
public class ConexionRepository {

    private static final String URL      = "jdbc:postgresql://localhost:5432/Proyecto2";
    private static final String USUARIO  = "postgres";
    private static final String PASSWORD = "ra73981790";

    public static Connection getConexion() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection cn = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            return cn;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL no encontrado. Verifica el pom.xml.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
