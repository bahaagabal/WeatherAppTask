package com.example.forecast.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core.domain.models.DailyForecast

@Composable
fun ForecastItem(day: DailyForecast) {
    val formattedDate = remember(day.date) {
        java.text.SimpleDateFormat("EEE, d MMM", java.util.Locale.getDefault())
            .format(java.util.Date(day.date * 1000))
    }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.Red),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = formattedDate)
        Text(text = "${day.temperature}°")
        AsyncImage(
            model = day.iconUrl,
            contentDescription = day.condition,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForecastItemPreview() {
    ForecastItem(
        day = DailyForecast(
            date = 1687507200000L,
            temperature = 26.5,
            condition = "Sunny",
            iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",

            )
    )
}
