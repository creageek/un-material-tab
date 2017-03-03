package com.ruslankishai.unmaterialtab.tabs

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View
import com.ruslankishai.unmaterialtab.DimensUtils


/**
 * Custom tab view that extends from simple View.
 * Exists as a part of RoundTabLayout and contains only basic
 * measurements, tab shape, text and icon drawing.
 *
 * Created by Ruslan Kishai on 1/1/2017.
 */
internal class RoundTab(context: Context) : View(context) {

    //<editor-fold desc="View variables">
    private var tab: RectF? = null
        private set
    private var tabPaint: Paint? = null
        private set
    private var tabStrokePaint: Paint? = null
        private set

    private var textBounds: Rect? = null
        private set
    private var textPaint: Paint? = null
        private set

    private var tabBackgroundColor: Int = 0
    private var tabStrokeColor: Int = 0
    private var tabTextColor: Int = 0
    private var tabIconColor: Int = 0xffffff

    private var isFirst = false
        private set
    private var isLast = false
        private set
    private var hasIcon = false
    private var hasStroke: Boolean = true

    private var parentHeight: Int = 0
        private set
    private var cornerRadius: Int = 50

    private var icon: Drawable? = null
    private var tabText: String? = ""
    //</editor-fold>

    //<editor-fold desc="View constructors">

    /**
     * Class constructor. Initializing view and takes few parameters.
     * @param context passing as parameter int View class constructor.
     * @param cornerRadius tab corners radius value (50 by default).
     * @param iconRes drawable resource that is using as an alternative
     * for a selected tab indicator.
     */
    constructor(context: Context, cornerRadius: Int, iconRes: Drawable?, isStrokeEnabled: Boolean)
            : this(context) {

        this.cornerRadius = cornerRadius
        this.hasStroke = isStrokeEnabled

        if (iconRes != null) {
            this.icon = iconRes
            hasIcon = true
        } else hasIcon = false
    }

    init {
        initView()
    }
    //</editor-fold>

    //<editor-fold desc="View initializing">
    /**
     * Initializing paint tools
     */
    private fun initView() {
        tab = RectF()
        tabStrokePaint = Paint()
        textBounds = Rect()
        textPaint = Paint()
        tabPaint = Paint()

    }

    /**
     * Initializing tab with data.
     * @param tabText tab title
     */
    internal fun initTab(tabText: String): RoundTab {
        this.tabText = tabText

        textPaint!!.textSize = DimensUtils.spToPx(context, 13).toFloat()
        textPaint!!.style = Paint.Style.STROKE
        textPaint!!.color = tabTextColor
        textPaint!!.isAntiAlias = true
        textPaint!!.isFakeBoldText = true
        textPaint!!.getTextBounds(tabText, 0, tabText.length, textBounds)


        tabStrokePaint!!.style = Paint.Style.STROKE
        tabStrokePaint!!.color = tabStrokeColor
        tabStrokePaint!!.strokeWidth = DimensUtils.dpToPx(context, 1.5f)
        tabStrokePaint!!.isAntiAlias = true

        tabPaint!!.style = Paint.Style.FILL
        tabPaint!!.color = tabBackgroundColor
        tabPaint!!.strokeWidth = DimensUtils.dpToPx(context, 1.5f)
        tabPaint!!.isAntiAlias = true

        icon?.setColorFilter(tabTextColor, PorterDuff.Mode.SRC_ATOP)

        return this
    }
    //</editor-fold>

    //<editor-fold desc="View drawing methods">
    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        tabPaint!!.color = tabBackgroundColor
        tabStrokePaint!!.color = tabStrokeColor
        textPaint!!.color = tabTextColor

        canvas.drawRoundRect(tab!!, cornerRadius.toFloat(), cornerRadius.toFloat(), tabPaint!!)

        if (hasStroke)
            canvas.drawRoundRect(tab!!, cornerRadius.toFloat(), cornerRadius.toFloat(), tabStrokePaint!!)

        if (hasIcon)
            canvas.drawText(tabText!!,
                    DimensUtils.dpToPx(context, 30) + tab!!.left + DimensUtils.dpToPx(context, 16),
                    (parentHeight / 2 + textBounds!!.height() / 2).toFloat(),
                    textPaint!!)
        else
            canvas.drawText(tabText!!,
                    tab!!.left + DimensUtils.dpToPx(context, 16),
                    (parentHeight / 2 + textBounds!!.height() / 2).toFloat(),
                    textPaint!!)

        icon?.setBounds((tab!!.left + DimensUtils.dpToPx(context, 16)).toInt(),
                (parentHeight / 2 - DimensUtils.dpToPx(context, 12)),
                (tab!!.left + DimensUtils.dpToPx(context, 40)).toInt(),
                (parentHeight / 2 + DimensUtils.dpToPx(context, 12)))

        if (hasIcon) {
            icon?.setColorFilter(tabTextColor, PorterDuff.Mode.SRC_ATOP)
            icon?.draw(canvas)
        }
    }
    //</editor-fold>

    //<editor-fold desc="View measuring">
    /**
     * Simple onLayout method with with sizes of tab bounds
     * depending on position and existing of an icon.
     * All values below are just pure magic and was calculated
     * by unknown power.
     * p.s. values are not merged for better manipulation in future.
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (hasIcon) {
            tab!!.left = DimensUtils.dpToPx(context, 6).toFloat()
            tab!!.top = (parentHeight / 2 -
                    DimensUtils.dpToPx(context, 6) -
                    DimensUtils.dpToPx(context, 12)).toFloat()
            tab!!.right = (textBounds!!.right +
                    DimensUtils.dpToPx(context, 27) +
                    DimensUtils.dpToPx(context, 12) +
                    DimensUtils.dpToPx(context, 16) * 2).toFloat()
            tab!!.bottom = (parentHeight / 2 +
                    DimensUtils.dpToPx(context, 6) +
                    DimensUtils.dpToPx(context, 12)).toFloat()

            if (isFirst) {
                tab!!.left = DimensUtils.dpToPx(context, 16).toFloat()
                tab!!.right = (textBounds!!.right +
                        DimensUtils.dpToPx(context, 32) +
                        DimensUtils.dpToPx(context, 16) +
                        DimensUtils.dpToPx(context, 16) * 2).toFloat()
                textBounds!!.left = DimensUtils.dpToPx(context, 16)
            }
        } else {
            tab!!.left = DimensUtils.dpToPx(context, 6).toFloat()
            tab!!.top = (parentHeight / 2 -
                    textBounds!!.height() / 2 -
                    DimensUtils.dpToPx(context, 12)).toFloat()
            tab!!.right = (textBounds!!.right +
                    DimensUtils.dpToPx(context, 6) +
                    DimensUtils.dpToPx(context, 16) * 2).toFloat()
            tab!!.bottom = (parentHeight / 2 +
                    textBounds!!.height() / 2 +
                    DimensUtils.dpToPx(context, 12)).toFloat()

            if (isFirst) {
                tab!!.left = DimensUtils.dpToPx(context, 16).toFloat()
                tab!!.right = (textBounds!!.right +
                        DimensUtils.dpToPx(context, 16) +
                        DimensUtils.dpToPx(context, 16) * 2).toFloat()
                textBounds!!.left = DimensUtils.dpToPx(context, 16)
            }
        }
    }

    /**
     * Simple onMeasure method with with sizes of custom view
     * depending on position and existing of an icon.
     * All values below are just pure magic and was calculated
     * by unknown power.
     * p.s. values are not merged for better manipulation in future.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (hasIcon && isFirst) {
            //Text size
            setMeasuredDimension(textBounds!!.right +
                    //Unknown value
                    DimensUtils.dpToPx(context, 16) * 3 +
                    //For tab left margin
                    DimensUtils.dpToPx(context, 8) +
                    //For an icon with a little bit of margin
                    DimensUtils.dpToPx(context, 30),
                    heightMeasureSpec)
        } else if (hasIcon && isLast) {
            //Text size
            setMeasuredDimension(textBounds!!.right +
                    //Unknown value
                    DimensUtils.dpToPx(context, 16) * 3 +
                    //Right margin and another magic
                    DimensUtils.dpToPx(context, 12) +
                    //For an icon with a little bit of margin
                    DimensUtils.dpToPx(context, 30),
                    heightMeasureSpec)
        } else if (hasIcon) {
            //Text size
            setMeasuredDimension(textBounds!!.right +
                    //Unknown values
                    DimensUtils.dpToPx(context, 16) * 2 +
                    DimensUtils.dpToPx(context, 16) +
                    //For an icon with a little bit of margin
                    DimensUtils.dpToPx(context, 30),
                    heightMeasureSpec)
        } else if (isFirst) {
            //Text size
            setMeasuredDimension(textBounds!!.right +
                    //Unknown value
                    DimensUtils.dpToPx(context, 16) * 3 +
                    //For tab left margin
                    DimensUtils.dpToPx(context, 6),
                    heightMeasureSpec)
        } else if (isLast) {
            //Text size
            setMeasuredDimension(textBounds!!.right +
                    //Unknown value
                    DimensUtils.dpToPx(context, 16) * 3 +
                    //For tab right margin
                    DimensUtils.dpToPx(context, 6),
                    heightMeasureSpec)
        } else {
            //Text size
            setMeasuredDimension(textBounds!!.right +
                    //Unknown value
                    DimensUtils.dpToPx(context, 16) * 2 +
                    //For inner tab margin
                    DimensUtils.dpToPx(context, 12),
                    heightMeasureSpec)
        }
    }
    //</editor-fold>

    //<editor-fold desc="View properties setters">
    /**
     * Sets a tab background color.
     * Calls by ValueAnimator in AnimatorUpdateListener.
     * @param tabBackgroundColor int Color resource id.
     */
    internal fun setTabBackgroundColor(tabBackgroundColor: Int) {
        this.tabBackgroundColor = tabBackgroundColor
    }

    /**
     * Sets a tab stroke color.
     * Calls by ValueAnimator in AnimatorUpdateListener.
     * @param tabStrokeColor int Color resource id.
     */
    internal fun setTabStrokeColor(tabStrokeColor: Int) {
        this.tabStrokeColor = tabStrokeColor
    }

    /**
     * Sets a tab text color.
     * Calls by ValueAnimator in AnimatorUpdateListener.
     * @param tabTextColor int Color resource id.
     */
    internal fun setTabTextColor(tabTextColor: Int) {
        this.tabTextColor = tabTextColor
    }

    /**
     * Sets a tab icon (maybe new type of indicator) color.
     * Calls by ValueAnimator in AnimatorUpdateListener.
     * @param tabIconColor int Color resource id.
     */
    internal fun setTabIconTint(tabIconColor: Int) {
        this.tabIconColor = tabIconColor
    }

    /**
     * Sets a tab parent view height.
     * @param parentHeight should take RoundTabLayout height by default.
     */
    internal fun setParentHeight(parentHeight: Int) {
        this.parentHeight = parentHeight
    }

    /**
     * Sets a boolean value which means that it is a first tab in a view.
     * Using to increase left side margin for specific tab.
     * @param first is it first tab in a view or not.
     */
    internal fun setFirst(first: Boolean) {
        isFirst = first
    }

    /**
     * Sets a boolean value which means that it is a last tab in a view.
     * Using to increase right side margin for specific tab.
     * @param last is it last tab in a view or not.
     */
    internal fun setLast(last: Boolean) {
        isLast = last
    }
    //</editor-fold>
}