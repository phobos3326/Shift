package com.example.shift.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shift.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repo: UserRepository
) : ViewModel() {

    val users = repo.getUsers().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()

    fun refresh() {
        viewModelScope.launch {
            try {
                repo.refreshUsers()
            } catch (e: Exception) {
                _event.emit("Ошибка: ${e.localizedMessage}")
            }
        }
    }
}