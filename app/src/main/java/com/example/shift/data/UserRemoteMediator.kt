package com.example.shift.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val api: UserApi,
    private val db: AppDatabase
) : RemoteMediator<Int, UserEntity>() {

    private val userDao = db.userDao()
    private val remoteKeysDao = db.remoteKeysDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, UserEntity>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val apiResponse = api.getUsers(page = page, results = state.config.pageSize, seed = "abc")
            val users = apiResponse.results.map {
                UserEntity(
                    id = it.login.uuid,
                    gender = it.gender,
                    fullName = "${it.name.first} ${it.name.last}",
                    email = it.email,
                    phone = it.phone,
                    cell = it.cell,
                    address = "${it.location.street.number} ${it.location.street.name}, ${it.location.city}, ${it.location.state}, ${it.location.country}",
                    postcode = it.location.postcode.toString(),
                    state = it.location.state,
                    country = it.location.country,
                    latitude = it.location.coordinates.latitude,
                    longitude = it.location.coordinates.longitude,
                    timezoneOffset = it.location.timezone.offset,
                    timezoneDescription = it.location.timezone.description,
                    dobDate = it.dob.date,
                    dobAge = it.dob.age,
                    registeredDate = it.registered.date,
                    registeredAge = it.registered.age,
                    idName = it.id.name,
                    idValue = it.id.value,
                    nat = it.nat,
                    thumbnail = it.picture.thumbnail,
                    picture = it.picture.large
                )
            }

            val endOfPaginationReached = users.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    userDao.clearAll()
                }

                val keys = users.map {
                    RemoteKeys(
                        userId = it.id,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (endOfPaginationReached) null else page + 1
                    )
                }

                remoteKeysDao.insertAll(keys)
                userDao.insertAll(users)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UserEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { user ->
            remoteKeysDao.remoteKeysUserId(user.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UserEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { user ->
            remoteKeysDao.remoteKeysUserId(user.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, UserEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { userId ->
                remoteKeysDao.remoteKeysUserId(userId)
            }
        }
    }
}