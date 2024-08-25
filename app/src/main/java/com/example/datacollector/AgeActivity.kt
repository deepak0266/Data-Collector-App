
package com.example.datacollector

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AgeActivity : AppCompatActivity() {
    private lateinit var ageInput: EditText
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var firstButton: ImageButton
    private lateinit var secondButton: ImageButton
    private lateinit var thirdButton: ImageButton
    private lateinit var fourthButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_age)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayoutAge)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ageInput = findViewById(R.id.ageInput)
        nextButton = findViewById(R.id.nextButton)
        prevButton = findViewById(R.id.prevButton)
        firstButton = findViewById(R.id.firstButton)
        secondButton = findViewById(R.id.secondButton)
        thirdButton = findViewById(R.id.thirdButton)
        fourthButton = findViewById(R.id.fourthButton)

        val gender = intent.getStringExtra("gender")

        // Previous Button
        prevButton.setOnClickListener {
            val intent = Intent(this, GenderActivity::class.java)
            startActivity(intent)
        }

        // Next Button
        nextButton.setOnClickListener {
            val age = ageInput.text.toString().toIntOrNull()
            if (age != null && age > 0 && age <= 120) {
                // Navigate to the Image Activity and pass the age
                val intent = Intent(this, ImageActivity::class.java)
                intent.putExtra("age", age) // Pass the age
                intent.putExtra("gender", gender)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()
            }
        }

        // Button Click Listeners (for 1, 2, 3, and 4 buttons)
        firstButton.setOnClickListener {
            // Navigate to the Gender Activity (and potentially pass the age if needed)
            val intent = Intent(this, GenderActivity::class.java)
            startActivity(intent)
        }

        thirdButton.setOnClickListener {
            // Navigate to the Image Activity (and potentially pass the age if needed)
            val intent = Intent(this, ImageActivity::class.java)
            startActivity(intent)
        }
        fourthButton.setOnClickListener {
            Toast.makeText(this, "Please provide selfie", Toast.LENGTH_SHORT).show()
        }
    }
}