package com.example.myapplication.data

import android.os.Parcelable
import android.os.Parcel
import com.google.gson.annotations.SerializedName

data class User(
    private var apiKey : String,
    private var secretKey : String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    fun getApiKey() : String {
        return this.apiKey
    }
    fun getSecretKey() : String {
        return this.secretKey
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(apiKey)
        parcel.writeString(secretKey)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}


