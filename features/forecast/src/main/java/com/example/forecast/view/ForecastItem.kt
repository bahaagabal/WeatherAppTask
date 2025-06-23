package com.example.forecast.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core.domain.models.DailyForecast
import com.example.weatherutils.formatDayFromTimestamp

@Composable
fun ForecastItem(day: DailyForecast) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatDayFromTimestamp(day.date),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${day.temperature}°",
                style = MaterialTheme.typography.bodyLarge
            )
            AsyncImage(
                model = day.iconUrl,
                contentDescription = day.condition,
                modifier = Modifier.size(40.dp)
            )
        }
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
