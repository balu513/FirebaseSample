package com.example.basicdemo.model;

public class Player {

    private long id;

    private String name;

    private String country;

    public Player() {
    }

    public Player(long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public Player(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
