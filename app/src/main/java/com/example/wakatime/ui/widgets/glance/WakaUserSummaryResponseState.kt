package com.example.wakatime.ui.widgets.glance

import com.example.wakatime.data.model.WakaUserSummaryData
import kotlinx.serialization.Serializable

@Serializable
sealed interface WakaUserSummaryResponseState{

    @Serializable
    data object IsLoading : WakaUserSummaryResponseState

    @Serializable
    data class Success(val data: WakaUserSummaryData) : WakaUserSummaryResponseState

    @Serializable
    data class Error(val message: String?) : WakaUserSummaryResponseState

}