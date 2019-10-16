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


package com.monkeyapp.numbers.apphelpers

import android.util.TypedValue
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

inline fun View.snackbar(stringId: Int, length: Int = Snackbar.LENGTH_SHORT, prepare: Snackbar.() -> Snackbar = {this}) =
    Snackbar.make(this, stringId, length).apply {
        val snackText = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        if (snackText != null) {
            val snackbarIconPadding = TypedValue()
            context.theme.resolveAttribute(R.attr.snackbarIconPadding, snackbarIconPadding, true)
            snackText.compoundDrawablePadding = context.resources.getDimensionPixelOffset(snackbarIconPadding.resourceId)

            val snackbarBgColor = TypedValue()
            context.theme.resolveAttribute(R.attr.snackbarBackground, snackbarBgColor, true)
            view.setBackgroundColor(ContextCompat.getColor(context, snackbarBgColor.resourceId))
        }

        prepare()
        show()
    }

fun Snackbar.action(@StringRes stringId: Int, onClickListener: View.OnClickListener) = setAction(stringId, onClickListener)

fun Snackbar.icon(@DrawableRes drawableId: Int, @ColorRes tintColorId: Int): Snackbar {
    val snackText = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    if (snackText != null) {
        val errorDrawable = context.getVectorDrawable(drawableId)!!
                .tintColor(context.getCompatColor(tintColorId))

        snackText.setCompoundDrawablesWithIntrinsicBounds(errorDrawable, null, null, null)
    }

    return this
}