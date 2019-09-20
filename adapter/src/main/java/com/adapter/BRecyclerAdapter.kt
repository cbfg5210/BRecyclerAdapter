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
    companion object {
        const val FLAG_PAYLOADS_SELECT = 10101
        const val FLAG_PAYLOADS_DESELECT = 10102
    }

    //保存列表项信息
    private val itemInfoMap = HashMap<Class<out Any>, ItemInfo>()
    private val layoutInflater = LayoutInflater.from(context)

    var items: MutableList<T> = ArrayList()
        private set
    //选中项列表
    val selections = ArrayList<T>()

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
        mItems ?: return this

        if (mItems.isNotEmpty()) {
            items.addAll(mItems)
            //如果有选中项,删除无效的选中项
            if (selections.size > 0) {
                val iterator = selections.iterator()
                while (iterator.hasNext()) {
                    val item = iterator.next()
                    if (!items.contains(item)) {
                        selections.remove(item)
                    }
                }
            }
        }

        return this
    }

    /**
     * 添加默认选中项
     */
    fun addSelection(item: T): BRecyclerAdapter<T> {
        selections.add(item)
        return this
    }

    /**
     * 选中全部
     */
    fun selectAll() {
        if (items.size > 0) {
            selections.addAll(items)
            notifyItemRangeChanged(0, items.size, FLAG_PAYLOADS_SELECT)
        }
    }

    /**
     * 全部取消选中
     */
    fun deselectAll() {
        if (selections.size > 0) {
            selections.clear()
            notifyItemRangeChanged(0, items.size, FLAG_PAYLOADS_DESELECT)
        }
    }

    /**
     * 移除单个数据项
     */
    fun remove(index: Int) {
        remove(items[index])
    }

    /**
     * 移除单个数据项
     */
    fun remove(item: T) {
        items.remove(item)
        selections.remove(item)
    }

    /**
     * 移除指定数据类型数据
     */
    fun remove(itemType: Class<Any>) {
        if (items.size == 0) {
            return
        }
        //逆序查询删除
        for (i in items.size - 1 downTo 0) {
            val item = items[i]
            if (item.javaClass == itemType) {
                selections.remove(item)
                items.removeAt(i)
                notifyItemRemoved(i)
            }
        }
    }

    /**
     * 移除选中项
     */
    fun removeSelections() {
        if (selections.size > 0) {
            selections.forEach { items.remove(it) }
            selections.clear()
        }
    }

    /**
     * 清空数据
     */
    fun clear() {
        items.clear()
        if (selections.size > 0) {
            selections.clear()
        }
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

    /**
     * 设置 Item 属性信息
     */
    fun setItemInfo(classType: Class<out Any>, selectable: Boolean, multiSelectable: Boolean): BRecyclerAdapter<T> {
        getItemInfo(classType).apply {
            this.selectable = selectable
            this.multiSelectable = multiSelectable
        }
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
        return if (type != -1) type else getItemInfo(items[position].javaClass).viewType
    }

    override fun onBindViewHolder(holder: BViewHolder<Any>, position: Int) {
        val item = items[position]
        val itemInfo = getItemInfo(item.javaClass)
        holder.setContents(item, itemInfo.selectable && selections.contains(item))
    }

    override fun onBindViewHolder(
            holder: BViewHolder<Any>,
            position: Int,
            payloads: MutableList<Any>
    ) {
        val item = items[position]
        val itemInfo = getItemInfo(item.javaClass)

        if (payloads.size == 0) {
            holder.setContents(item, itemInfo.selectable && selections.contains(item))
            return
        }

        //payloads 的 size 总是1
        val payload = payloads[0]
        if (payload is Int) {
            if (payload == FLAG_PAYLOADS_SELECT) {
                holder.setContents(item, true, payload)
                return
            }
            if (payload == FLAG_PAYLOADS_DESELECT) {
                holder.setContents(item, false, payload)
                return
            }
        }
        holder.setContents(item, itemInfo.selectable && selections.contains(item), payload)
    }

    private fun onItemClick(
            holder: BViewHolder<Any>,
            v: View,
            clicker: ((view: View, item: T, position: Int) -> Unit)?
    ): Boolean {
        val position = holder.adapterPosition

        if (position < 0 || position >= items.size) {
            return false
        }

        val item = items[position]
        val itemInfo = getItemInfo(item.javaClass)

        //选中/取消选中回调
        //可以选中
        if (itemInfo.selectable) {
            //多选
            if (itemInfo.multiSelectable) {
                //如果已经选中过该项则取消选中
                if (selections.contains(item)) {
                    selections.remove(item)
                    notifyItemChanged(position, FLAG_PAYLOADS_DESELECT)
                } else {
                    selections.add(item)
                    notifyItemChanged(position, FLAG_PAYLOADS_SELECT)
                }
            } else if (!selections.contains(item)) {
                //单选
                //还没选中过该项
                //判断是否已经选中同类型的项
                if (selections.size > 0) {
                    val min = selections.size.coerceAtMost(itemInfoMap.size)
                    for (i in 0..min) {
                        //如果已经选中同类型的项,先移除之前选中的
                        val selection = selections[i]
                        if (selection.javaClass == item.javaClass) {
                            selections.removeAt(i)
                            val index = items.indexOf(selection)
                            if (index != -1) {
                                notifyItemChanged(index, FLAG_PAYLOADS_DESELECT)
                            }
                            break
                        }
                    }
                }
                //将该项放置到最开始位置,方便检索是否已经选中同类型的项
                selections.add(0, item)
                notifyItemChanged(position, FLAG_PAYLOADS_SELECT)
            }
        }

        //点击/长按回调
        clicker?.run { this(v, item, position) }

        return true
    }

    /**
     * 获取item ViewType
     *
     * @param classType
     * @return
     */
    private fun getItemInfo(classType: Class<out Any>): ItemInfo {
        return itemInfoMap[classType]
                ?: ItemInfo(itemInfoMap.size).apply { itemInfoMap[classType] = this }
    }

    /**
     * 存储 Item 属性数据
     */
    private data class ItemInfo(var viewType: Int = 0,
                                var selectable: Boolean = false,
                                var multiSelectable: Boolean = false)
}