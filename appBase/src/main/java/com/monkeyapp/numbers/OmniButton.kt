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

package com.monkeyapp.numbers

import android.content.Context
import android.graphics.drawable.StateListDrawable
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import com.monkeyapp.numbers.helpers.isOcrAvailable
import com.monkeyapp.numbers.helpers.tintColor

class OmniButton : ImageButton {
    sealed class State {
        object Clean : State() {
            override fun getDrawableStates(): IntArray =
                    arrayListOf(R.attr.state_clean).toIntArray()
        }

        object Camera : State() {
            override fun getDrawableStates(): IntArray =
                    arrayListOf(R.attr.state_camera).toIntArray()
        }

        object None : State() {
            override fun getDrawableStates(): IntArray =
                    emptyArray<Int>().toIntArray()
        }

        abstract fun getDrawableStates(): IntArray
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    var isOcrAvailable = lazy { context.isOcrAvailable() }

    var state: State = State.None
        set(value) {
            when(value) {
                State.Camera -> {
                    field = if (isOcrAvailable.value) {
                        visibility = View.VISIBLE
                        State.Camera
                    } else {
                        // hide button when camera is not available
                        visibility = View.INVISIBLE
                        State.Clean
                    }
                }
                State.Clean -> {
                    visibility = View.VISIBLE
                    field = State.Clean
                }
            }

            refreshDrawableState()
        }

    init {
        val omniButtonDrawable = StateListDrawable()

        val clearDrawable = VectorDrawableCompat.create(resources, R.drawable.ic_clear, context.theme)!!
        omniButtonDrawable.addState(
                arrayListOf(R.attr.state_clean).toIntArray(),
                clearDrawable.tintColor(ContextCompat.getColor(context, R.color.primary_text)))

        val cameraDrawable = VectorDrawableCompat.create(resources, R.drawable.ic_camera, context.theme)!!
        omniButtonDrawable.addState(
                arrayListOf(R.attr.state_camera).toIntArray(),
                cameraDrawable.tintColor(ContextCompat.getColor(context, R.color.primary_text)))

        setImageDrawable(omniButtonDrawable)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray =
        when(state) {
            State.Clean, State.Camera -> {
                val drawableState = super.onCreateDrawableState(extraSpace + 1)
                View.mergeDrawableStates(drawableState, state.getDrawableStates())
            }
            else ->  {
                visibility = View.INVISIBLE
                super.onCreateDrawableState(extraSpace)
            }
        }
}