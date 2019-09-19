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
    private val viewTypeMap = HashMap<Class<Any>, Int>()
    //布局泵
    private val layoutInflater = LayoutInflater.from(context)

    var items: MutableList<T> = ArrayList()
        private set

    //点击、长按事件
    private var itemClickListener: ((view: View, item: T, position: Int) -> Unit)? = null
    private var itemLongClickListener: ((view: View, item: T, position: Int) -> Unit)? = null

    private var itemPicker: ItemPicker<T>? = null

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
     * 设置项选择器
     */
    fun setItemPicker(itemPicker: ItemPicker<T>): BRecyclerAdapter<T> {
        this.itemPicker = itemPicker
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
        val holder = viewHolderFactory.createViewHolder(layoutInflater, parent, item) as BViewHolder<Any>

        holder.setListeners(
                View.OnClickListener { v -> onItemClick(holder, v, itemClickListener) },
                View.OnLongClickListener { v -> onItemClick(holder, v, itemLongClickListener) }
        )

        return holder
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        itemPosition = position
        val type = viewHolderFactory.getItemViewType(items[position])
        return if (type != -1) type else getViewType(items[position].javaClass)
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

    private fun onItemClick(
            holder: BViewHolder<Any>,
            v: View,
            clicker: ((view: View, item: T, position: Int) -> Unit)?
    ): Boolean {
        val position = holder.adapterPosition
        if (position >= 0 && position < items.size) {
            val item = items[position]

            //选中/取消选中回调
            itemPicker?.run {
                if (!this.isSelectable(item)) {
                    return@run
                }
                if (this.isSelected(item)) {
                    if (!this.isSingleSelectable(item)) {
                        this.deselect(item)
                        notifyItemChanged(position)
                    }
                } else {
                    if (this.isSingleSelectable(item)) {
                        val currentSelection = this.getCurrentSelection(items)
                        if (currentSelection != null) {
                            this.deselect(currentSelection)
                            val index = items.indexOf(currentSelection)
                            if (index != -1) {
                                notifyItemChanged(index)
                            }
                        }
                    }
                    this.select(item)
                    notifyItemChanged(position)
                }
            }

            //点击/长按回调
            clicker?.run { this(v, item, position) }
        }
        return true
    }

    /**
     * 获取item ViewType
     *
     * @param classType
     * @return
     */
    private fun getViewType(classType: Class<Any>): Int {
        var viewType = viewTypeMap[classType]
        if (viewType == null) {
            viewType = viewTypeMap.size
            viewTypeMap[classType] = viewType
        }
        return viewType
    }
}