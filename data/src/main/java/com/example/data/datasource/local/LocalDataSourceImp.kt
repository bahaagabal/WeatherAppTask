package com.example.data.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.core.domain.DataError
import com.example.data.datasource.LocalDataSource
import kotlinx.coroutines.flow.first
import java.io.IOException
import javax.inject.Inject
import  com.example.core.domain.Result

private val Context.dataStore by preferencesDataStore(name = "weather_prefs")

class LocalDataSourceImp @Inject constructor(
    private val context: Context
) : LocalDataSource {

    private val CITY_KEY = stringPreferencesKey("last_searched_city")

    override suspend fun saveLastSearchedCity(city: String): Result<Unit, DataError.Local> {
        return try {
            context.dataStore.edit { preferences ->
                preferences[CITY_KEY] = city
            }
            Result.Success(Unit)
        } catch (e: IOException) {
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getLastSearchedCity(): Result<String?, DataError.Local> = try {
        val prefs = context.dataStore.data.first()
        Result.Success(prefs[CITY_KEY])
    } catch (e: IOException) {
        Result.Error(DataError.Local.DISK_FULL)
    } catch (e: Exception) {
        Result.Error(DataError.Local.UNKNOWN)
    }
}