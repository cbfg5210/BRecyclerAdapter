package com.adapter.demo.payloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adapter.BRecyclerAdapter
import com.adapter.demo.R
import com.adapter.demo.RankItem
import kotlinx.android.synthetic.main.fragment_list.view.*

class PayloadsFragment : Fragment() {
    private lateinit var adapter: BRecyclerAdapter<RankItem>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_list, container, false)

        layout.tvDesc.text = "Item 局部刷新 Demo（RecyclerView.Adapter.notifyItemChanged(int position, @Nullable Object payload)）。\n\nRecyclerView："

        adapter = BRecyclerAdapter<RankItem>(context!!, PayloadsVHFactory())
                .bindRecyclerView(layout.rvTest)
                .setItems(getItems())
                .setItemClickListener { view, item, position ->
                    item.rank += 100

                    if (view.id == R.id.btnRefreshAll) {
                        adapter.notifyItemChanged(position)
                    } else {
                        adapter.notifyItemChanged(position, item.rank)
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
