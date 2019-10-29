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

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import arrow.core.Either
import com.monkeyapp.numbers.apphelpers.snackbar
import kotlinx.android.synthetic.main.content_full_screen.*

class FullScreenFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels { MainViewModel.factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_full_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wordsTextView.text = when(val numberWordsText = mainViewModel.numberWordsText.value) {
            is Either.Right -> numberWordsText.b
            else -> ""
        }

        wordsTextView.setOnClickListener { copyToClipboard() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.full_screen, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.copy_to_clipboard -> copyToClipboard()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun copyToClipboard(): Boolean {
        getSystemService(requireContext(), ClipboardManager::class.java)?.let { clipboard ->
            val clip = ClipData.newPlainText(getString(R.string.app_name), wordsTextView.text)
            clipboard.setPrimaryClip(clip)

            wordsTextView.snackbar(R.string.full_screen_copied_to_clipboard)
            return true
        }

        return false
    }
}