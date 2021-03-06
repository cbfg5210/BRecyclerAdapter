package com.adapter.demo.drag

import android.view.LayoutInflater
import android.view.ViewGroup
import com.adapter.BViewHolder
import com.adapter.BViewHolderFactory
import com.adapter.demo.R
import com.adapter.model.RankItem
import kotlinx.android.synthetic.main.item_simple.view.*

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
class DragVHFactory : BViewHolderFactory() {

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        item: Any
    ): BViewHolder<out Any> {
        return RankItemVH(inflater, parent)
    }

    private inner class RankItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
        BViewHolder<RankItem>(inflater, parent, R.layout.item_simple) {

        override fun setContents(item: RankItem, isSelected: Boolean, payload: Any?) {
            itemView.ivIcon.setImageResource(R.mipmap.ic_launcher)
            itemView.tvRank.text = item.rank.toString()
        }
    }
}