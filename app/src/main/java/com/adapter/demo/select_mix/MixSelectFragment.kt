package com.adapter.demo.select_mix

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adapter.BRecyclerAdapter
import com.adapter.demo.R
import com.adapter.model.RankItem
import kotlinx.android.synthetic.main.fragment_list.view.rvTest
import kotlinx.android.synthetic.main.fragment_list_select_mix.*
import kotlinx.android.synthetic.main.fragment_list_select_mix.view.*

class MixSelectFragment : Fragment() {
    private lateinit var layout: View
    private lateinit var adapter: BRecyclerAdapter<RankItem>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_list_select_mix, container, false)

        adapter = BRecyclerAdapter<RankItem>(context!!, MixSelectVHFactory(selectable = false, multiSelectable = false))
                .bindRecyclerView(layout.rvTest)
                .setItemInfo(RankItem::class.java, selectable = false, multiSelectable = false)
                .setItems(getItems())
                .setItemClickListener { _, _, _ -> showSelectedItems() }

        layout.rgOptions.check(R.id.rbNoSelection)
        layout.rgOptions.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                //不可选
                R.id.rbNoSelection -> onSelectableChanged(
                        isSelectable = false,
                        isMultiSelectable = false,
                        flag = BRecyclerAdapter.FLAG_UNSELECTABLE)
                //单选
                R.id.rbSingleSelection -> onSelectableChanged(
                        isSelectable = true,
                        isMultiSelectable = false,
                        flag = BRecyclerAdapter.FLAG_SINGLE_SELECTABLE)
                //多选
                R.id.rbMultiSelection -> onSelectableChanged(
                        isSelectable = true,
                        isMultiSelectable = true,
                        flag = BRecyclerAdapter.FLAG_MULTI_SELECTABLE)
            }
        }

        return layout
    }

    /**
     * 不可选/单选/多选 状态更新
     */
    private fun onSelectableChanged(isSelectable: Boolean, isMultiSelectable: Boolean, flag: Int) {
        adapter.setItemInfo(RankItem::class.java, isSelectable, isMultiSelectable)
        adapter.selections.clear()
        adapter.notifyItemRangeChanged(0, adapter.itemCount, flag)

        if (isMultiSelectable) {
            showCbToggleSelectAll()
        } else {
            hideCbToggleSelectAll()
        }

        showSelectedItems()
    }

    private fun hideCbToggleSelectAll() {
        layout.cbToggleSelectAll.visibility = View.GONE
        layout.cbToggleSelectAll.setOnCheckedChangeListener(null)
    }

    private fun showCbToggleSelectAll() {
        layout.cbToggleSelectAll.visibility = View.VISIBLE
        layout.cbToggleSelectAll.isChecked = false
        layout.cbToggleSelectAll.text = "全选"

        layout.cbToggleSelectAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                layout.cbToggleSelectAll.text = "全不选"
                adapter.selectAll()
            } else {
                layout.cbToggleSelectAll.text = "全选"
                adapter.deselectAll()
            }

            showSelectedItems()
        }
    }

    private fun showSelectedItems() {
        layout.tvSelections.text = "选中的项: "
        adapter.selections.forEach { tvSelections.append("rank-${it.rank},") }
    }

    private fun getItems(): List<RankItem> {
        val items = ArrayList<RankItem>()
        for (i in 1..26) {
            items.add(RankItem(i, i))
        }
        return items
    }
}
