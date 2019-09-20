package com.adapter.demo.select_mix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adapter.BRecyclerAdapter
import com.adapter.BViewHolder
import com.adapter.BViewHolderFactory
import com.adapter.demo.R
import com.adapter.model.RankItem
import kotlinx.android.synthetic.main.item_select_mix.view.*

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/24 9:45
 * 功能描述：
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/24 9:45
 * 修改内容：
 */
class MixSelectVHFactory(private var selectable: Boolean, private var multiSelectable: Boolean) : BViewHolderFactory() {

    override fun createViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            item: Any
    ): BViewHolder<out Any> {
        return RankItemVH(inflater, parent)
    }

    private inner class RankItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
            BViewHolder<RankItem>(inflater, parent, R.layout.item_select_mix) {

        /**
         * 更新 不可选/单选/多选 状态:
         */
        private fun updateSelectableState() {
            if (!selectable) {
                itemView.rbSelect.visibility = View.GONE
                itemView.cbSelect.visibility = View.GONE
                return
            }

            if (multiSelectable) {
                itemView.rbSelect.visibility = View.GONE
                itemView.cbSelect.visibility = View.VISIBLE
            } else {
                itemView.rbSelect.visibility = View.VISIBLE
                itemView.cbSelect.visibility = View.GONE
            }
        }

        override fun setContents(item: RankItem, isSelected: Boolean, payload: Any?) {
            if (payload == null) {
                itemView.ivIcon.setImageResource(R.mipmap.ic_launcher)
                itemView.tvRank.text = item.rank.toString()

                updateSelectableState()

                if (selectable) {
                    if (multiSelectable) {
                        itemView.cbSelect.isChecked = isSelected
                    } else {
                        itemView.rbSelect.isChecked = isSelected
                    }
                }
                return
            }

            when (payload as Int) {
                BRecyclerAdapter.FLAG_UNSELECTABLE -> {
                    selectable = false
                    updateSelectableState()
                }
                BRecyclerAdapter.FLAG_SINGLE_SELECTABLE -> {
                    selectable = true
                    multiSelectable = false
                    updateSelectableState()
                    itemView.rbSelect.isChecked = isSelected
                }
                BRecyclerAdapter.FLAG_MULTI_SELECTABLE -> {
                    selectable = true
                    multiSelectable = true
                    updateSelectableState()
                    itemView.cbSelect.isChecked = isSelected
                }
                BRecyclerAdapter.FLAG_PAYLOADS_SELECT -> {
                    if (multiSelectable) {
                        itemView.cbSelect.isChecked = true
                    } else {
                        itemView.rbSelect.isChecked = true
                    }
                }
                BRecyclerAdapter.FLAG_PAYLOADS_DESELECT -> {
                    if (multiSelectable) {
                        itemView.cbSelect.isChecked = false
                    } else {
                        itemView.rbSelect.isChecked = false
                    }
                }
            }
        }
    }
}