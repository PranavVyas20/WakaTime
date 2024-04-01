package com.example.wakatime.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WakaUserSummaryData(
    val totalHours: Int,
    val totalMins: Int,
    val editors: List<WakaEditorData>
)

@Serializable
data class WakaEditorData(
    val name: String,
    val hours: Int,
    val mins: Int
)