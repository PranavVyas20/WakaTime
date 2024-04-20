package com.example.wakatime.ui.widgets.glance

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.wakatime.R
import com.example.wakatime.data.model.WakaEditorData
import com.example.wakatime.data.model.WakaUserSummaryData
import com.example.wakatime.workmanager.WakaRefreshWorker

import androidx.compose.ui.graphics.Color
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.layout.size
import kotlin.math.log2


val colorList: List<Color> = listOf(
    Color(0xFF56A231),
    Color(0xFF9563D1),
    Color(0xFFD37126),
    Color(0xFF39877C),
    Color(0xFFA23573),
    Color(0xFFC38329),
    Color(0xFFA80064),
    Color(0xFFD8A800),
    Color(0xFF6400A8),
    Color(0xFFA80000),
    Color(0xFF0096D8),
)

@Composable
fun WakaGlanceWidgetLoadingUI() {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .background(GlanceTheme.colors.background)
            .padding(12.dp)
            .cornerRadius(20.dp),
        contentAlignment = androidx.glance.layout.Alignment.Center
    ) {
        CircularProgressIndicator(color = ColorProvider(day = Color.Black, night = Color.White))
    }
}

@Composable
fun WakaGlanceWidgetErrorUI(errorMessage: String?) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .background(GlanceTheme.colors.background)
            .padding(12.dp)
            .cornerRadius(20.dp),
        contentAlignment = androidx.glance.layout.Alignment.Center
    ) {
        errorMessage?.let {
            Text(text = it)
        }
    }
}

@Composable
fun WakaGlanceWidgetUI(data: WakaUserSummaryData) {
    val textColorProvider = remember {
        ColorProvider(day = Color.Black, night = Color.White)
    }
    val widthList = remember {
        adjustWidthsToRow(data.editors.map { it.percentage }, 180f)
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
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                Text(
                    text = "Screen time",
                    style = TextStyle(color = textColorProvider)
                )
                Box(modifier = GlanceModifier.defaultWeight()) {}
                androidx.glance.Image(
                    provider = ImageProvider(R.drawable.baseline_autorenew_24),
                    contentDescription = "refresh_icon",
                    modifier = GlanceModifier.clickable(actionRunCallback<RefreshAction>())
                        .size(20.dp)
                )
            }

            Spacer(modifier = GlanceModifier.height(4.dp))
            Text(
                text = "${data.totalHours}h ${data.totalMins}m",
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 32.sp,
                    color = textColorProvider
                )
            )
            Spacer(modifier = GlanceModifier.height(4.dp))
            if (data.editors.isNotEmpty()) {
                Row(
                    modifier = GlanceModifier
                        .cornerRadius(12.dp)
                        .fillMaxWidth()
                        .height(18.dp)
                        .background(Color.Red)
                ) {
                    widthList.forEachIndexed { index, fl ->
                        Box(
                            modifier = GlanceModifier
                                .background(color = colorList[index])
                                .width(fl.dp)
                                .fillMaxHeight()
                        ) {}
                    }
                }
            }

            Spacer(modifier = GlanceModifier.height(4.dp))
            LazyColumn {
                itemsIndexed(data.editors.take(10)) { index, item ->
                    WakaListItem(item, textColorProvider, textColor = colorList[index])
                }
            }
        }
    }
}

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        WakaRefreshWorker.start(context = context)
    }
}

@Composable
fun WakaListItem(
    wakaEditorData: WakaEditorData,
    textColorProvider: ColorProvider,
    textColor: Color
) {
    Row(modifier = GlanceModifier.fillMaxWidth().padding(bottom = 2.dp)) {
        Text(
            text = wakaEditorData.name,
            style = TextStyle(color = ColorProvider(textColor), fontWeight = FontWeight.Medium)
        )
        Box(modifier = GlanceModifier.defaultWeight()) {}
        Text(
            text = wakaEditorData.primaryScreenTime,
            style = TextStyle(color = textColorProvider, fontWeight = FontWeight.Medium)
        )
    }
}

@Preview
@Composable
fun test() {
    val percentList = remember {
//        listOf(46.25f, 38.52f, 14.59f, 0.45f, 0.19f)
        listOf(95f, 4.7f, 0.1f, 0.15f, 0.05f)
    }
    val colorList = remember {
        listOf(Color(0xFFFF7518), Color(0xFF93C572), Color(0xFF0096FF), Color.Green, Color.White)
    }
    val mappedPercentageList = remember {
        adjustWidthsToRow(
            percentList,
            180f
        )
    }
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.material3.Text(
            text = "Android studio",
            color = Color.White,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
        )

        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .width(180.dp)
                .height(18.dp)
                .padding(horizontal = 12.dp)
                .clip(shape = RoundedCornerShape(12.dp))
                .background(Color.Black)
        ) {
            mappedPercentageList.forEachIndexed { index, fl ->
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .width(fl.dp)
                        .fillMaxHeight()
                        .background(
                            colorList[index]
                        )
                ) {}
            }
        }

    }
}

fun adjustWidthsToRow(percentages: List<Float>, rowWidth: Float): List<Float> {
    // Convert percentages to actual widths
    val widths = percentages.map { it * rowWidth / 100 }

    // Adjust widths less than or equal to 20f to 14f
    val adjustedWidths = widths.map {
        if (it <= 20f) {
            14f
        } else {
            it
        }
    }

    // Apply log2 transformation to widths greater than 0f
    val transformedWidths = adjustedWidths.map {
        log2(it)
    }

    // Calculate the total width of the boxes
    val totalWidth = transformedWidths.sum()

    // Calculate the ratio of the row width to the total width of the boxes
    val ratio = rowWidth / totalWidth

    // Adjust the widths proportionally based on the ratio
     val list = transformedWidths.map { it * ratio }
    Log.d("taggggg", list.toString())
    return list
}

