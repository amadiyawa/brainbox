package com.amadiyawa.feature_base.common.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}