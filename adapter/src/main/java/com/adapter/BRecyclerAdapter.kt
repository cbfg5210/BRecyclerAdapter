package com.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class BRecyclerAdapter<T : Any>(
    context: Context,
    private val viewHolderFactory: BViewHolderFactory
) : RecyclerView.Adapter<BViewHolder<Any>>() {
    //保存列表项数据信息
    private val viewTypeInfoList = HashMap<Class<*>, ViewTypeInfo>()
    //布局泵
    private val layoutInflater = LayoutInflater.from(context)

    var items: MutableList<T> = ArrayList()
        private set

    //点击、长按事件
    private var itemClickListener: OnDClickListener<T>? = null
    private var itemLongClickListener: OnDClickListener<T>? = null

    //记录调用 @see getItemViewType() 时的position
    private var itemPosition: Int = 0

    /**
     * 设置列表数据
     * @param mItems List<Any>?
     * @return BRecyclerAdapter
     */
    fun setItems(mItems: List<T>?): BRecyclerAdapter<T> {
        items.clear()
        mItems?.run { if (this.isNotEmpty()) items.addAll(this) }
        return this
    }

    /**
     * 设置adapter给RecyclerView
     *
     * @param recyclerView
     * @return
     */
    fun bindRecyclerView(recyclerView: RecyclerView): BRecyclerAdapter<T> {
        recyclerView.adapter = this
        return this
    }

    /**
     * 设置item views点击事件
     *
     * @param listener
     */
    fun setItemClickListener(@Nullable listener: OnDClickListener<T>): BRecyclerAdapter<T> {
        this.itemClickListener = listener
        return this
    }

    /**
     * 设置item views长按事件
     *
     * @param listener
     */
    fun setItemLongClickListener(@Nullable listener: OnDClickListener<T>): BRecyclerAdapter<T> {
        this.itemLongClickListener = listener
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BViewHolder<Any> {
        val item = items[itemPosition]

        val holder = viewHolderFactory.createViewHolder(layoutInflater, parent, item)
            ?: throw IllegalArgumentException("Cannot find the view holder for item:$item")

        val viewTypeInfo = getViewTypeInfo(item.javaClass)

        holder.setListeners(
            View.OnClickListener {
                onItemClick(
                    it,
                    holder.adapterPosition,
                    viewTypeInfo,
                    true,
                    itemClickListener
                )
            },
            View.OnLongClickListener {
                onItemClick(
                    it,
                    holder.adapterPosition,
                    viewTypeInfo,
                    false,
                    itemLongClickListener
                )
            }
        )
        return holder
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        itemPosition = position
        val type = viewHolderFactory.getItemViewType(items[position])
        return if (type != -1) type else getViewTypeInfo(items[position].javaClass).viewType
    }

    override fun onBindViewHolder(holder: BViewHolder<Any>, position: Int) {
        val item = items[position]
        holder.setContents(item, false)
    }

    override fun onBindViewHolder(
        holder: BViewHolder<Any>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (!viewHolderFactory.onBindViewHolder(holder, position, payloads)) {
            onBindViewHolder(holder, position)
        }
    }

    /**
     * 点击/长按处理
     *
     * @param v
     * @param position
     * @param viewTypeInfo
     * @param isClick 是否是点击：true-单击，false-长按
     * @param listener
     * @return
     */
    private fun onItemClick(
        v: View,
        position: Int,
        viewTypeInfo: ViewTypeInfo,
        isClick: Boolean,
        listener: OnDClickListener<T>?
    ): Boolean {
        if (position < 0 || position >= items.size) {
            return false
        }
        if (isClick) {
            if (viewTypeInfo.clickable) listener?.onClick(v, items[position], position)
        } else {
            if (viewTypeInfo.longClickable) listener?.onClick(v, items[position], position)
        }
        return true
    }

    /**
     * 设置view type
     *
     * @param classType
     * @param clickable
     * @param longClickable
     */
    fun setViewType(
        classType: Class<*>,
        clickable: Boolean = true,
        longClickable: Boolean = true
    ): BRecyclerAdapter<T> {
        getViewTypeInfo(classType).apply {
            this.clickable = clickable
            this.longClickable = longClickable
        }
        return this
    }

    /**
     * 获取item ViewTypeInfo
     *
     * @param classType
     * @return
     */
    private fun getViewTypeInfo(classType: Class<*>): ViewTypeInfo {
        return viewTypeInfoList[classType] ?: ViewTypeInfo().apply {
            //没有找到对应ViewTypeInfo的话则添加默认ViewTypeInfo
            viewType = viewTypeInfoList.size
            clickable = true
            longClickable = true
            viewTypeInfoList[classType] = this
        }
    }

    /**
     * 存储ViewType数据
     */
    private inner class ViewTypeInfo {
        internal var viewType: Int = 0
        internal var clickable: Boolean = false
        internal var longClickable: Boolean = false
    }
}