/*
MIT License

Copyright (c) 2017 - 2022 Po Cheng

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
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.gridlayout.widget.GridLayout
import com.monkeyapp.numbers.apphelpers.isPortraitMode

@Suppress("unused")
class NumberWordViewBehavior(context: Context?, attrs: AttributeSet?):
        CoordinatorLayout.Behavior<ViewGroup>(context, attrs) {

    override fun onMeasureChild(parent: CoordinatorLayout,
                                child: ViewGroup,
                                parentWidthMeasureSpec: Int,
                                widthUsed: Int,
                                parentHeightMeasureSpec: Int,
                                heightUsed: Int): Boolean {

        val extraHeight: Int = parent.children
              .filter { it !is ConstraintLayout && it !is GridLayout }
              .map { it.measuredHeight }
              .sum()

        if (parent.isPortraitMode) {
            val height = if (child is ConstraintLayout) {
                View.MeasureSpec.getSize(parentHeightMeasureSpec) / 2 - extraHeight
            } else {
                View.MeasureSpec.getSize(parentHeightMeasureSpec) / 2
            }

            parent.onMeasureChild(child,
                    parentWidthMeasureSpec, widthUsed,
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY), heightUsed)
        } else {
            val height = View.MeasureSpec.getSize(parentHeightMeasureSpec) - extraHeight
            val width = View.MeasureSpec.getSize(parentWidthMeasureSpec) / 2

            parent.onMeasureChild(child,
                    View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), widthUsed,
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY), heightUsed)
        }

        return true
    }

    override fun onLayoutChild(parent: CoordinatorLayout,
                               child: ViewGroup,
                               layoutDirection: Int): Boolean {

        if (child is GridLayout) {
            val lp = child.layoutParams
            if (lp is CoordinatorLayout.LayoutParams) {
                lp.dodgeInsetEdges = Gravity.BOTTOM

                lp.gravity = if (parent.isPortraitMode) {
                    Gravity.BOTTOM
                } else {
                    Gravity.END
                }
            }

            parent.onLayoutChild(child, layoutDirection)
            return true
        }

        if (child is ConstraintLayout) {
            val lp = child.layoutParams

            if (lp is CoordinatorLayout.LayoutParams) {
                lp.dodgeInsetEdges = Gravity.BOTTOM

                lp.gravity = if (parent.isPortraitMode) {
                    Gravity.TOP
                } else {
                    Gravity.START
                }
            }

            parent.onLayoutChild(child, layoutDirection)
            return true
        }

        return super.onLayoutChild(parent, child, layoutDirection)
    }
}
