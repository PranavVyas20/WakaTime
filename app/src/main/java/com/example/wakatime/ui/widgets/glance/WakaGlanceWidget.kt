package com.example.wakatime.ui.widgets.glance

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition

object WakaGlanceWidget: GlanceAppWidget() {

    override val sizeMode: SizeMode
        get() = SizeMode.Responsive(AvailableSizes.sizes)

    override val stateDefinition: GlanceStateDefinition<WakaUserSummaryResponseState>
        get() = WakaGlanceStateDefinition
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val wakaState = currentState<WakaUserSummaryResponseState>()

            when(wakaState) {
                is WakaUserSummaryResponseState.Error -> {
                    WakaGlanceWidgetErrorUI(errorMessage = wakaState.message)
                    Log.d("waka_tag", "error_state")

                }
                WakaUserSummaryResponseState.IsLoading -> {
                    WakaGlanceWidgetLoadingUI()
                    Log.d("waka_tag", "loading_state")

                }
                is WakaUserSummaryResponseState.Success -> {
                    Log.d("waka_tag", "success_state")
                    WakaGlanceWidgetUI(wakaState.data)
                }
            }
        }
    }
}