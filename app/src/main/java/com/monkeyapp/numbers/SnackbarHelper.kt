/*
MIT License

Copyright (c) 2017 Po Cheng

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

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.design.widget.Snackbar
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.widget.TextView

fun Snackbar.setIcon(drawbleId: Int, tintColorId: Int): Snackbar {
    var errorIcon = VectorDrawableCompat.create(context.resources, drawbleId, context.theme) as Drawable
    val errorColor = ContextCompat.getColor(context, tintColorId)

    errorIcon = errorIcon.mutate()
    errorIcon = DrawableCompat.wrap(errorIcon)

    DrawableCompat.setTint(errorIcon, errorColor)
    DrawableCompat.setTintMode(errorIcon, PorterDuff.Mode.SRC_IN)

    val snackText : TextView = view.findViewById(android.support.design.R.id.snackbar_text)

    view.setBackgroundColor(ContextCompat.getColor(context, R.color.primary_dark))
    snackText.setCompoundDrawablesWithIntrinsicBounds(errorIcon, null, null, null)
    snackText.compoundDrawablePadding = context.resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)

    return this
}