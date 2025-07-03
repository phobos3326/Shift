package com.example.shift.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val gender: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val cell: String,
    val address: String,      // Например: "8929 Valwood Pkwy, Billings, Michigan, United States, 63104"
    val postcode: String,
    val state: String,
    val country: String,
    val latitude: String,
    val longitude: String,
    val timezoneOffset: String,
    val timezoneDescription: String,
    val dobDate: String,
    val dobAge: Int,
    val registeredDate: String,
    val registeredAge: Int,
    val idName: String?,      // nullable, т.к. может отсутствовать
    val idValue: String?,     // nullable
    val nat: String,
    val thumbnail: String,
    val picture: String
)