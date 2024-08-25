package com.example.datacollector

import android.os.Parcel
import android.os.Parcelable
import android.graphics.Bitmap

data class ContactDataModel(
    val gender: String,
    val age: Int,
    val selfie: Bitmap?,
    val audio: String?,
    val time: String,
    val gpsLocation: String,
    val longitude: Int = 0,
    val latitude: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(gender)
        parcel.writeInt(age)
        parcel.writeParcelable(selfie, flags)
        parcel.writeString(audio)
        parcel.writeString(time)
        parcel.writeString(gpsLocation)
        parcel.writeInt(longitude)
        parcel.writeInt(latitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactDataModel> {
        override fun createFromParcel(parcel: Parcel): ContactDataModel {
            return ContactDataModel(parcel)
        }

        override fun newArray(size: Int): Array<ContactDataModel?> {
            return arrayOfNulls(size)
        }
    }
}