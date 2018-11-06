/*
MIT License

Copyright (c) 2017 - 2018 Po Cheng

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
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import arrow.core.Try
import arrow.core.getOrElse
import com.monkeyapp.numbers.R.id.my_nav_host_fragment
import com.monkeyapp.numbers.apphelpers.icon
import com.monkeyapp.numbers.apphelpers.ocrIntent
import com.monkeyapp.numbers.apphelpers.snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_number_word.*

class MainFragment : Fragment() {
    private  val requestCodeOcrCapture = 1000

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        mainViewModel.observe(this) { viewObj ->
            viewObj?.let {
                numberTextView.text = it.numberText
                omniButton.state = if (it.numberText.isEmpty())
                    OmniButton.State.Camera
                else
                    OmniButton.State.Clean

                wordsTextView.text = it.wordsText
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wordsTextView.setOnClickListener {
            val wordsText = wordsTextView.text.toString()
            if (wordsText.isNotBlank()) {
                rippleView.stopRippleAnimation {
                    NavHostFragment.findNavController(my_nav_host_fragment)
                            .navigate(R.id.action_main_to_full_screen)
                }
            }
        }

        wordsTextView.setOnTouchListener { _, event ->
            rippleView.startRippleAnimation(event.x, event.y)
            false
        }

        // bind lifecycle to rating helper
        lifecycle.addObserver(RatingPrompter(context!!, wordsTextView))
    }

    fun onButtonClick(button: View?) {
        when {
            button?.id == R.id.btnDel ->
                mainViewModel.backspace()

            button is OmniButton ->
                when (button.state) {
                    OmniButton.State.Clean ->
                        mainViewModel.reset()
                    OmniButton.State.Camera ->
                        startActivityForResult(context!!.ocrIntent, requestCodeOcrCapture)
                }

            button is Button && (button.text[0] == '.' || button.text[0] in '0'..'9') ->
                Try {
                    mainViewModel.append(button.text[0])
                }.getOrElse {
                    wordsTextView.snackbar(R.string.too_large_to_spell) {
                        icon(R.drawable.ic_error, R.color.accent)
                    }

                    // revoke the last digit
                    mainViewModel.backspace()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeOcrCapture && resultCode == Activity.RESULT_OK) {
            val number = data?.getStringExtra("number") ?: ""
            if (number.isNotBlank()) {
                Try {
                    mainViewModel.reset()
                    number.forEach { digit ->
                        mainViewModel.append(digit)
                    }
                }.getOrElse {
                    wordsTextView.snackbar(R.string.too_large_to_spell) {
                        icon(R.drawable.ic_error, R.color.accent)
                    }

                    mainViewModel.reset()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) =
            when (item?.itemId) {
                R.id.action_about -> {
                    NavHostFragment.findNavController(my_nav_host_fragment)
                              .navigate(R.id.action_main_to_about)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}