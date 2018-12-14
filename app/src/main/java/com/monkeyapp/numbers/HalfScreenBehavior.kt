package com.monkeyapp.numbers

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.gridlayout.widget.GridLayout
import com.monkeyapp.numbers.apphelpers.isPortraitMode

class HalfScreenBehavior(context: Context?, attrs: AttributeSet?): CoordinatorLayout.Behavior<ViewGroup>(context, attrs) {
    override fun onMeasureChild(parent: CoordinatorLayout, child: ViewGroup, parentWidthMeasureSpec: Int, widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int): Boolean {
        if (parent.isPortraitMode) {
           val height = View.MeasureSpec.getSize(parentHeightMeasureSpec) / 2
           parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY), heightUsed)
        } else {
           val width = View.MeasureSpec.getSize(parentWidthMeasureSpec) / 2
            parent.onMeasureChild(child, View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), widthUsed, parentHeightMeasureSpec, heightUsed)
        }

        return true
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: ViewGroup, dependency: View): Boolean {
        return child is GridLayout && dependency is RelativeLayout
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: ViewGroup, layoutDirection: Int): Boolean {
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

        return super.onLayoutChild(parent, child, layoutDirection)
    }
}