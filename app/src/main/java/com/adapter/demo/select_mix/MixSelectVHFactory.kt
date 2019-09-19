package com.adapter.demo.select_mix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adapter.BRecyclerAdapter
import com.adapter.BViewHolder
import com.adapter.BViewHolderFactory
import com.adapter.ItemPicker
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
class MixSelectVHFactory : BViewHolderFactory(), ItemPicker<RankItem> {
    companion object {
        const val FLAG_SELECTABLE_CHANGED = 1
    }

    val selectedItems = ArrayList<RankItem>()

    // 是否可选,如果不可选的话清空 selectedItems
    var isRankItemSelectable = false
        set(value) {
            field = value
            if (!value) {
                selectedItems.clear()
            }
        }

    // 是否单选,如果单选的话清空 selectedItems
    var isRankItemSingleSelectable = false
        set(value) {
            field = value
            if (value) {
                selectedItems.clear()
            }
        }

    override fun createViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            item: Any
    ): BViewHolder<out Any> {
        return RankItemVH(inflater, parent)
    }

    override fun isSelectable(t: RankItem) = isRankItemSelectable

    override fun isSingleSelectable(t: RankItem) = isRankItemSingleSelectable

    override fun isSelected(t: RankItem) = selectedItems.contains(t)

    override fun select(t: RankItem) {
        selectedItems.add(t)
    }

    override fun deselect(t: RankItem) {
        selectedItems.remove(t)
    }

    override fun getCurrentSelection(items: List<RankItem>): RankItem? {
        return if (isRankItemSingleSelectable && selectedItems.size > 0) selectedItems[0] else null
    }

    private inner class RankItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
            BViewHolder<RankItem>(inflater, parent, R.layout.item_select_mix) {

        private fun updateSelectableState() {
            if (isRankItemSelectable) {
                if (isRankItemSingleSelectable) {
                    itemView.rbSelect.visibility = View.VISIBLE
                    itemView.cbSelect.visibility = View.GONE
                } else {
                    itemView.rbSelect.visibility = View.GONE
                    itemView.cbSelect.visibility = View.VISIBLE
                }
            } else {
                itemView.rbSelect.visibility = View.GONE
                itemView.cbSelect.visibility = View.GONE
            }
        }

        override fun setContents(item: RankItem, payloads: MutableList<Any>?) {
            if (payloads != null && payloads.isNotEmpty()) {
                val flag = payloads[0] as Int
                if (flag == FLAG_SELECTABLE_CHANGED) {
                    updateSelectableState()
                } else if (flag == BRecyclerAdapter.FLAG_PAYLOADS_SELECT) {
                    if (isRankItemSingleSelectable) {
                        itemView.rbSelect.isChecked = true
                    } else {
                        itemView.cbSelect.isChecked = true
                    }
                } else if (flag == BRecyclerAdapter.FLAG_PAYLOADS_DESELECT) {
                    if (isRankItemSingleSelectable) {
                        itemView.rbSelect.isChecked = false
                    } else {
                        itemView.cbSelect.isChecked = false
                    }
                }
                return
            }

            itemView.ivIcon.setImageResource(R.mipmap.ic_launcher)
            itemView.tvRank.text = item.rank.toString()

            updateSelectableState()

            if (isRankItemSingleSelectable) {
                itemView.rbSelect.isChecked = isSelected(item)
            } else {
                itemView.cbSelect.isChecked = isSelected(item)
            }
        }
    }
}