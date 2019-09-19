package com.adapter.demo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.adapter.demo.payloads.PayloadsFragment
import com.adapter.demo.simple.SimpleFragment

class MainActivity : AppCompatActivity() {
    companion object {
        private const val FLAG_DEMO_SIMPLE = 1
        private const val FLAG_DEMO_PAYLOADS = 2
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
            R.id.menu_demo_simple -> showFragment(FLAG_DEMO_SIMPLE)
            R.id.menu_demo_payloads -> showFragment(FLAG_DEMO_PAYLOADS)
            R.id.menu_demo3 -> {
            }
        }
        return true
    }
}
