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

package com.monkeyapp.numbers.translators

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class RippleView : View {
    interface onAnimationEndListener {
        fun onAnimationEnd()
    }

    private val paint: Paint

    private var radius = 1.0f
    private var cx = 1.0f
    private var cy = 1.0f

    private var animatorSet: AnimatorSet? = null
    private var animatorEndListener: onAnimationEndListener? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        visibility = INVISIBLE

        paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = resources.getColor(android.R.color.darker_gray)
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

        animatorSet = AnimatorSet()
        animatorSet?.interpolator = AccelerateDecelerateInterpolator()

        val animatorList = ArrayList<Animator>()

        cx = x
        cy = y

        val initRadius = (Math.min(width, height) / 2.0f)

        val scaleAnimator = ValueAnimator.ofFloat(initRadius / 2.0f, initRadius * 4.5f)
        scaleAnimator.repeatCount = 0
        scaleAnimator.duration = 800
        scaleAnimator.addUpdateListener {
            radius = it.animatedValue as Float
            invalidate()
        }

        animatorList.add(scaleAnimator)

        val alphaAnimator = ObjectAnimator.ofFloat(this, "Alpha", 0.7f, 0.0f)
        alphaAnimator.repeatCount = 0
        alphaAnimator.duration = 800

        animatorList.add(alphaAnimator)

        animatorSet?.playTogether(animatorList)

        animatorSet?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                visibility = INVISIBLE

                animatorEndListener?.onAnimationEnd()
                animatorEndListener = null

                animatorSet?.removeAllListeners()
                animatorSet = null
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        visibility = VISIBLE
        animatorSet?.start()
    }

    fun stopRippleAnimation(listener: onAnimationEndListener) {
        if (animatorSet != null) {
            animatorEndListener = listener
        } else {
            listener.onAnimationEnd()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(cx, cy, radius, paint)
    }
}