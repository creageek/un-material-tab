package com.ruslankishai.unmaterialtab.tabs

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

import com.ruslankishai.unmaterialtab.DimensUtils

/**
* Created by Ruslan Kishai on 1/1/2017.
*/

class RoundTab : View {

    private var tabText: String? = null
    private var parentHeight: Int = 0

    private var tabPaint: Paint? = null
    private var tabStrokePaint: Paint? = null
    private var textPaint: Paint? = null
    private var textBounds: Rect? = null
    private var tab: RectF? = null

    private var isFirst = false
    private var isLast = false

    private var tabBackgroundColor: Int = 0
    private var tabStrokeColor: Int = 0
    private var tabTextColor: Int = 0

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    fun setTabStrokeColor(tabStrokeColor: Int) {
        this.tabStrokeColor = tabStrokeColor
    }

    fun setTabTextColor(tabTextColor: Int) {
        this.tabTextColor = tabTextColor
    }

    private fun initView() {
        textBounds = Rect()
        textPaint = Paint()
        tabPaint = Paint()
        tabStrokePaint = Paint()
        tab = RectF()
    }

    fun initTab(tabText: String): RoundTab {
        this.tabText = tabText

        textPaint!!.isAntiAlias = true
        textPaint!!.textSize = DimensUtils.spToPx(context, 13).toFloat()
        textPaint!!.style = Paint.Style.STROKE
        textPaint!!.isFakeBoldText = true
        textPaint!!.color = tabTextColor
        textPaint!!.getTextBounds(tabText, 0, tabText.length, textBounds)

        tabPaint!!.isAntiAlias = true
        tabPaint!!.style = Paint.Style.FILL
        tabPaint!!.strokeWidth = DimensUtils.dpToPx(context, 1.5f)
        tabPaint!!.color = tabBackgroundColor

        tabStrokePaint!!.isAntiAlias = true
        tabStrokePaint!!.style = Paint.Style.STROKE
        tabStrokePaint!!.strokeWidth = DimensUtils.dpToPx(context, 1.5f)
        tabStrokePaint!!.color = tabStrokeColor

        return this
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        tab!!.left = DimensUtils.dpToPx(context, 6).toFloat()
        tab!!.top = (parentHeight / 2 - textBounds!!.height() / 2 - DimensUtils.dpToPx(context, 12)).toFloat()
        tab!!.right = (textBounds!!.right + DimensUtils.dpToPx(context, 6) + DimensUtils.dpToPx(context, 16) * 2).toFloat()
        tab!!.bottom = (parentHeight / 2 + textBounds!!.height() / 2 + DimensUtils.dpToPx(context, 12)).toFloat()

        if (isFirst) {
            tab!!.left = DimensUtils.dpToPx(context, 16).toFloat()
            tab!!.right = (textBounds!!.right + DimensUtils.dpToPx(context, 16) + DimensUtils.dpToPx(context, 16) * 2).toFloat()
            textBounds!!.left = DimensUtils.dpToPx(context, 16)
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        tabPaint!!.color = tabBackgroundColor
        tabStrokePaint!!.color = tabStrokeColor
        textPaint!!.color = tabTextColor

        canvas.drawRoundRect(tab!!, 100f, 100f, tabPaint!!)
        canvas.drawRoundRect(tab!!, 100f, 100f, tabStrokePaint!!)
        canvas.drawText(tabText!!, tab!!.left + DimensUtils.dpToPx(context, 16), (parentHeight / 2 + textBounds!!.height() / 2).toFloat(), textPaint!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (isFirst)
            setMeasuredDimension(textBounds!!.right + DimensUtils.dpToPx(context, 16) * 3 +
                    DimensUtils.dpToPx(context, 6), heightMeasureSpec)
        else if (isLast)
            setMeasuredDimension(textBounds!!.right + DimensUtils.dpToPx(context, 16) * 3 +
                    DimensUtils.dpToPx(context, 6), heightMeasureSpec)
        else
            setMeasuredDimension(textBounds!!.right + DimensUtils.dpToPx(context, 16) * 2 +
                    DimensUtils.dpToPx(context, 12), heightMeasureSpec)
    }

    fun setTabBackgroundColor(tabBackgroundColor: Int) {
        this.tabBackgroundColor = tabBackgroundColor
    }

    fun setParentHeight(parentHeight: Int) {
        this.parentHeight = parentHeight
    }

    fun setFirst(first: Boolean) {
        isFirst = first
    }

    fun setLast(last: Boolean) {
        isLast = last
    }
}