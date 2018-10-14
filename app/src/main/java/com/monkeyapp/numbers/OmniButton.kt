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
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageButton
import com.monkeyapp.numbers.helpers.getCompatColor
import com.monkeyapp.numbers.helpers.getVectorDrawable
import com.monkeyapp.numbers.helpers.isOcrAvailable
import com.monkeyapp.numbers.helpers.tintColor

class OmniButton : AppCompatImageButton {
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

    private var isOcrAvailable = lazy { context.isOcrAvailable }

    var state: State = State.None
        set(value) {
            field = when {
                value == State.Camera && isOcrAvailable.value -> {
                    visibility = View.VISIBLE
                    State.Camera
                }
                value == State.Camera && !isOcrAvailable.value -> {
                    // hide button when camera is not available
                    visibility = View.INVISIBLE
                    State.None
                }
                value == State.Clean -> {
                    visibility = View.VISIBLE
                    State.Clean
                }
                else -> State.None
            }

            refreshDrawableState()
        }

    init {
        fun stateOf(@AttrRes attrId: Int) = arrayOf(attrId).toIntArray()
        fun drawableOf(@DrawableRes drawableId: Int) = context.getVectorDrawable(drawableId)!!
                .tintColor(context.getCompatColor(R.color.primary_text))

        val omniButtonDrawable = StateListDrawable()

        listOf(R.attr.state_clean to R.drawable.ic_clean,
               R.attr.state_camera to R.drawable.ic_camera)
                .map { (attrId, drawableId) ->
                    stateOf(attrId) to drawableOf(drawableId)
                }
                .forEach { (state, drawable) ->
                    omniButtonDrawable.addState(state, drawable)
                }

        setImageDrawable(omniButtonDrawable)

        state = State.Camera
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray =
            when (state) {
                State.Clean, State.Camera -> {
                    val drawableState = super.onCreateDrawableState(extraSpace + 1)
                    View.mergeDrawableStates(drawableState, state.getDrawableStates())
                }
                else -> {
                    visibility = View.INVISIBLE
                    super.onCreateDrawableState(extraSpace)
                }
            }
}