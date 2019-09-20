package com.adapter.demo.complex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adapter.BRecyclerAdapter
import com.adapter.demo.R
import com.adapter.model.CommodityItem
import com.adapter.model.ShopItem
import kotlinx.android.synthetic.main.fragment_complex.view.*
import kotlinx.android.synthetic.main.fragment_list.view.rvTest
import kotlinx.android.synthetic.main.fragment_list.view.tvDesc

class ComplexFragment : Fragment() {
    private lateinit var layout: View
    private lateinit var adapter: BRecyclerAdapter<Any>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_complex, container, false)
        layout.tvDesc.text = "复合 demo。"

        adapter = BRecyclerAdapter<Any>(context!!, ComplexVHFactory())
                .bindRecyclerView(layout.rvTest)
                .setItemInfo(ShopItem::class.java, selectable = true, multiSelectable = true)
                .setItemInfo(CommodityItem::class.java, selectable = true, multiSelectable = true)
                .setItems(getItems())
                .setItemClickListener { _, item, position ->
                    if (item is ShopItem) {
                        onShopClick(item, position)
                    } else {
                        onCommodityClick(item as CommodityItem, position)
                    }
                    count()
                }

        return layout
    }

    /**
     * 点击了店铺，全选/取消全选 该店铺商品
     */
    private fun onShopClick(shopItem: ShopItem, position: Int) {
        val selections = adapter.selections
        val items = adapter.items
        var i = position
        val flag: Int

        //全选
        if (selections.contains(shopItem)) {
            flag = BRecyclerAdapter.FLAG_PAYLOADS_SELECT

            while (++i < items.size) {
                val item = items[i]
                if (item !is CommodityItem) {
                    break
                }
                if (!selections.contains(item)) {
                    selections.add(item)
                }
            }
        } else {
            //取消全选
            flag = BRecyclerAdapter.FLAG_PAYLOADS_DESELECT

            while (++i < items.size) {
                val item = items[i]
                if (item !is CommodityItem) {
                    break
                }
                if (selections.contains(item)) {
                    selections.remove(item)
                }
            }
        }

        //判断是否已经是最后一项
        if (i == items.size - 1) {
            i++
        }
        adapter.notifyItemRangeChanged(position + 1, i - position - 1, flag)
    }

    /**
     * 点击了商品，如果该店铺下所有的商品都没有选中，要取消全选；如果该店铺下所有的商品都选中了，要设为全选
     */
    private fun onCommodityClick(commodityItem: CommodityItem, position: Int) {
        val items = adapter.items
        val selections = adapter.selections
        var shopItem: ShopItem? = null

        for (i in (position - 1) downTo 0) {
            val item = items[i]
            if (item is ShopItem) {
                shopItem = item
                break
            }
        }

        shopItem ?: return

        val shopIndex = items.indexOf(shopItem)
        var isAllSelected = true

        //判断该商店的商品是否全部被选中
        for (i in (shopIndex + 1) until items.size) {
            val item = items[i]
            if (item !is CommodityItem) {
                break
            }
            if (!selections.contains(item)) {
                isAllSelected = false
                break
            }
        }

        //商店目前是选中状态
        if (selections.contains(shopItem)) {
            //如果该商店的商品不是全部被选中，要取消商店的选中
            if (!isAllSelected) {
                selections.remove(shopItem)
                adapter.notifyItemChanged(shopIndex, BRecyclerAdapter.FLAG_PAYLOADS_DESELECT)
            }
        } else if (isAllSelected) {
            //商店目前不是选中状态
            //如果该商店的商品被全部选中，要设置商店的选中
            selections.add(shopItem)
            adapter.notifyItemChanged(shopIndex, BRecyclerAdapter.FLAG_PAYLOADS_SELECT)
        }
    }

    /**
     * 计算金额
     */
    private fun count() {
        var count = 0F
        adapter.selections.forEach { selection ->
            if (selection is CommodityItem) {
                count += selection.price
            }
        }
        layout.tvCount.text = String.format("总计：¥ %.2f", count)
    }

    private fun getItems(): List<Any> {
        return arrayListOf(
                ShopItem(R.mipmap.ic_launcher_round, "依依得衣服装店"),
                CommodityItem(R.mipmap.ic_launcher, "长袖连衣裙", 95F),
                CommodityItem(R.mipmap.ic_launcher, "衬衫裙", 85F),
                CommodityItem(R.mipmap.ic_launcher, "背带裤", 40F),
                ShopItem(R.mipmap.ic_launcher_round, "小米官方商店"),
                CommodityItem(R.mipmap.ic_launcher, "小米手机", 899F),
                CommodityItem(R.mipmap.ic_launcher, "充电宝", 50F),
                ShopItem(R.mipmap.ic_launcher_round, "乐淘百货"),
                CommodityItem(R.mipmap.ic_launcher, "32包竹浆本色抽纸", 27.9F),
                CommodityItem(R.mipmap.ic_launcher, "毛毛虫童鞋", 79.9F),
                CommodityItem(R.mipmap.ic_launcher, "花生酥516g", 32.8F)
        )
    }
}
