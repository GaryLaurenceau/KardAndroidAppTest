package com.kard.test.app.ui.common

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration : RecyclerView.ItemDecoration {
    private var leftFirst: Int = 0
    private var topFirst: Int = 0
    private var rightFirst: Int = 0
    private var bottomFirst: Int = 0
    private var leftLast: Int = 0
    private var topLast: Int = 0
    private var rightLast: Int = 0
    private var bottomLast: Int = 0

    constructor(leftFirst: Int, topFirst: Int, rightFirst: Int, bottomFirst: Int,
                leftLast: Int, topLast: Int, rightLast: Int, bottomLast: Int) {
        this.leftFirst = leftFirst
        this.topFirst = topFirst
        this.rightFirst = rightFirst
        this.bottomFirst = bottomFirst
        this.leftLast = leftLast
        this.topLast = topLast
        this.rightLast = rightLast
        this.bottomLast = bottomLast
    }

    constructor(context: Context, leftFirst: Int, topFirst: Int, rightFirst: Int, bottomFirst: Int,
                leftLast: Int, topLast: Int, rightLast: Int, bottomLast: Int) {
        this.leftFirst = if (leftFirst != -1) getDp(context, leftFirst) else -1
        this.topFirst = if (topFirst != -1) getDp(context, topFirst) else -1
        this.rightFirst = if (rightFirst != -1) getDp(context, rightFirst) else -1
        this.bottomFirst = if (bottomFirst != -1) getDp(context, bottomFirst) else -1
        this.leftLast = if (leftLast != -1) getDp(context, leftLast) else -1
        this.topLast = if (topLast != -1) getDp(context, topLast) else -1
        this.rightLast = if (rightLast != -1) getDp(context, rightLast) else -1
        this.bottomLast = if (bottomLast != -1) getDp(context, bottomLast) else -1
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == 0) {
            if (leftFirst != -1) outRect.left = leftFirst
            if (topFirst != -1) outRect.top = topFirst
            if (rightFirst != -1) outRect.right = rightFirst
            if (bottomFirst != -1) outRect.bottom = bottomFirst
        }
        if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
            if (leftLast != -1) outRect.left = leftLast
            if (topLast != -1) outRect.top = topLast
            if (rightLast != -1) outRect.right = rightLast
            if (bottomLast != -1) outRect.bottom = bottomLast
        }
    }

    private fun getDp(context: Context, size: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, size.toFloat(),
            context.resources.displayMetrics).toInt()
    }

}