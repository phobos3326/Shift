package com.example.shift.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: UserApi,
    private val dao: UserDao
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getUsers(): Flow<PagingData<UserEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 30,
                enablePlaceholders = false
            ),
            remoteMediator = UserRemoteMediator(api, dao),
            pagingSourceFactory = { dao.pagingSource() }
        ).flow
    }
}