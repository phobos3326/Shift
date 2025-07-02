package com.example.shift.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.shift.data.UserDto
import com.example.shift.data.UserEntity
import com.example.shift.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repo: UserRepository
) : ViewModel() {

    /*val users = repo.getUsers().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()*/

    init {
        //refresh()
    }

    /*fun refresh() {
        viewModelScope.launch {
            try {
                repo.refreshUsers()
            } catch (e: Exception) {
                _event.emit("Ошибка: ${e.localizedMessage}")
            }
        }
    }*/


    /*val users: Flow<PagingData<UserEntity>> = repo.getUsers()
        .cachedIn(viewModelScope)

    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()*/

    val users = repo.getUsers().cachedIn(viewModelScope)
}