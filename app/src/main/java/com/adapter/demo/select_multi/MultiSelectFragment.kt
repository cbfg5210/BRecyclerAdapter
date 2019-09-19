package com.adapter.demo.select_multi

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
import kotlinx.android.synthetic.main.fragment_list_select_mix.*
import kotlinx.android.synthetic.main.fragment_list_select_mix.view.*

class MultiSelectFragment : Fragment() {
    private lateinit var layout: View
    private lateinit var adapter: BRecyclerAdapter<RankItem>
    private lateinit var vhFactory: MultiSelectVHFactory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_list_select_multi, container, false)

        layout.tvDesc.text = "多选 Demo。\n\nRecyclerView："

        vhFactory = MultiSelectVHFactory()
        adapter = BRecyclerAdapter<RankItem>(context!!, vhFactory)
                .bindRecyclerView(layout.rvTest)
                .setItemPicker(vhFactory)
                .setItems(getItems())
                .setItemClickListener { _, _, _ -> showSelectedItems() }

        layout.cbToggleSelectAll.setOnCheckedChangeListener { _, isChecked ->
            vhFactory.selectedItems.clear()

            if (isChecked) {
                layout.cbToggleSelectAll.text = "全不选"
                vhFactory.selectedItems.addAll(adapter.items)
            } else {
                layout.cbToggleSelectAll.text = "全选"
            }

            adapter.notifyDataSetChanged()
            showSelectedItems()
        }

        return layout
    }

    private fun showSelectedItems() {
        layout.tvSelections.text = "选中的项: "
        vhFactory.selectedItems.forEach { tvSelections.append("rank-${it.rank},") }
    }

    private fun getItems(): List<RankItem> {
        val items = ArrayList<RankItem>()
        for (i in 1..26) {
            items.add(RankItem("", i))
        }
        return items
    }
}