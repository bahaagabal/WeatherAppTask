package com.example.cityinput.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cityinput.R
import com.example.cityinput.vm.CityInputViewModel

@Composable
fun CityInputRootScreen(
    viewModel: CityInputViewModel = hiltViewModel(),
    onSearchClicked: (String) -> Unit
) {
    val city by viewModel.city.observeAsState("")

    LaunchedEffect(Unit) {
        viewModel.loadSavedCity()
    }
    CityInputScreen(
        city = city,
        onCityChanged = viewModel::onCityChanged,
        onSearchClicked = {
            viewModel.saveCityAndSubmit {
                onSearchClicked(it)
            }
        }
    )

}

@Composable
fun CityInputScreen(
    city: String,
    onCityChanged: (String) -> Unit,
    onSearchClicked: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.feature_city_input_search_weather),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        OutlinedTextField(
            value = city,
            onValueChange = onCityChanged,
            label = { Text(stringResource(R.string.feature_city_input_city_name)) },
            placeholder = { Text(stringResource(R.string.feature_city_input_enter_your_city)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (city.isNotBlank()) {
                        onSearchClicked()
                    }
                }
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CityInputScreenPreview() {
    CityInputScreen(
        city = "Cairo",
        onCityChanged = {},
        onSearchClicked = {}
    )
}