package com.adapter.demo.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.adapter.BRecyclerAdapter
import com.adapter.demo.RankItem
import com.adapter.demo.R
import kotlinx.android.synthetic.main.fragment_list.view.*

class SimpleFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_list, container, false)

        layout.tvDesc.text = "最基础的一个 Demo，设置了点击和长按事件。\n\nRecyclerView："

        BRecyclerAdapter<RankItem>(context!!, SimpleVHFactory())
                .bindRecyclerView(layout.rvTest)
                .setItems(getItems())
                .setItemClickListener { _, item, _ -> Toast.makeText(activity, "点击了:${item.rank}", Toast.LENGTH_SHORT).show() }
                .setItemLongClickListener { _, item, _ -> Toast.makeText(activity, "长按了:${item.rank}", Toast.LENGTH_SHORT).show() }

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
