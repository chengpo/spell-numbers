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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import com.monkeyapp.numbers.apphelpers.icon
import com.monkeyapp.numbers.apphelpers.ocrIntent
import com.monkeyapp.numbers.apphelpers.onClick
import com.monkeyapp.numbers.apphelpers.snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_digit_pad.*
import kotlinx.android.synthetic.main.content_number_word.*

class MainFragment : Fragment() {
    private val mainViewModel: MainViewModel
            by viewModels(this::requireActivity) { MainViewModel.factory }

    private val ratingPrompter: RatingPrompter = RatingPrompter(this::requireContext) { digitPadView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        omniButtonView.onClick {
            when ((it as OmniButton).state) {
                OmniButton.State.Clean ->
                    mainViewModel.reset()

                OmniButton.State.Camera ->
                    startActivityForResult(context!!.ocrIntent, REQUEST_CODE_OCR_CAPTURE)
            }
        }

        digitPadView.forEach {
            if (it.id == R.id.btnDel) {
                it.setOnLongClickListener{
                    mainViewModel.reset()
                    true
                }
            }

            it.onClick { button ->
                when {
                    button.id == R.id.btnDel ->
                        mainViewModel.backspace()

                    button is Button && (button.text[0] == '.' || button.text[0] in '0'..'9') ->
                        mainViewModel.append(button.text[0])
                }
            }
        }

        wordsTextView.onClick {
            val wordsText = wordsTextView.text.toString()
            if (wordsText.isNotBlank()) {
                try {
                    NavHostFragment.findNavController(my_nav_host_fragment)
                            .navigate(R.id.action_main_to_full_screen)
                } catch (e: IllegalArgumentException) {
                    Log.e("MainFragment", "navigation failed", e)
                }
            }
        }

        mainViewModel.numberWords.observe(viewLifecycleOwner, Observer { numberWords ->
            numberWords?.let {
                numberTextView.text = it.numberText
                omniButtonView.state = if (it.numberText.isEmpty()) {
                    OmniButton.State.Camera
                } else {
                    OmniButton.State.Clean
                }

                wordsTextView.text = it.wordsText
            }
        })

        mainViewModel.error.observe(viewLifecycleOwner, Observer {
            digitPadView.snackbar(R.string.too_large_to_spell) {
                icon(R.drawable.ic_error, R.color.accent)
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
        val navController = NavHostFragment.findNavController(my_nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    private companion object {
        const val REQUEST_CODE_OCR_CAPTURE = 1000
    }
}
