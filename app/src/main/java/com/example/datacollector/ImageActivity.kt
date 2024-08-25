package com.example.datacollector

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import com.example.datacollector.ParcelableBitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.ByteArrayOutputStream

class ImageActivity : AppCompatActivity() {
    private lateinit var selfiePreview: ImageView
    private lateinit var takeSelfieButton: Button
    private lateinit var retakeSelfieButton: Button
    private lateinit var saveSelfieButton: Button
    private lateinit var prevButton: Button
    private lateinit var firstButton: ImageButton
    private lateinit var secondButton: ImageButton
    private lateinit var thirdButton: ImageButton
    private lateinit var fourthButton: ImageButton
    private lateinit var nextButton: Button

    private val CAMERA_REQUEST_CODE = 100
    private var capturedImage: Bitmap? = null // Stores the captured selfie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_image)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayoutImage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        selfiePreview = findViewById(R.id.selfiePreview)
        takeSelfieButton = findViewById(R.id.takeSelfieButton)
        retakeSelfieButton = findViewById(R.id.retakeSelfieButton)
        saveSelfieButton = findViewById(R.id.saveSelfieButton)
        prevButton = findViewById(R.id.prevButton)
        firstButton = findViewById(R.id.firstButton)
        secondButton = findViewById(R.id.secondButton)
        thirdButton = findViewById(R.id.thirdButton)
        fourthButton = findViewById(R.id.fourthButton)
        nextButton = findViewById(R.id.nextButton)


        // Previous Button
        prevButton.setOnClickListener {
            val intent = Intent(this, AgeActivity::class.java)
            startActivity(intent)
        }

        // Take Selfie Button
        takeSelfieButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestCameraPermission()
            } else {
                openCamera()
            }
        }

        // Retake Selfie Button
        retakeSelfieButton.setOnClickListener {
            openCamera()
        }

        val age = intent.getIntExtra("age", 0)
        val gender = intent.getStringExtra("gender")

        // Flag to track if the selfie is saved
        var isImageSave: Boolean = false

        // Save Selfie Button
        saveSelfieButton.setOnClickListener {
            if (capturedImage != null) {
                // Save the selfie (implementation depends on your storage method)
                // Example: Save as a JPEG file
                saveImageToInternalStorage(capturedImage!!)

                // Image saved successfully
                isImageSave = true

                Toast.makeText(this, "Selfie saved successfully", Toast.LENGTH_SHORT).show()

                // Navigate to the Submit Activity
                val intent = Intent(this, SubmitActivity::class.java)
                // Pass the age, gender and selfie
                intent.putExtra("age", age)
                intent.putExtra("gender", gender)
                intent.putExtra("selfie", ParcelableBitmap(capturedImage!!)) // Pass the selfie using ParcelableBitmap
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please take a selfie first", Toast.LENGTH_SHORT).show()
            }
        }

        // Button Click Listeners (for 1, 2, 3, and 4 buttons)
        firstButton.setOnClickListener {
            val intent = Intent(this, GenderActivity::class.java)
            startActivity(intent)
        }
        secondButton.setOnClickListener {
            val intent = Intent(this, AgeActivity::class.java)
            startActivity(intent)
        }

        fourthButton.setOnClickListener {
            Toast.makeText(this, "Please save image", Toast.LENGTH_SHORT).show()
        }
        nextButton.setOnClickListener {
            if (isImageSave) {
                val intent = Intent(this, SubmitActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Please save image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Helper function to open the camera
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    // Handle the result of the camera intent
    @Deprecated("This method has been deprecated in favor of using the Activity Result API" +
            "which brings increased type safety via an {@link ActivityResultContract} and the prebuilt " +
            "contracts for common intents available in " +
            "{@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for " +
            "testing, and allow receiving results in separate, testable classes independent from your " +
            "activity. Use {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)} " +
            "with the appropriate {@link ActivityResultContract} and handling the result in the " +
            "{@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            capturedImage = data?.extras?.get("data") as? Bitmap
            if (capturedImage != null) {
                selfiePreview.setImageBitmap(capturedImage)
            }
        }
    }

    // Helper function to save the selfie (example using internal storage)
    private fun saveImageToInternalStorage(image: Bitmap): Uri? {
        // Implement saving the image to internal storage
        // ...
        return null
    }

    // Helper function to request camera permission
    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

}