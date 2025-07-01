/*
package com.example.shift.data


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Model(
    @Json(name = "info")
    val info: Info?,
    @Json(name = "results")
    val results: List<Result?>?
) {
    @JsonClass(generateAdapter = true)
    data class Info(
        @Json(name = "page")
        val page: Int?,
        @Json(name = "results")
        val results: Int?,
        @Json(name = "seed")
        val seed: String?,
        @Json(name = "version")
        val version: String?
    )

    @JsonClass(generateAdapter = true)
    data class Result(
        @Json(name = "cell")
        val cell: String?,
        @Json(name = "dob")
        val dob: Dob?,
        @Json(name = "email")
        val email: String?,
        @Json(name = "gender")
        val gender: String?,
        @Json(name = "id")
        val id: Id?,
        @Json(name = "location")
        val location: Location?,
        @Json(name = "login")
        val login: Login?,
        @Json(name = "name")
        val name: Name?,
        @Json(name = "nat")
        val nat: String?,
        @Json(name = "phone")
        val phone: String?,
        @Json(name = "picture")
        val picture: Picture?,
        @Json(name = "registered")
        val registered: Registered?
    ) {
        @JsonClass(generateAdapter = true)
        data class Dob(
            @Json(name = "age")
            val age: Int?,
            @Json(name = "date")
            val date: String?
        )

        @JsonClass(generateAdapter = true)
        data class Id(
            @Json(name = "name")
            val name: String?,
            @Json(name = "value")
            val value: Any?
        )

        @JsonClass(generateAdapter = true)
        data class Location(
            @Json(name = "city")
            val city: String?,
            @Json(name = "coordinates")
            val coordinates: Coordinates?,
            @Json(name = "country")
            val country: String?,
            @Json(name = "postcode")
            val postcode: Int?,
            @Json(name = "state")
            val state: String?,
            @Json(name = "street")
            val street: Street?,
            @Json(name = "timezone")
            val timezone: Timezone?
        ) {
            @JsonClass(generateAdapter = true)
            data class Coordinates(
                @Json(name = "latitude")
                val latitude: String?,
                @Json(name = "longitude")
                val longitude: String?
            )

            @JsonClass(generateAdapter = true)
            data class Street(
                @Json(name = "name")
                val name: String?,
                @Json(name = "number")
                val number: Int?
            )

            @JsonClass(generateAdapter = true)
            data class Timezone(
                @Json(name = "description")
                val description: String?,
                @Json(name = "offset")
                val offset: String?
            )
        }

        @JsonClass(generateAdapter = true)
        data class Login(
            @Json(name = "md5")
            val md5: String?,
            @Json(name = "password")
            val password: String?,
            @Json(name = "salt")
            val salt: String?,
            @Json(name = "sha1")
            val sha1: String?,
            @Json(name = "sha256")
            val sha256: String?,
            @Json(name = "username")
            val username: String?,
            @Json(name = "uuid")
            val uuid: String?
        )

        @JsonClass(generateAdapter = true)
        data class Name(
            @Json(name = "first")
            val first: String?,
            @Json(name = "last")
            val last: String?,
            @Json(name = "title")
            val title: String?
        )

        @JsonClass(generateAdapter = true)
        data class Picture(
            @Json(name = "large")
            val large: String?,
            @Json(name = "medium")
            val medium: String?,
            @Json(name = "thumbnail")
            val thumbnail: String?
        )

        @JsonClass(generateAdapter = true)
        data class Registered(
            @Json(name = "age")
            val age: Int?,
            @Json(name = "date")
            val date: String?
        )
    }
}*/
