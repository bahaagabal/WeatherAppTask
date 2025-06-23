package com.example.data.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.datasource.LocalDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "weather_prefs")

class LocalDataSourceImp @Inject constructor(
    private val context: Context
) : LocalDataSource {

    private val CITY_KEY = stringPreferencesKey("last_searched_city")

    override suspend fun saveLastSearchedCity(city: String) {
        context.dataStore.edit { preferences ->
            preferences[CITY_KEY] = city
        }
    }

    override suspend fun getLastSearchedCity(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[CITY_KEY]
    }
}