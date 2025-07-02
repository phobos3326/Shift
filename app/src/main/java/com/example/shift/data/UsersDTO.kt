package com.example.shift.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    @param:Json(name = "results") val results: List<UserDto>
)

@JsonClass(generateAdapter = true)
data class UserDto(
    @param:Json(name = "login") val login: LoginDto,
    @param:Json(name = "name") val name: NameDto,
    @Json(name = "email") val email: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "location") val location: LocationDto,
    @Json(name = "picture" ) val picture: PictureDto,



    )
@JsonClass(generateAdapter = true)
data class LoginDto(@Json(name = "uuid") val uuid: String)
@JsonClass(generateAdapter = true)
data class NameDto(@Json(name = "first") val first: String, @Json(name = "last") val last: String)
@JsonClass(generateAdapter = true)
data class LocationDto(@Json(name = "street") val street: StreetDto, @Json(name = "city") val city: String)
@JsonClass(generateAdapter = true)
data class StreetDto(@Json(name = "name") val name: String, @Json(name = "number") val number: Int)
@JsonClass(generateAdapter = true)
data class PictureDto(@Json(name = "thumbnail") val thumbnail: String, @Json(name = "large") val large: String)
