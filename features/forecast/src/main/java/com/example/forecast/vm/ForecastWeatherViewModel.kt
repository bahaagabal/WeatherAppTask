package com.example.forecast.vm

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecases.GetForecastUseCase
import com.example.core.domain.usecases.GetLastSearchedCityUseCase
import com.example.forecast.contract.ForecastWeatherIntent
import com.example.forecast.contract.ForecastWeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.core.domain.Result

@HiltViewModel
class ForecastWeatherViewModel @Inject constructor(
    private val get7DaysForecastUseCase: GetForecastUseCase,
    private val getLastSearchedCityUseCase: GetLastSearchedCityUseCase
) : ViewModel() {

    var uiState by mutableStateOf(ForecastWeatherUiState())
        private set

    fun onIntent(intent: ForecastWeatherIntent) {
        when (intent) {
            is ForecastWeatherIntent.LoadForecast -> loadForecast(intent.city)
            is ForecastWeatherIntent.LoadSavedCityForecast -> loadSavedCityForecast()
        }
    }

    private fun loadSavedCityForecast() {
        viewModelScope.launch {
            when (val result = getLastSearchedCityUseCase()) {
                is Result.Success -> {
                    val city = result.data.orEmpty()
                    if (city.isNotBlank()) {
                        loadForecast(city)
                    }
                }

                else -> {
                    // No-op or log
                }
            }
        }
    }

    private fun loadForecast(city: String) {
        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch {
            when (val result = get7DaysForecastUseCase(city)) {
                is Result.Success -> {
                    uiState = ForecastWeatherUiState(
                        forecast = result.data,
                        isLoading = false
                    )
                }

                is Result.Error -> {
                    uiState = ForecastWeatherUiState(
                        error = result.error.toString(),
                        isLoading = false
                    )
                }
            }
        }
    }
}
