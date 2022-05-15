package com.itis.my.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


fun getTmpVideoFile(context: Context): File {
    return File.createTempFile(
        "MP4_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(Date())}_",
        ".mp4",
        context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
    ).apply {
        createNewFile()
        deleteOnExit()
    }
}

fun getTmpPhotoFile(context: Context): File {
    return File.createTempFile(
        "PNG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(Date())}_",
        ".png",
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    ).apply {
        createNewFile()
        deleteOnExit()
    }
}

fun getTmpAudioFile(context: Context): File {
    return File.createTempFile(
        "3GP_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(Date())}_",
        ".3gp",
        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
    ).apply {
        createNewFile()
        deleteOnExit()
    }
}