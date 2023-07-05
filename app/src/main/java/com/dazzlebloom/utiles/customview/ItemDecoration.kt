package com.dazzlebloom.utiles.customview

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(context: Context, private val displayWidthPx: Int) :
    RecyclerView.ItemDecoration() {
    /*private val horizontalMargin = context.resources.getDimensionPixelSize(R.dimen.span_middle_margin)
    private val maxItemSize =
        context.resources.getDimensionPixelSize(R.dimen.item_max_width)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex

        outRect.run {
            if (parent.getChildViewHolder(view) is MyViewHolder) {
                when (displayWidthPx) {
                    in 1..767 -> Unit
                    else -> {
                        val displayStartEndMargin =
                            (displayWidthPx - (maxItemSize * 2) - (horizontalMargin * 2)) / 2
                        if (spanIndex % 2 == 0) {
                            right = horizontalMargin
                            left = displayStartEndMargin
                        } else {
                            left = horizontalMargin
                            right = displayStartEndMargin
                        }
                    }
                }
            }
        }
    }*/
}