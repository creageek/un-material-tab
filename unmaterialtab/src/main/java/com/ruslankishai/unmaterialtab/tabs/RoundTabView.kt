package com.ruslankishai.unmaterialtab.tabs

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout

import com.ruslankishai.unmaterialtab.DimensUtils
import com.ruslankishai.unmaterialtab.R

import java.util.ArrayList

/**
* Created by Ruslan Kishai on 12/31/2016.
*/

class RoundTabView : HorizontalScrollView, ViewPager.OnPageChangeListener {
    internal var tabs: MutableList<RoundTab> = ArrayList()
    internal var clickedPosition = 0
    internal var layoutParams: LinearLayout.LayoutParams? = null
    internal var tabStrip: LinearLayout? = null
    internal var viewPager: ViewPager? = null

    internal var tabBackColor: Int = 0
    internal var tabStrokeColor: Int = 0

    constructor(context: Context) : super(context) {
        addViews(context)
        isHorizontalScrollBarEnabled = false
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RoundTabView)
        tabStrokeColor = typedArray.getColor(R.styleable.RoundTabView_accent, 0xff464646.toInt())
        typedArray.recycle()

        try {
            val tabViewColor = background as ColorDrawable
            tabBackColor = tabViewColor.color
        } catch (exception: ClassCastException) {
            Log.d("TAB", "Class Cast Exception")
            tabBackColor = 0x00ffffff
        }

        addViews(context)
        isHorizontalScrollBarEnabled = false
    }

    private fun addViews(context: Context) {
        tabStrip = LinearLayout(context)
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParams?.gravity = Gravity.CENTER
        tabStrip?.layoutParams = layoutParams
        tabStrip?.orientation = LinearLayout.HORIZONTAL
        this.addView(tabStrip, layoutParams)
    }

    fun setupWithViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        viewPager.setOnPageChangeListener(this)
        val adapter = viewPager.adapter

        for (i in 0..adapter.count - 1) {
            val tabText = adapter.getPageTitle(i) as String
            val tab = RoundTab(context).initTab(tabText.toUpperCase())
            if (i == viewPager.currentItem) {
                tab.setTabBackgroundColor(tabStrokeColor)
                tab.setTabTextColor(tabBackColor)
            } else {
                tab.setTabBackgroundColor(tabBackColor)
                tab.setTabTextColor(tabStrokeColor)
            }
            tab.setTabStrokeColor(tabStrokeColor)
            tabs.add(tab)
        }
        for (tab in tabs)
            tabStrip?.addView(tab, layoutParams)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        for (i in tabs.indices) {
            val tab = tabs[i]
            tab.setParentHeight(height)
            val finalI = i
            tab.setOnClickListener {
                if (finalI != clickedPosition && clickedPosition != -1) {
                    animateFade(tabs[clickedPosition], ANIMATION_FADE_OUT)
                    tabs[clickedPosition].invalidate()
                    clickedPosition = finalI
                    animateFade(tab, ANIMATION_FADE_IN)
                    tab.invalidate()
                    viewPager?.currentItem = finalI
                }
            }
            if (i == 0) {
                tab.setFirst(true)
                tab.requestLayout()
            } else if (i == tabs.size - 1) {
                tab.setLast(true)
                tab.requestLayout()
            }
            tab.invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, DimensUtils.dpToPx(context, 48))
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        scrollTabView(position)
        if (position != clickedPosition && clickedPosition != -1) {
            animateFade(tabs[clickedPosition], ANIMATION_FADE_OUT)
            tabs[clickedPosition].invalidate()
            clickedPosition = position
            animateFade(tabs[position], ANIMATION_FADE_IN)
            tabs[position].invalidate()
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}

    private fun animateFade(tab: RoundTab, animType: Int) {
        val colorFrom: Int
        val colorTo: Int
        val textColorFrom: Int
        val textColorTo: Int

        when (animType) {
            ANIMATION_FADE_IN->{
                colorFrom = 0x00ffffff
                colorTo = tabStrokeColor
                textColorFrom = tabStrokeColor
                textColorTo = tabBackColor
            }
            ANIMATION_FADE_OUT -> {
                textColorFrom = tabBackColor
                textColorTo = tabStrokeColor
                colorFrom = tabStrokeColor
                colorTo = 0x00ffffff
            }
            else -> {
                colorFrom = 0x00ffffff
                colorTo = tabStrokeColor
                textColorFrom = tabStrokeColor
                textColorTo = tabBackColor
            }
        }

        val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimator.duration = 250
        colorAnimator.addUpdateListener { valueAnimator -> tab.setTabBackgroundColor(valueAnimator.animatedValue as Int) }
        colorAnimator.start()

        val textAnimator = ValueAnimator.ofObject(ArgbEvaluator(), textColorFrom, textColorTo)
        textAnimator.duration = 250
        textAnimator.addUpdateListener { valueAnimator -> tab.setTabTextColor(valueAnimator.animatedValue as Int) }
        textAnimator.start()
    }

    private fun scrollTabView(position: Int) {
        var previousPosition = 0
        var currentPosition = 0
        var nextPosition = 0

        if (position == tabs.size - 1)
            smoothScrollTo(right, 0)

        if (position - 1 >= 0)
            for (i in 0..position - 1 - 1)
                previousPosition += tabs[i].width

        if (position + 1 < tabs.size)
            for (i in 0..position + 1 - 1)
                nextPosition += tabs[i].width

        for (i in 0..position)
            currentPosition += tabs[i].width

        if (previousPosition < scrollX)
            smoothScrollTo(previousPosition, 0)
        else if (currentPosition > right)
            smoothScrollTo(previousPosition, 0)
        else if (nextPosition > right)
            smoothScrollTo(currentPosition, 0)
    }

    companion object {
        val ANIMATION_FADE_IN = 1
        val ANIMATION_FADE_OUT = 0
    }
}