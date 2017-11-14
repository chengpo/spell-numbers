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
import android.graphics.drawable.StateListDrawable
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import com.monkeyapp.numbers.helper.tintColor

class OmniButton : ImageButton {
    companion object {
        val STATE_CLEAN = 1
        val STATE_CAMERA = 2
    }

    private val clearStateId = arrayListOf(R.attr.state_clear)
    private val cameraStateId = arrayListOf(R.attr.state_camera)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    var isCameraAvailable = false

    var state : Int = STATE_CAMERA
        set(value) {
            when(value) {
                STATE_CAMERA -> {
                    if (isCameraAvailable) {
                        field = STATE_CAMERA

                    } else {
                        // hide button when camera is not available
                        visibility = View.INVISIBLE
                        field = STATE_CLEAN
                    }
                }
                STATE_CLEAN -> {
                    visibility = View.VISIBLE
                    field = STATE_CLEAN
                }
            }

            refreshDrawableState()
        }

    init {
        val digitTextButtonDrawable = StateListDrawable()

        val clearDrawable = VectorDrawableCompat.create(resources, R.drawable.ic_clear, context.theme)!!
        digitTextButtonDrawable.addState(
                arrayListOf(R.attr.state_clear).toIntArray(),
                clearDrawable.tintColor(ContextCompat.getColor(context, R.color.primary_text)))

        val cameraDrawable = VectorDrawableCompat.create(resources, R.drawable.ic_camera, context.theme)!!
        digitTextButtonDrawable.addState(
                arrayListOf(R.attr.state_camera).toIntArray(),
                cameraDrawable.tintColor(ContextCompat.getColor(context, R.color.primary_text)))

        setImageDrawable(digitTextButtonDrawable)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        return when (state) {
            STATE_CLEAN -> {
                val drawableState = super.onCreateDrawableState(extraSpace + 1)
                View.mergeDrawableStates(drawableState, clearStateId.toIntArray())
            }
            STATE_CAMERA -> {
                val drawableState = super.onCreateDrawableState(extraSpace + 1)
                View.mergeDrawableStates(drawableState, cameraStateId.toIntArray())
            }
            else -> super.onCreateDrawableState(extraSpace)
        }
    }
}