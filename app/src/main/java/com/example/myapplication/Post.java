package com.example.myapplication;

import java.util.List;

public class Post {
    private String bookName;
    private List<String> imageUrls;

    public Post() {
        // Default constructor required for Firebase
    }

    public Post(String bookName, List<String> imageUrls) {
        this.bookName = bookName;
        this.imageUrls = imageUrls;
    }

    public String getBookName() {
        return bookName;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
}

