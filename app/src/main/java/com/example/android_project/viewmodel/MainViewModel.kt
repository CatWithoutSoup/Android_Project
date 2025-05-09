package com.example.android_project.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _selectedScreen = MutableStateFlow("screen1")
    val selectedScreen = _selectedScreen.asStateFlow()

    fun onScreenSelected(route: String) {
        _selectedScreen.value = route
    }
}