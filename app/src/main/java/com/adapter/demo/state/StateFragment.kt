package com.adapter.demo.state

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.adapter.BRecyclerAdapter
import com.adapter.demo.R
import com.adapter.demo.simple.SimpleVHFactory
import com.adapter.model.RankItem
import kotlinx.android.synthetic.main.fragment_state.*

/**
 * @author:  Tom Hawk
 * 添加时间: 2020/6/1 10:51
 * 功能描述:
 */
class StateFragment : Fragment(), View.OnClickListener {
    private lateinit var adapter: BRecyclerAdapter<RankItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_state, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLoading.setOnClickListener(this)
        btnEmpty.setOnClickListener(this)
        btnError.setOnClickListener(this)
        btnSetData.setOnClickListener(this)
        btnClearData.setOnClickListener(this)

        adapter = BRecyclerAdapter<RankItem>(view.context, SimpleVHFactory())
            .bindRecyclerView(rvList)
            .setEmptyLayout(R.layout.layout_state_empty) {
                Toast.makeText(
                    view.context,
                    "Click empty layout!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setErrorLayout(R.layout.layout_state_error) {
                Toast.makeText(
                    view.context,
                    "Click empty layout!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setLoadingLayout(R.layout.layout_state_loading)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnLoading -> adapter.showStatePage(BRecyclerAdapter.STATE_LOADING)
            R.id.btnEmpty -> adapter.showStatePage(BRecyclerAdapter.STATE_EMPTY)
            R.id.btnError -> adapter.showStatePage(BRecyclerAdapter.STATE_ERROR)
            R.id.btnSetData -> {
                val items = arrayListOf(
                    RankItem(0, 0),
                    RankItem(1, 1),
                    RankItem(2, 2),
                    RankItem(3, 3)
                )
                adapter.setItems(items)
                adapter.notifyDataSetChanged()
            }
            R.id.btnClearData -> {
                adapter.clear()
                adapter.notifyDataSetChanged()
            }
        }
    }
}