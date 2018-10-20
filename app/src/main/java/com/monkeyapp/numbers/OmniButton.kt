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

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageButton
import com.monkeyapp.numbers.apphelpers.getCompatColor
import com.monkeyapp.numbers.apphelpers.getVectorDrawable
import com.monkeyapp.numbers.apphelpers.isOcrAvailable
import com.monkeyapp.numbers.apphelpers.tintColor

class OmniButton : AppCompatImageButton {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var isOcrAvailable = lazy { context.isOcrAvailable }

    var state: State = State.None
        set(value) {
            val old = field

            field = when {
                value == State.Camera && isOcrAvailable.value -> {
                    State.Camera
                }
                value == State.Camera && !isOcrAvailable.value -> {
                    // hide button when camera is not available
                    State.None
                }
                value == State.Clean -> {
                    State.Clean
                }
                else -> {
                    State.None
                }
            }

            if (old != field) {
                if (field == State.None) {
                    fadeOut {
                        visibility = View.INVISIBLE
                        refreshDrawableState()
                    }
                } else {
                    fadeOutFadeIn {
                        visibility = View.VISIBLE
                        refreshDrawableState()
                    }
                }
            }
        }

    init {
        setImageDrawable(OmniDrawable(context))
        state = State.Camera
    }

    private fun fadeOutFadeIn(onStart: () -> Unit) {
        val fadeOutAnimator = ObjectAnimator.ofFloat(this, View.ALPHA, 1.0F, 0.0F)
        val fadeInAnimator = ObjectAnimator.ofFloat(this, View.ALPHA, 0.0F, 1.0F)
        fadeInAnimator.addListener(object: Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) = Unit
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationStart(animation: Animator?) = onStart()
        })

        val animatorSet = AnimatorSet()
        animatorSet.duration = 300L
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.playSequentially(fadeOutAnimator, fadeInAnimator)
        animatorSet.start()
    }

    private fun fadeOut(onEnd: () -> Unit) {
        val animator = ObjectAnimator.ofFloat(this, View.ALPHA, 1.0F, 0.0F)
        animator.addListener(object: Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) = onEnd()
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationStart(animation: Animator?) = Unit
        })
        animator.duration = 300L
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        return when (state) {
            State.Clean, State.Camera -> {
                val drawableState = super.onCreateDrawableState(extraSpace + 1)
                View.mergeDrawableStates(drawableState, state.getDrawableStates().toIntArray())
            }
            else -> {
                visibility = View.INVISIBLE
                super.onCreateDrawableState(extraSpace)
            }
        }
    }

    sealed class State {
        object Clean : State() {
            override fun getDrawableStates() = listOf(R.attr.state_clean)
        }

        object Camera : State() {
            override fun getDrawableStates() = listOf(R.attr.state_camera)
        }

        object None : State() {
            override fun getDrawableStates() = emptyList<Int>()
        }

        abstract fun getDrawableStates(): List<Int>
    }

    private class OmniDrawable(context: Context): StateListDrawable() {
        init {
            fun stateOf(@AttrRes attrId: Int) = arrayOf(attrId).toIntArray()
            fun drawableOf(@DrawableRes drawableId: Int) = context.getVectorDrawable(drawableId)

            listOf(R.attr.state_clean to R.drawable.ic_clean,
                    R.attr.state_camera to R.drawable.ic_camera)
                    .map { (attrId, drawableId) ->
                        stateOf(attrId) to drawableOf(drawableId)
                    }
                    .forEach { (state, drawable) ->
                        addState(state, drawable)
                    }

            tintColor(context.getCompatColor(R.color.primary_text))
        }
    }
}

