package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<ImageUpload> bookList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList);
        recyclerView.setAdapter(bookAdapter);

        // Set a click listener for the items in the RecyclerView
        bookAdapter.setOnItemClickListener(position -> {
            // Handle item click here
            ImageUpload clickedItem = bookList.get(position);
            openBookDetailActivity(clickedItem);
        });

        // Fetch data and update the bookList
        fetchDataFromFirebase();

        return view;
    }

    private void openBookDetailActivity(ImageUpload clickedItem) {
        // Create an intent to open the BookDetailActivity
        Intent intent = new Intent(getContext(), BookDetailActivity.class);
        // Pass the clicked item's details to the new activity
        intent.putExtra("bookName", clickedItem.getBookName());
        intent.putExtra("bookPrice", clickedItem.getBookPrice());
        intent.putExtra("imageUrl", clickedItem.getImageUrl());
        intent.putExtra("description", clickedItem.getDescription()); // Pass description
        intent.putExtra("author", clickedItem.getAuthor()); // Pass author
        intent.putExtra("condition", clickedItem.getCondition()); // Pass condition
        startActivity(intent);
    }




    private void fetchDataFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("uploads"); // Replace with your Firebase database reference
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookList.clear(); // Clear the list before updating

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageUpload book = snapshot.getValue(ImageUpload.class);
                    if (book != null) {
                        bookList.add(book);
                    }
                }

                bookAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });
    }

}
