package com.seoultech.livingtogether_android.ui.main.decoration

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginDecoration(private val context: Context, var margin: Int, private val orientation: Int) : RecyclerView.ItemDecoration() {

    init {
        margin = dpToPx(margin)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (position != itemCount - 1)
            when(orientation) {
                RecyclerView.HORIZONTAL -> outRect.right = margin
                RecyclerView.VERTICAL -> outRect.bottom = margin
            }
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}