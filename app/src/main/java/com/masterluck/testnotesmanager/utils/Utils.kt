package com.masterluck.testnotesmanager.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    // Check weather string format of date of note is the same that today's
    // using @formatForCheck format

    val formatForCheck = SimpleDateFormat("yyyyMMdd")
    val formatToday = SimpleDateFormat("hh:mm")
    val formatPast = SimpleDateFormat("dd.MM.yyyy")

    fun formatDate(time: Long): String {
        val date = Date(time)
        return if (formatForCheck.format(Calendar.getInstance().time) == formatForCheck.format(date))
            formatToday.format(date)
        else
            formatPast.format(date)
    }

}