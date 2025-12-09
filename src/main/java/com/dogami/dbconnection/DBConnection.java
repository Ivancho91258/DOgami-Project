package com.dogami.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // --- CONFIGURACIÓN ---
    private static final String DB_NAME = "dogami";
    private static final String USER = "IvanMolina";
    private static final String PASSWORD = "Maorintak1.";
    
    private static final String CLOUD_SQL_CONNECTION_NAME = "dogami-480702:us-east1:dogami-db-general";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver no encontrado");
            throw new SQLException("PostgreSQL JDBC Driver not found.", e);
        }

        String url;
        
        if (System.getenv("K_SERVICE") != null) {

            url = String.format("jdbc:postgresql:///%s?cloudSqlInstance=%s&socketFactory=com.google.cloud.sql.postgres.SocketFactory&user=%s&password=%s",
                    DB_NAME, CLOUD_SQL_CONNECTION_NAME, USER, PASSWORD);
            
            // En modo nube, pasamos la URL completa con credenciales
            return DriverManager.getConnection(url);
            
        } else {
            // Modo local
            url = String.format("jdbc:postgresql://localhost:5432/%s", DB_NAME);
            
            // En local, pasamos usuario y contraseña por separado
            return DriverManager.getConnection(url, USER, PASSWORD);
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}