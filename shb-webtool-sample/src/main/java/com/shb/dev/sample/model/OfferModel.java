package com.shb.dev.sample.model;

/**
 * @author Mohammad Rahmati, 5/13/2017 1:45 PM
 */
public class OfferModel {
    private int id;
    private String title;
    private int price;
    private String imageName;

    public OfferModel() {}

    public OfferModel(int id, String title, int price, String imageName) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.imageName = imageName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
