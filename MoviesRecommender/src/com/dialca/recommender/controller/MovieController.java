package com.dialca.recommender.controller;

import com.dialca.recommender.dao.MovieDao;
import com.dialca.recommender.model.Movie;

import java.util.List;

public class MovieController {
    private final MovieDao movieDao = new MovieDao();

    public boolean addMovie(String title, String description, String genre, int year, String poster_url) {
        Movie movie = new Movie(title, description, genre, year, poster_url);
        return movieDao.create(movie);
    }

    public List<Movie> getAllMovies() {
        return movieDao.getAll();
    }

    public Movie getMovieById(int id) {
        return movieDao.findById(id);
    }
    public Movie getMovieByTitle(String title) {
        return movieDao.findByTitle(title);
    }
    public List<Movie> getMoviesByGenre(String genre) {
        return movieDao.findByGenre(genre);
    }
    public List<Movie> searchMovies(String query){
        return movieDao.searchMovies(query);
    }
    public List<Movie> getNewReleases (int limit) {
        return movieDao.getNewReleases(limit);
    }
}
