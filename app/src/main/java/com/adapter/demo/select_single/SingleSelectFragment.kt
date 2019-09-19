package com.adapter.demo.select_single

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adapter.BRecyclerAdapter
import com.adapter.demo.R
import com.adapter.demo.RankItem
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.fragment_list_select_mix.*

class SingleSelectFragment : Fragment() {
    private lateinit var adapter: BRecyclerAdapter<RankItem>
    private lateinit var vhFactory: SingleSelectVHFactory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_list_select_single, container, false)

        layout.tvDesc.text = "单选 Demo。\n\nRecyclerView："

        vhFactory = SingleSelectVHFactory()
        adapter = BRecyclerAdapter<RankItem>(context!!, vhFactory)
                .bindRecyclerView(layout.rvTest)
                .setItemPicker(vhFactory)
                .setItems(getItems())
                .setItemClickListener { _, _, _ -> showSelectedItems() }

        return layout
    }

    private fun showSelectedItems() {
        tvSelections.text = "选中的项: ${vhFactory.selectedItem}"
    }

    private fun getItems(): List<RankItem> {
        val items = ArrayList<RankItem>()
        for (i in 1..26) {
            items.add(RankItem("", i))
        }
        return items
    }
}
