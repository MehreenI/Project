package com.example.myapplication;

import android.net.Uri;

import java.util.List;

public class Book {
    private String isbn;
    private String bookName;
    private String author;
    private int publishYear;
    private List<Uri> images; // Store image URIs here

    public Book() {
        // Default constructor required for DataSnapshot.getValue(Book.class)
    }

    public Book(String isbn, String bookName, String author, int publishYear, List<Uri> images) {
        this.isbn = isbn;
        this.bookName = bookName;
        this.author = author;
        this.publishYear = publishYear;
        this.images = images;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public List<Uri> getImages() {
        return images;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public void setImages(List<Uri> images) {
        this.images = images;
    }
}
