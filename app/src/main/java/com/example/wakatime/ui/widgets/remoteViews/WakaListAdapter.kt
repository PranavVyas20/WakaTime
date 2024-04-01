package com.example.wakatime.ui.widgets.remoteViews

import android.content.Context
import android.widget.ArrayAdapter
import com.example.wakatime.R

data class wakaListItemTemp(
    val t1: String = "t1",
    val t2: String = "t2"
)

class WakaListAdapter(context: Context, data: ArrayList<wakaListItemTemp>) :
    ArrayAdapter<wakaListItemTemp>(context, R.layout.waka_list_item, data) {
}