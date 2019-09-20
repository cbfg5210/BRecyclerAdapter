package com.adapter.demo.payloads

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adapter.BViewHolder
import com.adapter.BViewHolderFactory
import com.adapter.demo.R
import com.adapter.demo.RankItem
import kotlinx.android.synthetic.main.item_payloads.view.*

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
class PayloadsVHFactory : BViewHolderFactory() {

    override fun createViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            item: Any
    ): BViewHolder<out Any> {
        return RankItemVH(inflater, parent)
    }

    private inner class RankItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
            BViewHolder<RankItem>(inflater, parent, R.layout.item_payloads) {

        override fun setContents(item: RankItem, isSelected: Boolean, payload: Any?) {
            if (payload != null) {
                itemView.tvRank.text = payload.toString()
            } else {
                itemView.ivIcon.setImageResource(R.mipmap.ic_launcher)
                itemView.tvRank.text = item.rank.toString()
            }
        }

        override fun setListeners(clickListener: View.OnClickListener, longClickListener: View.OnLongClickListener) {
            itemView.btnRefreshAll.setOnClickListener(clickListener)
            itemView.btnRefreshPart.setOnClickListener(clickListener)
        }
    }
}