package com.example.data.datasource

interface LocalDataSource {

    suspend fun saveLastSearchedCity(city: String)
    suspend fun getLastSearchedCity(): String?
}