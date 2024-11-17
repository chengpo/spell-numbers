/*
MIT License

Copyright (c) 2017 - 2024 Po Cheng

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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.children
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import arrow.core.Either
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.monkeyapp.numbers.apphelpers.*
import com.monkeyapp.numbers.translators.SpellerError

class MainFragment : Fragment() {
    private var adView: AdView? = null
    private val mainViewModel: MainViewModel by viewModels { MainViewModel.Factory() }

    private val ocrScannerLauncher =
        registerForActivityResult(object : ActivityResultContract<Unit, String>() {
            override fun createIntent(context: Context, input: Unit): Intent {
                return context.ocrIntent
            }

            override fun parseResult(resultCode: Int, intent: Intent?): String {
                return if (resultCode == Activity.RESULT_OK)
                    intent?.getStringExtra("number").orEmpty()
                else
                    ""
            }
        }) { number ->
            if (number.isNotBlank()) {
                mainViewModel.reset()
                number.forEach { digit ->
                    mainViewModel.append(digit)
                }
            }
        }

    override fun onResume() {
        super.onResume()
        adView?.resume()
    }

    override fun onPause() {
        super.onPause()
        adView?.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adView?.destroy()
        adView = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.content_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (adView == null) {
            adView = AdView(requireContext()).apply(::setupAdView)
        }

        val omniButtonView = view.findViewById<OmniButton>(R.id.omniButtonView)
        val digitPadView = view.findViewById<ViewGroup>(R.id.digitPadView)
        val wordsTextView = view.findViewById<TextView>(R.id.wordsTextView)
        val numberTextView = view.findViewById<TextView>(R.id.numberTextView)

        omniButtonView.setOnClickListener { omniButton ->
            (omniButton as? OmniButton)?.state?.let {
                if (it == OmniButton.State.Clean)
                    mainViewModel.reset()
                else if (it == OmniButton.State.Camera)
                    ocrScannerLauncher.launch(Unit)
            }
        }

        digitPadView.children
            .first { it.id == R.id.btnDel }
            .apply {
                // long click to reset number
                setOnLongClickListener {
                    mainViewModel.reset()
                    return@setOnLongClickListener true
                }

                // delete last digit
                setOnClickListener {
                    mainViewModel.backspace()
                }
            }

        digitPadView.children
            .filter { it is Button && (it.text[0] == '.' || it.text[0] in '0'..'9') }
            .forEach { child ->
                child.setOnClickListener {
                    mainViewModel.append((it as Button).text[0])
                }
            }

        wordsTextView.setOnClickListener {
            try {
                val wordsText = wordsTextView.text.toString()
                if (wordsText.isNotBlank()) {
                    val action = MainFragmentDirections.actionMainToFullScreen(wordsText)

                    findNavController().navigate(action)
                }
            } catch (e: IllegalArgumentException) {
                Log.e("MainFragment", "navigation failed", e)
            }
        }

        mainViewModel.formattedNumberText.observe(viewLifecycleOwner) {
            numberTextView.text = it

            omniButtonView.state = if (it.isEmpty()) {
                OmniButton.State.Camera
            } else {
                OmniButton.State.Clean
            }
        }

        mainViewModel.numberWordsText.observe(viewLifecycleOwner) {
            when (it) {
                is Either.Right -> wordsTextView.text = it.b
                is Either.Left -> {
                    when (it.a) {
                        SpellerError.NUMBER_IS_TOO_LARGE -> {
                            digitPadView.snackbar(R.string.too_large_to_spell) {
                                icon(R.drawable.ic_error, R.color.accent)
                            }
                        }
                    }
                }
            }
        }

        setupMenu()

        // attach rating prompter
        RatingPrompter(activity = requireActivity(), anchorView = digitPadView)
            .bind(viewLifecycleOwner)

        trackScreen()
    }

    private fun trackScreen() {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, MainFragment::class.java.simpleName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, MainFragment::class.java.simpleName)
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return menuItem.onNavDestinationSelected(findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupAdView(adView: AdView) {
        val adContainerView = requireView().findViewById<FrameLayout>(R.id.adViewContainer)

        fun adaptiveAdSize(): AdSize {
            val display = requireActivity().windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = adContainerView.width.toFloat()
            if (adWidthPixels == 0.0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    adWidthPixels /= 2
                }
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                requireContext(),
                adWidth
            )
        }

        adContainerView.doOnLayout {
            adView.apply {
                setAdSize(adaptiveAdSize())
                adUnitId = resources.getString(R.string.ad_unit_id)
                adListener = object : AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Firebase.analytics.logEvent("AdLoadingFailed") {
                            param("ErrorCode", error.code.toString())
                            param("ErrorMessage", error.message)
                        }
                    }
                }
            }

            adContainerView.addView(adView)
            adView.loadAd(AdRequest.Builder().build())
        }
    }
}