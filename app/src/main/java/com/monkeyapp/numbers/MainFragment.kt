/*
MIT License

Copyright (c) 2017 - 2020 Po Cheng

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
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import arrow.core.Either
import com.monkeyapp.numbers.apphelpers.*
import com.monkeyapp.numbers.translators.SpellerError

class MainFragment : Fragment() {
    private val digitPadView
        get() = requireView().findViewById<ViewGroup>(R.id.digitPadView)

    private val omniButtonView
        get() = requireView().findViewById<OmniButton>(R.id.omniButtonView)

    private val wordsTextView
        get() = requireView().findViewById<TextView>(R.id.wordsTextView)

    private val numberTextView
        get() = requireView().findViewById<TextView>(R.id.numberTextView)

    private val mainViewModel: MainViewModel by viewModels { MainViewModel.factory }
    private val ratingPrompter: RatingPrompter by lazy { RatingPrompter(this::requireContext) { digitPadView } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        omniButtonView.setOnClickListener {
            when ((it as OmniButton).state) {
                OmniButton.State.Clean ->
                    mainViewModel.reset()

                OmniButton.State.Camera ->
                    startActivityForResult(requireContext().ocrIntent, REQUEST_CODE_OCR_CAPTURE)
            }
        }

        digitPadView.findFirstAndTakeAction(
                predicate = {it.id == R.id.btnDel },
                action = {
                    // long click to reset number
                    setOnLongClickListener {
                        mainViewModel.reset()
                        true
                    }

                    // delete last digit
                    setOnClickListener {
                        mainViewModel.backspace()
                    }
                })

        digitPadView.findAllAndTakeAction(
                predicate = { button ->
                    button is Button && (button.text[0] == '.' || button.text[0] in '0'..'9')},

                action = {
                    setOnClickListener {
                        mainViewModel.append((this as Button).text[0])
                    }
                }
        )

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

        mainViewModel.formattedNumberText.observe(viewLifecycleOwner, Observer {
            numberTextView.text = it

            omniButtonView.state = if (it.isEmpty()) {
                OmniButton.State.Camera
            } else {
                OmniButton.State.Clean
            }
        })

        mainViewModel.numberWordsText.observe(viewLifecycleOwner, Observer {
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
        })

        // attach rating prompter
        ratingPrompter.attach(viewLifecycleOwner)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OCR_CAPTURE && resultCode == Activity.RESULT_OK) {
            val number = data?.getStringExtra("number") ?: ""
            if (number.isNotBlank()) {
                mainViewModel.reset()
                number.forEach { digit ->
                    mainViewModel.append(digit)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController()) || super.onOptionsItemSelected(item)
    }

    private companion object {
        const val REQUEST_CODE_OCR_CAPTURE = 1000
    }
}
