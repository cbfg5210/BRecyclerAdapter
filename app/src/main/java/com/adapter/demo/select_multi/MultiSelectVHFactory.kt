package com.adapter.demo.select_multi

import android.view.LayoutInflater
import android.view.ViewGroup
import com.adapter.BRecyclerAdapter
import com.adapter.BViewHolder
import com.adapter.BViewHolderFactory
import com.adapter.demo.R
import com.adapter.demo.RankItem
import kotlinx.android.synthetic.main.item_select_mix.view.*
import kotlinx.android.synthetic.main.item_simple.view.ivIcon
import kotlinx.android.synthetic.main.item_simple.view.tvRank

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/24 9:45
 * 功能描述：
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/24 9:45
 * 修改内容：
 */
class MultiSelectVHFactory : BViewHolderFactory() {

    override fun createViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            item: Any
    ): BViewHolder<out Any> {
        return RankItemVH(inflater, parent)
    }

    private inner class RankItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
            BViewHolder<RankItem>(inflater, parent, R.layout.item_select_multi) {

        override fun setContents(item: RankItem, isSelected: Boolean, payloads: MutableList<Any>?) {
            if (payloads != null && payloads.isNotEmpty()) {
                when (payloads[0] as Int) {
                    BRecyclerAdapter.FLAG_PAYLOADS_SELECT -> itemView.cbSelect.isChecked = true
                    BRecyclerAdapter.FLAG_PAYLOADS_DESELECT -> itemView.cbSelect.isChecked = false
                }
                return
            }

            itemView.ivIcon.setImageResource(R.mipmap.ic_launcher)
            itemView.tvRank.text = item.rank.toString()
            itemView.cbSelect.isChecked = isSelected
        }
    }
}