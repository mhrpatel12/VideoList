package com.videolist.extensions

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    //FIND CURRENT SNAP VIEW
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    //LAYOUT MANAGER TO DETERMINE POSITION OF CURRENT SNAP VIEW
    return layoutManager.getPosition(snapView)
}