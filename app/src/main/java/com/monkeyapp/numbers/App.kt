package com.monkeyapp.numbers

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds

@Suppress("unused")
class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // enable night mode base on system setting
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            // enable night mode when battery save mode is on
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        }

        // set ads id
        MobileAds.initialize(this, "ca-app-pub-6498719425690429~1480158317")
    }
}
