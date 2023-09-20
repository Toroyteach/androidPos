package com.ahmadabuhasan.skripsi.connection.models;

public class OrderDetailsModel {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private double cost;
    private String imgUrl;

    public OrderDetailsModel(int id, String name, int quantity, double price, double cost, String imgUrl) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.cost = cost;
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
