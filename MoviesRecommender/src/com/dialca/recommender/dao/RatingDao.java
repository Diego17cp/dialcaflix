package com.dialca.recommender.dao;

import com.dialca.recommender.model.Rating;
import com.dialca.recommender.model.Movie;
import com.dialca.recommender.model.Users;
import com.dialca.recommender.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingDao {
    public boolean create(Rating rating) {
        String sql = "INSERT INTO rating (user_id, movie_id, rate, rating_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rating.getUser().getId());
            stmt.setInt(2, rating.getMovie().getId());
            stmt.setInt(3, rating.getRate());
            stmt.setTimestamp(4, Timestamp.valueOf(rating.getRatingDate()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear calificación");
            e.printStackTrace();
            return false;
        }
    }

    public List<Rating> findByUserId(int userId) {
        List<Rating> ratings = new ArrayList<>();
        String sql = """
                    SELECT r.*, m.*, u.*
                    FROM rating r
                    JOIN movie m ON r.movie_id = m.id
                    JOIN users u ON r.user_id = u.id
                    WHERE r.user_id = ?
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Users user = new Users(rs.getInt("u.id"), rs.getString("u.name"), rs.getString("u.email"),
                        rs.getString("u.password"));
                Movie movie = new Movie(rs.getInt("m.id"), rs.getString("m.title"), rs.getString("m.genre"),
                        rs.getInt("m.year"), rs.getString("m.description"), rs.getString("m.poster_url"));
                Rating rating = new Rating(rs.getInt("r.id"), user, movie, rs.getInt("r.rate"),
                        rs.getTimestamp("r.rating_date").toLocalDateTime());
                ratings.add(rating);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar calificaciones por ID de usuario");
            e.printStackTrace();
        }
        return ratings;
    }

    public boolean updateRating(Rating rating) {
        String sql = "UPDATE rating SET rate = ?, rating_date = current_timestamp WHERE user_id = ? AND movie_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rating.getRate());
            stmt.setInt(2, rating.getUser().getId());
            stmt.setInt(3, rating.getMovie().getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar calificación");
            e.printStackTrace();
            return false;
        }
    }
}
