package com.dialca.recommender.ia;

import com.dialca.recommender.controller.MovieController;
import com.dialca.recommender.controller.RatingController;
import com.dialca.recommender.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class RecommenderEngine {
    private final MovieController movieController;
    private final RatingController ratingController;

    private static final int POSITIVE_RATING_THRESHOLD = 3;
    private static final int MAX_RECOMMENDATIONS = 10;

    public RecommenderEngine() {
        this.movieController = new MovieController();
        this.ratingController = new RatingController();
    }

    public List<Movie> getRecommendations(Users user) {
        if (user == null)
            return getBasicRecommendations();
        List<Rating> userRatings = ratingController.getRatingsByUser(user);
        if (userRatings == null || userRatings.isEmpty())
            return getBasicRecommendations();
        else
            return getPersonalizedRecommendations(user, userRatings);
    }
    public List<Movie> getBasicRecommendations() {
        List<Movie> topRatedMovies = ratingController.getTopRatedMovies(MAX_RECOMMENDATIONS);
        if (topRatedMovies.size() < MAX_RECOMMENDATIONS) {
            List<Movie> allMovies = movieController.getAllMovies();
            List<Movie> additonalMovies = allMovies.stream()
                .filter(movie -> !topRatedMovies.contains(movie))
                .limit(MAX_RECOMMENDATIONS - topRatedMovies.size())
                .collect(Collectors.toList());
            topRatedMovies.addAll(additonalMovies);
        }
        return topRatedMovies;
    }
    private List<Movie> getPersonalizedRecommendations(Users user, List<Rating> userRatings) {
        Map<String, Double> genrePreferences = analyzeGenrePreferences(userRatings);
        List<Movie> allMovies = movieController.getAllMovies();
        Set<Integer> ratedMovieIds = userRatings.stream()
            .map(rating -> rating.getMovie().getId())
            .collect(Collectors.toSet());
        List<Movie> unwatchedMovies = allMovies.stream()
            .filter(movie -> !ratedMovieIds.contains(movie.getId()))
            .collect(Collectors.toList());
        List<ScoredMovie> scoredMovies = new ArrayList<>();
        for (Movie movie : unwatchedMovies) {
            double score = calculateRelevanceScore(movie, genrePreferences);
            scoredMovies.add(new ScoredMovie(movie, score));
        }
        return scoredMovies.stream()
            .sorted(Comparator.comparing(ScoredMovie::getScore).reversed())
            .limit(MAX_RECOMMENDATIONS)
            .map(ScoredMovie::getMovie)
            .collect(Collectors.toList());
    }
    private Map<String, Double> analyzeGenrePreferences(List<Rating> userRatings) {
        Map<String, List<Double>> genreRatings = new HashMap<>();
        for (Rating rating : userRatings) {
            String genre = rating.getMovie().getGenre();
            if (!genreRatings.containsKey(genre)) {
                genreRatings.put(genre, new ArrayList<>());
            }
            genreRatings.get(genre).add((double) rating.getRate());
        }
        Map<String, Double> genrePreferences = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : genreRatings.entrySet()) {
            String genre = entry.getKey();
            List<Double> ratings = entry.getValue();
            double averageRating = ratings.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
            if (averageRating >= POSITIVE_RATING_THRESHOLD) {
                genrePreferences.put(genre, averageRating);
            }
        }
        return genrePreferences;
    }
    private double calculateRelevanceScore(Movie movie, Map<String, Double> genrePreferences) {
        String genre = movie.getGenre();
        if (genrePreferences.containsKey(genre)) {
            double genreScore = genrePreferences.get(genre);
            double averageRating = ratingController.getAverageRatingForMovie(movie.getId());
            return (genreScore * 0.7) + (averageRating * 0.3);
        }
        return ratingController.getAverageRatingForMovie(movie.getId());
    }
    private static class ScoredMovie {
        private final Movie movie;
        private final double score;

        public ScoredMovie(Movie movie, double score) {
            this.movie = movie;
            this.score = score;
        }

        public Movie getMovie() {
            return movie;
        }

        public double getScore() {
            return score;
        }
    }
}

