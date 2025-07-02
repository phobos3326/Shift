package com.example.shift.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val api: UserApi,
    private val dao: UserDao
) : RemoteMediator<Int, UserEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastPage = state.pages.size
                lastPage + 1
            }
        }

        return try {
            val response = api.getUsers(page = page, results = state.config.pageSize, seed = "abc")
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

            if (loadType == LoadType.REFRESH) {
                dao.clearAll()
            }
            dao.insertAll(mapped)

            MediatorResult.Success(endOfPaginationReached = mapped.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}