package com.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class BRecyclerAdapter<T : Any>(
        context: Context,
        private val viewHolderFactory: BViewHolderFactory
) : RecyclerView.Adapter<BViewHolder<Any>>() {
    //保存列表项数据信息
    private val viewTypeInfoList = HashMap<Class<Any>, ViewTypeInfo>()
    //布局泵
    private val layoutInflater = LayoutInflater.from(context)

    var items: MutableList<T> = ArrayList()
        private set

    //点击、长按事件
    private var itemClickListener: ((view: View, item: T, position: Int) -> Unit)? = null
    private var itemLongClickListener: ((view: View, item: T, position: Int) -> Unit)? = null

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
     * 设置 item view 点击事件
     */
    fun setItemClickListener(itemClicker: (view: View, item: T, position: Int) -> Unit): BRecyclerAdapter<T> {
        this.itemClickListener = itemClicker
        return this
    }

    /**
     * 设置 item view 长按事件
     */
    fun setItemLongClickListener(itemLongClicker: (view: View, item: T, position: Int) -> Unit): BRecyclerAdapter<T> {
        this.itemLongClickListener = itemLongClicker
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BViewHolder<Any> {
        val item = items[itemPosition]
        val viewTypeInfo = getViewTypeInfo(item.javaClass)
        val holder = viewHolderFactory.createViewHolder(layoutInflater, parent, item)

        /*holder.setListeners(
                View.OnClickListener { onItemClick(it, holder.adapterPosition, viewTypeInfo, true, itemClickListener) },
                View.OnLongClickListener { onItemClick(it, holder.adapterPosition, viewTypeInfo, false, itemLongClickListener) }
        )*/

        holder.setListeners(
                View.OnClickListener { v ->
                    val position = holder.adapterPosition
                    if (viewTypeInfo.clickable && position >= 0 && position < items.size) {
                        itemClickListener?.run { this(v, items[position], position) }
                    }
                },
                View.OnLongClickListener { v ->
                    val position = holder.adapterPosition
                    if (viewTypeInfo.longClickable && position >= 0 && position < items.size) {
                        itemLongClickListener?.run { this(v, items[position], position) }
                    }
                    true
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
        holder.setContents(item, null)
    }

    override fun onBindViewHolder(
            holder: BViewHolder<Any>,
            position: Int,
            payloads: MutableList<Any>
    ) {
        val item = items[position]
        holder.setContents(item, payloads)
    }

    /**
     * 设置view type
     *
     * @param classType
     * @param clickable
     * @param longClickable
     */
    fun setViewType(
            classType: Class<Any>,
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
    private fun getViewTypeInfo(classType: Class<Any>): ViewTypeInfo {
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