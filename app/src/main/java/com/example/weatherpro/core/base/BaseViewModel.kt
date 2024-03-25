package com.example.weatherpro.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherpro.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

open class BaseViewModel<T>:ViewModel() {
    private var _viewState = MutableStateFlow<UiState<T>>(UiState.Empty())
    val viewState: StateFlow<UiState<T>> = _viewState.asStateFlow()

    protected suspend fun doOperation(
        operation: suspend () -> Flow<UiState<T>>
    ){
        val dataResult = operation()
        dataResult.onEach {
            when (it) {
                is UiState.Loading -> _viewState.value = UiState.Loading()
                is UiState.Success ->
                    if (it.data == null){
                        _viewState.value = UiState.Empty()
                    } else {
                        _viewState.value = UiState.Success(it.data)
                    }
                is UiState.Error -> _viewState.value = UiState.Error(it.message?:"Some error")
                is UiState.Empty -> _viewState.value = UiState.Empty()
            }
        }.launchIn(viewModelScope)
    }
}