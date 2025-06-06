package com.dialca.recommender.model;
import java.time.LocalDateTime;
public class Rating {
    private int id;
    private Users user;
    private Movie movie;
    private int rate;
    private LocalDateTime ratingDate;
    public Rating() {}
    // Constructor for creating a new rating
    public Rating(Users user, Movie movie, int rate, LocalDateTime ratingDate) {
        this.user = user;
        this.movie = movie;
        this.rate = rate;
        this.ratingDate = ratingDate;
    }
    public Rating(int id, Users user, Movie movie, int rate, LocalDateTime ratingDate) {
        this.id = id;
        this.user = user;
        this.movie = movie;
        this.rate = rate;
        this.ratingDate = ratingDate;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }
    public Movie getMovie() {
        return movie;
    }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    public int getRate() {
        return rate;
    }
    public void setRate(int rate) {
        this.rate = rate;
    }
    public LocalDateTime getRatingDate() {
        return ratingDate;
    }
    public void setRatingDate(LocalDateTime ratingDate) {
        this.ratingDate = ratingDate;
    }
}
