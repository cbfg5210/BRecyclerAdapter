package com.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/9/12 17:06
 * 功能描述：BViewHolder生产者
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2018/9/12 17:06
 * 修改内容：
 */
abstract class BViewHolderFactory {
    /**
     * 生成ViewHolder
     *
     * @param inflater LayoutInflater
     * @param parent ViewGroup?
     * @param item Any
     * @return BViewHolder<Any>?
     */
    abstract fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, item: Any): BViewHolder<Any>

    /**
     * 如果要根据item的某个属性来决定布局，需要重写这个方法，在
     * @see BViewHolderFactory.createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, item: Any)
     * 方法中根据此方法来判断返回ViewHolder
     *
     * @param item Any
     * @return Int
     */
    open fun getItemViewType(item: Any) = -1
}