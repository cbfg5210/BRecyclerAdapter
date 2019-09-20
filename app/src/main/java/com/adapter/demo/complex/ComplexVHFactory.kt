package com.adapter.demo.complex

import android.view.LayoutInflater
import android.view.ViewGroup
import com.adapter.BViewHolder
import com.adapter.BViewHolderFactory
import com.adapter.demo.R
import com.adapter.model.CommodityItem
import com.adapter.model.ShopItem
import kotlinx.android.synthetic.main.item_complex_commodity.view.*
import kotlinx.android.synthetic.main.item_complex_shop.view.*

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/7/24 9:45
 * 功能描述：
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/7/24 9:45
 * 修改内容：
 */
class ComplexVHFactory : BViewHolderFactory() {

    override fun createViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            item: Any
    ): BViewHolder<out Any> {
        return if (item is ShopItem) ShopItemVH(inflater, parent) else CommodityItemVH(inflater, parent)
    }

    /**
     * 商店 ViewHolder
     */
    private inner class ShopItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
            BViewHolder<ShopItem>(inflater, parent, R.layout.item_complex_shop) {

        override fun setContents(item: ShopItem, isSelected: Boolean, payload: Any?) {
            if (payload != null) {
                itemView.cbToggleSelectAll.isChecked = isSelected
                return
            }
            itemView.cbToggleSelectAll.isChecked = isSelected
            itemView.ivShopImage.setImageResource(item.image)
            itemView.tvShopName.text = item.name
        }
    }

    /**
     * 商品 ViewHolder
     */
    private inner class CommodityItemVH(inflater: LayoutInflater, parent: ViewGroup?) :
            BViewHolder<CommodityItem>(inflater, parent, R.layout.item_complex_commodity) {

        override fun setContents(item: CommodityItem, isSelected: Boolean, payload: Any?) {
            if (payload != null) {
                itemView.isSelected = isSelected
                itemView.cbSelect.isChecked = isSelected
                return
            }
            itemView.isSelected = isSelected
            itemView.cbSelect.isChecked = isSelected
            itemView.ivImage.setImageResource(item.image)
            itemView.tvName.text = item.name
            itemView.tvPrice.text = "¥ ${String.format("%.2f", item.price)}"
        }
    }
}