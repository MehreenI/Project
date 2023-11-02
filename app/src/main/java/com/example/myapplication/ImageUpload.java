package com.example.myapplication;

import java.util.Date;

public class ImageUpload {
    private String bookName;
    private String bookPrice;
    private String imageUrl;
    private String author;
    private String description;
    private String condition;

    private String uploadDate;
    public ImageUpload() {
        // Default constructor required for calls to DataSnapshot.getValue(ImageUpload.class)
    }

    public ImageUpload(String bookName, String bookPrice, String imageUrl,String author,String description,String condition, String uploadDate) {
        this.bookName = bookName;
        this.bookPrice = bookPrice;
        this.imageUrl = imageUrl;
        this.author = author;
        this.description = description;
        this.condition = condition;
        this.uploadDate = uploadDate; // Set the upload date to the current date
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

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getCondition() {
        return condition;
    }


    public String getUploadDate() {
        return uploadDate;
    }
}
