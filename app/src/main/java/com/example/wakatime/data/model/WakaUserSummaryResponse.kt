package com.example.wakatime.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


data class WakaUserSummaryResponse(
    @SerializedName("cumulative_total")
    val wakaCumulativeTotal: WakaCumulativeTotal? = null,
    @SerializedName("daily_average")
    val wakaDailyAverage: WakaDailyAverage? = null,
    @SerializedName("data")
    val wakaData: List<WakaData>? = null
)

data class WakaCumulativeTotal(
    @SerializedName("decimal")
    val decimal: String? = null,
    @SerializedName("digital")
    val digital: String? = null,
    @SerializedName("seconds")
    val seconds: Float? = null,
    @SerializedName("text")
    val text: String? = null
)

data class WakaDailyAverage(
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("text_including_other_language")
    val textIncludingOtherLanguage: String? = null
)

data class WakaData(
    @SerializedName("editors")
    val editors: List<WakaEditor>? = null,
    @SerializedName("grand_total")
    val grandTotal: WakaGrandTotal? = null
)

data class WakaEditor(
    @SerializedName("decimal")
    val decimal: String? = null,
    @SerializedName("digital")
    val digital: String? = null,
    @SerializedName("hours")
    val hours: Int? = null,
    @SerializedName("minutes")
    val minutes: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("percent")
    val percent: Float? = null,
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("total_seconds")
    val totalSeconds: Float? = null
)

data class WakaGrandTotal(
    @SerializedName( "decimal")
    val decimal: String? = null,
    @SerializedName( "digital")
    val digital: String? = null,
    @SerializedName( "hours")
    val hours: Int? = null,
    @SerializedName( "minutes")
    val minutes: Int? = null,
    @SerializedName( "text")
    val text: String? = null,
)

fun WakaUserSummaryResponse.toWakaUserSummaryData(): WakaUserSummaryData = WakaUserSummaryData(
    totalHours = convertSecondsToHoursAndMinutes(this.wakaCumulativeTotal?.seconds?:0f).first,
    totalMins = convertSecondsToHoursAndMinutes(this.wakaCumulativeTotal?.seconds?:0f).second,
    editors = this.wakaData?.get(0)?.editors?.map { it.toWakaEditorData() }?: emptyList()
)

private fun WakaEditor.toWakaEditorData(): WakaEditorData = WakaEditorData(
    name = this.name?:"",
    hours = convertSecondsToHoursAndMinutes(this.totalSeconds?:0f).first,
    mins = convertSecondsToHoursAndMinutes(this.totalSeconds?:0f).second,
)

private fun convertSecondsToHoursAndMinutes(totalSeconds: Float): Pair<Int, Int> {
    val totalSecondsInt = totalSeconds.toInt()
    val hours = totalSecondsInt / 3600
    val remainingSeconds = totalSecondsInt % 3600
    val minutes = remainingSeconds / 60
    return Pair(hours, minutes)
}

