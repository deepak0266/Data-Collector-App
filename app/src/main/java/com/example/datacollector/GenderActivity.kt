package com.example.datacollector

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GenderActivity : AppCompatActivity() {
    private lateinit var genderSpinner: Spinner
    private lateinit var nextButton: Button
    private lateinit var secondButton: ImageButton
    private lateinit var thirdButton: ImageButton
    private lateinit var fourthButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gender)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayoutGender)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        genderSpinner = findViewById(R.id.genderSpinner)
        nextButton = findViewById(R.id.nextButton)
        secondButton = findViewById(R.id.secondButton)
        thirdButton = findViewById(R.id.thirdButton)
        fourthButton = findViewById(R.id.fourthButton)

        // Set up the gender spinner
        val genderOptions = arrayOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, genderOptions) // Use your custom layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapter  // Set the adapter first
        genderSpinner.setSelection(-1) // Then set the selection to -1 (no pre-selected item)

        // Set up the Next button
        nextButton.setOnClickListener {
            val selectedGender = genderSpinner.selectedItem.toString()
            if (selectedGender != "-1")
            { // Check if a gender is selected
                // Save the selected gender (you'll need to implement your storage mechanism)
                // ...

                // Navigate to AgeActivity
                val intent = Intent(this, AgeActivity::class.java)
                intent.putExtra("gender", selectedGender)
                startActivity(intent)
            } else {
                // Show an error dialog or toast
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please select your gender.")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
        secondButton.setOnClickListener {
            val selectedGender = genderSpinner.selectedItem.toString()
            if (selectedGender != "-1") {
                // Navigate to AgeActivity
                val intent = Intent(this, AgeActivity::class.java)
                startActivity(intent)
            } else {
                // Or use Toast:
                Toast.makeText(this, "Please select your gender.", Toast.LENGTH_SHORT).show()
            }
        }
        thirdButton.setOnClickListener {
            Toast.makeText(this, "Please Go through the page 2", Toast.LENGTH_SHORT).show()

        }
        fourthButton.setOnClickListener {
            Toast.makeText(this, "Please Go through the page 2", Toast.LENGTH_SHORT).show()
        }

    }
}