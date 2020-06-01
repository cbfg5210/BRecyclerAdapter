package com.adapter.demo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.adapter.demo.complex.ComplexFragment
import com.adapter.demo.diff.DiffFragment
import com.adapter.demo.drag.DragFragment
import com.adapter.demo.multi_type_multi_bean.MultiTypeMBFragment
import com.adapter.demo.multi_type_single_bean.MultiTypeSBFragment
import com.adapter.demo.payloads.PayloadsFragment
import com.adapter.demo.select_mix.MixSelectFragment
import com.adapter.demo.select_multi.MultiSelectFragment
import com.adapter.demo.select_single.SingleSelectFragment
import com.adapter.demo.simple.SimpleFragment
import com.adapter.demo.state.StateFragment


class MainActivity : AppCompatActivity() {
    private var currentPageFlag: Int = -1
    private val tbTitleBar: ActionBar by lazy { supportActionBar!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFragment(R.id.simpleDemo)
    }

    private fun showFragment(id: Int) {
        if (currentPageFlag == id) {
            return
        }

        val fragment = when (id) {
            R.id.simpleDemo -> {
                //以下这句设置无效
                //tbTitleBar.title= "最基础的一个 demo"
                title = "最基础的一个 demo"
                tbTitleBar.subtitle = "设置了点击和长按事件,可以添加/移除 Item"
                SimpleFragment()
            }
            R.id.payloadsDemo -> {
                title = "Item 局部刷新 demo"
                tbTitleBar.subtitle =
                    "RecyclerView.Adapter.notifyItemChanged(int position, @Nullable Object payload)"
                PayloadsFragment()
            }
            R.id.singleSelectDemo -> {
                title = "单选 demo"
                tbTitleBar.subtitle = ""
                SingleSelectFragment()
            }
            R.id.multiSelectDemo -> {
                title = "多选 demo"
                tbTitleBar.subtitle = ""
                MultiSelectFragment()
            }
            R.id.mixSelectDemo -> {
                title = "不可选/单选/多选 切换 demo"
                tbTitleBar.subtitle = ""
                MixSelectFragment()
            }
            R.id.multiTypeSBDemo -> {
                title = "同种数据类型多 ViewType demo"
                tbTitleBar.subtitle = ""
                MultiTypeSBFragment()
            }
            R.id.multiTypeMBDemo -> {
                title = "多数据类型多 ViewType demo"
                tbTitleBar.subtitle = ""
                MultiTypeMBFragment()
            }
            R.id.complexDemo -> {
                title = "复合 demo"
                tbTitleBar.subtitle = ""
                ComplexFragment()
            }
            R.id.diffDemo -> {
                title = "DiffUtil 优化数据更新 demo"
                tbTitleBar.subtitle = ""
                DiffFragment()
            }
            R.id.dragDemo -> {
                title = "Item 拖拽 demo"
                tbTitleBar.subtitle = ""
                DragFragment()
            }
            R.id.stateDemo -> {
                title = "状态页 demo"
                tbTitleBar.subtitle = ""
                StateFragment()
            }
            else -> return
        }

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .commit()

        currentPageFlag = id
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        showFragment(item.itemId)
        return true
    }
}
