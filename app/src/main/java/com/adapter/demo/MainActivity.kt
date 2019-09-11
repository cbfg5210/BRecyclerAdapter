package com.adapter.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.adapter.BRecyclerAdapter
import com.adapter.OnDClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BRecyclerAdapter<TitleItem>(this, TitleItemVHFactory())
                .bindRecyclerView(rvTest)
                .setItems(getItems())
                .setItemClickListener(object : OnDClickListener<TitleItem> {
                    override fun onClick(view: View, item: TitleItem, position: Int) {
                        Toast.makeText(this@MainActivity, "click item:${item.title}", Toast.LENGTH_SHORT).show()
                    }
                })
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
