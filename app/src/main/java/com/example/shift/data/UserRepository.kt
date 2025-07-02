package com.example.shift.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: UserApi,
    private val db: AppDatabase
) {
    private val dao = db.userDao()
    @OptIn(ExperimentalPagingApi::class)
    fun getUsers(): Flow<PagingData<UserEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            remoteMediator = UserRemoteMediator(api, db),
            pagingSourceFactory = { db.userDao().pagingSource() }
        ).flow
    }

    fun getUserById(userId: String): Flow<UserEntity?> = dao.getUserById(userId)
}