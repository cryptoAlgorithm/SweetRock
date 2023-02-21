package com.cryptoalgo.sweetRock

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

class SweetRockApp: Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialise Firebase App Check
        FirebaseApp.initializeApp(this)
        // Temporarily disabled app check
        val appCheck = FirebaseAppCheck.getInstance()
        appCheck.installAppCheckProviderFactory(
            if (BuildConfig.DEBUG) DebugAppCheckProviderFactory.getInstance()
            else PlayIntegrityAppCheckProviderFactory.getInstance()
        )
    }
}