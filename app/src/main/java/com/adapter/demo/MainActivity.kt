package com.adapter.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adapter.BRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BRecyclerAdapter<TitleItem>(this, TitleItemVHFactory())
                .bindRecyclerView(rvTest)
                .setItems(getItems())
                .setItemClickListener { _, item, _ -> Toast.makeText(this@MainActivity, "click item:${item.title}", Toast.LENGTH_SHORT).show() }
                .setItemLongClickListener { _, item, _ -> Toast.makeText(this@MainActivity, "long click item:${item.title}", Toast.LENGTH_SHORT).show() }
    }

    private fun getItems(): List<TitleItem> {
        return arrayListOf(
                TitleItem("A"),
                TitleItem("B"),
                TitleItem("C"),
                TitleItem("D"),
                TitleItem("E"),
                TitleItem("F"),
                TitleItem("G"),
                TitleItem("H"),
                TitleItem("I"),
                TitleItem("J"),
                TitleItem("K"),
                TitleItem("O"),
                TitleItem("P"),
                TitleItem("Q"),
                TitleItem("R"),
                TitleItem("S"),
                TitleItem("T"),
                TitleItem("U"),
                TitleItem("V"),
                TitleItem("W"),
                TitleItem("X"),
                TitleItem("Y"),
                TitleItem("Z")
        )
    }
}
