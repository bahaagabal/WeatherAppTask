package com.example.forecast.vm

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
import com.example.core.presentation.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ForecastWeatherViewModel @Inject constructor(
    private val get7DaysForecastUseCase: GetForecastUseCase,
    private val getLastSearchedCityUseCase: GetLastSearchedCityUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ForecastWeatherUiState())
    val state = _state.asStateFlow()

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
        _state.update {
            it.copy(
                isLoading = true
            )
        }

        viewModelScope.launch {
            when (val result = get7DaysForecastUseCase(city)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            forecast = result.data,
                            isLoading = false
                        )
                    }
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            error = result.error.toUiText(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}
