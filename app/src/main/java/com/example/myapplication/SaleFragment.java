package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class SaleFragment : Fragment() {
    private var imageUri: Uri? = null
    private var imgGallery: ImageButton? = null
    private val btn: Button? = null
    private var upload_Data: Button? = null
    private var mStorageReference: StorageReference? = null
    private var mDataBaseReference: DatabaseReference? = null
    private var bookNameEditText: EditText? = null
    private var bookPriceEditText: EditText? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sale, container, false)
        imgGallery = view.findViewById(R.id.imageButton)
        //        btn = view.findViewById(R.id.btn);
        upload_Data = view.findViewById(R.id.uploadData)
        bookNameEditText = view.findViewById(R.id.bookNameEditText)
        bookPriceEditText = view.findViewById(R.id.price)
        mStorageReference = FirebaseStorage.getInstance().getReference("uploads")
        mDataBaseReference = FirebaseDatabase.getInstance().getReference("uploads")
        imgGallery.setOnClickListener(View.OnClickListener { v: View? -> pickImageFromGallery() })
        upload_Data.setOnClickListener(View.OnClickListener { v: View? -> uploadFile() })
        return view
    }

    private fun pickImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            imageUri = data.data
            imgGallery!!.setImageURI(imageUri)
        }
    }

    fun uploadFile() {
        if (imageUri != null) {
            val fileReference = mStorageReference!!.child(
                System.currentTimeMillis().toString() + "." + getFileExtension(
                    imageUri!!
                )
            )
            fileReference.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                    fileReference.downloadUrl.addOnSuccessListener { uri: Uri ->
                        val downloadUrl = uri.toString()
                        val bookName = bookNameEditText!!.text.toString()
                        val bookPrice = bookPriceEditText!!.text.toString()
                        val upload = ImageUpload(bookName, bookPrice, downloadUrl)
                        val uploadId = mDataBaseReference!!.push().key
                        mDataBaseReference!!.child(uploadId!!).setValue(upload)
                    }
                }
                .addOnFailureListener { e: Exception? ->
                    Toast.makeText(
                        activity,
                        "Fail to Upload Image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = activity!!.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 1000
    }
}