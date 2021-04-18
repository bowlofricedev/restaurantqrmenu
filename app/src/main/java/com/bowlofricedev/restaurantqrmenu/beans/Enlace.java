package com.bowlofricedev.restaurantqrmenu.beans;

public class Enlace {

    private int id;
    private String name, url, type, fav;

    public Enlace(String name, String url, String type, String fav) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.fav = fav;
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
}
