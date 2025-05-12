package com.example.android_project.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "user_profile")

    private val dataStore = context.dataStore

    suspend fun saveUserProfile(
        fullName: String,
        position: String,
        email: String,
        avatarUri: String,
        deckUrl: String
    ) {
        dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.FULL_NAME] = fullName
            prefs[UserPreferencesKeys.POSITION] = position
            prefs[UserPreferencesKeys.EMAIL] = email
            prefs[UserPreferencesKeys.AVATAR_URI] = avatarUri
            prefs[UserPreferencesKeys.DECK_URL] = deckUrl
        }
    }

    val userProfileFlow: Flow<UserProfile> = dataStore.data
        .map { prefs ->
            UserProfile(
                fullName = prefs[UserPreferencesKeys.FULL_NAME] ?: "",
                position = prefs[UserPreferencesKeys.POSITION] ?: "",
                email = prefs[UserPreferencesKeys.EMAIL] ?: "",
                avatarUri = prefs[UserPreferencesKeys.AVATAR_URI] ?: "",
                deckUrl = prefs[UserPreferencesKeys.DECK_URL] ?: ""
            )
        }
}