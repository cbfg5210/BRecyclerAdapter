package com.adapter.demo.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.adapter.BRecyclerAdapter
import com.adapter.demo.R
import com.adapter.model.RankItem
import kotlinx.android.synthetic.main.fragment_list.view.*

class SimpleFragment : Fragment(), View.OnClickListener {
    private lateinit var adapter: BRecyclerAdapter<RankItem>
    private var idCount: Int = 3

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_list, container, false)

        adapter = BRecyclerAdapter<RankItem>(context!!, SimpleVHFactory())
                .bindRecyclerView(layout.rvTest)
                .setItems(getItems())
                .setItemLongClickListener { _, item, position -> Toast.makeText(activity, "长按了 ItemView, position=$position，rank=${item.rank}", Toast.LENGTH_SHORT).show() }
                .setItemClickListener { view, item, position ->
                    when (view.id) {
                        R.id.ivIcon -> Toast.makeText(activity, "点击了 Icon, position=$position，rank=${item.rank}", Toast.LENGTH_SHORT).show()
                        R.id.tvRank -> Toast.makeText(activity, "点击了 Rank, position=$position，rank=${item.rank}", Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(activity, "点击了 ItemView, position=$position，rank=${item.rank}", Toast.LENGTH_SHORT).show()
                    }
                }

        layout.btnTopAdd.setOnClickListener(this)
        layout.btnTopRemove.setOnClickListener(this)
        layout.btnBottomAdd.setOnClickListener(this)
        layout.btnBottomRemove.setOnClickListener(this)

        return layout
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnTopAdd -> {
                adapter.items.add(0, RankItem(idCount, ++idCount))
                adapter.notifyItemInserted(0)
            }
            R.id.btnTopRemove -> {
                if (adapter.itemCount > 0) {
                    adapter.removeAt(0)
                    adapter.notifyItemRemoved(0)
                }
            }
            R.id.btnBottomAdd -> {
                val index = adapter.itemCount
                adapter.items.add(index, RankItem(idCount, ++idCount))
                adapter.notifyItemInserted(index)
            }
            R.id.btnBottomRemove -> {
                if (adapter.itemCount > 0) {
                    val index = adapter.itemCount - 1
                    adapter.removeAt(index)
                    adapter.notifyItemRemoved(index)
                }
            }
        }
    }

    private fun getItems(): List<RankItem> {
        val items = ArrayList<RankItem>()
        for (i in 1..idCount) {
            items.add(RankItem(i, i))
        }
        return items
    }
}
