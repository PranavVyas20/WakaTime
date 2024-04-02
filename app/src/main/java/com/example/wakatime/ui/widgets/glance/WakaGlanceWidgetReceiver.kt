package com.example.wakatime.ui.widgets.glance

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.example.wakatime.workmanager.WakaSyncWorker

class WakaGlanceWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = WakaGlanceWidget

    override fun onEnabled(context: Context) {
        Log.d("waka_tag", "enabled")
        super.onEnabled(context)
        WakaSyncWorker.start(context = context)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        WakaSyncWorker.cancel(context = context)
        super.onDeleted(context, appWidgetIds)
    }
}