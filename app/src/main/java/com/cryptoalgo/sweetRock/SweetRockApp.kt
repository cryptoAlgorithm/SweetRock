package com.cryptoalgo.sweetRock

import android.app.Application
import com.google.firebase.FirebaseApp

class SweetRockApp: Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialise Firebase App Check
        FirebaseApp.initializeApp(this)
        // Temporarily disabled app check
        // val appCheck = FirebaseAppCheck.getInstance()
        /*appCheck.installAppCheckProviderFactory(
            // if (0 != (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE)) DebugAppCheckProviderFactory.getInstance()
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )*/
    }
}