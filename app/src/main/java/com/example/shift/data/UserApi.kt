package com.example.shift.data

import retrofit2.http.GET

interface UserApi {
    @GET("api/?results=20")
    suspend fun getUsers(): UserResponse
}