package com.example.weatherapptask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cityinput.view.CityInputRootScreen
import com.example.currentweather.view.CurrentWeatherRootScreen
import com.example.forecast.view.ForecastWeatherRootScreen
import com.example.weatherapptask.ui.theme.WeatherAppTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        here we define the 3 stories on the same single screen because it's approximately
        the same data no need to make a navigation if there is more than screen for example
        the detailed on we will make navController with navHost to handle the navigation
         */
        enableEdgeToEdge()
        setContent {
            WeatherAppTaskTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var city by rememberSaveable { mutableStateOf("") }
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState()),

                        ) {
                        CityInputRootScreen(
                            onSearchClicked = { searchedCity ->
                                city = searchedCity
                            }
                        )
                        CurrentWeatherRootScreen(city = city)
                        Spacer(modifier = Modifier.height(16.dp))
                        ForecastWeatherRootScreen(city = city)

                    }
                }
            }
        }
    }
}