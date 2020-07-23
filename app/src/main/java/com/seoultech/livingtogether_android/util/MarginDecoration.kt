package com.seoultech.livingtogether_android.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginDecoration private constructor(private var context: Context) :
    RecyclerView.ItemDecoration() {

    private val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    private var margin: Int? = null
    private var orientation: Int? = null
    private var numOfColumns: Int? = null
    private var marginHorizontal: Int? = null
    private var marginVertical: Int? = null

    constructor(context: Context, margin: Int, orientation: Int) : this(context) {
        this.orientation = orientation
        this.margin = margin
    }

    constructor(context: Context, numOfColumns: Int, marginHorizontal: Int, marginVertical: Int) : this(context) {
        this.numOfColumns = numOfColumns
        this.marginHorizontal = marginHorizontal
        this.marginVertical = marginVertical
    }

    init {
        orientation?.let {
            margin = margin!!.px
        }

        numOfColumns?.let {
            marginHorizontal = marginHorizontal!!.px
            marginVertical = marginVertical!!.px
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        orientation?.let {
            if (position != itemCount - 1) {
                when (orientation) {
                    RecyclerView.HORIZONTAL -> outRect.right = margin!!
                    RecyclerView.VERTICAL -> outRect.bottom = margin!!
                }
            }
        }

        numOfColumns?.let {
            if (position + 1 < numOfColumns!! || (position + 1) % numOfColumns!! != 0) {
                outRect.right = marginHorizontal!! / 2
            } else {
                outRect.left = marginHorizontal!! / 2 + marginHorizontal!! % 2
            }

            if (position < numOfColumns!! || position < itemCount - (numOfColumns!! - itemCount % numOfColumns!!)) {
                outRect.bottom = marginVertical!!
            }
        }
    }
}
