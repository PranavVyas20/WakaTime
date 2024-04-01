package com.example.wakatime.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wakatime.data.reposiitory.WakaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WakaViewModel @Inject constructor(
    private val wakaRepository: WakaRepository
) : ViewModel() {

    fun getWakaUserSummary() {
        viewModelScope.launch {
            wakaRepository.getWakaUserSummary()
                .onSuccess {
                    Log.d("waka_time", "$it")
                }
                .onFailure {
                    Log.d("waka_time", "${it.message}")
                }
        }
    }
}