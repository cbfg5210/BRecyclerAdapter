package com.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.adapter.bean.BHeadFootBean

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
class BHeadFootVHFactory : BViewHolderFactory() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, item: Any): BViewHolder<Any>? {
        return if (item is BHeadFootBean) HeadFootVH(inflater, parent) as BViewHolder<Any> else null
    }

    internal inner class HeadFootVH(inflater: LayoutInflater, parent: ViewGroup?) : BViewHolder<BHeadFootBean>(inflater, parent, false, R.layout.item_adapter_head_foot) {
        private val tvRecyclerHeadFoot: TextView = itemView.findViewById(R.id.tvRecyclerHeadFoot)

        override fun setContents(item: BHeadFootBean, selectable: Boolean) {
            tvRecyclerHeadFoot.text = item.msg
        }
    }
}