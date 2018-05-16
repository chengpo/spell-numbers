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
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import com.monkeyapp.numbers.helpers.*
import kotlinx.android.synthetic.main.content_number_word.*
import com.monkeyapp.numbers.translators.EnglishNumberSpeller.LargeNumberException
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        // bind lifecycle to rating helper
        lifecycle.addObserver(RatingHelper(this, wordTextView))
        omniButton.state = OmniButton.State.Camera

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.digitStr.observe(this@MainActivity, Observer<String> { digits ->
            digits?.let {
                digitTextView.text = it
                omniButton.state = if (it.isEmpty())
                    OmniButton.State.Camera
                else
                    OmniButton.State.Clean
            }
        })

        mainViewModel.numberStr.observe(this@MainActivity, Observer<String> { numbers ->
            wordTextView.text = numbers
        })

        wordTextView.setOnClickListener {
            val numberWord = wordTextView.text.toString()
            if (numberWord.isNotBlank()) {
                rippleView.stopRippleAnimation {
                    showFullScreen(numberWord)
                }
            }
        }

        wordTextView.setOnTouchListener { _, event ->
            rippleView.startRippleAnimation(event.x, event.y)
            false
        }
    }

    fun onButtonClicked(button: View) {
        try {
            when {
                button.id == R.id.btnDel -> mainViewModel.deleteDigit()
                button is OmniButton ->
                    when (button.state) {
                        OmniButton.State.Clean -> mainViewModel.resetDigit()
                        OmniButton.State.Camera -> startActivityForResult(
                                Intent(INTENT_ACTION_OCR_CAPTURE),
                                RC_OCR_CAPTURE)
                    }
                button is Button ->
                    when (button.text[0]) {
                        '.', in '0'..'9' -> mainViewModel.appendDigit(button.text[0])
                    }
            }
        } catch (exception: LargeNumberException) {
            wordTextView.snackbar(R.string.too_large_to_spell) {
                icon(R.drawable.ic_error, R.color.accent)
            }

            // revoke the last digit
            mainViewModel.deleteDigit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) =
            when (item?.itemId) {
                R.id.action_about -> {
                    startActivity<AboutActivity>()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_OCR_CAPTURE && resultCode == Activity.RESULT_OK) {
            val number = data?.getStringExtra("number") ?: ""

            if (number.isNotBlank()) {
                mainViewModel.resetDigit()
                number.forEach {
                    mainViewModel.appendDigit(it)
                }
            }
        }
    }
}
