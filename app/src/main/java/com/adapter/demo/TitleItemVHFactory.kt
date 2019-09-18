package com.adapter.demo

import android.view.LayoutInflater
import android.view.ViewGroup
import com.adapter.BViewHolder
import com.adapter.BViewHolderFactory
import kotlinx.android.synthetic.main.item_text.view.*

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/24 9:45
 * 功能描述：
 *
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/24 9:45
 * 修改内容：
 */
class TitleItemVHFactory : BViewHolderFactory() {

    override fun createViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            item: Any
    ): BViewHolder<out Any> {
        return TitleItemVH(inflater, parent)
    }

    private inner class TitleItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
            BViewHolder<TitleItem>(inflater, parent, R.layout.item_text) {

        override fun setContents(item: TitleItem, payloads: MutableList<Any>?) {
            itemView.tvText.text = item.title
        }
    }
}