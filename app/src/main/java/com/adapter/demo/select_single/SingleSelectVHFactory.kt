//package com.adapter.demo.select_single
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import com.adapter.BViewHolder
//import com.adapter.BViewHolderFactory
//import com.adapter.ItemPicker
//import com.adapter.demo.R
//import com.adapter.demo.RankItem
//import kotlinx.android.synthetic.main.item_select_mix.view.*
//import kotlinx.android.synthetic.main.item_simple.view.ivIcon
//import kotlinx.android.synthetic.main.item_simple.view.tvRank
//
///**
// * 添加人：  Tom Hawk
// * 添加时间：2018/7/24 9:45
// * 功能描述：
// *
// * 修改人：  Tom Hawk
// * 修改时间：2018/7/24 9:45
// * 修改内容：
// */
//class SingleSelectVHFactory : BViewHolderFactory(), ItemPicker<RankItem> {
//    var selectedItem: RankItem? = null
//
//    override fun createViewHolder(
//            inflater: LayoutInflater,
//            parent: ViewGroup?,
//            item: Any
//    ): BViewHolder<out Any> {
//        return RankItemVH(inflater, parent)
//    }
//
//    override fun isSelectable(t: RankItem) = true
//
//    override fun isSingleSelectable(t: RankItem) = true
//
//    override fun isSelected(t: RankItem) = selectedItem == t
//
//    override fun select(t: RankItem) {
//        selectedItem = t
//    }
//
//    override fun deselect(t: RankItem) {
//    }
//
//    override fun getCurrentSelection(items: List<RankItem>) = selectedItem
//
//    private inner class RankItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
//            BViewHolder<RankItem>(inflater, parent, R.layout.item_select_single) {
//
//        override fun setContents(item: RankItem, payloads: MutableList<Any>?) {
//            if (payloads != null && payloads.isNotEmpty()) {
//                itemView.rbSelect.isChecked = isSelected(item)
//            } else {
//                itemView.ivIcon.setImageResource(R.mipmap.ic_launcher)
//                itemView.tvRank.text = item.rank.toString()
//                itemView.rbSelect.isChecked = isSelected(item)
//            }
//        }
//    }
//}