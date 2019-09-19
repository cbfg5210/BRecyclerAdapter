package com.adapter.demo.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adapter.BRecyclerAdapter
import com.adapter.demo.R
import com.adapter.demo.RankItem
import kotlinx.android.synthetic.main.fragment_list.view.rvTest
import kotlinx.android.synthetic.main.fragment_list.view.tvDesc
import kotlinx.android.synthetic.main.fragment_list_select.*
import kotlinx.android.synthetic.main.fragment_list_select.view.*

class SelectFragment : Fragment() {
    private lateinit var adapter: BRecyclerAdapter<RankItem>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_list_select, container, false)

        layout.tvDesc.text = "选择 Item Demo。\n\nRecyclerView："

        val vhFactory = SelectVHFactory()

        adapter = BRecyclerAdapter<RankItem>(context!!, vhFactory)
                .bindRecyclerView(layout.rvTest)
                .setItemPicker(vhFactory)
                .setItems(getItems())
                .setItemClickListener { _, _, _ ->
                    tvSelections.text = "选中的项: "
                    vhFactory.selectedItems.forEach { tvSelections.append("rank-${it.rank},") }
                }

        layout.rgOptions.check(R.id.rbNoSelection)
        layout.rgOptions.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                //不可选
                R.id.rbNoSelection -> {
                    tvSelections.text = ""
                    vhFactory.isRankItemSelectable = false
                    adapter.notifyItemRangeChanged(0, adapter.itemCount, 1)
                }
                //单选
                R.id.rbSingleSelection -> {
                    vhFactory.isRankItemSelectable = true
                    vhFactory.isRankItemSingleSelectable = true
                    adapter.notifyItemRangeChanged(0, adapter.itemCount, 1)
                }
                //多选
                R.id.rbMultiSelection -> {
                    vhFactory.isRankItemSelectable = true
                    vhFactory.isRankItemSingleSelectable = false
                    adapter.notifyItemRangeChanged(0, adapter.itemCount, 1)
                }
            }
        }

        return layout
    }

    private fun getItems(): List<RankItem> {
        val items = ArrayList<RankItem>()
        for (i in 1..26) {
            items.add(RankItem("", i))
        }
        return items
    }
}
