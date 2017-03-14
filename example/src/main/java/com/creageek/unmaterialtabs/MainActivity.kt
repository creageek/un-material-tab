package com.creageek.unmaterialtabs

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ruslankishai.unmaterialtab.tabs.RoundTab

import com.ruslankishai.unmaterialtab.tabs.RoundTabLayout

class MainActivity : AppCompatActivity() {

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ViewAdapter(supportFragmentManager)

        var viewPager = findViewById(R.id.view_pager_circle) as ViewPager
        viewPager.adapter = adapter

        var tabView = findViewById(R.id.circle_tab_view) as RoundTabLayout
        var tab = RoundTab(applicationContext, 50, null, false)
        tab.initTab("Programmatically".toUpperCase())
        tabView.addTab(tab)
        tab = RoundTab(applicationContext, 50, null, true)
        tab.initTab("Really!")
        tabView.addTab(tab)

        viewPager = findViewById(R.id.view_pager_custom) as ViewPager
        viewPager.adapter = adapter
        tabView = findViewById(R.id.custom_tab_view) as RoundTabLayout
        tabView.setupWithViewPager(viewPager)

        viewPager = findViewById(R.id.view_pager_rounded) as ViewPager
        viewPager.adapter = adapter
        tabView = findViewById(R.id.rounded_tab_view) as RoundTabLayout
        tabView.setupWithViewPager(viewPager)

        viewPager = findViewById(R.id.view_pager_rectangle) as ViewPager
        viewPager.adapter = adapter
        tabView = findViewById(R.id.rectangle_tab_view) as RoundTabLayout
        tabView.setupWithViewPager(viewPager)

        viewPager = findViewById(R.id.view_pager_circle_stroke) as ViewPager
        viewPager.adapter = adapter
        tabView = findViewById(R.id.circle_tab_view_stroke) as RoundTabLayout
        tabView.setupWithViewPager(viewPager)
        tabView.getTab(0).icon = resources.getDrawable(R.drawable.ic_public)
        tabView.getTab(0).hasIcon = true
        tabView.getTab(0).hasStroke = true
        tabView.setOnTabSelectedListener(object : RoundTabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: RoundTab, position: Int) {
                Toast.makeText(applicationContext, "onTabSelected ${tab.tabText} on position $position", Toast.LENGTH_SHORT).show()
            }

            override fun onTabReselected(tab: RoundTab, position: Int) {
                Toast.makeText(applicationContext, "onTabReselected ${tab.tabText} on position $position", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private inner class ViewAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "All"
                1 -> return "Articles"
                2 -> return "Interviews"
                3 -> return "News"
                4 -> return "Events"
                5 -> return "Links"
                else -> return null
            }
        }

        override fun getItem(position: Int): Fragment {
            val fragment = PageFragment()
            val bundle = Bundle()
            bundle.putCharSequence("title", getPageTitle(position))
            fragment.arguments = bundle
            return fragment
        }

        override fun getCount() = 6
    }
}

