package com.adapter.demo.diff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adapter.BDiffCallback
import com.adapter.BRecyclerAdapter
import com.adapter.demo.R
import com.adapter.model.RankItem
import kotlinx.android.synthetic.main.fragment_diff.view.*

class DiffFragment : Fragment(), View.OnClickListener {
    private lateinit var adapter: BRecyclerAdapter<RankItem>
    private var idCount: Int = 3

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_diff, container, false)

        adapter = BRecyclerAdapter<RankItem>(context!!, DiffVHFactory())
                .bindRecyclerView(layout.rvTest)
                .setItems(getItems())
                .setBDiffCallback(object : BDiffCallback<RankItem> {
                    override fun areContentsTheSame(oldItem: RankItem, newItem: RankItem): Boolean {
                        return oldItem.id == newItem.id
                                && oldItem.rank == newItem.rank
                    }

                    override fun getChangePayload(oldItem: RankItem, newItem: RankItem): Any? = 1
                })

        layout.btnTopAdd.setOnClickListener(this)
        layout.btnTopRemove.setOnClickListener(this)
        layout.btnUpdateAll.setOnClickListener(this)
        layout.btnShuffle.setOnClickListener(this)

        return layout
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnTopAdd -> {
                val newItems = ArrayList<RankItem>()
                newItems.addAll(adapter.items)

                idCount++
                newItems.add(0, RankItem(idCount, idCount))
                idCount++
                newItems.add(0, RankItem(idCount, idCount))
                adapter.refreshItems(newItems)
            }

            R.id.btnTopRemove -> {
                val newItems = ArrayList<RankItem>()
                newItems.addAll(adapter.items)

                if (newItems.size > 1) {
                    newItems.removeAt(0)
                    newItems.removeAt(0)
                } else if (newItems.size > 0) {
                    adapter.removeAt(0)
                }
                adapter.refreshItems(newItems)
            }

            R.id.btnShuffle -> {
                val newItems = ArrayList<RankItem>()
                adapter.items.forEach { newItems.add(RankItem(it.id, it.rank)) }
                newItems.shuffle()
                adapter.refreshItems(newItems)
            }

            R.id.btnUpdateAll -> {
                val newItems = ArrayList<RankItem>()
                adapter.items.forEach { newItems.add(RankItem(it.id, it.rank + 1)) }
                adapter.refreshItems(newItems)
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
