package com.example.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.core.domain.DataError
import com.example.data.datasource.LocalDataSource
import kotlinx.coroutines.flow.first
import java.io.IOException
import javax.inject.Inject
import  com.example.core.domain.Result

class LocalDataSourceImp @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : LocalDataSource {

    private val CITY_KEY = stringPreferencesKey("last_searched_city")

    override suspend fun saveLastSearchedCity(city: String): Result<Unit, DataError.Local> {
        return try {
            dataStore.edit { preferences ->
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
        val prefs = dataStore.data.first()
        Result.Success(prefs[CITY_KEY])
    } catch (e: IOException) {
        Result.Error(DataError.Local.DISK_FULL)
    } catch (e: Exception) {
        Result.Error(DataError.Local.UNKNOWN)
    }
}