package com.itis.my

import android.app.Application
import androidx.room.Room
import com.google.firebase.FirebaseApp

class DTHEApplication : Application() {

    override fun onCreate() {
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "DTHEDATABASE")
            .build()
        FirebaseApp.initializeApp(applicationContext)
        super.onCreate()
    }

    companion object {
        lateinit var database: AppDatabase
    }
}