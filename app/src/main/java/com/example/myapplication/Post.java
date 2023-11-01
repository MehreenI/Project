package com.example.myapplication;

import java.util.List;

public class Post {
    private String bookName;
    private String bookPrice;
    private String imageUrl;

    public Post(String bookName, String bookPrice, String imageUrl) {
        this.bookName = bookName;
        this.bookPrice = bookPrice;
        this.imageUrl = imageUrl;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

