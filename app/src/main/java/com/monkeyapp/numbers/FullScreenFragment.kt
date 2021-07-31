/*
MIT License

Copyright (c) 2017 - 2021 Po Cheng

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.google.accompanist.appcompattheme.createAppCompatTheme
import com.monkeyapp.numbers.apphelpers.snackbar

class FullScreenFragment : Fragment() {
    private val args: FullScreenFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                contentView()
            }
        }
    }

    @Composable
    private fun contentView() {
        AppCompatTheme {
            numberWordsView()
        }
    }

    @Preview
    @Composable
    private fun numberWordsView() {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.Center) {

            var multiplier by remember { mutableStateOf(1f) }
            val textStyle = MaterialTheme.typography.h5

            ClickableText(
                text = buildAnnotatedString {
                    append(args.numberWordsText)
                },
                maxLines = 5,
                modifier = Modifier.padding(20.dp),
                overflow = TextOverflow.Visible,
                style = textStyle.copy(
                    fontSize = textStyle.fontSize * multiplier,
                    color = colorResource(id = R.color.primary_text)
                ),
                onTextLayout = {
                    if (it.hasVisualOverflow) {
                        multiplier *= 0.99f
                    }
                },
            ) {
                copyToClipboard()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.full_screen, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.copy_to_clipboard -> {
                copyToClipboard()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun copyToClipboard() {
        getSystemService(requireContext(), ClipboardManager::class.java)?.let { clipboard ->
            val clip = ClipData.newPlainText(getString(R.string.app_name), args.numberWordsText)
            clipboard.setPrimaryClip(clip)

            requireView().snackbar(R.string.full_screen_copied_to_clipboard)
        }
    }
}


