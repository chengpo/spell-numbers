/*
MIT License

Copyright (c) 2017 - 2019 Po Cheng

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.monkeyapp.numbers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val navController = Navigation.findNavController(this, R.id.my_nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        toolbar.setupWithNavController(navController, appBarConfiguration)

        // FIXME: reserve space for ad
        // adView.minimumHeight = AdSize.SMART_BANNER.getHeightInPixels(this)

        // load ad
        adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                FirebaseAnalytics.getInstance(applicationContext)
                        .logEvent("AdLoadingFailed", bundleOf("ErrorCode" to errorCode.toString()))
            }
        }

        adView.loadAd(AdRequest.Builder().build())
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onPause() {
        super.onPause()
        adView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.destroy()
    }

    override fun onSupportNavigateUp() =
            Navigation.findNavController(this, R.id.my_nav_host_fragment).navigateUp()
}
