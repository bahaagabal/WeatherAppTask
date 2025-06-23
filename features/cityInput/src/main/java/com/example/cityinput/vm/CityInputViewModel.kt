package com.example.cityinput.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.Result
import com.example.core.domain.usecases.GetLastSearchedCityUseCase
import com.example.core.domain.usecases.SaveLastSearchedCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityInputViewModel @Inject constructor(
    private val saveCityUseCase: SaveLastSearchedCityUseCase,
    private val getCityUseCase: GetLastSearchedCityUseCase
) : ViewModel() {

    private val _city = MutableLiveData("")
    val city: LiveData<String> = _city

    fun onCityChanged(newCity: String) {
        _city.value = newCity
    }

    fun saveCityAndSubmit(onSaved: (String) -> Unit) {
        val currentCity = _city.value.orEmpty()
        if (currentCity.isNotBlank()) {
            viewModelScope.launch {
                saveCityUseCase(currentCity)
                onSaved(currentCity)
            }
        }
    }

    fun loadSavedCity() {
        viewModelScope.launch {
            val result = getCityUseCase()
            if (result is Result.Success) {
                _city.postValue(result.data.orEmpty())
            }
        }
    }
}