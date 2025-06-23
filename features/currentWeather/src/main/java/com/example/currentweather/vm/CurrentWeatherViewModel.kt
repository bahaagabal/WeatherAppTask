package com.example.currentweather.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecases.GetCurrentWeatherUseCase
import com.example.core.domain.usecases.GetLastSearchedCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.core.domain.Result
import com.example.core.presentation.toUiText
import com.example.currentweather.model.CurrentWeatherUiState
import kotlinx.coroutines.launch

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getLastSearchedCityUseCase: GetLastSearchedCityUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<CurrentWeatherUiState>()
    val uiState: LiveData<CurrentWeatherUiState> = _uiState

    fun loadSavedCityWeather() {
        viewModelScope.launch {
            when (val result = getLastSearchedCityUseCase()) {
                is Result.Success -> {
                    val savedCity = result.data.orEmpty()
                    if (savedCity.isNotBlank()) {
                        loadCurrentWeather(savedCity)
                    }
                }

                else -> {
                    // Do Nothing
                }
            }
        }
    }

    fun loadCurrentWeather(city: String) {
        _uiState.value = CurrentWeatherUiState(isLoading = true)

        viewModelScope.launch {
            when (val result = getCurrentWeatherUseCase(city)) {
                is Result.Success -> {
                    _uiState.value = CurrentWeatherUiState(
                        weather = result.data,
                        isLoading = false
                    )
                }

                is Result.Error -> {
                    _uiState.value = CurrentWeatherUiState(
                        error = result.error.toUiText(),
                        isLoading = false
                    )
                }
            }
        }
    }
}
