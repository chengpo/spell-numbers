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


package com.monkeyapp.numbers.helpers

import com.google.android.material.snackbar.Snackbar
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.monkeyapp.numbers.R

inline fun View.snackbar(stringId: Int, length: Int = Snackbar.LENGTH_SHORT, prepare: Snackbar.() -> Snackbar) =
    Snackbar.make(this, stringId, length).apply {
        prepare()
        show()
    }

fun Snackbar.action(stringId: Int, onClickListener: View.OnClickListener) = setAction(stringId, onClickListener)

inline fun Snackbar.dismissCallback(crossinline callback: () -> Unit) =
    addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            callback()
        }
    })

fun Snackbar.icon(drawbleId: Int, tintColorId: Int): Snackbar {
    val snackText = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)

    val errorDrawable = VectorDrawableCompat
            .create(context.resources, drawbleId, context.theme)!!
            .tintColor(ContextCompat.getColor(context, tintColorId))

    snackText.setCompoundDrawablesWithIntrinsicBounds(errorDrawable, null, null, null)
    snackText.compoundDrawablePadding = context.resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)

    view.setBackgroundColor(ContextCompat.getColor(context, R.color.primary_dark))
    return this
}