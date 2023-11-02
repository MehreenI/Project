package com.example.myapplication;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SaleFragment extends Fragment {
    private static final int GALLERY_REQUEST_CODE = 1000;
    private Uri imageUri;
    private ImageButton imgGallery;
    private Button upload_Data;
    private StorageReference mStorageReference;
    private DatabaseReference mDataBaseReference;

    private EditText bookNameEditText;
    private EditText bookPriceEditText;
    private EditText bookAuthor;
    private EditText bookDescription;

    private RadioGroup radioGroup;
    private RadioButton radioButtonNew;
    private RadioButton radioButtonUsed;

    String condition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale, container, false);

        imgGallery = view.findViewById(R.id.imageButton);
        upload_Data = view.findViewById(R.id.uploadData);

        bookNameEditText = view.findViewById(R.id.bookNameEditText);
        bookPriceEditText = view.findViewById(R.id.price);
        bookAuthor = view.findViewById(R.id.bookAuthorEditText);
        bookDescription = view.findViewById(R.id.DescriptionEditText);


        radioGroup = view.findViewById(R.id.radioGroup);
        radioButtonNew = view.findViewById(R.id.radioButtonNew);
        radioButtonUsed = view.findViewById(R.id.radioButtonUsed);

        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        mDataBaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        // Set an OnCheckedChangeListener for the RadioGroup
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Check which RadioButton is selected and take action accordingly
            if (checkedId == R.id.radioButtonNew) {
                condition = "New";
            } else if (checkedId == R.id.radioButtonUsed) {
                condition = "Used";
            }
        });

        imgGallery.setOnClickListener(v -> pickImageFromGallery());
        upload_Data.setOnClickListener(v -> uploadFile());



        return view;
    }

    private void pickImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            imageUri = data.getData();
            imgGallery.setImageURI(imageUri);
        }
    }

    public void uploadFile() {
        if (imageUri != null) {
            StorageReference fileReference = mStorageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            String bookName = bookNameEditText.getText().toString();
                            String bookPrice = bookPriceEditText.getText().toString();
                            String author = bookAuthor.getText().toString();
                            String description = bookDescription.getText().toString();
                            String old_new_condition = condition;

                            // Get the current date and time
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                            String uploadDate = sdf.format(new Date());

//                            Verify k koi b field empty toh nhi
                            if (!bookName.isEmpty() && !bookPrice.isEmpty() && !author.isEmpty() && !description.isEmpty() && !uploadDate.isEmpty()) {
                                ImageUpload upload = new ImageUpload(bookName, bookPrice, downloadUrl, author, description, old_new_condition, uploadDate);
                                String uploadId = mDataBaseReference.push().getKey();
                                mDataBaseReference.child(uploadId).setValue(upload);
                                Toast.makeText(getActivity(), "Post Added Successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Fail to Upload Image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getActivity(), "Please fill in all the Fields", Toast.LENGTH_SHORT).show();
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
