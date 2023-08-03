@file:Suppress("DEPRECATION")

package com.example.arfilter

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.arfilter.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var imageUri: Uri? = null
    private lateinit var storageReference: StorageReference
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firestoreDB: FirebaseFirestore

    private val PICK_IMAGE = 0
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PERMISSION = 100
    private var lastimage_on = false
    private val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Check for camera and storage permissions
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION)
        }

        binding.selectImagebtn.setOnClickListener {
            selectImage()
        }

        binding.uploadimagebtn.setOnClickListener {
            uploadImage()
        }

        binding.downbtn.setOnClickListener {
            val intent = Intent(this, retrieve::class.java)
            startActivity(intent)
        }

        firestoreDB = FirebaseFirestore.getInstance()


    }

    override fun onResume() {
        super.onResume()

        if (lastimage_on) {
            lastimage()

        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage() {
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading File....")
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA)
        val now = Date()
        val fileName = formatter.format(now)
        storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        // Add null check here
        if (imageUri != null) {
            storageReference.putFile(imageUri!!)
                .addOnSuccessListener {

                    Toast.makeText(this, "Successfully Uploaded", Toast.LENGTH_SHORT).show()
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }

                    // Get the download URL of the uploaded image and add it to Firestore
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val map = HashMap<String, Any>()
                        map["pic"] = uri.toString()
                        firestoreDB.collection("images")
                            .add(map)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Image added to Firestore", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this,
                                    "Error adding image to Firestore: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }.addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Failed to get download URL: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
                .addOnFailureListener { e ->
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                    Toast.makeText(this, "Failed to Upload: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            Toast.makeText(this, "Please select an image to upload", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }
    }


    private fun selectImage() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Image Source")
        builder.setItems(arrayOf("Gallery", "Camera")) { _, which ->
            when (which) {
                0 -> {
                    lastimage_on = false
                    // Launch the image picker from the gallery
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, PICK_IMAGE)
                }

                1 -> {
                    lastimage_on = true
                    val intent = Intent(this, com.unity3d.player.UnityPlayerActivity::class.java)
                    intent.putExtra("name", "value")
                    startActivity(intent)

                }
            }
        }
        val dialog = builder.create()
        dialog.show()
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE -> {
                    imageUri = data?.data
                    binding.firebaseimage.setImageURI(imageUri)
                }

                REQUEST_IMAGE_CAPTURE -> {
                    binding.firebaseimage.setImageURI(imageUri)
                }
            }
        }
    }

    private fun lastimage() {
        val folderPath = "/storage/emulated/0/DCIM/MyGallery/"
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN
        )
        val selection = "${MediaStore.Images.Media.DATA} like '$folderPath%'"
        val sortOrder = "${MediaStore.Images.Media._ID} DESC"
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )

        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            if (columnIndex >= 0) {
                val imagePath = cursor.getString(columnIndex)
                if (imagePath.endsWith(".jpg") || imagePath.endsWith(".png")) {
                    imageUri = Uri.fromFile(File(imagePath)) // assign value to imageUri
                    Glide.with(this).load(imageUri).into(binding.firebaseimage)
                }
            }

            cursor.close()
        }
    }


}


