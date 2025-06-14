package com.dialca.recommender.dao;

import com.dialca.recommender.model.Movie;
import com.dialca.recommender.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDao {
    public boolean create(Movie movie) {
        String sql = "INSERT INTO movie (title, genre, year, description, poster_url) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.setInt(3, movie.getYear());
            stmt.setString(4, movie.getDescription());
            stmt.setString(5, movie.getPosterUrl());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear película");
            e.printStackTrace();
            return false;
        }
    }
    public Movie findById(int id) {
        String sql = "SELECT * FROM movie WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("genre"),
                        rs.getInt("year"), rs.getString("description"), rs.getString("poster_url"));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar película por ID");
            e.printStackTrace();
        }
        return null;
    }
    public Movie findByTitle(String title) {
        String sql = "SELECT * FROM movie WHERE title = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("genre"),
                        rs.getInt("year"), rs.getString("description"), rs.getString("poster_url"));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar película por título");
            e.printStackTrace();
        }
        return null;
    }
    public List<Movie> findByGenre(String genre) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie WHERE genre = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, genre);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Movie movie = new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("genre"),
                        rs.getInt("year"), rs.getString("description"), rs.getString("poster_url"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar películas por género");
            e.printStackTrace();
        }
        return movies;
    }
    public List<Movie> searchMovies(String query) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie WHERE title ILIKE ? OR genre ILIKE ? OR description ILIKE ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            stmt.setString(3, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Movie movie = new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("genre"),
                        rs.getInt("year"), rs.getString("description"), rs.getString("poster_url"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar películas por consulta");
            e.printStackTrace();
        }
        return movies;
    }
    public List<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie";
        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Movie movie = new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("genre"),
                        rs.getInt("year"), rs.getString("description"), rs.getString("poster_url"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las películas");
            e.printStackTrace();
        }
        return movies;
    }
    public List<Movie> getNewReleases(int limit) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie ORDER BY year DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Movie movie = new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("genre"),
                        rs.getInt("year"), rs.getString("description"), rs.getString("poster_url"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nuevos lanzamientos");
            e.printStackTrace();
        }
        return movies;
    }
}
