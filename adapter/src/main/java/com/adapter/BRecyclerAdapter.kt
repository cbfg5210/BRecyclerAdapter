package com.adapter

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adapter.bean.BHeadFootBean
import com.adapter.bean.BHeadFootType
import com.adapter.bean.BStatusBean
import com.adapter.bean.BStatusType
import java.util.*
import kotlin.collections.ArrayList

class BRecyclerAdapter<T : Any>(private val context: Context, val viewHolderFactory: BViewHolderFactory) : RecyclerView.Adapter<BViewHolder<Any>>() {
    //保存列表项数据信息
    private val viewTypeInfoList = HashMap<Class<*>, ViewTypeInfo>()
    //布局泵
    private val layoutInflater = LayoutInflater.from(context)

    private var items: MutableList<T> = ArrayList()
    //选中项列表
    val selections = ArrayList<Any>()
    private var lastSelection: Any? = null
    private var currentSelection: Any? = null
    //是否可以多选
    private var multiSelectable = false
    //点击、长按事件
    private var itemClickListener: OnDClickListener<T>? = null
    private var itemLongClickListener: OnDClickListener<T>? = null

    //计算列表项差异
    private lateinit var diffCallback: DiffUtilCallback
    //记录调用 @see getItemViewType() 时的position
    private var itemPosition: Int = 0

    private var finalVHFactory = viewHolderFactory

    /**
     * 设置列表项差异计算callback
     * @param diffItemCallback DiffUtil.ItemCallback<Any>
     */
    fun setDiffItemCallback(diffItemCallback: DiffUtil.ItemCallback<T>): BRecyclerAdapter<T> {
        diffCallback = DiffUtilCallback(diffItemCallback)
        return this
    }

    /**
     * 返回列表数据
     *
     * @return
     */
    fun getItems(): MutableList<T> {
        return items
    }

    /**
     * 设置列表数据
     * @param items List<Any>?
     * @return BRecyclerAdapter
     */
    fun setItems(items: List<T>?): BRecyclerAdapter<T> {
        this.items.clear()
        if (items != null && items.isNotEmpty()) {
            this.items.addAll(items)
        }
        return this
    }

    /**
     * 返回列表数据，返回的是一个与原列表数据相同但是引用不同的列表
     *
     * @return
     */
    fun getNewItems(): MutableList<T>? {
        return ArrayList(items)
    }

    /**
     * 设置是否可以多选
     *
     * @param multiSelectable
     */
    fun toggleMultiSelectable(multiSelectable: Boolean): BRecyclerAdapter<T> {
        this.multiSelectable = multiSelectable
        return this
    }

    /**
     * 查询指定的item类型是否可以选中
     * @param clazzArray Array<out Class<Any>>
     * @return Boolean
     */
    fun isAllSelectable(vararg clazzArray: Class<*>): Boolean {
        for (i in clazzArray.indices) {
            if (!getViewTypeInfo(clazzArray[i]).selectable) {
                return false
            }
        }
        return true
    }

    /**
     * 设置是否可以选中
     *
     * @param selectable
     * @param clearSelected 是否移除该viewType选中的项
     * @param clazzArray
     */
    fun toggleSelectable(selectable: Boolean, clearSelected: Boolean, vararg clazzArray: Class<*>): BRecyclerAdapter<T> {
        var clazz: Class<*>

        for (i in clazzArray.indices) {
            clazz = clazzArray[i]

            getViewTypeInfo(clazz).selectable = selectable

            if (clearSelected) {
                //移除之前选中的项
                for (j in selections.size - 1 downTo 0) {
                    if (selections[j].javaClass === clazz) {
                        selections.removeAt(j)
                    }
                }
            }
        }
        return this
    }

    /**
     * 添加选中项
     *
     * @param position
     */
    fun addSelection(position: Int): BRecyclerAdapter<T> {
        if (position < 0 || position >= itemCount) {
            return this
        }
        val toBeSelectedItem = items[position]
        if (multiSelectable) {
            if (selections.contains(toBeSelectedItem)) selections.remove(toBeSelectedItem)
            else selections.add(toBeSelectedItem)

            lastSelection = currentSelection
            currentSelection = toBeSelectedItem

        } else if (currentSelection !== toBeSelectedItem) {
            lastSelection = currentSelection
            currentSelection = toBeSelectedItem
            //selections.remove(null)不会报错，所以不用判断lastSelection是否为空
            selections.remove(lastSelection)

            currentSelection?.run { if (!selections.contains(this)) selections.add(this) }
        }
        lastSelection?.run { notifyItemChanged(items.indexOf(this), 1) }
        notifyItemChanged(position, 1)

        return this
    }

    /**
     * 移除列表项
     *
     * @param position
     * @param notifyRefresh 是否通知刷新
     */
    fun remove(position: Int, notifyRefresh: Boolean) {
        if (position < 0 || position >= itemCount) {
            return
        }
        val item = items[position]
        selections.remove(item)
        items.remove(item)

        if (lastSelection === item) {
            lastSelection = null
        }
        if (currentSelection === item) {
            currentSelection = null
        }
        if (notifyRefresh) {
            notifyItemRemoved(position)
        }
    }

    /**
     * 移除部分项
     *
     * @param toBeRemovedItems
     * @param notifyRefresh
     */
    fun remove(toBeRemovedItems: List<Any>?, notifyRefresh: Boolean) {
        if (toBeRemovedItems == null || toBeRemovedItems.isEmpty()) {
            return
        }
        var item: Any
        for (i in toBeRemovedItems.indices.reversed()) {
            item = toBeRemovedItems[i]

            selections.remove(item)
            items.remove(item)

            if (lastSelection === item) {
                lastSelection = null
            }
            if (currentSelection === item) {
                currentSelection = null
            }
        }
        if (notifyRefresh) {
            notifyDataSetChanged()
        }
    }

    /**
     * 扩展viewHolderFactory
     * @param exts Array<out BViewHolderFactory>
     */
    fun extend(vararg exts: BViewHolderFactory): BRecyclerAdapter<T> {
        finalVHFactory = object : BViewHolderFactory() {
            override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, item: Any): BViewHolder<Any>? {
                var bViewHolder = viewHolderFactory.createViewHolder(inflater, parent, item)
                if (bViewHolder == null && exts.isNotEmpty()) {
                    for (i in exts.indices) {
                        bViewHolder = exts[i].createViewHolder(inflater, parent, item)
                        if (bViewHolder != null) break
                    }
                }
                return bViewHolder
            }
        }
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

    /**
     * 通过DiffUtil通知更新列表
     *
     * @param newData
     */
    fun notifyDiffDataSetChanged(newData: List<T>?) {
        if (items === newData) {
            //新旧数据的引用一样，说明数据是一样的，可能是在更新数据的时候调用的是getItems()，而不是getNewItems()
            setItems(newData)
            notifyDataSetChanged()
            return
        }

        //判断是否要从选中列表中移除
        if (selections.size > 0) {
            if (newData == null || newData.isEmpty()) {
                selections.clear()
            } else {
                var item: Any
                for (i in selections.size - 1 downTo 0) {
                    item = selections[i]
                    if (newData.contains(item)) {
                        continue
                    }
                    selections.removeAt(i)
                    if (lastSelection === item) {
                        lastSelection = null
                    }
                    if (currentSelection === item) {
                        currentSelection = null
                    }
                }
            }
        }

        Thread {
            //计算
            diffCallback.setNewData(newData)
            val result = DiffUtil.calculateDiff(diffCallback, true)
            //刷新列表
            getActivity(context)?.run {
                if (!isFinishing) {
                    runOnUiThread {
                        setItems(newData)
                        result.dispatchUpdatesTo(this@BRecyclerAdapter)
                    }
                }
            }
        }.start()
    }

    /**
     * 通过context获取activity
     */
    private fun getActivity(context: Context?): Activity? {
        context ?: return null

        var ctx = context
        while (ctx is ContextWrapper) {
            if (ctx is Activity) {
                return ctx
            }
            ctx = ctx.baseContext
        }

        return null
    }

    /**
     * 显示状态页面
     * 使用的时候要注意：显示状态页要注意BRecyclerAdapter的数据类型要是Object而不是具体的数据类
     * @param statusBean
     */
    fun showStatusLayout(statusBean: T) {
        items.clear()
        items.add(statusBean)
    }

    /**
     * 隐藏状态页面
     */
    fun hideStatusLayout(@BStatusType statusType: Int = BStatusType.ALL) {
        if (statusType == BStatusType.ALL) {
            for (i in items.indices.reversed()) {
                if (items[i] is BStatusBean) {
                    items.removeAt(i)
                }
            }
        } else {
            for (i in items.indices.reversed()) {
                if (items[i] is BStatusBean && (items[i] as BStatusBean).type == statusType) {
                    items.removeAt(i)
                }
            }
        }
    }

    /**
     * 显示header
     * @param headerBean T
     * @return BRecyclerAdapter<T>
     */
    fun addHeader(headerBean: T): BRecyclerAdapter<T> {
        if (itemCount > 0 && (items[0] is BHeadFootBean)) {
            items.removeAt(0)
        }
        items.add(0, headerBean)
        return this
    }

    /**
     * 显示footer
     * @param footerBean T
     * @return BRecyclerAdapter<T>
     */
    fun addFooter(footerBean: T): BRecyclerAdapter<T> {
        if (itemCount > 0 && (items[itemCount - 1] is BHeadFootBean)) {
            items.removeAt(itemCount - 1)
        }
        items.add(itemCount, footerBean)
        return this
    }

    /**
     * 隐藏header/footer
     * @param headFootType Int
     */
    fun hideHeaderFooter(@BHeadFootType headFootType: Int = BHeadFootType.BOTH) {
        if (headFootType == BHeadFootType.BOTH) {
            for (i in items.indices.reversed()) {
                if (items[i] is BHeadFootBean) {
                    items.removeAt(i)
                }
            }
        } else {
            for (i in items.indices.reversed()) {
                if (items[i] is BHeadFootBean && (items[i] as BHeadFootBean).type == headFootType) {
                    items.removeAt(i)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BViewHolder<Any> {
        val item = items[itemPosition]

        val holder = finalVHFactory.createViewHolder(layoutInflater, parent, item)
                ?: throw IllegalArgumentException("Cannot find the view holder for item:$item")

        val viewTypeInfo = getViewTypeInfo(item.javaClass)

        holder.setListeners(
                View.OnClickListener { onItemClick(it, holder.adapterPosition, viewTypeInfo, true, itemClickListener) },
                View.OnLongClickListener { onItemClick(it, holder.adapterPosition, viewTypeInfo, false, itemLongClickListener) }
        )
        return holder
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        itemPosition = position
        val type = finalVHFactory.getItemViewType(items[position])
        return if (type != -1) type else getViewTypeInfo(items[position].javaClass).viewType
    }

    override fun onBindViewHolder(holder: BViewHolder<Any>, position: Int) {
        val item = items[position]
        val viewTypeInfo = getViewTypeInfo(item.javaClass)

        holder.setContents(item, viewTypeInfo.selectable)
        holder.setSelected((viewTypeInfo.selectable || viewTypeInfo.longClickSelectable) && selections.contains(item))
    }

    override fun onBindViewHolder(holder: BViewHolder<Any>, position: Int, payloads: MutableList<Any>) {
        if (!finalVHFactory.onBindViewHolder(holder, position, payloads)) {
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
    private fun onItemClick(v: View, position: Int, viewTypeInfo: ViewTypeInfo, isClick: Boolean, listener: OnDClickListener<T>?): Boolean {
        if (position < 0 || position >= items.size) {
            return false
        }
        if (isClick) {
            if (viewTypeInfo.selectable) addSelection(position)
            if (viewTypeInfo.clickable) listener?.onClick(v, items[position], position)
        } else {
            if (viewTypeInfo.longClickSelectable) addSelection(position)
            if (viewTypeInfo.longClickable) listener?.onClick(v, items[position], position)
        }
        return true
    }

    /**
     * 设置view type
     *
     * @param classType
     * @param clickable
     * @param selectable
     * @param longClickable
     * @param longClickSelectable
     */
    fun setViewType(classType: Class<*>,
                    clickable: Boolean = true,
                    selectable: Boolean = false,
                    longClickable: Boolean = true,
                    longClickSelectable: Boolean = false): BRecyclerAdapter<T> {

        getViewTypeInfo(classType).apply {
            this.clickable = clickable
            this.selectable = selectable
            this.longClickable = longClickable
            this.longClickSelectable = longClickSelectable
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
            selectable = false
            longClickable = true
            longClickSelectable = false
            viewTypeInfoList[classType] = this
        }
    }

    /**
     * diff util
     */
    private inner class DiffUtilCallback(private val itemCallback: DiffUtil.ItemCallback<T>) : DiffUtil.Callback() {
        private var newData: List<T>? = null

        fun setNewData(newData: List<T>?) {
            this.newData = newData
        }

        override fun getOldListSize() = itemCount

        override fun getNewListSize() = newData?.size ?: 0

        // 判断Item是否已经存在
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return itemCallback.areItemsTheSame(items[oldItemPosition], newData!![newItemPosition])
        }

        // 如果Item已经存在则会调用此方法，判断Item的内容是否一致
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return itemCallback.areContentsTheSame(items[oldItemPosition], newData!![newItemPosition])
        }
    }

    /**
     * 存储ViewType数据
     */
    private inner class ViewTypeInfo {
        internal var viewType: Int = 0
        internal var clickable: Boolean = false
        internal var selectable: Boolean = false
        internal var longClickable: Boolean = false
        internal var longClickSelectable: Boolean = false
    }
}