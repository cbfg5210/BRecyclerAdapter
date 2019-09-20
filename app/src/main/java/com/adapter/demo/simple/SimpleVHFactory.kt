package com.adapter.demo.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adapter.BViewHolder
import com.adapter.BViewHolderFactory
import com.adapter.demo.R
import com.adapter.demo.model.RankItem
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
class SimpleVHFactory : BViewHolderFactory() {

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

        /*
         * 默认会给 itemView 设置点击和长按事件,
         * 如果需要给除 itemView 外的 view 设置点击/长按事件则需要重写以下方法,
         * 否则不用重写以下方法
         */
        override fun setListeners(
            clickListener: View.OnClickListener,
            longClickListener: View.OnLongClickListener
        ) {
            //默认给 itemView 设置点击和长按事件,如果不需要可以去掉以下一句
            super.setListeners(clickListener, longClickListener)

            //给 icon 设置点击事件
            itemView.ivIcon.setOnClickListener(clickListener)
            ////给 icon 设置长按事件
            //itemView.ivIcon.setOnLongClickListener(longClickListener)

            //给 rank 设置点击事件
            itemView.tvRank.setOnClickListener(clickListener)
            ////给 rank 设置长按事件
            //itemView.tvRank.setOnLongClickListener(longClickListener)
        }
    }
}