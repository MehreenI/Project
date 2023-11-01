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

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class SaleFragment extends Fragment {
    private static final int GALLERY_REQUEST_CODE = 1000;
    private Uri imageUri;
    private ImageButton imgGallery;
    private Button btn;
    private Button upload_Data;
    private StorageReference mStorageReference;
    private DatabaseReference mDataBaseReference;

    private EditText bookNameEditText;
    private EditText bookPriceEditText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale, container, false);

        imgGallery = view.findViewById(R.id.imageButton);
//        btn = view.findViewById(R.id.btn);
        upload_Data = view.findViewById(R.id.uploadData);

        bookNameEditText = view.findViewById(R.id.bookNameEditText);
        bookPriceEditText = view.findViewById(R.id.price);

        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        mDataBaseReference = FirebaseDatabase.getInstance().getReference("uploads");

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

                            ImageUpload upload = new ImageUpload(bookName, bookPrice, downloadUrl);
                            String uploadId = mDataBaseReference.push().getKey();
                            mDataBaseReference.child(uploadId).setValue(upload);

                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Fail to Upload Image", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
