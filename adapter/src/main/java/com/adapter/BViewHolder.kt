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
abstract class BViewHolder<T : Any>(inflater: LayoutInflater,
                                    parent: ViewGroup?,
                                    @LayoutRes layoutRes: Int,
                                    attachToRoot: Boolean = false) : RecyclerView.ViewHolder(inflater.inflate(layoutRes, parent, attachToRoot)) {

    abstract fun setContents(item: T, isSelected: Boolean, payloads: MutableList<Any>? = null)

    open fun setListeners(clickListener: View.OnClickListener, longClickListener: View.OnLongClickListener) {
        itemView.setOnClickListener(clickListener)
        itemView.setOnLongClickListener(longClickListener)
    }
}