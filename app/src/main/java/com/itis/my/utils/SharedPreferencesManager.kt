package com.itis.my.utils

import android.content.Context

object SharedPreferencesManager {

    fun isFirstLaunch(context: Context): Boolean {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE).getBoolean(
            ARG_IS_FIRST_LAUNCH, true
        )
    }

    fun setLaunched(context: Context) {
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().putBoolean(
            ARG_IS_FIRST_LAUNCH, false
        ).apply()
    }

    private const val NAME = "Settings"
    private const val ARG_IS_FIRST_LAUNCH = "AIFL"
}