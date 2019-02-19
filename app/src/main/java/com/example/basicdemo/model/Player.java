package com.example.basicdemo.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Player implements Comparable<Player> {

    private long id;

    private String name;

    private String country;

    private String timeStamp;

    private final String DATE_FORMAT ="yyyy-MM-dd HH:mm:ss";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getName() {
        return name;
    }


    public String getCountry() {
        return country;
    }


    public Player() {
    }

    public Player(long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        timeStamp = format.format(new Date());
    }

    public Player(String name, String country) {
        this(0,name,country);
        this.name = name;
        this.country = country;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }



    @Override
    public int compareTo(Player o) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

        Date date1 = null;
        try {
            date1 = format.parse(this.timeStamp);
            Date date2 = format.parse(o.timeStamp);
            return date1.compareTo(date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
