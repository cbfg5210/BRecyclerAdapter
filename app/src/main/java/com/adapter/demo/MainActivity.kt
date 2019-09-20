package com.adapter.demo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.adapter.demo.multi_type_single_bean.MultiTypeSBFragment
import com.adapter.demo.payloads.PayloadsFragment
import com.adapter.demo.select_mix.MixSelectFragment
import com.adapter.demo.select_multi.MultiSelectFragment
import com.adapter.demo.select_single.SingleSelectFragment
import com.adapter.demo.simple.SimpleFragment

class MainActivity : AppCompatActivity() {
    companion object {
        private const val FLAG_DEMO_SIMPLE = 1
        private const val FLAG_DEMO_PAYLOADS = 2
        private const val FLAG_DEMO_SELECT_SINGLE = 3
        private const val FLAG_DEMO_SELECT_MULTI = 4
        private const val FLAG_DEMO_SELECT_MIX = 5
        private const val FLAG_DEMO_MULTI_TYPE_SB = 6
        //private const val FLAG_DEMO_SELECT_MIX = 5
    }

    private var currentPageFlag: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showFragment(FLAG_DEMO_SIMPLE)
    }

    private fun showFragment(flag: Int) {
        if (currentPageFlag == flag) {
            return
        }

        val fragment = when (flag) {
            FLAG_DEMO_SIMPLE -> SimpleFragment()
            FLAG_DEMO_PAYLOADS -> PayloadsFragment()
            FLAG_DEMO_SELECT_SINGLE -> SingleSelectFragment()
            FLAG_DEMO_SELECT_MULTI -> MultiSelectFragment()
            FLAG_DEMO_SELECT_MIX -> MixSelectFragment()
            FLAG_DEMO_MULTI_TYPE_SB -> MultiTypeSBFragment()
            else -> return
        }

        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
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
        }
        return true
    }
}
