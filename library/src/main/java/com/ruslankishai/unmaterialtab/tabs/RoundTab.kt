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
class RoundTab(context: Context) : View(context) {

    //<editor-fold desc="View variables">
    private val INNER_VERTICAL_PADDING = DimensUtils.dpToPx(context, 12f)
    private val INNER_HORIZONTAL_PADDING = DimensUtils.dpToPx(context, 16f) * 2

    private val OUTER_HORIZONTAL_EDGE_PADDING = DimensUtils.dpToPx(context, 16f)
    private val OUTER_HORIZONTAL_PADDING = DimensUtils.dpToPx(context, 6f)

    private val ICON_SIZE = DimensUtils.dpToPx(context, 24)
    private val ICON_HORIZONTAL_PADDING = DimensUtils.dpToPx(context, 8)
    private val ICON_HORIZONTAL_EDGE_PADDING = DimensUtils.dpToPx(context, 16)

    private var tab: RectF? = null
    private var tabPaint: Paint? = null
    private var tabStrokePaint: Paint? = null

    private var textBounds: Rect? = null
    private var textPaint: Paint? = null

    private var tabBackgroundColor: Int = 0
    private var tabStrokeColor: Int = 0
    private var tabTextColor: Int = 0
    private var tabIconColor: Int = 0xffffff

    private var isFirst = false
    private var isLast = false

    private var parentHeight: Int = 0

    var hasIcon = false
    var hasStroke: Boolean = true

    var icon: Drawable? = null

    var tabText: String? = ""
    var cornerRadius: Int = 50
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
                    tab!!.left +
                            ICON_SIZE +
                            ICON_HORIZONTAL_EDGE_PADDING +
                            ICON_HORIZONTAL_PADDING,
                    (parentHeight / 2 + textBounds!!.height() / 2).toFloat(),
                    textPaint!!)
        else
            canvas.drawText(tabText!!,
                    tab!!.left + INNER_HORIZONTAL_PADDING / 2,
                    (parentHeight / 2 + textBounds!!.height() / 2).toFloat(),
                    textPaint!!)

        icon?.setBounds((tab!!.left + ICON_HORIZONTAL_PADDING * 2).toInt(),
                (parentHeight / 2 - INNER_VERTICAL_PADDING).toInt(),
                (tab!!.left + ICON_HORIZONTAL_PADDING * 2 + ICON_SIZE).toInt(),
                (parentHeight / 2 + INNER_VERTICAL_PADDING).toInt())

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
            tab!!.left = OUTER_HORIZONTAL_PADDING
            tab!!.top = parentHeight / 2 -
                    textBounds!!.height() / 2 -
                    INNER_VERTICAL_PADDING
            tab!!.right = textBounds!!.right +
                    INNER_HORIZONTAL_PADDING +
                    ICON_SIZE +
                    ICON_HORIZONTAL_PADDING * 2
            tab!!.bottom = parentHeight / 2 +
                    textBounds!!.height() / 2 +
                    INNER_VERTICAL_PADDING

            if (isFirst) {
                tab!!.left = OUTER_HORIZONTAL_EDGE_PADDING
                tab!!.right = textBounds!!.right +
                        INNER_HORIZONTAL_PADDING +
                        ICON_SIZE +
                        //Looks like I cant figure out where I missed this 4px.
                        //If you'll find it - feel free to send a pull request :)
                        4 +
                        ICON_HORIZONTAL_EDGE_PADDING +
                        ICON_HORIZONTAL_PADDING
            }
        } else {
            tab!!.left = OUTER_HORIZONTAL_PADDING
            tab!!.right = textBounds!!.right +
                    OUTER_HORIZONTAL_PADDING +
                    INNER_HORIZONTAL_PADDING

            tab!!.top = (parentHeight / 2 -
                    textBounds!!.height() / 2).toFloat() - INNER_VERTICAL_PADDING
            tab!!.bottom = (parentHeight / 2 +
                    textBounds!!.height() / 2).toFloat() +
                    INNER_VERTICAL_PADDING

            if (isFirst) {
                tab!!.left = OUTER_HORIZONTAL_EDGE_PADDING
                tab!!.right = textBounds!!.right +
                        OUTER_HORIZONTAL_EDGE_PADDING +
                        INNER_HORIZONTAL_PADDING
                textBounds!!.left = OUTER_HORIZONTAL_EDGE_PADDING.toInt()
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
            setMeasuredDimension((textBounds!!.right +
                    INNER_HORIZONTAL_PADDING +
                    OUTER_HORIZONTAL_EDGE_PADDING +
                    ICON_SIZE +
                    ICON_HORIZONTAL_EDGE_PADDING).toInt(),
                    heightMeasureSpec)
        } else if (hasIcon && isLast) {
            setMeasuredDimension((textBounds!!.right +
                    INNER_HORIZONTAL_PADDING +
                    OUTER_HORIZONTAL_EDGE_PADDING +
                    OUTER_HORIZONTAL_PADDING +
                    ICON_HORIZONTAL_EDGE_PADDING +
                    ICON_SIZE).toInt(),
                    heightMeasureSpec)
        } else if (hasIcon) {
            setMeasuredDimension((textBounds!!.right +
                    INNER_HORIZONTAL_PADDING +
                    OUTER_HORIZONTAL_PADDING +
                    ICON_SIZE +
                    ICON_HORIZONTAL_PADDING * 2).toInt(),
                    heightMeasureSpec)
        } else if (isFirst) {
            setMeasuredDimension((textBounds!!.right +
                    OUTER_HORIZONTAL_EDGE_PADDING +
                    INNER_HORIZONTAL_PADDING +
                    OUTER_HORIZONTAL_PADDING).toInt(),
                    heightMeasureSpec)
        } else if (isLast) {
            setMeasuredDimension((textBounds!!.right +
                    OUTER_HORIZONTAL_EDGE_PADDING +
                    INNER_HORIZONTAL_PADDING +
                    OUTER_HORIZONTAL_PADDING).toInt(),
                    heightMeasureSpec)
        } else {
            setMeasuredDimension((textBounds!!.right +
                    INNER_HORIZONTAL_PADDING +
                    OUTER_HORIZONTAL_PADDING * 2).toInt(),
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