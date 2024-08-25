package com.example.datacollector

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SummaryActivity : AppCompatActivity() {

    private lateinit var addContactButton: Button
    private lateinit var adapter: SummaryAdapter
    private lateinit var contactDataList: MutableList<ContactDataModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_summary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get the button id and handle click
        addContactButton = findViewById(R.id.addContactButton)
        addContactButton.setOnClickListener {
            val intent = Intent(this, GenderActivity::class.java)
            startActivity(intent)
        }

        // Load the contact data (assuming you're using SharedPreferences)
        contactDataList = loadContactData()

        // Set up the RecyclerView
        val summaryRecyclerView: RecyclerView = findViewById(R.id.summaryRecyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        summaryRecyclerView.layoutManager = layoutManager
        adapter = SummaryAdapter(contactDataList)
        summaryRecyclerView.adapter = adapter

        // Handle the data received from SubmitActivity (do this AFTER loading from SharedPreferences)
        if (intent.hasExtra("contactDataList")) {
            val newContactData = intent.getParcelableExtra<ContactDataModel>("contactDataList")
            if (newContactData != null) {
                contactDataList.add(0, newContactData) // Add the new item at the top
                adapter.notifyItemInserted(0) // Update the RecyclerView
                saveContactData(contactDataList) // Save the updated list
            }
        }
    }

    // Helper function to save data (using SharedPreferences as an example)
    private fun saveContactData(contactDataList: MutableList<ContactDataModel>) {
        val sharedPreferences = getSharedPreferences("contact_data", Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            // You'll likely need to serialize/deserialize the ContactData object for SharedPreferences
            val serializedData = Gson().toJson(contactDataList) // Example using Gson
            putString("contact_data", serializedData) // Save the JSON string

            apply()
        }
    }

    // Helper function to load data (using SharedPreferences as an example)
    private fun loadContactData(): MutableList<ContactDataModel> {
        val sharedPreferences = getSharedPreferences("contact_data", MODE_PRIVATE)
        val serializedData = sharedPreferences.getString("contact_data", "[]") // Default to empty array
        if (serializedData != null) {
            val type = object : TypeToken<List<ContactDataModel>>() {}.type
            val contactDataList = Gson().fromJson<List<ContactDataModel>>(serializedData, type)
            return contactDataList.toMutableList()
        }
        return mutableListOf() // Return an empty list if no data is found
    }
}