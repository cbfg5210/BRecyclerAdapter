package com.adapter.demo.select_single

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adapter.BRecyclerAdapter
import com.adapter.demo.R
import com.adapter.model.RankItem
import kotlinx.android.synthetic.main.fragment_list.view.rvTest
import kotlinx.android.synthetic.main.fragment_list_select_single.view.*

class SingleSelectFragment : Fragment() {
    private lateinit var layout: View
    private lateinit var adapter: BRecyclerAdapter<RankItem>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_list_select_single, container, false)

        val items = getItems()

        adapter = BRecyclerAdapter<RankItem>(context!!, SingleSelectVHFactory())
                .bindRecyclerView(layout.rvTest)
                .setItemInfo(RankItem::class.java, selectable = true, multiSelectable = false)
                .setItems(items)
                .addSelection(items[0])
                .setItemClickListener { _, _, _ -> showSelectedItem() }

        showSelectedItem()

        return layout
    }

    private fun showSelectedItem() {
        layout.tvSelections.text = "选中的项: ${adapter.selections}"
    }

    private fun getItems(): List<RankItem> {
        val items = ArrayList<RankItem>()
        for (i in 1..26) {
            items.add(RankItem("", i))
        }
        return items
    }
}
