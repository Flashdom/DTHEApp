package com.itis.my

import android.app.Application
import androidx.room.Room

class DTHEApplication : Application() {

    override fun onCreate() {
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "DTHEDATABASE")
            .build()
        super.onCreate()
    }

    companion object {
        lateinit var database: AppDatabase
    }
}