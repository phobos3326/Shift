package com.example.shift.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class UserResponse(
    @Json(name = "results") val results: List<UserDto>
)


data class UserDto(
    @Json(name = "login") val login: LoginDto,
    @Json(name = "name") val name: NameDto,
    @Json(name = "email") val email: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "location") val location: LocationDto,
    @Json(name = "picture" ) val picture: PictureDto,



)

data class LoginDto(@Json(name = "id") val uuid: String)

data class NameDto(@Json(name = "first") val first: String, @Json(name = "last") val last: String)
data class LocationDto(@Json(name = "street") val street: StreetDto, @Json(name = "city") val city: String)
data class StreetDto(@Json(name = "name") val name: String, @Json(name = "number") val number: Int)
data class PictureDto(@Json(name = "thumbnail") val thumbnail: String, @Json(name = "large") val large: String)
