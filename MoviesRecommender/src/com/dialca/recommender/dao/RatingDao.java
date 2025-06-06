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
    public Rating findByUserAndMovie(Users user, Movie movie) {
        String sql = "SELECT * FROM rating WHERE user_id = ? AND movie_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            stmt.setInt(2, movie.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Rating(rs.getInt("id"), user, movie, rs.getInt("rate"),
                        rs.getTimestamp("rating_date").toLocalDateTime());
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar calificación por usuario y película");
            e.printStackTrace();
        }
        return null;
    }
    public double getAverageRatingForMovie(int movieId){
        String sql = "SELECT AVG(rate) AS average_rating FROM rating WHERE movie_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("average_rating");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la calificación promedio de la película");
            e.printStackTrace();
        }
        return 0.0;
    }
    public List<Movie> getTopRatedMovies(int limit) {
        List<Movie> topRatedMovies = new ArrayList<>();
        String sql = """
                    SELECT m.*, AVG(r.rate) AS average_rating
                    FROM movie m
                    JOIN rating r ON m.id = r.movie_id
                    GROUP BY m.id
                    ORDER BY average_rating DESC
                    LIMIT ?
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Movie movie = new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("genre"),
                        rs.getInt("year"), rs.getString("description"), rs.getString("poster_url"));
                topRatedMovies.add(movie);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las películas mejor valoradas");
            e.printStackTrace();
        }
        return topRatedMovies;
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
