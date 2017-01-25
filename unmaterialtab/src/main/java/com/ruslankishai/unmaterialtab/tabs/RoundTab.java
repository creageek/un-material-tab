package com.ruslankishai.unmaterialtab.tabs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ruslankishai.unmaterialtab.DimensUtils;

/**
 * Created by Ruslan Kishai aka creageek on 1/1/2017.
 */

public class RoundTab extends View {

    private String tabText;
    private int parentHeight;

    private Paint tabPaint, tabStrokePaint, textPaint;
    private Rect textBounds;
    private RectF tab;

    private boolean isFirst = false;
    private boolean isLast = false;

    private int tabBackgroundColor;
    private int tabStrokeColor;
    private int tabTextColor;

    public RoundTab(Context context) {
        super(context);
        initView();
    }

    public RoundTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setTabStrokeColor(int tabStrokeColor) {
        this.tabStrokeColor = tabStrokeColor;
    }

    public void setTabTextColor(int tabTextColor) {
        this.tabTextColor = tabTextColor;
    }

    private void initView() {
        textBounds = new Rect();
        textPaint = new Paint();
        tabPaint = new Paint();
        tabStrokePaint = new Paint();
        tab = new RectF();
    }

    public RoundTab initTab(String tabText) {
        this.tabText = tabText;

        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DimensUtils.spToPx(getContext(), 13));
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setFakeBoldText(true);
        textPaint.setColor(tabTextColor);
        textPaint.getTextBounds(tabText, 0, tabText.length(), textBounds);

        tabPaint.setAntiAlias(true);
        tabPaint.setStyle(Paint.Style.FILL);
        tabPaint.setStrokeWidth(DimensUtils.dpToPx(getContext(), 1.5f));
        tabPaint.setColor(tabBackgroundColor);

        tabStrokePaint.setAntiAlias(true);
        tabStrokePaint.setStyle(Paint.Style.STROKE);
        tabStrokePaint.setStrokeWidth(DimensUtils.dpToPx(getContext(), 1.5f));
        tabStrokePaint.setColor(tabStrokeColor);

        return this;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        tab.left = DimensUtils.dpToPx(getContext(), 6);
        tab.top = parentHeight / 2 - textBounds.height() / 2 - DimensUtils.dpToPx(getContext(), 12);
        tab.right = textBounds.right + DimensUtils.dpToPx(getContext(), 6) + DimensUtils.dpToPx(getContext(), 16) * 2;
        tab.bottom = parentHeight / 2 + textBounds.height() / 2 + DimensUtils.dpToPx(getContext(), 12);

        if (isFirst) {
            tab.left = DimensUtils.dpToPx(getContext(), 16);
            tab.right = textBounds.right + DimensUtils.dpToPx(getContext(), 16) + DimensUtils.dpToPx(getContext(), 16) * 2;
            textBounds.left = DimensUtils.dpToPx(getContext(), 16);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        tabPaint.setColor(tabBackgroundColor);
        tabStrokePaint.setColor(tabStrokeColor);
        textPaint.setColor(tabTextColor);

        canvas.drawRoundRect(tab, 100, 100, tabPaint);
        canvas.drawRoundRect(tab, 100, 100, tabStrokePaint);
        canvas.drawText(tabText, tab.left + DimensUtils.dpToPx(getContext(), 16), parentHeight / 2 + textBounds.height() / 2, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isFirst)
            setMeasuredDimension(textBounds.right + DimensUtils.dpToPx(getContext(), 16) * 3 +
                    DimensUtils.dpToPx(getContext(), 6), heightMeasureSpec);
        else if (isLast)
            setMeasuredDimension(textBounds.right + DimensUtils.dpToPx(getContext(), 16) * 3 +
                    DimensUtils.dpToPx(getContext(), 6), heightMeasureSpec);
        else
            setMeasuredDimension(textBounds.right + DimensUtils.dpToPx(getContext(), 16) * 2 +
                    DimensUtils.dpToPx(getContext(), 12), heightMeasureSpec);
    }

    public void setTabBackgroundColor(int tabBackgroundColor) {
        this.tabBackgroundColor = tabBackgroundColor;
    }

    public void setParentHeight(int parentHeight) {
        this.parentHeight = parentHeight;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}