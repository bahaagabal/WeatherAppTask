package com.example.data.datasource

import com.example.core.domain.DataError
import com.example.core.domain.Result

interface LocalDataSource {

    suspend fun saveLastSearchedCity(city: String): Result<Unit, DataError.Local>
    suspend fun getLastSearchedCity(): Result<String?, DataError.Local>
}