package com.itis.my.utils

import java.time.Instant
import java.time.ZoneId


fun getDateString(date: Long): String {
    val zdt =
        Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDateTime()
    return if (zdt.month.value > 9) "${zdt.dayOfMonth}.${zdt.month.value}.${zdt.year}  ${formatHour(zdt.hour)}:${formatMinute(zdt.minute)}:${formatHour(zdt.second)}" else "${zdt.dayOfMonth}.0${zdt.month.value}.${zdt.year}  ${formatHour(zdt.hour)}:${formatMinute(zdt.minute)}:${formatHour(zdt.second)}"
}

private fun formatHour(hour: Int): String {
    return if (hour > 9) hour.toString() else "0$hour"
}


private fun formatMinute(minute: Int): String {
    return if (minute > 9) minute.toString() else "0$minute"
}


private fun formatSecond(second: Int): String {
    return if (second > 9) second.toString() else "0$second"
}
