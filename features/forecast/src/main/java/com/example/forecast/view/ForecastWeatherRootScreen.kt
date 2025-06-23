package com.example.forecast.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.forecast.contract.ForecastWeatherIntent
import com.example.forecast.contract.ForecastWeatherUiState
import com.example.forecast.vm.ForecastWeatherViewModel

@Composable
fun ForecastWeatherRootScreen(
    city: String,
    viewModel: ForecastWeatherViewModel = hiltViewModel()
) {
    val state = viewModel.uiState

    LaunchedEffect(city) {
        if (city.isNotBlank()) {
            viewModel.onIntent(ForecastWeatherIntent.LoadForecast(city))
        } else {
            viewModel.onIntent(ForecastWeatherIntent.LoadSavedCityForecast)
        }
    }

    ForecastWeatherScreen(uiState = state)
}

@Composable
fun ForecastWeatherScreen(uiState: ForecastWeatherUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }

            uiState.forecast != null -> {
                /*
                we make the list here in static size because it's inside a coloumn so
                you have to make it's height static or remove the lazy column and iterate manually
                through the data
                 */
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(400.dp)
                ) {
                    items(uiState.forecast, key = { item -> item }) { day ->
                        ForecastItem(day)
                    }
                }
            }

            uiState.error != null -> {
                Text(
                    text = uiState.error.asString(),
                    color = Color.Red,
                )
            }
        }
    }
}