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
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class RippleView : View {
    private val paint: Paint

    private var radius = 1.0f
    private var cx = 1.0f
    private var cy = 1.0f

    private var animatorSet: AnimatorSet? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        visibility = INVISIBLE

        paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = ResourcesCompat.getColor(resources, R.color.ripple_gray, context.theme)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(parentWidth, parentHeight)
    }

    fun startRippleAnimation(x : Float, y : Float) {
        if (animatorSet != null) {
            return
        }

        cx = x
        cy = y

        animatorSet = AnimatorSet()
        animatorSet?.run {
            val initRadius = (Math.min(width, height) / 2.0f)

            interpolator = AccelerateDecelerateInterpolator()

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) = Unit
                override fun onAnimationCancel(animation: Animator?) = Unit

                override fun onAnimationStart(animation: Animator?) {
                    visibility = VISIBLE
                }

                override fun onAnimationEnd(animation: Animator?) {
                    visibility = INVISIBLE
                    animatorSet = null
                }
            })

            playTogether(
                listOf<Animator>(
                    ValueAnimator.ofFloat(initRadius / 2.0f, initRadius * 4.5f).apply {
                        // scale animation
                        repeatCount = 0
                        duration = 500
                        addUpdateListener {
                            radius = it.animatedValue as Float
                            invalidate()
                        }
                    },
                    ObjectAnimator.ofFloat(this@RippleView, "alpha", 0.7f, 0.0f).apply {
                        // alpha animation
                        repeatCount = 0
                        duration = 500
                    }))

            start()
        }
    }

    fun stopRippleAnimation(onAnimationEndAction: () -> Unit) {
        if (animatorSet == null) {
            onAnimationEndAction()
        } else {
            animatorSet?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) = Unit
                override fun onAnimationCancel(animation: Animator?) = Unit
                override fun onAnimationStart(animation: Animator?) = Unit

                override fun onAnimationEnd(animation: Animator?) = onAnimationEndAction()
            })
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(cx, cy, radius, paint)
    }
}