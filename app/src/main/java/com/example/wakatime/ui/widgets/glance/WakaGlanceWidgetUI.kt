package com.example.wakatime.ui.widgets.glance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.wakatime.data.model.WakaEditorData
import com.example.wakatime.data.model.WakaUserSummaryData


@Composable
fun WakaGlanceWidgetUI(data: WakaUserSummaryData) {
    val textColorProvider = remember {
        ColorProvider(day = Color.Black, night = Color.White)
    }
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .background(GlanceTheme.colors.background)
            .padding(12.dp)
            .cornerRadius(20.dp)
    ) {
        Column(modifier = GlanceModifier.fillMaxSize()) {
            Text(
                text = "Screen time",
                style = TextStyle(color = textColorProvider)
            )
            Spacer(modifier = GlanceModifier.height(4.dp))
            Text(
                text = "${data.totalHours}h ${data.totalMins}m",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = textColorProvider
                )
            )
            Spacer(modifier = GlanceModifier.height(4.dp))
            LazyColumn {
                items(data.editors) {
                    WakaListItem(it, textColorProvider)
                }
            }
        }
    }
}

@Composable
fun WakaListItem(wakaEditorData: WakaEditorData, textColorProvider: ColorProvider) {
    Row(modifier = GlanceModifier.fillMaxWidth()) {
        Text(
            text = "${wakaEditorData.hours}h ${wakaEditorData.mins}m",
            style = TextStyle(color = textColorProvider)
        )
        Box(modifier = GlanceModifier.defaultWeight()) {}
        Text(text = wakaEditorData.name, style = TextStyle(color = textColorProvider))
    }
}



