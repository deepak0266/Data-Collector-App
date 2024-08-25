package com.example.datacollector

import android.os.Parcel
import android.os.Parcelable
import android.graphics.Bitmap // Import Bitmap class

data class ParcelableBitmap(val bitmap: Bitmap) : Parcelable {
    // Constructor to create a ParcelableBitmap from a Parcel
    constructor(parcel: Parcel) : this(parcel.readParcelable(Bitmap::class.java.classLoader)!!)

    // Method to write the Bitmap to a Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(bitmap, flags)
    }

    // Describe the contents of the Parcelable object
    override fun describeContents(): Int {
        return 0
    }

    // Companion object to create a Creator for the Parcelable
    companion object CREATOR : Parcelable.Creator<ParcelableBitmap> {
        override fun createFromParcel(parcel: Parcel): ParcelableBitmap {
            return ParcelableBitmap(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableBitmap?> {
            return arrayOfNulls(size)
        }
    }
}