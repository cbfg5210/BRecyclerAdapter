package com.adapter.demo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.adapter.demo.complex.ComplexFragment
import com.adapter.demo.multi_type_multi_bean.MultiTypeMBFragment
import com.adapter.demo.multi_type_single_bean.MultiTypeSBFragment
import com.adapter.demo.payloads.PayloadsFragment
import com.adapter.demo.select_mix.MixSelectFragment
import com.adapter.demo.select_multi.MultiSelectFragment
import com.adapter.demo.select_single.SingleSelectFragment
import com.adapter.demo.simple.SimpleFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val FLAG_DEMO_SIMPLE = 1
        private const val FLAG_DEMO_PAYLOADS = 2
        private const val FLAG_DEMO_SELECT_SINGLE = 3
        private const val FLAG_DEMO_SELECT_MULTI = 4
        private const val FLAG_DEMO_SELECT_MIX = 5
        private const val FLAG_DEMO_MULTI_TYPE_SB = 6
        private const val FLAG_DEMO_MULTI_TYPE_MB = 7
        private const val FLAG_DEMO_COMPLEX = 8
    }

    private var currentPageFlag: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(tbTitleBar)

        showFragment(FLAG_DEMO_SIMPLE)
    }

    private fun showFragment(flag: Int) {
        if (currentPageFlag == flag) {
            return
        }

        val fragment = when (flag) {
            FLAG_DEMO_SIMPLE -> {
                //以下这句设置无效
                //tbTitleBar.title= "最基础的一个 demo"
                title = "最基础的一个 demo"
                tbTitleBar.subtitle = "设置了点击和长按事件,可以添加/移除 Item"
                SimpleFragment()
            }
            FLAG_DEMO_PAYLOADS -> {
                title = "Item 局部刷新 demo"
                tbTitleBar.subtitle = "RecyclerView.Adapter.notifyItemChanged(int position, @Nullable Object payload)"
                PayloadsFragment()
            }
            FLAG_DEMO_SELECT_SINGLE -> {
                title = "单选 demo"
                tbTitleBar.subtitle = ""
                SingleSelectFragment()
            }
            FLAG_DEMO_SELECT_MULTI -> {
                title = "多选 demo"
                tbTitleBar.subtitle = ""
                MultiSelectFragment()
            }
            FLAG_DEMO_SELECT_MIX -> {
                title = "不可选/单选/多选 切换 demo"
                tbTitleBar.subtitle = ""
                MixSelectFragment()
            }
            FLAG_DEMO_MULTI_TYPE_SB -> {
                title = "同种数据类型多 ViewType demo"
                tbTitleBar.subtitle = ""
                MultiTypeSBFragment()
            }
            FLAG_DEMO_MULTI_TYPE_MB -> {
                title = "多数据类型多 ViewType demo"
                tbTitleBar.subtitle = ""
                MultiTypeMBFragment()
            }
            FLAG_DEMO_COMPLEX -> {
                title = "复合 demo"
                tbTitleBar.subtitle = ""
                ComplexFragment()
            }
            else -> return
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.vbContainer, fragment)
                .commit()

        currentPageFlag = flag
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.simpleDemo -> showFragment(FLAG_DEMO_SIMPLE)
            R.id.payloadsDemo -> showFragment(FLAG_DEMO_PAYLOADS)
            R.id.singleSelectDemo -> showFragment(FLAG_DEMO_SELECT_SINGLE)
            R.id.multiSelectDemo -> showFragment(FLAG_DEMO_SELECT_MULTI)
            R.id.mixSelectDemo -> showFragment(FLAG_DEMO_SELECT_MIX)
            R.id.multiTypeSBDemo -> showFragment(FLAG_DEMO_MULTI_TYPE_SB)
            R.id.multiTypeMBDemo -> showFragment(FLAG_DEMO_MULTI_TYPE_MB)
            R.id.complexDemo -> showFragment(FLAG_DEMO_COMPLEX)
        }
        return true
    }
}
