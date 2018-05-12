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

import android.support.design.widget.Snackbar
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.monkeyapp.numbers.R
import org.jetbrains.anko.backgroundColor


inline fun View.snackbar(stringId: Int, length: Int = Snackbar.LENGTH_SHORT, action: Snackbar.() -> Snackbar) {
    val snackbar = Snackbar.make(this, stringId, length)
    action(snackbar)
    snackbar.show()
}

inline fun Snackbar.action(stringId: Int, crossinline callback: () -> Unit) = setAction(stringId, { callback() })

inline fun Snackbar.dismissCallback(crossinline callback: () -> Unit) =
    addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            callback()
        }
    })

fun Snackbar.icon(drawbleId: Int, tintColorId: Int): Snackbar {
    val snackText = view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
    with(snackText) {
        val errorDrawable = VectorDrawableCompat
                .create(context.resources, drawbleId, context.theme)!!
                .tintColor(ContextCompat.getColor(context, tintColorId))

        setCompoundDrawablesWithIntrinsicBounds(errorDrawable, null, null, null)
        compoundDrawablePadding = context.resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
    }

    view.backgroundColor = ContextCompat.getColor(context, R.color.primary_dark)
    return this
}