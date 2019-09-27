package com.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class BRecyclerAdapter<T : Any>(private val context: Context) :
    RecyclerView.Adapter<BViewHolder<Any>>() {
    companion object {
        const val FLAG_PAYLOADS_SELECT = 10101
        const val FLAG_PAYLOADS_DESELECT = 10102
        const val FLAG_UNSELECTABLE = 10103
        const val FLAG_SINGLE_SELECTABLE = 10104
        const val FLAG_MULTI_SELECTABLE = 10105
    }

    private lateinit var vhFactory: BViewHolderFactory

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

    private lateinit var recyclerView: RecyclerView
    private lateinit var diffCallback: DiffUtilCallback
    private var itemTouchHelper: ItemTouchHelper? = null

    constructor(context: Context, viewHolderFactory: BViewHolderFactory) : this(context) {
        this.vhFactory = viewHolderFactory
    }

    /**
     * 设置 BViewHolderFactory
     */
    fun setViewHolderFactory(vhFactory: BViewHolderFactory): BRecyclerAdapter<T> {
        this.vhFactory = vhFactory
        return this
    }

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
     * 使用 DiffUtil 刷新数据
     */
    fun refreshItems(newItems: List<T>) {
        diffCallback.newData = newItems

        AppExecutors.get()
            .diskIO()
            .execute {
                //val startTime = System.currentTimeMillis()
                val diffResult = DiffUtil.calculateDiff(diffCallback)
                //val endTime = System.currentTimeMillis()
                //Log.e("***", "calculateDiff cost time:${endTime - startTime}")

                if (!Utils.isContextValid(context)) {
                    return@execute
                }

                AppExecutors.get()
                    .mainThread()
                    .execute {
                        items.clear()
                        items.addAll(newItems)
                        diffResult.dispatchUpdatesTo(this)
                    }
            }
    }

    /**
     * 添加默认选中项
     */
    fun addSelection(item: T): BRecyclerAdapter<T> {
        require(items.size != 0) { "You should set items before invoking this function!" }
        selections.add(item)
        return this
    }

    /**
     * 添加默认选中项
     */
    fun addSelectionByIndex(itemIndex: Int): BRecyclerAdapter<T> {
        require(items.size != 0) { "You should set items before invoking this function!" }
        selections.add(items[itemIndex])
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
    fun removeAt(index: Int) {
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
     * @param clearSelections 是否清空选中项,true:清空
     */
    fun clear(clearSelections: Boolean = true) {
        items.clear()
        if (clearSelections && selections.size > 0) {
            selections.clear()
        }
    }

    /**
     * 比较内容是否完全一致
     * 内容不一致回调
     */
    fun setBDiffCallback(bDiffCallback: BDiffCallback<T>): BRecyclerAdapter<T> {
        this.diffCallback = DiffUtilCallback(bDiffCallback)
        return this
    }

    /**
     * 允许拖拽
     */
    fun enableDrag(
        classType: Class<out Any>,
        itemTouchCallback: ItemTouchHelper.Callback? = null
    ): BRecyclerAdapter<T> {
        getItemInfo(classType).apply { this.draggable = true }

        if (itemTouchHelper == null) {
            val callback = itemTouchCallback ?: ItemTouchHelperCallback()
            itemTouchHelper = ItemTouchHelper(callback).apply { attachToRecyclerView(recyclerView) }
        }

        return this
    }

    /**
     * 设置拖拽时的背景色
     */
    fun setDragBgColor(classType: Class<out Any>, @ColorInt normalBgColor: Int, @ColorInt dragBgColor: Int): BRecyclerAdapter<T> {
        getItemInfo(classType).apply {
            this.normalBgColor = normalBgColor
            this.dragBgColor = dragBgColor
        }
        return this
    }

    /**
     * 设置拖拽时的背景
     */
    fun setDragBgRes(classType: Class<out Any>, @DrawableRes normalBgRes: Int, @DrawableRes dragBgRes: Int): BRecyclerAdapter<T> {
        getItemInfo(classType).apply {
            this.normalBgRes = normalBgRes
            this.dragBgRes = dragBgRes
        }
        return this
    }

    /**
     * 禁止拖拽
     */
    fun disableDrag(classType: Class<out Any>) {
        getItemInfo(classType).apply { this.draggable = false }
    }

    /**
     * 设置adapter给RecyclerView
     *
     * @param recyclerView
     * @return
     */
    fun bindRecyclerView(recyclerView: RecyclerView): BRecyclerAdapter<T> {
        this.recyclerView = recyclerView
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
    fun setItemInfo(
        classType: Class<out Any>,
        selectable: Boolean,
        multiSelectable: Boolean
    ): BRecyclerAdapter<T> {
        getItemInfo(classType).apply {
            this.selectable = selectable
            this.multiSelectable = multiSelectable
        }
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BViewHolder<Any> {
        val item = items[itemPosition]
        val holder =
            vhFactory.createViewHolder(layoutInflater, parent, item) as BViewHolder<Any>

        holder.setListeners(
            View.OnClickListener { v -> onItemClick(holder, v, itemClickListener) },
            View.OnLongClickListener { v ->
                itemTouchHelper?.run {
                    if (getItemInfo(item.javaClass).draggable) {
                        startDrag(holder)
                    }
                }
                onItemClick(holder, v, itemLongClickListener)
            }
        )

        return holder
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        itemPosition = position
        val type = vhFactory.getItemViewType(items[position])
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
            if (payload == FLAG_PAYLOADS_DESELECT || payload == FLAG_UNSELECTABLE) {
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
     * 查询 Item 可选状态
     */
    fun getItemSelectableState(classType: Class<out Any>): Int {
        val itemInfo = itemInfoMap[classType]

        return if (itemInfo == null || !itemInfo.selectable) {
            FLAG_UNSELECTABLE
        } else if (itemInfo.multiSelectable) {
            FLAG_MULTI_SELECTABLE
        } else {
            FLAG_SINGLE_SELECTABLE
        }
    }

    /**
     * 存储 Item 属性数据
     */
    private data class ItemInfo(
        var viewType: Int = 0,
        var selectable: Boolean = false,
        var multiSelectable: Boolean = false,
        var draggable: Boolean = false,
        @ColorInt var normalBgColor: Int = -1,
        @ColorInt var dragBgColor: Int = -1,
        @DrawableRes var normalBgRes: Int = 0,
        @DrawableRes var dragBgRes: Int = 0
    )

    /**
     * DiffUtil.Callback
     */
    private inner class DiffUtilCallback(private val bDiffCallback: BDiffCallback<T>) :
        DiffUtil.Callback() {
        lateinit var newData: List<T>

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return items[oldItemPosition] == newData[newItemPosition]
        }

        override fun getOldListSize(): Int = items.size

        override fun getNewListSize(): Int = newData.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return bDiffCallback.areContentsTheSame(
                items[oldItemPosition],
                newData[newItemPosition]
            )
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return bDiffCallback.getChangePayload(items[oldItemPosition], newData[newItemPosition])
        }
    }

    /**
     * ItemTouchHelper.Callback
     */
    private inner class ItemTouchHelperCallback : ItemTouchHelper.Callback() {

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            if (!getItemInfo(items[viewHolder.adapterPosition].javaClass).draggable) {
                return makeMovementFlags(
                    0, 0
                )
            }

            val dragFlags = if (recyclerView.layoutManager is GridLayoutManager) {
                ItemTouchHelper.UP or
                        ItemTouchHelper.DOWN or
                        ItemTouchHelper.LEFT or
                        ItemTouchHelper.RIGHT
            } else {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }
            return makeMovementFlags(dragFlags, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            Collections.swap(items, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                val position = viewHolder?.adapterPosition ?: return
                val itemInfo = getItemInfo(items[position].javaClass)

                if (itemInfo.dragBgColor != -1) {
                    viewHolder.itemView.setBackgroundColor(itemInfo.dragBgColor)
                } else if (itemInfo.dragBgRes != 0) {
                    viewHolder.itemView.setBackgroundResource(itemInfo.dragBgRes)
                }
            }
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)

            val position = viewHolder.adapterPosition
            val itemInfo = getItemInfo(items[position].javaClass)

            if (itemInfo.dragBgColor != -1) {
                viewHolder.itemView.setBackgroundColor(itemInfo.normalBgColor)
            } else if (itemInfo.dragBgRes != 0) {
                viewHolder.itemView.setBackgroundResource(itemInfo.normalBgRes)
            }
        }

        override fun isLongPressDragEnabled(): Boolean = true
    }
}