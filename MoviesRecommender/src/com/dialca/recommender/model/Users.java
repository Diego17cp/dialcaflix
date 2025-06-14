package com.dialca.recommender.model;
public class Users {
    private int id;
    private String name;
    private String email;
    private String password;
    public Users () {};
    // Constructor para nuevos usuarios
    public Users (String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Constructor para usuarios ya registrados
    public Users (int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
