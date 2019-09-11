package com.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.adapter.bean.BStatusBean

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/12 9:54
 * 功能描述：
 *
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/12 9:54
 * 修改内容：
 */
class BStatusVHFactory : BViewHolderFactory() {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, item: Any): BViewHolder<Any>? {
        return if (item is BStatusBean) StatusLayoutVH(inflater, parent) as BViewHolder<Any> else null
    }

    internal inner class StatusLayoutVH(inflater: LayoutInflater, parent: ViewGroup?) : BViewHolder<BStatusBean>(inflater, parent, false, R.layout.item_adapter_status) {
        private val tvRecyclerStatus: TextView = itemView.findViewById(R.id.tvRecyclerStatus)

        override fun setContents(item: BStatusBean, selectable: Boolean) {
            tvRecyclerStatus.text = item.tip
        }
    }
}