package com.adapter

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/9/19 10:26
 * 功能描述：列表项选择器接口
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/9/19 10:26
 * 修改内容：
 */
interface ItemPicker<T> {
    /**
     * 指定的数据类是否可以被选中
     */
    fun isSelectable(t: T): Boolean

    /**
     * 是否只能单选
     */
    fun isSingleSelectable(t: T): Boolean

    /**
     * 该项是否已被选中
     */
    fun isSelected(t: T): Boolean

    /**
     * 选中该项
     */
    fun select(t: T)

    /**
     * 取消选中该项
     */
    fun deselect(t: T)

    /**
     * 获取当前选中项，单选时用到
     */
    fun getCurrentSelection(items: List<T>): T?
}