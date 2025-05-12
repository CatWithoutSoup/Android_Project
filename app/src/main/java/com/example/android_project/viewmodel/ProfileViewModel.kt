package com.example.android_project.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_project.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = UserPreferencesRepository(application)
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> get() = _fullName
    fun onFullNameChanged(value: String) { _fullName.value = value }

    private val _position = MutableStateFlow("")
    val position: StateFlow<String> get() = _position
    fun onPositionChanged(value: String) { _position.value = value }

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> get() = _email
    fun onEmailChanged(value: String) { _email.value = value }

    private val _profileImageUrl = MutableStateFlow("https://cdn-icons-png.flaticon.com/512/287/287226.png")
    val profileImageUrl: StateFlow<String> = _profileImageUrl

    private val _deckUrl = MutableStateFlow("")
    val deckUrl: StateFlow<String> = _deckUrl
    fun onDeckUrlChanged(value: String) { _deckUrl.value = value }

    fun onProfileImageChanged(newImageUrl: String) {
        _profileImageUrl.value = newImageUrl
    }

    fun saveProfileData(fullName: String, position: String, email: String) {
        _fullName.value = fullName
        _position.value = position
        _email.value = email
    }

    fun updateProfile(fullName: String, position: String, email: String, avatarUri: String, deckUrl: String) {
        viewModelScope.launch {
            repository.saveUserProfile(fullName, position, email, avatarUri, deckUrl)
        }
    }
}