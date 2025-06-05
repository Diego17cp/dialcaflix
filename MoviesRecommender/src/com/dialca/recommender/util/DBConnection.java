package com.dialca.recommender.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://dpg-d0vsb8emcj7s73fuf6c0-a.oregon-postgres.render.com:5432/dialcaflix_g3qt";
    private static final String USER = "dialcadev";
    private static final String PASSWORD = "Zc5uZf1SdFjWLUC8q4DxzIVLfIRcwtAR";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver de Postgre no encontrado");
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
