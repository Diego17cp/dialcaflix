package com.dialca.recommender.controller;

import com.dialca.recommender.dao.RatingDao;
import com.dialca.recommender.model.Rating;
import com.dialca.recommender.model.Movie;
import com.dialca.recommender.model.Users;

import java.time.LocalDateTime;
import java.util.List;

public class RatingController {
    private final RatingDao ratingDao = new RatingDao();
    public boolean rateMovie(Users user, Movie movie, int rate) {
        Rating rating = new Rating(user, movie, rate, LocalDateTime.now());
        return ratingDao.create(rating);
    }
    public boolean updateRating(Users user, Movie movie, int newRate) {
        Rating existingRating = ratingDao.findByUserAndMovie(user, movie);
        if (existingRating != null) {
            existingRating.setRate(newRate);
            return ratingDao.updateRating(existingRating);
        } else {
            return rateMovie(user, movie, newRate);
        }
    }
    public List<Rating> getRatingsByUser(Users user) {
        return ratingDao.findByUserId(user.getId());
    }
    public double getAverageRatingForMovie(int movieId) {
        return ratingDao.getAverageRatingForMovie(movieId);
    }
    public List<Movie> getTopRatedMovies(int limit) {
        return ratingDao.getTopRatedMovies(limit);
    }
}
