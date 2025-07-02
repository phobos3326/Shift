package com.example.shift.data

data class UserResponse(
    val results: List<UserDto>,
    val info: Info
)
data class Info(val seed: String, val page: Int, val results: Int, val version: String)