package com.adapter.demo.drag

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adapter.BRecyclerAdapter
import com.adapter.demo.R
import com.adapter.model.RankItem
import kotlinx.android.synthetic.main.fragment_drag.view.*
import kotlinx.android.synthetic.main.fragment_list.view.rvTest

class DragFragment : Fragment(), View.OnClickListener {
    private lateinit var layout: View
    private lateinit var adapter: BRecyclerAdapter<RankItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_drag, container, false)

        adapter = BRecyclerAdapter<RankItem>(context!!, DragVHFactory())
            .bindRecyclerView(layout.rvTest)
            .setItems(getItems())

        layout.btnLinear.setOnClickListener(this)
        layout.btnGrid.setOnClickListener(this)
        layout.btnEnableDrag.setOnClickListener(this)
        layout.btnDisableDrag.setOnClickListener(this)
        layout.btnNotify.setOnClickListener(this)

        return layout
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnEnableDrag -> {
                adapter.enableDrag(RankItem::class.java)
                adapter.setDragBgColor(RankItem::class.java, 0, Color.LTGRAY)
                //adapter.setDragBgRes(RankItem::class.java, 0, R.drawable.bg_drag_bg)
            }
            R.id.btnDisableDrag -> {
                adapter.disableDrag(RankItem::class.java)
            }
            R.id.btnNotify -> {
                adapter.notifyDataSetChanged()
            }
            R.id.btnLinear -> {
                layout.rvTest.layoutManager = LinearLayoutManager(context)
            }
            R.id.btnGrid -> {
                val gridManager = GridLayoutManager(context, RecyclerView.VERTICAL)
                gridManager.spanCount = 3
                layout.rvTest.layoutManager = gridManager
            }
        }
    }

    private fun getItems(): List<RankItem> {
        val items = ArrayList<RankItem>()
        for (i in 1..26) {
            items.add(RankItem(i, i))
        }
        return items
    }
}
