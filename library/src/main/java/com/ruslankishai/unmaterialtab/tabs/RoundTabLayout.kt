package com.ruslankishai.unmaterialtab.tabs

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.ruslankishai.unmaterialtab.DimensUtils
import com.ruslankishai.unmaterialtab.R
import java.util.*

/**
 * Custom tab layout which can be used as a material
 * TabLayout alternative and consists basic functionality
 * which Google's TabLayout has.
 *
 * Created by Ruslan Kishai on 12/31/2016.
 */
class RoundTabLayout : HorizontalScrollView, ViewPager.OnPageChangeListener {

    //<editor-fold desc="Layout variables">
    private var tabs: MutableList<RoundTab> = ArrayList()
    private var layoutParams: LinearLayout.LayoutParams? = null
    private var tabStrip: LinearLayout? = null
    private var viewPager: ViewPager? = null

    private var tabBackColor: Int = 0
    private var tabStrokeColor: Int = 0
    private var iconRes: Drawable? = null

    private var cornerRadius: Int = 50
    private var clickedPosition = 0

    private var hasStroke = true
    //</editor-fold>

    //<editor-fold desc="Layout constructors">
    /**
     * Class constructor.
     * Hides parent view scrollbar and adds tabs to the strip.
     * @param context passing as parameter int HorizontalScrollView class constructor.
     */
    //FIXME throws NPE with kotlin-style default constructor
    constructor(context: Context) : super(context) {
        isHorizontalScrollBarEnabled = false
        addViews(context)
    }

    /**
     * Class constructor. Initializing layout and adds tabs to the strip.
     * Gets RoundTabLayout background, tabs corners radius and selector
     * icon (if exists). Hides parent view scrollbar.
     * @param context passing as parameter int HorizontalScrollView class constructor.
     * @param attrs xml layout attributes.
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RoundTabLayout)
        tabStrokeColor = typedArray.getColor(R.styleable.RoundTabLayout_accent, 0xff464646.toInt())
        cornerRadius = typedArray.getInt(R.styleable.RoundTabLayout_cornerRadius, 50)
        hasStroke = typedArray.getBoolean(R.styleable.RoundTabLayout_withStroke, true)
        iconRes = typedArray.getDrawable(R.styleable.RoundTabLayout_src)
        typedArray.recycle()

        try {
            val tabViewColor = background as ColorDrawable
            tabBackColor = tabViewColor.color
        } catch (exception: ClassCastException) {
            Log.d(TAG, "Tab layout background color Class Cast Exception")
            tabBackColor = 0x00ffffff
        }

        isHorizontalScrollBarEnabled = false
        addViews(context)
    }
    //</editor-fold>

    //<editor-fold desc="Layout composing">
    /**
     * Adds LinearLayout to the parent view & configures LayoutParams.
     * @param context using to initialize LinearLayout.
     */
    private fun addViews(context: Context) {
        tabStrip = LinearLayout(context)
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParams?.gravity = Gravity.CENTER
        tabStrip?.layoutParams = layoutParams
        tabStrip?.orientation = LinearLayout.HORIZONTAL
        this.addView(tabStrip, layoutParams)
    }

    /**
     * Adds tabs to child LinearLayout and forming tabs strip.
     * @param viewPager gets ViewPager with info about title and tabs count
     * to initialize RoundTabLayout with tabs.
     */
    @Suppress("DEPRECATION")
    fun setupWithViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        viewPager.setOnPageChangeListener(this)

        for (i in 0..viewPager.adapter.count - 1) {
            val tabText = viewPager.adapter.getPageTitle(i) as String
            val tab = RoundTab(context, cornerRadius, iconRes, hasStroke)
                    .initTab(tabText.toUpperCase())

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

    /**
     * Adds tab to child LinearLayout and forming tabs strip.
     * @param tab user tab with custom text and colors.
     */
    private fun addTab(tab: RoundTab) {
        tabs.add(tab)
        tabStrip?.addView(tab, layoutParams)
    }
    //</editor-fold>

    //<editor-fold desc="Layout drawing methods">
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        for (i in tabs.indices) {
            val tab = tabs[i]
            tab.setParentHeight(height)
            val index = i

            tab.setOnClickListener {
                if (index != clickedPosition && clickedPosition != -1) {
                    animateFade(tabs[clickedPosition], ANIMATION_FADE_OUT)
                    tabs[clickedPosition].invalidate()
                    clickedPosition = index
                    animateFade(tab, ANIMATION_FADE_IN)
                    tab.invalidate()
                    viewPager?.currentItem = index
                }
            }

            if (i == 0) {
                tab.setFirst(true)
                tab.requestLayout()
            }

            if (i == tabs.size - 1) {
                tab.setLast(true)
                tab.requestLayout()
            }

            tab.invalidate()
        }
    }
    //</editor-fold>

    //<editor-fold desc="Layout listeners">
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

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}
    //</editor-fold>

    //<editor-fold desc="Layout animation & scrolling">
    private fun animateFade(tab: RoundTab, animType: Int) {
        val tabBackgroundFrom: Int
        val tabBackgroundTo: Int
        val contentColorFrom: Int
        val contentColorTo: Int

        when (animType) {
            ANIMATION_FADE_IN -> {
                tabBackgroundFrom = 0x00ffffff
                tabBackgroundTo = tabStrokeColor
                contentColorFrom = tabStrokeColor
                contentColorTo = tabBackColor
            }
            ANIMATION_FADE_OUT -> {
                tabBackgroundFrom = tabStrokeColor
                tabBackgroundTo = 0x00ffffff
                contentColorFrom = tabBackColor
                contentColorTo = tabStrokeColor
            }
            else -> {
                tabBackgroundFrom = 0x00ffffff
                tabBackgroundTo = tabStrokeColor
                contentColorFrom = tabStrokeColor
                contentColorTo = tabBackColor
            }
        }

        val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), tabBackgroundFrom, tabBackgroundTo)
        colorAnimator.duration = 250
        colorAnimator.addUpdateListener { valueAnimator ->
            tab.setTabBackgroundColor(valueAnimator.animatedValue as Int)
        }
        colorAnimator.start()

        val textAnimator = ValueAnimator.ofObject(ArgbEvaluator(), contentColorFrom, contentColorTo)
        textAnimator.duration = 200
        textAnimator.addUpdateListener { valueAnimator ->
            tab.setTabTextColor(valueAnimator.animatedValue as Int)
        }
        textAnimator.start()

        val iconAnimator = ValueAnimator.ofObject(ArgbEvaluator(), contentColorFrom, contentColorTo)
        iconAnimator.duration = 350
        iconAnimator.addUpdateListener { valueAnimator ->
            tab.setTabIconTint(valueAnimator.animatedValue as Int)
        }
        iconAnimator.start()
    }

    private fun scrollTabView(position: Int) {
        var previousPosition = 0
        var nextPosition = 0

        if (position == tabs.size - 1)
            smoothScrollTo(right, 0)

        if (position - 1 >= 0)
            for (i in 0..position - 1 - 1)
                previousPosition += tabs[i].width

        if (position + 1 < tabs.size)
            for (i in 0..position + 1 - 1)
                nextPosition += tabs[i].width

        val currentPosition = (0..position).sumBy { tabs[it].width }

        if (previousPosition < scrollX)
            smoothScrollTo(previousPosition, 0)
        else if (currentPosition > right)
            smoothScrollTo(previousPosition, 0)
        else if (nextPosition > right)
            smoothScrollTo(currentPosition, 0)
    }
    //</editor-fold>

    //<editor-fold desc="Layout measurements & etc">
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, DimensUtils.dpToPx(context, 48))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        //Fix tab selector issue when resuming layout
        invalidate()
    }
    //</editor-fold>

    fun getTab(position: Int) = tabs[position]

    companion object {
        val ANIMATION_FADE_IN = 1
        val ANIMATION_FADE_OUT = 0

        val TAG: String = RoundTabLayout::class.java.simpleName
    }
}