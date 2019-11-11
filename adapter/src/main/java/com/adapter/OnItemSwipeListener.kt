package com.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/11/11 9:47
 * 功能描述：Item 侧滑回调
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/11/11 9:47
 * 修改内容：
 */
interface OnItemSwipeListener {
    fun onSwipeStart(viewHolder: RecyclerView.ViewHolder, position: Int)
    fun clearView(viewHolder: RecyclerView.ViewHolder, position: Int)
    fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int)
}