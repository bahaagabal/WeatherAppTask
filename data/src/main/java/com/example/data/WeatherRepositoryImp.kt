package com.example.data

import com.example.core.domain.DataError
import com.example.core.domain.map
import com.example.core.domain.models.CurrentWeather
import com.example.core.domain.models.DailyForecast
import com.example.core.domain.repository.WeatherRepository
import com.example.data.datasource.LocalDataSource
import com.example.data.datasource.RemoteDataSource
import com.example.data.mappers.CurrentWeatherMapper
import com.example.data.mappers.DailyForecastMapper
import javax.inject.Inject
import com.example.core.domain.Result

class WeatherRepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val currentWeatherMapper: CurrentWeatherMapper,
    private val forecastMapper: DailyForecastMapper
) : WeatherRepository {

    override suspend fun getCurrentWeather(city: String): Result<CurrentWeather, DataError.Remote> {
        return remoteDataSource.getCurrentWeather(city).map {
            currentWeatherMapper.map(it)
        }
    }

    override suspend fun get7DayForecast(city: String): Result<List<DailyForecast>, DataError.Remote> {
        return remoteDataSource.get7DayForecast(city).map { list ->
            list.map { forecastMapper.map(it) }
        }
    }

    override suspend fun saveLastSearchedCity(city: String) {
        localDataSource.saveLastSearchedCity(city)
    }

    override suspend fun getLastSearchedCity(): String? {
        return localDataSource.getLastSearchedCity()
    }
}