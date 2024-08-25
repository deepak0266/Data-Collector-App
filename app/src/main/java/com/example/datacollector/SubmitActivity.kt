package com.example.datacollector

import com.example.datacollector.ContactDataModel
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class SubmitActivity : AppCompatActivity() {
    private lateinit var genderType: TextView
    private lateinit var ageNumber: TextView
    private lateinit var submissionTime: TextView
    private lateinit var gpsLocation: TextView
    private lateinit var selfiePreview: ImageView
    private lateinit var submitButton: Button
    private val LOCATION_REQUEST_CODE = 101
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_submit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayoutSubmit)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        genderType = findViewById(R.id.genderType)
        ageNumber = findViewById(R.id.age)
        submissionTime = findViewById(R.id.submissionTime)
        gpsLocation = findViewById(R.id.gpsLocation)
        selfiePreview = findViewById(R.id.selfiePreview)
        submitButton = findViewById(R.id.submitButton)

        // Retrieve the age, gender, and selfie from the Intent (passed from ImageActivity)
        val age = intent.getIntExtra("age", 0)
        val gender = intent.getStringExtra("gender")

        // Display the selfie, age, and gender
        genderType.text = "$gender"
        ageNumber.text = age.toString()
        val selfie = intent.getParcelableExtra<ParcelableBitmap>("selfie")?.bitmap

        selfiePreview.setImageBitmap(selfie)

        // Get the current submission time
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val currentDateAndTime = simpleDateFormat.format(System.currentTimeMillis())
        submissionTime.text = currentDateAndTime

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Get the GPS location
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
        } else {
            getLastKnownLocation()
        }

        submitButton.setOnClickListener {
            // Create the ContactDataModel
            val contactData = ContactDataModel(
                gender!!,
                age,
                selfie,
                null,
                currentDateAndTime,
                gpsLocation.text.toString() // You might want to parse this to latitude and longitude
            )

            // Save the data using SharedPreferences

            Log.d("SubmitActivity", "Submit button clicked")


            saveContactData(contactData)

            // Navigate to SummaryActivity
            val intent = Intent(this, SummaryActivity::class.java)
            // Pass the age, selfie, and gender
            intent.putExtra("age", age)
            intent.putExtra("selfie", selfie?.let { it1 -> ParcelableBitmap(it1) })
            intent.putExtra("gender", gender)
            intent.putExtra("time", currentDateAndTime) // Pass the time
            // Extracting longitude and latitude from the string and passing them as Ints
            val gpsLocationParts = gpsLocation.text.toString().split(", ")
            if (gpsLocationParts.size == 2) {
                val longitude = gpsLocationParts[0].split(": ")[1].toFloatOrNull()
                val latitude = gpsLocationParts[1].split(": ")[1].toFloatOrNull()
                if (longitude != null && latitude != null) {
                    intent.putExtra("longitude", longitude.toInt())
                    intent.putExtra("latitude", latitude.toInt())
                }
            }
            startActivity(intent)
        }
    }

    // Helper function to request location permission
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "This app needs access to your location to record your GPS coordinates.", Toast.LENGTH_LONG).show() // Explain why
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        } else {
            getLastKnownLocation()
        }
    }

    // Helper function to get the last known location
    private fun getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // GPS location found
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        gpsLocation.text = "GPS Location: Longitude: $longitude, Latitude: $latitude"
                    } else {
                        gpsLocation.text = "GPS Location: Not available"
                    }
                }
        }
    }

    // Helper function to get the current location (not used in this example)
    private fun getCurrentLocation() {
        // ...
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the location
                getLastKnownLocation()
            } else {
                // Permission denied, show a more detailed explanation and/or retry option
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // User denied permission but you can still explain why you need it
                    AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs access to your location to record your GPS coordinates.  Without this permission, we cannot accurately record your location. Would you like to try again?")
                        .setPositiveButton("Retry") { dialog, which ->
                            requestLocationPermission()
                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            // Handle cancellation
                            Toast.makeText(this, "Location access is required for this feature.", Toast.LENGTH_SHORT).show()
                        }
                        .show()
                } else {
                    // User denied permission AND checked "Never ask again"
                    // You can no longer ask for permission.
                    // Handle this case by informing the user or disabling the feature.
                    Toast.makeText(this, "Location permission denied. You can enable it in settings.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Function to save data to SharedPreferences
    private fun saveContactData(contactData: ContactDataModel) {
        val sharedPreferences = getSharedPreferences("contact_data", Context.MODE_PRIVATE)

        // Get existing data from SharedPreferences
        val existingDataListJson = sharedPreferences.getString("contact_data", "[]")

        // Initialize an empty list
        val existingDataList: MutableList<ContactDataModel> = mutableListOf()

        try {
            // Try to deserialize as a list
            existingDataList.addAll(Gson().fromJson(
                existingDataListJson,
                object : TypeToken<List<ContactDataModel>>() {}.type
            ))
        } catch (e: JsonSyntaxException) {
            // If the JSON was an object instead of an array, handle that case
            val singleData: ContactDataModel? = Gson().fromJson(
                existingDataListJson,
                ContactDataModel::class.java
            )
            singleData?.let { existingDataList.add(it) }
        }

        // Add new data to the list
        existingDataList.add(0, contactData)

        // Save the updated list to SharedPreferences
        val serializedData = Gson().toJson(existingDataList)
        with(sharedPreferences.edit()) {
            putString("contact_data", serializedData)
            apply()
        }
    }


}

