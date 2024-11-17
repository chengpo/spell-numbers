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

package com.monkeyapp.numbers.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.monkeyapp.numbers.BuildConfig
import com.monkeyapp.numbers.R

@Preview("About")
@Composable
private fun Preview() {
    ContentView()
}

@Composable
fun ContentView(onLaunchUrl: ((String) -> Unit)? = null) {
    AppCompatTheme {
        AboutView(onLaunchUrl)
    }
}

@Composable
private fun AboutView(onLaunchUrl: ((String) -> Unit)?) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Image(
            painter = painterResource(R.mipmap.ic_launcher_round),
            modifier = Modifier
                .size(160.dp)
                .padding(10.dp),
            contentScale = ContentScale.FillBounds,
            contentDescription = stringResource(R.string.app_name)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = stringResource(R.string.about_app_version, BuildConfig.VERSION_NAME),
                style = MaterialTheme.typography.body2
            )
        }

        Spacer(modifier = Modifier.size(160.dp))

        Column {
            val aboutText = buildAnnotatedString {
                val projectUrl = "https://github.com/chengpo/spell-numbers"
                append(stringResource(R.string.about_app_project))

                pushStringAnnotation(tag = "URL", annotation = projectUrl)
                withStyle(
                    style = SpanStyle(
                        color = colorResource(id = R.color.link_text),
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append(projectUrl)
                }
                pop()
            }

            val aboutModifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
            val aboutStyle =
                TextStyle(textAlign = TextAlign.Center,
                    fontFamily = FontFamily.SansSerif,
                    color = colorResource(id = R.color.primary_text)
                )
                    .merge(MaterialTheme.typography.body2)

            ClickableText(
                text = aboutText,
                modifier = aboutModifier,
                style = aboutStyle
            ) { offset ->
                aboutText.getStringAnnotations(tag = "URL", offset, offset)
                    .firstOrNull()?.let { annotation ->
                        onLaunchUrl?.invoke(annotation.item)
                    }
            }
        }

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            Text(
                text = stringResource(R.string.about_app_copyright),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption
            )
        }
    }
}