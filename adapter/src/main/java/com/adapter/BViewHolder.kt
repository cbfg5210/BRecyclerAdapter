package com.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * base ViewHolder
 * @param T : Any
 * @constructor
 */
abstract class BViewHolder<T : Any>(inflater: LayoutInflater, parent: ViewGroup?, attachToRoot: Boolean, @LayoutRes layoutRes: Int) : RecyclerView.ViewHolder(inflater.inflate(layoutRes, parent, attachToRoot)) {

    abstract fun setContents(item: T, selectable: Boolean)

    open fun setListeners(clickListener: View.OnClickListener, longClickListener: View.OnLongClickListener) {
        itemView.setOnClickListener(clickListener)
        itemView.setOnLongClickListener(longClickListener)
    }

    open fun setSelected(isSelected: Boolean) {
        itemView.isSelected = isSelected
    }
}