package com.example.shift.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: UserApi,
    private val dao: UserDao
) {
    fun getUsers(): Flow<List<UserEntity>> = dao.getUsers()

    suspend fun refreshUsers() {
        val response = api.getUsers()
        val mapped = response.results.map {
            UserEntity(
                id = it.login.uuid,
                fullName = "${it.name.first} ${it.name.last}",
                email = it.email,
                phone = it.phone,
                address = "${it.location.street.number} ${it.location.street.name}, ${it.location.city}",
                thumbnail = it.picture.thumbnail,
                picture = it.picture.large
            )
        }
        dao.clearAll()
        dao.insertAll(mapped)
    }
}