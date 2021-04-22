package com.bowlofricedev.restaurantqrmenu.beans;

public class Enlace {

    private int id;
    private String name, url, type, fav;
    private long timeMillis;

    public Enlace(String name, String url, String type, String fav, long timeMillis) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.fav = fav;
        this.timeMillis = timeMillis;
    }

    public Enlace() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }
}
