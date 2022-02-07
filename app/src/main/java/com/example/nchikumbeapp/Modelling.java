package com.example.nchikumbeapp;

public class Modelling {
    String ImageUrl;
    String ProductNAme;
    String price;
    String description;

    public Modelling(String imageUrl, String productNAme, String price,String description) {
        ImageUrl = imageUrl;
        ProductNAme = productNAme;
        this.price = price;
        this.description = description;
    }

    public Modelling() {
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getProductNAme() {
        return ProductNAme;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public void setProductNAme(String productNAme) {
        ProductNAme = productNAme;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
