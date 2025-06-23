package com.example.currentweather.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.domain.models.CurrentWeather
import com.example.currentweather.model.CurrentWeatherUiState
import com.example.currentweather.vm.CurrentWeatherViewModel
import androidx.compose.ui.graphics.Color
import com.example.core.presentation.components.WeatherSection

@Composable
fun CurrentWeatherRootScreen(
    city: String,
    viewModel: CurrentWeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.observeAsState()

    LaunchedEffect(city) {
        if (city.isNotBlank()) {
            viewModel.loadCurrentWeather(city)
        } else {
            viewModel.loadSavedCityWeather()
        }
    }

    CurrentWeatherScreen(uiState = uiState)
}

@Composable
fun CurrentWeatherScreen(uiState: CurrentWeatherUiState?) {
    Column(modifier = Modifier.padding(16.dp)) {
        when {
            uiState?.isLoading == true -> {
                CircularProgressIndicator()
            }

            uiState?.weather != null -> {
                WeatherSection(weather = uiState.weather)
            }

            uiState?.error != null -> {
                Text(
                    text = uiState.error,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherScreenPreview() {
    CurrentWeatherScreen(
        uiState = CurrentWeatherUiState(
            weather = CurrentWeather(
                temperature = 26.5,
                condition = "Sunny",
                iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
                city = "cairo"
            )
        )
    )
}


