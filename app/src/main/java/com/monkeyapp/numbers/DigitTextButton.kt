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

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton

class DigitTextButton : ImageButton {
    private val STATE_CLEAN_ID = arrayListOf(R.attr.state_clear)
    private val STATE_CAMERA_ID = arrayListOf(R.attr.state_camera)

    companion object {
        val STATE_CLEAN = 1
        val STATE_CAMERA = 2
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    var state : Int = STATE_CAMERA
        set(value) {
            field = value
            refreshDrawableState()
        }

    init {
        val digitTextButtonDrawable = StateListDrawable()

        val clearDrawable = VectorDrawableCompat.create(resources, R.drawable.ic_clear, context.theme) as Drawable
        digitTextButtonDrawable.addState(arrayListOf(R.attr.state_clear).toIntArray(),
                clearDrawable.tintColor(R.color.primary_text))

        val cameraDrawable = VectorDrawableCompat.create(resources, R.drawable.ic_camera, context.theme) as Drawable
        digitTextButtonDrawable.addState(arrayListOf(R.attr.state_camera).toIntArray(),
                cameraDrawable.tintColor(R.color.primary_text))

        setImageDrawable(digitTextButtonDrawable)
    }

    private fun Drawable.tintColor(tintColorId: Int): Drawable {
        val tintColor = ContextCompat.getColor(context, tintColorId)

        var drawable = mutate()
        drawable = DrawableCompat.wrap(drawable)

        DrawableCompat.setTint(drawable, tintColor)
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)

        return drawable
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        when (state) {
            STATE_CLEAN -> {
                val drawableState = super.onCreateDrawableState(extraSpace + 1)
                View.mergeDrawableStates(drawableState, STATE_CLEAN_ID.toIntArray())
                return drawableState
            }

            STATE_CAMERA -> {
                val drawableState = super.onCreateDrawableState(extraSpace + 1)
                View.mergeDrawableStates(drawableState, STATE_CAMERA_ID.toIntArray())
                return drawableState
            }

            else -> return super.onCreateDrawableState(extraSpace)
        }
    }
}