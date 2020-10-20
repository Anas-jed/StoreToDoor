package com.example.storetodoor;

public class ProductPOJO {

    String name;
    int price;

    public ProductPOJO() {
    }

    public ProductPOJO(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }



}
