package com.example.myapplication;

public class ImageUpload {
    private String bookName;
    private String bookPrice;
    private String imageUrl;

    public ImageUpload() {
        // Default constructor required for calls to DataSnapshot.getValue(ImageUpload.class)
    }

    public ImageUpload(String bookName, String bookPrice, String imageUrl) {
        this.bookName = bookName;
        this.bookPrice = bookPrice;
        this.imageUrl = imageUrl;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
