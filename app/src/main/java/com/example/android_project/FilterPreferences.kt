package com.example.android_project

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "filters")

class FilterPreferences(private val context: Context) {

    private val filterKey = stringSetPreferencesKey("selected_filters")

    val selectedFilters: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[filterKey] ?: emptySet()
    }

    suspend fun saveFilters(filters: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[filterKey] = filters
        }
    }
}