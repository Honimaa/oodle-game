package com.example.oodlegame.model;

public class Usuario {

    private int id;
    private String email;
    private String username;
    private String password;

    public Usuario(){
    }


    public Usuario (String email, String username, String password){
        this.email = email;
        this.username = username;
        this.password = password;
    }


    public Usuario (int id, String email, String username, String password){
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
