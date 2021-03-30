package com.example.mySuperMarket.objects;

public class Item {
    private String name;
    private float price;
    private String date;
    private String image;
    private String desc;
    private String id;
    private String cartDesc;

    public Item(String name, float price, String date, String image, String desc, String id, String cartDesc) {
        this.name = name;
        this.price = price;
        this.date = date;
        this.image = image;
        this.desc = desc;
        this.id = id;
        this.cartDesc = cartDesc;
    }

    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCartDesc() {
        return cartDesc;
    }

    public void setCartDesc(String cartDesc) {
        this.cartDesc = cartDesc;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", date='" + date + '\'' +
                ", image='" + image + '\'' +
                ", desc='" + desc + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
