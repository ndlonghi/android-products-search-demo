package com.ndlonghi.productssearch;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String title;
    private Number price;
    private String image;

    public Product(
            String id,
            String title,
            Number price,
            String image
    ) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
