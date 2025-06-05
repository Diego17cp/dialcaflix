package com.dialca.recommender.dao;

import com.dialca.recommender.model.Users;
import com.dialca.recommender.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDao {
    public boolean create(Users user) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear usuario");
            e.printStackTrace();
            return false;
        }
    }

    public Users findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Users(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("password"));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por email");
            e.printStackTrace();
        }
        return null;
    }
    public List<Users> getAll() {
        List<Users> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Users user = new Users(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios");
            e.printStackTrace();
        }
        return users;
    }
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario");
            e.printStackTrace();
            return false;
        }
    }
}
