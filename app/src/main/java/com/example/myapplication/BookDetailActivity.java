package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        // Retrieve details from the intent
        Intent intent = getIntent();
        String bookName = intent.getStringExtra("bookName");
        String bookPrice = intent.getStringExtra("bookPrice");
        String imageUrl = intent.getStringExtra("imageUrl");


        // Set the details in the layout elements
        ImageView bookDetailImage = findViewById(R.id.bookDetailImage);
        TextView bookDetailName = findViewById(R.id.bookDetailName);
        TextView bookDetailPrice = findViewById(R.id.bookDetailPrice);



        bookDetailName.setText(bookName);
        bookDetailPrice.setText("Rs: " +bookPrice+"/-");

        // Load the image into the ImageView (you can use Picasso or another image loading library)
        Picasso.get().load(imageUrl).into(bookDetailImage);

    }
}