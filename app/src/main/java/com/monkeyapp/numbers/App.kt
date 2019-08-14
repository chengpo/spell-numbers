package com.monkeyapp.numbers

import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this, "ca-app-pub-6498719425690429~1480158317")
    }
}