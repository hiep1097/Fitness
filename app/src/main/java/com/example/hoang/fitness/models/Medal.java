package com.example.hoang.fitness.models;

import java.io.Serializable;

public class Medal implements Serializable {
    private String id;
    private String image;
    private int price;
    private boolean daMua;

    public Medal() {
    }

    public Medal(String image, int price, boolean daMua) {
        this.image = image;
        this.price = price;
        this.daMua = daMua;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isDaMua() {
        return daMua;
    }

    public void setDaMua(boolean daMua) {
        this.daMua = daMua;
    }
}
