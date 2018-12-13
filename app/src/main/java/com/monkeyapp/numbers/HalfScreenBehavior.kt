package com.monkeyapp.numbers

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout

class HalfScreenBehavior: CoordinatorLayout.Behavior<ViewGroup> {
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasureChild(parent: CoordinatorLayout, child: ViewGroup, parentWidthMeasureSpec: Int, widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int): Boolean {
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed)
    }
}