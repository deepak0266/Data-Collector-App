package com.example.datacollector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SummaryAdapter(private val contactDataList: List<ContactDataModel>) :
    RecyclerView.Adapter<SummaryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val genderText: TextView = itemView.findViewById(R.id.genderText)
        val ageText: TextView = itemView.findViewById(R.id.ageText)
        val selfieImage: ImageView = itemView.findViewById(R.id.selfieImage)
        val audioText: TextView = itemView.findViewById(R.id.audioText) // Audio TextView
        val timeText: TextView = itemView.findViewById(R.id.timeText) // Time TextView
        val gpsLocationText: TextView = itemView.findViewById(R.id.gpsLocationText) // GPS Location TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_rv_summary_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contactData = contactDataList[position]
        holder.genderText.text = contactData.gender
        holder.ageText.text = contactData.age.toString()
        holder.selfieImage.setImageBitmap(contactData.selfie)
        holder.audioText.text = contactData.audio ?: "N/A" // Handle null audio
        holder.timeText.text = contactData.time
        holder.gpsLocationText.text = contactData.gpsLocation
    }

    override fun getItemCount(): Int {
        return contactDataList.size
    }
}