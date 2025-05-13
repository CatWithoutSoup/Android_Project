package com.example.android_project.data

import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKeys {
    val FULL_NAME = stringPreferencesKey("full_name")
    val POSITION = stringPreferencesKey("position")
    val EMAIL = stringPreferencesKey("email")
    val AVATAR_URI = stringPreferencesKey("avatar_uri")
    val DECK_URL = stringPreferencesKey("deck_url")
}