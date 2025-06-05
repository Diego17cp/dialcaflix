package com.dialca.recommender.model;
public class Movie {
    private int id;
    private String title;
    private String genre;
    private int year;
    private String description;
    private String poster_url;
    public Movie() {}
    public Movie(int id, String title, String genre, int year, String description, String poster_url) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.description = description;
        this.poster_url = poster_url;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPosterUrl() {
        return poster_url;
    }
    public void setPosterUrl(String poster_url) {
        this.poster_url = poster_url;
    }
}
